package com.kochun.wxmp.back.controller;

import com.alibaba.fastjson.JSONArray;
import com.kochun.wxmp.core.entity.system.SysUser;
import com.kochun.wxmp.core.service.SysUserService;
import org.apache.dubbo.config.annotation.Reference;
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
    SysUserService sysUserService;

    @GetMapping("/getUserList")
    public String getUserList(){

        List<SysUser> list=sysUserService.list();

        System.out.println(JSONArray.toJSONString(list));

        return "111";
    }

    @PostMapping("/test")
    public String test(){

        List<SysUser> list=sysUserService.list();

        System.out.println(JSONArray.toJSONString(list));

        return "111";
    }

    @PostMapping("/user/list")
    public String list(){

        List<SysUser> list=sysUserService.list();

        System.out.println(JSONArray.toJSONString(list));

        return "user:list";
    }

    @PostMapping("/user/view")
    public String view(){

        List<SysUser> list=sysUserService.list();

        System.out.println(JSONArray.toJSONString(list));

        return "user:view";
    }
}
