package com.cy.pj.common.aspect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SysCacheAspect {

    private Map<String, Object> cache = new ConcurrentHashMap<>();

    @Pointcut("execution(* com.cy.pj.sys.service.impl.SysMenuServiceImpl.findZtreeMenuNodes()) ")
    //@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredCache)")
    public void doCachePointCut() {
    }

    @Pointcut("execution(* com.cy.pj.sys.service.impl.SysMenuServiceImpl.updateObject(..)) ")
    public void doCacheUpdatePointCut() {
    }

    @AfterReturning("doCacheUpdatePointCut()")
    public void doAfterReturning() {
        cache.clear();
    }

    @Around("doCachePointCut()")
    public Object doCacheAround(ProceedingJoinPoint jp)
            throws Throwable {
        System.out.println("Get data from cache");
        Object obj = cache.get("menuZTree");//假设key为menuZTree
        if (obj != null) return obj;
        Object result = jp.proceed();
        System.out.println("Put data to cache");
        cache.put("menuZTree", result);
        return result;
    }


}