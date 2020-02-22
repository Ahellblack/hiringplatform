package com.siti.system.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zyw on 2018/9/13.
 * 更新日志已读情况
 */
@Table(name = "sys_update_log_read")
public class UpdateLogRead {
    /**
     * 版本号
     */
    @Id
    private String version;
    @Id
    private String terminal;
    /**
     * 已阅读的用户ID
     */
    private String userIds;
    /**
     * 更新时间
     */
    private String updateTime;



    public String getUpdateTime() {
        return updateTime;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getUpdateTime(Date date) {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
