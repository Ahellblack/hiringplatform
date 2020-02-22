package com.siti.system.biz;


import com.siti.common.constant.ConstantPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.siti.config.YmlConfig;
import com.siti.system.ctrl.LoginCtrl;
import com.siti.system.mapper.UpdateLogReadMapper;
import com.siti.system.po.UpdateLogRead;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by zyw on 2018/9/13.
 * 查询 更新日志
 */
@Service
@Transactional
public class SysInfoBiz {

    @Resource
    private YmlConfig ymlConfig;
    @Resource
    private UpdateLogReadMapper updateLogReadMapper;

    public boolean judgeIfRead(String version, String terminal) {
        if (version == null || "".equals(version.trim())) {
            return true;
        }
        UpdateLogRead updateLogReadTemp = new UpdateLogRead();
        updateLogReadTemp.setVersion(version);
        updateLogReadTemp.setTerminal(terminal);
        UpdateLogRead updateLogRead = updateLogReadMapper.selectByPrimaryKey(updateLogReadTemp);
        if (updateLogRead == null) {
            updateLogReadMapper.insert(updateLogReadTemp);
            return false;
        }
        if ((updateLogRead.getUserIds() == null ? "" : updateLogRead.getUserIds()).contains("," + LoginCtrl.getLoginUserInfo().getId() + ",")) {
            return true;
        }
        return false;
    }

    public Map<String, Object> searchUpdateLog(String terminal) {
        Map<String, Object> map = new HashMap<>();
        String version = null;
        List<String> logList = new ArrayList<>();
        if ("APP".equals(terminal)) {
            version = null;/*ymlConfig.getAppversion();*/
            logList = getLogText(version, terminal);

        } else if ("PC".equals(terminal)) {
            version = ConstantPath.imagesPath;
            logList = getLogText(version, terminal);
        }
        map.put("version", version);
        map.put("list", logList);
        map.put("status", 0);
        return map;
    }

    public List<String> getLogText(String version, String terminal) {
        URL url = this.getClass().getClassLoader().getResource("");
        String logFilePath = url.getPath() + "/static/updateLog/"
                + ("APP".equals(terminal) ? "APP/" : "PC/")
                + version + ".md";
        List<String> logReadList = new ArrayList<>();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = new FileInputStream(logFilePath);
            isr = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(isr);
            String lineTxt;
            while ((lineTxt = br.readLine()) != null) {
                logReadList.add(lineTxt);
            }
        } catch (Exception e) {
            new ArrayList<>();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (Exception e) {
                new ArrayList<>();
            }
        }
        return logReadList;
    }
}