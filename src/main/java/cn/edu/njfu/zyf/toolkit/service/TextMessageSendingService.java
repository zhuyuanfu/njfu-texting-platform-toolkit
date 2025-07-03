package cn.edu.njfu.zyf.toolkit.service;

import org.springframework.web.multipart.MultipartFile;

public interface TextMessageSendingService {
    String resolveFileAndSendHttpRequest(String jSessionId, MultipartFile excelFile);
}
