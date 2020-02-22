package com.siti.utils;

import java.io.Serializable;

/**
 * Created by 20517 on 2018/2/7.
 * 邮件收件人、收件人信息
 */
public class MailUsr implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 收件地址
     */
    private String address;
    /**
     * 发送收件密码（发送方的邮件需要）
     */
    private String password;
    /**
     * 用户称呼
     */
    private String personal;
    /**
     * 发送类型：TO: 收件人；CC: 抄送；BCC: 密送
     * 为null或空的话，则是发件人信息
     */
    private String type;

    public MailUsr() {
    }

    public MailUsr(String address, String password) {
        this.address = address;
        this.password = password;
    }

    public MailUsr(String address, String personal, String type) {
        this.address = address;
        this.personal = personal;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
