package com.siti.system.vo;

/**
 * Created by DC on 2019/10/25 - 13:35- {TIME}.
 **/
//@Data
//@NoArgsConstructor
public class OrgFenceVo {
    private String name;
    private int userId;
    private String orgId;
    private String roleId;
    private String majorRegion;
    private String shortName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMajorRegion() {
        return majorRegion;
    }

    public void setMajorRegion(String majorRegion) {
        this.majorRegion = majorRegion;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
