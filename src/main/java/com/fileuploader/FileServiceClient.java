package com.fileuploader;

import com.fileuploader.FileDao;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.Part;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Repository
public class FileServiceClient implements FileDao {



    public String saveFile(MultipartFile multiPartFile) throws IOException, JSONException {
            return saveFile(multiPartFile.getInputStream(),multiPartFile.getOriginalFilename(),multiPartFile.getContentType());
    }

    @Override
    public String saveFile(MultipartFile multipartFile, String key) throws IOException, JSONException {
        return saveFile(multipartFile.getInputStream(),multipartFile.getOriginalFilename(),multipartFile.getContentType(),key);
    }

    @Override
    public String saveFile(Part part) throws JSONException, IOException {
            return saveFile(part.getInputStream(),part.getSubmittedFileName(),part.getContentType());
    }

    @Override
    public String saveFile(Part part, String key) throws IOException, JSONException {
        return saveFile(part.getInputStream(),part.getSubmittedFileName(),part.getContentType(),key);
    }

    @Override
    public String saveFile(InputStream inputStream, String fileName, String contentType) throws IOException, JSONException {
        String attachmentName = "file";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

            URL url = new URL("http://fs.msda.ge/api/FilesSaveFile");
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream req = new DataOutputStream(httpUrlConnection.getOutputStream());
            // this.getUploadedFileName(multiPartFile)
            req.writeBytes(twoHyphens + boundary + crlf);
            req.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\";filename=\"" + fileName + "\"" + crlf);
            req.writeBytes("Content-type: " + contentType + crlf);
            req.writeBytes(crlf);

            int b = 0;
            while ((b = inputStream.read()) != -1) {
                req.write(b);
            }
            req.writeBytes(crlf);
            req.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            req.flush();
            req.close();


            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            JSONObject obj = new JSONObject(response);

            responseStream.close();
            httpUrlConnection.disconnect();
            if (obj.has("error") && !obj.isNull("error")) {
                return obj.getString("error");
            } else {
                System.out.println(obj.getJSONObject("result").getJSONObject("data").getString("_id"));
                return obj.getJSONObject("result").getJSONObject("data").getString("_id");
            }


    }

    @Override
    public String saveFile(InputStream inputStream, String fileName, String contentType,String key) throws IOException, JSONException {
        String fileParameter = "file";
        String keyParameter="key";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String charset="UTF-8";
            //InputStream is = multiPartFile.getInputStream();

            URL url = new URL("http://fs.msda.ge/api/FilesSaveFile");
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream req = new DataOutputStream(httpUrlConnection.getOutputStream());
            // this.getUploadedFileName(multiPartFile)


            req.writeBytes(twoHyphens + boundary + crlf);
            req.writeBytes("Content-Disposition: form-data; name=\"" + keyParameter+ "\"" + crlf);
            req.writeBytes("Content-type: " + "text/plain;"+ "charset=" + charset + crlf);
            req.writeBytes(crlf);
            req.writeBytes(key);
            req.writeBytes(crlf);
            req.flush();

            req.writeBytes(twoHyphens + boundary + crlf);
            req.writeBytes("Content-Disposition: form-data; name=\"" + fileParameter + "\";filename=\"" + fileName + "\"" + crlf);
            req.writeBytes("Content-type: " + contentType + crlf);
            req.writeBytes(crlf);

            int b = 0;
            while ((b = inputStream.read()) != -1) {
                req.write(b);
            }
            req.writeBytes(crlf);
            req.writeBytes(twoHyphens + boundary + twoHyphens + crlf);


            req.flush();
            req.close();


            InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            JSONObject obj = new JSONObject(response);

            responseStream.close();
            httpUrlConnection.disconnect();
            if (obj.has("error") && !obj.isNull("error")) {
                return obj.getString("error");
            } else {
                System.out.println(obj.getJSONObject("result").getJSONObject("data").getString("_id"));
                return obj.getJSONObject("result").getJSONObject("data").getString("_id");
            }


    }

    @Override
    public InputStream getFile(String id) throws IOException {
        Map<String,String> params=new HashMap<>();
        params.put("fileId",id);
        return getFile(params);
    }

    @Override
    public InputStream getFile(String id, String key) throws IOException {
        Map<String,String> params=new HashMap<>();
        params.put("fileId",id);
        params.put("key",key);
        return getFile(params);
    }

    private InputStream getFile(Map<String,String> params) throws IOException {
        String requestURL ="http://fs.msda.ge/api/FilesGetFile";

        URL url = new URL(requestURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);

        httpConn.setDoInput(true); // true indicates the server returns response

        StringBuffer requestParams = new StringBuffer();

        if (params != null && params.size() > 0) {

            httpConn.setDoOutput(true); // true indicates POST request

            // creates the params string, encode them using URLEncoder
            Iterator<String> paramIterator = params.keySet().iterator();
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(
                        URLEncoder.encode(value, "UTF-8"));
                requestParams.append("&");
            }

            // sends POST data
            DataOutputStream req = new DataOutputStream(httpConn.getOutputStream());
            req.writeBytes(requestParams.toString());
            req.flush();
            //get data from
            InputStream inputStream = httpConn.getInputStream();
            return inputStream;

        }

        return null;
    }

}
