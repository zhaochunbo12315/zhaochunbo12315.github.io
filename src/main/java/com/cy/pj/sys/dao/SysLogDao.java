package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.sys.entity.SysLog;

@Mapper
public interface SysLogDao {
    /**
     * 将用户行为日志写入到数据库
     *
     * @param entity
     * @return
     */
    int insertObject(SysLog entity);

    /**
     * 基于id执行数据删除
     *
     * @param ids
     * @return
     */
    int deleteObjects(@Param("ids") Integer... ids);//array

    /**
     * 基于条件查询记录总数
     *
     * @param username
     * @return
     */
    //@Select("select count(*) from sys_logs where username=null")
    //@Select("select count(*) from sys_logs")
    int getRowCount(String username);

    /**
     * 基于条件查询当前页记录
     *
     * @param username   查询条件
     * @param startIndex 当前页的起始设置
     * @param pageSize   页面大小(每页最多显示多少条数据)
     * @return
     */
    List<SysLog> findPageObjects(String username, Integer startIndex, Integer pageSize); //limit startIndex,pageSize

}
