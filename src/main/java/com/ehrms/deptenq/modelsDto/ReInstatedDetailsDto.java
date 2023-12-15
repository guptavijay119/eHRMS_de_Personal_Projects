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

public class ReInstatedDetailsDto {

	private Long id;

	private CaseDetails casedetails;

	private String sevarthId;

	private Long employeeId;

	private String firstName;

	private String middleName;

	private String lastName;

	private boolean employeeReInstated;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate reInstatedOrderDate;

	private String UploadOrderReInstatment;

	private String filepath;

	private String contentType;

	private String fileNameMr;

	private String fileNameEn;

	private String regionalText;

	private boolean active;
	
	
	// added on 03/01/2023
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "date_of_Suspension")
	private LocalDate dateofSuspension;
	
	
	@Transient
	private String caseNo;
	
	@Transient
	private long forwardToUser;

	
	private User fromUser;

	
	private User currentUser;
	
	private boolean withde = true;

	private String fileNo;

	//added on 29/09/2022
	private GlobalOrg globalorg;
	
	//added on 20/10/2022 as per DE requirements...
    private SubDepartment subDepartment;
    
    @Transient
	private String employeeDataId;
    
	private EmployeeDetails empData;
	
	private Long year;
	private Long month;
	private Long day;
	

	
	// GENERATING GETTERS AND SETTERS

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Long getMonth() {
		return month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}

	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
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

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public void setCasedetails(CaseDetails casedetails) {
		this.casedetails = casedetails;
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

	public boolean isEmployeeReInstated() {
		return employeeReInstated;
	}

	public void setEmployeeReInstated(boolean employeeReInstated) {
		this.employeeReInstated = employeeReInstated;
	}

	
	public LocalDate getReInstatedOrderDate() {
		return reInstatedOrderDate;
	}

	public void setReInstatedOrderDate(LocalDate reInstatedOrderDate) {
		this.reInstatedOrderDate = reInstatedOrderDate;
	}

	public String getUploadOrderReInstatment() {
		return UploadOrderReInstatment;
	}

	public void setUploadOrderReInstatment(String uploadOrderReInstatment) {
		UploadOrderReInstatment = uploadOrderReInstatment;
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

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
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

	public LocalDate getDateofSuspension() {
		return dateofSuspension;
	}

	public void setDateofSuspension(LocalDate dateofSuspension) {
		this.dateofSuspension = dateofSuspension;
	}

}
