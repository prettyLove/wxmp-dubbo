package com.kochun.wxmp.back.controller;


import com.kochun.wxmp.core.dto.SystemModuleDTO;
import com.kochun.wxmp.core.entity.system.SysUser;
import com.kochun.wxmp.core.entity.system.SystemModule;
import com.kochun.wxmp.core.service.SystemModuleService;
import com.kochun.wxmp.core.service.common.RedisService;
import com.kochun.wxmp.core.vo.system.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kochun
 * @since 2019-08-18
 */
@Api(tags = "系统模块管理", value = "菜单，权限")
@RestController
@RequestMapping("/systemModule")
public class SystemModuleController {

    @Reference(version = "1.0.0")
    SystemModuleService systemModuleService;

    @Reference(version = "1.0.0")
    RedisService redisService;

    @ApiOperation("获取前端所需菜单")
    @GetMapping(value = "/buildMenus")
    public ResponseEntity buildMenus(){
        List<MenuVo> menuVoList=new ArrayList<>();
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            String token = (String) subject.getPrincipals().getPrimaryPrincipal();
            SysUser user= (SysUser) redisService.get(token);
            List<SystemModule> systemModules = systemModuleService.listSystemModuleByUserId(user.getId());
            List<SystemModuleDTO> systemModuleDTOS=systemModuleService.parseMenuTree(systemModules);
            menuVoList = systemModuleService.buildMenus(systemModuleDTOS);
        }
        return new ResponseEntity<>(menuVoList, HttpStatus.OK);
    }

}
