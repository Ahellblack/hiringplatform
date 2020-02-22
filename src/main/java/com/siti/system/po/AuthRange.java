package com.siti.system.po;

import java.io.Serializable;

/**
 * Created by zyw on 2017/11/8.
 * 无数据库对应表
 */
public final class AuthRange implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 组织ID
     */
    private Integer orgId;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 权限ID
     */
    private Integer id;
    /**
     * 权限类型：1 菜单，3 方法
     */
    private Integer type;
    /**
     * 是否可设置范围：1是0否
     */
    private Integer setRange;
    /**
     * 父级权限ID
     */
    private Integer pid;
    /**
     * 权限名称
     */
    private String authName;
    /**
     * 对应的数据范围
     */
    private String ranges;
    /**
     * 范围名称
     */
    private String rangeName;

    public Integer getUserId() {return userId;}

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSetRange() {
        return setRange;
    }

    public void setSetRange(Integer setRange) {
        this.setRange = setRange;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    @Override
    public String toString() {
        return "AuthRange{" +
                "userId=" + userId +
                ", orgId=" + orgId +
                ", roleId=" + roleId +
                ", id=" + id +
                ", type=" + type +
                ", setRange=" + setRange +
                ", pid=" + pid +
                ", authName=" + authName +
                ", rangeName=" + rangeName +
                ", ranges='" + ranges + '\'' +
                '}';
    }
}
