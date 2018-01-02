package com.zhangwq.service.imp;

import com.google.common.collect.Lists;
import com.zhangwq.beans.CacheKeyConstants;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysAclMapper;
import com.zhangwq.dao.SysRoleAclMapper;
import com.zhangwq.dao.SysRoleUserMapper;
import com.zhangwq.model.SysAcl;
import com.zhangwq.service.ISysCoreService;
import com.zhangwq.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysCoreService implements ISysCoreService {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysCacheService sysCacheService;


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

    @Override
    public boolean hasUrlAcl(String url) {
        if (isSupperAdmin()) {
            return true;
        }

        List<SysAcl> sysAcls = sysAclMapper.getAclByUrl(url);
        if (CollectionUtils.isEmpty(sysAcls)) {
            return true;
        }
        List<SysAcl> userAcls = this.getCurrentUserAclListFromCache();
        List<Integer> userAclIds = userAcls.stream().map(userAcl -> userAcl.getId()).collect(Collectors.toList());
        boolean hasVaildAcl = false;
        for (SysAcl sysAcl : sysAcls) {
            if (sysAcl == null || sysAcl.getStatus() != 1) {
                continue;
            }
            hasVaildAcl = true;
            if (userAclIds.contains(sysAcl.getId())) {
                return true;
            }
        }

        if (!hasVaildAcl) {
            return true;
        }
        return false;
    }

    private boolean isSupperAdmin() {
        if (RequestHolder.getCurrentUser().getUsername().equalsIgnoreCase("admin")) {
            return true;
        }
        return false;
    }

    private List<SysAcl> getCurrentUserAclListFromCache(){
         int userId = RequestHolder.getCurrentUser().getId();
         String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.SUER_ACLS,String.valueOf(userId));
         if(StringUtils.isBlank(cacheValue)){
             List<SysAcl> sysAcls = getCurrentUserAclList();
             if(CollectionUtils.isNotEmpty(sysAcls)){
                 sysCacheService.saveCache(JsonMapper.objToString(sysAcls),600,CacheKeyConstants.SUER_ACLS,String.valueOf(userId));
             }
             return sysAcls;
         }
         return JsonMapper.strToObject(cacheValue, new TypeReference<List<SysAcl>>() {});
    }
}
