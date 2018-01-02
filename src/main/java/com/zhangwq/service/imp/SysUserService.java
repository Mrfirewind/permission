package com.zhangwq.service.imp;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zhangwq.beans.PageQuery;
import com.zhangwq.beans.PageResult;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysRoleUserMapper;
import com.zhangwq.dao.SysUserMapper;
import com.zhangwq.exception.ParamException;
import com.zhangwq.model.SysUser;
import com.zhangwq.param.UserParam;
import com.zhangwq.service.ISysLogService;
import com.zhangwq.service.ISysUserService;
import com.zhangwq.util.BeanValidator;
import com.zhangwq.util.IpUtil;
import com.zhangwq.util.MD5Util;
import com.zhangwq.util.PasswordUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysUserService implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private ISysLogService sysLogService;

    @Override
    public void saveUser(UserParam userParam) {
        BeanValidator.check(userParam);
        if (checkTelphoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("电话已被占用");
        }

        if (checkEmailExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        PasswordUtil.randomPassword();
        String password = "12345678";
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser sysUser = SysUser.builder().username(userParam.getUsername()).telephone(userParam.getTelephone()).mail(userParam.getMail()).deptId(userParam.getDeptId()).status(userParam.getStatus()).remark(userParam.getRemark()).password(encryptedPassword).build();
        sysUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysUser.setOperateTime(new Date());

        //TODO email
        sysUserMapper.insertSelective(sysUser);
        sysLogService.saveUserLog(null, sysUser);
    }

    public void updateUser(UserParam userParam) {
        BeanValidator.check(userParam);
        if (checkTelphoneExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("电话已被占用");
        }

        if (checkEmailExist(userParam.getTelephone(), userParam.getId())) {
            throw new ParamException("邮箱已被占用");
        }

        SysUser beforeUser = sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(beforeUser, "待更新的用户不存在");
        SysUser afterUser = SysUser.builder().id(userParam.getId()).username(userParam.getUsername()).telephone(userParam.getTelephone()).mail(userParam.getMail()).deptId(userParam.getDeptId()).status(userParam.getStatus()).remark(userParam.getRemark()).build();
        afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterUser.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(afterUser);
        sysLogService.saveUserLog(beforeUser, afterUser);

    }

    @Override
    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }

    @Override
    public PageResult<SysUser> getPageByDeptId(Integer deptId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> sysUsers = sysUserMapper.getPageByDeptId(deptId, pageQuery);
            return PageResult.<SysUser>builder().total(count).data(sysUsers).build();
        }

        return PageResult.<SysUser>builder().build();
    }

    @Override
    public List<SysUser> getAllUser() {
        return sysUserMapper.getAllUser();
    }

    @Override
    public List<SysUser> getUserByRoleIdList(List<Integer> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByUserIds(userIdList);
    }

    private boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByEmail(mail, userId) > 0;
    }

    private boolean checkTelphoneExist(String telphone, Integer userId) {
        return sysUserMapper.countByTelPhone(telphone, userId) > 0;
    }
}
