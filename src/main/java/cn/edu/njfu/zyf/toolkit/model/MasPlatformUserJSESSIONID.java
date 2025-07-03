package cn.edu.njfu.zyf.toolkit.model;

/**
 * useless. nobody uses my toolkit but myself.
 * @author zyf
 *
 */
public class MasPlatformUserJSESSIONID {
    private String userName;
    private String JSESSIONID;
    
    public MasPlatformUserJSESSIONID() {  }

    public MasPlatformUserJSESSIONID(String userName, String jSESSIONID) {
        this.userName = userName;
        this.JSESSIONID = jSESSIONID;
    }
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getJSESSIONID() {
        return JSESSIONID;
    }
    public void setJSESSIONID(String jSESSIONID) {
        JSESSIONID = jSESSIONID;
    }
    
}
