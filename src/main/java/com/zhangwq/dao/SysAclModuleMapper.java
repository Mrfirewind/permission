package com.zhangwq.dao;

import com.zhangwq.model.SysAclModule;

public interface SysAclModuleMapper {
    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);
}