package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig   implements WebMvcConfigurer {

    @Value("${file.uploadUrlQR}")
    private String  fileUrlQR;
    @Value("${file.uploadUrlEC}")
    private String  fileUrlEC;
    @Value("${file.uploadUrlScreenImg}")
    private String  fileUrlScreenImg;

//    @Value("${file.api}")
//    private String  fileApi;

    public void addResourceHandlers(ResourceHandlerRegistry register){
        register.addResourceHandler("/EC/**").addResourceLocations("file:"+fileUrlEC);
        register.addResourceHandler("/user/qrImg/**").addResourceLocations("file:"+fileUrlQR);
        register.addResourceHandler("/screen/**").addResourceLocations("file:"+fileUrlScreenImg);
    }
}