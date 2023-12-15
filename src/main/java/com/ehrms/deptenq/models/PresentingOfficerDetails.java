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
@Table(schema = "departmentalenquiry", name = "presenting_officer_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PresentingOfficerDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "presentingofficer_id_seq", name = "presentingofficer_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "presentingofficer_id_seq")
	@Column(name = "presentingofficer_id")
	private Long id;

	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;
	
	
	@Column(name = "presenting_officer_appointed", nullable = true)
	@ColumnDefault(value = "false")
	private boolean presentingOfficerAppointed;
	
	
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
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "department_name")
	private GlobalOrg globalorg;
	

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_department")
	private SubDepartment subDepartment;
	
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "designation")
	private Designation designation;
	
	@Column(name = "other_designation")
	private String otherDesignation;
	
	@Column(name = "mobile_number")
	private Long mobileNumber;

	@Column(name = "email")
	private String email;
	
	 @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	   @JoinColumn(name="revenue_division")
//	@Column(name = "revenue_division")
	private Division revenueDivision;
	 
	 
	 @Column(name = "presenting_officer_detail")
		private String presentingOfficerDetail;
	
	/**
	 * @return the presentingOfficerDetail
	 */
	public String getPresentingOfficerDetail() {
		return presentingOfficerDetail;
	}

	/**
	 * @param presentingOfficerDetail the presentingOfficerDetail to set
	 */
	public void setPresentingOfficerDetail(String presentingOfficerDetail) {
		this.presentingOfficerDetail = presentingOfficerDetail;
	}

	@Column(name = "file_name_mr")
	private String fileNameMr;

	@Column(name = "file_name_en")
	private String fileNameEn;

	@Transient
	private String regionalText;

	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;
	
	//added on 13-10-2022
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name = "appointment_date")
	private LocalDate appointmentDate;
	
	@Transient
	private String caseNo;
	
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
	
	
	@Column(name = "office_name")
	private String officerName;
	
	
	/*
	 * //added on 29/09/2022
	 * 
	 * @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "global_org_id") private GlobalOrg globalorg;
	 */
	
	// getters and setters
	

	/**
	 * @return the officerName
	 */
	public String getOfficerName() {
		return officerName;
	}

	/**
	 * @param officerName the officerName to set
	 */
	public void setOfficerName(String officerName) {
		this.officerName = officerName;
	}

	public Long getId() {
		return id;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public boolean isPresentingOfficerAppointed() {
		return presentingOfficerAppointed;
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

	public GlobalOrg getGlobalorg() {
		return globalorg;
	}

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public Designation getDesignation() {
		return designation;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public String getEmail() {
		return email;
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

	public void setPresentingOfficerAppointed(boolean presentingOfficerAppointed) {
		this.presentingOfficerAppointed = presentingOfficerAppointed;
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

	public void setGlobalorg(GlobalOrg globalorg) {
		this.globalorg = globalorg;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setEmail(String email) {
		this.email = email;
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

	/**
	 * @return the revenueDivision
	 */
	public Division getRevenueDivision() {
		return revenueDivision;
	}

	/**
	 * @param revenueDivision the revenueDivision to set
	 */
	public void setRevenueDivision(Division revenueDivision) {
		this.revenueDivision = revenueDivision;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getOtherDesignation() {
		return otherDesignation;
	}

	public void setOtherDesignation(String otherDesignation) {
		this.otherDesignation = otherDesignation;
	}
	
	


	
	
	
	
	
	
	
	
	
	
	
}
