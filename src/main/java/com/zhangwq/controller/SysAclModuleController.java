package com.zhangwq.controller;

import com.zhangwq.common.JsonData;
import com.zhangwq.param.AclModuleParam;
import com.zhangwq.service.ISysAclModuleService;
import com.zhangwq.service.ISysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/aclmodule")
@Slf4j
public class SysAclModuleController {
    @Autowired
    private ISysAclModuleService sysAclModuleService;
    @Autowired
    private ISysTreeService sysTreeService;

    @RequestMapping(value = "/acl.page")
    public ModelAndView deptPage(){
        return new ModelAndView("acl");
    }

    @RequestMapping(value = "/save.json")
    @ResponseBody
    public JsonData createAclModule(AclModuleParam aclModuleParam){
        sysAclModuleService.saveAclModule(aclModuleParam);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam aclModuleParam){
        sysAclModuleService.updateAclModule(aclModuleParam);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData getDeptTree(){
        return JsonData.createBySuccess(sysTreeService.getAclModuleTree());
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteSysDept(@RequestParam("id") Integer id){
        sysAclModuleService.deleteAclModule(id);
        return JsonData.createBySuccess();
    }
}
