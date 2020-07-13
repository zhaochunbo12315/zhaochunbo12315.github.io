package com.cy.pj.sys.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public List<CheckBox> findObjects() {
        return sysRoleDao.findObjects();
    }

    @Override
    public SysRoleMenuVo findObjectById(Integer id) {
        //1.合法性验证
        if (id == null || id <= 0)
            throw new IllegalArgumentException("id的值不合法");
        //2.执行查询
        SysRoleMenuVo result = sysRoleDao.findObjectById(id);
        //3.验证结果并返回
        if (result == null)
            throw new ServiceException("此记录已经不存在");
        return result;

    }

    @Override
    public int updateObject(SysRole entity, Integer[] menuIds) {
        //1.参数校验
        if (entity == null)
            throw new IllegalArgumentException("保存对象不能为空");
        if (StringUtils.isEmpty(entity.getName()))
            throw new IllegalArgumentException("角色名不允许为空");
        if (menuIds == null || menuIds.length == 0)
            throw new IllegalArgumentException("必须为角色授权");
        //2.更新角色自身信息
        int rows = sysRoleDao.updateObject(entity);
        //3.更新角色菜单关系数据
        sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
        sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
        //4.返回结果
        return rows;
    }

    @Override
    public int saveObject(SysRole entity, Integer[] menuIds) {
        //1.参数校验
        if (entity == null)
            throw new IllegalArgumentException("保存对象不能为空");
        if (StringUtils.isEmpty(entity.getName()))
            throw new IllegalArgumentException("角色名不允许为空");
        if (menuIds == null || menuIds.length == 0)
            throw new IllegalArgumentException("必须为角色授权");
        //2.保存角色自身信息
        int rows = sysRoleDao.insertObject(entity);
        //3.保存角色菜单关系数据
        sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
        //4.返回结果
        return rows;
    }

    @Override
    public int deleteObject(Integer id) {
        //1.参数校验
        if (id == null || id < 1)
            throw new IllegalArgumentException("id值无效");
        //2.删除数据
        //2.1删除关系数据
        sysRoleMenuDao.deleteObjectsByRoleId(id);
        sysUserRoleDao.deleteObjectsByRoleId(id);
        //2.2删除自身数据
        int rows = sysRoleDao.deleteObject(id);
        //3.验证并返回结果
        if (rows == 0)
            throw new ServiceException("记录可能已经不存在");
        return rows;
    }

    @Override
    public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
        //1.参数校验
        if (pageCurrent == null || pageCurrent < 1)
            throw new IllegalArgumentException("当前页码值不正确");
        //2.查询总记录数并校验
        int rowCount = sysRoleDao.getRowCount(name);
        if (rowCount == 0)
            throw new ServiceException("没有查询到对应的记录");
        //3.查询当前页数据
        int pageSize = 3;
        int startIndex = (pageCurrent - 1) * pageSize;
        List<SysRole> records =
                sysRoleDao.findPageObjects(name, startIndex, pageSize);
        //4.封装查询结果并返回
//		PageObject<SysRole> pageObject=new PageObject<>();
//		pageObject.setRowCount(rowCount);
//		pageObject.setRecords(records);
//		pageObject.setPageCurrent(pageCurrent);
//		pageObject.setPageSize(pageSize);
//		pageObject.setPageCount((rowCount-1)/pageSize+1);
//		return pageObject;
        return new PageObject<>(rowCount, records, pageCurrent, pageSize);
    }

}
