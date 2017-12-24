package com.zhangwq.common;

import com.zhangwq.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final String START_TIME = "startTime";
    @Override
    //请求前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Map params = request.getParameterMap();
        log.info("request start url:{},params:{}",url, JsonMapper.objToString(params));
        long startTime= System.currentTimeMillis();
        request.setAttribute(START_TIME,startTime);
        return true;
    }

    @Override
    //请求正常结束
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    //任何情况下请求结束
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        long startTime = (Long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        log.info("request completed url:{},cost:{}",url, endTime - startTime);
        this.removeThreadLocalInfo();
    }

    private static void removeThreadLocalInfo(){
        RequestHolder.remove();
    }
}
