package com.zhangwq.controller;

import com.zhangwq.common.JsonData;
import com.zhangwq.param.DeptParam;
import com.zhangwq.service.ISysDeptService;
import com.zhangwq.service.ISysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysTreeService sysTreeService;

    @RequestMapping(value = "/dept.page")
    public ModelAndView deptPage(DeptParam deptParam){
        return new ModelAndView("dept");
    }

    @RequestMapping(value = "/save.json")
    @ResponseBody
    public JsonData createSysDept(DeptParam deptParam){
        sysDeptService.saveDept(deptParam);
        return JsonData.createBySuccess();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData getDeptTree(){
        return JsonData.createBySuccess(sysTreeService.getDeptTree());
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateSysDept(DeptParam deptParam){
        sysDeptService.updateDept(deptParam);
        return JsonData.createBySuccess();
    }
}
