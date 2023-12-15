package com.ehrms.deptenq.utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	private FileUploadUtil() {
		
	}
	
	public static final String STATICDIRECTORY = "filerepository";
    
    public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save file: " + fileName, ioe);
        }      
    }
    
    public static void deleteFile(String uploadDir,String fileName) {
		try {
			Files.deleteIfExists(Paths.get(uploadDir).resolve(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}