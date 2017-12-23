package com.zhangwq.service;

import com.zhangwq.dto.DeptLevelDto;

import java.util.List;

public interface ISysTreeService {
    public List<DeptLevelDto> getDeptTree();
}
