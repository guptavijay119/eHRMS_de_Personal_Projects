package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class RuleApplicableReportsController {


	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;

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



	private static final Logger LOG = LoggerFactory.getLogger(RuleApplicableReportsController.class);


	/* Added on 17-11-2022 case details report page */

	@GetMapping("/ruleApplicableReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "ruleid", required = false) String ruleid,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

		
		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}
		LOG.info("rule applicable reports controller....");
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

		/* model.addAttribute("divisiondata", divisionRepository.findAll()); */

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
		if(ruleid == null) {
			ruleid="";
		}
		model.addAttribute("ruleid", ruleid);

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {

			/* rule8 division wise data count */
			List<CaseDetails> rule8 = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(1),caseId);
			model.addAttribute("rule8", rule8.size());
			model.addAttribute("empRul8Count", rule8.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule10 division wise data count */
			List<CaseDetails> rule10 = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(2),caseId);
			model.addAttribute("rule10", rule10.size());
			model.addAttribute("empRule10Count", rule10.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule13first division wise data count */

			List<CaseDetails> rule13first = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(3),caseId);
			model.addAttribute("rule13first", rule13first.size());
			model.addAttribute("empRule13Countfirst",
					rule13first.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule13second rule applicable wise data count */

			List<CaseDetails> rule13second = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(4),caseId);
			model.addAttribute("rule13second", rule13second.size());
			model.addAttribute("employeerule13secondcount",
					rule13second.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
			model.addAttribute("empRule13SecondCount",
					rule13second.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule27 rule applicable wise data count */

			List<CaseDetails> rule27 = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(5),caseId);
			model.addAttribute("rule27", rule27.size());
			model.addAttribute("empRule27Count", rule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule27 rule applicable wise data count */

			List<CaseDetails> rule8andrule27 = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(6),caseId);
			model.addAttribute("rule8andrule27", rule8andrule27.size());
			model.addAttribute("empRule8AndRule27Count",
					rule8andrule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule10 rule applicable wise data count */

			List<CaseDetails> rule8andrule10 = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(7),caseId);
			model.addAttribute("rule8andrule10", rule8andrule10.size());
			model.addAttribute("empRule8AndRule10Count",
					rule8andrule10.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule10 rule27 rule applicable wise data count */

			List<CaseDetails> rule8andrule10andrule27 = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(8),caseId);
			model.addAttribute("rule8andrule10andrule27", rule8andrule10andrule27.size());
			model.addAttribute("empRule8AndRule10Andrule27Count",
					rule8andrule10andrule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
		
			
			noticePages = caseDetailsRepo.findByIdNotIn(pageable,caseId);
			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);
			// List<CaseDetails> total = caseDetailsRepo.find();
		     allCases = caseDetailsRepo.findByIdNotIn(allPages,caseId);
		     model.addAttribute("empCountTotal", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
		     
		     long countCases = allCases.getContent().size();
			model.addAttribute("totalCases", countCases);

			if (StringUtils.isNotBlank(ruleid)) {
			//	model.addAttribute("ruleid", ruleid);
				noticePages = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(ruleid), pageable,caseId);
				allCases = caseDetailsRepo.findByRuleApplicable_IdAndIdNotIn(Long.valueOf(ruleid),allPages,caseId);
				
			} else {
				noticePages = caseDetailsRepo.findByIdNotIn(pageable,caseId);
				allCases = caseDetailsRepo.findByIdNotIn(allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());

			}
	
			//allCases = caseDetailsRepo.findAll(allPages);
			System.out.println( "emp = "+allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
			model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
			/* rule8 division wise data count */
			List<CaseDetails> rule8 = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(1),org,caseId);
			model.addAttribute("rule8", rule8.size());
			model.addAttribute("empRul8Count", rule8.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule10 division wise data count */
			List<CaseDetails> rule10 = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(2),org,caseId);
			model.addAttribute("rule10", rule10.size());
			model.addAttribute("empRule10Count", rule10.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule13first division wise data count */

			List<CaseDetails> rule13first = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(3),org,caseId);
			model.addAttribute("rule13first", rule13first.size());
			model.addAttribute("empRule13Countfirst",
					rule13first.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule13second rule applicable wise data count */

			List<CaseDetails> rule13second = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(4),org,caseId);
			model.addAttribute("rule13second", rule13second.size());
			model.addAttribute("employeerule13secondcount",
					rule13second.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
			model.addAttribute("empRule13SecondCount",
					rule13second.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule27 rule applicable wise data count */

			List<CaseDetails> rule27 = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(5),org,caseId);
			model.addAttribute("rule27", rule27.size());
			model.addAttribute("empRule27Count", rule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule27 rule applicable wise data count */

			List<CaseDetails> rule8andrule27 = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(6),org,caseId);
			model.addAttribute("rule8andrule27", rule8andrule27.size());
			model.addAttribute("empRule8AndRule27Count",
					rule8andrule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule10 rule applicable wise data count */

			List<CaseDetails> rule8andrule10 = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(7),org,caseId);
			model.addAttribute("rule8andrule10", rule8andrule10.size());
			model.addAttribute("empRule8AndRule10Count",
					rule8andrule10.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule10 rule27 rule applicable wise data count */

			List<CaseDetails> rule8andrule10andrule27 = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(8),org,caseId);
			model.addAttribute("rule8andrule10andrule27", rule8andrule10andrule27.size());
			model.addAttribute("empRule8AndRule10Andrule27Count",
					rule8andrule10andrule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
		
			
			noticePages = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,pageable,caseId);
			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);
			// List<CaseDetails> total = caseDetailsRepo.find();
		     allCases = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,allPages,caseId);
		     model.addAttribute("empCountTotal", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
		     
		     long countCases = allCases.getContent().size();
			model.addAttribute("totalCases", countCases);

			if (StringUtils.isNotBlank(ruleid)) {
			//	model.addAttribute("ruleid", ruleid);
				noticePages = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(ruleid),org, pageable,caseId);
				allCases = caseDetailsRepo.findByRuleApplicable_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(ruleid),org,allPages,caseId);
				
			} else {
				noticePages = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,pageable,caseId);
				allCases = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());

			}
	
			//allCases = caseDetailsRepo.findAll(allPages);
//			System.out.println( "emp = "+allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
			model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

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

			/* rule8 division wise data count */
			List<CaseDetails> rule8 = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(1),caseId);
			model.addAttribute("rule8", rule8.size());
			model.addAttribute("empRul8Count", rule8.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule10 division wise data count */
			List<CaseDetails> rule10 = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(2),caseId);
			model.addAttribute("rule10", rule10.size());
			model.addAttribute("empRule10Count", rule10.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule13first division wise data count */

			List<CaseDetails> rule13first = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(3),caseId);
			model.addAttribute("rule13first", rule13first.size());
			model.addAttribute("empRule13Countfirst",
					rule13first.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule13second rule applicable wise data count */

			List<CaseDetails> rule13second = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(4),caseId);
			model.addAttribute("rule13second", rule13second.size());
			model.addAttribute("empRule13SecondCount",
					rule13second.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule27 rule applicable wise data count */

			List<CaseDetails> rule27 = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(5),caseId);
			model.addAttribute("rule27", rule27.size());
			model.addAttribute("empRule27Count", rule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule27 rule applicable wise data count */

			List<CaseDetails> rule8andrule27 = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(6),caseId);
			model.addAttribute("rule8andrule27", rule8andrule27.size());
			model.addAttribute("empRule8AndRule27Count",
					rule8andrule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule10 rule applicable wise data count */

			List<CaseDetails> rule8andrule10 = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(7),caseId);
			model.addAttribute("rule8andrule10", rule8andrule10.size());
			model.addAttribute("empRule8AndRule10Count",
					rule8andrule10.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			/* rule8 and rule10 rule27 rule applicable wise data count */

			List<CaseDetails> rule8andrule10andrule27 = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(8),caseId);
			model.addAttribute("rule8andrule10andrule27", rule8andrule10andrule27.size());
			model.addAttribute("empRule8AndRule10Andrule27Count",
					rule8andrule10andrule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
			// List<CaseDetails> total = caseDetailsRepo.find();

			// depend on employee we are getting data code

			noticePages = caseDetailsRepo.findByCurrentUserInAndIdNotIn(userList,pageable,caseId);
			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findByCurrentUserInAndIdNotIn(userList,allPages,caseId);
			 model.addAttribute("empCountTotal", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
			// for Role _Department we will get rule 8 count

			/*
			 * allCases = caseDetailsRepo.findAll(allPages); model.addAttribute("empCount",
			 * allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
			 */

			long countCases = caseDetailsRepo.countDistinctIdByCurrentUserInAndIdNotIn(userList,caseId);
			model.addAttribute("totalCases", countCases);
			noticePages = caseDetailsRepo.findDistinctIdByCurrentUserInAndIdNotIn(userList, pageable,caseId);
			if (StringUtils.isNotBlank(ruleid)) {
				model.addAttribute("ruleid", ruleid);
				noticePages = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList, Long.valueOf(ruleid), pageable,caseId);
				allCases = caseDetailsRepo.findByCurrentUserInAndRuleApplicable_IdAndIdNotIn(userList,Long.valueOf(ruleid),allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
//				model.addAttribute("totalCases", allCases.getContent().size());
			} else {
				model.addAttribute("ruleid", "");
				noticePages = caseDetailsRepo.findByCurrentUserInAndIdNotIn(
						userList, pageable,caseId);
				allCases = caseDetailsRepo.findByCurrentUserInAndIdNotIn(userList,allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
//				model.addAttribute("totalCases", allCases.getContent().size());
			}
		}

		else {

			List<CaseDetails> rule8 = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser,
					Long.valueOf(1),caseId);
			model.addAttribute("rule8", rule8.size());
			model.addAttribute("empRul8Count", rule8.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> rule10 = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser,
					Long.valueOf(2),caseId);
			model.addAttribute("rule10", rule10.size());
			model.addAttribute("empRule10Count", rule10.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> rule13first = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser,
					Long.valueOf(3),caseId);
			model.addAttribute("rule13first", rule13first.size());
			model.addAttribute("empRule13Countfirst",
					rule13first.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> rule13second = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser,
					Long.valueOf(4),caseId);
			model.addAttribute("rule13second", rule13second.size());
			model.addAttribute("empRule13SecondCount",
					rule13second.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> rule27 = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser,
					Long.valueOf(5),caseId);
			model.addAttribute("rule27", rule27.size());
			model.addAttribute("empRule27Count", rule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> rule8andrule27 = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser,
					Long.valueOf(6),caseId);
			model.addAttribute("rule8andrule27", rule8andrule27.size());
			model.addAttribute("empRule8AndRule27Count",
					rule8andrule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> rule8andrule10 = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser,
					Long.valueOf(7),caseId);
			model.addAttribute("rule8andrule10", rule8andrule10.size());
			model.addAttribute("empRule8AndRule10Count",
					rule8andrule10.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

			List<CaseDetails> rule8andrule10andrule27 = caseDetailsRepo
					.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser, Long.valueOf(8),caseId);
			model.addAttribute("rule8andrule10andrule27", rule8andrule10andrule27.size());
			model.addAttribute("empRule8AndRule10Andrule27Count",
					rule8andrule10andrule27.stream().mapToLong(s -> s.getEmployeeList().size()).sum());

//		     	  noticePages = caseDetailsRepo.findAll(pageable);
			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);
//			noticePages = caseDetailsRepo.findByCurrentUserAndIdNotIn(currentUser,pageable,caseId);
			allCases = caseDetailsRepo.findByCurrentUserAndIdNotIn(currentUser,allPages,caseId);
			 model.addAttribute("empCountTotal", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
			/* Employee count down on Depend on Departments code written (21/11/2022) */
//			allCases = caseDetailsRepo.findByCurrentUserAndGlobalorgAndIdNotIn(currentUser,
//					currentUser.getPimsEmployee().getGlobalOrgId(), allPages,caseId);
			model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

//			long countCases = caseDetailsRepo.countDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(currentUser.getPimsEmployee().getGlobalOrgId(),caseId);
			model.addAttribute("totalCases", allCases.getContent().size());
		
			if (StringUtils.isNotBlank(ruleid)) {
				model.addAttribute("ruleid", ruleid);
				noticePages = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser, Long.valueOf(ruleid), pageable,caseId);
				allCases = caseDetailsRepo.findByCurrentUserAndRuleApplicable_IdAndIdNotIn(currentUser,Long.valueOf(ruleid),allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
//				model.addAttribute("totalCases", allCases.getContent().size());
			} else {
				model.addAttribute("ruleid", "");
				noticePages = caseDetailsRepo.findByCurrentUserAndIdNotIn(
						currentUser, pageable,caseId);
				allCases = caseDetailsRepo.findByCurrentUserAndIdNotIn(currentUser,allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
//				model.addAttribute("totalCases", allCases.getContent().size());
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

		model.addAttribute("totalCasesLabel",messageSource.getMessage("deptenq.totalcases.label", null, loc.resolveLocale(request)));
		 model.addAttribute("totalEmployeeCountLabel",messageSource.getMessage("deptenq.totalemployee.label", null, loc.resolveLocale(request)));

		return "DeptInquiryViews/ReportsForRuleApplicabeWisePendingCases.html";

	}

}
