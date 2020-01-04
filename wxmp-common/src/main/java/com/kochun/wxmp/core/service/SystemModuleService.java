package com.kochun.wxmp.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kochun.wxmp.core.dto.SystemModuleDTO;
import com.kochun.wxmp.core.vo.system.MenuVo;
import com.kochun.wxmp.core.vo.system.RoleSystemModuleVO;
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
     * @param userId
     * @return java.util.List<SystemModule>
     **/
    List<SystemModule> listSystemModuleByUserId(Long userId);



    /***
     * // 将数据条目转换成树的层级对象
     * @author kochun
     * @date 2019/12/27 8:51
     * @param list
     * @return java.util.List<com.kochun.wxmp.core.dto.SystemModuleDTO>
     **/
    List<SystemModuleDTO> parseMenuTree(List<SystemModule> list);

    /***
     *  用于构建VUE前端左边菜单
     * @author kochun
     * @date 2019/12/27 8:22
     * @param systemModuleDTOS
     * @return java.util.List<com.kochun.wxmp.core.vo.system.MenuVo>
     **/
    List<MenuVo> buildMenus(List<SystemModuleDTO> systemModuleDTOS);



    /**
     * @author kochun
     * @description //TODO
     * @date 2019/8/20 10:55
     * @param userName
     * @return java.util.List<SystemModule>
     **/
    List<SystemModule> listSystemModulePermissionByUserName(String userName);


}
