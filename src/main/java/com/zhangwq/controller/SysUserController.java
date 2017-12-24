package com.zhangwq.controller;

import com.zhangwq.beans.PageQuery;
import com.zhangwq.beans.PageResult;
import com.zhangwq.common.JsonData;
import com.zhangwq.param.UserParam;
import com.zhangwq.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping(value = "/save.json")
    @ResponseBody
    public JsonData createSysUser(UserParam userParam){
        sysUserService.saveUser(userParam);
        return JsonData.createBySuccess();
    }


    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateSysUser(UserParam userParam){
        sysUserService.updateUser(userParam);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData userPage(@RequestParam(value = "deptId",defaultValue = "0") Integer deptId, PageQuery pageQuery){
        PageResult result =  sysUserService.getPageByDeptId(deptId,pageQuery);
        return JsonData.createBySuccess(result);
    }
}
