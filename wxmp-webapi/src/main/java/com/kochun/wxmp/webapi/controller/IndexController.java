package com.kochun.wxmp.webapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kochun.wxmp.core.entity.system.SystemUser;
import com.kochun.wxmp.core.service.SystemUserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class IndexController {

    @Reference(version = "1.0.0")
    SystemUserService systemUserService;

    @GetMapping("/getUserList")
    public String getUserList(){

        List<SystemUser> list=systemUserService.list();

        //List<SysUser> list1=sysUserService.selectTest();
        IPage<SystemUser> page=new Page<>();
        page.setPages(2);
        page.setSize(10);
        Page<SystemUser> pageList= (Page<SystemUser>) systemUserService.page(page);

        System.out.println(JSONArray.toJSONString(list));

        //System.out.println(JSONArray.toJSONString(list1));

        System.out.println(JSONArray.toJSONString(pageList));
        return "111";
    }
}
