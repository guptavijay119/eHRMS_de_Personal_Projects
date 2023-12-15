package com.ehrms.deptenq.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.NoDECertificate;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.NoDeCertificateRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class AbeyanceReportsController {



	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private SubDepartmentRepository subDepartmentRepo;


	@Autowired
	private CaseDetailsRepository caseDetailsRepo;
	
	@Autowired
	private IEmployeeDetailsRepository empRepo;
	
	@Autowired
	private NoDeCertificateRepository  nodecertificaterepository;
	
	/*  written on 19/10/2023  for pagination to vigialance certificate  by vijay */
    private static final String CURRENTPAGE = "currentPage";
	
	private static final String BEGIN = "begin";
	
	private static final String TOTALPAGES = "totalPages";
	
	private static final String CURRENTPAGECOUNT = "currentPageCount";
	
	
	private static final String TOTALITEMS = "totalItems";
	



	private static final Logger LOG = LoggerFactory.getLogger(AbeyanceReportsController.class);

	
	

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/abeyannceReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

		LOG.info("Gender applicable reports controller....");
		
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


		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			
			List<EmployeeDetails> finalList = empRepo.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCasedetailsIdNotIn(true,caseId);
			noticePages  = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(finalList,pageable,caseId);			
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				finalList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases  = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(finalList,allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				finalList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			model.addAttribute("totalEmpCount", finalList.size());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
//			GlobalOrg orgx = currentUser.getPimsEmployee().getGlobalOrgId();
//			long org = orgx.getId();
//			List<EmployeeDetails> finalList = empRepo.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(true,org,caseId);
//			noticePages  = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(finalList,pageable,org,caseId);			
//			noticePages.getContent().stream().forEach(s->{
//				List<EmployeeDetails> empp = new ArrayList<>();
//				finalList.stream().forEach(ss->{
//					if(ss.getCasedetails().getId()==s.getId()) {
//						empp.add(ss);
//					}	
//				});
//				s.setEmployeeList(empp);
//			});
//			allCases  = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(finalList,allPage,org,caseId);
//			allCases.getContent().stream().forEach(s->{
//				List<EmployeeDetails> empp = new ArrayList<>();
//				finalList.stream().forEach(ss->{
//					if(ss.getCasedetails().getId()==s.getId()) {
//						empp.add(ss);
//					}	
//				});
//				s.setEmployeeList(empp);
//			});
//			model.addAttribute("totalEmpCount", finalList.size());

		} 
		
		
		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN") || currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			
			
			List<EmployeeDetails> finalList = empRepo.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserInAndCasedetailsIdNotIn(true,userList,caseId);
			noticePages  = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(finalList,pageable,userList,caseId);			
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				finalList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases  = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(finalList,allPage,userList,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				finalList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			model.addAttribute("totalEmpCount", finalList.size());
			
		}

		else {
			
			
			List<EmployeeDetails> finalList = empRepo.findByCasedetailsIsNotNullAndFinalOutcomeDetailsWhethercasekeptinabeyanceAndCurrentUserAndCasedetailsIdNotIn(true,currentUser,caseId);
			noticePages  = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(finalList,pageable,currentUser,caseId);			
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				finalList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases  = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(finalList,allPage,currentUser,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				finalList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			model.addAttribute("totalEmpCount", finalList.size());
			
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
		
		return "DeptInquiryViews/ReportsforAbeyanceCase.html";

	}	
	
	
	
	
	/*
	 * @Author vijay gupta writtrn for visialace certificate methods reports
	 *  Date :: 18/10/2023
	 *  	
	 *  /* code ADDED  on 30/10/2023 by vijay for search by employee name */
	
	
	@GetMapping("/nodeCertificate/{page}")
	public String nodeCertificate(Model model, @PathVariable("page") int page,
			@RequestParam(name = "employeename", required = false) String employeename,
			@RequestParam(name = "viewreport", required = false) String viewreport,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection,
			@RequestParam(name = "filter", required = false) String filter) {

		LOG.info("nodeCertificate....");
			User currentUser = userService.getCurrentUserPojo();
			Page<NoDECertificate> nodecertificate = null;
			
			if (sortDirection == null) {
				sortDirection = "ASC";
			}
			if (sortField == null) {
				sortField = "caseNo";
			}
			
//			if(employeename != null && !employeename.equalsIgnoreCase("null") && StringUtils.isNotBlank(employeename)) {
//				//nodecertificate = nodecertificaterepository.findByIdIn(Arrays.asList(Long.valueOf(employeename)));
//				nodecertificate = nodecertificaterepository.findByEmployeeName(employeename,p );
//			} else {
//				nodecertificate = nodecertificaterepository.findByIdIn(Arrays.asList(1L,2L,3L,4L));
//			}
//			//model.addAttribute("group", nodecertificate);
//			String empname="null";
			String empname=employeename;
			
			if(employeename != null && !employeename.equalsIgnoreCase("null") && StringUtils.isNotBlank(employeename)) {
				
				empname=employeename;
				
			}
			model.addAttribute("employeename", empname);
			
		
			
			
			
			

			final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()	: Sort.by(sortField).descending();
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDirection);
			model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
			
	


		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_SUPERADMIN")) {
				
			
			//model.addAttribute("nodecertificaterepolist", nodecertificaterepository.findAll(Sort.by("id")));
			
			
			
			if (page < 0) {
				page = 1;
			}
			
			PageRequest pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
			Page<NoDECertificate> noticePages = null;
			
			if (StringUtils.isNotBlank(empname)  && !employeename.equalsIgnoreCase("null")) {
				model.addAttribute("caseno", empname);
				//noticePages = caseDetailsRepo.findByCaseNoContainingIgnoreCaseAndIdNotIn(employeename,caseIdList, pageable);
				noticePages = nodecertificaterepository.findByEmployeeName(empname,pageable );
		
			} 
			
			else {
				noticePages = nodecertificaterepository.findByOrderById(pageable);
						
			}
			
			
		
			
			if (filter != null && filter.equalsIgnoreCase("ALL")) {
				noticePages = nodecertificaterepository.findAll(pageable);
			}
			
			
			
			
			
			



			model.addAttribute("nodecertificaterepolist", noticePages.getContent());
			model.addAttribute(CURRENTPAGE, page);
			int begin = Math.max(1, page - 5);
			int end = Math.min(begin + 4, noticePages.getTotalPages());
			model.addAttribute(BEGIN, begin);
			model.addAttribute("end", end);
			model.addAttribute(TOTALPAGES, noticePages.getTotalPages());
			model.addAttribute(TOTALITEMS, noticePages.getTotalElements());

			
			

		}
		
		
		
		
		
		  else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()) .contains("ROLE_SECRETARYOLD")) {
			  
			  LOG.info("this is for ROLE_SECRETARYOLD");
		  }
		 
		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_DEPARTMENTADMIN") || 
				currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_SECRETARY")) 
		{
			 LOG.info("this is for ROLE_DEPARTMENTADMIN or ROLE_SECRETARY ");
		}

		else {
			LOG.info("this is for Default if and else if  condition got failed than  else  block is going to execute. ");
		}
		
				return "DeptInquiryViews/VigialanceCertificateReports.html";

	}	
	
}

