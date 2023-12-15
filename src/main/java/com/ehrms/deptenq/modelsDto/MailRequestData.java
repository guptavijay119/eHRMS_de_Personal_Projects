package com.ehrms.deptenq.modelsDto;

import java.util.HashMap;
import java.util.Map;

public class MailRequestData {

private String from;
	
	private String to;
	
	private String contentType;
	
	private String bcc;
	
	private String subject;
	
	private String content;
	
	private String fileName;
	
	private byte[] data;
	
	private Map<String,FileData> mapFiles = new HashMap<>();
	
	public MailRequestData(String from, String to, String contentType, String bcc, String subject, String content,
			String fileName, byte[] data,Map<String,FileData> mapFiles) {
		this.from = from;
		this.to = to;
		this.contentType = contentType;
		this.bcc = bcc;
		this.subject = subject;
		this.content = content;
		this.fileName = fileName;
		this.data = data;
		this.mapFiles = mapFiles;
		
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Map<String, FileData> getMapFiles() {
		return mapFiles;
	}

	public void setMapFiles(Map<String, FileData> mapFiles) {
		this.mapFiles = mapFiles;
	}

}
