package com.kochun.wxmp.back.controller;/**
 * @ClassNameDemoController
 * @Description //TODO
 * @Author kochun
 * @Date 2019-09-27 22:35
 * @Version V1.0
 **/
import com.kochun.wxmp.core.entity.system.SystemConfig;
import com.kochun.wxmp.core.service.EmailService;
import com.kochun.wxmp.core.service.SystemModuleService;
import com.kochun.wxmp.core.service.common.RedisService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: kochun
 * @Date: 2019-09-27 22:35
 * @version: 1.0
 */
@Controller
public class DemoController {


   @Reference(version = "1.0.0")
   private EmailService emailService;

    @GetMapping("/say")
    public String say(){

        List<SystemConfig> list=emailService.getSystemConfigList();
        System.out.println(list.size());

        return "1212";
    }

}
