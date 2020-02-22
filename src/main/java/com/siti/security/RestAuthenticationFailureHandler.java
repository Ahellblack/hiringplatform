package com.siti.security;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "login error");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> map = new HashMap<>();
        map.put("status", "fail");
        map.put("info", exception.getMessage());
        String authstr = JSON.toJSONString(map);
        response.getWriter().write(authstr);

    }

}
