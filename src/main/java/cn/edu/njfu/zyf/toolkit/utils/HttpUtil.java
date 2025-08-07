package cn.edu.njfu.zyf.toolkit.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpUtil {
    
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String requestWithMapFormData(String method, String requestUrl, Map<String, String> header, Map<String, String> formData) throws IOException {

        URL url = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setDoOutput(true);
        
        if (header != null) {
	        Iterator<Entry<String, String>> headerIterator = header.entrySet().iterator();
	        while(headerIterator.hasNext()) {
	            Entry<String, String> entry = headerIterator.next();
	            con.setRequestProperty(entry.getKey(), entry.getValue());
	        }
        }
        
        if (formData != null) {
	        Iterator<Entry<String, String>> formDataIterator = formData.entrySet().iterator();
	        StringBuilder formDataBuilder = new StringBuilder();
	        while(formDataIterator.hasNext()) {
	            Entry<String, String> entry = formDataIterator.next();
	            String key = URLEncoder.encode(entry.getKey(), "UTF-8");
	            String value = URLEncoder.encode(entry.getValue(), "UTF-8");
	            formDataBuilder.append(key).append('=').append(value).append('&');
	        }
	        String formDataPayload = formDataBuilder.toString();
	        if (formDataPayload.endsWith("&")) {
	            formDataPayload = formDataPayload.substring(0, formDataPayload.length() - 1);
	        }
	        
	        //logger.info("formDataPayload   =  " + formDataPayload);
	        OutputStream os = con.getOutputStream();
	        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
	        osw.write(formDataPayload);
	        osw.close();
	        os.close();
        }
        con.connect();
        
        InputStream is = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder responseBuilder = new StringBuilder();
        String responseLine = null;
        while((responseLine = br.readLine()) != null) {
            responseBuilder.append(responseLine).append("\n");
        }
        br.close();
        isr.close();
        is.close();
        
        String responseText = responseBuilder.toString();
        return responseText;
    }

    public static String requestWithStringRequestBody(String method, String requestUrl, Map<String, String> header, String requestBody) throws IOException {

        URL url = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setDoOutput(true);
        
        if (header != null) {
	        Iterator<Entry<String, String>> headerIterator = header.entrySet().iterator();
	        while(headerIterator.hasNext()) {
	            Entry<String, String> entry = headerIterator.next();
	            con.setRequestProperty(entry.getKey(), entry.getValue());
	        }
        }
        
        if (!CommonUtil.isStringEmpty(requestBody)) {
	        OutputStream os = con.getOutputStream();
	        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
	        osw.write(requestBody);
	        osw.close();
	        os.close();
        }
        con.connect();
        
        InputStream is = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder responseBuilder = new StringBuilder();
        String responseLine = null;
        while((responseLine = br.readLine()) != null) {
            responseBuilder.append(responseLine).append("\n");
        }
        br.close();
        isr.close();
        is.close();
        
        String responseText = responseBuilder.toString();
        return responseText;
    }

}
