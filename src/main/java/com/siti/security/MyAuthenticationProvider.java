package com.siti.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.siti.utils.BASE64Util;
import com.siti.utils.Md5Utils;

import java.util.Collection;

/**
 * Created by zyw on 2018-07-13.
 * 自定义验证
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    private LoginUserInfoService myUserDetailsService;

    @Autowired
    public void setMyUserDetailsService(LoginUserInfoService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String usernameTemp = authentication.getName();
        String logPwd = BASE64Util.decode((String) authentication.getCredentials());
        if (logPwd == null) {
            throw new BadCredentialsException("登录失败，请重新登录！");
        }
        String username;
        String password;
        String pushId = null;
        if (logPwd.endsWith("#THT#")) {
            System.out.println("跳转登录");
            username = usernameTemp;
            password = logPwd.substring(0, logPwd.length() - 5);
        } else {
            System.out.println("普通登录");
            //String temp = logPwd.substring(logPwd.indexOf("&") + 1, logPwd.lastIndexOf("&"));
            String pwd = logPwd/*BASE64Util.decode(temp)*/;
            if (pwd == null) {
                throw new BadCredentialsException("登录失败，请重新登录！");
            }
            if (usernameTemp.contains("&")) {
                username = usernameTemp.substring(0, usernameTemp.lastIndexOf("&"));
                pushId = usernameTemp.substring(usernameTemp.lastIndexOf("&") + 1, usernameTemp.length());
            } else {
                username = usernameTemp;
            }
            password = Md5Utils.encryptString(pwd);
        }
        LoginUserInfo userInfo = (LoginUserInfo) myUserDetailsService.loadUserByUsername(username);
        if (userInfo == null || "".equals(username)) {
            throw new BadCredentialsException("用户不存在！");
        } else if ("".equals(username) || userInfo.getStatus() == 0) {
            throw new BadCredentialsException("用户不存在！");
        } else if (userInfo.getStatus() == 2) {
            throw new BadCredentialsException("用户已被冻结！缘由：" + (userInfo.getRemark() == null ? "" : userInfo.getRemark()));
        } else {
            // 验证密码是否正确
            if (!userInfo.getPassword().equals(password)) {
                throw new BadCredentialsException("用户名或密码错误！");
            }
        }
        if (pushId != null) {
            userInfo.setPushId(pushId);
            System.out.println("pushId: " + pushId);
        }
        userInfo.setPassword(null);
        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userInfo, logPwd, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
