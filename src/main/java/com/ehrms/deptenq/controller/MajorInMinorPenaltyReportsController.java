package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
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
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class MajorInMinorPenaltyReportsController {

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

	private static final Logger LOG = LoggerFactory.getLogger(MajorInMinorPenaltyReportsController.class);

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/majorminorReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

		LOG.info("misconduct applicable reports controller....");

		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}
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


		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
		Page<CaseDetails> allCases = null;

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {

			List<FinalOutcomeDetails> finaloutlist = finalOutRepo
					.findByCasedetailsRuleApplicableIdNotInAndWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIdIn(
							Arrays.asList(2L), true, Arrays.asList(1L));
			noticePages = caseDetailsRepo.findDistinctIdByFinaloutListIn(finaloutlist, pageable);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByFinaloutListIn(finaloutlist, allPage);
			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {

			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			List<FinalOutcomeDetails> finaloutlist = finalOutRepo
					.findByCasedetailsRuleApplicableIdNotInAndWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIdInAndCurrentUserPimsEmployeeGlobalOrgId(
							Arrays.asList(2L), true, Arrays.asList(1L),org);
			noticePages = caseDetailsRepo.findDistinctIdByFinaloutListInAndCurrentUserPimsEmployeeGlobalOrgId(finaloutlist,org, pageable);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByFinaloutListInAndCurrentUserPimsEmployeeGlobalOrgId(finaloutlist,org, allPage);
			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

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
			
			List<FinalOutcomeDetails> finaloutlist = finalOutRepo
					.findByCasedetailsRuleApplicableIdNotInAndWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIdIn(
							Arrays.asList(2L), true, Arrays.asList(1L));
			noticePages = caseDetailsRepo.findDistinctIdByFinaloutListInAndCurrentUserIn(finaloutlist,userList, pageable);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByFinaloutListInAndCurrentUserIn(finaloutlist,userList, allPage);
			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

		}

		else {
			
			List<FinalOutcomeDetails> finaloutlist = finalOutRepo
					.findByCasedetailsRuleApplicableIdNotInAndWhetherCaseFinallyDecidedAndTypeOfPenaltyInflictedIdIn(
							Arrays.asList(2L), true, Arrays.asList(1L));
			noticePages = caseDetailsRepo.findDistinctIdByFinaloutListInAndCurrentUser(finaloutlist,currentUser, pageable);
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByFinaloutListInAndCurrentUser(finaloutlist,currentUser, allPage);
			model.addAttribute("empCount", allCases.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

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

		return "DeptInquiryViews/ReportsforMajorinMinorPenalty.html";

	}

}
