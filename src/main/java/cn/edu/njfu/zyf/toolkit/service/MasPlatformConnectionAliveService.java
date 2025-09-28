package cn.edu.njfu.zyf.toolkit.service;

public interface MasPlatformConnectionAliveService {
    void checkOrKeepConnectionAlive();
    
    String getJSESSIONID(String userName);
    String setJSESSIONID(String userName, String jsessionid);
    String getJsessionidStatus();
    String getLastConnectionAliveTime(String userName);    
}
