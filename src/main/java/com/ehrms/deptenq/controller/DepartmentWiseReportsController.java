package com.ehrms.deptenq.controller;

import java.time.LocalDateTime;
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
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.modelsDto.DashboardData;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;
import com.ehrms.deptenq.utility.AsyncExecutors;
import com.ehrms.deptenq.utility.SplitUtility;

@Controller
@RequestMapping("departmentalEnquiry")
public class DepartmentWiseReportsController {

	@Autowired
	private IGlobalOrgRepository globalRepo;


	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;
	
	@Autowired
	private FinalOutcomeRepository finalRepo;
	
	@Autowired
	private SplitUtility util;


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
	private CaseDetailsRepository caseRepo;
	

	@Autowired
	private AsyncExecutors exe;


	@Autowired
	private SubDepartmentRepository subDepartmentRepo;



	private static final Logger LOG = LoggerFactory.getLogger(DepartmentWiseReportsController.class);

	
	

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/departmentWiseReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "globalorgid", required = false) String globalorgdid,
			@RequestParam(name = "caseid", required = false) String caseid,
			@RequestParam(name = "subdepartmentId", required = false) String subdepartmentId,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {

		LOG.info("misconduct applicable reports controller....");
		
		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}

		/* model.addAttribute("divisiondata", divisionRepository.findAll()); */
		User currentUser = userService.getCurrentUserPojo();

		ExecutorService executorService = exe.getExe();
		long starttime = System.currentTimeMillis();
//		List<Callable<String>> callList = new ArrayList<>();
		
		List<Callable<String>> callList = new ArrayList<>();
//			System.out.println(starttime);
	//		System.out.println(System.currentTimeMillis());
			long starttime1 = System.currentTimeMillis();
//			List<CaseDetails> finalOut = caseRepo.findAll();
			long casecount = caseRepo.count();
			List<Integer> largeList = util.getSplitdata((int)casecount);
////			List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
//			while(true) {
//				if(casecount>10) {
//					largeList.add(10);
//				} else {
//					if(casecount>0 && casecount<=10) {
//						largeList.add((int)casecount);
//					} else {
//						break;
//					}
//				}
//				casecount=casecount-10;
//			}
			List<CaseDetails> finalOut = new ArrayList<>();
			if(!largeList.isEmpty()) {
				for(Integer i=1;i<=largeList.size();i++) {
					final Integer index = i ;
					callList.add(new Callable<String>() {
						@Override
						public String call() throws Exception {																		
							Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
							Page<CaseDetails> caseList = caseRepo.findAll(pageable);
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
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
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

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		Page<CaseDetails> noticePages = null;
		List<CaseDetails> allCases = new ArrayList<>();
		if(globalorgdid == null) {
			globalorgdid = "";
		}
		SubDepartment subDepartment = null;
		if(subdepartmentId != null && !subdepartmentId.equalsIgnoreCase("null") && StringUtils.isNotBlank(subdepartmentId)) {
			subDepartment = subDepartmentRepo.findById(Long.valueOf(subdepartmentId)).orElse(new SubDepartment());
			
		} else {
			subdepartmentId = "";			
		}
		if(StringUtils.isNotBlank(caseid) && !caseid.equalsIgnoreCase("null")) {
			caseid = caseid;
		} else {
			caseid = "";
		}
		final String caseidx = caseid;
		
		model.addAttribute("caseid", caseid);
		model.addAttribute("subdepartmentid", subdepartmentId);
		model.addAttribute("globalorgdid", globalorgdid);
		GlobalOrg org = null;
		if(globalorgdid != null && !globalorgdid.equalsIgnoreCase("null") && !globalorgdid.equalsIgnoreCase("")) {
			model.addAttribute("depSelected", true);
			org = globalRepo.findById(Long.valueOf(globalorgdid)).orElse(new GlobalOrg());
		} else {
			model.addAttribute("depSelected", false);
		}
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			List<DashboardData> dataList = new ArrayList<>();
			List<GlobalOrg> orgList = globalRepo.findByActive(true);
			List<Callable<String>> callList2 = new ArrayList<>();
			starttime = System.currentTimeMillis();
			for(GlobalOrg s : orgList) {
//			orgList.stream().forEach(s->{
				callList2.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						DashboardData data = getdashBoradData(s,caseId,currentUser);
						dataList.add(data);
						return null;
					}
					
				});
				
			}
			try {
				List<Future<String>> taskFutureList = executorService.invokeAll(callList2);
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
			endtime1 = System.currentTimeMillis();
			LOG.info("Time Taken:"+TimeUnit.MILLISECONDS.toSeconds(endtime1-starttime));
			if(caseid != null && !caseid.equalsIgnoreCase("null") && StringUtils.isNotBlank(caseid)) {
				noticePages = caseRepo.findByIdAndIdNotIn(Long.valueOf(caseid),pageable,caseId);
//				model.addAttribute("subdepartmentlist", subDepartmentRepo.findByGlobalorg(org));
//				model.addAttribute("orgname", org.getGlobalOrgName());
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseRepo.findByIdAndIdNotIn(Long.valueOf(caseidx),pageable,caseId);
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
				
//				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//				allCases =  caseRepo.findByIdAndIdNotIn(Long.valueOf(caseid),allPage,caseId);
				model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());	
			} else {
			
				if(org != null) {
					final GlobalOrg orgx = org;
					if(subDepartment != null) {
						final SubDepartment subDepartmentx = subDepartment;
						noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotInAndSubDepartment(org,pageable,caseId,subDepartment);
						model.addAttribute("subdepartmentlist", subDepartmentRepo.findByGlobalorg(org));
						model.addAttribute("orgname", org.getGlobalOrgName());
//						Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotInAndSubDepartment(org,allPage,caseId,subDepartment);
						largeList = util.getSplitdata((int)noticePages.getTotalElements());
						if(!largeList.isEmpty()) {
							for(Integer i=1;i<=largeList.size();i++) {
								final Integer index1 = i ;
								callList.add(new Callable<String>() {
									@Override
									public String call() throws Exception {																		
										Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
										Page<CaseDetails> caseList =  caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotInAndSubDepartment(orgx,pageable,caseId,subDepartmentx);
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
						
						model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());					
					} else {
						noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,pageable,caseId);
						model.addAttribute("subdepartmentlist", subDepartmentRepo.findByGlobalorg(org));
						model.addAttribute("orgname", org.getGlobalOrgName());
		//				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
		//				allCases = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(org,allPage,caseId);
						largeList = util.getSplitdata((int)noticePages.getTotalElements());
						if(!largeList.isEmpty()) {
							for(Integer i=1;i<=largeList.size();i++) {
								final Integer index1 = i ;
								callList.add(new Callable<String>() {
									@Override
									public String call() throws Exception {																		
										Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
										Page<CaseDetails> caseList = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(orgx,pageable,caseId);
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
						model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());
					}
				} else {
					noticePages = caseRepo.findByIdNotIn(pageable,caseId);
					endtime1 = System.currentTimeMillis();
					LOG.info("Time Taken:"+TimeUnit.MILLISECONDS.toSeconds(endtime1-starttime));
					largeList = util.getSplitdata((int)noticePages.getTotalElements());
					
					if(!largeList.isEmpty()) {
						for(Integer i=1;i<=largeList.size();i++) {
							final Integer index1 = i ;
							callList.add(new Callable<String>() {
								@Override
								public String call() throws Exception {																		
									Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
									Page<CaseDetails> caseList = caseRepo.findByIdNotIn(pageable,caseId);
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
					
					
//					Pageable allPage = PageRequest.of(0, (int)noticePages.getTotalElements(), sort);
//					allCases = caseRepo.findByIdNotIn(allPage,caseId);
					model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());
					endtime1 = System.currentTimeMillis();
					LOG.info("Time Taken:"+TimeUnit.MILLISECONDS.toSeconds(endtime1-starttime));
				}
			}
			model.addAttribute("dashboarddatalist", dataList);
		} 
		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
		
			
			List<DashboardData> dataList = new ArrayList<>();
			GlobalOrg og = currentUser.getPimsEmployee().getGlobalOrgId();
			model.addAttribute("globalorgdid", og.getId());
			DashboardData data = getdashBoradData(og,caseId,currentUser);
			dataList.add(data);

			if(caseid != null && !caseid.equalsIgnoreCase("null") && StringUtils.isNotBlank(caseid)) {
				noticePages = caseRepo.findByIdAndIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(Long.valueOf(caseid),pageable,caseId,og);

//				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//				allCases = caseRepo.findByIdAndIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(Long.valueOf(caseid),allPage,caseId,og);
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseRepo.findByIdAndIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(Long.valueOf(caseidx),pageable,caseId,og);
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
				
				model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());	
			} else {
			
				if(og != null) {
					if(subDepartment != null) {
						final SubDepartment subDepartmentx = subDepartment;
						noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotInAndSubDepartment(og,pageable,caseId,subDepartment);
						model.addAttribute("subdepartmentlist", subDepartmentRepo.findByGlobalorg(og));
						model.addAttribute("orgname", og.getGlobalOrgName());
//						Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//						allCases = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotInAndSubDepartment(og,allPage,caseId,subDepartment);
						largeList = util.getSplitdata((int)noticePages.getTotalElements());
						if(!largeList.isEmpty()) {
							for(Integer i=1;i<=largeList.size();i++) {
								final Integer index1 = i ;
								callList.add(new Callable<String>() {
									@Override
									public String call() throws Exception {																		
										Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
										Page<CaseDetails> caseList = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotInAndSubDepartment(og,pageable,caseId,subDepartmentx);
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
						
						
						model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());					
					} else {
						noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(og,pageable,caseId);
						model.addAttribute("subdepartmentlist", subDepartmentRepo.findByGlobalorg(og));
						model.addAttribute("orgname", og.getGlobalOrgName());
			//			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			//			allCases = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(og,allPage,caseId);
						largeList = util.getSplitdata((int)noticePages.getTotalElements());
						if(!largeList.isEmpty()) {
							for(Integer i=1;i<=largeList.size();i++) {
								final Integer index1 = i ;
								callList.add(new Callable<String>() {
									@Override
									public String call() throws Exception {																		
										Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
										Page<CaseDetails> caseList = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(og,pageable,caseId);
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
						
						
						model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());
					}
				} 
			}
			model.addAttribute("dashboarddatalist", dataList);
			
			
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
			
			List<DashboardData> dataList = new ArrayList<>();
			GlobalOrg og = currentUser.getPimsEmployee().getGlobalOrgId();
	
			DashboardData data = getdashBoradData(og,caseId,currentUser);
			dataList.add(data);

			
			
			if(org != null) {
				final GlobalOrg orgx = org;
				if(subDepartment != null) {
					final SubDepartment subDepartmentx = subDepartment;
					noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotInAndSubDepartment(org,userList,pageable,caseId,subDepartment);
					model.addAttribute("subdepartmentlist", subDepartmentRepo.findByGlobalorg(org));
					model.addAttribute("orgname", org.getGlobalOrgName());
//					Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//					allCases = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotInAndSubDepartment(org,userList,allPage,caseId,subDepartment);
					largeList = util.getSplitdata((int)noticePages.getTotalElements());
					if(!largeList.isEmpty()) {
						for(Integer i=1;i<=largeList.size();i++) {
							final Integer index1 = i ;
							callList.add(new Callable<String>() {
								@Override
								public String call() throws Exception {																		
									Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
									Page<CaseDetails> caseList = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotInAndSubDepartment(orgx,userListx,pageable,caseId,subDepartmentx);
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
					
					
					model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());					
				} else {
					noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotIn(org,userList,pageable,caseId);
					model.addAttribute("subdepartmentlist", subDepartmentRepo.findByGlobalorg(org));
					model.addAttribute("orgname", org.getGlobalOrgName());
//					Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//					allCases = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotIn(org,userList,allPage,caseId);
					largeList = util.getSplitdata((int)noticePages.getTotalElements());
					if(!largeList.isEmpty()) {
						for(Integer i=1;i<=largeList.size();i++) {
							final Integer index1 = i ;
							callList.add(new Callable<String>() {
								@Override
								public String call() throws Exception {																		
									Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
									Page<CaseDetails> caseList =  caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotIn(orgx,userListx,pageable,caseId);
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
					
					model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());
				}
			} else {
				noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotIn(og,userList,pageable,caseId);
				
//				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//				allCases =  caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotIn(og,userList,allPage,caseId);
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserInAndIdNotIn(og,userListx,pageable,caseId);
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
				
				
				model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());
			}
			model.addAttribute("dashboarddatalist", dataList);
			
			
		}

		else {
			
			List<DashboardData> dataList = new ArrayList<>();
			GlobalOrg og = currentUser.getPimsEmployee().getGlobalOrgId();
	
			DashboardData data = getdashBoradData(og,caseId,currentUser);
			dataList.add(data);

			
			
			if(org != null) {
				noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndIdNotIn(org,currentUser,pageable,caseId);
				final GlobalOrg orgx = org;
//				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
//				allCases = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndIdNotIn(org,currentUser,allPage,caseId);
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndIdNotIn(orgx,currentUser,pageable,caseId);
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
				
				
				model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());
			} else {
				noticePages = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndIdNotIn(og,currentUser,pageable,caseId);
				
		//		Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
		//		allCases =  caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndIdNotIn(og,currentUser,allPage,caseId);
				largeList = util.getSplitdata((int)noticePages.getTotalElements());
				if(!largeList.isEmpty()) {
					for(Integer i=1;i<=largeList.size();i++) {
						final Integer index1 = i ;
						callList.add(new Callable<String>() {
							@Override
							public String call() throws Exception {																		
								Pageable pageable = PageRequest.of(index1-1, 10, Sort.by("id"));
								Page<CaseDetails> caseList = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserAndIdNotIn(og,currentUser,pageable,caseId);
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
				
				
				model.addAttribute("totalEmpCount", allCases.stream().mapToLong(s->s.getEmployeeList().size()).sum());
			}
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
		
		 model.addAttribute("totalCasesLabel",messageSource.getMessage("deptenq.totalcases.label", null, loc.resolveLocale(request)));
		 model.addAttribute("totalEmployeeCountLabel",messageSource.getMessage("deptenq.totalemployee.label", null, loc.resolveLocale(request)));
		
		return "DeptInquiryViews/ReportsforDepartmentwise.html";

	}	
	
	private DashboardData getdashBoradData(GlobalOrg s,Set<Long> caseIdx,User currentUser) {
		DashboardData data = new DashboardData();
		
	
		
		final Set<Long> caseId = caseIdx;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN") || currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(s,caseId);
			data.setDepartmentName(s.getGlobalOrgName());
			data.setCountCaseNo(caseList.size());
			data.setOrgid(s.getId());
			data.setTotalemployee(caseList.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());			
		}  else
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")|| currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARY")){
			List<SubDepartment> subList = currentUser.getSecsubList().stream().filter(ss -> ss.isActive())
					.map(ss -> ss.getSubDepartment()).collect(Collectors.toList());
			List<User> userList = new ArrayList<>();
			if(subList.isEmpty()) {
				userList = userRepo.findBySubDepartmentIn(
						subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
				userList.add(currentUser);
			} else {
				userList = userRepo.findBySubDepartmentIn(subList);
			}
			List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserInAndIdNotIn(userList,caseId);
			data.setDepartmentName(s.getGlobalOrgName());
			data.setCountCaseNo(caseList.size());
			data.setOrgid(s.getId());
			data.setTotalemployee(caseList.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());			
		} else {
			List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserAndIdNotIn(currentUser,caseId);
			data.setDepartmentName(s.getGlobalOrgName());
			data.setCountCaseNo(caseList.size());
			data.setOrgid(s.getId());
			data.setTotalemployee(caseList.stream().mapToLong(ss->ss.getEmployeeList().size()).sum());
		}
		
		
		
		return data;
	}
	
}

