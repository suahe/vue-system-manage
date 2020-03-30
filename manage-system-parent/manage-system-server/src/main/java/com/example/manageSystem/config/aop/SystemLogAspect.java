package com.example.manageSystem.config.aop;

/**
 * 通过aop拦截插入日志记录
 */
/*@Component
@Aspect*/
public class SystemLogAspect {

   /* private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    private static final ThreadLocal<Long> timeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal beginTime");
    private static final ThreadLocal<Log> logThreadLocal = new NamedThreadLocal<Log>("ThreadLocal log");

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private LogService logService;

    @Pointcut("execution(* com.example.manageSystem.*.module.*.controller.*.*(..))")
    public void controllerAspect() {

    }

    *//**
     * 用于拦截controller层记录用户的操作的开始时间
     *
     * @param joinpoint 切点
     * @throws InterruptedException
     *//*
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinpoint) throws InterruptedException {
        long beginTime = System.currentTimeMillis();//1、开始时间
        timeThreadLocal.set(beginTime);//线程绑定变量(该数据只有当前请求的线程可见)
        HttpServletRequest request = null;
        if (RequestContextHolder.getRequestAttributes() != null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        if (logger.isDebugEnabled()) {
            //日志级别为debug
            logger.debug("开始计时:{} URI:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(beginTime), request != null ? request.getRequestURI() : "");
        }
    }

    *//**
     * 用于拦截controller层记录用户的操作
     *
     * @param joinpoint 切点
     * @throws Exception
     *//*
    @SuppressWarnings("unchecked")
    @After("controllerAspect()")
    public void doAfter(JoinPoint joinpoint) {

        if (null != RequestContextHolder.getRequestAttributes()) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // 保存日志
            try {
                long beginTime = timeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
                long endTime = System.currentTimeMillis();  //2、结束时间
                Long timer = endTime - beginTime;//计算耗时
                request.setAttribute("timer", timer);
                Log log = LogUtils.saveLog(request);//调用保存日志方法(方法在公共方法中)
                threadPoolTaskExecutor.execute(new SaveLogThread(log, logService));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 打印JVM信息。
            if (logger.isDebugEnabled()) {
                long beginTime = timeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
                long endTime = System.currentTimeMillis();  //2、结束时间
                Object[] obj = new Object[10];
                obj[0] = new SimpleDateFormat("hh:mm:ss.SSS").format(endTime);
                obj[1] = endTime - beginTime;
                obj[2] = request.getRequestURI();
                obj[3] = Runtime.getRuntime().maxMemory() / 1024 / 1024;
                obj[4] = Runtime.getRuntime().totalMemory() / 1024 / 1024;
                obj[5] = Runtime.getRuntime().freeMemory() / 1024 / 1024;
                obj[6] = (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024;
                logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m", obj);
            }
            //删除线程变量中的数据，防止内存泄漏
            timeThreadLocal.remove();
            logThreadLocal.remove();
        }
    }*/
}
