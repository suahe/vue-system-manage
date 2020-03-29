package com.example.manageSystem.admin.module.log.utils;

import com.alibaba.fastjson.JSON;
import com.example.manageSystem.admin.enums.LogType;
import com.example.manageSystem.admin.model.Log;
import com.example.manageSystem.admin.model.User;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class LogUtils {
   /* //工具类中的service注入不能用注解方式   所以用spring方式载入
    private static LogService logService = SpringContextHolder.getBean("logService");*/
    /**
     * 保存日志
     * @throws UnsupportedEncodingException
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title) throws UnsupportedEncodingException{
        //User user = UserUtils.getSession(User.SESSION_KEY);
        User user = new User();
        if (user != null && user.getUserId() != null){
            Log log = new Log();
            log.setType(ex == null ? LogType.ACCESS_TYPE.getValue(): LogType.EXCEPTION_TYPE.getValue());// 类型（1：接入日志；2：错误日志）
            log.setTime(Long.parseLong(request.getAttribute("timer")+""));// 耗时
            //log.setRequest_url(StringUtils.getRemoteAddr(request));// 操作用户的IP地址
            log.setUserAgent(request.getHeader("user-agent"));// 操作用户代理信息
            log.setRequestUrl(request.getRequestURI());// 操作的URI
            log.setParams(JSON.toJSONString(request.getParameterMap()));// 操作提交的数据
            log.setMethod(request.getMethod()); // 操作的方式
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            String browser =userAgent.getBrowser().getName();//获取浏览器名称
            String windows =userAgent.getOperatingSystem().getName();//获取操作系统
            log.setBrowserName(browser);// 浏览器名称
            log.setWindows(windows);//操作系统名称
            log.setCreator(user.getUserId().toString());
            log.setCreateDate(System.currentTimeMillis());
            // 异步保存日志
            new SaveLogThread(log, handler, ex).start();
        }
    }