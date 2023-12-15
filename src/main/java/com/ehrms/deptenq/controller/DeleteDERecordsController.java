package com.ehrms.deptenq.controller;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ehrms.deptenq.constants.DepartmentConstants;
import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.ChargesheetDetails;
import com.ehrms.deptenq.models.CourtCaseDetails;
import com.ehrms.deptenq.models.DetailsKeptAbeyanceCases;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.InquiryOfficerDetails;
import com.ehrms.deptenq.models.PresentingOfficerDetails;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.ReInstatedDetails;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.repository.CaseDetailsRepository;
import com.ehrms.deptenq.repository.DetailsKeptAbeyanceCasesRepository;
import com.ehrms.deptenq.repository.FinalOutcomeRepository;
import com.ehrms.deptenq.repository.IChargesheetDetailsRepository;
import com.ehrms.deptenq.repository.ICourtCaseDetailsRepository;
import com.ehrms.deptenq.repository.IEmployeeDetailsRepository;
import com.ehrms.deptenq.repository.IPresentingOfficerRepository;
import com.ehrms.deptenq.repository.IProsecutionProposalDetailsRepository;
import com.ehrms.deptenq.repository.IReInstatedRepository;
import com.ehrms.deptenq.repository.InquiryOfficerRepository;
import com.ehrms.deptenq.repository.SuspensionDetailsRepository;


@Controller
@RequestMapping("departmentalEnquiry")
public class DeleteDERecordsController {
	
	@PersistenceContext
	private EntityManager em;
	
	/** The transactiona template. */
	@Autowired
	private TransactionTemplate transactionaTemplate;
	
	@Autowired
	private IEmployeeDetailsRepository employeedetailsrepo;
	
	@Autowired
	private IChargesheetDetailsRepository chagesheetRepo;
	
	@Autowired
	private  DepartmentConstants departmentconstants;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LocaleResolver loc;
	
	@Autowired
	private SuspensionDetailsRepository suspensionDetailsRepo;
	
	@Autowired
	private IReInstatedRepository reinstatedRepository;
	
	@Autowired
	private IPresentingOfficerRepository presentingofficerRepository;
	
	@Autowired
	private IProsecutionProposalDetailsRepository   prosecutionRepo;
	
	@Autowired
	private ICourtCaseDetailsRepository   courtcaseRepo;

	@Autowired
	private DetailsKeptAbeyanceCasesRepository  detailskeptabeyancecasesRepository;
	
	@Autowired
	private FinalOutcomeRepository  finaloutcomeRepository;
	
	@Autowired
	private CaseDetailsRepository  casedetailsRepository;
	
	@Autowired
	private InquiryOfficerRepository inquiryofficerRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(DeleteDERecordsController.class);
	
	
	@GetMapping("/reports")
	public String ReportsMethods(Model model) {
		System.out.println("this is reports page.....");

		return "DeptInquiryViews/reports";
	}
	
	
	
	
	
	/* Delete Employee list row Records */

	@GetMapping("/deleteEmployeeDetailsRecords/{id}")
	public String deleteEmployeeDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of Employee details Records! ");

		EmployeeDetails emp = employeedetailsrepo.findById(id).orElse(new EmployeeDetails());

		   
		
	      model.addAttribute("employeeDetails", emp);
//     	  emp.setCasedetails(null);		
//	      emp.setGlobalorg(null);
//		  emp.setServicegroup(null);
//		  emp.setSubDepartment(null);
//		  emp.setGradepay(null);  
//		  emp.setPayband(null);   
//		  emp.setPaycommission(null); 
//		  emp.setGender(null);
//		  emp.setDesignation(null);  
//		  emp.setFromUser(null);
//		  emp.setCurrentUser(null);
//		  emp.setGlobalorg(null);
		
		  try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<EmployeeDetails> delete = cb.createCriteriaDelete(EmployeeDetails.class);

				    // set the root class
				    Root<EmployeeDetails> e = delete.from(EmployeeDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), emp.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			}  
		  

//		employeedetailsrepo.deleteById(id);
		redirect.addFlashAttribute(departmentconstants.MESSAGE,
				messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

		return "redirect:/departmentalEnquiry/deemployeedetails/1";
	}

	
	
	
	/* Delete Chargesheet list row Records */

	@GetMapping("/deleteChargesheetDetailsRecords/{id}")
	public String deleteChargesheetDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of Chargesheet details Records! ");

//		EmployeeDetails emp = employeedetailsrepo.findById(id).orElse(new EmployeeDetails());
              // creating of new obj
		   ChargesheetDetails chargedtls=chagesheetRepo.findById(id).orElse(new ChargesheetDetails());
		   
		   
		 
		   
	      model.addAttribute("chargeSheetObj", chargedtls);
//	      chargedtls.setCaseDetails(null);	//setCaseDetails	
//	      chargedtls.setFromUser(null);
//	      chargedtls.setCurrentUser(null);
//	      chargedtls.setGlobalorg(null);
			/*
			 * emp.setGlobalorg(null); emp.setServicegroup(null);
			 * emp.setSubDepartment(null); emp.setGradepay(null); emp.setPayband(null);
			 * emp.setPaycommission(null); emp.setGender(null); emp.setDesignation(null);
			 */ 
	      try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<ChargesheetDetails> delete = cb.
				     createCriteriaDelete(ChargesheetDetails.class);

				    // set the root class
				    Root<ChargesheetDetails> e = delete.from(ChargesheetDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), chargedtls.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			}  

//	      chagesheetRepo.delete(chargedtls);
		redirect.addFlashAttribute(departmentconstants.MESSAGE,
				messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

		return "redirect:/departmentalEnquiry/chargesheetDetails/1";
	}

	
	
	
	/* Deleting list of Suspension Details Records  */

	@GetMapping("/deleteSuspensionDetailsRecords/{id}")
	public String deleteSuspensionDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of Suspension Details Records ");

//		EmployeeDetails emp = employeedetailsrepo.findById(id).orElse(new EmployeeDetails());
            SuspensionDetails suspensiondetails=suspensionDetailsRepo.findById(id).orElse(new SuspensionDetails());
		   
		
	      model.addAttribute("suspensionObj", suspensiondetails);
//	      suspensiondetails.setCasedetails(null);	
//	      suspensiondetails.setSusUnderRule(null);
//	      suspensiondetails.setFromUser(null);
//	      suspensiondetails.setCurrentUser(null);
//	      suspensiondetails.setGlobalorg(null);
	      try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<SuspensionDetails> delete = cb.
				     createCriteriaDelete(SuspensionDetails.class);

				    // set the root class
				    Root<SuspensionDetails> e = delete.from(SuspensionDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), suspensiondetails.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			} 

//	      suspensionDetailsRepo.delete(suspensiondetails);
	      
		redirect.addFlashAttribute(departmentconstants.MESSAGE,
				messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

		return "redirect:/departmentalEnquiry/suspensionDetails/1";
	}

	
	
	
	
	
	/* Deleting list of Re-instated Details Records  */

	@GetMapping("/deleteReinstatedDetailsRecords/{id}")
	public String deleteReinstatedDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of Re-instated Details Records ");
		
		ReInstatedDetails  reinstateddetails=reinstatedRepository.findById(id).orElse(new ReInstatedDetails());
		 model.addAttribute("reinstatedObjDetails", reinstateddetails);
//		 reinstateddetails.setCasedetails(null);	
//		 reinstateddetails.setFromUser(null);
//		 reinstateddetails.setCurrentUser(null);
//		 reinstateddetails.setGlobalorg(null);
//		 
//		 reinstatedRepository.delete(reinstateddetails);
//	      
		 try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<ReInstatedDetails> delete = cb.
				     createCriteriaDelete(ReInstatedDetails.class);

				    // set the root class
				    Root<ReInstatedDetails> e = delete.from(ReInstatedDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), reinstateddetails.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			} 
		 
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

			return "redirect:/departmentalEnquiry/reinstaledDetails/1";
		}
	
	

		
	
	
	/* Deleting list of presenting officer Details Records : Date(13/09/2022)*/

	@GetMapping("/deletePresentingOfficerDetailsRecords/{id}")
	public String deletePresentingOfficerDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of Presenting Officer Details Records ...");
		
		PresentingOfficerDetails  presentingofficerdetails=presentingofficerRepository.findById(id).orElse(new PresentingOfficerDetails());
		 model.addAttribute("presentingofficerdetailsObj", presentingofficerdetails);
		 
		/* Setting up dropdown foreign key is null */
//		 presentingofficerdetails.setCasedetails(null);	
//		 presentingofficerdetails.setGlobalorg(null);
//		 presentingofficerdetails.setSubDepartment(null);
//		 presentingofficerdetails.setDesignation(null);
//		 presentingofficerdetails.setRevenueDivision(null);
//		 presentingofficerdetails.setFromUser(null);
//		 presentingofficerdetails.setCurrentUser(null);
//		 presentingofficerdetails.setGlobalorg(null);
		 
		 // calling of delete method
//		 presentingofficerRepository.delete(presentingofficerdetails);
		 try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<PresentingOfficerDetails> delete = cb.
				     createCriteriaDelete(PresentingOfficerDetails.class);

				    // set the root class
				    Root<PresentingOfficerDetails> e = delete.from(PresentingOfficerDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), presentingofficerdetails.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			} 
		 
	      
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

			return "redirect:/departmentalEnquiry/presentingofficerdetails/1";
		}
	
	
	     
	     
	   
	
	
	/* Deleting list of presenting officer Details Records : Date(13/09/2022)*/

	@GetMapping("/deleteProsecutionProposalDetailsRecords/{id}")
	public String deleteProsecutionProposalDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of Presenting Officer Details Records ...");
		
//		PresentingOfficerDetails  presentingofficerdetails=presentingofficerRepository.findById(id).orElse(new PresentingOfficerDetails());
		 
		ProsecutionProposalDetails prosecutionproposaldetails=prosecutionRepo.findById(id).orElse(new ProsecutionProposalDetails());
		model.addAttribute("presentingofficerdetailsObj", prosecutionproposaldetails);
		 
		/* Setting up dropdown foreign key is null */
//		prosecutionproposaldetails.setCasedetails(null);	
//		prosecutionproposaldetails.setFromUser(null);
//		prosecutionproposaldetails.setCurrentUser(null);
//		prosecutionproposaldetails.setGlobalorg(null);
//			
//		 
//		 // calling of delete method
//		prosecutionRepo.delete(prosecutionproposaldetails);
		
		 try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<ProsecutionProposalDetails> delete = cb.
				     createCriteriaDelete(ProsecutionProposalDetails.class);

				    // set the root class
				    Root<ProsecutionProposalDetails> e = delete.from(ProsecutionProposalDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), prosecutionproposaldetails.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			} 
		 
	      
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

			return "redirect:/departmentalEnquiry/prosecutionProposaldetails/1";
		}
	
	
	
	
	/* Deleting list of Court Case Details Records : Date(14/09/2022)*/

	@GetMapping("/deleteCourtCaseDetailsRecords/{id}")
	public String deleteCourtCaseDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of Court Case Details Records ...");
		
		 
	      CourtCaseDetails courtcasedetails=courtcaseRepo.findById(id).orElse(new CourtCaseDetails());
		model.addAttribute("CourtCaseObj", courtcasedetails);
		 
		/* Setting up dropdown foreign key is null */
//		courtcasedetails.setCasedetails(null);	
//		courtcasedetails.setCourtName(null);
//		courtcasedetails.setFromUser(null);
//		courtcasedetails.setCurrentUser(null);
//		courtcasedetails.setGlobalorg(null);
//		 
//		 // calling of delete method
//		courtcaseRepo.delete(courtcasedetails);
//	      
		 try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<CourtCaseDetails> delete = cb.
				     createCriteriaDelete(CourtCaseDetails.class);

				    // set the root class
				    Root<CourtCaseDetails> e = delete.from(CourtCaseDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), courtcasedetails.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			} 
		 
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

			return "redirect:/departmentalEnquiry/courtCasedetails/1";
		}
	
	
	
	
	
	/* Deleting list of Details kept Abeyance Details Records : Date(15/09/2022)*/

	@GetMapping("/deleteDetailsKeptAbeyanceDetailsRecords/{id}")
	public String deleteDetailsKeptAbeyanceDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
                    LOG.info("hi this is the deleting of details kept Abeyance cases Details Records .........");
		
		 
//	      CourtCaseDetails courtcasedetails=courtcaseRepo.findById(id).orElse(new CourtCaseDetails());
		DetailsKeptAbeyanceCases  detailskeptabeyancecases=detailskeptabeyancecasesRepository.findById(id).orElse(new DetailsKeptAbeyanceCases());
		model.addAttribute("addDetailsKeptAbeyanceCasesObj", detailskeptabeyancecases);
		 
		/* Setting up dropdown foreign key is null */
//		detailskeptabeyancecases.setCasedetails(null);	
//		detailskeptabeyancecases.setReasonreasonKeepingInAbeyance(null);
//		detailskeptabeyancecases.setFromUser(null);
//		detailskeptabeyancecases.setCurrentUser(null);
//		detailskeptabeyancecases.setGlobalorg(null);
//	
//		
//		 // calling of delete method
//	    	  detailskeptabeyancecasesRepository.delete(detailskeptabeyancecases);
		 try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<DetailsKeptAbeyanceCases> delete = cb.
				     createCriteriaDelete(DetailsKeptAbeyanceCases.class);

				    // set the root class
				    Root<DetailsKeptAbeyanceCases> e = delete.from(DetailsKeptAbeyanceCases.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), detailskeptabeyancecases.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			} 
		 
			  redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

			return "redirect:/departmentalEnquiry/keptInAbeyanceCasesdetails/1";
		}
	
	
	
	/* Deleting list of final outcome Details Records : Date(15/09/2022)*/

	@GetMapping("/deletefinalOutComeDetailsRecords/{id}")
	public String deletefinalOutComeDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of final Out Come Details Records .........");
		
		 
//	      CourtCaseDetails courtcasedetails=courtcaseRepo.findById(id).orElse(new CourtCaseDetails());
		FinalOutcomeDetails finaloutcomedetails=finaloutcomeRepository.findById(id).orElse(new FinalOutcomeDetails());
		model.addAttribute("finaloutcomedetailsObj", finaloutcomedetails);
		 
		/* Setting up dropdown foreign key is null */
//		finaloutcomedetails.setCasedetails(null);	
//		finaloutcomedetails.setCasePendingWith(null);
//		finaloutcomedetails.setDecisionTaken(null);
//		finaloutcomedetails.setTypeOfPenaltyInflicted(null);
//		finaloutcomedetails.setFromUser(null);
//		finaloutcomedetails.setCurrentUser(null);
//		finaloutcomedetails.setGlobalorg(null);
//	//	courtcasedetails.setCourtName(null);
//		 
//		 // calling of delete method
//		finaloutcomeRepository.delete(finaloutcomedetails);
		
		 try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<FinalOutcomeDetails> delete = cb.
				     createCriteriaDelete(FinalOutcomeDetails.class);

				    // set the root class
				    Root<FinalOutcomeDetails> e = delete.from(FinalOutcomeDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), finaloutcomedetails.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			} 
		 
	      
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

			return "redirect:/departmentalEnquiry/finalOutCometails/1";
		}
	
	
	
	
	/* Deleting list of final outcome Details Records : Date(15/09/2022)*/

	@GetMapping("/deleteDECasesBacklogRecords/{id}")
	public String deleteDECasesBacklogRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		System.out.println("hi this is the deleting of DE Cases Backlog Records  Records .........");
		
		 
//		FinalOutcomeDetails finaloutcomedetails=finaloutcomeRepository.findById(id).orElse(new FinalOutcomeDetails());
		   CaseDetails  casedetails=casedetailsRepository.findById(id).orElse(new CaseDetails());
		
		model.addAttribute("cases", casedetails);
		
		/* Setting up dropdown foreign key is null */
//		casedetails.setMisConductType(null);
//		casedetails.setRuleApplicable(null);
//		casedetails.setEmployeeType(null);
//		casedetails.setFromUser(null);
//		casedetails.setCurrentUser(null);
//		casedetails.setGlobalorg(null);
//		
//
//		 // calling of delete method
//		casedetailsRepository.delete(casedetails);
		
		 try {
				transactionaTemplate.execute(transactionStatus->{

					CriteriaBuilder cb = this.em.getCriteriaBuilder();

				    // create delete
				    CriteriaDelete<CaseDetails> delete = cb.
				     createCriteriaDelete(CaseDetails.class);

				    // set the root class
				    Root<CaseDetails> e = delete.from(CaseDetails.class);

				    // set where clause
				    delete.where(cb.equal(e.get("id"), casedetails.getId()));

				    // perform update
				    this.em.createQuery(delete).executeUpdate();
					return null;
				});
				
			} catch (Exception e) {
				LOG.error("Exception", e);
			} 
		 
	      
			redirect.addFlashAttribute(departmentconstants.MESSAGE,
					messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

			return "redirect:/departmentalEnquiry/caseDetails";
		}
	
	
	
	
	/* Deleting list of Inquiry Officer  Details Records ::  Date(30/09/2022)*/

	@GetMapping("/deleteInquiryOfficerDetailsRecords/{id}")
	public String deleteInquiryOfficerDetailsRecords(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
		LOG.info("hi this is the deleting of inquiry officer  Details Records .........");
		       InquiryOfficerDetails inquiryofficerdetails=inquiryofficerRepo.findById(id).orElse(new InquiryOfficerDetails());
				model.addAttribute("inquiryofficerdetailsObj", inquiryofficerdetails);
		        /* Setting up dropdown foreign key is null */
//				inquiryofficerdetails.setCasedetails(null);	
//				inquiryofficerdetails.setListOfInquiryOfficer(null);
//				inquiryofficerdetails.setFromUser(null);
//				inquiryofficerdetails.setCurrentUser(null);
//				inquiryofficerdetails.setGlobalorg(null);
//		       // calling of delete method
//				inquiryofficerRepo.delete(inquiryofficerdetails);
				 try {
						transactionaTemplate.execute(transactionStatus->{

							CriteriaBuilder cb = this.em.getCriteriaBuilder();

						    // create delete
						    CriteriaDelete<InquiryOfficerDetails> delete = cb.
						     createCriteriaDelete(InquiryOfficerDetails.class);

						    // set the root class
						    Root<InquiryOfficerDetails> e = delete.from(InquiryOfficerDetails.class);

						    // set where clause
						    delete.where(cb.equal(e.get("id"), inquiryofficerdetails.getId()));

						    // perform update
						    this.em.createQuery(delete).executeUpdate();
							return null;
						});
						
					} catch (Exception e) {
						LOG.error("Exception", e);
					} 
				 
			    redirect.addFlashAttribute(departmentconstants.MESSAGE,
				messageSource.getMessage("deptenq.redirect.deletesuccess", null, loc.resolveLocale(request)));

			return "redirect:/departmentalEnquiry/inquiryofficerdetails/1";
		}
	
}
