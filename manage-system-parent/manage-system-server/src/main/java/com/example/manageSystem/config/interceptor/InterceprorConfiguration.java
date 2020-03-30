package com.example.manageSystem.config.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 注册拦截器
 */
@Configuration
@AllArgsConstructor
public class InterceprorConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public LogInterceptor logInterceptor(){
        System.out.println("创建logInterception Bean");
        return new LogInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        System.out.println("进入注册拦截器");
        //注册拦截器
        InterceptorRegistration ir = registry.addInterceptor(logInterceptor());
        //InterceptorRegistration ir = registry.addInterceptor(new LogInterceptor());
        //配置拦截的路径
        ir.addPathPatterns("/**");
        //ir.excludePathPatterns("/login");
    }

}
