package com.ehrms.deptenq.modelsDto;

public class MailDTO {

	private Long id;

	private String mailTo;
	
	private String mailSubject;

	private String mailBody;
	
	private Long chargesheetDetailId;

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

	public Long getChargesheetDetailId() {
		return chargesheetDetailId;
	}

	public void setChargesheetDetailId(Long chargesheetDetailId) {
		this.chargesheetDetailId = chargesheetDetailId;
	}

	

	
}
