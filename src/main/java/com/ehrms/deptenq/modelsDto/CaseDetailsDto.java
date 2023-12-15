package com.ehrms.deptenq.modelsDto;

import javax.persistence.Transient;

import com.ehrms.deptenq.models.Districts;
import com.ehrms.deptenq.models.Division;
import com.ehrms.deptenq.models.EmployeeType;
import com.ehrms.deptenq.models.GlobalOrg;
import com.ehrms.deptenq.models.MisconductTypesMaster;
import com.ehrms.deptenq.models.RulesApplicableMaster;
import com.ehrms.deptenq.models.SubDepartment;
import com.ehrms.deptenq.models.SubRuleApplicableMaster;
import com.ehrms.deptenq.models.User;

public class CaseDetailsDto {

	private Long id;
	
	private String caseNo;
	
	private String previousCaseNo;
	
	private String transferStatus;
	
	private boolean activeForTransfer;
	
	private GlobalOrg previousGlobalOrg;
	
	private String reasonForTransfer;
	
	private String fileNameTransferApproval;
	
	private String filePathTransferApproval;
	
	private User caseTransferredTo;
	
	private String casefiledDate;
	
	private MisconductTypesMaster misConductType;
	
	private RulesApplicableMaster ruleApplicable;
	
	private EmployeeType employeeType;
	
	private boolean inquiryOfficerAppointed;
	
	private boolean chargeSheetfiled;
	
	private boolean defenseStatementReceived;
	
	private boolean eoReportSubmitted;
	
	private Long caseStatus;
	
	private boolean anyCourtMatterAssociated;
	
	private String caseSummary;
	
	private String remark;
	
	private String fileSubject;

	private String totalNoOfEmployee;
	
	private boolean active;
	
	@Transient
	private long forwardToUser;

	private boolean approved;

	private User fromUser;


	private User currentUser;
	
	
	/* added on 15/11/2022 as per de Requirements */
	private Division division;
	private Districts district;
	
	//added on 29/09/2022
	private GlobalOrg globalorg;
	
	//added on 20/10/2022
     private SubDepartment subDepartment;
     
     
     private SubRuleApplicableMaster subRule;
     
     private boolean whetherChargesheetIssued;

     private String pendingWith;

     private String otherThanGovServ;
     
	/**
	 * @return the whetherChargesheetIssued
	 */
	public boolean isWhetherChargesheetIssued() {
		return whetherChargesheetIssued;
	}

	/**
	 * @param whetherChargesheetIssued the whetherChargesheetIssued to set
	 */
	public void setWhetherChargesheetIssued(boolean whetherChargesheetIssued) {
		this.whetherChargesheetIssued = whetherChargesheetIssued;
	}

	/**
	 * @return the subRule
	 */
	public SubRuleApplicableMaster getSubRule() {
		return subRule;
	}

	/**
	 * @param subRule the subRule to set
	 */
	public void setSubRule(SubRuleApplicableMaster subRule) {
		this.subRule = subRule;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCasefiledDate() {
		return casefiledDate;
	}

	public void setCasefiledDate(String casefiledDate) {
		this.casefiledDate = casefiledDate;
	}

	public MisconductTypesMaster getMisConductType() {
		return misConductType;
	}

	public void setMisConductType(MisconductTypesMaster misConductType) {
		this.misConductType = misConductType;
	}

	public RulesApplicableMaster getRuleApplicable() {
		return ruleApplicable;
	}

	public void setRuleApplicable(RulesApplicableMaster ruleApplicable) {
		this.ruleApplicable = ruleApplicable;
	}

	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}

	public boolean isInquiryOfficerAppointed() {
		return inquiryOfficerAppointed;
	}

	public void setInquiryOfficerAppointed(boolean inquiryOfficerAppointed) {
		this.inquiryOfficerAppointed = inquiryOfficerAppointed;
	}

	public boolean isChargeSheetfiled() {
		return chargeSheetfiled;
	}

	public void setChargeSheetfiled(boolean chargeSheetfiled) {
		this.chargeSheetfiled = chargeSheetfiled;
	}

	public boolean isDefenseStatementReceived() {
		return defenseStatementReceived;
	}

	public void setDefenseStatementReceived(boolean defenseStatementReceived) {
		this.defenseStatementReceived = defenseStatementReceived;
	}

	public boolean isEoReportSubmitted() {
		return eoReportSubmitted;
	}

	public void setEoReportSubmitted(boolean eoReportSubmitted) {
		this.eoReportSubmitted = eoReportSubmitted;
	}

	public Long getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(Long caseStatus) {
		this.caseStatus = caseStatus;
	}

	public boolean isAnyCourtMatterAssociated() {
		return anyCourtMatterAssociated;
	}

	public void setAnyCourtMatterAssociated(boolean anyCourtMatterAssociated) {
		this.anyCourtMatterAssociated = anyCourtMatterAssociated;
	}

	public String getCaseSummary() {
		return caseSummary;
	}

	public void setCaseSummary(String caseSummary) {
		this.caseSummary = caseSummary;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getFileSubject() {
		return fileSubject;
	}

	public void setFileSubject(String fileSubject) {
		this.fileSubject = fileSubject;
	}

	public String getTotalNoOfEmployee() {
		return totalNoOfEmployee;
	}

	public void setTotalNoOfEmployee(String totalNoOfEmployee) {
		this.totalNoOfEmployee = totalNoOfEmployee;
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
	 * @return the approved
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * @param approved the approved to set
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public Districts getDistrict() {
		return district;
	}

	public void setDistrict(Districts district) {
		this.district = district;
	}

	/**
	 * @return the pendingWith
	 */
	public String getPendingWith() {
		return pendingWith;
	}

	/**
	 * @param pendingWith the pendingWith to set
	 */
	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public String getOtherThanGovServ() {
		return otherThanGovServ;
	}

	public void setOtherThanGovServ(String otherThanGovServ) {
		this.otherThanGovServ = otherThanGovServ;
	}

	public String getPreviousCaseNo() {
		return previousCaseNo;
	}

	public void setPreviousCaseNo(String previousCaseNo) {
		this.previousCaseNo = previousCaseNo;
	}

	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	public boolean isActiveForTransfer() {
		return activeForTransfer;
	}

	public void setActiveForTransfer(boolean activeForTransfer) {
		this.activeForTransfer = activeForTransfer;
	}

	public GlobalOrg getPreviousGlobalOrg() {
		return previousGlobalOrg;
	}

	public void setPreviousGlobalOrg(GlobalOrg previousGlobalOrg) {
		this.previousGlobalOrg = previousGlobalOrg;
	}

	public String getReasonForTransfer() {
		return reasonForTransfer;
	}

	public void setReasonForTransfer(String reasonForTransfer) {
		this.reasonForTransfer = reasonForTransfer;
	}

	public String getFileNameTransferApproval() {
		return fileNameTransferApproval;
	}

	public void setFileNameTransferApproval(String fileNameTransferApproval) {
		this.fileNameTransferApproval = fileNameTransferApproval;
	}

	public String getFilePathTransferApproval() {
		return filePathTransferApproval;
	}

	public User getCaseTransferredTo() {
		return caseTransferredTo;
	}

	public void setCaseTransferredTo(User caseTransferredTo) {
		this.caseTransferredTo = caseTransferredTo;
	}

	public void setFilePathTransferApproval(String filePathTransferApproval) {
		this.filePathTransferApproval = filePathTransferApproval;
	}
	
}
