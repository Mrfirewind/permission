package com.zhangwq.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhangwq.common.JsonData;
import com.zhangwq.model.SysUser;
import com.zhangwq.param.RoleParam;
import com.zhangwq.service.*;
import com.zhangwq.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysTreeService sysTreeService;

    @Autowired
    private ISysRoleAclService sysRoleAclService;

    @Autowired
    private ISysRoleUserService sysRoleUserService;

    @Autowired
    private ISysUserService sysUserService;

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

    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData listyRoleTree(@RequestParam("roleId") int roleId) {
        return JsonData.createBySuccess(sysTreeService.roleTree(roleId));
    }

    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.spliteToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.spliteToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId,userIdList);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData listUser(@RequestParam("roleId") int roleId) {
        List<SysUser> selectUsers = sysRoleUserService.listUser(roleId);
        List<SysUser> allUsers = sysUserService.getAllUser();
        List<SysUser> unSelectUsers = Lists.newArrayList();

        Set<Integer> selectUserSet = selectUsers.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        for (SysUser sysUser : allUsers) {
            if (sysUser.getStatus() == 1 && !selectUserSet.contains(sysUser.getId())) {
                unSelectUsers.add(sysUser);
            }
        }

//        selectUsers = selectUsers.stream().filter(sysUser -> sysUser.getStatus() != 1).collect(Collectors.toList());
        Map<String, List<SysUser>> result = Maps.newHashMap();
        result.put("selected", selectUsers);
        result.put("unselected", unSelectUsers);
        return JsonData.createBySuccess(result);
    }


}
