package com.siti.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyw on 2017/10/16.
 * 返回成功信息工具类
 */
public class ReturnMessage {

    private ReturnMessage() {
    }

    /**
     * 添加成功
     *
     * @return
     */
    public static Map<String, Object> addSuccess() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        map.put("message", "添加成功！");
        return map;
    }

    /**
     * 提交成功
     *
     * @return
     */
    public static Map<String, Object> submitSuccess() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        map.put("message", "提交成功！");
        return map;
    }

    /**
     * 修改成功
     *
     * @return
     */
    public static Map<String, Object> saveSuccess() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        map.put("message", "保存成功！");
        return map;
    }

    /**
     * 删除成功
     *
     * @return
     */
    public static Map<String, Object> deleteSuccess() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        map.put("message", "删除成功！");
        return map;
    }

    /**
     * 查询成功
     *
     * @return
     */
    public static Map<String, Object> searchSuccess() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        map.put("message", "查询成功！");
        return map;
    }

    /**
     * 审批成功
     *
     * @return
     */
    public static Map<String, Object> processSuccess() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        map.put("message", "审批成功！");
        return map;
    }

    /**
     * 成功
     *
     * @return
     */
    public static Map<String, Object> successMessage(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        map.put("message", message);
        return map;
    }

    /**
     * Error
     *
     * @return
     */
    public static Map<String, Object> failMessage(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", -1);
        map.put("message", message);
        return map;
    }


}
