package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.InquiryOfficerList;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

public class InquiryOfficerDto {

	private Long id;

	private CaseDetails casedetails;

	private boolean inquiryOfficerAppointed;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate appointmentDate;

	private InquiryOfficerList listOfInquiryOfficer;
	
	private String otherinquiryOfficer;

	private String sevarthId;

	private Long employeeId;
	
	private String appellation;

	private String firstName;

	private String middleName;

	private String lastName;

	private Long mobileNumber;

	private String email;

	private String uploadOrderofAppointment;

	private boolean inquiryReportReceived;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofInquiryReportReceived;

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

	// added on 29/09/2022
	private GlobalOrg globalorg;

	// added on 20/10/2022 as per DE requirements....
	private SubDepartment subDepartment;

	// added on 14/11/2022 as per de requirements
	private boolean inquiryReportSubmitted;
	private String inquiryReportfileUpload;
	
	private String inquiryReportfileUploadFilePath;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate inquiryReportDate;

	private String inquiryOfficerDesignation;

	/**
	 * @return the inquiryOfficerDesignation
	 */
	public String getInquiryOfficerDesignation() {
		return inquiryOfficerDesignation;
	}

	/**
	 * @param inquiryOfficerDesignation the inquiryOfficerDesignation to set
	 */
	public void setInquiryOfficerDesignation(String inquiryOfficerDesignation) {
		this.inquiryOfficerDesignation = inquiryOfficerDesignation;
	}

	public Long getId() {
		return id;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public boolean isInquiryOfficerAppointed() {
		return inquiryOfficerAppointed;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public String getSevarthId() {
		return sevarthId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public String getUploadOrderofAppointment() {
		return uploadOrderofAppointment;
	}

	public boolean isInquiryReportReceived() {
		return inquiryReportReceived;
	}

	public LocalDate getDateofInquiryReportReceived() {
		return dateofInquiryReportReceived;
	}

	public String getFilepath() {
		return filepath;
	}

	public String getContentType() {
		return contentType;
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

	public void setInquiryOfficerAppointed(boolean inquiryOfficerAppointed) {
		this.inquiryOfficerAppointed = inquiryOfficerAppointed;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public InquiryOfficerList getListOfInquiryOfficer() {
		return listOfInquiryOfficer;
	}

	public void setListOfInquiryOfficer(InquiryOfficerList listOfInquiryOfficer) {
		this.listOfInquiryOfficer = listOfInquiryOfficer;
	}

	public void setSevarthId(String sevarthId) {
		this.sevarthId = sevarthId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUploadOrderofAppointment(String uploadOrderofAppointment) {
		this.uploadOrderofAppointment = uploadOrderofAppointment;
	}

	public void setInquiryReportReceived(boolean inquiryReportReceived) {
		this.inquiryReportReceived = inquiryReportReceived;
	}

	public void setDateofInquiryReportReceived(LocalDate dateofInquiryReportReceived) {
		this.dateofInquiryReportReceived = dateofInquiryReportReceived;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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

	public boolean isInquiryReportSubmitted() {
		return inquiryReportSubmitted;
	}

	public void setInquiryReportSubmitted(boolean inquiryReportSubmitted) {
		this.inquiryReportSubmitted = inquiryReportSubmitted;
	}

	public String getInquiryReportfileUpload() {
		return inquiryReportfileUpload;
	}

	public void setInquiryReportfileUpload(String inquiryReportfileUpload) {
		this.inquiryReportfileUpload = inquiryReportfileUpload;
	}

	public LocalDate getInquiryReportDate() {
		return inquiryReportDate;
	}

	public void setInquiryReportDate(LocalDate inquiryReportDate) {
		this.inquiryReportDate = inquiryReportDate;
	}

	/**
	 * @return the appellation
	 */
	public String getAppellation() {
		return appellation;
	}

	/**
	 * @param appellation the appellation to set
	 */
	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}

	/**
	 * @return the inquiryReportfileUploadFilePath
	 */
	public String getInquiryReportfileUploadFilePath() {
		return inquiryReportfileUploadFilePath;
	}

	/**
	 * @param inquiryReportfileUploadFilePath the inquiryReportfileUploadFilePath to set
	 */
	public void setInquiryReportfileUploadFilePath(String inquiryReportfileUploadFilePath) {
		this.inquiryReportfileUploadFilePath = inquiryReportfileUploadFilePath;
	}

	public String getOtherinquiryOfficer() {
		return otherinquiryOfficer;
	}

	public void setOtherinquiryOfficer(String otherinquiryOfficer) {
		this.otherinquiryOfficer = otherinquiryOfficer;
	}

}
