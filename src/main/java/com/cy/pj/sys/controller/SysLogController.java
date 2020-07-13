package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/log/")
@Lazy(true)
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("doDeleteObjects")
    @ResponseBody
    public JsonResult doDeleteObjects(Integer... ids) {//1ds=1,2,3
        sysLogService.deleteObjects(ids);
        return new JsonResult("delete ok");
    }

    @RequestMapping("doFindPageObjects")
    @ResponseBody
    public JsonResult doFindPageObjects(String username, Integer pageCurrent) throws Exception {
        PageObject<SysLog> pageObject =
                sysLogService.findPageObjects(username, pageCurrent);
        System.out.println("pageObject=" + pageObject);
        return new JsonResult(pageObject);
    }//exception-->dispatcherservlet--->处理异常
}
