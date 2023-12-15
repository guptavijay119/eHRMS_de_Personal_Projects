package com.ehrms.deptenq.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.DistrictsRepository;
import com.ehrms.deptenq.repository.GenderRepository;
import com.ehrms.deptenq.repository.IDivisionRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.MisconductTypesMasterRepository;
import com.ehrms.deptenq.repository.RulesApplicableMasterRepository;
import com.ehrms.deptenq.repository.Service_GroupRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class CompositeReportsController {

	private static final Logger LOG = LoggerFactory.getLogger(CompositeReportsController.class);

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;

	@Autowired
	private MisconductTypesMasterRepository misconductTypesMasterRepository;

	@Autowired
	private RulesApplicableMasterRepository ruleApplicableMasterRepository;

	@Autowired
	private GenderRepository genderRepository;

	@Autowired
	private Service_GroupRepository serviceGrpRepo;

	@Autowired
	private IDivisionRepository divisionRepository;

	@Autowired
	private DistrictsRepository districtsRepository;

	@Autowired
	private IGlobalOrgRepository globalorgRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private IEmployeeDetailsRepository employeedetailsrepo;

	/* Added on 13-10-2022 */
	@GetMapping("/commonReportsforAllFields/{page}")
	public String commonReportsforAllFields(Model model, @PathVariable("page") int page,
			 @RequestParam(name = "ruleApplicable", required = false) String rule, 
			
			  @RequestParam(name = "misConductType", required = false) String misconduct,
			  
			  @RequestParam(name = "gender", required = false) String gender,
			  
			  @RequestParam(name = "division", required = false) String division,
			  
			  @RequestParam(name = "district", required = false) String district,
			  
			  @RequestParam(name = "globalOrg", required = false) String globalOrg,
			  
			  @RequestParam(name = "servicegroup", required = false) String servicegroup,
			 
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		
		if(sortField == null) {
			sortField = "caseNo";
		}
		if(sortDirection == null) {
			sortDirection = "asc";
		}

		LOG.info("hi this is the common Reports for All Fields  Methods .......");

		model.addAttribute("misConductTypeList", misconductTypesMasterRepository.findAll());
		model.addAttribute("rulesapplicableList", ruleApplicableMasterRepository.findByActive(true));

		model.addAttribute("genderList", genderRepository.findByActiveOrderById(true));
		model.addAttribute("serviceGroupList", serviceGrpRepo.findByOrderById());
		model.addAttribute("divisionList", divisionRepository.findByActive(true));
		model.addAttribute("districtList",
				districtsRepository.findByState_StateCodeOrderByDistrictName(Long.valueOf(18)));
		model.addAttribute("globalOrgList", globalorgRepo.findByActive(true));

		if(StringUtils.isNotBlank(misconduct) && misconduct.equalsIgnoreCase("null")) {
			misconduct = "";
		}
		model.addAttribute("misConductType", misconduct);
		
		if(StringUtils.isNotBlank(rule) && rule.equalsIgnoreCase("null")) {
			rule = "";
		}
		model.addAttribute("rulesapplicable", rule);

		if(StringUtils.isNotBlank(gender) && gender.equalsIgnoreCase("null")) {
			gender = "";
		}
		model.addAttribute("gender", gender);
		
		if(StringUtils.isNotBlank(servicegroup) && servicegroup.equalsIgnoreCase("null")) {
			servicegroup = "";
		}
		model.addAttribute("serviceGroup", servicegroup);
		
		if(StringUtils.isNotBlank(division) && division.equalsIgnoreCase("null")) {
			division = "";
		}
		model.addAttribute("division", division);
		
		if(StringUtils.isNotBlank(district) && district.equalsIgnoreCase("null")) {
			district = "";
		}
		model.addAttribute("district", district);
		
		if(StringUtils.isNotBlank(globalOrg) && globalOrg.equalsIgnoreCase("null")) {
			globalOrg = "";
		}
		model.addAttribute("globalOrg", globalOrg);

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
		Page<CaseDetails> noticePages = caseDetailsRepo.findAll(pageable);

		Page<CaseDetails> allCases = caseDetailsRepo.findAll(pageable);
		Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableId(Long.valueOf(rule), pageable);
				allCases = caseDetailsRepo.findDistinctIdByRuleApplicableId(Long.valueOf(rule), pageableall);
			}

			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeId(Long.valueOf(misconduct), pageable);
				allCases = caseDetailsRepo.findDistinctIdByMisConductTypeId(Long.valueOf(misconduct), pageableall);
			}

			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByDivisionIdAndDistrictId(Long.valueOf(division),Long.valueOf(district), pageable);
				allCases = caseDetailsRepo.findDistinctIdByDivisionIdAndDistrictId(Long.valueOf(division),Long.valueOf(district), pageableall);
			}
			
			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByDivisionId(Long.valueOf(division), pageable);
				allCases = caseDetailsRepo.findDistinctIdByDivisionId(Long.valueOf(division), pageableall);
			}

			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByDistrictId(Long.valueOf(district), pageable);
				allCases = caseDetailsRepo.findDistinctIdByDistrictId(Long.valueOf(district), pageableall);
			}

			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});

				allCases = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}

			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageable);
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageableall);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});

				allCases = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}

			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgId(Long.valueOf(globalOrg), pageable);
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgId(Long.valueOf(globalOrg), pageableall);
			}
			
			//-----------Rule and misconduct---//////
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndMisConductTypeId(Long.valueOf(rule), Long.valueOf(misconduct),pageable);
				allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndMisConductTypeId(Long.valueOf(rule), Long.valueOf(misconduct),pageableall);
			}

			//----------------------------Rule and division and district
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndDistrictId(Long.valueOf(rule), Long.valueOf(division),Long.valueOf(district), pageable);
				allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndDistrictId(Long.valueOf(rule), Long.valueOf(division),Long.valueOf(district), pageableall);
			}

			//----------------------------Misconduct and division and district
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndDistrictId(Long.valueOf(misconduct), Long.valueOf(division),Long.valueOf(district), pageable);
				allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndDistrictId(Long.valueOf(misconduct), Long.valueOf(division),Long.valueOf(district), pageableall);
			}
			
			//----Globalorg and division and district///

			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district), pageable);
				allCases = caseDetailsRepo.findDistinctIdByDistrictId(Long.valueOf(district), pageableall);
			}
			
			
			///-------------------------Globalorg and misconduct///

			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
//				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndMisConductTypeId(Long.valueOf(globalOrg),Long.valueOf(misconduct), pageable);
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndMisConductTypeId(Long.valueOf(globalOrg),Long.valueOf(misconduct), pageableall);
				
			}
			
			
			//======= Globalorg and rule

			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableId(Long.valueOf(globalOrg),Long.valueOf(rule), pageable);
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableId(Long.valueOf(globalOrg),Long.valueOf(rule), pageableall);
				
			}
			
			//==================globaorg and division and district

			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district), pageable);
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district), pageableall);
			}
			
			
			///========================globalorg and misc and division and district
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(misconduct), pageable);
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(misconduct), pageableall);
			}
			
			
			///========================globalorg and rule and division and district
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule), pageable);
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule), pageableall);
			}
			
			
			///========================globalorg and rule and misc and division and district
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),Long.valueOf(misconduct), pageable);
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),Long.valueOf(misconduct), pageableall);
			}
			
			
			//// ------------- globalorg and rule and misc /////////////
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(rule),Long.valueOf(misconduct), pageable);
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeId(Long.valueOf(globalOrg),Long.valueOf(rule),Long.valueOf(misconduct), pageableall);
			}
			
			
			///----------- globalorg and misc and rule and div and dist and servicegroup
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
				
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}
			
			///----------- globalorg and misc and rule and div and dist and gender
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}
			
			///----------- globalorg and misc  and gender
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}
			
			///----------- globalorg and misc  and servicegroup
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}
			
			///----------- globalorg and rule  and servicegroup
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}
			
			///----------- globalorg and misc and rule and gender
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}
			
			///----------- globalorg and misc and rule and servicegroup
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
			}
			
			///----------- globalorg and misc and rule and gender and servicegroup
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderIdAndServicegroupId(Long.valueOf(gender),Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
			///----------- globalorg and misc and rule and div and dist and gender and servicegroup
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderIdAndServicegroupId(Long.valueOf(gender),Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
			///----------- globalorg and gender
			
			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndEmployeeListIn(Long.valueOf(globalOrg),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndEmployeeListIn(Long.valueOf(globalOrg),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
			///----------- globalorg and servicegroup
			
			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndEmployeeListIn(Long.valueOf(globalOrg),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndEmployeeListIn(Long.valueOf(globalOrg),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
			
			//----------- globalorg and servicegroup and gender
			
			if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndEmployeeListIn(Long.valueOf(globalOrg),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndEmployeeListIn(Long.valueOf(globalOrg),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//----------- globalorg and servicegroup and gender and miscoduct
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
		//----------- globalorg  and gender and rule
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//----------- globalorg  and gender and rule and div and dist
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//----------- globalorg  and servicegroup and rule and div and dist
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//----------- globalorg and servicegroup and gender and rule and div and dist
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
//----------- globalorg  and gender and misc and div and dist
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//----------- globalorg  and servicegroup and misc and div and dist
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//----------- globalorg and servicegroup and gender and misc and div and dist
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndDistrictIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(district),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//----------- globalorg and servicegroup and gender and rule
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg),Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
			//-----------   servicegroup and gender and rule
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndEmployeeListIn(Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndEmployeeListIn(Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//-----------   servicegroup and gender and misc
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndEmployeeListIn(Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndEmployeeListIn(Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
			//-----------   servicegroup and gender and misc and rule
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//-----------   gender and misc
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndEmployeeListIn(Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndEmployeeListIn(Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//-----------   servicegroup and misc
			
			if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndEmployeeListIn(Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndEmployeeListIn(Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
			
			
			//-----------   gender and rule
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

				noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndEmployeeListIn(Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndEmployeeListIn(Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			//-----------   servicegroup and rule
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndEmployeeListIn(Long.valueOf(rule),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndEmployeeListIn(Long.valueOf(rule),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			
			//-----------   servicegroup and rule and misc
			
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
					&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
					&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
				
				List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

				noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
				noticePages.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
				allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
				allCases.getContent().forEach(s -> {
					List<EmployeeDetails> empp = new ArrayList<>();
					empList.forEach(ss -> {
						if (ss.getCasedetails().getId() == s.getId()) {
							empp.add(ss);
						}
					});
					s.setEmployeeList(empp);
				});
				
			}
			
			

		
		
		//-----------   gender and rule and misc
		
				if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
		
				
				//-----------   gender and rule and div and dist
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(division),Long.valueOf(district),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(division),Long.valueOf(district),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//-----------   servicegroup and rule and div and dist
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(division),Long.valueOf(district),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(division),Long.valueOf(district),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//-----------   gender and misc and div and dist
				
				if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(misconduct),Long.valueOf(division),Long.valueOf(district),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(misconduct),Long.valueOf(division),Long.valueOf(district),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//-----------   servicegroup and misc and div and dist
				
				if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(misconduct),Long.valueOf(division),Long.valueOf(district),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(misconduct),Long.valueOf(division),Long.valueOf(district),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//-----------   servicegroup  and div and dist
				
				if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(division),Long.valueOf(district),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(division),Long.valueOf(district),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//-----------   gender  and div and dist
				
				if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(division),Long.valueOf(district),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(division),Long.valueOf(district),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//-----------   gender  and servicegroup and div and dist
				
				if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isNotBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderIdAndServicegroupId(Long.valueOf(gender),Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(division),Long.valueOf(district),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByDivisionIdAndDistrictIdAndEmployeeListIn(Long.valueOf(division),Long.valueOf(district),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//-----------   gender  and servicegroup 
				
				if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderIdAndServicegroupId(Long.valueOf(gender),Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByEmployeeListIn(empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				// ------------ division and rule
				if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionId(Long.valueOf(rule), Long.valueOf(division), pageable);
					allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionId(Long.valueOf(rule), Long.valueOf(division), pageableall);
				}

				//----------------------------Misconduct and division 
				
				if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
					noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionId(Long.valueOf(misconduct), Long.valueOf(division), pageable);
					allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionId(Long.valueOf(misconduct), Long.valueOf(division), pageableall);
				}
				
				//----Globalorg and division///

				if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionId(Long.valueOf(globalOrg), Long.valueOf(division), pageable);
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionId(Long.valueOf(globalOrg), Long.valueOf(division), pageableall);
				}
						
				
				
				///========================globalorg and misc and division 
				if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(misconduct), pageable);
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(misconduct), pageableall);
				}
				
				
				///========================globalorg and rule and division
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule), pageable);
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule), pageableall);
				}
				
				
				///========================globalorg and rule and misc and division
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),Long.valueOf(misconduct), pageable);
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeId(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),Long.valueOf(misconduct), pageableall);
				}
				
				
				//----------- rule and misc and division and globalorg and service 
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
					
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
				}
				
				///----------- globalorg and misc and rule and div  and gender
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
				}
				
				
				///----------- globalorg and misc and rule and div  and gender and servicegroup
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderIdAndServicegroupId(Long.valueOf(gender),Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),Long.valueOf(misconduct),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//============ rule and gender and division and org///////////
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				//----------- globalorg  and servicegroup and rule and div
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				//----------- globalorg and servicegroup and gender and rule and div
				
				if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndRuleApplicableIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(rule),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
	//----------- globalorg  and gender and misc and div 
				
				if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(misconduct),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(misconduct),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				//----------- globalorg  and servicegroup and misc and div
				
				if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(misconduct),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(misconduct),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				//----------- globalorg and servicegroup and gender and misc and div
				
				if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(misconduct),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndMisConductTypeIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),Long.valueOf(misconduct),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				//----------- globalorg and servicegroup and gender  and div
				
				if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
						&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
						&& StringUtils.isNotBlank(servicegroup) && StringUtils.isNotBlank(globalOrg)) {
					
					List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupIdAndGenderId(Long.valueOf(servicegroup),Long.valueOf(gender));

					noticePages = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),empList, pageable);
					noticePages.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
					allCases = caseDetailsRepo.findDistinctIdByGlobalorgIdAndDivisionIdAndEmployeeListIn(Long.valueOf(globalOrg), Long.valueOf(division),empList, pageableall);
					allCases.getContent().forEach(s -> {
						List<EmployeeDetails> empp = new ArrayList<>();
						empList.forEach(ss -> {
							if (ss.getCasedetails().getId() == s.getId()) {
								empp.add(ss);
							}
						});
						s.setEmployeeList(empp);
					});
					
				}
				
				
				//-----------   gender and rule and div 
					
					if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
							&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
							&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
						
						List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

						noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(division),empList, pageable);
						noticePages.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
						allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(division),empList, pageableall);
						allCases.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
					}
					
					
					//-----------   servicegroup and rule and div 
					
					if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
							&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
							&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
						
						List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

						noticePages = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(division),empList, pageable);
						noticePages.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
						allCases = caseDetailsRepo.findDistinctIdByRuleApplicableIdAndDivisionIdAndEmployeeListIn(Long.valueOf(rule),Long.valueOf(division),empList, pageableall);
						allCases.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
					}
					
					
					//-----------   gender and misc and div
					
					if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isNotBlank(gender)
							&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
							&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
						
						List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

						noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndEmployeeListIn(Long.valueOf(misconduct),Long.valueOf(division),empList, pageable);
						noticePages.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
						allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndEmployeeListIn(Long.valueOf(misconduct),Long.valueOf(division),empList, pageableall);
						allCases.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
					}
					
					
					//-----------   servicegroup and misc and div
					
					if (StringUtils.isBlank(rule) && StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(gender)
							&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
							&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
						
						List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

						noticePages = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndEmployeeListIn(Long.valueOf(misconduct),Long.valueOf(division),empList, pageable);
						noticePages.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
						allCases = caseDetailsRepo.findDistinctIdByMisConductTypeIdAndDivisionIdAndEmployeeListIn(Long.valueOf(misconduct),Long.valueOf(division),empList, pageableall);
						allCases.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
					}
					
					
					//-----------   servicegroup  and div
					
					if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isBlank(gender)
							&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
							&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
						
						List<EmployeeDetails> empList = employeedetailsrepo.findByServicegroupId(Long.valueOf(servicegroup));

						noticePages = caseDetailsRepo.findDistinctIdByDivisionIdAndEmployeeListIn(Long.valueOf(division),empList, pageable);
						noticePages.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
						allCases = caseDetailsRepo.findDistinctIdByDivisionIdAndEmployeeListIn(Long.valueOf(division),empList, pageableall);
						allCases.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
					}
					
					
					//-----------   gender  and div
					
					if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
							&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
							&& StringUtils.isBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
						
						List<EmployeeDetails> empList = employeedetailsrepo.findByGenderId(Long.valueOf(gender));

						noticePages = caseDetailsRepo.findDistinctIdByDivisionIdAndEmployeeListIn(Long.valueOf(division),empList, pageable);
						noticePages.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
						allCases = caseDetailsRepo.findDistinctIdByDivisionIdAndEmployeeListIn(Long.valueOf(division),empList, pageableall);
						allCases.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
					}
					
					
					//-----------   gender  and servicegroup and div
					
					if (StringUtils.isBlank(rule) && StringUtils.isBlank(misconduct) && StringUtils.isNotBlank(gender)
							&& StringUtils.isNotBlank(division) && StringUtils.isBlank(district)
							&& StringUtils.isNotBlank(servicegroup) && StringUtils.isBlank(globalOrg)) {
						
						List<EmployeeDetails> empList = employeedetailsrepo.findByGenderIdAndServicegroupId(Long.valueOf(gender),Long.valueOf(servicegroup));

						noticePages = caseDetailsRepo.findDistinctIdByDivisionIdAndEmployeeListIn(Long.valueOf(division),empList, pageable);
						noticePages.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
						allCases = caseDetailsRepo.findDistinctIdByDivisionIdAndEmployeeListIn(Long.valueOf(division),empList, pageableall);
						allCases.getContent().forEach(s -> {
							List<EmployeeDetails> empp = new ArrayList<>();
							empList.forEach(ss -> {
								if (ss.getCasedetails().getId() == s.getId()) {
									empp.add(ss);
								}
							});
							s.setEmployeeList(empp);
						});
						
					}


	}
		
		
		

		//// -------------------------------///-----------------

		model.addAttribute("caseDetailsAllRecordsreport", allCases);
		model.addAttribute("caseDetailsAllRecords", noticePages.getContent());
		model.addAttribute("currentPage", page);
		int begin = Math.max(1, page - 5);
		if (page >= 5) {
			begin = page;
		}
		int end = Math.min(begin + 4, noticePages.getTotalPages());
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("totalPages", noticePages.getTotalPages());
		model.addAttribute("totalItems", allCases.getTotalElements());
		Map<String, CaseDetails> mapEn = new TreeMap<>();

		model.addAttribute("noticelist", mapEn.values().stream().collect(Collectors.toList()));

		return "DeptInquiryViews/CommonReportPageForDE.html";

	}

}
