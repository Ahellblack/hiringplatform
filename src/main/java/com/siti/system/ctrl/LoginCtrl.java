package com.siti.system.ctrl;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.siti.security.LoginUserInfo;

import javax.servlet.http.HttpSession;

/**
 * Created by zyw on 2017/9/18.
 * 登录
 */
@RestController
@RequestMapping("login")
public class LoginCtrl {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @PostMapping
    public static LoginUserInfo getLoginUserInfo() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        return (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @GetMapping(value = "/logout")
    public String logout(HttpSession session) {
        SecurityContextHolder.clearContext();
        return "login";
    }

}
