package com.zhangwq.dao;

import com.zhangwq.model.SysLogWithBLOBs;

public interface SysLogMapper {
    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);
}