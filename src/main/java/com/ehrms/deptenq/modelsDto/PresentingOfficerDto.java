package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.Designation;
import com.ehrms.deptenq.models.Division;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PresentingOfficerDto {

	private Long id;

	private CaseDetails casedetails;

	private boolean presentingOfficerAppointed;

	private String sevarthId;

	private Long employeeId;

	private String firstName;

	private String middleName;

	private String lastName;

	private GlobalOrg globalorg;

	private SubDepartment subDepartment;

	private Designation designation;

	private Long mobileNumber;

	private String email;
	
	private String otherDesignation;
	
	//added on 13-10-2022
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate appointmentDate;


	private Division revenueDivision;

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
	
	private String officerName;
	
	private String presentingOfficerDetail;

	
	// getters and setters Generating
	
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

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

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

	public String getOtherDesignation() {
		return otherDesignation;
	}

	public void setOtherDesignation(String otherDesignation) {
		this.otherDesignation = otherDesignation;
	}



}
