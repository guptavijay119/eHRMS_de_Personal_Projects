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

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.modelsDto.DashboardData;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class SuspendedCaseEmployeeReportController {

	@Autowired
	private IEmployeeDetailsRepository empRepo;
	
//	@Autowired
//	private SuspensionDetailsRepository susRepo;
	

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;
	
	@Autowired
	private IGlobalOrgRepository globalRepo;

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;

	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
//	@Autowired
//	private IReInstatedRepository reintRepo;

//	private static final Logger LOG = LoggerFactory.getLogger(RetiredEmployeesReportsController.class);

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/suspendedEmployeesReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

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
		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);


		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
//		List<CaseDetails> caseList = null;
		Page<CaseDetails> allCases = null;
		model.addAttribute("localdatenow", LocalDate.now());

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {

			
			List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndSuspensionDetailsCurrentStatus(true,false,true,"Still Under Suspension");

			
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList,pageable);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList,allPage);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			model.addAttribute("empCount", empList.size());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {

			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndSuspensionDetailsCurrentStatus(true,false,org,true,org,"Still Under Suspension");

			
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,org,pageable);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,org,allPage);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
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


			
			List<EmployeeDetails> empList = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndSuspensionDetailsCurrentStatus(true,false,userList,true,userList,"Still Under Suspension");
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserIn(empList,userList,pageable);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserIn(empList,userList,allPage);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			model.addAttribute("empCount", empList.size());

		}

		else {
			

			
			List<EmployeeDetails> empList = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndSuspensionDetailsCurrentStatus(true,false,currentUser,true,currentUser,"Still Under Suspension");
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUser(empList,currentUser,pageable);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUser(empList,currentUser,allPage);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
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

		return "DeptInquiryViews/ReportsforSuspendedEmployee.html";

	}
	
	
	@GetMapping("/suspendedEmployeesDeptReportsController/{page}")
	public String suspendedEmployeesDeptController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "globalorgid", required = false) String globalorgdid,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {


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
		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);


		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
//		List<CaseDetails> caseList = null;
		Page<CaseDetails> allCases = null;
		model.addAttribute("localdatenow", LocalDate.now());
		if(globalorgdid != null && globalorgdid.equalsIgnoreCase("null")) {
			globalorgdid = "";
		}
		model.addAttribute("globalorgid", globalorgdid);
		GlobalOrg org = null;
		if(globalorgdid != null && !globalorgdid.equalsIgnoreCase("null") && !globalorgdid.equalsIgnoreCase("")) {
			org = globalRepo.findById(Long.valueOf(globalorgdid)).orElse(new GlobalOrg());
		}

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			
			List<DashboardData> dataList = new ArrayList<>();
			List<GlobalOrg> orgList = globalRepo.findByActive(true);			
			orgList.stream().forEach(s->{
				DashboardData data = getdashBoradData(s);
				dataList.add(data);
			});
			
			if(org != null) {

				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndSuspensionDetailsCurrentStatus(true,false,org,true,org,"Still Under Suspension");
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,org,pageable);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,org,allPage);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				model.addAttribute("empCount", empList.size());
			} else {

				
				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndSuspensionDetailsCurrentStatus(true,false,true,"Still Under Suspension");

				
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList,pageable);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList,allPage);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				model.addAttribute("empCount", empList.size());
			}
			
			
			
			model.addAttribute("dashboarddatalist", dataList);

		}
		
		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {

			org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndSuspensionDetailsCurrentStatus(true,false,org,true,org,"Still Under Suspension");

			
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,org,pageable);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,org,allPage);
			model.addAttribute("empCount", empList.size());
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});

		}

		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")|| currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARY")) {
			List<DashboardData> dataList = new ArrayList<>();
			GlobalOrg og = currentUser.getPimsEmployee().getGlobalOrgId();
	
			DashboardData data = getdashBoradData(og);
			dataList.add(data);
			
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

			if(org != null) {

				
				List<EmployeeDetails> empList = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndSuspensionDetailsCurrentStatus(true,false,org,userList,true,org,userList,"Still Under Suspension");
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(empList,org,pageable,userList);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserIn(empList,org,allPage,userList);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				model.addAttribute("empCount", empList.size());
			} else {

				
				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndSuspensionDetailsCurrentStatus(true,false,userList,true,userList,"Still Under Suspension");
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserIn(empList,userList,pageable);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserIn(empList,userList,allPage);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				model.addAttribute("empCount", empList.size());
			}

		}

		else {
			List<DashboardData> dataList = new ArrayList<>();
			GlobalOrg og = currentUser.getPimsEmployee().getGlobalOrgId();
	
			DashboardData data = getdashBoradData(og);
			dataList.add(data);
			
			if(org != null) {

				
				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndSuspensionDetailsCurrentStatus(true,false,org,currentUser,true,org,currentUser,"Still Under Suspension");
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(empList,org,pageable,currentUser);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				
				allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUser(empList,org,allPage,currentUser);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				model.addAttribute("empCount", empList.size());
			} else {

				
				List<EmployeeDetails> empList =  empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndSuspensionDetailsCurrentStatus(true,false,currentUser,true,currentUser,"Still Under Suspension");
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUser(empList,currentUser,pageable);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUser(empList,currentUser,allPage);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				model.addAttribute("empCount", empList.size());
			}
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

		return "DeptInquiryViews/ReportsforSuspendeDept.html";

	}
	
	
	private DashboardData getdashBoradData(GlobalOrg s) {
		DashboardData data = new DashboardData();
		

		
		List<EmployeeDetails> empList = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndSuspensionDetailsCurrentStatus(true,false,s,true,s,"Still Under Suspension");
		List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empList,s);
		data.setOrgid(s.getId());
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(caseList.size());
		data.setTotalemployee(empList.size());
		
		
		return data;
	}

}
