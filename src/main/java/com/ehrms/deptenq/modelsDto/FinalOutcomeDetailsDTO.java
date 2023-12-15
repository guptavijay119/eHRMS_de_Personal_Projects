package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.CasePendingWithPojo;
import com.ehrms.deptenq.models.DecisionTakenPojo;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.ReasonForKeepingCaseAbeyance;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.TypeOfPenaltyInflictedPojo;
import com.ehrms.deptenq.models.User;

public class FinalOutcomeDetailsDTO {

	private Long id;

	private CaseDetails casedetails;

	private boolean inquiryReportReceived;

	private String sevarthId;

	private Long employeeId;

	private String firstName;

	private String middleName;

	private String lastName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofReceipt;

	private CasePendingWithPojo casePendingWith;

	private boolean whetherCaseFinallyDecided;

	private DecisionTakenPojo decisionTaken;
	
	private boolean whethercasekeptinabeyance;

	private TypeOfPenaltyInflictedPojo typeOfPenaltyInflicted;

	private String fileName;

	private String filepath;

	private String contentType;

	private String fileNameMr;

	private String fileNameEn;

	private String regionalText;

	private boolean active;

	@Transient
	private String caseNo;

	@Transient
	private long forwardToUser;

	private User fromUser;

	private User currentUser;

	// 29/09/2022
	private GlobalOrg globalorg;

	// added on 20/10/2022 as per DE requirements...
	private SubDepartment subDepartment;

	private boolean withde = false;

	private String processFlow;
	
	private String remark;
	
	private boolean whetherfinalorderIssued;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfFinalOrder;
	
	/*
	 * //added by vijay for date of order upload (08/12/2022)
	 * 
	 * @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate dateofOrderUpload;
	 */
	
	private String fileNo;
	
	private String decisionTakenWithoutDe;
	
	private String otherdecision;
	
	private   ReasonForKeepingCaseAbeyance reasonreasonKeepingInAbeyance;
	
	@Transient
	private String employeeDataId;
	
	
	private EmployeeDetails empData;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrder;
	

	// generating getters and setters

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

	/*
	 * public LocalDate getDateofOrderUpload() { return dateofOrderUpload; }
	 * 
	 * public void setDateofOrderUpload(LocalDate dateofOrderUpload) {
	 * this.dateofOrderUpload = dateofOrderUpload; }
	 */

}
