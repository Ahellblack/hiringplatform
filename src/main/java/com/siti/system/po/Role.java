package com.siti.system.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;//角色名，唯一

    private String code;//角色编码，唯一

    private String type;//角色类型：admin管理员；manage管理角色；bike单车企业角色

    private String descr;

    private Integer updateBy;

    private Date updateTime;    //更新时间

    @Transient
    private Integer canUpdate;   // 是否可删除

    @Transient
    private Integer canDelete;    //  是否可修改

    @Transient
    private String[] authIds;  // 权限

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Integer canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Integer getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Integer canDelete) {
        this.canDelete = canDelete;
    }

    public String[] getAuthIds() {
        return authIds;
    }

    public void setAuthIds(String[] authIds) {
        this.authIds = authIds;
    }
}
