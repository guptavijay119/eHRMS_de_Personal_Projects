package com.ehrms.deptenq.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	boolean fileUpload(String uploadPath,MultipartFile file,String fileName);
	
	ByteArrayResource getFile(String uploadPath,String fileName);
	
	boolean removefile(String uploadPath,String fileName);

}
