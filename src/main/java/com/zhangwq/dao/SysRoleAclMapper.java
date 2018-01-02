package com.zhangwq.dao;

import com.zhangwq.model.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import javax.xml.ws.Service;
import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    List<Integer> getAclIdListByRoleIds(@Param("roleIds") List<Integer> roleIds);

    void deleteByRoleId(@Param("roleId") Integer roleId);

    void insertBatch(@Param("roleAclList") List<SysRoleAcl> roleAclList);

    List<Integer> getRoleIdListByAclId(@Param("aclId") Integer aclId);

}