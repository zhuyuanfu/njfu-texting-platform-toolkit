package cn.edu.njfu.zyf.toolkit.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextMessageDelivery {
    private String name;
    private String mobile;
    private LocalDateTime sendDateTime;    
    private String message;
    private int sendMode; // 如果为0，则触发短信平台的立即发送；如果为20，则触发短信平台的定时发送
    
    public TextMessageDelivery() { }
    
    public TextMessageDelivery(String name, String mobile, LocalDateTime sendDateTime, String message, int sendMode) {
        super();
        this.name = name;
        this.mobile = mobile;
        this.sendDateTime = sendDateTime;
        this.message = message;
        this.sendMode = sendMode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public LocalDateTime getSendDateTime() {
        return sendDateTime;
    }
    public void setSendDateTime(LocalDateTime sendDateTime) {
        this.sendDateTime = sendDateTime;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    
    public int getSendMode() {
        return sendMode;
    }

    public void setSendMode(int sendMode) {
        this.sendMode = sendMode;
    }

    @Override
    public String toString() {
        return "Message Delivery: [" + name + " " + mobile + " " + this.getStringSendDateTime() + " " + message + "]";
    }
    
    public String getStringSendDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        return this.sendDateTime.format(dtf);
    }
}
