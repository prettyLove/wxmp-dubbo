package com.kochun.wxmp.back.config;

import com.kochun.wxmp.back.shiro.JWTShiroRealm;
import com.kochun.wxmp.back.shiro.ShiroFilterChainManager;
import com.kochun.wxmp.back.shiro.cache.CustomCacheManager;
import com.kochun.wxmp.back.shiro.filter.JwtAuthFilter;
import com.kochun.wxmp.back.shiro.filter.URLPermissionsFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.Map;

/**
 * @author kochun
 * @description shiro配置类
 * @date 2019/8/25 21:33
 **/
@Configuration
public class ShiroConfig {


    /**
     * token 验证
     * @author kochun
     * @date 2019/9/11 18:29
     * @return com.kochun.wxmp.back.shiro.JWTShiroRealm
     **/
    @Bean("jwtRealm")
    public JWTShiroRealm jwtShiroRealm() {
        JWTShiroRealm rm = new JWTShiroRealm();
        return rm;
    }


    @Bean("securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(JWTShiroRealm jwtShiroRealm, RedisTemplate<String, Object> template ) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 使用自定义Realm
        defaultWebSecurityManager.setRealm(jwtShiroRealm);
        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        // 设置自定义Cache缓存
        defaultWebSecurityManager.setCacheManager(new CustomCacheManager(template));
        return defaultWebSecurityManager;
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager, ShiroFilterChainManager shiroFilterChainManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器 必须设置 SecurityManager
        factoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("authcToken", createAuthFilter());
        //filterMap.put("perms", createURLPermissionsFilter());

        factoryBean.setFilters(filterMap);

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        factoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        factoryBean.setSuccessUrl("/index");

        //未授权界面
        factoryBean.setUnauthorizedUrl("/401");
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainManager.shiroFilterChainDefinition().getFilterChainMap());

        System.out.println("Shiro拦截器工厂类注入成功");
        return factoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题，https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，如果要完全禁用，要配合下面的noSessionCreation的Filter来实现
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    /**
     * <pre>
     * 注入bean，此处应注意：
     *
     * (1)代码顺序，应放置于shiroFilter后面，否则报错：
     * 	No SecurityManager accessible to the calling code, either bound to the org.apache.shiro.util.
     * 	ThreadContext or as a vm static singleton. This is an invalid application configuration.
     *
     * (2)如不在此注册，在filter中将无法正常注入bean
     * </pre>
     */
    @Bean("JwtAuthFilter")
    protected JwtAuthFilter createAuthFilter( ){
        return new JwtAuthFilter();
    }

//
//    @Bean("URLPermissionsFilter")
//    protected URLPermissionsFilter createURLPermissionsFilter( ){
//        return new URLPermissionsFilter();
//    }

}
