package com.company.app.common.interceptor;


import com.company.app.common.security.AuthorityUtil;
import com.company.app.common.utils.HttpResponseUtil;
import com.company.app.common.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    private LogUtil logUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String ip = HttpResponseUtil.getIpAddress(request);

        logUtil.info(this, " @ "
                + ip + " 调用" + request.getMethod()
                + "接口 [ " + request.getRequestURI() + " ]  参数"
                + (StringUtils.hasLength(request.getQueryString()) ? " [ " + request.getQueryString() + " ]" : "空") +
                ("POST".equals(request.getMethod()) ? "请求体*" : ""));

        if (authorityUtil.checkAuthority(handler,request.getSession())){
            return true;
        } else {
            askLogin(response,"无权访问");
            return false;
        }
    }


    /**
     * 返回请登录信息
     */
    private void askLogin(HttpServletResponse response, String message) throws IOException {
        response.sendError(401);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(message);
    }
}