package com.zbf.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LjlMyWebMvcConfig implements WebMvcConfigurer {

    /**
     * 自定义静态资源方法
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/pic/**").addResourceLocations("file:D://pic//","file:D://test1//");
    }
    /**
     * 统一跨域访问配置
     *
     */
   /* @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("*").maxAge(3600).allowCredentials(true);
    }*/
}
