package com.siti.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.siti.utils.RandomValidateCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by szy on 2019/1/4 14:40.
 */
@Controller
@RequestMapping("verify")
public class RandomVerifyCtrl {
    private static final Logger logger = LoggerFactory.getLogger(RandomVerifyCtrl.class);
    /**
     * 生成验证码
     */
    @RequestMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        /*String inputStr = requestMap.get("inputStr").toString();
        String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");*/
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败>>>>   ", e);
        }
    }
}
