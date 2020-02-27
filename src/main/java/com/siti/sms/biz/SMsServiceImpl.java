package com.siti.sms.biz;

/*
import com.cloopen.rest.sdk.CCPRestSmsSDK;
*/

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.siti.config.YmlConfig;
import com.siti.utils.CaffeineUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class SMsServiceImpl {

    @Resource
    CaffeineUtil caffeineUtil;

    @Resource
    YmlConfig ymlConfig;

    Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    public boolean send(String phone) {//传入用户手机号
        //生成4位数随机验证码
        int number = new Random().nextInt((9999 - 1111 + 1) + 1111);
        String yzm = String.valueOf(number);
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        restAPI.setAccount(ymlConfig.getAccountSid(), ymlConfig.getAuthToken());// 初始化主帐号和主帐号TOKEN
        restAPI.setAppId(ymlConfig.getAppId());// 初始化应用ID
        HashMap<String, Object> result = restAPI.sendTemplateSMS(phone, ymlConfig.getMsgModelId(), new String[]{yzm, "2"});//参数1:用户手机号,参数2:使用的模板号,参数3:设置验证码和过期时间提示

        System.out.println("SDKTestSendTemplateSMS result=" + result);

        if ("000000".equals(result.get("statusCode"))) {
            //正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
            //发送成功后将验证码存入Redis,设置2分钟的过期时间
            String key = "code:" + phone;
            cache.put(key,yzm);
            /*CaffeineUtil.build().put(key, yzm);*/
            return true;
        } else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
            return false;
        }
    }
    /**
     * 取缓存数据做对比
     * */
    public boolean getCode(String phone, String code) {
         if (phone != null && code != null) {
            String key = "code:" + phone;
            String CurrentYZM = (String)cache.getIfPresent(key);

            if(code.equals(CurrentYZM)){
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
    }

    public static void main(String[] args) {

        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
        restAPI.setAccount("8a216da855826478015599e3f66e1411", "71a6619327734d81957e60f2eeaa2626");// 初始化主帐号和主帐号TOKEN
        restAPI.setAppId("8a216da86df13579016df755a04704a1");// 初始化应用ID
        HashMap<String, Object> result = restAPI.sendTemplateSMS("18360865166", "564132", new String[]{"你好", "内容填什么"});

    }

}