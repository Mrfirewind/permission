package com.zhangwq.service;

import com.zhangwq.beans.PageQuery;
import com.zhangwq.beans.PageResult;
import com.zhangwq.model.*;
import com.zhangwq.param.SearchLogParam;

public interface ISysLogService {

   void recover(int id);

    PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery page);

    void saveDeptLog(SysDept before, SysDept after);

    void saveUserLog(SysUser before, SysUser after);

    void saveAclModuleLog(SysAclModule before, SysAclModule after);

    void saveAclLog(SysAcl before, SysAcl after);

    void saveRoleLog(SysRole before, SysRole after);
}
