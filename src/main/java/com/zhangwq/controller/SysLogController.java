package com.zhangwq.controller;

import com.zhangwq.service.ISysLogService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@RequestMapping("/sys/log")
public class SysLogController {

    private ISysLogService sysLogService;
}
