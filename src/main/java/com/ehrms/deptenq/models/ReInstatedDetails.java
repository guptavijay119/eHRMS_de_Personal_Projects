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
@Table(schema = "departmentalenquiry", name = "reinstated_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ReInstatedDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "ReInstated_id_seq", name = "ReInstated_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ReInstated_id_seq")
	@Column(name = "reinstated_id")
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

	@Column(name = "last_name")
	private String lastName;

	
	@Column(name = "employee_reinstated", nullable = true)
	@ColumnDefault(value = "false")
	private boolean employeeReInstated;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "re_instated_order_date")
	private LocalDate reInstatedOrderDate;
	
	// added on 03/01/2023
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "date_of_suspension")
	private LocalDate dateofSuspension;
	
	
	@Column(name = "upload_order_reinstatment")
	private String UploadOrderReInstatment;
	
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
	
	@Column(name="with_de")
	@ColumnDefault(value="true")
	private boolean withde = false;
	
	@Column(name="file_no")
	private String fileNo;
	
	@Transient
	private String employeeDataId;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="employee_data_id")
	private EmployeeDetails empData;
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

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="from_user_id")
	private User fromUser;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="current_user_id")
	private User currentUser;
	
	 //added on 29/09/2022		
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "global_org_id")
	private GlobalOrg globalorg;
	
	
	//added on 20/10/2022		
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_department_id")
	private SubDepartment subDepartment;
	
	@Column(name="year")
	private Long year;
	@Column(name="month")
	private Long month;
	@Column(name="day")
	private Long day;
	
//	
//	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
//	private List<SuspensionDetails> suspensionList = new ArrayList<>();
//	
	
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

//	public List<SuspensionDetails> getSuspensionList() {
//		return suspensionList;
//	}
//
//	public void setSuspensionList(List<SuspensionDetails> suspensionList) {
//		this.suspensionList = suspensionList;
//	}

	public LocalDate getDateofSuspension() {
		return dateofSuspension;
	}

	public void setDateofSuspension(LocalDate dateofSuspension) {
		this.dateofSuspension = dateofSuspension;
	}


	
	
	
	
}
