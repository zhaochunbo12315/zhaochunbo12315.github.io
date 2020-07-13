package com.cy.pj.common.web;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
/**
 * @ControllerAdvice 注解描述的类为Spring MVC 提供一个
 * 全局异常处理类，控制层出现异常以后，可以由此类进行处理
 * @author qilei
 */
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cy.pj.common.vo.JsonResult;

//@ResponseBody
//@ControllerAdvice
@RestControllerAdvice  //@ResponseBody+@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @return
     * @ExceptionHandler 描述的方法为spring mvc中的异常处理方法，
     */
    @ExceptionHandler(RuntimeException.class)
    public JsonResult doHandleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return new JsonResult(e);//{state:0,message:"..",data:null}
    }

    @ExceptionHandler(ShiroException.class)
    public JsonResult doHandleShiroException(
            ShiroException e) {
        JsonResult r = new JsonResult();
        r.setState(0);
        if (e instanceof UnknownAccountException) {
            r.setMessage("账户不存在");
        } else if (e instanceof LockedAccountException) {
            r.setMessage("账户已被禁用");
        } else if (e instanceof IncorrectCredentialsException) {
            r.setMessage("密码不正确");
        } else if (e instanceof AuthorizationException) {
            r.setMessage("没有此操作权限");
        } else {
            r.setMessage("系统维护中");
        }
        e.printStackTrace();
        return r;
    }


}
