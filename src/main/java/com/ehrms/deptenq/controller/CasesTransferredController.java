package com.ehrms.deptenq.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("/departmentalEnquiry/")
public class CasesTransferredController {
	private static final Logger LOG = LoggerFactory.getLogger(CasesTransferredController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CaseDetailsRepository caseDetailsRepo;
	
	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@GetMapping("/casestransferred/{page}")
	public String caseDetailsListRecords(Model model, @PathVariable("page") int page,
			@RequestParam(name = "caseno", required = false) String caseno,
			
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		LOG.info("hi this is the case details records page.... ");

		if (sortDirection == null) {
			sortDirection = "ASC";
		}
		if (sortField == null) {
			sortField = "caseNo";
		}

		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDirection);
		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

//		if(caseno != null) {
//			model.addAttribute("caseDetailsAllRecords", caseDetailsRepo.);
//		} else {
//			model.addAttribute("caseDetailsAllRecords", caseDetailsRepo.findAll());
//		}

		// pagination Writting coding
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {
			page = 1;
		}

//		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if (caseno != null) {
				model.addAttribute("caseno", caseno);
				noticePages = caseDetailsRepo.findByCaseNoContainingIgnoreCaseAndActiveForTransfer(caseno,true, pageable);
			} else {
				noticePages = caseDetailsRepo.findByActiveForTransfer(true,pageable);
				model.addAttribute("caseno", "");
			}
		} // if close
		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
			List<SubDepartment> subDepartmentList = subDepartmentRepo
					.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
//			List<User> userList = userRepo.findBySubDepartmentIn(subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
//			userList.add(currentUser);
			noticePages = caseDetailsRepo
					.findByCurrentUserSubDepartmentInOrCurrentUserPimsEmployeeGlobalOrgIdAndActiveForTransferAndCurrentUserSubDepartmentIsNull(
							subDepartmentList, currentUser.getPimsEmployee().getGlobalOrgId(),true, pageable);
			model.addAttribute("caseno", "");
		} else {
			if (caseno != null && StringUtils.isNotBlank(caseno)) {
				model.addAttribute("caseno", caseno);
				noticePages = caseDetailsRepo.findByCurrentUserAndCaseNoContainingIgnoreCaseAndActiveForTransfer(
						currentUser, caseno,true, pageable);
			} else {
				noticePages = caseDetailsRepo.findByCurrentUserAndActiveForTransfer(
						currentUser,true, pageable);
				model.addAttribute("caseno", "");
			}
		} // else close

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
		model.addAttribute("noticelist", noticePages.getContent());

		return "DeptInquiryViews/Reportsforcasestransferred";

	}

}
