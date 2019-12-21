package com.joysupply;

import com.joysupply.interceptor.UserLoginInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = {"com.joysupply.dao"})
@EnableCaching
@EnableTransactionManagement
public class ByOaApplication implements WebMvcConfigurer {
    @Autowired
    private UserLoginInterceptor userLoginInterceptor;

    public static void main(String[] args) {
        try {
            SpringApplication.run(ByOaApplication.class, args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/**").
                excludePathPatterns("/css/**").
                excludePathPatterns("/iconfont/**").
                excludePathPatterns("/img/**").
                excludePathPatterns("/js/**").
                excludePathPatterns("/layui/**").
                excludePathPatterns("/common/*").
                excludePathPatterns("/sso/*").
                excludePathPatterns("/treetable-lay/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**")
                .addResourceLocations("classpath:/templates/");
        String root = System.getProperty("user.dir");
        registry.addResourceHandler( "/upload/**").addResourceLocations("file:" + root + "\\upload\\");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("100MB");
        return factory.createMultipartConfig();
    }
}
