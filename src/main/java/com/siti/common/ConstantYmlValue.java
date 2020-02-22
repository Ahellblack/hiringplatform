package com.siti.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConstantYmlValue {

    @Value("${request.limit.seconds:30}")
    private   long limitSecond;

    @Value("${redis.addr:#{null}}")
    private String redisAddr;

    @Value("${redis.password:#{null}}")
    private String redisPassword;


    @Value("${caffine.duration:50}")
    private int caffineDuration;

    public int getCaffineDuration() {
        return caffineDuration;
    }

    public void setCaffineDuration(int caffineDuration) {
        this.caffineDuration = caffineDuration;
    }

    public long getLimitSecond() {
        return limitSecond;
    }

    public void setLimitSecond(long limitSecond) {
        this.limitSecond = limitSecond;
    }

    public String getRedisAddr() {
        return redisAddr;
    }

    public void setRedisAddr(String redisAddr) {
        this.redisAddr = redisAddr;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }
}
