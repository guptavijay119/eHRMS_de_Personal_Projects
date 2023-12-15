package com.ehrms.deptenq.modelsDto;

import java.time.LocalDate;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;


public class CaseHearingDto {
	

	private Long id;
	

	
	@Transient
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate hearingDate;

	@Transient
	private String weblink;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
