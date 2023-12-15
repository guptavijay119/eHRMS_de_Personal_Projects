package com.ehrms.deptenq.modelsDto;

import org.springframework.web.multipart.MultipartFile;

public class CaseTransferDto {
	
	private long id;
	
	private String reason;
	
	private long globalorg;
	
	private String otherThanGovServ;
	
	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getOtherThanGovServ() {
		return otherThanGovServ;
	}

	public void setOtherThanGovServ(String otherThanGovServ) {
		this.otherThanGovServ = otherThanGovServ;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getGlobalorg() {
		return globalorg;
	}

	public void setGlobalorg(long globalorg) {
		this.globalorg = globalorg;
	}
	
	

}
