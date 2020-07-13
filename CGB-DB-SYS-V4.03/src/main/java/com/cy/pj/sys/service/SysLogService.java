package com.cy.pj.sys.service;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;


public interface SysLogService {
    /**
     * 将用户行为日志写入到数据库
     *
     * @param entity
     */
    void saveObject(SysLog entity);

    /**
     * 基于多个id进行删除操作
     *
     * @param ids
     * @return
     */
    int deleteObjects(Integer... ids);

    /**
     * 基于用户请求，进行日志信息的分页查询，并对结果进行封装。
     *
     * @param username
     * @param pageCurrent
     * @return
     */
    PageObject<SysLog> findPageObjects(String username, Integer pageCurrent);

}
