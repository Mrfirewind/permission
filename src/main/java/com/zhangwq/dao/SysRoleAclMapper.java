package com.zhangwq.dao;

import com.zhangwq.model.SysRoleAcl;

public interface SysRoleAclMapper {
    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);
}