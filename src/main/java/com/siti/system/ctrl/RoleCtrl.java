package com.siti.system.ctrl;

import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.siti.common.BaseCtrl;
import com.siti.system.biz.RoleBiz;
import com.siti.system.mapper.UserRoleMapper;
import com.siti.system.po.Role;
import com.siti.utils.ReturnMessage;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zyw on 2018/7/25.
 */
@RestController/*Controller+ResponseBody等价于这个*/
@RequestMapping("role")
public class RoleCtrl extends BaseCtrl<RoleBiz, Role> {

    @Resource
    private UserRoleMapper userRoleBiz;


    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    @PostMapping("saveRole")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> saveRole(@RequestBody Role role) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            role.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
            String[] authIds = role.getAuthIds();
            biz.saveRole("ADD", role, authIds);
            return ReturnMessage.addSuccess();
        } catch (Exception e) {
            map.put("status", -1);
            map.put("message", e.getLocalizedMessage());
        }
        return map;
    }

    /**
     * 更新角色
     *
     * @param role
     * @return responseStatus指的是自定义一些异常，同时在页面上进行解释
     */
    @PutMapping("updateRole")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> updateRole(@RequestBody Role role) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            role.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
            String[] authIds = role.getAuthIds();
            biz.saveRole("UPDATE", role, authIds);
            return ReturnMessage.addSuccess();
        } catch (Exception e) {
            map.put("status", -1);
            map.put("message", e.getLocalizedMessage());
        }
        return map;
    }

    /**
     * 根据角色名查找角色
     *
     * @param page
     * @param pageSize
     * @param roleName
     * @return
     */
    @GetMapping(params = {"page", "pageSize"})
    public PageInfo<Role> findByPageAndParams(int page, int pageSize, String roleName) {
        return biz.findByNameLike(page, pageSize, roleName);
    }

    /**
     * 删除角色
     *
     * @param role
     * @return
     */
    @DeleteMapping("deleteById")
    public Map<String, Object> deleteById(Integer id, Role role) {
        Map<String, Object> map = new HashMap<String, Object>();
        biz.deleteById(id);
        return ReturnMessage.deleteSuccess();
    }

    /**
     * 根据当前用户角色获取低等级角色(返回的角色信息里包括其用户角色自身)
     * 查看权限
     *
     * @return
     */
    @GetMapping("lowGradeRole")
    public Set<Role> getLowGradeRole() {
        List<Integer> roleIds = userRoleBiz.getRoleIdByUserId(1);
        Set<Role> roles = new HashSet<Role>();
        //获取所有权限低于当前用户的角色
        for (int i = 0; i < roleIds.size(); i++) {
            List<Role> roleList = biz.getLowGradeRole(roleIds.get(i));
            roles.addAll(roleList);
        }
        return roles;
    }

    /**
     * 查看权限
     *
     * @return
     */
    @GetMapping("rolesByAuthId")
    public List<Role> getRolesByAuthId(Integer authId) {
        return biz.getRolesByAuthId(authId);
    }

    /**
     * 获取所有角色
     *
     * @return
     */
    @GetMapping("getAllRoles")
    public List<Role> getAllRoles() {
        return biz.getRoles(null);
    }

    /**
     * 系统管理-运维用户-获取组织名称*/
    @GetMapping("getOrgList")
    public List<Map<String, Object>> getOrgList(String type) {
        return biz.getOrgNameByRole(type);
    }

    /**
     * 根据类型获取角色
     *
     * @return
     */
    @GetMapping("getRoleByType")
    public List<Role> getRoleByType (String type) {
        return biz.getRoles(type);
    }



    public String getModelName() {
        return "角色管理";
    }

}
