package com.zhangwq.service;

import com.zhangwq.beans.PageQuery;
import com.zhangwq.beans.PageResult;
import com.zhangwq.model.SysAcl;
import com.zhangwq.param.AclParam;

public interface ISysAclService {

    void saveAcl(AclParam aclParam);

    void updateAcl(AclParam aclParam);

    PageResult<SysAcl> listAcl(Integer aclModuleId, PageQuery pageQuery) ;

}
