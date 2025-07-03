package cn.edu.njfu.zyf.toolkit.controller;

import java.io.IOException;


import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.njfu.zyf.toolkit.service.TextMessageSendingService;
import io.swagger.annotations.ApiOperation;

@RestController
public class ToolkitController {
    
    @Autowired
    private TextMessageSendingService textMessageSendingService;
    
    @ApiOperation(value = "说声hi")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hi";
    }
    
    @ApiOperation(value = "上传【值班日期短信定时发送】的文件，通过http请求发送给移动短信平台")
    @RequestMapping(value = "/uploadScheduledMessage", method = RequestMethod.POST)
    public String findUntestedDorm(
            String jSessionId,
            MultipartFile messageExcelFile, 
            HttpServletResponse response) throws IOException {
        
        return textMessageSendingService.resolveFileAndSendHttpRequest(jSessionId, messageExcelFile);
    }

}
