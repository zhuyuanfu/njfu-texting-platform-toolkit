package cn.edu.njfu.zyf.toolkit.service;

import org.springframework.web.multipart.MultipartFile;

import cn.edu.njfu.zyf.toolkit.model.TextMessageDelivery;

public interface TextMessageService {
    String resolveFileAndSendHttpRequest(String jSessionId, MultipartFile excelFile);
    boolean sendTextMessage(TextMessageDelivery delivery);
    boolean sendImmediately(String mobile, String content);
    String delete9999ScheduledMessages();
}
