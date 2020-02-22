package com.siti.enterprise.po;

public class PositionInfo {

  private long id;
  private String postName;
  private String dscrpt;
  private String postType;
  private java.sql.Timestamp inputTime;
  private java.sql.Timestamp updateTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getPostName() {
    return postName;
  }

  public void setPostName(String postName) {
    this.postName = postName;
  }


  public String getDscrpt() {
    return dscrpt;
  }

  public void setDscrpt(String dscrpt) {
    this.dscrpt = dscrpt;
  }


  public String getPostType() {
    return postType;
  }

  public void setPostType(String postType) {
    this.postType = postType;
  }


  public java.sql.Timestamp getInputTime() {
    return inputTime;
  }

  public void setInputTime(java.sql.Timestamp inputTime) {
    this.inputTime = inputTime;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }

}
