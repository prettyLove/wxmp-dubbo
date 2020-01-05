package com.kochun.wxmp.back.shiro;

import com.kochun.wxmp.back.shiro.jwt.JwtToken;
import com.kochun.wxmp.common.Constant;
import com.kochun.wxmp.common.utils.StringUtil;
import com.kochun.wxmp.core.entity.system.Role;
import com.kochun.wxmp.core.entity.system.SysUser;
import com.kochun.wxmp.core.entity.system.SystemModule;
import com.kochun.wxmp.core.service.RoleService;
import com.kochun.wxmp.core.service.SysUserService;
import com.kochun.wxmp.core.service.SystemModuleService;
import com.kochun.wxmp.core.service.common.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;


/**
 * 自定义身份认证
 * 基于HMAC（ 散列消息认证码）的控制域
 */
public class JWTShiroRealm extends AuthorizingRealm {
    private final Logger log = LoggerFactory.getLogger(JWTShiroRealm.class);

    @Resource
    private SysUserService sysUserService;

    @Resource
    private RoleService roleService;

    @Resource
    private RedisService redisService;

    @Resource
    SystemModuleService systemModuleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("验证token==========  判断用户是否正确");
        String token = (String) authenticationToken.getCredentials();
        System.out.println("当前token:    "+token);
        // 解密获得account，用于和数据库进行对比
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 帐号为空
        if (StringUtil.isBlank(account)) {
            throw new AuthenticationException("Token中帐号为空(The account in Token is empty.)");
        }
//        if (sysUserService == null) {
//            this.sysUserService = SpringContextBeanService.getBean(SysUserService.class);
//        }
//        if (redisService == null) {
//            this.redisService = SpringContextBeanService.getBean(RedisService.class);
//        }

        // 查询用户是否存在
        SysUser sysUser = sysUserService.getJwtTokenInfo(account);

        if (sysUser == null) {
            throw new AuthenticationException("该帐号不存在(The account does not exist.)");
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtil.verify(token) && redisService.hasKey(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisService.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, getName());
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("获取权限================");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        String token = (String) principals.getPrimaryPrincipal();
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 普通用户 获取角色对应的菜单
        //List<SystemModule> resourcesList  = systemModuleService.getSystemModuleListByUserId(user);
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        //SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        for(SystemModule resources: resourcesList){
//            simpleAuthorizationInfo.addStringPermission(resources.getUrl());
//        }
        List<Role> roles = roleService.getRoleIdsByUserName(account);
        if (roles != null) {
            roles.forEach((Role role) -> {
                //这里循环把角色编码放进shiro角色里面，因为考虑到Id是lang类型，不匹配，同时Id没有意义
                simpleAuthorizationInfo.addRole(role.getCode());
            });

        }

        List<SystemModule> resourcesList = systemModuleService.listSystemModulePermissionByUserName(account);
        resourcesList.forEach(systemModule -> {
            if (StringUtils.isNotEmpty(systemModule.getPermission())) {
                String permission = systemModule.getPermission();
                if (StringUtils.isNotEmpty(permission)) {
                    simpleAuthorizationInfo.addStringPermission(permission);
                }
            }
        });

        //模拟添加url的权限
        //模拟添加url的权限
        //simpleAuthorizationInfo.addStringPermission("user:list");
        //simpleAuthorizationInfo.addStringPermission("user:view");

        System.out.println("权限加载完毕=============");
        return simpleAuthorizationInfo;
    }
}
