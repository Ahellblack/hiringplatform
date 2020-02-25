package com.siti.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by dell on 2020/1/2.
 */

@Configuration
public class CaffeineInit {

    private static Cache caffeineInit;


    @Bean
    public static Cache getCaffeineConfig() {
        //Config config = new Config();
        Cache<String, Object> manualCache = Caffeine.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                //.refreshAfterWrite(1, TimeUnit.MINUTES)
                .expireAfterAccess(10,TimeUnit.MINUTES)
                .maximumSize(10000).build();

        return manualCache;
    }
}



