package com.siti.sms.ctrl;

import com.siti.common.ReturnResult;
import com.siti.sms.biz.SMsServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2020/2/25.
 */
@RequestMapping("msg")
@RestController
public class SMsController {

    @Resource
    SMsServiceImpl sMsService;

    @PostMapping("send")//发送验证码的请求
    public ReturnResult send(String phone){
        if (sMsService.send(phone)) {//调用发送方法并存入Redis
            return new ReturnResult(1,"验证码发送成功");
        }
        return new ReturnResult(0,"发送异常");
    }
    //验证用户提交的验证码
    @PostMapping(value = "validatephone")
    public ReturnResult register(String phone, String code){
        boolean flag = sMsService.getCode(phone,code);//取出缓存中的验证码
        if (flag) {
            return new ReturnResult(1,"验证通过");
        } else {
            return new ReturnResult(0,"验证失败");
        }

    }
}
