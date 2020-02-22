package com.siti.system.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.siti.system.biz.SysInfoBiz;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zyw on 2018/9/13.
 * 系统信息
 */
@RestController
@RequestMapping("sysInfo")
public class SysInfoCtrl {
    @Resource
    private SysInfoBiz sysInfoBiz;

    /**
     * 获取版本号
     *
     * @return
     */
    @GetMapping(value = "getPCversion")
    public Map<String, Object> getPcVersion() {
        return sysInfoBiz.searchUpdateLog("PC");
    }

    /**
     * 查询 更新日志(PC)
     *
     * @return
     */
    @GetMapping(value = "getUpdateLogPC")
    public List<String> getUpdateLogPC(String version) {
        sysInfoBiz.judgeIfRead(version, "PC");
        return sysInfoBiz.getLogText(version, "PC");
    }

    /**
     * 查询 更新日志(APP)
     *
     * @return
     */
    @GetMapping(value = "getUpdateLogAPP")
    public List<String> getUpdateLogAPP(String version) {
        sysInfoBiz.judgeIfRead(version, "APP");
        return sysInfoBiz.getLogText(version, "APP");
    }

}
