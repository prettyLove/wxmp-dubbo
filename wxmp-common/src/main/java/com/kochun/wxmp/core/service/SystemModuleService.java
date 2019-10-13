package com.kochun.wxmp.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kochun.wxmp.core.bo.system.RoleSystemModuleVO;
import com.kochun.wxmp.core.entity.system.SysUser;
import com.kochun.wxmp.core.entity.system.SystemModule;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kochun
 * @since 2019-08-18
 */
public interface SystemModuleService extends IService<SystemModule> {


    /**
     * @author kochun
     * @description //TODO
     * @date 2019/8/20 10:55
     * @param user
     * @return java.util.List<SystemModule>
     **/
    List<SystemModule> getSystemModuleListByUserId(SysUser user);



    /***
     * 获取每个角色拥有的systemModule,用于角色权限判断
     * @author kochun
     * @date 2019/9/12 22:40
     * @param
     * @return java.util.List<com.kochun.wxmp.core.bo.system.RoleSystemModuleVO>
     **/
    List<RoleSystemModuleVO> listRoleSystemModuleVO();







}
