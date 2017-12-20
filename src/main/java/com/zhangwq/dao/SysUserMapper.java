package com.zhangwq.dao;

import com.zhangwq.model.SysUser;

public interface SysUserMapper {
    int insert(SysUser record);

    int insertSelective(SysUser record);
}