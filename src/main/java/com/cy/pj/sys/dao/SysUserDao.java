package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

@Mapper
public interface SysUserDao {
    /**
     * 基于登陆用户id修改用户密码
     *
     * @param password 新的密码
     * @param salt     新的盐值
     * @param id
     * @return
     */
    @Update("update sys_users set password=#{password},salt=#{salt},modifiedTime=now() where id=#{id}")
    int updatePassword(String password, String salt, Integer id);

    /**
     * 基于用户名查找用户信息
     *
     * @param username
     * @return
     */
    @Select("select * from sys_users where username=#{username}")
    SysUser findUserByUserName(String username);

    /**
     * 更新用户信息
     *
     * @param entity
     * @return
     */
    int updateObject(SysUser entity);

    /**
     * 基于用户id获取用户以及用户对应的部门信息
     *
     * @param id
     * @return
     */
    SysUserDeptVo findObjectById(Integer id);

    /**
     * 写入用户信息
     *
     * @param entity
     * @return
     */
    int insertObject(SysUser entity);

    /**
     * 基于用户id修改用户状态
     *
     * @param id           用户id
     * @param valid        用户状态
     * @param modifiedUser 修改用户
     * @return
     */
    @Update("update sys_users set valid=#{valid},modifiedUser=#{modifiedUser},modifiedTime=now() where id=#{id}")
    int validById(Integer id, Integer valid, String modifiedUser);

    /**
     * 按条件统计用户记录总数
     *
     * @param username
     * @return
     */
    int getRowCount(String username);

    /**
     * 基于条件查询当前页用户信息以及用户对应的部门信息，方案：
     * 1)方案1：业务层向数据层发起多此查询请求，然后进行数据封装
     * 2)方案2：数据层通过嵌套查询(先查询一张表，可以基于结果再次查询其它表)
     * 3)方案3：通过多表关联查询，查询这些信息
     * select u.*,d.name deptName
     * from sys_users u left join sys_depts d
     * on u.dept_id=d.id
     *
     * @param username
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<SysUserDeptVo> findPageObjects(String username, Integer startIndex, Integer pageSize);
}



