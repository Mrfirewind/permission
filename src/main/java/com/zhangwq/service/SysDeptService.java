package com.zhangwq.service;

import com.google.common.base.Preconditions;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.dao.SysDeptMapper;
import com.zhangwq.exception.ParamException;
import com.zhangwq.model.SysDept;
import com.zhangwq.param.DeptParam;
import com.zhangwq.util.BeanValidator;
import com.zhangwq.util.IpUtil;
import com.zhangwq.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SysDeptService implements ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public void saveDept(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下存在相同的部门");
        }
        SysDept sysDept = SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        sysDept.setLevel(LevelUtil.calculateLevel(this.getLevel(deptParam.getParentId()), deptParam.getParentId()));
        sysDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(sysDept);
    }

    public void updateDept(DeptParam deptParam) {
        BeanValidator.check(deptParam);
        if (checkExist(deptParam.getParentId(), deptParam.getName(), deptParam.getId())) {
            throw new ParamException("同一层级下存在相同的部门");
        }
        SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(beforeDept, "待更新的部门不存在");


        SysDept afterDept = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId()).seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        afterDept.setLevel(LevelUtil.calculateLevel(this.getLevel(deptParam.getParentId()), deptParam.getParentId()));
        afterDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterDept.setOperateTime(new Date());
        updateWithChild(beforeDept, afterDept);
    }

    @Transactional
    public void updateWithChild(SysDept beforeDept, SysDept afterDept) {
        sysDeptMapper.updateByPrimaryKey(afterDept);
        String newLevel = afterDept.getLevel();
        String oldLevel = beforeDept.getLevel();
        if (!oldLevel.equals(newLevel)) {
            List<SysDept> childDepts = sysDeptMapper.getChildDeptListByLevel(oldLevel);
            if (CollectionUtils.isNotEmpty(childDepts)) {
                for (SysDept childDept : childDepts) {
                    String level = childDept.getLevel();
                    if (level.indexOf(oldLevel) == 0) {
                        level = newLevel + level.substring(oldLevel.length());
                        childDept.setLevel(level);
                    }
                }
                sysDeptMapper.updateBatchLevel(childDepts);
            }
        }
    }

    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    private String getLevel(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return null;
        }
        return sysDept.getLevel();
    }
}
