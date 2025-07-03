package cn.edu.njfu.zyf.toolkit.service;

public interface MasPlatformConnectionAliveService {
    String getJSESSIONID();
    String setJSESSIONID(String jsessionid);
    boolean isConnectionAlive();
    String getLastConnectionAliveTime();
    boolean keepConnectionAlive();
}
