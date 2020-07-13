package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 基于此dao操作角色菜单关系表
 *
 * @author qilei
 */
@Mapper
public interface SysRoleMenuDao {
    /**
     * 基于角色id获取角色对应的菜单id对象
     *
     * @param roleIds
     * @return
     */
    List<Integer> findMenuIdsByRoleIds(@Param("roleIds") Integer[] roleIds);

    /**
     * 基于角色id查询所有的菜单id
     *
     * @param roleId
     * @return
     */
    @Select("select menu_id from sys_role_menus where role_id=#{roleId}")
    List<Integer> findMenuIdsByRoleId(Integer roleId);

    /**
     * 保存角色和菜单的关系数据
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    int insertObjects(Integer roleId, Integer[] menuIds);//array

    /**
     * 基于菜单id删除角色菜单关系数据
     *
     * @param menuId
     * @return
     */
    @Delete("delete from sys_role_menus where menu_id=#{menuId}")
    int deleteObjectsByMenuId(Integer menuId);

    /**
     * 基于角色id删除角色菜单关系数据
     *
     * @param menuId
     * @return
     */
    @Delete("delete from sys_role_menus where role_id=#{roleId}")
    int deleteObjectsByRoleId(Integer roleId);
}




