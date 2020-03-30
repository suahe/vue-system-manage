package com.example.manageSystem.config.filter;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean setFilter(){
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new LogintFilter());
        filterBean.setName("FilterConfiguration");
        filterBean.addUrlPatterns("/*");
        filterBean.addInitParameter("exclusions","//login,//loginout");
        return filterBean;
    }
}
