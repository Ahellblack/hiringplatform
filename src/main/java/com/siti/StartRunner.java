package com.siti;

import com.siti.config.EasyCache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class StartRunner implements ApplicationRunner {

    @Resource
    EasyCache easyCache;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        //        项目启动后调用
        easyCache.init();
    }
}
