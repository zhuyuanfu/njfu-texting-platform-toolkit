package cn.edu.njfu.zyf.toolkit.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.njfu.zyf.toolkit.model.TeachingStaff;
import cn.edu.njfu.zyf.toolkit.service.TeachingStaffService;
import io.swagger.annotations.ApiOperation;

@RestController()
@RequestMapping("/teachingStaff")
public class TeachingStaffController {

    @Autowired
    private TeachingStaffService service;
    
    @ApiOperation(value = "查看今天哪些老师过生日")
    @RequestMapping(value = "/teachingStaffsWhoseBirthdayIsToday", method = RequestMethod.GET)
    public List<TeachingStaff> getJSESSIONID() throws IOException {
        return service.listTodaysTeachingStaff();
    }
    
   
    
}
