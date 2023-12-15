package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.ChargesheetDetails;
import com.ehrms.deptenq.models.CourtCaseDetails;
import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.DetailsKeptAbeyanceCases;
import com.ehrms.deptenq.models.Division;
import com.ehrms.deptenq.models.FinalOutcomeDetails;
import com.ehrms.deptenq.models.Gender;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.GradePay;
import com.ehrms.deptenq.models.PayCommission;
import com.ehrms.deptenq.models.Payband;
import com.ehrms.deptenq.models.ProsecutionProposalDetails;
import com.ehrms.deptenq.models.ReInstatedDetails;
import com.ehrms.deptenq.models.RulesApplicableMaster;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionDetails;
import com.ehrms.deptenq.models.User;

public class EmployeeDetailsDto {

	private Long id;

	private CaseDetails casedetails;

	@Transient
	private String caseNo;

	private Long deEmpId;

	private Long employeeId;

	private String firstName;

	private String middleName;

	private String lastName;
	
	private String email;
	
	private String mobileNumber;

	private Gender gender;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate localdate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate joinCurrentOrgdate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate superannuationDate;

	private GlobalOrg globalorg;

	/* private GlobalOrg subDepartmentName; */

	private String subDepartment;

	private Service_Group servicegroup;

	private Designation designation;

	private PayCommission paycommission;

	private Payband payband;

	private GradePay gradepay;

	private boolean active;

	private String sevarthId;

	private Division revenueDivision;

	// added on 20/10/2022
	private SubDepartment subDepartments;

	// added on 14/11/2022
	private RulesApplicableMaster ruleApplicable;
	private String officeName;

	@Transient
	private long forwardToUser;

	private User fromUser;

	private User currentUser;
	

	private ChargesheetDetails chargesheetDetails;


	private SuspensionDetails suspensionDetails;
	

	private ReInstatedDetails reInstatedDetails;
	

	private ProsecutionProposalDetails prosecutionProposalDetails;
	

	private CourtCaseDetails courtCaseDetails;
	

	private DetailsKeptAbeyanceCases detailsKeptAbeyanceCases;
	

	private FinalOutcomeDetails finalOutcomeDetails;

	/**
	 * @return the chargesheetDetails
	 */
	public ChargesheetDetails getChargesheetDetails() {
		return chargesheetDetails;
	}

	/**
	 * @param chargesheetDetails the chargesheetDetails to set
	 */
	public void setChargesheetDetails(ChargesheetDetails chargesheetDetails) {
		this.chargesheetDetails = chargesheetDetails;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the suspensionDetails
	 */
	public SuspensionDetails getSuspensionDetails() {
		return suspensionDetails;
	}

	/**
	 * @param suspensionDetails the suspensionDetails to set
	 */
	public void setSuspensionDetails(SuspensionDetails suspensionDetails) {
		this.suspensionDetails = suspensionDetails;
	}

	/**
	 * @return the reInstatedDetails
	 */
	public ReInstatedDetails getReInstatedDetails() {
		return reInstatedDetails;
	}

	/**
	 * @param reInstatedDetails the reInstatedDetails to set
	 */
	public void setReInstatedDetails(ReInstatedDetails reInstatedDetails) {
		this.reInstatedDetails = reInstatedDetails;
	}

	/**
	 * @return the prosecutionProposalDetails
	 */
	public ProsecutionProposalDetails getProsecutionProposalDetails() {
		return prosecutionProposalDetails;
	}

	/**
	 * @param prosecutionProposalDetails the prosecutionProposalDetails to set
	 */
	public void setProsecutionProposalDetails(ProsecutionProposalDetails prosecutionProposalDetails) {
		this.prosecutionProposalDetails = prosecutionProposalDetails;
	}

	/**
	 * @return the courtCaseDetails
	 */
	public CourtCaseDetails getCourtCaseDetails() {
		return courtCaseDetails;
	}

	/**
	 * @param courtCaseDetails the courtCaseDetails to set
	 */
	public void setCourtCaseDetails(CourtCaseDetails courtCaseDetails) {
		this.courtCaseDetails = courtCaseDetails;
	}

	/**
	 * @return the detailsKeptAbeyanceCases
	 */
	public DetailsKeptAbeyanceCases getDetailsKeptAbeyanceCases() {
		return detailsKeptAbeyanceCases;
	}

	/**
	 * @param detailsKeptAbeyanceCases the detailsKeptAbeyanceCases to set
	 */
	public void setDetailsKeptAbeyanceCases(DetailsKeptAbeyanceCases detailsKeptAbeyanceCases) {
		this.detailsKeptAbeyanceCases = detailsKeptAbeyanceCases;
	}

	/**
	 * @return the finalOutcomeDetails
	 */
	public FinalOutcomeDetails getFinalOutcomeDetails() {
		return finalOutcomeDetails;
	}

	/**
	 * @param finalOutcomeDetails the finalOutcomeDetails to set
	 */
	public void setFinalOutcomeDetails(FinalOutcomeDetails finalOutcomeDetails) {
		this.finalOutcomeDetails = finalOutcomeDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeEmpId() {
		return deEmpId;
	}

	public void setDeEmpId(Long deEmpId) {
		this.deEmpId = deEmpId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getLocaldate() {
		return localdate;
	}

	public void setLocaldate(LocalDate localdate) {
		this.localdate = localdate;
	}

	public LocalDate getJoinCurrentOrgdate() {
		return joinCurrentOrgdate;
	}

	public void setJoinCurrentOrgdate(LocalDate joinCurrentOrgdate) {
		this.joinCurrentOrgdate = joinCurrentOrgdate;
	}

	public LocalDate getSuperannuationDate() {
		return superannuationDate;
	}

	public void setSuperannuationDate(LocalDate superannuationDate) {
		this.superannuationDate = superannuationDate;
	}

	public GlobalOrg getGlobalorg() {
		return globalorg;
	}

	public void setGlobalorg(GlobalOrg globalorg) {
		this.globalorg = globalorg;
	}

	public Service_Group getServicegroup() {
		return servicegroup;
	}

	public void setServicegroup(Service_Group servicegroup) {
		this.servicegroup = servicegroup;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public PayCommission getPaycommission() {
		return paycommission;
	}

	public void setPaycommission(PayCommission paycommission) {
		this.paycommission = paycommission;
	}

	public Payband getPayband() {
		return payband;
	}

	public void setPayband(Payband payband) {
		this.payband = payband;
	}

	public GradePay getGradepay() {
		return gradepay;
	}

	public void setGradepay(GradePay gradepay) {
		this.gradepay = gradepay;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/*
	 * public SubDepartment getSubDepartment() { return subDepartment; }
	 * 
	 * public void setSubDepartment(SubDepartment subDepartment) {
	 * this.subDepartment = subDepartment; }
	 */

	public String getSevarthId() {
		return sevarthId;
	}

	public void setSevarthId(String sevarthId) {
		this.sevarthId = sevarthId;
	}

	/*
	 * public CaseDetails getCasedetails() { return casedetails; }
	 * 
	 * public void setCasedetails(CaseDetails casedetails) { this.casedetails =
	 * casedetails; }
	 */
	public Division getRevenueDivision() {
		return revenueDivision;
	}

	public void setRevenueDivision(Division revenueDivision) {
		this.revenueDivision = revenueDivision;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public long getForwardToUser() {
		return forwardToUser;
	}

	public void setForwardToUser(long forwardToUser) {
		this.forwardToUser = forwardToUser;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public void setCasedetails(CaseDetails casedetails) {
		this.casedetails = casedetails;
	}

	public String getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(String subDepartment) {
		this.subDepartment = subDepartment;
	}

	public SubDepartment getSubDepartments() {
		return subDepartments;
	}

	public void setSubDepartments(SubDepartment subDepartments) {
		this.subDepartments = subDepartments;
	}

	public RulesApplicableMaster getRuleApplicable() {
		return ruleApplicable;
	}

	public void setRuleApplicable(RulesApplicableMaster ruleApplicable) {
		this.ruleApplicable = ruleApplicable;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	/*
	 * public CaseDetails getCasedtls() { return casedtls; }
	 * 
	 * public void setCasedtls(CaseDetails casedtls) { this.casedtls = casedtls; }
	 */
}
