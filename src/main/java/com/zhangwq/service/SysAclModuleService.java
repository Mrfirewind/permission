package com.zhangwq.service;

import com.google.common.base.Preconditions;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysAclModuleMapper;
import com.zhangwq.exception.ParamException;
import com.zhangwq.model.SysAclModule;
import com.zhangwq.model.SysDept;
import com.zhangwq.param.AclModuleParam;
import com.zhangwq.util.BeanValidator;
import com.zhangwq.util.IpUtil;
import com.zhangwq.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SysAclModuleService implements ISysAclModuleService {

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Override
    public void saveAclModule(AclModuleParam aclModuleParam) {
        BeanValidator.check(aclModuleParam);
        if (checkExist(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId())) {
            throw new ParamException("同一层级下存在相同的名称的权限模块");
        }

        SysAclModule sysAclModule = SysAclModule.builder().name(aclModuleParam.getName()).parentId(aclModuleParam.getParentId()).status(aclModuleParam.getStatus()).remark(aclModuleParam.getRemark()).seq(aclModuleParam.getSeq()).build();
        sysAclModule.setLevel(LevelUtil.calculateLevel(this.getLevel(aclModuleParam.getParentId()), aclModuleParam.getParentId()));
        sysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(sysAclModule);
    }

    @Override
    public void updateAclModule(AclModuleParam aclModuleParam) {
        BeanValidator.check(aclModuleParam);
        if (checkExist(aclModuleParam.getParentId(), aclModuleParam.getName(), aclModuleParam.getId())) {
            throw new ParamException("同一层级下存在相同的名称的权限模块");
        }
        SysAclModule beforeSysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleParam.getId());
        Preconditions.checkNotNull(beforeSysAclModule, "待更新的权限模块不存在");
        SysAclModule afterSysAclModule = SysAclModule.builder().id(aclModuleParam.getId()).name(aclModuleParam.getName()).parentId(aclModuleParam.getParentId()).status(aclModuleParam.getStatus()).seq(aclModuleParam.getSeq()).remark(aclModuleParam.getRemark()).build();
        afterSysAclModule.setLevel(LevelUtil.calculateLevel(this.getLevel(aclModuleParam.getParentId()), aclModuleParam.getParentId()));
        afterSysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterSysAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterSysAclModule.setOperateTime(new Date());
        updateWithChild(beforeSysAclModule, afterSysAclModule);
    }

    @Transactional
    public void updateWithChild(SysAclModule beforeAclModule, SysAclModule afterAclModule) {
        String newLevel = afterAclModule.getLevel();
        String oldLevel = beforeAclModule.getLevel();
        if (!oldLevel.equals(newLevel)) {
            List<SysAclModule> childSysAclModules = sysAclModuleMapper.getChildDeptListByLevel(oldLevel);
            if (CollectionUtils.isNotEmpty(childSysAclModules)) {
                for (SysAclModule childAclModule : childSysAclModules) {
                    String level = childAclModule.getLevel();
                    if (level.indexOf(oldLevel) == 0) {
                        level = newLevel + level.substring(oldLevel.length());
                        childAclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.updateBatchLevel(childSysAclModules);
            }
        }
        sysAclModuleMapper.updateByPrimaryKey(afterAclModule);
    }

    private boolean checkExist(Integer parentId, String aclModuleName, Integer deptId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, deptId) > 0;
    }

    private String getLevel(Integer deptId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(deptId);
        if (sysAclModule == null) {
            return null;
        }
        return sysAclModule.getLevel();
    }
}
