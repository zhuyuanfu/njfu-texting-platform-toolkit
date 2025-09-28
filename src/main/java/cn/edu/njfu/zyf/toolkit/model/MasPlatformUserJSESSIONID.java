package cn.edu.njfu.zyf.toolkit.model;

import java.time.LocalDateTime;

/**
 * useless. nobody uses my toolkit but myself.
 * @author zyf
 *
 */
public class MasPlatformUserJSESSIONID {
    private String userName;
    private String JSESSIONID;
    private boolean isAlive;
    private LocalDateTime lastConnectionAliveTime;

	public MasPlatformUserJSESSIONID() {  }

    public MasPlatformUserJSESSIONID(String userName, String jSESSIONID) {
        this.userName = userName;
        this.JSESSIONID = jSESSIONID;
        this.isAlive = false;
        this.lastConnectionAliveTime = LocalDateTime.of(1970, 1, 1, 0, 0);
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
    public boolean isAlive() {
    	return this.isAlive;
    }
    public void setAlive(boolean alive) {
    	this.isAlive = alive;
    }
    public LocalDateTime getLastConnectionAliveTime() {
		return lastConnectionAliveTime;
	}

	public void setLastConnectionAliveTime(LocalDateTime lastConnectionAliveTime) {
		this.lastConnectionAliveTime = lastConnectionAliveTime;
	}
	@Override
	public String toString() {
		return this.userName + ", " + this.JSESSIONID + ", is alive: " + isAlive + ", last alive time: " + getLastConnectionAliveTime();
	}
}
