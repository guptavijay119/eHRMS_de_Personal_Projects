package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import com.ehrms.deptenq.constants.DepartmentConstants;
import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.Districts;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.DistrictsRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;
import com.ehrms.deptenq.utility.AsyncExecutors;
import com.ehrms.deptenq.utility.SplitUtility;

@Controller
@RequestMapping("departmentalEnquiry")
public class DistrictReportsController {

//	@Autowired
//	private IGlobalOrgRepository globalRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private CaseDetailsRepository caseRepo;

//	@Autowired
//	private IEmployeeDetailsRepository empRepo;

	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@Autowired
	private DistrictsRepository disRepo;
	
	@Autowired
	private FinalOutcomeRepository finalRepo;
	
	/* written code on autowired on 02/11/2023 by vijay */
	@Autowired
	private  DepartmentConstants departmentconstants;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LocaleResolver loc;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AsyncExecutors exe;
	
	@Autowired
	private SplitUtility util;
	

	private static final Logger LOG = LoggerFactory.getLogger(DistrictReportsController.class);

	/* Added on 23-11-2022 case details report page */

	@GetMapping("/districtWiseReportsController/{page}")
	public String ruleApplicableReportsControllers(Model model, @PathVariable("page") int page,
			@RequestParam(name = "districtid", required = false) String districtid,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

		LOG.info("district Wise Reports Controller ....");

		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}
		
		long starttime1 = System.currentTimeMillis();
//		List<CaseDetails> finalOut = caseDetailsRepo.findAll();		
		long casecount = caseRepo.count();
		List<Integer> largeList = util.getSplitdata((int)casecount);
		List<Callable<String>> callList = new ArrayList<>();
		List<CaseDetails> finalOut = new ArrayList<>();
		if(!largeList.isEmpty()) {
			for(Integer i=1;i<=largeList.size();i++) {
				final Integer index = i ;
				callList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {																		
						Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
						Page<CaseDetails> caseList = caseRepo.findAll(pageable);
						finalOut.addAll(caseList.getContent());
						return null;
					}
				});
			}
		}
		ExecutorService executorService = exe.getExe();
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callList);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endtime1 = System.currentTimeMillis();
		LOG.info("First Step time required :"+TimeUnit.MILLISECONDS.toSeconds(endtime1-starttime1));
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;

		/* model.addAttribute("divisiondata", divisionRepository.findAll()); */

		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDirection);
		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

		// pagination Writting coding
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {
			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
		Page<CaseDetails> allCases = null;
		if(districtid == null) {
			districtid = "";
		}
		model.addAttribute("districtid", districtid);
		if(districtid != null && !districtid.equalsIgnoreCase("null") && StringUtils.isNotBlank(districtid)) {
			model.addAttribute("divisionid", disRepo.findById(Long.valueOf(districtid)).orElse(new Districts()).getDivisionId().getOrder());
		}

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {

			/* solapur data count */
			List<CaseDetails> solapur = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(533),caseId);
			model.addAttribute("solapur", solapur.size());
			model.addAttribute("solapurempcount", solapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* satara data count */
			List<CaseDetails> satara = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(534),caseId);
			model.addAttribute("satara", satara.size());
			model.addAttribute("sataraempcount", satara.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule13first division wise data count */
			List<CaseDetails> ratanagri = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(535),caseId);
			model.addAttribute("ratanagri", ratanagri.size());
			model.addAttribute("ratanagriCountemployee",
					ratanagri.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* dulhe data count */
			List<CaseDetails> dulhe = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(505),caseId);
			model.addAttribute("dulhe", dulhe.size());
			model.addAttribute("dulheCountemployee", dulhe.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* jalgaon data count */

			List<CaseDetails> jalgaon = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(506),caseId);
			model.addAttribute("jalgaon", jalgaon.size());
			model.addAttribute("jalgaonempcount", jalgaon.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* Buldana data count */
			List<CaseDetails> buldana = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(507),caseId);
			model.addAttribute("buldana", buldana.size());
			model.addAttribute("empbuldanaCount", buldana.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* akola data count */

			List<CaseDetails> akola = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(508),caseId);
			model.addAttribute("akola", akola.size());
			model.addAttribute("empakolaCount", akola.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* washim data count */

			List<CaseDetails> washim = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(509),caseId);
			model.addAttribute("washim", washim.size());
			model.addAttribute("empwashimCount", washim.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* amravati data count */

			List<CaseDetails> amravati = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(510),caseId);
			model.addAttribute("amravati", amravati.size());
			model.addAttribute("empamravaticount", amravati.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* wardha data count */

			List<CaseDetails> wardha = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(511),caseId);
			model.addAttribute("wardha", wardha.size());
			model.addAttribute("empwardhacount", wardha.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nagpur data count */

			List<CaseDetails> nagpur = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(512),caseId);
			model.addAttribute("nagpur", nagpur.size());
			model.addAttribute("empnagpurcount", nagpur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* bhandara data count */

			List<CaseDetails> bhandara = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(513),caseId);
			model.addAttribute("bhandara", bhandara.size());
			model.addAttribute("empbhandaracount", bhandara.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* gondiya data count */

			List<CaseDetails> gondiya = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(514),caseId);
			model.addAttribute("gondiya", gondiya.size());
			model.addAttribute("empgondiyaCount", gondiya.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ganchiroli data count */

			List<CaseDetails> ganchiroli = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(515),caseId);
			model.addAttribute("ganchiroli", ganchiroli.size());
			model.addAttribute("empganchiroliCount",
					ganchiroli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* chandrapur data count */

			List<CaseDetails> chandrapur = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(516),caseId);
			model.addAttribute("chandrapur", chandrapur.size());
			model.addAttribute("empchandrapurCount",
					chandrapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* yavatmal data count */

			List<CaseDetails> yavatmal = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(517),caseId);
			model.addAttribute("yavatmal", yavatmal.size());
			model.addAttribute("empyavatmalCount", yavatmal.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nanded data count */

			List<CaseDetails> nanded = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(518),caseId);
			model.addAttribute("nanded", nanded.size());
			model.addAttribute("empnandedCount", nanded.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* hingoli data count */

			List<CaseDetails> hingoli = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(519),caseId);
			model.addAttribute("hingoli", hingoli.size());
			model.addAttribute("emphingoliCount", hingoli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* parbhani data count */

			List<CaseDetails> parbhani = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(520),caseId);
			model.addAttribute("parbhani", parbhani.size());
			model.addAttribute("empparbhaniCount", parbhani.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* jalna data count */

			List<CaseDetails> jalna = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(521),caseId);
			model.addAttribute("jalna", jalna.size());
			model.addAttribute("empjalnaCount", jalna.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nashik data count */

			List<CaseDetails> nashik = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(523),caseId);
			model.addAttribute("nashik", nashik.size());
			model.addAttribute("empnashikCount", nashik.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* thane data count */

			List<CaseDetails> thane = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(524),caseId);
			model.addAttribute("thane", thane.size());
			model.addAttribute("empthaneCount", thane.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai sub urban data count */

			List<CaseDetails> mumbaisuburban = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(525),caseId);
			model.addAttribute("mumbaisuburban", mumbaisuburban.size());
			model.addAttribute("empmumbaisuburbanCount",
					mumbaisuburban.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai data count */

			List<CaseDetails> mumbai = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(526),caseId);
			model.addAttribute("mumbai", mumbai.size());
			model.addAttribute("empmumbaiCount", mumbai.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* pune data count */

			List<CaseDetails> pune = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(528),caseId);
			model.addAttribute("pune", pune.size());
			model.addAttribute("emppuneCount", pune.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ahmad nagar data count */

			List<CaseDetails> ahmadnagar = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(529),caseId);
			model.addAttribute("ahmadnagar", ahmadnagar.size());
			model.addAttribute("empahmadnagarCount",
					ahmadnagar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* bid data count */

			List<CaseDetails> bid = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(530),caseId);
			model.addAttribute("bid", bid.size());
			model.addAttribute("empbidCount", bid.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* latur data count */
			List<CaseDetails> latur = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(531),caseId);
			model.addAttribute("latur", latur.size());
			model.addAttribute("emplaturCount", latur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* osmanabad data count */

			List<CaseDetails> osmanabad = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(532),caseId);
			model.addAttribute("osmanabad", osmanabad.size());
			model.addAttribute("emposmanabadCount",
					osmanabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* sindhurg data count */
			List<CaseDetails> sindhurg = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(536),caseId);
			model.addAttribute("sindhurg", sindhurg.size());
			model.addAttribute("empsindhurgCount", sindhurg.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* kolhapur data count */
			List<CaseDetails> kolhapur = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(537),caseId);
			model.addAttribute("kolhapur", kolhapur.size());
			model.addAttribute("empkolhapurCount", kolhapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* sangli data count */
			List<CaseDetails> sangli = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(538),caseId);
			model.addAttribute("sangli", sangli.size());
			model.addAttribute("empsangliCount", sangli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* raigad data count */
			List<CaseDetails> raigad = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(527),caseId);
			model.addAttribute("raigad", raigad.size());
			model.addAttribute("empraigadCount", raigad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* aurangabad data count */
			List<CaseDetails> aurangabad = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(522),caseId);
			model.addAttribute("aurangabad", aurangabad.size());
			model.addAttribute("empaurangabadCount",
					aurangabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai city data count */
			List<CaseDetails> mumbaicity = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(642),caseId);
			model.addAttribute("mumbaicity", mumbaicity.size());
			model.addAttribute("empmumbaicityCount",
					mumbaicity.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nandurbar data count */
			List<CaseDetails> nandurbar = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(504),caseId);
			model.addAttribute("nandurbar", nandurbar.size());
			model.addAttribute("empnandurbarCount",
					nandurbar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* palghar data count */
			List<CaseDetails> palghar = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(766),caseId);
			model.addAttribute("palghar", palghar.size());
			model.addAttribute("emppalgharCount", palghar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			// List<CaseDetails> total = caseDetailsRepo.find();

//			long countCases = caseRepo.count();
			/* model.addAttribute("totalCases", countCases); */
			
			if (StringUtils.isNotBlank(districtid)) {
				
				noticePages = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(districtid), pageable,caseId);
				allCases = caseRepo.findByDistrict_IdAndIdNotIn(Long.valueOf(districtid), allPage,caseId);
			} else {
				noticePages = caseRepo.findByIdNotIn(pageable,caseId);
				allCases = caseRepo.findByIdNotIn(allPage,caseId);
			}

			model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {

			/* solapur data count */
			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			List<CaseDetails> solapur = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(533),org,caseId);
			model.addAttribute("solapur", solapur.size());
			model.addAttribute("solapurempcount", solapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* satara data count */
			List<CaseDetails> satara = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(534),org,caseId);
			model.addAttribute("satara", satara.size());
			model.addAttribute("sataraempcount", satara.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule13first division wise data count */
			List<CaseDetails> ratanagri = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(535),org,caseId);
			model.addAttribute("ratanagri", ratanagri.size());
			model.addAttribute("ratanagriCountemployee",
					ratanagri.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* dulhe data count */
			List<CaseDetails> dulhe = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(505),org,caseId);
			model.addAttribute("dulhe", dulhe.size());
			model.addAttribute("dulheCountemployee", dulhe.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* jalgaon data count */

			List<CaseDetails> jalgaon = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(506),org,caseId);
			model.addAttribute("jalgaon", jalgaon.size());
			model.addAttribute("jalgaonempcount", jalgaon.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* Buldana data count */
			List<CaseDetails> buldana = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(507),org,caseId);
			model.addAttribute("buldana", buldana.size());
			model.addAttribute("empbuldanaCount", buldana.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* akola data count */

			List<CaseDetails> akola = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(508),org,caseId);
			model.addAttribute("akola", akola.size());
			model.addAttribute("empakolaCount", akola.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* washim data count */

			List<CaseDetails> washim = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(509),org,caseId);
			model.addAttribute("washim", washim.size());
			model.addAttribute("empwashimCount", washim.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* amravati data count */

			List<CaseDetails> amravati = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(510),org,caseId);
			model.addAttribute("amravati", amravati.size());
			model.addAttribute("empamravaticount", amravati.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* wardha data count */

			List<CaseDetails> wardha = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(511),org,caseId);
			model.addAttribute("wardha", wardha.size());
			model.addAttribute("empwardhacount", wardha.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nagpur data count */

			List<CaseDetails> nagpur = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(512),org,caseId);
			model.addAttribute("nagpur", nagpur.size());
			model.addAttribute("empnagpurcount", nagpur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* bhandara data count */

			List<CaseDetails> bhandara = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(513),org,caseId);
			model.addAttribute("bhandara", bhandara.size());
			model.addAttribute("empbhandaracount", bhandara.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* gondiya data count */

			List<CaseDetails> gondiya = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(514),org,caseId);
			model.addAttribute("gondiya", gondiya.size());
			model.addAttribute("empgondiyaCount", gondiya.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ganchiroli data count */

			List<CaseDetails> ganchiroli = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(515),org,caseId);
			model.addAttribute("ganchiroli", ganchiroli.size());
			model.addAttribute("empganchiroliCount",
					ganchiroli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* chandrapur data count */

			List<CaseDetails> chandrapur = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(516),org,caseId);
			model.addAttribute("chandrapur", chandrapur.size());
			model.addAttribute("empchandrapurCount",
					chandrapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* yavatmal data count */

			List<CaseDetails> yavatmal = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(517),org,caseId);
			model.addAttribute("yavatmal", yavatmal.size());
			model.addAttribute("empyavatmalCount", yavatmal.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nanded data count */

			List<CaseDetails> nanded = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(518),org,caseId);
			model.addAttribute("nanded", nanded.size());
			model.addAttribute("empnandedCount", nanded.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* hingoli data count */

			List<CaseDetails> hingoli = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(519),org,caseId);
			model.addAttribute("hingoli", hingoli.size());
			model.addAttribute("emphingoliCount", hingoli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* parbhani data count */

			List<CaseDetails> parbhani = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(520),org,caseId);
			model.addAttribute("parbhani", parbhani.size());
			model.addAttribute("empparbhaniCount", parbhani.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* jalna data count */

			List<CaseDetails> jalna = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(521),org,caseId);
			model.addAttribute("jalna", jalna.size());
			model.addAttribute("empjalnaCount", jalna.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nashik data count */

			List<CaseDetails> nashik = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(523),org,caseId);
			model.addAttribute("nashik", nashik.size());
			model.addAttribute("empnashikCount", nashik.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* thane data count */

			List<CaseDetails> thane = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(524),org,caseId);
			model.addAttribute("thane", thane.size());
			model.addAttribute("empthaneCount", thane.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai sub urban data count */

			List<CaseDetails> mumbaisuburban = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(525),org,caseId);
			model.addAttribute("mumbaisuburban", mumbaisuburban.size());
			model.addAttribute("empmumbaisuburbanCount",
					mumbaisuburban.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai data count */

			List<CaseDetails> mumbai = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(526),org,caseId);
			model.addAttribute("mumbai", mumbai.size());
			model.addAttribute("empmumbaiCount", mumbai.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* pune data count */

			List<CaseDetails> pune = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(528),org,caseId);
			model.addAttribute("pune", pune.size());
			model.addAttribute("emppuneCount", pune.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ahmad nagar data count */

			List<CaseDetails> ahmadnagar = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(529),org,caseId);
			model.addAttribute("ahmadnagar", ahmadnagar.size());
			model.addAttribute("empahmadnagarCount",
					ahmadnagar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* bid data count */

			List<CaseDetails> bid = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(530),org,caseId);
			model.addAttribute("bid", bid.size());
			model.addAttribute("empbidCount", bid.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* latur data count */
			List<CaseDetails> latur = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(531),org,caseId);
			model.addAttribute("latur", latur.size());
			model.addAttribute("emplaturCount", latur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* osmanabad data count */

			List<CaseDetails> osmanabad = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(532),org,caseId);
			model.addAttribute("osmanabad", osmanabad.size());
			model.addAttribute("emposmanabadCount",
					osmanabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* sindhurg data count */
			List<CaseDetails> sindhurg = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(536),org,caseId);
			model.addAttribute("sindhurg", sindhurg.size());
			model.addAttribute("empsindhurgCount", sindhurg.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* kolhapur data count */
			List<CaseDetails> kolhapur = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(537),org,caseId);
			model.addAttribute("kolhapur", kolhapur.size());
			model.addAttribute("empkolhapurCount", kolhapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* sangli data count */
			List<CaseDetails> sangli = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(538),org,caseId);
			model.addAttribute("sangli", sangli.size());
			model.addAttribute("empsangliCount", sangli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* raigad data count */
			List<CaseDetails> raigad = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(527),org,caseId);
			model.addAttribute("raigad", raigad.size());
			model.addAttribute("empraigadCount", raigad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* aurangabad data count */
			List<CaseDetails> aurangabad = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(522),org,caseId);
			model.addAttribute("aurangabad", aurangabad.size());
			model.addAttribute("empaurangabadCount",
					aurangabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai city data count */
			List<CaseDetails> mumbaicity = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(642),org,caseId);
			model.addAttribute("mumbaicity", mumbaicity.size());
			model.addAttribute("empmumbaicityCount",
					mumbaicity.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nandurbar data count */
			List<CaseDetails> nandurbar = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(504),org,caseId);
			model.addAttribute("nandurbar", nandurbar.size());
			model.addAttribute("empnandurbarCount",
					nandurbar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* palghar data count */
			List<CaseDetails> palghar = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(766),org,caseId);
			model.addAttribute("palghar", palghar.size());
			model.addAttribute("emppalgharCount", palghar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			// List<CaseDetails> total = caseDetailsRepo.find();

//			long countCases = caseRepo.count();
			/* model.addAttribute("totalCases", countCases); */
			
			if (StringUtils.isNotBlank(districtid)) {
				
				noticePages = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(districtid),org, pageable,caseId);
				allCases = caseRepo.findByDistrict_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(districtid),org, allPage,caseId);
			} else {
				noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,pageable,caseId);
				allCases = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,allPage,caseId);
			}

			model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

		}

		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")|| currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARY")) {
			List<SubDepartment> subList = currentUser.getSecsubList().stream().filter(s -> s.isActive())
					.map(s -> s.getSubDepartment()).collect(Collectors.toList());
			List<User> userList = new ArrayList<>();
			if(subList.isEmpty()) {
				userList = userRepo.findBySubDepartmentIn(
						subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
				userList.add(currentUser);
			} else {
				userList = userRepo.findBySubDepartmentIn(subList);
			}
			/* solapur data count */
			List<CaseDetails> solapur = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(533),caseId);
			model.addAttribute("solapur", solapur.size());
			model.addAttribute("solapurempcount", solapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* satara data count */
			List<CaseDetails> satara = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(534),caseId);
			model.addAttribute("satara", satara.size());
			model.addAttribute("sataraempcount", satara.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			
			/* rule13first division wise data count */
			List<CaseDetails> ratanagri = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(535),caseId);
			model.addAttribute("ratanagri", ratanagri.size());
			model.addAttribute("ratanagriCountemployee",
					ratanagri.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			/* dulhe data count */
			List<CaseDetails> dulhe = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(505),caseId);
			model.addAttribute("dulhe", dulhe.size());
			model.addAttribute("dulheCountemployee", dulhe.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* jalgaon data count */

			List<CaseDetails> jalgaon = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(506),caseId);
			model.addAttribute("jalgaon", jalgaon.size());
			model.addAttribute("jalgaonempcount", jalgaon.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			
			/* Buldana data count */
			List<CaseDetails> buldana = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(507),caseId);
			model.addAttribute("buldana", buldana.size());
			model.addAttribute("empbuldanaCount", buldana.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* akola data count */

			List<CaseDetails> akola = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(508),caseId);
			model.addAttribute("akola", akola.size());
			model.addAttribute("empakolaCount", akola.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* washim data count */

			List<CaseDetails> washim = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(509),caseId);
			model.addAttribute("washim", washim.size());
			model.addAttribute("empwashimCount", washim.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* amravati data count */

			List<CaseDetails> amravati = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(510),caseId);
			model.addAttribute("amravati", amravati.size());
			model.addAttribute("empamravaticount", amravati.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* wardha data count */

			List<CaseDetails> wardha = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(511),caseId);
			model.addAttribute("wardha", wardha.size());
			model.addAttribute("empwardhacount", wardha.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nagpur data count */

			List<CaseDetails> nagpur = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(512),caseId);
			model.addAttribute("nagpur", nagpur.size());
			model.addAttribute("empnagpurcount", nagpur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* bhandara data count */

			List<CaseDetails> bhandara = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(513),caseId);
			model.addAttribute("bhandara", bhandara.size());
			model.addAttribute("empbhandaracount", bhandara.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* gondiya data count */

			List<CaseDetails> gondiya = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(514),caseId);
			model.addAttribute("gondiya", gondiya.size());
			model.addAttribute("empgondiyaCount", gondiya.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ganchiroli data count */

			List<CaseDetails> ganchiroli = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(515),caseId);
			model.addAttribute("ganchiroli", ganchiroli.size());
			model.addAttribute("empganchiroliCount",
					ganchiroli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* chandrapur data count */

			List<CaseDetails> chandrapur = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(516),caseId);
			model.addAttribute("chandrapur", chandrapur.size());
			model.addAttribute("empchandrapurCount",
					chandrapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* yavatmal data count */

			List<CaseDetails> yavatmal = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(517),caseId);
			model.addAttribute("yavatmal", yavatmal.size());
			model.addAttribute("empyavatmalCount", yavatmal.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nanded data count */

			List<CaseDetails> nanded = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(518),caseId);
			model.addAttribute("nanded", nanded.size());
			model.addAttribute("empnandedCount", nanded.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* hingoli data count */

			List<CaseDetails> hingoli = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(519),caseId);
			model.addAttribute("hingoli", hingoli.size());
			model.addAttribute("emphingoliCount", hingoli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* parbhani data count */

			List<CaseDetails> parbhani = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(520),caseId);
			model.addAttribute("parbhani", parbhani.size());
			model.addAttribute("empparbhaniCount", parbhani.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* jalna data count */

			List<CaseDetails> jalna = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(521),caseId);
			model.addAttribute("jalna", jalna.size());
			model.addAttribute("empjalnaCount", jalna.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nashik data count */

			List<CaseDetails> nashik = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(523),caseId);
			model.addAttribute("nashik", nashik.size());
			model.addAttribute("empnashikCount", nashik.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* thane data count */

			List<CaseDetails> thane = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(524),caseId);
			model.addAttribute("thane", thane.size());
			model.addAttribute("empthaneCount", thane.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai sub urban data count */

			List<CaseDetails> mumbaisuburban = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(525),caseId);
			model.addAttribute("mumbaisuburban", mumbaisuburban.size());
			model.addAttribute("empmumbaisuburbanCount",
					mumbaisuburban.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai data count */

			List<CaseDetails> mumbai = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(526),caseId);
			model.addAttribute("mumbai", mumbai.size());
			model.addAttribute("empmumbaiCount", mumbai.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* pune data count */

			List<CaseDetails> pune = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(528),caseId);
			model.addAttribute("pune", pune.size());
			model.addAttribute("emppuneCount", pune.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ahmad nagar data count */

			List<CaseDetails> ahmadnagar = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(529),caseId);
			model.addAttribute("ahmadnagar", ahmadnagar.size());
			model.addAttribute("empahmadnagarCount",
					ahmadnagar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* bid data count */

			List<CaseDetails> bid = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(530),caseId);
			model.addAttribute("bid", bid.size());
			model.addAttribute("empbidCount", bid.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* latur data count */
			List<CaseDetails> latur = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(531),caseId);
			model.addAttribute("latur", latur.size());
			model.addAttribute("emplaturCount", latur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* osmanabad data count */

			List<CaseDetails> osmanabad = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(532),caseId);
			model.addAttribute("osmanabad", osmanabad.size());
			model.addAttribute("emposmanabadCount",
			osmanabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			/* sindhurg data count */
			List<CaseDetails> sindhurg = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(536),caseId);
			model.addAttribute("sindhurg", sindhurg.size());
			model.addAttribute("empsindhurgCount", sindhurg.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* kolhapur data count */
			List<CaseDetails> kolhapur = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(537),caseId);
			model.addAttribute("kolhapur", kolhapur.size());
			model.addAttribute("empkolhapurCount", kolhapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			
			/* sangli data count */
			List<CaseDetails> sangli = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(538),caseId);
			model.addAttribute("sangli", sangli.size());
			model.addAttribute("empsangliCount", sangli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			
			/* raigad data count */
			List<CaseDetails> raigad = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(527),caseId);
			model.addAttribute("raigad", raigad.size());
			model.addAttribute("empraigadCount", raigad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* aurangabad data count */
			List<CaseDetails> aurangabad = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(522),caseId);
			model.addAttribute("aurangabad", aurangabad.size());
			model.addAttribute("empaurangabadCount",
					aurangabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai city data count */
			List<CaseDetails> mumbaicity = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(642),caseId);
			model.addAttribute("mumbaicity", mumbaicity.size());
			model.addAttribute("empmumbaicityCount",
					mumbaicity.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nandurbar data count */
			List<CaseDetails> nandurbar = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(504),caseId);
			model.addAttribute("nandurbar", nandurbar.size());
			model.addAttribute("empnandurbarCount",
					nandurbar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* palghar data count */
			List<CaseDetails> palghar = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(766),caseId);
			model.addAttribute("palghar", palghar.size());
			model.addAttribute("emppalgharCount", palghar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			// depend on employee we are getting data code

			noticePages = caseRepo.findByIdNotInAndCurrentUserIn(pageable,caseId,userList);
			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseRepo.findByIdNotInAndCurrentUserIn(allPages,caseId,userList);
			model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

			// for Role _Department we will get rule 8 count

			/*
			 * allCases = caseDetailsRepo.findAll(allPages); model.addAttribute("empCount",
			 * allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
			 */

			long countCases = caseRepo.countDistinctIdByCurrentUserInAndIdNotIn(userList,caseId);
			model.addAttribute("totalCases", countCases);
			noticePages = caseRepo.findDistinctIdByCurrentUserInAndIdNotIn(userList, pageable,caseId);
			if(StringUtils.isNotBlank(districtid)) {
				model.addAttribute("districtid",districtid );
				noticePages = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(districtid),pageable,caseId);	
				allCases = caseRepo.findByCurrentUserInAndDistrict_IdAndIdNotIn(userList,Long.valueOf(districtid),allPage,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());

			}
			else {
				
				model.addAttribute("districtid","");
				noticePages = caseRepo.findByCurrentUserInAndIdNotIn(userList,pageable,caseId);
				allCases = caseRepo.findByCurrentUserInAndIdNotIn(userList,allPage,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());

				
			}
		}

		else {

			/* solapur data count */
			List<CaseDetails> solapur = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(533),caseId);
			model.addAttribute("solapur", solapur.size());
			model.addAttribute("solapurempcount", solapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* satara data count */
			List<CaseDetails> satara = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(534),caseId);
			model.addAttribute("satara", satara.size());
			model.addAttribute("sataraempcount", satara.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ratanagri data count */
			List<CaseDetails> ratanagri = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(535),caseId);
			model.addAttribute("ratanagri", ratanagri.size());
			model.addAttribute("ratanagriCountemployee",
					ratanagri.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* dulhe data count */
			List<CaseDetails> dulhe = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(505),caseId);
			model.addAttribute("dulhe", dulhe.size());
			model.addAttribute("dulheCountemployee", dulhe.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* jalgaon data count */

			List<CaseDetails> jalgaon = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(506),caseId);
			model.addAttribute("jalgaon", jalgaon.size());
			model.addAttribute("jalgaonempcount", jalgaon.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			
			/* Buldana data count */
			List<CaseDetails> buldana = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(507),caseId);
			model.addAttribute("buldana", buldana.size());
			model.addAttribute("empbuldanaCount", buldana.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* akola data count */

			List<CaseDetails> akola = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(508),caseId);
			model.addAttribute("akola", akola.size());
			model.addAttribute("empakolaCount", akola.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* washim data count */

			List<CaseDetails> washim = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(509),caseId);
			model.addAttribute("washim", washim.size());
			model.addAttribute("empwashimCount", washim.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* amravati data count */

			List<CaseDetails> amravati = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(510),caseId);
			model.addAttribute("amravati", amravati.size());
			model.addAttribute("empamravaticount", amravati.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			/* wardha data count */

			List<CaseDetails> wardha = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(511),caseId);
			model.addAttribute("wardha", wardha.size());
			model.addAttribute("empwardhacount", wardha.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nagpur data count */

			List<CaseDetails> nagpur = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(512),caseId);
			model.addAttribute("nagpur", nagpur.size());
			model.addAttribute("empnagpurcount", nagpur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* bhandara data count */

			List<CaseDetails> bhandara = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(513),caseId);
			model.addAttribute("bhandara", bhandara.size());
			model.addAttribute("empbhandaracount", bhandara.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* gondiya data count */

			List<CaseDetails> gondiya = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(514),caseId);
			model.addAttribute("gondiya", gondiya.size());
			model.addAttribute("empgondiyaCount", gondiya.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ganchiroli data count */

			List<CaseDetails> ganchiroli = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(515),caseId);
			model.addAttribute("ganchiroli", ganchiroli.size());
			model.addAttribute("empganchiroliCount",
					ganchiroli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* chandrapur data count */

			List<CaseDetails> chandrapur = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(516),caseId);
			model.addAttribute("chandrapur", chandrapur.size());
			model.addAttribute("empchandrapurCount",
					chandrapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* yavatmal data count */

			List<CaseDetails> yavatmal = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(517),caseId);
			model.addAttribute("yavatmal", yavatmal.size());
			model.addAttribute("empyavatmalCount", yavatmal.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nanded data count */

			List<CaseDetails> nanded = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(518),caseId);
			model.addAttribute("nanded", nanded.size());
			model.addAttribute("empnandedCount", nanded.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* hingoli data count */

			List<CaseDetails> hingoli = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(519),caseId);
			model.addAttribute("hingoli", hingoli.size());
			model.addAttribute("emphingoliCount", hingoli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* parbhani data count */

			List<CaseDetails> parbhani = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(520),caseId);
			model.addAttribute("parbhani", parbhani.size());
			model.addAttribute("empparbhaniCount", parbhani.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* jalna data count */

			List<CaseDetails> jalna = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(521),caseId);
			model.addAttribute("jalna", jalna.size());
			model.addAttribute("empjalnaCount", jalna.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* nashik data count */

			List<CaseDetails> nashik = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(523),caseId);
			model.addAttribute("nashik", nashik.size());
			model.addAttribute("empnashikCount", nashik.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* thane data count */

			List<CaseDetails> thane = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(524),caseId);
			model.addAttribute("thane", thane.size());
			model.addAttribute("empthaneCount", thane.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai sub urban data count */

			List<CaseDetails> mumbaisuburban = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(525),caseId);
			model.addAttribute("mumbaisuburban", mumbaisuburban.size());
			model.addAttribute("empmumbaisuburbanCount",
					mumbaisuburban.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* mumbai data count */

			List<CaseDetails> mumbai = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(526),caseId);
			model.addAttribute("mumbai", mumbai.size());
			model.addAttribute("empmumbaiCount", mumbai.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* pune data count */

			List<CaseDetails> pune = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(528),caseId);
			model.addAttribute("pune", pune.size());
			model.addAttribute("emppuneCount", pune.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* ahmad nagar data count */

			List<CaseDetails> ahmadnagar = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(529),caseId);
			model.addAttribute("ahmadnagar", ahmadnagar.size());
			model.addAttribute("empahmadnagarCount",
					ahmadnagar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* bid data count */

			List<CaseDetails> bid = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(530),caseId);
			model.addAttribute("bid", bid.size());
			model.addAttribute("empbidCount", bid.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* latur data count */
			List<CaseDetails> latur = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(531),caseId);
			model.addAttribute("latur", latur.size());
			model.addAttribute("emplaturCount", latur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* osmanabad data count */

			List<CaseDetails> osmanabad = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(532),caseId);
			model.addAttribute("osmanabad", osmanabad.size());
			model.addAttribute("emposmanabadCount",
					osmanabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* sindhurg data count */
			List<CaseDetails> sindhurg = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(536),caseId);
			model.addAttribute("sindhurg", sindhurg.size());
			model.addAttribute("empsindhurgCount", sindhurg.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* kolhapur data count */
			List<CaseDetails> kolhapur = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(537),caseId);
			model.addAttribute("kolhapur", kolhapur.size());
			model.addAttribute("empkolhapurCount", kolhapur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* sangli data count */
			List<CaseDetails> sangli = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(538),caseId);
			model.addAttribute("sangli", sangli.size());
			model.addAttribute("empsangliCount", sangli.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* raigad data count */
			List<CaseDetails> raigad = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(527),caseId);
			model.addAttribute("raigad", raigad.size());
			model.addAttribute("empraigadCount", raigad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			
			/* aurangabad data count */
			List<CaseDetails> aurangabad = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(522),caseId);
			model.addAttribute("aurangabad", aurangabad.size());
			model.addAttribute("empaurangabadCount",
			aurangabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			/* mumbai city data count */
			List<CaseDetails> mumbaicity = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(642),caseId);
			model.addAttribute("mumbaicity", mumbaicity.size());
			model.addAttribute("empmumbaicityCount",
					mumbaicity.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			
			/* nandurbar data count */
			List<CaseDetails> nandurbar = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(504),caseId);
			model.addAttribute("nandurbar", nandurbar.size());
			model.addAttribute("empnandurbarCount",
					nandurbar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* palghar data count */
			List<CaseDetails> palghar = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(766),caseId);
			model.addAttribute("palghar", palghar.size());
			model.addAttribute("emppalgharCount", palghar.stream().mapToLong(s -> s.getEmployeeList().size()).sum());


			
		//	  noticePages = caseRepo.findAll(pageable);
			  Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);
				
			  
				/* Employee count  down on Depend on Departments  code written (21/11/2022) */
				//allCases = caseRepo.findByCurrentUserAndGlobalorg(currentUser, currentUser.getPimsEmployee().getGlobalOrgId(),allPages);
				allCases = caseRepo.findByCurrentUserAndGlobalorgAndIdNotIn(currentUser, currentUser.getPimsEmployee().getGlobalOrgId(),allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
				
				
//				List<InquiryOfficerDetails> inqOffDeFil = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUser(ioList,false,currentUser);
//				noticePages = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUser(inqOffDeFil,currentUser, pageable);
//				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//				allCases = caseDetailsRepo.findDistinctIdByIoListIn(inqOffDeFil, allPage);
//
//				model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
//				
				
				
				
			  
			long countCases = caseRepo.countDistinctIdByCurrentUserAndIdNotIn(currentUser,caseId);
			model.addAttribute("totalItems", countCases);
			
			if(StringUtils.isNotBlank(districtid)) {
				model.addAttribute("districtid",districtid );
				noticePages = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(districtid),pageable,caseId);	
				allCases = caseRepo.findByCurrentUserAndDistrict_IdAndIdNotIn(currentUser,Long.valueOf(districtid),allPage,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());

			}
			else {
				
				model.addAttribute("districtid","");
				noticePages = caseRepo.findByCurrentUserAndIdNotIn(currentUser,pageable,caseId);
				allCases = caseRepo.findByCurrentUserAndIdNotIn(currentUser,allPage,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());

				
			}

		}

		model.addAttribute("caseDetailsAllRecordsreport", allCases);
		model.addAttribute("caseDetailsAllRecords", noticePages);
		model.addAttribute("currentPage", page);
		int begin = Math.max(1, page - 5);
		if (page >= 5) {
			begin = page;
		}
		int end = Math.min(begin + 4, noticePages.getTotalPages());
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("totalPages", noticePages.getTotalPages());
		model.addAttribute("totalItems", noticePages.getTotalElements());
		
		model.addAttribute("totalCasesLabel",messageSource.getMessage("deptenq.totalcases.label", null, loc.resolveLocale(request)));
		model.addAttribute("totalEmployeeCountLabel",messageSource.getMessage("deptenq.totalemployee.label", null, loc.resolveLocale(request)));
		
		model.addAttribute("alldistricts",messageSource.getMessage("deptenq.totalemployee.alldistricts.label", null, loc.resolveLocale(request)));
		model.addAttribute("nagpurlabel",messageSource.getMessage("deptenq.totalemployee.ngpdivdist.label", null, loc.resolveLocale(request)));
		model.addAttribute("amravatidistlabel",messageSource.getMessage("deptenq.totalemployee.amravatidistlabel.label", null, loc.resolveLocale(request)));
		model.addAttribute("sambhajinagar",messageSource.getMessage("deptenq.totalemployee.sambhaji.label", null, loc.resolveLocale(request)));
		model.addAttribute("konkandivdist",messageSource.getMessage("deptenq.totalemployee.konkandivdist.label", null, loc.resolveLocale(request)));
		model.addAttribute("nashikdivdist",messageSource.getMessage("deptenq.totalemployee.nashikdivdist.label", null, loc.resolveLocale(request)));
		model.addAttribute("punedivdist",messageSource.getMessage("deptenq.totalemployee.punedivdist.label", null, loc.resolveLocale(request)));
		
		return "DeptInquiryViews/ReportsForDistrictsWisePendingCases.html";

	}

}
