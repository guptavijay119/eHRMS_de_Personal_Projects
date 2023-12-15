package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.Service_Group;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

public class ProsecutionProposalDetailsDto {

	private Long id;

	private CaseDetails casedetails;

	private boolean prosecutionProposalReceived;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfReceipt;

	private String statusForGrantedDeniedPending;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrderReceipt;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate courtChargeSheetDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfCommunication;

	private String fileNameUploadCopy;

	private String contentType;

	private String filepath;

	private String fileNameMr;

	private String fileNameEn;

	private String regionalText;

	private boolean active;

	private boolean whetherCaseFiledCourt;

	private String courtName;

	private String courtCaseNo;

	/* added on 07/11/2022 */
	private String fileNo;

	private boolean withde = false;

	/* added on 12/11/2022 as per DE Requirements */
	private Long employeeId;

	private String sevarthId;

	private String firstName;

	private String middleName;

	private String lastName;
	

	private Service_Group servicegroup;


	private Designation designation;
	
	private String processFlow;
	
	private String decisionCourt;

	@Transient
	private String employeeDataId;
	
	private EmployeeDetails empData;
	
	private String finalcourtordercopy;

	private String contentTypefinalcourt;

	private String filepathfinalcourt;
	
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	 private LocalDate dateofChargesheetIssued;
	
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
	 * @return the courtName
	 */
	public String getCourtName() {
		return courtName;
	}

	/**
	 * @param courtName the courtName to set
	 */
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	/**
	 * @return the courtCaseNo
	 */
	public String getCourtCaseNo() {
		return courtCaseNo;
	}

	/**
	 * @param courtCaseNo the courtCaseNo to set
	 */
	public void setCourtCaseNo(String courtCaseNo) {
		this.courtCaseNo = courtCaseNo;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrderCaseFiled;

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

	// adding fields

	private boolean whetherCaseFiledIntheCourt;

	private String status;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrderoftheCourt;

	/* added on 18/10/2022 as per DE requirements */
	public String remark;

	// generating getter & setter

	public Long getId() {
		return id;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public boolean isProsecutionProposalReceived() {
		return prosecutionProposalReceived;
	}

	public LocalDate getDateOfReceipt() {
		return dateOfReceipt;
	}

	public LocalDate getDateOfOrderReceipt() {
		return dateOfOrderReceipt;
	}

	public LocalDate getDateOfCommunication() {
		return dateOfCommunication;
	}

	public String getFileNameUploadCopy() {
		return fileNameUploadCopy;
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

	public boolean isWhetherCaseFiledCourt() {
		return whetherCaseFiledCourt;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getDateOfOrderCaseFiled() {
		return dateOfOrderCaseFiled;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCasedetails(CaseDetails casedetails) {
		this.casedetails = casedetails;
	}

	public void setProsecutionProposalReceived(boolean prosecutionProposalReceived) {
		this.prosecutionProposalReceived = prosecutionProposalReceived;
	}

	public void setDateOfReceipt(LocalDate dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public void setDateOfOrderReceipt(LocalDate dateOfOrderReceipt) {
		this.dateOfOrderReceipt = dateOfOrderReceipt;
	}

	public void setDateOfCommunication(LocalDate dateOfCommunication) {
		this.dateOfCommunication = dateOfCommunication;
	}

	public void setFileNameUploadCopy(String fileNameUploadCopy) {
		this.fileNameUploadCopy = fileNameUploadCopy;
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

	public void setWhetherCaseFiledCourt(boolean whetherCaseFiledCourt) {
		this.whetherCaseFiledCourt = whetherCaseFiledCourt;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDateOfOrderCaseFiled(LocalDate dateOfOrderCaseFiled) {
		this.dateOfOrderCaseFiled = dateOfOrderCaseFiled;
	}

	public String getStatusForGrantedDeniedPending() {
		return statusForGrantedDeniedPending;
	}

	public void setStatusForGrantedDeniedPending(String statusForGrantedDeniedPending) {
		this.statusForGrantedDeniedPending = statusForGrantedDeniedPending;
	}

	public boolean isWhetherCaseFiledIntheCourt() {
		return whetherCaseFiledIntheCourt;
	}

	public void setWhetherCaseFiledIntheCourt(boolean whetherCaseFiledIntheCourt) {
		this.whetherCaseFiledIntheCourt = whetherCaseFiledIntheCourt;
	}

	public LocalDate getDateOfOrderoftheCourt() {
		return dateOfOrderoftheCourt;
	}

	public void setDateOfOrderoftheCourt(LocalDate dateOfOrderoftheCourt) {
		this.dateOfOrderoftheCourt = dateOfOrderoftheCourt;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the decisionCourt
	 */
	public String getDecisionCourt() {
		return decisionCourt;
	}

	/**
	 * @param decisionCourt the decisionCourt to set
	 */
	public void setDecisionCourt(String decisionCourt) {
		this.decisionCourt = decisionCourt;
	}

	/**
	 * @return the finalcourtordercopy
	 */
	public String getFinalcourtordercopy() {
		return finalcourtordercopy;
	}

	/**
	 * @param finalcourtordercopy the finalcourtordercopy to set
	 */
	public void setFinalcourtordercopy(String finalcourtordercopy) {
		this.finalcourtordercopy = finalcourtordercopy;
	}

	/**
	 * @return the contentTypefinalcourt
	 */
	public String getContentTypefinalcourt() {
		return contentTypefinalcourt;
	}

	/**
	 * @param contentTypefinalcourt the contentTypefinalcourt to set
	 */
	public void setContentTypefinalcourt(String contentTypefinalcourt) {
		this.contentTypefinalcourt = contentTypefinalcourt;
	}

	/**
	 * @return the filepathfinalcourt
	 */
	public String getFilepathfinalcourt() {
		return filepathfinalcourt;
	}

	/**
	 * @param filepathfinalcourt the filepathfinalcourt to set
	 */
	public void setFilepathfinalcourt(String filepathfinalcourt) {
		this.filepathfinalcourt = filepathfinalcourt;
	}

	public LocalDate getDateofChargesheetIssued() {
		return dateofChargesheetIssued;
	}

	public void setDateofChargesheetIssued(LocalDate dateofChargesheetIssued) {
		this.dateofChargesheetIssued = dateofChargesheetIssued;
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

	public LocalDate getCourtChargeSheetDate() {
		return courtChargeSheetDate;
	}

	public void setCourtChargeSheetDate(LocalDate courtChargeSheetDate) {
		this.courtChargeSheetDate = courtChargeSheetDate;
	}

}
