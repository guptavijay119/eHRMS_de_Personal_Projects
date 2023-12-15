package com.ehrms.deptenq.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ehrms.deptenq.modelsDto.FileData;
import com.ehrms.deptenq.modelsDto.MailRequestData;

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final String MAILSERVICEIP = "localhost:8088/mailservice";
	
	public static final String MAILSERVICEURLUPLOAD =  "http://"+MAILSERVICEIP+"/mail/sent";

	@Async
	public void sendEmailMultipleFiles(String from, String to, String bcc,String subject, String content, Map<String, FileData> mapFiles) {
		
		try {
					MailRequestData request = new MailRequestData(from,to,null,bcc,subject,content,null,null,mapFiles);
					restTemplate.postForEntity(MAILSERVICEURLUPLOAD, request, Boolean.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}		
	}
	
	@Async
	@Override
	public void sendEmail(String from, String to, String bcc,String subject, String content, String fileName,byte[] data,String contentType) {
		
		try {
					MailRequestData request = new MailRequestData(from,to,contentType,bcc,subject,content,fileName,data,null);
					restTemplate.postForEntity(MAILSERVICEURLUPLOAD, request, Boolean.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}		
	}

}
