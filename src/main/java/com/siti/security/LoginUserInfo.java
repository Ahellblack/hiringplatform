package com.siti.security;

import com.siti.system.po.Auth;
import com.siti.system.po.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;


import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * Created by zyw on 2018-07-13.
 */
public class LoginUserInfo extends User implements UserDetails, Principal {

    /**
     * 目录权限列表
     */
    private List<Auth> menuList;
    /**
     * 所有角色的所有授权信息
     */
    private List<Auth> authList;

    private String auth;

    public LoginUserInfo() {
    }

    public LoginUserInfo(User user, List<Auth> menuList, List<Auth> authList) {
        super(user);
        this.menuList = menuList;
        this.authList = authList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authList == null || authList.size() < 1) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
        }
        StringBuilder commaBuilder = new StringBuilder();
        for (Auth auth : authList) {
            if (auth.getCode() == null || "".equals(auth.getCode())) {
                continue;
            }
            commaBuilder.append(auth.getCode()).append(",");
        }
        String authorities = null;
        if (commaBuilder.length() > 0) {
            authorities = commaBuilder.substring(0, commaBuilder.length());
        }
        this.auth = authorities;
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    public List<Auth> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Auth> menuList) {
        this.menuList = menuList;
    }

    public List<Auth> getAuthList() {
        return authList;
    }

    public void setAuthList(List<Auth> authList) {
        this.authList = authList;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
