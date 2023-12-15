package com.ehrms.deptenq.modelsDto;

import javax.persistence.Transient;

import com.ehrms.deptenq.models.CaseDetails;
import com.ehrms.deptenq.models.EmployeeDetails;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.ReasonForKeepingCaseAbeyance;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.User;

public class DetailsKeptAbeyanceCasesDto {

	private Long id;

	private CaseDetails casedetails;

	private String sevarthId;

	private Long employeeId;

	private String firstName;

	private String middleName;

	private String lastName;

	private boolean casesKeptinAbeyance;

	private ReasonForKeepingCaseAbeyance reasonreasonKeepingInAbeyance;

	private boolean active;

	@Transient
	private String caseNo;

	@Transient
	private long forwardToUser;

	private User fromUser;

	private User currentUser;

	// added on 29/09/2022
	private GlobalOrg globalorg;

	// added on 20/10/2022 as per DE requirements...
	private SubDepartment subDepartment;

	private EmployeeDetails empData;

	@Transient
	private String employeeDataId;

	// generating getter and setters..

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

	public boolean isCasesKeptinAbeyance() {
		return casesKeptinAbeyance;
	}

	public void setCasesKeptinAbeyance(boolean casesKeptinAbeyance) {
		this.casesKeptinAbeyance = casesKeptinAbeyance;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ReasonForKeepingCaseAbeyance getReasonreasonKeepingInAbeyance() {
		return reasonreasonKeepingInAbeyance;
	}

	public void setReasonreasonKeepingInAbeyance(ReasonForKeepingCaseAbeyance reasonreasonKeepingInAbeyance) {
		this.reasonreasonKeepingInAbeyance = reasonreasonKeepingInAbeyance;
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

}
