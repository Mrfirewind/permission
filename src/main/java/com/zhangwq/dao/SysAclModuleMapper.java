package com.zhangwq.dao;

import com.zhangwq.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    int countByNameAndParentId(@Param("parentId") int parentId, @Param("name") String name, @Param("id") Integer id);

    List<SysAclModule> getChildDeptListByLevel(@Param("level") String level);

    void updateBatchLevel(@Param("sysAclModules") List<SysAclModule> sysAclModules);

    List<SysAclModule> getAllAclModule();

    int countByParentId(@Param("parentId") int parentId);
}