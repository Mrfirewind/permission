package com.zhangwq.service;

import com.zhangwq.model.SysRole;
import com.zhangwq.param.RoleParam;

import java.util.List;

public interface ISysRoleService {

    void saveRole(RoleParam roleParam);

    void updateRole(RoleParam roleParam);

    List<SysRole> getAllRole();

    List<SysRole> getRoleListById(Integer userId);

    List<SysRole> getRoleListByAclId(Integer aclId);
}
