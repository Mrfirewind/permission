package com.zhangwq.dao;

import com.zhangwq.model.SysAcl;

public interface SysAclMapper {
    int insert(SysAcl record);

    int insertSelective(SysAcl record);
}