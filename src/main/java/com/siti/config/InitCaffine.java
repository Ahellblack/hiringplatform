package com.siti.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * Created by DC on 2019/12/10 - 14:52- {TIME}.
 **/
public class InitCaffine {

    private static Cache initCaffine;

    private static int durations;

    private static TimeUnit units;

    private static int maxSizes;

    private InitCaffine(){

    }

    /**
     * 根据输入配置生成Cache
     * @param duration  时长
     * @param unit 单位默认分钟
     * @param maxSize 容器得最大容量
     * @return
     */
    public static Cache getInstance(int duration, TimeUnit unit, int maxSize){
        durations=duration!=0?duration:1;
        units=unit!=null?unit:TimeUnit.MINUTES;
        maxSizes=maxSize!=0?(maxSize>1000?maxSize:1000):1000;
        return initCaffine= innerClass.INSTANCE;
    }

    /**
     * 静态内部类，懒加载生成单实例
     */
    private static class  innerClass{
        private static final Cache INSTANCE= Caffeine.newBuilder()
                .expireAfterWrite(durations,units)
                .maximumSize(1000)
                .build();
    }

}
