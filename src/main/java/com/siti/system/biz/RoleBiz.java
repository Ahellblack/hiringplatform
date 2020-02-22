package com.siti.system.biz;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siti.common.BaseBiz;
import com.siti.common.constant.ConstantsButton;
import com.siti.system.ctrl.LoginCtrl;
import com.siti.system.mapper.RoleAuthMapper;
import com.siti.system.mapper.RoleMapper;
import com.siti.system.mapper.UserRoleMapper;
import com.siti.system.po.Role;
import com.siti.system.po.RoleAuth;
import com.siti.system.po.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zyw on 2018/7/25.
 */
@Service
public class RoleBiz extends BaseBiz<RoleMapper, Role> {
    @Autowired
    private RoleAuthMapper roleAuthDao;
    @Autowired
    private RoleMapper roleDao;
    @Autowired
    private AuthBiz authBiz;

    @Autowired
    private UserRoleMapper userRoleDao;


    /**
     * 保存角色,保存权限关系
     *
     * @param role
     */
    public void saveRole(String operation, Role role, String[] authIds) {
        if (roleDao.findExistByCode(role.getCode(), role.getId())) {
            throw new RuntimeException("角色编码重复");
        }
        if (roleDao.findExistByName(role.getName(), role.getId())) {
            throw new RuntimeException("角色名称重复");
        }
        if (role.getType() == null || "".equals(role.getType())
                || (!"admin".equals(role.getType()) &&!"manage".equals(role.getType()) && !"bike".equals(role.getType()))) {
            throw new RuntimeException("角色类型有误");
        }
        if ("ADD".equals(operation)) {
            dao.insert(role);
        } else {
            roleAuthDao.deleteByRoleId(role.getId());
        }
        if (authIds != null) {
            Map<Integer, String> rangeMap = new HashMap<Integer, String>();
            for (int i = 0; i < authIds.length; i++) {
                String[] str = authIds[i].split("#");
                if (str.length > 1) {
                    rangeMap.put(Integer.parseInt(str[0]), str[1]);
                }
                RoleAuth roleAuth = new RoleAuth();
                roleAuth.setRoleId(role.getId());
                roleAuth.setUpdateBy(LoginCtrl.getLoginUserInfo().getId());
                roleAuth.setAuthId(Integer.parseInt(str[0]));
                roleAuth.setRanges(rangeMap.get(Integer.parseInt(str[0])));
                roleAuthDao.insert(roleAuth);
            }
        }
        if ("ADD".equals(operation)) return;
        dao.updateByPrimaryKey(role);
    }

    /**
     * 根据角色名查找角色的信息
     *
     * @param page
     * @param pageSize
     * @param roleName
     * @return
     */
    public PageInfo<Role> findByNameLike(int page, int pageSize, String roleName) {
        if (roleName == null || "".equals(roleName.trim())) {
            roleName = null;
        } else {
            roleName = roleName.trim();
        }
        User user = LoginCtrl.getLoginUserInfo();
        if (user == null) {
            return null;
        }
        // 角色的增删改不分组织，所以要么是数据仅限自己，要么就是全部
        String ranges = authBiz.getAuthRange(ConstantsButton.SEARCH_ROLE);
        if (ranges == null) {
            return new PageInfo<>(new ArrayList<>());
        }
        PageHelper.startPage(page, pageSize);
        List<Role> roleList = dao.findByNameLike(ranges, user.getId(), roleName);
        String rangesUpdate = authBiz.getAuthRange(ConstantsButton.UPDATE_ROLE);
        String rangesDelete = authBiz.getAuthRange(ConstantsButton.DELETE_ROLE);
        for (Role role : roleList) {
            role.setCanUpdate(0);
            role.setCanDelete(0);
            // 更新
            if (rangesUpdate != null) {
                if ("all".equals(rangesUpdate)) {
                    role.setCanUpdate(1);
                } else {
                    if (user.getId().equals(role.getUpdateBy())) {
                        role.setCanUpdate(1);
                    }
                }
            }
            // 删除
            if (rangesDelete != null) {
                if ("all".equals(rangesDelete)) {
                    role.setCanDelete(1);
                } else {
                    if (user.getId().equals(role.getUpdateBy())) {
                        role.setCanDelete(1);
                    }
                }
            }
        }
        return new PageInfo<Role>(roleList);
    }

    public void deleteById(Object id) {
        Integer idd = Integer.parseInt(id.toString());
        roleAuthDao.deleteByRoleId(idd);
        userRoleDao.deleteByRoleId(idd);
        dao.deleteByPrimaryKey(id);
    }

    /**
     * 根据当前角色ID获取该ID对应的角色信息和低等级角色
     *
     * @param roleId 角色ID
     * @return 返回角色列表，其中包括roleId对应的角色，和等级（权限）低于该角色的所有角色。
     */
    public List<Role> getLowGradeRole(int roleId) {
        List<Integer> roleIds = roleAuthDao.getLowGradeRoleId(roleId);
        return dao.findByIds(roleIds);
    }

    public List<Role> getRolesByAuthId(Integer authId) {
        return dao.getRolesByAuthId(authId);
    }

    public List<Role> getRoles(String type) {
        return dao.getRoles(type);
    }

    public List<Map<String, Object>> getOrgNameByRole(String type) {
        return dao.getOrgNameByRole(type);
    }
}
