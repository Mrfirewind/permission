package com.zhangwq.service;

import com.zhangwq.model.SysUser;

import java.util.List;

public interface ISysRoleUserService {

    List<SysUser> listUser(Integer roleId);

    void changeRoleUsers(Integer roleId, List<Integer> userIds);
}
