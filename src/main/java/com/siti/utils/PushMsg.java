package com.siti.utils;

import com.cloopen.rest.sdk.CCPRestSmsSDK;

import java.util.Map;

/**
 * 短信推送---云通讯【http://www.yuntongxun.com/】
 * @author zuoysun
 * @time 2016-07-11 pm
 * */
public class PushMsg {
    private static String ACCOUNT_SID = "8a216da855826478015599e3f66e1411";
    private static String AUTH_TOKEN = "71a6619327734d81957e60f2eeaa2626";
    private static String APPID = "8aaf0708624670f2016261f89d5b09be";
    private static String SERVERIP = "app.cloopen.com";
    private static String SERVERPORT = "8883";
    private static String TEMPLATEID = "240720";//240720 // 100070


    /**
     * SDK
     *
     * @param phoneNumber 要发送的手机号，逗号分隔
     * @return 状态码（000000 成功，160040 发送次数超过最大限制，160038 操作频繁，160032 短信模板无效）
     */
    public static Map<String, Object> pushMsgToClient(String phoneNumber, Integer count) {
        // 初始化SDK
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        // 初始化配置
        restAPI.init(SERVERIP, SERVERPORT);
        restAPI.setAccount(ACCOUNT_SID, AUTH_TOKEN);
        restAPI.setAppId(APPID);
        // 发送短信
        return restAPI.sendTemplateSMS(phoneNumber, TEMPLATEID, new String[]{count + ""});
    }

    public static void main(String[] args) throws Exception {
        /*Map<String, Object> a = PushMsg.pushMsgToClient("13764471835", 10);
        System.out.println(a);*/
    }

}
