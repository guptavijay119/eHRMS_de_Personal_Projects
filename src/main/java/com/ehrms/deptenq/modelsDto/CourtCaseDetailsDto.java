package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.CourtName;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

public class CourtCaseDetailsDto {

	private Long id;

	private CaseDetails casedetails;

	private boolean anyRelatedCourtCaseExist;

	private String courtCaseNo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate courtCaseFiledDate;

	private CourtName courtType;

	private String status;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfFinalOrder;

	private String uploadOder;

	private String contentType;

	private String filepath;

	private String fileNameMr;

	private String fileNameEn;

	private String regionalText;

	private boolean active;

	// addng extra fields according to pdf

	private String sevarthId;
	private String firstName;
	private String middleName;
	private String lastName;

	@Transient
	private String caseNo;

	@Transient
	private long forwardToUser;

	private User fromUser;

	private User currentUser;

	private String courtName;

	// added on 29/09/2022
	private GlobalOrg globalorg;

	// added on 20/10/2022 as per DE requirements...
	private SubDepartment subDepartment;

	private EmployeeDetails empData;

	@Transient
	private String employeeDataId;

	// generating getters and setters

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

	public Long getId() {
		return id;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public boolean isAnyRelatedCourtCaseExist() {
		return anyRelatedCourtCaseExist;
	}

	public String getCourtCaseNo() {
		return courtCaseNo;
	}

	public LocalDate getCourtCaseFiledDate() {
		return courtCaseFiledDate;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getDateOfFinalOrder() {
		return dateOfFinalOrder;
	}

	public String getUploadOder() {
		return uploadOder;
	}

	public String getContentType() {
		return contentType;
	}

	public String getFilepath() {
		return filepath;
	}

	public String getFileNameMr() {
		return fileNameMr;
	}

	public String getFileNameEn() {
		return fileNameEn;
	}

	public String getRegionalText() {
		return regionalText;
	}

	public boolean isActive() {
		return active;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCasedetails(CaseDetails casedetails) {
		this.casedetails = casedetails;
	}

	public void setAnyRelatedCourtCaseExist(boolean anyRelatedCourtCaseExist) {
		this.anyRelatedCourtCaseExist = anyRelatedCourtCaseExist;
	}

	public void setCourtCaseNo(String courtCaseNo) {
		this.courtCaseNo = courtCaseNo;
	}

	public void setCourtCaseFiledDate(LocalDate courtCaseFiledDate) {
		this.courtCaseFiledDate = courtCaseFiledDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDateOfFinalOrder(LocalDate dateOfFinalOrder) {
		this.dateOfFinalOrder = dateOfFinalOrder;
	}

	public void setUploadOder(String uploadOder) {
		this.uploadOder = uploadOder;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setFileNameMr(String fileNameMr) {
		this.fileNameMr = fileNameMr;
	}

	public void setFileNameEn(String fileNameEn) {
		this.fileNameEn = fileNameEn;
	}

	public void setRegionalText(String regionalText) {
		this.regionalText = regionalText;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getSevarthId() {
		return sevarthId;
	}

	public void setSevarthId(String sevarthId) {
		this.sevarthId = sevarthId;
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

	/*
	 * public CourtName getCourtName() { return courtName; }
	 * 
	 * public void setCourtName(CourtName courtName) { this.courtName = courtName; }
	 */

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

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	public CourtName getCourtType() {
		return courtType;
	}

	public void setCourtType(CourtName courtType) {
		this.courtType = courtType;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

}
