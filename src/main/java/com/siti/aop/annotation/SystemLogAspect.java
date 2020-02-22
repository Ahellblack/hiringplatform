package com.siti.aop.annotation;

import com.siti.aop.mapper.SysLogMapper;
import com.siti.aop.po.SysLog;
import com.siti.security.LoginUserInfo;
import com.siti.security.RestAuthenticationSuccessHandler;
import com.siti.utils.CustomSystemUtil;
import com.siti.utils.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;



@Aspect
@Service
public class SystemLogAspect {

    //注入Service用于把日志保存数据库
    @Resource
    private SysLogMapper sysLogMapper;

    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    //Controller层切点
    @Pointcut("@annotation( com.siti.aop.annotation.Log)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) {
            logger.info("before " + joinPoint);
        }
    }

    /*//配置controller环绕通知,使用在方法aspect()上注册的切入点
    @Around("controllerAspect()")
    public Object around(JoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
//        ((ProceedingJoinPoint) joinPoint).proceed();
        long end = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
        }
        Object[] args = joinPoint.getArgs();
        Object result = ((ProceedingJoinPoint) joinPoint).proceed(args);
        *//*} catch (Throwable e) {
            long end = System.currentTimeMillis();
            if(logger.isInfoEnabled()){
                logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
            }
        }*//*
        return result;
    }*/

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public void after(JoinPoint joinPoint) {
        //读取session中的用户
        LoginUserInfo user = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //请求的IP
        String ipAddress = CustomSystemUtil.INTRANET_IP;
        /*String ip = request.getRemoteAddr();*/
        //String ip = "127.0.0.1";
        try {
            if (logger.isInfoEnabled()) {
                logger.info("after " + joinPoint);
            }
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operationType = method.getAnnotation(Log.class).operationType();
                        operationName = method.getAnnotation(Log.class).operationName();
                        break;
                    }
                }
            }
            //*========控制台输出=========*//

            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()") + "." + operationType);
            System.out.println("方法描述:" + operationName);
            System.out.println("请求人:" + user.getRealName());
            System.out.println("请求IP:" + ipAddress);
            //*========数据库日志=========*//
            SysLog log = new SysLog();
            log.setId(UUID.randomUUID().toString());
            log.setDescription(operationName);
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()") + "." + operationType);
            log.setLogType((long) 0);
            log.setRequestIp(ipAddress);
            log.setExceptionCode(null);
            log.setExceptionDetail(null);
            log.setParams(Arrays.toString(joinPoint.getArgs()));
            log.setCreateBy(user.getRealName());
            log.setCreateDate(new Date());
            //保存数据库
            sysLogMapper.insert(log);

        } catch (Exception e) {
           /* //记录本地异常日志
            logger.error("异常信息:{}", e.getMessage());*/
        }
    }

    //配置后置返回通知,使用在方法aspect()上注册的切入点
    @AfterReturning("controllerAspect()")
    public void afterReturn(JoinPoint joinPoint) {

        if (logger.isInfoEnabled()) {
            logger.info("afterReturn " + joinPoint);
        }
    }

    /**
     * 异常通知 用于拦截记录异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        //读取session中的用户
        LoginUserInfo user = (LoginUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //请求的IP
        String ip = request.getRemoteAddr();
//        String ip = "127.0.0.1";;

        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params += JsonUtil.toJsonString(joinPoint.getArgs()[i]) + ";";
            }
        }
        try {

            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operationType = method.getAnnotation(Log.class).operationType();
                        operationName = method.getAnnotation(Log.class).operationName();
                        break;
                    }
                }
            }
             /*========控制台输出=========*/

            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()") + "." + operationType);
            System.out.println("方法描述:" + operationName);
            System.out.println("请求人:" + user.getRealName());
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + params);
               /*==========数据库日志=========*/
            SysLog log = new SysLog();
            log.setId(UUID.randomUUID().toString());
            log.setDescription(operationName);
            log.setExceptionCode(e.getClass().getName());
            log.setLogType((long) 1);
            log.setExceptionDetail(e.getMessage());
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setParams(params);
            log.setCreateBy(user.getName());
            log.setCreateDate(new Date());
            log.setRequestIp(ip);
            //保存数据库
            sysLogMapper.insert(log);

        } catch (Exception ex) {
            //记录本地异常日志
            logger.error("==异常通知异常==");
            logger.error("异常信息:{}", ex.getMessage());
        }
         /*==========记录本地异常日志==========*/
        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);

    }

}