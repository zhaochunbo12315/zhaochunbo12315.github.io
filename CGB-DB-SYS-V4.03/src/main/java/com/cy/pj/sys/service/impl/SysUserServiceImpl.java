package com.cy.pj.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;
import com.cy.pj.sys.vo.SysUserMenuVo;

/**
 * @Transactional 注解描述类时，表示类中所有方法都要开启事务
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED,
        rollbackFor = Throwable.class,
        readOnly = false,
        timeout = 60,
        propagation = Propagation.REQUIRED)

//@Slf4j
public class SysUserServiceImpl implements SysUserService {
    //private static final Logger log=
    //LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;


    @Override
    public int updatePassword(String sourcePassword, String newPassword, String cfgPassword) {
        //1.参数校验
        //1)非空校验
        if (StringUtils.isEmpty(sourcePassword))
            throw new IllegalArgumentException("原密码不能为空");
        if (StringUtils.isEmpty(newPassword))
            throw new IllegalArgumentException("新密码不能为空");
        //2)新密码和确认密码是否相同
        if (!newPassword.equals(cfgPassword))
            throw new IllegalArgumentException("新密码和确认密码不一致");
        //3)校验原密码是否正确(将sourcePassword加密以后与数据库中的密码进行比对)
        //3.1)获取登陆用户
        SysUser user = ShiroUtils.getUser();
        //3.2)对用户输入的原密码进行加密
        SimpleHash sHash = new SimpleHash("MD5", sourcePassword, user.getSalt(), 1);
        String sourceHashedPassword = sHash.toHex();
        //3.3)获取登陆用户对应的数据库中已经加密的密码
        String hashedPassword = user.getPassword();
        if (!hashedPassword.equals(sourceHashedPassword))
            throw new IllegalArgumentException("原密码输入的不正确");
        //2.修改密码
        //1)获取新的盐值
        String newSalt = UUID.randomUUID().toString();
        //2)对用户输入的新的密码进行加密
        sHash = new SimpleHash("MD5", newPassword, newSalt, 1);
        String newHashedPassword = sHash.toHex();
        //3)更新密码
        int rows = sysUserDao.updatePassword(newHashedPassword, newSalt, user.getId());
        //3.返回结果
        return rows;
    }

    /**
     * 方法上的@Transactional注解用于定义事务特性，readOnly=true一般用于描述查询方法，表示这是一个读事务，
     * 允许并发读。
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> findObjectById(Integer userId) {
        //log.info("method start {}", System.currentTimeMillis());
        //1.参数校验
        if (userId == null || userId < 1)
            throw new IllegalArgumentException("用户id不正确");
        //2.查询用户信息以及对应部门信息
        SysUserDeptVo user = sysUserDao.findObjectById(userId);
        if (user == null)
            throw new ServiceException("用户可能已经不存在");
        //3.查询用户对应的角色
        List<Integer> roleIds =
                sysUserRoleDao.findRoleIdsByUserId(userId);
        //4.对数据进行封装
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("roleIds", roleIds);
        //log.info("method end {} ",System.currentTimeMillis());
        return map;
    }

    @Override
    public int updateObject(SysUser entity, Integer[] roleIds) {
        //log.info("method start {}",System.currentTimeMillis());
        //1.参数校验
        if (entity == null)
            throw new IllegalArgumentException("保存对象不能为空");
        if (StringUtils.isEmpty(entity.getUsername()))
            throw new IllegalArgumentException("用户名不允许为空");
        if (roleIds == null || roleIds.length == 0)
            throw new IllegalArgumentException("必须为用户指定角色");
        //2.更新用户自身信息
        int rows = sysUserDao.updateObject(entity);
        //3.更新用户和角色关系数据
        //3.1先删除原有关系数据
        sysUserRoleDao.deleteObjectsByUserId(entity.getId());
        //3.2再添加新的关系数据
        sysUserRoleDao.insertObjects(entity.getId(), roleIds);
        //log.info("method end {}",System.currentTimeMillis());
        //4.返回结果
        return rows;
    }

    //@RequiredLog("保存用户")
    @Override
    public int saveObject(SysUser entity, Integer[] roleIds) {
        //log.info("method start:"+System.currentTimeMillis());
        //1.参数校验
        if (entity == null)
            throw new IllegalArgumentException("保存对象不能为空");
        if (StringUtils.isEmpty(entity.getUsername()))
            throw new IllegalArgumentException("用户名不允许为空");
        if (StringUtils.isEmpty(entity.getPassword()))
            throw new IllegalArgumentException("密码不允许为空");
        if (roleIds == null || roleIds.length == 0)
            throw new IllegalArgumentException("必须为用户指定角色");
        //2.保存用户自身信息
        //2.1对密码进行加密
        String salt = UUID.randomUUID().toString();
        //String hashedPassword=DigestUtils.md5DigestAsHex((entity.getPassword()+salt).getBytes());
        SimpleHash simpleHash = new SimpleHash(
                "MD5",//algorithmName 算法名称
                entity.getPassword(), //source原先的密码
                salt,//salt盐值
                1);//hashIterations加密次数
        String hashedPassword = simpleHash.toHex();
        //2.2将密码，盐值存储到entity对象
        entity.setSalt(salt);
        entity.setPassword(hashedPassword);
        //2.3将entity对象写入到数据库
        int rows = sysUserDao.insertObject(entity);
        //3.保存用户和角色关系数据
        sysUserRoleDao.insertObjects(entity.getId(), roleIds);

        //log.info("method end:"+System.currentTimeMillis());
        //4.返回结果
        return rows;
    }

    /**
     * @RequiresPermissions 注解由shiro提供， 描述方法时，表示此方法必须授权才能访问
     * 思考：
     * 1)为谁进行授权?登陆用户(已认证的用户)
     * 2)为什么给他授权？(认证用户具备访问这个资源的权限)
     * 3)如何检测认证用户是否具备访问这个资源的权限？
     * 3.1)查询用户具备什么权限标识?(用户可以访问哪些菜单-菜单中有一个权限标识字段:permission)
     * 3.2)检测用户拥有的权限标识中是否包含@RequiresPermissions注解中定义的字符串，假如有
     * 则认为有权限，
     * 4)由谁授权？SecurityManager(这个接口继承了认证和授权接口)
     */
    @RequiresPermissions("sys:user:update")
    @RequiredLog("禁用启用")//注解中的内容表示操作
    @Override
    public int validById(Integer id, Integer valid, String modifiedUser) {
        //1.参数校验
        if (id == null || id < 1)
            throw new IllegalArgumentException("用户id不正确");
        if (valid != 1 && valid != 0)
            throw new IllegalArgumentException("用户状态不正确");
        //....
        //2.修改用户状态
        int rows = sysUserDao.validById(id, valid, modifiedUser);
        if (rows == 0)
            throw new ServiceException("用户可能不存在");
        //3.返回结果
        return rows;
    }

    @Transactional(readOnly = true)
    @RequiredLog("用户查询")
    @Override
    public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
        System.out.println("user.service.findPageObjects.thread.name=" + Thread.currentThread().getName());
        //1.参数校验
        if (pageCurrent == null || pageCurrent < 1)
            throw new IllegalArgumentException("页码值无效");
        //2.查询总记录数并校验
        int rowCount = sysUserDao.getRowCount(username);
        if (rowCount == 0)
            throw new ServiceException("没有找到对应记录");
        //3.查询当前页要呈现的记录
        int pageSize = 3;
        int startIndex = (pageCurrent - 1) * pageSize;
        List<SysUserDeptVo> records =
                sysUserDao.findPageObjects(username, startIndex, pageSize);

        //4.封装结果并返回
        return new PageObject<SysUserDeptVo>(rowCount, records, pageCurrent, pageSize);

    }

}
