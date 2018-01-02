package com.zhangwq.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.zhangwq.common.ApplicationContextHelper;
import com.zhangwq.common.JsonData;
import com.zhangwq.common.RequestHolder;
import com.zhangwq.model.SysUser;
import com.zhangwq.service.ISysCoreService;
import com.zhangwq.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AclControlFilter implements Filter {

    private static final Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    private static final String noAuthUrl = "/sys/user/noAuth.page";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet.addAll(exclusionUrlList);
        exclusionUrlSet.add(noAuthUrl);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        Map requestMap = request.getParameterMap();
        if (exclusionUrlSet.contains(servletPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        SysUser sysUser = (SysUser) RequestHolder.getCurrentUser();
        if (sysUser == null) {
            log.info("someone visit {} no auth,param:{}", servletPath, JsonMapper.objToString(requestMap));
            noAuth(request, response);
            return;
        }

        ISysCoreService sysCoreService = ApplicationContextHelper.popBean(ISysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)) {
            log.info("{} visit {} no auth,param:{}", sysUser.getUsername(), servletPath, JsonMapper.objToString(requestMap));
            noAuth(request, response);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        if (servletPath.endsWith(".json")) {
            JsonData jsonData = JsonData.createByError("无权限访问，如需访问，请联系管理员");
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(JsonMapper.objToString(jsonData));
            return;
        }

        clientRedirect(noAuthUrl,response);
    }

    private void clientRedirect(String url, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n" + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n" + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }


    @Override
    public void destroy() {

    }
}
