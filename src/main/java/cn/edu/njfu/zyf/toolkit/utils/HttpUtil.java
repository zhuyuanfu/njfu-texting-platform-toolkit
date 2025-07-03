package cn.edu.njfu.zyf.toolkit.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpUtil {
    
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    
    public static String post(
            String requestUrl,
            Map<String, String> header,
            Map<String, String> formData
            ) throws IOException {
        
        //logger.debug("header: {}", mapToPrettyString(header));
        //logger.debug("formData: {}", mapToPrettyString(formData));

        URL url = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        
        Iterator<Entry<String, String>> headerIterator = header.entrySet().iterator();
        while(headerIterator.hasNext()) {
            Entry<String, String> entry = headerIterator.next();
            con.setRequestProperty(entry.getKey(), entry.getValue());
        }
        
        Iterator<Entry<String, String>> formDataIterator = formData.entrySet().iterator();
        StringBuilder formDataBuilder = new StringBuilder();
        while(formDataIterator.hasNext()) {
            Entry<String, String> entry = formDataIterator.next();
            String key = URLEncoder.encode(entry.getKey(), Charset.forName("UTF-8"));
            String value = URLEncoder.encode(entry.getValue(), Charset.forName("UTF-8"));
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
    
    public static String mapToPrettyString(Map map) {
        StringBuilder sb = new StringBuilder();
        sb.append('{').append('\n');
        Iterator<Entry> itr = map.entrySet().iterator();
        while(itr.hasNext()) {
            Entry e = itr.next();
            sb.append('\t').append(e.getKey()).append(" -> ").append(e.getValue()).append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
    
    public static String get(String requestUrl, Map<String, String> header) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        
        Iterator<Entry<String, String>> headerIterator = header.entrySet().iterator();
        while(headerIterator.hasNext()) {
            Entry<String, String> entry = headerIterator.next();
            con.setRequestProperty(entry.getKey(), entry.getValue());
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
