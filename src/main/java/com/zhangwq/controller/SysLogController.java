package com.zhangwq.controller;

import com.zhangwq.beans.PageQuery;
import com.zhangwq.beans.PageResult;
import com.zhangwq.common.JsonData;
import com.zhangwq.model.SysLogWithBLOBs;
import com.zhangwq.param.SearchLogParam;
import com.zhangwq.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @RequestMapping(value = "/page.json")
    @ResponseBody
    public JsonData createSysDept(SearchLogParam searchLogParam, PageQuery pageQuery){
        PageResult<SysLogWithBLOBs> pageResult =  sysLogService.searchPageList(searchLogParam,pageQuery);
        return JsonData.createBySuccess(pageResult);
    }
    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@RequestParam("id") int id) {
        sysLogService.recover(id);
        return JsonData.createBySuccess();
    }
}
