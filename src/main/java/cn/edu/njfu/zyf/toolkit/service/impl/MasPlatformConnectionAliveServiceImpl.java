package cn.edu.njfu.zyf.toolkit.service.impl;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.njfu.zyf.toolkit.config.MasPlatformConnectionAliveCheckingConfig;
import cn.edu.njfu.zyf.toolkit.service.MasPlatformConnectionAliveService;

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
    public String setJSESSIONID(String newJSESSIONID) {
        return config.writeJSESSIONIDToDisk(newJSESSIONID);
    }

    @Override
    public boolean isConnectionAlive() {
        return config.checkConnectionAlive();
    }

    @Override
    public String getLastConnectionAliveTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return config.getLastConnectionAliveTime().format(dtf);
    }

    @Override
    public boolean checkOrKeepConnectionAlive() {
        return config.checkConnectionAlive();
    }
}
