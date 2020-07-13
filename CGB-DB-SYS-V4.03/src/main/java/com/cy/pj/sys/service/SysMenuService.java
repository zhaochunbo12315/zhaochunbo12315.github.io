package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.vo.SysUserMenuVo;

public interface SysMenuService {

    /**
     * 查询用户对应的菜单信息
     *
     * @param userId
     * @return
     */
    List<SysUserMenuVo> findUserMenusByUserId(Integer userId);

    /**
     * 将菜单信息更新到数据库
     *
     * @param entity
     * @return
     */
    int updateObject(SysMenu entity);

    /**
     * 将菜单信息保存到数据库
     *
     * @param entity
     * @return
     */
    int saveObject(SysMenu entity);

    /**
     * 查找zTree节点信息
     *
     * @return
     */
    List<Node> findZtreeMenuNodes();

    /**
     * 基于菜单id执行菜单删除操作
     *
     * @param id
     * @return
     */
    int deleteObject(Integer id);

    List<Map<String, Object>> findObjects();
}
