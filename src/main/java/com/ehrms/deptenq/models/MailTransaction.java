package com.ehrms.deptenq.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(schema = "departmentalenquiry", name = "mail_transaction")
@DynamicUpdate(true)
@Audited
@AuditOverride(forClass = Auditable.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MailTransaction extends Auditable<String> {

	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="mail_id_seq", name="mail_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "mail_id_seq")
	@Column(name="mail_id")
	private Long id;

	@Column(name = "mail_to")
	private String mailTo;
	
	@Column(name = "mail_subject", length = 10485760)
	private String mailSubject;

	@Column(name = "mail_body", length = 10485760)
	private String mailBody;
	
	@Column(name="file_name")
	private String fileName;

	@Column(name="file_path")
	private String filePath;
	
	@Column(name="content_type")
	private String contentType;
	
	@Column(name="sent_date")
	private LocalDateTime sentDate;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "chargesheet_details_id")
	private ChargesheetDetails chargesheetDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
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

	public ChargesheetDetails getChargesheetDetails() {
		return chargesheetDetails;
	}

	public void setChargesheetDetails(ChargesheetDetails chargesheetDetails) {
		this.chargesheetDetails = chargesheetDetails;
	}

	public LocalDateTime getSentDate() {
		return sentDate;
	}

	public void setSentDate(LocalDateTime sentDate) {
		this.sentDate = sentDate;
	}


	

	
}
