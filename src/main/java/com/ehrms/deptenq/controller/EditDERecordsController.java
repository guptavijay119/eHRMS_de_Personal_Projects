package com.ehrms.deptenq.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
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
import com.ehrms.deptenq.models.ChargesheetDetails;
import com.ehrms.deptenq.models.CourtCaseDetails;
import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.DetailsKeptAbeyanceCases;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.InquiryOfficerList;
import com.ehrms.deptenq.models.PresentingOfficerDetails;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.ReInstatedDetails;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.CasePendingWithRepository;
import com.ehrms.deptenq.repository.DecisionTakenRepository;
import com.ehrms.deptenq.repository.DesignationRepository;
import com.ehrms.deptenq.repository.DetailsKeptAbeyanceCasesRepository;
import com.ehrms.deptenq.repository.DistrictsRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.GenderRepository;
import com.ehrms.deptenq.repository.GradePayRepository;
import com.ehrms.deptenq.repository.IChargesheetDetailsRepository;
import com.ehrms.deptenq.repository.ICourtCaseDetailsRepository;
import com.ehrms.deptenq.repository.ICourtNameRepository;
import com.ehrms.deptenq.repository.IDivisionRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.IPresentingOfficerRepository;
import com.ehrms.deptenq.repository.IProsecutionProposalDetailsRepository;
import com.ehrms.deptenq.repository.IReInstatedRepository;
import com.ehrms.deptenq.repository.InquiryOfficerListRepository;
import com.ehrms.deptenq.repository.InquiryOfficerRepository;
import com.ehrms.deptenq.repository.MisconductTypesMasterRepository;
import com.ehrms.deptenq.repository.PayBandRepository;
import com.ehrms.deptenq.repository.PayCommissionRepository;
import com.ehrms.deptenq.repository.ReasonForKeepingCaseAbeyanceRepository;
import com.ehrms.deptenq.repository.RulesApplicableMasterRepository;
import com.ehrms.deptenq.repository.Service_GroupRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.SuspensionDetailsRepository;
import com.ehrms.deptenq.repository.SuspensionRuleRepository;
import com.ehrms.deptenq.repository.TypeOfPenaltyRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.UserService;


@Controller
@RequestMapping("departmentalEnquiry")
public class EditDERecordsController {
	
	@Autowired
	private IEmployeeDetailsRepository employeedetailsrepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DistrictsRepository discRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private CaseDetailsRepository caseDetailsRepo;
	
	@Autowired
	private IChargesheetDetailsRepository chargesheetRepo;
	
	@Autowired
	private MisconductTypesMasterRepository misconductTypesMasterRepo;
	
	@Autowired
	private GenderRepository genderRepo;

	@Autowired
	private Service_GroupRepository serviceGrpRepo;

	@Autowired
	private DesignationRepository designationRepo;

	@Autowired
	private PayCommissionRepository PayCommissionRepo;

	@Autowired
	private PayBandRepository paybandRepo;

	@Autowired
	private GradePayRepository gradepayRepo;

	@Autowired
	private IGlobalOrgRepository globalorgRepo;

	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@Autowired
	private SuspensionDetailsRepository suspensionDetailsRepo;
	
	@Autowired
	private SuspensionRuleRepository suspensionRuleRepo;
	
	
	@Autowired
	private IReInstatedRepository reinstatedRepository;
	
	@Autowired
	private InquiryOfficerRepository inquiryofficerRepository;
	
	@Autowired
	private InquiryOfficerListRepository inquiryOfficerListRepo;
	
	@Autowired
	private IPresentingOfficerRepository   presentingOfficerRepo;
	
	@Autowired
	private IDivisionRepository divisionRepository;
	
	@Autowired
	private IProsecutionProposalDetailsRepository   prosecutionRepo;
	
	
	@Autowired
	private ICourtCaseDetailsRepository  courtcaseRepo;
	
	@Autowired
	private ICourtNameRepository courtNameRepo;
	
	@Autowired
	private DetailsKeptAbeyanceCasesRepository abeyanceCaseRepo;
	
	@Autowired
	private ReasonForKeepingCaseAbeyanceRepository reasonforkeepingcaseabeyanceRepository;
	
	@Autowired
	private FinalOutcomeRepository finaloutcomerepository;
	
	@Autowired
	private DecisionTakenRepository decisiontakenrepository;
	
	
	@Autowired
	private CasePendingWithRepository casependingwithRepository;

	@Autowired
	private TypeOfPenaltyRepository typeofpenaltyrepository;
	
	@Autowired
	private IChargesheetDetailsRepository   chargesheetdetailsRepository;
	
	@Autowired
	private RulesApplicableMasterRepository  rulesApplicableMasterRepo;
	
	@Autowired
	private FinalOutcomeRepository finalOutRepo;
	
	@Autowired
	private IGlobalOrgRepository orgRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(EditDERecordsController.class);
	
	private static final String SPLITTING =  "splitting";
	
	private static final String SEARCH = "search";
	
	
	
	/* fetching records of employee details methods */

	@GetMapping("/editEmployeeDetails/{id}/{page}")
	public String editEmployeeDetails(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the fetching of Employee details Records! ");

	
		EmployeeDetails empdtls = employeedetailsrepo.findById(id).orElse(new EmployeeDetails());
//		suspensiondetails.setCaseNo(suspensiondetails.getCasedetails().getCaseNo());

		empdtls.setCaseNo(empdtls.getCasedetails().getCaseNo());
		model.addAttribute("cases", empdtls.getCasedetails());
		model.addAttribute("employeeDetails", empdtls);
		
		if(empdtls.getCasedetails().getIoList().isEmpty()) {
			model.addAttribute("disablepresentingofficer", true);
		} 
		if(empdtls.getCasedetails().getMisConductType() != null && (empdtls.getCasedetails().getMisConductType().getId()==1 || empdtls.getCasedetails().getMisConductType().getId()==2)) {
			model.addAttribute("disableprosecution", true);
		} 

		model.addAttribute("gender", genderRepo.findByActiveOrderById(true));
		model.addAttribute("serviceGroup", serviceGrpRepo.findAll());
		model.addAttribute("designation", designationRepo.findAll());
		model.addAttribute("paycommission", PayCommissionRepo.findAll());
		model.addAttribute("payband", paybandRepo.findAll());
		model.addAttribute("gradepay", gradepayRepo.findAll());
		model.addAttribute("globalOrgList1", globalorgRepo.findAll());
		model.addAttribute("rulesapplicable",rulesApplicableMasterRepo.findByActive(true));
	//	model.addAttribute("subDepartmentlist", subDepartmentRepo.findAll());

		        // added division as per latest pdf
	     	     model.addAttribute("divisionRepolist", divisionRepository.findAll());
		

		// request.getSession().setAttribute("employeeId", empdtls);
		try {

			if (empdtls.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				CaseDetails cd = empdtls.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<EmployeeDetails> noticePages = employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId(),pageable);

				model.addAttribute("employeeList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("employeeList", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				model.addAttribute("employeeList", employeedetailsrepo.findAll());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("employeeList", employeedetailsrepo.findAll());
		}

		return "DeptInquiryViews/delinquentEmployeesDetails";
	}

	

	
	/* fetching & Updating records of Suspension Details methods */

	@GetMapping("/editsusdtlsRecords/{id}/{page}")
	public String editsusdtlsRecords(@PathVariable("id") long id, Model model, @PathVariable("page") int page) {
		LOG.info("hi this is the  fetching and updating suspension  Records! ");

		// calling case number on Suspension Details
		// EmployeeDetails empdtls = employeedetailsrepo.findById(id).orElse(new
		// EmployeeDetails());

		// calling case number on Suspension Details
		SuspensionDetails suspensiondetails = suspensionDetailsRepo.findById(id).orElse(new SuspensionDetails());
		if(suspensiondetails.getCasedetails() != null) {
			model.addAttribute("cases", suspensiondetails.getCasedetails());
			if(suspensiondetails.getCasedetails().getIoList().isEmpty()) {
				model.addAttribute("disablepresentingofficer", true);
			} 
			if(suspensiondetails.getCasedetails().getMisConductType() != null && (suspensiondetails.getCasedetails().getMisConductType().getId()==1 || suspensiondetails.getCasedetails().getMisConductType().getId()==2)) {
				model.addAttribute("disableprosecution", true);
			}
			suspensiondetails.setCaseNo(suspensiondetails.getCasedetails().getCaseNo());
			CaseDetails caseDetails = suspensiondetails.getCasedetails();
			if(!caseDetails.getEmployeeList().isEmpty()){
				for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
					if(emp.getFirstName().equalsIgnoreCase(suspensiondetails.getFirstName()) 
							&& emp.getMiddleName().equalsIgnoreCase(suspensiondetails.getMiddleName())
							&& emp.getLastName().equalsIgnoreCase(suspensiondetails.getLastName())) {
						suspensiondetails.setEmployeeDataId(emp.getId().toString());
					}
				}
				model.addAttribute("employeeList", caseDetails.getEmployeeList());
			}	
		}
		model.addAttribute("suspensionRules", suspensionRuleRepo.findAll(Sort.by("order")));
		model.addAttribute("suspensionObj", suspensiondetails);

		// request.getSession().setAttribute("employeeId", empdtls);
		try {

			if (suspensiondetails.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = suspensiondetails.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC, "id"));

				Page<SuspensionDetails> noticePages = suspensionDetailsRepo
						.findByCasedetailsId(suspensiondetails.getCasedetails().getId(), pageable);

				model.addAttribute("suspensionEmployeeList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());

//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}

				Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC, "id"));

				Page<SuspensionDetails> noticePages = suspensionDetailsRepo.findAll(pageable);

				model.addAttribute("suspensionEmployeeList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
			}
		} catch (Exception a) {
//			a.printStackTrace();
			int index = 1;

			if (page != 0) {
				index = (page * 10) - (10 - 1);
				model.addAttribute("pageIndex", index);

			}

			if (page < 0) {

				page = 1;
			}

			Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC, "id"));

			Page<SuspensionDetails> noticePages = suspensionDetailsRepo.findAll(pageable);

			model.addAttribute("suspensionEmployeeList", noticePages);
			model.addAttribute("currentPage", page);
			int begin = Math.max(1, page - 5);
			int end = Math.min(begin + 4, noticePages.getTotalPages());
			model.addAttribute("begin", begin);
			model.addAttribute("end", end);
			model.addAttribute("totalPages", noticePages.getTotalPages());
			model.addAttribute("totalItems", noticePages.getTotalElements());
			model.addAttribute("noticelist", noticePages.getContent());
		}

		return "DeptInquiryViews/suspensionDetails";
	}
	
	
	
	
	
	
	
	
	/* fetching & Updating records of  ReInstated  Details methods */

	@GetMapping("/editReinstateddtlsRecords/{id}/{page}")
	public String editReinstateddtlsRecords(@PathVariable("id") long id, Model model, @PathVariable("page") int page) {
		LOG.info("hi this is the  fetching and updating Reinstated Details  Records! ");

		
		
		// calling case number on Reinstated  Details
	     	ReInstatedDetails   reinstateddetails=reinstatedRepository.findById(id).orElse(new ReInstatedDetails());
	     //	reinstateddetails.setCasedetails(reinstatedRepository.findByCaseNo(reinstateddetails.getCaseNo()));
	     	if(reinstateddetails.getCasedetails() != null) {
	     		model.addAttribute("cases", reinstateddetails.getCasedetails());
	     		if(reinstateddetails.getCasedetails().getIoList().isEmpty()) {
					model.addAttribute("disablepresentingofficer", true);
				} 
				if(reinstateddetails.getCasedetails().getMisConductType() != null && (reinstateddetails.getCasedetails().getMisConductType().getId()==1 || reinstateddetails.getCasedetails().getMisConductType().getId()==2)) {
					model.addAttribute("disableprosecution", true);
				}
	     		reinstateddetails.setCaseNo(reinstateddetails.getCasedetails().getCaseNo());
	     		CaseDetails caseDetails = reinstateddetails.getCasedetails();
	     		if(!caseDetails.getEmployeeList().isEmpty()){
					for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
						if(emp.getFirstName().equalsIgnoreCase(reinstateddetails.getFirstName()) 
								&& emp.getMiddleName().equalsIgnoreCase(reinstateddetails.getMiddleName())
								&& emp.getLastName().equalsIgnoreCase(reinstateddetails.getLastName())) {
							reinstateddetails.setEmployeeDataId(emp.getId().toString());
						}
					}
					model.addAttribute("employeeList", caseDetails.getEmployeeList());
				}
	     	}
	     	model.addAttribute("reinstatedObjDetails", reinstateddetails);

		// request.getSession().setAttribute("employeeId", empdtls);
		try {

			if (reinstateddetails.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = reinstateddetails.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<ReInstatedDetails> noticePages = reinstatedRepository.findByCasedetailsId(reinstateddetails.getCasedetails().getId(), pageable);

				
				model.addAttribute("reinstatedGridList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());

//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<ReInstatedDetails> noticePages = reinstatedRepository.findAll(pageable);

				
				model.addAttribute("reinstatedGridList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("reinstatedGridList", reinstatedRepository.findAll());
		}

		return "DeptInquiryViews/reinstatedDetails";
	}
	
	
	
	
	/* Fetching Records of Inquiry Officer Details Methods */

	@GetMapping("/editInquiryOfficerDetails/{id}/{page}")
	public String editInquiryOfficerDetails(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the fetching of inquiry officer details Records! ");

	
//		EmployeeDetails empdtls = employeedetailsrepo.findById(id).orElse(new EmployeeDetails());
		
		    InquiryOfficerDetails  inquiryofficer=inquiryofficerRepository.findById(id).orElse(new InquiryOfficerDetails());
		    inquiryofficer.setCaseNo(inquiryofficer.getCasedetails().getCaseNo());
		    model.addAttribute("cases", inquiryofficer.getCasedetails());
		    if(inquiryofficer.getCasedetails().getIoList().isEmpty()) {
				model.addAttribute("disablepresentingofficer", true);
			} 
			if(inquiryofficer.getCasedetails().getMisConductType() != null && (inquiryofficer.getCasedetails().getMisConductType().getId()==1 || inquiryofficer.getCasedetails().getMisConductType().getId()==2)) {
				model.addAttribute("disableprosecution", true);
			}
		    
		    model.addAttribute("inquiryofficerdetailsObj", inquiryofficer);
		    model.addAttribute("InquiryOfficerlist", inquiryOfficerListRepo.findByOrderById());
			Set<String> inquiryOfficerTypeList = new LinkedHashSet<>();
			List<InquiryOfficerList> listinq = inquiryOfficerListRepo.findAll(Sort.by("order"));
//			listinq.sort((a1,a2)->Integer.valueOf(a1.getOrder()).compareTo(Integer.valueOf(a2.getOrder())));
//			inquiryOfficerTypeList = listinq.stream().map(s->s.getDesignation()).collect(Collectors.toSet());
			for(InquiryOfficerList ss : listinq) {
				inquiryOfficerTypeList.add(ss.getDesignation());
			}
			model.addAttribute("inquiryOfficerTypeList", inquiryOfficerTypeList);
			if(inquiryofficer.getListOfInquiryOfficer() != null) {
				model.addAttribute("inqListid", inquiryofficer.getListOfInquiryOfficer().getId());
			}
			model.addAttribute("otherinqtype", inquiryofficer.getOtherinquiryOfficer());
	
		try {

			if (inquiryofficer.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = inquiryofficer.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<InquiryOfficerDetails> noticePages = inquiryofficerRepository.findByCasedetailsId(inquiryofficer.getCasedetails().getId(),pageable);

				model.addAttribute("InquiryOfficerGridList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				model.addAttribute("InquiryOfficerGridList", inquiryofficerRepository.findAll());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("InquiryOfficerGridList", inquiryofficerRepository.findAll());
		}

		return "DeptInquiryViews/inquiryOfficerDetails";
	}

	
	
	
	/* Fetching Records of Presenting  Officer Details Methods */

	@GetMapping("/editPresentingOfficerDetails/{id}/{page}")
	public String editPresentingOfficerDetails(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the fetching of Presenting  officer details Records! ");

	
//		EmployeeDetails empdtls = employeedetailsrepo.findById(id).orElse(new EmployeeDetails());
		
//		    InquiryOfficerDetails  inquiryofficer=inquiryofficerRepository.findById(id).orElse(new InquiryOfficerDetails());
		
		PresentingOfficerDetails  presentingofficerdetails=presentingOfficerRepo.findById(id).orElse(new PresentingOfficerDetails());
		presentingofficerdetails.setCaseNo(presentingofficerdetails.getCasedetails().getCaseNo());
		model.addAttribute("cases", presentingofficerdetails.getCasedetails());
		 if(presentingofficerdetails.getCasedetails().getIoList().isEmpty()) {
				model.addAttribute("disablepresentingofficer", true);
			} 
			if(presentingofficerdetails.getCasedetails().getMisConductType() != null && (presentingofficerdetails.getCasedetails().getMisConductType().getId()==1 || presentingofficerdetails.getCasedetails().getMisConductType().getId()==2)) {
				model.addAttribute("disableprosecution", true);
			}
		    model.addAttribute("presentingofficerdetailsObj", presentingofficerdetails);
		    
		    model.addAttribute("globalOrgList1", globalorgRepo.findByOrderById());
		    model.addAttribute("subDepartmentlist", subDepartmentRepo.findByOrderById());
		    List<Long>  longs = new  ArrayList<>(Arrays.asList(4L));
			List<Designation> designationList = designationRepo.findByServiceGroupId_IdNotIn(longs);
//			Designation designation = designationRepo.findById(744L).orElse(new Designation());
//			designationList.add(designation);
			model.addAttribute("designation", designationList);
			   model.addAttribute("divisionRepolist", divisionRepository.findByActive(true,Sort.by("order")));
			   
			   
			   
//				model.addAttribute("designation", designationRepo.findAll());
//		    model.addAttribute("InquiryOfficerlist", inquiryOfficerListRepo.findByOrderById());
		    
	
		try {

			if (presentingofficerdetails.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = presentingofficerdetails.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<PresentingOfficerDetails> noticePages = presentingOfficerRepo.findByCasedetailsId(presentingofficerdetails.getCasedetails().getId(),pageable);

				model.addAttribute("PresentingOfficerGridList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				model.addAttribute("PresentingOfficerGridList", presentingOfficerRepo.findAll());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("PresentingOfficerGridList", presentingOfficerRepo.findAll());
		}

		return "DeptInquiryViews/presentingOfficerDetails";
	}

	
	
	
	
	
	
	/* Fetching Records of Prosecution Proposal Details Methods :Date(13/09/2022) */

	@GetMapping("/editProsecutionProposalDtls/{id}/{page}")
	public String editProsecutionProposalDtls(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the fetching of Prosecution Proposal details Records! ");

		ProsecutionProposalDetails  prosecutionproposaldetails=prosecutionRepo.findById(id).orElse(new ProsecutionProposalDetails());
		if(prosecutionproposaldetails.getCasedetails() != null) {
			model.addAttribute("cases", prosecutionproposaldetails.getCasedetails());
			prosecutionproposaldetails.setCaseNo(prosecutionproposaldetails.getCasedetails().getCaseNo());
			CaseDetails caseDetails = prosecutionproposaldetails.getCasedetails();
			 if(prosecutionproposaldetails.getCasedetails().getIoList().isEmpty()) {
					model.addAttribute("disablepresentingofficer", true);
				} 
				if(prosecutionproposaldetails.getCasedetails().getMisConductType() != null && (prosecutionproposaldetails.getCasedetails().getMisConductType().getId()==1 || prosecutionproposaldetails.getCasedetails().getMisConductType().getId()==2)) {
					model.addAttribute("disableprosecution", true);
				}
			if(!caseDetails.getEmployeeList().isEmpty()){
				for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
					if(emp.getFirstName().equalsIgnoreCase(prosecutionproposaldetails.getFirstName()) 
							&& emp.getMiddleName().equalsIgnoreCase(prosecutionproposaldetails.getMiddleName())
							&& emp.getLastName().equalsIgnoreCase(prosecutionproposaldetails.getLastName())) {
						prosecutionproposaldetails.setEmployeeDataId(emp.getId().toString());
					}
				}
				model.addAttribute("employeeList", caseDetails.getEmployeeList());
			}	
		}
		model.addAttribute("ProsecutionProposalObj", prosecutionproposaldetails);
		    
		    
		/*
		 * model.addAttribute("globalOrgList1", globalorgRepo.findByOrderById());
		 * model.addAttribute("subDepartmentlist", subDepartmentRepo.findByOrderById());
		 * model.addAttribute("designation", designationRepo.findAll());
		 * model.addAttribute("divisionRepolist", divisionRepository.findByOrderById());
		 */
			   
			   
			   
//				model.addAttribute("designation", designationRepo.findAll());
//		    model.addAttribute("InquiryOfficerlist", inquiryOfficerListRepo.findByOrderById());
		    
	
		try {

			if (prosecutionproposaldetails.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = prosecutionproposaldetails.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<ProsecutionProposalDetails> noticePages = prosecutionRepo.findByCasedetailsId(prosecutionproposaldetails.getCasedetails().getId(),pageable);

				model.addAttribute("ProsecutionDetailsGridList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				model.addAttribute("ProsecutionDetailsGridList", prosecutionRepo.findAll());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("ProsecutionDetailsGridList", prosecutionRepo.findAll());
		}

		return "DeptInquiryViews/prosecutionProposalDetails";
	}

	
	
	
	
	
	/* Fetching Records of court case Details Methods :Date(14/09/2022) */

	@GetMapping("/editCourtCaseDtlsRecords/{id}/{page}")
	public String editCourtCaseDtlsRecords(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the fetching of Court Case details Records! ");

//		ProsecutionProposalDetails  prosecutionproposaldetails=prosecutionRepo.findById(id).orElse(new ProsecutionProposalDetails());
		 
		CourtCaseDetails  courtcasedetails=courtcaseRepo.findById(id).orElse(new CourtCaseDetails());
		model.addAttribute("cases", courtcasedetails.getCasedetails());
		 if(courtcasedetails.getCasedetails().getIoList().isEmpty()) {
				model.addAttribute("disablepresentingofficer", true);
			} 
			if(courtcasedetails.getCasedetails().getMisConductType() != null && (courtcasedetails.getCasedetails().getMisConductType().getId()==1 || courtcasedetails.getCasedetails().getMisConductType().getId()==2)) {
				model.addAttribute("disableprosecution", true);
			}
		courtcasedetails.setCaseNo(courtcasedetails.getCasedetails().getCaseNo());
		model.addAttribute("CourtCaseObj", courtcasedetails);
		    
		model.addAttribute("CourtNameList", courtNameRepo.findByOrderById());
		    
		/*
		 * model.addAttribute("globalOrgList1", globalorgRepo.findByOrderById());
		 * model.addAttribute("subDepartmentlist", subDepartmentRepo.findByOrderById());
		 * model.addAttribute("designation", designationRepo.findAll());
		 * model.addAttribute("divisionRepolist", divisionRepository.findByOrderById());
		 */
			   
			   
			   
//				model.addAttribute("designation", designationRepo.findAll());
//		    model.addAttribute("InquiryOfficerlist", inquiryOfficerListRepo.findByOrderById());
		    
	
		try {

			if (courtcasedetails.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = courtcasedetails.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<CourtCaseDetails> noticePages = courtcaseRepo.findByCasedetailsId(courtcasedetails.getCasedetails().getId(),pageable);

				
				model.addAttribute("CourtCaseDetailsGridList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				model.addAttribute("CourtCaseDetailsGridList", courtcaseRepo.findAll());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("CourtCaseDetailsGridList", courtcaseRepo.findAll());
		}

		return "DeptInquiryViews/courtCaseDetails";
	}

	
	
	
	/* Fetching Records of Details Kept Abeyance Cases Details Methods :Date(14/09/2022) */

	@GetMapping("/editdetailsKeptAbeyanceCaseDtlsRecords/{id}/{page}")
	public String editdetailsKeptAbeyanceCaseDtlsRecords(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the fetching of abeyance cases details Records! ");

//		ProsecutionProposalDetails  prosecutionproposaldetails=prosecutionRepo.findById(id).orElse(new ProsecutionProposalDetails());
		 
//		CourtCaseDetails  courtcasedetails=courtcaseRepo.findById(id).orElse(new CourtCaseDetails());
		DetailsKeptAbeyanceCases   detailskeptabeyancecases=abeyanceCaseRepo.findById(id).orElse(new DetailsKeptAbeyanceCases());
		model.addAttribute("cases", detailskeptabeyancecases.getCasedetails());
		detailskeptabeyancecases.setCaseNo(detailskeptabeyancecases.getCasedetails().getCaseNo());
		if(detailskeptabeyancecases.getCasedetails().getIoList().isEmpty()) {
			model.addAttribute("disablepresentingofficer", true);
		} 
		if(detailskeptabeyancecases.getCasedetails().getMisConductType() != null && (detailskeptabeyancecases.getCasedetails().getMisConductType().getId()==1 || detailskeptabeyancecases.getCasedetails().getMisConductType().getId()==2)) {
			model.addAttribute("disableprosecution", true);
		}
		model.addAttribute("addDetailsKeptAbeyanceCasesObj", detailskeptabeyancecases);
		model.addAttribute("reasonkeptList", reasonforkeepingcaseabeyanceRepository.findAll());
		//model.addAttribute("CourtNameList", courtNameRepo.findByOrderById());
		    
		/*
		 * model.addAttribute("globalOrgList1", globalorgRepo.findByOrderById());
		 * model.addAttribute("subDepartmentlist", subDepartmentRepo.findByOrderById());
		 * model.addAttribute("designation", designationRepo.findAll());
		 * model.addAttribute("divisionRepolist", divisionRepository.findByOrderById());
		 */
			   
			   
			   
//				model.addAttribute("designation", designationRepo.findAll());
//		    model.addAttribute("InquiryOfficerlist", inquiryOfficerListRepo.findByOrderById());
		    
	
		try {

			if (detailskeptabeyancecases.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = detailskeptabeyancecases.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<DetailsKeptAbeyanceCases> noticePages = abeyanceCaseRepo.findByCasedetailsId(detailskeptabeyancecases.getCasedetails().getId(),pageable);

				
				model.addAttribute("DetailsKeptAbeyanceCasesGridList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				model.addAttribute("DetailsKeptAbeyanceCasesGridList", abeyanceCaseRepo.findAll());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("DetailsKeptAbeyanceCasesGridList", abeyanceCaseRepo.findAll());
		}

		return "DeptInquiryViews/DetailsKeptAbeyanceCases";
	}

	
	
	
	
	/* Fetching Records of Final Outcome Details Methods :Date(14/09/2022) */

	@GetMapping("/editfinalOutcomeDtlsRecords/{id}/{page}")
	public String editfinalOutcomeDtlsRecords(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the fetching of final Out come Dtls Records Records! ");

//		ProsecutionProposalDetails  prosecutionproposaldetails=prosecutionRepo.findById(id).orElse(new ProsecutionProposalDetails());
		 
//		CourtCaseDetails  courtcasedetails=courtcaseRepo.findById(id).orElse(new CourtCaseDetails());
		boolean special = false;
		FinalOutcomeDetails   finaloutcomedetails=finaloutcomerepository.findById(id).orElse(new FinalOutcomeDetails());
		model.addAttribute("cases", finaloutcomedetails.getCasedetails());
		if(finaloutcomedetails.getCasedetails().getIoList().isEmpty()) {
			model.addAttribute("disablepresentingofficer", true);
		} 
		if(finaloutcomedetails.getCasedetails().getMisConductType() != null && (finaloutcomedetails.getCasedetails().getMisConductType().getId()==1 || finaloutcomedetails.getCasedetails().getMisConductType().getId()==2)) {
			model.addAttribute("disableprosecution", true);
		}
		if(finaloutcomedetails.getDecisionTaken() != null 
				&& (finaloutcomedetails.getDecisionTaken().getId() == 3 || finaloutcomedetails.getDecisionTaken().getId()==4
				|| finaloutcomedetails.getDecisionTaken().getId()==5)) {
			special = true;
		} else {
			special = false;
		}
		if(finaloutcomedetails.getCasedetails() != null) {
			finaloutcomedetails.setCaseNo(finaloutcomedetails.getCasedetails().getCaseNo());
			
			CaseDetails caseDetails = finaloutcomedetails.getCasedetails();
			if(!caseDetails.getEmployeeList().isEmpty()){
				for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
					if(emp.getFirstName().equalsIgnoreCase(finaloutcomedetails.getFirstName()) 
							&& emp.getMiddleName().equalsIgnoreCase(finaloutcomedetails.getMiddleName())
							&& emp.getLastName().equalsIgnoreCase(finaloutcomedetails.getLastName())) {
						finaloutcomedetails.setEmployeeDataId(emp.getId().toString());
					}
				}
				model.addAttribute("employeeList", caseDetails.getEmployeeList());
			}	
			
		}
		model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails);
		
		model.addAttribute("DecisionTakenList", decisiontakenrepository.findByActive(true,Sort.by("order")));
		model.addAttribute("casependingwithlist", casependingwithRepository.findByActive(true));
		model.addAttribute("TypeofPenaltylist", typeofpenaltyrepository.findByActive(true));
		model.addAttribute("reasonkeptList", reasonforkeepingcaseabeyanceRepository.findByActive(true));
			   
			   
			   

	
		try {

			if (finaloutcomedetails.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = finaloutcomedetails.getCasedetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<FinalOutcomeDetails> noticePages = finaloutcomerepository.findByCasedetailsId(finaloutcomedetails.getCasedetails().getId(),pageable);

				
				model.addAttribute("FinalOutComeList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				model.addAttribute("FinalOutComeList", finaloutcomerepository.findAll());
			}
		} catch (Exception a) {
			a.printStackTrace();
			Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
			Page<FinalOutcomeDetails> noticePages = finaloutcomerepository.findByFileNoIsNotNullAndFileNoIsNot("",pageable);
			model.addAttribute("FinalOutComeList", noticePages);
		}
		if(special) {
			return "DeptInquiryViews/finaloutcomespecial";
		} else {
			return "DeptInquiryViews/FinalOutcomeDetails";
		}
	}

	
	
	
	
	/**  Fetching Records of Chargesheet Details Records Methods :Date(15/09/2022) */

	@GetMapping("/editchargesheetDtlsRecords/{id}/{page}")
	public String editchargesheetDtlsRecords(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
//		System.out.println();
		LOG.info("hi this is the fetching of Chargesheet Details Dtls Records Records! ");

		 
		ChargesheetDetails  chargesheetdetails=chargesheetdetailsRepository.findById(id).orElse(new ChargesheetDetails());
		if(chargesheetdetails.getCaseDetails() != null) {
			model.addAttribute("cases", chargesheetdetails.getCaseDetails());
			chargesheetdetails.setCaseNo(chargesheetdetails.getCaseDetails().getCaseNo());
			if(chargesheetdetails.getCaseDetails().getIoList().isEmpty()) {
				model.addAttribute("disablepresentingofficer", true);
			} 
			if(chargesheetdetails.getCaseDetails().getMisConductType() != null && (chargesheetdetails.getCaseDetails().getMisConductType().getId()==1 || chargesheetdetails.getCaseDetails().getMisConductType().getId()==2)) {
				model.addAttribute("disableprosecution", true);
			} 
			
			if(!chargesheetdetails.getCaseDetails().getEmployeeList().isEmpty()){
				for(EmployeeDetails emp : chargesheetdetails.getCaseDetails().getEmployeeList()) {
					if(emp.getFirstName().equalsIgnoreCase(chargesheetdetails.getFirstName()) 
							&& emp.getMiddleName().equalsIgnoreCase(chargesheetdetails.getMiddleName())
							&& emp.getLastName().equalsIgnoreCase(chargesheetdetails.getLastName())) {
						chargesheetdetails.setEmployeeDataId(emp.getId().toString());
					}
				}
				model.addAttribute("employeeList", chargesheetdetails.getCaseDetails().getEmployeeList());
			}
		}
		
		model.addAttribute("chargeSheetObj", chargesheetdetails);
		
		/*model.addAttribute("DecisionTakenList", decisiontakenrepository.findAll());
		model.addAttribute("casependingwithlist", casependingwithRepository.findAll());
		model.addAttribute("TypeofPenaltylist", typeofpenaltyrepository.findAll());
			   */
		try {

			if (chargesheetdetails.getCaseDetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}
				
				CaseDetails cd = chargesheetdetails.getCaseDetails();
				if(cd != null) {
					if(cd.getSuspensionList().size()>0) {
						model.addAttribute("reinstateEnable", true);
					}
					if(cd.getRuleApplicable() != null) {
						model.addAttribute("ruleid", cd.getRuleApplicable().getId());
					}
					if(cd.getSubRule() != null) {
						model.addAttribute("subruleid", cd.getSubRule().getId());
					}
					if(cd.getMisConductType() != null) {
						model.addAttribute("miscodunctid", cd.getMisConductType().getId());
					}
					model.addAttribute("chargesheetissued", cd.isWhetherChargesheetIssued());
					if(cd.getEmployeeType().getId()==1) {
						model.addAttribute("single", true);
						if(cd.getEmployeeList().size()==1 
								&& cd.getEmployeeList().get(0).getSuperannuationDate() != null 
								&& cd.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
							model.addAttribute("retired", true);
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<ChargesheetDetails> noticePages = chargesheetdetailsRepository.findByCaseDetailsId(chargesheetdetails.getCaseDetails().getId(),pageable);

				
				model.addAttribute("chargesheetList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				model.addAttribute("chargesheetList", chargesheetdetailsRepository.findAll());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("chargesheetList", chargesheetdetailsRepository.findAll());
		}

		return "DeptInquiryViews/chargeSheetDetails";
	}
	
	
	@GetMapping("/reportsforProsecutionProposal/{page}")
	public String reportsforProsecutionProposal(Model model, @PathVariable("page") int page,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "globalorgid", required = false) String globalorgdid,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		LOG.info("hi this is the reports  Details Records For search  page.......");

		String customsearch = null;
		if (search != null && search.contains("splitting")) {
			customsearch = search.split("splitting")[1];
			if(customsearch.equalsIgnoreCase("null")) {
				customsearch = "";
			}
		}
		
		if(globalorgdid != null && globalorgdid.equalsIgnoreCase("null")) {
			globalorgdid = "";
		}
		model.addAttribute("globalorgid", globalorgdid);
		GlobalOrg org = null;
		if(globalorgdid != null && !globalorgdid.equalsIgnoreCase("null") && !globalorgdid.equalsIgnoreCase("")) {
			org = orgRepo.findById(Long.valueOf(globalorgdid)).orElse(new GlobalOrg());
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

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
		Page<CaseDetails> allCases = null;

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if(org != null) {
//				List<ProsecutionProposalDetails> proseList = prosecutionRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCasedetailsIdNotIn(true,caseId);
				List<EmployeeDetails> empProList = employeedetailsrepo.findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndCurrentUserPimsEmployeeGlobalOrgId(caseId,true,org);

				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(empProList,pageable,caseId,org);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empProList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUserPimsEmployeeGlobalOrgId(empProList,pageable,caseId,org);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empProList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				model.addAttribute("empCount", empProList.size());
//				model.addAttribute("empCount", allCases.getContent().stream().mapToLong(s -> s.getProsecutionProposal().size()).sum());
			} else {
//				List<ProsecutionProposalDetails> proseList = prosecutionRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(true,customsearch,caseId);
				List<EmployeeDetails> empProList = employeedetailsrepo.findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCase(caseId,true,customsearch);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndIdNotIn(empProList,pageable,customsearch,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empProList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndIdNotIn(empProList,allPage,customsearch,caseId);
				allCases.getContent().stream().forEach(s->{
//					List<ProsecutionProposalDetails> empp = new ArrayList<>();
					List<EmployeeDetails> empp = new ArrayList<>();
					empProList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				model.addAttribute("empCount", empProList.size());
//				model.addAttribute("empCount", allCases.getContent().stream().mapToLong(s -> s.getProsecutionProposal().size()).sum());
			}

		} else 		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			customsearch = userService.getCurrentUserPojo().getPimsEmployee().getGlobalOrgId().getGlobalOrgName();
			search = "truesplitting"+customsearch;
			model.addAttribute("search", search);
			if(StringUtils.isBlank(customsearch)) {
//				List<ProsecutionProposalDetails> proseList = prosecutionRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCasedetailsIdNotIn(true,caseId);
//				noticePages = caseDetailsRepo.findDistinctIdByProsecutionProposalInAndIdNotIn(proseList,pageable,caseId);
//				noticePages.getContent().stream().forEach(s->{
//					List<ProsecutionProposalDetails> empp = new ArrayList<>();
//					proseList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setProsecutionProposal(empp);
//				});
//				
//				
//				allCases = caseDetailsRepo.findDistinctIdByProsecutionProposalInAndIdNotIn(proseList,allPage,caseId);
//				allCases.getContent().stream().forEach(s->{
//					List<ProsecutionProposalDetails> empp = new ArrayList<>();
//					proseList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setProsecutionProposal(empp);
//				});
//				
//				model.addAttribute("empCount", proseList.size());
				List<EmployeeDetails> empProList = employeedetailsrepo.findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceived(caseId,true);

				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,pageable,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empProList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotIn(empProList,pageable,caseId);
				allCases.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empProList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				model.addAttribute("empCount", empProList.size());
			} else {
//				List<ProsecutionProposalDetails> proseList = prosecutionRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndCasedetailsIdNotIn(true,customsearch,caseId);
//				noticePages = caseDetailsRepo.findDistinctIdByProsecutionProposalInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndIdNotIn(proseList,pageable,customsearch,caseId);
//				noticePages.getContent().stream().forEach(s->{
//					List<ProsecutionProposalDetails> empp = new ArrayList<>();
//					proseList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setProsecutionProposal(empp);
//				});
//				
//				
//				allCases = caseDetailsRepo.findDistinctIdByProsecutionProposalInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndIdNotIn(proseList,allPage,customsearch,caseId);
//				allCases.getContent().stream().forEach(s->{
//					List<ProsecutionProposalDetails> empp = new ArrayList<>();
//					proseList.stream().forEach(ss->{
//						if(ss.getCasedetails().getId()==s.getId()) {
//							empp.add(ss);
//						}	
//					});
//					s.setProsecutionProposal(empp);
//				});
//				
//				model.addAttribute("empCount", proseList.size());
//				model.addAttribute("empCount", allCases.getContent().stream().mapToLong(s -> s.getProsecutionProposal().size()).sum());
				List<EmployeeDetails> empProList = employeedetailsrepo.findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndAndCasedetailsCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCase(caseId,true,customsearch);
				noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndIdNotIn(empProList,pageable,customsearch,caseId);
				noticePages.getContent().stream().forEach(s->{
					List<EmployeeDetails> empp = new ArrayList<>();
					empProList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				
				allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndCurrentUserPimsEmployeeGlobalOrgIdGlobalOrgNameContainingIgnoreCaseAndIdNotIn(empProList,allPage,customsearch,caseId);
				allCases.getContent().stream().forEach(s->{
//					List<ProsecutionProposalDetails> empp = new ArrayList<>();
					List<EmployeeDetails> empp = new ArrayList<>();
					empProList.stream().forEach(ss->{
						if(ss.getCasedetails().getId()==s.getId()) {
							empp.add(ss);
						}	
					});
					s.setEmployeeList(empp);
				});
				
				model.addAttribute("empCount", empProList.size());
			}

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
//			List<ProsecutionProposalDetails> proseList = prosecutionRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsIsNotNullAndCurrentUserInAndCasedetailsIdNotIn(true,userList,caseId);
//			noticePages = caseDetailsRepo.findDistinctIdByProsecutionProposalInAndCurrentUserInAndIdNotIn(proseList,pageable,userList,caseId);
//			noticePages.getContent().stream().forEach(s->{
//				List<ProsecutionProposalDetails> empp = new ArrayList<>();
//				proseList.stream().forEach(ss->{
//					if(ss.getCasedetails().getId()==s.getId()) {
//						empp.add(ss);
//					}	
//				});
//				s.setProsecutionProposal(empp);
//			});
//			
//			allCases = caseDetailsRepo.findDistinctIdByProsecutionProposalInAndCurrentUserInAndIdNotIn(proseList,allPage,userList,caseId);
//			allCases.getContent().stream().forEach(s->{
//				List<ProsecutionProposalDetails> empp = new ArrayList<>();
//				proseList.stream().forEach(ss->{
//					if(ss.getCasedetails().getId()==s.getId()) {
//						empp.add(ss);
//					}	
//				});
//				s.setProsecutionProposal(empp);
//			});
//			model.addAttribute("empCount", proseList.size());
//			model.addAttribute("empCount", allCases.getContent().stream().mapToLong(s -> s.getProsecutionProposal().size()).sum());
			List<EmployeeDetails> empProList = employeedetailsrepo.findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndCurrentUserIn(caseId,true,userList);

			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUserIn(empProList,pageable,caseId,userList);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empProList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUserIn(empProList,allPage,caseId,userList);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empProList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("empCount", empProList.size());

		} else {
//			List<ProsecutionProposalDetails> proseList = prosecutionRepo.findDistinctCasedetailsByProsecutionProposalReceivedAndCasedetailsCurrentUserAndCasedetailsIsNotNullAndCasedetailsIdNotIn(true,currentUser,caseId);
////			List<EmployeeDetails> empList = 
//			noticePages = caseDetailsRepo.findDistinctIdByProsecutionProposalInAndCurrentUserAndIdNotIn(proseList,pageable,currentUser,caseId);
//			noticePages.getContent().stream().forEach(s->{
//				List<ProsecutionProposalDetails> empp = new ArrayList<>();
//				proseList.stream().forEach(ss->{
//					if(ss.getCasedetails().getId()==s.getId()) {
//						empp.add(ss);
//					}	
//				});
//				s.setProsecutionProposal(empp);
//			});
//			
//			allCases = caseDetailsRepo.findDistinctIdByProsecutionProposalInAndCurrentUserAndIdNotIn(proseList,allPage,currentUser,caseId);
//			allCases.getContent().stream().forEach(s->{
//				List<ProsecutionProposalDetails> empp = new ArrayList<>();
//				proseList.stream().forEach(ss->{
//					if(ss.getCasedetails().getId()==s.getId()) {
//						empp.add(ss);
//					}	
//				});
//				s.setProsecutionProposal(empp);
//			});
//			model.addAttribute("empCount", proseList.size());
//			model.addAttribute("empCount", allCases.getContent().stream().mapToLong(s -> s.getProsecutionProposal().size()).sum());
			List<EmployeeDetails> empProList = employeedetailsrepo.findDistinctCasedetailsByCasedetailsIsNotNullAndCasedetailsIdNotInAndProsecutionProposalDetailsProsecutionProposalReceivedAndCurrentUser(caseId,true,currentUser);

			noticePages = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empProList,pageable,caseId,currentUser);
			noticePages.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empProList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			
			allCases = caseDetailsRepo.findDistinctIdByEmployeeListInAndIdNotInAndCurrentUser(empProList,allPage,caseId,currentUser);
			allCases.getContent().stream().forEach(s->{
				List<EmployeeDetails> empp = new ArrayList<>();
				empProList.stream().forEach(ss->{
					if(ss.getCasedetails().getId()==s.getId()) {
						empp.add(ss);
					}	
				});
				s.setEmployeeList(empp);
			});
			
			model.addAttribute("empCount", empProList.size());
		}
		//// -------------------------------///-----------------
		
//		Map<Long,CaseDetails> caseList = new HashMap<>();
//		noticePages.stream().filter(s->s.getCasedetails() != null && s.getCasedetails().getId() != null).forEach(s->caseList.put(s.getCasedetails().getId(), s.getCasedetails()));
//		model.addAttribute("caselist", caseList.values().parallelStream().collect(Collectors.toList()));
		model.addAttribute("allcases", allCases);
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

		model.addAttribute("noticelist", noticePages.getContent());
		return "DeptInquiryViews/reportsforprosection";

	}
	
	

	
	
	
	@GetMapping("/editfinalOutcomeDtlsRecordsWithoutDe/{id}/{page}")
	public String editfinalOutcomeDtlsRecordsWithoutDe(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the fetching of final Out come Dtls Records Records! ");

//		ProsecutionProposalDetails  prosecutionproposaldetails=prosecutionRepo.findById(id).orElse(new ProsecutionProposalDetails());
		 
//		CourtCaseDetails  courtcasedetails=courtcaseRepo.findById(id).orElse(new CourtCaseDetails());
		FinalOutcomeDetails   finaloutcomedetails=finaloutcomerepository.findById(id).orElse(new FinalOutcomeDetails());
		if(finaloutcomedetails.getCasedetails() != null) {
			finaloutcomedetails.setCaseNo(finaloutcomedetails.getCasedetails().getCaseNo());
		}
		model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails);
		
		model.addAttribute("DecisionTakenList", decisiontakenrepository.findAll());
		model.addAttribute("casependingwithlist", casependingwithRepository.findAll());
		model.addAttribute("TypeofPenaltylist", typeofpenaltyrepository.findAll());
			   
			   
			   

	
		try {

			
			
			if (finaloutcomedetails.getCasedetails() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<FinalOutcomeDetails> noticePages = finaloutcomerepository.findByFileNoIsNotNullAndFileNoIsNot("",pageable);

				
				model.addAttribute("FinalOutComeList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
				
//				model.addAttribute("employeeList",
//						employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));

			} else {
				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<FinalOutcomeDetails> noticePages = finaloutcomerepository.findByFileNoIsNotNullAndFileNoIsNot("",pageable);

				
				model.addAttribute("FinalOutComeList", noticePages);
				model.addAttribute("currentPage", page);
				int begin = Math.max(1, page - 5);
				int end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());
			}
		} catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("FinalOutComeList", finaloutcomerepository.findAll());
		}

		return "DeptInquiryViews/FinalOutcomeWithoutDe";
	}

	
	
	
	

}
