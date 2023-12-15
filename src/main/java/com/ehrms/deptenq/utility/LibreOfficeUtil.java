package com.ehrms.deptenq.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class LibreOfficeUtil {
	    
    private static final String LIBER_OFFICE_HOME = "C:/Program Files/LibreOffice/program/";
	private static final String TEMPORARY_PATH = "temp/";
    private static final String FILE_DOWNLOAD_PATH = "file/";
    private static final String FILE_PREVIEW_PATH = "preview/";
    private static final String ENCODEING_UTF8 = "UTF-8";
    
    
//    public static void main(String[] args) throws IOException {
//        System.out.println(convert("a.docx", "pdf"));
//    }

 
    public static void convert(String html) throws IOException {
        //The relative directory of the preview file
//        String targetDir = DateTimeUtil.getCurrentTimeDate() + "/" + String.valueOf(System.currentTimeMillis()) + "/";
//        String previewAbsolutelyDir = FILE_PREVIEW_PATH + targetDir;
        String uuid = UUID.randomUUID().toString();
        String directory = "file/temp/";
        createDir(directory);
        String fileName= uuid+"_temp.html";
      
        File f = new File(directory+fileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write(html);
        bw.close();
        StringBuffer url = new StringBuffer(f.toURI().toURL().toString());
        String urlmod = StringUtils.replace(url.toString(), "file:/", "");
        String urlmod2 = StringUtils.remove(urlmod, fileName);
        StringBuffer command = new StringBuffer().append(LIBER_OFFICE_HOME).append("soffice --headless --norestore --writer --convert-to pdf")
                .append(" ").append(urlmod).append(" --outdir ").append(urlmod2);
        //Create a directory - because the directory does not necessarily exist
//        System.out.println(f.getPath());
//        System.out.println(f.toURI());
//       System.out.println(f.toURI().toURL());
        //Return process object--Process
        Process exec = Runtime.getRuntime().exec(command.toString());
        
        //Result information
//        InputStream inputStream = exec.getInputStream();
        //IOUtils- directly convert the stream into a string
//        String result = IOUtils.toString(inputStream, ENCODEING_UTF8);
//        if(StringUtils.isBlank(result)) {
//            //Error message
//            InputStream errorStream = exec.getErrorStream();
//            throw new RuntimeException(IOUtils.toString(errorStream, ENCODEING_UTF8));
//        }
//        String sourceFileName = sourceFileRelativePath.substring(sourceFileRelativePath.lastIndexOf("\\") + 1).substring(0, sourceFileRelativePath.lastIndexOf(".")+1);
//        return targetDir + sourceFileName + targetType;
         Path path = Paths.get("file"+File.separator+"temp"+File.separator+fileName.replace(".html", ".pdf"));
 //        System.out.println(f.getAbsolutePath().replace(".html", ".pdf"));
//         System.out.println(f.getCanonicalPath());
 //       Path path = Paths.get("./"+fileName.replace(".html", ".pdf"));
        String pdfFileName = fileName.replace(".html", ".pdf"); 
//        System.out.println(path.getFileName());
//        Scanner file=new Scanner(new File(pdfFileName));
//        System.out.println(new LibreOfficeUtil().getFileName(pdfFileName));
//        System.out.println(Files.readAllBytes(Paths.get(pdfFileName)));
    }
    
    public static void createDir(String dirPath) {
        File fd = null;  
        try {  
            fd = new File(dirPath);  
            if (!fd.exists()) {  
                fd.mkdirs();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            fd = null;  
        } 
    }
    
    public File getFileName(String FileName) {
    	File fxx = new File(
    			this.getClass().getClassLoader().getResource(FileName).getFile()
		    );
    	return fxx;
    }
    
    
    public String convertcustom(String html) throws IOException, InterruptedException {

        String uuid = UUID.randomUUID().toString();
        String directory = "file/temp/";
        createDir(directory);
        String fileName= uuid+"_temp.html";
      
        File f = new File(directory+fileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write(html);
        bw.close();
        StringBuffer url = new StringBuffer(f.toURI().toURL().toString());
        String urlmod = StringUtils.replace(url.toString(), "file:/", "");
        String urlmod2 = StringUtils.remove(urlmod, fileName);
        StringBuffer command = new StringBuffer().append(LIBER_OFFICE_HOME).append("soffice --headless --norestore --writer --convert-to pdf")
                .append(" ").append(urlmod).append(" --outdir ").append(urlmod2);

        //Return process object--Process
        Process exec = Runtime.getRuntime().exec(command.toString());
        

         Path path = Paths.get("file"+File.separator+"temp"+File.separator+fileName.replace(".html", ".pdf"));

        String pdfFileName = fileName.replace(".html", ".pdf"); 
//        System.out.println(path.getFileName());

        Thread.sleep(5000);
        return directory+pdfFileName;
    }
    
}
