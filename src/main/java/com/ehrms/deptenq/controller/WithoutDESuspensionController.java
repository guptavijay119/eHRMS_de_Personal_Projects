package com.ehrms.deptenq.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
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
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.SuspensionDetailsRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class WithoutDESuspensionController {
	
	@Autowired
	private SuspensionDetailsRepository susRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@Autowired
	private IGlobalOrgRepository orgRepo;
	
	@GetMapping("/suspendedEmployeesWithoutDEReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection,
			@RequestParam(name = "globalorgid", required = false) String globalorgdid) {
		
		
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

		Pageable pageable = PageRequest.of(page - 1, 10, sort);
		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);

		if(globalorgdid != null && globalorgdid.equalsIgnoreCase("null")) {
			globalorgdid = "";
		}
		model.addAttribute("globalorgid", globalorgdid);
		GlobalOrg org = null;
		if(globalorgdid != null && !globalorgdid.equalsIgnoreCase("null") && !globalorgdid.equalsIgnoreCase("")) {
			org = orgRepo.findById(Long.valueOf(globalorgdid)).orElse(new GlobalOrg());
		}
		User currentUser = userService.getCurrentUserPojo();
		Page<SuspensionDetails> noticePages = null;
//		List<CaseDetails> caseList = null;
		Page<SuspensionDetails> allCases = null;
		model.addAttribute("localdatenow", LocalDate.now());

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if(org != null) {
				noticePages =  susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndGlobalorgActiveAndCurrentUserPimsEmployeeGlobalOrgId(true,"",true,pageable,org);
				allCases = susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndGlobalorgActiveAndCurrentUserPimsEmployeeGlobalOrgId(true,"",true,allPage,org);
			} else {
				noticePages =  susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndGlobalorgActive(true,"",true,pageable);
				allCases = susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndGlobalorgActive(true,"",true,allPage);
			}
			model.addAttribute("empCount", allCases.getContent().size());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
			org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			noticePages =  susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(true,"",org,pageable);
			allCases = susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(true,"",org,allPage);
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
			
			noticePages =  susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(true,"",pageable,userList);
			allCases = susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(true,"",allPage,userList);
			model.addAttribute("empCount", allCases.getContent().size());

		}

		else {
			
			
			noticePages =  susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
			allCases = susRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",allPage,currentUser);
			model.addAttribute("empCount", allCases.getContent().size());
		}
		
		noticePages.getContent().parallelStream().forEach(s->{
			if(s.getSusUnderRule() != null && s.getSusUnderRule().getId()==2L && s.getDateofActualSuspension() != null) {
				LocalDate dd = s.getDateofActualSuspension();
				long days = ChronoUnit.DAYS.between(dd, LocalDate.now());
				if(days<90 && days>0) {
					s.setRemainingdays(90-days);
				} else {
					s.setRemainingdays(0);
				}
			}
			if(s.getSusUnderRule() != null && s.getSusUnderRule().getId()==1L && s.getDateofDeemedSuspension() != null) {
				LocalDate dd = s.getDateofDeemedSuspension();
				long days = ChronoUnit.DAYS.between(dd, LocalDate.now());
				if(days<90 && days>0) {
					s.setRemainingdays(90-days);
				} else {
					s.setRemainingdays(0);
				}
			}
		});

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

		return "DeptInquiryViews/ReportsforSuspendedEmployeeWithoutDe.html";

	}
	
	
//	@GetMapping("/suspendedEmployeesDeptReportsController/{page}")
//	public String suspendedEmployeesDeptController(Model model, @PathVariable("page") int page,
//			@RequestParam(name = "globalorgid", required = false) String globalorgdid,
//			@RequestParam(name = "sort-field", required = false) String sortField,
//			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
//
//
//
//		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
//				: Sort.by(sortField).descending();
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDirection);
//		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
//
//		// pagination Writting coding
//		int index = 1;
//
//		if (page != 0) {
//			index = (page * 10) - (10 - 1);
//			model.addAttribute("pageIndex", index);
//		}
//
//		if (page < 0) {
//			page = 1;
//		}
//
//		Pageable pageable = PageRequest.of(page - 1, 10, sort);
//		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//
//
//		User currentUser = userService.getCurrentUserPojo();
//		Page<CaseDetails> noticePages = null;
////		List<CaseDetails> caseList = null;
//		Page<CaseDetails> allCases = null;
//		model.addAttribute("localdatenow", LocalDate.now());
//		
//		model.addAttribute("globalorgdid", globalorgdid);
//		GlobalOrg org = null;
//		if(globalorgdid != null && !globalorgdid.equalsIgnoreCase("null")) {
//			org = globalRepo.findByGlobalOrgName(globalorgdid);
//		}
//
//		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
//				.contains("ROLE_SUPERADMIN")) {
//			
//			List<DashboardData> dataList = new ArrayList<>();
//			List<GlobalOrg> orgList = globalRepo.findByActive(true);			
//			orgList.stream().forEach(s->{
//				DashboardData data = getdashBoradData(s);
//				dataList.add(data);
//			});
//			
//			if(org != null) {
//
//				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgId(true,false,org,true,org);
//				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,org,pageable);
//				noticePages.getContent().stream().forEach(s->{
//					List<EmployeeDetails> empp = new ArrayList<>();
//					empList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setEmployeeList(empp);
//				});
//				
//				allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,org,allPage);
//				model.addAttribute("empCount", empList.size());
//			} else {
//
//				
//				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNull(true,false,true);
//
//				
//				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList,pageable);
//				noticePages.getContent().stream().forEach(s->{
//					List<EmployeeDetails> empp = new ArrayList<>();
//					empList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setEmployeeList(empp);
//				});
//				
//				allCases = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList,allPage);
//				model.addAttribute("empCount", empList.size());
//			}
//			
//			
//			
//			model.addAttribute("dashboarddatalist", dataList);
//
//		}
//
//		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
//				.contains("ROLE_DEPARTMENTADMIN")) {
//			List<DashboardData> dataList = new ArrayList<>();
//			GlobalOrg og = currentUser.getPimsEmployee().getGlobalOrgId();
//	
//			DashboardData data = getdashBoradData(og);
//			dataList.add(data);
//			
//			List<User> userList = userRepo.findBySubDepartmentIn(
//					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
//			userList.add(currentUser);
//
//			if(org != null) {
//
//				
//				List<EmployeeDetails> empList = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(true,false,org,userList,true,org,userList);
//				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(empList,org,pageable,userList);
//				noticePages.getContent().stream().forEach(s->{
//					List<EmployeeDetails> empp = new ArrayList<>();
//					empList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setEmployeeList(empp);
//				});
//				
//				allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(empList,org,allPage,userList);
//				model.addAttribute("empCount", empList.size());
//			} else {
//
//				
//				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserIn(true,false,userList,true,userList);
//				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserIn(empList,userList,pageable);
//				noticePages.getContent().stream().forEach(s->{
//					List<EmployeeDetails> empp = new ArrayList<>();
//					empList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setEmployeeList(empp);
//				});
//				
//				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserIn(empList,userList,allPage);
//				model.addAttribute("empCount", empList.size());
//			}
//
//		}
//
//		else {
//			List<DashboardData> dataList = new ArrayList<>();
//			GlobalOrg og = currentUser.getPimsEmployee().getGlobalOrgId();
//	
//			DashboardData data = getdashBoradData(og);
//			dataList.add(data);
//			
//			if(org != null) {
//
//				
//				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(true,false,org,currentUser,true,org,currentUser);
//				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(empList,org,pageable,currentUser);
//				noticePages.getContent().stream().forEach(s->{
//					List<EmployeeDetails> empp = new ArrayList<>();
//					empList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setEmployeeList(empp);
//				});
//				
//				
//				allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(empList,org,allPage,currentUser);
//				model.addAttribute("empCount", empList.size());
//			} else {
//
//				
//				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUser(true,false,currentUser,true,currentUser);
//				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUser(empList,currentUser,pageable);
//				noticePages.getContent().stream().forEach(s->{
//					List<EmployeeDetails> empp = new ArrayList<>();
//					empList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setEmployeeList(empp);
//				});
//				
//				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUser(empList,currentUser,allPage);
//				model.addAttribute("empCount", empList.size());
//			}
//		}
//
//		model.addAttribute("caseDetailsAllRecordsreport", allCases);
//		model.addAttribute("caseDetailsAllRecords", noticePages);
//		model.addAttribute("currentPage", page);
//		int begin = Math.max(1, page - 5);
//		if (page >= 5) {
//			begin = page;
//		}
//		int end = Math.min(begin + 4, noticePages.getTotalPages());
//		model.addAttribute("begin", begin);
//		model.addAttribute("end", end);
//		model.addAttribute("totalPages", noticePages.getTotalPages());
//		model.addAttribute("totalItems", noticePages.getTotalElements());
//
//		return "DeptInquiryViews/ReportsforSuspendeDept.html";
//
//	}

}
