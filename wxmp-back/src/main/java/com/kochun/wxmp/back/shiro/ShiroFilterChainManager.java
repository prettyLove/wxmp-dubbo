package com.kochun.wxmp.back.shiro;
import com.kochun.wxmp.core.entity.system.SystemModule;
import com.kochun.wxmp.core.vo.system.RoleSystemModuleVO;
import com.kochun.wxmp.core.service.SystemModuleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author tomsun28
 * @Description Filter 管理器
 * @Date 11:16 2018/2/28
 */
@Component
public class ShiroFilterChainManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroFilterChainManager.class);

    @Resource
    private SystemModuleService systemModuleService;



    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {

        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        chainDefinition.addPathDefinition("/", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/index", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/test/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/login", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/logout", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/sysUser/logout", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/signup", "noSessionCreation,anon");
        // swagger-ui
        chainDefinition.addPathDefinition("/swagger-ui.html", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/swagger-ui.html/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/v2/api-docs", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/configuration/ui", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/swagger-resources", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/swagger-resources/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/swagger-resources/**/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/configuration/security", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/webjars/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/webjars/springfox-swagger-ui/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/null/swagger-resources/", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/null/swagger-resources/configuration/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/null/swagger-resources/configuration/ui", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/null/swagger-resources/configuration/security", "noSessionCreation,anon");


        //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //自定义加载权限资源关系  初始化需要加载所有的需要登陆验证资源
//        List<SystemModule> resourcesList = systemModuleService.list();
//        resourcesList.forEach(systemModule -> {
//                    if (StringUtils.isNotEmpty(systemModule.getPermission())) {
//                        String permission = systemModule.getPermission();
//                        // 排除#
//                        if (permission != null) {
//                            // 这里的url 不合规会造成 tomcat 启动失败
//                            permission = permission.trim();
//                            if (permission.length() > 2) {
//                                chainDefinition.addPathDefinition(permission, "noSessionCreation,authcToken,pathPermissions");
//                            }
//                        }
//                    }
//                });

//        String module = systemModule.getModuleUrl();
//        // 排除#
//        if (module != null) {
//            // 这里的url 不合规会造成 tomcat 启动失败
//            module = module.trim();
//            if (module.length() > 2) {
//
//                System.out.println(permission + "  len:" + module.length() + "");
//                chainDefinition.addPathDefinition(module, permission);
//            }
//        }
       // System.out.println("资源条目："+resourcesList.size());

        //chainDefinition.addPathDefinition("/user/list","noSessionCreation,authcToken");
        //chainDefinition.addPathDefinition("/user/view","noSessionCreation,authcToken");

        chainDefinition.addPathDefinition("/**", "authcToken");
        return chainDefinition;
    }
}
