package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;
import com.cy.pj.sys.vo.SysUserMenuVo;

public interface SysUserService {

    /**
     * 修改登陆用户密码
     *
     * @param sourcePassword 原密码
     * @param newPassword    新密码
     * @param cfgPassword    确认密码
     * @return 修改行数
     */
    int updatePassword(String sourcePassword, String newPassword, String cfgPassword);

    /**
     * 基于用户id查询用户，用户对应的部门，用户对应的角色信息。
     *
     * @param userId
     * @return
     */
    Map<String, Object> findObjectById(Integer userId);

    /**
     * 更新用户以及用户对应的角色关系数据
     *
     * @param entity
     * @param roleIds
     * @return
     */
    int updateObject(SysUser entity, Integer[] roleIds);

    /**
     * 保存用户以及用户对应的角色关系数据
     *
     * @param entity
     * @param roleIds
     * @return
     */
    int saveObject(SysUser entity, Integer[] roleIds);

    /**
     * 修改用户状态
     *
     * @param id
     * @param valid
     * @param modifiedUser
     * @return
     */
    int validById(Integer id, Integer valid, String modifiedUser);

    /**
     * 基于条件查询用户以及用户对应的部门信息，一行记录映射为一个SysUserDeptVo对象
     *
     * @param username
     * @param pageCurrent
     * @return
     */
    PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent);

}
