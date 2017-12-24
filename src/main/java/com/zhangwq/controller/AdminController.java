package com.zhangwq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @RequestMapping(value = "/index.page")
    public ModelAndView indexPage(){
        return new ModelAndView("admin");
    }

}
