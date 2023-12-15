package com.ehrms.deptenq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ehrms.deptenq.modelsDto.FileUpload;



@Service
public class FileServiceImpl implements FileService{

	@Autowired
	private RestTemplate restTemplate;
	
//	private static final String FILESERVICEIP = "10.186.48.70";
	
	private static final String FILESERVICEIP = "localhost:8066/fileservice";

	
	public static final String FILESERVICEURLUPLOAD =  "http://"+FILESERVICEIP+"/uploadfile";
	
	public static final String FILESERVICEURLGET =  "http://"+FILESERVICEIP+"/getUploadFile";
	
	public static final String FILESERVICEURLREMOVE =  "http://"+FILESERVICEIP+"/removeUploadFile";
	
	@Override
	public boolean fileUpload(String uploadPath, MultipartFile file,String fileName) {
		try {
			FileUpload up = new FileUpload();
			up.setUploadpath(uploadPath);
			if(fileName != null) {
				up.setFileName(fileName);
			} else {
				up.setFileName(file.getOriginalFilename());
			}
			up.setContentType(file.getContentType());
			up.setFilebytes(file.getBytes());
			restTemplate.postForEntity(FILESERVICEURLUPLOAD, up, Boolean.class);
			return true;
		} catch(Exception a) {
			return false;
		}
	}

	@Override
	public ByteArrayResource getFile(String uploadPath, String fileName) {
		FileUpload up = new FileUpload();
		up.setUploadpath(uploadPath);
		up.setFileName(fileName);
		ResponseEntity<ByteArrayResource> data = restTemplate.postForEntity(FILESERVICEURLGET, up, ByteArrayResource.class);
		return data.getBody();
	}

	@Override
	public boolean removefile(String uploadPath, String fileName) {
		FileUpload up = new FileUpload();
		up.setUploadpath(uploadPath);
		up.setFileName(fileName);
		restTemplate.postForEntity(FILESERVICEURLREMOVE, up, Boolean.class);
		return true;	

	}

}
