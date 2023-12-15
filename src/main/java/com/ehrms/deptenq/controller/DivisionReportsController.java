package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
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
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.IDivisionRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;
import com.ehrms.deptenq.utility.AsyncExecutors;
import com.ehrms.deptenq.utility.SplitUtility;

@Controller
@RequestMapping("departmentalEnquiry")
public class DivisionReportsController {



	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;

	



	@Autowired
	private CaseDetailsRepository caseDetailsRepo;


	@Autowired
	private SubDepartmentRepository subDepartmentRepo;



	@Autowired
	private IDivisionRepository divisionRepository;


	private static final Logger LOG = LoggerFactory.getLogger(DivisionReportsController.class);
	
	/* written code on autowired on 02/11/2023 by vijay */
	@Autowired
	private  DepartmentConstants departmentconstants;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LocaleResolver loc;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AsyncExecutors exe;
	
	@Autowired
	private SplitUtility util;

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/reportCaseDtlsDivisionWisePending/{page}")
	public String reportCaseDtlsDivisionWisePending(Model model, @PathVariable("page") int page,
			@RequestParam(name = "divid", required = false) String divid,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

		LOG.info("hi this is the reports  Details Records For search  page.......");
		
		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}
		long starttime1 = System.currentTimeMillis();
//		List<CaseDetails> finalOut = caseDetailsRepo.findAll();		
		long casecount = caseDetailsRepo.count();
		List<Integer> largeList = util.getSplitdata((int)casecount);
		List<Callable<String>> callList = new ArrayList<>();
		List<CaseDetails> finalOut = new ArrayList<>();
		if(!largeList.isEmpty()) {
			for(Integer i=1;i<=largeList.size();i++) {
				final Integer index = i ;
				callList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {																		
						Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
						Page<CaseDetails> caseList = caseDetailsRepo.findAll(pageable);
						finalOut.addAll(caseList.getContent());
						return null;
					}
				});
			}
		}
		ExecutorService executorService = exe.getExe();
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
		LOG.info("First Step time required :"+TimeUnit.MILLISECONDS.toSeconds(endtime1-starttime1));
	
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

		model.addAttribute("divisiondata", divisionRepository.findAll());

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
		if(divid == null) {
			divid = "";
		}

		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
		List<CaseDetails> allCases = new ArrayList<>();
		model.addAttribute("divid", divid);
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
		
			callList = new ArrayList<>();
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Pune division wise data count */
					List<CaseDetails> pune = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(6),caseId);
					model.addAttribute("puneCases", pune.size());
					model.addAttribute("puneEmployeeCount", pune.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {
					/* Aurangabad division wise data count */
					List<CaseDetails> aurangabad = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(2),caseId);
					model.addAttribute("aurangabad", aurangabad.size());
					model.addAttribute("aurangabadEmployeeCount",
							aurangabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {
					/* Nagpur division wise data count */
					List<CaseDetails> nagpur = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(4),caseId);
					model.addAttribute("nagpur", nagpur.size());
					model.addAttribute("nagpurEmployeeCount", nagpur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {
					/* Nasik division wise data count */

					List<CaseDetails> nasik = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(5),caseId);
					model.addAttribute("nasik", nasik.size());
					model.addAttribute("nasikEmployeeCount", nasik.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {
					/* amravati division wise data count */

					List<CaseDetails> amravati = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(1),caseId);
					model.addAttribute("amravati", amravati.size());
					model.addAttribute("amravatiEmployeeCount",
							amravati.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {
					/* Nasik division wise data count */

					List<CaseDetails> kokan = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(3),caseId);
					model.addAttribute("kokan", kokan.size());
					model.addAttribute("kokanEmployeeCount", kokan.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});

			


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

		

			

			

			// List<CaseDetails> total = caseDetailsRepo.find();

			noticePages = caseDetailsRepo.findByIdNotIn(pageable,caseId);
			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);

			
			callList = new ArrayList<>();
			
			largeList = util.getSplitdata((int)noticePages.getTotalElements());
			if(!largeList.isEmpty()) {
				for(Integer i=1;i<=largeList.size();i++) {
					final Integer index1 = i ;
					callList.add(new Callable<String>() {
						@Override
						public String call() throws Exception {																		
							Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
							Page<CaseDetails> caseList = caseDetailsRepo.findByIdNotIn(pageable,caseId);
							allCases.addAll(caseList.getContent());
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
			
			/* Employee count down on Depend on Departments code written (21/11/2022) */
	//		allCases = caseDetailsRepo.findByIdNotIn(allPages,caseId);
			model.addAttribute("empCountTotal", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

			long countCases = allCases.size();
			model.addAttribute("totalCases", countCases);

			if (StringUtils.isNotBlank(divid)) {
				final String dividx = divid;
				// model.addAttribute("ruleid",divid );
				noticePages = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(divid), pageable,caseId);
//				allCases = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(divid), allPages,caseId);
				callList = new ArrayList<>();
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseDetailsRepo.findByDivision_IdAndIdNotIn(Long.valueOf(dividx), pageable,caseId);
								allCases.addAll(caseList.getContent());
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
				
				// model.addAttribute("empCount",
				// allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());

			} else {
				noticePages = caseDetailsRepo.findByIdNotIn(pageable,caseId);
//				allCases = caseDetailsRepo.findByIdNotIn(allPages,caseId);
				callList = new ArrayList<>();
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseDetailsRepo.findByIdNotIn(pageable,caseId);
								allCases.addAll(caseList.getContent());
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
				
			}
			model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
//			/* Pune division wise data count */
//			GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
//			List<CaseDetails> pune = caseDetailsRepo.findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(6),org,caseId);
//			model.addAttribute("puneCases", pune.size());
//			model.addAttribute("puneEmployeeCount", pune.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
//
//			/* Aurangabad division wise data count */
//			List<CaseDetails> aurangabad = caseDetailsRepo.findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(2),org,caseId);
//			model.addAttribute("aurangabad", aurangabad.size());
//			model.addAttribute("aurangabadEmployeeCount",
//					aurangabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
//
//			/* Nagpur division wise data count */
//
//			List<CaseDetails> nagpur = caseDetailsRepo.findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(4),org,caseId);
//			model.addAttribute("nagpur", nagpur.size());
//			model.addAttribute("nagpurEmployeeCount", nagpur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
//
//			/* Nasik division wise data count */
//
//			List<CaseDetails> nasik = caseDetailsRepo.findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(5),org,caseId);
//			model.addAttribute("nasik", nasik.size());
//			model.addAttribute("nasikEmployeeCount", nasik.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
//
//			/* amravati division wise data count */
//
//			List<CaseDetails> amravati = caseDetailsRepo.findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(1),org,caseId);
//			model.addAttribute("amravati", amravati.size());
//			model.addAttribute("amravatiEmployeeCount",
//					amravati.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
//
//			/* Nasik division wise data count */
//
//			List<CaseDetails> kokan = caseDetailsRepo.findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(3),org,caseId);
//			model.addAttribute("kokan", kokan.size());
//			model.addAttribute("kokanEmployeeCount", kokan.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
//
//			// List<CaseDetails> total = caseDetailsRepo.find();
//
//			noticePages = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,pageable,caseId);
//			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);
//
//			/* Employee count down on Depend on Departments code written (21/11/2022) */
//			allCases = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,allPages,caseId);
//			model.addAttribute("empCountTotal", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
//
//			long countCases = allCases.getContent().size();
//			model.addAttribute("totalCases", countCases);
//
//			if (StringUtils.isNotBlank(divid)) {
//				// model.addAttribute("ruleid",divid );
//				noticePages = caseDetailsRepo.findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(divid),org, pageable,caseId);
//				allCases = caseDetailsRepo.findByDivision_IdAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(Long.valueOf(divid),org, allPages,caseId);
//				// model.addAttribute("empCount",
//				// allCases.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
//
//			} else {
//				noticePages = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,pageable,caseId);
//				allCases = caseDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,allPages,caseId);
//			}
//			model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
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
			final List<User> userListx = userList;
			callList = new ArrayList<>();
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Pune division wise data count */
					List<CaseDetails> pune = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(userListx, Long.valueOf(6),caseId);
					model.addAttribute("puneCases", pune.size());
					model.addAttribute("puneEmployeeCount", pune.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Aurangabad division wise data count */
					List<CaseDetails> aurangabad = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(userListx, Long.valueOf(2),caseId);
					model.addAttribute("aurangabad", aurangabad.size());
					model.addAttribute("aurangabadEmployeeCount",		
							aurangabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Nagpur division wise data count */

					List<CaseDetails> nagpur = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(userListx, Long.valueOf(4),caseId);
					model.addAttribute("nagpur", nagpur.size());
					model.addAttribute("nagpurEmployeeCount", nagpur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});

		
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Nasik division wise data count */

					List<CaseDetails> nasik = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(userListx, Long.valueOf(5),caseId);
					model.addAttribute("nasik", nasik.size());
					model.addAttribute("nasikEmployeeCount", nasik.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* amravati division wise data count */

					List<CaseDetails> amravati = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(userListx, Long.valueOf(1),caseId);
					model.addAttribute("amravati", amravati.size());
					model.addAttribute("amravatiEmployeeCount",
							amravati.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {


					/* Nasik division wise data count */

					List<CaseDetails> kokan = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(userListx, Long.valueOf(3),caseId);
					model.addAttribute("kokan", kokan.size());
					model.addAttribute("kokanEmployeeCount", kokan.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			
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
			



			




			// List<CaseDetails> total = caseDetailsRepo.find();

			noticePages = caseDetailsRepo.findByCurrentUserIn(userList,pageable);
//			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);

			/* Employee count down on Depend on Departments code written (21/11/2022) */
//			allCases = caseDetailsRepo.findByCurrentUserInAndIdNotIn(userList,allPages,caseId);
			callList = new ArrayList<>();
			largeList = util.getSplitdata((int)noticePages.getTotalElements());
		
			if(!largeList.isEmpty()) {
				for(Integer i=1;i<=largeList.size();i++) {
					final Integer index1 = i ;
					callList.add(new Callable<String>() {
						@Override
						public String call() throws Exception {																		
							Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
							Page<CaseDetails> caseList = caseDetailsRepo.findByCurrentUserInAndIdNotIn(userListx,pageable,caseId);
							allCases.addAll(caseList.getContent());
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
			
			
			model.addAttribute("empCountTotal", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

			long countCases = caseDetailsRepo.countDistinctIdByCurrentUserInAndIdNotIn(userList,caseId);
			model.addAttribute("totalCases", countCases);
			noticePages = caseDetailsRepo.findDistinctIdByCurrentUserInAndIdNotIn(userList, pageable,caseId);
			if (StringUtils.isNotBlank(divid)) {
				final String dividx = divid;
				model.addAttribute("divid", divid);
				noticePages = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(
						userList, Long.valueOf(divid), pageable,caseId);
//				allCases = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(
//						userList, Long.valueOf(divid), allPages,caseId);
				callList = new ArrayList<>();
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseDetailsRepo.findByCurrentUserInAndDivision_IdAndIdNotIn(
										userListx, Long.valueOf(dividx), pageable,caseId);
								allCases.addAll(caseList.getContent());
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
				
				
				model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
			} else {
				model.addAttribute("divid", "");
				noticePages = caseDetailsRepo.findByCurrentUserInAndIdNotIn(
						userList, pageable,caseId);
//				allCases = caseDetailsRepo.findByCurrentUserInAndIdNotIn(
//						userList, allPages,caseId);
				callList = new ArrayList<>();
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseDetailsRepo.findByCurrentUserInAndIdNotIn(
										userListx, pageable,caseId);
								allCases.addAll(caseList.getContent());
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
				
				model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
			}
		}

		else {
			
			callList = new ArrayList<>();
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Pune division wise data count */
					List<CaseDetails> pune = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(currentUser, Long.valueOf(6),caseId);
					model.addAttribute("puneCases", pune.size());
					model.addAttribute("puneEmployeeCount", pune.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Aurangabad division wise data count */
					List<CaseDetails> aurangabad = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(currentUser,
							Long.valueOf(2),caseId);
					model.addAttribute("aurangabad", aurangabad.size());
					model.addAttribute("aurangabadEmployeeCount",
							aurangabad.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Nagpur division wise data count */

					List<CaseDetails> nagpur = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(currentUser, Long.valueOf(4),caseId);
					model.addAttribute("nagpur", nagpur.size());
					model.addAttribute("nagpurEmployeeCount", nagpur.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Nasik division wise data count */

					List<CaseDetails> nasik = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(currentUser, Long.valueOf(5),caseId);
					model.addAttribute("nasik", nasik.size());
					model.addAttribute("nasikEmployeeCount", nasik.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* amravati division wise data count */

					List<CaseDetails> amravati = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(currentUser, Long.valueOf(1),caseId);
					model.addAttribute("amravati", amravati.size());
					model.addAttribute("amravatiEmployeeCount",
					amravati.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			
			callList.add(new Callable<String>() {

				@Override
				public String call() throws Exception {

					/* Nasik division wise data count */

					List<CaseDetails> kokan = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(currentUser, Long.valueOf(3),caseId);
					model.addAttribute("kokan", kokan.size());
					model.addAttribute("kokanEmployeeCount", kokan.stream().mapToLong(s -> s.getEmployeeList().size()).sum());
					return null;
				}
			});
			
			


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
		

		

			

			

		

			

			noticePages = caseDetailsRepo.findByCurrentUserAndIdNotIn(currentUser,pageable,caseId);
			Pageable allPages = PageRequest.of(0, Integer.MAX_VALUE, sort);

			/* Employee count down on Depend on Departments code written (21/11/2022) */
//			allCases = caseDetailsRepo.findByCurrentUserAndIdNotIn(currentUser,allPages,caseId);
			callList = new ArrayList<>();
			largeList = util.getSplitdata((int)noticePages.getTotalElements());
			if(!largeList.isEmpty()) {
				for(Integer i=1;i<=largeList.size();i++) {
					final Integer index1 = i ;
					callList.add(new Callable<String>() {
						@Override
						public String call() throws Exception {																		
							Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
							Page<CaseDetails> caseList = caseDetailsRepo.findByCurrentUserAndIdNotIn(currentUser,pageable,caseId);
							allCases.addAll(caseList.getContent());
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
			model.addAttribute("empCountTotal", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());

			// List<CaseDetails> total = caseDetailsRepo.find();

			long countCases = caseDetailsRepo.countDistinctIdByCurrentUserAndIdNotIn(
					currentUser,caseId);
			model.addAttribute("totalCases", countCases);
			if (StringUtils.isNotBlank(divid)) {
				final String dividx = divid;
				model.addAttribute("divid", divid);
				noticePages = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(
						currentUser, Long.valueOf(divid), pageable,caseId);
				callList = new ArrayList<>();
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(
										currentUser, Long.valueOf(dividx), pageable,caseId);
								allCases.addAll(caseList.getContent());
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
//				allCases = caseDetailsRepo.findByCurrentUserAndDivision_IdAndIdNotIn(
//						currentUser, Long.valueOf(divid), allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
			} else {
				model.addAttribute("divid", "");
				noticePages = caseDetailsRepo.findByCurrentUserAndIdNotIn(
						currentUser, pageable,caseId);
				callList = new ArrayList<>();
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseDetailsRepo.findByCurrentUserAndIdNotIn(
										currentUser, pageable,caseId);
								allCases.addAll(caseList.getContent());
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
				
//				allCases = caseDetailsRepo.findByCurrentUserAndIdNotIn(
//						currentUser, allPages,caseId);
				model.addAttribute("empCount", allCases.stream().mapToLong(ss -> ss.getEmployeeList().size()).sum());
			}

		}
		//// -------------------------------///-----------------

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
		 endtime1 = System.currentTimeMillis();
			LOG.info("First Step time required :"+TimeUnit.MILLISECONDS.toSeconds(endtime1-starttime1));
		return "DeptInquiryViews/ReportsForRevenueDivisionWisePendingCases";

	}

	/*
	 * // Added on 17-11-2022 case details report page
	 * 
	 * @GetMapping("/reportCaseDtlsDistrictWisePending/{page}") public String
	 * reportCaseDtlsDistrictWisePending(Model model, @PathVariable("page") int
	 * page,
	 * 
	 * @RequestParam(name = "rule", required = false) String rule,
	 * 
	 * @RequestParam(name = "search", required = false) String search,
	 * 
	 * @RequestParam(name = "misconduct", required = false) String misconduct,
	 * 
	 * @RequestParam(name = "viewreport", required = false) String viewreport,
	 * 
	 * @RequestParam(name = "fromdate", required = false) String fromdate,
	 * 
	 * @RequestParam(name = "todate", required = false) String todate,
	 * 
	 * @RequestParam(name = "sort-field", required = false) String sortField,
	 * 
	 * @RequestParam(name = "sort-dir", required = false) String sortDirection) {
	 * 
	 * LOG.info("hi this is the reports  Details Records For search  page.......");
	 * 
	 * 
	 * // model.addAttribute("districtdata", districtsrepository.findAll()); //
	 * CaseDetails cases =new CaseDetails();
	 * model.addAttribute("cases",caseDetailsRepo.findAll());
	 * 
	 * 
	 * String customsearch = null; if (search != null && search.contains("major")) {
	 * customsearch = search.split("splitting")[1]; } if (search != null &&
	 * search.contains("minor")) { customsearch = search.split("splitting")[1]; } if
	 * (search != null && search.contains("lessthan5")) { customsearch =
	 * search.split("splitting")[1]; } if (search != null &&
	 * search.contains("morethan5")) { customsearch = search.split("splitting")[1];
	 * } if (search != null && search.contains("retired")) { customsearch =
	 * search.split("splitting")[1]; } if (customsearch != null &&
	 * customsearch.equalsIgnoreCase("null")) { customsearch = ""; }
	 * 
	 * final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
	 * Sort.by(sortField).ascending() : Sort.by(sortField).descending();
	 * model.addAttribute("sortField", sortField); model.addAttribute("sortDir",
	 * sortDirection); model.addAttribute("reverseSortDir",
	 * sortDirection.equals("asc") ? "desc" : "asc");
	 * 
	 * // pagination Writting coding int index = 1;
	 * 
	 * if (page != 0) { index = (page * 10) - (10 - 1);
	 * model.addAttribute("pageIndex", index); }
	 * 
	 * if (page < 0) { page = 1; }
	 * 
	 * Pageable pageable = PageRequest.of(page - 1, 10, sort);
	 * 
	 * // added on 29/09/2022 , depend on employee and department we are getting
	 * grid // list values.... User currentUser = userService.getCurrentUserPojo();
	 * Page<CaseDetails> noticePages = null; Page<CaseDetails> allCases = null;
	 * 
	 * if
	 * (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList
	 * ()) .contains("ROLE_SUPERADMIN")) { if (StringUtils.isNotBlank(search) &&
	 * !search.equalsIgnoreCase("null") && (StringUtils.isBlank(fromdate) &&
	 * StringUtils.isBlank(todate))) { model.addAttribute("search", search);
	 * List<EmployeeDetails> empList = employeedetailsrepo
	 * .findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
	 * search, search, search); if (empList.isEmpty()) { if
	 * (search.contains("major")) { if (StringUtils.isNotBlank(customsearch)) {
	 * noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), customsearch, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), customsearch, pageableall); } else { noticePages =
	 * caseDetailsRepo.findDistinctIdByRuleApplicableIdNotIn(Arrays.asList(2L),
	 * pageable); Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
	 * allCases = caseDetailsRepo.findByRuleApplicableIdNotIn(Arrays.asList(2L),
	 * pageableall); } } else if (search.contains("minor")) { if
	 * (StringUtils.isNotBlank(customsearch)) { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), customsearch, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), customsearch, pageableall); } else { noticePages =
	 * caseDetailsRepo.findDistinctIdByRuleApplicableIdIn(Arrays.asList(2L),
	 * pageable); Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
	 * allCases = caseDetailsRepo.findByRuleApplicableIdIn(Arrays.asList(2L),
	 * pageableall); } } else if (search.contains("lessthan5")) { if (customsearch
	 * != null && !customsearch.equalsIgnoreCase("null")) { List<ChargesheetDetails>
	 * chargeList = chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now().minusYears(5), true, customsearch); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeList, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeList, pageableall); }
	 * else { List<ChargesheetDetails> chargeList = chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetIssued(
	 * LocalDate.now().minusYears(5), true); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeList, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeList, pageableall);
	 * 
	 * } } else if (search.contains("morethan5")) { if (customsearch != null &&
	 * !customsearch.equalsIgnoreCase("null")) { List<ChargesheetDetails> chargeList
	 * = chargesheetRepo
	 * .findByChargesheetDateLessThanAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now().minusYears(5), true, customsearch); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeList, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeList, pageableall); }
	 * else { List<ChargesheetDetails> chargeList = chargesheetRepo
	 * .findByChargesheetDateLessThanAndChargesheetIssued(LocalDate.now().minusYears
	 * (5), true); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeList, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeList, pageableall); }
	 * } else if (search.contains("retired")) { if (customsearch != null &&
	 * !customsearch.equalsIgnoreCase("null")) { List<EmployeeDetails>
	 * empListRetired = employeedetailsrepo
	 * .findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now(), customsearch); model.addAttribute("retireEmployees",
	 * empListRetired.size()); noticePages =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageableall);
	 * } else { List<EmployeeDetails> empListRetired = employeedetailsrepo
	 * .findBySuperannuationDateLessThan(LocalDate.now());
	 * model.addAttribute("retireEmployees", empListRetired.size()); noticePages =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageableall);
	 * } } else { noticePages = caseDetailsRepo
	 * .findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * search, search, search, search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * search, search, search, search, pageableall); } } else { noticePages =
	 * caseDetailsRepo
	 * .findDistinctIdByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * empList, search, search, search, search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByEmployeeListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * empList, search, search, search, search, pageableall); }
	 * 
	 * } else if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
	 * && (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate))) {
	 * model.addAttribute("search", search); model.addAttribute("fromdate",
	 * fromdate); model.addAttribute("todate", todate); List<ChargesheetDetails>
	 * chargeSheetList = chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
	 * LocalDate.parse(fromdate), LocalDate.parse(todate)); List<EmployeeDetails>
	 * empList = employeedetailsrepo
	 * .findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
	 * search, search, search); if (empList.isEmpty() && chargeSheetList.isEmpty())
	 * { if (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findByRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), search, pageableall); } else if (search.contains("minor"))
	 * { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), search, pageableall);
	 * 
	 * } else if (search.contains("retired")) { List<EmployeeDetails> empListRetired
	 * = employeedetailsrepo
	 * .findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now(), customsearch); model.addAttribute("retireEmployees",
	 * empListRetired.size()); noticePages =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageableall);
	 * } else { noticePages = caseDetailsRepo
	 * .findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * search, search, search, search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * search, search, search, search, pageableall); } } else if (!empList.isEmpty()
	 * && chargeSheetList.isEmpty()) { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
	 * search, empList, search, empList, search, empList, search, empList,
	 * pageable); Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
	 * allCases = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCaseNoContainingIgnoreCaseAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
	 * search, empList, search, empList, search, empList, search, empList,
	 * pageableall); } else if (empList.isEmpty() && !chargeSheetList.isEmpty()) {
	 * if (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), chargeSheetList, search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), chargeSheetList, search, pageableall); } else if
	 * (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), chargeSheetList, search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), chargeSheetList, search, pageableall); } else if
	 * (search.contains("retired")) { List<EmployeeDetails> empListRetired =
	 * employeedetailsrepo
	 * .findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now(), customsearch); model.addAttribute("retireEmployees",
	 * empListRetired.size()); noticePages =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageableall);
	 * } else { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
	 * search, chargeSheetList, search, chargeSheetList, search, chargeSheetList,
	 * search, chargeSheetList, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
	 * search, chargeSheetList, search, chargeSheetList, search, chargeSheetList,
	 * search, chargeSheetList, pageableall); }
	 * 
	 * } else { if (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), chargeSheetList, empList, search, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), chargeSheetList, empList, search, pageableall); } else if
	 * (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), chargeSheetList, empList, search, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * Arrays.asList(2L), chargeSheetList, empList, search, pageableall); } else if
	 * (search.contains("retired")) { List<EmployeeDetails> empListRetired =
	 * employeedetailsrepo
	 * .findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now(), customsearch); model.addAttribute("retireEmployees",
	 * empListRetired.size()); noticePages =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByEmployeeListIn(empListRetired, pageableall);
	 * } else { noticePages = caseDetailsRepo
	 * .findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
	 * search, chargeSheetList, empList, search, chargeSheetList, empList, search,
	 * chargeSheetList, empList, search, chargeSheetList, empList, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
	 * search, chargeSheetList, empList, search, chargeSheetList, empList, search,
	 * chargeSheetList, empList, search, chargeSheetList, empList, pageableall); } }
	 * 
	 * } else { if ((StringUtils.isNotBlank(fromdate) &&
	 * StringUtils.isNoneBlank(todate)) && StringUtils.isBlank(search)) {
	 * model.addAttribute("fromdate", fromdate); model.addAttribute("todate",
	 * todate); List<ChargesheetDetails> chargeSheetList = chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
	 * LocalDate.parse(fromdate), LocalDate.parse(todate)); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeSheetList, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListIn(chargeSheetList,
	 * pageableall);
	 * 
	 * } else { noticePages = caseDetailsRepo.findAll(pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findAll(pageableall); } }
	 * 
	 * } else if
	 * (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList
	 * ()) .contains("ROLE_DEPARTMENTADMIN")) { // List<SubDepartment>
	 * subDepartmentList = ; List<User> userList = userRepo.findBySubDepartmentIn(
	 * subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().
	 * getGlobalOrgId())); userList.add(currentUser); // noticePages =
	 * caseDetailsRepo.findByCurrentUserIn(userList, pageable); if
	 * (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null") &&
	 * (StringUtils.isBlank(fromdate) && StringUtils.isBlank(todate))) {
	 * model.addAttribute("search", search); List<EmployeeDetails> empList =
	 * employeedetailsrepo
	 * .findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
	 * search, search, search); if (empList.isEmpty()) { if
	 * (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), customsearch, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), customsearch, pageableall); } else if
	 * (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), customsearch, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), customsearch, pageableall); } else if
	 * (search.contains("lessthan5")) { List<ChargesheetDetails> chargeList =
	 * chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now().minusYears(5), true, customsearch); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserIn(chargeList,
	 * userList, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findByChargeSheetListInAndCurrentUserIn(chargeList, userList,
	 * pageableall); } else if (search.contains("morethan5")) {
	 * List<ChargesheetDetails> chargeList = chargesheetRepo
	 * .findByChargesheetDateLessThanAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now().minusYears(5), true, customsearch); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserIn(chargeList,
	 * userList, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserIn(chargeList,
	 * userList, pageableall); } else if (search.contains("retired")) {
	 * List<EmployeeDetails> empListRetired = employeedetailsrepo
	 * .findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now(), customsearch); model.addAttribute("retireEmployees",
	 * empListRetired.size()); noticePages =
	 * caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserIn(
	 * empListRetired, userList, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserIn(
	 * empListRetired, userList, pageableall); } else { noticePages =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, search, userList, search, userList, search, userList, search,
	 * pageable); Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
	 * allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, search, userList, search, userList, search, userList, search,
	 * pageableall); } } else { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndEmployeeListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, empList, userList, search, userList, search, userList, search,
	 * userList, search, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndEmployeeListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, empList, userList, search, userList, search, userList, search,
	 * userList, search, pageableall); } } else if (StringUtils.isNotBlank(search)
	 * && !search.equalsIgnoreCase("null") && (StringUtils.isNotBlank(fromdate) &&
	 * StringUtils.isNotBlank(todate))) { model.addAttribute("search", search);
	 * model.addAttribute("fromdate", fromdate); model.addAttribute("todate",
	 * todate); List<ChargesheetDetails> chargeSheetList = chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
	 * LocalDate.parse(fromdate), LocalDate.parse(todate)); List<EmployeeDetails>
	 * empList = employeedetailsrepo
	 * .findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
	 * search, search, search); if (empList.isEmpty() && chargeSheetList.isEmpty())
	 * { if (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), search, pageableall); } else if
	 * (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), search, pageableall); } else { noticePages =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, search, userList, search, userList, search, userList, search,
	 * pageable); Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
	 * allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, search, userList, search, userList, search, userList, search,
	 * pageableall); } } else if (!empList.isEmpty() && chargeSheetList.isEmpty()) {
	 * noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
	 * userList, search, empList, userList, search, empList, userList, search,
	 * empList, userList, search, empList, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
	 * userList, search, empList, userList, search, empList, userList, search,
	 * empList, userList, search, empList, pageableall); } else if
	 * (empList.isEmpty() && !chargeSheetList.isEmpty()) { if
	 * (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), chargeSheetList, search, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), chargeSheetList, search, pageableall); } else if
	 * (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), chargeSheetList, search, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), chargeSheetList, search, pageableall); } else {
	 * noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
	 * userList, search, chargeSheetList, userList, search, chargeSheetList,
	 * userList, search, chargeSheetList, userList, search, chargeSheetList,
	 * pageable); Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
	 * allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
	 * userList, search, chargeSheetList, userList, search, chargeSheetList,
	 * userList, search, chargeSheetList, userList, search, chargeSheetList,
	 * pageableall); } } else { if (search.contains("major")) { noticePages =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), chargeSheetList, empList, search, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), chargeSheetList, empList, search, pageableall);
	 * } else if (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), chargeSheetList, empList, search, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * userList, Arrays.asList(2L), chargeSheetList, empList, search, pageableall);
	 * } else { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
	 * userList, search, chargeSheetList, empList, userList, search,
	 * chargeSheetList, empList, userList, search, chargeSheetList, empList,
	 * userList, search, chargeSheetList, empList, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
	 * userList, search, chargeSheetList, empList, userList, search,
	 * chargeSheetList, empList, userList, search, chargeSheetList, empList,
	 * userList, search, chargeSheetList, empList, pageableall); } }
	 * 
	 * } else if ((StringUtils.isNotBlank(fromdate) &&
	 * StringUtils.isNoneBlank(todate)) && StringUtils.isBlank(search)) {
	 * model.addAttribute("fromdate", fromdate); model.addAttribute("todate",
	 * todate); List<ChargesheetDetails> chargeSheetList = chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
	 * LocalDate.parse(fromdate), LocalDate.parse(todate)); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserIn(
	 * chargeSheetList, userList, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUserIn(
	 * chargeSheetList, userList, pageableall); } else { noticePages =
	 * caseDetailsRepo.findByCurrentUserIn(userList, pageable); Pageable pageableall
	 * = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findByCurrentUserIn(userList, pageableall); }
	 * 
	 * }
	 * 
	 * else { if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")
	 * && (StringUtils.isBlank(fromdate) && StringUtils.isBlank(todate))) {
	 * model.addAttribute("search", search); List<EmployeeDetails> empList =
	 * employeedetailsrepo
	 * .findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
	 * search, search, search); if (empList.isEmpty()) { if
	 * (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), customsearch, pageable); Pageable pageableall
	 * = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), customsearch, pageableall); } else if
	 * (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), customsearch, pageable); Pageable pageableall
	 * = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), customsearch, pageableall); } else if
	 * (search.contains("lessthan5")) { List<ChargesheetDetails> chargeList =
	 * chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now().minusYears(5), true, customsearch); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUser(chargeList,
	 * currentUser, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUser(chargeList,
	 * currentUser, pageableall); } else if (search.contains("morethan5")) {
	 * List<ChargesheetDetails> chargeList = chargesheetRepo
	 * .findByChargesheetDateLessThanAndChargesheetIssuedAndCaseDetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now().minusYears(5), true, customsearch); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUser(chargeList,
	 * currentUser, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUser(chargeList,
	 * currentUser, pageableall); } else if (search.contains("retired")) {
	 * List<EmployeeDetails> empListRetired = employeedetailsrepo
	 * .findBySuperannuationDateLessThanAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * LocalDate.now(), customsearch); model.addAttribute("retireEmployees",
	 * empListRetired.size()); noticePages =
	 * caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUser(empListRetired,
	 * currentUser, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUser(empListRetired,
	 * currentUser, pageableall); } else { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, search, currentUser, search, currentUser, search, currentUser,
	 * search, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, search, currentUser, search, currentUser, search, currentUser,
	 * search, pageableall); } } else { noticePages = caseDetailsRepo
	 * .findByCurrentUserAndEmployeeListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, empList, currentUser, search, currentUser, search, currentUser,
	 * search, currentUser, search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findByCurrentUserAndEmployeeListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, empList, currentUser, search, currentUser, search, currentUser,
	 * search, currentUser, search, pageableall); } } else if
	 * (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null") &&
	 * (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate))) {
	 * model.addAttribute("search", search); model.addAttribute("fromdate",
	 * fromdate); model.addAttribute("todate", todate); List<ChargesheetDetails>
	 * chargeSheetList = chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
	 * LocalDate.parse(fromdate), LocalDate.parse(todate)); List<EmployeeDetails>
	 * empList = employeedetailsrepo
	 * .findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
	 * search, search, search); if (empList.isEmpty() && chargeSheetList.isEmpty())
	 * { if (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), search, pageableall); } else if
	 * (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), search, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), search, pageableall); } else { noticePages =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, search, currentUser, search, currentUser, search, currentUser,
	 * search, pageable); Pageable pageableall = PageRequest.of(0,
	 * Integer.MAX_VALUE, sort); allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, search, currentUser, search, currentUser, search, currentUser,
	 * search, pageableall); } } else if (!empList.isEmpty() &&
	 * chargeSheetList.isEmpty()) { noticePages = caseDetailsRepo
	 * .findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
	 * currentUser, search, empList, currentUser, search, empList, currentUser,
	 * search, empList, currentUser, search, empList, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndEmployeeListIn(
	 * currentUser, search, empList, currentUser, search, empList, currentUser,
	 * search, empList, currentUser, search, empList, pageableall); } else if
	 * (empList.isEmpty() && !chargeSheetList.isEmpty()) { if
	 * (search.contains("major")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), chargeSheetList, search, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), chargeSheetList, search, pageableall); } else
	 * if (search.contains("minor")) { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), chargeSheetList, search, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), chargeSheetList, search, pageableall); } else
	 * { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
	 * currentUser, search, chargeSheetList, currentUser, search, chargeSheetList,
	 * currentUser, search, chargeSheetList, currentUser, search, chargeSheetList,
	 * pageable); Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
	 * allCases = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListIn(
	 * currentUser, search, chargeSheetList, currentUser, search, chargeSheetList,
	 * currentUser, search, chargeSheetList, currentUser, search, chargeSheetList,
	 * pageableall); } } else { if (search.contains("major")) { noticePages =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), chargeSheetList, empList, search, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdNotInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), chargeSheetList, empList, search,
	 * pageableall); } else if (search.contains("minor")) { noticePages =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), chargeSheetList, empList, search, pageable);
	 * Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableIdInAndChargeSheetListInAndEmployeeListInAndGlobalorgGlobalOrgNameContainingIgnoreCase(
	 * currentUser, Arrays.asList(2L), chargeSheetList, empList, search,
	 * pageableall); } else { noticePages = caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
	 * currentUser, search, chargeSheetList, empList, currentUser, search,
	 * chargeSheetList, empList, currentUser, search, chargeSheetList, empList,
	 * currentUser, search, chargeSheetList, empList, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo
	 * .findDistinctIdByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndCaseNoContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListInOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseAndChargeSheetListInAndEmployeeListIn(
	 * currentUser, search, chargeSheetList, empList, currentUser, search,
	 * chargeSheetList, empList, currentUser, search, chargeSheetList, empList,
	 * currentUser, search, chargeSheetList, empList, pageableall); } }
	 * 
	 * } else if ((StringUtils.isNotBlank(fromdate) &&
	 * StringUtils.isNoneBlank(todate)) && StringUtils.isBlank(search)) {
	 * model.addAttribute("fromdate", fromdate); model.addAttribute("todate",
	 * todate); List<ChargesheetDetails> chargeSheetList = chargesheetRepo
	 * .findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
	 * LocalDate.parse(fromdate), LocalDate.parse(todate)); noticePages =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUser(
	 * chargeSheetList, currentUser, pageable); Pageable pageableall =
	 * PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByChargeSheetListInAndCurrentUser(
	 * chargeSheetList, currentUser, pageableall); } else { noticePages =
	 * caseDetailsRepo.findDistinctIdByCurrentUser(currentUser, pageable); Pageable
	 * pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort); allCases =
	 * caseDetailsRepo.findDistinctIdByCurrentUser(currentUser, pageableall); }
	 * 
	 * } //// -------------------------------///-----------------
	 * 
	 * model.addAttribute("caseDetailsAllRecordsreport", allCases);
	 * model.addAttribute("caseDetailsAllRecords", noticePages);
	 * model.addAttribute("currentPage", page); int begin = Math.max(1, page - 5);
	 * if (page >= 5) { begin = page; } int end = Math.min(begin + 4,
	 * noticePages.getTotalPages()); model.addAttribute("begin", begin);
	 * model.addAttribute("end", end); model.addAttribute("totalPages",
	 * noticePages.getTotalPages()); model.addAttribute("totalItems",
	 * noticePages.getTotalElements()); Map<String, CaseDetails> mapEn = new
	 * TreeMap<>(); noticePages.getContent().forEach(s -> { List<String>
	 * employeeNames = new ArrayList<>(); List<EmployeeDetails> empList =
	 * employeedetailsrepo.findByCasedetailsCaseNo(s.getCaseNo());
	 * List<ChargesheetDetails> charge =
	 * chargesheetRepo.findByCaseDetailsCaseNo(s.getCaseNo()); if (charge != null &&
	 * !charge.isEmpty() && charge.get(0).getChargesheetIssued() &&
	 * charge.get(0).getChargesheetDate() != null) {
	 * s.setChargesheetDate(charge.get(0).getChargesheetDate().toString()); } if
	 * (!empList.isEmpty()) { empList.parallelStream().forEach(ss -> { String
	 * fullName = ""; if (StringUtils.isNotBlank(ss.getFirstName())) { fullName +=
	 * ss.getFirstName(); } if (StringUtils.isNotBlank(ss.getMiddleName())) {
	 * fullName += " " + ss.getMiddleName(); } if
	 * (StringUtils.isNotBlank(ss.getLastName())) { fullName += " " +
	 * ss.getLastName(); } employeeNames.add(fullName); });
	 * s.setEmployeeNames(employeeNames); } mapEn.put(s.getCaseNo(), s); });
	 * 
	 * model.addAttribute("noticelist",
	 * mapEn.values().stream().collect(Collectors.toList())); if (viewreport !=
	 * null) { model.addAttribute("viewreport", Boolean.parseBoolean(viewreport)); }
	 * else { model.addAttribute("viewreport", false); } return
	 * "DeptInquiryViews/ReportsForDistrictsWisePendingCases";
	 * 
	 * }
	 * 
	 */

}
