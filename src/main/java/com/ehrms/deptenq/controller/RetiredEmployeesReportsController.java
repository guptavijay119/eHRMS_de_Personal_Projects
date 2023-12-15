package com.ehrms.deptenq.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class RetiredEmployeesReportsController {

	@Autowired
	private IEmployeeDetailsRepository empRepo;
	
	@Autowired
	private FinalOutcomeRepository finalOutRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;

	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	
	/* written code on autowired on 10/11/2023 by vijay */
	@Autowired
	private  DepartmentConstants departmentconstants;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LocaleResolver loc;
	
	@Autowired
	private MessageSource messageSource;
	


//	private static final Logger LOG = LoggerFactory.getLogger(RetiredEmployeesReportsController.class);

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/retiredEmployeesReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

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


		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
//		List<CaseDetails> caseList = null;
		Page<CaseDetails> allCases = null;
		model.addAttribute("localdatenow", LocalDate.now());

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			
			List<EmployeeDetails> empList = empRepo.findBySuperannuationDateLessThanAndCasedetailsIdNotIn(LocalDate.now(),caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,pageable,caseId);
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,allPage,caseId);
			model.addAttribute("empCount", empList.size());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			List<EmployeeDetails> empList = empRepo.findBySuperannuationDateLessThanAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(LocalDate.now(),org,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,pageable,caseId);
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,allPage,caseId);
			model.addAttribute("empCount", empList.size());

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


			List<EmployeeDetails> empList = empRepo.findBySuperannuationDateLessThanAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(LocalDate.now(),userList,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,userList,pageable,caseId);
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,userList,allPage,caseId);
			model.addAttribute("empCount", empList.size());

		}

		else {
			
			List<EmployeeDetails> empList = empRepo.findBySuperannuationDateLessThanAndCasedetailsCurrentUserAndCasedetailsIdNotIn(LocalDate.now(),currentUser,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,currentUser,pageable,caseId);
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,currentUser,allPage,caseId);
			model.addAttribute("empCount", empList.size());
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

		
		return "DeptInquiryViews/ReportsforRetiredEmployees.html";

	}

}
