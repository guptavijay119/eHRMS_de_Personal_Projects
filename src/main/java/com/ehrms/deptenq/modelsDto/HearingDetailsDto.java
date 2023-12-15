package com.ehrms.deptenq.modelsDto;

import org.springframework.web.multipart.MultipartFile;

public class HearingDetailsDto {
	
	private String id;
	
	private String hearingStatus;
	
	private String hearingSummery;
	private String hearingSubject;
	
	private String hearingDate;
	
	public String getHearingSubject() {
		return hearingSubject;
	}

	public void setHearingSubject(String hearingSubject) {
		this.hearingSubject = hearingSubject;
	}

	private MultipartFile file;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getHearingDate() {
		return hearingDate;
	}

	public void setHearingDate(String hearingDate) {
		this.hearingDate = hearingDate;
	}

}
