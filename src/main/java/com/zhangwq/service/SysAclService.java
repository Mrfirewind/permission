package com.zhangwq.service;

import com.google.common.base.Preconditions;
import com.zhangwq.beans.PageQuery;
import com.zhangwq.beans.PageResult;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysAclMapper;
import com.zhangwq.exception.ParamException;
import com.zhangwq.model.SysAcl;
import com.zhangwq.param.AclParam;
import com.zhangwq.util.BeanValidator;
import com.zhangwq.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SysAclService implements ISysAclService {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Override
    public void saveAcl(AclParam aclParam) {
        BeanValidator.check(aclParam);
        if (checkExist(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId())) {
            throw new ParamException("当前权限模块下存在相同的权限名称");
        }

        SysAcl sysAcl = SysAcl.builder().name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId()).url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).seq(aclParam.getSeq()).remark(aclParam.getRemark()).build();
        sysAcl.setCode(generateCode());
        sysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAcl.setOperateTime(new Date());
        sysAclMapper.insertSelective(sysAcl);
    }

    @Override
    public void updateAcl(AclParam aclParam) {
        BeanValidator.check(aclParam);
        if (checkExist(aclParam.getAclModuleId(), aclParam.getName(), aclParam.getId())) {
            throw new ParamException("当前权限模块下存在相同的权限名称");
        }

        SysAcl beforeSysAcl = sysAclMapper.selectByPrimaryKey(aclParam.getId());
        Preconditions.checkNotNull(beforeSysAcl, "待更新的权限点不存在");

        SysAcl afterSysAcl = SysAcl.builder().id(aclParam.getId()).name(aclParam.getName()).aclModuleId(aclParam.getAclModuleId()).url(aclParam.getUrl()).type(aclParam.getType()).status(aclParam.getStatus()).seq(aclParam.getSeq()).remark(aclParam.getRemark()).build();
        afterSysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterSysAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterSysAcl.setOperateTime(new Date());

        sysAclMapper.updateByPrimaryKeySelective(afterSysAcl);

    }

    @Override
    public PageResult<SysAcl> listAcl(Integer aclModuleId, PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        int count = sysAclMapper.countByAclModule(aclModuleId);
        if (count > 0) {
            List<SysAcl> sysAcls = sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            return PageResult.<SysAcl>builder().data(sysAcls).total(count).build();
        }

        return PageResult.<SysAcl>builder().build();
    }


    private boolean checkExist(int aclModelId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModelId,name,id) > 0;
    }

    private String generateCode() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date()) + "_" + (int) Math.random() * 100;
    }
}
