package com.zhangwq.service;

import com.zhangwq.model.SysAcl;

import java.util.List;

public interface ISysCoreService {

    List<SysAcl> getCurrentUserAclList();

    List<SysAcl> getRoleAclList(int roleId);

    List<SysAcl> getUserAclList(int userId);
}
