package com.zhangwq.service;

import com.google.common.collect.Lists;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysAclMapper;
import com.zhangwq.dao.SysRoleAclMapper;
import com.zhangwq.dao.SysRoleUserMapper;
import com.zhangwq.model.SysAcl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysCoreService implements ISysCoreService {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return this.getUserAclList(userId);
    }

    @Override
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> roleAclIdList = sysRoleAclMapper.getAclIdListByRoleIds(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(roleAclIdList)) {
            return Lists.newArrayList();
        }

        return sysAclMapper.getAclByIds(roleAclIdList);
    }

    @Override
    public List<SysAcl> getUserAclList(int userId) {
        if (isSupperAdmin()) {
            return sysAclMapper.getAllAcl();
        }

        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }

        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIds(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }

        List<SysAcl> sysAcls = sysAclMapper.getAclByIds(userAclIdList);
        return sysAcls;
    }

    private boolean isSupperAdmin() {
        return true;
    }
}
