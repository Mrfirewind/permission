package com.zhangwq.controller;

import com.zhangwq.common.JsonData;
import com.zhangwq.param.RoleParam;
import com.zhangwq.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @RequestMapping(value = "/role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    @RequestMapping(value = "/save.json")
    @ResponseBody
    public JsonData createSysRole(RoleParam roleParam) {
        sysRoleService.saveRole(roleParam);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateSysRole(RoleParam roleParam) {
        sysRoleService.updateRole(roleParam);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData listySysRole() {
        return JsonData.createBySuccess(sysRoleService.getAllRole());
    }
}
