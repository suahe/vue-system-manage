package com.example.manageSystem.admin.module.shiro.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/*@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean shiroSessionFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ShiroSessionFilter());
        filterRegistrationBean.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap();
        initParameters.put("serverSessionTimeout", "10000");
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }
}*/
