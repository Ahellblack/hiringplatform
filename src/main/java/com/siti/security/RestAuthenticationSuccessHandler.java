package com.siti.security;

import com.alibaba.fastjson.JSON;
import com.sun.xml.internal.bind.v2.TODO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.siti.system.biz.UserBiz;
import com.siti.utils.DateOrTimeTrans;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserBiz userBiz;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //System.out.println(request.getCookies()[0].getName() + "-" + request.getCookies()[0].getValue());
        //response.addHeader(request.getCookies()[0].getName(), request.getCookies()[0].getValue());

        //获得授权后可得到用户信息
        LoginUserInfo userDetails = (LoginUserInfo) authentication.getPrincipal();
        //输出登录提示信息
        String ipAddress = getIpAddress(request);
        String userAgent = request.getHeader("User-Agent"); // 请求信息（终端类型(1Android,2iPhone,3iPad,4PC)）
        logger.info("[" + userDetails.getUserName() + "] logged in the system at " + DateOrTimeTrans.nowTimetoString() + ", IP:" + ipAddress + ",User-Agent:" + userAgent);
        //#TODO
        //登录日志
        /*loginRecordBiz.save(new LoginRecord(userDetails.getId(), userAgent, ipAddress));*/
        if (userDetails.getPushId() != null && !"".equals(userDetails.getPushId())) {
            userBiz.updatePushIdByUserName(userDetails.getPushId(), userDetails.getUserName());
        }
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> map = new HashMap<>();
        map.put("status", "success");
        //map.put("cookieName", request.getCookies()[0].getName());//JSESSIONID
        //map.put("cookieValue", request.getCookies()[0].getValue());//48F3BC2ED285777FE4DE33EFAFC73473
        map.put("info", authentication.getPrincipal());
        if(userDetails.getRoleCode().equals("fence")){
            map.put("fence", 1);
        }else{
            map.put("fence", 0);
        }
        String authstr = JSON.toJSONString(map);
        response.getWriter().write(authstr);
        //response.getWriter().write("sucess");
    }

    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
