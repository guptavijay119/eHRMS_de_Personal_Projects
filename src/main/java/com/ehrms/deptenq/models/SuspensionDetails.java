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
@Table(schema = "departmentalenquiry", name = "suspension_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SuspensionDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "suspension_id_seq", name = "suspension_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suspension_id_seq")
	@Column(name = "suspension_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;

	@Column(name = "sevarth_id", nullable = true)
	private String sevarthId;

	@Column(name = "employee_id", nullable = true)
	private Long employeeId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;
	
	@Column(name="with_de")
	@ColumnDefault(value="true")
	private boolean withde =false;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "is_employee_suspended", nullable = true)
	@ColumnDefault(value = "false")
	private boolean employeeSuspended;

	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "sus_under_rule_id")
	private SuspensionRules susUnderRule;

	@Column(name = "date_of_order_actual_suspension")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrder;

	@Column(name = "date_of_actual_suspension")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofActualSuspension;

	@Column(name = "uploadorder_actual_suspension_name")
	private String uploadOrderActualSuspensionName;

	@Column(name = "file_path_actual_suspension")
	private String filepathActualSuspension;

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

	@Column(name = "dateof_deemed_suspension")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofDeemedSuspension;

	@Column(name = "upload_order_deemed_suspension")
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String uploadOrderDeemedSuspension;

	@Column(name = "filepath_uploadorder_deemed_suspension")
	private String filePathUploadOrderDeemedSuspension;

	@Column(name = "whether_suspensionis_extnd3months", nullable = true)
	@ColumnDefault(value = "false")
	private boolean whetherSuspensionIsExtnd3Months;
	
	@Column(name = "whether_review_taken", nullable = true)
	@ColumnDefault(value = "false")
	private boolean whetherReviewTaken;
	
	@Column(name = "current_status")
	private String currentStatus;

	@Column(name = "period_of_extension_in_months")
	private String periodofExtension;

	@Column(name = "dateof_extension_order")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofExtensionOrder;

	@Column(name = "extension_upload_order")
	private String extensionUploadOrder;

	@Column(name = "file_path_extension_upload_order")
	private String filePathExtensionUploadOrder;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="employee_data_id")
	private EmployeeDetails empData;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "service_group")
	private Service_Group servicegroup;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "designation")
	private Designation designation;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile_no")
	private String mobileNumber;
	
	@Column(name = "remark")
	private String remark;
	
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

	/* adding fields according to latest pdf */
	@Transient
	private String employeeDataId;
	
	
	
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

	@Column(name = "date_of_order_deemed_suspension")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrder1;
	
	
	@Column(name = "period_of_extension_in_days")
	private String periodofExtensionInDays;
	
	
	@Transient
	private String caseNo;
	
	@Column(name="file_no")
	private String fileNo;
	
	@Transient
	private long forwardToUser;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="from_user_id")
	private User fromUser;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="current_user_id")
	private User currentUser;
	
	@Transient
	private long remainingdays;
	
	//added on 29/09/2022		
		@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		@JoinColumn(name = "global_org_id")
		private GlobalOrg globalorg;
		
		
		 //	added on 20/10/2022
		@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
		@JoinColumn(name = "sub_department_id")
		private SubDepartment subDepartment;
	
	// GENERATING GETTERS AND SETTERS

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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

	public long getRemainingdays() {
		return remainingdays;
	}

	public void setRemainingdays(long remainingdays) {
		this.remainingdays = remainingdays;
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
