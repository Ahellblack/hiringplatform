package com.siti.system.ctrl;

import com.github.pagehelper.PageInfo;
import com.siti.system.po.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.siti.common.BaseCtrl;
import com.siti.system.biz.AuthBiz;
import com.siti.system.po.Auth;
import com.siti.system.po.AuthRange;
import com.siti.utils.ReturnMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zyw on 2018/7/25.
 */
@RestController
@RequestMapping("auth")
public class AuthCtrl extends BaseCtrl<AuthBiz, Auth> {

    /**
     * 查看所有权限
     *
     * @param page
     * @param pageSize
     * @param sort
     * @return
     */
    @GetMapping(params = {"page", "pageSize"})
    public PageInfo<Auth> findByPageAndParams(int page, int pageSize, Integer sort) {
        return biz.findBySort(page, pageSize, sort);
    }

    @DeleteMapping
    public Map<String, Object> deleteById(Integer id, Integer pid, Integer sort) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            biz.deleteById(id, pid, sort);
            map = ReturnMessage.deleteSuccess();
        } catch (Exception e) {
            map.put("status", -1);
            map.put("message", e.getLocalizedMessage());
        }
        return map;
    }

    /**
     * 添加权限信息
     *
     * @param pidSort    父节点的排序号
     * @param insertSort 插入节点的排序号
     * @param insertType 添加节点至插入节点之前（1）或之后（2）
     */
    @PostMapping
    public Map<String, Object> saveAuth(Auth auth, Integer pidSort, Integer insertSort, Integer insertType) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            biz.saveAuth(auth, pidSort, insertSort, insertType);
            map = ReturnMessage.addSuccess();
        } catch (Exception e) {
            map.put("status", -1);
            map.put("message", e.getLocalizedMessage());
        }
        return map;
    }

    /**
     * 修改权限信息
     *
     * @param oldPid     修改之前的父节点id
     * @param pidSort    修改之后的父节点排序号
     * @param insertSort 插入节点的排序号
     * @param insertType 添加节点至插入节点之前（1）或之后（2）
     */
    @PutMapping
    public Map<String, Object> updateAuth(Auth auth, Integer oldPid, Integer pidSort, Integer insertSort, Integer insertType) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            biz.updateAuth(auth, oldPid, pidSort, insertSort, insertType);
            map = ReturnMessage.saveSuccess();
        } catch (Exception e) {
            map.put("status", -1);
            map.put("message", e.getLocalizedMessage());
        }
        return map;
    }

    /**
     * 查询权限菜单树（不包含按钮权限）
     *
     * @return
     */
    @GetMapping("updateAuthsTree")
    public List<Auth> getAuthsTreeWithoutBtn() {
        return biz.getAuthsTreeWithoutBtn();
    }

    @GetMapping("sonAuths")
    public List<Auth> getAuthsByPid(Integer pid) {
        return biz.getAuthsByPid(pid);
    }

    /**
     * 根据角色Id获取权限
     */
    @GetMapping("roleId/{roleId}")
    public List<AuthRange> getByRole(@PathVariable int roleId) {
        return biz.getByRoleId(roleId);
    }

    @GetMapping("authsTree")
    public List<Auth> getAuthsTree() {
        return biz.getAuthsTree();
    }

    @GetMapping("authsButton")
    public List<Auth> getAuthsButton() {
        return biz.getAuthsButton();
    }


    /**
     * 获取菜单
     *
     * @return
     */
    @GetMapping("menu")
    public List<Auth> getMenu() {
        List<Role> rolelist = LoginCtrl.getLoginUserInfo().getRoles();
        List<Integer> roleIds = new ArrayList<Integer>();
        rolelist.forEach(data->{
            roleIds.add(data.getId());
        });
        return biz.getMenu(roleIds);
    }


    public String getModelName() {
        return "权限管理";
    }

}
