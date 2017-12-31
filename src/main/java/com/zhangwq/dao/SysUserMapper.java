package com.zhangwq.dao;

import com.zhangwq.beans.PageQuery;
import com.zhangwq.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findByKeyword(@Param("keyword") String keyword);

    int countByEmail(@Param("mail") String mail,@Param("id") Integer id);

    int countByTelPhone(@Param("telphone") String telphone,@Param("id") Integer id);

    int countByDeptId(@Param("deptId") Integer deptId);

    List<SysUser> getPageByDeptId(@Param("deptId") Integer deptId,@Param("pageQuery") PageQuery pageQuery);

    List<SysUser> getByUserIds(@Param("userIdList") List<Integer> userIdList);

    List<SysUser> getAllUser();
}