package com.example.manageSystem.config.interceptor;

import com.example.manageSystem.admin.model.Log;
import com.example.manageSystem.admin.model.User;
import com.example.manageSystem.admin.module.log.service.LogService;
import com.example.manageSystem.admin.module.log.utils.LogUtils;
import com.example.manageSystem.config.thread.SaveLogThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;

/**
 * 日志拦截器
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(null==user) {//未登录
            //直接重定向到登录页面
            response.sendRedirect("http://localhost:8080/login#/login");
            return false;
        }
        /*if (logger.isDebugEnabled()){*/
             long beginTime = System.currentTimeMillis();//1、开始时间
            startTimeThreadLocal.set(beginTime);//线程绑定变量（该数据只有当前请求的线程可见）
            logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS")
                    .format(beginTime), request.getRequestURI());
        /*}*/
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null){
            logger.info("ViewName: " + modelAndView.getViewName());
        }
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        // 保存日志
        try {
            long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
            long endTime = System.currentTimeMillis();  //2、结束时间
            Long timer = endTime - beginTime;//计算耗时
            request.setAttribute("timer", timer);
            Log log = LogUtils.saveLog(request, handler, ex, null);//调用保存日志方法(方法在公共方法中)
            threadPoolTaskExecutor.execute(new SaveLogThread(log, logService));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 打印JVM信息。
        if (logger.isDebugEnabled()){
            long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
            long endTime = System.currentTimeMillis();  //2、结束时间
            Object[] obj = new Object[10];
            obj[0] = new SimpleDateFormat("hh:mm:ss.SSS").format(endTime);
            obj[1] = endTime - beginTime;
            obj[2] = request.getRequestURI();
            obj[3] = Runtime.getRuntime().maxMemory()/1024/1024;
            obj[4] = Runtime.getRuntime().totalMemory()/1024/1024;
            obj[5] = Runtime.getRuntime().freeMemory()/1024/1024;
            obj[6] = (Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024;
            logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",obj);
            //删除线程变量中的数据，防止内存泄漏
            startTimeThreadLocal.remove();
        }
    }
}
