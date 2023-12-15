package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class SubDepartmentController {
	
	@Autowired
	private SubDepartmentRepository subRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CaseDetailsRepository caseDetailsRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/getsubdepartmentdata/{page}")
	public String getSubdepartmentData(Model model, @PathVariable("page") int page,
			@RequestParam(name = "search", required = false) String search,
			RedirectAttributes redirect) {
		SubDepartment subDepart = null;
		List<SubDepartment> subList = new ArrayList<>();
		if(search != null && !search.equalsIgnoreCase("null") && !search.equalsIgnoreCase("departmentadmin")) {
			subDepart = subRepository.findById(Long.valueOf(search)).orElse(new SubDepartment());
			subList.add(subDepart);
			model.addAttribute("search", search);
		} else {
			if(search != null && !search.equalsIgnoreCase("null") && search.equalsIgnoreCase("departmentadmin")) {
				model.addAttribute("search", "departmentadmin");
			} else {
				model.addAttribute("search", "");	
			}
		}
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {
			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id"));
		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id"));

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
		Page<CaseDetails> allCases = null;
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
//		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
//				.contains("ROLE_SUPERADMIN")) {
			if(search != null && !search.equalsIgnoreCase("null") && search.equalsIgnoreCase("departmentadmin")) {
				if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_SECRETARY")) {
					List<User> uu = userRepository.findByPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId());
					User uuu = null; 
					for(User uux : uu) {
						if(uux.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_DEPARTMENTADMIN")) {
							uuu = uux;
							break;
						}
					}
					noticePages = caseDetailsRepo.findByCurrentUserAndIdNotIn(uuu,caseId, pageable);
					allCases =caseDetailsRepo.findByCurrentUserAndIdNotIn(uuu,caseId, allPage);
				} else {
					noticePages = caseDetailsRepo.findByCurrentUserAndIdNotIn(currentUser,caseId, pageable);
					allCases =caseDetailsRepo.findByCurrentUserAndIdNotIn(currentUser,caseId, allPage);
				}
			} else {
				noticePages = caseDetailsRepo.findByCurrentUserSubDepartmentInAndIdNotIn(subList,caseId, pageable);
				allCases =caseDetailsRepo.findByCurrentUserSubDepartmentInAndIdNotIn(subList,caseId, allPage);
			}
					
			
//		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
//				.contains("ROLE_SECRETARY")) {
//			noticePages = caseDetailsRepo.findByCurrentUserSubDepartmentInAndIdNotIn(subList,caseIdx, pageable);
//			allCases = caseDetailsRepo.findByCurrentUserSubDepartmentInAndIdNotIn(subList,caseIdx, allPage);
//			
//		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
//				.contains("ROLE_DEPARTMENTADMIN")) {
//			noticePages = caseDetailsRepo.findByCurrentUserSubDepartmentInAndIdNotIn(subList,caseIdx, pageable);
//			allCases = caseDetailsRepo.findByCurrentUserSubDepartmentInAndIdNotIn(subList,caseIdx, allPage);
//			
//		}
		model.addAttribute("caseDetailsAllRecordsreport", allCases);
		model.addAttribute("empCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());
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
		
		return "DeptInquiryViews/ReportsforSubdepartment";
	}

}
