package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.modelsDto.DashboardData;
import com.ehrms.deptenq.modelsDto.GroupAData;
import com.ehrms.deptenq.modelsDto.GroupBData;
import com.ehrms.deptenq.modelsDto.GroupCData;
import com.ehrms.deptenq.modelsDto.GroupDData;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.Service_GroupRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class GroupWiseReportsController {

	@Autowired
	private IEmployeeDetailsRepository empRepo;
	
	@Autowired
	private Service_GroupRepository groupRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;
	
	@Autowired
	private IGlobalOrgRepository globalRepo;

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;
	
	@Autowired
	private FinalOutcomeRepository finalRepo;

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
	

//	private static final Logger LOG = LoggerFactory.getLogger(RetiredEmployeesReportsController.class);

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/groupWiseReportAllController/{page}")
	public String ruleApplicableReportsControllerAll(Model model, @PathVariable("page") int page,
			@RequestParam(name = "group", required = false) String group,
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
		Page<CaseDetails> allCases = null;
		List<Service_Group> ggList = null;
		if(group != null && !group.equalsIgnoreCase("null") && StringUtils.isNotBlank(group)) {
			ggList = groupRepo.findByIdIn(Arrays.asList(Long.valueOf(group)));
		} else {
			ggList = groupRepo.findByIdIn(Arrays.asList(1L,2L,3L,4L));
		}
		model.addAttribute("group", group);

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			
			List<EmployeeDetails> empList = empRepo.findByServicegroupIdInAndCasedetailsIdNotIn(Arrays.asList(1L,2L,3L,4L),caseId);
			List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId);
			model.addAttribute("totalEmployee", empList.size());
			model.addAttribute("totalCases", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsIdNotIn(Arrays.asList(1L),caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId);
			model.addAttribute("totalEmployeeA",  empList.size());
			model.addAttribute("totalCasesA", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsIdNotIn(Arrays.asList(2L),caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId);
			model.addAttribute("totalEmployeeB",  empList.size());
			model.addAttribute("totalCasesB", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsIdNotIn(Arrays.asList(3L),caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId);
			model.addAttribute("totalEmployeeC",  empList.size());
			model.addAttribute("totalCasesC", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsIdNotIn(Arrays.asList(4L),caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId);
			model.addAttribute("totalEmployeeD",  empList.size());
			model.addAttribute("totalCasesD", caseList.size());
			
			
			List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCasedetailsIdNotIn(ggList,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil, pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil, allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("totalEmpCount", empListFil.size());
			


		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			List<EmployeeDetails> empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(1L,2L,3L,4L),org,caseId);
			List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployee", empList.size());
			model.addAttribute("totalCases", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(1L),org,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployeeA",  empList.size());
			model.addAttribute("totalCasesA", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(2L),org,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployeeB",  empList.size());
			model.addAttribute("totalCasesB", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(3L),org,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployeeC",  empList.size());
			model.addAttribute("totalCasesC", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(4L),org,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployeeD",  empList.size());
			model.addAttribute("totalCasesD", caseList.size());
			
			
			List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(ggList,org,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListFil,org, pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListFil,org, allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("totalEmpCount", empListFil.size());
			


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
			
			List<EmployeeDetails> empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(Arrays.asList(1L,2L,3L,4L),userList,caseId);
			List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,userList,caseId);
			model.addAttribute("totalEmployee", empList.size());
			model.addAttribute("totalCases", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(Arrays.asList(1L),userList,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,userList,caseId);
			model.addAttribute("totalEmployeeA",  empList.size());
			model.addAttribute("totalCasesA", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(Arrays.asList(2L),userList,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,userList,caseId);
			model.addAttribute("totalEmployeeB",  empList.size());
			model.addAttribute("totalCasesB", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(Arrays.asList(3L),userList,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,userList,caseId);
			model.addAttribute("totalEmployeeC",  empList.size());
			model.addAttribute("totalCasesC", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(Arrays.asList(4L),userList,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,userList,caseId);
			model.addAttribute("totalEmployeeD",  empList.size());
			model.addAttribute("totalCasesD", caseList.size());
			
			
			List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(ggList,userList,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListFil,userList, pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil, allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("totalEmpCount", empListFil.size());

		}

		else {
			
			List<EmployeeDetails> empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(Arrays.asList(1L,2L,3L,4L),currentUser,caseId);
			List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,currentUser,caseId);
			model.addAttribute("totalEmployee", empList.size());
			model.addAttribute("totalCases", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(Arrays.asList(1L),currentUser,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,currentUser,caseId);
			model.addAttribute("totalEmployeeA",  empList.size());
			model.addAttribute("totalCasesA", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(Arrays.asList(2L),currentUser,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,currentUser,caseId);
			model.addAttribute("totalEmployeeB",  empList.size());
			model.addAttribute("totalCasesB", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(Arrays.asList(3L),currentUser,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,currentUser,caseId);
			model.addAttribute("totalEmployeeC",  empList.size());
			model.addAttribute("totalCasesC", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(Arrays.asList(4L),currentUser,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,currentUser,caseId);
			model.addAttribute("totalEmployeeD",  empList.size());
			model.addAttribute("totalCasesD", caseList.size());
			
			
			List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(ggList,currentUser,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListFil,currentUser, pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil, allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("totalEmpCount", empListFil.size());
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
		
		model.addAttribute("grpa",messageSource.getMessage("deptenq.grpa.label", null, loc.resolveLocale(request)));
		model.addAttribute("grpb",messageSource.getMessage("deptenq.grpb.label", null, loc.resolveLocale(request)));
		model.addAttribute("grpc",messageSource.getMessage("deptenq.grpc.label", null, loc.resolveLocale(request)));
		model.addAttribute("grpd",messageSource.getMessage("deptenq.grpd.label", null, loc.resolveLocale(request)));
		
		return "DeptInquiryViews/ReportsforGroupWiseAll.html";

	}
	
	
	@GetMapping("/groupWiseReportDepartmentController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "globalorgdid", required = false) String globalorgdid,
			@RequestParam(name = "group", required = false) String group,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		
		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}


		if(StringUtils.isBlank(sortField)) {
			sortField = "caseNo";
		}
		if(StringUtils.isBlank(sortDirection)) {
			sortDirection = "asc";
		}
		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDirection);
		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
		
		List<CaseDetails> finalOut = caseDetailsRepo.findAll();		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		final Set<Long> caseId = caseIdx;

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
		Page<CaseDetails> allCases = null;
		List<Service_Group> ggList = null;
		if(group != null && !group.equalsIgnoreCase("null") && StringUtils.isNotBlank(group)) {
			ggList = groupRepo.findByIdIn(Arrays.asList(Long.valueOf(group)));
		} else {
			ggList = groupRepo.findByIdIn(Arrays.asList(1L,2L,3L,4L));
		}
		if(group != null && group.equalsIgnoreCase("null")) {
			group = "";
		}
		model.addAttribute("group", group);
		if(globalorgdid != null && globalorgdid.equalsIgnoreCase("null") && !globalorgdid.equalsIgnoreCase("")) {
			globalorgdid = "";
		}
		model.addAttribute("globalorgdid", globalorgdid);
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
				List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(ggList,org,caseId);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListFil, pageable,org,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListFil, allPage,org,caseId);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				model.addAttribute("totalEmpCount", empListFil.size());
			} else {
			
				List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCasedetailsIdNotIn(ggList,caseId);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil, pageable,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil, allPage,caseId);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				model.addAttribute("totalEmpCount", empListFil.size());
			}
			model.addAttribute("dashboarddatalist", dataList);

		}else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			model.addAttribute("globalorgdid", org.getId());
			List<EmployeeDetails> empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(1L,2L,3L,4L),org,caseId);
			List<CaseDetails> caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployee", empList.size());
			model.addAttribute("totalCases", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(1L),org,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployeeA",  empList.size());
			model.addAttribute("totalCasesA", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(2L),org,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployeeB",  empList.size());
			model.addAttribute("totalCasesB", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(3L),org,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployeeC",  empList.size());
			model.addAttribute("totalCasesC", caseList.size());
			
			empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(4L),org,caseId);
			caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,org,caseId);
			model.addAttribute("totalEmployeeD",  empList.size());
			model.addAttribute("totalCasesD", caseList.size());
			
			
			List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(ggList,org,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListFil,org, pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListFil,org, allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("totalEmpCount", empListFil.size());
			


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
			
			List<DashboardData> dataList = new ArrayList<>();
			List<GlobalOrg> orgList = globalRepo.findByActive(true);			
			orgList.stream().forEach(s->{
				DashboardData data = getdashBoradData(s);
				dataList.add(data);
			});
			
			
			List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(ggList,userList,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListFil,userList, pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil, allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("totalEmpCount", empListFil.size());
			model.addAttribute("dashboarddatalist", dataList);
		}

		else {
			

			List<DashboardData> dataList = new ArrayList<>();
			List<GlobalOrg> orgList = globalRepo.findByActive(true);			
			orgList.stream().forEach(s->{
				DashboardData data = getdashBoradData(s);
				dataList.add(data);
			});
			List<EmployeeDetails> empListFil = empRepo.findByServicegroupInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(ggList,currentUser,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListFil,currentUser, pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil, allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("totalEmpCount", empListFil.size());
			model.addAttribute("dashboarddatalist", dataList);
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

		return "DeptInquiryViews/ReportsforGroupWiseDept.html";

	}
	
	
	private DashboardData getdashBoradData(GlobalOrg s) {
		DashboardData data = new DashboardData();
		
		List<CaseDetails> finalOut = caseDetailsRepo.findAll();		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empList = null;
		List<CaseDetails> caseList = null;
		
		empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(1L),s,caseId);
		caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,s,caseId);
		GroupAData a = new GroupAData();
		a.setCountCaseNo(caseList.size());
		a.setTotalemployee(empList.size());


		empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(2L),s,caseId);
		caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,s,caseId);
		GroupBData b = new GroupBData();
		b.setCountCaseNo(caseList.size());
		b.setTotalemployee(empList.size());
		

		empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(3L),s,caseId);
		caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,s,caseId);
		GroupCData c = new GroupCData();
		c.setCountCaseNo(caseList.size());
		c.setTotalemployee(empList.size());
		

		empList = empRepo.findByServicegroupIdInAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotIn(Arrays.asList(4L),s,caseId);
		caseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empList,s,caseId);
		GroupDData d = new GroupDData();
		d.setCountCaseNo(caseList.size());
		d.setTotalemployee(empList.size());
		
		
		data.setDepartmentName(s.getGlobalOrgName());
		data.setDataA(a);
		data.setDataB(b);
		data.setDataC(c);
		data.setDataD(d);
		data.setOrgid(s.getId());		
		return data;
	}

}
