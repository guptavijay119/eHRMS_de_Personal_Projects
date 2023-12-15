package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

public class ChargesheetDto {

	private Long id;

	private boolean chargesheetIssued;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate chargesheetDate;

	@Column(name = "sevarth_id")
	private String sevarthId;

	private Long employeeId;

	private String firstName;

	private String middleName;

	private String lastName;

	private String fileName;

	private String contentType;

	private String filepath;

	private String fileNameMr;

	private String fileNameEn;

	@Transient
	private String regionalText;

	private boolean active;

	private CaseDetails caseDetails;

	@Transient
	private String caseNo;

	@Transient
	private long forwardToUser;

	private User fromUser;

	private User currentUser;

	// added on 29/09/2022
	private GlobalOrg globalorg;

	// added on 20/10/2022
	private SubDepartment subDepartment;
	
	private EmployeeDetails empData;
	
	@Transient
	private String employeeDataId;
	
	
	private boolean revisedChargesheetIssued;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate revisedChargesheetDate;
	
	
	private String revisedfileName;

	private String revisedcontentType;

	private String revisedfilepath;

	// added on 18/10/2022
//     private List<EmployeeDetails> employeeList = new ArrayList<>();

	// generating getters and setters

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getChargesheetIssued() {
		return chargesheetIssued;
	}

	public void setChargesheetIssued(boolean chargesheetIssued) {
		this.chargesheetIssued = chargesheetIssued;
	}

	public LocalDate getChargesheetDate() {
		return chargesheetDate;
	}

	public void setChargesheetDate(LocalDate chargesheetDate) {
		this.chargesheetDate = chargesheetDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
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

	public CaseDetails getCaseDetails() {
		return caseDetails;
	}

	public void setCaseDetails(CaseDetails caseDetails) {
		this.caseDetails = caseDetails;
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

	/**
	 * @return the sevarthId
	 */
	public String getSevarthId() {
		return sevarthId;
	}

	/**
	 * @param sevarthId the sevarthId to set
	 */
	public void setSevarthId(String sevarthId) {
		this.sevarthId = sevarthId;
	}

	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
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
	 * @return the revisedChargesheetIssued
	 */
	public boolean isRevisedChargesheetIssued() {
		return revisedChargesheetIssued;
	}

	/**
	 * @param revisedChargesheetIssued the revisedChargesheetIssued to set
	 */
	public void setRevisedChargesheetIssued(boolean revisedChargesheetIssued) {
		this.revisedChargesheetIssued = revisedChargesheetIssued;
	}

	/**
	 * @return the revisedChargesheetDate
	 */
	public LocalDate getRevisedChargesheetDate() {
		return revisedChargesheetDate;
	}

	/**
	 * @param revisedChargesheetDate the revisedChargesheetDate to set
	 */
	public void setRevisedChargesheetDate(LocalDate revisedChargesheetDate) {
		this.revisedChargesheetDate = revisedChargesheetDate;
	}

	/**
	 * @return the revisedfileName
	 */
	public String getRevisedfileName() {
		return revisedfileName;
	}

	/**
	 * @param revisedfileName the revisedfileName to set
	 */
	public void setRevisedfileName(String revisedfileName) {
		this.revisedfileName = revisedfileName;
	}

	/**
	 * @return the revisedcontentType
	 */
	public String getRevisedcontentType() {
		return revisedcontentType;
	}

	/**
	 * @param revisedcontentType the revisedcontentType to set
	 */
	public void setRevisedcontentType(String revisedcontentType) {
		this.revisedcontentType = revisedcontentType;
	}

	/**
	 * @return the revisedfilepath
	 */
	public String getRevisedfilepath() {
		return revisedfilepath;
	}

	/**
	 * @param revisedfilepath the revisedfilepath to set
	 */
	public void setRevisedfilepath(String revisedfilepath) {
		this.revisedfilepath = revisedfilepath;
	}

//	public List<EmployeeDetails> getEmployeeList() {
//		return employeeList;
//	}
//
//	public void setEmployeeList(List<EmployeeDetails> employeeList) {
//		this.employeeList = employeeList;
//	}

}
