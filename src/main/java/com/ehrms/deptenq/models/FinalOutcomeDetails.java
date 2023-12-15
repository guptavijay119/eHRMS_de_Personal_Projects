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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "departmentalenquiry", name = "final_outcome_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class FinalOutcomeDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "final_outcome_id_seq", name = "final_outcome_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "final_outcome_id_seq")
	@Column(name = "final_outcome_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;

	@Column(name = "inquiry_report_received", nullable = true)
	@ColumnDefault(value = "false")
	private boolean inquiryReportReceived;
	
	@Column(name = "whether_final_order_issued", nullable = true)
	@ColumnDefault(value = "false")
	private boolean whetherfinalorderIssued;
	
	@Column(name = "whether_case_kept_in_abeyance", nullable = true)
	@ColumnDefault(value = "false")
	private boolean whethercasekeptinabeyance;
	
	@Column(name = "date_of_final_order")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfFinalOrder;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "reason_keeping_in_abeyance")
	private   ReasonForKeepingCaseAbeyance reasonreasonKeepingInAbeyance;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="employee_data_id")
	private EmployeeDetails empData;
	
	@Column(name = "date_of_order")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrder;
	
	
	
//	private String caseType;
	

	/**
	 * @return the dateOfOrder
	 */
	public LocalDate getDateOfOrder() {
		return dateOfOrder;
	}

	/**
	 * @param dateOfOrder the dateOfOrder to set
	 */
	public void setDateOfOrder(LocalDate dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	/**
	 * @return the reasonreasonKeepingInAbeyance
	 */
	public ReasonForKeepingCaseAbeyance getReasonreasonKeepingInAbeyance() {
		return reasonreasonKeepingInAbeyance;
	}

	/**
	 * @param reasonreasonKeepingInAbeyance the reasonreasonKeepingInAbeyance to set
	 */
	public void setReasonreasonKeepingInAbeyance(ReasonForKeepingCaseAbeyance reasonreasonKeepingInAbeyance) {
		this.reasonreasonKeepingInAbeyance = reasonreasonKeepingInAbeyance;
	}

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

	@Column(name = "date_of_receipt")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofReceipt;
	
	
	/*
	 * //added by vijay for date of order upload (08/12/2022)
	 * 
	 * @Column(name = "date_of_orderUpload")
	 * 
	 * @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate dateofOrderUpload;
	 */

	

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_pending_with")
	private CasePendingWithPojo casePendingWith;

	@Column(name = "whether_case_finally_decided", nullable = true)
	@ColumnDefault(value = "false")
	private boolean whetherCaseFinallyDecided;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "decision_taken")
	private DecisionTakenPojo decisionTaken;

	// @Column(name = "typeof_penalty_inflicted", nullable = true)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "typeof_penalty_inflicted")
	private TypeOfPenaltyInflictedPojo typeOfPenaltyInflicted;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_path")
	private String filepath;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "file_name_mr")
	private String fileNameMr;

	@Column(name = "file_name_en")
	private String fileNameEn;

	@Transient
	private String regionalText;

	@Column(name = "is_active")

	@ColumnDefault(value = "false")
	private boolean active;
	@Transient
	private String caseNo;

	@Transient
	private long forwardToUser;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "from_user_id")
	private User fromUser;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "current_user_id")
	private User currentUser;

	// added on 29/09/2022
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "global_org_id")
	private GlobalOrg globalorg;

	// added on 20/10/2022
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_department_id")
	private SubDepartment subDepartment;

	@Column(name = "with_de")
	@ColumnDefault(value = "true")
	private boolean withde = false;

	@Column(name = "processFlow")
	private String processFlow;

	@Column(name = "remark")
	private String remark;
	
	@Column(name="file_no")
	private String fileNo;
	
	@Transient
	private String employeeDataId;
	
	
	@Column(name="decision_taken_without_de")
	private String decisionTakenWithoutDe;
	
	@Column(name="other_decision")
	private String otherdecision;

	// generating getters and setters

	/**
	 * @return the otherdecision
	 */
	public String getOtherdecision() {
		return otherdecision;
	}

	/**
	 * @param otherdecision the otherdecision to set
	 */
	public void setOtherdecision(String otherdecision) {
		this.otherdecision = otherdecision;
	}

	/**
	 * @return the decisionTakenWithoutDe
	 */
	public String getDecisionTakenWithoutDe() {
		return decisionTakenWithoutDe;
	}

	/**
	 * @param decisionTakenWithoutDe the decisionTakenWithoutDe to set
	 */
	public void setDecisionTakenWithoutDe(String decisionTakenWithoutDe) {
		this.decisionTakenWithoutDe = decisionTakenWithoutDe;
	}

	/**
	 * @return the employeeDataId
	 */
	public String getEmployeeDataId() {
		return employeeDataId;
	}

	/**
	 * @param employeeDataId the employeeDataId to set
	 */
	public void setEmployeeDataId(String employeeDataId) {
		this.employeeDataId = employeeDataId;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the processFlow
	 */
	public String getProcessFlow() {
		return processFlow;
	}

	/**
	 * @param processFlow the processFlow to set
	 */
	public void setProcessFlow(String processFlow) {
		this.processFlow = processFlow;
	}

	/**
	 * @return the withde
	 */
	public boolean isWithde() {
		return withde;
	}

	/**
	 * @param withde the withde to set
	 */
	public void setWithde(boolean withde) {
		this.withde = withde;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public void setCasedetails(CaseDetails casedetails) {
		this.casedetails = casedetails;
	}

	public boolean isInquiryReportReceived() {
		return inquiryReportReceived;
	}

	public void setInquiryReportReceived(boolean inquiryReportReceived) {
		this.inquiryReportReceived = inquiryReportReceived;
	}

	public LocalDate getDateofReceipt() {
		return dateofReceipt;
	}

	public void setDateofReceipt(LocalDate dateofReceipt) {
		this.dateofReceipt = dateofReceipt;
	}

	public boolean isWhetherCaseFinallyDecided() {
		return whetherCaseFinallyDecided;
	}

	public void setWhetherCaseFinallyDecided(boolean whetherCaseFinallyDecided) {
		this.whetherCaseFinallyDecided = whetherCaseFinallyDecided;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileNameMr() {
		return fileNameMr;
	}

	public void setFileNameMr(String fileNameMr) {
		this.fileNameMr = fileNameMr;
	}

	public String getFileNameEn() {
		return fileNameEn;
	}

	public void setFileNameEn(String fileNameEn) {
		this.fileNameEn = fileNameEn;
	}

	public String getRegionalText() {
		return regionalText;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public CasePendingWithPojo getCasePendingWith() {
		return casePendingWith;
	}

	public void setCasePendingWith(CasePendingWithPojo casePendingWith) {
		this.casePendingWith = casePendingWith;
	}

	public DecisionTakenPojo getDecisionTaken() {
		return decisionTaken;
	}

	public void setDecisionTaken(DecisionTakenPojo decisionTaken) {
		this.decisionTaken = decisionTaken;
	}

	public TypeOfPenaltyInflictedPojo getTypeOfPenaltyInflicted() {
		return typeOfPenaltyInflicted;
	}

	public void setTypeOfPenaltyInflicted(TypeOfPenaltyInflictedPojo typeOfPenaltyInflicted) {
		this.typeOfPenaltyInflicted = typeOfPenaltyInflicted;
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

	public GlobalOrg getGlobalorg() {
		return globalorg;
	}

	public void setGlobalorg(GlobalOrg globalorg) {
		this.globalorg = globalorg;
	}

	public String getSevarthId() {
		return sevarthId;
	}

	public void setSevarthId(String sevarthId) {
		this.sevarthId = sevarthId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * @return the whetherfinalorderIssued
	 */
	public boolean isWhetherfinalorderIssued() {
		return whetherfinalorderIssued;
	}

	/**
	 * @param whetherfinalorderIssued the whetherfinalorderIssued to set
	 */
	public void setWhetherfinalorderIssued(boolean whetherfinalorderIssued) {
		this.whetherfinalorderIssued = whetherfinalorderIssued;
	}

	/**
	 * @return the dateOfFinalOrder
	 */
	public LocalDate getDateOfFinalOrder() {
		return dateOfFinalOrder;
	}

	/**
	 * @param dateOfFinalOrder the dateOfFinalOrder to set
	 */
	public void setDateOfFinalOrder(LocalDate dateOfFinalOrder) {
		this.dateOfFinalOrder = dateOfFinalOrder;
	}

	/**
	 * @return the fileNo
	 */
	public String getFileNo() {
		return fileNo;
	}

	/**
	 * @param fileNo the fileNo to set
	 */
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	/**
	 * @return the whethercasekeptinabeyance
	 */
	public boolean isWhethercasekeptinabeyance() {
		return whethercasekeptinabeyance;
	}

	/**
	 * @param whethercasekeptinabeyance the whethercasekeptinabeyance to set
	 */
	public void setWhethercasekeptinabeyance(boolean whethercasekeptinabeyance) {
		this.whethercasekeptinabeyance = whethercasekeptinabeyance;
	}

	/**
	 * @return the empData
	 */
	public EmployeeDetails getEmpData() {
		return empData;
	}

	/**
	 * @param empData the empData to set
	 */
	public void setEmpData(EmployeeDetails empData) {
		this.empData = empData;
	}

	/*
	 * public LocalDate getDateofOrderUpload() { return dateofOrderUpload; }
	 * 
	 * public void setDateofOrderUpload(LocalDate dateofOrderUpload) {
	 * this.dateofOrderUpload = dateofOrderUpload; }
	 */

}
