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
    
    @ApiOperation(value = "查看访问mas平台使用的JSESSIONID")
    @RequestMapping(value = "/jsessionid", method = RequestMethod.GET)
    public String getJSESSIONID() throws IOException {
        
        return service.getJSESSIONID();
    }
    
    @ApiOperation(value = "设置访问mas平台使用的JSESSIONID")
    @RequestMapping(value = "/jsessionid", method = RequestMethod.PUT)
    public String setJSESSIONID(String newJSESSIONID) throws IOException {
        if (newJSESSIONID == null || newJSESSIONID.equals("")) {
            return "An empty JSESSIONID is not allowed.";
        }
        return service.setJSESSIONID(newJSESSIONID);
    }
    
    @ApiOperation(value = "查看该JSESSIONID对mas平台来说是否仍然活着")
    @RequestMapping(value = "/connectionAliveStatus", method = RequestMethod.GET)
    public boolean getConnectionAliveStatus() throws IOException {
        return service.isConnectionAlive();
    }
    
    @ApiOperation(value = "查看该JSESSIONID最近一次存活时间")
    @RequestMapping(value = "/connectionLastAliveTime", method = RequestMethod.GET)
    public String getConnectionLastAliveTime() throws IOException {
        return service.getLastConnectionAliveTime();
    }
    
    
}
