package com.example.manageSystem.admin.module.log.utils;

import com.alibaba.fastjson.JSON;
import com.example.manageSystem.admin.enums.LogType;
import com.example.manageSystem.admin.model.Log;
import com.example.manageSystem.admin.model.LogAnnotation;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.shiro.utils.ShiroUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.web.method.HandlerMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LogUtils {

    /**
     * 采用拦截器日志记录
     * 保存日志
     */
    public static Log saveLog(HttpServletRequest request, Object handler, Exception ex, String title){
            Log log = new Log();
            log.setType(ex == null ? LogType.ACCESS_TYPE.getValue() : LogType.EXCEPTION_TYPE.getValue());// 类型（1：接入日志；2：错误日志）
            log.setTime(Long.parseLong(request.getAttribute("timer") + ""));// 耗时
            log.setIp(getIpAddr(request));// 操作用户的IP地址
            log.setUserAgent(request.getHeader("user-agent"));// 操作用户代理信息
            log.setRequestUrl(request.getRequestURI());// 操作的URI
            log.setRequestHeaders(JSON.toJSONString(getHeadersInfo(request)));//获取请求头
            log.setParams(JSON.toJSONString(request.getParameterMap()));// 操作提交的数据
            log.setMethod(request.getMethod()); // 操作的方式
            log.setException(ex == null?null:ex.getMessage()); // 异常信息
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            String browser = userAgent.getBrowser().getName();//获取浏览器名称
            String windows = userAgent.getOperatingSystem().getName();//获取操作系统
            log.setBrowserName(browser);// 浏览器名称
            log.setWindows(windows);//操作系统名称
            HttpSession session = request.getSession();
            if(session!=null){
                User user = (User) session.getAttribute("user");
                log.setCreator(user==null?null:user.getUsername().toString());
            }
            log.setCreateDate(new Date());
            log.setModuleName(getModuleName(handler));
            return log;
    }

    /**
     * 采用aop日志记录
     * 保存日志
     */
    public static Log saveLog(HttpServletRequest request){
        Log log = new Log();
        log.setTime(Long.parseLong(request.getAttribute("timer") + ""));// 耗时
        log.setIp(getIpAddr(request));// 操作用户的IP地址
        log.setUserAgent(request.getHeader("user-agent"));// 操作用户代理信息
        log.setRequestUrl(request.getRequestURI());// 操作的URI
        log.setRequestHeaders(JSON.toJSONString(getHeadersInfo(request)));//获取请求头
        log.setParams(JSON.toJSONString(request.getParameterMap()));// 操作提交的数据
        log.setMethod(request.getMethod()); // 操作的方式
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String browser = userAgent.getBrowser().getName();//获取浏览器名称
        String windows = userAgent.getOperatingSystem().getName();//获取操作系统
        log.setBrowserName(browser);// 浏览器名称
        log.setWindows(windows);//操作系统名称
        User user = ShiroUtils.getUserEntity();
        log.setCreator(user==null?null:user.getUsername().toString());
        log.setCreateDate(new Date());
        return log;
    }

    /**
     * 获取请求IP
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取请求头
     */
    private static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 通过注解获取类名和方法名
     */
    private static String getModuleName(Object handler){
        // 获取日志标题
        String moduleName = null ;
        if (handler instanceof HandlerMethod){
            Method m = ((HandlerMethod)handler).getMethod();
            LogAnnotation clazz = m.getDeclaringClass().getAnnotation(LogAnnotation.class);//类注解
            LogAnnotation method = m.getAnnotation(LogAnnotation.class);//方法注解
            if(clazz != null && method != null) {
                String[] s = new String[2];
                s[0] = clazz.name();
                s[1] = method.name();
                moduleName = s[0]+ "-"+s[1];
            }
        }
      return  moduleName;
    }
}