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
import com.ehrms.deptenq.models.Gender;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.GenderRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;



@Controller
@RequestMapping("departmentalEnquiry")
public class GenderWiseReportsController {

	@Autowired
	private GenderRepository genderRepo;


	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserService userService;



	@Autowired
	private CaseDetailsRepository caseDetailsRepo;
	
	@Autowired
	private IGlobalOrgRepository globalRepo;



	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@Autowired
	private IEmployeeDetailsRepository empRepo;
	
	/* added on 03/01/2023 */
	@Autowired
	private FinalOutcomeRepository finalRepo;

	
	private static final Logger LOG = LoggerFactory.getLogger(GenderWiseReportsController.class);

	
	

	/* Added on 17-11-2022 case details report page */

	@GetMapping("/genderWiseReportsController/{page}")
	public String ruleApplicableReportsController(Model model, @PathVariable("page") int page,
			@RequestParam(name = "gender", required = false) String gender,
			@RequestParam(name = "globalorg", required = false) String globalorg,
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
		model.addAttribute("globalList", globalRepo.findByActive(true));

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
		
		List<Gender> genderMaster = null;
		if(StringUtils.isNotBlank(gender) && !gender.equalsIgnoreCase("null")) {
			if(gender.equalsIgnoreCase("1")) {
				genderMaster = genderRepo.findByIdIn(Arrays.asList(1L));
			}
			if(gender.equalsIgnoreCase("2")) {
				genderMaster = genderRepo.findByIdIn(Arrays.asList(2L));
			}
			if(gender.equalsIgnoreCase("4")) {
				genderMaster = genderRepo.findByIdIn(Arrays.asList(4L));
			}
		} else {
			genderMaster = genderRepo.findByIdIn(Arrays.asList(1L,2L,4L));
		}
		if(StringUtils.isNotBlank(gender) &&  gender.equalsIgnoreCase("null")) {
			gender = "";
		}
		model.addAttribute("gender", gender);
		if(globalorg == null) {
			globalorg = "";
		}
		model.addAttribute("globalorg", globalorg);

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			
			
			if(StringUtils.isBlank(globalorg)) {
				List<EmployeeDetails> empList = empRepo.findByGenderInAndCasedetailsIdNotIn(genderRepo.findByIdIn(Arrays.asList(1L,2L,4L)),caseId);
				List<EmployeeDetails> maleList = empRepo.findByGenderIdAndCasedetailsIdNotIn(1L,caseId);
				List<EmployeeDetails> femaleList = empRepo.findByGenderIdAndCasedetailsIdNotIn(2L,caseId);
				List<EmployeeDetails> transgenderList = empRepo.findByGenderIdAndCasedetailsIdNotIn(4L,caseId);
				
				List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empList,caseId);	
				
				
				model.addAttribute("totalCases", allGenCase.size());
				model.addAttribute("totalEmpCount", empList.size());
				
				List<EmployeeDetails> empListFil = empRepo.findByGenderInAndCasedetailsIdNotIn(genderMaster,caseId);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil,pageable,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empListFil,allPage,caseId);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				
				model.addAttribute("empCount", 
						empListFil.size());
							
				
				List<CaseDetails> maleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(maleList,caseId);
				model.addAttribute("maleCases", maleCaseList.size());
				model.addAttribute("empMaleCount", maleList.size());
				
				
				List<CaseDetails> femaleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(femaleList,caseId);
				model.addAttribute("femaleCases", femaleCaseList.size());
				model.addAttribute("empFemaleCount", femaleList.size());
				
				List<CaseDetails> transCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(transgenderList,caseId);
				model.addAttribute("transCases", transCaseList.size());
				model.addAttribute("empTransCount", transgenderList.size());
			} else {
				model.addAttribute("globalorg", Long.valueOf(globalorg));
				List<EmployeeDetails> empList = empRepo.findByGenderInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(genderRepo.findByIdIn(Arrays.asList(1L,2L,4L)),Long.valueOf(globalorg),caseId);
				List<EmployeeDetails> maleList = empRepo.findByGenderIdAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(1L,Long.valueOf(globalorg),caseId);
				List<EmployeeDetails> femaleList = empRepo.findByGenderIdAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(2L,Long.valueOf(globalorg),caseId);
				List<EmployeeDetails> transgenderList = empRepo.findByGenderIdAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(4L,Long.valueOf(globalorg),caseId);
				
				List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,Long.valueOf(globalorg),caseId);	
				
				
				model.addAttribute("totalCases", allGenCase.size());
				model.addAttribute("totalEmpCount", empList.size());
				
				List<EmployeeDetails> empListFil = empRepo.findByGenderInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(genderMaster,Long.valueOf(globalorg),caseId);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListFil,pageable,Long.valueOf(globalorg),caseId);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListFil,allPage,Long.valueOf(globalorg),caseId);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				
				model.addAttribute("empCount", 
						empListFil.size());
							
				
				List<CaseDetails> maleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(maleList,Long.valueOf(globalorg),caseId);
				model.addAttribute("maleCases", maleCaseList.size());
				model.addAttribute("empMaleCount", maleList.size());
				
				
				List<CaseDetails> femaleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(femaleList,Long.valueOf(globalorg),caseId);
				model.addAttribute("femaleCases", femaleCaseList.size());
				model.addAttribute("empFemaleCount", femaleList.size());
				
				List<CaseDetails> transCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(transgenderList,Long.valueOf(globalorg),caseId);
				model.addAttribute("transCases", transCaseList.size());
				model.addAttribute("empTransCount", transgenderList.size());
			}
			

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
			
				GlobalOrg org = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId();
				globalorg = String.valueOf(org.getId());
				model.addAttribute("globalorg", org.getId());
				List<EmployeeDetails> empList = empRepo.findByGenderInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(genderRepo.findByIdIn(Arrays.asList(1L,2L,4L)),Long.valueOf(globalorg),caseId);
				List<EmployeeDetails> maleList = empRepo.findByGenderIdAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(1L,Long.valueOf(globalorg),caseId);
				List<EmployeeDetails> femaleList = empRepo.findByGenderIdAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(2L,Long.valueOf(globalorg),caseId);
				List<EmployeeDetails> transgenderList = empRepo.findByGenderIdAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(4L,Long.valueOf(globalorg),caseId);
				
				List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empList,Long.valueOf(globalorg),caseId);	
				
				
				model.addAttribute("totalCases", allGenCase.size());
				model.addAttribute("totalEmpCount", empList.size());
				
				List<EmployeeDetails> empListFil = empRepo.findByGenderInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndCasedetailsIdNotIn(genderMaster,Long.valueOf(globalorg),caseId);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListFil,pageable,Long.valueOf(globalorg),caseId);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(empListFil,allPage,Long.valueOf(globalorg),caseId);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empListFil.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				
				model.addAttribute("empCount", 
						empListFil.size());
							
				
				List<CaseDetails> maleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(maleList,Long.valueOf(globalorg),caseId);
				model.addAttribute("maleCases", maleCaseList.size());
				model.addAttribute("empMaleCount", maleList.size());
				
				
				List<CaseDetails> femaleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(femaleList,Long.valueOf(globalorg),caseId);
				model.addAttribute("femaleCases", femaleCaseList.size());
				model.addAttribute("empFemaleCount", femaleList.size());
				
				List<CaseDetails> transCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdIdAndIdNotIn(transgenderList,Long.valueOf(globalorg),caseId);
				model.addAttribute("transCases", transCaseList.size());
				model.addAttribute("empTransCount", transgenderList.size());
			

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
			
			
			List<EmployeeDetails> empList = empRepo.findByGenderInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(genderRepo.findByIdIn(Arrays.asList(1L,2L,4L)),userList,caseId);
			List<EmployeeDetails> maleList = empRepo.findByGenderIdAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(1L,userList,caseId);
			List<EmployeeDetails> femaleList = empRepo.findByGenderIdAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(2L,userList,caseId);
			List<EmployeeDetails> transgenderList = empRepo.findByGenderIdAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(4L,userList,caseId);
			
			List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empList,userList,caseId);	
			
			
			model.addAttribute("totalCases", allGenCase.size());
			model.addAttribute("totalEmpCount", empList.size());
			
			List<EmployeeDetails> empListFil = empRepo.findByGenderInAndCasedetailsCurrentUserInAndCasedetailsIdNotIn(genderMaster,userList,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListFil,userList,pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(empListFil,userList,allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("empCount", 
					empListFil.size());
						
			
			List<CaseDetails> maleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(maleList,userList,caseId);
			model.addAttribute("maleCases", maleCaseList.size());
			model.addAttribute("empMaleCount", maleList.size());
			
			
			List<CaseDetails> femaleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(femaleList,userList,caseId);
			model.addAttribute("femaleCases", femaleCaseList.size());
			model.addAttribute("empFemaleCount", femaleList.size());
			
			List<CaseDetails> transCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserInAndIdNotIn(transgenderList,userList,caseId);
			model.addAttribute("transCases", transCaseList.size());
			model.addAttribute("empTransCount", transgenderList.size());
			
			
		}

		else {
			List<EmployeeDetails> empList = empRepo.findByGenderInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(genderRepo.findByIdIn(Arrays.asList(1L,2L,4L)),currentUser,caseId);
			List<EmployeeDetails> maleList = empRepo.findByGenderIdAndCasedetailsCurrentUserAndCasedetailsIdNotIn(1L,currentUser,caseId);
			List<EmployeeDetails> femaleList = empRepo.findByGenderIdAndCasedetailsCurrentUserAndCasedetailsIdNotIn(2L,currentUser,caseId);
			List<EmployeeDetails> transgenderList = empRepo.findByGenderIdAndCasedetailsCurrentUserAndCasedetailsIdNotIn(4L,currentUser,caseId);
			
			List<CaseDetails> allGenCase = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empList,currentUser,caseId);	
			
			
			model.addAttribute("totalCases", allGenCase.size());
			model.addAttribute("totalEmpCount", empList.size());
			
			List<EmployeeDetails> empListFil = empRepo.findByGenderInAndCasedetailsCurrentUserAndCasedetailsIdNotIn(genderMaster,currentUser,caseId);
			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListFil,currentUser,pageable,caseId);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			Pageable allPage = PageRequest.of(0, Integer.MAX_VALUE, sort);
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(empListFil,currentUser,allPage,caseId);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empListFil.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("empCount", 
					empListFil.size());
						
			
			List<CaseDetails> maleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(maleList,currentUser,caseId);
			model.addAttribute("maleCases", maleCaseList.size());
			model.addAttribute("empMaleCount", maleList.size());
			
			
			List<CaseDetails> femaleCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(femaleList,currentUser,caseId);
			model.addAttribute("femaleCases", femaleCaseList.size());
			model.addAttribute("empFemaleCount", femaleList.size());
			
			List<CaseDetails> transCaseList = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserAndIdNotIn(transgenderList,currentUser,caseId);
			model.addAttribute("transCases", transCaseList.size());
			model.addAttribute("empTransCount", transgenderList.size());
			
			
			
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
		
		return "DeptInquiryViews/ReportsforGenderWise.html";

	}	
	
}

