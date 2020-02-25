package com.siti.system.po;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name="sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String realName;
    private String password;
    private String sex;
    private String image;
    private String userType;

    @Transient
    private String imageURL;
    private String idCard;   //  身份证号
    private String emailAddr;     //邮箱地址
    private String phoneNum;   //  手机号码

    @Transient
    private Integer roleId; // 主要角色ID
    private String roleCode; // 主要角色
    @Transient
    private String roleName; // 主要角色
    @Transient
    private String majorRegion; // 负责区域
    @Transient
    private String areas; // 负责区域
    private Integer status;    // 用户状态：0：已删除；1：有效；2：冻结'
    private Integer updateBy;    //操作用户ID
    private String pushId; // 推送ID
    @Transient
    private String updateName;    //操作用户
    private Date updateTime;    //更新时间
    private String remark;  // 备注信息
    @Transient
    private List<Role> roles;
    @Transient
    private Integer canUpdate;   // 是否可删除
    @Transient
    private Integer canDelete;    //  是否可修改


    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.realName = user.getRealName();
        this.password = user.getPassword();
        this.sex = user.getSex();
        this.image = user.getImage();
        this.imageURL = user.getImageURL();
        this.idCard = user.getIdCard();
        this.phoneNum = user.getPhoneNum();
        this.majorRegion = user.getMajorRegion();
        this.roleCode = user.getRoleCode();
        this.roleId = user.getRoleId();
        this.status = user.getStatus();
        this.updateBy = user.getUpdateBy();
        this.updateTime = user.getUpdateTime();
        this.pushId = user.getPushId();
        this.updateName = user.getUpdateName();
        this.remark = user.getRemark();
        this.roles = user.getRoles();
        this.emailAddr = user.getEmailAddr();
        this.userType = user.getUserType();
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRealName() {
        return realName;
    }

    public User setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public User setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getImage() {
        return image;
    }

    public User setImage(String image) {
        this.image = image;
        return this;
    }


    public String getImageURL() {
        return imageURL;
    }

    public User setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public String getIdCard() {
        return idCard;
    }

    public User setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public User setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
        return this;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public User setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public User setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public User setRoleCode(String roleCode) {
        this.roleCode = roleCode;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public User setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public String getMajorRegion() {
        return majorRegion;
    }

    public User setMajorRegion(String majorRegion) {
        this.majorRegion = majorRegion;
        return this;
    }

    public String getAreas() {
        return areas;
    }

    public User setAreas(String areas) {
        this.areas = areas;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public User setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public User setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    public String getPushId() {
        return pushId;
    }

    public User setPushId(String pushId) {
        this.pushId = pushId;
        return this;
    }

    public String getUpdateName() {
        return updateName;
    }

    public User setUpdateName(String updateName) {
        this.updateName = updateName;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public User setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public User setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public User setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Integer getCanUpdate() {
        return canUpdate;
    }

    public User setCanUpdate(Integer canUpdate) {
        this.canUpdate = canUpdate;
        return this;
    }

    public Integer getCanDelete() {
        return canDelete;
    }

    public User setCanDelete(Integer canDelete) {
        this.canDelete = canDelete;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
