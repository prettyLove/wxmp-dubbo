package com.kochun.wxmp.service;

import com.alibaba.fastjson.JSONArray;
import com.kochun.wxmp.core.dto.SystemModuleDTO;
import com.kochun.wxmp.core.entity.system.SystemModule;
import com.kochun.wxmp.core.entity.system.SystemUser;
import com.kochun.wxmp.core.service.SystemModuleService;
import com.kochun.wxmp.core.vo.system.MenuVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxxmpServiceApplicationTests {

    @Resource
    private SystemModuleService systemModuleService;

    @Test
    public void contextLoads() {

        SystemUser sysUser=new SystemUser();
        sysUser.setId(2L);
        sysUser.setIsSuper(true);
        sysUser.setIsDeleted(false);
        List<SystemModule>  list= systemModuleService.listSystemModuleByUserId(sysUser.getId());
        System.out.println(list.size());

        List<SystemModuleDTO> systemModuleDTOS=systemModuleService.parseMenuTree(list);

        List<MenuVo> menuVoList=systemModuleService.buildMenus(systemModuleDTOS);
        System.out.println(systemModuleDTOS.size());
        System.out.println(JSONArray.toJSONString(systemModuleDTOS));

    }

}
