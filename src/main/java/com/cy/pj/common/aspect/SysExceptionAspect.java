package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * 异常监控切面对象
 *
 * @author qilei
 */
@Slf4j
@Aspect
@Component
public class SysExceptionAspect {
    /**
     * 异常通知
     */
    @AfterThrowing(pointcut = "bean(*ServiceImpl)", throwing = "e")
    public void doHandleException(JoinPoint jp, Exception e) {
        System.out.println("e.class=" + e.getClass().getName());
        //获取目标对象类型
        String clsName = jp.getTarget().getClass().getName();
        //方法签名(此对象中封装了要调用的目标方法信息：例如方法名，参数类型，返回值，。。。)
        MethodSignature ms = (MethodSignature) jp.getSignature();
        log.error("{}'exception msg is  {}", clsName + "." + ms.getName(), e.getMessage());
    }
}
