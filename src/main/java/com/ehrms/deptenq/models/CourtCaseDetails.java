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
@Table(schema = "departmentalenquiry", name = "court_case_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CourtCaseDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "courtcase_id_seq", name = "courtcase_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courtcase_id_seq")
	@Column(name = "courtcase_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;

	@Column(name = "any_related_court_case_exist", nullable = true)
	@ColumnDefault(value = "false")
	private boolean anyRelatedCourtCaseExist;

	@Column(name = "court_case_no", nullable = true)
	private String courtCaseNo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "court_case_filed_date")
	private LocalDate courtCaseFiledDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "court_type")
	private CourtName courtType;

	@Column(name = "status", nullable = true)
	private String status;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "date_of_final_order")
	private LocalDate dateOfFinalOrder;

	@Column(name = "upload_order")
	private String uploadOder;

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

	// adding Fields according to pdf

	@Column(name = "sevarth_Id")
	private String sevarthId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

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

	// added on 11/11/2022
	@Column(name = "court_name", nullable = true)
	private String courtName;
	
	@Transient
	private String employeeDataId;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="employee_data_id")
	private EmployeeDetails empData;

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
