package com.ehrms.deptenq.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class ReportsGroupA {
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ReportsGroupA.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CaseDetailsRepository caseDetailsRepo;
	
	@Autowired
	private IEmployeeDetailsRepository employeedetailsrepo;
	
	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@Autowired
	private UserRepository userRepo;

	
	
	/* Added on 13-10-2022 */
	@GetMapping("/getTotalCaseGroupA/{page}")
	public String reportsforAllFields(Model model, @PathVariable("page") int page,
			@RequestParam(name = "rule", required = false) String rule,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "misconduct", required = false) String misconduct,
			@RequestParam(name = "viewreport", required = false) String viewreport,
			@RequestParam(name = "fromdate", required = false) String fromdate,
			@RequestParam(name = "todate", required = false) String todate,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		LOG.info("hi this is the reports  Details Records For search  page.......");

		

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

		
		///Add code here////
		
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(1L);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageable);
			Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageableall);
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {

		} else {

		}
		
		//// -------------------------------///-----------------

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
	
		return "DeptInquiryViews/totalcasegroupA";

	}

}
