package com.example.manageSystem.admin.module.log.utils;

import com.example.manageSystem.admin.model.Log;
import com.example.manageSystem.admin.module.log.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import java.lang.reflect.Method;

/**
 * 开启保存日志线程
 */
public static class SaveLogThread extends Thread{
    //工具类中的service注入不能用注解方式   所以用spring方式载入
    private static LogService logService = SpringContextHolder.getBean("logService");


    private Log log;
    private Object handler;
    private Exception ex;

    public SaveLogThread(Log log, Object handler, Exception ex){
        super(SaveLogThread.class.getSimpleName());
        this.log = log;
        this.handler = handler;
        this.ex = ex;
    }

    @Override
    public void run() {
        // 获取日志标题
        if (StringUtils.isBlank(log.getTitle())){
            String title = null ;
            if (handler instanceof HandlerMethod){
                Method m = ((HandlerMethod)handler).getMethod();
                Logger clazz = m.getDeclaringClass().getAnnotation(Logger.class);//类注解
                Logger method = m.getAnnotation(Logger.class);//方法注解

                if(clazz != null && method != null) {
                    String[] s = new String[2];
                    s[0] = clazz.name();
                    s[1] = method.name();
                    title = StringUtils.join(s, "-");
                }else {
                    title = "";
                }
            }
            log.setTitle(title);
        }
        // 如果有异常，设置异常信息
        log.setException(Exceptions.getStackTraceAsString(ex));
        // 如果无标题并无异常日志，则不保存信息
        if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())){
            return;
        }
        // 保存日志信息
        logService.save(log);
    }
}
}
