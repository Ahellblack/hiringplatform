package com.siti.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zyw on 2018-07-13.
 * 登录超时拦截器
 */
@Component
public class SecurityInteceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //System.out.println(httpServletRequest.getRequestURL());
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        //if(!httpServletRequest.getRequestURL().toString().contains("login")){
        Object obj = httpServletRequest.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        if (obj == null) {
            String url = "http://" + httpServletRequest.getLocalAddr() + ":" + httpServletRequest.getLocalPort() + httpServletRequest.getContextPath() + "/login";
            //ajax请求超时判断
            String requestType = httpServletRequest.getHeader("X-Requested-With");
            if ((requestType != null) && requestType.equalsIgnoreCase("XMLHttpRequest")) {
                httpServletResponse.setHeader("sessionstatus", "timeout");
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //System.out.println("postHandle...");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //System.out.println("afterCompletion...");
    }
}
