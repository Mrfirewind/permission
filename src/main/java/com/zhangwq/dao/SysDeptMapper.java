package com.zhangwq.dao;

import com.zhangwq.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> getAllDept();

    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    void updateBatchLevel(@Param("sysDeptList") List<SysDept> sysDepts);

    int countByNameAndParentId(@Param("parentId") int parentId, @Param("name") String name, @Param("id") Integer id);

    int countByParentId(@Param("parentId") int parentId);
}