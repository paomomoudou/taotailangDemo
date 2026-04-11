package com.moudou.taotailangdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 自动适配路径
        String realPath = System.getProperty("user.home") + "/upload/";

        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + realPath + File.separator);
    }
}