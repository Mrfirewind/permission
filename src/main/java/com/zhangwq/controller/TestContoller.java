package com.zhangwq.controller;

import com.zhangwq.exception.PermissionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class TestContoller {
    @RequestMapping("/test.json")
    @ResponseBody
    public String test() {
        throw new PermissionException("test exception");
//        return "permisson";
    }
}
