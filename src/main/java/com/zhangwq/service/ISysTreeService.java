package com.zhangwq.service;

import com.zhangwq.dto.AclModuleDto;
import com.zhangwq.dto.DeptLevelDto;

import java.util.List;

public interface ISysTreeService {
    List<DeptLevelDto> getDeptTree();

    List<AclModuleDto> getAclModuleTree();

    List<AclModuleDto> roleTree(int roleId);
}
