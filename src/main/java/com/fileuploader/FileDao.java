package com.fileuploader;
import org.codehaus.jettison.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

public interface FileDao {
    public String saveFile(MultipartFile multipartFile) throws IOException, JSONException;
    public String saveFile(MultipartFile multipartFile,String key) throws IOException, JSONException;
    public String saveFile(Part part) throws JSONException, IOException;
    public String saveFile(Part part,String key) throws IOException, JSONException;
    public String saveFile(InputStream inputStream,String fileName,String contentType) throws IOException, JSONException;
    public String saveFile(InputStream inputStream,String fileName,String contentType,String key) throws IOException, JSONException;
    public InputStream getFile(String id) throws IOException;
    public InputStream getFile(String id,String key) throws IOException;

}
