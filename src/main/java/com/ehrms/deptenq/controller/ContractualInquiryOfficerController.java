package com.ehrms.deptenq.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehrms.deptenq.constants.DepartmentConstants;
import com.ehrms.deptenq.constants.ProposalState;
import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.CaseHearing;
import com.ehrms.deptenq.models.HearingDetails;
import com.ehrms.deptenq.models.HearingParticipants;
import com.ehrms.deptenq.models.IORequestTransaction;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.InquiryOfficerList;
import com.ehrms.deptenq.models.User;
import com.ehrms.deptenq.modelsDto.CaseHearingDto;
import com.ehrms.deptenq.modelsDto.HearingDetailsDto;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.CaseHearingRepository;
import com.ehrms.deptenq.repository.HearingDetailsRepository;
import com.ehrms.deptenq.repository.HearingParticipantsRepository;
import com.ehrms.deptenq.repository.IORequestTransactionRepository;
import com.ehrms.deptenq.repository.InquiryOfficerListRepository;
import com.ehrms.deptenq.repository.InquiryOfficerRepository;
import com.ehrms.deptenq.repository.UserRepository;
import com.ehrms.deptenq.service.FileService;
import com.ehrms.deptenq.service.MailService;
import com.ehrms.deptenq.service.UserService;

@Controller
@RequestMapping("departmentalEnquiry")
public class ContractualInquiryOfficerController {
	
	@Autowired
	private InquiryOfficerListRepository inqOfficerListRepo;



	@Autowired
	private UserService userService;

	@Autowired
	private CaseDetailsRepository caseDetailsRepo;
	
	@Autowired
	private LocaleResolver loc;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private MessageSource msgSource;
	
	@Autowired
	private IORequestTransactionRepository ioReqRepo;
	
	@Autowired
	private InquiryOfficerRepository ioDetailRepo;
	
	@Autowired
	private CaseHearingRepository caseHearingRepo;
	
	@Autowired
	private HearingDetailsRepository hearingDRepo;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private HearingParticipantsRepository hearingPartRepo;
	
	@Autowired
	private MailService mailService;
	

	
	private static final Logger LOG = LoggerFactory.getLogger(ContractualInquiryOfficerController.class);
	
	
	@GetMapping("/iolistdetails")
	public String getInqOffList(@RequestParam(name="caseid",required = false) Long caseid, Model model,RedirectAttributes redirect) {
		if(caseid != null) {
			CaseDetails cased = caseDetailsRepo.findById(caseid).orElse(new CaseDetails());
			model.addAttribute("cased", cased);
		}
		List<InquiryOfficerList> list = inqOfficerListRepo.findByInquiryOfficerlistAndActive("Contractual IO",true,Sort.by("order"));
		model.addAttribute("iolist", list);
		model.addAttribute("contractual", true);
		model.addAttribute("title", msgSource.getMessage("de.sidebar.commonsidebar.contractinqofflist", null, loc.resolveLocale(request)));
		
		model.addAttribute("doyyouwanttoassigntitle", msgSource.getMessage("de.sidebar.commonsidebar.doyyouwanttoassigntitle.label", null, loc.resolveLocale(request)));
		model.addAttribute("yeslabel", msgSource.getMessage("de.sidebar.commonsidebar.yeslabel.label", null, loc.resolveLocale(request)));
		model.addAttribute("nolabel", msgSource.getMessage("de.sidebar.commonsidebar.nolabel.label", null, loc.resolveLocale(request)));
		return "DeptInquiryViews/contractinqlist.html";
	}
	
	
	@GetMapping("/iolistdivisionaldetails")
	public String iolistdivisionaldetails(Model model,RedirectAttributes redirect) {
		List<InquiryOfficerList> list = inqOfficerListRepo.findByInquiryOfficerlistNotInAndActive(Arrays.asList("Contractual IO","Other","Disciplinary Authority"),true,Sort.by("order"));
		model.addAttribute("iolist", list);
		model.addAttribute("contractual", false);
		model.addAttribute("title", msgSource.getMessage("de.sidebar.commonsidebar.listofdivinqlist", null, loc.resolveLocale(request)));
		return "DeptInquiryViews/contractinqlist.html";
	}
	
	
	

	@GetMapping("/assigncontractualio")
	public String assigncontractualio(@RequestParam(name="caseid",required = false) Long caseid,@RequestParam(name="ioid",required = false) Long ioid,
			RedirectAttributes redirect) {
		if(caseid != null && ioid != null) {
			if(!ioReqRepo.existsByRequestStatusAndCasedetailsId("PENDING",caseid)) {
				IORequestTransaction io = new IORequestTransaction();
				io.setCasedetails(caseDetailsRepo.findById(caseid).orElse(new CaseDetails()));
				io.setIo(inqOfficerListRepo.findById(ioid).orElse(new InquiryOfficerList()));
				io.setReceivedDate(LocalDate.now());
				io.setRequestStatus(ProposalState.PENDING.toString());
				ioReqRepo.save(io);
				redirect.addFlashAttribute("message", msgSource.getMessage("inboxgad.redirect.ioassignsuccess", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/assigninqofficer/1?sort-field=caseNo&sort-dir=asc";
			} else {
				redirect.addFlashAttribute("message", msgSource.getMessage("inboxgad.redirect.ioassignsuccessalreayd", null, loc.resolveLocale(request)));
				return "redirect:/departmentalEnquiry/assigninqofficer/1?sort-field=caseNo&sort-dir=asc";
			}
		} else {
			if(caseid != null) {
				CaseDetails cased = caseDetailsRepo.findById(caseid).orElse(new CaseDetails());
				if(cased.getDivision() != null) {
					InquiryOfficerList iod = inqOfficerListRepo.findByDivision(cased.getDivision());
					if(!ioReqRepo.existsByRequestStatusAndCasedetailsId("PENDING",caseid)) {
						IORequestTransaction io = new IORequestTransaction();
						io.setCasedetails(caseDetailsRepo.findById(caseid).orElse(new CaseDetails()));
						io.setIo(iod);
						io.setReceivedDate(LocalDate.now());
						io.setRequestStatus(ProposalState.PENDING.toString());
						ioReqRepo.save(io);
						redirect.addFlashAttribute("message", msgSource.getMessage("inboxgad.redirect.ioassignsuccess", null, loc.resolveLocale(request)));
						return "redirect:/departmentalEnquiry/assigninqofficer/1?sort-field=caseNo&sort-dir=asc";
					}
				}
			}
		}
		return "redirect:/departmentalEnquiry/assigninqofficer/1?sort-field=caseNo&sort-dir=asc";
	}
	
	
	
	@GetMapping("/iorequestlist")
	public String iorequestlist(Model model,RedirectAttributes redirect) {
		User currentUser = userService.getCurrentUserPojo();
		List<IORequestTransaction> list = ioReqRepo.findByIoAndRequestStatus(currentUser.getIo(),"PENDING");
		model.addAttribute("rejected", ioReqRepo.countByIoAndRejectedDateIsNotNull(currentUser.getIo()));
		model.addAttribute("accepted", ioReqRepo.countByIoAndApprovalDateIsNotNull(currentUser.getIo()));
		model.addAttribute("total", ioReqRepo.countByIo(currentUser.getIo()));
		model.addAttribute("iolist", list);
		List<CaseDetails> caseall = list.stream().map(s->s.getCasedetails()).collect(Collectors.toList());
		model.addAttribute("caseall", caseall);
		
		return "DeptInquiryViews/ioreqlist.html";
	}
	
	
	

	@GetMapping("/processiorequest/{id}")
	public String processiorequest(Model model,RedirectAttributes redirect,@RequestParam(name="action",required = false)String action,@PathVariable Long id) {
		IORequestTransaction io = ioReqRepo.findById(id).orElse(new IORequestTransaction());
		if(StringUtils.equalsIgnoreCase(action, "ACCEPT")) {			
			io.setApprovalDate(LocalDate.now());
			io.setRequestStatus("ACCEPTED");
			CaseDetails cd = io.getCasedetails();
			if(cd.getIoList() != null && !cd.getIoList().isEmpty()) {
				InquiryOfficerDetails dd = cd.getIoList().get(0);
				dd.setListOfInquiryOfficer(io.getIo());
				dd.setInquiryOfficerAppointed(true);
				ioDetailRepo.save(dd);
			} else {
				InquiryOfficerDetails dd = new InquiryOfficerDetails();
				dd.setListOfInquiryOfficer(io.getIo());
				dd.setCasedetails(cd);
				dd.setAppointmentDate(io.getReceivedDate());
				dd.setFirstName(io.getIo().getFirstName());
				dd.setMiddleName(io.getIo().getMiddelName());
				dd.setLastName(io.getIo().getLastName());
				dd.setInquiryOfficerAppointed(true);
				dd.setCurrentUser(cd.getCurrentUser());
				ioDetailRepo.save(dd);
			}
			redirect.addFlashAttribute("message", "Request Updated Successfully");
		}
		if(StringUtils.equalsIgnoreCase(action, "REJECT")) {
			io.setRejectedDate(LocalDate.now());
			io.setRequestStatus("REJECTED");
			redirect.addFlashAttribute("message", "Request Rejected");
		}
		ioReqRepo.save(io);
	
		return "redirect:/departmentalEnquiry/iorequestlist";
	}
	
	
	
	@GetMapping("/getiocases")
	public String getiocases(Model model,RedirectAttributes redirect) {
		User currentUser = userService.getCurrentUserPojo();
		List<IORequestTransaction> list = ioReqRepo.findByIoAndRequestStatus(currentUser.getIo(),"ACCEPTED");
		model.addAttribute("rejected", ioReqRepo.countByIoAndRejectedDateIsNotNull(currentUser.getIo()));
		model.addAttribute("accepted", ioReqRepo.countByIoAndApprovalDateIsNotNull(currentUser.getIo()));
		model.addAttribute("total", ioReqRepo.countByIo(currentUser.getIo()));
		model.addAttribute("iolist", list);
		List<CaseDetails> caseall = list.stream().map(s->s.getCasedetails()).collect(Collectors.toList());
		model.addAttribute("caseall", caseall);
		
		return "DeptInquiryViews/ioacceptedcase.html";
	}
	
	@GetMapping("/assignhearing/{id}")
	public String assignhearing(Model model,RedirectAttributes redirect,@PathVariable Long id) {
		IORequestTransaction io = ioReqRepo.findById(id).orElse(new IORequestTransaction());
		CaseHearing dd = null;
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		if(caseHearingRepo.existsByCasedetailsAndIo(io.getCasedetails(),io)) {
			dd = caseHearingRepo.findByCasedetailsAndIo(io.getCasedetails(),io);
			if(dd.getHearingDetails() != null && !dd.getHearingDetails().isEmpty()) {
				model.addAttribute("lasthearingdate", dd.getHearingDetails().get(0).getHearingDate().format(df));
			}
			dd.getHearingDetails().sort((a1,a2)->a2.getHearingDate().compareTo(a1.getHearingDate()));
			model.addAttribute("hearingdetaillist",dd.getHearingDetails());
			
		} else {
			dd = new CaseHearing();
			dd.setCasedetails(io.getCasedetails());
			dd.setIo(io);		
			dd = caseHearingRepo.save(dd);	
			model.addAttribute("message", "Hearing Info Generated");
		}
		model.addAttribute("dd", dd);
		if(dd.getHearingDetails() != null && !dd.getHearingDetails().isEmpty()) {
			model.addAttribute("partlist", dd.getHearingDetails().get(0).getHearingParticipants());
		}
		return "DeptInquiryViews/hearinginfo.html";
	}
	
	
	@PostMapping("/addhearingdetails")
	public String addhearingdetails(Model model,RedirectAttributes redirect,@ModelAttribute("dd") CaseHearingDto dto) {
		
		CaseHearing dd =  caseHearingRepo.findById(dto.getId()).orElse(new CaseHearing());
		HearingDetails hd = new HearingDetails();
		hd.setHearingDate(dto.getHearingDate());
		hd.setWebLink(dto.getWeblink());
		hd.setHearing(dd);
		hd.setHearingStatus("FIXED");
		hd = hearingDRepo.save(hd);
		final HearingDetails hdf = hd;
		dd.getCasedetails().getEmployeeList().forEach(s->{
			HearingParticipants hp = new HearingParticipants();
			hp.setHearing(hdf);
			hp.setEmp(s);
			hp.setParticipantType("EMP");
			hearingPartRepo.save(hp);			
		});
		dd.getCasedetails().getPoList().forEach(s->{
			HearingParticipants hp = new HearingParticipants();
			hp.setHearing(hdf);
			hp.setPo(s);
			hp.setParticipantType("PO");
			hearingPartRepo.save(hp);	
		});
		dd.getCasedetails().getIoList().forEach(s->{
			HearingParticipants hp = new HearingParticipants();
			hp.setHearing(hdf);
			hp.setIo(s.getListOfInquiryOfficer());
			hp.setParticipantType("IO");
			hearingPartRepo.save(hp);	
		});

		redirect.addFlashAttribute("message", "Hearing Details Saved Successfully");
		return "redirect:/departmentalEnquiry/assignhearing/"+dd.getIo().getId();
	}
	
	
	@PostMapping("/updatehearingdetails")
	public ResponseEntity<?> updatehearingdetails(HearingDetailsDto form,Model model,
			RedirectAttributes redirect) {
		HearingDetails hd = hearingDRepo.findById(Long.valueOf(form.getId())).orElse(new HearingDetails());
		hd.setHearingStatus(form.getHearingStatus());
		hd.setHearingSummery(form.getHearingSummery());
		hd.setHearingDate(LocalDate.parse(form.getHearingDate()));
		if(form.getHearingStatus() != null && form.getHearingStatus().equalsIgnoreCase("COMPLETED")) {
			IORequestTransaction cd = hd.getHearing().getIo();
			cd.setRequestStatus("PENDINGREPORT");
			ioReqRepo.save(cd);
		}
		
		if(form.getFile() != null && !form.getFile().isEmpty() && StringUtils.equalsIgnoreCase(form.getFile().getContentType(), MediaType.APPLICATION_PDF_VALUE)) {
			String filePath = DepartmentConstants.DIRECTORYPATH + "hearing/"
					+ hd.getHearing().getId() + "/" + "hearingdetails/"+hd.getId()+"/";
			hd.setFileName(form.getFile().getOriginalFilename());
			hd.setFilePath(filePath);
			hd.setContentType(form.getFile().getContentType());
			fileService.fileUpload(filePath, form.getFile(), hd.getFileName());
		}
		hearingDRepo.save(hd);
		return ResponseEntity.ok("Updated Successfully");
	}
	
	
	@GetMapping("/gethearingdetailfile/{id}")
	public ResponseEntity<ByteArrayResource> gethearingdetailfile(@PathVariable String id) {
		HearingDetails charge = hearingDRepo.findById(Long.valueOf(id)).orElse(new HearingDetails());
		HttpHeaders headers = new HttpHeaders();
		headers.add(DepartmentConstants.CONTENTDEPOSITION,
				DepartmentConstants.INLINEFILENAME + "file.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(fileService.getFile(charge.getFilePath(), charge.getFileName()));
	}
	
	
	@PostMapping("/sendhearingnotice")
	public ResponseEntity<?> sendhearingnotice(HearingDetailsDto form,Model model,
			RedirectAttributes redirect) {
		HearingDetails hd = hearingDRepo.findById(Long.valueOf(form.getId())).orElse(new HearingDetails());
		hd.setHearingNotice(form.getHearingSummery());
		hd.setHearingSubject(form.getHearingSubject());
		hd.setNoticeSentDate(LocalDate.now());
		hd.setHearingNoticeSent(true);
//		String subject= "Hearing Notice";
//		if(form.getHearingSubject() != null) {
//			subject= form.getHearingSubject();
//		}
		hd = hearingDRepo.save(hd);
		hd.getHearingParticipants().forEach(s->{
			String email = "pubgkttt18181@gmail.com";
			try {
				if(s.getEmp().getEmail() != null) {
					email = s.getEmp().getEmail();
				}
			} catch(Exception a) {
				
			}
			try {
				mailService.sendEmail(null, email, null, form.getHearingSubject(), form.getHearingSummery(), null, null, null);
			} catch(Exception a) {
				
			}
		});
		
		
		
		
		return ResponseEntity.ok("Hearing Notice Sent Successfully");
	}
	
	
	
	@GetMapping("/getiocasespendingreport")
	public String getiocasespendingreport(Model model,RedirectAttributes redirect) {
		User currentUser = userService.getCurrentUserPojo();
		List<IORequestTransaction> list = ioReqRepo.findByIoAndRequestStatus(currentUser.getIo(),"PENDINGREPORT");
		model.addAttribute("rejected", ioReqRepo.countByIoAndRejectedDateIsNotNull(currentUser.getIo()));
		model.addAttribute("accepted", ioReqRepo.countByIoAndApprovalDateIsNotNull(currentUser.getIo()));
		model.addAttribute("total", ioReqRepo.countByIo(currentUser.getIo()));
		model.addAttribute("iolist", list);
		List<CaseDetails> caseall = list.stream().map(s->s.getCasedetails()).collect(Collectors.toList());
		model.addAttribute("caseall", caseall);
		
		return "DeptInquiryViews/ioacceptedcasependingreport.html";
	}
	
	
	@GetMapping("/getiocasesreportsubmitted")
	public String getiocasesreportsubmitted(Model model,RedirectAttributes redirect) {
		User currentUser = userService.getCurrentUserPojo();
		List<IORequestTransaction> list = ioReqRepo.findByIoAndRequestStatus(currentUser.getIo(),"REPORTSUBMITTED");
		model.addAttribute("rejected", ioReqRepo.countByIoAndRejectedDateIsNotNull(currentUser.getIo()));
		model.addAttribute("accepted", ioReqRepo.countByIoAndApprovalDateIsNotNull(currentUser.getIo()));
		model.addAttribute("total", ioReqRepo.countByIo(currentUser.getIo()));
		model.addAttribute("iolist", list);
		List<CaseDetails> caseall = list.stream().map(s->s.getCasedetails()).collect(Collectors.toList());
		model.addAttribute("caseall", caseall);
		
		return "DeptInquiryViews/ioacceptedcasereportsubmitted.html";
	}
	
	
	
	@PostMapping("/uploadreport")
	public ResponseEntity<?> uploadreport(HearingDetailsDto form,Model model,
			RedirectAttributes redirect) {
		IORequestTransaction hd = ioReqRepo.findById(Long.valueOf(form.getId())).orElse(new IORequestTransaction());
		InquiryOfficerDetails io = hd.getHearing().getCasedetails().getIoList().get(0);
		io.setInquiryReportDate(LocalDate.now());
		io.setInquiryReportReceived(true);
		io.setDateofInquiryReportReceived(LocalDate.now());
		if (form.getFile() != null && !form.getFile().isEmpty()
				&& form.getFile().getContentType().equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE)) {

			String filePath = DepartmentConstants.DIRECTORYPATH + "casedetails/"
					+ io.getCasedetails().getId() + "/" + "inquiryofficerDetails/inquiryReportfileUpload/"
					+ io.getId() + "/";
			io.setInquiryReportfileUpload(form.getFile().getOriginalFilename());
			io.setContentType(form.getFile().getContentType());
			io.setInquiryReportfileUploadFilePath(filePath);
			fileService.fileUpload(filePath, form.getFile(), io.getInquiryReportfileUpload());

		}
		io.setInquiryReportSubmitted(true);
		ioDetailRepo.save(io);
//		IORequestTransaction ioreq =  hd.getHearing().getIo();
		hd.setRequestStatus("REPORTSUBMITTED");
		ioReqRepo.save(hd);		
		return ResponseEntity.ok("Inquiry Report Uploaded Successfully");
	}
	
	
	
	@GetMapping("/getioupcominghearing")
	public String getioupcominghearing(Model model,RedirectAttributes redirect) {
		List<HearingDetails> list = hearingDRepo.findByHearingStatus("FIXED",Sort.by(Direction.DESC,"hearingDate"));
		model.addAttribute("hearinglist", list);
		List<CaseDetails> caseall = list.stream().map(s->s.getHearing().getCasedetails()).collect(Collectors.toList());
		model.addAttribute("caseall", caseall);
		return "DeptInquiryViews/ioupcominghearing.html";
	}




}
