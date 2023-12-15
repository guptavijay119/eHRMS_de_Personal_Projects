package com.ehrms.deptenq.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehrms.deptenq.constants.DepartmentConstants;
import com.ehrms.deptenq.constants.PROBATIONCONSTANTS;
import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.CasePendingWithPojo;
import com.ehrms.deptenq.models.ChargesheetDetails;
import com.ehrms.deptenq.models.CourtCaseDetails;
import com.ehrms.deptenq.models.CourtName;
import com.ehrms.deptenq.models.DecisionTakenPojo;
import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.DetailsKeptAbeyanceCases;
import com.ehrms.deptenq.models.Districts;
import com.ehrms.deptenq.models.Division;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.EmployeeType;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.Gender;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.IORequestTransaction;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.InquiryOfficerList;
import com.ehrms.deptenq.models.MailTransaction;
import com.ehrms.deptenq.models.MisconductTypesMaster;
import com.ehrms.deptenq.models.PresentingOfficerDetails;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.ReInstatedDetails;
import com.ehrms.deptenq.models.ReasonForKeepingCaseAbeyance;
import com.ehrms.deptenq.models.Role;
import com.ehrms.deptenq.models.RulesApplicableMaster;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SubRuleApplicableMaster;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.SuspensionRules;
import com.ehrms.deptenq.models.TypeOfPenaltyInflictedPojo;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.modelsDto.CaseDetailsDto;
import com.ehrms.deptenq.modelsDto.CaseTransferDto;
import com.ehrms.deptenq.modelsDto.ChargesheetDto;
import com.ehrms.deptenq.modelsDto.CourtCaseDetailsDto;
import com.ehrms.deptenq.modelsDto.DetailsKeptAbeyanceCasesDto;
import com.ehrms.deptenq.modelsDto.EmployeeDetailsDto;
import com.ehrms.deptenq.modelsDto.FileData;
import com.ehrms.deptenq.modelsDto.FinalOutcomeDetailsDTO;
import com.ehrms.deptenq.modelsDto.InquiryOfficerDto;
import com.ehrms.deptenq.modelsDto.MailDTO;
import com.ehrms.deptenq.modelsDto.PresentingOfficerDto;
import com.ehrms.deptenq.modelsDto.ProbationOrderDto;
import com.ehrms.deptenq.modelsDto.ProsecutionProposalDetailsDto;
import com.ehrms.deptenq.modelsDto.ReInstatedDetailsDto;
import com.ehrms.deptenq.modelsDto.SuspensionDetailsDto;
import com.ehrms.deptenq.repository.AppllationMasterRepository;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.CasePendingWithRepository;
import com.ehrms.deptenq.repository.DecisionTakenRepository;
import com.ehrms.deptenq.repository.DesignationRepository;
import com.ehrms.deptenq.repository.DetailsKeptAbeyanceCasesRepository;
import com.ehrms.deptenq.repository.DistrictsRepository;
import com.ehrms.deptenq.repository.EmployeeTypeRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.GenderRepository;
import com.ehrms.deptenq.repository.GradePayRepository;
import com.ehrms.deptenq.repository.IChargesheetDetailsRepository;
import com.ehrms.deptenq.repository.ICourtCaseDetailsRepository;
import com.ehrms.deptenq.repository.ICourtNameRepository;
import com.ehrms.deptenq.repository.IDivisionRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.IORequestTransactionRepository;
import com.ehrms.deptenq.repository.IPresentingOfficerRepository;
import com.ehrms.deptenq.repository.IProsecutionProposalDetailsRepository;
import com.ehrms.deptenq.repository.IReInstatedRepository;
import com.ehrms.deptenq.repository.InquiryOfficerListRepository;
import com.ehrms.deptenq.repository.InquiryOfficerRepository;
import com.ehrms.deptenq.repository.MailTransactionRepository;
import com.ehrms.deptenq.repository.MisconductTypesMasterRepository;
import com.ehrms.deptenq.repository.PayBandRepository;
import com.ehrms.deptenq.repository.PayCommissionRepository;
import com.ehrms.deptenq.repository.ReasonForKeepingCaseAbeyanceRepository;
import com.ehrms.deptenq.repository.RolesRepository;
import com.ehrms.deptenq.repository.RulesApplicableMasterRepository;
import com.ehrms.deptenq.repository.Service_GroupRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.SubRuleRepository;
import com.ehrms.deptenq.repository.SuspensionDetailsRepository;
import com.ehrms.deptenq.repository.SuspensionRuleRepository;
import com.ehrms.deptenq.repository.TypeOfPenaltyRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.FileService;
import com.ehrms.deptenq.service.MailService;
import com.ehrms.deptenq.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;


@Controller
@RequestMapping("departmentalEnquiry")
public class DepartmentalEnquiryController {

	@Autowired
	private MisconductTypesMasterRepository misconductTypesMasterRepo;
	
	@PersistenceContext
	private EntityManager em;
	
	/** The transactiona template. */
	@Autowired
	private TransactionTemplate transactionaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RolesRepository roleRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private RulesApplicableMasterRepository rulesApplicableMasterRepo;
	
	@Autowired
	private SubRuleRepository subRuleRepo;

	@Autowired
	private EmployeeTypeRepository employeeTypeRepo;

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LocaleResolver loc;

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
	private IEmployeeDetailsRepository employeedetailsrepo;

	@Autowired
	private IChargesheetDetailsRepository chargesheetRepo;

	@Autowired
	private SuspensionRuleRepository suspensionRuleRepo;

	@Autowired
	private SuspensionDetailsRepository suspensionDetailsRepo;

	@Autowired
	private IReInstatedRepository reinstatedRepository;

	@Autowired
	private InquiryOfficerRepository inquiryofficerRepository;

	@Autowired
	private IPresentingOfficerRepository ipresentingofficerrepository;

	@Autowired
	private IProsecutionProposalDetailsRepository iprosecutionproposaldetailsRepository;

	@Autowired
	private ICourtCaseDetailsRepository icourtcasedetailsRepository;

	@Autowired
	private ICourtNameRepository icourtnamerepository;

	@Autowired
	private IDivisionRepository divisionRepository;

	@Autowired
	private InquiryOfficerListRepository inquiryOfficerListRepo;

	@Autowired
	private DetailsKeptAbeyanceCasesRepository detailskeptabeyancecasesRepository;

	@Autowired
	private FinalOutcomeRepository finaloutcomeRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private ReasonForKeepingCaseAbeyanceRepository reasonforkeepingcaseabeyanceRepository;

	@Autowired
	private DecisionTakenRepository decisiontakenrepository;

	@Autowired
	private CasePendingWithRepository casependingwithRepository;

	@Autowired
	private TypeOfPenaltyRepository typeofpenaltyrepository;

	@Autowired
	private DepartmentConstants departmentconstants;

	@Autowired
	private AppllationMasterRepository appellationRepo;
	
	@Autowired
	private IProsecutionProposalDetailsRepository   prosecutionRepo;
	
	@Autowired
	private DistrictsRepository   districtsrepository;
	
	@Autowired
	private IORequestTransactionRepository ioReqRepo;
	
	@Autowired
	private MailTransactionRepository mailTransactionRepo;
	
	@Autowired
	private MailService mailService;
	

	private static final Logger LOG = LoggerFactory.getLogger(DepartmentalEnquiryController.class);

	/*
	 * private static final String MESSAGE = "message";
	 * 
	 * private static final String DIRECTORYPATH = "departmentalEnquiry/";
	 * 
	 * private static final String CONTENTDEPOSITION = "Content-Disposition";
	 * 
	 * private static final String INLINEFILENAME = "inline; filename=";
	 * 
	 */
	/** The Constant CONTENTDEPOSITION. */
	private static final String CONTENTDEPOSITION = "Content-Disposition";

	@GetMapping("/dashboard")
	public String dashboard() {
		return "DeptInquiryViews/de_registration";
	}

	/* Landing Methods of case details Screen */

	@GetMapping("/caseDetails")
	public String caseDetails(Model model, @RequestParam(name = "session", required = false) boolean session) {

		if (session) {
			request.getSession().setAttribute("caseid", null);
		}
		try {
			String caseid = request.getSession().getAttribute("caseid").toString();
			model.addAttribute("orgList", globalorgRepo.findByActive(true,Sort.by("id")));
			if (caseid != null) {
				CaseDetails cases = caseDetailsRepo.findById(Long.valueOf(caseid)).orElse(new CaseDetails());
				if(cases.getIoList().isEmpty()) {
					model.addAttribute("disablepresentingofficer", true);
				} 
				if(cases.getMisConductType() != null && (cases.getMisConductType().getId()==1 || cases.getMisConductType().getId()==1)) {
					model.addAttribute("disableprosecution", true);
				}
				if(cases.getSuspensionList().size()>0) {
					model.addAttribute("reinstateEnable", true);
				}
				if(cases.getEmployeeList().size()==1 
						&& cases.getEmployeeList().get(0).getSuperannuationDate() != null 
						&& cases.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
					model.addAttribute("retired", true);
				}
				if(cases.getEmployeeType().getId()==1) {
					model.addAttribute("single", true);
					if(cases.getEmployeeList().size()==1 
							&& cases.getEmployeeList().get(0).getSuperannuationDate() != null 
							&& cases.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
						model.addAttribute("retired", true);
					}
					if(Integer.valueOf(cases.getTotalNoOfEmployee())!=cases.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cases.getTotalNoOfEmployee());
						model.addAttribute("entered", cases.getEmployeeList().size());
					} else {
						model.addAttribute("totalemployee", cases.getTotalNoOfEmployee());
						model.addAttribute("entered", cases.getEmployeeList().size());
					}
				} else {
					if(Integer.valueOf(cases.getTotalNoOfEmployee())!=cases.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cases.getTotalNoOfEmployee());
						model.addAttribute("entered", cases.getEmployeeList().size());
					} else {
						model.addAttribute("totalemployee", cases.getTotalNoOfEmployee());
						model.addAttribute("entered", cases.getEmployeeList().size());
					}
				}
				if (cases != null) {
					model.addAttribute("cases", cases);
				} else {
					model.addAttribute("cases", new CaseDetails());
				}
			} else {
				model.addAttribute("cases", new CaseDetails());
			}
		} catch (Exception r) {
//			LOG.error("error", r);
			model.addAttribute("cases", new CaseDetails());
		}
		model.addAttribute("misConductType", misconductTypesMasterRepo.findAll(Sort.by("order")));
		model.addAttribute("rulesapplicable", rulesApplicableMasterRepo.findByActive(true));
		model.addAttribute("subrulesapplicable", subRuleRepo.findAll(Sort.by("id")));
		model.addAttribute("employeeType", employeeTypeRepo.findAll());
		model.addAttribute("divisiondata", divisionRepository.findByActive(true));
		model.addAttribute("districtdata", districtsrepository.findByState_StateCodeOrderByDistrictName(Long.valueOf(18)));
		return "DeptInquiryViews/caseDetails";
	}

	/* fetching records of case details methods */

	// @RequestMapping(value="/viewPreSale", method = {RequestMethod.POST,
	// RequestMethod.GET})
	@GetMapping("/fetchcaseDetails/{caseid}")
	public String caseDetails(@PathVariable("caseid") Long caseid, Model model) {
		LOG.info("hi this is the fetching of case details Records! ");

		CaseDetails casedtls = caseDetailsRepo.findById(caseid).orElse(new CaseDetails());
		model.addAttribute("orgList", globalorgRepo.findByActive(true, Sort.by("id")));
		model.addAttribute("transferred", casedtls.getTransferStatus());
		if(casedtls.getEmployeeList().size()==1 
				&& casedtls.getEmployeeList().get(0).getSuperannuationDate() != null 
				&& casedtls.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
			model.addAttribute("retired", true);
			
		}
		if(casedtls.getIoList().isEmpty()) {
			model.addAttribute("disablepresentingofficer", true);
		} 
		if(casedtls.getMisConductType() != null && (casedtls.getMisConductType().getId()==1 || casedtls.getMisConductType().getId()==1)) {
			model.addAttribute("disableprosecution", true);
		} 
		if(casedtls.getSuspensionList().size()>0) {
			model.addAttribute("reinstateEnable", true);
		}
		if(casedtls.getEmployeeType().getId()==1) {
			model.addAttribute("single", true);
			if(casedtls.getEmployeeList().size()==1 
					&& casedtls.getEmployeeList().get(0).getSuperannuationDate() != null 
					&& casedtls.getEmployeeList().get(0).getSuperannuationDate().isBefore(LocalDate.now())) {
				model.addAttribute("retired", true);
			}
			if(Integer.valueOf(casedtls.getTotalNoOfEmployee())!=casedtls.getEmployeeList().size()) {
				model.addAttribute("totalemployee", casedtls.getTotalNoOfEmployee());
				model.addAttribute("entered", casedtls.getEmployeeList().size());
			} else {
				model.addAttribute("totalemployee", casedtls.getTotalNoOfEmployee());
				model.addAttribute("entered", casedtls.getEmployeeList().size());
			}
		} else {
			if(Integer.valueOf(casedtls.getTotalNoOfEmployee())!=casedtls.getEmployeeList().size()) {
				model.addAttribute("totalemployee", casedtls.getTotalNoOfEmployee());
				model.addAttribute("entered", casedtls.getEmployeeList().size());
			} else {
				model.addAttribute("totalemployee", casedtls.getTotalNoOfEmployee());
				model.addAttribute("entered", casedtls.getEmployeeList().size());
			}
		}

		model.addAttribute("cases", casedtls);
		model.addAttribute("misConductType", misconductTypesMasterRepo.findAll(Sort.by("order")));
		model.addAttribute("rulesapplicable", rulesApplicableMasterRepo.findByActive(true,Sort.by("id")));
		model.addAttribute("employeeType", employeeTypeRepo.findAll());
		model.addAttribute("subrulesapplicable", subRuleRepo.findAll(Sort.by("id")));
		
		model.addAttribute("divisiondata", divisionRepository.findByActive(true));
		
		model.addAttribute("districtdata", districtsrepository.findByState_StateCodeOrderByDistrictName(Long.valueOf(18)));
		request.getSession().setAttribute("caseid", caseid);

		return "DeptInquiryViews/caseDetails";
	}
	/* Saving case details screens Methods */

	@PostMapping("/addcasedetails")
	public String addcasedetails(@ModelAttribute("cases") CaseDetailsDto caseDetailsDto, BindingResult result,
			@RequestParam(name = "fileName", required = false) MultipartFile file,
			Model model, RedirectAttributes redirect) {
		long caseid = 0;
		try {
			User currentUser = userService.getCurrentUserPojo();
			CaseDetails caseDetails = new CaseDetails();
			if (caseDetailsDto.getId() != null
					&& caseDetailsRepo.existsById(caseDetailsDto.getId())) {

				caseDetails = caseDetailsRepo.findById(caseDetailsDto.getId()).orElse(new CaseDetails());
				if(caseDetailsDto.getCaseNo() != null && StringUtils.isNotBlank(caseDetailsDto.getCaseNo())
						&& !StringUtils.equalsIgnoreCase(caseDetailsDto.getCaseNo(), caseDetails.getPreviousCaseNo())) {
					caseDetails.setCaseNo(caseDetailsDto.getCaseNo());
					caseDetails.setTransferStatus(null);
				}
				caseDetails.setFileSubject(caseDetailsDto.getFileSubject());
				if(!caseDetailsDto.getTotalNoOfEmployee().equalsIgnoreCase("0")) {
					caseDetails.setTotalNoOfEmployee(StringUtils.stripStart(caseDetailsDto.getTotalNoOfEmployee(), "0"));
				} else {
					caseDetails.setTotalNoOfEmployee(caseDetailsDto.getTotalNoOfEmployee());					
				}
				caseDetails.setOtherThanGovServ(caseDetailsDto.getOtherThanGovServ());
				// caseDetails = caseDetailsRepo.findById(caseDetailsDto.getId()).orElse(new
				// CaseDetails());
				caseDetails.setCaseNo(caseDetailsDto.getCaseNo());
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_VERIFICATION")) {
					caseDetails.setApproved(caseDetailsDto.isApproved());
				}
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_DEPARTMENT")) {
					if (caseDetails.isApproved()) {
						caseDetails.setApproved(false);
					}
				}
				caseDetails.setCaseSummary(caseDetailsDto.getCaseSummary());

				if (caseDetailsDto.getMisConductType() != null) {
					caseDetails.setMisConductType(misconductTypesMasterRepo
							.findById(caseDetailsDto.getMisConductType().getId()).orElse(new MisconductTypesMaster()));
				}

				else {
					caseDetails.setMisConductType(null);
				}

				if (caseDetailsDto.getRuleApplicable() != null && caseDetailsDto.getRuleApplicable().getId() != null) {
					if(caseDetailsDto.getRuleApplicable().getId()==3) {
						caseDetails.setWhetherChargesheetIssued(caseDetailsDto.isWhetherChargesheetIssued());
					} else {
						caseDetails.setWhetherChargesheetIssued(true);
					}

					caseDetails.setRuleApplicable(rulesApplicableMasterRepo
							.findById(caseDetailsDto.getRuleApplicable().getId()).orElse(new RulesApplicableMaster()));
				} else {
					caseDetails.setRuleApplicable(null);
				}
				
				if(caseDetailsDto.getSubRule() != null && caseDetailsDto.getSubRule().getId() != null) {
					caseDetails.setSubRule(subRuleRepo.findById(caseDetailsDto.getSubRule().getId()).orElse(new SubRuleApplicableMaster()));
				}
				
				else {
					
					caseDetails.setSubRule(null);
					
					
				}

				if (caseDetailsDto.getEmployeeType() != null) {
					caseDetails.setEmployeeType(employeeTypeRepo.findById(caseDetailsDto.getEmployeeType().getId())
							.orElse(new EmployeeType()));
				}

				else {
					caseDetails.setEmployeeType(null);

				}
				
				/* code written for district and division   on 15/11/2022 */
				if (caseDetailsDto.getDistrict() != null) {
					caseDetails.setDistrict(districtsrepository.findById(caseDetailsDto.getDistrict().getId()).orElse(new Districts()));
				}
				else {
					caseDetails.setDistrict(null);
				}
				
				if (caseDetailsDto.getDivision() != null) {
					caseDetails.setDivision(divisionRepository.findById(caseDetailsDto.getDivision().getId()).orElse(new Division()));
				}
				else {
					caseDetails.setDivision(null);
				}
				
				if (file != null) {
					if (!file.isEmpty() && file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
						String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
								+ caseDetails.getId() + "/" + "caseextensiondetails/"
								 + "/";
						caseDetails.setFileName(file.getOriginalFilename());
						caseDetails.setContentType(file.getContentType());
						caseDetails.setFilepath(filePath);
						fileService.fileUpload(filePath, file, caseDetails.getFileName());
						caseDetailsRepo.save(caseDetails);
					}

					else {
						redirect.addFlashAttribute("error", "Please select correct pdf file only..");
					}
				}
				
				
				caseDetails.setActive(true);
				caseDetails = caseDetailsRepo.save(caseDetails);
				caseid = caseDetails.getId();
				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
			} 
			
			
			
			else {

//				caseDetails.setCasedetails(caseDetailsRepo.findById(caseDetailsDto.getCasedetails().getId()).orElse(new CaseDetails()));

				// for save
				/* this code is checking on Case Details screen at case id is exist or not */
				if(caseDetailsRepo.existsByCaseNo(caseDetailsDto.getCaseNo())) {
//					caseDetails = caseDetailsRepo.findByCaseNo(caseDetailsDto.getCaseNo());
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							messageSource.getMessage("dept.inq.casenoexists", null, loc.resolveLocale(request)));
					return "redirect:/departmentalEnquiry/caseDetails";
				}
				
				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(caseDetailsDto.getCaseNo(),currentUser)) {

					caseDetails = caseDetailsRepo.findByCaseNoContainingIgnoreCaseAndCurrentUser(caseDetailsDto.getCaseNo(),currentUser).get(0);
				} else {
					caseDetails.setCaseNo(caseDetailsDto.getCaseNo());
					caseDetails.setCaseSummary(caseDetailsDto.getCaseSummary());
				}
				// added on 29/09/2022 ,

				if (currentUser != null) {
					caseDetails.setCurrentUser(currentUser);
					caseDetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
					if (currentUser.getSubDepartment() != null) {
						caseDetails.setSubDepartment(currentUser.getSubDepartment());
					}
				}

				caseDetails.setFileSubject(caseDetailsDto.getFileSubject());
				caseDetails.setOtherThanGovServ(caseDetailsDto.getOtherThanGovServ());
				if(!caseDetailsDto.getTotalNoOfEmployee().equalsIgnoreCase("0")) {
					caseDetails.setTotalNoOfEmployee(StringUtils.stripStart(caseDetailsDto.getTotalNoOfEmployee(), "0"));
				} else {
					caseDetails.setTotalNoOfEmployee(caseDetailsDto.getTotalNoOfEmployee());					
				}
				if (caseDetailsDto.getMisConductType() != null) {
					caseDetails.setMisConductType(misconductTypesMasterRepo
							.findById(caseDetailsDto.getMisConductType().getId()).orElse(new MisconductTypesMaster()));
				}

				else {
					caseDetails.setMisConductType(null);
				}

				if (caseDetailsDto.getRuleApplicable() != null && caseDetailsDto.getRuleApplicable().getId() != null) {
					if(caseDetailsDto.getRuleApplicable().getId()==3) {
						caseDetails.setWhetherChargesheetIssued(caseDetailsDto.isWhetherChargesheetIssued());
					} else {
						caseDetails.setWhetherChargesheetIssued(true);
					}
					caseDetails.setRuleApplicable(rulesApplicableMasterRepo
							.findById(caseDetailsDto.getRuleApplicable().getId()).orElse(new RulesApplicableMaster()));
				}

				else {
					caseDetails.setRuleApplicable(null);
				}
				
				if(caseDetailsDto.getSubRule() != null && caseDetailsDto.getSubRule().getId() != null) {
					caseDetails.setSubRule(subRuleRepo.findById(caseDetailsDto.getSubRule().getId()).orElse(new SubRuleApplicableMaster()));
				}

				if (caseDetailsDto.getEmployeeType() != null) {
					caseDetails.setEmployeeType(employeeTypeRepo.findById(caseDetailsDto.getEmployeeType().getId())
							.orElse(new EmployeeType()));
				}

				else {
					caseDetails.setEmployeeType(null);

				}
				
				
				/* code written for district and division   on 15/11/2022 */
				if (caseDetailsDto.getDistrict() != null) {
					caseDetails.setDistrict(districtsrepository.findById(caseDetailsDto.getDistrict().getId()).orElse(new Districts()));
				}
				else {
					caseDetails.setDistrict(null);
				}
				
				if (caseDetailsDto.getDivision() != null) {
					caseDetails.setDivision(divisionRepository.findById(caseDetailsDto.getDivision().getId()).orElse(new Division()));
				}
				else {
					caseDetails.setDivision(null);
				}

				caseDetails.setActive(true);
				caseDetails = caseDetailsRepo.save(caseDetails);
				caseid = caseDetails.getId();
				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
			}
		}

		catch (Exception e) {
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
//s			redirect.addAttribute(model);
		}
//		return "redirect:/departmentalEnquiry/caseDetails";
		if(caseid != 0) {
			return "redirect:/departmentalEnquiry/fetchcaseDetails/"+caseid;
		} else {
			return "redirect:/departmentalEnquiry/caseDetails";
		}
	}
	
	
	@PostMapping("/transfercasedetails")
	public ResponseEntity<?> transfercasedetails(@ModelAttribute CaseTransferDto data,Model model,
			RedirectAttributes redirect) {
//		long caseid = 0;
		GlobalOrg org = globalorgRepo.findById(data.getGlobalorg()).orElse(new GlobalOrg());
		Role role =roleRepo.findById(1L).orElse(new Role());
		List<Role> roleList = new ArrayList<>();
		roleList.add(role);
		List<User> userList = userRepo.findByPimsEmployeeGlobalOrgIdAndActiveAndProcessTypeAndRolesIn(org,true,"de",roleList,Sort.by("id"));

		try {
//			User currentUser = userService.getCurrentUserPojo();
			CaseDetails caseDetailsNew = new CaseDetails();
			if(data.getId()>0 && !userList.isEmpty()) {
				CaseDetails caseDetailsOld = caseDetailsRepo.findById(data.getId()).orElse(new CaseDetails());
				long caseId = caseDetailsOld.getId();
				BeanUtils.copyProperties(caseDetailsOld, caseDetailsNew);
				caseDetailsOld.setActiveForTransfer(true);
				caseDetailsOld.setReasonForTransfer(data.getReason());
				caseDetailsOld.setTransferStatus("Transferred");
				if(data.getFile() != null 
						&& !data.getFile().isEmpty() 
						&& StringUtils.equalsIgnoreCase(data.getFile().getContentType(), MediaType.APPLICATION_PDF_VALUE)) {
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ caseId + "/" + "transfercasedocument/";
					caseDetailsOld.setFileNameTransferApproval(data.getFile().getOriginalFilename());
					caseDetailsOld.setFilePathTransferApproval(filePath);
					fileService.fileUpload(filePath, data.getFile(), caseDetailsOld.getFileNameTransferApproval());
				}
				caseDetailsOld.setCaseTransferredTo(userList.get(0));
				caseDetailsOld = caseDetailsRepo.save(caseDetailsOld);
				caseDetailsNew.setTransferStatus("ChangeRequired");
				caseDetailsNew.setId(null);
				caseDetailsNew.setPreviousCaseNo(caseDetailsOld.getCaseNo());
				caseDetailsNew.setGlobalorg(org);
				caseDetailsNew.setCurrentUser(userList.get(0));
				caseDetailsNew.setEmployeeList(null);
				caseDetailsNew.setChargeSheetList(null);
				caseDetailsNew.setSuspensionList(null);
				caseDetailsNew.setReinstatedList(null);
				caseDetailsNew.setIoList(null);
				caseDetailsNew.setPoList(null);
				caseDetailsNew.setProsecutionProposal(null);
				caseDetailsNew.setCourtCaseList(null);
				caseDetailsNew.setAbeyanceList(null);
				caseDetailsNew.setFinaloutList(null);
				
				
				caseDetailsNew = caseDetailsRepo.save(caseDetailsNew);
				
				System.out.println();
				
				List<EmployeeDetails> empDetailsOld = employeedetailsrepo.findByCasedetailsId(caseId);
				for(EmployeeDetails emp : empDetailsOld) {
					
					EmployeeDetails empNew = new EmployeeDetails();
					ChargesheetDetails chargeNew = new ChargesheetDetails();
					SuspensionDetails susNew =new SuspensionDetails();
					ReInstatedDetails reinNew = new ReInstatedDetails();
					ProsecutionProposalDetails prosNew = new ProsecutionProposalDetails();
					FinalOutcomeDetails finalNew = new FinalOutcomeDetails();

					BeanUtils.copyProperties(emp, empNew);
					empNew.setId(null);
					empNew.setCasedetails(caseDetailsNew);
					empNew.setCurrentUser(userList.get(0));
					empNew.setGlobalorg(org);
					if(emp.getChargesheetDetails() != null) {
						BeanUtils.copyProperties(emp.getChargesheetDetails(), chargeNew);
						chargeNew.setId(null);
						chargeNew.setCaseDetails(caseDetailsNew);
						chargeNew.setCurrentUser(userList.get(0));
						chargeNew.setGlobalorg(org);
						chargeNew.setEmpData(empNew);
						empNew.setChargesheetDetails(chargeNew);
					}
					
					if(emp.getSuspensionDetails() != null) {
						BeanUtils.copyProperties(emp.getSuspensionDetails(), susNew);
						susNew.setId(null);
						susNew.setCasedetails(caseDetailsNew);
						susNew.setCurrentUser(userList.get(0));
						susNew.setGlobalorg(org);
						susNew.setEmpData(empNew);
						empNew.setSuspensionDetails(susNew);
					}
					
					if(emp.getReInstatedDetails() != null) {
						BeanUtils.copyProperties(emp.getReInstatedDetails(), reinNew);
						reinNew.setId(null);
						reinNew.setCasedetails(caseDetailsNew);
						reinNew.setCurrentUser(userList.get(0));
						reinNew.setGlobalorg(org);
						reinNew.setEmpData(empNew);
						empNew.setReInstatedDetails(reinNew);
					}
					
					if(emp.getProsecutionProposalDetails() != null) {
						BeanUtils.copyProperties(emp.getProsecutionProposalDetails(), prosNew);
						prosNew.setId(null);
						prosNew.setCasedetails(caseDetailsNew);
						prosNew.setCurrentUser(userList.get(0));
						prosNew.setGlobalorg(org);
						prosNew.setEmpData(empNew);
						empNew.setProsecutionProposalDetails(prosNew);
					}
					
					if(emp.getFinalOutcomeDetails() != null) {
						BeanUtils.copyProperties(emp.getFinalOutcomeDetails(), finalNew);
						finalNew.setId(null);
						finalNew.setCasedetails(caseDetailsNew);
						finalNew.setCurrentUser(userList.get(0));
						finalNew.setGlobalorg(org);
						finalNew.setEmpData(empNew);
						empNew.setFinalOutcomeDetails(finalNew);
					}

					
					employeedetailsrepo.save(empNew);
				}
//				List<ChargesheetDetails> chargeSheetOld = chargesheetRepo.findByCaseDetailsId(caseId);
//				for(ChargesheetDetails charge : chargeSheetOld) {
//					
//				}
//				List<SuspensionDetails> susEmpOld = suspensionDetailsRepo.findByCasedetailsId(caseId);
//				List<ReInstatedDetails> reinsteOld = reinstatedRepository.findByCasedetailsId(caseId);
				InquiryOfficerDetails inqDetailsOld = inquiryofficerRepository.findByCasedetailsId(caseId);
				if(inqDetailsOld != null) {
					InquiryOfficerDetails inqNew = new InquiryOfficerDetails();
					BeanUtils.copyProperties(inqDetailsOld, inqNew);
					inqNew.setId(null);
					inqNew.setCasedetails(caseDetailsNew);
					inqNew.setCurrentUser(userList.get(0));
					inqNew.setGlobalorg(org);
					inquiryofficerRepository.save(inqNew);
				}
				
				PresentingOfficerDetails preseOld = ipresentingofficerrepository.findByCasedetailsId(caseId);
				if(preseOld != null) {
					PresentingOfficerDetails presenNew = new PresentingOfficerDetails();
					BeanUtils.copyProperties(preseOld, presenNew);
					presenNew.setId(null);
					presenNew.setCasedetails(caseDetailsNew);
					presenNew.setCurrentUser(userList.get(0));
					presenNew.setGlobalorg(org);
					ipresentingofficerrepository.save(presenNew);
				}
				
//				List<ProsecutionProposalDetails> proseOld = prosecutionRepo.findByCasedetailsId(caseId);
//				List<FinalOutcomeDetails> finalOld = finaloutcomeRepository.findByCasedetailsId(caseId);

			}
//			if(caseDetailsDto.getId() != null) {
//				CaseDetails caseDetailsOld = caseDetailsRepo.findById(caseDetailsDto.getId()).orElse(new CaseDetails());
//				long caseId = caseDetailsOld.getId();
//				BeanUtils.copyProperties(caseDetailsOld, caseDetailsNew);
//				System.out.println();
//				
////				List<EmployeeDetails> empDetailsOld = employeedetailsrepo.findByCasedetailsId(caseId);
////				List<ChargesheetDetails> chargeSheetOld = chargesheetRepo.findByCaseDetailsId(caseId);
////				List<SuspensionDetails> susEmpOld = suspensionDetailsRepo.findByCasedetailsId(caseId);
////				List<ReInstatedDetails> reinsteOld = reinstatedRepository.findByCasedetailsId(caseId);
////				InquiryOfficerDetails inqDetailsOld = inquiryofficerRepository.findByCasedetailsId(caseId);
////				PresentingOfficerDetails preseOld = ipresentingofficerrepository.findByCasedetailsId(caseId);
////				List<ProsecutionProposalDetails> proseOld = prosecutionRepo.findByCasedetailsId(caseId);
////				List<FinalOutcomeDetails> finalOld = finaloutcomeRepository.findByCasedetailsId(caseId);
//			}
		}

		catch (Exception e) {
			e.printStackTrace();
//s			redirect.addAttribute(model);
		}
//		return "redirect:/departmentalEnquiry/caseDetails";
		
//			return "redirect:/departmentalEnquiry/caseDetails";
//		ObjectNode obb = objectMapper.createObjectNode();
//		obb.put("message", "Successfully transfered case to ");
//		obb.put("disabledUpdate", true);
		return ResponseEntity.ok("Successfully transfered case to "+org.getGlobalOrgName());
		
	}

	/*
	 * landing page of case details data Description : Inthis method landing Case
	 * Details Page list..
	 */

	@GetMapping("/caseDtlsRcdsInboxlst/{page}")
	public String caseDetailsListRecords(Model model, @PathVariable("page") int page,
			@RequestParam(name = "caseno", required = false) String caseno,
			@RequestParam(name = "caseid", required = false) String caseid,
			@RequestParam(name = "viewreport", required = false) String viewreport,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		LOG.info("hi this is the case details records page.... ");

		if (sortDirection == null) {
			sortDirection = "ASC";
		}
		if (sortField == null) {
			sortField = "caseNo";
		}
		if(caseid != null) {
			
		} else {
			caseid= "";
		}
		model.addAttribute("caseid", caseid);
		if(caseno != null) {
			
		} else {
			caseno= "";
		}
		model.addAttribute("caseno", caseno);

		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDirection);
		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

//		if(caseno != null) {
//			model.addAttribute("caseDetailsAllRecords", caseDetailsRepo.);
//		} else {
//			model.addAttribute("caseDetailsAllRecords", caseDetailsRepo.findAll());
//		}

		// pagination Writting coding
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {
			page = 1;
		}

//		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		List<CaseDetails> casesTransferList = caseDetailsRepo.findByActiveForTransfer(true);
		List<Long> caseIdList = new ArrayList<>();
		caseIdList = casesTransferList.parallelStream().map(s->s.getId()).collect(Collectors.toList());
		if(caseIdList.isEmpty()) {
			caseIdList.add(0L);
		}
		Page<CaseDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if (caseno != null) {
				model.addAttribute("caseno", caseno);
				noticePages = caseDetailsRepo.findByCaseNoContainingIgnoreCaseAndIdNotIn(caseno,caseIdList, pageable);
			} else if(caseid != null && StringUtils.isNotBlank(caseid) && !caseid.equalsIgnoreCase("null")){
				noticePages = caseDetailsRepo.findById(Long.valueOf(caseid),pageable);
				model.addAttribute("caseid", caseid);
			} else {
				noticePages = caseDetailsRepo.findByIdNotIn(caseIdList,pageable);
				model.addAttribute("caseno", "");
			}
		} // if close
		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
			List<SubDepartment> subDepartmentList = subDepartmentRepo
					.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
//			List<User> userList = userRepo.findBySubDepartmentIn(subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
//			userList.add(currentUser);
			noticePages = caseDetailsRepo
					.findByCurrentUserSubDepartmentInOrCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentIsNullAndIdNotIn(
							subDepartmentList, currentUser.getPimsEmployee().getGlobalOrgId(),caseIdList, pageable);
			model.addAttribute("caseno", "");
		} else {
			if (caseno != null && StringUtils.isNotBlank(caseno)) {
				model.addAttribute("caseno", caseno);
				noticePages = caseDetailsRepo.findByCurrentUserAndCaseNoContainingIgnoreCaseAndIdNotIn(
						currentUser, caseno,caseIdList, pageable);
			} else if(caseid != null && StringUtils.isNotBlank(caseid) && !caseid.equalsIgnoreCase("null")){
				noticePages = caseDetailsRepo.findByCurrentUserAndId(
						currentUser,Long.valueOf(caseid), pageable);
				model.addAttribute("caseid", caseid);
			}else {
				noticePages = caseDetailsRepo.findByCurrentUserAndIdNotIn(
						currentUser,caseIdList, pageable);
				model.addAttribute("caseno", "");
			}
		} // else close

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

		return "DeptInquiryViews/inbox";

	}

	/* Employee landing page methods */

	@GetMapping("/deemployeedetails/{page}")
	public String employeeDetails(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		LOG.info("Hi this is the employee details page method......");

		model.addAttribute("gender", genderRepo.findByActiveOrderById(true));
		model.addAttribute("serviceGroup", serviceGrpRepo.findByOrderById());
		 model.addAttribute("designation", designationRepo.findAll()); 
	
		model.addAttribute("paycommission", PayCommissionRepo.findAll());
		model.addAttribute("payband", paybandRepo.findAll());
		model.addAttribute("gradepay", gradepayRepo.findAll());
//		model.addAttribute("rulesapplicable", rulesApplicableMasterRepo.findAll());
		model.addAttribute("rulesapplicable",rulesApplicableMasterRepo.findByActive(true));
		/* model.addAttribute("globalOrgList1", globalorgRepo.findAll()); */
		model.addAttribute("subDepartmentlist", subDepartmentRepo.findAll());

		// added division as per latest pdf
		/* model.addAttribute("divisionRepolist", divisionRepository.findAll()); */
		// this code for employee list Grid
//		model.addAttribute("employeeList", employeedetailsrepo.findAll());

		EmployeeDetails employeeDetails = new EmployeeDetails();
		model.addAttribute("employeeDetails", employeeDetails);

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);

		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

		// Page<EmployeeDetails> noticePages = employeedetailsrepo.findAll(pageable);

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<EmployeeDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = employeedetailsrepo.findAll(pageable);
		}else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
	//		noticePages = employeedetailsrepo.findByCurrentUserPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId(), pageable);
		} // if close
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			noticePages = employeedetailsrepo.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = employeedetailsrepo.findByCurrentUser(currentUser, pageable);
		} // else close

		model.addAttribute("employeeList", noticePages);
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

	
		/* Fetching Records of while Session DE Cases back log Grids code part */

		try {
			Long caseNo = (Long) request.getSession().getAttribute("caseid");
			if (caseNo != null) {

				// model.addAttribute("employeeList",
//		  employeedetailsrepo.findByCasedetailsId(caseNo);
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseNo)).orElse(new CaseDetails());
				model.addAttribute("cases", cd);
				if(cd != null) {
					if(cd.getIoList().isEmpty()) {
						model.addAttribute("disablepresentingofficer", true);
					} 
					if(cd.getMisConductType() != null && (cd.getMisConductType().getId()==1 || cd.getMisConductType().getId()==1)) {
						model.addAttribute("disableprosecution", true);
					} 
					
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
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					} else {
						if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered", employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						} else {
							model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
							model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
						}
					}
					
				}
				List<EmployeeDetails> emp = employeedetailsrepo.findByCasedetailsId(caseNo);
				if (!emp.isEmpty()) {
					emp.get(0).setCaseNo(emp.get(0).getCasedetails().getCaseNo());
					model.addAttribute("employeeDetails", emp.get(0));
				} else {
					employeeDetails = new EmployeeDetails();
					employeeDetails.setCaseNo(caseDetailsRepo.findById(caseNo).orElse(new CaseDetails()).getCaseNo());
					model.addAttribute("employeeDetails", employeeDetails);
				}
				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				// noticePages = employeedetailsrepo.findByCasedetailsId(caseNo, pageable);
				// added on 29/09/2022 , depend on employee and department we are getting grid
				// list values....
//				User currentUser  = userService.getCurrentUserPojo();
//				Page<EmployeeDetails> noticePages = 	null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = employeedetailsrepo.findByCasedetailsId(caseNo, pageable);
				} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SECRETARYOLD")) {
					
//					noticePages = employeedetailsrepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(currentUser.getPimsEmployee().getGlobalOrgId(),caseNo, pageable);
				}// if close
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
					noticePages = employeedetailsrepo.findByCurrentUserInAndCasedetailsId(userList, caseNo, pageable);
				}
				else {
					noticePages = employeedetailsrepo.findByCurrentUserAndCasedetailsId(currentUser, caseNo, pageable);
				} // else close

				model.addAttribute("employeeList", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("noticelist", noticePages.getContent());

			} else {
//				model.addAttribute("employeeList", employeedetailsrepo.findAll());
			}
		}

		catch (Exception a) {
			a.printStackTrace();
			model.addAttribute("employeeList", employeedetailsrepo.findAll());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/delinquentEmployeesDetails";
	}

	/*
	 * Method :Employee Details save method.. Description : this method Saving
	 * employee data on database...
	 */
	@PostMapping("/addemployeedetails")
	public String addemployeedetails(@ModelAttribute("employeeDetails") EmployeeDetailsDto employeedetailsdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "action", required = false) String action) {

		EmployeeDetails employeedetails = new EmployeeDetails();
		User currentUser = userService.getCurrentUserPojo();

		try {
			if (employeedetailsdto.getId() != null && employeedetailsdto.getId() > 0) {
				LOG.info("Hi this is the employee details update Existing Records  Methods .....");

				employeedetails = employeedetailsrepo.findById(employeedetailsdto.getId())
						.orElse(new EmployeeDetails());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(employeedetailsdto.getCaseNo(),currentUser)) {
					employeedetails.setCasedetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(employeedetailsdto.getCaseNo(),currentUser));
				}

				if (employeedetailsdto.getGender() != null) {
					employeedetails.setGender(
							genderRepo.findById(employeedetailsdto.getGender().getId()).orElse(new Gender()));
				}

				else {
					employeedetails.setGender(null);
				}

				if (employeedetailsdto.getGlobalorg() != null) {
					employeedetails.setGlobalorg(
							globalorgRepo.findById(employeedetailsdto.getGlobalorg().getId()).orElse(new GlobalOrg()));
				} else {
					employeedetails.setGlobalorg(null);
				}

				if (employeedetailsdto.getServicegroup() != null) {
					employeedetails.setServicegroup(serviceGrpRepo
							.findById(employeedetailsdto.getServicegroup().getId()).orElse(new Service_Group()));
				}

				else {
					employeedetails.setServicegroup(null);
				}

				if (employeedetailsdto.getDesignation() != null) {
					employeedetails.setDesignation(
							designationRepo.findById(employeedetailsdto.getDesignation().getDesignation_id())
									.orElse(new Designation()));
				}

				else {
					employeedetails.setDesignation(null);
				}
				
				
				if (employeedetailsdto.getRuleApplicable() != null) {
					
					employeedetails.setRuleApplicable(rulesApplicableMasterRepo.findById(employeedetailsdto.getRuleApplicable().getId()).orElse(new RulesApplicableMaster()));
				}

				else {
					employeedetails.setRuleApplicable(null);
				}

				employeedetails.setOfficeName(employeedetailsdto.getOfficeName());
				employeedetails.setSubDepartment(employeedetailsdto.getSubDepartment());
				employeedetails.setSevarthId(employeedetailsdto.getSevarthId());
				employeedetails.setEmployeeId(employeedetailsdto.getEmployeeId());
				employeedetails.setFirstName(employeedetailsdto.getFirstName());
				employeedetails.setMiddleName(employeedetailsdto.getMiddleName());
				employeedetails.setLastName(employeedetailsdto.getLastName());
				employeedetails.setEmail(employeedetailsdto.getEmail());
				employeedetails.setMobileNumber(employeedetailsdto.getMobileNumber());
				
				if (employeedetailsdto.getLocaldate() != null) {
					employeedetails.setLocaldate(employeedetailsdto.getLocaldate());
				}
				if (employeedetailsdto.getJoinCurrentOrgdate() != null) {
					employeedetails.setJoinCurrentOrgdate(employeedetailsdto.getJoinCurrentOrgdate());
				}
				if (employeedetailsdto.getSuperannuationDate() != null) {
					employeedetails.setSuperannuationDate(employeedetailsdto.getSuperannuationDate());
				}
				if (employeedetailsdto.getRevenueDivision() != null) {
					employeedetails.setRevenueDivision(divisionRepository
							.findById(employeedetailsdto.getRevenueDivision().getId()).orElse(new Division()));
				}

				else {
					employeedetails.setRevenueDivision(null);
				}

//				employeedetails.setJoinCurrentOrgdate(employeedetailsdto.getJoinCurrentOrgdate());
//				employeedetails.setSuperannuationDate(employeedetailsdto.getSuperannuationDate());
//				
//				employeedetails.setSubDepartment(subDepartmentRepo
//						.findById(employeedetailsdto.getSubDepartment().getId()).orElse(new SubDepartment()));
//				
//				employeedetails.setServicegroup(serviceGrpRepo.findById(employeedetailsdto.getServicegroup().getId())
//						.orElse(new Service_Group()));

//				if() {
//					
//				}
//				employeedetails.setRevenueDivision(divisionRepository
//						.findById(employeedetailsdto.getRevenueDivision().getId()).orElse(new Division()));
//				
//
//				employeedetails.setDesignation(designationRepo
//						.findById(employeedetailsdto.getDesignation().getDesignation_id()).orElse(new Designation()));
				employeedetails.setActive(true);

				employeedetails = employeedetailsrepo.save(employeedetails);
				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editEmployeeDetails/"+employeedetails.getId()+"/1";
			}

			else {

				LOG.info("Hi this is the employee details save methods .....");

//				employeedetails.setCasedetails(caseDetailsRepo.findByCaseNoContainingIgnoreCase(employeedetailsdto.getCaseNo()));
//
//				if (employeedetailsrepo.existsByCasedetails(employeedetails.getCasedetails())) {
//					employeedetails = employeedetailsrepo.findByCasedetails(employeedetails.getCasedetails());
//				}

				// employeedetails.setCasedetails(caseDetailsRepo.findById(employeedetailsdto.getCasedetails().getId()).orElse(new
				// CaseDetails()));
				// employeedetails.setCaseNo(employeedetails.getCasedetails().getCaseNo());
				// employeedetails.setCasedetails(employeedetails.getCasedetails().getCaseNo());

				// save

				CaseDetails cd = caseDetailsRepo.findByCaseNo(employeedetailsdto.getCaseNo());
				if(cd != null  && Integer.valueOf(cd.getTotalNoOfEmployee()) == cd.getEmployeeList().size()) {
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							"All the employees have been already entered");
					return "redirect:/departmentalEnquiry/deemployeedetails/1";
				}

				if (cd != null) {
					employeedetails.setCasedetails(cd);
				} else {
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
					return "redirect:/departmentalEnquiry/deemployeedetails/1";
				}

				if (StringUtils.isNotBlank(employeedetailsdto.getSevarthId())
						&& employeedetailsrepo.existsByCasedetailsAndSevarthId(employeedetails.getCasedetails(),
								employeedetailsdto.getSevarthId())) {
					employeedetails = employeedetailsrepo.findByCasedetailsAndSevarthId(
							employeedetails.getCasedetails(), employeedetailsdto.getSevarthId());
					/*
					 * redirect.addFlashAttribute(departmentconstants.MESSAGE,
					 * messageSource.getMessage("dept.inq.sevarthId.exist", null,
					 * loc.resolveLocale(request))); return
					 * "redirect:/departmentalEnquiry/deemployeedetails/1";
					 */

				}

				/*
				 * else {
				 * 
				 * 
				 * }
				 */

//				if(action != null && action.equalsIgnoreCase("addmore")) {
//					if(employeedetailsrepo.existsByCasedetailsAndSevarthId(employeedetails.getCasedetails(),employeedetailsdto.getSevarthId())) {
//				    	employeedetails = employeedetailsrepo.findByCasedetailsAndSevarthId(employeedetails.getCasedetails(),employeedetailsdto.getSevarthId());
//					    	
//						}
//				}

				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					employeedetails.setCurrentUser(currentUser);
					employeedetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}

				if (employeedetailsdto.getGender() != null) {
					employeedetails.setGender(
							genderRepo.findById(employeedetailsdto.getGender().getId()).orElse(new Gender()));
				}

				else {
					employeedetails.setGender(null);
				}

				if (employeedetailsdto.getGlobalorg() != null) {
					employeedetails.setGlobalorg(
							globalorgRepo.findById(employeedetailsdto.getGlobalorg().getId()).orElse(new GlobalOrg()));
				}

				else {
					employeedetails.setGlobalorg(null);
				}

				if (employeedetailsdto.getServicegroup() != null) {
					employeedetails.setServicegroup(serviceGrpRepo
							.findById(employeedetailsdto.getServicegroup().getId()).orElse(new Service_Group()));
				}

				else {
					employeedetails.setServicegroup(null);
				}

				if (employeedetailsdto.getDesignation() != null) {
					employeedetails.setDesignation(
							designationRepo.findById(employeedetailsdto.getDesignation().getDesignation_id())
									.orElse(new Designation()));
				}

				else {
					employeedetails.setDesignation(null);
				}
				
				
				
                      if (employeedetailsdto.getRuleApplicable() != null) {
					
					employeedetails.setRuleApplicable(rulesApplicableMasterRepo.findById(employeedetailsdto.getRuleApplicable().getId()).orElse(new RulesApplicableMaster()));
				}
				else {
					employeedetails.setRuleApplicable(null);
				}
                      
				employeedetails.setOfficeName(employeedetailsdto.getOfficeName());

				/*
				 * employeedetails.setPaycommission(
				 * PayCommissionRepo.findById(employeedetailsdto.getPaycommission().
				 * getPayCommisisonId()) .orElse(new PayCommission()));
				 */

				/*
				 * employeedetails.setPayband(
				 * paybandRepo.findById(employeedetailsdto.getPayband().getId()).orElse(new
				 * Payband()));
				 */
				/*
				 * employeedetails.setGradepay(
				 * gradepayRepo.findById(employeedetailsdto.getGradepay().getGradPayId()).orElse
				 * (new GradePay()));
				 */
				// employeedetails.setCaseNo(employeedetailsdto.getCasedetails().getCaseNo());
				employeedetails.setSubDepartment(employeedetailsdto.getSubDepartment());
				employeedetails.setSevarthId(employeedetailsdto.getSevarthId());
				employeedetails.setEmployeeId(employeedetailsdto.getEmployeeId());
				employeedetails.setFirstName(employeedetailsdto.getFirstName());
				employeedetails.setMiddleName(employeedetailsdto.getMiddleName());
				employeedetails.setLastName(employeedetailsdto.getLastName());
				employeedetails.setEmail(employeedetailsdto.getEmail());
				employeedetails.setMobileNumber(employeedetailsdto.getMobileNumber());
				// setting up date fields
				if (employeedetailsdto.getLocaldate() != null) {
					employeedetails.setLocaldate(employeedetailsdto.getLocaldate());
				}
				if (employeedetailsdto.getJoinCurrentOrgdate() != null) {
					employeedetails.setJoinCurrentOrgdate(employeedetailsdto.getJoinCurrentOrgdate());
				}
				if (employeedetailsdto.getSuperannuationDate() != null) {
					employeedetails.setSuperannuationDate(employeedetailsdto.getSuperannuationDate());
				}
				if (employeedetailsdto.getRevenueDivision() != null) {
					employeedetails.setRevenueDivision(divisionRepository
							.findById(employeedetailsdto.getRevenueDivision().getId()).orElse(new Division()));
				}

				else {
					employeedetails.setRevenueDivision(null);
				}
				
				if(currentUser.getSubDepartment() != null) {
					employeedetails.setSubDepartments(currentUser.getSubDepartment());
				}

				employeedetails.setActive(true);

				employeedetails = employeedetailsrepo.save(employeedetails);
				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editEmployeeDetails/"+employeedetails.getId()+"/1";
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
			e.printStackTrace();
		}
		//model.addAttribute( "confirmMsg", messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request) ) );)
		return "redirect:/departmentalEnquiry/deemployeedetails/1";
	}

	/*
	 * Method :Chargesheet details landing method.. Description : written landing
	 * form logic
	 */

	@GetMapping("/chargesheetDetails/{page}")
	public String ChargesheetLanding(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {

		LOG.info("this is the chargesheetDetails landing form methods ...");

		model.addAttribute("chargeSheetObj", new ChargesheetDetails());

//		model.addAttribute("FirstName", employeedetailsrepo.findAll());

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		Page<ChargesheetDetails> noticePages = null;
		User currentUser = userService.getCurrentUserPojo();
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = chargesheetRepo.findAll(pageable);
		}  else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
//			noticePages = chargesheetRepo.findByCurrentUserPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId(), pageable);
		}
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			noticePages = chargesheetRepo.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = chargesheetRepo.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("chargesheetList", noticePages);
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
		/*
		 * try { Long caseid = (Long) request.getSession().getAttribute("caseid");
		 * LOG.info(caseid); if (caseid != null) {
		 * 
		 * model.addAttribute("chargeSheetObj",
		 * chargesheetRepo.findByCaseDetailsId(caseid));
		 * 
		 * } else { model.addAttribute("chargeSheetObj", new ChargesheetDetails()); } }
		 * catch (Exception a) { a.printStackTrace();
		 * model.addAttribute("chargeSheetObj", new ChargesheetDetails()); }
		 */

		/* Fetching Records of while DE Cases back log Grids code part */

		try {
			Long caseNo = (Long) request.getSession().getAttribute("caseid");
			if (caseNo != null) {

				// model.addAttribute("employeeList",
//		  employeedetailsrepo.findByCasedetailsId(caseNo);
//				EmployeeDetails emp = employeedetailsrepo.findByCasedetailsId(caseNo);
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseNo)).orElse(new CaseDetails());
				model.addAttribute("cases", cd);
				if(cd != null) {
					if(cd.getIoList().isEmpty()) {
						model.addAttribute("disablepresentingofficer", true);
					} 
					if(cd.getMisConductType() != null && (cd.getMisConductType().getId()==1 || cd.getMisConductType().getId()==1)) {
						model.addAttribute("disableprosecution", true);
					}
					
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
				}
				List<ChargesheetDetails> chargesheet = chargesheetRepo.findByCaseDetailsId(caseNo);

				if (chargesheet != null && !chargesheet.isEmpty()) {
					ChargesheetDetails charge = chargesheet.get(0);
					CaseDetails caseDetails = charge.getCaseDetails();
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						charge.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						charge.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						charge.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						charge.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						charge.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					charge.setCaseNo(chargesheet.get(0).getCaseDetails().getCaseNo());
					model.addAttribute("chargeSheetObj", charge);
					if(!charge.getCaseDetails().getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : charge.getCaseDetails().getEmployeeList()) {
							if(emp.getFirstName().equalsIgnoreCase(charge.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(charge.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(charge.getLastName())) {
								charge.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", charge.getCaseDetails().getEmployeeList());
					}	
					
				}

				else {
					ChargesheetDetails charge = new ChargesheetDetails();
					CaseDetails caseDetails = caseDetailsRepo.findById(caseNo).orElse(new CaseDetails());
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						charge.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						charge.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						charge.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						charge.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						charge.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					charge.setCaseNo(caseDetails.getCaseNo());
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(charge.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(charge.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(charge.getLastName())) {
								charge.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}
					charge.setCaseNo(caseDetails.getCaseNo());

					model.addAttribute("chargeSheetObj", charge);
				}

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = chargesheetRepo.findByCaseDetailsId(caseNo, pageable);
				} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SECRETARYOLD")) {
					
		//			noticePages = chargesheetRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCaseDetailsId(currentUser.getPimsEmployee().getGlobalOrgId(),caseNo, pageable);
				}
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
					noticePages = chargesheetRepo.findByCaseDetailsIdAndCurrentUserIn(caseNo, userList, pageable);
				}
				else {
					noticePages = chargesheetRepo.findByCaseDetailsIdAndCurrentUser(caseNo, currentUser, pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("chargesheetList", noticePages.getContent());

			} else {
//				model.addAttribute("chargesheetList", chargesheetRepo.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("chargesheetList", new ArrayList<>());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/chargeSheetDetails";
	}

	/*
	 * Method :Chargesheet details saving method.. Description: written saving form
	 * logic code
	 */

	@PostMapping("/sendEmail")
	public ResponseEntity<String> sendEmail(@ModelAttribute("mailObj")  @Valid MailDTO mailDTO) {
		
		long id = mailDTO.getChargesheetDetailId();
		ChargesheetDetails chargesheetDetails = chargesheetRepo.findById(id).orElse(new ChargesheetDetails());
		
		MailTransaction mailTransaction = new MailTransaction();
		
		mailTransaction.setMailTo(mailDTO.getMailTo());
		mailTransaction.setMailSubject(mailDTO.getMailSubject());
		mailTransaction.setMailBody(mailDTO.getMailBody());	
		
		mailTransaction.setFileName(chargesheetDetails.getFileName());
		mailTransaction.setFilePath(chargesheetDetails.getFilepath());
		mailTransaction.setContentType(chargesheetDetails.getContentType());
		
		mailTransaction.setSentDate(LocalDateTime.now());
		
		mailTransaction.setChargesheetDetails(chargesheetDetails);
		
		mailTransactionRepo.save(mailTransaction);
		
		Map<String, FileData> mapFiles = new HashMap<>();
		
		FileData fileData = new FileData();
		
		fileData.setContentType(chargesheetDetails.getContentType());		
		fileData.setFileName(chargesheetDetails.getFileName());
				
		ByteArrayResource file =fileService.getFile(chargesheetDetails.getFilepath(), chargesheetDetails.getFileName());
		
		fileData.setData(file.getByteArray());

		mapFiles.put(chargesheetDetails.getFileName(), fileData);
		
		System.out.println("Started Mail");
		
		mailService.sendEmailMultipleFiles(null, mailTransaction.getMailTo(), null, mailTransaction.getMailSubject(), mailTransaction.getMailBody(), mapFiles);
		
		System.out.println("Sent Mail");
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@PostMapping("/addchargeSheetdetails")
	public String addchargeSheetdetails(@ModelAttribute("chargeSheetObj") ChargesheetDto chargesheetDtlDto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "fileName", required = false) MultipartFile file,
			@RequestParam(name = "revisedfileName", required = false) MultipartFile revisedfile) {

		// creating new objects of chargesheet details
		ChargesheetDetails chargesheetdetails = new ChargesheetDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (chargesheetDtlDto.getId() != null && chargesheetDtlDto.getId() > 0) {

				chargesheetdetails = chargesheetRepo.findById(chargesheetDtlDto.getId())
						.orElse(new ChargesheetDetails());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(chargesheetDtlDto.getCaseNo(),currentUser)) {
					chargesheetdetails.setCaseDetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(chargesheetDtlDto.getCaseNo(),currentUser));
				}
				
				if(StringUtils.isNotBlank(chargesheetDtlDto.getEmployeeDataId())) {
					if(chargesheetRepo.existsByCaseDetailsAndEmpDataId(chargesheetdetails.getCaseDetails(), Long.valueOf(chargesheetDtlDto.getEmployeeDataId()))) {
						chargesheetdetails = chargesheetRepo.findByCaseDetailsAndEmpDataId(chargesheetdetails.getCaseDetails(), Long.valueOf(chargesheetDtlDto.getEmployeeDataId()));
					} else {
						chargesheetdetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(chargesheetDtlDto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

				// chargesheetdetails.setCaseDetails(caseDetailsRepo.findById(chargesheetDtlDto.getCaseDetails().getId()).orElse(new
				// CaseDetails()));

				/* added on 18/10/2022 as per de requirements */
				/*
				 * if (chargesheetDtlDto.getFileName() != null) {
				 * chargesheetdetails.setServicegroup(serviceGrpRepo
				 * .findById(employeedetailsdto.getServicegroup().getId()).orElse(new
				 * Service_Group())); }
				 */
				chargesheetdetails.setActive(true);
				chargesheetdetails.setChargesheetDate(chargesheetDtlDto.getChargesheetDate());
				chargesheetdetails.setChargesheetIssued(chargesheetDtlDto.getChargesheetIssued());

				
				/* chargesheetdetails.setEmployeeId(chargesheetDtlDto.getEmployeeId()); */
				chargesheetdetails.setSevarthId(chargesheetDtlDto.getSevarthId());
				chargesheetdetails.setFirstName(chargesheetDtlDto.getFirstName());
				chargesheetdetails.setMiddleName(chargesheetDtlDto.getMiddleName());
				chargesheetdetails.setLastName(chargesheetDtlDto.getLastName());
				chargesheetdetails.setRevisedChargesheetIssued(chargesheetDtlDto.isRevisedChargesheetIssued());
				chargesheetdetails.setRevisedChargesheetDate(chargesheetDtlDto.getRevisedChargesheetDate());
				if(!chargesheetDtlDto.isRevisedChargesheetIssued()) {
					chargesheetdetails.setRevisedfileName(null);
					chargesheetdetails.setRevisedcontentType(null);
					chargesheetdetails.setRevisedfilepath(null);
				}
				if (!file.isEmpty() && file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ chargesheetdetails.getCaseDetails().getId() + "/" + "chargetsheetdetails/"
							+ chargesheetdetails.getId() + "/";
					chargesheetdetails.getCaseDetails().setPendingWith("Pending with Department before appointing Inquiry Officer");
					chargesheetdetails.setFileName(file.getOriginalFilename());
					chargesheetdetails.setContentType(file.getContentType());
					chargesheetdetails.setFilepath(filePath);

					fileService.fileUpload(filePath, file, chargesheetdetails.getFileName());

				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				if (!revisedfile.isEmpty() && revisedfile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ chargesheetdetails.getCaseDetails().getId() + "/" + "chargetsheetdetails/"
							+ chargesheetdetails.getId() + "/revisedfile/";

					chargesheetdetails.setRevisedfileName(revisedfile.getOriginalFilename());
					chargesheetdetails.setRevisedcontentType(revisedfile.getContentType());
					chargesheetdetails.setRevisedfilepath(filePath);

					fileService.fileUpload(filePath, revisedfile, chargesheetdetails.getRevisedfileName());

				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				
				
				chargesheetdetails = chargesheetRepo.save(chargesheetdetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editchargesheetDtlsRecords/"+chargesheetdetails.getId()+"/1";
			}

			else {

				LOG.info("Chargesheet saving Methods....");
				
				
				chargesheetdetails.setSevarthId(chargesheetDtlDto.getSevarthId());
				chargesheetdetails.setFirstName(chargesheetDtlDto.getFirstName());
				chargesheetdetails.setMiddleName(chargesheetDtlDto.getMiddleName());
				chargesheetdetails.setLastName(chargesheetDtlDto.getLastName());

				CaseDetails cd = caseDetailsRepo.findByCaseNo(chargesheetDtlDto.getCaseNo());
				
				

				if (cd != null) {
					chargesheetdetails.setCaseDetails(cd);
				} else {
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
					return "redirect:/departmentalEnquiry/chargesheetDetails/1";
				}

//				if (StringUtils.isNotBlank(chargesheetDtlDto.getSevarthId())
//						&& chargesheetRepo.existsByCaseDetailsAndSevarthId(cd, chargesheetDtlDto.getSevarthId())) {
//					chargesheetdetails = chargesheetRepo.findByCaseDetailsAndSevarthId(cd,
//							chargesheetDtlDto.getSevarthId());
//				}
				
				if(StringUtils.isNotBlank(chargesheetDtlDto.getEmployeeDataId())) {
					if(chargesheetRepo.existsByCaseDetailsAndEmpDataId(chargesheetdetails.getCaseDetails(), Long.valueOf(chargesheetDtlDto.getEmployeeDataId()))) {
						chargesheetdetails = chargesheetRepo.findByCaseDetailsAndEmpDataId(chargesheetdetails.getCaseDetails(), Long.valueOf(chargesheetDtlDto.getEmployeeDataId()));
					} else {
						chargesheetdetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(chargesheetDtlDto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}
				

				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					chargesheetdetails.setCurrentUser(currentUser);
					chargesheetdetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}

				// chargesheetdetails.setChargesheetDate(chargesheetDtlDto.getChargesheetDate());
				chargesheetdetails.setChargesheetDate(chargesheetDtlDto.getChargesheetDate());
				chargesheetdetails.setChargesheetIssued(chargesheetDtlDto.getChargesheetIssued());
				chargesheetdetails.setRevisedChargesheetIssued(chargesheetDtlDto.isRevisedChargesheetIssued());
				chargesheetdetails.setRevisedChargesheetDate(chargesheetDtlDto.getRevisedChargesheetDate());
				if(!chargesheetDtlDto.isRevisedChargesheetIssued()) {
					chargesheetdetails.setRevisedfileName(null);
					chargesheetdetails.setRevisedcontentType(null);
					chargesheetdetails.setRevisedfilepath(null);
				}
				
				if (file != null) {
					if (!file.isEmpty() && file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
						String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
								+ chargesheetdetails.getCaseDetails().getId() + "/" + "chargetsheetdetails/"
								+ chargesheetdetails.getId() + "/";
						chargesheetdetails.setFileName(file.getOriginalFilename());
						chargesheetdetails.setContentType(file.getContentType());
						cd.setPendingWith("Pending with Department before appointing Inquiry Officer");
						caseDetailsRepo.save(cd);
						chargesheetdetails.setFilepath(filePath);
						fileService.fileUpload(filePath, file, chargesheetdetails.getFileName());
						chargesheetRepo.save(chargesheetdetails);
					}

					else {
						redirect.addFlashAttribute("error", "Please select correct pdf file only..");
					}
				}
				
				if (!revisedfile.isEmpty() && revisedfile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ chargesheetdetails.getCaseDetails().getId() + "/" + "chargetsheetdetails/"
							+ chargesheetdetails.getId() + "/revisedfile/";

					chargesheetdetails.setRevisedfileName(revisedfile.getOriginalFilename());
					chargesheetdetails.setRevisedcontentType(revisedfile.getContentType());
					chargesheetdetails.setRevisedfilepath(filePath);

					fileService.fileUpload(filePath, revisedfile, chargesheetdetails.getRevisedfileName());

				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				chargesheetdetails = chargesheetRepo.save(chargesheetdetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editchargesheetDtlsRecords/"+chargesheetdetails.getId()+"/1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}
		return "redirect:/departmentalEnquiry/chargesheetDetails/1";
	}

	/*
	 * Method :getcaseDetailsJson Description : fetching data in Get Case Details
	 * Json
	 */
	@GetMapping("/getchargesheetDetailsJson")
	@ResponseBody
	public ChargesheetDetails getChargesheetDetailsJson(
			@RequestParam(name = "caseid", required = false) String caseid) {
		if (caseid != null) {
			return chargesheetRepo
					.findByCaseDetails(caseDetailsRepo.findByCaseNoContainingIgnoreCase(caseid.toString()).get(0))
					.get(0);
		}
		return null;
	}

	@GetMapping("/downloadDoc/{id}")
	public ResponseEntity<ByteArrayResource> getFile(@PathVariable String id) {
		ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + "file.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(charge.getFilepath(), charge.getFileName()));
	}
	
	@GetMapping("/downloadDocChargesheet2/{id}")
	public ResponseEntity<ByteArrayResource> getFileChargesheet2(@PathVariable String id) {
		ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + "file2.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(charge.getRevisedfilepath(), charge.getRevisedfileName()));
	}

	/*
	 * Method :Suspension details landing method. Description : written landing form
	 * logic
	 */

	@GetMapping("/suspensionDetails/{page}")
	public String suspensionDetails(Model model, @PathVariable("page") int page) {
		LOG.info("this is the suspensionDetails landing form methods ...");
		model.addAttribute("suspensionRules", suspensionRuleRepo.findAll(Sort.by("order")));
		model.addAttribute("suspensionObj", new SuspensionDetails());

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<SuspensionDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = suspensionDetailsRepo.findAll(pageable);
		}  else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
	//		noticePages = suspensionDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId(), pageable);
		}
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			noticePages = suspensionDetailsRepo.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = suspensionDetailsRepo.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("suspensionEmployeeList", noticePages);
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

		/*
		 * try { Long caseid = (Long) request.getSession().getAttribute("caseid");
		 * LOG.info(caseid); if (caseid != null) {
		 * 
		 * model.addAttribute("suspensionObj",
		 * suspensionDetailsRepo.findByCasedetailsId(caseid));
		 * 
		 * } else { model.addAttribute("suspensionObj", new SuspensionDetails()); } }
		 * catch (Exception a) { a.printStackTrace();
		 * model.addAttribute("suspensionObj", new SuspensionDetails()); }
		 */

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
			System.out.println(caseid);
			if (caseid != null) {
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseid)).orElse(new CaseDetails());
				model.addAttribute("cases", cd);
				if(cd != null) {
					if(cd.getIoList().isEmpty()) {
						model.addAttribute("disablepresentingofficer", true);
					} 
					if(cd.getMisConductType() != null && (cd.getMisConductType().getId()==1 || cd.getMisConductType().getId()==1)) {
						model.addAttribute("disableprosecution", true);
					}
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
					
				}
				// model.addAttribute("employeeList",
				// employeedetailsrepo.findByCasedetailsId(caseNo);
				// EmployeeDetails emp = employeedetailsrepo.findByCasedetailsId(caseNo);
				// ChargesheetDetails chargesheet=chargesheetRepo.findByCaseDetailsId(caseid);

				/*------------------------------------------------------------------------------*/

//	List<ChargesheetDetails> chargesheet = chargesheetRepo.findByCaseDetailsId(caseNo);
//				
//				if (chargesheet != null && !chargesheet.isEmpty()) {
//					ChargesheetDetails charge = chargesheet.get(0);
//					CaseDetails caseDetails = charge.getCaseDetails();
//					if(caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						charge.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						charge.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						charge.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						charge.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						charge.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
//					}
//					charge.setCaseNo(chargesheet.get(0).getCaseDetails().getCaseNo());
//					model.addAttribute("chargeSheetObj", charge);
//				} 
//				
//				else {
//					ChargesheetDetails charge = new ChargesheetDetails();
//					CaseDetails caseDetails = caseDetailsRepo.findById(caseNo).orElse(new CaseDetails());
//					if(caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						charge.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						charge.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						charge.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						charge.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						charge.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
//					}
//					charge.setCaseNo(caseDetails.getCaseNo());
//					
//					
//					model.addAttribute("chargeSheetObj", charge);
//				}
//
//				
//				

				/*------------------------------------------------------------------------------*/

				List<SuspensionDetails> susdetails = suspensionDetailsRepo.findByCasedetailsId(caseid);
				if (susdetails != null && !susdetails.isEmpty()) {
					SuspensionDetails suspensionobj1 = susdetails.get(0);
					CaseDetails caseDetails = suspensionobj1.getCasedetails();

					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						suspensionobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						suspensionobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						suspensionobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						suspensionobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						suspensionobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					suspensionobj1.setCaseNo(susdetails.get(0).getCasedetails().getCaseNo());
					
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(suspensionobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(suspensionobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(suspensionobj1.getLastName())) {
								suspensionobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}	
					
					model.addAttribute("suspensionObj", suspensionobj1);
				}

				else {
					SuspensionDetails suspensionobj1 = new SuspensionDetails();

					CaseDetails caseDetails = caseDetailsRepo.findById(caseid).orElse(new CaseDetails());
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						suspensionobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						suspensionobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						suspensionobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						suspensionobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						suspensionobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					suspensionobj1.setCaseNo(caseDetails.getCaseNo());
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(suspensionobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(suspensionobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(suspensionobj1.getLastName())) {
								suspensionobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}	

					model.addAttribute("suspensionObj", suspensionobj1);
				}

//					susdetails.get(0).setCaseNo(susdetails.get(0).getCasedetails().getCaseNo());

				/* Passing Obj to UI key and value Pair */
				/*
				 * model.addAttribute("suspensionObj", susdetails.get(0)); } else {
				 * SuspensionDetails sus = new SuspensionDetails();
				 * sus.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new
				 * CaseDetails()).getCaseNo()); model.addAttribute("suspensionObj", sus); }
				 */
				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = suspensionDetailsRepo.findByCasedetailsId(caseid, pageable);
				}else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SECRETARYOLD")) {
					
		//			noticePages = suspensionDetailsRepo.findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(currentUser.getPimsEmployee().getGlobalOrgId(),caseid, pageable);
				}
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
					noticePages = suspensionDetailsRepo.findByCasedetailsIdAndCurrentUserIn(caseid, userList,
							pageable);
				}
				else {
					noticePages = suspensionDetailsRepo.findByCasedetailsIdAndCurrentUser(caseid, currentUser,
							pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("suspensionEmployeeList", noticePages.getContent());

			} else {
//				model.addAttribute("suspensionEmployeeList", suspensionDetailsRepo.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			noticePages = suspensionDetailsRepo.findByCasedetailsId(null, pageable);
			model.addAttribute("currentPage", page);
			begin = Math.max(1, page - 5);
			if (page >= 5) {
				begin = page;
			}
			end = Math.min(begin + 4, noticePages.getTotalPages());
			model.addAttribute("begin", begin);
			model.addAttribute("end", end);
			model.addAttribute("totalPages", noticePages.getTotalPages());
			model.addAttribute("totalItems", noticePages.getTotalElements());
			model.addAttribute("suspensionEmployeeList", noticePages.getContent());
			model.addAttribute("suspensionEmployeeList", new ArrayList<>());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/suspensionDetails";
	}

	/*
	 * Method :Suspension details saving method.. Description :written saving form
	 * logic code
	 */
	@PostMapping("/addsuspensionDetails")
	public String addsuspensionDetails(@ModelAttribute("suspensionObj") SuspensionDetailsDto suspensiondetailsdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "uploadOrderActualSuspensionName", required = false) MultipartFile file1,
			@RequestParam(name = "uploadOrderDeemedSuspension", required = false) MultipartFile file2,
			@RequestParam(name = "extensionUploadOrder", required = false) MultipartFile file3) {

		/* Creating new object of Suspension Details */

		SuspensionDetails suspensiondetails = new SuspensionDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (suspensiondetailsdto.getId() != null && suspensiondetailsdto.getId() > 0) {
				suspensiondetails = suspensionDetailsRepo.findById(suspensiondetailsdto.getId())
						.orElse(new SuspensionDetails());

				if (StringUtils.isNotBlank(suspensiondetailsdto.getCaseNo())
						&& caseDetailsRepo.existsByCaseNoAndCurrentUser(suspensiondetailsdto.getCaseNo(),currentUser)) {
					suspensiondetails.setCasedetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(suspensiondetailsdto.getCaseNo(),currentUser));
				}
				
				if(StringUtils.isNotBlank(suspensiondetailsdto.getEmployeeDataId())) {
					if(suspensionDetailsRepo.existsByCasedetailsAndEmpDataId(suspensiondetails.getCasedetails(), Long.valueOf(suspensiondetailsdto.getEmployeeDataId()))) {
						suspensiondetails = suspensionDetailsRepo.findByCasedetailsAndEmpDataId(suspensiondetails.getCasedetails(), Long.valueOf(suspensiondetailsdto.getEmployeeDataId()));
					} else {
						suspensiondetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(suspensiondetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

				

//			suspensiondetails.setCasedetails(suspensiondetailsdto.getCasedetails());
				suspensiondetails.setSevarthId(suspensiondetailsdto.getSevarthId());
				/* suspensiondetails.setEmployeeId(suspensiondetailsdto.getEmployeeId()); */
				suspensiondetails.setFirstName(suspensiondetailsdto.getFirstName());
				suspensiondetails.setLastName(suspensiondetailsdto.getLastName());
				suspensiondetails.setMiddleName(suspensiondetailsdto.getMiddleName());
				suspensiondetails.setEmployeeSuspended(suspensiondetailsdto.isEmployeeSuspended());
				if (suspensiondetailsdto.getSusUnderRule() != null) {
					suspensiondetails.setSusUnderRule(suspensionRuleRepo
							.findById(suspensiondetailsdto.getSusUnderRule().getId()).orElse(new SuspensionRules()));
					if(suspensiondetails.getSusUnderRule().getId() == 2) {
						suspensiondetails.setDateofDeemedSuspension(null);
						suspensiondetails.setDateOfOrder1(null);
						suspensiondetails.setDateofActualSuspension(suspensiondetailsdto.getDateofActualSuspension());
						suspensiondetails.setDateOfOrder(suspensiondetailsdto.getDateOfOrder());
					}
					if(suspensiondetails.getSusUnderRule().getId() == 1) {
						suspensiondetails.setDateofActualSuspension(null);
						suspensiondetails.setDateOfOrder(null);
						suspensiondetails.setDateofDeemedSuspension(suspensiondetailsdto.getDateofDeemedSuspension());
						suspensiondetails.setDateOfOrder1(suspensiondetailsdto.getDateOfOrder1());
					}
				} else {
					suspensiondetails.setSusUnderRule(null);
				}
				
				/* suspensiondetails.setFileNo(suspensiondetailsdto.getFileNo()); */
//				suspensiondetails.setDateOfOrder(suspensiondetailsdto.getDateOfOrder());
//				suspensiondetails.setDateofActualSuspension(suspensiondetailsdto.getDateofActualSuspension());
//				suspensiondetails.setDateofDeemedSuspension(suspensiondetailsdto.getDateofDeemedSuspension());
//				suspensiondetails.setUploadOrderDeemedSuspension(suspensiondetailsdto.getUploadOrderDeemedSuspension());
				suspensiondetails
						.setWhetherSuspensionIsExtnd3Months(suspensiondetailsdto.isWhetherSuspensionIsExtnd3Months());
				suspensiondetails.setPeriodofExtension(suspensiondetailsdto.getPeriodofExtension());
				suspensiondetails.setDateofExtensionOrder(suspensiondetailsdto.getDateofExtensionOrder());
				suspensiondetails.setExtensionUploadOrder(suspensiondetailsdto.getExtensionUploadOrder());
				/* suspensiondetails.setWithde(suspensiondetailsdto.isWithde()); */
				// adding code according to latest pdf
				suspensiondetails.setRemark(suspensiondetailsdto.getRemark());
//				suspensiondetails.setDateOfOrder1(suspensiondetailsdto.getDateOfOrder1());
				suspensiondetails.setPeriodofExtensionInDays(suspensiondetailsdto.getPeriodofExtensionInDays());
				suspensiondetails.setWhetherReviewTaken(suspensiondetailsdto.isWhetherReviewTaken());
				suspensiondetails.setCurrentStatus(suspensiondetailsdto.getCurrentStatus().replace(",", "").trim());
				
				
				if (file1 != null && !file1.isEmpty()
						&& file1.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = "fileNo/" + suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetails/" + suspensiondetails.getId() + "/";
					suspensiondetails.setUploadOrderActualSuspensionName(file1.getOriginalFilename());
//					suspensiondetails.setFilepathActualSuspension(file1.getOriginalFilename());
					suspensiondetails.setContentType(file1.getContentType());
					suspensiondetails.setFilepathActualSuspension(filePath);
					fileService.fileUpload(filePath, file1, suspensiondetails.getUploadOrderActualSuspensionName());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				// this is for second file upload code
				if (file2 != null && !file2.isEmpty()
						&& file2.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetails/" + suspensiondetails.getId() + "/";
					suspensiondetails.setUploadOrderDeemedSuspension(file2.getOriginalFilename());
					suspensiondetails.setContentType(file2.getContentType());
					suspensiondetails.setFilePathUploadOrderDeemedSuspension(filePath);
					fileService.fileUpload(filePath, file2, suspensiondetails.getUploadOrderDeemedSuspension());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				// this is for third file upload code

				if (file3 != null && !file3.isEmpty()
						&& file3.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetails/" + suspensiondetails.getId() + "/";
					suspensiondetails.setExtensionUploadOrder(file3.getOriginalFilename());
					suspensiondetails.setContentType(file3.getContentType());
					suspensiondetails.setFilePathExtensionUploadOrder(filePath);
					fileService.fileUpload(filePath, file3, suspensiondetails.getExtensionUploadOrder());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				suspensiondetails = suspensionDetailsRepo.save(suspensiondetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editsusdtlsRecords/"+suspensiondetails.getId()+"/1";

			}

			else {
//				// suspensiondetails.setCasedetails(caseDetailsRepo.findById(suspensiondetailsdto.getCasedetails().getId()).orElse(new
//				// CaseDetails()));
//				suspensiondetails.setCasedetails(caseDetailsRepo.findByCaseNoContainingIgnoreCase(suspensiondetailsdto.getCaseNo()));
//
//				// for save
//				/* this code is checking on Case Details screen at case id is exist or not */
//				if (suspensionDetailsRepo.existsByCasedetails(suspensiondetails.getCasedetails())) {
//					suspensiondetails = suspensionDetailsRepo.findByCasedetails(suspensiondetails.getCasedetails());
//				}
//
//				// employeedetails.setCasedetails(caseDetailsRepo.findByCaseNoContainingIgnoreCase(employeedetailsdto.getCaseNo()));
//
//				/*
//				 * if(employeedetailsrepo.existsByCasedetails(employeedetails.getCasedetails()))
//				 * { employeedetails =
//				 * employeedetailsrepo.findByCasedetails(employeedetails.getCasedetails()); }
//				 */
				// save
				if (StringUtils.isBlank(suspensiondetailsdto.getFileNo())) {
					CaseDetails cd = caseDetailsRepo.findByCaseNo(suspensiondetailsdto.getCaseNo())
							;

					if (cd != null) {
						suspensiondetails.setCasedetails(cd);
					} else {
						redirect.addFlashAttribute(departmentconstants.MESSAGE,
								messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
						return "redirect:/departmentalEnquiry/suspensionDetails/1";
					}

					if (StringUtils.isNotBlank(suspensiondetailsdto.getSevarthId()) && suspensionDetailsRepo
							.existsByCasedetailsAndSevarthId(cd, suspensiondetailsdto.getSevarthId())) {
						suspensiondetails = suspensionDetailsRepo.findByCasedetailsAndSevarthId(cd,
								suspensiondetailsdto.getSevarthId());
					}
				}
				
				if(StringUtils.isNotBlank(suspensiondetailsdto.getEmployeeDataId())) {
					if(suspensionDetailsRepo.existsByCasedetailsAndEmpDataId(suspensiondetails.getCasedetails(), Long.valueOf(suspensiondetailsdto.getEmployeeDataId()))) {
						suspensiondetails = suspensionDetailsRepo.findByCasedetailsAndEmpDataId(suspensiondetails.getCasedetails(), Long.valueOf(suspensiondetailsdto.getEmployeeDataId()));
					} else {
						suspensiondetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(suspensiondetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}
				/* suspensiondetails.setFileNo(suspensiondetailsdto.getFileNo()); */

				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					suspensiondetails.setCurrentUser(currentUser);
					suspensiondetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				suspensiondetails.setSevarthId(suspensiondetailsdto.getSevarthId());
				suspensiondetails.setEmployeeId(suspensiondetailsdto.getEmployeeId());
				suspensiondetails.setFirstName(suspensiondetailsdto.getFirstName());
				suspensiondetails.setLastName(suspensiondetailsdto.getLastName());
				/* suspensiondetails.setWithde(suspensiondetailsdto.isWithde()); */
				// suspensiondetails.setUploadOrderActualSuspensionName(suspensiondetailsdto.getUploadOrderActualSuspensionName());
//				suspensiondetails.setUploadOrderActualSuspensionName(file1.getOriginalFilename());
				suspensiondetails.setMiddleName(suspensiondetailsdto.getMiddleName());
				suspensiondetails.setEmployeeSuspended(suspensiondetailsdto.isEmployeeSuspended());
				
				suspensiondetails.setWhetherReviewTaken(suspensiondetailsdto.isWhetherReviewTaken());
				suspensiondetails.setCurrentStatus(suspensiondetailsdto.getCurrentStatus().replace(",", "").trim());
				
				suspensiondetails.setRemark(suspensiondetailsdto.getRemark());
				if (suspensiondetailsdto.getSusUnderRule() != null) {
					suspensiondetails.setSusUnderRule(suspensionRuleRepo
							.findById(suspensiondetailsdto.getSusUnderRule().getId()).orElse(new SuspensionRules()));
					if(suspensiondetails.getSusUnderRule().getId() == 2) {
						suspensiondetails.setDateofDeemedSuspension(null);
						suspensiondetails.setDateOfOrder1(null);
						suspensiondetails.setDateofActualSuspension(suspensiondetailsdto.getDateofActualSuspension());
						suspensiondetails.setDateOfOrder(suspensiondetailsdto.getDateOfOrder());
					}
					if(suspensiondetails.getSusUnderRule().getId() == 1) {
						suspensiondetails.setDateofActualSuspension(null);
						suspensiondetails.setDateOfOrder(null);
						suspensiondetails.setDateofDeemedSuspension(suspensiondetailsdto.getDateofDeemedSuspension());
						suspensiondetails.setDateOfOrder1(suspensiondetailsdto.getDateOfOrder1());
					}
				} else {
					suspensiondetails.setSusUnderRule(null);
				}
//				suspensiondetails.setDateOfOrder(suspensiondetailsdto.getDateOfOrder());
//
//				suspensiondetails.setDateofActualSuspension(suspensiondetailsdto.getDateofActualSuspension());
				suspensiondetails
						.setUploadOrderActualSuspensionName(suspensiondetailsdto.getUploadOrderActualSuspensionName());

				// 17-08-2022
//				suspensiondetails.setDateofDeemedSuspension(suspensiondetailsdto.getDateofDeemedSuspension());
				suspensiondetails.setUploadOrderDeemedSuspension(suspensiondetailsdto.getUploadOrderDeemedSuspension());
				suspensiondetails
						.setWhetherSuspensionIsExtnd3Months(suspensiondetailsdto.isWhetherSuspensionIsExtnd3Months());
				suspensiondetails.setPeriodofExtension(suspensiondetailsdto.getPeriodofExtension());
				suspensiondetails.setDateofExtensionOrder(suspensiondetailsdto.getDateofExtensionOrder());
				suspensiondetails.setExtensionUploadOrder(suspensiondetailsdto.getExtensionUploadOrder());

				// adding code according to latest pdf
//				suspensiondetails.setDateOfOrder1(suspensiondetailsdto.getDateOfOrder1());
				suspensiondetails.setPeriodofExtensionInDays(suspensiondetailsdto.getPeriodofExtensionInDays());

				// adding code according to latest pdf
				suspensiondetails.setDateOfOrder1(suspensiondetailsdto.getDateOfOrder1());
				suspensiondetails.setPeriodofExtensionInDays(suspensiondetailsdto.getPeriodofExtensionInDays());
				
				

				if (file1 != null && !file1.isEmpty()
						&& file1.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetails/" + suspensiondetails.getId() + "/";
					suspensiondetails.setUploadOrderActualSuspensionName(file1.getOriginalFilename());
//					suspensiondetails.setFilepathActualSuspension(file1.getOriginalFilename());
					suspensiondetails.setContentType(file1.getContentType());
					suspensiondetails.setFilepathActualSuspension(filePath);
					fileService.fileUpload(filePath, file1, suspensiondetails.getUploadOrderActualSuspensionName());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				// this is for second file upload code
				if (file2 != null && !file2.isEmpty()
						&& file2.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetails/" + suspensiondetails.getId() + "/";
					suspensiondetails.setUploadOrderDeemedSuspension(file2.getOriginalFilename());
					suspensiondetails.setContentType(file2.getContentType());
					suspensiondetails.setFilePathUploadOrderDeemedSuspension(filePath);
					fileService.fileUpload(filePath, file2, suspensiondetails.getUploadOrderDeemedSuspension());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				// this is for third file upload code

				if (file3 != null && !file3.isEmpty()
						&& file3.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetails/" + suspensiondetails.getId() + "/";
					suspensiondetails.setExtensionUploadOrder(file3.getOriginalFilename());
					suspensiondetails.setContentType(file3.getContentType());
					suspensiondetails.setFilePathExtensionUploadOrder(filePath);
					fileService.fileUpload(filePath, file3, suspensiondetails.getExtensionUploadOrder());
				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				suspensiondetails = suspensionDetailsRepo.save(suspensiondetails);
				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editsusdtlsRecords/"+suspensiondetails.getId()+"/1";

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/suspensionDetails/1";

	}

	/* ReInstated landing form Method */

	@GetMapping("/reinstaledDetails/{page}")
	public String ReInstatedlandingformMethod(Model model, @PathVariable("page") int page,
			RedirectAttributes redirect) {
		LOG.info("Hi this is the reinstated details page method.............");

		ReInstatedDetails reinstateddetails = new ReInstatedDetails();
		model.addAttribute("reinstatedObjDetails", reinstateddetails);
//		model.addAttribute("reinstatedGridList", reinstatedRepository.findAll());

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<ReInstatedDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = reinstatedRepository.findByOrderById(pageable);
		}else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
			noticePages = reinstatedRepository.findByCurrentUserPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId(), pageable);
		}
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			noticePages = reinstatedRepository.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = reinstatedRepository.findByCurrentUserOrderById(currentUser, pageable);
		}

		model.addAttribute("reinstatedGridList", noticePages);
		// System.out.println(noticePages);
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
		model.addAttribute("reinstatedGridList", noticePages.getContent());

		/*
		 * try { Long caseid = (Long) request.getSession().getAttribute("caseid");
		 * LOG.info(caseid); if (caseid != null) {
		 * 
		 * model.addAttribute("reinstatedObjDetails",
		 * reinstatedRepository.findByCasedetailsId(caseid));
		 * 
		 * } else { model.addAttribute("reinstatedObjDetails", reinstateddetails); } }
		 * catch (Exception a) { a.printStackTrace();
		 * model.addAttribute("reinstatedObjDetails", reinstateddetails); }
		 */

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
//			System.out.println(caseid);
			if (caseid != null) {
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseid)).orElse(new CaseDetails());
				model.addAttribute("cases", cd);
				if(cd != null) {
					if(cd.getIoList().isEmpty()) {
						model.addAttribute("disablepresentingofficer", true);
					} 
					if(cd.getMisConductType() != null && (cd.getMisConductType().getId()==1 || cd.getMisConductType().getId()==1)) {
						model.addAttribute("disableprosecution", true);
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
				}
				/*------------------------------------------------------------------------------*/

				List<ReInstatedDetails> reinstdetails = reinstatedRepository.findByCasedetailsId(caseid);
				if (reinstdetails != null && !reinstdetails.isEmpty()) {
					ReInstatedDetails reinstationobj1 = reinstdetails.get(0);
					CaseDetails caseDetails = reinstationobj1.getCasedetails();

					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						reinstationobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						reinstationobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						reinstationobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						reinstationobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						reinstationobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(reinstationobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(reinstationobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(reinstationobj1.getLastName())) {
								reinstationobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}
					
					reinstationobj1.setCaseNo(reinstdetails.get(0).getCasedetails().getCaseNo());
					model.addAttribute("reinstatedObjDetails", reinstationobj1);
				}

				else {
					ReInstatedDetails reinstationobj1 = new ReInstatedDetails();

					CaseDetails caseDetails = caseDetailsRepo.findById(caseid).orElse(new CaseDetails());
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						reinstationobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						reinstationobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						reinstationobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						reinstationobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						reinstationobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					reinstationobj1.setCaseNo(caseDetails.getCaseNo());
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(reinstationobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(reinstationobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(reinstationobj1.getLastName())) {
								reinstationobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}

					model.addAttribute("reinstatedObjDetails", reinstationobj1);
				}

				// model.addAttribute("employeeList",
//		  employeedetailsrepo.findByCasedetailsId(caseNo);

				// EmployeeDetails emp = employeedetailsrepo.findByCasedetailsId(caseNo);
				// ChargesheetDetails chargesheet=chargesheetRepo.findByCaseDetailsId(caseid);
				// SuspensionDetails
				// susdetails=suspensionDetailsRepo.findByCasedetailsId(caseid);

				/*
				 * List<ReInstatedDetails> reinstated =
				 * reinstatedRepository.findByCasedetailsId(caseid); if (!reinstated.isEmpty())
				 * {
				 * reinstated.get(0).setCaseNo(reinstated.get(0).getCasedetails().getCaseNo());
				 * 
				 * Passing Obj to UI key and value Pair
				 * model.addAttribute("reinstatedObjDetails", reinstated.get(0)); } else {
				 * ReInstatedDetails res = new ReInstatedDetails();
				 * res.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new
				 * CaseDetails()).getCaseNo()); model.addAttribute("reinstatedObjDetails", res);
				 * }
				 */

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
//				noticePages = null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = reinstatedRepository.findByCasedetailsId(caseid, pageable);
				}else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SECRETARYOLD")) {
					
					noticePages = reinstatedRepository.findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(currentUser.getPimsEmployee().getGlobalOrgId(),caseid, pageable);
				}
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
					noticePages = reinstatedRepository.findByCasedetailsIdInAndCurrentUserIn(Arrays.asList(caseid),userList, pageable);
				}
				else {
					noticePages = reinstatedRepository.findByCasedetailsIdInAndCurrentUser(Arrays.asList(caseid),currentUser, pageable);
				}
				model.addAttribute("reinstatedGridList", noticePages.getContent());

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
//				List<ReInstatedDetails> reinStateList = new ArrayList<>();
//				noticePages.getContent().stream().forEach(s->{
//					ReInstatedDetails rein = reinstatedRepository.findById(s.getId()).orElse(new ReInstatedDetails());
//					reinStateList.add(rein);
//				});
				

			} else {
//				model.addAttribute("reinstatedGridList", reinstatedRepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("reinstatedGridList", reinstatedRepository.findAll());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/reinstatedDetails";
	}

	/*
	 * Method : details saving method.. Description : written saving form logic code
	 */

	@PostMapping("/addreinstatedDetails")
	public String addreinstatedDetails(
			@ModelAttribute("reinstatedObjDetails") ReInstatedDetailsDto reinstateddetailsdto, BindingResult result,
			Model model, RedirectAttributes redirect,
			@RequestParam(name = "UploadOrderReInstatment", required = false) MultipartFile file1) {
		LOG.info("HI This  is the reinstated save method call...");

		/* Creating new object of ReInstatedDetails screen */
		ReInstatedDetails reinstateddetails = new ReInstatedDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (reinstateddetailsdto.getId() != null && reinstateddetailsdto.getId() > 0) {

				/* finding id of ReInstated details id */
				reinstateddetails = reinstatedRepository.findById(reinstateddetailsdto.getId())
						.orElse(new ReInstatedDetails());

				if (StringUtils.isNotBlank(reinstateddetailsdto.getCaseNo())
						&& caseDetailsRepo.existsByCaseNoAndCurrentUser(reinstateddetailsdto.getCaseNo(),currentUser)) {
					reinstateddetails.setCasedetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(reinstateddetailsdto.getCaseNo(),currentUser));
					if(reinstateddetails.getEmpData() != null && reinstateddetails.getEmpData().getSuspensionDetails() != null && reinstateddetailsdto.isEmployeeReInstated()
							&& reinstateddetails.getEmpData().getSuspensionDetails().getCurrentStatus().equalsIgnoreCase("Still Under Suspension")) {
						
						try {
							final long susid = reinstateddetails.getEmpData().getSuspensionDetails().getId();
							transactionaTemplate.execute(transactionStatus->{
								CriteriaBuilder cb = em.getCriteriaBuilder();
								CriteriaUpdate<SuspensionDetails> update = cb.createCriteriaUpdate(SuspensionDetails.class);
								Root<SuspensionDetails> root = update.from(SuspensionDetails.class);
								update.set("currentStatus", "Reinstated")
								.set("whetherReviewTaken", true).where(cb.equal(root.get("id"), susid));								
								em.createQuery(update).executeUpdate();
								transactionStatus.flush();
								return null;
							});
							
						} catch (Exception e) {
							LOG.error("Exception", e);
						}
					}
					if(reinstateddetails.getEmpData() != null && reinstateddetails.getEmpData().getSuspensionDetails() != null && reinstateddetailsdto.isEmployeeReInstated()) {
						LocalDate actualDate = reinstateddetails.getEmpData().getSuspensionDetails().getDateofActualSuspension();
						LocalDate deemedDate = reinstateddetails.getEmpData().getSuspensionDetails().getDateofDeemedSuspension();
						final long reinid = reinstateddetails.getId();
						if(reinstateddetails.getEmpData().getSuspensionDetails().getSusUnderRule().getId()==2) {
							
							if(actualDate != null) {
								
								 Period period = Period.between(actualDate, reinstateddetails.getReInstatedOrderDate());			 
									 long years = period.getYears();
									 long month=period.getMonths();
									 long days=period.getDays();
									 transactionaTemplate.execute(transactionStatus->{
											CriteriaBuilder cb = em.getCriteriaBuilder();
											CriteriaUpdate<ReInstatedDetails> update = cb.createCriteriaUpdate(ReInstatedDetails.class);
											Root<ReInstatedDetails> root = update.from(ReInstatedDetails.class);
											update.set("year", years)
											.set("month", month)
											.set("day", days).where(cb.equal(root.get("id"), reinid));								
											em.createQuery(update).executeUpdate();
											transactionStatus.flush();
											return null;
										});
								
							} 
						}
						if(reinstateddetails.getEmpData().getSuspensionDetails().getSusUnderRule().getId()==1) {
							if(deemedDate != null) {
								 Period period = Period.between(deemedDate, reinstateddetails.getReInstatedOrderDate());			 
									 long years = period.getYears();
									 long month=period.getMonths();
									 long days=period.getDays();
									 transactionaTemplate.execute(transactionStatus->{
											CriteriaBuilder cb = em.getCriteriaBuilder();
											CriteriaUpdate<ReInstatedDetails> update = cb.createCriteriaUpdate(ReInstatedDetails.class);
											Root<ReInstatedDetails> root = update.from(ReInstatedDetails.class);
											update.set("year", years)
											.set("month", month)
											.set("day", days).where(cb.equal(root.get("id"), reinid));								
											em.createQuery(update).executeUpdate();
											transactionStatus.flush();
											return null;
										});
							}
						}
						
					}
				}
				
				if(StringUtils.isNotBlank(reinstateddetailsdto.getEmployeeDataId())) {
					if(reinstatedRepository.existsByCasedetailsAndEmpDataId(reinstateddetails.getCasedetails(), Long.valueOf(reinstateddetailsdto.getEmployeeDataId()))) {
						reinstateddetails = reinstatedRepository.findByCasedetailsAndEmpDataId(reinstateddetails.getCasedetails(), Long.valueOf(reinstateddetailsdto.getEmployeeDataId()));
					} else {
						reinstateddetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(reinstateddetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

				reinstateddetails.setSevarthId(reinstateddetailsdto.getSevarthId());
				reinstateddetails.setEmployeeId(reinstateddetailsdto.getEmployeeId());
				reinstateddetails.setFileNo(reinstateddetailsdto.getFileNo());
				reinstateddetails.setWithde(reinstateddetailsdto.isWithde());
				reinstateddetails.setFirstName(reinstateddetailsdto.getFirstName());
				reinstateddetails.setLastName(reinstateddetailsdto.getLastName());

				reinstateddetails.setMiddleName(reinstateddetailsdto.getMiddleName());
				reinstateddetails.setEmployeeReInstated(reinstateddetailsdto.isEmployeeReInstated());
				reinstateddetails.setReInstatedOrderDate(reinstateddetailsdto.getReInstatedOrderDate());
				reinstateddetails.setUploadOrderReInstatment(reinstateddetailsdto.getUploadOrderReInstatment());

				if (file1 != null && !file1.isEmpty()
						&& file1.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (reinstateddetails.getCasedetails() != null) {
						id = reinstateddetails.getCasedetails().getId().toString();
					} else {
						id = "fileNo/" + reinstateddetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "reinstatedDetails/" + reinstateddetails.getId() + "/";
					reinstateddetails.setUploadOrderReInstatment(file1.getOriginalFilename());
					reinstateddetails.setContentType(file1.getContentType());
					reinstateddetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file1, reinstateddetails.getUploadOrderReInstatment());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				reinstateddetails = reinstatedRepository.save(reinstateddetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editReinstateddtlsRecords/"+reinstateddetails.getId()+"/1";

			}

			else {

				// reinstateddetails.setCasedetails(caseDetailsRepo.findById(reinstateddetailsdto.getCasedetails().getId()).orElse(new
				// CaseDetails()));

				// reinstateddetails.setCasedetails(caseDetailsRepo.findByCaseNoContainingIgnoreCase(reinstateddetailsdto.getCaseNo()));

				if (StringUtils.isBlank(reinstateddetailsdto.getFileNo())) {
					CaseDetails cd = caseDetailsRepo.findByCaseNo(reinstateddetailsdto.getCaseNo())
							;

					if (cd != null) {
						reinstateddetails.setCasedetails(cd);
					} else {
						redirect.addFlashAttribute(departmentconstants.MESSAGE,
								messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
						return "redirect:/departmentalEnquiry/reinstaledDetails/1";
					}
					// if
					// (reinstatedRepository.existsByCasedetails(reinstateddetails.getCasedetails()))
					// {
					// reinstateddetails =
					// reinstatedRepository.findByCasedetails(reinstateddetails.getCasedetails());
					//
					// }
					if (StringUtils.isNotBlank(reinstateddetailsdto.getSevarthId()) && reinstatedRepository
							.existsByCasedetailsAndSevarthId(cd, reinstateddetailsdto.getSevarthId())) {
						reinstateddetails = reinstatedRepository.findByCasedetailsAndSevarthId(cd,
								reinstateddetailsdto.getSevarthId());
					}
				}
				
				if(StringUtils.isNotBlank(reinstateddetailsdto.getEmployeeDataId())) {
					if(reinstatedRepository.existsByCasedetailsAndEmpDataId(reinstateddetails.getCasedetails(), Long.valueOf(reinstateddetailsdto.getEmployeeDataId()))) {
						reinstateddetails = reinstatedRepository.findByCasedetailsAndEmpDataId(reinstateddetails.getCasedetails(), Long.valueOf(reinstateddetailsdto.getEmployeeDataId()));
					} else {
						reinstateddetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(reinstateddetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}
				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					reinstateddetails.setCurrentUser(currentUser);
					reinstateddetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				reinstateddetails.setFileNo(reinstateddetailsdto.getFileNo());
				reinstateddetails.setWithde(reinstateddetailsdto.isWithde());

				/* reinstateddetails.setSevarthId(reinstateddetailsdto.getSevarthId()); */
				reinstateddetails.setSevarthId(reinstateddetailsdto.getSevarthId());
				reinstateddetails.setEmployeeId(reinstateddetailsdto.getEmployeeId());
				reinstateddetails.setFirstName(reinstateddetailsdto.getFirstName());
				reinstateddetails.setLastName(reinstateddetailsdto.getLastName());
				reinstateddetails.setMiddleName(reinstateddetailsdto.getMiddleName());
				reinstateddetails.setEmployeeReInstated(reinstateddetailsdto.isEmployeeReInstated());
				reinstateddetails.setReInstatedOrderDate(reinstateddetailsdto.getReInstatedOrderDate());
				reinstateddetails.setUploadOrderReInstatment(reinstateddetailsdto.getUploadOrderReInstatment());
				// reinstateddetails.setUploadOrderReInstatment(reinstateddetailsdto.getUploadOrderReInstatment());

				if (file1 != null && !file1.isEmpty()
						&& file1.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (reinstateddetails.getCasedetails() != null) {
						id = reinstateddetails.getCasedetails().getId().toString();
					} else {
						id = "fileNo/" + reinstateddetails.getFileNo();
					}

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetails/" + reinstateddetails.getId() + "/";
					reinstateddetails.setUploadOrderReInstatment(file1.getOriginalFilename());
					reinstateddetails.setContentType(file1.getContentType());
					reinstateddetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file1, reinstateddetails.getUploadOrderReInstatment());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				
				reinstateddetails = reinstatedRepository.save(reinstateddetails);
				final long reinid = reinstateddetails.getId();
				if(reinstateddetails.getEmpData() != null && reinstateddetails.getEmpData().getSuspensionDetails() != null && reinstateddetailsdto.isEmployeeReInstated()) {
					LocalDate actualDate = reinstateddetails.getEmpData().getSuspensionDetails().getDateofActualSuspension();
					LocalDate deemedDate = reinstateddetails.getEmpData().getSuspensionDetails().getDateofDeemedSuspension();
					if(reinstateddetails.getEmpData().getSuspensionDetails().getSusUnderRule().getId()==2) {
						if(actualDate != null) {
							 Period period = Period.between(actualDate, reinstateddetails.getReInstatedOrderDate());			 
								 long years = period.getYears();
								 long month=period.getMonths();
								 long days=period.getDays();
								 transactionaTemplate.execute(transactionStatus->{
										CriteriaBuilder cb = em.getCriteriaBuilder();
										CriteriaUpdate<ReInstatedDetails> update = cb.createCriteriaUpdate(ReInstatedDetails.class);
										Root<ReInstatedDetails> root = update.from(ReInstatedDetails.class);
										update.set("year", years)
										.set("month", month)
										.set("day", days).where(cb.equal(root.get("id"), reinid));								
										em.createQuery(update).executeUpdate();
										transactionStatus.flush();
										return null;
									});
							
						} 
					}
					if(reinstateddetails.getEmpData().getSuspensionDetails().getSusUnderRule().getId()==1) {
						if(deemedDate != null) {
							 Period period = Period.between(deemedDate, reinstateddetails.getReInstatedOrderDate());			 
								 long years = period.getYears();
								 long month=period.getMonths();
								 long days=period.getDays();
								 transactionaTemplate.execute(transactionStatus->{
										CriteriaBuilder cb = em.getCriteriaBuilder();
										CriteriaUpdate<ReInstatedDetails> update = cb.createCriteriaUpdate(ReInstatedDetails.class);
										Root<ReInstatedDetails> root = update.from(ReInstatedDetails.class);
										update.set("year", years)
										.set("month", month)
										.set("day", days).where(cb.equal(root.get("id"), reinid));								
										em.createQuery(update).executeUpdate();
										transactionStatus.flush();
										return null;
									});
						}
					}
				}
				
				
				if(reinstateddetails.getEmpData() != null && reinstateddetails.getEmpData().getSuspensionDetails() != null 
						&& reinstateddetailsdto.isEmployeeReInstated()
						&& reinstateddetails.getEmpData().getSuspensionDetails().getCurrentStatus().equalsIgnoreCase("Still Under Suspension")) {
					try {
						final long susid = reinstateddetails.getEmpData().getSuspensionDetails().getId();
						transactionaTemplate.execute(transactionStatus->{
							CriteriaBuilder cb = em.getCriteriaBuilder();
							CriteriaUpdate<SuspensionDetails> update = cb.createCriteriaUpdate(SuspensionDetails.class);
							Root<SuspensionDetails> root = update.from(SuspensionDetails.class);
							update.set("currentStatus", "Reinstated")
							.set("whetherReviewTaken", true).where(cb.equal(root.get("id"), susid));
							em.createQuery(update).executeUpdate();
							transactionStatus.flush();
							return null;
						});
						
					} catch (Exception e) {
						LOG.error("Exception", e);
					}
				}
				
				reinstateddetails = reinstatedRepository.save(reinstateddetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editReinstateddtlsRecords/"+reinstateddetails.getId()+"/1";

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/reinstaledDetails/1";

	}

	/* inquiry officer landing form Method */

	@GetMapping("/inquiryofficerdetails/{page}")
	public String inquiryofficerdetails(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		LOG.info("Hi this is the inquiry officer details  page method.............");

		InquiryOfficerDetails inquiryofficerdetails = new InquiryOfficerDetails();

		model.addAttribute("InquiryOfficerlist", inquiryOfficerListRepo.findByActive(true,Sort.by("order")));
		Set<String> inquiryOfficerTypeList = new LinkedHashSet<>();
		List<InquiryOfficerList> listinq = inquiryOfficerListRepo.findByActive(true,Sort.by("order"));
//		listinq.sort((a1,a2)->Integer.valueOf(a1.getOrder()).compareTo(Integer.valueOf(a2.getOrder())));
//		inquiryOfficerTypeList = listinq.stream().map(s->s.getDesignation()).collect(Collectors.toSet());
		for(InquiryOfficerList ss : listinq) {
			inquiryOfficerTypeList.add(ss.getDesignation());
		}
		model.addAttribute("inquiryOfficerTypeList", inquiryOfficerTypeList);
		model.addAttribute("inquiryofficerdetailsObj", inquiryofficerdetails);

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<InquiryOfficerDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = inquiryofficerRepository.findAll(pageable);
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
			noticePages = inquiryofficerRepository.findByCurrentUserPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId(), pageable);
		}
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			noticePages = inquiryofficerRepository.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = inquiryofficerRepository.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("InquiryOfficerGridList", noticePages);
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
		model.addAttribute("appellationlist", appellationRepo.findAll());
		model.addAttribute("inqListid", null);

		/*
		 * try { Long caseid = (Long) request.getSession().getAttribute("caseid");
		 * LOG.info(caseid); if (caseid != null) {
		 * 
		 * model.addAttribute("inquiryofficerdetailsObj",
		 * inquiryofficerRepository.findByCasedetailsId(caseid));
		 * 
		 * } else { model.addAttribute("inquiryofficerdetailsObj",
		 * inquiryofficerdetails); } } catch (Exception a) { a.printStackTrace();
		 * model.addAttribute("inquiryofficerdetailsObj", inquiryofficerdetails); }
		 */

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
			System.out.println(caseid);
			if (caseid != null) {
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseid)).orElse(new CaseDetails());
				model.addAttribute("cases", cd);
				if(cd != null) {
					if(cd.getIoList().isEmpty()) {
						model.addAttribute("disablepresentingofficer", true);
					} 
					if(cd.getMisConductType() != null && (cd.getMisConductType().getId()==1 || cd.getMisConductType().getId()==1)) {
						model.addAttribute("disableprosecution", true);
					}
					
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
				}
				// model.addAttribute("employeeList",
//		  employeedetailsrepo.findByCasedetailsId(caseNo);

				// SuspensionDetails
				// susdetails=suspensionDetailsRepo.findByCasedetailsId(caseid);
				// ReInstatedDetails
				// reinstated=reinstatedRepository.findByCasedetailsId(caseid);
				InquiryOfficerDetails inqofficer = inquiryofficerRepository.findByCasedetailsId(caseid);
				if (inqofficer != null) {
					inqofficer.setCaseNo(inqofficer.getCasedetails().getCaseNo());

					/* Passing Obj to UI key and value Pair */
					model.addAttribute("inquiryofficerdetailsObj", inqofficer);
					if(inqofficer.getListOfInquiryOfficer() != null) {
						model.addAttribute("inqListid", inqofficer.getListOfInquiryOfficer().getId());
					}
					model.addAttribute("otherinqtype", inqofficer.getOtherinquiryOfficer());
				} else {
					inqofficer = new InquiryOfficerDetails();
					inqofficer.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new CaseDetails()).getCaseNo());
					model.addAttribute("inquiryofficerdetailsObj", inqofficer);
				}

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = inquiryofficerRepository.findByCasedetailsId(caseid, pageable);
				} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SECRETARYOLD")) {
					
					noticePages = inquiryofficerRepository.findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(currentUser.getPimsEmployee().getGlobalOrgId(),caseid, pageable);
				}
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
					noticePages = inquiryofficerRepository.findByCasedetailsIdAndCurrentUserIn(caseid, userList,
							pageable);
				}
				else {
					noticePages = inquiryofficerRepository.findByCasedetailsIdAndCurrentUser(caseid, currentUser,
							pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("InquiryOfficerGridList", noticePages.getContent());

			} else {
//				model.addAttribute("InquiryOfficerGridList", inquiryofficerRepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("InquiryOfficerGridList", inquiryofficerRepository.findAll());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/inquiryOfficerDetails";
	}

	/*
	 * Method : addinquiryOfficerDetails saving method.. Description : written
	 * saving form logic code
	 */

	@PostMapping("/addinquiryOfficerDetails")
	public String addinquiryOfficerDetails(
			@ModelAttribute("inquiryofficerdetailsObj") InquiryOfficerDto inquiryofficerdto, BindingResult result,
			Model model, RedirectAttributes redirect,
			@RequestParam(name = "uploadOrderofAppointment", required = false) MultipartFile file,
			@RequestParam(name = "inquiryReportfileUpload", required = false) MultipartFile inquiryReportfileUpload) {

		// creating new object of InquiryOfficerDetails model
		InquiryOfficerDetails inquiryofficerdetails = new InquiryOfficerDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (inquiryofficerdto.getId() != null && inquiryofficerdto.getId() > 0) {

				/* Finding id of Inquiry officer details id */
//				reinstateddetails = reinstatedRepository.findById(reinstateddetailsdto.getId()).orElse(new ReInstatedDetails());
				inquiryofficerdetails = inquiryofficerRepository.findById(inquiryofficerdto.getId())
						.orElse(new InquiryOfficerDetails());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(inquiryofficerdto.getCaseNo(),currentUser)) {
					inquiryofficerdetails.setCasedetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(inquiryofficerdto.getCaseNo(),currentUser));
				}

				// inquiryofficerdetails.setCasedetails(inquiryofficerdto.getCasedetails());
				inquiryofficerdetails.setInquiryOfficerAppointed(inquiryofficerdto.isInquiryOfficerAppointed());
				inquiryofficerdetails.setAppointmentDate(inquiryofficerdto.getAppointmentDate());
				
				String dd = request.getParameter("inqofficerList");
				if(dd != null && StringUtils.isNotBlank(dd)) {
					inquiryofficerdetails.setListOfInquiryOfficer(inquiryOfficerListRepo.findById(Long.valueOf(dd)).orElse(new InquiryOfficerList()));
				} else {
					inquiryofficerdetails.setListOfInquiryOfficer(null);
					String other = request.getParameter("reason");
					if(other != null && StringUtils.isNotBlank(other)) {
						inquiryofficerdetails.setOtherinquiryOfficer(other);
					}
				}

//				if (inquiryofficerdto.getListOfInquiryOfficer() != null) {
//					inquiryofficerdetails.setListOfInquiryOfficer(inquiryofficerdto.getListOfInquiryOfficer());
//				} else {
//					inquiryofficerdto.setListOfInquiryOfficer(null);
//
//				}
				inquiryofficerdetails.setInquiryReportDate(inquiryofficerdto.getInquiryReportDate());
				inquiryofficerdetails.setInquiryReportSubmitted(inquiryofficerdto.isInquiryReportSubmitted());
				if(!inquiryofficerdetails.isInquiryReportSubmitted()) {
					inquiryofficerdetails.getCasedetails().setPendingWith("Pending with Department after receiving Report from the  Inquiry Officer");
				} else {
					inquiryofficerdetails.getCasedetails().setPendingWith("Pending with Inquiry Officer");
				}
				inquiryofficerdetails.setInquiryReportfileUpload(inquiryofficerdto.getInquiryReportfileUpload());
				inquiryofficerdetails.setInquiryReportDate(inquiryofficerdto.getInquiryReportDate());
				inquiryofficerdetails.setInquiryOfficerDesignation(inquiryofficerdto.getInquiryOfficerDesignation());
				inquiryofficerdetails.setSevarthId(inquiryofficerdto.getSevarthId());
				inquiryofficerdetails.setEmployeeId(inquiryofficerdto.getEmployeeId());
				inquiryofficerdetails.setFirstName(inquiryofficerdto.getFirstName());
				inquiryofficerdetails.setMiddleName(inquiryofficerdto.getMiddleName());
				inquiryofficerdetails.setLastName(inquiryofficerdto.getLastName());
				inquiryofficerdetails.setMobileNumber(inquiryofficerdto.getMobileNumber());
				inquiryofficerdetails.setAppellation(inquiryofficerdto.getAppellation());
				inquiryofficerdetails.setEmail(inquiryofficerdto.getEmail());
//				inquiryofficerdetails.setUploadOrderofAppointment(inquiryofficerdto.getUploadOrderofAppointment());
				inquiryofficerdetails.setInquiryReportReceived(inquiryofficerdto.isInquiryReportReceived());
				inquiryofficerdetails
						.setDateofInquiryReportReceived(inquiryofficerdto.getDateofInquiryReportReceived());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ inquiryofficerdetails.getCasedetails().getId() + "/" + "inquiryofficerDetails/"
							+ inquiryofficerdetails.getId() + "/";
					inquiryofficerdetails.setUploadOrderofAppointment(file.getOriginalFilename());
					inquiryofficerdetails.setContentType(file.getContentType());
					inquiryofficerdetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, inquiryofficerdetails.getUploadOrderofAppointment());

				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				
				if (inquiryReportfileUpload != null && !inquiryReportfileUpload.isEmpty()
						&& inquiryReportfileUpload.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ inquiryofficerdetails.getCasedetails().getId() + "/" + "inquiryofficerDetails/inquiryReportfileUpload/"
							+ inquiryofficerdetails.getId() + "/";
					inquiryofficerdetails.setInquiryReportfileUpload(inquiryReportfileUpload.getOriginalFilename());
					inquiryofficerdetails.setContentType(inquiryReportfileUpload.getContentType());
					inquiryofficerdetails.setInquiryReportfileUploadFilePath(filePath);
					fileService.fileUpload(filePath, inquiryReportfileUpload, inquiryofficerdetails.getInquiryReportfileUpload());

				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				inquiryofficerdetails = inquiryofficerRepository.save(inquiryofficerdetails);
				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editInquiryOfficerDetails/"+inquiryofficerdetails.getId()+"/1";
			}

			else {

				/*
				 * // inquiryofficerdetails.setCasedetails(caseDetailsRepo.findById(
				 * inquiryofficerdto.getCasedetails().getId()).orElse(new // CaseDetails()));
				 * inquiryofficerdetails.setCasedetails(caseDetailsRepo.
				 * findByCaseNoContainingIgnoreCase( inquiryofficerdto.getCaseNo())); this code
				 * is checking on Case Details screen at case id is exist or not if
				 * (inquiryofficerRepository.existsByCasedetails(inquiryofficerdetails.
				 * getCasedetails())) { inquiryofficerdetails =
				 * inquiryofficerRepository.findByCasedetails(inquiryofficerdetails.
				 * getCasedetails()); }
				 */

				CaseDetails cd = caseDetailsRepo.findByCaseNo(inquiryofficerdto.getCaseNo());

				if (cd != null) {
					inquiryofficerdetails.setCasedetails(cd);
//					if(!inquiryofficerdetails.isInquiryReportSubmitted()) {
//						inquiryofficerdetails.getCasedetails().setPendingWith("Inquiry Officer");
//					} else {
//						inquiryofficerdetails.getCasedetails().setPendingWith("Department");
//					}
				} else {
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
					return "redirect:/departmentalEnquiry/inquiryofficerdetails/1";
				}
				if (inquiryofficerRepository.existsByCasedetails(inquiryofficerdetails.getCasedetails())) {
					inquiryofficerdetails = inquiryofficerRepository
							.findByCasedetails(inquiryofficerdetails.getCasedetails());

				}

				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					inquiryofficerdetails.setCurrentUser(currentUser);
					inquiryofficerdetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				inquiryofficerdetails.setInquiryOfficerAppointed(inquiryofficerdto.isInquiryOfficerAppointed());
				inquiryofficerdetails.setAppointmentDate(inquiryofficerdto.getAppointmentDate());

				String dd = request.getParameter("inqofficerList");
				if(dd != null) {
					inquiryofficerdetails.setListOfInquiryOfficer(inquiryOfficerListRepo.findById(Long.valueOf(dd)).orElse(new InquiryOfficerList()));
				} else {
					inquiryofficerdetails.setListOfInquiryOfficer(null);
					String other = request.getParameter("reason");
					if(other != null) {
						inquiryofficerdetails.setOtherinquiryOfficer(other);
					}
				}
//				if (inquiryofficerdto.getListOfInquiryOfficer() != null) {
//					inquiryofficerdetails.setListOfInquiryOfficer(inquiryofficerdto.getListOfInquiryOfficer());
//				}
//
//				else {
//					inquiryofficerdto.setListOfInquiryOfficer(null);
//
//				}
				inquiryofficerdetails.setInquiryReportDate(inquiryofficerdto.getInquiryReportDate());
//				inquiryofficerdetails.setSevarthId(inquiryofficerdto.getSevarthId());
//				inquiryofficerdetails.setEmployeeId(inquiryofficerdto.getEmployeeId());
				inquiryofficerdetails.setInquiryReportSubmitted(inquiryofficerdto.isInquiryReportSubmitted());
				inquiryofficerdetails.setInquiryReportfileUpload(inquiryofficerdto.getInquiryReportfileUpload());
				inquiryofficerdetails.setInquiryReportDate(inquiryofficerdto.getInquiryReportDate());
				inquiryofficerdetails.setAppellation(inquiryofficerdto.getAppellation());
				inquiryofficerdetails.setFirstName(inquiryofficerdto.getFirstName());
				inquiryofficerdetails.setMiddleName(inquiryofficerdto.getMiddleName());
				inquiryofficerdetails.setLastName(inquiryofficerdto.getLastName());
				inquiryofficerdetails.setMobileNumber(inquiryofficerdto.getMobileNumber());
				inquiryofficerdetails.setEmail(inquiryofficerdto.getEmail());
				inquiryofficerdetails.setInquiryOfficerDesignation(inquiryofficerdto.getInquiryOfficerDesignation());
//				inquiryofficerdetails.setInquiryReportReceived(inquiryofficerdto.isInquiryReportReceived());
//				inquiryofficerdetails.setDateofInquiryReportReceived(inquiryofficerdto.getDateofInquiryReportReceived());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ inquiryofficerdetails.getCasedetails().getId() + "/" + "inquiryofficerDetails/"
							+ inquiryofficerdetails.getId() + "/";
					inquiryofficerdetails.setUploadOrderofAppointment(file.getOriginalFilename());
					inquiryofficerdetails.setContentType(file.getContentType());
					inquiryofficerdetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, inquiryofficerdetails.getUploadOrderofAppointment());

				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				
				if (inquiryReportfileUpload != null && !inquiryReportfileUpload.isEmpty()
						&& inquiryReportfileUpload.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ inquiryofficerdetails.getCasedetails().getId() + "/" + "inquiryofficerDetails/inquiryReportfileUpload/"
							+ inquiryofficerdetails.getId() + "/";
					inquiryofficerdetails.setInquiryReportfileUpload(inquiryReportfileUpload.getOriginalFilename());
					inquiryofficerdetails.setContentType(inquiryReportfileUpload.getContentType());
					inquiryofficerdetails.setInquiryReportfileUploadFilePath(filePath);
					fileService.fileUpload(filePath, inquiryReportfileUpload, inquiryofficerdetails.getInquiryReportfileUpload());

				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				inquiryofficerdetails = inquiryofficerRepository.save(inquiryofficerdetails);
				if(!inquiryofficerdetails.isInquiryReportSubmitted()) {
					cd.setPendingWith("Pending with Department after receiving Report from the  Inquiry Officer");
					caseDetailsRepo.save(cd);
				} else {
					cd.setPendingWith("Pending with Inquiry Officer");
					caseDetailsRepo.save(cd);
				}

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editInquiryOfficerDetails/"+inquiryofficerdetails.getId()+"/1";
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/inquiryofficerdetails/1";

	}

	/* Presenting Officer Landing form Method */

	@GetMapping("/presentingofficerdetails/{page}")
	public String presentingofficerdetails(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		LOG.info("Hi this is the presenting officer details  page method.............");

		PresentingOfficerDetails presentingofficerdetails = new PresentingOfficerDetails();

		model.addAttribute("presentingofficerdetailsObj", presentingofficerdetails);

		model.addAttribute("divisionRepolist", divisionRepository.findByActive(true,Sort.by("order")));
		model.addAttribute("globalOrgList1", globalorgRepo.findByOrderById());
		model.addAttribute("subDepartmentlist", subDepartmentRepo.findByOrderById());
		/* model.addAttribute("designation", designationRepo.findAll()); */
		List<Long>  longs = new  ArrayList<>(Arrays.asList(4L));
		List<Designation> designationList = designationRepo.findByServiceGroupId_IdNotIn(longs);
//		Designation designation = designationRepo.findById(744L).orElse(new Designation());
//		designationList.add(designation);
		model.addAttribute("designation", designationList);
			

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<PresentingOfficerDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = ipresentingofficerrepository.findAll(pageable);
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
			noticePages = ipresentingofficerrepository.findByCurrentUserPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId(), pageable);
		}
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			noticePages = ipresentingofficerrepository.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = ipresentingofficerrepository.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("PresentingOfficerGridList", noticePages);
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

//		try {
//			Long caseid = (Long) request.getSession().getAttribute("caseid");
//			LOG.info(caseid);
//			if (caseid != null) {
//
//				model.addAttribute("presentingofficerdetailsObj",
//						ipresentingofficerrepository.findByCasedetailsId(caseid));
//
//			} else {
//				model.addAttribute("presentingofficerdetailsObj", presentingofficerdetails);
//			}
//		} catch (Exception a) {
//			a.printStackTrace();
//			model.addAttribute("presentingofficerdetailsObj", presentingofficerdetails);
//		}

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
			System.out.println(caseid);
			if (caseid != null) {
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseid)).orElse(new CaseDetails());
				model.addAttribute("cases", cd);
				if(cd != null) {
					if(cd.getIoList().isEmpty()) {
						model.addAttribute("disablepresentingofficer", true);
					} 
					if(cd.getMisConductType() != null && (cd.getMisConductType().getId()==1 || cd.getMisConductType().getId()==1)) {
						model.addAttribute("disableprosecution", true);
					}
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
				}
				if(cd != null) {
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
				}

				// model.addAttribute("employeeList",
//		  employeedetailsrepo.findByCasedetailsId(caseNo);

				// SuspensionDetails
				// susdetails=suspensionDetailsRepo.findByCasedetailsId(caseid);
				// ReInstatedDetails
				// reinstated=reinstatedRepository.findByCasedetailsId(caseid);
				// InquiryOfficerDetails
				// inqofficer=inquiryofficerRepository.findByCasedetailsId(caseid);

				PresentingOfficerDetails presentinofficer = ipresentingofficerrepository.findByCasedetailsId(caseid);
				if (presentinofficer != null) {
					presentinofficer.setCaseNo(presentinofficer.getCasedetails().getCaseNo());

					/* Passing Obj to UI key and value Pair */
					model.addAttribute("presentingofficerdetailsObj", presentinofficer);
				} else {
					presentinofficer = new PresentingOfficerDetails();
					presentinofficer.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new CaseDetails()).getCaseNo());
					model.addAttribute("presentingofficerdetailsObj", presentinofficer);
				}

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = ipresentingofficerrepository.findByCasedetailsId(caseid, pageable);
				}  else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SECRETARYOLD")) {
					
					noticePages = ipresentingofficerrepository.findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(currentUser.getPimsEmployee().getGlobalOrgId(),caseid, pageable);
				}
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
					noticePages = ipresentingofficerrepository.findByCasedetailsIdAndCurrentUserIn(caseid, userList,
							pageable);
				}
				else {
					noticePages = ipresentingofficerrepository.findByCasedetailsIdAndCurrentUser(caseid, currentUser,
							pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("PresentingOfficerGridList", noticePages.getContent());

			} else {
//				model.addAttribute("PresentingOfficerGridList", ipresentingofficerrepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("PresentingOfficerGridList", ipresentingofficerrepository.findAll());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/presentingOfficerDetails";
	}

	/*
	 * Author : Vijay Gupta Method : addinquiryOfficerDetails saving method..
	 * Description : written saving form logic code
	 */

	@PostMapping("/addPresentingOfficerDetails")
	public String addPresentingOfficerDetails(
			@ModelAttribute("presentingofficerdetailsObj") PresentingOfficerDto presentingofficerdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "fileName", required = false) MultipartFile file) {

		// creating new object of Presenting officer details model.
		PresentingOfficerDetails presentingofficerdetails = new PresentingOfficerDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {

			if (presentingofficerdto.getId() != null && presentingofficerdto.getId() > 0) {

				presentingofficerdetails = ipresentingofficerrepository.findById(presentingofficerdto.getId())
						.orElse(new PresentingOfficerDetails());
				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(presentingofficerdto.getCaseNo(),currentUser)) {
					presentingofficerdetails.setCasedetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(presentingofficerdto.getCaseNo(),currentUser));
				}
				presentingofficerdetails.setAppointmentDate(presentingofficerdto.getAppointmentDate());
				presentingofficerdetails
						.setPresentingOfficerAppointed(presentingofficerdto.isPresentingOfficerAppointed());
				presentingofficerdetails.setSevarthId(presentingofficerdto.getSevarthId());
				presentingofficerdetails.setEmployeeId(presentingofficerdto.getEmployeeId());
				presentingofficerdetails.setFirstName(presentingofficerdto.getFirstName());
				presentingofficerdetails.setMiddleName(presentingofficerdto.getMiddleName());
				presentingofficerdetails.setLastName(presentingofficerdto.getLastName());
				presentingofficerdetails.setOfficerName(presentingofficerdto.getOfficerName());
				presentingofficerdetails.setOtherDesignation(presentingofficerdto.getOtherDesignation());
				presentingofficerdetails.setPresentingOfficerDetail(presentingofficerdto.getPresentingOfficerDetail());
				if (presentingofficerdto.getGlobalorg() != null) {
					presentingofficerdetails.setGlobalorg(globalorgRepo
							.findById(presentingofficerdto.getGlobalorg().getId()).orElse(new GlobalOrg()));
				}

				else {
					presentingofficerdetails.setGlobalorg(null);
				}

				if (presentingofficerdto.getSubDepartment() != null) {
					presentingofficerdetails.setSubDepartment(subDepartmentRepo
							.findById(presentingofficerdto.getSubDepartment().getId()).orElse(new SubDepartment()));
				}

				else {
					presentingofficerdetails.setSubDepartment(null);
				}

				if (presentingofficerdto.getDesignation() != null) {
					presentingofficerdetails.setDesignation(
							designationRepo.findById(presentingofficerdto.getDesignation().getDesignation_id())
									.orElse(new Designation()));
				}

				else {
					presentingofficerdetails.setDesignation(null);
				}

				if (presentingofficerdto.getRevenueDivision() != null) {
					presentingofficerdetails.setRevenueDivision(presentingofficerdto.getRevenueDivision());
				}

				else {
					presentingofficerdetails.setRevenueDivision(null);
				}

				presentingofficerdetails.setMobileNumber(presentingofficerdto.getMobileNumber());
				presentingofficerdetails.setEmail(presentingofficerdto.getEmail());

				presentingofficerdetails = ipresentingofficerrepository.save(presentingofficerdetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editPresentingOfficerDetails/"+presentingofficerdetails.getId()+"/1";
			}

			else {

				/*
				 * // presentingofficerdetails.setCasedetails(caseDetailsRepo.findById(
				 * presentingofficerdetails.getCasedetails().getId()).orElse(new //
				 * CaseDetails())); presentingofficerdetails.setCasedetails(caseDetailsRepo.
				 * findByCaseNoContainingIgnoreCase( presentingofficerdetails.getCaseNo()));
				 * 
				 * this code is checking on Case Details screen at case id is exist or not
				 * 
				 * if
				 * (ipresentingofficerrepository.existsByCasedetails(presentingofficerdetails.
				 * getCasedetails())) {
				 * 
				 * presentingofficerdetails =
				 * ipresentingofficerrepository.findByCasedetails(presentingofficerdetails.
				 * getCasedetails()); }
				 */

				// save

				CaseDetails cd = caseDetailsRepo.findByCaseNo(presentingofficerdto.getCaseNo())
						;

				if (cd != null) {
					presentingofficerdetails.setCasedetails(cd);
				} else {
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
					return "redirect:/departmentalEnquiry/presentingofficerdetails/1";

				}
				if (StringUtils.isNotBlank(presentingofficerdto.getSevarthId()) && ipresentingofficerrepository
						.existsByCasedetailsAndSevarthId(cd, presentingofficerdto.getSevarthId())) {
					presentingofficerdetails = ipresentingofficerrepository.findByCasedetailsAndSevarthId(cd,
							presentingofficerdto.getSevarthId());
				}
				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					presentingofficerdetails.setCurrentUser(currentUser);
					presentingofficerdetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				presentingofficerdetails.setOfficerName(presentingofficerdto.getOfficerName());
				presentingofficerdetails.setAppointmentDate(presentingofficerdto.getAppointmentDate());
				presentingofficerdetails
						.setPresentingOfficerAppointed(presentingofficerdto.isPresentingOfficerAppointed());
				presentingofficerdetails.setSevarthId(presentingofficerdto.getSevarthId());
				presentingofficerdetails.setEmployeeId(presentingofficerdto.getEmployeeId());
				presentingofficerdetails.setFirstName(presentingofficerdto.getFirstName());
				presentingofficerdetails.setMiddleName(presentingofficerdto.getMiddleName());
				presentingofficerdetails.setLastName(presentingofficerdto.getLastName());
				presentingofficerdetails.setPresentingOfficerDetail(presentingofficerdto.getPresentingOfficerDetail());
				presentingofficerdetails.setOtherDesignation(presentingofficerdto.getOtherDesignation());
				if (presentingofficerdto.getGlobalorg() != null) {
					presentingofficerdetails.setGlobalorg(globalorgRepo
							.findById(presentingofficerdto.getGlobalorg().getId()).orElse(new GlobalOrg()));
				}

				else {
					presentingofficerdetails.setGlobalorg(null);
				}

				if (presentingofficerdto.getSubDepartment() != null) {
					presentingofficerdetails.setSubDepartment(subDepartmentRepo
							.findById(presentingofficerdto.getSubDepartment().getId()).orElse(new SubDepartment()));
				}

				else {
					presentingofficerdetails.setSubDepartment(null);
				}

				if (presentingofficerdto.getDesignation() != null) {
					presentingofficerdetails.setDesignation(
							designationRepo.findById(presentingofficerdto.getDesignation().getDesignation_id())
									.orElse(new Designation()));
				}

				else {
					presentingofficerdetails.setDesignation(null);
				}

				if (presentingofficerdto.getRevenueDivision() != null) {
					presentingofficerdetails.setRevenueDivision(presentingofficerdto.getRevenueDivision());
				}

				else {
					presentingofficerdetails.setRevenueDivision(null);
				}

				presentingofficerdetails.setMobileNumber(presentingofficerdto.getMobileNumber());
				presentingofficerdetails.setEmail(presentingofficerdto.getEmail());
//				presentingofficerdetails.setRevenueDivision(presentingofficerdto.getRevenueDivision());
				presentingofficerdetails = ipresentingofficerrepository.save(presentingofficerdetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editPresentingOfficerDetails/"+presentingofficerdetails.getId()+"/1";

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/presentingofficerdetails/1";

	}

	/* Landing Prosecution Proposal Details form Method */

	@GetMapping("/prosecutionProposaldetails/{page}")
	public String prosecutionProposaldetails(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		LOG.info("Hi this is the prosecution Proposal details  page method.............");

		ProsecutionProposalDetails prosecutionproposaldetails = new ProsecutionProposalDetails();

		model.addAttribute("ProsecutionProposalObj", prosecutionproposaldetails);

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<ProsecutionProposalDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = iprosecutionproposaldetailsRepository.findAll(pageable);
		}else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
			noticePages = iprosecutionproposaldetailsRepository.findByCurrentUserPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId(), pageable);
		}
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			noticePages = iprosecutionproposaldetailsRepository.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = iprosecutionproposaldetailsRepository.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("ProsecutionDetailsGridList", noticePages);
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

		/*
		 * try { Long caseid = (Long) request.getSession().getAttribute("caseid");
		 * LOG.info(caseid); if (caseid != null) {
		 * 
		 * model.addAttribute("ProsecutionProposalObj",
		 * iprosecutionproposaldetailsRepository.findByCasedetailsId(caseid));
		 * 
		 * } else { model.addAttribute("ProsecutionProposalObj",
		 * prosecutionproposaldetails); } } catch (Exception a) { a.printStackTrace();
		 * model.addAttribute("ProsecutionProposalObj", prosecutionproposaldetails); }
		 */

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
			System.out.println(caseid);
			if (caseid != null) {
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseid)).orElse(new CaseDetails());
				if(cd != null) {
					if(cd.getIoList().isEmpty()) {
						model.addAttribute("disablepresentingofficer", true);
					} 
					if(cd.getMisConductType() != null && (cd.getMisConductType().getId()==1 || cd.getMisConductType().getId()==1)) {
						model.addAttribute("disableprosecution", true);
					}
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
					
				}
				// model.addAttribute("employeeList",
//		  employeedetailsrepo.findByCasedetailsId(caseNo);

				// SuspensionDetails
				// susdetails=suspensionDetailsRepo.findByCasedetailsId(caseid);
				// ReInstatedDetails
				// reinstated=reinstatedRepository.findByCasedetailsId(caseid);
				// InquiryOfficerDetails
				// inqofficer=inquiryofficerRepository.findByCasedetailsId(caseid);
				// PresentingOfficerDetails
				// presentinofficer=ipresentingofficerrepository.findByCasedetailsId(caseid);
//				ProsecutionProposalDetails prosecutionproposaldetail = iprosecutionproposaldetailsRepository
//						.findByCasedetailsId(caseid);
//				if (prosecutionproposaldetail != null) {
//					prosecutionproposaldetail.setCaseNo(prosecutionproposaldetail.getCasedetails().getCaseNo());
//					CaseDetails caseDetails = prosecutionproposaldetail.getCasedetails();
//					if(!caseDetails.getEmployeeList().isEmpty()){
//						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
//							if(emp.getFirstName().equalsIgnoreCase(reinstationobj1.getFirstName()) 
//									&& emp.getMiddleName().equalsIgnoreCase(reinstationobj1.getMiddleName())
//									&& emp.getLastName().equalsIgnoreCase(reinstationobj1.getLastName())) {
//								reinstationobj1.setEmployeeDataId(emp.getId().toString());
//							}
//						}
//						model.addAttribute("employeeList", caseDetails.getEmployeeList());
//					}
//
//					/* Passing Obj to UI key and value Pair */
//					model.addAttribute("ProsecutionProposalObj", prosecutionproposaldetail);
//				} else {
//					prosecutionproposaldetail = new ProsecutionProposalDetails();
//					prosecutionproposaldetail
//							.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new CaseDetails()).getCaseNo());
//					model.addAttribute("ProsecutionProposalObj", prosecutionproposaldetail);
//				}
				List<ProsecutionProposalDetails> susdetails = iprosecutionproposaldetailsRepository.findByCasedetailsId(caseid);
				if (susdetails != null && !susdetails.isEmpty()) {
					ProsecutionProposalDetails suspensionobj1 = susdetails.get(0);
					CaseDetails caseDetails = suspensionobj1.getCasedetails();

					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						suspensionobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						suspensionobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						suspensionobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						suspensionobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					suspensionobj1.setCaseNo(susdetails.get(0).getCasedetails().getCaseNo());
					
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(suspensionobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(suspensionobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(suspensionobj1.getLastName())) {
								suspensionobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}	
					
					model.addAttribute("ProsecutionProposalObj", suspensionobj1);
				}

				else {
					ProsecutionProposalDetails suspensionobj1 = new ProsecutionProposalDetails();

					CaseDetails caseDetails = caseDetailsRepo.findById(caseid).orElse(new CaseDetails());
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						suspensionobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						suspensionobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						suspensionobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						suspensionobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					suspensionobj1.setCaseNo(caseDetails.getCaseNo());

					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(suspensionobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(suspensionobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(suspensionobj1.getLastName())) {
								suspensionobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}	

					model.addAttribute("ProsecutionProposalObj", suspensionobj1);
				}
				

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = iprosecutionproposaldetailsRepository.findByCasedetailsId(caseid, pageable);
				} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SECRETARYOLD")) {
					
					noticePages = iprosecutionproposaldetailsRepository.findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(currentUser.getPimsEmployee().getGlobalOrgId(),caseid, pageable);
				}
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
					noticePages = iprosecutionproposaldetailsRepository.findByCasedetailsIdAndCurrentUserIn(caseid,
							userList, pageable);
				}
				else {
					noticePages = iprosecutionproposaldetailsRepository.findByCasedetailsIdAndCurrentUser(caseid,
							currentUser, pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("ProsecutionDetailsGridList", noticePages.getContent());

			} else {
//				model.addAttribute("ProsecutionDetailsGridList", iprosecutionproposaldetailsRepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("ProsecutionDetailsGridList", iprosecutionproposaldetailsRepository.findAll());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/prosecutionProposalDetails";
	}

	/*
	 * Author : Vijay Gupta Method : addinquiryOfficerDetails saving method..
	 * Description : written saving form logic code
	 */

	@PostMapping("/addProsecutionProposalDetails")
	public String addProsecutionProposalDetails(
			@ModelAttribute("ProsecutionProposalObj") ProsecutionProposalDetailsDto prosecutionproposaldetailsdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "finalcourtorder", required = false) MultipartFile finalcourtorderfile,
			@RequestParam(name = "filedata", required = false) MultipartFile file) {

		// creating new object of ProsecutionProposalDetails model
		ProsecutionProposalDetails prosecutionproposaldetails = new ProsecutionProposalDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (prosecutionproposaldetailsdto.getId() != null && prosecutionproposaldetailsdto.getId() > 0) {

				// checking id of prosecution proposal details
				prosecutionproposaldetails = iprosecutionproposaldetailsRepository
						.findById(prosecutionproposaldetailsdto.getId()).orElse(new ProsecutionProposalDetails());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(prosecutionproposaldetailsdto.getCaseNo(),currentUser)) {
					prosecutionproposaldetails.setCasedetails(caseDetailsRepo
							.findByCaseNoAndCurrentUser(prosecutionproposaldetailsdto.getCaseNo(),currentUser));
				}
				if(StringUtils.isNotBlank(prosecutionproposaldetailsdto.getEmployeeDataId())) {
					if(iprosecutionproposaldetailsRepository.existsByCasedetailsAndEmpDataId(prosecutionproposaldetails.getCasedetails(), Long.valueOf(prosecutionproposaldetailsdto.getEmployeeDataId()))) {
						prosecutionproposaldetails = iprosecutionproposaldetailsRepository.findByCasedetailsAndEmpDataId(prosecutionproposaldetails.getCasedetails(), Long.valueOf(prosecutionproposaldetailsdto.getEmployeeDataId()));
					} else {
						prosecutionproposaldetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(prosecutionproposaldetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}
				

				/* added on 18/10/2022 as per DE requirements */
				prosecutionproposaldetails.setRemark(prosecutionproposaldetailsdto.getRemark());
				prosecutionproposaldetails.setCourtName(prosecutionproposaldetailsdto.getCourtName());
				prosecutionproposaldetails.setCourtCaseNo(prosecutionproposaldetailsdto.getCourtCaseNo());
				prosecutionproposaldetails.setSevarthId(prosecutionproposaldetailsdto.getSevarthId());
				prosecutionproposaldetails.setFirstName(prosecutionproposaldetailsdto.getFirstName());
				prosecutionproposaldetails.setMiddleName(prosecutionproposaldetailsdto.getMiddleName());
				prosecutionproposaldetails.setLastName(prosecutionproposaldetailsdto.getLastName());
				prosecutionproposaldetails
						.setProsecutionProposalReceived(prosecutionproposaldetailsdto.isProsecutionProposalReceived());
				prosecutionproposaldetails.setDateOfReceipt(prosecutionproposaldetailsdto.getDateOfReceipt());
				prosecutionproposaldetails.setStatusForGrantedDeniedPending(
						prosecutionproposaldetailsdto.getStatusForGrantedDeniedPending());
				prosecutionproposaldetails.setDateOfOrderReceipt(prosecutionproposaldetailsdto.getDateOfOrderReceipt());
				prosecutionproposaldetails
						.setDateOfCommunication(prosecutionproposaldetailsdto.getDateOfCommunication());
				prosecutionproposaldetails
						.setWhetherCaseFiledCourt(prosecutionproposaldetailsdto.isWhetherCaseFiledCourt());
				prosecutionproposaldetails
						.setDateOfOrderCaseFiled(prosecutionproposaldetailsdto.getDateOfOrderCaseFiled());
				// added fields according to new pdf
				prosecutionproposaldetails
						.setWhetherCaseFiledIntheCourt(prosecutionproposaldetailsdto.isWhetherCaseFiledIntheCourt());

				if (prosecutionproposaldetailsdto.getStatus() != null) {
					prosecutionproposaldetails.setStatus(prosecutionproposaldetailsdto.getStatus());
				}

				else {
					prosecutionproposaldetails.setStatus(null);

				}

				prosecutionproposaldetails
						.setDateOfOrderoftheCourt(prosecutionproposaldetailsdto.getDateOfOrderoftheCourt());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ prosecutionproposaldetails.getCasedetails().getId() + "/" + "prosecutionProposalDetails/"
							+ prosecutionproposaldetails.getId() + "/";
					prosecutionproposaldetails.setFileNameUploadCopy(file.getOriginalFilename());
					prosecutionproposaldetails.setContentType(file.getContentType());
					prosecutionproposaldetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, prosecutionproposaldetails.getFileNameUploadCopy());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				
				if (finalcourtorderfile != null && !finalcourtorderfile.isEmpty()
						&& finalcourtorderfile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
//					String id = null;
//					if (prosecutionproposaldetails.getCasedetails() != null) {
//						id = prosecutionproposaldetails.getCasedetails().getId().toString();
//					} else {
//						id = prosecutionproposaldetails.getFileNo();
//					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ prosecutionproposaldetails.getCasedetails().getId() + "/" + "prosecutionProposalDetails/"
							+ prosecutionproposaldetails.getId() + "/";
					prosecutionproposaldetails.setFinalcourtordercopy(finalcourtorderfile.getOriginalFilename());
					prosecutionproposaldetails.setContentTypefinalcourt(finalcourtorderfile.getContentType());
					prosecutionproposaldetails.setFilepathfinalcourt(filePath);
					fileService.fileUpload(filePath, finalcourtorderfile, prosecutionproposaldetails.getFinalcourtordercopy());
				}
				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				
//				if (finalcourtorderfile != null && !finalcourtorderfile.isEmpty()
//						&& finalcourtorderfile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
//
//					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
//							+ prosecutionproposaldetails.getCasedetails().getId() + "/" + "prosecutionProposalDetails/"
//							+ prosecutionproposaldetails.getId() + "/";
//					prosecutionproposaldetails.setFileNameUploadCopy(file.getOriginalFilename());
//					prosecutionproposaldetails.setContentType(file.getContentType());
//					prosecutionproposaldetails.setFilepath(filePath);
//					fileService.fileUpload(filePath, file, prosecutionproposaldetails.getFileNameUploadCopy());
//
//				}
//
//				else {
//					redirect.addFlashAttribute("error", "Please select correct pdf file");
//				}

				prosecutionproposaldetails = iprosecutionproposaldetailsRepository.save(prosecutionproposaldetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editProsecutionProposalDtls/"+prosecutionproposaldetails.getId()+"/1";

			}

			else {
				if (StringUtils.isBlank(prosecutionproposaldetailsdto.getFileNo())) {
					CaseDetails cd = caseDetailsRepo.findByCaseNo(prosecutionproposaldetailsdto.getCaseNo());

					if (cd != null) {
						prosecutionproposaldetails.setCasedetails(cd);
					} else {
						redirect.addFlashAttribute(departmentconstants.MESSAGE,
								messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
						return "redirect:/departmentalEnquiry/prosecutionProposaldetails/1";
					}

					if (StringUtils.isNotBlank(prosecutionproposaldetailsdto.getSevarthId()) && prosecutionRepo
							.existsByCasedetailsAndSevarthId(cd, prosecutionproposaldetailsdto.getSevarthId())) {
						prosecutionproposaldetails = prosecutionRepo.findByCasedetailsAndSevarthId(cd,
								prosecutionproposaldetailsdto.getSevarthId());
					}
				}

				CaseDetails cd = caseDetailsRepo
						.findByCaseNo(prosecutionproposaldetailsdto.getCaseNo());

				if (cd != null) {
					prosecutionproposaldetails.setCasedetails(cd);
				} else {
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
					return "redirect:/departmentalEnquiry/prosecutionProposaldetails/1";
				}
				
				if(StringUtils.isNotBlank(prosecutionproposaldetailsdto.getEmployeeDataId())) {
					if(iprosecutionproposaldetailsRepository.existsByCasedetailsAndEmpDataId(prosecutionproposaldetails.getCasedetails(), Long.valueOf(prosecutionproposaldetailsdto.getEmployeeDataId()))) {
						prosecutionproposaldetails = iprosecutionproposaldetailsRepository.findByCasedetailsAndEmpDataId(prosecutionproposaldetails.getCasedetails(), Long.valueOf(prosecutionproposaldetailsdto.getEmployeeDataId()));
					} else {
						prosecutionproposaldetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(prosecutionproposaldetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

//				if (iprosecutionproposaldetailsRepository
//						.existsByCasedetails(prosecutionproposaldetails.getCasedetails())) {
//					prosecutionproposaldetails = iprosecutionproposaldetailsRepository
//							.findByCasedetails(prosecutionproposaldetails.getCasedetails());
//
//				}

				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					prosecutionproposaldetails.setCurrentUser(currentUser);
					prosecutionproposaldetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}

				/* added on 18/10/2022 as per DE requirements */
				prosecutionproposaldetails.setRemark(prosecutionproposaldetailsdto.getRemark());
				prosecutionproposaldetails.setCourtName(prosecutionproposaldetailsdto.getCourtName());
				prosecutionproposaldetails.setCourtCaseNo(prosecutionproposaldetailsdto.getCourtCaseNo());
				prosecutionproposaldetails.setSevarthId(prosecutionproposaldetailsdto.getSevarthId());
				prosecutionproposaldetails.setFirstName(prosecutionproposaldetailsdto.getFirstName());
				prosecutionproposaldetails.setMiddleName(prosecutionproposaldetailsdto.getMiddleName());
				prosecutionproposaldetails.setLastName(prosecutionproposaldetailsdto.getLastName());
				prosecutionproposaldetails
						.setProsecutionProposalReceived(prosecutionproposaldetailsdto.isProsecutionProposalReceived());
				prosecutionproposaldetails.setDateOfReceipt(prosecutionproposaldetailsdto.getDateOfReceipt());
				prosecutionproposaldetails.setStatusForGrantedDeniedPending(
						prosecutionproposaldetailsdto.getStatusForGrantedDeniedPending());
				prosecutionproposaldetails.setDateOfOrderReceipt(prosecutionproposaldetailsdto.getDateOfOrderReceipt());
				prosecutionproposaldetails
						.setDateOfCommunication(prosecutionproposaldetailsdto.getDateOfCommunication());
//				prosecutionproposaldetails.setFileNameUploadCopy(prosecutionproposaldetailsdto.getFileNameUploadCopy());
				prosecutionproposaldetails
						.setWhetherCaseFiledCourt(prosecutionproposaldetailsdto.isWhetherCaseFiledCourt());
				prosecutionproposaldetails
						.setDateOfOrderCaseFiled(prosecutionproposaldetailsdto.getDateOfOrderCaseFiled());

				// added fields according to new pdf
				prosecutionproposaldetails
						.setWhetherCaseFiledIntheCourt(prosecutionproposaldetailsdto.isWhetherCaseFiledIntheCourt());
//				prosecutionproposaldetails.setStatus(prosecutionproposaldetailsdto.getStatus());

				if (prosecutionproposaldetailsdto.getStatus() != null) {
					prosecutionproposaldetails.setStatus(prosecutionproposaldetailsdto.getStatus());
				}

				else {
					prosecutionproposaldetails.setStatus(null);

				}

				prosecutionproposaldetails
						.setDateOfOrderoftheCourt(prosecutionproposaldetailsdto.getDateOfOrderoftheCourt());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ prosecutionproposaldetails.getCasedetails().getId() + "/" + "prosecutionProposalDetails/"
							+ prosecutionproposaldetails.getId() + "/";
					prosecutionproposaldetails.setFileNameUploadCopy(file.getOriginalFilename());
					prosecutionproposaldetails.setContentType(file.getContentType());
					prosecutionproposaldetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, prosecutionproposaldetails.getFileNameUploadCopy());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				
				if (finalcourtorderfile != null && !finalcourtorderfile.isEmpty()
						&& finalcourtorderfile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (prosecutionproposaldetails.getCasedetails() != null) {
						id = prosecutionproposaldetails.getCasedetails().getId().toString();
					} else {
						id = prosecutionproposaldetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "prosecutiondetails/" + prosecutionproposaldetails.getId() + "/";
					prosecutionproposaldetails.setFinalcourtordercopy(finalcourtorderfile.getOriginalFilename());
					prosecutionproposaldetails.setContentTypefinalcourt(finalcourtorderfile.getContentType());
					prosecutionproposaldetails.setFilepathfinalcourt(filePath);
					fileService.fileUpload(filePath, finalcourtorderfile, prosecutionproposaldetails.getFinalcourtordercopy());
				}
				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				prosecutionproposaldetails = iprosecutionproposaldetailsRepository.save(prosecutionproposaldetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editProsecutionProposalDtls/"+prosecutionproposaldetails.getId()+"/1";
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/prosecutionProposaldetails/1";

	}

	/* Landing form court case Method . */

	@GetMapping("/courtCasedetails/{page}")
	public String courtCasedetails(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		LOG.info("Hi this is the court case details  page method.............");

		CourtCaseDetails courtcasedetails = new CourtCaseDetails();

		model.addAttribute("CourtCaseObj", courtcasedetails);
		List<CourtName> courtname = icourtnamerepository.findByOrderById();

		model.addAttribute("CourtcaseList", icourtcasedetailsRepository.findAll());
		
		model.addAttribute("CourtNameList", courtname);

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<CourtCaseDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = icourtcasedetailsRepository.findAll(pageable);
		} else {
			noticePages = icourtcasedetailsRepository.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("CourtCaseDetailsGridList", noticePages);
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

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
//			System.out.println(caseid);
			if (caseid != null) {

				/*------------------------------------------------------------------------------*/

				List<CourtCaseDetails> courtcasedtls = icourtcasedetailsRepository.findByCasedetailsId(caseid);
				if (courtcasedtls != null && !courtcasedtls.isEmpty()) {
					CourtCaseDetails courtcasedetails1 = courtcasedtls.get(0);
					CaseDetails caseDetails = courtcasedetails1.getCasedetails();

					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						courtcasedetails1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						// courtcasedetails1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						courtcasedetails1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						courtcasedetails1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						courtcasedetails1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(courtcasedetails1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(courtcasedetails1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(courtcasedetails1.getLastName())) {
								courtcasedetails1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}
					courtcasedetails1.setCaseNo(courtcasedtls.get(0).getCasedetails().getCaseNo());
					model.addAttribute("CourtCaseObj", courtcasedetails1);
				}

				else {
					CourtCaseDetails courtcasedetails1 = new CourtCaseDetails();

					CaseDetails caseDetails = caseDetailsRepo.findById(caseid).orElse(new CaseDetails());
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						courtcasedetails1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						// courtcasedetails1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						courtcasedetails1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						courtcasedetails1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						courtcasedetails1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(courtcasedetails1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(courtcasedetails1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(courtcasedetails1.getLastName())) {
								courtcasedetails1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}
					courtcasedetails1.setCaseNo(caseDetails.getCaseNo());

					model.addAttribute("CourtCaseObj", courtcasedetails1);
				}

				// model.addAttribute("employeeList",
//		  employeedetailsrepo.findByCasedetailsId(caseNo);

				// ProsecutionProposalDetails
				// prosecutionproposaldetail=iprosecutionproposaldetailsRepository.findByCasedetailsId(caseid);
				/*
				 * List<CourtCaseDetails> courtcasedtls =
				 * icourtcasedetailsRepository.findByCasedetailsId(caseid); if
				 * (!courtcasedtls.isEmpty()) {
				 * courtcasedtls.get(0).setCaseNo(courtcasedtls.get(0).getCasedetails().
				 * getCaseNo());
				 * 
				 * Passing Obj to UI key and value Pair model.addAttribute("CourtCaseObj",
				 * courtcasedtls.get(0)); } else { CourtCaseDetails court = new
				 * CourtCaseDetails();
				 * court.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new
				 * CaseDetails()).getCaseNo()); model.addAttribute("CourtCaseObj", court); }
				 */

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = icourtcasedetailsRepository.findByCasedetailsId(caseid, pageable);
				} else {
					noticePages = icourtcasedetailsRepository.findByCasedetailsIdAndCurrentUser(caseid, currentUser,
							pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("CourtCaseDetailsGridList", noticePages.getContent());

			} else {
//				model.addAttribute("CourtcaseList", icourtcasedetailsRepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("CourtcaseList", new ArrayList<CourtCaseDetails>());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/courtCaseDetails";
	}

	/*
	 * Method : addCourtCaseDetails saving method.. Description : written saving
	 * form logic code
	 */

	@PostMapping("/addCourtCaseDetails")
	public String addCourtCaseDetails(@ModelAttribute("CourtCaseObj") CourtCaseDetailsDto courtcasedetailsdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "uploadOder", required = false) MultipartFile file) {

		// creating new object of ProsecutionProposalDetails model
		CourtCaseDetails courtcasedetails = new CourtCaseDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (courtcasedetailsdto.getId() != null && courtcasedetailsdto.getId() > 0) {

				// for update
				courtcasedetails = icourtcasedetailsRepository.findById(courtcasedetailsdto.getId())
						.orElse(new CourtCaseDetails());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(courtcasedetailsdto.getCaseNo(),currentUser)) {
					courtcasedetails.setCasedetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(courtcasedetailsdto.getCaseNo(),currentUser));
				}
				
				if(StringUtils.isNotBlank(courtcasedetailsdto.getEmployeeDataId())) {
					if(icourtcasedetailsRepository.existsByCasedetailsAndEmpDataId(courtcasedetails.getCasedetails(), Long.valueOf(courtcasedetailsdto.getEmployeeDataId()))) {
						courtcasedetails = icourtcasedetailsRepository.findByCasedetailsAndEmpDataId(courtcasedetails.getCasedetails(), Long.valueOf(courtcasedetailsdto.getEmployeeDataId()));
					} else {
						courtcasedetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(courtcasedetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

				/* courtcasedetails.setCasedetails(courtcasedetailsdto.getCasedetails()); */
				courtcasedetails.setSevarthId(courtcasedetailsdto.getSevarthId());
				courtcasedetails.setFirstName(courtcasedetailsdto.getFirstName());
				courtcasedetails.setMiddleName(courtcasedetailsdto.getMiddleName());
				courtcasedetails.setLastName(courtcasedetailsdto.getLastName());
				courtcasedetails.setCourtName(courtcasedetailsdto.getCourtName());

				courtcasedetails.setAnyRelatedCourtCaseExist(courtcasedetailsdto.isAnyRelatedCourtCaseExist());
				courtcasedetails.setCourtCaseNo(courtcasedetailsdto.getCourtCaseNo());
				courtcasedetails.setCourtCaseFiledDate(courtcasedetailsdto.getCourtCaseFiledDate());

				if (courtcasedetailsdto.getCourtType() != null) {
					courtcasedetails.setCourtType(icourtnamerepository
							.findById(courtcasedetailsdto.getCourtType().getId()).orElse(new CourtName()));
				}

				else {
					courtcasedetails.setCourtType(null);

				}

				// employeedetails.setGender(genderRepo.findById(employeedetailsdto.getGender().getId()).orElse(new
				// Gender()));
				// courtcasedetails.setCourtName(courtcasedetailsdto.getCourtName().getCourtName());
				if (courtcasedetailsdto.getStatus() != null) {
					courtcasedetails.setStatus(courtcasedetailsdto.getStatus());
				}

				else {
					courtcasedetails.setStatus(null);

				}

				courtcasedetails.setDateOfFinalOrder(courtcasedetailsdto.getDateOfFinalOrder());
				// courtcasedetails.setUploadOder(courtcasedetailsdto.getUploadOder());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ courtcasedetails.getCasedetails().getId() + "/" + "courtcaseDetails/"
							+ courtcasedetails.getId() + "/";
					courtcasedetails.setUploadOder(file.getOriginalFilename());
					courtcasedetails.setContentType(file.getContentType());
					courtcasedetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, courtcasedetails.getUploadOder());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
              
				icourtcasedetailsRepository.save(courtcasedetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));

			}

			else {

				// courtcasedetails.setCasedetails(caseDetailsRepo.findById(courtcasedetailsdto.getCasedetails().getId()).orElse(new
				// CaseDetails()));

				/*
				 * courtcasedetails.setCasedetails(caseDetailsRepo.
				 * findByCaseNoContainingIgnoreCase( courtcasedetailsdto.getCaseNo()));
				 * 
				 * this code is checking on Case Details screen at case id is exist or not if
				 * (icourtcasedetailsRepository.existsByCasedetails(courtcasedetails.
				 * getCasedetails())) {
				 * 
				 * courtcasedetails =
				 * icourtcasedetailsRepository.findByCasedetails(courtcasedetails.getCasedetails
				 * ()); }
				 */

				// save
				CaseDetails cd = caseDetailsRepo.findByCaseNo(courtcasedetailsdto.getCaseNo())
						;
				if (cd != null) {
					courtcasedetails.setCasedetails(cd);
				} else {
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
					return "redirect:/departmentalEnquiry/courtCasedetails/1";
				}
//				if (StringUtils.isNotBlank(courtcasedetailsdto.getSevarthId()) && icourtcasedetailsRepository
//						.existsByCasedetailsAndSevarthId(cd, courtcasedetailsdto.getSevarthId())) {
//					courtcasedetails = icourtcasedetailsRepository.findByCasedetailsAndSevarthId(cd,
//							courtcasedetailsdto.getSevarthId());
//				}
				if(StringUtils.isNotBlank(courtcasedetailsdto.getEmployeeDataId())) {
					if(icourtcasedetailsRepository.existsByCasedetailsAndEmpDataId(courtcasedetails.getCasedetails(), Long.valueOf(courtcasedetailsdto.getEmployeeDataId()))) {
						courtcasedetails = icourtcasedetailsRepository.findByCasedetailsAndEmpDataId(courtcasedetails.getCasedetails(), Long.valueOf(courtcasedetailsdto.getEmployeeDataId()));
					} else {
						courtcasedetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(courtcasedetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}
				

				// added on 29/09/2022 ,
		//		User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					courtcasedetails.setCurrentUser(currentUser);
					courtcasedetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				courtcasedetails.setSevarthId(courtcasedetailsdto.getSevarthId());
				courtcasedetails.setFirstName(courtcasedetailsdto.getFirstName());
				courtcasedetails.setMiddleName(courtcasedetailsdto.getMiddleName());
				courtcasedetails.setLastName(courtcasedetailsdto.getLastName());
				courtcasedetails.setAnyRelatedCourtCaseExist(courtcasedetailsdto.isAnyRelatedCourtCaseExist());
				courtcasedetails.setCourtCaseNo(courtcasedetailsdto.getCourtCaseNo());
				courtcasedetails.setCourtCaseFiledDate(courtcasedetailsdto.getCourtCaseFiledDate());
				courtcasedetails.setCourtName(courtcasedetailsdto.getCourtName());

				if (courtcasedetailsdto.getCourtType() != null) {
					courtcasedetails.setCourtType(icourtnamerepository
							.findById(courtcasedetailsdto.getCourtType().getId()).orElse(new CourtName()));
				}

				else {
					courtcasedetails.setCourtType(null);

				}

				if (courtcasedetailsdto.getStatus() != null) {
					courtcasedetails.setStatus(courtcasedetailsdto.getStatus());
				}

				else {
					courtcasedetails.setStatus(null);

				}

				courtcasedetails.setDateOfFinalOrder(courtcasedetailsdto.getDateOfFinalOrder());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ courtcasedetails.getCasedetails().getId() + "/" + "courtcaseDetails/"
							+ courtcasedetails.getId() + "/";
					courtcasedetails.setUploadOder(file.getOriginalFilename());
					courtcasedetails.setContentType(file.getContentType());
					courtcasedetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, courtcasedetails.getUploadOder());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				icourtcasedetailsRepository.save(courtcasedetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/courtCasedetails/1";

	}

	/*
	 * fetching records of employee details methods
	 * 
	 * @GetMapping("/editEmployeeDetails/{id}/{page}") public String
	 * editEmployeeDetails(@PathVariable("id") long id, Model
	 * model,@PathVariable("page") int page) {
	 * LOG.info("hi this is the fetching of Employee details Records! ");
	 * 
	 * 
	 * EmployeeDetails empdtls = employeedetailsrepo.findById(id).orElse(new
	 * EmployeeDetails());
	 * 
	 * 
	 * model.addAttribute("employeeDetails", empdtls);
	 * 
	 * model.addAttribute("gender", genderRepo.findAll());
	 * model.addAttribute("serviceGroup", serviceGrpRepo.findAll());
	 * model.addAttribute("designation", designationRepo.findAll());
	 * model.addAttribute("globalOrgList1", globalorgRepo.findAll());
	 * model.addAttribute("subDepartmentlist", subDepartmentRepo.findAll());
	 * 
	 * // request.getSession().setAttribute("employeeId", empdtls); try {
	 * 
	 * if (empdtls.getCasedetails() != null) {
	 * 
	 * int index = 1;
	 * 
	 * if (page != 0) { index = (page * 10) - (10 - 1);
	 * model.addAttribute("pageIndex", index);
	 * 
	 * }
	 * 
	 * if (page < 0) {
	 * 
	 * page = 1; }
	 * 
	 * Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC,
	 * "id"));
	 * 
	 * Page<EmployeeDetails> noticePages =
	 * employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId(),
	 * pageable);
	 * 
	 * model.addAttribute("employeeList", noticePages);
	 * model.addAttribute("currentPage", page); int begin = Math.max(1, page - 5);
	 * int end = Math.min(begin + 4, noticePages.getTotalPages());
	 * model.addAttribute("begin", begin); model.addAttribute("end", end);
	 * model.addAttribute("totalPages", noticePages.getTotalPages());
	 * model.addAttribute("totalItems", noticePages.getTotalElements());
	 * model.addAttribute("noticelist", noticePages.getContent());
	 * 
	 * // model.addAttribute("employeeList", //
	 * employeedetailsrepo.findByCasedetailsId(empdtls.getCasedetails().getId()));
	 * 
	 * } else { model.addAttribute("employeeList", employeedetailsrepo.findAll()); }
	 * } catch (Exception a) { a.printStackTrace();
	 * model.addAttribute("employeeList", employeedetailsrepo.findAll()); }
	 * 
	 * return "DeptInquiryViews/delinquentEmployeesDetails"; }
	 */

	/* Landing form kept case Abeyance Method . */

	@GetMapping("/keptInAbeyanceCasesdetails/{page}")
	public String keptInAbeyanceCasesdetails(Model model, RedirectAttributes redirect, @PathVariable("page") int page) {
		LOG.info("Hi this is the keptInAbeyanceCasesdetails  page method.............");

		DetailsKeptAbeyanceCases detailskeptabeyancecases = new DetailsKeptAbeyanceCases();
		model.addAttribute("addDetailsKeptAbeyanceCasesObj", detailskeptabeyancecases);
		model.addAttribute("reasonkeptList", reasonforkeepingcaseabeyanceRepository.findAll());

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<DetailsKeptAbeyanceCases> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = detailskeptabeyancecasesRepository.findAll(pageable);
		} else {
			noticePages = detailskeptabeyancecasesRepository.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("DetailsKeptAbeyanceCasesGridList", noticePages);
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

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
			System.out.println(caseid);
			if (caseid != null) {

				/*------------------------------------------------------------------------------*/

				List<DetailsKeptAbeyanceCases> detailskeptdetails = detailskeptabeyancecasesRepository
						.findByCasedetailsId(caseid);
				if (detailskeptdetails != null && !detailskeptdetails.isEmpty()) {
					DetailsKeptAbeyanceCases keptobj1 = detailskeptdetails.get(0);
					CaseDetails caseDetails = keptobj1.getCasedetails();

					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						keptobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						keptobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						keptobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						keptobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						keptobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(keptobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(keptobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(keptobj1.getLastName())) {
								keptobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}
					keptobj1.setCaseNo(detailskeptdetails.get(0).getCasedetails().getCaseNo());
					model.addAttribute("addDetailsKeptAbeyanceCasesObj", keptobj1);
				}

				else {
					DetailsKeptAbeyanceCases keptsobj1 = new DetailsKeptAbeyanceCases();

					CaseDetails caseDetails = caseDetailsRepo.findById(caseid).orElse(new CaseDetails());
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						keptsobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						keptsobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						keptsobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						keptsobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						keptsobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(keptsobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(keptsobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(keptsobj1.getLastName())) {
								keptsobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}
					keptsobj1.setCaseNo(caseDetails.getCaseNo());

					model.addAttribute("addDetailsKeptAbeyanceCasesObj", keptsobj1);
				}
				// model.addAttribute("employeeList",
//		  employeedetailsrepo.findByCasedetailsId(caseNo);

				// ProsecutionProposalDetails
				// prosecutionproposaldetail=iprosecutionproposaldetailsRepository.findByCasedetailsId(caseid);
				// CourtCaseDetails
				// courtcasedtls=icourtcasedetailsRepository.findByCasedetailsId(caseid);
				/*
				 * List<DetailsKeptAbeyanceCases> detailskeptabeyance =
				 * detailskeptabeyancecasesRepository .findByCasedetailsId(caseid); if
				 * (detailskeptabeyance != null && !detailskeptabeyance.isEmpty()) {
				 * DetailsKeptAbeyanceCases dekept = detailskeptabeyance.get(0);
				 * 
				 * Passing Obj to UI key and value Pair
				 * model.addAttribute("addDetailsKeptAbeyanceCasesObj", dekept); } else {
				 * DetailsKeptAbeyanceCases dekept = new DetailsKeptAbeyanceCases();
				 * dekept.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new
				 * CaseDetails()).getCaseNo());
				 * model.addAttribute("addDetailsKeptAbeyanceCasesObj", dekept); }
				 */

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = detailskeptabeyancecasesRepository.findByCasedetailsId(caseid, pageable);
				} else {
					noticePages = detailskeptabeyancecasesRepository.findByCasedetailsIdAndCurrentUser(caseid,
							currentUser, pageable);
				}

				model.addAttribute("DetailsKeptAbeyanceCasesGridList", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}
				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("DetailsKeptAbeyanceCasesGridList", noticePages.getContent());

			} else {
//				model.addAttribute("DetailsKeptAbeyanceCasesGridList", detailskeptabeyancecasesRepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("DetailsKeptAbeyanceCasesGridList", new ArrayList<>());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/DetailsKeptAbeyanceCases";
	}

	/*
	 * Method : addDetailsKeptAbeyanceCases saving method.. Description : written
	 * saving form logic code
	 */

	@PostMapping("/addDetailsKeptAbeyanceCases")
	public String addDetailsKeptAbeyanceCases(
			@ModelAttribute("addDetailsKeptAbeyanceCasesObj") DetailsKeptAbeyanceCasesDto detailskeptabeyancecasesdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "fileName", required = false) MultipartFile file) {

		// creating new object of DetailsKeptAbeyanceCases model
		DetailsKeptAbeyanceCases detailskeptabeyancecases = new DetailsKeptAbeyanceCases();
		User currentUser = userService.getCurrentUserPojo();

		try {
			if (detailskeptabeyancecasesdto.getId() != null && detailskeptabeyancecasesdto.getId() > 0) {

				// for update
				detailskeptabeyancecases = detailskeptabeyancecasesRepository
						.findById(detailskeptabeyancecasesdto.getId()).orElse(new DetailsKeptAbeyanceCases());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(detailskeptabeyancecasesdto.getCaseNo(),currentUser)) {
					detailskeptabeyancecases.setCasedetails(caseDetailsRepo
							.findByCaseNoAndCurrentUser(detailskeptabeyancecasesdto.getCaseNo(),currentUser));
				}
				
				if(StringUtils.isNotBlank(detailskeptabeyancecasesdto.getEmployeeDataId())) {
					if(detailskeptabeyancecasesRepository.existsByCasedetailsAndEmpDataId(detailskeptabeyancecases.getCasedetails(), Long.valueOf(detailskeptabeyancecasesdto.getEmployeeDataId()))) {
						detailskeptabeyancecases = detailskeptabeyancecasesRepository.findByCasedetailsAndEmpDataId(detailskeptabeyancecases.getCasedetails(), Long.valueOf(detailskeptabeyancecasesdto.getEmployeeDataId()));
					} else {
						detailskeptabeyancecases.setEmpData(employeedetailsrepo.findById(Long.valueOf(detailskeptabeyancecasesdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

				
				/*
				 * detailskeptabeyancecases.setCasedetails(detailskeptabeyancecasesdto.
				 * getCasedetails());
				 */

				detailskeptabeyancecases.setSevarthId(detailskeptabeyancecasesdto.getSevarthId());
				detailskeptabeyancecases.setEmployeeId(detailskeptabeyancecasesdto.getEmployeeId());
				detailskeptabeyancecases.setFirstName(detailskeptabeyancecasesdto.getFirstName());
				detailskeptabeyancecases.setMiddleName(detailskeptabeyancecasesdto.getMiddleName());
				detailskeptabeyancecases.setLastName(detailskeptabeyancecasesdto.getLastName());
				detailskeptabeyancecases.setCasesKeptinAbeyance(detailskeptabeyancecasesdto.isCasesKeptinAbeyance());

				// detailskeptabeyancecases.setReasonKeepingInAbeyance(detailskeptabeyancecasesdto.getReasonKeepingInAbeyance());
				if (detailskeptabeyancecasesdto.getReasonreasonKeepingInAbeyance() != null) {
					detailskeptabeyancecases.setReasonreasonKeepingInAbeyance(reasonforkeepingcaseabeyanceRepository
							.findById(detailskeptabeyancecasesdto.getReasonreasonKeepingInAbeyance().getId())
							.orElse(new ReasonForKeepingCaseAbeyance()));
				}

				else {
					detailskeptabeyancecases.setReasonreasonKeepingInAbeyance(null);

				}

				// detailskeptabeyancecases.setCourtName(icourtnamerepository.findById(courtcasedetailsdto.getCourtName().getId()).orElse(new
				// CourtName()));
				detailskeptabeyancecasesRepository.save(detailskeptabeyancecases);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));

			}

			else {
				
				CaseDetails cd = caseDetailsRepo
						.findByCaseNo(detailskeptabeyancecasesdto.getCaseNo());

				if (cd != null) {
					detailskeptabeyancecases.setCasedetails(cd);
				} else {
					redirect.addFlashAttribute(departmentconstants.MESSAGE,
							messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
					return "redirect:/departmentalEnquiry/keptInAbeyanceCasesdetails/1";
				}
				if (detailskeptabeyancecasesRepository.existsByCasedetails(detailskeptabeyancecases.getCasedetails())) {
					detailskeptabeyancecases = detailskeptabeyancecasesRepository
							.findByCasedetails(detailskeptabeyancecases.getCasedetails());

				}
				
				if(StringUtils.isNotBlank(detailskeptabeyancecasesdto.getEmployeeDataId())) {
					if(detailskeptabeyancecasesRepository.existsByCasedetailsAndEmpDataId(detailskeptabeyancecases.getCasedetails(), Long.valueOf(detailskeptabeyancecasesdto.getEmployeeDataId()))) {
						detailskeptabeyancecases = detailskeptabeyancecasesRepository.findByCasedetailsAndEmpDataId(detailskeptabeyancecases.getCasedetails(), Long.valueOf(detailskeptabeyancecasesdto.getEmployeeDataId()));
					} else {
						detailskeptabeyancecases.setEmpData(employeedetailsrepo.findById(Long.valueOf(detailskeptabeyancecasesdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

				// added on 29/09/2022 ,
	//			User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					detailskeptabeyancecases.setCurrentUser(currentUser);
					detailskeptabeyancecases.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}

				detailskeptabeyancecases.setSevarthId(detailskeptabeyancecasesdto.getSevarthId());
				detailskeptabeyancecases.setEmployeeId(detailskeptabeyancecasesdto.getEmployeeId());
				detailskeptabeyancecases.setFirstName(detailskeptabeyancecasesdto.getFirstName());
				detailskeptabeyancecases.setMiddleName(detailskeptabeyancecasesdto.getMiddleName());
				detailskeptabeyancecases.setLastName(detailskeptabeyancecasesdto.getLastName());
				detailskeptabeyancecases.setCasesKeptinAbeyance(detailskeptabeyancecasesdto.isCasesKeptinAbeyance());

				// detailskeptabeyancecases.setReasonKeepingInAbeyance(detailskeptabeyancecasesdto.getReasonKeepingInAbeyance());
				if (detailskeptabeyancecasesdto.getReasonreasonKeepingInAbeyance() != null) {
					detailskeptabeyancecases.setReasonreasonKeepingInAbeyance(reasonforkeepingcaseabeyanceRepository
							.findById(detailskeptabeyancecasesdto.getReasonreasonKeepingInAbeyance().getId())
							.orElse(new ReasonForKeepingCaseAbeyance()));
				}

				else {
					detailskeptabeyancecases.setReasonreasonKeepingInAbeyance(null);
				}

				detailskeptabeyancecasesRepository.save(detailskeptabeyancecases);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/keptInAbeyanceCasesdetails/1";

	}

	/* Landing form Final Out Come Method . */

	@GetMapping("/finalOutCometails/{page}")
	public String finalOutCometails(Model model, RedirectAttributes redirect, @PathVariable("page") int page,@RequestParam(name="backto",required = false)String backto) {
		LOG.info("Hi this is the finaLOutCome details  page method.............");

		FinalOutcomeDetails finaloutcomedetails = new FinalOutcomeDetails();

		model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails);
		boolean special = false;

		model.addAttribute("DecisionTakenList", decisiontakenrepository.findByActive(true,Sort.by("order")));
		model.addAttribute("casependingwithlist", casependingwithRepository.findByActive(true));
		model.addAttribute("TypeofPenaltylist", typeofpenaltyrepository.findByActive(true));
		model.addAttribute("reasonkeptList", reasonforkeepingcaseabeyanceRepository.findByActive(true));

		// for grid
//		model.addAttribute("FinalOutComeList", finaloutcomeRepository.findAll());

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<FinalOutcomeDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = finaloutcomeRepository.findAll(pageable);
		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SECRETARYOLD")) {
			
			noticePages = finaloutcomeRepository.findByCurrentUserPimsEmployeeGlobalOrgId(currentUser.getPimsEmployee().getGlobalOrgId(), pageable);
		}
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
			noticePages = finaloutcomeRepository.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = finaloutcomeRepository.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("FinalOutComeList", noticePages);
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

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
			System.out.println(caseid);
			if (caseid != null) {
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseid)).orElse(new CaseDetails());
				model.addAttribute("cases", cd);
				
				
				if(cd != null) {
					if(cd.getIoList().isEmpty()) {
						model.addAttribute("disablepresentingofficer", true);
					} 
					if(cd.getMisConductType() != null && (cd.getMisConductType().getId()==1 || cd.getMisConductType().getId()==1)) {
						model.addAttribute("disableprosecution", true);
					}
//					if(cd.getRuleApplicable() != null) {
//						if(cd.getSubRule() != null && cd.getSubRule().getId()==1) {
//							model.addAttribute("hideinquiryreport", true);
//						}
//						if(cd.getRuleApplicable().getId()==4) {
//							model.addAttribute("hideinquiryreport", true);
//						}
//					}
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
				}
				/*------------------------------------------------------------------------------*/

				List<FinalOutcomeDetails> finaloutcomedetailss = finaloutcomeRepository.findByCasedetailsId(caseid);
				FinalOutcomeDetails finalOutcomeDetailsobj1 = new FinalOutcomeDetails();
				if (finaloutcomedetailss != null && !finaloutcomedetailss.isEmpty()) {
					if(StringUtils.isNotBlank(backto) && backto.equalsIgnoreCase("regular")) {
						for(FinalOutcomeDetails finalout : finaloutcomedetailss) {
							if(finalout.getDecisionTaken() != null && (finalout.getDecisionTaken().getId() == 2 || finalout.getDecisionTaken().getId()==1)) {
								finalOutcomeDetailsobj1 = finalout;
								special = false;
								break;
							}
						}
					} else if(StringUtils.isNotBlank(backto) && backto.equalsIgnoreCase("special")) {
						for(FinalOutcomeDetails finalout : finaloutcomedetailss) {
							if(finalout.getDecisionTaken() != null && (finalout.getDecisionTaken().getId() == 3 || finalout.getDecisionTaken().getId()==4
									|| finalout.getDecisionTaken().getId()==5)) {
								finalOutcomeDetailsobj1 = finalout;
								special = true;
								break;
							}
						}
					} else {
						finalOutcomeDetailsobj1 = finaloutcomeRepository.findById(finaloutcomedetailss.get(0).getId()).orElse(new FinalOutcomeDetails());
					}
					CaseDetails caseDetails = cd;
					if(finalOutcomeDetailsobj1 == null) {
						finalOutcomeDetailsobj1 = new FinalOutcomeDetails();
					}
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						FinalOutcomeDetailsobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						FinalOutcomeDetailsobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						FinalOutcomeDetailsobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						FinalOutcomeDetailsobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						FinalOutcomeDetailsobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					finalOutcomeDetailsobj1.setCaseNo(finaloutcomedetailss.get(0).getCasedetails().getCaseNo());
					if(!caseDetails.getEmployeeList().isEmpty()){
					//	EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(finalOutcomeDetailsobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(finalOutcomeDetailsobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(finalOutcomeDetailsobj1.getLastName())) {
								finalOutcomeDetailsobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}
					
					if(caseDetails.getSubRule() != null && caseDetails.getSubRule().getId()==1) {
						model.addAttribute("subRule10", true);
					}
					
//					finalOutcomeDetailsobj1.setCasePendingWith(casependingwithRepository.findById(finalOutcomeDetailsobj1.getCasePendingWith().getId()).orElse(new CasePendingWithPojo()));
					model.addAttribute("finaloutcomedetailsObj", finalOutcomeDetailsobj1);
					if(finalOutcomeDetailsobj1.getDecisionTaken() != null 
							&& (finalOutcomeDetailsobj1.getDecisionTaken().getId() == 3 || finalOutcomeDetailsobj1.getDecisionTaken().getId()==4
							|| finalOutcomeDetailsobj1.getDecisionTaken().getId()==5)) {
						special = true;
					} else {
						special = false;
					}
 				}

				else {
					FinalOutcomeDetails finaloutcomedetails1 = new FinalOutcomeDetails();
					CaseDetails casedtls = new CaseDetails();
					CaseDetails caseDetails = caseDetailsRepo.findById(caseid).orElse(casedtls);
					if(caseDetails.getSubRule() != null && caseDetails.getSubRule().getId()==1) {
						model.addAttribute("subRule10", true);
					} 
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						finaloutcomedetails1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						finaloutcomedetails1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						finaloutcomedetails1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						finaloutcomedetails1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						finaloutcomedetails1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(finaloutcomedetails1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(finaloutcomedetails1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(finaloutcomedetails1.getLastName())) {
								finaloutcomedetails1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}	
					
					finaloutcomedetails1.setCaseNo(caseDetails.getCaseNo());

					model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails1);
				}

				/*
				 * List<FinalOutcomeDetails> finaloutcomedtls =
				 * finaloutcomeRepository.findByCasedetailsId(caseid); if (finaloutcomedtls !=
				 * null && !finaloutcomedtls.isEmpty()) { FinalOutcomeDetails finalout =
				 * finaloutcomedtls.get(0);
				 * finalout.setCaseNo(finalout.getCasedetails().getCaseNo());
				 * 
				 * Passing Obj to UI key and value Pair
				 * model.addAttribute("finaloutcomedetailsObj", finalout); } else {
				 * FinalOutcomeDetails finalout = new FinalOutcomeDetails();
				 * finalout.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new
				 * CaseDetails()).getCaseNo()); model.addAttribute("finaloutcomedetailsObj",
				 * finalout); }
				 */

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = finaloutcomeRepository.findByCasedetailsId(caseid, pageable);
				} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SECRETARYOLD")) {
					
					noticePages = finaloutcomeRepository.findByCurrentUserPimsEmployeeGlobalOrgIdAndCasedetailsId(currentUser.getPimsEmployee().getGlobalOrgId(),caseid, pageable);
				}
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
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
					noticePages = finaloutcomeRepository.findByCasedetailsIdAndCurrentUserIn(caseid, userList,
							pageable);
				}
				else {
					noticePages = finaloutcomeRepository.findByCasedetailsIdAndCurrentUser(caseid, currentUser,
							pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}

				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("FinalOutComeList", noticePages.getContent());

			} else {
//				model.addAttribute("FinalOutComeList", finaloutcomeRepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("FinalOutComeList", new ArrayList<>());
		} // Ending try block of DE Cases backlog records
		if(special) {
			return "DeptInquiryViews/finaloutcomespecial";
		} else {
			return "DeptInquiryViews/FinalOutcomeDetails";
		}
		
	}
	
	
	@GetMapping("/finalOutCometailsSpecial/{page}")
	public String finalOutCometailsSpecial(Model model, RedirectAttributes redirect, @PathVariable("page") int page,@RequestParam(name="backto",required = false)String backto) {
		LOG.info("Hi this is the finaLOutCome details  page method.............");
boolean special = false;
		FinalOutcomeDetails finaloutcomedetails = new FinalOutcomeDetails();

		model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails);

		model.addAttribute("DecisionTakenList", decisiontakenrepository.findByActive(true,Sort.by("order")));
		model.addAttribute("casependingwithlist", casependingwithRepository.findByActive(true));
		model.addAttribute("TypeofPenaltylist", typeofpenaltyrepository.findByActive(true));
		model.addAttribute("reasonkeptList", reasonforkeepingcaseabeyanceRepository.findByActive(true));

		// for grid
//		model.addAttribute("FinalOutComeList", finaloutcomeRepository.findAll());

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<FinalOutcomeDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = finaloutcomeRepository.findAll(pageable);
		}
		else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
			List<User> userList = userRepo.findBySubDepartmentIn(
					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
			userList.add(currentUser);
			noticePages = finaloutcomeRepository.findByCurrentUserIn(userList, pageable);
		}
		else {
			noticePages = finaloutcomeRepository.findByCurrentUser(currentUser, pageable);
		}

		model.addAttribute("FinalOutComeList", noticePages);
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

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
			System.out.println(caseid);
			if (caseid != null) {
				CaseDetails cd = caseDetailsRepo.findById(Long.valueOf(caseid)).orElse(new CaseDetails());
				model.addAttribute("cases", cd);
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
					}
					if(Integer.valueOf(cd.getTotalNoOfEmployee())!=cd.getEmployeeList().size()) {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					} else {
						model.addAttribute("totalemployee", cd.getTotalNoOfEmployee());
						model.addAttribute("entered",  employeedetailsrepo.findByCasedetailsId(cd.getId()).size());
					}
				}
				/*------------------------------------------------------------------------------*/

				List<FinalOutcomeDetails> finaloutcomedetailss = finaloutcomeRepository.findByCasedetailsId(caseid);
				FinalOutcomeDetails finalOutcomeDetailsobj1 = null;
				if (finaloutcomedetailss != null && !finaloutcomedetailss.isEmpty()) {
					
					if(StringUtils.isNotBlank(backto) && backto.equalsIgnoreCase("regular")) {
						for(FinalOutcomeDetails finalout : finaloutcomedetailss) {
							if(finalout.getDecisionTaken() != null && (finalout.getDecisionTaken().getId() == 2 || finalout.getDecisionTaken().getId()==1)) {
								finalOutcomeDetailsobj1 = finalout;
								special = false;
								break;
							}
						}
					} else if(StringUtils.isNotBlank(backto) && backto.equalsIgnoreCase("special")) {
						for(FinalOutcomeDetails finalout : finaloutcomedetailss) {
							if(finalout.getDecisionTaken() != null && (finalout.getDecisionTaken().getId() == 3 || finalout.getDecisionTaken().getId()==4
									|| finalout.getDecisionTaken().getId()==5)) {
								finalOutcomeDetailsobj1 = finalout;
								special = true;
								break;
							}
						}
					} else {
						finalOutcomeDetailsobj1 = finaloutcomeRepository.findById(finaloutcomedetailss.get(0).getId()).orElse(new FinalOutcomeDetails());
					}
					
					
				//	 finaloutcomeRepository.findById(finaloutcomedetailss.get(0).getId()).orElse(new FinalOutcomeDetails());
					CaseDetails caseDetails = cd;
					if(finalOutcomeDetailsobj1 == null) {
						special = true;
						finalOutcomeDetailsobj1 = new FinalOutcomeDetails();
					}

					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						FinalOutcomeDetailsobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						FinalOutcomeDetailsobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						FinalOutcomeDetailsobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						FinalOutcomeDetailsobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						FinalOutcomeDetailsobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					finalOutcomeDetailsobj1.setCaseNo(cd.getCaseNo());
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(finalOutcomeDetailsobj1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(finalOutcomeDetailsobj1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(finalOutcomeDetailsobj1.getLastName())) {
								finalOutcomeDetailsobj1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}
					
					if(caseDetails.getSubRule() != null && caseDetails.getSubRule().getId()==1) {
						model.addAttribute("subRule10", true);
					}
					
//					finalOutcomeDetailsobj1.setCasePendingWith(casependingwithRepository.findById(finalOutcomeDetailsobj1.getCasePendingWith().getId()).orElse(new CasePendingWithPojo()));
					model.addAttribute("finaloutcomedetailsObj", finalOutcomeDetailsobj1);
				}

				else {
					FinalOutcomeDetails finaloutcomedetails1 = new FinalOutcomeDetails();
					CaseDetails casedtls = new CaseDetails();
					CaseDetails caseDetails = caseDetailsRepo.findById(caseid).orElse(casedtls);
					if(caseDetails.getSubRule() != null && caseDetails.getSubRule().getId()==1) {
						model.addAttribute("subRule10", true);
					} 
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
//						finaloutcomedetails1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
//						finaloutcomedetails1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
//						finaloutcomedetails1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
//						finaloutcomedetails1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
//						finaloutcomedetails1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					if(!caseDetails.getEmployeeList().isEmpty()){
						for(EmployeeDetails emp : employeedetailsrepo.findByCasedetailsId(caseDetails.getId())) {
							if(emp.getFirstName().equalsIgnoreCase(finaloutcomedetails1.getFirstName()) 
									&& emp.getMiddleName().equalsIgnoreCase(finaloutcomedetails1.getMiddleName())
									&& emp.getLastName().equalsIgnoreCase(finaloutcomedetails1.getLastName())) {
								finaloutcomedetails1.setEmployeeDataId(emp.getId().toString());
							}
						}
						model.addAttribute("employeeList", caseDetails.getEmployeeList());
					}	
					
					finaloutcomedetails1.setCaseNo(caseDetails.getCaseNo());

					model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails1);
				}

	

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = finaloutcomeRepository.findByCasedetailsId(caseid, pageable);
				} 
				else if(currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_DEPARTMENTADMIN")) {
					List<User> userList = userRepo.findBySubDepartmentIn(
							subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
					userList.add(currentUser);
					noticePages = finaloutcomeRepository.findByCasedetailsIdAndCurrentUserIn(caseid, userList,
							pageable);
				}
				else {
					noticePages = finaloutcomeRepository.findByCasedetailsIdAndCurrentUser(caseid, currentUser,
							pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}

				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("FinalOutComeList", noticePages.getContent());

			} else {
//				model.addAttribute("FinalOutComeList", finaloutcomeRepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("FinalOutComeList", new ArrayList<>());
		} // Ending try block of DE Cases backlog records

//		return "DeptInquiryViews/finaloutcomespecial";
		if(special) {
			return "DeptInquiryViews/finaloutcomespecial";
		} else {
			return "DeptInquiryViews/FinalOutcomeDetails";
		}
	}

	/*
	 * Method : add Final Outcome Details saving method.. Description : written
	 * saving form logic code
	 */

	@PostMapping("/addFinalOutcomeDetails")
	public String addFinalOutcomeDetails(
			@ModelAttribute("finaloutcomedetailsObj") FinalOutcomeDetailsDTO finaloutcomedetailsdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "fileName", required = false) MultipartFile file) {

		// creating new object of Final Outcome DetailsDTO model
		FinalOutcomeDetails finaloutcomedetails = new FinalOutcomeDetails();
		User currenUser = userService.getCurrentUserPojo();
		
		try {
			if (finaloutcomedetailsdto.getId() != null && finaloutcomedetailsdto.getId() > 0) {

				// for update
				finaloutcomedetails = finaloutcomeRepository.findById(finaloutcomedetailsdto.getId())
						.orElse(new FinalOutcomeDetails());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(finaloutcomedetailsdto.getCaseNo(),currenUser)) {
					finaloutcomedetails.setCasedetails(caseDetailsRepo
							.findByCaseNoAndCurrentUser(finaloutcomedetailsdto.getCaseNo(),currenUser));
					if (finaloutcomedetailsdto.getCasePendingWith() != null
							&& finaloutcomedetailsdto.getCasePendingWith().getId() != null) {
						finaloutcomedetails.getCasedetails()
								.setPendingWith(casependingwithRepository
										.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
										.orElse(new CasePendingWithPojo()).getCasePendingNameEn());
					}
					if (finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance() != null
							&& finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance().getId() != null) {
						finaloutcomedetails.getCasedetails()
								.setPendingWith("Abeyance reason : " + reasonforkeepingcaseabeyanceRepository
										.findById(finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance().getId())
										.orElse(new ReasonForKeepingCaseAbeyance())
										.getReasonKeepingCaseAbeyanceCodeName());
					}
//					if(finaloutcomedetailsdto.isWhetherCaseFinallyDecided()) {
//						finaloutcomedetails.getCasedetails()
//						.setPendingWith("Case Decided");
//					}
					List<CaseDetails> finalOut = caseDetailsRepo.findAll();		
					Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
							&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
							&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
					if(caseIdx.contains(finaloutcomedetails.getCasedetails().getId())) {
						if(finaloutcomedetailsdto.getCasePendingWith() != null
							&& finaloutcomedetailsdto.getCasePendingWith().getId() != null
							&& (finaloutcomedetailsdto.getCasePendingWith().getId() == 3 
								||finaloutcomedetailsdto.getCasePendingWith().getId() ==4 
								|| finaloutcomedetailsdto.getCasePendingWith().getId()==5)) {
							finaloutcomedetails.getCasedetails()
							.setPendingWith(casependingwithRepository
									.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
									.orElse(new CasePendingWithPojo()).getCasePendingNameEn());
						} else {
							finaloutcomedetails.getCasedetails()
							.setPendingWith("Case Decided");
						}
					}
					if(finaloutcomedetails.getCasedetails().getEmployeeList().size()==1 && finaloutcomedetailsdto.isWhetherCaseFinallyDecided()) {
						if(finaloutcomedetailsdto.getCasePendingWith() != null
								&& finaloutcomedetailsdto.getCasePendingWith().getId() != null
								&& (finaloutcomedetailsdto.getCasePendingWith().getId() == 3 
									||finaloutcomedetailsdto.getCasePendingWith().getId() ==4 
									|| finaloutcomedetailsdto.getCasePendingWith().getId()==5)) {
								finaloutcomedetails.getCasedetails()
								.setPendingWith(casependingwithRepository
										.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
										.orElse(new CasePendingWithPojo()).getCasePendingNameEn());
							} else {
								finaloutcomedetails.getCasedetails()
								.setPendingWith("Case Decided");
							}
					}
				}
				
				if(StringUtils.isNotBlank(finaloutcomedetailsdto.getEmployeeDataId())) {
					if(finaloutcomeRepository.existsByCasedetailsAndEmpDataId(finaloutcomedetails.getCasedetails(), Long.valueOf(finaloutcomedetailsdto.getEmployeeDataId()))) {
						finaloutcomedetails = finaloutcomeRepository.findByCasedetailsAndEmpDataId(finaloutcomedetails.getCasedetails(), Long.valueOf(finaloutcomedetailsdto.getEmployeeDataId()));
					} else {
						finaloutcomedetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(finaloutcomedetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

				/*
				 * finaloutcomedetails.setCasedetails(finaloutcomedetailsdto.getCasedetails());
				 */

				finaloutcomedetails.setInquiryReportReceived(finaloutcomedetailsdto.isInquiryReportReceived());
				finaloutcomedetails.setDateofReceipt(finaloutcomedetailsdto.getDateofReceipt());
//				finaloutcomedetails.setDateofOrderUpload(finaloutcomedetailsdto.getDateofOrderUpload());
//				finaloutcomedetails.setCasePendingWith(finaloutcomedetailsdto.getCasePendingWith());
				finaloutcomedetails.setWhetherCaseFinallyDecided(finaloutcomedetailsdto.isWhetherCaseFinallyDecided());
//				finaloutcomedetails.setDecisionTaken(finaloutcomedetailsdto.getDecisionTaken());
//				finaloutcomedetails.setContentType(finaloutcomedetailsdto.getTypeOfPenaltyInflicted());

				finaloutcomedetails.setSevarthId(finaloutcomedetailsdto.getSevarthId());
				finaloutcomedetails.setEmployeeId(finaloutcomedetailsdto.getEmployeeId());
				finaloutcomedetails.setFirstName(finaloutcomedetailsdto.getFirstName());
				finaloutcomedetails.setMiddleName(finaloutcomedetailsdto.getMiddleName());
				finaloutcomedetails.setLastName(finaloutcomedetailsdto.getLastName());
				finaloutcomedetails.setDateOfOrder(finaloutcomedetailsdto.getDateOfOrder());
				
				if (finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance() != null && finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance().getId() != null) {
					finaloutcomedetails.setReasonreasonKeepingInAbeyance(reasonforkeepingcaseabeyanceRepository
							.findById(finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance().getId())
							.orElse(new ReasonForKeepingCaseAbeyance()));
				}
				finaloutcomedetails.setWhethercasekeptinabeyance(finaloutcomedetailsdto.isWhethercasekeptinabeyance());

				if (finaloutcomedetailsdto.getCasePendingWith() != null && finaloutcomedetailsdto.getCasePendingWith().getId() != null) {
					finaloutcomedetails.setCasePendingWith(
							casependingwithRepository.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
									.orElse(new CasePendingWithPojo()));
				}

				else {
					finaloutcomedetails.setCasePendingWith(null);

				}
				if (finaloutcomedetailsdto.getDecisionTaken() != null && finaloutcomedetailsdto.getDecisionTaken().getId() != null) {
					finaloutcomedetails.setDecisionTaken(
							decisiontakenrepository.findById(finaloutcomedetailsdto.getDecisionTaken().getId())
									.orElse(new DecisionTakenPojo()));
					if(finaloutcomedetails.getDecisionTaken().getId()==3 ||
							finaloutcomedetails.getDecisionTaken().getId()==4 ||
							finaloutcomedetails.getDecisionTaken().getId()==5) {
						finaloutcomedetails.setWhetherCaseFinallyDecided(true);
					}
				}

				else {
					finaloutcomedetails.setDecisionTaken(null);

				}
				if (finaloutcomedetailsdto.getTypeOfPenaltyInflicted() != null && finaloutcomedetailsdto.getTypeOfPenaltyInflicted().getId() != null) {
					finaloutcomedetails.setTypeOfPenaltyInflicted(
							typeofpenaltyrepository.findById(finaloutcomedetailsdto.getTypeOfPenaltyInflicted().getId())
									.orElse(new TypeOfPenaltyInflictedPojo()));
				}

				else {
					finaloutcomedetails.setTypeOfPenaltyInflicted(null);

				}

				finaloutcomedetails.setFileNameEn(finaloutcomedetailsdto.getFileName());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ finaloutcomedetails.getCasedetails().getId() + "/" + "finaloutcomeDetails/"
							+ finaloutcomedetails.getId() + "/";
					finaloutcomedetails.setFileName(file.getOriginalFilename());
					finaloutcomedetails.setContentType(file.getContentType());
					finaloutcomedetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, finaloutcomedetails.getFileName());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				finaloutcomedetails = finaloutcomeRepository.save(finaloutcomedetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editfinalOutcomeDtlsRecords/"+finaloutcomedetails.getId()+"/1";

			}

			else {

//				finaloutcomedetails.setCasedetails(caseDetailsRepo.findById(finaloutcomedetailsdto.getCasedetails().getId()).orElse(new CaseDetails()));
//
//				// for save
//				/* this code is checking on Case Details screen at case id is exist or not */
//				if (finaloutcomeRepository.existsByCasedetails(finaloutcomedetails.getCasedetails())) {
//
//					finaloutcomedetails = finaloutcomeRepository.findByCasedetails(finaloutcomedetails.getCasedetails());
//				}

				// save
				finaloutcomedetails = new FinalOutcomeDetails();
				if (StringUtils.isBlank(finaloutcomedetailsdto.getFileNo())) {
					CaseDetails cd = caseDetailsRepo.findByCaseNo(finaloutcomedetailsdto.getCaseNo())
							;
	
					if (cd != null) {
						finaloutcomedetails.setCasedetails(cd);
						if (finaloutcomedetailsdto.getCasePendingWith() != null
								&& finaloutcomedetailsdto.getCasePendingWith().getId() != null) {
							finaloutcomedetails.getCasedetails()
									.setPendingWith(casependingwithRepository
											.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
											.orElse(new CasePendingWithPojo()).getCasePendingNameEn());
						}
						if (finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance() != null
								&& finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance().getId() != null) {
							finaloutcomedetails.getCasedetails()
									.setPendingWith("Case in abeyance : " + reasonforkeepingcaseabeyanceRepository
											.findById(finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance().getId())
											.orElse(new ReasonForKeepingCaseAbeyance())
											.getReasonKeepingCaseAbeyanceCodeName());
						}
//						if(finaloutcomedetailsdto.isWhetherCaseFinallyDecided()) {
//							finaloutcomedetails.getCasedetails()
//							.setPendingWith("Case Decided");
//						}
						List<CaseDetails> finalOut = caseDetailsRepo.findAll();		
						Set<Long> caseIdx = finalOut.stream().filter(ss->!ss.getFinaloutList().isEmpty() 
								&& ss.getFinaloutList().stream().allMatch(sss->sss.isWhetherCaseFinallyDecided())
								&& ss.getFinaloutList().size()==ss.getEmployeeList().size()).map(ss->ss.getId()).collect(Collectors.toSet());
						if(caseIdx.contains(finaloutcomedetails.getCasedetails().getId())) {
							if(finaloutcomedetailsdto.getCasePendingWith() != null
									&& finaloutcomedetailsdto.getCasePendingWith().getId() != null
									&& (finaloutcomedetailsdto.getCasePendingWith().getId() == 3 
										||finaloutcomedetailsdto.getCasePendingWith().getId() ==4 
										|| finaloutcomedetailsdto.getCasePendingWith().getId()==5)) {
									finaloutcomedetails.getCasedetails()
									.setPendingWith(casependingwithRepository
											.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
											.orElse(new CasePendingWithPojo()).getCasePendingNameEn());
								} else {
									finaloutcomedetails.getCasedetails()
									.setPendingWith("Case Decided");
								}
						}
						if(cd.getEmployeeList().size()==1 && finaloutcomedetailsdto.isWhetherCaseFinallyDecided()) {
							if(finaloutcomedetailsdto.getCasePendingWith() != null
									&& finaloutcomedetailsdto.getCasePendingWith().getId() != null
									&& (finaloutcomedetailsdto.getCasePendingWith().getId() == 3 
										||finaloutcomedetailsdto.getCasePendingWith().getId() ==4 
										|| finaloutcomedetailsdto.getCasePendingWith().getId()==5)) {
									finaloutcomedetails.getCasedetails()
									.setPendingWith(casependingwithRepository
											.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
											.orElse(new CasePendingWithPojo()).getCasePendingNameEn());
								} else {
									finaloutcomedetails.getCasedetails()
									.setPendingWith("Case Decided");
								}
						}
					} else {
						redirect.addFlashAttribute(departmentconstants.MESSAGE,
								messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
						return "redirect:/departmentalEnquiry/finalOutCometails/1";
					}
				
					if (StringUtils.isNotBlank(finaloutcomedetailsdto.getSevarthId())
							&& finaloutcomeRepository.existsByCasedetailsAndSevarthId(cd, finaloutcomedetailsdto.getSevarthId())) {
						finaloutcomedetails = finaloutcomeRepository.findByCasedetailsAndSevarthId(cd,
								finaloutcomedetailsdto.getSevarthId());
					}
				} else {
					finaloutcomedetails.setFileNo(finaloutcomedetailsdto.getFileNo());
				}
				if(StringUtils.isNotBlank(finaloutcomedetailsdto.getEmployeeDataId())) {
					if(finaloutcomeRepository.existsByCasedetailsAndEmpDataId(finaloutcomedetails.getCasedetails(), Long.valueOf(finaloutcomedetailsdto.getEmployeeDataId()))) {
						finaloutcomedetails = finaloutcomeRepository.findByCasedetailsAndEmpDataId(finaloutcomedetails.getCasedetails(), Long.valueOf(finaloutcomedetailsdto.getEmployeeDataId()));
					} else {
						finaloutcomedetails.setEmpData(employeedetailsrepo.findById(Long.valueOf(finaloutcomedetailsdto.getEmployeeDataId())).orElse(new EmployeeDetails()));
					}
				}

				// added on 29/09/2022 ,
				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					currentUser = userRepo.findById(currentUser.getId()).orElse(new User());
					finaloutcomedetails.setCurrentUser(currentUser);
					finaloutcomedetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				
				if (finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance() != null && finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance().getId() != null) {
					finaloutcomedetails.setReasonreasonKeepingInAbeyance(reasonforkeepingcaseabeyanceRepository
							.findById(finaloutcomedetailsdto.getReasonreasonKeepingInAbeyance().getId())
							.orElse(new ReasonForKeepingCaseAbeyance()));
				}
				finaloutcomedetails.setWhethercasekeptinabeyance(finaloutcomedetailsdto.isWhethercasekeptinabeyance());

				finaloutcomedetails.setInquiryReportReceived(finaloutcomedetailsdto.isInquiryReportReceived());
//				finaloutcomedetails.setDateofOrderUpload(finaloutcomedetailsdto.getDateofOrderUpload());
				finaloutcomedetails.setDateofReceipt(finaloutcomedetailsdto.getDateofReceipt());
				finaloutcomedetails.setSevarthId(finaloutcomedetailsdto.getSevarthId());
				finaloutcomedetails.setEmployeeId(finaloutcomedetailsdto.getEmployeeId());
				finaloutcomedetails.setFirstName(finaloutcomedetailsdto.getFirstName());
				finaloutcomedetails.setMiddleName(finaloutcomedetailsdto.getMiddleName());
				finaloutcomedetails.setLastName(finaloutcomedetailsdto.getLastName());
				finaloutcomedetails.setDateOfOrder(finaloutcomedetailsdto.getDateOfOrder());

				
				finaloutcomedetails.setWhetherCaseFinallyDecided(finaloutcomedetailsdto.isWhetherCaseFinallyDecided());
//				finaloutcomedetails.setCasePendingWith(finaloutcomedetailsdto.getCasePendingWith());
				// finaloutcomedetails.setDecisionTaken(finaloutcomedetailsdto.getDecisionTaken());
				// finaloutcomedetails.setContentType(finaloutcomedetailsdto.getTypeOfPenaltyInflicted());
//				employeedetails.setGender(genderRepo.findById(employeedetailsdto.getGender().getId()).orElse(new Gender()));
//				courtcasedetails.setCourtName(icourtnamerepository.findById(courtcasedetailsdto.getCourtName().getId()).orElse(new CourtName()));
				if (finaloutcomedetailsdto.getCasePendingWith() != null && finaloutcomedetailsdto.getCasePendingWith().getId() != null) {
					finaloutcomedetails.setCasePendingWith(
							casependingwithRepository.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
									.orElse(new CasePendingWithPojo()));
				}

				else {
					finaloutcomedetails.setCasePendingWith(null);

				}
				if (finaloutcomedetailsdto.getDecisionTaken() != null && finaloutcomedetailsdto.getDecisionTaken().getId() != null) {
					finaloutcomedetails.setDecisionTaken(
							decisiontakenrepository.findById(finaloutcomedetailsdto.getDecisionTaken().getId())
									.orElse(new DecisionTakenPojo()));
					if(finaloutcomedetails.getDecisionTaken().getId()==3 ||
							finaloutcomedetails.getDecisionTaken().getId()==4 ||
							finaloutcomedetails.getDecisionTaken().getId()==5) {
						finaloutcomedetails.setWhetherCaseFinallyDecided(true);
					}
				}

				else {
					finaloutcomedetails.setDecisionTaken(null);

				}
				if (finaloutcomedetailsdto.getTypeOfPenaltyInflicted() != null && finaloutcomedetailsdto.getTypeOfPenaltyInflicted().getId() != null) {
					finaloutcomedetails.setTypeOfPenaltyInflicted(
							typeofpenaltyrepository.findById(finaloutcomedetailsdto.getTypeOfPenaltyInflicted().getId())
									.orElse(new TypeOfPenaltyInflictedPojo()));
				}

				else {
					finaloutcomedetails.setTypeOfPenaltyInflicted(null);

				}

				finaloutcomedetails.setFileNameEn(finaloutcomedetailsdto.getFileName());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/"
							+ finaloutcomedetails.getCasedetails().getId() + "/" + "finaloutcomeDetails/"
							+ finaloutcomedetails.getId() + "/";
					finaloutcomedetails.setFileName(file.getOriginalFilename());
					finaloutcomedetails.setContentType(file.getContentType());
					finaloutcomedetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, finaloutcomedetails.getFileName());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				finaloutcomedetails = finaloutcomeRepository.save(finaloutcomedetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editfinalOutcomeDtlsRecords/"+finaloutcomedetails.getId()+"/1";

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/finalOutCometails/1";

	}

	/*
	 * this Method is Fetching the Records Depends on Sevarth id on suspension
	 * details
	 */

	@GetMapping("/getEmpDetail")
	@ResponseBody
	public ObjectNode getSusEmpData(@RequestParam(name = "sevarthid", required = false) String sevarthid) {
		ObjectNode obb = objectMapper.createObjectNode();
		if (sevarthid != null) {
			List<EmployeeDetails> empDetail = employeedetailsrepo.findBySevarthId(sevarthid);
			obb.put("sevarthId", empDetail.get(0).getSevarthId());
			obb.put("firstName", empDetail.get(0).getFirstName());
			obb.put("middleName", empDetail.get(0).getMiddleName());
			obb.put("lastName", empDetail.get(0).getLastName());
			obb.put("employeeId", empDetail.get(0).getEmployeeId());
		}
		return obb;
	}

	@GetMapping(value = "getEmployeeList")
	public ResponseEntity<?> getEmployeeList(@RequestParam("q") final String input) {
		List<EmployeeDetails> empDetail = employeedetailsrepo.findBySevarthIdContainingIgnoreCase(input);
		List<String> idList = new ArrayList<>();
		empDetail.stream().forEach(s -> {
			idList.add(s.getSevarthId());
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}

	@GetMapping(value = "getCaseList")
	public ResponseEntity<?> getCaseDetailId(@RequestParam("q") final String input) {
		List<CaseDetails> empDetail = caseDetailsRepo.findByCaseNoContainingIgnoreCase(input);
		List<String> idList = new ArrayList<>();
		empDetail.stream().forEach(s -> {
			idList.add(s.getCaseNo());
		});
		return new ResponseEntity<>(idList, HttpStatus.OK);
	}

	// Method for suspension details fetching file1 details records

	@GetMapping("/DocUploadOrderSusNamefile1/{id}")
	public ResponseEntity<ByteArrayResource> getFileforSusdtlsfile1(@PathVariable String id) {
		LOG.info("Method for  suspension details fetching file1  details records");

//		ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		SuspensionDetails susdtls = suspensionDetailsRepo.findById(Long.valueOf(id)).orElse(new SuspensionDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + susdtls.getUploadOrderActualSuspensionName() + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(fileService
				.getFile(susdtls.getFilepathActualSuspension(), susdtls.getUploadOrderActualSuspensionName()));
	}

	// Method for Suspension details fetching file2 details records...

	@GetMapping("/DocUploadOrderSusNamefile2/{id}")
	public ResponseEntity<ByteArrayResource> getFileforSusdtlsfile2(@PathVariable String id) {
		LOG.info("Method for  suspension details fetching file2  details records");
//		ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		SuspensionDetails susdtls = suspensionDetailsRepo.findById(Long.valueOf(id)).orElse(new SuspensionDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + susdtls.getUploadOrderDeemedSuspension() + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(fileService
				.getFile(susdtls.getFilePathUploadOrderDeemedSuspension(), susdtls.getUploadOrderDeemedSuspension()));
	}

	// Method for Suspension details fetching file3 details records......
	@GetMapping("/DocUploadOrderSusNamefile3/{id}")
	public ResponseEntity<ByteArrayResource> getFileforSusdtlsfile3(@PathVariable String id) {
		LOG.info("Method for  suspension details fetching file3  details records");
//			ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		SuspensionDetails susdtls = suspensionDetailsRepo.findById(Long.valueOf(id)).orElse(new SuspensionDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + susdtls.getExtensionUploadOrder() + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(
				fileService.getFile(susdtls.getFilePathExtensionUploadOrder(), susdtls.getExtensionUploadOrder()));
	}

	// Date:28-09-2022
	// Method for Reinstated details fetching file1 details records......
	@GetMapping("/DocUploadOrderReinstatedNamefile1/{id}")
	public ResponseEntity<ByteArrayResource> getFileforReinstateddtlsfile1(@PathVariable String id) {
		// LOG.info("Method for suspension details fetching file3 details records");
//					ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		ReInstatedDetails susdtls = reinstatedRepository.findById(Long.valueOf(id)).orElse(new ReInstatedDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME  + "file.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(susdtls.getFilepath(), susdtls.getUploadOrderReInstatment()));
	}

	@GetMapping("/DocuploadOrderofAppointment/{id}")
	public ResponseEntity<ByteArrayResource> getFileforInqOfffile1(@PathVariable String id) {
		// LOG.info("Method for suspension details fetching file3 details records");
//					ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		InquiryOfficerDetails susdtls = inquiryofficerRepository.findById(Long.valueOf(id))
				.orElse(new InquiryOfficerDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME  + "file.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(susdtls.getFilepath(), susdtls.getUploadOrderofAppointment()));
	}
	
	@GetMapping("/DocuploadOrderofInquiryReport/{id}")
	public ResponseEntity<ByteArrayResource> getFileforInqOfffileInqReport(@PathVariable String id) {
		// LOG.info("Method for suspension details fetching file3 details records");
//					ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		InquiryOfficerDetails susdtls = inquiryofficerRepository.findById(Long.valueOf(id))
				.orElse(new InquiryOfficerDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + "file.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(susdtls.getInquiryReportfileUploadFilePath(), susdtls.getInquiryReportfileUpload()));
	}

	@GetMapping("/DocuploadOrderofProsecution/{id}")
	public ResponseEntity<ByteArrayResource> getFileforProsecution(@PathVariable String id) {
		// LOG.info("Method for suspension details fetching file3 details records");
//					ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		ProsecutionProposalDetails susdtls = iprosecutionproposaldetailsRepository.findById(Long.valueOf(id))
				.orElse(new ProsecutionProposalDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + susdtls.getFileNameUploadCopy() + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(susdtls.getFilepath(), susdtls.getFileNameUploadCopy()));
	}
	
	@GetMapping("/DocuploadOrderofProsecutionfinalcourtordercopy/{id}")
	public ResponseEntity<ByteArrayResource> getFileforProsecutionfinalcourtordercopy(@PathVariable String id) {
		// LOG.info("Method for suspension details fetching file3 details records");
//					ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		ProsecutionProposalDetails susdtls = iprosecutionproposaldetailsRepository.findById(Long.valueOf(id))
				.orElse(new ProsecutionProposalDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME  + "file.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(susdtls.getFilepath(), susdtls.getFinalcourtordercopy()));
	}

	@GetMapping("/DocuploadOrderofCourtCase/{id}")
	public ResponseEntity<ByteArrayResource> getFileforCourtCase(@PathVariable String id) {
		// LOG.info("Method for suspension details fetching file3 details records");
//					ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		CourtCaseDetails susdtls = icourtcasedetailsRepository.findById(Long.valueOf(id))
				.orElse(new CourtCaseDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + susdtls.getUploadOder() + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(susdtls.getFilepath(), susdtls.getUploadOder()));
	}

	@GetMapping("/DocuploadOrderofFinalOutcome/{id}")
	public ResponseEntity<ByteArrayResource> getFileforFinaloutcome(@PathVariable String id) {
		// LOG.info("Method for suspension details fetching file3 details records");
//					ChargesheetDetails charge = chargesheetRepo.findById(Long.valueOf(id)).orElse(new ChargesheetDetails());
		FinalOutcomeDetails susdtls = finaloutcomeRepository.findById(Long.valueOf(id))
				.orElse(new FinalOutcomeDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(departmentconstants.CONTENTDEPOSITION,
				departmentconstants.INLINEFILENAME + susdtls.getFileName() + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(susdtls.getFilepath(), susdtls.getFileName()));
	}

	@GetMapping("/reports/{page}")
	public String reports(Model model, @PathVariable("page") int page,
			@RequestParam(name = "rule", required = false) String rule,
			@RequestParam(name = "caseno", required = false) String caseno,
			@RequestParam(name = "misconduct", required = false) String misconduct,
			@RequestParam(name = "viewreport", required = false) String viewreport) {
		LOG.info("hi this is the reports  Details Records page.......");

//		if(caseno != null) {
//			model.addAttribute("caseDetailsAllRecords", caseDetailsRepo.);
//		} else {
//			model.addAttribute("caseDetailsAllRecords", caseDetailsRepo.findAll());
//		}

		// pagination Writting coding
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {
			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<CaseDetails> noticePages = null;
		if (currentUser.getPimsEmployee().getGlobalOrgId().getId() == 701) {
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(caseno)) {
				model.addAttribute("rule", rule);
				model.addAttribute("caseno", caseno);
				model.addAttribute("misconduct", misconduct);

				noticePages = caseDetailsRepo
						.findByRuleApplicableRuleNameAndCaseNoContainingIgnoreCaseAndMisConductTypeMisconductNameContainingIgnoreCase(
								rule, caseno, misconduct, pageable);

			}

			else if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(caseno) && StringUtils.isBlank(misconduct)) {
				model.addAttribute("rule", rule);
				model.addAttribute("caseno", "");
				model.addAttribute("misconduct", "");
				noticePages = caseDetailsRepo.findByRuleApplicableRuleName(rule, pageable);

			}

			else if (StringUtils.isBlank(rule) && StringUtils.isNoneBlank(caseno) && StringUtils.isBlank(misconduct)) {
				model.addAttribute("caseno", caseno);
				model.addAttribute("rule", "");
				model.addAttribute("misconduct", "");
				noticePages = caseDetailsRepo.findByCaseNoContainingIgnoreCase(caseno, pageable);
			}

			// added on 12-10-2022
			else if (StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(caseno) && StringUtils.isBlank(rule)) {
				model.addAttribute("misconduct", misconduct);
				model.addAttribute("rule", "");
				model.addAttribute("caseno", "");
				noticePages = caseDetailsRepo.findByMisConductTypeMisconductNameContainingIgnoreCase(misconduct,
						pageable);

			}

			else {
				noticePages = caseDetailsRepo.findAll(pageable);
				model.addAttribute("rule", "");
				model.addAttribute("caseno", "");
				model.addAttribute("misconduct", "");
			}
		} // if close

		else {
			if (StringUtils.isNotBlank(rule) && StringUtils.isNotBlank(caseno) && StringUtils.isNotBlank(misconduct)) {
				model.addAttribute("rule", rule);
				model.addAttribute("caseno", caseno);
				model.addAttribute("misconduct", misconduct);
				noticePages = caseDetailsRepo
						.findByCurrentUserAndRuleApplicableRuleNameAndCaseNoContainingIgnoreCaseAndMisConductTypeMisconductNameContainingIgnoreCase(
								currentUser, rule, caseno, misconduct, pageable);

			}

			else if (StringUtils.isNotBlank(rule) && StringUtils.isBlank(caseno)
					&& StringUtils.isNotBlank(misconduct)) {
				model.addAttribute("rule", rule);
				model.addAttribute("caseno", "");
				model.addAttribute("misconduct", "");
				noticePages = caseDetailsRepo.findByCurrentUserAndRuleApplicableRuleName(currentUser, rule, pageable);

			}

			else if (StringUtils.isBlank(rule) && StringUtils.isNoneBlank(caseno)
					&& StringUtils.isNotBlank(misconduct)) {
				model.addAttribute("caseno", caseno);
				model.addAttribute("rule", "");
				model.addAttribute("misconduct", "");
				noticePages = caseDetailsRepo.findByCurrentUserAndCaseNoContainingIgnoreCase(currentUser, caseno,
						pageable);

			}
//				added on 12-10-2022
			else if (StringUtils.isNotBlank(misconduct) && StringUtils.isBlank(caseno) && StringUtils.isBlank(rule)) {
				model.addAttribute("misconduct", misconduct);
				model.addAttribute("rule", "");
				model.addAttribute("caseno", "");
				noticePages = caseDetailsRepo.findByMisConductTypeMisconductNameContainingIgnoreCase(currentUser,
						misconduct, pageable);

			}

			else {
				noticePages = caseDetailsRepo.findByCurrentUser(currentUser, pageable);
				model.addAttribute("caseno", "");
				model.addAttribute("rule", "");
				model.addAttribute("misconduct", "");
			}
		} // else close

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
		noticePages.getContent().forEach(s -> {
			List<String> employeeNames = new ArrayList<>();
			List<EmployeeDetails> empList = employeedetailsrepo.findByCasedetailsCaseNo(s.getCaseNo());
			List<ChargesheetDetails> charge = chargesheetRepo.findByCaseDetailsCaseNo(s.getCaseNo());
			if (charge != null && !charge.isEmpty() && charge.get(0).getChargesheetIssued()
					&& charge.get(0).getChargesheetDate() != null) {
				s.setChargesheetDate(charge.get(0).getChargesheetDate().toString());
			}
			empList.parallelStream().forEach(ss -> {
				String fullName = "";
				if (StringUtils.isNotBlank(ss.getFirstName())) {
					// employeeNames.add(ss.getFirstName());
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
		});

		model.addAttribute("noticelist", noticePages.getContent());
		if (viewreport != null) {
			model.addAttribute("viewreport", Boolean.parseBoolean(viewreport));
		} else {
			model.addAttribute("viewreport", false);
		}

		return "DeptInquiryViews/reportswithfilter";

	}



	/* Added on 13-10-2022 */
	@GetMapping("/reportsforAllFieldsIOPO/{page}")
	public String reportsforAllFieldsByInquiryOfficer(Model model, @PathVariable("page") int page,
			@RequestParam(name = "rule", required = false) String rule,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "misconduct", required = false) String misconduct,
			@RequestParam(name = "viewreport", required = false) String viewreport,
			@RequestParam(name = "fromdate", required = false) String fromdate,
			@RequestParam(name = "todate", required = false) String todate,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection,
			@RequestParam(name="inqid",required=false) String inqid ) {
		LOG.info("hi this is the reports  Details Records For search  page.......");

		String customsearch = null;
		if (search != null && search.contains("finalout")) {
			customsearch = search.split("splitting")[1];
		}

		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDirection);
		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");
		model.addAttribute("inquiryofficerlist", inquiryOfficerListRepo.findByActive(true,Sort.by("order")));
		

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
 
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				List<InquiryOfficerDetails> inqList = inquiryofficerRepository
						.findByListOfInquiryOfficerInquiryOfficerlistContainingIgnoreCase(search);
				List<PresentingOfficerDetails> poList = ipresentingofficerrepository
						.findByRevenueDivisionDivisionNameContainingIgnoreCase(search);
				if (inqList.isEmpty() && poList.isEmpty()) {
					if (search.contains("finalout")) {
						List<FinalOutcomeDetails> finalList = finaloutcomeRepository
								.findByWhetherCaseFinallyDecided(true);
						noticePages = caseDetailsRepo.findByFinaloutListInAndGlobalorgGlobalOrgName(finalList,
								customsearch, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findByFinaloutListInAndGlobalorgGlobalOrgName(finalList,
								customsearch, pageableall);
					} else {
						noticePages = caseDetailsRepo
								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
										search, search, search, search, search, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findByRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
										search, search, search, search, search, pageableall);
					}
				} else if (inqList.isEmpty() && !poList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findByPoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
									poList, search, search, search, search, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases =caseDetailsRepo
							.findByPoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
									poList, search, search, search, search, search, pageableall);
				} else if (!inqList.isEmpty() && poList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findByIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
									inqList, search, search, search, search, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases =caseDetailsRepo
							.findByIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
									inqList, search, search, search, search, search, pageableall);
				} else {
					noticePages = caseDetailsRepo
							.findByPoListInOrIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
									poList, inqList, search, search, search, search, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases =caseDetailsRepo
							.findByPoListInOrIoListInOrRuleApplicableRuleNameContainingIgnoreCaseOrCaseNoContainingIgnoreCaseOrMisConductTypeMisconductNameContainingIgnoreCaseOrGlobalorgGlobalOrgNameContainingIgnoreCaseOrFileSubjectContainingIgnoreCase(
									poList, inqList, search, search, search, search, search, pageableall);
				}

			} else {
				if(StringUtils.isNotBlank(inqid)) {
					model.addAttribute("inqofficerid", inqid);
					List<InquiryOfficerDetails> inqList = inquiryofficerRepository.findByListOfInquiryOfficerId(Long.valueOf(inqid));
					noticePages = caseDetailsRepo.findDistinctIdByIoListIn(inqList,pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases =caseDetailsRepo.findDistinctIdByIoListIn(inqList,pageableall);
				} else {
					if ((StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate))) {
						model.addAttribute("fromdate", fromdate);
						model.addAttribute("todate", todate);
						List<ChargesheetDetails> chargeSheetList = chargesheetRepo
								.findByChargesheetDateGreaterThanEqualAndChargesheetDateLessThanEqual(
										LocalDate.parse(fromdate), LocalDate.parse(todate));
						noticePages = caseDetailsRepo.findByChargeSheetListIn(chargeSheetList, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =caseDetailsRepo.findByChargeSheetListIn(chargeSheetList, pageableall);
					} else {
						noticePages = caseDetailsRepo.findAll(pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =caseDetailsRepo.findAll(pageableall);
					}
				}
			}

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
//			List<SubDepartment> subDepartmentList = ;
			List<User> userList = userRepo.findBySubDepartmentIn(
					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
			userList.add(currentUser);
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				List<InquiryOfficerDetails> inqList = inquiryofficerRepository
						.findByListOfInquiryOfficerInquiryOfficerlistContainingIgnoreCase(search);
				List<PresentingOfficerDetails> poList = ipresentingofficerrepository
						.findByRevenueDivisionDivisionNameContainingIgnoreCase(search);
				if (inqList.isEmpty() && poList.isEmpty()) {
					if (search.contains("finalout")) {
						List<FinalOutcomeDetails> finalList = finaloutcomeRepository
								.findByWhetherCaseFinallyDecided(true);
						noticePages = caseDetailsRepo.findByFinaloutListInAndGlobalorgGlobalOrgNameAndCurrentUserIn(
								finalList, customsearch, userList, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =caseDetailsRepo.findByFinaloutListInAndGlobalorgGlobalOrgNameAndCurrentUserIn(
								finalList, customsearch, userList, pageableall);
					} else {
						noticePages = caseDetailsRepo
								.findByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
										userList, search, userList, search, userList, search, userList, search,
										userList, search, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases =caseDetailsRepo
								.findByCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
										userList, search, userList, search, userList, search, userList, search,
										userList, search, pageableall);
					}
				} else if (inqList.isEmpty() && !poList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findByCurrentUserInAndPoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
									userList, poList, userList, search, userList, search, userList, search, userList,
									search, userList, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases =caseDetailsRepo
							.findByCurrentUserInAndPoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
									userList, poList, userList, search, userList, search, userList, search, userList,
									search, userList, search, pageableall);
				} else if (!inqList.isEmpty() && poList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findByCurrentUserInAndIoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
									userList, inqList, userList, search, userList, search, userList, search, userList,
									search, userList, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases =caseDetailsRepo
							.findByCurrentUserInAndIoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
									userList, inqList, userList, search, userList, search, userList, search, userList,
									search, userList, search, pageableall);
				} else {
					noticePages = caseDetailsRepo
							.findByCurrentUserInAndPoListInOrCurrentUserInAndIoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
									userList, poList, userList, inqList, userList, search, userList, search, userList,
									search, userList, search, userList, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findByCurrentUserInAndPoListInOrCurrentUserInAndIoListInOrCurrentUserInAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCaseNoContainingIgnoreCaseOrCurrentUserInAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserInAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserInAndFileSubjectContainingIgnoreCase(
									userList, poList, userList, inqList, userList, search, userList, search, userList,
									search, userList, search, userList, search, pageableall);
				}
			} else if(StringUtils.isNotBlank(inqid)) {
					model.addAttribute("inqofficerid", inqid);
					List<InquiryOfficerDetails> inqList = inquiryofficerRepository.findByListOfInquiryOfficerId(Long.valueOf(inqid));
					noticePages = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserIn(inqList,userList,pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUserIn(inqList,userList,pageableall);
				}else{
				noticePages = caseDetailsRepo.findByCurrentUserIn(userList, pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findByCurrentUserIn(userList, pageableall);
			}

		}

		else {
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				List<InquiryOfficerDetails> inqList = inquiryofficerRepository
						.findByListOfInquiryOfficerInquiryOfficerlistContainingIgnoreCase(search);
				List<PresentingOfficerDetails> poList = ipresentingofficerrepository
						.findByRevenueDivisionDivisionNameContainingIgnoreCase(search);
				if (inqList.isEmpty() && poList.isEmpty()) {
					if (search.contains("finalout")) {
						List<FinalOutcomeDetails> finalList = finaloutcomeRepository
								.findByWhetherCaseFinallyDecided(true);
						noticePages = caseDetailsRepo.findByFinaloutListInAndGlobalorgGlobalOrgNameAndCurrentUser(
								finalList, customsearch, currentUser, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo.findByFinaloutListInAndGlobalorgGlobalOrgNameAndCurrentUser(
								finalList, customsearch, currentUser, pageableall);
					} else {
						noticePages = caseDetailsRepo
								.findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, currentUser, search, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = caseDetailsRepo
								.findByCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, currentUser, search, pageableall);
					}
				} else if (inqList.isEmpty() && !poList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findByCurrentUserAndPoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
									currentUser, poList, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, currentUser, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findByCurrentUserAndPoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
									currentUser, poList, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, currentUser, search, pageableall);
				} else if (!inqList.isEmpty() && poList.isEmpty()) {
					noticePages = caseDetailsRepo
							.findByCurrentUserAndIoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
									currentUser, inqList, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, currentUser, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findByCurrentUserAndIoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
									currentUser, inqList, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, currentUser, search, pageableall);
				} else {
					noticePages = caseDetailsRepo
							.findByCurrentUserAndPoListInOrCurrentUserAndIoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
									currentUser, poList, currentUser, inqList, currentUser, search, currentUser, search,
									currentUser, search, currentUser, search, currentUser, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = caseDetailsRepo
							.findByCurrentUserAndPoListInOrCurrentUserAndIoListInOrCurrentUserAndRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCaseNoContainingIgnoreCaseOrCurrentUserAndMisConductTypeMisconductNameContainingIgnoreCaseOrCurrentUserAndGlobalorgGlobalOrgNameContainingIgnoreCaseOrCurrentUserAndFileSubjectContainingIgnoreCase(
									currentUser, poList, currentUser, inqList, currentUser, search, currentUser, search,
									currentUser, search, currentUser, search, currentUser, search, pageableall);
				}
			} else if(StringUtils.isNotBlank(inqid)) {
				model.addAttribute("inqofficerid", inqid);
				List<InquiryOfficerDetails> inqList = inquiryofficerRepository.findByListOfInquiryOfficerId(Long.valueOf(inqid));
				noticePages = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUser(inqList,currentUser,pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findDistinctIdByIoListInAndCurrentUser(inqList,currentUser,pageableall);
			}else {
				noticePages = caseDetailsRepo.findByCurrentUser(currentUser, pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = caseDetailsRepo.findByCurrentUser(currentUser, pageableall);
			}

		}
		
		if(StringUtils.isNotBlank(inqid)) {
			model.addAttribute("inqofficerid", inqid);

		} else {
			model.addAttribute("inqofficerid", "");

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
		Map<String, CaseDetails> mapEn = new TreeMap<>();
		noticePages.getContent().forEach(s -> {
			InquiryOfficerDetails inq = inquiryofficerRepository.findByCasedetails(s);
			PresentingOfficerDetails presenting = ipresentingofficerrepository.findByCasedetails(s);
			List<FinalOutcomeDetails> finalOut = finaloutcomeRepository.findByCasedetails(s);
			if (inq != null) {
				if (inq.getListOfInquiryOfficer() != null) {
					s.setInquiryofficername(inq.getListOfInquiryOfficer().getInquiryOfficerlist());
				}
			}
			if (presenting != null) {
				if (presenting.getRevenueDivision() != null) {
					s.setPresentingofficerdivision(presenting.getRevenueDivision().getDivisionName());
				}
			}
			if (finalOut != null && !finalOut.isEmpty()) {
				s.setFinaloutcomestatus(finalOut.get(0).isWhetherCaseFinallyDecided());
			}
			mapEn.put(s.getCaseNo(), s);
		});
		model.addAttribute("noticelist", mapEn.values().parallelStream().collect(Collectors.toList()));
		if (viewreport != null) {
			model.addAttribute("viewreport", Boolean.parseBoolean(viewreport));
		} else {
			model.addAttribute("viewreport", false);
		}

		return "DeptInquiryViews/IOPOReport";

	}

	@GetMapping("/reportsforSuspended/{page}")
	public String reportsforSuspended(Model model, @PathVariable("page") int page,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		LOG.info("hi this is the reports  Details Records For search  page.......");

		String customsearch = null;
		if (search != null && search.contains("splitting")) {
			customsearch = search.split("splitting")[1];
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

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<SuspensionDetails> noticePages = null;
		Page<SuspensionDetails> allCases = null;
		
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				try {
					LocalDate date = LocalDate.parse(search);
					noticePages = suspensionDetailsRepo
							.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrDateOfOrderOrDateofActualSuspensionOrDateofDeemedSuspensionOrDateofExtensionOrderOrEmployeeSuspendedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									search, search, search, search, search, search, date, date, date, date,
									BooleanUtils.toBooleanObject(search), search, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = suspensionDetailsRepo
							.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrDateOfOrderOrDateofActualSuspensionOrDateofDeemedSuspensionOrDateofExtensionOrderOrEmployeeSuspendedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									search, search, search, search, search, search, date, date, date, date,
									BooleanUtils.toBooleanObject(search), search, search, pageableall);
				} catch (Exception a) {
					if (customsearch != null && StringUtils.isBlank(customsearch)) {
						noticePages = suspensionDetailsRepo
								.findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = suspensionDetailsRepo
								.findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], pageableall);
					} else {
						noticePages = suspensionDetailsRepo
								.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrEmployeeSuspendedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										search, search, search, search, search, search,
										true, search, search, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = suspensionDetailsRepo
								.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrSusUnderRuleSusRuleNameContainingIgnoreCaseOrEmployeeSuspendedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										search, search, search, search, search, search,
										true, search, search, pageableall);
					}
				}
			} else {
				noticePages = suspensionDetailsRepo.findAll(pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = suspensionDetailsRepo.findAll(pageableall);
			}

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
			List<User> userList = userRepo.findBySubDepartmentIn(
					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
			userList.add(currentUser);
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				try {
					LocalDate date = LocalDate.parse(search);
					noticePages = suspensionDetailsRepo
							.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserInAndDateOfOrderOrCurrentUserInAndDateofActualSuspensionOrCurrentUserInAndDateofDeemedSuspensionOrCurrentUserInAndDateofExtensionOrderOrCurrentUserInAndEmployeeSuspendedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									userList, search, userList, search, userList, search, userList, search, userList,
									search, userList, search, userList, date, userList, date, userList, date, userList,
									date, userList, BooleanUtils.toBooleanObject(search), userList, search, userList,
									search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = suspensionDetailsRepo
							.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserInAndDateOfOrderOrCurrentUserInAndDateofActualSuspensionOrCurrentUserInAndDateofDeemedSuspensionOrCurrentUserInAndDateofExtensionOrderOrCurrentUserInAndEmployeeSuspendedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									userList, search, userList, search, userList, search, userList, search, userList,
									search, userList, search, userList, date, userList, date, userList, date, userList,
									date, userList, BooleanUtils.toBooleanObject(search), userList, search, userList,
									search, pageableall);
				} catch (Exception a) {
					if (customsearch != null) {
						noticePages = suspensionDetailsRepo
								.findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUserIn(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], userList, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = suspensionDetailsRepo
								.findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUserIn(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], userList, pageableall);
					} else {
						noticePages = suspensionDetailsRepo
								.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserInAndEmployeeSuspendedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										userList, search, userList, search, userList, search, userList, search,
										userList, search, userList, search, userList,
										BooleanUtils.toBooleanObject(search), userList, search, userList, search,
										pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = suspensionDetailsRepo
								.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserInAndEmployeeSuspendedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										userList, search, userList, search, userList, search, userList, search,
										userList, search, userList, search, userList,
										BooleanUtils.toBooleanObject(search), userList, search, userList, search,
										pageableall);
					}
				}

			} else {
				noticePages = suspensionDetailsRepo.findByCurrentUserIn(userList, pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = suspensionDetailsRepo.findByCurrentUserIn(userList, pageableall);
			}
		} else {
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				try {
					LocalDate date = LocalDate.parse(search);
					noticePages = suspensionDetailsRepo
							.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndDateOfOrderOrCurrentUserAndDateofActualSuspensionOrCurrentUserAndDateofDeemedSuspensionOrCurrentUserAndDateofExtensionOrderOrCurrentUserAndEmployeeSuspendedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									currentUser, search, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, currentUser, search, currentUser, date, currentUser, date,
									currentUser, date, currentUser, date, currentUser,
									BooleanUtils.toBooleanObject(search), currentUser, search, currentUser, search,
									pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = suspensionDetailsRepo
							.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndDateOfOrderOrCurrentUserAndDateofActualSuspensionOrCurrentUserAndDateofDeemedSuspensionOrCurrentUserAndDateofExtensionOrderOrCurrentUserAndEmployeeSuspendedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									currentUser, search, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, currentUser, search, currentUser, date, currentUser, date,
									currentUser, date, currentUser, date, currentUser,
									BooleanUtils.toBooleanObject(search), currentUser, search, currentUser, search,
									pageableall);
				} catch (Exception a) {
					if (customsearch != null) {
						noticePages = suspensionDetailsRepo
								.findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUser(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], currentUser, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = suspensionDetailsRepo
								.findByEmployeeSuspendedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUser(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], currentUser, pageableall);
					} else {
						noticePages = suspensionDetailsRepo
								.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndEmployeeSuspendedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, currentUser, search, currentUser, search, currentUser,
										BooleanUtils.toBooleanObject(search), currentUser, search, currentUser, search,
										pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = suspensionDetailsRepo
								.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndSusUnderRuleSusRuleNameContainingIgnoreCaseOrCurrentUserAndEmployeeSuspendedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, currentUser, search, currentUser, search, currentUser,
										BooleanUtils.toBooleanObject(search), currentUser, search, currentUser, search,
										pageableall);
					}
				}

			} else {
				List<ReInstatedDetails> reinstaList = reinstatedRepository.findByCurrentUser(currentUser);
//				List<String> sevarthIdList = reinstaList.stream().map(s->s.getSevarthId()).collect(Collectors.toList());
				List<String> firstName = reinstaList.stream().map(s -> s.getFirstName()).collect(Collectors.toList());
//				List<String> middleName = reinstaList.stream().map(s->s.getMiddleName()).collect(Collectors.toList());
//				List<String> lastName = reinstaList.stream().map(s->s.getLastName()).collect(Collectors.toList());
				noticePages = suspensionDetailsRepo.findByCurrentUserAndFirstNameNotIn(currentUser, firstName,
						pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases =  suspensionDetailsRepo.findByCurrentUserAndFirstNameNotIn(currentUser, firstName,
						pageableall);
			}

		}
		//// -------------------------------///-----------------
		
		Map<Long,CaseDetails> caseList = new HashMap<>();
		noticePages.stream().forEach(s->caseList.put(s.getCasedetails().getId(), s.getCasedetails()));
		model.addAttribute("caselist", caseList.values().parallelStream().collect(Collectors.toList()));
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
		return "DeptInquiryViews/reportsSuspended";

	}

	@GetMapping("/reportsforReinstated/{page}")
	public String reportsforReinstated(Model model, @PathVariable("page") int page,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		LOG.info("hi this is the reports  Details Records For search  page.......");

		String customsearch = null;
		if (search != null && search.contains("splitting")) {
			customsearch = search.split("splitting")[1];
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

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<ReInstatedDetails> noticePages = null;
		Page<ReInstatedDetails> allCases = null;

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				try {
					LocalDate date = LocalDate.parse(search);
					noticePages = reinstatedRepository
							.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrReInstatedOrderDateOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									search, search, search, search, search, date, BooleanUtils.toBooleanObject(search),
									search, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = reinstatedRepository
							.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrReInstatedOrderDateOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									search, search, search, search, search, date, BooleanUtils.toBooleanObject(search),
									search, search, pageableall);
				} catch (Exception a) {
					if (customsearch != null) {
						noticePages = reinstatedRepository
								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = reinstatedRepository
								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], pageableall);
					} else {
						noticePages = reinstatedRepository
								.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										search, search, search, search, search, BooleanUtils.toBooleanObject(search),
										search, search, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = reinstatedRepository
								.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										search, search, search, search, search, BooleanUtils.toBooleanObject(search),
										search, search, pageableall);
						
					}
				}
			} else {
				noticePages = reinstatedRepository.findAll(pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases =  reinstatedRepository.findAll(pageableall);
			}

		} else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
			List<User> userList = userRepo.findBySubDepartmentIn(
					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
			userList.add(currentUser);
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				try {
					LocalDate date = LocalDate.parse(search);
					noticePages = reinstatedRepository
							.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndReInstatedOrderDateOrCurrentUserInAndEmployeeReInstatedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									userList, search, userList, search, userList, search, userList, search, userList,
									search, userList, date, userList, BooleanUtils.toBooleanObject(search), userList,
									search, userList, search, pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = reinstatedRepository
							.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndReInstatedOrderDateOrCurrentUserInAndEmployeeReInstatedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									userList, search, userList, search, userList, search, userList, search, userList,
									search, userList, date, userList, BooleanUtils.toBooleanObject(search), userList,
									search, userList, search, pageableall);
				} catch (Exception a) {
					if (customsearch != null) {
						noticePages = reinstatedRepository
								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUserIn(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], userList, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = reinstatedRepository
								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUserIn(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], userList, pageableall);
					} else {
						noticePages = reinstatedRepository
								.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndEmployeeReInstatedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										userList, search, userList, search, userList, search, userList, search,
										userList, search, userList, BooleanUtils.toBooleanObject(search), userList,
										search, userList, search, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = reinstatedRepository
								.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndEmployeeReInstatedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										userList, search, userList, search, userList, search, userList, search,
										userList, search, userList, BooleanUtils.toBooleanObject(search), userList,
										search, userList, search, pageableall);
					}
				}

			} else {
				noticePages = reinstatedRepository.findByCurrentUserIn(userList, pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = reinstatedRepository.findByCurrentUserIn(userList, pageableall);
			}

		} else {
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				try {
					LocalDate date = LocalDate.parse(search);
					noticePages = reinstatedRepository
							.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndReInstatedOrderDateOrCurrentUserAndEmployeeReInstatedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									currentUser, search, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, currentUser, date, currentUser,
									BooleanUtils.toBooleanObject(search), currentUser, search, currentUser, search,
									pageable);
					Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
					allCases = reinstatedRepository
							.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndReInstatedOrderDateOrCurrentUserAndEmployeeReInstatedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
									currentUser, search, currentUser, search, currentUser, search, currentUser, search,
									currentUser, search, currentUser, date, currentUser,
									BooleanUtils.toBooleanObject(search), currentUser, search, currentUser, search,
									pageableall);
				} catch (Exception a) {
					if (customsearch != null) {
						noticePages = reinstatedRepository
								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUser(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], currentUser, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = reinstatedRepository
								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUser(
										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
										search.split("splitting")[1], currentUser, pageableall);
					} else {
						noticePages = reinstatedRepository
								.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndEmployeeReInstatedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, currentUser, search, currentUser, BooleanUtils.toBooleanObject(search),
										currentUser, search, currentUser, search, pageable);
						Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
						allCases = reinstatedRepository
								.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndEmployeeReInstatedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
										currentUser, search, currentUser, search, currentUser, search, currentUser,
										search, currentUser, search, currentUser, BooleanUtils.toBooleanObject(search),
										currentUser, search, currentUser, search, pageableall);
					}
				}

			} else {
				noticePages = reinstatedRepository.findByCurrentUser(currentUser, pageable);
				Pageable pageableall = PageRequest.of(0, Integer.MAX_VALUE, sort);
				allCases = reinstatedRepository.findByCurrentUser(currentUser, pageableall);
			}

		}
		//// -------------------------------///-----------------

		Map<Long,CaseDetails> caseList = new HashMap<>();
		noticePages.stream().forEach(s->caseList.put(s.getCasedetails().getId(), s.getCasedetails()));
		model.addAttribute("caselist", caseList.values().parallelStream().collect(Collectors.toList()));
		model.addAttribute("caseDetailsAllRecords", noticePages);
		model.addAttribute("currentPage", page);
		model.addAttribute("allcases", allCases);
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
		return "DeptInquiryViews/reportsReinstated";

	}

	// this code for download view pdf file

	@PostMapping("/downloadorder/")
	@ResponseBody
	public ResponseEntity<?> getFile(ProbationOrderDto body)
			throws IOException, InterruptedException, GeneralSecurityException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		

//		String baseUrl =  "http://" + request.getServerName() + ":8080" ;
//		String baseUrl = "https://115.124.119.239";
		String baseUrl = null;
//		if(request.getServerPort()==443) {
//			baseUrl = request.getScheme() + "://" + request.getServerName();
//		} else {
//			baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();	
//		}
		if(request.getServerName().equalsIgnoreCase("localhost")) {
			baseUrl = "http://localhost:8080";
		}else if(request.getServerName().equalsIgnoreCase("10.186.48.32")) {
			baseUrl = "http://10.186.48.32:8080";
		} else if(request.getServerName().equalsIgnoreCase("115.124.119.239")){
			baseUrl = "http://10.48.102.3:8080";
		} else if(request.getServerName().equalsIgnoreCase("10.48.102.3")) {
			baseUrl = "http://10.48.102.3:8080";
		} else {
			baseUrl = "http://localhost:8080";
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add(CONTENTDEPOSITION, "inline; filename=HTMLPreview.pdf");
		ConverterProperties props = new ConverterProperties();
		FontProvider ff = new DefaultFontProvider(true, true, true);

		ff.addFont(FontProgramFactory.createFont("FreeSans.ttf"));
		props.setFontProvider(ff);

		props.setBaseUri(baseUrl + "/de/othercontent/");
		List<String> src = new ArrayList<>();
		src.add(Jsoup.parse(body.getHtml1()).html());
		if (body.getHtml2() != null) {
			src.add(Jsoup.parse(body.getHtml2()).html());
		}

		MediaDeviceDescription mediaDeviceDescription = new MediaDeviceDescription(
				com.itextpdf.styledxmlparser.css.media.MediaType.PRINT);
//        mediaDeviceDescription.setOrientation("LANDSCAPE");
		props.setMediaDeviceDescription(mediaDeviceDescription);

		PdfWriter writer = new PdfWriter(out);
		PdfDocument pdf = new PdfDocument(writer);

//        pdf.setDefaultPageSize(new PageSize(5000,1000));
		pdf.setDefaultPageSize(PageSize.A4);
		PdfMerger merger = new PdfMerger(pdf);

		for (String html : src) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfDocument temp = new PdfDocument(new PdfWriter(baos));
			temp.setDefaultPageSize(PageSize.A4.rotate());
			Document document = Jsoup.parse(html);
			Element head = document.head();
			head.append("<link rel=\"stylesheet\" href=" + props.getBaseUri() + "materialdesignicons.min.css" + ">");
			head.append("<link rel=\"stylesheet\" href=" + props.getBaseUri() + "vendor.bundle.base.css" + ">");
			head.append("<link rel=\"stylesheet\" href=" + props.getBaseUri() + "style.css" + ">");
			head.append("<link rel=\"stylesheet\" href=" + props.getBaseUri() + "bootstrap-table.min.css" + ">");
			HtmlConverter.convertToPdf(document.html(), temp, props);

			temp = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
			temp.setDefaultPageSize(PageSize.TABLOID);
			merger.merge(temp, 1, temp.getNumberOfPages());
			temp.close();

		}
		pdf.close();

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(PROBATIONCONSTANTS.BASE64STRING.label + Base64.getEncoder().encodeToString(out.toByteArray()));

	}

	/*
	 * Method :Suspension Without De details landing method. Description : written
	 * landing form logic
	 */

	@GetMapping("/suspensionDetailsWithoutDE/{page}")
	public String suspensionDetailsWithoutDE(Model model, @PathVariable("page") int page) {
	
		LOG.info("this is the suspensionDetailsWithoutDE landing form methods (07/11/2022) ...");
		request.getSession().setAttribute("caseid", null);
//		model.addAttribute("suspensionRules", suspensionRuleRepo.findAll());
		model.addAttribute("suspensionObj", new SuspensionDetails());
		model.addAttribute("suspensionRules", suspensionRuleRepo.findAll(Sort.by("order")));
		
		// pagination Writting coding start here.....

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);
				}

				if (page < 0) {

					page = 1;
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				User currentUser = userService.getCurrentUserPojo();
				Page<SuspensionDetails> noticePages = null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = suspensionDetailsRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
				} else {
					noticePages = suspensionDetailsRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
				}
				model.addAttribute("serviceGroup", serviceGrpRepo.findByOrderById());
				model.addAttribute("suspensionEmployeeList", noticePages);
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

				return "DeptInquiryViews/suspensionDetailsWithoutDE";
	}

	
	/*
	 * Method :Suspension details saving method.. Description :written saving form
	 * logic code
	 */
	@PostMapping("/addsuspensionWithoutDEDetails")
	public String addsuspensionWithoutDEDetails(
			@ModelAttribute("suspensionObj") SuspensionDetailsDto suspensiondetailsdto, BindingResult result,
			Model model, RedirectAttributes redirect,
			@RequestParam(name = "uploadOrderActualSuspensionName", required = false) MultipartFile file1,
			@RequestParam(name = "uploadOrderDeemedSuspension", required = false) MultipartFile file2,
			@RequestParam(name = "extensionUploadOrder", required = false) MultipartFile file3) {

		/* Creating new object of Suspension Details */

		SuspensionDetails suspensiondetails = new SuspensionDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (suspensiondetailsdto.getId() != null && suspensiondetailsdto.getId() > 0) {
				suspensiondetails = suspensionDetailsRepo.findById(suspensiondetailsdto.getId())
						.orElse(new SuspensionDetails());

				if (StringUtils.isNotBlank(suspensiondetailsdto.getCaseNo())
						&& caseDetailsRepo.existsByCaseNoAndCurrentUser(suspensiondetailsdto.getCaseNo(),currentUser)) {
					suspensiondetails.setCasedetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(suspensiondetailsdto.getCaseNo(),currentUser));
				}

//			suspensiondetails.setCasedetails(suspensiondetailsdto.getCasedetails());
				suspensiondetails.setSevarthId(suspensiondetailsdto.getSevarthId());
				suspensiondetails.setEmployeeId(suspensiondetailsdto.getEmployeeId());
				suspensiondetails.setFirstName(suspensiondetailsdto.getFirstName());
				suspensiondetails.setLastName(suspensiondetailsdto.getLastName());
				suspensiondetails.setMiddleName(suspensiondetailsdto.getMiddleName());
				suspensiondetails.setEmail(suspensiondetailsdto.getEmail());
				suspensiondetails.setMobileNumber(suspensiondetailsdto.getMobileNumber());
				if(suspensiondetailsdto.getServicegroup() != null && suspensiondetailsdto.getDesignation() != null) {
					suspensiondetails.setDesignation(designationRepo.findById(suspensiondetailsdto.getDesignation().getDesignation_id()).orElse(new Designation()));
					suspensiondetails.setServicegroup(serviceGrpRepo.findById(suspensiondetailsdto.getServicegroup().getId()).orElse(new Service_Group()));
				}
				suspensiondetails.setMiddleName(suspensiondetailsdto.getMiddleName());
				suspensiondetails.setEmployeeSuspended(suspensiondetailsdto.isEmployeeSuspended());
				if (suspensiondetailsdto.getSusUnderRule() != null) {
					suspensiondetails.setSusUnderRule(suspensionRuleRepo
							.findById(suspensiondetailsdto.getSusUnderRule().getId()).orElse(new SuspensionRules()));
				} else {
					suspensiondetails.setSusUnderRule(null);
				}
				suspensiondetails.setFileNo(suspensiondetailsdto.getFileNo());
				suspensiondetails.setUploadOrderActualSuspensionName(file1.getOriginalFilename());
				suspensiondetails.setDateOfOrder(suspensiondetailsdto.getDateOfOrder());
				suspensiondetails.setDateofActualSuspension(suspensiondetailsdto.getDateofActualSuspension());
				suspensiondetails.setDateofDeemedSuspension(suspensiondetailsdto.getDateofDeemedSuspension());
				suspensiondetails.setUploadOrderDeemedSuspension(suspensiondetailsdto.getUploadOrderDeemedSuspension());
				suspensiondetails
						.setWhetherSuspensionIsExtnd3Months(suspensiondetailsdto.isWhetherSuspensionIsExtnd3Months());
				suspensiondetails.setPeriodofExtension(suspensiondetailsdto.getPeriodofExtension());
				suspensiondetails.setDateofExtensionOrder(suspensiondetailsdto.getDateofExtensionOrder());
				suspensiondetails.setExtensionUploadOrder(suspensiondetailsdto.getExtensionUploadOrder());
				suspensiondetails.setWithde(suspensiondetailsdto.isWithde());
				// adding code according to latest pdf
				suspensiondetails.setDateOfOrder1(suspensiondetailsdto.getDateOfOrder1());
				suspensiondetails.setPeriodofExtensionInDays(suspensiondetailsdto.getPeriodofExtensionInDays());

				if (file1 != null && !file1.isEmpty()
						&& file1.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetailswithoutde/" + suspensiondetails.getId() + "/";
					suspensiondetails.setUploadOrderActualSuspensionName(file1.getOriginalFilename());
					suspensiondetails.setContentType(file1.getContentType());
					suspensiondetails.setFilepathActualSuspension(filePath);
					fileService.fileUpload(filePath, file1, suspensiondetails.getUploadOrderActualSuspensionName());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				// this is for second file upload code
				if (file2 != null && !file2.isEmpty()
						&& file2.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetailswithoutde/" + suspensiondetails.getId() + "/";
					suspensiondetails.setUploadOrderDeemedSuspension(file2.getOriginalFilename());
					suspensiondetails.setContentType(file2.getContentType());
					suspensiondetails.setFilePathUploadOrderDeemedSuspension(filePath);
					fileService.fileUpload(filePath, file2, suspensiondetails.getUploadOrderDeemedSuspension());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				// this is for third file upload code

				if (file3 != null && !file3.isEmpty()
						&& file3.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetailswithoutde/" + suspensiondetails.getId() + "/";
					suspensiondetails.setExtensionUploadOrder(file3.getOriginalFilename());
					suspensiondetails.setContentType(file3.getContentType());
					suspensiondetails.setFilePathExtensionUploadOrder(filePath);
					fileService.fileUpload(filePath, file3, suspensiondetails.getExtensionUploadOrder());
				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				suspensiondetails = suspensionDetailsRepo.save(suspensiondetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				
				return "redirect:/departmentalEnquiry/editsusdtlsRecordswithoutDE/"+suspensiondetails.getId()+"/1";

			}

			else {
				// save
				if (StringUtils.isBlank(suspensiondetailsdto.getFileNo())) {
					CaseDetails cd = caseDetailsRepo.findByCaseNoContainingIgnoreCase(suspensiondetailsdto.getCaseNo())
							.get(0);

					if (cd != null) {
						suspensiondetails.setCasedetails(cd);
					} else {
						redirect.addFlashAttribute(departmentconstants.MESSAGE,
								messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
						return "redirect:/departmentalEnquiry/suspensionDetailsWithoutDE/1";
					}

					if (StringUtils.isNotBlank(suspensiondetailsdto.getSevarthId()) && suspensionDetailsRepo
							.existsByCasedetailsAndSevarthId(cd, suspensiondetailsdto.getSevarthId())) {
						suspensiondetails = suspensionDetailsRepo.findByCasedetailsAndSevarthId(cd,
								suspensiondetailsdto.getSevarthId());
					}
				}
				suspensiondetails.setFileNo(suspensiondetailsdto.getFileNo());

				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					suspensiondetails.setCurrentUser(currentUser);
					suspensiondetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				suspensiondetails.setSevarthId(suspensiondetailsdto.getSevarthId());
				suspensiondetails.setEmployeeId(suspensiondetailsdto.getEmployeeId());
				suspensiondetails.setFirstName(suspensiondetailsdto.getFirstName());
				suspensiondetails.setLastName(suspensiondetailsdto.getLastName());
				suspensiondetails.setEmail(suspensiondetailsdto.getEmail());
				suspensiondetails.setMobileNumber(suspensiondetailsdto.getMobileNumber());
				if(suspensiondetailsdto.getServicegroup() != null && suspensiondetailsdto.getDesignation() != null) {
					suspensiondetails.setDesignation(designationRepo.findById(suspensiondetailsdto.getDesignation().getDesignation_id()).orElse(new Designation()));
					suspensiondetails.setServicegroup(serviceGrpRepo.findById(suspensiondetailsdto.getServicegroup().getId()).orElse(new Service_Group()));
				}
				/* suspensiondetails.setWithde(suspensiondetailsdto.isWithde()); */
				// suspensiondetails.setUploadOrderActualSuspensionName(suspensiondetailsdto.getUploadOrderActualSuspensionName());
//				suspensiondetails.setUploadOrderActualSuspensionName(file1.getOriginalFilename());
				suspensiondetails.setMiddleName(suspensiondetailsdto.getMiddleName());
				suspensiondetails.setEmployeeSuspended(suspensiondetailsdto.isEmployeeSuspended());

				if (suspensiondetailsdto.getSusUnderRule() != null) {
					suspensiondetails.setSusUnderRule(suspensionRuleRepo
							.findById(suspensiondetailsdto.getSusUnderRule().getId()).orElse(new SuspensionRules()));
				}

				else {
					suspensiondetails.setSusUnderRule(null);
				}
				suspensiondetails.setDateOfOrder(suspensiondetailsdto.getDateOfOrder());

				suspensiondetails.setDateofActualSuspension(suspensiondetailsdto.getDateofActualSuspension());
//				suspensiondetails
//						.setUploadOrderActualSuspensionName(suspensiondetailsdto.getUploadOrderActualSuspensionName());
//
				// 17-08-2022
				suspensiondetails.setDateofDeemedSuspension(suspensiondetailsdto.getDateofDeemedSuspension());
//				suspensiondetails.setUploadOrderDeemedSuspension(suspensiondetailsdto.getUploadOrderDeemedSuspension());
				suspensiondetails
						.setWhetherSuspensionIsExtnd3Months(suspensiondetailsdto.isWhetherSuspensionIsExtnd3Months());
				suspensiondetails.setPeriodofExtension(suspensiondetailsdto.getPeriodofExtension());
				suspensiondetails.setDateofExtensionOrder(suspensiondetailsdto.getDateofExtensionOrder());
//				suspensiondetails.setExtensionUploadOrder(suspensiondetailsdto.getExtensionUploadOrder());

				// adding code according to latest pdf
				suspensiondetails.setDateOfOrder1(suspensiondetailsdto.getDateOfOrder1());
				suspensiondetails.setPeriodofExtensionInDays(suspensiondetailsdto.getPeriodofExtensionInDays());

				// adding code according to latest pdf
				suspensiondetails.setDateOfOrder1(suspensiondetailsdto.getDateOfOrder1());
				suspensiondetails.setPeriodofExtensionInDays(suspensiondetailsdto.getPeriodofExtensionInDays());

				if (file1 != null && !file1.isEmpty()
						&& file1.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetailswithoutde/" + suspensiondetails.getId() + "/";
					suspensiondetails.setUploadOrderActualSuspensionName(file1.getOriginalFilename());
					suspensiondetails.setContentType(file1.getContentType());
					suspensiondetails.setFilepathActualSuspension(filePath);
					fileService.fileUpload(filePath, file1, suspensiondetails.getUploadOrderActualSuspensionName());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				// this is for second file upload code
				if (file2 != null && !file2.isEmpty()
						&& file2.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetailswithoutde/" + suspensiondetails.getId() + "/";
					suspensiondetails.setUploadOrderDeemedSuspension(file2.getOriginalFilename());
					suspensiondetails.setContentType(file2.getContentType());
					suspensiondetails.setFilePathUploadOrderDeemedSuspension(filePath);
					fileService.fileUpload(filePath, file2, suspensiondetails.getUploadOrderDeemedSuspension());
				} else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				// this is for third file upload code

				if (file3 != null && !file3.isEmpty()
						&& file3.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (suspensiondetails.getCasedetails() != null) {
						id = suspensiondetails.getCasedetails().getId().toString();
					} else {
						id = suspensiondetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "suspensiondetailswithoutde/" + suspensiondetails.getId() + "/";
					suspensiondetails.setExtensionUploadOrder(file3.getOriginalFilename());
					suspensiondetails.setContentType(file3.getContentType());
					suspensiondetails.setFilePathExtensionUploadOrder(filePath);
					fileService.fileUpload(filePath, file3, suspensiondetails.getExtensionUploadOrder());
				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				suspensiondetails = suspensionDetailsRepo.save(suspensiondetails);
				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editsusdtlsRecordswithoutDE/"+suspensiondetails.getId()+"/1";

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/suspensionDetailsWithoutDE/1";

	}

	/* ReInstated landing form WithoutDE Method(07/11/2022) */

	@GetMapping("/reinstatedDetailsWithoutDE/{page}")
	public String reinstatedDetailsWithoutDE(Model model, @PathVariable("page") int page, RedirectAttributes redirect) {
		LOG.info("Hi this is thereinstatedDetailsWithoutDE page method.............");
		request.getSession().setAttribute("caseid", null);
		ReInstatedDetails reinstateddetails = new ReInstatedDetails();
		model.addAttribute("reinstatedObjDetails", reinstateddetails);
		
		
		// pagination Writting coding start here.....

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);
				}

				if (page < 0) {

					page = 1;
				}
				

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				User currentUser = userService.getCurrentUserPojo();
				Page<ReInstatedDetails> noticePages = null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					
					noticePages = reinstatedRepository.findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
				} else {
					noticePages = reinstatedRepository.findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
				}

				
				
				
		
				
				
				
				model.addAttribute("reinstatedGridList", noticePages);
				// System.out.println(noticePages);
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
				model.addAttribute("reinstatedGridList", noticePages.getContent());


		
		

		return "DeptInquiryViews/reinstatedDetailsWithoutDE";
	}

	/*
	 * Method : details saving 
	 * method.. Description : written saving form logic code
	 */

	@PostMapping("/addreinstatedDetailsWithoutDe")
	public String addreinstatedDetailsWithoutDe(
			@ModelAttribute("reinstatedObjDetails") ReInstatedDetailsDto reinstateddetailsdto, BindingResult result,
			Model model, RedirectAttributes redirect,
			@RequestParam(name = "UploadOrderReInstatment", required = false) MultipartFile file1) {
		LOG.info("HI This  is the addreinstatedDetailsWithoutDe  save method call...");

		/* Creating new object of ReInstatedDetails screen */
		ReInstatedDetails reinstateddetails = new ReInstatedDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (reinstateddetailsdto.getId() != null && reinstateddetailsdto.getId() > 0) {

				/* finding id of ReInstated details id */
				reinstateddetails = reinstatedRepository.findById(reinstateddetailsdto.getId())
						.orElse(new ReInstatedDetails());

				if (StringUtils.isNotBlank(reinstateddetailsdto.getCaseNo())
						&& caseDetailsRepo.existsByCaseNoAndCurrentUser(reinstateddetailsdto.getCaseNo(),currentUser)) {
					reinstateddetails.setCasedetails(
							caseDetailsRepo.findByCaseNoAndCurrentUser(reinstateddetailsdto.getCaseNo(),currentUser));
				}
				//reinstateddetails.setCasedetails(reinstateddetailsdto.getCaseNo());
				reinstateddetails.setSevarthId(reinstateddetailsdto.getSevarthId());
				reinstateddetails.setEmployeeId(reinstateddetailsdto.getEmployeeId());
				reinstateddetails.setFileNo(reinstateddetailsdto.getFileNo());
				/* reinstateddetails.setWithde(reinstateddetailsdto.isWithde()); */
				reinstateddetails.setFirstName(reinstateddetailsdto.getFirstName());
				reinstateddetails.setLastName(reinstateddetailsdto.getLastName());
				reinstateddetails.setWithde(reinstateddetailsdto.isWithde());
				reinstateddetails.setMiddleName(reinstateddetailsdto.getMiddleName());
				reinstateddetails.setEmployeeReInstated(reinstateddetailsdto.isEmployeeReInstated());
				reinstateddetails.setReInstatedOrderDate(reinstateddetailsdto.getReInstatedOrderDate());
				reinstateddetails.setDateofSuspension(reinstateddetailsdto.getDateofSuspension());
				reinstateddetails.setUploadOrderReInstatment(reinstateddetailsdto.getUploadOrderReInstatment());

				if (file1 != null && !file1.isEmpty()
						&& file1.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (reinstateddetails.getCasedetails() != null) {
						id = reinstateddetails.getCasedetails().getId().toString();
					} else {
						id = "fileNo/" + reinstateddetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "reinstatedDetailswithoutde/" + reinstateddetails.getId() + "/";
					reinstateddetails.setUploadOrderReInstatment(file1.getOriginalFilename());
					reinstateddetails.setContentType(file1.getContentType());
					reinstateddetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file1, reinstateddetails.getUploadOrderReInstatment());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				reinstateddetails = reinstatedRepository.save(reinstateddetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				
				return "redirect:/departmentalEnquiry/editReinstateddtlsRecordsWithoutDe/"+reinstateddetails.getId()+"/1";	

			}

			else {

				

				if (StringUtils.isBlank(reinstateddetailsdto.getFileNo())) {
					CaseDetails cd = caseDetailsRepo.findByCaseNoContainingIgnoreCase(reinstateddetailsdto.getCaseNo())
							.get(0);

					if (cd != null) {
						reinstateddetails.setCasedetails(cd);
					} else {
						redirect.addFlashAttribute(departmentconstants.MESSAGE,
								messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
						return "redirect:/departmentalEnquiry/reinstatedDetailsWithoutDE/1";
					}
					
					
					if (StringUtils.isNotBlank(reinstateddetailsdto.getSevarthId()) && reinstatedRepository
							.existsByCasedetailsAndSevarthId(cd, reinstateddetailsdto.getSevarthId())) {
						reinstateddetails = reinstatedRepository.findByCasedetailsAndSevarthId(cd,
								reinstateddetailsdto.getSevarthId());
					}
				}
				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					reinstateddetails.setCurrentUser(currentUser);
					reinstateddetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				reinstateddetails.setFileNo(reinstateddetailsdto.getFileNo());
				reinstateddetails.setCaseNo(reinstateddetailsdto.getCaseNo());
				/* reinstateddetails.setWithde(reinstateddetailsdto.isWithde()); */

				/* reinstateddetails.setSevarthId(reinstateddetailsdto.getSevarthId()); */
				reinstateddetails.setSevarthId(reinstateddetailsdto.getSevarthId());
				reinstateddetails.setEmployeeId(reinstateddetailsdto.getEmployeeId());
				reinstateddetails.setFirstName(reinstateddetailsdto.getFirstName());
				reinstateddetails.setLastName(reinstateddetailsdto.getLastName());
				reinstateddetails.setMiddleName(reinstateddetailsdto.getMiddleName());
				reinstateddetails.setEmployeeReInstated(reinstateddetailsdto.isEmployeeReInstated());
				reinstateddetails.setReInstatedOrderDate(reinstateddetailsdto.getReInstatedOrderDate());
				reinstateddetails.setDateofSuspension(reinstateddetailsdto.getDateofSuspension());
				reinstateddetails.setUploadOrderReInstatment(reinstateddetailsdto.getUploadOrderReInstatment());
				// reinstateddetails.setUploadOrderReInstatment(reinstateddetailsdto.getUploadOrderReInstatment());

				if (file1 != null && !file1.isEmpty()
						&& file1.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (reinstateddetails.getCasedetails() != null) {
						id = reinstateddetails.getCasedetails().getId().toString();
					} else {
						id = "fileNo/" + reinstateddetails.getFileNo();
					}

					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "reinstatedDetailswithoutde/" + reinstateddetails.getId() + "/";
					reinstateddetails.setUploadOrderReInstatment(file1.getOriginalFilename());
					reinstateddetails.setContentType(file1.getContentType());
					reinstateddetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file1, reinstateddetails.getUploadOrderReInstatment());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				reinstateddetails = reinstatedRepository.save(reinstateddetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				
				return "redirect:/departmentalEnquiry/editReinstateddtlsRecordsWithoutDe/"+reinstateddetails.getId()+"/1";	

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}
		return "redirect:/departmentalEnquiry/reinstatedDetailsWithoutDE/1";

	}

	/* Landing Prosecution Proposal Details form WithoutDE Method (07/11/2022) */
	@GetMapping("/prosecutionProposaldetailsWithoutDE/{page}")
	public String prosecutionProposaldetailsWithoutDE(Model model, RedirectAttributes redirect,
			@PathVariable("page") int page) {
		LOG.info("Landing Prosecution Proposal Details form WithoutDE Method (07/11/2022).............");
		
		request.getSession().setAttribute("caseid", null);
		ProsecutionProposalDetails prosecutionproposaldetails = new ProsecutionProposalDetails();
		model.addAttribute("ProsecutionProposalObj", prosecutionproposaldetails);
		
		// pagination Writting coding start here.....

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);
				}

				if (page < 0) {

					page = 1;
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				User currentUser = userService.getCurrentUserPojo();
				Page<ProsecutionProposalDetails> noticePages = null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = iprosecutionproposaldetailsRepository.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
				} else {
					noticePages = iprosecutionproposaldetailsRepository.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
				}
				
				/*
				 * noticePages =
				 * suspensionDetailsRepo.findByFileNoIsNotNullAndFileNoIsNot("",pageable); }
				 * else { noticePages = suspensionDetailsRepo.c }
				 */

				model.addAttribute("ProsecutionDetailsGridList", noticePages);
				model.addAttribute("serviceGroup", serviceGrpRepo.findByOrderById());
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

		
		
				return "DeptInquiryViews/prosecutionProposalDetailsWithoutDE";
	}

	
	
	/*
	 * Method : addProsecutionProposalDetailsWithoutDe saving method..
	 * Description : written saving form logic code
	 */

	@PostMapping("/addProsecutionProposalDetailsWithoutDe")
	public String addProsecutionProposalDetailsWithoutDe(
			@ModelAttribute("ProsecutionProposalObj") ProsecutionProposalDetailsDto prosecutionproposaldetailsdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "filedata", required = false) MultipartFile file,
			@RequestParam(name = "finalcourtorder", required = false) MultipartFile finalcourtorderfile) {

		// creating new object of ProsecutionProposalDetails model
		ProsecutionProposalDetails prosecutionproposaldetails = new ProsecutionProposalDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (prosecutionproposaldetailsdto.getId() != null && prosecutionproposaldetailsdto.getId() > 0) {

				// checking id of prosecution proposal details
				prosecutionproposaldetails = iprosecutionproposaldetailsRepository
						.findById(prosecutionproposaldetailsdto.getId()).orElse(new ProsecutionProposalDetails());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(prosecutionproposaldetailsdto.getCaseNo(),currentUser)) {
					prosecutionproposaldetails.setCasedetails(caseDetailsRepo
							.findByCaseNoAndCurrentUser(prosecutionproposaldetailsdto.getCaseNo(),currentUser));
				}
				
				prosecutionproposaldetails.setSevarthId(prosecutionproposaldetailsdto.getSevarthId());
				prosecutionproposaldetails.setFirstName(prosecutionproposaldetailsdto.getFirstName());
				prosecutionproposaldetails.setLastName(prosecutionproposaldetailsdto.getLastName());
				prosecutionproposaldetails.setMiddleName(prosecutionproposaldetailsdto.getMiddleName());
				prosecutionproposaldetails.setProcessFlow(prosecutionproposaldetailsdto.getProcessFlow());
				prosecutionproposaldetails.setCourtName(prosecutionproposaldetailsdto.getCourtName());
				prosecutionproposaldetails.setCourtCaseNo(prosecutionproposaldetailsdto.getCourtCaseNo());
				prosecutionproposaldetails.setDecisionCourt(prosecutionproposaldetailsdto.getDecisionCourt());
				if(prosecutionproposaldetailsdto.getServicegroup() != null && prosecutionproposaldetailsdto.getDesignation() != null) {
					prosecutionproposaldetails.setDesignation(designationRepo.findById(prosecutionproposaldetailsdto.getDesignation().getDesignation_id()).orElse(new Designation()));
					prosecutionproposaldetails.setServicegroup(serviceGrpRepo.findById(prosecutionproposaldetailsdto.getServicegroup().getId()).orElse(new Service_Group()));
				}
				prosecutionproposaldetails.setCourtChargeSheetDate(prosecutionproposaldetailsdto.getCourtChargeSheetDate());
				prosecutionproposaldetails.setFileNo(prosecutionproposaldetailsdto.getFileNo());
//				prosecutionproposaldetails.setEmployeeId(prosecutionproposaldetailsdto.getEmployeeId());
				/* added on 18/10/2022 as per DE requirements */
				prosecutionproposaldetails.setRemark(prosecutionproposaldetailsdto.getRemark());
				prosecutionproposaldetails
						.setProsecutionProposalReceived(prosecutionproposaldetailsdto.isProsecutionProposalReceived());
				prosecutionproposaldetails.setDateOfReceipt(prosecutionproposaldetailsdto.getDateOfReceipt());
				prosecutionproposaldetails.setStatusForGrantedDeniedPending(
						prosecutionproposaldetailsdto.getStatusForGrantedDeniedPending());
				prosecutionproposaldetails.setDateOfOrderReceipt(prosecutionproposaldetailsdto.getDateOfOrderReceipt());
				prosecutionproposaldetails.setDateofChargesheetIssued(prosecutionproposaldetailsdto.getDateofChargesheetIssued());
				prosecutionproposaldetails
						.setDateOfCommunication(prosecutionproposaldetailsdto.getDateOfCommunication());
				prosecutionproposaldetails
						.setWhetherCaseFiledCourt(prosecutionproposaldetailsdto.isWhetherCaseFiledCourt());
				prosecutionproposaldetails
						.setDateOfOrderCaseFiled(prosecutionproposaldetailsdto.getDateOfOrderCaseFiled());
				// added fields according to new pdf
				prosecutionproposaldetails
						.setWhetherCaseFiledIntheCourt(prosecutionproposaldetailsdto.isWhetherCaseFiledIntheCourt());
				prosecutionproposaldetails.setWithde(prosecutionproposaldetailsdto.isWithde());
				if (prosecutionproposaldetailsdto.getStatus() != null) {
					prosecutionproposaldetails.setStatus(prosecutionproposaldetailsdto.getStatus());
				}

				else {
					prosecutionproposaldetails.setStatus(null);

				}

				prosecutionproposaldetails
						.setDateOfOrderoftheCourt(prosecutionproposaldetailsdto.getDateOfOrderoftheCourt());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (prosecutionproposaldetails.getCasedetails() != null) {
						id = prosecutionproposaldetails.getCasedetails().getId().toString();
					} else {
						id = prosecutionproposaldetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "prosecutiondetailswithoutde/" + prosecutionproposaldetails.getId() + "/";

					prosecutionproposaldetails.setFileNameUploadCopy(file.getOriginalFilename());
					prosecutionproposaldetails.setContentType(file.getContentType());
					prosecutionproposaldetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, prosecutionproposaldetails.getFileNameUploadCopy());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				
				if (finalcourtorderfile != null && !finalcourtorderfile.isEmpty()
						&& finalcourtorderfile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (prosecutionproposaldetails.getCasedetails() != null) {
						id = prosecutionproposaldetails.getCasedetails().getId().toString();
					} else {
						id = prosecutionproposaldetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "prosecutiondetailswithoutde/" + prosecutionproposaldetails.getId() + "/";
					prosecutionproposaldetails.setFinalcourtordercopy(finalcourtorderfile.getOriginalFilename());
					prosecutionproposaldetails.setContentTypefinalcourt(finalcourtorderfile.getContentType());
					prosecutionproposaldetails.setFilepathfinalcourt(filePath);
					fileService.fileUpload(filePath, finalcourtorderfile, prosecutionproposaldetails.getFinalcourtordercopy());
				}
				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				prosecutionproposaldetails = iprosecutionproposaldetailsRepository.save(prosecutionproposaldetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editProsecutionProposalDtlsWithoutDe/"+prosecutionproposaldetails.getId()+"/1";

			}

			else {

				
				if (StringUtils.isBlank(prosecutionproposaldetailsdto.getFileNo())) {
				CaseDetails cd = caseDetailsRepo.findByCaseNoContainingIgnoreCase(prosecutionproposaldetailsdto.getCaseNo()).get(0);
			
				
				if (cd != null) {
					prosecutionproposaldetails.setCasedetails(cd);
					if (StringUtils.isNotBlank(prosecutionproposaldetailsdto.getSevarthId()) && prosecutionRepo
							.existsByCasedetailsAndSevarthId(cd, prosecutionproposaldetailsdto.getSevarthId())) {
						prosecutionproposaldetails = prosecutionRepo.findByCasedetailsAndSevarthId(cd,
								prosecutionproposaldetailsdto.getSevarthId());
					}
				} 
				}

				

				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					prosecutionproposaldetails.setCurrentUser(currentUser);
					prosecutionproposaldetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				
				prosecutionproposaldetails.setSevarthId(prosecutionproposaldetailsdto.getSevarthId());
				prosecutionproposaldetails.setFirstName(prosecutionproposaldetailsdto.getFirstName());
				prosecutionproposaldetails.setLastName(prosecutionproposaldetailsdto.getLastName());
				prosecutionproposaldetails.setMiddleName(prosecutionproposaldetailsdto.getMiddleName());
				prosecutionproposaldetails.setProcessFlow(prosecutionproposaldetailsdto.getProcessFlow());
				prosecutionproposaldetails.setCourtName(prosecutionproposaldetailsdto.getCourtName());
				prosecutionproposaldetails.setCourtCaseNo(prosecutionproposaldetailsdto.getCourtCaseNo());
				prosecutionproposaldetails.setDecisionCourt(prosecutionproposaldetailsdto.getDecisionCourt());
				prosecutionproposaldetails.setDateofChargesheetIssued(prosecutionproposaldetailsdto.getDateofChargesheetIssued());
				if(prosecutionproposaldetailsdto.getServicegroup() != null && prosecutionproposaldetailsdto.getDesignation() != null) {
					prosecutionproposaldetails.setDesignation(designationRepo.findById(prosecutionproposaldetailsdto.getDesignation().getDesignation_id()).orElse(new Designation()));
					prosecutionproposaldetails.setServicegroup(serviceGrpRepo.findById(prosecutionproposaldetailsdto.getServicegroup().getId()).orElse(new Service_Group()));
				}
				prosecutionproposaldetails.setCourtChargeSheetDate(prosecutionproposaldetailsdto.getCourtChargeSheetDate());
				
				prosecutionproposaldetails.setFileNo(prosecutionproposaldetailsdto.getFileNo());
//				prosecutionproposaldetails.setEmployeeId(prosecutionproposaldetailsdto.getEmployeeId());
				/* added on 18/10/2022 as per DE requirements */
				prosecutionproposaldetails.setRemark(prosecutionproposaldetailsdto.getRemark());
				prosecutionproposaldetails
						.setProsecutionProposalReceived(prosecutionproposaldetailsdto.isProsecutionProposalReceived());
				prosecutionproposaldetails.setDateOfReceipt(prosecutionproposaldetailsdto.getDateOfReceipt());
				prosecutionproposaldetails.setStatusForGrantedDeniedPending(
						prosecutionproposaldetailsdto.getStatusForGrantedDeniedPending());
				prosecutionproposaldetails.setDateOfOrderReceipt(prosecutionproposaldetailsdto.getDateOfOrderReceipt());
				prosecutionproposaldetails
						.setDateOfCommunication(prosecutionproposaldetailsdto.getDateOfCommunication());
				prosecutionproposaldetails
						.setWhetherCaseFiledCourt(prosecutionproposaldetailsdto.isWhetherCaseFiledCourt());
				prosecutionproposaldetails
						.setDateOfOrderCaseFiled(prosecutionproposaldetailsdto.getDateOfOrderCaseFiled());

				// added fields according to new pdf
				prosecutionproposaldetails
						.setWhetherCaseFiledIntheCourt(prosecutionproposaldetailsdto.isWhetherCaseFiledIntheCourt());

				if (prosecutionproposaldetailsdto.getStatus() != null) {
					prosecutionproposaldetails.setStatus(prosecutionproposaldetailsdto.getStatus());
				}

				else {
					prosecutionproposaldetails.setStatus(null);

				}

				prosecutionproposaldetails
						.setDateOfOrderoftheCourt(prosecutionproposaldetailsdto.getDateOfOrderoftheCourt());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (prosecutionproposaldetails.getCasedetails() != null) {
						id = prosecutionproposaldetails.getCasedetails().getId().toString();
					} else {
						id = prosecutionproposaldetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "prosecutiondetailswithoutde/" + prosecutionproposaldetails.getId() + "/";
					prosecutionproposaldetails.setFileNameUploadCopy(file.getOriginalFilename());
					prosecutionproposaldetails.setContentType(file.getContentType());
					prosecutionproposaldetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, prosecutionproposaldetails.getFileNameUploadCopy());
				}
				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}
				
				
				if (finalcourtorderfile != null && !finalcourtorderfile.isEmpty()
						&& finalcourtorderfile.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {
					String id = null;
					if (prosecutionproposaldetails.getCasedetails() != null) {
						id = prosecutionproposaldetails.getCasedetails().getId().toString();
					} else {
						id = prosecutionproposaldetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "prosecutiondetailswithoutde/" + prosecutionproposaldetails.getId() + "/";
					prosecutionproposaldetails.setFinalcourtordercopy(finalcourtorderfile.getOriginalFilename());
					prosecutionproposaldetails.setContentTypefinalcourt(finalcourtorderfile.getContentType());
					prosecutionproposaldetails.setFilepathfinalcourt(filePath);
					fileService.fileUpload(filePath, finalcourtorderfile, prosecutionproposaldetails.getFinalcourtordercopy());
				}
				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				prosecutionproposaldetails = iprosecutionproposaldetailsRepository.save(prosecutionproposaldetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editProsecutionProposalDtlsWithoutDe/"+prosecutionproposaldetails.getId()+"/1";

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/prosecutionProposaldetailsWithoutDE/1";

	}
	
	
	
	
	
	
	
	/* 09/11/2022 :: edit suspension records without DE  PAGE*/
	
	@GetMapping("/editsusdtlsRecordswithoutDE/{id}/{page}")
	public String editsusdtlsRecordswithoutDE(@PathVariable("id") long id, Model model, @PathVariable("page") int page) {
		LOG.info("hi this is edit suspension records without DE  PAGE ");

		// calling case number on Suspension Details
		// EmployeeDetails empdtls = employeedetailsrepo.findById(id).orElse(new
		// EmployeeDetails());

		// calling case number on Suspension Details
		
		User currentUser = userService.getCurrentUserPojo();
		SuspensionDetails suspensiondetails = suspensionDetailsRepo.findById(id).orElse(new SuspensionDetails());
		if(suspensiondetails.getCasedetails() != null) {
			suspensiondetails.setCaseNo(suspensiondetails.getCasedetails().getCaseNo());
		}
		model.addAttribute("serviceGroup", serviceGrpRepo.findByOrderById());
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

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

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

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				Page<SuspensionDetails> noticePages = null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = suspensionDetailsRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
				} else {
					noticePages = suspensionDetailsRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",currentUser,pageable);
				}

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

			Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

			Page<SuspensionDetails> noticePages = suspensionDetailsRepo.findByEmployeeSuspendedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);

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

		return "DeptInquiryViews/suspensionDetailsWithoutDE";
	}
	
	
	
	
	
	
	/* 09/11/2022 :: edit suspension records without DE  PAGE*/

	@GetMapping("/editReinstateddtlsRecordsWithoutDe/{id}/{page}")
	public String editReinstateddtlsRecordsWithoutDe(@PathVariable("id") long id, Model model, @PathVariable("page") int page) {
		LOG.info("hi this is the  edit Reinstated dtls Records Without De ");

		
			User currentUser = userService.getCurrentUserPojo();
		// calling case number on Reinstated  Details
	     	ReInstatedDetails   reinstateddetails=reinstatedRepository.findById(id).orElse(new ReInstatedDetails());
	     //	reinstateddetails.setCasedetails(reinstatedRepository.findByCaseNo(reinstateddetails.getCaseNo()));
	     	if(reinstateddetails.getCasedetails() != null) {
	     		reinstateddetails.setCaseNo(reinstateddetails.getCasedetails().getCaseNo());
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

				Page<ReInstatedDetails> noticePages = null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					
					noticePages = reinstatedRepository.findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
				} else {
					noticePages = reinstatedRepository.findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
				}

				
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
			Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
			Page<ReInstatedDetails> noticePages = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
					.contains("ROLE_SUPERADMIN")) {
				
				noticePages = reinstatedRepository.findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
			} else {
				noticePages = reinstatedRepository.findByEmployeeReInstatedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
			}
			model.addAttribute("reinstatedGridList", noticePages);
		}

		return "DeptInquiryViews/reinstatedDetailsWithoutDE";
	}
	
	
	/* 09/11/2022 :: edit prosecution proposal details records without DE  PAGE*/
	@GetMapping("/editProsecutionProposalDtlsWithoutDe/{id}/{page}")
	public String editProsecutionProposalDtlsWithoutDe(@PathVariable("id") long id, Model model,@PathVariable("page") int page) {
		LOG.info("hi this is the edit Prosecution Proposal Dtls Without De ");

		User currentUser = userService.getCurrentUserPojo();
		model.addAttribute("serviceGroup", serviceGrpRepo.findByOrderById());
		ProsecutionProposalDetails  prosecutionproposaldetails=prosecutionRepo.findById(id).orElse(new ProsecutionProposalDetails());
		if(prosecutionproposaldetails.getCasedetails() != null) {
			prosecutionproposaldetails.setCaseNo(prosecutionproposaldetails.getCasedetails().getCaseNo());
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

			if (prosecutionproposaldetails.getFileNo() != null) {

				int index = 1;

				if (page != 0) {
					index = (page * 10) - (10 - 1);
					model.addAttribute("pageIndex", index);

				}

				if (page < 0) {

					page = 1;
				}

				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

				Page<ProsecutionProposalDetails> noticePages = null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = iprosecutionproposaldetailsRepository.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
				} else {
					noticePages = iprosecutionproposaldetailsRepository.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
				}

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
				Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				Page<ProsecutionProposalDetails> noticePages = null;
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = iprosecutionproposaldetailsRepository.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
				} else {
					noticePages = iprosecutionproposaldetailsRepository.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
				}
				model.addAttribute("ProsecutionDetailsGridList", noticePages);
			}
		} catch (Exception a) {
			a.printStackTrace();
			Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
			Page<ProsecutionProposalDetails> noticePages = null;
			if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
					.contains("ROLE_SUPERADMIN")) {
				noticePages = iprosecutionproposaldetailsRepository.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNot(true,"",pageable);
			} else {
				noticePages = iprosecutionproposaldetailsRepository.findByProsecutionProposalReceivedAndCasedetailsIsNullAndFileNoIsNotNullAndFileNoIsNotAndCurrentUser(true,"",pageable,currentUser);
			}
			model.addAttribute("ProsecutionDetailsGridList", noticePages);
		}

		return "DeptInquiryViews/prosecutionProposalDetailsWithoutDE";
	}
	
	
	
	/* search  Record button for without DE prosecution proposal details (10/11/2022)*/
	
	@GetMapping("/prosecutionProposalwithoutdeSearch/{page}")
	public String prosecutionProposalwithoutdeSearch(Model model, @PathVariable("page") int page,
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		LOG.info("hi this is the prosecution Proposal without de Search  page.......");

		String customsearch = null;
		if (search != null && search.contains("splitting")) {
			customsearch = search.split("splitting")[1];
		}

//		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
//				: Sort.by(sortField).descending();
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDirection);
//		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

		// pagination Writting coding
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {
			page = 1;
		}

        //	Pageable pageable = PageRequest.of(page - 1, 10, sort); // 10/11/2022
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));

		
		
		
		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		Page<ProsecutionProposalDetails> noticePages = null;

		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
				model.addAttribute("search", search);
				
				
//				try {
//					LocalDate date = LocalDate.parse(search);
//					noticePages = iprosecutionproposaldetailsRepository
//							.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrReInstatedOrderDateOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
//									search, search, search, search, search, date, BooleanUtils.toBooleanObject(search),
//									search, search, pageable);
//				} 
//				
				
				
				
//				catch (Exception a) {
//					if (customsearch != null) {
//						noticePages = iprosecutionproposaldetailsRepository
//								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCase(
//										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
//										search.split("splitting")[1], pageable);
//					} else {
//						noticePages = iprosecutionproposaldetailsRepository
//								.findByCasedetailsCaseNoContainingIgnoreCaseOrSevarthIdContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmployeeReInstatedOrCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
//										search, search, search, search, search, BooleanUtils.toBooleanObject(search),
//										search, search, pageable);
//					}
//				}
			} 
			
//			else {
//				noticePages = iprosecutionproposaldetailsRepository.findAll(pageable);
//			}

		}
		
		
		
		
//		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
//				.contains("ROLE_DEPARTMENTADMIN")) {
//			List<User> userList = userRepo.findBySubDepartmentIn(
//					subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
//			userList.add(currentUser);
//			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
//				model.addAttribute("search", search);
//				try {
//					LocalDate date = LocalDate.parse(search);
//					noticePages = iprosecutionproposaldetailsRepository
//							.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndReInstatedOrderDateOrCurrentUserInAndEmployeeReInstatedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
//									userList, search, userList, search, userList, search, userList, search, userList,
//									search, userList, date, userList, BooleanUtils.toBooleanObject(search), userList,
//									search, userList, search, pageable);
//				} catch (Exception a) {
//					if (customsearch != null) {
//						noticePages = reinstatedRepository
//								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUserIn(
//										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
//										search.split("splitting")[1], userList, pageable);
//					} else {
//						noticePages = iprosecutionproposaldetailsRepository
//								.findByCurrentUserInAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserInAndSevarthIdContainingIgnoreCaseOrCurrentUserInAndFirstNameContainingIgnoreCaseOrCurrentUserInAndMiddleNameContainingIgnoreCaseOrCurrentUserInAndLastNameContainingIgnoreCaseOrCurrentUserInAndEmployeeReInstatedOrCurrentUserInAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserInAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
//										userList, search, userList, search, userList, search, userList, search,
//										userList, search, userList, BooleanUtils.toBooleanObject(search), userList,
//										search, userList, search, pageable);
//					}
//				}
//
//			} else {
//				noticePages = iprosecutionproposaldetailsRepository.findByCurrentUserIn(userList, pageable);
//			}
//
//		} 
//		
//		
//		
		
		
		
		
//		else {
//			if (StringUtils.isNotBlank(search) && !search.equalsIgnoreCase("null")) {
//				model.addAttribute("search", search);
//				try {
//					LocalDate date = LocalDate.parse(search);
//					noticePages = iprosecutionproposaldetailsRepository
//							.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndReInstatedOrderDateOrCurrentUserAndEmployeeReInstatedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
//									currentUser, search, currentUser, search, currentUser, search, currentUser, search,
//									currentUser, search, currentUser, date, currentUser,
//									BooleanUtils.toBooleanObject(search), currentUser, search, currentUser, search,
//									pageable);
//				}
//				
//				catch (Exception a) {
//					if (customsearch != null) {
//						noticePages = iprosecutionproposaldetailsRepository
//								.findByEmployeeReInstatedAndCasedetailsGlobalorgGlobalOrgNameContainingIgnoreCaseAndCurrentUser(
//										BooleanUtils.toBooleanObject(search.split("splitting")[0]),
//										search.split("splitting")[1], currentUser, pageable);
//					} else {
//						noticePages = iprosecutionproposaldetailsRepository
//								.findByCurrentUserAndCasedetailsCaseNoContainingIgnoreCaseOrCurrentUserAndSevarthIdContainingIgnoreCaseOrCurrentUserAndFirstNameContainingIgnoreCaseOrCurrentUserAndMiddleNameContainingIgnoreCaseOrCurrentUserAndLastNameContainingIgnoreCaseOrCurrentUserAndEmployeeReInstatedOrCurrentUserAndCasedetailsRuleApplicableRuleNameContainingIgnoreCaseOrCurrentUserAndCasedetailsMisConductTypeMisconductNameContainingIgnoreCase(
//										currentUser, search, currentUser, search, currentUser, search, currentUser,
//										search, currentUser, search, currentUser, BooleanUtils.toBooleanObject(search),
//										currentUser, search, currentUser, search, pageable);
//					}
//				}
//
//			} else {
//				noticePages = iprosecutionproposaldetailsRepository.findByCurrentUser(currentUser, pageable);
//			}
//
//		}
		

		
		
		model.addAttribute("ProsecutionDetailsGridList", noticePages);
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
		return "DeptInquiryViews/prosecutionProposalDetailsWithoutDE";

	}
	
	
	
	
	
	/* Common Report page for all fields methods * Date:12/11/2022 */
	
	
	

	@GetMapping("/finalOutCometailswithoutde/{page}")
	public String finalOutCometailsWithoutDe(Model model, RedirectAttributes redirect, @PathVariable("page") int page,@RequestParam(name="fileNo",required = false) String fileNo)
	{
		LOG.info("Hi this is the finaLOutCome details  page method.............");

		request.getSession().setAttribute("caseid", null);
		FinalOutcomeDetails finaloutcomedetails = new FinalOutcomeDetails();
		
//		ProsecutionProposalDetails proseDe = prosecutionRepo.findByFileNo(fileNo);
//		if(proseDe != null) {
//			if(pro)
//		}
		if(fileNo != null) {
			finaloutcomedetails.setFileNo(fileNo);
			ProsecutionProposalDetails prosecution = prosecutionRepo.findByFileNo(fileNo);
			if(prosecution != null) {
				finaloutcomedetails.setFirstName(prosecution.getFirstName());
				finaloutcomedetails.setMiddleName(prosecution.getMiddleName());
				finaloutcomedetails.setLastName(prosecution.getLastName());
				finaloutcomedetails.setSevarthId(prosecution.getSevarthId());
				finaloutcomedetails.setWhetherCaseFinallyDecided(true);
				model.addAttribute("processflow", prosecution.getProcessFlow());
			}
		}
		
		model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails);

		model.addAttribute("DecisionTakenList", decisiontakenrepository.findAll());
		model.addAttribute("casependingwithlist", casependingwithRepository.findByActive(true));
		model.addAttribute("TypeofPenaltylist", typeofpenaltyrepository.findAll());

		// for grid
//		model.addAttribute("FinalOutComeList", finaloutcomeRepository.findAll());

		// pagination Writting coding start here.....

		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {

			page = 1;
		}

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		User currentUser = userService.getCurrentUserPojo();
		Page<FinalOutcomeDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			noticePages = finaloutcomeRepository.findAll(pageable);
		} else {
			noticePages = finaloutcomeRepository.findByCurrentUserAndFileNoIsNotNull(currentUser, pageable);
		}

		model.addAttribute("FinalOutComeList", noticePages);
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

		/* * Fetching Records of while DE Cases back log Grids code part Area */

		try {
			Long caseid = (Long) request.getSession().getAttribute("caseid");
			System.out.println(caseid);
			if (caseid != null) {

				/*------------------------------------------------------------------------------*/

				List<FinalOutcomeDetails> finaloutcomedetailss = finaloutcomeRepository.findByCasedetailsId(caseid);
				if (finaloutcomedetailss != null && !finaloutcomedetailss.isEmpty()) {
					FinalOutcomeDetails FinalOutcomeDetailsobj1 = finaloutcomedetailss.get(0);
					CaseDetails caseDetails = FinalOutcomeDetailsobj1.getCasedetails();

					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
						FinalOutcomeDetailsobj1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
						FinalOutcomeDetailsobj1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
						FinalOutcomeDetailsobj1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
						FinalOutcomeDetailsobj1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
						FinalOutcomeDetailsobj1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					FinalOutcomeDetailsobj1.setCaseNo(finaloutcomedetailss.get(0).getCasedetails().getCaseNo());
					model.addAttribute("finaloutcomedetailsObj", FinalOutcomeDetailsobj1);
				}

				else {
					FinalOutcomeDetails finaloutcomedetails1 = new FinalOutcomeDetails();
					CaseDetails casedtls = new CaseDetails();
					CaseDetails caseDetails = caseDetailsRepo.findById(caseid).orElse(casedtls);
					if (caseDetails.getEmployeeList() != null && !caseDetails.getEmployeeList().isEmpty()) {
						finaloutcomedetails1.setSevarthId(caseDetails.getEmployeeList().get(0).getSevarthId());
						finaloutcomedetails1.setEmployeeId(caseDetails.getEmployeeList().get(0).getEmployeeId());
						finaloutcomedetails1.setFirstName(caseDetails.getEmployeeList().get(0).getFirstName());
						finaloutcomedetails1.setMiddleName(caseDetails.getEmployeeList().get(0).getMiddleName());
						finaloutcomedetails1.setLastName(caseDetails.getEmployeeList().get(0).getLastName());
					}
					finaloutcomedetails1.setCaseNo(caseDetails.getCaseNo());

					model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails1);
				}

				/*
				 * List<FinalOutcomeDetails> finaloutcomedtls =
				 * finaloutcomeRepository.findByCasedetailsId(caseid); if (finaloutcomedtls !=
				 * null && !finaloutcomedtls.isEmpty()) { FinalOutcomeDetails finalout =
				 * finaloutcomedtls.get(0);
				 * finalout.setCaseNo(finalout.getCasedetails().getCaseNo());
				 * 
				 * Passing Obj to UI key and value Pair
				 * model.addAttribute("finaloutcomedetailsObj", finalout); } else {
				 * FinalOutcomeDetails finalout = new FinalOutcomeDetails();
				 * finalout.setCaseNo(caseDetailsRepo.findById(caseid).orElse(new
				 * CaseDetails()).getCaseNo()); model.addAttribute("finaloutcomedetailsObj",
				 * finalout); }
				 */

				pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
				if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
						.contains("ROLE_SUPERADMIN")) {
					noticePages = finaloutcomeRepository.findByCasedetailsId(caseid, pageable);
				} else {
					noticePages = finaloutcomeRepository.findByCasedetailsIdAndCurrentUser(caseid, currentUser,
							pageable);
				}

//				model.addAttribute("chargeSheetObj", noticePages);
				model.addAttribute("currentPage", page);
				begin = Math.max(1, page - 5);
				if (page >= 5) {
					begin = page;
				}

				end = Math.min(begin + 4, noticePages.getTotalPages());
				model.addAttribute("begin", begin);
				model.addAttribute("end", end);
				model.addAttribute("totalPages", noticePages.getTotalPages());
				model.addAttribute("totalItems", noticePages.getTotalElements());
				model.addAttribute("FinalOutComeList", noticePages.getContent());

			} else {
//				model.addAttribute("FinalOutComeList", finaloutcomeRepository.findAll());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("FinalOutComeList", new ArrayList<>());
		} // Ending try block of DE Cases backlog records

		return "DeptInquiryViews/FinalOutcomeWithoutDe";
	}

	
	
	@PostMapping("/addFinalOutcomeDetailsWithoutDe")
	public String addFinalOutcomeDetailsWithoutDe(
			@ModelAttribute("finaloutcomedetailsObj") FinalOutcomeDetailsDTO finaloutcomedetailsdto,
			BindingResult result, Model model, RedirectAttributes redirect,
			@RequestParam(name = "fileName", required = false) MultipartFile file) {

		// creating new object of Final Outcome DetailsDTO model
		FinalOutcomeDetails finaloutcomedetails = new FinalOutcomeDetails();
		User currentUser = userService.getCurrentUserPojo();
		try {
			if (finaloutcomedetailsdto.getId() != null && finaloutcomedetailsdto.getId() > 0) {

				// for update
				finaloutcomedetails = finaloutcomeRepository.findById(finaloutcomedetailsdto.getId())
						.orElse(new FinalOutcomeDetails());

				if (caseDetailsRepo.existsByCaseNoAndCurrentUser(finaloutcomedetailsdto.getCaseNo(),currentUser)) {
					finaloutcomedetails.setCasedetails(caseDetailsRepo
							.findByCaseNoAndCurrentUser(finaloutcomedetailsdto.getCaseNo(),currentUser));
				}

				/*
				 * finaloutcomedetails.setCasedetails(finaloutcomedetailsdto.getCasedetails());
				 */
				finaloutcomedetails.setWhetherfinalorderIssued(finaloutcomedetailsdto.isWhetherfinalorderIssued());
				finaloutcomedetails.setDateOfFinalOrder(finaloutcomedetailsdto.getDateOfFinalOrder());
				finaloutcomedetails.setInquiryReportReceived(finaloutcomedetailsdto.isInquiryReportReceived());
				finaloutcomedetails.setDateofReceipt(finaloutcomedetailsdto.getDateofReceipt());
//				finaloutcomedetails.setCasePendingWith(finaloutcomedetailsdto.getCasePendingWith());
				finaloutcomedetails.setWhetherCaseFinallyDecided(finaloutcomedetailsdto.isWhetherCaseFinallyDecided());
//				finaloutcomedetails.setDecisionTaken(finaloutcomedetailsdto.getDecisionTaken());
//				finaloutcomedetails.setContentType(finaloutcomedetailsdto.getTypeOfPenaltyInflicted());

				finaloutcomedetails.setSevarthId(finaloutcomedetailsdto.getSevarthId());
				finaloutcomedetails.setEmployeeId(finaloutcomedetailsdto.getEmployeeId());
				finaloutcomedetails.setFirstName(finaloutcomedetailsdto.getFirstName());
				finaloutcomedetails.setMiddleName(finaloutcomedetailsdto.getMiddleName());
				finaloutcomedetails.setLastName(finaloutcomedetailsdto.getLastName());
				finaloutcomedetails.setDecisionTakenWithoutDe(finaloutcomedetailsdto.getDecisionTakenWithoutDe());
				finaloutcomedetails.setOtherdecision(finaloutcomedetailsdto.getOtherdecision());
				if (finaloutcomedetailsdto.getCasePendingWith() != null && finaloutcomedetailsdto.getCasePendingWith().getId() != null) {
					finaloutcomedetails.setCasePendingWith(
							casependingwithRepository.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
									.orElse(new CasePendingWithPojo()));
				}

				else {
					finaloutcomedetails.setCasePendingWith(null);

				}
				if (finaloutcomedetailsdto.getDecisionTaken() != null && finaloutcomedetailsdto.getDecisionTaken().getId() != null) {
					finaloutcomedetails.setDecisionTaken(
							decisiontakenrepository.findById(finaloutcomedetailsdto.getDecisionTaken().getId())
									.orElse(new DecisionTakenPojo()));
				}

				else {
					finaloutcomedetails.setDecisionTaken(null);

				}
				if (finaloutcomedetailsdto.getTypeOfPenaltyInflicted() != null && finaloutcomedetailsdto.getTypeOfPenaltyInflicted().getId() != null) {
					finaloutcomedetails.setTypeOfPenaltyInflicted(
							typeofpenaltyrepository.findById(finaloutcomedetailsdto.getTypeOfPenaltyInflicted().getId())
									.orElse(new TypeOfPenaltyInflictedPojo()));
				}

				else {
					finaloutcomedetails.setTypeOfPenaltyInflicted(null);

				}


				finaloutcomedetails.setFileNameEn(finaloutcomedetailsdto.getFileName());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (finaloutcomedetails.getCasedetails() != null) {
						id = finaloutcomedetails.getCasedetails().getId().toString();
					} else {
						id = finaloutcomedetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "finaloutcomedetailswithoutde/" + finaloutcomedetails.getId() + "/";
					
					finaloutcomedetails.setFileName(file.getOriginalFilename());
					finaloutcomedetails.setContentType(file.getContentType());
					finaloutcomedetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, finaloutcomedetails.getFileName());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				finaloutcomedetails = finaloutcomeRepository.save(finaloutcomedetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.updatesucess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editfinalOutcomeDtlsRecordsWithoutDe/"+finaloutcomedetails.getId()+"/1";

			}

			else {

//				finaloutcomedetails.setCasedetails(caseDetailsRepo.findById(finaloutcomedetailsdto.getCasedetails().getId()).orElse(new CaseDetails()));
//
//				// for save
//				/* this code is checking on Case Details screen at case id is exist or not */
//				if (finaloutcomeRepository.existsByCasedetails(finaloutcomedetails.getCasedetails())) {
//
//					finaloutcomedetails = finaloutcomeRepository.findByCasedetails(finaloutcomedetails.getCasedetails());
//				}

				// save
				if (StringUtils.isBlank(finaloutcomedetailsdto.getFileNo())) {
					CaseDetails cd = caseDetailsRepo.findByCaseNoContainingIgnoreCase(finaloutcomedetailsdto.getCaseNo())
							.get(0);
	
					if (cd != null) {
						finaloutcomedetails.setCasedetails(cd);
					} else {
						redirect.addFlashAttribute(departmentconstants.MESSAGE,
								messageSource.getMessage("dept.inq.does not.exist", null, loc.resolveLocale(request)));
						return "redirect:/departmentalEnquiry/finalOutCometails/1";
					}
				
					if (StringUtils.isNotBlank(finaloutcomedetailsdto.getSevarthId())
							&& chargesheetRepo.existsByCaseDetailsAndSevarthId(cd, finaloutcomedetailsdto.getSevarthId())) {
						finaloutcomedetails = finaloutcomeRepository.findByCasedetailsAndSevarthId(cd,
								finaloutcomedetailsdto.getSevarthId());
					}
				}
				finaloutcomedetails.setFileNo(finaloutcomedetailsdto.getFileNo());
				// added on 29/09/2022 ,
//				User currentUser = userService.getCurrentUserPojo();
				if (currentUser != null) {
					finaloutcomedetails.setCurrentUser(currentUser);
					finaloutcomedetails.setGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
				}
				finaloutcomedetails.setWhetherfinalorderIssued(finaloutcomedetailsdto.isWhetherfinalorderIssued());
				finaloutcomedetails.setDateOfFinalOrder(finaloutcomedetailsdto.getDateOfFinalOrder());
				finaloutcomedetails.setInquiryReportReceived(finaloutcomedetailsdto.isInquiryReportReceived());
				finaloutcomedetails.setDateofReceipt(finaloutcomedetailsdto.getDateofReceipt());
				finaloutcomedetails.setSevarthId(finaloutcomedetailsdto.getSevarthId());
				finaloutcomedetails.setEmployeeId(finaloutcomedetailsdto.getEmployeeId());
				finaloutcomedetails.setFirstName(finaloutcomedetailsdto.getFirstName());
				finaloutcomedetails.setMiddleName(finaloutcomedetailsdto.getMiddleName());
				finaloutcomedetails.setLastName(finaloutcomedetailsdto.getLastName());
				finaloutcomedetails.setWhetherCaseFinallyDecided(finaloutcomedetailsdto.isWhetherCaseFinallyDecided());
				finaloutcomedetails.setDecisionTakenWithoutDe(finaloutcomedetailsdto.getDecisionTakenWithoutDe());
				finaloutcomedetails.setOtherdecision(finaloutcomedetailsdto.getOtherdecision());
//				finaloutcomedetails.setCasePendingWith(finaloutcomedetailsdto.getCasePendingWith());
				// finaloutcomedetails.setDecisionTaken(finaloutcomedetailsdto.getDecisionTaken());
				// finaloutcomedetails.setContentType(finaloutcomedetailsdto.getTypeOfPenaltyInflicted());
//				employeedetails.setGender(genderRepo.findById(employeedetailsdto.getGender().getId()).orElse(new Gender()));
//				courtcasedetails.setCourtName(icourtnamerepository.findById(courtcasedetailsdto.getCourtName().getId()).orElse(new CourtName()));
				if (finaloutcomedetailsdto.getCasePendingWith() != null && finaloutcomedetailsdto.getCasePendingWith().getId() != null) {
					finaloutcomedetails.setCasePendingWith(
							casependingwithRepository.findById(finaloutcomedetailsdto.getCasePendingWith().getId())
									.orElse(new CasePendingWithPojo()));
				}

				else {
					finaloutcomedetails.setCasePendingWith(null);

				}
				if (finaloutcomedetailsdto.getDecisionTaken() != null && finaloutcomedetailsdto.getDecisionTaken().getId() != null) {
					finaloutcomedetails.setDecisionTaken(
							decisiontakenrepository.findById(finaloutcomedetailsdto.getDecisionTaken().getId())
									.orElse(new DecisionTakenPojo()));
				}

				else {
					finaloutcomedetails.setDecisionTaken(null);

				}
				if (finaloutcomedetailsdto.getTypeOfPenaltyInflicted() != null && finaloutcomedetailsdto.getTypeOfPenaltyInflicted().getId() != null) {
					finaloutcomedetails.setTypeOfPenaltyInflicted(
							typeofpenaltyrepository.findById(finaloutcomedetailsdto.getTypeOfPenaltyInflicted().getId())
									.orElse(new TypeOfPenaltyInflictedPojo()));
				}

				else {
					finaloutcomedetails.setTypeOfPenaltyInflicted(null);

				}

				finaloutcomedetails.setFileNameEn(finaloutcomedetailsdto.getFileName());

				if (file != null && !file.isEmpty()
						&& file.getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

					String id = null;
					if (finaloutcomedetails.getCasedetails() != null) {
						id = finaloutcomedetails.getCasedetails().getId().toString();
					} else {
						id = finaloutcomedetails.getFileNo();
					}
					String filePath = departmentconstants.DIRECTORYPATH + "casedetails/" + id + "/"
							+ "finaloutcomedetailswithoutde/" + finaloutcomedetails.getId() + "/";
					
					finaloutcomedetails.setFileName(file.getOriginalFilename());
					finaloutcomedetails.setContentType(file.getContentType());
					finaloutcomedetails.setFilepath(filePath);
					fileService.fileUpload(filePath, file, finaloutcomedetails.getFileName());

				}

				else {
					redirect.addFlashAttribute("error", "Please select correct pdf file");
				}

				finaloutcomedetails = finaloutcomeRepository.save(finaloutcomedetails);

				redirect.addFlashAttribute(departmentconstants.MESSAGE,
						messageSource.getMessage("deptenq.redirect.savesuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/editfinalOutcomeDtlsRecordsWithoutDe/"+finaloutcomedetails.getId()+"/1";

			}
		}

		catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.error", null, loc.resolveLocale(request)));
		}

		return "redirect:/departmentalEnquiry/finalOutCometailswithoutde/1/"+finaloutcomedetailsdto.getFileNo();

	}
	
	
	
	
	@GetMapping("/assigninqofficer/{page}")
	public String assigninqofficer(Model model, @PathVariable("page") int page,
			@RequestParam(name = "caseno", required = false) String caseno,
			@RequestParam(name = "caseid", required = false) String caseid,
			@RequestParam(name = "viewreport", required = false) String viewreport,
			@RequestParam(name = "sort-field", required = false) String sortField,
			@RequestParam(name = "sort-dir", required = false) String sortDirection) {
		LOG.info("hi this is the case details records page.... ");

		if (sortDirection == null) {
			sortDirection = "ASC";
		}
		if (sortField == null) {
			sortField = "caseNo";
		}
		if(caseid != null) {
			
		} else {
			caseid= "";
		}
		model.addAttribute("caseid", caseid);
		if(caseno != null) {
			
		} else {
			caseno= "";
		}
		model.addAttribute("caseno", caseno);

		final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDirection);
		model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

//		if(caseno != null) {
//			model.addAttribute("caseDetailsAllRecords", caseDetailsRepo.);
//		} else {
//			model.addAttribute("caseDetailsAllRecords", caseDetailsRepo.findAll());
//		}

		// pagination Writting coding
		int index = 1;

		if (page != 0) {
			index = (page * 10) - (10 - 1);
			model.addAttribute("pageIndex", index);
		}

		if (page < 0) {
			page = 1;
		}

//		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.ASC, "id"));
		Pageable pageable = PageRequest.of(page - 1, 10, sort);

		// added on 29/09/2022 , depend on employee and department we are getting grid
		// list values....
		User currentUser = userService.getCurrentUserPojo();
		List<InquiryOfficerDetails> listio = inquiryofficerRepository.findByCasedetailsIsNotNull();
		
		List<Long> caseIdList = new ArrayList<>();
		caseIdList = listio.parallelStream().map(s->s.getCasedetails().getId()).collect(Collectors.toList());
		if(caseIdList.isEmpty()) {
			caseIdList.add(0L);
		}
		Page<CaseDetails> noticePages = null;
		if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_SUPERADMIN")) {
			if (caseno != null) {
				model.addAttribute("caseno", caseno);
				noticePages = caseDetailsRepo.findByCaseNoContainingIgnoreCaseAndIdNotIn(caseno,caseIdList, pageable);
			} else if(caseid != null && StringUtils.isNotBlank(caseid) && !caseid.equalsIgnoreCase("null")){
				noticePages = caseDetailsRepo.findById(Long.valueOf(caseid),pageable);
				model.addAttribute("caseid", caseid);
			} else {
				noticePages = caseDetailsRepo.findByIdNotIn(caseIdList,pageable);
				model.addAttribute("caseno", "");
			}
		} // if close
		else if (currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toList())
				.contains("ROLE_DEPARTMENTADMIN")) {
			List<SubDepartment> subDepartmentList = subDepartmentRepo
					.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId());
//			List<User> userList = userRepo.findBySubDepartmentIn(subDepartmentRepo.findByGlobalorg(currentUser.getPimsEmployee().getGlobalOrgId()));
//			userList.add(currentUser);
			noticePages = caseDetailsRepo
					.findByCurrentUserSubDepartmentInOrCurrentUserPimsEmployeeGlobalOrgIdAndCurrentUserSubDepartmentIsNullAndIdNotIn(
							subDepartmentList, currentUser.getPimsEmployee().getGlobalOrgId(),caseIdList, pageable);
			model.addAttribute("caseno", "");
		} else {
			if (caseno != null && StringUtils.isNotBlank(caseno)) {
				model.addAttribute("caseno", caseno);
				noticePages = caseDetailsRepo.findByCurrentUserAndCaseNoContainingIgnoreCaseAndIdNotIn(
						currentUser, caseno,caseIdList, pageable);
			} else if(caseid != null && StringUtils.isNotBlank(caseid) && !caseid.equalsIgnoreCase("null")){
				noticePages = caseDetailsRepo.findByCurrentUserAndId(
						currentUser,Long.valueOf(caseid), pageable);
				model.addAttribute("caseid", caseid);
			}else {
				noticePages = caseDetailsRepo.findByCurrentUserAndIdNotIn(
						currentUser,caseIdList, pageable);
				model.addAttribute("caseno", "");
			}
		} // else close

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
		
		model.addAttribute("clickforcategory",messageSource.getMessage("deptenq.totalemployee.clickforCategory.label", null, loc.resolveLocale(request)));
		model.addAttribute("divisional",messageSource.getMessage("deptenq.totalemployee.divisional.label", null, loc.resolveLocale(request)));
		model.addAttribute("contractual",messageSource.getMessage("deptenq.totalemployee.contractual.label", null, loc.resolveLocale(request)));
		model.addAttribute("cancellabel",messageSource.getMessage("deptenq.totalemployee.cancellabel.label", null, loc.resolveLocale(request)));
		model.addAttribute("cancelledioassign ",messageSource.getMessage("deptenq.totalemployee.cancelledforassigningio.label", null, loc.resolveLocale(request)));
		
		return "DeptInquiryViews/assignio.html";

	}
	
	
	
	
	

	
	
	

	
	
	
	

}
