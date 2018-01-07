package com.zhangwq.service.imp;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zhangwq.beans.LogType;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysLogMapper;
import com.zhangwq.dao.SysRoleAclMapper;
import com.zhangwq.model.SysLogWithBLOBs;
import com.zhangwq.model.SysRoleAcl;
import com.zhangwq.service.ISysRoleAclService;
import com.zhangwq.util.IpUtil;
import com.zhangwq.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SysRoleAclService implements ISysRoleAclService {

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIds(Lists.newArrayList(roleId));
        if (aclIdList.size() == originAclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }

        this.updateRoleAcls(roleId, aclIdList);
        this.saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }

    private void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        sysRoleAclMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }

        List<SysRoleAcl> sysRoleAclList = Lists.newLinkedList();
        for (Integer aclId : aclIdList) {
            SysRoleAcl sysRoleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).operator(RequestHolder.getCurrentUser().getUsername()).operateTime(new Date()).operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).build();
            sysRoleAclList.add(sysRoleAcl);
        }

        sysRoleAclMapper.insertBatch(sysRoleAclList);
    }

    private void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.objToString(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.objToString(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
}
