//package com.example.examsystem.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    private String filePath = "D:/Study/MyAdmin/scr/avatar/";
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/avatar/**").addResourceLocations("file:"+filePath);
//        System.out.println("静态资源获取");
//    }
//}
