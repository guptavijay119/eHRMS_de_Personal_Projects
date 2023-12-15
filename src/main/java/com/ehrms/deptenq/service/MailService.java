package com.ehrms.deptenq.service;

import java.util.Map;

import com.ehrms.deptenq.modelsDto.FileData;                             

public interface MailService {

	void sendEmailMultipleFiles(String from, String to, String bcc, String subject, String content,
			Map<String, FileData> mapFiles);

	void sendEmail(String from, String to, String bcc, String subject, String content, String fileName, byte[] data,
			String contentType);
}
