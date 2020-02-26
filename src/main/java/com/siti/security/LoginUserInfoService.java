package com.siti.security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.siti.system.mapper.AuthMapper;
import com.siti.system.mapper.RoleMapper;
import com.siti.system.mapper.UserMapper;
import com.siti.system.po.Auth;
import com.siti.system.po.Role;
import com.siti.system.po.User;
import com.siti.utils.FileCopy;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyw on 2018-07-13.
 * 登录验证
 */
@Service
public class LoginUserInfoService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private AuthMapper authMapper;

    private final static String HEADPATHURL = "recruit";

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user;
        try {
            user = userMapper.findUserByUserName(userName);
        } catch (Exception e) {
            throw new UsernameNotFoundException("服务器异常，登录失败！");
        }
        if (user == null || user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户不存在！");
        } else {
            if (user.getImage() != null && !"".equals(user.getImage())) {
                user.setImageURL(FileCopy.setFilePath(HEADPATHURL, user.getImage().split(";")).get(0));
            } else {
                user.setImageURL("");
            }
            try { // role不要了
                /*List<Role> roles = roleMapper.getByUserId(user.getId());

                List<Integer> roleIds = new ArrayList<Integer>();
                for (Role role : roles) {
                    roleIds.add(role.getId());
                }
                List<Auth> menuList = authMapper.getMenu(roleIds);   //  获取角色的目录权限
                List<Auth> authList = authMapper.getPermissionByRoleId(roleIds);    //  获取所有角色的所有授权信息*/
                return new LoginUserInfo(user, null, null);
            } catch (Exception e) {
                throw new UsernameNotFoundException("登录失败，用户角色异常！");
            }
        }
    }

}
