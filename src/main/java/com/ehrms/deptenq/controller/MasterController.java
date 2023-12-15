package com.ehrms.deptenq.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.ModuleName;
import com.ehrms.deptenq.models.ProcessListDynamic;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.modelsDto.CommonABData;
import com.ehrms.deptenq.modelsDto.DashboardData;
import com.ehrms.deptenq.modelsDto.GroupAData;
import com.ehrms.deptenq.modelsDto.GroupBData;
import com.ehrms.deptenq.modelsDto.MasterDashboardData;
import com.ehrms.deptenq.modelsDto.SubDepartmentData;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.IChargesheetDetailsRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.IProsecutionProposalDetailsRepository;
import com.ehrms.deptenq.repository.LoginAuditRepository;
import com.ehrms.deptenq.repository.ModuleNameRepository;
import com.ehrms.deptenq.repository.ProcessListDynamicRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.SuspensionDetailsRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.utility.AsyncExecutors;
import com.ehrms.deptenq.utility.CaptchaUtil;
import com.ehrms.deptenq.utility.DirectoryUtil;
import com.ehrms.deptenq.utility.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.apiclub.captcha.Captcha;

@Controller
//@RequestMapping("/departmentalinquiry")
public class MasterController {
	private static final Logger LOG = LoggerFactory.getLogger(MasterController.class);

	@Autowired
	private IGlobalOrgRepository globalorgRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProcessListDynamicRepository processRepo;
	
	@Autowired
	private AsyncExecutors exe;
	
	@Autowired
	private SuspensionDetailsRepository susDeRepo;
	
	@Autowired
	private ModuleNameRepository moduleRepo;
	
	@Autowired
	private ObjectMapper objectMapper;
		
	@Autowired
	private CaseDetailsRepository caseRepo;
	
	@Autowired
	private IEmployeeDetailsRepository empRepo;
	
//	@Autowired
//	private SuspensionDetailsRepository susRepo;
	
	@Autowired
	private IChargesheetDetailsRepository chargeRepo;
	
//	@Autowired
//	private IReInstatedRepository reinstateRepo;
//	
//	@Autowired
//	private FinalOutcomeRepository fileOutRepo;
	
	@Autowired
	private LoginAuditRepository loginAudiRepo;
	
//	@Autowired
//	private DetailsKeptAbeyanceCasesRepository abeyRepo;
	
	@Autowired
	private com.ehrms.deptenq.repository.ProcessNameRepository processNameRepo;
	
	@Autowired
	private IProsecutionProposalDetailsRepository prosecRepo;
	
	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	
//	@Autowired
//	private ObjectMapper obbj;

	private void getCaptcha(User user) {
		Captcha captcha = CaptchaUtil.createCaptcha(240, 70);
		user.setHiddenCaptcha(captcha.getAnswer());
		user.setCaptcha(""); // value entered by the User
		user.setRealCaptcha(CaptchaUtil.encodeBase64(captcha));

	}


	@GetMapping("/")
	public String root(Model model, User user,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {

			/*
			 * if(SecurityUtils.isCurrentUserInRole("ROLE_EMPLOYEE")) { return
			 * "redirect:/getEmployeeDashBoard"; } else {
			 * 
			 * }
			 */
			final User user1 = userRepository.findByEmail(SecurityUtils.getCurrentUserLogin());
//			List<UsersProcess> processList = userProcessRepo.findByUser(user1);
			List<DashboardData> dataList = new ArrayList<>();
			
			if(!user1.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_USERMANAGEMENT") 
					&& !user1.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_IO")) {
				
			
				List<GlobalOrg> orgList = globalorgRepo.findByActive(true);
				
				ExecutorService executorService = exe.getExe();
				
				Collection<Callable<String>> callables = new ArrayList<>();
				long starttime = System.currentTimeMillis();
				List<Callable<String>> callList = new ArrayList<>();
	//			System.out.println(starttime);
				if (user1.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_SUPERADMIN")) {
			//		System.out.println(System.currentTimeMillis());
					long starttime1 = System.currentTimeMillis();
//					List<CaseDetails> finalOut = caseRepo.findAll();
					long casecount = caseRepo.count();
					List<Integer> largeList = new ArrayList<>();
//					List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
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
					for(GlobalOrg s : orgList) {
	//				orgList.stream().forEach(s->{
						callables.add(new Callable<String>() {
							@Override
							public String call() throws Exception {
								
								final DashboardData data = getdashBoradData(s,caseId);
								Collection<Callable<String>> callablestiny = new ArrayList<>();
								callablestiny.add(new Callable<String>() {
									@Override
									public String call() throws Exception {
//										data.setDataA(getGroupAData(s,caseId));
										return null;
									}
								});
								callablestiny.add(new Callable<String>() {
									@Override
									public String call() throws Exception {
//										data.setDataB(getGroupBData(s,caseId));
										return null;
									}
								});
								callablestiny.add(new Callable<String>() {
									@Override
									public String call() throws Exception {
//										data.setDataAB(getGroupABData(s,caseId));
										return null;
									}
								});
								try {
									List<Future<String>> taskFutureList = executorService.invokeAll(callablestiny);
									for (Future<String> future : taskFutureList) {
	//									future.get();
										try {
											future.get();
										} catch (Exception e) {
	//										continue;
											e.printStackTrace();
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
	//							data.setDataA(getGroupAData(s,caseId));
	//							data.setDataB(getGroupBData(s,caseId));
	//							data.setDataAB(getGroupABData(s,caseId));
								
								dataList.add(data);
								return null;
							}						
						});
	//				});
					}
					
				} else {
	//				if(user1.getSubDepartment() != null) {
	//					GlobalOrg s = user1.getPimsEmployee().getGlobalOrgId();
	//					DashboardData data = getdashBoradDataSubDepartment(s,user1.getSubDepartment());
	//					data.setDataA(getGroupADataSubDepartment(s,user1.getSubDepartment()));
	//					data.setDataB(getGroupBDataSubDepartment(s,user1.getSubDepartment()));
	//					data.setDataAB(getGroupABDataSubDepartment(s,user1.getSubDepartment()));
	//
	//					dataList.add(data);
	//				} else {
					if (user1.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_SECRETARY")) {
						List<SubDepartment> subList = user1.getSecsubList().stream().filter(s -> s.isActive())
								.map(s -> s.getSubDepartment()).collect(Collectors.toList());
						List<User> userList = new ArrayList<>();
						if(subList.isEmpty()) {
							userList = userRepository.findBySubDepartmentIn(
									subDepartmentRepo.findByGlobalorg(user1.getPimsEmployee().getGlobalOrgId()));
							userList.add(user1);
						} else {
							userList = userRepository.findBySubDepartmentIn(subList);
						}						
						userList.add(user1);
						List<User> uu = userRepository.findByPimsEmployeeGlobalOrgId(user1.getPimsEmployee().getGlobalOrgId());
						for(User uux : uu) {
							if(uux.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_DEPARTMENTADMIN")) {
								userList.add(uux);
								break;
							}
						}
						Long casecount = caseRepo.countByCurrentUserIn(userList);
						List<Integer> largeList = new ArrayList<>();
//						List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
						while(true) {
							if(casecount>10) {
								largeList.add(10);
							} else {
								if(casecount>0 && casecount<=10) {
									largeList.add(casecount.intValue());
								} else {
									break;
								}
							}
							casecount=casecount-10;
						}
						List<CaseDetails> finalOut = new ArrayList<>();
						final List<User> userList1 = userList;
						if(!largeList.isEmpty()) {
							for(Integer i=1;i<=largeList.size();i++) {
								final Integer index = i ;
								callList.add(new Callable<String>() {
									@Override
									public String call() throws Exception {																		
										Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
										Page<CaseDetails> caseList = caseRepo.findByCurrentUserIn(userList1,pageable);
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
						
//						List<CaseDetails> finalOut = caseRepo.findByCurrentUserIn(userList);		
						Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
								&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
								&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
						if(caseIdx.isEmpty()) {
							caseIdx.add(0L);
						}
						List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
						if(!casesTransferList.isEmpty()) {
							caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
						}
						final Set<Long> caseId = caseIdx;

							DashboardData data = getdashBoradDataIn(userList,caseId);
//							data.setDataA(getGroupADataIn(userList,caseId));
//							data.setDataB(getGroupBDataIn(userList,caseId));
//							data.setDataAB(getGroupABDataIn(userList,caseId));
							data.setSubDepartmentList(getSubDepartmentDataIn(userList));
							
							dataList.add(data);

					} else if (user1.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_DEPARTMENTADMIN")) {
							GlobalOrg s = user1.getPimsEmployee().getGlobalOrgId();
							
							Long casecount = caseRepo.countByCurrentUserPimsEmployeeGlobalOrgId(s);
							List<Integer> largeList = new ArrayList<>();
//							List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
							while(true) {
								if(casecount>10) {
									largeList.add(10);
								} else {
									if(casecount>0 && casecount<=10) {
										largeList.add(casecount.intValue());
									} else {
										break;
									}
								}
								casecount=casecount-10;
							}
							List<CaseDetails> finalOut = new ArrayList<>();
							if(!largeList.isEmpty()) {
								for(Integer i=1;i<=largeList.size();i++) {
									final Integer index = i ;
									callList.add(new Callable<String>() {
										@Override
										public String call() throws Exception {																		
											Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
											Page<CaseDetails> caseList = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgId(s,pageable);
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

							
							
//							List<CaseDetails> finalOut = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgId(s);		
							Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
									&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
									&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
							if(caseIdx.isEmpty()) {
								caseIdx.add(0L);
							}
							List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
							if(!casesTransferList.isEmpty()) {
								caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
							}
							final Set<Long> caseId = caseIdx;
	//						if(caseId.isEmpty()) {
	//							DashboardData data = getdashBoradData(s);
	//							data.setDataA(getGroupAData(s));
	//							data.setDataB(getGroupBData(s));
	//							data.setDataAB(getGroupABData(s));
	//							data.setSubDepartmentList(getSubDepartmentData(user1));
	//							
	//							dataList.add(data);
	//						} else {
								DashboardData data = getdashBoradData(s,caseId);
//								data.setDataA(getGroupAData(s,caseId));
//								data.setDataB(getGroupBData(s,caseId));
//								data.setDataAB(getGroupABData(s,caseId));
								data.setSubDepartmentList(getSubDepartmentData(user1));
								
								dataList.add(data);
	//						}
							
						} else {
	//						GlobalOrg s = user1.getPimsEmployee().getGlobalOrgId();
							DashboardData data = getdashBoradDataCurrentUser(user1);
//							data.setDataA(getGroupADataCurrentUser(user1));
//							data.setDataB(getGroupBDataCurrentUser(user1));
//							data.setDataAB(getGroupABDataCurrentUser(user1));
							data.setSubDepartmentList(getSubDepartmentData(user1));
							
							dataList.add(data);
						}
	//				}
				}
				try {
					long starttime3 = System.currentTimeMillis();
					List<Future<String>> taskFutureList = executorService.invokeAll(callables);
					for (Future<String> future : taskFutureList) {
						try {
							future.get();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					long endtime3 = System.currentTimeMillis();
					LOG.info("Time required for final step:"+TimeUnit.MILLISECONDS.toSeconds(endtime3-starttime3));
				} catch (Exception e) {
					e.printStackTrace();
				}
	//			finally{
					  executorService.shutdown(); 
	//				}
				long endtime = System.currentTimeMillis();
	//			System.out.println(endtime);
				LOG.info("Time Difference in seconds superadmin dashboard:"+TimeUnit.MILLISECONDS.toSeconds(endtime-starttime));
	//			System.out.println();
				if(sortDirection != null) {
					model.addAttribute("sortField", sortField);
					model.addAttribute("sortDir", sortDirection);
					model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
					if(sortDirection.equalsIgnoreCase("asc")) {
						dataList.sort((a1,a2)->Long.valueOf(a1.getCountCaseNo()).compareTo(Long.valueOf(a2.getCountCaseNo())));
					} else {
						dataList.sort((a1,a2)->Long.valueOf(a2.getCountCaseNo()).compareTo(Long.valueOf(a1.getCountCaseNo())));					
					}
				} else {
					model.addAttribute("sortDir", "desc");
					model.addAttribute("reverseSortDir", "asc");
					dataList.sort((a1,a2)->Long.valueOf(a2.getCountCaseNo()).compareTo(Long.valueOf(a1.getCountCaseNo())));
				}
				
				model.addAttribute("dataList", dataList);
				MasterDashboardData data = new MasterDashboardData();
				data.setTotalCases(dataList.parallelStream().mapToLong(s->s.getCountCaseNo()).sum());
				data.setTotalMajor(dataList.parallelStream().mapToLong(s->s.getMajor()).sum());
				data.setTotalMinor(dataList.parallelStream().mapToLong(s->s.getMinor()).sum());
				data.setTotalSuspension(dataList.parallelStream().mapToLong(s->s.getCaseSuspended()).sum());
				data.setWithoutdeSuspended(dataList.parallelStream().mapToLong(s->s.getWithoutdeSuspended()).sum());
				data.setWithoutdeProsecuted(dataList.parallelStream().mapToLong(s->s.getWithoutdeProsecuted()).sum());
				data.setTotalRetired(dataList.parallelStream().mapToLong(s->s.getNumberOfCasesWithRetiredEmp()).sum());
				data.setTotalProsecution(dataList.parallelStream().mapToLong(s->s.getNumberOfCasesWithProsecution()).sum());
				data.setTotalLessThan5(dataList.parallelStream().mapToLong(s->s.getLessThan5years()).sum());
				data.setTotalMoreThan5(dataList.parallelStream().mapToLong(s->s.getGreaterThan5years()).sum());
				data.setDashboardData(dataList);
				
				long countcases  = data.getDashboardData().parallelStream().mapToLong(s->s.getSubDepartmentList().parallelStream().mapToLong(ss->ss.getCountCaseNo()).sum()).sum();
				
				long countcasesMajor  = data.getDashboardData().parallelStream().mapToLong(s->s.getSubDepartmentList().parallelStream().mapToLong(ss->ss.getMajor()).sum()).sum();
				
				long countcasesMinor  = data.getDashboardData().parallelStream().mapToLong(s->s.getSubDepartmentList().parallelStream().mapToLong(ss->ss.getMinor()).sum()).sum();
				
				long countcasesSuspension  = data.getDashboardData().parallelStream().mapToLong(s->s.getSubDepartmentList().parallelStream().mapToLong(ss->ss.getCaseSuspended()).sum()).sum();
				
				long countcasesRetired  = data.getDashboardData().parallelStream().mapToLong(s->s.getSubDepartmentList().parallelStream().mapToLong(ss->ss.getNumberOfCasesWithRetiredEmp()).sum()).sum();
				
				long countcasesProsecution  = data.getDashboardData().parallelStream().mapToLong(s->s.getSubDepartmentList().parallelStream().mapToLong(ss->ss.getNumberOfCasesWithProsecution()).sum()).sum();
				
				long countcasesLessthan5  = data.getDashboardData().parallelStream().mapToLong(s->s.getSubDepartmentList().parallelStream().mapToLong(ss->ss.getLessThan5years()).sum()).sum();
				
				long countcasesMorethan5  = data.getDashboardData().parallelStream().mapToLong(s->s.getSubDepartmentList().parallelStream().mapToLong(ss->ss.getGreaterThan5years()).sum()).sum();
				
				
				if(data.getTotalCases()>=countcases) {
					data.setTotalCasesad(data.getTotalCases()-countcases);
				} else {
					data.setTotalCasesad(0);
				}
				
				if(data.getTotalMajor()>=countcasesMajor) {
					data.setTotalMajorad(data.getTotalMajor()-countcasesMajor);
				} else {
					data.setTotalMajorad(0);
				}
				
				if(data.getTotalMinor()>=countcasesMinor) {
					data.setTotalMinorad(data.getTotalMinor()-countcasesMinor);
				} else {
					data.setTotalMinorad(0);
				}

				if(data.getTotalSuspension()>=countcasesSuspension) {
					data.setTotalSuspensionad(data.getTotalSuspension()-countcasesSuspension);
				} else {
					data.setTotalSuspensionad(0);
				}

				if(data.getTotalRetired()>=countcasesRetired) {
					data.setTotalRetiredad(data.getTotalRetired()-countcasesRetired);
				} else {
					data.setTotalRetiredad(0);
				}

				if(data.getTotalProsecution()>=countcasesProsecution) {
					data.setTotalProsecutionad(data.getTotalProsecution()-countcasesProsecution);
				} else {
					data.setTotalProsecutionad(0);
				}

				if(data.getTotalLessThan5()>=countcasesLessthan5) {
					data.setTotalLessThan5ad(data.getTotalLessThan5()-countcasesLessthan5);
				} else {
					data.setTotalLessThan5ad(0);
				}

				if(data.getTotalMoreThan5()>=countcasesMorethan5) {
					data.setTotalMoreThan5ad(data.getTotalMoreThan5()-countcasesMorethan5);
				} else {
					data.setTotalMoreThan5ad(0);
				}
				
				model.addAttribute("masterData", data);

			} else {
				MasterDashboardData data = new MasterDashboardData();
				model.addAttribute("masterData", data);
			}
			
			model.addAttribute("visitorcount", loginAudiRepo.count());
			return "DeptInquiryViews/de_registration.html";

		}

		else {
			getCaptcha(user);
			model.addAttribute("user", user);
			model.addAttribute("globalOrgList", globalorgRepo.findAll().stream()
					.filter(GlobalOrg::isActive)
					.map((GlobalOrg globalorg) -> (globalorg.getGlobalOrgName())).collect(Collectors.toList()));
//			model.addAttribute("department", DirectoryUtil.GAD);
//	    		model.addAttribute("departmentList", departRepo.findAll());
//			List<ObjectNode> obbList = new ArrayList<>();
//			globalorgRepo.findAll().stream()
//			.filter(GlobalOrg::isActive).forEach(s->{
//				ObjectNode obb = obbj.createObjectNode();
//				obb.put("id", s.getTenantId());
//				obb.put("name", s.getGlobalOrgName());				
//				obbList.add(obb);
//			});
//			model.addAttribute("obbList", obbList);
			model.addAttribute("department", DirectoryUtil.GAD);
			model.addAttribute("visitorcount", loginAudiRepo.count());
			 return "login.html"; 
			/* return "DepartmentalEnquirylogin.html"; */
		}
	}


	@GetMapping("/login")
	public String login(Model model, User user) {
		LOG.info("hi this is the login page.....");
		getCaptcha(user);
		model.addAttribute("user", user);
		model.addAttribute("globalOrgList", globalorgRepo.findAll().stream()
				.filter(GlobalOrg::isActive)
				.map((GlobalOrg globalorg) -> (globalorg.getGlobalOrgName())).collect(Collectors.toList()));
		model.addAttribute("department", DirectoryUtil.GAD);
//		List<ObjectNode> obbList = new ArrayList<>();
//		globalorgRepo.findAll().stream()
//		.filter(GlobalOrg::isActive).forEach(s->{
//			ObjectNode obb = obbj.createObjectNode();
//			obb.put("id", s.getTenantId());
//			obb.put("name", s.getGlobalOrgName());
//			obbList.add(obb);
//		});
//		model.addAttribute("obbList", obbList);
//		model.addAttribute("departmentList", departRepo.findAll());
		return "login";

	}

	/*
	 * Author : 
	 * Method :getProcessListDashboard 
	 * Description : landing name of sidenav bar
	 */

	@GetMapping("/getprocessListDashboard")
	public String getProcessListDashboard(Model model) {
		try {
			User user = userRepository.findByEmail(SecurityUtils.getCurrentUserLogin());
			// Long processid =
			// Long.valueOf(request.getSession().getAttribute(PROCESSID).toString());
			List<ProcessListDynamic> processList = processRepo
					
					.findByProcessNameIsNullAndModuleNameAndRoleInOrderById(moduleRepo.findById(4L).orElse(new ModuleName()) ,user.getRoles().stream().collect(Collectors.toList()));
			model.addAttribute("processList", processList);
		} catch (Exception a) {
			LOG.error("Exception", a);
		}
		return "commonsidebar/processlistsidebarx :: processlistsidebar";
	}
	
	
	@GetMapping("/getprocessList")
	public String getProcessList(Model model) {
		try {
			User user = userRepository.findByEmail(SecurityUtils.getCurrentUserLogin());
			Long processid = 12L;
			List<ProcessListDynamic> processList = processRepo.findByProcessNameAndRoleInOrderById(processNameRepo.findById(processid).orElse(new com.ehrms.deptenq.models.ProcessName())
					,user.getRoles().stream().collect(Collectors.toList()));
			model.addAttribute("processList", processList);
//			model.addAttribute(MESSAGE, messageSource.getMessage("inbox.redirect.successsave", null, loc.resolveLocale(requestt)));
		} catch(Exception a) {
			LOG.error("Exception", a);
		}
		return "commonsidebar/processlist :: processlist1";
	}
	
	@Transactional
	private  DashboardData getdashBoradData(GlobalOrg s) {
		DashboardData data = new DashboardData();
		
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgId(s);

				long countEmployees = caseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();

				data.setCountCaseNo(caseList.size());
				data.setTotalemployee(countEmployees);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgId(true,false,s,true,s);
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empListSuspended,s);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseGreater5List = caseRepo.findDistinctIdByChargeSheetListIn(chargeRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)));
				long countGreaterthan5years = caseGreater5List.size();

				long employeeCountGreater5 = caseGreater5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreater5);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLess5List = caseRepo.findDistinctIdByChargeSheetListIn(chargeRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)));
				long countLessThan5Years = caseLess5List.size();

				long employeeCountLessThan5 = caseLess5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();

				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> minorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableId(s,2L);
				long minor = minorCaseList.size();

				long employeeCountminor = minorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountMinor(employeeCountminor); 
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> majorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotIn(s,Arrays.asList(2L));
				long major = majorCaseList.size();

				long employeeCountMajor = majorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNull(true,s);
				List<CaseDetails> caseProsec = caseRepo.findDistinctIdByProsecutionProposalIn(proseList);
				long prosecutionCount = caseProsec.size();
				data.setNumberOfCasesWithProsecution(prosecutionCount);
				data.setEmployeeCountProsecutionCases(proseList.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNull(s,LocalDate.now());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListIn(empList); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		

//		List<SuspensionDetails> listSuspended = susRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNull(s,true);
//		List<EmployeeDetails> empListSuspended = new ArrayList<>();
//		listSuspended.stream().forEach(ss->{
//			List<EmployeeDetails> emp = empRepo.findByFirstNameAndMiddleNameAndLastName(ss.getFirstName(), ss.getMiddleName(), ss.getLastName());
//			empListSuspended.addAll(emp);
//		});
//		List<CaseDetails> suspendedCases = caseRepo.findDistinctIdByEmployeeListIn(empListSuspended);
		

		data.setDepartmentName(s.getGlobalOrgName());
		

		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
//				future.get();
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
		return data;
	}
	
	@Transactional
	private  DashboardData getdashBoradDataIn(List<User> s,Set<Long> caseId) {
		DashboardData data = new DashboardData();
		
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserInAndIdNotIn(s,caseId);

				long countEmployees = caseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();

				data.setCountCaseNo(caseList.size());
				data.setTotalemployee(countEmployees);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListSuspended,s,caseId);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<SuspensionDetails> suswithoutde =  susDeRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(true,"",s);
				data.setWithoutdeSuspended(suswithoutde.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<ProsecutionProposalDetails> prowithoutde =  prosecRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserIn(true,"",s);
				data.setWithoutdeProsecuted(prowithoutde.size());
				return null;
			}						
		});
		
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseGreater5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findByCurrentUserInAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
				long countGreaterthan5years = caseGreater5List.size();

				long employeeCountGreater5 = caseGreater5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreater5);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLess5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findByCurrentUserInAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
				long countLessThan5Years = caseLess5List.size();

				long employeeCountLessThan5 = caseLess5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();

				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> minorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdAndIdNotIn(s,2L,caseId);
				long minor = minorCaseList.size();

				long employeeCountminor = minorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountMinor(employeeCountminor); 
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> majorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdNotInAndIdNotIn(s,Arrays.asList(2L),caseId);
				long major = majorCaseList.size();

				long employeeCountMajor = majorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
//				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(true,s,caseListId,caseId);
				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserInAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,caseId);
//				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
//				long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count();
//				employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(empProList.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserInAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,LocalDate.now(),caseId);
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		

//		List<SuspensionDetails> listSuspended = susRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNull(s,true);
//		List<EmployeeDetails> empListSuspended = new ArrayList<>();
//		listSuspended.stream().forEach(ss->{
//			List<EmployeeDetails> emp = empRepo.findByFirstNameAndMiddleNameAndLastName(ss.getFirstName(), ss.getMiddleName(), ss.getLastName());
//			empListSuspended.addAll(emp);
//		});
//		List<CaseDetails> suspendedCases = caseRepo.findDistinctIdByEmployeeListIn(empListSuspended);
		

		data.setDepartmentName(s.get(0).getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
		

		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
//				future.get();
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
		return data;
	}
	
	@Transactional
	private  DashboardData getdashBoradData(GlobalOrg s,Set<Long> caseId) {
		DashboardData data = new DashboardData();
		
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(s,caseId);

				long countEmployees = caseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();

				data.setCountCaseNo(caseList.size());
				data.setTotalemployee(countEmployees);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListSuspended,s,caseId);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<SuspensionDetails> suswithoutde =  susDeRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(true,"",s);
				data.setWithoutdeSuspended(suswithoutde.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<ProsecutionProposalDetails> prowithoutde =  prosecRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUserPimsEmployeeGlobalOrgId(true,"",s);
				data.setWithoutdeProsecuted(prowithoutde.size());
				return null;
			}						
		});
		
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseGreater5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
				long countGreaterthan5years = caseGreater5List.size();

				long employeeCountGreater5 = caseGreater5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreater5);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLess5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
				long countLessThan5Years = caseLess5List.size();

				long employeeCountLessThan5 = caseLess5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();

				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> minorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdNotIn(s,2L,caseId);
				long minor = minorCaseList.size();

				long employeeCountminor = minorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountMinor(employeeCountminor); 
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> majorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdNotIn(s,Arrays.asList(2L),caseId);
				long major = majorCaseList.size();

				long employeeCountMajor = majorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
//				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(true,s,caseListId,caseId);
				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,caseId);
//				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
//				long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count();
//				employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(empProList.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,LocalDate.now(),caseId);
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		

//		List<SuspensionDetails> listSuspended = susRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNull(s,true);
//		List<EmployeeDetails> empListSuspended = new ArrayList<>();
//		listSuspended.stream().forEach(ss->{
//			List<EmployeeDetails> emp = empRepo.findByFirstNameAndMiddleNameAndLastName(ss.getFirstName(), ss.getMiddleName(), ss.getLastName());
//			empListSuspended.addAll(emp);
//		});
//		List<CaseDetails> suspendedCases = caseRepo.findDistinctIdByEmployeeListIn(empListSuspended);
		

		data.setDepartmentName(s.getGlobalOrgName());
		data.setOrgid(s.getId());

		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
//				future.get();
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
		return data;
	}
	
	
	private  DashboardData getdashBoradDataCurrentUser(User s) {
		DashboardData data = new DashboardData();
		ExecutorService executorService = exe.getExe();
		List<Callable<String>> callList = new ArrayList<>();
//		List<CaseDetails> finalOut = caseRepo.findByCurrentUser(s);		
		Long casecount = caseRepo.countByCurrentUser(s);
		List<Integer> largeList = new ArrayList<>();
//		List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
		while(true) {
			if(casecount>10) {
				largeList.add(10);
			} else {
				if(casecount>0 && casecount<=10) {
					largeList.add(casecount.intValue());
				} else {
					break;
				}
			}
			casecount=casecount-10;
		}
		List<CaseDetails> finalOut = new ArrayList<>();
		if(!largeList.isEmpty()) {
			for(Integer i=1;i<=largeList.size();i++) {
				final Integer index = i ;
				callList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {																		
						Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
						Page<CaseDetails> caseList = caseRepo.findByCurrentUser(s,pageable);
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
		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserAndIdNotIn(s,caseId);
				

				long countEmployees = caseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();

				data.setCountCaseNo(caseList.size());
				data.setTotalemployee(countEmployees);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListSuspended,s,caseId);
				data.setCaseSuspended(caseListSuspended.size());
				data.setNumberSuspended(empListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<SuspensionDetails> suswithoutde =  susDeRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",s);
				data.setWithoutdeSuspended(suswithoutde.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {

				List<ProsecutionProposalDetails> prowithoutde =  prosecRepo.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",s);
				data.setWithoutdeProsecuted(prowithoutde.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {

				List<CaseDetails> caseGreater5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findByCurrentUserAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
				long countGreaterthan5years = caseGreater5List.size();
				long employeeCountGreater5 = caseGreater5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreater5);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
	
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {

				List<CaseDetails> caseLess5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findByCurrentUserAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
				long countLessThan5Years = caseLess5List.size();
				long employeeCountLessThan5 = caseLess5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {

				List<CaseDetails> minorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserAndRuleApplicableIdAndIdNotIn(s,2L,caseId);
				long minor = minorCaseList.size();
				long employeeCountminor = minorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountMinor(employeeCountminor); 
				data.setMinor(minor);
				return null;
			}						
		});


		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {

				List<CaseDetails> majorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserAndRuleApplicableIdNotInAndIdNotIn(s,Arrays.asList(2L),caseId);
				long major = majorCaseList.size();
				long employeeCountMajor = majorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});

		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {

				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empProList,caseId,s);

				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(empProList.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {

				
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,LocalDate.now(),caseId);
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empList,caseId,s); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
	

	
		

		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
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
	
		

	

	
		
		
		data.setDepartmentName(s.getPimsEmployee().getGlobalOrgId().getGlobalOrgName());

	
	


	
		
		
		
		
		
		return data;
	}
	
	
	private  DashboardData getdashBoradDataSubDepartment(GlobalOrg s,SubDepartment subDep) {
		DashboardData data = new DashboardData();
		
		List<CaseDetails> finalOut = caseRepo.findAll();		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndIdNotIn(s,subDep,caseId);
//		long countEmployees = 0;
		
//		for(CaseDetails cases : caseList) {
//			if(cases.getEmployeeList() != null) {
//				countEmployees += cases.getEmployeeList().size();
//			}
//		}
		
		long countEmployees = caseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
//		List<SuspensionDetails> listSuspended = susRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeSuspendedAndCasedetailsIsNotNull(s,subDep,true);
//		List<EmployeeDetails> empListSuspended = new ArrayList<>();
//		listSuspended.stream().forEach(ss->{
//			List<EmployeeDetails> emp = empRepo.findByFirstNameAndMiddleNameAndLastName(ss.getFirstName(), ss.getMiddleName(), ss.getLastName());
//			empListSuspended.addAll(emp);
//		});
//		List<CaseDetails> suspendedCases = caseRepo.findDistinctIdByEmployeeListIn(empListSuspended);
		List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIdNotIn(true,false,s,subDep,caseId,true,s,subDep,caseId);
		List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndIdNotIn(empListSuspended,s,subDep,caseId);


		List<CaseDetails> caseGreater5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(s,subDep,true,LocalDate.now().minusYears(5)),caseId);
		long countGreaterthan5years = caseGreater5List.size();
		long employeeCountGreater5 = caseGreater5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
		data.setEmployeeCountGreaterThan5years(employeeCountGreater5);

		List<CaseDetails> caseLess5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(s,subDep,true,LocalDate.now().minusYears(5)),caseId);
		long countLessThan5Years = caseLess5List.size();
		long employeeCountLessThan5 = caseLess5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
		data.setEmployeeCountLessThan5years(employeeCountLessThan5);
		
		List<CaseDetails> minorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdAndIdNotIn(s,subDep,2L,caseId);
		long minor = minorCaseList.size();
		long employeeCountminor = minorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
		data.setEmployeeCountMinor(employeeCountminor); 
		
		List<CaseDetails> majorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdNotIn(s,subDep,Arrays.asList(2L),caseId);
		long major = majorCaseList.size();
		long employeeCountMajor = majorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
		data.setEmployeeCountMajor(employeeCountMajor);
		

		List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdNotIn(true,s,subDep,caseId);
		List<CaseDetails> caseProsec = caseRepo.findDistinctIdByProsecutionProposalInAndIdNotIn(proseList,caseId);
		long prosecutionCount = caseProsec.size();
		data.setNumberOfCasesWithProsecution(prosecutionCount);
		data.setEmployeeCountProsecutionCases(proseList.size());

		List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,subDep,LocalDate.now(),caseId);
		List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
		long retiredCases = caseListRetired.size(); 
		data.setNumberOfCasesWithRetiredEmp(retiredCases);
		data.setEmployeeCountRetiredCases(empList.size());
		
		
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(caseList.size());
		data.setMajor(major);
		data.setMinor(minor);
		data.setLessThan5years(countLessThan5Years);
		data.setGreaterThan5years(countGreaterthan5years);
		data.setNumberSuspended(empListSuspended.size());
		data.setCaseSuspended(caseListSuspended.size());
		data.setTotalemployee(countEmployees);
		
		
		
		
		
		return data;
	}
	
	private GroupAData getGroupAData(GlobalOrg s) {
		GroupAData data = new GroupAData();
		
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
//		List<CaseDetails> finalOut = caseRepo.findAll();		
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIsNotNull(s,groupList); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListIn(s,empLists);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotIn(true,false,s,true,s,groupList);
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empListSuspended,s);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId));
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId));
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdIn(s,2L,caseListId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdIn(s,Arrays.asList(2L),caseListId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdIn(true,s,caseListId);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByProsecutionProposalIn(proseList);
				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdIn(s,LocalDate.now(),caseListId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId()==1)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListIn(empList); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
	
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);

		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private GroupBData getGroupBData(GlobalOrg s) {
		GroupBData data = new GroupBData();
		
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(1L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
//		List<CaseDetails> finalOut = caseRepo.findAll();		
//		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
//				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
//				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
//		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIsNotNull(s,groupList); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListIn(s,empLists);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotIn(true,false,s,true,s,groupList);
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empListSuspended,s);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId));
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId));
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdIn(s,2L,caseListId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdIn(s,Arrays.asList(2L),caseListId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdIn(true,s,caseListId);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByProsecutionProposalIn(proseList);
				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdIn(s,LocalDate.now(),caseListId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId() ==2)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListIn(empList); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
	
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);

		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private CommonABData getGroupABData(GlobalOrg s) {
		CommonABData data = new CommonABData();
		List<Service_Group> groupList = new ArrayList<>();
//		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
//		List<CaseDetails> finalOut = caseRepo.findAll();		
//		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
//				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
//				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
//		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIsNotNull(s,groupList); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListIn(s,empLists);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotIn(true,false,s,true,s,groupList);
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgId(empListSuspended,s);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId));
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountGreaterthan5years += caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId));
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountLessThan5+=caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdIn(s,2L,caseListId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountMinor+=caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdIn(s,Arrays.asList(2L),caseListId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountMajor+=caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdIn(s,caseListId);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByProsecutionProposalIn(proseList);
				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountProse+=proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdIn(s,LocalDate.now(),caseListId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId()==1 || ss.getServicegroup().getId() ==2)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListIn(empList); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
	
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	
	private GroupAData getGroupADataIn(List<User> s,Set<Long> caseId) {
		GroupAData data = new GroupAData();
		
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
//		List<CaseDetails> finalOut = caseRepo.findAll();		
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserInAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserInAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListSuspended,s,caseId);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserInAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserInAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
//				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(true,s,caseListId,caseId);
				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserInAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseListId,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,caseId);
//				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
//				employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserInAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId()==1)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
	
		data.setDepartmentName(s.get(0).getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
		data.setCountCaseNo(countCases);

		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private GroupAData getGroupAData(GlobalOrg s,Set<Long> caseId) {
		GroupAData data = new GroupAData();
		
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
//		List<CaseDetails> finalOut = caseRepo.findAll();		
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListSuspended,s,caseId);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
//				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(true,s,caseListId,caseId);
				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseListId,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,caseId);
//				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
//				employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId()==1)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
	
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);

		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	
	private GroupBData getGroupBDataIn(List<User> s,Set<Long> caseId) {
		GroupBData data = new GroupBData();
		
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(1L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
//		List<CaseDetails> finalOut = caseRepo.findAll();		
//		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
//				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
//				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
//		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserInAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserInAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListSuspended,s,caseId);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserInAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserInAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
//				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(true,s,caseListId,caseId);
				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserInAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseListId,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,caseId);
//				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count();
//				employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserInAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId() ==2)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
	
		data.setDepartmentName(s.get(0).getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
		data.setCountCaseNo(countCases);

		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	
	private GroupBData getGroupBData(GlobalOrg s,Set<Long> caseId) {
		GroupBData data = new GroupBData();
		
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(1L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
//		List<CaseDetails> finalOut = caseRepo.findAll();		
//		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
//				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
//				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
//		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListSuspended,s,caseId);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
//				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(true,s,caseListId,caseId);
				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseListId,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,caseId);
//				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count();
//				employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId() ==2)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
		
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
		
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
	
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);

		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private CommonABData getGroupABDataIn(List<User> s,Set<Long> caseId) {
		CommonABData data = new CommonABData();
		List<Service_Group> groupList = new ArrayList<>();
//		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
//		List<CaseDetails> finalOut = caseRepo.findAll();		
//		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
//				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
//				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
//		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserInAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserInAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserInAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserInAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListSuspended,s,caseId);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserInAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountGreaterthan5years += caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserInAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountLessThan5+=caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountMinor+=caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserInAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountMajor+=caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
//				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,caseListId,caseId);
				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserInAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseListId,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,caseId);
//				proseCase.stream().forEach(ss->{
//					List<EmployeeDetails> pp = new ArrayList<>();
//					ss.getEmployeeList().forEach(sss->{
//						empProList.stream().forEach(ssss->{
//							if(sss.getCasedetails().getId()==ssss.getCasedetails().getId()) {
//								pp.add(sss);
//							}
//						});
//					});
//					ss.setEmployeeList(pp);
//				});
//				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count();
				employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserInAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId()==1 || ss.getServicegroup().getId() ==2)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
	
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
		data.setDepartmentName(s.get(0).getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private CommonABData getGroupABData(GlobalOrg s,Set<Long> caseId) {
		CommonABData data = new CommonABData();
		List<Service_Group> groupList = new ArrayList<>();
//		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
//		List<CaseDetails> finalOut = caseRepo.findAll();		
//		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
//				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
//				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
//		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListIdx = null;

		long countCases = caseList.size();
		caseListIdx= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
		final Set<Long> caseListId = caseListIdx;
		
		ExecutorService executorService = exe.getExe();

		Collection<Callable<String>> callables = new ArrayList<>();
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
				List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndIdNotIn(empListSuspended,s,caseId);
				data.setNumberSuspended(empListSuspended.size());
				data.setCaseSuspended(caseListSuspended.size());
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countGreaterthan5years = caseGreateThan5List.size();
				long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountGreaterthan5years += caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
				data.setGreaterThan5years(countGreaterthan5years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
				long countLessThan5Years = caseLessThan5List.size();
				long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountLessThan5+=caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountLessThan5years(employeeCountLessThan5);
				data.setLessThan5years(countLessThan5Years);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
				long minor = caseListMinor.size();
				long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountMinor+=caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMinor(employeeCountMinor);
				data.setMinor(minor);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
				long major = caseListMajor.size();
				long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
				employeeCountMajor+=caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
				data.setEmployeeCountMajor(employeeCountMajor);
				data.setMajor(major);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
//				List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,caseListId,caseId);
				List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseListId,caseId,true);
				List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,caseId);
//				proseCase.stream().forEach(ss->{
//					List<EmployeeDetails> pp = new ArrayList<>();
//					ss.getEmployeeList().forEach(sss->{
//						empProList.stream().forEach(ssss->{
//							if(sss.getCasedetails().getId()==ssss.getCasedetails().getId()) {
//								pp.add(sss);
//							}
//						});
//					});
//					ss.setEmployeeList(pp);
//				});
//				long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
				long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count();
				employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
				data.setNumberOfCasesWithProsecution(proseCase.size());
				data.setEmployeeCountProsecutionCases(employeeCountProse);
				return null;
			}						
		});
		
		callables.add(new Callable<String>() {
			@Override
			public String call() throws Exception {
				List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
				empList = empList.stream().filter(ss->ss.getServicegroup() != null && (ss.getServicegroup().getId()==1 || ss.getServicegroup().getId() ==2)).collect(Collectors.toList());
				List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
				long retiredCases = caseListRetired.size(); 
				data.setNumberOfCasesWithRetiredEmp(retiredCases);
				data.setEmployeeCountRetiredCases(empList.size());
				return null;
			}						
		});
		
	
		
		try {
			List<Future<String>> taskFutureList = executorService.invokeAll(callables);
			for (Future<String> future : taskFutureList) {
				try {
					future.get();
				} catch (Exception e) {
//					continue;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally{
//			  executorService.shutdown(); 
//			}
	
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private GroupAData getGroupADataSubDepartment(GlobalOrg s,SubDepartment subDep) {
		GroupAData data = new GroupAData();
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
		List<CaseDetails> finalOut = caseRepo.findAll();		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,subDep,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeListInAndIdNotIn(s,subDep,empLists,caseId);
		Set<Long> caseListId = null;

		long countCases = caseList.size();
		caseListId= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,subDep,true,empLists,subDep);
		List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIdNotIn(true,false,s,subDep,caseId,true,s,subDep,groupList,caseId);
		List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndIdNotIn(empListSuspended,s,subDep,caseId);
		
		
		List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,subDep,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countGreaterthan5years = caseGreateThan5List.size();
		long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
		
		
		List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,subDep,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countLessThan5Years = caseLessThan5List.size();
		long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountLessThan5years(employeeCountLessThan5);
		
		List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdAndIdInAndIdNotIn(s,subDep,2L,caseListId,caseId);
		long minor = caseListMinor.size();
		long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountMinor(employeeCountMinor);
		
		List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,subDep,Arrays.asList(2L),caseListId,caseId);
		long major = caseListMajor.size();
		long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountMajor(employeeCountMajor);

		
		List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(true,s,subDep,caseListId,caseId);
		List<CaseDetails> proseCase = caseRepo.findDistinctIdByProsecutionProposalInAndIdNotIn(proseList,caseId);
		long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setNumberOfCasesWithProsecution(proseCase.size());
		data.setEmployeeCountProsecutionCases(employeeCountProse);

	
		List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,subDep,LocalDate.now(),caseListId,caseId);
		List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
		long retiredCases = caseListRetired.size(); 
		data.setNumberOfCasesWithRetiredEmp(retiredCases);
		data.setEmployeeCountRetiredCases(empList.size());
		
		
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setMajor(major);
		data.setMinor(minor);
		data.setLessThan5years(countLessThan5Years);
		data.setGreaterThan5years(countGreaterthan5years);
		data.setNumberSuspended(empListSuspended.size());
		data.setCaseSuspended(caseListSuspended.size());
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private GroupBData getGroupBDataSubDepartment(GlobalOrg s,SubDepartment subDep) {
		GroupBData data = new GroupBData();
		
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(1L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
		List<CaseDetails> finalOut = caseRepo.findAll();		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,subDep,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeListInAndIdNotIn(s,subDep,empLists,caseId);
		Set<Long> caseListId = null;

		long countCases = caseList.size();
		caseListId= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,subDep,true,empLists,subDep);
		List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIdNotIn(true,false,s,subDep,caseId,true,s,subDep,groupList,caseId);
		List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndIdNotIn(empListSuspended,s,subDep,caseId);
		
		List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,subDep,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countGreaterthan5years = caseGreateThan5List.size();
		long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
		
		
		List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,subDep,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countLessThan5Years = caseLessThan5List.size();
		long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountLessThan5years(employeeCountLessThan5);
		
		List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdAndIdInAndIdNotIn(s,subDep,2L,caseListId,caseId);
		long minor = caseListMinor.size();
		long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountMinor(employeeCountMinor);
		
		List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,subDep,Arrays.asList(2L),caseListId,caseId);
		long major = caseListMajor.size();
		long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountMajor(employeeCountMajor);

		
		List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(true,s,subDep,caseListId,caseId);
		List<CaseDetails> proseCase = caseRepo.findDistinctIdByProsecutionProposalInAndIdNotIn(proseList,caseId);
		long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setNumberOfCasesWithProsecution(proseCase.size());
		data.setEmployeeCountProsecutionCases(employeeCountProse);

	
		List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,subDep,LocalDate.now(),caseListId,caseId);
		List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
		long retiredCases = caseListRetired.size(); 
		data.setNumberOfCasesWithRetiredEmp(retiredCases);
		data.setEmployeeCountRetiredCases(empList.size());
		
		
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setMajor(major);
		data.setMinor(minor);
		data.setLessThan5years(countLessThan5Years);
		data.setGreaterThan5years(countGreaterthan5years);
		data.setNumberSuspended(empListSuspended.size());
		data.setCaseSuspended(caseListSuspended.size());
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private CommonABData getGroupABDataSubDepartment(GlobalOrg s,SubDepartment subDep) {
		CommonABData data = new CommonABData();
		List<Service_Group> groupList = new ArrayList<>();
//		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
		List<CaseDetails> finalOut = caseRepo.findAll();		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,subDep,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeListInAndIdNotIn(s,subDep,empLists,caseId);
		Set<Long> caseListId = null;

		long countCases = caseList.size();
		caseListId= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,subDep,true,empLists,subDep);
		List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndServicegroupNotInAndCasedetailsIdNotIn(true,false,s,subDep,caseId,true,s,subDep,groupList,caseId);
		List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndIdNotIn(empListSuspended,s,subDep,caseId);
		
		List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,subDep,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countGreaterthan5years = caseGreateThan5List.size();
		long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountGreaterthan5years += caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
		
		
		List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,subDep,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countLessThan5Years = caseLessThan5List.size();
		long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountLessThan5+=caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountLessThan5years(employeeCountLessThan5);
		
		List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdAndIdInAndIdNotIn(s,subDep,2L,caseListId,caseId);
		long minor = caseListMinor.size();
		long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountMinor+=caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountMinor(employeeCountMinor);
		
		List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,subDep,Arrays.asList(2L),caseListId,caseId);
		long major = caseListMajor.size();
		long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountMajor+=caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountMajor(employeeCountMajor);

		
		List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,subDep,caseListId,caseId);
		List<CaseDetails> proseCase = caseRepo.findDistinctIdByProsecutionProposalInAndIdNotIn(proseList,caseId);
		long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountProse+=proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setNumberOfCasesWithProsecution(proseCase.size());
		data.setEmployeeCountProsecutionCases(employeeCountProse);

	
		List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,subDep,LocalDate.now(),caseListId,caseId);
		List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
		long retiredCases = caseListRetired.size(); 
		data.setNumberOfCasesWithRetiredEmp(retiredCases);
		data.setEmployeeCountRetiredCases(empList.size());
		
		
		data.setDepartmentName(s.getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setMajor(major);
		data.setMinor(minor);
		data.setLessThan5years(countLessThan5Years);
		data.setGreaterThan5years(countGreaterthan5years);
		data.setNumberSuspended(empListSuspended.size());
		data.setCaseSuspended(caseListSuspended.size());
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private List<SubDepartmentData> getSubDepartmentDataIn(List<User> user) {
		List<SubDepartmentData> subDepartmentList = new ArrayList<>();
		List<SubDepartment> subDepartment = user.stream().filter(s->s.getSubDepartment() != null).map(s->s.getSubDepartment()).collect(Collectors.toList());
		
		List<CaseDetails> finalOut = caseRepo.findAll();		
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		
		subDepartment.stream().forEach(s->{
			SubDepartmentData data = new SubDepartmentData();
//			long countCases = caseRepo.countDistinctIdByCurrentUserSubDepartment(s);
			data.setSubdepartmentid(s.getId());
			long countCases = caseRepo.countDistinctIdByCurrentUserSubDepartmentAndIdNotIn(s,caseId);
			long countEmployees = empRepo.countDistinctCasedetailsByCurrentUserSubDepartmentAndCasedetailsIdNotIn(s,caseId);
//			long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserSubDepartmentAndEmployeeSuspendedAndCasedetailsIsNotNull(s,true);
			List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserSubDepartmentAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,caseId,"Still Under Suspension");
			List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserSubDepartmentAndIdNotIn(empListSuspended,s,caseId);

			List<CaseDetails> caseGreater5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
			long countGreaterthan5years = caseGreater5List.size();
			long employeeCountGreater5 = caseGreater5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setEmployeeCountGreaterThan5years(employeeCountGreater5);

			List<CaseDetails> caseLess5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
			long countLessThan5Years = caseLess5List.size();
			long employeeCountLessThan5 = caseLess5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setEmployeeCountLessThan5years(employeeCountLessThan5);
			
			List<CaseDetails> minorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableIdAndIdNotIn(s,2L,caseId);
			long minor = minorCaseList.size();
			long employeeCountminor = minorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setEmployeeCountMinor(employeeCountminor); 
			
			List<CaseDetails> majorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdNotIn(s,Arrays.asList(2L),caseId);
			long major = majorCaseList.size();
			long employeeCountMajor = majorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setEmployeeCountMajor(employeeCountMajor);
			

			List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdNotIn(true,s,caseId);
			List<CaseDetails> caseProsec = caseRepo.findDistinctIdByProsecutionProposalInAndIdNotIn(proseList,caseId);
			long prosecutionCount = caseProsec.size();
			long employeeCountProse = caseProsec.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setNumberOfCasesWithProsecution(prosecutionCount);
			data.setEmployeeCountProsecutionCases(employeeCountProse);

			List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,LocalDate.now(),caseId);
			List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
			long retiredCases = caseListRetired.size(); 
			data.setNumberOfCasesWithRetiredEmp(retiredCases);
			data.setEmployeeCountRetiredCases(empList.size());
			
			
			data.setDepartmentName(s.getSubdepartmentNameEn());
			data.setCountCaseNo(countCases);
			data.setMajor(major);
			data.setMinor(minor);
			data.setLessThan5years(countLessThan5Years);
			data.setGreaterThan5years(countGreaterthan5years);
			data.setNumberSuspended(empListSuspended.size());
			data.setCaseSuspended(caseListSuspended.size());
			
			data.setTotalemployee(countEmployees);
			subDepartmentList.add(data);
		});
		
		return subDepartmentList;
	}
	
	private List<SubDepartmentData> getSubDepartmentData(User user) {
		List<SubDepartmentData> subDepartmentList = new ArrayList<>();
		List<SubDepartment> subDepartment = subDepartmentRepo.findByGlobalorgAndActive(user.getPimsEmployee().getGlobalOrgId(),true);
		
		ExecutorService executorService = exe.getExe();
		List<Callable<String>> callList = new ArrayList<>();
//		List<CaseDetails> finalOut = caseRepo.findByCurrentUser(s);		
		Long casecount = caseRepo.count();
		List<Integer> largeList = new ArrayList<>();
//		List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
		while(true) {
			if(casecount>10) {
				largeList.add(10);
			} else {
				if(casecount>0 && casecount<=10) {
					largeList.add(casecount.intValue());
				} else {
					break;
				}
			}
			casecount=casecount-10;
		}
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
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		
		subDepartment.stream().forEach(s->{
			SubDepartmentData data = new SubDepartmentData();
//			long countCases = caseRepo.countDistinctIdByCurrentUserSubDepartment(s);
			data.setSubdepartmentid(s.getId());
			long countCases = caseRepo.countDistinctIdByCurrentUserSubDepartmentAndIdNotIn(s,caseId);
			long countEmployees = empRepo.countDistinctCasedetailsByCurrentUserSubDepartmentAndCasedetailsIdNotIn(s,caseId);
//			long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserSubDepartmentAndEmployeeSuspendedAndCasedetailsIsNotNull(s,true);
			List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserSubDepartmentAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserSubDepartmentAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,caseId,"Still Under Suspension");
			List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserSubDepartmentAndIdNotIn(empListSuspended,s,caseId);

			List<CaseDetails> caseGreater5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
			long countGreaterthan5years = caseGreater5List.size();
			long employeeCountGreater5 = caseGreater5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setEmployeeCountGreaterThan5years(employeeCountGreater5);

			List<CaseDetails> caseLess5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNull(s,true,LocalDate.now().minusYears(5)),caseId);
			long countLessThan5Years = caseLess5List.size();
			long employeeCountLessThan5 = caseLess5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setEmployeeCountLessThan5years(employeeCountLessThan5);
			
			List<CaseDetails> minorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableIdAndIdNotIn(s,2L,caseId);
			long minor = minorCaseList.size();
			long employeeCountminor = minorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setEmployeeCountMinor(employeeCountminor); 
			
			List<CaseDetails> majorCaseList = caseRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndRuleApplicableIdNotInAndIdNotIn(s,Arrays.asList(2L),caseId);
			long major = majorCaseList.size();
			long employeeCountMajor = majorCaseList.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setEmployeeCountMajor(employeeCountMajor);
			

			List<ProsecutionProposalDetails> proseList = prosecRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCurrentUserSubDepartmentAndCasedetailsIsNotNullAndCasedetailsIdNotIn(true,s,caseId);
			List<CaseDetails> caseProsec = caseRepo.findDistinctIdByProsecutionProposalInAndIdNotIn(proseList,caseId);
			long prosecutionCount = caseProsec.size();
			long employeeCountProse = caseProsec.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).size()).sum();
			data.setNumberOfCasesWithProsecution(prosecutionCount);
			data.setEmployeeCountProsecutionCases(employeeCountProse);

			List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserSubDepartmentAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,LocalDate.now(),caseId);
			List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId); 
			long retiredCases = caseListRetired.size(); 
			data.setNumberOfCasesWithRetiredEmp(retiredCases);
			data.setEmployeeCountRetiredCases(empList.size());
			
			
			data.setDepartmentName(s.getSubdepartmentNameEn());
			data.setCountCaseNo(countCases);
			data.setMajor(major);
			data.setMinor(minor);
			data.setLessThan5years(countLessThan5Years);
			data.setGreaterThan5years(countGreaterthan5years);
			data.setNumberSuspended(empListSuspended.size());
			data.setCaseSuspended(caseListSuspended.size());
			
			data.setTotalemployee(countEmployees);
			subDepartmentList.add(data);
		});
		
		return subDepartmentList;
	}
	
	private GroupAData getGroupADataCurrentUser(User s) {
		GroupAData data = new GroupAData();
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
		ExecutorService executorService = exe.getExe();
		List<Callable<String>> callList = new ArrayList<>();
//		List<CaseDetails> finalOut = caseRepo.findByCurrentUser(s);		
		Long casecount = caseRepo.countByCurrentUser(s);
		List<Integer> largeList = new ArrayList<>();
//		List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
		while(true) {
			if(casecount>10) {
				largeList.add(10);
			} else {
				if(casecount>0 && casecount<=10) {
					largeList.add(casecount.intValue());
				} else {
					break;
				}
			}
			casecount=casecount-10;
		}
		List<CaseDetails> finalOut = new ArrayList<>();
		if(!largeList.isEmpty()) {
			for(Integer i=1;i<=largeList.size();i++) {
				final Integer index = i ;
				callList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {																		
						Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
						Page<CaseDetails> caseList = caseRepo.findByCurrentUser(s,pageable);
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
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListId = null;

		long countCases = caseList.size();
		caseListId= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
		List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
		List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListSuspended,s,caseId);
		
		
		List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countGreaterthan5years = caseGreateThan5List.size();
		long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
		
		
		List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countLessThan5Years = caseLessThan5List.size();
		long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountLessThan5years(employeeCountLessThan5);
		
		List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
		long minor = caseListMinor.size();
		long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountMinor(employeeCountMinor);
		
		List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
		long major = caseListMajor.size();
		long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountMajor(employeeCountMajor);

		
		List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseId,true);
		List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empProList,caseId,s);
//		long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
//		employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
		data.setNumberOfCasesWithProsecution(proseCase.size());
		data.setEmployeeCountProsecutionCases(employeeCountProse);

	
		List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
		List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empList,caseId,s); 
		long retiredCases = caseListRetired.size(); 
		data.setNumberOfCasesWithRetiredEmp(retiredCases);
		long employeeRetired = caseListRetired.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountRetiredCases(employeeRetired);
		
		
		data.setDepartmentName(s.getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setMajor(major);
		data.setMinor(minor);
		data.setLessThan5years(countLessThan5Years);
		data.setGreaterThan5years(countGreaterthan5years);
		data.setNumberSuspended(empListSuspended.size());
		data.setCaseSuspended(caseListSuspended.size());
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private GroupBData getGroupBDataCurrentUser(User s) {
		GroupBData data = new GroupBData();
		
		List<Service_Group> groupList = new ArrayList<>();
		groupList.add(new Service_Group(1L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
		ExecutorService executorService = exe.getExe();
		List<Callable<String>> callList = new ArrayList<>();
//		List<CaseDetails> finalOut = caseRepo.findByCurrentUser(s);		
		Long casecount = caseRepo.countByCurrentUser(s);
		List<Integer> largeList = new ArrayList<>();
//		List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
		while(true) {
			if(casecount>10) {
				largeList.add(10);
			} else {
				if(casecount>0 && casecount<=10) {
					largeList.add(casecount.intValue());
				} else {
					break;
				}
			}
			casecount=casecount-10;
		}
		List<CaseDetails> finalOut = new ArrayList<>();
		if(!largeList.isEmpty()) {
			for(Integer i=1;i<=largeList.size();i++) {
				final Integer index = i ;
				callList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {																		
						Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
						Page<CaseDetails> caseList = caseRepo.findByCurrentUser(s,pageable);
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
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListId = null;

		long countCases = caseList.size();
		caseListId= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
		List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
		
		List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListSuspended,s,caseId);
		
		List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countGreaterthan5years = caseGreateThan5List.size();
		long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
		
		
		List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countLessThan5Years = caseLessThan5List.size();
		long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountLessThan5years(employeeCountLessThan5);
		
		List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
		long minor = caseListMinor.size();
		long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountMinor(employeeCountMinor);
		
		List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
		long major = caseListMajor.size();
		long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountMajor(employeeCountMajor);

		
		List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseId,true);
		List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empProList,caseId,s);
//		long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count();
//		employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
		data.setNumberOfCasesWithProsecution(proseCase.size());
		data.setEmployeeCountProsecutionCases(employeeCountProse);

	
		List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
		List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empList,caseId,s); 
		long retiredCases = caseListRetired.size(); 
		data.setNumberOfCasesWithRetiredEmp(retiredCases);
		long employeeRetired = caseListRetired.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		data.setEmployeeCountRetiredCases(employeeRetired);

		
		
		data.setDepartmentName(s.getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setMajor(major);
		data.setMinor(minor);
		data.setLessThan5years(countLessThan5Years);
		data.setGreaterThan5years(countGreaterthan5years);
		data.setNumberSuspended(empListSuspended.size());
		data.setCaseSuspended(caseListSuspended.size());
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	private CommonABData getGroupABDataCurrentUser(User s) {
		CommonABData data = new CommonABData();
		List<Service_Group> groupList = new ArrayList<>();
//		groupList.add(new Service_Group(2L));
		groupList.add(new Service_Group(3L));
		groupList.add(new Service_Group(4L));
		
		ExecutorService executorService = exe.getExe();
		List<Callable<String>> callList = new ArrayList<>();
//		List<CaseDetails> finalOut = caseRepo.findByCurrentUser(s);		
		Long casecount = caseRepo.countByCurrentUser(s);
		List<Integer> largeList = new ArrayList<>();
//		List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
		while(true) {
			if(casecount>10) {
				largeList.add(10);
			} else {
				if(casecount>0 && casecount<=10) {
					largeList.add(casecount.intValue());
				} else {
					break;
				}
			}
			casecount=casecount-10;
		}
		List<CaseDetails> finalOut = new ArrayList<>();
		if(!largeList.isEmpty()) {
			for(Integer i=1;i<=largeList.size();i++) {
				final Integer index = i ;
				callList.add(new Callable<String>() {
					@Override
					public String call() throws Exception {																		
						Pageable pageable = PageRequest.of(index-1, 10, Sort.by("id"));
						Page<CaseDetails> caseList = caseRepo.findByCurrentUser(s,pageable);
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
		Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
				&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
				&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
		if(caseIdx.isEmpty()) {
			caseIdx.add(0L);
		}
		List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
		if(!casesTransferList.isEmpty()) {
			caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
		}
		final Set<Long> caseId = caseIdx;
		
		List<EmployeeDetails> empLists = empRepo.findByCurrentUserAndServicegroupNotInAndCasedetailsIsNotNullAndCasedetailsIdNotIn(s,groupList,caseId); 
		List<CaseDetails> caseList = caseRepo.findDistinctIdByCurrentUserAndEmployeeListInAndIdNotIn(s,empLists,caseId);
		Set<Long> caseListId = null;

		long countCases = caseList.size();
		caseListId= caseList.stream().map(CaseDetails::getId).collect(Collectors.toSet());
//		long countSuspended = susRepo.countDistinctCasedetailsByCurrentUserAndEmployeeSuspendedAndCasedetailsIsNotNullAndCasedetailsEmployeeListIn(s,true,empLists);
		List<EmployeeDetails> empListSuspended = empRepo.findBySuspensionDetailsEmployeeSuspendedAndReInstatedDetailsEmployeeReInstatedAndCurrentUserAndCasedetailsIdNotInOrSuspensionDetailsEmployeeSuspendedAndReInstatedDetailsIsNullAndCurrentUserAndServicegroupNotInAndCasedetailsIdNotInAndSuspensionDetailsCurrentStatus(true,false,s,caseId,true,s,groupList,caseId,"Still Under Suspension");
		List<CaseDetails> caseListSuspended = caseRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListSuspended,s,caseId);
		
		List<CaseDetails> caseGreateThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserAndChargesheetIssuedAndChargesheetDateLessThanAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countGreaterthan5years = caseGreateThan5List.size();
		long employeeCountGreaterthan5years = caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountGreaterthan5years += caseGreateThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountGreaterThan5years(employeeCountGreaterthan5years);
		
		
		List<CaseDetails> caseLessThan5List = caseRepo.findDistinctIdByChargeSheetListInAndIdNotIn(chargeRepo.findDistinctCasedetailsByCurrentUserAndChargesheetIssuedAndChargesheetDateGreaterThanEqualAndCaseDetailsIsNotNullAndCaseDetailsIdIn(s,true,LocalDate.now().minusYears(5),caseListId),caseId);
		long countLessThan5Years = caseLessThan5List.size();
		long employeeCountLessThan5 = caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountLessThan5+=caseLessThan5List.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountLessThan5years(employeeCountLessThan5);
		
		List<CaseDetails> caseListMinor = caseRepo.findDistinctCasedetailsByCurrentUserAndRuleApplicableIdAndIdInAndIdNotIn(s,2L,caseListId,caseId);
		long minor = caseListMinor.size();
		long employeeCountMinor = caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountMinor+=caseListMinor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountMinor(employeeCountMinor);
		
		List<CaseDetails> caseListMajor = caseRepo.findDistinctCasedetailsByCurrentUserAndRuleApplicableIdNotInAndIdInAndIdNotIn(s,Arrays.asList(2L),caseListId,caseId);
		long major = caseListMajor.size();
		long employeeCountMajor = caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		employeeCountMajor+=caseListMajor.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count()).sum();
		data.setEmployeeCountMajor(employeeCountMajor);

		
		List<EmployeeDetails> empProList = empRepo.findDistinctCasedetailsByCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(s,caseId,true);
		List<CaseDetails> proseCase = caseRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empProList,caseId,s);
//		long employeeCountProse = proseCase.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count()).sum();
		long employeeCountProse = empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==1).count();
		employeeCountProse+=empProList.stream().filter(sss->sss.getServicegroup() !=null &&  sss.getServicegroup().getId()==2).count();
		data.setNumberOfCasesWithProsecution(proseCase.size());
		data.setEmployeeCountProsecutionCases(employeeCountProse);

	
		List<EmployeeDetails> empList = empRepo.findDistinctCasedetailsByCurrentUserAndSuperannuationDateLessThanAndCasedetailsIsNotNullAndCasedetailsIdInAndCasedetailsIdNotIn(s,LocalDate.now(),caseListId,caseId);
		List<CaseDetails> caseListRetired = caseRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empList,caseId,s); 
		long retiredCases = caseListRetired.size(); 
		data.setNumberOfCasesWithRetiredEmp(retiredCases);
		long employeeRetired = caseListRetired.parallelStream().mapToLong(ss->empRepo.findByCasedetailsId(ss.getId()).stream().filter(sss->sss.getServicegroup() !=null &&  (sss.getServicegroup().getId()==1 || sss.getServicegroup().getId()==2)).count()).sum();
		data.setEmployeeCountRetiredCases(employeeRetired);

		
		
		data.setDepartmentName(s.getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
		data.setCountCaseNo(countCases);
		data.setMajor(major);
		data.setMinor(minor);
		data.setLessThan5years(countLessThan5Years);
		data.setGreaterThan5years(countGreaterthan5years);
		data.setNumberSuspended(empListSuspended.size());
		data.setCaseSuspended(caseListSuspended.size());
		data.setTotalemployee(empLists.size());
		
		
		return data;
	}
	
	
	@GetMapping("/groupabemployeedatafetching")
	public String groupabemployeedatafetching(Model model) {
		LOG.info("this method is calling for  fetch data of group AB employee");
		ExecutorService executorService = exe.getExe();
		final User user1 = userRepository.findByEmail(SecurityUtils.getCurrentUserLogin());
		List<DashboardData> dataList = new ArrayList<>();
		if (user1.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_SUPERADMIN")) {

			Collection<Callable<String>> callables = new ArrayList<>();
			List<GlobalOrg> orgList = globalorgRepo.findByActive(true);
			long starttime1 = System.currentTimeMillis();
//			List<CaseDetails> finalOut = caseRepo.findAll();		
			List<Callable<String>> callList = new ArrayList<>();
			Long casecount = caseRepo.count();
			List<Integer> largeList = new ArrayList<>();
//			List<List<Integer>> output = ListUtils.partition(largeList, targetSize);
			while (true) {
				if (casecount > 10) {
					largeList.add(10);
				} else {
					if (casecount > 0 && casecount <= 10) {
						largeList.add(casecount.intValue());
					} else {
						break;
					}
				}
				casecount = casecount - 10;
			}
			List<CaseDetails> finalOut = new ArrayList<>();
			if (casecount > 10) {
				for (Integer i = 1; i <= largeList.size(); i++) {
					final Integer index = i;
					callList.add(new Callable<String>() {
						@Override
						public String call() throws Exception {
							Pageable pageable = PageRequest.of(index - 1, 10, Sort.by("id"));
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

			LOG.info("First Step time required dashboard :" + TimeUnit.MILLISECONDS.toSeconds(endtime1 - starttime1));

			Set<Long> caseIdx = finalOut.stream()
					.filter(ss -> !ss.getFinaloutList().isEmpty()
							&& ss.getFinaloutList().stream().allMatch(FinalOutcomeDetails::isWhetherCaseFinallyDecided)
							&& ss.getFinaloutList().size() == ss.getEmployeeList().size())
					.map(CaseDetails::getId).collect(Collectors.toSet());
			endtime1 = System.currentTimeMillis();
			LOG.info("First Step time required dashboard :" + TimeUnit.MILLISECONDS.toSeconds(endtime1 - starttime1));
			if (caseIdx.isEmpty()) {
				caseIdx.add(0L);
			}
			List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
			if (!casesTransferList.isEmpty()) {
				caseIdx.addAll(casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
			}

			final Set<Long> caseId = caseIdx;
			for (GlobalOrg s : orgList) {

				callables.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						final DashboardData data = new DashboardData();
						Collection<Callable<String>> callablestiny = new ArrayList<>();

						callablestiny.add(new Callable<String>() {
							@Override
							public String call() throws Exception {
								data.setDataA(getGroupAData(s, caseId));
								return null;
							}
						});
						callablestiny.add(new Callable<String>() {
							@Override
							public String call() throws Exception {
								data.setDataB(getGroupBData(s, caseId));
								return null;
							}
						});
						callablestiny.add(new Callable<String>() {
							@Override
							public String call() throws Exception {
								data.setDataAB(getGroupABData(s, caseId));
								return null;
							}
						});

						/* Commented by vijay on 11/10/2023 */

//						callablestiny.add(new Callable<String>() {
//							@Override
//							public String call() throws Exception {
//								data.setDataCD(getGroupCDData(s,caseId));
//								return null;
//							}
//						});

						try {
							List<Future<String>> taskFutureList = executorService.invokeAll(callablestiny);
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

						dataList.add(data);
						return null;
					}
				});

			}
			try {
				long starttime3 = System.currentTimeMillis();
				List<Future<String>> taskFutureList = executorService.invokeAll(callables);
				for (Future<String> future : taskFutureList) {
					try {
						future.get();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				long endtime3 = System.currentTimeMillis();
				LOG.info("Time required for final step:" + TimeUnit.MILLISECONDS.toSeconds(endtime3 - starttime3));
			} catch (Exception e) {
				e.printStackTrace();
			}
			model.addAttribute("dataList", dataList);
			LOG.info("data list methods size : : " + dataList.size());

		} else {
			if (user1.getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains("ROLE_SECRETARY")) {
				List<SubDepartment> subList = user1.getSecsubList().stream().filter(s -> s.isActive())
						.map(s -> s.getSubDepartment()).collect(Collectors.toList());
				List<User> userList = new ArrayList<>();
				if (subList.isEmpty()) {
					userList = userRepository.findBySubDepartmentIn(
							subDepartmentRepo.findByGlobalorg(user1.getPimsEmployee().getGlobalOrgId()));
					userList.add(user1);
				} else {
					userList = userRepository.findBySubDepartmentIn(subList);
				}
				userList.add(user1);
				List<User> uu = userRepository.findByPimsEmployeeGlobalOrgId(user1.getPimsEmployee().getGlobalOrgId());
				for (User uux : uu) {
					if (uux.getRoles().stream().map(Role::getName).collect(Collectors.toList())
							.contains("ROLE_DEPARTMENTADMIN")) {
						userList.add(uux);
						break;
					}
				}
				List<CaseDetails> finalOut = caseRepo.findByCurrentUserIn(userList);
				Set<Long> caseIdx = finalOut.stream()
						.filter(ss -> !ss.getFinaloutList().isEmpty()
								&& ss.getFinaloutList().stream().allMatch(sss -> sss.isWhetherCaseFinallyDecided())
								&& ss.getFinaloutList().size() == ss.getEmployeeList().size())
						.map(ss -> ss.getId()).collect(Collectors.toSet());
				if (caseIdx.isEmpty()) {
					caseIdx.add(0L);
				}
				List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
				if (!casesTransferList.isEmpty()) {
					caseIdx.addAll(
							casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
				}
				final Set<Long> caseId = caseIdx;

				DashboardData data = new DashboardData();
				Collection<Callable<String>> callablestiny = new ArrayList<>();
				final List<User> userList1 = userList;
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataA(getGroupADataIn(userList1, caseId));
						return null;
					}
				});
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataB(getGroupBDataIn(userList1, caseId));
						return null;
					}
				});
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataAB(getGroupABDataIn(userList1, caseId));
						return null;
					}
				});


				try {
					List<Future<String>> taskFutureList = executorService.invokeAll(callablestiny);
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

				
				
				
				
//				data.setSubDepartmentList(getSubDepartmentDataIn(userList));
				dataList.add(data);
				model.addAttribute("dataList", dataList);

			} else if (user1.getRoles().stream().map(Role::getName).collect(Collectors.toList())
					.contains("ROLE_DEPARTMENTADMIN")) {
				GlobalOrg s = user1.getPimsEmployee().getGlobalOrgId();
				List<CaseDetails> finalOut = caseRepo.findByCurrentUserPimsEmployeeGlobalOrgId(s);
				Set<Long> caseIdx = finalOut.stream()
						.filter(ss -> !ss.getFinaloutList().isEmpty()
								&& ss.getFinaloutList().stream().allMatch(sss -> sss.isWhetherCaseFinallyDecided())
								&& ss.getFinaloutList().size() == ss.getEmployeeList().size())
						.map(ss -> ss.getId()).collect(Collectors.toSet());
				if (caseIdx.isEmpty()) {
					caseIdx.add(0L);
				}
				List<CaseDetails> casesTransferList = caseRepo.findByActiveForTransfer(true);
				if (!casesTransferList.isEmpty()) {
					caseIdx.addAll(
							casesTransferList.parallelStream().map(CaseDetails::getId).collect(Collectors.toList()));
				}
				final Set<Long> caseId = caseIdx;

				DashboardData data = new DashboardData();
				Collection<Callable<String>> callablestiny = new ArrayList<>();
				
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataA(getGroupAData(s, caseId));
						return null;
					}
				});
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataB(getGroupBData(s, caseId));
						return null;
					}
				});
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataAB(getGroupABData(s, caseId));
						return null;
					}
				});


				try {
					List<Future<String>> taskFutureList = executorService.invokeAll(callablestiny);
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
				
			
				
				
//				data.setSubDepartmentList(getSubDepartmentData(user1));
				dataList.add(data);
				model.addAttribute("dataList", dataList);
//						}

			} else {
//						GlobalOrg s = user1.getPimsEmployee().getGlobalOrgId();
				DashboardData data = new DashboardData();
				
				Collection<Callable<String>> callablestiny = new ArrayList<>();
				
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataA(getGroupADataCurrentUser(user1));
						return null;
					}
				});
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataB(getGroupBDataCurrentUser(user1));
						return null;
					}
				});
				callablestiny.add(new Callable<String>() {
					@Override
					public String call() throws Exception {
						data.setDataAB(getGroupABDataCurrentUser(user1));
						return null;
					}
				});


				try {
					List<Future<String>> taskFutureList = executorService.invokeAll(callablestiny);
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
				
				
			
				
				
//				data.setSubDepartmentList(getSubDepartmentData(user1));
				dataList.add(data);
				model.addAttribute("dataList", dataList);

			}
		}
		return "DeptInquiryViews/de_registration_group_ab_employee_data_with_ajaxcall.html :: EmployeeGroupABFrag";

	}
	
	
}
