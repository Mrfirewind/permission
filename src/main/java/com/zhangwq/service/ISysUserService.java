package com.zhangwq.service;

import com.zhangwq.beans.PageQuery;
import com.zhangwq.beans.PageResult;
import com.zhangwq.model.SysUser;
import com.zhangwq.param.UserParam;

import java.util.List;

public interface ISysUserService {

    void saveUser(UserParam userParam);

    void updateUser(UserParam userParam);

    SysUser findByKeyword(String keyword);

    PageResult getPageByDeptId(Integer deptId, PageQuery pageQuery);

    List<SysUser> getAllUser();

    List<SysUser> getUserByRoleIdList(List<Integer> roleIdList);
}
