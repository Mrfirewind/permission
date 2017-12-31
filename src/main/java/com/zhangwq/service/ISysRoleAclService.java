package com.zhangwq.service;

import java.util.List;

public interface ISysRoleAclService {

     void changeRoleAcls(Integer roleId, List<Integer> aclIdList);

}
