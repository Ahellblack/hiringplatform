package com.siti.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ht on 2018/1/24.
 */
@Configuration
public class YmlConfig extends WebMvcConfigurerAdapter {

    /**
     * 保留N小时内的无效文件
     */
    @Value("${filePath.invalidFileDelete}")
    private Integer invalidFileDelete;
    /**
     * 文件服务器地址
     */
    @Value("${filePath.files}")
    private String filepath;

    /**
     * 文件服务器地址
     */
    @Value("${filePath.images}")
    private String imagepath;
    /**
     * 版本号
     */
    @Value("${version.pcversion}")
    private String pcversion;

    /**
     * 小版本号
     */
    @Value("${version.build}")
    private String build;




}
