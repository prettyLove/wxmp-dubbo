package com.kochun.wxmp.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kochun.wxmp.core.entity.system.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author kochun
 * @since 2019-08-18
 */
public interface SystemRoleService extends IService<Role> {

    /**
     * @author kochun
     * @description 通过userId获得该用户的所有角色ID
     * @date 2019/8/22 13:46
     * @param userId
     * @return java.util.List<Role>
     **/
    List<Role> getRoleIdsByUserId(long userId);


    /**
     * @author kochun
     * @description 通过userId获得该用户的所有角色ID
     * @date 2019/8/22 13:46
     * @param userName
     * @return java.util.List<Role>
     **/
    List<Role> getRoleIdsByUserName(String userName);
}
