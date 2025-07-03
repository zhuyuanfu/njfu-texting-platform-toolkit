package cn.edu.njfu.zyf.toolkit.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.njfu.zyf.toolkit.config.MasPlatformConnectionAliveCheckingConfig;
import cn.edu.njfu.zyf.toolkit.service.MasPlatformConnectionAliveService;
import cn.edu.njfu.zyf.toolkit.utils.HttpUtil;

@Service
public class MasPlatformConnectionAliveServiceImpl implements MasPlatformConnectionAliveService{
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private MasPlatformConnectionAliveCheckingConfig config;
    
    @Override
    public String getJSESSIONID() {
        config.readJSESSIONIDFromDisk();
        return config.getJSESSIONID();
    }

    @Override
    public String setJSESSIONID(String jsessionid) {
        return config.writeJSESSIONIDToDisk(jsessionid);
    }

    @Override
    public boolean isConnectionAlive() {
        return visitMASIndexPageToCheckIfConnectionIsAlive();
    }

    @Override
    public String getLastConnectionAliveTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return config.getLastConnectionAliveTime().format(dtf);
    }

    @Override
    public boolean keepConnectionAlive() {
        return visitMASIndexPageToCheckIfConnectionIsAlive();
    }

    private boolean visitMASIndexPageToCheckIfConnectionIsAlive() {
        String url = "http://121.248.150.95:6789/index.do";
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", "JSESSIONID=" + config.getJSESSIONID());
        boolean alive = false;
        try {
            String response = HttpUtil.get(url, header);
            if(response.contains("条新上行短信，请")) {
                alive = true;
                config.setLastConnectionAliveTime(LocalDateTime.now());
            } else {
                logger.info("Connection dead. Please use a new JSESSIONID.");
            }
        } catch (IOException e) {
            logger.error("Connection dead. Please use a new JSESSIONID. {}", e);
        }
        config.setConnectionAlive(alive);
        return alive;
    }
}
