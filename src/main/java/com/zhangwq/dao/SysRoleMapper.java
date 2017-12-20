package com.zhangwq.dao;

import com.zhangwq.model.SysRole;

public interface SysRoleMapper {
    int insert(SysRole record);

    int insertSelective(SysRole record);
}