package com.ehrms.deptenq.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.IProsecutionProposalDetailsRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class WithoutDEProsecutionController {
	
	@Autowired
	private IProsecutionProposalDetailsRepository susRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@Autowired
	private IGlobalOrgRepository orgRepo;
	
	@GetMapping("/prosecutionEmployeesWithoutDEReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "globalorgid", required = false) String globalorgdid,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		
		if(sortField == null) {
			sortField = "fileNo";
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
		
		if(globalorgdid != null && globalorgdid.equalsIgnoreCase("null")) {
			globalorgdid = "";
		}
		model.addAttribute("globalorgid", globalorgdid);
		GlobalOrg org = null;
		if(globalorgdid != null && !globalorgdid.equalsIgnoreCase("null") && !globalorgdid.equalsIgnoreCase("")) {
			org = orgRepo.findById(Long.valueOf(globalorgdid)).orElse(new GlobalOrg());
		}

		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);


		User currentUser = userService.getCurrentUserPojo();
		Page<ProsecutionProposalDetails> noticePages = null;
//		List<CaseDetails> caseList = null;
		Page<ProsecutionProposalDetails> allCases = null;
		model.addAttribute("localdatenow", LocalDate.now());

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			
			noticePages =  susRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgIdActiveAndCurrentUserPimsEmployeeGlobalOrgId(true,"",true,pageable,org);
			allCases = susRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgIdActiveAndCurrentUserPimsEmployeeGlobalOrgId(true,"",true,allPage,org);
			model.addAttribute("empCount", allCases.getContent().size());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
			org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			noticePages =  susRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(true,"",org,pageable);
			allCases = susRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(true,"",org,allPage);
			model.addAttribute("empCount", allCases.getContent().size());

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


			
			noticePages =  susRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(true,"",pageable,userList);
			allCases = susRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(true,"",allPage,userList);
			model.addAttribute("empCount", allCases.getContent().size());
		}

		else {
			

			
			noticePages =  susRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
			allCases = susRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",allPage,currentUser);
			model.addAttribute("empCount", allCases.getContent().size());
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

		return "DeptInquiryViews/ReportsforProsecutionEmployeeWithoutDe.html";

	}
	


}
