package com.zhangwq.service;

import com.zhangwq.param.DeptParam;

public interface ISysDeptService {

    void saveDept(DeptParam deptParam);

     void updateDept(DeptParam deptParam);

     void deleteDept(Integer deptId);
}
