package com.ehrms.deptenq.models;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;




@Entity
@Table(schema = "departmentalenquiry", name = "case_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CaseDetails extends Auditable<String>{

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "case_id_seq", name = "case_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "case_id_seq")
	@Column(name = "case_id")
	private Long id;

	@Column(name = "case_no", nullable = true)
	private String caseNo;
	
	@Column(name = "previous_case_no")
	private String previousCaseNo;
	
	@Column(name = "transfer_status")
	private String transferStatus;
	
	@Column(name = "active_for_transfer")
	@ColumnDefault(value = "false")
	private boolean activeForTransfer;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "previous_global_org_id")
	private GlobalOrg previousGlobalOrg;
	
	@Column(name = "reasong_for_transfer")
	private String reasonForTransfer;
	
	@Column(name = "filename_transfer_approval")
	private String fileNameTransferApproval;
	
	@Column(name = "filepath_transfer_approval")
	private String filePathTransferApproval;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "previous_global_org_id")
	@JoinColumn(name = "case_transferred_to_user")
	private User caseTransferredTo;
	

	@Column(name = "case_filed_date")
	private String casefiledDate;
	
	@Column(name = "file_subject")
	private String fileSubject;
	
	@Column(name = "total_no_of_employee")
	private String totalNoOfEmployee;
	
	
	@Transient
	private List<String> employeeNames;
	
	

	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="misconduct_type")
	private MisconductTypesMaster misConductType;
	
	@OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name="rule_applicable")
	private RulesApplicableMaster ruleApplicable;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="employee_type")
	private EmployeeType employeeType;

	@Column(name = "inquiry_officer_appointed")
	private boolean inquiryOfficerAppointed;

	@Column(name = "charge_sheet_filed")
	private boolean chargeSheetfiled;

	@Column(name = "defense_statement_received")
	private boolean defenseStatementReceived;

	@Column(name = "eo_report_submitted")
	private boolean eoReportSubmitted;

	@Column(name = "case_status")
	private Long caseStatus;

	@Column(name = "any_court_matter_associated")
	private boolean anyCourtMatterAssociated;

	@Column(name = "case_summary")
	private String caseSummary;

	@Column(name = "remark")
	private String remark;

	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;
	
	@Column(name = "approved")
	@ColumnDefault(value = "false")
	private boolean approved;
	
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
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "global_org_id")
	private GlobalOrg globalorg;
	
	@Transient
	private String chargesheetDate;
	
	@Transient
	private String durationOfCase;
	
	@Transient
	private String inquiryofficername;
	
	@Transient
	private String presentingofficerdivision;
	
	@Transient
	private boolean finaloutcomestatus;
	
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "casedetails")
	@Fetch(FetchMode.SELECT)
	private List<EmployeeDetails> employeeList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "caseDetails")
	@OrderBy("chargesheetDate ASC")
	private List<ChargesheetDetails> chargeSheetList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
	private List<SuspensionDetails> suspensionList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
	private List<ReInstatedDetails> reinstatedList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
	private List<InquiryOfficerDetails> ioList = new ArrayList<>();
	
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
	private List<PresentingOfficerDetails> poList = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
	private List<ProsecutionProposalDetails> prosecutionProposal = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
	private List<CourtCaseDetails> courtCaseList = new ArrayList<>();
	
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "casedetails")
	private List<DetailsKeptAbeyanceCases> abeyanceList = new ArrayList<>();

	
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "casedetails")
	@Fetch(FetchMode.SELECT)
	private Set<FinalOutcomeDetails> finaloutList = new HashSet<>();
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_department_id")
	private SubDepartment subDepartment;
	
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "casedetails")
//	@Where(clause = "order by receivedDate desc")
	private List<IORequestTransaction> iorequest = new ArrayList<>();
	
	
	/* added on 15/11/2022 as per de Requirements */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "district_id")
	private Districts district;
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "division_id")
	private Division division;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_rule_id")
	private SubRuleApplicableMaster subRule;
	
	@Column(name = "whether_charge_sheet_issued")
	@ColumnDefault(value = "false")
	private boolean whetherChargesheetIssued;
	
	@Column(name = "pending_with_whome")
	@ColumnDefault(value = "Pending with department")
	private String pendingWith = "Pending with department";
	
	
	@Column(name = "other_than_gov_serv")
	private String otherThanGovServ;
	
	
	@Column(name="case_extension_file_name")
	private String caseExtensionFileName;
	
	@Column(name="case_extension_file_path")
	private String caseExtensionFilePath;
	

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "file_path")
	private String filepath;
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

	/**
	 * @return the employeeNames
	 */
	public List<String> getEmployeeNames() {
		return employeeNames;
	}

	/**
	 * @param employeeNames the employeeNames to set
	 */
	public void setEmployeeNames(List<String> employeeNames) {
		this.employeeNames = employeeNames;
	}

	/**
	 * @return the chargesheetDate
	 */
	public String getChargesheetDate() {
		return chargesheetDate;
	}

	/**
	 * @param chargesheetDate the chargesheetDate to set
	 */
	public void setChargesheetDate(String chargesheetDate) {
		this.chargesheetDate = chargesheetDate;
	}

	public List<ChargesheetDetails> getChargeSheetList() {
//		return this.chargeSheetList.stream().filter(ChargesheetDetails::getChargesheetIssued).collect(Collectors.toList());
		if(!this.chargeSheetList.isEmpty() && this.chargeSheetList.get(0).getChargesheetDate() != null) {
			this.chargesheetDate = this.chargeSheetList.get(0).getChargesheetDate().toString();
		}
		return chargeSheetList;
	}

	public void setChargeSheetList(List<ChargesheetDetails> chargeSheetList) {
		this.chargeSheetList = chargeSheetList;
	}

	public List<EmployeeDetails> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<EmployeeDetails> employeeList) {
		this.employeeList = employeeList;
	}

	public String getInquiryofficername() {
		return inquiryofficername;
	}

	public void setInquiryofficername(String inquiryofficername) {
		this.inquiryofficername = inquiryofficername;
	}

	public String getPresentingofficerdivision() {
		return presentingofficerdivision;
	}

	public void setPresentingofficerdivision(String presentingofficerdivision) {
		this.presentingofficerdivision = presentingofficerdivision;
	}

	public boolean isFinaloutcomestatus() {
		return finaloutcomestatus;
	}

	public void setFinaloutcomestatus(boolean finaloutcomestatus) {
		this.finaloutcomestatus = finaloutcomestatus;
	}

	public List<InquiryOfficerDetails> getIoList() {
		return this.ioList.stream().filter(InquiryOfficerDetails::isInquiryOfficerAppointed).collect(Collectors.toList());
	}

	public void setIoList(List<InquiryOfficerDetails> ioList) {
		this.ioList =ioList ;
	}

	public List<PresentingOfficerDetails> getPoList() {
		return this.poList.stream().filter(PresentingOfficerDetails::isPresentingOfficerAppointed).collect(Collectors.toList());
	}

	public void setPoList(List<PresentingOfficerDetails> poList) {
		this.poList = poList;
	}

	/**
	 * @return the finaloutList
	 */
	public Set<FinalOutcomeDetails> getFinaloutList() {
//		return this.finaloutList.stream().filter(FinalOutcomeDetails::isInquiryReportReceived).collect(Collectors.toList());
		return finaloutList;
	}

	/**
	 * @param finaloutList the finaloutList to set
	 */
	public void setFinaloutList(Set<FinalOutcomeDetails> finaloutList) {
		this.finaloutList = finaloutList;
	}

	/**
	 * @return the subDepartment
	 */
	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	/**
	 * @param subDepartment the subDepartment to set
	 */
	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * @return the suspensionList
	 */
	public List<SuspensionDetails> getSuspensionList() {
		return this.suspensionList.stream().filter(SuspensionDetails::isEmployeeSuspended).collect(Collectors.toList());
	}

	/**
	 * @param suspensionList the suspensionList to set
	 */
	public void setSuspensionList(List<SuspensionDetails> suspensionList) {
		this.suspensionList = suspensionList;
	}

	/**
	 * @return the reinstatedList
	 */
	public List<ReInstatedDetails> getReinstatedList() {
		return this.reinstatedList.stream().filter(ReInstatedDetails::isEmployeeReInstated).collect(Collectors.toList());
	}

	/**
	 * @param reinstatedList the reinstatedList to set
	 */
	public void setReinstatedList(List<ReInstatedDetails> reinstatedList) {
		this.reinstatedList = reinstatedList;
	}

	/**
	 * @return the prosecutionProposal
	 */
	public List<ProsecutionProposalDetails> getProsecutionProposal() {
		return this.prosecutionProposal.stream().filter(ProsecutionProposalDetails::isProsecutionProposalReceived).collect(Collectors.toList());
	}

	/**
	 * @param prosecutionProposal the prosecutionProposal to set
	 */
	public void setProsecutionProposal(List<ProsecutionProposalDetails> prosecutionProposal) {
		this.prosecutionProposal = prosecutionProposal;
	}

	/**
	 * @return the courtCaseList
	 */
	public List<CourtCaseDetails> getCourtCaseList() {
		return this.courtCaseList.stream().filter(CourtCaseDetails::isAnyRelatedCourtCaseExist).collect(Collectors.toList());
	}

	/**
	 * @param courtCaseList the courtCaseList to set
	 */
	public void setCourtCaseList(List<CourtCaseDetails> courtCaseList) {
		this.courtCaseList = courtCaseList;
	}

	/**
	 * @return the abeyanceList
	 */
	public List<DetailsKeptAbeyanceCases> getAbeyanceList() {
		return this.abeyanceList.stream().filter(DetailsKeptAbeyanceCases::isCasesKeptinAbeyance).collect(Collectors.toList());
	}

	/**
	 * @param abeyanceList the abeyanceList to set
	 */
	public void setAbeyanceList(List<DetailsKeptAbeyanceCases> abeyanceList) {
		this.abeyanceList = abeyanceList;
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

	public Districts getDistrict() {
		return district;
	}

	public void setDistrict(Districts district) {
		this.district = district;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
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

	public void setFilePathTransferApproval(String filePathTransferApproval) {
		this.filePathTransferApproval = filePathTransferApproval;
	}

	public User getCaseTransferredTo() {
		return caseTransferredTo;
	}

	public void setCaseTransferredTo(User caseTransferredTo) {
		this.caseTransferredTo = caseTransferredTo;
	}

	public String getCaseExtensionFileName() {
		return caseExtensionFileName;
	}

	public void setCaseExtensionFileName(String caseExtensionFileName) {
		this.caseExtensionFileName = caseExtensionFileName;
	}

	public String getCaseExtensionFilePath() {
		return caseExtensionFilePath;
	}

	public void setCaseExtensionFilePath(String caseExtensionFilePath) {
		this.caseExtensionFilePath = caseExtensionFilePath;
	}

	public String getDurationOfCase() {
//		System.out.println(this.getChargeSheetList());
		if(this.chargesheetDate != null) {
			LocalDate chargesheetdate = LocalDate.parse(this.chargesheetDate);
			Period pp = Period.between(chargesheetdate, LocalDate.now());
			this.durationOfCase = pp.getYears()+" Years, "+pp.getMonths()+" Months, "+pp.getDays()+" Days";
		} else {
			this.durationOfCase = "";
		}
		return this.durationOfCase;
	}

	public void setDurationOfCase(String durationOfCase) {
		this.durationOfCase = durationOfCase;
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

	public List<IORequestTransaction> getIorequest() {
		List<IORequestTransaction> io = iorequest;
		io.sort((a2,a1)->a2.getReceivedDate().compareTo(a1.getReceivedDate()));
		return io;
	}

	public void setIorequest(List<IORequestTransaction> iorequest) {
		this.iorequest = iorequest;
	}

}
