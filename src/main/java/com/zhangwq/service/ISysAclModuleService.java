package com.zhangwq.service;

import com.zhangwq.param.AclModuleParam;

public interface ISysAclModuleService {

    void saveAclModule(AclModuleParam aclModuleParam);

    void updateAclModule(AclModuleParam aclModuleParam);

    void deleteAclModule(Integer aclModuleId);
}
