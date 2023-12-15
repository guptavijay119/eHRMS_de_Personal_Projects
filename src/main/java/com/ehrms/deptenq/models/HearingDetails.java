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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "departmentalenquiry", name = "hearing_details")
@Audited
@DynamicUpdate(true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class HearingDetails extends Auditable<String>{
	
	@Id
	@SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "hearing_details_id_seq", name = "hearing_details_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hearing_details_id_seq")
	@Column(name = "hearing_details_id")
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name="case_hearing_id")
	private CaseHearing hearing;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name="hearing_date")
	private LocalDate hearingDate;
	
	@Column(name="hearing_weblink")
	private String webLink;
	
	@Column(name="hearing_status")
	private String hearingStatus;
	
	@Column(name="hearing_summery")
	private String hearingSummery;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "hearing")
	private List<HearingParticipants> hearingParticipants ;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="file_path")
	private String filePath;
	
	
	@Column(name="content_type")
	private String contentType;
	
	
	@Column(name="hearing_notice")
	private String hearingNotice;
	
	@Column(name="hearing_subject")
	private String hearingSubject;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name="notice_sent_date")
	private LocalDate noticeSentDate;
	
	
	@Column(name="hearing_notice_sent")
	@ColumnDefault("false")
	private boolean hearingNoticeSent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CaseHearing getHearing() {
		return hearing;
	}

	public void setHearing(CaseHearing hearing) {
		this.hearing = hearing;
	}

	public LocalDate getHearingDate() {
		return hearingDate;
	}

	public void setHearingDate(LocalDate hearingDate) {
		this.hearingDate = hearingDate;
	}

	public String getWebLink() {
		return webLink;
	}

	public void setWebLink(String webLink) {
		this.webLink = webLink;
	}

	public String getHearingStatus() {
		return hearingStatus;
	}

	public void setHearingStatus(String hearingStatus) {
		this.hearingStatus = hearingStatus;
	}

	public String getHearingSummery() {
		return hearingSummery;
	}

	public void setHearingSummery(String hearingSummery) {
		this.hearingSummery = hearingSummery;
	}

	public List<HearingParticipants> getHearingParticipants() {
		return hearingParticipants;
	}

	public void setHearingParticipants(List<HearingParticipants> hearingParticipants) {
		this.hearingParticipants = hearingParticipants;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getHearingNotice() {
		return hearingNotice;
	}

	public void setHearingNotice(String hearingNotice) {
		this.hearingNotice = hearingNotice;
	}

	public boolean getHearingNoticeSent() {
		return hearingNoticeSent;
	}

	public void setHearingNoticeSent(boolean hearingNoticeSent) {
		this.hearingNoticeSent = hearingNoticeSent;
	}

	public String getHearingSubject() {
		return hearingSubject;
	}

	public void setHearingSubject(String hearingSubject) {
		this.hearingSubject = hearingSubject;
	}

	public LocalDate getNoticeSentDate() {
		return noticeSentDate;
	}

	public void setNoticeSentDate(LocalDate noticeSentDate) {
		this.noticeSentDate = noticeSentDate;
	}

}
