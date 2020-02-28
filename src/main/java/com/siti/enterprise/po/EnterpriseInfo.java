package com.siti.enterprise.po;

import javax.persistence.Transient;

public class EnterpriseInfo {

  private long id;
  private String entName;
  private String entFullname;
  private String entAddress;
  private String entPic;
  private String city;
  private String province;
  private double lng;
  private double lat;
  private String contactName;
  private String tel;
  private String emailAddress;
  private String industry;
  private String entType;
  private String staffAmount;
  private java.sql.Timestamp enterTime;
  private String qualiCertificate;
  private long isVerify;
  private java.sql.Timestamp updateTime;
  private String remark;

  @Transient
  private String password;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getEntName() {
    return entName;
  }

  public void setEntName(String entName) {
    this.entName = entName;
  }


  public String getEntFullname() {
    return entFullname;
  }

  public void setEntFullname(String entFullname) {
    this.entFullname = entFullname;
  }


  public String getEntAddress() {
    return entAddress;
  }

  public void setEntAddress(String entAddress) {
    this.entAddress = entAddress;
  }


  public String getEntPic() {
    return entPic;
  }

  public void setEntPic(String entPic) {
    this.entPic = entPic;
  }


  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }


  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }


  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }


  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }


  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }


  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }


  public String getEntType() {
    return entType;
  }

  public void setEntType(String entType) {
    this.entType = entType;
  }


  public String getStaffAmount() {
    return staffAmount;
  }

  public void setStaffAmount(String staffAmount) {
    this.staffAmount = staffAmount;
  }


  public java.sql.Timestamp getEnterTime() {
    return enterTime;
  }

  public void setEnterTime(java.sql.Timestamp enterTime) {
    this.enterTime = enterTime;
  }


  public String getQualiCertificate() {
    return qualiCertificate;
  }

  public void setQualiCertificate(String qualiCertificate) {
    this.qualiCertificate = qualiCertificate;
  }


  public long getIsVerify() {
    return isVerify;
  }

  public void setIsVerify(long isVerify) {
    this.isVerify = isVerify;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
