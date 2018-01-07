package com.zhangwq.service.imp;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zhangwq.beans.LogType;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysLogMapper;
import com.zhangwq.dao.SysRoleUserMapper;
import com.zhangwq.dao.SysUserMapper;
import com.zhangwq.model.SysLogWithBLOBs;
import com.zhangwq.model.SysRoleUser;
import com.zhangwq.model.SysUser;
import com.zhangwq.service.ISysRoleUserService;
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
public class SysRoleUserService implements ISysRoleUserService {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public List<SysUser> listUser(Integer roleId) {
        List<Integer> userIds = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIds)) {
            return Lists.newArrayList();
        }

        return sysUserMapper.getByUserIds(userIds);
    }

    @Override
    public void changeRoleUsers(Integer roleId, List<Integer> userIds) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (userIds.size() == originUserIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIds);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }

        this.updateRoleUsers(roleId, userIds);
        this.saveRoleUserLog(roleId, originUserIdList, userIds);
    }

    private void updateRoleUsers(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }

        List<SysRoleUser> sysRoleUsers = Lists.newLinkedList();
        for (Integer userId : userIdList) {
            SysRoleUser sysRoleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(RequestHolder.getCurrentUser().getUsername()).operateTime(new Date()).operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).build();
            sysRoleUsers.add(sysRoleUser);
        }

        sysRoleUserMapper.insertBatch(sysRoleUsers);
    }

    private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
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
