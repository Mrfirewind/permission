package com.zhangwq.dao;

import com.zhangwq.model.SysRoleUser;

public interface SysRoleUserMapper {
    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);
}