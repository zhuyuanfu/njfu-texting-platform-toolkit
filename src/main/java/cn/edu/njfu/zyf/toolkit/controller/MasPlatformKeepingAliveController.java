package cn.edu.njfu.zyf.toolkit.controller;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.njfu.zyf.toolkit.service.MasPlatformConnectionAliveService;
import io.swagger.annotations.ApiOperation;

@RestController()
@RequestMapping("/masPlatform")
public class MasPlatformKeepingAliveController {

    @Autowired
    private MasPlatformConnectionAliveService service;
    
    @ApiOperation(value = "设置访问mas平台使用的JSESSIONID")
    @RequestMapping(value = "/jsessionid", method = RequestMethod.PUT)
    public String setJSESSIONID(String userName, String newJSESSIONID) throws IOException {
        if (newJSESSIONID == null || newJSESSIONID.equals("")) {
            return "An empty JSESSIONID is not allowed.";
        }
        return service.setJSESSIONID(userName, newJSESSIONID);
    }
    
    @ApiOperation(value = "查看所有JSESSIONID状态")
    @RequestMapping(value = "/connectionAliveStatus", method = RequestMethod.GET)
    public String getConnectionAliveStatus() throws IOException {
        return service.getJsessionidStatus();
    } 
    
    @ApiOperation(value = "手动检查一次所有JSESSION是否存活")
    @RequestMapping(value = "/checkAliveStatus", method = RequestMethod.POST)
    public void checkAllStatus() throws IOException {
        service.checkOrKeepConnectionAlive();
    } 
}
