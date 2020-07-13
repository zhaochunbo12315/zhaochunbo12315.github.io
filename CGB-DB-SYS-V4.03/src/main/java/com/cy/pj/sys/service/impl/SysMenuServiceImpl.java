package com.cy.pj.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredCache;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public List<SysUserMenuVo> findUserMenusByUserId(Integer userId) {
        //1.基于用户id查询用户对应的角色
        List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(userId);
        //2.基于用户角色查询角色对应的菜单id
        List<Integer> menuIds =
                sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[]{}));
        //3.基于菜单id查询菜单相关信息
        List<SysUserMenuVo> userMenus =
                sysMenuDao.findMenusByIds(menuIds);
        return userMenus;
    }

    //@RequiredCache
    @Override
    public List<Node> findZtreeMenuNodes() {
        return sysMenuDao.findZtreeMenuNodes();
    }

    @Override
    public int updateObject(SysMenu entity) {
        //1.参数校验
        if (entity == null)
            throw new IllegalArgumentException("保存对象不能为空");
        if (StringUtils.isEmpty(entity.getName()))//验证null和空串
            throw new IllegalArgumentException("菜单名不允许为空");
        if (StringUtils.isEmpty(entity.getPermission()))
            throw new IllegalArgumentException("授权标识不允许为空");
        //.......
        //2.保存菜单对象
        int rows = sysMenuDao.updateObject(entity);
        //3.验证结果并返回
        return rows;
    }

    @Override
    public int saveObject(SysMenu entity) {
        //1.参数校验
        if (entity == null)
            throw new IllegalArgumentException("保存对象不能为空");
        if (StringUtils.isEmpty(entity.getName()))//验证null和空串
            throw new IllegalArgumentException("菜单名不允许为空");
        if (StringUtils.isEmpty(entity.getPermission()))
            throw new IllegalArgumentException("授权标识不允许为空");
        //.......
        //2.保存菜单对象
        int rows = sysMenuDao.insertObject(entity);
        //3.验证结果并返回
        return rows;
    }

    @Override
    public int deleteObject(Integer id) {
        //1.参数校验
        if (id == null || id < 1)
            throw new IllegalArgumentException("id值无效");
        //2.基于id获取子菜单并校验
        int childCount = sysMenuDao.getChildCount(id);
        if (childCount > 0)
            throw new ServiceException("请先删除子元素");
        //3.删除菜单元素
        //3.1删除角色菜单关系数据
        sysRoleMenuDao.deleteObjectsByMenuId(id);
        //3.2删除自身信息
        int rows = sysMenuDao.deleteObject(id);
        //4.校验结果并返回
        if (rows == 0)
            throw new ServiceException("记录可能已经不存在");
        return rows;
    }

    @Override
    public List<Map<String, Object>> findObjects() {
        List<Map<String, Object>> list = sysMenuDao.findObjects();
        if (list == null || list.size() == 0)
            throw new ServiceException("没有对应的菜单信息");
        return list;
    }

}
