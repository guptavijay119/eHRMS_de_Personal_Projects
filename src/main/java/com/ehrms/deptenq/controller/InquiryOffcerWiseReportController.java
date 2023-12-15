package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.InquiryOfficerList;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.modelsDto.MasterDashboardData;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.InquiryOfficerListRepository;
import com.ehrms.deptenq.repository.InquiryOfficerRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class InquiryOffcerWiseReportController {

	@Autowired
	private InquiryOfficerRepository inqDetailRepo;

	@Autowired
	private InquiryOfficerListRepository inqOfficerListRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;

	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	
	/* written code on autowired on 02/11/2023 by vijay */
	@Autowired
	private  DepartmentConstants departmentconstants;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LocaleResolver loc;
	
	@Autowired
	private MessageSource messageSource;
	
//	@Autowired
//	private FinalOutcomeRepository finalRepo;

	private static final Logger LOG = LoggerFactory.getLogger(InquiryOffcerWiseReportController.class);

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/ioReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "io", required = false) String io,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

		LOG.info("misconduct applicable reports controller....");
		
		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}

		List<CaseDetails> finalOut = caseDetailsRepo.findAll();		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseDetailsRepo.findByActiveForTransfer(true);
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

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
		Page<CaseDetails> allCases = null;

		List<InquiryOfficerList> ioList = null;
		List<InquiryOfficerList> ioallList = null;

		if (StringUtils.isNotBlank(io) && !io.equalsIgnoreCase("null")) {
			if (io.equalsIgnoreCase("22")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(22L));
			}
			if (io.equalsIgnoreCase("1")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(1L));
			}
			if (io.equalsIgnoreCase("2")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(2L));
			}
			if (io.equalsIgnoreCase("3")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(3L));
			}
			if (io.equalsIgnoreCase("4")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(4L));
			}
			if (io.equalsIgnoreCase("5")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(5L));
			}
			if (io.equalsIgnoreCase("7")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(7L));
			}
			if (io.equalsIgnoreCase("6")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(6L));
			}
			if (io.equalsIgnoreCase("8")) {
				ioList = inqOfficerListRepo
						.findByInquiryOfficerlistAndActive("Contractual IO", true);
			}
			if (io.equalsIgnoreCase("21")) {
				ioList = inqOfficerListRepo.findByIdIn(Arrays.asList(21L));
			}
		} else {
			ioList = inqOfficerListRepo.findAll();
		}
		ioallList = inqOfficerListRepo.findAll();
		model.addAttribute("io", io);

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {

			List<InquiryOfficerDetails> ioListall = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(ioallList,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList22 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(22L,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList1 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(1L,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList2 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(2L,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList3 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(3L,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList4 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(4L,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList5 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(5L,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList7 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(7L,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList6 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(6L,false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList8 = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(
					inqOfficerListRepo
					.findByInquiryOfficerlistAndActive("Contractual IO",true),false,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList21 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(21L,false,Arrays.asList("Case Decided"));

			List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioListall,caseId);

			model.addAttribute("totalCases", allGenCase.size());
			model.addAttribute("totalEmpCount", allGenCase.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<InquiryOfficerDetails> inqOffDeFil = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsPendingWithNotIn(ioList,false,Arrays.asList("Case Decided"));
			noticePages = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(inqOffDeFil, pageable,caseId);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(inqOffDeFil, allPage,caseId);

			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> disciplinaryAuthorityCases = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList22,caseId);
			model.addAttribute("disciplinaryAuthorityCases", disciplinaryAuthorityCases.size());
			model.addAttribute("disciplinaryAuthorityCasesEmp", disciplinaryAuthorityCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> secretaryCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList1,caseId);
			model.addAttribute("secretaryCaseList", secretaryCaseList.size());
			model.addAttribute("secretaryCaseListEmp",  secretaryCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> kokanCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList2,caseId);
			model.addAttribute("kokanCaseList", kokanCaseList.size());
			model.addAttribute("kokanCaseListEmp", kokanCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> puneCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList3,caseId);
			model.addAttribute("puneCaseList", puneCaseList.size());
			model.addAttribute("puneCaseListEmp", puneCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> nashikCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList4,caseId);
			model.addAttribute("nashikCaseList", nashikCaseList.size());
			model.addAttribute("nashikCaseListEmp", nashikCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> auranCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList5,caseId);
			model.addAttribute("auranCaseList", auranCaseList.size());
			model.addAttribute("auranCaseListEmp", auranCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> nagpurCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList7,caseId);
			model.addAttribute("nagpurCaseList", nagpurCaseList.size());
			model.addAttribute("nagpurCaseListEmp", nagpurCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> amravatiCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList6,caseId);
			model.addAttribute("amravatiCaseList", amravatiCaseList.size());
			model.addAttribute("amravatiCaseListEmp", amravatiCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> conractCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList8,caseId);
			model.addAttribute("conractCaseList", conractCaseList.size());
			model.addAttribute("conractCaseListEmp", conractCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> othersCaseList = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioList21,caseId);
			model.addAttribute("othersCaseList", othersCaseList.size());
			model.addAttribute("othersCaseListEmp", othersCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {

			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			List<InquiryOfficerDetails> ioListall = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(ioallList,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList22 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(22L,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList1 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(1L,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList2 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(2L,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList3 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(3L,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList4 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(4L,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList5 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(5L,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList7 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(7L,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList6 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(6L,false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList8 = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(
					inqOfficerListRepo
					.findByInquiryOfficerlistAndActive("Contractual IO",true),false,org,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList21 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(21L,false,org,Arrays.asList("Case Decided"));

			List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByIoListInAndIdNotIn(ioListall,caseId);

			model.addAttribute("totalCases", allGenCase.size());
			model.addAttribute("totalEmpCount", allGenCase.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<InquiryOfficerDetails> inqOffDeFil = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsPendingWithNotIn(ioList,false,org,Arrays.asList("Case Decided"));
			noticePages = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(inqOffDeFil,org, pageable,caseId);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(inqOffDeFil,org, allPage,caseId);

			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> disciplinaryAuthorityCases = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList22,org,caseId);
			model.addAttribute("disciplinaryAuthorityCases", disciplinaryAuthorityCases.size());
			model.addAttribute("disciplinaryAuthorityCasesEmp", disciplinaryAuthorityCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> secretaryCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList1,org,caseId);
			model.addAttribute("secretaryCaseList", secretaryCaseList.size());
			model.addAttribute("secretaryCaseListEmp",  secretaryCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> kokanCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList2,org,caseId);
			model.addAttribute("kokanCaseList", kokanCaseList.size());
			model.addAttribute("kokanCaseListEmp", kokanCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> puneCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList3,org,caseId);
			model.addAttribute("puneCaseList", puneCaseList.size());
			model.addAttribute("puneCaseListEmp", puneCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> nashikCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList4,org,caseId);
			model.addAttribute("nashikCaseList", nashikCaseList.size());
			model.addAttribute("nashikCaseListEmp", nashikCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> auranCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList5,org,caseId);
			model.addAttribute("auranCaseList", auranCaseList.size());
			model.addAttribute("auranCaseListEmp", auranCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> nagpurCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList7,org,caseId);
			model.addAttribute("nagpurCaseList", nagpurCaseList.size());
			model.addAttribute("nagpurCaseListEmp", nagpurCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> amravatiCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList6,org,caseId);
			model.addAttribute("amravatiCaseList", amravatiCaseList.size());
			model.addAttribute("amravatiCaseListEmp", amravatiCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> conractCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList8,org,caseId);
			model.addAttribute("conractCaseList", conractCaseList.size());
			model.addAttribute("conractCaseListEmp", conractCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> othersCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(ioList21,org,caseId);
			model.addAttribute("othersCaseList", othersCaseList.size());
			model.addAttribute("othersCaseListEmp", othersCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

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
			
			List<InquiryOfficerDetails> ioListall = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(ioallList,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList22 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(22L,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList1 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(1L,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList2 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(2L,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList3 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(3L,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList4 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(4L,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList5 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(5L,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList7 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(7L,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList6 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(6L,false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList8 = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(
					inqOfficerListRepo
					.findByInquiryOfficerlistAndActive("Contractual IO",true),false,userList,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList21 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(21L,false,userList,Arrays.asList("Case Decided"));

			List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioListall,userList,caseId);

			model.addAttribute("totalCases", allGenCase.size());
			model.addAttribute("totalEmpCount", allGenCase.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<InquiryOfficerDetails> inqOffDeFil = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserInAndCasedetailsPendingWithNotIn(ioList,false,userList,Arrays.asList("Case Decided"));
			noticePages = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(inqOffDeFil,userList, pageable,caseId);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(inqOffDeFil,userList, allPage,caseId);

			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> disciplinaryAuthorityCases = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList22,userList,caseId);
			model.addAttribute("disciplinaryAuthorityCases", disciplinaryAuthorityCases.size());
			model.addAttribute("disciplinaryAuthorityCasesEmp", disciplinaryAuthorityCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> secretaryCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList1,userList,caseId);
			model.addAttribute("secretaryCaseList", secretaryCaseList.size());
			model.addAttribute("secretaryCaseListEmp",  secretaryCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> kokanCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList2,userList,caseId);
			model.addAttribute("kokanCaseList", kokanCaseList.size());
			model.addAttribute("kokanCaseListEmp", kokanCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> puneCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList3,userList,caseId);
			model.addAttribute("puneCaseList", puneCaseList.size());
			model.addAttribute("puneCaseListEmp", puneCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> nashikCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList4,userList,caseId);
			model.addAttribute("nashikCaseList", nashikCaseList.size());
			model.addAttribute("nashikCaseListEmp", nashikCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> auranCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList5,userList,caseId);
			model.addAttribute("auranCaseList", auranCaseList.size());
			model.addAttribute("auranCaseListEmp", auranCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> nagpurCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList7,userList,caseId);
			model.addAttribute("nagpurCaseList", nagpurCaseList.size());
			model.addAttribute("nagpurCaseListEmp", nagpurCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> amravatiCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList6,userList,caseId);
			model.addAttribute("amravatiCaseList", amravatiCaseList.size());
			model.addAttribute("amravatiCaseListEmp", amravatiCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> conractCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList8,userList,caseId);
			model.addAttribute("conractCaseList", conractCaseList.size());
			model.addAttribute("conractCaseListEmp", conractCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> othersCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserInAndIdNotIn(ioList21,userList,caseId);
			model.addAttribute("othersCaseList", othersCaseList.size());
			model.addAttribute("othersCaseListEmp", othersCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

		}

		else {
			

			List<InquiryOfficerDetails> ioListall = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(ioallList,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList22 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(22L,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList1 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(1L,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList2 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(2L,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList3 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(3L,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList4 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(4L,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList5 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(5L,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList7 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(7L,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList6 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(6L,false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList8 = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(
					inqOfficerListRepo
					.findByInquiryOfficerlistAndActive("Contractual IO",true),false,currentUser,Arrays.asList("Case Decided"));
			List<InquiryOfficerDetails> ioList21 = inqDetailRepo.findByListOfInquiryOfficerIdAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(21L,false,currentUser,Arrays.asList("Case Decided"));

			List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioListall,currentUser,caseId);

			model.addAttribute("totalCases", allGenCase.size());
			model.addAttribute("totalEmpCount", allGenCase.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<InquiryOfficerDetails> inqOffDeFil = inqDetailRepo.findByListOfInquiryOfficerInAndInquiryReportSubmittedAndCasedetailsCurrentUserAndCasedetailsPendingWithNotIn(ioList,false,currentUser,Arrays.asList("Case Decided"));
			noticePages = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(inqOffDeFil,currentUser, pageable,caseId);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(inqOffDeFil,currentUser, allPage,caseId);

			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> disciplinaryAuthorityCases = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList22,currentUser,caseId);
			model.addAttribute("disciplinaryAuthorityCases", disciplinaryAuthorityCases.size());
			model.addAttribute("disciplinaryAuthorityCasesEmp", disciplinaryAuthorityCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> secretaryCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList1,currentUser,caseId);
			model.addAttribute("secretaryCaseList", secretaryCaseList.size());
			model.addAttribute("secretaryCaseListEmp",  secretaryCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> kokanCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList2,currentUser,caseId);
			model.addAttribute("kokanCaseList", kokanCaseList.size());
			model.addAttribute("kokanCaseListEmp", kokanCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> puneCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList3,currentUser,caseId);
			model.addAttribute("puneCaseList", puneCaseList.size());
			model.addAttribute("puneCaseListEmp", puneCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> nashikCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList4,currentUser,caseId);
			model.addAttribute("nashikCaseList", nashikCaseList.size());
			model.addAttribute("nashikCaseListEmp", nashikCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> auranCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList5,currentUser,caseId);
			model.addAttribute("auranCaseList", auranCaseList.size());
			model.addAttribute("auranCaseListEmp", auranCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> nagpurCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList7,currentUser,caseId);
			model.addAttribute("nagpurCaseList", nagpurCaseList.size());
			model.addAttribute("nagpurCaseListEmp", nagpurCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> amravatiCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList6,currentUser,caseId);
			model.addAttribute("amravatiCaseList", amravatiCaseList.size());
			model.addAttribute("amravatiCaseListEmp", amravatiCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> conractCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList8,currentUser,caseId);
			model.addAttribute("conractCaseList", conractCaseList.size());
			model.addAttribute("conractCaseListEmp", conractCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			
			List<CaseDetails> othersCaseList = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserAndIdNotIn(ioList21,currentUser,caseId);
			model.addAttribute("othersCaseList", othersCaseList.size());
			model.addAttribute("othersCaseListEmp", othersCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

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

		return "DeptInquiryViews/ReportsforInquiryOfficerWise.html";

	}

}
