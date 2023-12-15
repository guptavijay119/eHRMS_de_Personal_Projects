package com.ehrms.deptenq.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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
import com.ehrms.deptenq.models.ChargesheetDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.IChargesheetDetailsRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;
import com.ehrms.deptenq.utility.AsyncExecutors;


@Controller
@RequestMapping("departmentalEnquiry")
public class TotalPendingCasesReportsController {


	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;



	@Autowired
	private CaseDetailsRepository caseDetailsRepo;



	@Autowired
	private SubDepartmentRepository subDepartmentRepo;

	@Autowired
	private IEmployeeDetailsRepository employeedetailsrepo;

	@Autowired
	private IChargesheetDetailsRepository chargesheetRepo;
	
	/* written code on autowired on 02/11/2023 by vijay */
	@Autowired
	private  DepartmentConstants departmentconstants;
	
	@Autowired
	private AsyncExecutors exe;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LocaleResolver loc;
	
	@Autowired
	private MessageSource messageSource;
	

	




	

	private static final Logger LOG = LoggerFactory.getLogger(TotalPendingCasesReportsController.class);
	
	@GetMapping("/reportsforAllFields/{page}")
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

		String customsearch = null;
		if (search != null && search.contains("major")) {
			customsearch = search.split("splitting")[1];
		}
		if (search != null && search.contains("minor")) {
			customsearch = search.split("splitting")[1];
		}
		if (search != null && search.contains("lessthan5")) {
			customsearch = search.split("splitting")[1];
		}
		if (search != null && search.contains("morethan5")) {
			customsearch = search.split("splitting")[1];
		}
		if (search != null && search.contains("retired")) {
			customsearch = search.split("splitting")[1];
		}
		if(customsearch != null && customsearch.equalsIgnoreCase("null")) {
			customsearch = "";
		}
		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}
		if(viewreport == null) {
			viewreport = "true";
		}
		ExecutorService executorService = exe.getExe();
		List<Callable<String>> callList = new ArrayList<>();
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
		long starttime = System.currentTimeMillis();
		long starttime1 = System.currentTimeMillis();
//		List<CaseDetails> finalOut = caseRepo.findAll();
		long casecount = caseDetailsRepo.count();
		List<Integer> largeList = new ArrayList<>();
//		List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
		while(true) {
			if(casecount>10) {
				largeList.add(10);
			} else {
				if(casecount>0 && casecount<=10) {
					largeList.add((int)casecount);
				} else {
					break;
				}
			}
			casecount=casecount-10;
		}
		List<CaseDetails> finalOut = new ArrayList<>();
		if(!largeList.isEmpty()) {
			for(Integer i=1;i<=largeList.size();i++) {
				final Integer index1 = i ;
				callList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {																		
						Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
						Page<CaseDetails> caseList = caseDetailsRepo.findAll(pageable);
						finalOut.addAll(caseList.getContent());
						return null;
					}
				});
			}
		}
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callList);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long endtime1 = System.currentTimeMillis();
		LOG.info("First Step time required dashboard :"+TimeUnit.MILLISECONDS.toSeconds(endtime1-starttime1));

		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(FinalOutcomeDetails::isWhetherCaseFinallyDecided)
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(CaseDetails::getId).collect(Collectors.toSet());
		endtime1 = System.currentTimeMillis();
		LOG.info("First Step time required dashboard :"+TimeUnit.MILLISECONDS.toSeconds(endtime1-starttime1));
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		
//		
//		List<CaseDetails> finalOut = caseDetailsRepo.findAll();		
//		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
//				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
//				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
//		if(caseIdx.isEmpty()) {
//			caseIdx.add(0L);
//		}
		List<CaseDetails> casesTransferList = caseDetailsRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			
			
			
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
					&& (StringUtils.isBlank(fromdate) && StringUtils.isBlank(todate))) {
				model.addAttribute("search", search);
				List<EmployeeDetails> empList = employeedetailsrepo
						.findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
								search, search, search);
				if (empList.isEmpty()) {
					if (search.contains("major")) {
						if(StringUtils.isNotBlank(customsearch)) {
							noticePages = caseDetailsRepo
									.findDistinctIdByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
											Arrays.asList(2L), customsearch, pageable,caseId);
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases = caseDetailsRepo
									.findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
											Arrays.asList(2L), customsearch, pageableall,caseId);
						} else {
							noticePages = caseDetailsRepo
									.findDistinctIdByRuleApplicableIdNotInAndIdNotIn(
											Arrays.asList(2L), pageable,caseId);
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases = caseDetailsRepo
									.findByRuleApplicableIdNotInAndIdNotIn(
											Arrays.asList(2L), pageableall,caseId);
						}
					} else if (search.contains("minor")) {
						if(StringUtils.isNotBlank(customsearch)) {
							noticePages = caseDetailsRepo
									.findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
											Arrays.asList(2L), customsearch, pageable,caseId);
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases = caseDetailsRepo
									.findByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
											Arrays.asList(2L), customsearch, pageableall,caseId);
						} else {
							noticePages = caseDetailsRepo
									.findDistinctIdByRuleApplicableIdInAndIdNotIn(
											Arrays.asList(2L), pageable,caseId);
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases = caseDetailsRepo
									.findByRuleApplicableIdInAndIdNotIn(
											Arrays.asList(2L), pageableall,caseId);
						}
					} else if (search.contains("lessthan5")) {
						if(customsearch != null && !customsearch.equalsIgnoreCase("null")) {
							List<ChargesheetDetails> chargeList = chargesheetRepo
									.findByChargesheetDateGreaterThanEqualAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
											LocalDate.now().minusYears(5), true, customsearch);
							noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageable,caseId);
							noticePages.getContent().stream().forEach(s->{
								List<ChargesheetDetails> empp = new ArrayList<>();
								chargeList.stream().forEach(ss->{
									if(ss.getCaseDetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setChargeSheetList(empp);
							});
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageableall,caseId);
							allCases.getContent().stream().forEach(s->{
								List<ChargesheetDetails> empp = new ArrayList<>();
								chargeList.stream().forEach(ss->{
									if(ss.getCaseDetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setChargeSheetList(empp);
							});
						} else {
							List<ChargesheetDetails> chargeList = chargesheetRepo
									.findByChargesheetDateGreaterThanEqualAndChargesheetIssued(
											LocalDate.now().minusYears(5), true);
							noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageable,caseId);
							noticePages.getContent().stream().forEach(s->{
								List<ChargesheetDetails> empp = new ArrayList<>();
								chargeList.stream().forEach(ss->{
									if(ss.getCaseDetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setChargeSheetList(empp);
							});
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageableall,caseId);
							allCases.getContent().stream().forEach(s->{
								List<ChargesheetDetails> empp = new ArrayList<>();
								chargeList.stream().forEach(ss->{
									if(ss.getCaseDetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setChargeSheetList(empp);
							});
							
						}
					} else if (search.contains("morethan5")) {
						if(customsearch != null && !customsearch.equalsIgnoreCase("null")) {
							List<ChargesheetDetails> chargeList = chargesheetRepo
									.findByChargesheetDateLessThanAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
											LocalDate.now().minusYears(5), true, customsearch);
							noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageable,caseId);
							noticePages.getContent().stream().forEach(s->{
								List<ChargesheetDetails> empp = new ArrayList<>();
								chargeList.stream().forEach(ss->{
									if(ss.getCaseDetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setChargeSheetList(empp);
							});
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageableall,caseId);
							allCases.getContent().stream().forEach(s->{
								List<ChargesheetDetails> empp = new ArrayList<>();
								chargeList.stream().forEach(ss->{
									if(ss.getCaseDetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setChargeSheetList(empp);
							});
						} else {
							List<ChargesheetDetails> chargeList = chargesheetRepo
									.findByChargesheetDateLessThanAndChargesheetIssued(
											LocalDate.now().minusYears(5), true);
							noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageable,caseId);
							noticePages.getContent().stream().forEach(s->{
								List<ChargesheetDetails> empp = new ArrayList<>();
								chargeList.stream().forEach(ss->{
									if(ss.getCaseDetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setChargeSheetList(empp);
							});
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageableall,caseId);
							allCases.getContent().stream().forEach(s->{
								List<ChargesheetDetails> empp = new ArrayList<>();
								chargeList.stream().forEach(ss->{
									if(ss.getCaseDetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setChargeSheetList(empp);
							});
						}
					} else if(search.contains("retired")) {
						if(customsearch != null && !customsearch.equalsIgnoreCase("null")) {
							List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
							model.addAttribute("retireEmployees", empListRetired.size());
							noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
							noticePages.getContent().stream().forEach(s->{
								List<EmployeeDetails> empp = new ArrayList<>();
								empListRetired.stream().forEach(ss->{
									if(ss.getCasedetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setEmployeeList(empp);
							});
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);
							allCases.getContent().stream().forEach(s->{
								List<EmployeeDetails> empp = new ArrayList<>();
								empListRetired.stream().forEach(ss->{
									if(ss.getCasedetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setEmployeeList(empp);
							});
						} else {
							List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsIdNotIn(LocalDate.now(),caseId);
							model.addAttribute("retireEmployees", empListRetired.size());
							noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
							noticePages.getContent().stream().forEach(s->{
								List<EmployeeDetails> empp = new ArrayList<>();
								empListRetired.stream().forEach(ss->{
									if(ss.getCasedetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setEmployeeList(empp);
							});
							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
							allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);	
							allCases.getContent().stream().forEach(s->{
								List<EmployeeDetails> empp = new ArrayList<>();
								empListRetired.stream().forEach(ss->{
									if(ss.getCasedetails().getId()==s.getId()) {
										empp.add(ss);
									}	
								});
								s.setEmployeeList(empp);
							});
						}
					} else {
						noticePages = caseDetailsRepo
								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										search, search, search, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										search, search, search, search, pageableall,caseId);
					}
				} else {
					noticePages = caseDetailsRepo
							.findDistinctIdByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
									empList, search, search, search, search, pageable,caseId);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findDistinctIdByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
									empList, search, search, search, search, pageableall,caseId);
				}

			} else if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
					&& (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate))) {
				model.addAttribute("search", search);
				model.addAttribute("fromdate", fromdate);
				model.addAttribute("todate", todate);
				List<ChargesheetDetails> chargeSheetList = chargesheetRepo
						.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(LocalDate.parse(fromdate),
								LocalDate.parse(todate));
				List<EmployeeDetails> empList = employeedetailsrepo
						.findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
								search, search, search);
				if (empList.isEmpty() && chargeSheetList.isEmpty()) {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), search, pageableall,caseId);
						
					} else if(search.contains("retired")) {
						List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
						model.addAttribute("retireEmployees", empListRetired.size());
						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);
						allCases.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
					} else {
						noticePages = caseDetailsRepo
								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										search, search, search, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										search, search, search, search, pageableall,caseId);
					}
				} else if (!empList.isEmpty() && chargeSheetList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
									search, empList, search, empList, search, empList, search, empList, pageable,caseId);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
									search, empList, search, empList, search, empList, search, empList, pageableall,caseId);
				} else if (empList.isEmpty() && !chargeSheetList.isEmpty()) {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), chargeSheetList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), chargeSheetList, search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), chargeSheetList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), chargeSheetList, search, pageableall,caseId);
					} else if(search.contains("retired")) {
						List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
						model.addAttribute("retireEmployees", empListRetired.size());
						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);
						allCases.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
					} else {
						noticePages = caseDetailsRepo
								.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
										search, chargeSheetList, search, chargeSheetList, search, chargeSheetList,
										search, chargeSheetList, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
										search, chargeSheetList, search, chargeSheetList, search, chargeSheetList,
										search, chargeSheetList, pageableall,caseId);
					}

				} else {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), chargeSheetList, empList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), chargeSheetList, empList, search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), chargeSheetList, empList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										Arrays.asList(2L), chargeSheetList, empList, search, pageableall,caseId);
					} else if(search.contains("retired")) {
						List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
						model.addAttribute("retireEmployees", empListRetired.size());
						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);
						allCases.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
					} else {
						noticePages = caseDetailsRepo
								.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
										search, chargeSheetList, empList, search, chargeSheetList, empList, search,
										chargeSheetList, empList, search, chargeSheetList, empList, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
										search, chargeSheetList, empList, search, chargeSheetList, empList, search,
										chargeSheetList, empList, search, chargeSheetList, empList, pageableall,caseId);
					}
				}

			} else {
				if ((StringUtils.isNotBlank(fromdate) && StringUtils.isNoneBlank(todate))
						&& StringUtils.isBlank(search)) {
					model.addAttribute("fromdate", fromdate);
					model.addAttribute("todate", todate);
					List<ChargesheetDetails> chargeSheetList = chargesheetRepo
							.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
									LocalDate.parse(fromdate), LocalDate.parse(todate));
					noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeSheetList, pageable,caseId);
					noticePages.getContent().stream().forEach(s->{
						List<ChargesheetDetails> empp = new ArrayList<>();
						chargeSheetList.stream().forEach(ss->{
							if(ss.getCaseDetails().getId()==s.getId()) {
								empp.add(ss);
							}	
						});
						s.setChargeSheetList(empp);
					});
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeSheetList, pageableall,caseId);
					allCases.getContent().stream().forEach(s->{
						List<ChargesheetDetails> empp = new ArrayList<>();
						chargeSheetList.stream().forEach(ss->{
							if(ss.getCaseDetails().getId()==s.getId()) {
								empp.add(ss);
							}	
						});
						s.setChargeSheetList(empp);
					});

				} else {
					noticePages = caseDetailsRepo.findDistinctIdByIdNotIn(pageable,caseId);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo.findDistinctIdByIdNotIn(pageableall,caseId);
				}
			}

		} else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
//			customsearch = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId().getGlobalOrgName();
////			search = customsearch;
//			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
//					&& (StringUtils.isBlank(fromdate) && StringUtils.isBlank(todate))) {
//				model.addAttribute("search", search);
//				List<EmployeeDetails> empList = employeedetailsrepo
//						.findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
//								search, search, search);
//				if (empList.isEmpty()) {
//					if (search.contains("major")) {
//						if(StringUtils.isNotBlank(customsearch)) {
//							noticePages = caseDetailsRepo
//									.findDistinctIdByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//											Arrays.asList(2L), customsearch, pageable,caseId);
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases = caseDetailsRepo
//									.findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//											Arrays.asList(2L), customsearch, pageableall,caseId);
//						} else {
//							noticePages = caseDetailsRepo
//									.findDistinctIdByRuleApplicableIdNotInAndIdNotIn(
//											Arrays.asList(2L), pageable,caseId);
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases = caseDetailsRepo
//									.findByRuleApplicableIdNotInAndIdNotIn(
//											Arrays.asList(2L), pageableall,caseId);
//						}
//					} else if (search.contains("minor")) {
//						if(StringUtils.isNotBlank(customsearch)) {
//							noticePages = caseDetailsRepo
//									.findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//											Arrays.asList(2L), customsearch, pageable,caseId);
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases = caseDetailsRepo
//									.findByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//											Arrays.asList(2L), customsearch, pageableall,caseId);
//						} else {
//							noticePages = caseDetailsRepo
//									.findDistinctIdByRuleApplicableIdInAndIdNotIn(
//											Arrays.asList(2L), pageable,caseId);
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases = caseDetailsRepo
//									.findByRuleApplicableIdInAndIdNotIn(
//											Arrays.asList(2L), pageableall,caseId);
//						}
//					} else if (search.contains("lessthan5")) {
//						if(customsearch != null && !customsearch.equalsIgnoreCase("null")) {
//							List<ChargesheetDetails> chargeList = chargesheetRepo
//									.findByChargesheetDateGreaterThanEqualAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
//											LocalDate.now().minusYears(5), true, customsearch);
//							noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageable,caseId);
//							noticePages.getContent().stream().forEach(s->{
//								List<ChargesheetDetails> empp = new ArrayList<>();
//								chargeList.stream().forEach(ss->{
//									if(ss.getCaseDetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setChargeSheetList(empp);
//							});
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageableall,caseId);
//							allCases.getContent().stream().forEach(s->{
//								List<ChargesheetDetails> empp = new ArrayList<>();
//								chargeList.stream().forEach(ss->{
//									if(ss.getCaseDetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setChargeSheetList(empp);
//							});
//						} else {
//							List<ChargesheetDetails> chargeList = chargesheetRepo
//									.findByChargesheetDateGreaterThanEqualAndChargesheetIssued(
//											LocalDate.now().minusYears(5), true);
//							noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageable,caseId);
//							noticePages.getContent().stream().forEach(s->{
//								List<ChargesheetDetails> empp = new ArrayList<>();
//								chargeList.stream().forEach(ss->{
//									if(ss.getCaseDetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setChargeSheetList(empp);
//							});
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageableall,caseId);
//							allCases.getContent().stream().forEach(s->{
//								List<ChargesheetDetails> empp = new ArrayList<>();
//								chargeList.stream().forEach(ss->{
//									if(ss.getCaseDetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setChargeSheetList(empp);
//							});
//							
//						}
//					} else if (search.contains("morethan5")) {
//						if(customsearch != null && !customsearch.equalsIgnoreCase("null")) {
//							List<ChargesheetDetails> chargeList = chargesheetRepo
//									.findByChargesheetDateLessThanAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
//											LocalDate.now().minusYears(5), true, customsearch);
//							noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageable,caseId);
//							noticePages.getContent().stream().forEach(s->{
//								List<ChargesheetDetails> empp = new ArrayList<>();
//								chargeList.stream().forEach(ss->{
//									if(ss.getCaseDetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setChargeSheetList(empp);
//							});
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageableall,caseId);
//							allCases.getContent().stream().forEach(s->{
//								List<ChargesheetDetails> empp = new ArrayList<>();
//								chargeList.stream().forEach(ss->{
//									if(ss.getCaseDetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setChargeSheetList(empp);
//							});
//						} else {
//							List<ChargesheetDetails> chargeList = chargesheetRepo
//									.findByChargesheetDateLessThanAndChargesheetIssued(
//											LocalDate.now().minusYears(5), true);
//							noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageable,caseId);
//							noticePages.getContent().stream().forEach(s->{
//								List<ChargesheetDetails> empp = new ArrayList<>();
//								chargeList.stream().forEach(ss->{
//									if(ss.getCaseDetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setChargeSheetList(empp);
//							});
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeList, pageableall,caseId);
//							allCases.getContent().stream().forEach(s->{
//								List<ChargesheetDetails> empp = new ArrayList<>();
//								chargeList.stream().forEach(ss->{
//									if(ss.getCaseDetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setChargeSheetList(empp);
//							});
//						}
//					} else if(search.contains("retired")) {
//						if(customsearch != null && !customsearch.equalsIgnoreCase("null")) {
//							List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
//							model.addAttribute("retireEmployees", empListRetired.size());
//							noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
//							noticePages.getContent().stream().forEach(s->{
//								List<EmployeeDetails> empp = new ArrayList<>();
//								empListRetired.stream().forEach(ss->{
//									if(ss.getCasedetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setEmployeeList(empp);
//							});
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);
//							allCases.getContent().stream().forEach(s->{
//								List<EmployeeDetails> empp = new ArrayList<>();
//								empListRetired.stream().forEach(ss->{
//									if(ss.getCasedetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setEmployeeList(empp);
//							});
//						} else {
//							List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsIdNotIn(LocalDate.now(),caseId);
//							model.addAttribute("retireEmployees", empListRetired.size());
//							noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
//							noticePages.getContent().stream().forEach(s->{
//								List<EmployeeDetails> empp = new ArrayList<>();
//								empListRetired.stream().forEach(ss->{
//									if(ss.getCasedetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setEmployeeList(empp);
//							});
//							Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//							allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);							
//							allCases.getContent().stream().forEach(s->{
//								List<EmployeeDetails> empp = new ArrayList<>();
//								empListRetired.stream().forEach(ss->{
//									if(ss.getCasedetails().getId()==s.getId()) {
//										empp.add(ss);
//									}	
//								});
//								s.setEmployeeList(empp);
//							});
//						}
//					} else {
//						noticePages = caseDetailsRepo
//								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										search, search, search, search, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										search, search, search, search, pageableall,caseId);
//					}
//				} else {
//					noticePages = caseDetailsRepo
//							.findDistinctIdByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//									empList, search, search, search, search, pageable,caseId);
//					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//					allCases = caseDetailsRepo
//							.findDistinctIdByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//									empList, search, search, search, search, pageableall,caseId);
//				}
//
//			} else if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
//					&& (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate))) {
//				model.addAttribute("search", search);
//				model.addAttribute("fromdate", fromdate);
//				model.addAttribute("todate", todate);
//				List<ChargesheetDetails> chargeSheetList = chargesheetRepo
//						.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(LocalDate.parse(fromdate),
//								LocalDate.parse(todate));
//				List<EmployeeDetails> empList = employeedetailsrepo
//						.findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
//								search, search, search);
//				if (empList.isEmpty() && chargeSheetList.isEmpty()) {
//					if (search.contains("major")) {
//						noticePages = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), search, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), search, pageableall,caseId);
//					} else if (search.contains("minor")) {
//						noticePages = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), search, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), search, pageableall,caseId);
//						
//					} else if(search.contains("retired")) {
//						List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
//						model.addAttribute("retireEmployees", empListRetired.size());
//						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
//						noticePages.getContent().stream().forEach(s->{
//							List<EmployeeDetails> empp = new ArrayList<>();
//							empListRetired.stream().forEach(ss->{
//								if(ss.getCasedetails().getId()==s.getId()) {
//									empp.add(ss);
//								}	
//							});
//							s.setEmployeeList(empp);
//						});
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);
//						allCases.getContent().stream().forEach(s->{
//							List<EmployeeDetails> empp = new ArrayList<>();
//							empListRetired.stream().forEach(ss->{
//								if(ss.getCasedetails().getId()==s.getId()) {
//									empp.add(ss);
//								}	
//							});
//							s.setEmployeeList(empp);
//						});
//					} else {
//						noticePages = caseDetailsRepo
//								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										search, search, search, search, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										search, search, search, search, pageableall,caseId);
//					}
//				} else if (!empList.isEmpty() && chargeSheetList.isEmpty()) {
//					noticePages = caseDetailsRepo
//							.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
//									search, empList, search, empList, search, empList, search, empList, pageable,caseId);
//					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//					allCases = caseDetailsRepo
//							.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
//									search, empList, search, empList, search, empList, search, empList, pageableall,caseId);
//				} else if (empList.isEmpty() && !chargeSheetList.isEmpty()) {
//					if (search.contains("major")) {
//						noticePages = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), chargeSheetList, search, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), chargeSheetList, search, pageableall,caseId);
//					} else if (search.contains("minor")) {
//						noticePages = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), chargeSheetList, search, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), chargeSheetList, search, pageableall,caseId);
//					} else if(search.contains("retired")) {
//						List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
//						model.addAttribute("retireEmployees", empListRetired.size());
//						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
//						noticePages.getContent().stream().forEach(s->{
//							List<EmployeeDetails> empp = new ArrayList<>();
//							empListRetired.stream().forEach(ss->{
//								if(ss.getCasedetails().getId()==s.getId()) {
//									empp.add(ss);
//								}	
//							});
//							s.setEmployeeList(empp);
//						});
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);
//						allCases.getContent().stream().forEach(s->{
//							List<EmployeeDetails> empp = new ArrayList<>();
//							empListRetired.stream().forEach(ss->{
//								if(ss.getCasedetails().getId()==s.getId()) {
//									empp.add(ss);
//								}	
//							});
//							s.setEmployeeList(empp);
//						});
//					} else {
//						noticePages = caseDetailsRepo
//								.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
//										search, chargeSheetList, search, chargeSheetList, search, chargeSheetList,
//										search, chargeSheetList, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
//										search, chargeSheetList, search, chargeSheetList, search, chargeSheetList,
//										search, chargeSheetList, pageableall,caseId);
//					}
//
//				} else {
//					if (search.contains("major")) {
//						noticePages = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), chargeSheetList, empList, search, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), chargeSheetList, empList, search, pageableall,caseId);
//					} else if (search.contains("minor")) {
//						noticePages = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), chargeSheetList, empList, search, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
//										Arrays.asList(2L), chargeSheetList, empList, search, pageableall,caseId);
//					} else if(search.contains("retired")) {
//						List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
//						model.addAttribute("retireEmployees", empListRetired.size());
//						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageable,caseId);
//						noticePages.getContent().stream().forEach(s->{
//							List<EmployeeDetails> empp = new ArrayList<>();
//							empListRetired.stream().forEach(ss->{
//								if(ss.getCasedetails().getId()==s.getId()) {
//									empp.add(ss);
//								}	
//							});
//							s.setEmployeeList(empp);
//						});
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListRetired, pageableall,caseId);
//						allCases.getContent().stream().forEach(s->{
//							List<EmployeeDetails> empp = new ArrayList<>();
//							empListRetired.stream().forEach(ss->{
//								if(ss.getCasedetails().getId()==s.getId()) {
//									empp.add(ss);
//								}	
//							});
//							s.setEmployeeList(empp);
//						});
//					} else {
//						noticePages = caseDetailsRepo
//								.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
//										search, chargeSheetList, empList, search, chargeSheetList, empList, search,
//										chargeSheetList, empList, search, chargeSheetList, empList, pageable,caseId);
//						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseDetailsRepo
//								.findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
//										search, chargeSheetList, empList, search, chargeSheetList, empList, search,
//										chargeSheetList, empList, search, chargeSheetList, empList, pageableall,caseId);
//					}
//				}
//
//			} else {
//				if ((StringUtils.isNotBlank(fromdate) && StringUtils.isNoneBlank(todate))
//						&& StringUtils.isBlank(search)) {
//					model.addAttribute("fromdate", fromdate);
//					model.addAttribute("todate", todate);
//					List<ChargesheetDetails> chargeSheetList = chargesheetRepo
//							.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
//									LocalDate.parse(fromdate), LocalDate.parse(todate));
//					noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeSheetList, pageable,caseId);
//					noticePages.getContent().stream().forEach(s->{
//						List<ChargesheetDetails> empp = new ArrayList<>();
//						chargeSheetList.stream().forEach(ss->{
//							if(ss.getCaseDetails().getId()==s.getId()) {
//								empp.add(ss);
//							}	
//						});
//						s.setChargeSheetList(empp);
//					});
//					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//					allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeSheetList, pageableall,caseId);
//					allCases.getContent().stream().forEach(s->{
//						List<ChargesheetDetails> empp = new ArrayList<>();
//						chargeSheetList.stream().forEach(ss->{
//							if(ss.getCaseDetails().getId()==s.getId()) {
//								empp.add(ss);
//							}	
//						});
//						s.setChargeSheetList(empp);
//					});
//
//				} else {
//					noticePages = caseDetailsRepo.findDistinctIdByIdNotInAndAndGlobalorgGlobalOrgNameContainingIgnoreCase(pageable,caseId,customsearch);
//					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
//					allCases = caseDetailsRepo.findDistinctIdByIdNotInAndAndGlobalorgGlobalOrgNameContainingIgnoreCase(pageableall,caseId,customsearch);
//				}
//			}
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN") || currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARY")) {
//			List<SubDepartment> subDepartmentList = ;
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
			
			GlobalOrg org = currentUser.getPimsEmployee().getGlobalOrgId();
			
			Long casecount1 = caseDetailsRepo.countByCurrentUserPimsEmployeeGlobalOrgId(org);
			List<Integer> largeList1 = new ArrayList<>();
//			List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
			while(true) {
				if(casecount1>10) {
					largeList1.add(10);
				} else {
					if(casecount1>0 && casecount1<=10) {
						largeList1.add(casecount1.intValue());
					} else {
						break;
					}
				}
				casecount1=casecount1-10;
			}
			List<CaseDetails> finalOutx = new ArrayList<>();
			if(!largeList1.isEmpty()) {
				for(Integer i=1;i<=largeList1.size();i++) {
					final Integer indexx = i ;
					callList.add(new Callable<String>() {
						@Override
						public String call() throws Exception {																		
							Pageable pageable = PageRequest.of(indexx-1, 10, Sort.by("id"));
							Page<CaseDetails> caseList = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgId(org,pageable);
							finalOutx.addAll(caseList.getContent());
							return null;
						}
					});
				}
			}
			
			try {
				List<Future<String>> taskFutureList = executorService.invokeAll(callList);
				for (Future<String> future : taskFutureList) {
					try {
						future.get();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			
//			List<CaseDetails> finalOut = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgId(s);		
			caseIdx = finalOutx.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
					&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
					&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
			if(caseIdx.isEmpty()) {
				caseIdx.add(0L);
			}
			
//			List<User> userList = userRepo.findBySubDepartmentIn(
//					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
//			userList.add(currentUser);
//			noticePages = caseDetailsRepo.findDistinctIdByCurrentUserIn(userList, pageable);
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
					&& (StringUtils.isBlank(fromdate) && StringUtils.isBlank(todate))) {
				model.addAttribute("search", search);
				List<EmployeeDetails> empList = employeedetailsrepo
						.findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
								search, search, search);
				if (empList.isEmpty()) {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), customsearch, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), customsearch, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), customsearch, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), customsearch, pageableall,caseId);
					} else if (search.contains("lessthan5")) {
						List<ChargesheetDetails> chargeList = chargesheetRepo
								.findByChargesheetDateGreaterThanEqualAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
										LocalDate.now().minusYears(5), true, customsearch);
						noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserInAndIdNotIn(chargeList, userList,
								pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<ChargesheetDetails> empp = new ArrayList<>();
							chargeList.stream().forEach(ss->{
								if(ss.getCaseDetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setChargeSheetList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserInAndIdNotIn(chargeList, userList,
								pageableall,caseId);
						allCases.getContent().stream().forEach(s->{
							List<ChargesheetDetails> empp = new ArrayList<>();
							chargeList.stream().forEach(ss->{
								if(ss.getCaseDetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setChargeSheetList(empp);
						});
					} else if (search.contains("morethan5")) {
						List<ChargesheetDetails> chargeList = chargesheetRepo
								.findByChargesheetDateLessThanAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
										LocalDate.now().minusYears(5), true, customsearch);
						noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserInAndIdNotIn(chargeList, userList,
								pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<ChargesheetDetails> empp = new ArrayList<>();
							chargeList.stream().forEach(ss->{
								if(ss.getCaseDetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setChargeSheetList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserInAndIdNotIn(chargeList, userList,
								pageableall,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<ChargesheetDetails> empp = new ArrayList<>();
							chargeList.stream().forEach(ss->{
								if(ss.getCaseDetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setChargeSheetList(empp);
						});
					} else if(search.contains("retired")) {
						List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
						model.addAttribute("retireEmployees", empListRetired.size());
						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListRetired,userList, pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListRetired,userList, pageableall,caseId);
						allCases.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
					} else {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, search, userList, search, userList, search, userList, search,
										pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, search, userList, search, userList, search, userList, search,
										pageableall,caseId);
					}
				} else {
					noticePages = caseDetailsRepo
							.findDistinctIdByCurrentUserInAndEmployeeListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
									userList, empList, userList, search, userList, search, userList, search, userList,
									search, pageable,caseIdx);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findDistinctIdByCurrentUserInAndEmployeeListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
									userList, empList, userList, search, userList, search, userList, search, userList,
									search, pageableall,caseIdx);
				}
			} else if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
					&& (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate))) {
				model.addAttribute("search", search);
				model.addAttribute("fromdate", fromdate);
				model.addAttribute("todate", todate);
				List<ChargesheetDetails> chargeSheetList = chargesheetRepo
						.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(LocalDate.parse(fromdate),
								LocalDate.parse(todate));
				List<EmployeeDetails> empList = employeedetailsrepo
						.findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
								search, search, search);
				if (empList.isEmpty() && chargeSheetList.isEmpty()) {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), search, pageableall,caseId);
					} else {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, search, userList, search, userList, search, userList, search,
										pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, search, userList, search, userList, search, userList, search,
										pageableall,caseId);
					}
				} else if (!empList.isEmpty() && chargeSheetList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
									userList, search, empList, userList, search, empList, userList, search, empList,
									userList, search, empList, pageable,caseId);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
									userList, search, empList, userList, search, empList, userList, search, empList,
									userList, search, empList, pageableall,caseId);
				} else if (empList.isEmpty() && !chargeSheetList.isEmpty()) {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), chargeSheetList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), chargeSheetList, search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), chargeSheetList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), chargeSheetList, search, pageableall,caseId);
					} else {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
										userList, search, chargeSheetList, userList, search, chargeSheetList, userList,
										search, chargeSheetList, userList, search, chargeSheetList, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
										userList, search, chargeSheetList, userList, search, chargeSheetList, userList,
										search, chargeSheetList, userList, search, chargeSheetList, pageableall,caseId);
					}
				} else {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), chargeSheetList, empList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), chargeSheetList, empList, search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), chargeSheetList, empList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										userList, Arrays.asList(2L), chargeSheetList, empList, search, pageableall,caseId);
					} else {
						noticePages = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
										userList, search, chargeSheetList, empList, userList, search, chargeSheetList,
										empList, userList, search, chargeSheetList, empList, userList, search,
										chargeSheetList, empList, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
										userList, search, chargeSheetList, empList, userList, search, chargeSheetList,
										empList, userList, search, chargeSheetList, empList, userList, search,
										chargeSheetList, empList, pageableall,caseId);
					}
				}

			} else if ((StringUtils.isNotBlank(fromdate) && StringUtils.isNoneBlank(todate))
					&& StringUtils.isBlank(search)) {
				model.addAttribute("fromdate", fromdate);
				model.addAttribute("todate", todate);
				List<ChargesheetDetails> chargeSheetList = chargesheetRepo
						.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(LocalDate.parse(fromdate),
								LocalDate.parse(todate));
				noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserInAndIdNotIn(chargeSheetList, userList,
						pageable,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<ChargesheetDetails> empp = new ArrayList<>();
					chargeSheetList.stream().forEach(ss->{
						if(ss.getCaseDetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setChargeSheetList(empp);
				});
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserInAndIdNotIn(chargeSheetList, userList,
						pageableall,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<ChargesheetDetails> empp = new ArrayList<>();
					chargeSheetList.stream().forEach(ss->{
						if(ss.getCaseDetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setChargeSheetList(empp);
				});
			} else {
				noticePages = caseDetailsRepo.findDistinctIdByCurrentUserInAndIdNotIn(userList, pageable,caseId);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findDistinctIdByCurrentUserInAndIdNotIn(userList, pageableall,caseId);
			}

		}

		else {
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
					&& (StringUtils.isBlank(fromdate) && StringUtils.isBlank(todate))) {
				model.addAttribute("search", search);
				List<EmployeeDetails> empList = employeedetailsrepo
						.findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
								search, search, search);
				if (empList.isEmpty()) {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), customsearch, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), customsearch, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), customsearch, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), customsearch, pageableall,caseId);
					} else if (search.contains("lessthan5")) {
						List<ChargesheetDetails> chargeList = chargesheetRepo
								.findByChargesheetDateGreaterThanEqualAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
										LocalDate.now().minusYears(5), true, customsearch);
						noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserAndIdNotIn(chargeList, currentUser,
								pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<ChargesheetDetails> empp = new ArrayList<>();
							chargeList.stream().forEach(ss->{
								if(ss.getCaseDetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setChargeSheetList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserAndIdNotIn(chargeList, currentUser,
								pageableall,caseId);
						allCases.getContent().stream().forEach(s->{
							List<ChargesheetDetails> empp = new ArrayList<>();
							chargeList.stream().forEach(ss->{
								if(ss.getCaseDetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setChargeSheetList(empp);
						});
					} else if (search.contains("morethan5")) {
						List<ChargesheetDetails> chargeList = chargesheetRepo
								.findByChargesheetDateLessThanAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
										LocalDate.now().minusYears(5), true, customsearch);
						noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserAndIdNotIn(chargeList, currentUser,
								pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<ChargesheetDetails> empp = new ArrayList<>();
							chargeList.stream().forEach(ss->{
								if(ss.getCaseDetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setChargeSheetList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserAndIdNotIn(chargeList, currentUser,
								pageableall,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<ChargesheetDetails> empp = new ArrayList<>();
							chargeList.stream().forEach(ss->{
								if(ss.getCaseDetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setChargeSheetList(empp);
						});
					} else if(search.contains("retired")) {
						List<EmployeeDetails> empListRetired = employeedetailsrepo.findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(LocalDate.now(),customsearch,caseId);
						model.addAttribute("retireEmployees", empListRetired.size());
						noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListRetired,currentUser, pageable,caseId);
						noticePages.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =  caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListRetired,currentUser, pageableall,caseId);
						allCases.getContent().stream().forEach(s->{
							List<EmployeeDetails> empp = new ArrayList<>();
							empListRetired.stream().forEach(ss->{
								if(ss.getCasedetails().getId()==s.getId()) {
									empp.add(ss);
								}	
							});
							s.setEmployeeList(empp);
						});
						model.addAttribute("retireEmployees", allCases.getContent().stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					} else {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, pageableall,caseId);
					}
				} else {
					noticePages = caseDetailsRepo
							.findDistinctIdByCurrentUserAndEmployeeListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
									currentUser, empList, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, pageable,caseId);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findDistinctIdByCurrentUserAndEmployeeListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
									currentUser, empList, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, pageableall,caseId);
				}
			} else if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
					&& (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate))) {
				model.addAttribute("search", search);
				model.addAttribute("fromdate", fromdate);
				model.addAttribute("todate", todate);
				List<ChargesheetDetails> chargeSheetList = chargesheetRepo
						.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(LocalDate.parse(fromdate),
								LocalDate.parse(todate));
				List<EmployeeDetails> empList = employeedetailsrepo
						.findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
								search, search, search);
				if (empList.isEmpty() && chargeSheetList.isEmpty()) {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), search, pageableall,caseId);
					} else {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, pageableall,caseId);
					}
				} else if (!empList.isEmpty() && chargeSheetList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
									currentUser, search, empList, currentUser, search, empList, currentUser, search,
									empList, currentUser, search, empList, pageable,caseId);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListInAndIdNotIn(
									currentUser, search, empList, currentUser, search, empList, currentUser, search,
									empList, currentUser, search, empList, pageableall,caseId);
				} else if (empList.isEmpty() && !chargeSheetList.isEmpty()) {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), chargeSheetList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), chargeSheetList, search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), chargeSheetList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), chargeSheetList, search, pageableall,caseId);
					} else {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
										currentUser, search, chargeSheetList, currentUser, search, chargeSheetList,
										currentUser, search, chargeSheetList, currentUser, search, chargeSheetList,
										pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndIdNotIn(
										currentUser, search, chargeSheetList, currentUser, search, chargeSheetList,
										currentUser, search, chargeSheetList, currentUser, search, chargeSheetList,
										pageableall,caseId);
					}
				} else {
					if (search.contains("major")) {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), chargeSheetList, empList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), chargeSheetList, empList, search, pageableall,caseId);
					} else if (search.contains("minor")) {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), chargeSheetList, empList, search, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndIdNotIn(
										currentUser, Arrays.asList(2L), chargeSheetList, empList, search, pageableall,caseId);
					} else {
						noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
										currentUser, search, chargeSheetList, empList, currentUser, search,
										chargeSheetList, empList, currentUser, search, chargeSheetList, empList,
										currentUser, search, chargeSheetList, empList, pageable,caseId);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInAndIdNotIn(
										currentUser, search, chargeSheetList, empList, currentUser, search,
										chargeSheetList, empList, currentUser, search, chargeSheetList, empList,
										currentUser, search, chargeSheetList, empList, pageableall,caseId);
					}
				}

			} else if ((StringUtils.isNotBlank(fromdate) && StringUtils.isNoneBlank(todate))
					&& StringUtils.isBlank(search)) {
				model.addAttribute("fromdate", fromdate);
				model.addAttribute("todate", todate);
				List<ChargesheetDetails> chargeSheetList = chargesheetRepo
						.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(LocalDate.parse(fromdate),
								LocalDate.parse(todate));
				noticePages = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserAndIdNotIn(chargeSheetList, currentUser,
						pageable,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<ChargesheetDetails> empp = new ArrayList<>();
					chargeSheetList.stream().forEach(ss->{
						if(ss.getCaseDetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setChargeSheetList(empp);
				});
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserAndIdNotIn(chargeSheetList, currentUser,
						pageableall,caseId);
				allCases.getContent().stream().forEach(s->{
					List<ChargesheetDetails> empp = new ArrayList<>();
					chargeSheetList.stream().forEach(ss->{
						if(ss.getCaseDetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setChargeSheetList(empp);
				});
			} else {
				noticePages = caseDetailsRepo.findDistinctIdByCurrentUserAndIdNotIn(currentUser, pageable,caseId);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findDistinctIdByCurrentUserAndIdNotIn(currentUser, pageableall,caseId);
			}

		}
		//// -------------------------------///-----------------

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
		Map<String, CaseDetails> mapEn = new TreeMap<>();
		noticePages.getContent().forEach(s -> {
			List<String> employeeNames = new ArrayList<>();
			List<EmployeeDetails> empList = employeedetailsrepo.findByCasedetailsCaseNo(s.getCaseNo());
			List<ChargesheetDetails> charge = chargesheetRepo.findByCaseDetailsCaseNo(s.getCaseNo());
			if (charge != null && !charge.isEmpty() && charge.get(0).getChargesheetIssued()
					&& charge.get(0).getChargesheetDate() != null) {
				s.setChargesheetDate(charge.get(0).getChargesheetDate().toString());
				Period pp = Period.between(charge.get(0).getChargesheetDate(), LocalDate.now());
				String durationOfCase = pp.getYears()+" Years,"+pp.getMonths()+" Months,"+pp.getDays()+" Days";
				s.setDurationOfCase(durationOfCase);
			}
			if (!empList.isEmpty()) {
				empList.parallelStream().forEach(ss -> {
					String fullName = "";
					if (StringUtils.isNotBlank(ss.getFirstName())) {
						fullName += ss.getFirstName();
					}
					if (StringUtils.isNotBlank(ss.getMiddleName())) {
						fullName += " " + ss.getMiddleName();
					}
					if (StringUtils.isNotBlank(ss.getLastName())) {
						fullName += " " + ss.getLastName();
					}
					employeeNames.add(fullName);
				});
				s.setEmployeeNames(employeeNames);
			}
			mapEn.put(s.getCaseNo(), s);
		});

		model.addAttribute("noticelist", mapEn.values().stream().collect(Collectors.toList()));
		if (viewreport != null) {
			model.addAttribute("viewreport", Boolean.parseBoolean(viewreport));
		} else {
			model.addAttribute("viewreport", false);
		}
		
		
		 model.addAttribute("totalCasesLabel",messageSource.getMessage("deptenq.totalcases.label", null, loc.resolveLocale(request)));
		 model.addAttribute("totalEmployeeCountLabel",messageSource.getMessage("deptenq.totalemployee.label", null, loc.resolveLocale(request)));
					
		
	return "DeptInquiryViews/reportswithfilter";

	}
}
