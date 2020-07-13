package com.cy.pj.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.cy.pj.common.exception.ServiceException;

/**
 * Spring MVC 中的拦截器
 *
 * @author qilei
 */
public class TimeAccessInterceptor implements HandlerInterceptor {
    /**
     * 拦截器中的preHandle方法会在目标控制层方法执行之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //1.获得日历对象
        Calendar c = Calendar.getInstance();
        //2.定义开始访问时间
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long start = c.getTimeInMillis();
        //3.定义终止访问时间
        c.set(Calendar.HOUR_OF_DAY, 19);
        long end = c.getTimeInMillis();
        //4.进行时间判定
        long current = System.currentTimeMillis();
        if (current < start || current > end)
            throw new ServiceException("请在9:00~17:00时间范围内访问");
        return true;//false 表示不放行
    }
}









