package com.cy.pj.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.util.IPUtils;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 通过@Aspect注解描述的类型，为AOP中的一种切面类型。在这种切面类型中通常要
 * 定义两部分内容：
 * 1)切入点(PointCut):在什么位置进行功能增强
 * 2)通知(Advice) :功能增强
 */
@Order
@Slf4j
@Aspect
@Component
public class SysLogAspect {
    /**
     * @Pointcut 用于定义切入点
     * bean(sysUserServiceImpl) 为切入点表达式，sysUserServiceImpl为spring
     * 容器中某个bean的名字。在当前应用中,sysUserServiceImpl代表的bean对象中
     * 所有方法的集合为切入点(这个切入点中任意的一个方法执行时，都要进行功能扩展)。
     */
    //@Pointcut("bean(sysUserServiceImpl)")
    @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
    public void doPointCut() {
    }//方法实现不需要写任何内容

    /**
     * @param jp 连接点，@Around描述的方法中，参数类型要求为ProceedingJoinPoint。
     * @return 目标业务方法的返回值，对于@Around描述的方法，其返回值类型要求为Object。
     * @throws Throwable
     * @Around 注解用于描述通知(Advice), 切面中的方法除了切入点都是通知。通知中要实现你要扩展功能。
     * @Around 注解描述的通知为Advice中的一种环绕通知。环绕通知中可以手动调用目标方法，可以在目标
     * 方法之前和之后进行额外业务实现。
     */
    @Around("doPointCut()")//描述doPointCut()方法的@Pointcut注解中内容为切入点表达式
    //@Around("bean(sysUserServiceImpl)")
    public Object logAround(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("method start {}", start);
        try {
            Object result = jp.proceed();//调用本类中其它通知，或下一个切面，或目标方法。
            long end = System.currentTimeMillis();
            log.info("method end {}", end);
            //保存用户行为日志
            saveLog(jp, (end - start));
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("method error {}", System.currentTimeMillis());
            //saveErrorLog(jp,(end-start));
            throw e;
        }
    }

    @Autowired
    private SysLogService sysLogService;

    private void saveLog(ProceedingJoinPoint jp, long time) throws Exception {
        //1.获取用户行为日志
        //1.1获取方法签名信息:方法目标对象类型的类全名+方法名
        Class<?> targetCls = jp.getTarget().getClass();
        MethodSignature ms = (MethodSignature) jp.getSignature();
        String method = targetCls.getName() + "." + ms.getName();
        //1.2获取方法参数信息
        //String params=Arrays.toString(jp.getArgs());
        String params = new ObjectMapper().writeValueAsString(jp.getArgs());
        //1.3获取操作名
        //1.3.1获取目标方法对象
        Method targetMethod =
                targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        //1.3.2获取方法对象上的RequiredLog注解
        RequiredLog requiredLog = targetMethod.getAnnotation(RequiredLog.class);
        //1.3.3获取注解上定义的操作名
        String operation = "";
        if (requiredLog != null) {
            operation = requiredLog.value();
        }
        //2.封装用户行为日志
        SysLog sysLog = new SysLog();
        sysLog.setIp(IPUtils.getIpAddr());
        sysLog.setUsername(ShiroUtils.getUsername());//做完登陆以后修改这个名字
        sysLog.setMethod(method);//类名+方法名
        sysLog.setParams(params);
        sysLog.setOperation(operation);
        sysLog.setTime(time);
        sysLog.setCreatedTime(new Date());
        //3.将日志存储到数据库
//		new Thread() {
//			public void run() {
//				sysLogService.saveObject(sysLog);
//			};
//		}.start();//1M
        try {
            sysLogService.saveObject(sysLog);
        } catch (Exception e) {
        }

    }//1000

}









