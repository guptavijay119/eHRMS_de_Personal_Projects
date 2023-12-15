package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SuspensionRules;
import com.ehrms.deptenq.models.User;

public class SuspensionDetailsDto {

	private Long id;

	private CaseDetails casedetails;

	private String sevarthId;

	private Long employeeId;

	private String firstName;

	private String middleName;

	private String lastName;

	private boolean employeeSuspended;

	private boolean withde = true;

	private SuspensionRules susUnderRule;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrder;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofActualSuspension;

	private String uploadOrderActualSuspensionName;

	private String filepathActualSuspension;

	private String contentType;

	private String fileNameMr;

	private String fileNameEn;

	@Transient
	private String regionalText;

	private boolean active;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofDeemedSuspension;

	private String uploadOrderDeemedSuspension;

	private String filePathUploadOrderDeemedSuspension;

	private boolean whetherSuspensionIsExtnd3Months;

	private String periodofExtension;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofExtensionOrder;

	private String extensionUploadOrder;

	private String filePathExtensionUploadOrder;

	// adding extra variable according to latest pdf
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrder1;

	private String periodofExtensionInDays;

	@Transient
	private String caseNo;

	private String fileNo;

	@Transient
	private long forwardToUser;

	private User fromUser;

	private User currentUser;

	private EmployeeDetails empData;

	// added on 29/09/2022
	private GlobalOrg globalorg;

	// added on 20/10/2022 as per DE requirements...
	private SubDepartment subDepartment;

	// GENERATING GETTERS AND SETTERS
	
	private String remark;
	
	private boolean whetherReviewTaken;
	
	private String currentStatus;
	
	@Transient
	private String employeeDataId;
	

	private Service_Group servicegroup;


	private Designation designation;
	
	private String email;
	
	private String mobileNumber;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDate getDateofActualSuspension() {
		return dateofActualSuspension;
	}

	public void setDateofActualSuspension(LocalDate dateofActualSuspension) {
		this.dateofActualSuspension = dateofActualSuspension;
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

	public LocalDate getDateofDeemedSuspension() {
		return dateofDeemedSuspension;
	}

	public void setDateofDeemedSuspension(LocalDate dateofDeemedSuspension) {
		this.dateofDeemedSuspension = dateofDeemedSuspension;
	}

	public String getUploadOrderDeemedSuspension() {
		return uploadOrderDeemedSuspension;
	}

	public void setUploadOrderDeemedSuspension(String uploadOrderDeemedSuspension) {
		this.uploadOrderDeemedSuspension = uploadOrderDeemedSuspension;
	}

	public boolean isWhetherSuspensionIsExtnd3Months() {
		return whetherSuspensionIsExtnd3Months;
	}

	public void setWhetherSuspensionIsExtnd3Months(boolean whetherSuspensionIsExtnd3Months) {
		this.whetherSuspensionIsExtnd3Months = whetherSuspensionIsExtnd3Months;
	}

	public String getPeriodofExtension() {
		return periodofExtension;
	}

	public void setPeriodofExtension(String periodofExtension) {
		this.periodofExtension = periodofExtension;
	}

	public LocalDate getDateofExtensionOrder() {
		return dateofExtensionOrder;
	}

	public void setDateofExtensionOrder(LocalDate dateofExtensionOrder) {
		this.dateofExtensionOrder = dateofExtensionOrder;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public void setCasedetails(CaseDetails casedetails) {
		this.casedetails = casedetails;
	}

	public String getUploadOrderActualSuspensionName() {
		return uploadOrderActualSuspensionName;
	}

	public void setUploadOrderActualSuspensionName(String uploadOrderActualSuspensionName) {
		this.uploadOrderActualSuspensionName = uploadOrderActualSuspensionName;
	}

	public String getFilepathActualSuspension() {
		return filepathActualSuspension;
	}

	public void setFilepathActualSuspension(String filepathActualSuspension) {
		this.filepathActualSuspension = filepathActualSuspension;
	}

	public String getFilePathUploadOrderDeemedSuspension() {
		return filePathUploadOrderDeemedSuspension;
	}

	public void setFilePathUploadOrderDeemedSuspension(String filePathUploadOrderDeemedSuspension) {
		this.filePathUploadOrderDeemedSuspension = filePathUploadOrderDeemedSuspension;
	}

	public String getExtensionUploadOrder() {
		return extensionUploadOrder;
	}

	public void setExtensionUploadOrder(String extensionUploadOrder) {
		this.extensionUploadOrder = extensionUploadOrder;
	}

	public String getFilePathExtensionUploadOrder() {
		return filePathExtensionUploadOrder;
	}

	public void setFilePathExtensionUploadOrder(String filePathExtensionUploadOrder) {
		this.filePathExtensionUploadOrder = filePathExtensionUploadOrder;
	}

	public boolean isEmployeeSuspended() {
		return employeeSuspended;
	}

	public void setEmployeeSuspended(boolean employeeSuspended) {
		this.employeeSuspended = employeeSuspended;
	}

	public SuspensionRules getSusUnderRule() {
		return susUnderRule;
	}

	public void setSusUnderRule(SuspensionRules susUnderRule) {
		this.susUnderRule = susUnderRule;
	}

	public LocalDate getDateOfOrder() {
		return dateOfOrder;
	}

	public void setDateOfOrder(LocalDate dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	public LocalDate getDateOfOrder1() {
		return dateOfOrder1;
	}

	public void setDateOfOrder1(LocalDate dateOfOrder1) {
		this.dateOfOrder1 = dateOfOrder1;
	}

	public String getPeriodofExtensionInDays() {
		return periodofExtensionInDays;
	}

	public void setPeriodofExtensionInDays(String periodofExtensionInDays) {
		this.periodofExtensionInDays = periodofExtensionInDays;
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

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
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
	 * @return the whetherReviewTaken
	 */
	public boolean isWhetherReviewTaken() {
		return whetherReviewTaken;
	}

	/**
	 * @param whetherReviewTaken the whetherReviewTaken to set
	 */
	public void setWhetherReviewTaken(boolean whetherReviewTaken) {
		this.whetherReviewTaken = whetherReviewTaken;
	}

	/**
	 * @return the currentStatus
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus the currentStatus to set
	 */
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
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
