package com.cy.pj.sys.service.impl;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDao sysLogDao;//has a


    @Async
    //@Transactional(propagation = Propagation.REQUIRED)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveObject(SysLog entity) {
        String tName = Thread.currentThread().getName();
        System.out.println("log.service.saveObject.thread.name=" + tName);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }

        sysLogDao.insertObject(entity);

    }

    @RequiresPermissions("sys:log:delete")
    @Override
    public int deleteObjects(Integer... ids) {
        //1.参数校验
        if (ids == null || ids.length == 0)
            throw new IllegalArgumentException("请先选择");
        //2.删除记录并校验结果
        int rows = sysLogDao.deleteObjects(ids);
        if (rows == 0)
            throw new ServiceException("记录可能已经不存在");
        //3.返回删除结果
        return rows;
    }

    @Override
    public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
        //1.验证参数的有效性
        if (pageCurrent == null || pageCurrent < 1)
            throw new IllegalArgumentException("页码值无效");
        //2.查询总记录数
        int rowCount = sysLogDao.getRowCount(username);
        if (rowCount == 0)
            throw new ServiceException("没有找到对应记录");
        //3.查询当前页记录
        int pageSize = 5;
        int startIndex = (pageCurrent - 1) * pageSize;
        List<SysLog> records =
                sysLogDao.findPageObjects(username, startIndex, pageSize);
        //4.对数据进行封装
        //return new PageObject<>(rowCount, records, pageCurrent, pageSize);
        return new PageObject<>(rowCount, records, pageCurrent, pageSize);
    }

}
