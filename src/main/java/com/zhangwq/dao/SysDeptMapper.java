package com.zhangwq.dao;

import com.zhangwq.model.SysDept;

public interface SysDeptMapper {
    int insert(SysDept record);

    int insertSelective(SysDept record);
}