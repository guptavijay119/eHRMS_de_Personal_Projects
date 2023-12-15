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
@Table(schema = "departmentalenquiry", name = "prosecution_proposal_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ProsecutionProposalDetails extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "prosecutionproposal_id_seq", name = "prosecutionproposal_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prosecutionproposal_id_seq")
	@Column(name = "prosecutionproposal_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;

	@Column(name = "prosecution_proposal_received", nullable = true)
	@ColumnDefault(value = "false")
	private boolean prosecutionProposalReceived;

	@Column(name = "date_of_receipt")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfReceipt;
	
	@Column(name = "date_of_chargesheet_in_court")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate courtChargeSheetDate;

	@Column(name = "status_for_granted_denied_pending", nullable = true)
	private String statusForGrantedDeniedPending;

	@Column(name = "date_of_order_receipt")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrderReceipt;

	@Column(name = "date_of_communication")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfCommunication;

	@Column(name = "file_name_upload_copy")
	private String fileNameUploadCopy;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "file_path")
	private String filepath;
	
	
	@Column(name = "final_court_order_copy")
	private String finalcourtordercopy;

	@Column(name = "content_type_final_court")
	private String contentTypefinalcourt;

	@Column(name = "file_path_final_court")
	private String filepathfinalcourt;

	@Column(name = "file_name_mr")
	private String fileNameMr;

	@Column(name = "file_name_en")
	private String fileNameEn;

	@Transient
	private String regionalText;

	@Column(name = "is_active")
	@ColumnDefault(value = "false")
	private boolean active;

	@Column(name = "whether_case_filed_in_court", nullable = true)
	@ColumnDefault(value = "false")
	private boolean whetherCaseFiledCourt;

	@Column(name = "date_of_order_case_filed")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfOrderCaseFiled;

	// adding column According to latest pdf

	@Column(name = "whether_case_filed_in_the_court", nullable = true)
	@ColumnDefault(value = "false")
	private boolean whetherCaseFiledIntheCourt;

	@Column(name = "court_case_status", nullable = true)
	private String status;
	
	@Column(name = "decision_court", nullable = true)
	private String decisionCourt;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "date_of_order_of_court")
	private LocalDate dateOfOrderoftheCourt;

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

	/* added on 18/10/2022 as per DE requirements */
	@Column(name = "remark", nullable = true, length = 1000)
	public String remark;

	// added on 20/10/2022
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sub_department_id")
	private SubDepartment subDepartment;

	@Column(name = "court_name")
	private String courtName;

	@Column(name = "court_case_no")
	private String courtCaseNo;

	/* added on 07/11/2022 */
	@Column(name = "file_no")
	private String fileNo;

	@Column(name = "with_de")
	@ColumnDefault(value = "true")
	private boolean withde = false;

	@Column(name = "sevarth_id", nullable = true)
	private String sevarthId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "service_group")
	private Service_Group servicegroup;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "designation")
	private Designation designation;
	
	@Column(name="processFlow")
	private String processFlow;
	
	@Transient
	private String employeeDataId;
	
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="employee_data_id")
	private EmployeeDetails empData;
	
//	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "caseDetails")
//	@OrderBy("chargesheetDate ASC")
//	private List<ChargesheetDetails> chargeSheetList = new ArrayList<>();
	
	
	// added on 03/01/2023
	@Column(name = "date_of_chargesheet_issued")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateofChargesheetIssued;
	
	

	// generating getter & setter

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
	 * @return the processFlow
	 */
	public String getProcessFlow() {
		return processFlow;
	}

	/**
	 * @param processFlow the processFlow to set
	 */
	public void setProcessFlow(String processFlow) {
		this.processFlow = processFlow;
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

	public Long getId() {
		return id;
	}

	public CaseDetails getCasedetails() {
		return casedetails;
	}

	public boolean isProsecutionProposalReceived() {
		return prosecutionProposalReceived;
	}

	public LocalDate getDateOfReceipt() {
		return dateOfReceipt;
	}

	public LocalDate getDateOfOrderReceipt() {
		return dateOfOrderReceipt;
	}

	public LocalDate getDateOfCommunication() {
		return dateOfCommunication;
	}

	public String getFileNameUploadCopy() {
		return fileNameUploadCopy;
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

	public boolean isWhetherCaseFiledCourt() {
		return whetherCaseFiledCourt;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getDateOfOrderCaseFiled() {
		return dateOfOrderCaseFiled;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCasedetails(CaseDetails casedetails) {
		this.casedetails = casedetails;
	}

	public void setProsecutionProposalReceived(boolean prosecutionProposalReceived) {
		this.prosecutionProposalReceived = prosecutionProposalReceived;
	}

	public void setDateOfReceipt(LocalDate dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public void setDateOfOrderReceipt(LocalDate dateOfOrderReceipt) {
		this.dateOfOrderReceipt = dateOfOrderReceipt;
	}

	public void setDateOfCommunication(LocalDate dateOfCommunication) {
		this.dateOfCommunication = dateOfCommunication;
	}

	public void setFileNameUploadCopy(String fileNameUploadCopy) {
		this.fileNameUploadCopy = fileNameUploadCopy;
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

	public void setWhetherCaseFiledCourt(boolean whetherCaseFiledCourt) {
		this.whetherCaseFiledCourt = whetherCaseFiledCourt;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDateOfOrderCaseFiled(LocalDate dateOfOrderCaseFiled) {
		this.dateOfOrderCaseFiled = dateOfOrderCaseFiled;
	}

	public String getStatusForGrantedDeniedPending() {
		return statusForGrantedDeniedPending;
	}

	public void setStatusForGrantedDeniedPending(String statusForGrantedDeniedPending) {
		this.statusForGrantedDeniedPending = statusForGrantedDeniedPending;
	}

	public boolean isWhetherCaseFiledIntheCourt() {
		return whetherCaseFiledIntheCourt;
	}

	public void setWhetherCaseFiledIntheCourt(boolean whetherCaseFiledIntheCourt) {
		this.whetherCaseFiledIntheCourt = whetherCaseFiledIntheCourt;
	}

	public LocalDate getDateOfOrderoftheCourt() {
		return dateOfOrderoftheCourt;
	}

	public void setDateOfOrderoftheCourt(LocalDate dateOfOrderoftheCourt) {
		this.dateOfOrderoftheCourt = dateOfOrderoftheCourt;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public SubDepartment getSubDepartment() {
		return subDepartment;
	}

	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment = subDepartment;
	}

	/**
	 * @return the courtName
	 */
	public String getCourtName() {
		return courtName;
	}

	/**
	 * @param courtName the courtName to set
	 */
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	/**
	 * @return the courtCaseNo
	 */
	public String getCourtCaseNo() {
		return courtCaseNo;
	}

	/**
	 * @param courtCaseNo the courtCaseNo to set
	 */
	public void setCourtCaseNo(String courtCaseNo) {
		this.courtCaseNo = courtCaseNo;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
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
	 * @return the decisionCourt
	 */
	public String getDecisionCourt() {
		return decisionCourt;
	}

	/**
	 * @param decisionCourt the decisionCourt to set
	 */
	public void setDecisionCourt(String decisionCourt) {
		this.decisionCourt = decisionCourt;
	}

	/**
	 * @return the finalcourtordercopy
	 */
	public String getFinalcourtordercopy() {
		return finalcourtordercopy;
	}

	/**
	 * @param finalcourtordercopy the finalcourtordercopy to set
	 */
	public void setFinalcourtordercopy(String finalcourtordercopy) {
		this.finalcourtordercopy = finalcourtordercopy;
	}

	/**
	 * @return the contentTypefinalcourt
	 */
	public String getContentTypefinalcourt() {
		return contentTypefinalcourt;
	}

	/**
	 * @param contentTypefinalcourt the contentTypefinalcourt to set
	 */
	public void setContentTypefinalcourt(String contentTypefinalcourt) {
		this.contentTypefinalcourt = contentTypefinalcourt;
	}

	/**
	 * @return the filepathfinalcourt
	 */
	public String getFilepathfinalcourt() {
		return filepathfinalcourt;
	}

	/**
	 * @param filepathfinalcourt the filepathfinalcourt to set
	 */
	public void setFilepathfinalcourt(String filepathfinalcourt) {
		this.filepathfinalcourt = filepathfinalcourt;
	}

//	public List<ChargesheetDetails> getChargeSheetList() {
//		return chargeSheetList;
//	}
//
//	public void setChargeSheetList(List<ChargesheetDetails> chargeSheetList) {
//		this.chargeSheetList = chargeSheetList;
//	}

	public LocalDate getDateofChargesheetIssued() {
		return dateofChargesheetIssued;
	}

	public void setDateofChargesheetIssued(LocalDate dateofChargesheetIssued) {
		this.dateofChargesheetIssued = dateofChargesheetIssued;
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

	public LocalDate getCourtChargeSheetDate() {
		return courtChargeSheetDate;
	}

	public void setCourtChargeSheetDate(LocalDate courtChargeSheetDate) {
		this.courtChargeSheetDate = courtChargeSheetDate;
	}

}
