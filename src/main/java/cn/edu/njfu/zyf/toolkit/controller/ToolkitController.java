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
    
    // 日……swagger界面这个表格是一团屎……
    @ApiOperation(value = "上传类似【短信推送-2025年8月.xlsx】的文件，通过http请求，在移动短信平台上设置定时发送任务。\n "
    		+ "第一行是表头，不发送。数据从第二行开始。例如：\n"
    		+ "-----+-------+----------+------------+\n"
    		+ "|姓名|手机号码|发送日期      |短信内容            |\n"
    		+ "+----+-------+----------+------------+\n"
    		+ "|张三|182...  |2025/7/30|【总值班室...|\n"
    		+ "|李四|139...  |2025/7/31|【总值班室...|\n")
    @RequestMapping(value = "/uploadScheduledMessage", method = RequestMethod.POST)
    public String findUntestedDorm(
            String jSessionId,
            MultipartFile messageExcelFile, 
            HttpServletResponse response) throws IOException {
        
        return textMessageSendingService.resolveFileAndSendHttpRequest(jSessionId, messageExcelFile);
    }
    
    @ApiOperation(value = "立即发送一条短信")
    @RequestMapping(value = "/sendImmediately", method = RequestMethod.POST)
    public boolean findUntestedDorm(String mobile, String message) throws IOException {
        return textMessageSendingService.sendImmediately(mobile, message);
    }
    

}
