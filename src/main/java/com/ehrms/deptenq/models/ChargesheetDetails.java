package com.ehrms.deptenq.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(schema = "departmentalenquiry", name = "chargesheet_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ChargesheetDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "chargesheet_id_seq", name = "chargesheet_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chargesheet_id_seq")
	@Column(name = "chargesheet_id")
	private Long id;

	@Column(name = "chargesheet_issued", nullable = true)
	@ColumnDefault(value = "false")
	private boolean chargesheetIssued;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "chargesheet_date")
	private LocalDate chargesheetDate;

	@Column(name = "sevarth_id")
	private String sevarthId;

	@Column(name = "employee_id", nullable = true)
	private Long employeeId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "file_path")
	private String filepath;

	@Column(name = "file_name_mr")
	private String fileNameMr;

	@Column(name = "file_name_en")
	private String fileNameEn;

	@Transient
	private String regionalText;

	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails caseDetails;

	@Transient
	private String caseNo;

	@Transient
	private String employeeDataId;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="employee_data_id")
	private EmployeeDetails empData;
	
	
	@Column(name = "revised_chargesheet_issued", nullable = true)
	@ColumnDefault(value = "false")
	private boolean revisedChargesheetIssued;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "revised_chargesheet_date")
	private LocalDate revisedChargesheetDate;
	
	
	@Column(name = "revised_file_name")
	private String revisedfileName;

	@Column(name = "revised_content_type")
	private String revisedcontentType;

	@Column(name = "revised_file_path")
	private String revisedfilepath;
	
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "chargesheetDetails")
	private List<MailTransaction> mailTransaction = new ArrayList<>();
	
	
	
	public List<MailTransaction> getMailTransaction() {
		
		List<MailTransaction> dd = mailTransaction;
		if(dd != null && !dd.isEmpty()) {
			dd.sort((a2,a1)->a1.getSentDate().compareTo(a2.getSentDate()));
		}
		return dd;
//		return mailTransaction;
	}

	public void setMailTransaction(List<MailTransaction> mailTransaction) {
		this.mailTransaction = mailTransaction;
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

	// added on 18/10/2022
//	   @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
//	   private List<EmployeeDetails> employeeList = new ArrayList<>();

	// gen getters and setters

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

//	public List<EmployeeDetails> getEmployeeList() {
//		return employeeList;
//	}
//
//	public void setEmployeeList(List<EmployeeDetails> employeeList) {
//		this.employeeList = employeeList;
//	}

}
