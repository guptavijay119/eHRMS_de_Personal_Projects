package com.ehrms.deptenq.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.NoDECertificate;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.models.VigilanceCertificateCounter;
import com.ehrms.deptenq.modelsDto.VigilanceDto;
import com.ehrms.deptenq.repository.DesignationRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IGlobalOrgRepository;
import com.ehrms.deptenq.repository.IProsecutionProposalDetailsRepository;
import com.ehrms.deptenq.repository.NoDeCertificateRepository;
import com.ehrms.deptenq.repository.SubDepartmentRepository;
import com.ehrms.deptenq.repository.SuspensionDetailsRepository;
import com.ehrms.deptenq.repository.VigilanceCounterRepository;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class NoDECertificateController {
	
	@Autowired
	private IEmployeeDetailsRepository empRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	private SuspensionDetailsRepository susRepo;
	
	@Autowired
	private NoDeCertificateRepository nodeRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	

	@Autowired
	private VigilanceCounterRepository vigCountRepo;
	
	@Autowired
	private IGlobalOrgRepository orgRepo;
	
	@Autowired
	private DesignationRepository desigRepo;
	
	@Autowired
	private SubDepartmentRepository subRepo;
	
	@Autowired
	private IProsecutionProposalDetailsRepository proseRepo;

	
	@GetMapping(value = "generatenode")
	public String getCaseDetailIds(@RequestParam(name="caseid",required = false) String caseid,
			@RequestParam(name="sevarthid",required = false) String sevarthid,
			@RequestParam(name="vigstatus",required = false) String vigstatus,
			@RequestParam(name="desigName",required = false) String desigName,
			@RequestParam(name="desigId",required = false) String desigId,
			@RequestParam(name="orgid",required = false) String orgid,
			@RequestParam(name="subdid",required = false) String subdid,
			@RequestParam(name="employeeName",required = false) String employeeName, Model model, RedirectAttributes redirect) {
		User currentUser = userService.getCurrentUserPojo();
		DateTimeFormatter fortm = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		Locale locale = Locale.forLanguageTag("mr");
		Context context = new Context(locale);
		String content = null;
		
//		context.setVariable("employeeName", employeeName);
//		
//		context.setVariable("desigName", desigName);
		
		context.setVariable("currentUser", currentUser);
		if(vigstatus != null && vigstatus.equalsIgnoreCase("withoutdesuspension")) {
			List<SuspensionDetails> empList6 = susRepo.findDistinctFileNoBySevarthIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(sevarthid,"");
			List<VigilanceDto> obbList = new ArrayList<>();
			empList6.stream().forEach(s->{
				VigilanceDto dto = new VigilanceDto();
				dto.setEmployeeName(s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
				dto.setSevarthid(s.getSevarthId());

				if(s.getFileNo() != null) {
					dto.setCaseno(s.getFileNo());
					dto.setCaseid(String.valueOf(s.getId()));
				} else {
					dto.setCaseno("");
					dto.setCaseid("");
				}
				if(s.getDesignation() != null) {
					dto.setDesignationName(s.getDesignation().getDesignation_name_en());
				} else {
					dto.setDesignationName("");
				}
				dto.setFinaldecision("Chargesheet not issued");
				dto.setDepartment(s.getGlobalorg().getGlobalOrgName());
				dto.setIssus("Yes");
				
				dto.setSubdepartment("");
				dto.setVigilancestatus("withoutdesuspension");
				obbList.add(dto);
			});
			

			context.setVariable("obbList", obbList);
			NoDECertificate gr = new NoDECertificate();
			gr.setSevarthid(sevarthid);
			if(vigCountRepo.existsByOrg(currentUser.getPimsEmployee().getGlobalOrgId())) {
				VigilanceCertificateCounter vig = vigCountRepo.findByOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				int counter = vig.getCounter();
				gr.setCounter(counter+1);
				vig.setCounter(gr.getCounter());
				vigCountRepo.save(vig);
			} else {
				VigilanceCertificateCounter vig = new VigilanceCertificateCounter();
				vig.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				vig.setCounter(1);
				gr.setCounter(1);
				vigCountRepo.save(vig);
			}
			gr.setCertificateType(3L);
			
			gr = nodeRepo.save(gr);
			context.setVariable("grid", gr.getCounter());
			content = templateEngine.process("DeptInquiryViews/templates/nodecertificateformat3", context);
			gr.setGrData(content);
//			gr.
			gr = nodeRepo.save(gr);
			model.addAttribute("gr", gr);
		}
		
		if(vigstatus != null && vigstatus.equalsIgnoreCase("withoutdeprosecution")) {
			List<ProsecutionProposalDetails> proseList = proseRepo.findBySevarthIdContainingIgnoreCaseAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(sevarthid,"Sanction Granted",true);
			List<VigilanceDto> obbList = new ArrayList<>();
			proseList.stream().forEach(s->{
				VigilanceDto dto = new VigilanceDto();
				dto.setEmployeeName(s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
				dto.setSevarthid(s.getSevarthId());

				if(s.getFileNo() != null) {
					dto.setCaseno(s.getFileNo());
					dto.setCaseid(String.valueOf(s.getId()));
				} else {
					dto.setCaseno("");
					dto.setCaseid("");
				}
				dto.setDesignationName("");
				if(StringUtils.isNotBlank(s.getStatus())) {
					dto.setFinaldecision(s.getStatus());
				} else {
					dto.setFinaldecision("Chargesheet not issued");
				}
				dto.setDepartment(s.getGlobalorg().getGlobalOrgName());
				dto.setIssus("Yes");
				if(s.getCourtChargeSheetDate()!= null) {
					dto.setChargedate(s.getCourtChargeSheetDate().format(fortm));
				} else {
					dto.setChargedate("");
				}
				
				dto.setSubdepartment("");
				dto.setVigilancestatus("withoutdesuspension");
				obbList.add(dto);
			});

			context.setVariable("obbList", obbList);
			NoDECertificate gr = new NoDECertificate();
			gr.setSevarthid(sevarthid);
			
			if(vigCountRepo.existsByOrg(currentUser.getPimsEmployee().getGlobalOrgId())) {
				VigilanceCertificateCounter vig = vigCountRepo.findByOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				int counter = vig.getCounter();
				gr.setCounter(counter+1);
				vig.setCounter(gr.getCounter());
				vigCountRepo.save(vig);
			} else {
				VigilanceCertificateCounter vig = new VigilanceCertificateCounter();
				vig.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				vig.setCounter(1);
				gr.setCounter(1);
				vigCountRepo.save(vig);
			}
			gr.setCertificateType(4L);
			gr = nodeRepo.save(gr);
			context.setVariable("grid", gr.getCounter());
			content = templateEngine.process("DeptInquiryViews/templates/nodecertificateformat4", context);
			gr.setGrData(content);
//			gr.
			gr = nodeRepo.save(gr);
			model.addAttribute("gr", gr);
		}
		
		if(vigstatus != null && vigstatus.equalsIgnoreCase("pendingde")) {

			List<VigilanceDto> obbList = new ArrayList<>();
			List<Boolean> retired = new ArrayList<>();
			List<LocalDate> superannuationDate = new ArrayList<>();
			List<EmployeeDetails> empList5 = empRepo.findDistinctCasedetailsBySevarthIdAndFinalOutcomeDetailsIsNullOrSevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdOrSevarthIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(sevarthid,sevarthid,true,1L,sevarthid,false);
			empList5.stream().forEach(s->{
					VigilanceDto dto = new VigilanceDto();
					dto.setEmployeeName(s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
					dto.setSevarthid(s.getSevarthId());
					dto.setCaseno(s.getCasedetails().getCaseNo());
					dto.setCaseid(String.valueOf(s.getCasedetails().getId()));
					dto.setDesignationName(s.getDesignation().getDescription());
					
					if(s.getFinalOutcomeDetails() != null && s.getFinalOutcomeDetails().getDecisionTaken() != null) {
						dto.setFinaldecision(s.getFinalOutcomeDetails().getDecisionTaken().getDecisionNameHindiEn());
					} else {
						dto.setFinaldecision(s.getCasedetails().getPendingWith());
					}
					dto.setDepartment(s.getCasedetails().getCurrentUser().getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
					if(s.getCasedetails().getCurrentUser().getSubDepartment() != null) {
						
						dto.setSubdepartment(s.getCasedetails().getCurrentUser().getSubDepartment().getSubdepartmentNameEn());
					} else {
						dto.setSubdepartment("");
					}
					if(s.getChargesheetDetails() != null) {
						dto.setChargedate(s.getChargesheetDetails().getChargesheetDate().format(fortm));
					} else {
						dto.setChargedate("");
					}
					dto.setMisconduct(s.getCasedetails().getMisConductType().getMisconductName());
					dto.setRule(s.getCasedetails().getRuleApplicable().getRuleName());
					if(s.getSuspensionDetails() != null && s.getReInstatedDetails()==null) {
						dto.setIssus("Yes");
					} else {
						dto.setIssus("No");
					}
					if(s.getSuperannuationDate() != null &&  s.getSuperannuationDate().isBefore(LocalDate.now())) {
						retired.add(true);
						superannuationDate.add(s.getSuperannuationDate());
					}
					dto.setVigilancestatus("pendingde");
					
					obbList.add(dto);
				});
			
			NoDECertificate gr = new NoDECertificate();
			gr.setSevarthid(sevarthid);
			if(retired.contains(true) && !superannuationDate.isEmpty()) {
				gr.setSuperannuationDate(superannuationDate.get(0).format(fortm));
			}
			if(vigCountRepo.existsByOrg(currentUser.getPimsEmployee().getGlobalOrgId())) {
				VigilanceCertificateCounter vig = vigCountRepo.findByOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				int counter = vig.getCounter();
				gr.setCounter(counter+1);
				vig.setCounter(gr.getCounter());
				vigCountRepo.save(vig);
			} else {
				VigilanceCertificateCounter vig = new VigilanceCertificateCounter();
				vig.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				vig.setCounter(1);
				gr.setCounter(1);
				vigCountRepo.save(vig);
			}
			gr.setCertificateType(2L);
			gr = nodeRepo.save(gr);
			context.setVariable("grid", gr.getCounter());
			context.setVariable("ret", retired.contains(true));
			context.setVariable("retired", gr.getSuperannuationDate());
			context.setVariable("obbList", obbList);
			
			content = templateEngine.process("DeptInquiryViews/templates/nodecertificateformat2", context);
		
			gr.setGrData(content);
//			gr.
			gr = nodeRepo.save(gr);
			model.addAttribute("gr", gr);
		}
		
		if(vigstatus != null && vigstatus.equalsIgnoreCase("nodatafound")) {
			NoDECertificate gr = new NoDECertificate();
			if(desigId != null && StringUtils.isNotBlank(desigId)) {
				desigName = desigRepo.findById(Long.valueOf(desigId)).orElse(new Designation()).getDesignation_name_en();
			}
			if(sevarthid != null) {
				List<GlobalOrg> orgList = orgRepo.findByActive(true, Sort.by("id"));
				List<Object> obj = null;
				for(GlobalOrg org : orgList) {
					try {
						ResponseEntity<ArrayList> data = restTemplate
								.getForEntity("http://localhost:8075/commonservice/serviceAPI/getempinfosevarthbyorg/"
										+ org.getId() + "?q=" + StringUtils.upperCase(sevarthid), ArrayList.class);
						if(data.getBody() != null && !data.getBody().isEmpty()) {
							obj = data.getBody();
							gr.setOrg(org);
							break;
						}
					} catch(Exception a) {
						break;
					}
	
				}
				if(obj != null && !obj.isEmpty()) {
					if(obj.get(2) != null) {
						desigName =  obj.get(2).toString();
					} 
				}
			}
			gr.setSevarthid(sevarthid);
			gr.setEmployeeName(employeeName);
			gr.setDesignationName(desigName);
			if(StringUtils.isNotBlank(orgid) && !orgid.equalsIgnoreCase("undefined")) {
				gr.setOrg(orgRepo.findById(Long.valueOf(orgid)).orElse(new GlobalOrg()));
				if(StringUtils.isNotBlank(subdid)) {
					gr.setSubDepartment(subRepo.findById(Long.valueOf(subdid)).orElse(new SubDepartment()));
				}
			} else {
				gr.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
			}
			
			if(vigCountRepo.existsByOrg(currentUser.getPimsEmployee().getGlobalOrgId())) {
				VigilanceCertificateCounter vig = vigCountRepo.findByOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				int counter = vig.getCounter();
				gr.setCounter(counter+1);
				vig.setCounter(gr.getCounter());
				vigCountRepo.save(vig);
			} else {
				VigilanceCertificateCounter vig = new VigilanceCertificateCounter();
				vig.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				vig.setCounter(1);
				gr.setCounter(1);
				vigCountRepo.save(vig);
			}
			gr.setCertificateType(1L);
			gr = nodeRepo.save(gr);
			context.setVariable("grid", gr.getCounter());
			context.setVariable("employeeName", employeeName);
			
			context.setVariable("desigName", desigName);
			content = templateEngine.process("DeptInquiryViews/templates/nodecertificateformat", context);
			gr.setGrData(content);
			gr = nodeRepo.save(gr);
			model.addAttribute("gr", gr);
			
		}

		
		
	
	
		
		return "DeptInquiryViews/empgroupAvieworder";
	}
	
	
	@GetMapping(value = "generatenodebysurname")
	public String generatenodebysurname(@RequestParam(name="caseid",required = false) String caseid,
			@RequestParam(name="sevarthid",required = false) String sevarthid,
			@RequestParam(name="vigstatus",required = false) String vigstatus,
			@RequestParam(name="desigName",required = false) String desigName,
			@RequestParam(name="employeeName",required = false) String employeeName,
			@RequestParam(name="orgid",required = false) String orgid,
			@RequestParam(name="birthdate",required = false) String birthdate,
			Model model, RedirectAttributes redirect) {
		User currentUser = userService.getCurrentUserPojo();
		DateTimeFormatter fortm = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		Locale locale = Locale.forLanguageTag("mr");
		Context context = new Context(locale);
		String content = null;
		String[] employeeNameArray = employeeName.split("\\s+");
		String firstName = "";
		String middleName = "";
		String lastName = "";
		
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		if(employeeNameArray.length==1) {
			firstName = employeeNameArray[0];
		} 
		if(employeeNameArray.length==2) {
			firstName = employeeNameArray[0];
			lastName = employeeNameArray[1];			
		} 
		if(employeeNameArray.length==3) {
			firstName = employeeNameArray[0];
			middleName = employeeNameArray[1];
			lastName = employeeNameArray[2];
		}


		
		context.setVariable("currentUser", currentUser);
		if(vigstatus != null && vigstatus.equalsIgnoreCase("withoutdesuspension")) {
			List<SuspensionDetails> empList6 = susRepo.findDistinctFileNoByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNull(firstName,middleName,lastName,Long.valueOf(orgid),"");
			List<VigilanceDto> obbList = new ArrayList<>();
			empList6.stream().forEach(s->{
				VigilanceDto dto = new VigilanceDto();
				dto.setEmployeeName(s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
				dto.setSevarthid(s.getSevarthId());
				if(s.getFileNo() != null) {
					dto.setCaseno(s.getFileNo());
					dto.setCaseid(String.valueOf(s.getId()));
				} else {
					dto.setCaseno("");
					dto.setCaseid("");
				}
				if(s.getDesignation() != null) {
					dto.setDesignationName(s.getDesignation().getDesignation_name_en());
				} else {
					dto.setDesignationName("");
				}
				dto.setFinaldecision("Chargesheet not issued");
				dto.setDepartment(s.getGlobalorg().getGlobalOrgName());
				dto.setIssus("Yes");
				if(s.getDateofActualSuspension() != null) {
					dto.setSuspensiondate(s.getDateofActualSuspension().format(fortm));
				}
				if(s.getDateofDeemedSuspension() != null) {
					dto.setSuspensiondate(s.getDateofDeemedSuspension().format(fortm));
				}
				dto.setSubdepartment("");
				dto.setVigilancestatus("withoutdesuspension");
				obbList.add(dto);
			});
			

			context.setVariable("obbList", obbList);
			NoDECertificate gr = new NoDECertificate();
			gr.setSevarthid(sevarthid);
			gr.setEmployeeName(employeeName);
			gr.setDesignationName(desigName);
			if(orgid != null) {
				gr.setOrg(orgRepo.findById(Long.valueOf(orgid)).orElse(new GlobalOrg()));
			}
			if(vigCountRepo.existsByOrg(currentUser.getPimsEmployee().getGlobalOrgId())) {
				VigilanceCertificateCounter vig = vigCountRepo.findByOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				int counter = vig.getCounter();
				gr.setCounter(counter+1);
				vig.setCounter(gr.getCounter());
				vigCountRepo.save(vig);
			} else {
				VigilanceCertificateCounter vig = new VigilanceCertificateCounter();
				vig.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				vig.setCounter(1);
				gr.setCounter(1);
				vigCountRepo.save(vig);
			}
			gr.setCertificateType(3L);
			gr = nodeRepo.save(gr);
			context.setVariable("grid", gr.getCounter());
			content = templateEngine.process("DeptInquiryViews/templates/nodecertificateformat3", context);
			gr.setGrData(content);
//			gr.
			gr = nodeRepo.save(gr);
			model.addAttribute("gr", gr);
		}
		
		if(vigstatus != null && vigstatus.equalsIgnoreCase("withoutdeprosecution")) {
			List<ProsecutionProposalDetails> empList7 = proseRepo.findDistinctFileNoByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndGlobalorgIdAndEmpDataIsNullAndFileNoIsNotNullAndAndFileNoIsNotAndCasedetailsIsNullAndStatusForGrantedDeniedPendingAndWhetherCaseFiledIntheCourtAndStatusIsNotNullAndCourtNameIsNotNullAndCourtCaseNoIsNotNull(firstName,middleName,lastName,Long.valueOf(orgid),"","Sanction Granted",true);
			List<VigilanceDto> obbList = new ArrayList<>();
			empList7.stream().forEach(s->{
				VigilanceDto dto = new VigilanceDto();
				dto.setEmployeeName(s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
				dto.setSevarthid(s.getSevarthId());
				if(s.getFileNo() != null) {
					dto.setCaseno(s.getFileNo());
					dto.setCaseid(String.valueOf(s.getId()));
				} else {
					dto.setCaseno("");
					dto.setCaseid("");
				}
				
				dto.setDesignationName("");
				if(StringUtils.isNotBlank(s.getStatus())) {
					dto.setFinaldecision(s.getStatus());
				} else {
					dto.setFinaldecision("Chargesheet not issued");
				}
				dto.setDepartment(s.getGlobalorg().getGlobalOrgName());
				dto.setIssus("Yes");
				if(s.getCourtChargeSheetDate() != null) {
					dto.setChargedate(s.getCourtChargeSheetDate().format(fortm));
				} else {
//					if(s.getDateOfOrderReceipt() != null) {
//						dto.setChargedate(s.getDateOfOrderReceipt().format(fortm));
//					} else {
						dto.setChargedate("");
//					}
				}
				
				dto.setSubdepartment("");
				dto.setVigilancestatus("withoutdeprosecution");
				obbList.add(dto);
			});
			

			context.setVariable("obbList", obbList);
			NoDECertificate gr = new NoDECertificate();
			gr.setSevarthid(sevarthid);
			gr.setEmployeeName(employeeName);
			gr.setDesignationName(desigName);
			if(orgid != null) {
				gr.setOrg(orgRepo.findById(Long.valueOf(orgid)).orElse(new GlobalOrg()));
			}
			if(vigCountRepo.existsByOrg(currentUser.getPimsEmployee().getGlobalOrgId())) {
				VigilanceCertificateCounter vig = vigCountRepo.findByOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				int counter = vig.getCounter();
				gr.setCounter(counter+1);
				vig.setCounter(gr.getCounter());
				vigCountRepo.save(vig);
			} else {
				VigilanceCertificateCounter vig = new VigilanceCertificateCounter();
				vig.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				vig.setCounter(1);
				gr.setCounter(1);
				vigCountRepo.save(vig);
			}
			gr.setCertificateType(4L);
			gr = nodeRepo.save(gr);
			context.setVariable("grid", gr.getCounter());
			content = templateEngine.process("DeptInquiryViews/templates/nodecertificateformat4", context);
			gr.setGrData(content);
//			gr.
			gr = nodeRepo.save(gr);
			model.addAttribute("gr", gr);
		}
		
		if(vigstatus != null && vigstatus.equalsIgnoreCase("pendingde")) {

			List<VigilanceDto> obbList = new ArrayList<>();
			List<Boolean> retired = new ArrayList<>();
			List<LocalDate> superannuationDate = new ArrayList<>();
			List<EmployeeDetails> empList5 = empRepo
					.findDistinctCasedetailsByFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsIsNullOrFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecidedAndFinalOutcomeDetailsDecisionTakenIdOrFirstNameContainingIgnoreCaseAndMiddleNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndLocaldateAndCasedetailsGlobalorgIdAndFinalOutcomeDetailsWhetherCaseFinallyDecided(
							firstName, middleName, lastName, LocalDate.parse(birthdate,df), Long.valueOf(orgid), firstName,
							middleName, lastName, LocalDate.parse(birthdate,df), Long.valueOf(orgid), true, 1L, firstName,
							middleName, lastName, LocalDate.parse(birthdate,df), Long.valueOf(orgid), false);
			empList5.stream().forEach(s->{
					VigilanceDto dto = new VigilanceDto();
					dto.setEmployeeName(s.getFirstName()+" "+s.getMiddleName()+" "+s.getLastName());
					dto.setSevarthid(s.getSevarthId());
					dto.setCaseno(s.getCasedetails().getCaseNo());
					dto.setCaseid(String.valueOf(s.getCasedetails().getId()));
					
					if(s.getDesignation() != null) {
						dto.setDesignationName(s.getDesignation().getDescription());
					} else {
						dto.setDesignationName("");
					}
					
					if(s.getFinalOutcomeDetails() != null && s.getFinalOutcomeDetails().getDecisionTaken() != null) {
						dto.setFinaldecision(s.getFinalOutcomeDetails().getDecisionTaken().getDecisionNameHindiEn());
					} else {
						if(s.getCasedetails().getPendingWith() != null) {
							dto.setFinaldecision(s.getCasedetails().getPendingWith());
						} else {
							dto.setFinaldecision("");
						}
					}
					dto.setDepartment(s.getCasedetails().getCurrentUser().getPimsEmployee().getGlobalOrgId().getGlobalOrgName());
					if(s.getCasedetails().getCurrentUser().getSubDepartment() != null) {
						
						dto.setSubdepartment(s.getCasedetails().getCurrentUser().getSubDepartment().getSubdepartmentNameEn());
					} else {
						dto.setSubdepartment("");
					}
					if(s.getChargesheetDetails() != null) {
						dto.setChargedate(s.getChargesheetDetails().getChargesheetDate().format(fortm));
					} else {
						if(s.getCasedetails() != null && !s.getCasedetails().getChargeSheetList().isEmpty()) {
							dto.setChargedate(s.getCasedetails().getChargeSheetList().get(0).getChargesheetDate().format(fortm));
						} else {
							dto.setChargedate("");
						}
					}
					if(s.getSuperannuationDate() != null &&  s.getSuperannuationDate().isBefore(LocalDate.now())) {
						retired.add(true);
						superannuationDate.add(s.getSuperannuationDate());
					}
					dto.setMisconduct(s.getCasedetails().getMisConductType().getMisconductName());
					dto.setRule(s.getCasedetails().getRuleApplicable().getRuleName());
					if(s.getSuspensionDetails() != null && s.getReInstatedDetails()==null) {
						dto.setIssus("Yes");
					} else {
						dto.setIssus("No");
					}
					dto.setVigilancestatus("pendingde");
					
					obbList.add(dto);
				});
			
			NoDECertificate gr = new NoDECertificate();
			gr.setEmployeeName(employeeName);
			for(VigilanceDto dd : obbList) {
				if(StringUtils.isNotBlank(dd.getDesignationName())) {
					desigName = dd.getDesignationName();
					break;
				}
			}
			if(retired.contains(true) && !superannuationDate.isEmpty()) {
				gr.setSuperannuationDate(superannuationDate.get(0).format(fortm));
			}
			
			gr.setDesignationName(desigName);
			if(orgid != null) {
				gr.setOrg(orgRepo.findById(Long.valueOf(orgid)).orElse(new GlobalOrg()));
			}
			gr.setSevarthid(sevarthid);
			if(vigCountRepo.existsByOrg(currentUser.getPimsEmployee().getGlobalOrgId())) {
				VigilanceCertificateCounter vig = vigCountRepo.findByOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				int counter = vig.getCounter();
				gr.setCounter(counter+1);
				vig.setCounter(gr.getCounter());
				vigCountRepo.save(vig);
			} else {
				VigilanceCertificateCounter vig = new VigilanceCertificateCounter();
				vig.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				vig.setCounter(1);
				gr.setCounter(1);
				vigCountRepo.save(vig);
			}
			gr.setCertificateType(2L);
			gr = nodeRepo.save(gr);
			context.setVariable("grid", gr.getCounter());
			context.setVariable("ret", retired.contains(true));
			context.setVariable("retired", gr.getSuperannuationDate());
			context.setVariable("obbList", obbList);
			
			content = templateEngine.process("DeptInquiryViews/templates/nodecertificateformat2", context);
		
			gr.setGrData(content);
//			gr.
			gr = nodeRepo.save(gr);
			model.addAttribute("gr", gr);
		}
		
		if(vigstatus != null && vigstatus.equalsIgnoreCase("nodatafound")) {
			NoDECertificate gr = new NoDECertificate();
			gr.setSevarthid(sevarthid);
			if(vigCountRepo.existsByOrg(currentUser.getPimsEmployee().getGlobalOrgId())) {
				VigilanceCertificateCounter vig = vigCountRepo.findByOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				int counter = vig.getCounter();
				gr.setCounter(counter+1);
				vig.setCounter(gr.getCounter());
				vigCountRepo.save(vig);
			} else {
				VigilanceCertificateCounter vig = new VigilanceCertificateCounter();
				vig.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
				vig.setCounter(1);
				gr.setCounter(1);
				vigCountRepo.save(vig);
			}
			gr.setCertificateType(1L);
			gr = nodeRepo.save(gr);
			context.setVariable("grid", gr.getCounter());			
			context.setVariable("employeeName", employeeName);
			
			context.setVariable("desigName", desigName);
			content = templateEngine.process("DeptInquiryViews/templates/nodecertificateformat", context);
			gr.setEmployeeName(employeeName);
			gr.setDesignationName(desigName);
			if(orgid != null && !orgid.equalsIgnoreCase("undefined")) {
				gr.setOrg(orgRepo.findById(Long.valueOf(orgid)).orElse(new GlobalOrg()));
			} else {
				gr.setOrg(currentUser.getPimsEmployee().getGlobalOrgId());
			}
			gr.setGrData(content);
			gr = nodeRepo.save(gr);
			model.addAttribute("gr", gr);
			
		}

		
		
	
	
		
		return "DeptInquiryViews/empgroupAvieworder";
	}
	

}
