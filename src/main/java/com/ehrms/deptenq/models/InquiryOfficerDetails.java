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
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "departmentalenquiry", name = "inquiry_officer_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class InquiryOfficerDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "InquiryOfficer_id_seq", name = "InquiryOfficer_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "InquiryOfficer_id_seq")
	@Column(name = "inquiryofficer_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;

	@Column(name = "inquiry_officer_appointed", nullable = true)
	@ColumnDefault(value = "false")
	private boolean inquiryOfficerAppointed;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "appointment_date")
	private LocalDate appointmentDate;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "list_inquiry_officer")
	private InquiryOfficerList listOfInquiryOfficer;
	
	@Column(name = "other_inquiry_officer", nullable = true)
	private String otherinquiryOfficer;

	@Column(name = "sevarth_id", nullable = true)
	private String sevarthId;

	@Column(name = "employee_Id", nullable = true)
	private Long employeeId;

	@Column(name = "appellation")
	private String appellation;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "mobile_number")
	private Long mobileNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "upload_order_of_appointment")
	private String uploadOrderofAppointment;

	@Column(name = "inquiry_report_received", nullable = true)
	@ColumnDefault(value = "false")
	private boolean inquiryReportReceived;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date_of_inquiry_report_received")
	private LocalDate dateofInquiryReportReceived;

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

	// added on 14/11/2022 as per de requirements
	@Column(name = "inquiry_report_submitted", nullable = true)
	@ColumnDefault(value = "false")
	private boolean inquiryReportSubmitted;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "inquiry_report_date")
	private LocalDate inquiryReportDate;

	@Column(name = "inquiry_report_file_upload")
	private String inquiryReportfileUpload;
	
	@Column(name = "inquiry_report_file_upload_file_path")
	private String inquiryReportfileUploadFilePath;

	@Column(name = "inquiry_officer_designation")
	private String inquiryOfficerDesignation;

	// gen get and set below

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

	public void setId(Long id) {
		this.id = id;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public void setCasedetails(CaseDetails casedetails) {
		this.casedetails = casedetails;
	}

	public boolean isInquiryOfficerAppointed() {
		return inquiryOfficerAppointed;
	}

	public void setInquiryOfficerAppointed(boolean inquiryOfficerAppointed) {
		this.inquiryOfficerAppointed = inquiryOfficerAppointed;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
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

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUploadOrderofAppointment() {
		return uploadOrderofAppointment;
	}

	public void setUploadOrderofAppointment(String uploadOrderofAppointment) {
		this.uploadOrderofAppointment = uploadOrderofAppointment;
	}

	public boolean isInquiryReportReceived() {
		return inquiryReportReceived;
	}

	public void setInquiryReportReceived(boolean inquiryReportReceived) {
		this.inquiryReportReceived = inquiryReportReceived;
	}

	public LocalDate getDateofInquiryReportReceived() {
		return dateofInquiryReportReceived;
	}

	public void setDateofInquiryReportReceived(LocalDate dateofInquiryReportReceived) {
		this.dateofInquiryReportReceived = dateofInquiryReportReceived;
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

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
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

	public boolean isInquiryReportSubmitted() {
		return inquiryReportSubmitted;
	}

	public void setInquiryReportSubmitted(boolean inquiryReportSubmitted) {
		this.inquiryReportSubmitted = inquiryReportSubmitted;
	}

	public LocalDate getInquiryReportDate() {
		return inquiryReportDate;
	}

	public void setInquiryReportDate(LocalDate inquiryReportDate) {
		this.inquiryReportDate = inquiryReportDate;
	}

	public String getInquiryReportfileUpload() {
		return inquiryReportfileUpload;
	}

	public void setInquiryReportfileUpload(String inquiryReportfileUpload) {
		this.inquiryReportfileUpload = inquiryReportfileUpload;
	}

	public String getOtherinquiryOfficer() {
		return otherinquiryOfficer;
	}

	public void setOtherinquiryOfficer(String otherinquiryOfficer) {
		this.otherinquiryOfficer = otherinquiryOfficer;
	}

}
