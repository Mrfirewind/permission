package com.zhangwq.controller;

import com.zhangwq.beans.PageQuery;
import com.zhangwq.beans.PageResult;
import com.zhangwq.common.JsonData;
import com.zhangwq.model.SysAcl;
import com.zhangwq.param.AclParam;
import com.zhangwq.service.ISysAclService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {

    @Autowired
    private ISysAclService sysAclService;

    @RequestMapping(value = "/save.json")
    @ResponseBody
    public JsonData createAcl(AclParam aclParam) {
        sysAclService.saveAcl(aclParam);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAcl(AclParam aclParam) {
        sysAclService.updateAcl(aclParam);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData listAcl(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        PageResult<SysAcl> sysAclPageResult =  sysAclService.listAcl(aclModuleId,pageQuery);
        return JsonData.createBySuccess(sysAclPageResult);
    }
}
