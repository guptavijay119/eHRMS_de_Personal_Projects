package com.ehrms.deptenq.models;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//import com. hrms.recruitment.constants.LANGUAGECONSTANTS;

@Entity
@Table(schema = "departmentalenquiry", name = "employee_details")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class EmployeeDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "empDetails_id_seq", name = "empDetails_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empDetails_id_seq")
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;

	@Column(name = "sevarth_id")
	private String sevarthId;

	@Column(name = "employee_id", nullable = true)
	private Long employeeId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;
	
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile_no")
	private String mobileNumber;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "gender_id")
	private Gender gender;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "date_of_birth")
	private LocalDate localdate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "join_current_org_date")
	private LocalDate joinCurrentOrgdate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "superannuation_date")
	private LocalDate superannuationDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "department_name")
	private GlobalOrg globalorg;

	/* @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) */
	@Column(name = "sub_department_name")
	private String subDepartment;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "service_group")
	private Service_Group servicegroup;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "designation")
	private Designation designation;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "paycommission_id")
	private PayCommission paycommission;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "payband_id")
	private Payband payband;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "grade_pay")
	private GradePay gradepay;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "emp")
	private NoDECertificate nodecert;

	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "revenue_division")
	private Division revenueDivision;

	/* added on 20/10/2022 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_department_id")
	private SubDepartment subDepartments;

	/* added on 14/11/2022 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "rule_applicable")
	private RulesApplicableMaster ruleApplicable;

	@Column(name = "office_name", nullable = true)
	private String officeName;

	// generating getters and setters method....

	@Transient
	private String caseNo;

	@Transient
	private long forwardToUser;
	
	@Transient
	private boolean retired;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "from_user_id")
	private User fromUser;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "current_user_id")
	private User currentUser;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "empData")
	private ChargesheetDetails chargesheetDetails;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "empData")
	private SuspensionDetails suspensionDetails;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "empData")
	private ReInstatedDetails reInstatedDetails;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "empData")
	private ProsecutionProposalDetails prosecutionProposalDetails;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "empData")
	private CourtCaseDetails courtCaseDetails;
//	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "empData")
	private DetailsKeptAbeyanceCases detailsKeptAbeyanceCases;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "empData")
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



	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		if(middleName==null) {
			middleName = "";
		}
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		if(lastName==null) {
			lastName = "";
		}
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

	/*
	 * public GlobalOrg getSubDepartmentName() { return subDepartmentName; }
	 * 
	 * 
	 * public void setSubDepartmentName(GlobalOrg subDepartmentName) {
	 * this.subDepartmentName = subDepartmentName; }
	 */

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * public SubDepartment getSubDepartment() { return subDepartment; }
	 * 
	 * 
	 * public void setSubDepartment(SubDepartment subDepartment) {
	 * this.subDepartment = subDepartment; }
	 */

	public String getSevarthId() {
		if(sevarthId==null) {
			sevarthId = "";
		}
		return sevarthId;
	}

	public void setSevarthId(String sevarthId) {
		this.sevarthId = sevarthId;
	}

	/*
	 * public CaseDetails getCasedetails() { return casedls; }
	 * 
	 * 
	 * public void setCasedetails(CaseDetails casedetails) { this.casedetails =
	 * casedetails; }
	 * 
	 */

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

	public Division getRevenueDivision() {
		return revenueDivision;
	}

	public void setRevenueDivision(Division revenueDivision) {
		this.revenueDivision = revenueDivision;
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

	public NoDECertificate getNodecert() {
		return nodecert;
	}

	public void setNodecert(NoDECertificate nodecert) {
		this.nodecert = nodecert;
	}

	public boolean isRetired() {
		this.retired = false;
		if(this.superannuationDate != null) {
			LocalDate retiredDate = this.superannuationDate;
			LocalDate currentDate = LocalDate.now();
			if(currentDate.isAfter(retiredDate)) {
				this.retired = true;
			}
		}		
		return this.retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
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

}
