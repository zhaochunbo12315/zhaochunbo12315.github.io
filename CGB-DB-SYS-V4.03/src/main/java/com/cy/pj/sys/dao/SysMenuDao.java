package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserMenuVo;

/**
 * 基于此dao操作菜单表:sys_menus
 */
@Mapper
public interface SysMenuDao {
    /**
     * 基于多个菜单id查询菜单的id，url，name以及对应的子菜单(二级菜单)。
     *
     * @param menuIds
     * @return
     */
    List<SysUserMenuVo> findMenusByIds(@Param("menuIds") List<Integer> menuIds);

    /**
     * 基于多个菜单id获取菜单表中的授权标识
     *
     * @param menuIds
     * @return
     */
    List<String> findPermissions(
            @Param("menuIds") Integer[] menuIds);//menuIds,param1

    /**
     * 更新菜单信息
     *
     * @param entity
     * @return
     */
    int updateObject(SysMenu entity);

    /**
     * 将菜单对象持久化到数据库
     *
     * @param entity
     * @return
     */
    int insertObject(SysMenu entity);

    /**
     * 查询菜单节点信息
     *
     * @return
     */
    @Select("select id,name,parentId from sys_menus")
    List<Node> findZtreeMenuNodes();

    /**
     * 基于id统计子菜单的个数
     *
     * @param id
     * @return
     */
    @Select("select count(*) from sys_menus where parentId=#{id}")
    int getChildCount(Integer id);

    /**
     * 基于id删除菜单元素
     *
     * @param id
     * @return
     */
    @Delete("delete from sys_menus where id=#{id}")
    int deleteObject(Integer id);

    /**
     * 查询所有菜单以及这个菜单对应的上级菜单名称?
     * 难点：如何获取上级菜单名称
     */
    List<Map<String, Object>> findObjects();
}
