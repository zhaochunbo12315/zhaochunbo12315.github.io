package com.cy.pj.sys.service.realm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;

/**
 * Shiro框架中的Realm对象，通过此对象完成认证和授权业务数据的获取和封装。
 *
 * @author qilei
 */
@Service
public class ShiroUserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserDao sysUserDao;

    /**
     * 设置凭证匹配器，通过此对象对登陆时输入的密码进行加密，
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        //1.构建凭证匹配其对象
        HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher();
        //2.设置加密算法
        cMatcher.setHashAlgorithmName("MD5");
        //3.设置加密次数
        cMatcher.setHashIterations(1);
        super.setCredentialsMatcher(cMatcher);//一定要将创建对象传入父类方法。
    }

    /**
     * 此方法负责认证信息的获取和封装
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.获取用户登陆时输入的用户名
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        String username = uToken.getUsername();
        //2.基于用户名查询数据库中对应的用户信息.
        SysUser user = sysUserDao.findUserByUserName(username);
        //3.验证用户是否存在
        if (user == null) throw new UnknownAccountException();
        //4.验证用户是否被禁用
        if (user.getValid() == 0) throw new LockedAccountException();
        //5.对查询到的用户信息进行封装并返回。
        ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user,//principal 用户身份
                user.getPassword(), //hashedCredentials 已加密的密码
                credentialsSalt,//credentialsSalt
                getName());
        return info;//info对象会返回给securityManager对象，由此对象调用认证方法对用户信息进行认证
    }

    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Autowired
    private SysMenuDao sysMenuDao;

    /**
     * 负责授权信息的获取和封装
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("==doGetAuthorizationInfo==");
        //1.获取登陆用户信息(当获取用户身份时，我们应该如何进行转换由登陆时设置的身份决定)
        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        //2.基于登陆用户id查找用户对应的角色id并校验
        List<Integer> roleIds =
                sysUserRoleDao.findRoleIdsByUserId(user.getId());
        if (roleIds == null || roleIds.size() == 0)
            throw new AuthorizationException();
        //3.基于角色id获取角色对应的菜单id并校验
        List<Integer> menuIds =
                sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[]{}));
        if (menuIds == null || menuIds.size() == 0)
            throw new AuthorizationException();
        //4.基于菜单id获取菜单对应的授权标识(permission)
        List<String> permissions =//sys:user:update,sys:user:update
                sysMenuDao.findPermissions(menuIds.toArray(new Integer[]{}));
        if (permissions == null || permissions.size() == 0)
            throw new AuthorizationException();
        Set<String> stringPermissions = new HashSet<>();//不允许内容重复
        for (String per : permissions) {
            if (!StringUtils.isEmpty(per)) {
                stringPermissions.add(per);
            }
        }
        //System.out.println("user.permissions="+stringPermissions);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(stringPermissions);
        return info;//返回给securityManager，由此对象基于用户权限信息进行授权。
    }


}
