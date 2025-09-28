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
    public void checkOrKeepConnectionAlive() {
        config.checkAllJSESSIONIDsAlive();
    }

	@Override
	public String getJSESSIONID(String userName) {
		 config.readJSESSIONIDFromDisk();
	     return config.getJSESSIONID(userName);
	}

	@Override
	public String setJSESSIONID(String userName, String jsessionid) {
		config.setJSESSIONID(userName, jsessionid);
		config.writeJSESSIONIDsToDisk();
		return "Done, check 4 urself.";
	}

	@Override
	public String getJsessionidStatus() {
		return config.getConnectionStatuses();
	}

	@Override
	public String getLastConnectionAliveTime(String userName) {
		// TODO Auto-generated method stub
		return null;
	}
}
