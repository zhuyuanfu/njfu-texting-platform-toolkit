package cn.edu.njfu.zyf.toolkit.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import cn.edu.njfu.zyf.toolkit.model.MasPlatformUserJSESSIONID;
import cn.edu.njfu.zyf.toolkit.utils.CommonUtil;
import cn.edu.njfu.zyf.toolkit.utils.HttpUtil;

@Component
public class MasPlatformConnectionAliveCheckingConfig  {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Value("${masplatform.JSESSIONIDDiskStoragePath}")
    private String jSessionIdDiskStoragePath;
        
    // 虽然key里存了用户名，value里又存一份用户名不太好，但是我懒啊
    private Map<String, MasPlatformUserJSESSIONID> jsessionIdMap = new HashMap<>();

    public String getJSESSIONID(String userName) {
        return jsessionIdMap.get(userName).getJSESSIONID();
    }
    

    public String getConnectionStatuses() {
    	return CommonUtil.mapToPrettyString(jsessionIdMap);
    }

    public void setConnectionAlive(String userName, boolean isConnectionAlive) {
        this.jsessionIdMap.get(userName).setAlive(isConnectionAlive);
    }


    public void setJSESSIONID(String userName, String jSESSIONID) {
    	if (this.jsessionIdMap.containsKey(userName)) {
    		this.jsessionIdMap.get(userName).setJSESSIONID(jSESSIONID);
    	} else {
    		MasPlatformUserJSESSIONID j = new MasPlatformUserJSESSIONID(userName, jSESSIONID);
    		this.jsessionIdMap.put(userName, j);
    	}
    	writeJSESSIONIDsToDisk();
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.readJSESSIONIDFromDisk();
        this.checkAllJSESSIONIDsAlive();
        logger.info("** MasPlatformConnectionAliveCheckingConfig has been initiated. **");
    }
        
    public String writeJSESSIONIDsToDisk() {
        File file = new File(this.jSessionIdDiskStoragePath);
        OutputStream os = null;
        OutputStreamWriter osr = null;
        try {
            os = new FileOutputStream(file);
            osr = new OutputStreamWriter(os);
            StringBuilder sb = new StringBuilder();
            Iterator<Entry<String, MasPlatformUserJSESSIONID>> itr = jsessionIdMap.entrySet().iterator();
            while(itr.hasNext()) {
            	Entry<String, MasPlatformUserJSESSIONID> entry = itr.next();
            	String userName = entry.getKey();
            	MasPlatformUserJSESSIONID jsessionid = entry.getValue();
                sb.append(userName).append('=').append(jsessionid.getJSESSIONID()).append('\n');
            }
            osr.write(sb.toString());
        } catch (IOException e) {
            logger.error("write jsessionid error, {}", e);
        } finally {
            if(osr != null) {
                try {
                    osr.flush();
                    osr.close();
                    
                } catch (IOException e) {
                    logger.error("close OutputStreamWriter error, {}", e);
                }
            }
            if(os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    logger.error("close OutputStream error, {}", e);
                }
            }
        }
        return "Writing jsessionids to disk finished.";
    }
    
    public void readJSESSIONIDFromDisk() {
        logger.info("path: {}", this.jSessionIdDiskStoragePath);
        File file = new File(this.jSessionIdDiskStoragePath);
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = new FileInputStream(file);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null) {
            	String[] userAndJSESSIONIDArray = line.split("=");
            	MasPlatformUserJSESSIONID jsessionid = new MasPlatformUserJSESSIONID(userAndJSESSIONIDArray[0], userAndJSESSIONIDArray[1]);
            	this.jsessionIdMap.put(userAndJSESSIONIDArray[0], jsessionid);
            	line = br.readLine();
            }
        } catch(IOException e) {
            logger.error("read jsessionid file error, {}", e);
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("close br error, {}", e);
                }
            }
            if(isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    logger.error("close isr error, {}", e);
                }
            }
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("close is error, {}", e);
                }
            }
        }
    }
    
    public void checkAllJSESSIONIDsAlive() {
        String url = "http://121.248.150.95:6789/sms/sendSms.do?act=getNoticeMap";
        Iterator<MasPlatformUserJSESSIONID> itr = this.jsessionIdMap.values().iterator();
        while (itr.hasNext()) {
        	MasPlatformUserJSESSIONID jsessionIdWrapper = itr.next(); 
        	Map<String, String> header = new HashMap<>();
            header.put("Cookie", "JSESSIONID=" + jsessionIdWrapper.getJSESSIONID());
            header.put("connection", "keep-alive");
            header.put("content-length", "0");
            boolean alive = false;
            try {
                String response = HttpUtil.requestWithStringRequestBody("POST", url, header, "");
                //logger.info(response);
                if(response.contains("登录超时")) {
                    logger.warn("Checking if {} is alive using {}. Response says:  \n登录超时\n",jsessionIdWrapper.getUserName(), url);
                    logger.warn("{} is dead. Set a new JSESSIONID for him.", jsessionIdWrapper.getUserName());
                } else {
                	alive = true;
                    logger.info("Checking if {} is alive using {}. Response: {}", jsessionIdWrapper.getUserName(), url, response.trim());
                    jsessionIdWrapper.setLastConnectionAliveTime(LocalDateTime.now());
                }
            } catch (IOException e) {
                logger.error("Exception. Skipping {}. Error: {}", jsessionIdWrapper.getUserName(), e);
            }
            jsessionIdWrapper.setAlive(alive);
        }
    }
}
