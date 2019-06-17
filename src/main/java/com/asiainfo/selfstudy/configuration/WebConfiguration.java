package com.asiainfo.selfstudy.configuration;

import com.asiainfo.selfstudy.utils.DateFormat;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    //  增加拦截器操作
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    //  增加跨域访问
    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }


    // 增加格式化的相关操作
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormat("yyyy-MM-dd  HH:mm:ss"));
    }

    // 增加视图控制
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }
}
