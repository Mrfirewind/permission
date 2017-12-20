package com.zhangwq.common;

import com.zhangwq.exception.ParamException;
import com.zhangwq.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 如果类被spring管理，会在http请求返回时，被该类捕捉住
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object, Exception e) {
        String url = request.getRequestURL().toString();
        String defaultMsg = "System Error";
        ModelAndView mv;
        if (url.endsWith(".json")) {
            if (e instanceof PermissionException || e instanceof ParamException) {
                JsonData result = JsonData.createByError(e.getMessage());
                mv = new ModelAndView("jsonView", result.toMap());
            } else {
                log.error("unknow json exception，url" + url, e);
                JsonData result = JsonData.createByError(defaultMsg);
                mv = new ModelAndView("jsonView", result.toMap());
            }
        } else if (url.endsWith(".page")) {
            log.error("unknow page exception，url" + url, e);
            JsonData result = JsonData.createByError(defaultMsg);
            mv = new ModelAndView("exception", result.toMap());
        }else{
            log.error("unknow exception，url" + url, e);
            JsonData result = JsonData.createByError(defaultMsg);
            mv = new ModelAndView("jsonView", result.toMap());
        }
        return mv;
    }
}
