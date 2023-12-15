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
import com.ehrms.deptenq.models.MisconductTypesMaster;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.MisconductTypesMasterRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class MisconductReportsController {

	@Autowired
	private MisconductTypesMasterRepository misconductTypesMasterRepo;

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

	private static final Logger LOG = LoggerFactory.getLogger(MisconductReportsController.class);

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/misconductReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "misconduct", required = false) String misconduct,
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

		List<MisconductTypesMaster> miscType = null;
		List<MisconductTypesMaster> miscTypeAdmin = null;
		List<MisconductTypesMaster> miscTypeCriminal = null;
		if (StringUtils.isNotBlank(misconduct) && !misconduct.equalsIgnoreCase("null")) {
			if (misconduct.equalsIgnoreCase("Administrative")) {
				miscType = misconductTypesMasterRepo.findByIdIn(Arrays.asList(1L, 7L, 8L));
			}
			if (misconduct.equalsIgnoreCase("Criminal")) {
				miscType = misconductTypesMasterRepo.findByIdIn(Arrays.asList(2L, 3L, 4L, 6L));
			}
		} else {
			miscType = misconductTypesMasterRepo.findAll();
		}
		model.addAttribute("misconduct", misconduct);

		miscTypeAdmin = misconductTypesMasterRepo.findByIdIn(Arrays.asList(1L, 7L, 8L));
		miscTypeCriminal = misconductTypesMasterRepo.findByIdIn(Arrays.asList(2L, 3L, 4L, 6L));

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {

			List<CaseDetails> misconductCases = caseDetailsRepo
					.findByMisConductTypeInAndIdNotIn(misconductTypesMasterRepo.findAll(),caseId);

			model.addAttribute("totalCases", misconductCases.size());
			model.addAttribute("totalEmpCount",
					misconductCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			noticePages = caseDetailsRepo.findByMisConductTypeInAndIdNotIn(miscType, pageable,caseId);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findByMisConductTypeInAndIdNotIn(miscType, allPage,caseId);

			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> adminCaseList = caseDetailsRepo.findDistinctIdByMisConductTypeInAndIdNotIn(miscTypeAdmin,caseId);
			model.addAttribute("administrative", adminCaseList.size());
			model.addAttribute("empAdminCaseCount",
					adminCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> crimCaseList = caseDetailsRepo.findDistinctIdByMisConductTypeInAndIdNotIn(miscTypeCriminal,caseId);
			model.addAttribute("criminal", crimCaseList.size());
			model.addAttribute("empCrimCaseCount",
					crimCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			List<CaseDetails> misconductCases = caseDetailsRepo
					.findByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(misconductTypesMasterRepo.findAll(),org,caseId);

			model.addAttribute("totalCases", misconductCases.size());
			model.addAttribute("totalEmpCount",
					misconductCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			noticePages = caseDetailsRepo.findByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(miscType,org, pageable,caseId);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(miscType,org, allPage,caseId);

			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> adminCaseList = caseDetailsRepo.findDistinctIdByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(miscTypeAdmin,org,caseId);
			model.addAttribute("administrative", adminCaseList.size());
			model.addAttribute("empAdminCaseCount",
					adminCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> crimCaseList = caseDetailsRepo.findDistinctIdByMisConductTypeInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(miscTypeCriminal,org,caseId);
			model.addAttribute("criminal", crimCaseList.size());
			model.addAttribute("empCrimCaseCount",
					crimCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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

			List<CaseDetails> misconductCases = caseDetailsRepo.findByCurrentUserInAndMisConductTypeInAndIdNotIn(userList,
					misconductTypesMasterRepo.findAll(),caseId);
			model.addAttribute("totalCases", misconductCases.size());
			model.addAttribute("totalEmpCount",
					misconductCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			noticePages = caseDetailsRepo.findDistinctIdByCurrentUserInAndMisConductTypeInAndIdNotIn(userList, miscType,
					pageable,caseId);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByCurrentUserInAndMisConductTypeInAndIdNotIn(userList, miscType, allPage,caseId);

			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> adminCaseList = caseDetailsRepo.findByCurrentUserInAndMisConductTypeInAndIdNotIn(userList,
					miscTypeAdmin,caseId);
			model.addAttribute("administrative", adminCaseList.size());
			model.addAttribute("empAdminCaseCount",
					adminCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> crimCaseList = caseDetailsRepo.findByCurrentUserInAndMisConductTypeInAndIdNotIn(userList,
					miscTypeCriminal,caseId);
			model.addAttribute("criminal", crimCaseList.size());
			model.addAttribute("empCrimCaseCount",
					crimCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

		}

		else {

			List<CaseDetails> misconductCases = caseDetailsRepo.findByCurrentUserAndMisConductTypeInAndIdNotIn(currentUser,
					misconductTypesMasterRepo.findAll(),caseId);
			model.addAttribute("totalCases", misconductCases.size());
			model.addAttribute("totalEmpCount",
					misconductCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndMisConductTypeInAndIdNotIn(currentUser, miscType,
					pageable,caseId);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndMisConductTypeInAndIdNotIn(currentUser, miscType, allPage,caseId);

			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> adminCaseList = caseDetailsRepo.findByCurrentUserAndMisConductTypeInAndIdNotIn(currentUser,
					miscTypeAdmin,caseId);
			model.addAttribute("administrative", adminCaseList.size());
			model.addAttribute("empAdminCaseCount",
					adminCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> crimCaseList = caseDetailsRepo.findByCurrentUserAndMisConductTypeInAndIdNotIn(currentUser,
					miscTypeCriminal,caseId);
			model.addAttribute("criminal", crimCaseList.size());
			model.addAttribute("empCrimCaseCount",
					crimCaseList.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

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
		
		return "DeptInquiryViews/ReportsforMisconductWise.html";

	}

}
