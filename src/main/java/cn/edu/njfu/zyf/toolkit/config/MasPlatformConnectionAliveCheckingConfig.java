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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import cn.edu.njfu.zyf.toolkit.utils.HttpUtil;

@Component
public class MasPlatformConnectionAliveCheckingConfig  {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Value("${masplatform.JSESSIONIDDiskStoragePath}")
    private String jSessionIdDiskStoragePath;
    
    private String JSESSIONID;
    
    private boolean isConnectionAlive;
    
    private LocalDateTime lastConnectionAliveTime = LocalDateTime.of(1970, 1, 1, 0, 0);
    
    public String getJSESSIONID() {
        return this.JSESSIONID;
    }
    
    public boolean getConnectionAlive() {
        return this.isConnectionAlive;
    }
    
    public LocalDateTime getLastConnectionAliveTime() {
        return this.lastConnectionAliveTime;
    }

    public boolean isConnectionAlive() {
        return isConnectionAlive;
    }

    public void setConnectionAlive(boolean isConnectionAlive) {
        this.isConnectionAlive = isConnectionAlive;
    }

    public void setJSESSIONID(String jSESSIONID) {
        JSESSIONID = jSESSIONID;
    }

    public void setLastConnectionAliveTime(LocalDateTime lastConnectionAliveTime) {
        this.lastConnectionAliveTime = lastConnectionAliveTime;
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.readJSESSIONIDFromDisk();
        this.checkConnectionAlive();
        logger.info("** MasPlatformConnectionAliveCheckingConfig has been initiated. **");
    }
    
    public String writeJSESSIONIDToDisk(String newJSESSIONID) {
        File file = new File(this.jSessionIdDiskStoragePath);
        OutputStream os =null;
        OutputStreamWriter osr = null;
        try {
            os = new FileOutputStream(file);
            osr = new OutputStreamWriter(os);
            osr.write("admin=" + newJSESSIONID);
            this.JSESSIONID = newJSESSIONID;
        } catch (IOException e) {
            logger.error("write jsession error, {}", e);
            return "writing jsessionid to disk failed. go see log file for urself.";
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
        return "writing jsessionid to disk succeeded.";
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
            String jsessionid = line.split("=")[1];
            this.JSESSIONID = jsessionid;
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
    
    public Boolean checkConnectionAlive() {
        String url = "http://121.248.150.95:6789/sms/sendSms.do?act=getNoticeMap";
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "JSESSIONID=" + this.JSESSIONID);
        header.put("connection", "keep-alive");
        header.put("content-length", "0");
        boolean alive = false;
        try {
            String response = HttpUtil.post(url, header, "");
            if(!response.contains("登录超时")) {
                alive = true;
                this.lastConnectionAliveTime = LocalDateTime.now();
                logger.info("Checking if JSESSIONID is alive using {}. Response: {}", url, response.trim());

            } else {
                logger.warn("Checking if JSESSIONID is alive using {}. Response says:  \n登录超时\n", url);
                logger.warn("Connection dead. Set a new JSESSIONID.");
            }
        } catch (IOException e) {
            logger.error("Connection dead. Set a new JSESSIONID. {}", e);
        }
        this.isConnectionAlive = alive;
        return alive;
    }


}
