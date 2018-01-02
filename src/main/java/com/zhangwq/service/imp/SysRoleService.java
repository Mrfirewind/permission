package com.zhangwq.service.imp;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysRoleAclMapper;
import com.zhangwq.dao.SysRoleMapper;
import com.zhangwq.dao.SysRoleUserMapper;
import com.zhangwq.exception.ParamException;
import com.zhangwq.model.SysRole;
import com.zhangwq.param.RoleParam;
import com.zhangwq.service.ISysLogService;
import com.zhangwq.service.ISysRoleService;
import com.zhangwq.util.BeanValidator;
import com.zhangwq.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysRoleService implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private ISysLogService sysLogService;


    @Override
    public void saveRole(RoleParam roleParam) {
        BeanValidator.check(roleParam);
        if (checkExist(roleParam.getName(), roleParam.getId())) {
            throw new ParamException("存在相同的角色名称");
        }

        SysRole sysRole = SysRole.builder().name(roleParam.getName()).type(roleParam.getType()).status(roleParam.getStatus()).remark(roleParam.getRemark()).build();

        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperateTime(new Date());
        sysRoleMapper.insertSelective(sysRole);
        sysLogService.saveRoleLog(null, sysRole);
    }

    @Override
    public void updateRole(RoleParam roleParam) {
        BeanValidator.check(roleParam);
        if (checkExist(roleParam.getName(), roleParam.getId())) {
            throw new ParamException("存在相同的角色名称");
        }

        SysRole beforeSysRole = sysRoleMapper.selectByPrimaryKey(roleParam.getId());
        Preconditions.checkNotNull(beforeSysRole, "待更新的角色不存在");

        SysRole afterSysRole = SysRole.builder().id(roleParam.getId()).name(roleParam.getName()).type(roleParam.getType()).status(roleParam.getStatus()).remark(roleParam.getRemark()).build();
        afterSysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterSysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterSysRole.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(afterSysRole);
        sysLogService.saveRoleLog(beforeSysRole, afterSysRole);
    }

    @Override
    public List<SysRole> getAllRole() {
        return sysRoleMapper.getAllRole();
    }

    @Override
    public List<SysRole> getRoleListById(Integer userId) {
        List<Integer> roleIds = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }

        return sysRoleMapper.getRoleListByRoleIds(roleIds);
    }

    @Override
    public List<SysRole> getRoleListByAclId(Integer aclId) {
        List<Integer> roleIds = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Lists.newArrayList();
        }

        return sysRoleMapper.getRoleListByRoleIds(roleIds);
    }

    private boolean checkExist(String roleName, Integer roleId) {
        return sysRoleMapper.countByName(roleName, roleId) > 0;
    }
}
