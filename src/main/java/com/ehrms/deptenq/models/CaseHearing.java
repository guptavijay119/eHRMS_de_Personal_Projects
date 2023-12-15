package com.ehrms.deptenq.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "departmentalenquiry", name = "case_hearing")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CaseHearing extends Auditable<String>{
	
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "case_hearing_id_seq", name = "case_hearing_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "case_hearing_id_seq")
	@Column(name = "case_hearing_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_details_id")
	private CaseDetails casedetails;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "inquiry_request_id")
	private IORequestTransaction io;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "hearing")
//	@Where(clause = "order by hearingDate desc")
	private List<HearingDetails> hearingDetails ;
	
	
	@Transient
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate hearingDate;

	@Transient
	private String weblink;
	
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


	public IORequestTransaction getIo() {
		return io;
	}

	public void setIo(IORequestTransaction io) {
		this.io = io;
	}

	public List<HearingDetails> getHearingDetails() {
		List<HearingDetails> dd = hearingDetails;
		if(dd != null && !dd.isEmpty()) {
			dd.sort((a2,a1)->a2.getHearingDate().compareTo(a1.getHearingDate()));
		}
		return dd;
	}

	public void setHearingDetails(List<HearingDetails> hearingDetails) {
		this.hearingDetails = hearingDetails;
	}

	public LocalDate getHearingDate() {
		return hearingDate;
	}

	public void setHearingDate(LocalDate hearingDate) {
		this.hearingDate = hearingDate;
	}

	public String getWeblink() {
		return weblink;
	}

	public void setWeblink(String weblink) {
		this.weblink = weblink;
	}
	

}
