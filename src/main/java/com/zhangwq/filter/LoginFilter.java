package com.zhangwq.filter;

import com.sun.deploy.net.HttpResponse;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        SysUser sysUser = (SysUser) request.getSession().getAttribute("user");
        if (sysUser == null) {
            String path = "/signin.jsp";
            response.sendRedirect(path);
            return;
        }

        RequestHolder.add(sysUser);
        RequestHolder.add(request);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
