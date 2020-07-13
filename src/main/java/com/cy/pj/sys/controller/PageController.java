package com.cy.pj.sys.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysMenuService;
import com.cy.pj.sys.vo.SysUserMenuVo;

/**
 * 此Controller主要负责响应一些页面
 */
@Controller
@RequestMapping("/")
public class PageController {
    //private AtomicInteger at=new AtomicInteger(1);
    @Autowired
    private SysMenuService sysMenuService;

    @RequestMapping("doIndexUI")
    public String doIndexUI(Model model) {//model对象为spring mvc 模块设计的用于封装响应数据的一个对象
        //int a=at.getAndIncrement();
        SysUser user = ShiroUtils.getUser();
        List<SysUserMenuVo> userMenus =
                sysMenuService.findUserMenusByUserId(user.getId());
        System.out.println("userMenus=" + userMenus);
        model.addAttribute("user", user);//底层会将数据存储到请求作用域
        model.addAttribute("userMenus", userMenus);
        return "starter";
    }

    @RequestMapping("doLoginUI")
    public String doLoginUI() {
        return "login";
    }

    /**
     * 返回日志列表页面
     *
     * @return
     */
//	@RequestMapping("log/log_list")
//	public String doLogUI() {
//		return "sys/log_list";
//	}

//	@RequestMapping("menu/menu_list")
//	public String doMenuUI() {
//		return "sys/menu_list";
//	}
    //rest风格的一种url定义，语法{url}
    //@PathVariable 注解描述的方法参数，表示他的值从url路径中获取(和参数名相同的url变量值)
    @RequestMapping("{module}/{moduleUI}")
    public String doModuleUI(@PathVariable String moduleUI) {//module
        return "sys/" + moduleUI;
    }


    @RequestMapping("doPageUI")
    public String doPageUI() {

        return "common/page";
    }

}






