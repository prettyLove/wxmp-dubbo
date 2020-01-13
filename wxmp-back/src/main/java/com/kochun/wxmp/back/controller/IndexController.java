package com.kochun.wxmp.back.controller;

import com.alibaba.fastjson.JSONArray;
import com.kochun.wxmp.core.entity.system.SystemModule;
import com.kochun.wxmp.core.entity.system.SystemUser;
import com.kochun.wxmp.core.service.SystemModuleService;
import com.kochun.wxmp.core.service.SystemUserService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 *  测试
 * @author kochun
 * @date 2019/10/7 12:08
 * @param
 * @return
 **/
@RestController
public class IndexController {

    @Reference(version = "1.0.0")
    SystemUserService systemUserService;

    @Reference(version = "1.0.0")
    SystemModuleService systemModuleService;

    @GetMapping("/getSystemModule")
    public ResponseEntity<?> getUserList(){

        List<SystemModule> list=systemModuleService.list();

        System.out.println(JSONArray.toJSONString(list));

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/test")
    public String test(){

        List<SystemUser> list=systemUserService.list();

        System.out.println(JSONArray.toJSONString(list));

        return "111";
    }

    @PostMapping("/user/list")
    @RequiresPermissions("user:list")
    public String list(){

        List<SystemUser> list=systemUserService.list();

        System.out.println(JSONArray.toJSONString(list));

        return "user:list";
    }

    @PostMapping("/user/view")
    @RequiresPermissions("user:view")
    public String view(){

        List<SystemUser> list=systemUserService.list();

        System.out.println(JSONArray.toJSONString(list));

        return "user:view";
    }
}
