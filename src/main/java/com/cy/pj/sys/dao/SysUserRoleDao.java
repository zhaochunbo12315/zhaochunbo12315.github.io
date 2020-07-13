package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserRoleDao {
    /**
     * 基于用户id查询用户对应的角色id
     *
     * @param userId
     * @return
     */
    @Select("select role_id from sys_user_roles where user_id=#{userId}")
    List<Integer> findRoleIdsByUserId(Integer userId);

    int insertObjects(Integer userId, Integer[] roleIds);

    @Delete("delete from sys_user_roles where role_id=#{roleId}")
    int deleteObjectsByRoleId(Integer roleId);

    @Delete("delete from sys_user_roles where user_id=#{userId}")
    int deleteObjectsByUserId(Integer userId);
}
