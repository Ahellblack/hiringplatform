package com.siti.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by zyw on 2018-07-13.
 * spring security config
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private MyAuthenticationProvider myAuthenticationProvider;

    private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Autowired
    public void setMyAuthenticationProvider(MyAuthenticationProvider myAuthenticationProvider) {
        this.myAuthenticationProvider = myAuthenticationProvider;
    }

    @Autowired
    public void setRestAuthenticationSuccessHandler(RestAuthenticationSuccessHandler restAuthenticationSuccessHandler) {
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login/logout"
                , "/user/getEmailCheckCode"
                , "/user/checkInLaw"
                , "/user/updatePwdByEmailCode"
                , "/uploadFiles"
                , "/sysInfo/getPCversion"
                , "/sysInfo/getAPPversion"
                , "/enterprise/**"
                , "/release/**"
                , "/position/**"
                , "/msg/**"
        );
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();        // 解决前端无法使用iframe的问题
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/*").permitAll()
                // 其他地址访问需要验证权限
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //.loginPage("/login")// 指定登录页
                //.successForwardUrl("/indexHome")// 登录成功后的主页
                .successHandler(restAuthenticationSuccessHandler)
                .failureHandler(new RestAuthenticationFailureHandler())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(myAuthenticationProvider);
    }

}
