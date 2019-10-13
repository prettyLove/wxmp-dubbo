package com.kochun.wxmp.service.back.system.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kochun.wxmp.core.entity.system.Role;
import com.kochun.wxmp.core.service.RoleService;
import com.kochun.wxmp.mapper.system.RoleMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author kochun
 * @since 2019-08-18
 */
@Service(version = "1.0.0")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    RoleMapper roleMapper;

    /**
     * @author kochun
     * @description 通过userId获得该用户的所有角色ID
     * @date 2019/8/22 13:46
     * @param userId
     * @return java.util.List<Role>
     **/
    @Override
    public List<Role> getRoleIdsByUserId(long userId) {
        return roleMapper.getRoleIdsByUserId(userId);
    }

    /**
     * @param userName
     * @return java.util.List<Role>
     * @author kochun
     * @description 通过userId获得该用户的所有角色ID
     * @date 2019/8/22 13:46
     **/
    @Override
    public List<Role> getRoleIdsByUserName(String userName) {
        return roleMapper.getRoleIdsByUserName(userName);
    }


}
