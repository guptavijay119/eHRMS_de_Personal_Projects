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

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "departmentalenquiry", name = "io_request_transaction")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class IORequestTransaction extends Auditable<String>{
	
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "io_request_transaction_id_seq", name = "io_request_transaction_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "io_request_transaction_id_seq")
	@Column(name = "io_request_transaction_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "inquiry_officer_id")
	private InquiryOfficerList io;
	
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "io")
	private CaseHearing hearing;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "request_received_date")
	private LocalDate receivedDate;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "request_approval_date")
	private LocalDate approvalDate;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "request_rejected_date")
	private LocalDate rejectedDate;
	
	
	@Column(name = "request_status")
	private String requestStatus;
	
	@Column(name = "remark")
	private String remark;

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

	public InquiryOfficerList getIo() {
		return io;
	}

	public void setIo(InquiryOfficerList io) {
		this.io = io;
	}

	public LocalDate getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDate receivedDate) {
		this.receivedDate = receivedDate;
	}

	public LocalDate getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
	}

	public LocalDate getRejectedDate() {
		return rejectedDate;
	}

	public void setRejectedDate(LocalDate rejectedDate) {
		this.rejectedDate = rejectedDate;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public CaseHearing getHearing() {
		return hearing;
	}

	public void setHearing(CaseHearing hearing) {
		this.hearing = hearing;
	}



}
