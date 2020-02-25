package com.siti.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ht on 2018/1/24.
 */
@Configuration
public class YmlConfig extends WebMvcConfigurerAdapter {

    /**
     * 保留N小时内的无效文件
     */
    @Value("${filePath.invalidFileDelete}")
    private Integer invalidFileDelete;
    /**
     * 文件服务器地址
     */
    @Value("${filePath.files}")
    private String filepath;

    /**
     * 文件服务器地址
     */
    @Value("${filePath.images}")
    private String imagepath;
    /**
     * 版本号
     */
    @Value("${version.pcversion}")
    private String pcversion;

    /**
     * 小版本号
     */
    @Value("${version.build}")
    private String build;

    /**
     * 容联云accountSid
     */
    @Value("${msg.accountSid}")
    private String accountSid;

    /**
     * 容联云authToken
     */
    @Value("${msg.authToken}")
    private String authToken;

    /**
     * 容联云appId
     */
    @Value("${msg.appId}")
    private String appId;

    /**
     * 容联云serberRip
     */
    @Value("${msg.serberRip}")
    private String serberRip;

    /**
     * 容联云serverPort
     */
    @Value("${msg.serverPort}")
    private String serverPort;

    /**
     * 容联云msgModelId
     */
    @Value("${msg.msgModelId}")
    private String msgModelId;


    public Integer getInvalidFileDelete() {
        return invalidFileDelete;
    }

    public void setInvalidFileDelete(Integer invalidFileDelete) {
        this.invalidFileDelete = invalidFileDelete;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getPcversion() {
        return pcversion;
    }

    public void setPcversion(String pcversion) {
        this.pcversion = pcversion;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSerberRip() {
        return serberRip;
    }

    public void setSerberRip(String serberRip) {
        this.serberRip = serberRip;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getMsgModelId() {
        return msgModelId;
    }

    public void setMsgModelId(String msgModelId) {
        this.msgModelId = msgModelId;
    }
}
