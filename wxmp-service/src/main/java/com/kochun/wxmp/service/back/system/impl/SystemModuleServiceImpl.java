package com.kochun.wxmp.service.back.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kochun.wxmp.core.bo.system.RoleSystemModuleVO;
import com.kochun.wxmp.core.entity.system.Role;
import com.kochun.wxmp.core.entity.system.SysUser;
import com.kochun.wxmp.core.entity.system.SysUserRole;
import com.kochun.wxmp.core.entity.system.SystemModule;
import com.kochun.wxmp.core.service.SystemModuleService;
import com.kochun.wxmp.mapper.system.RoleMapper;
import com.kochun.wxmp.mapper.system.SysUserRoleMapper;
import com.kochun.wxmp.mapper.system.SystemModuleMapper;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kochun
 * @since 2019-08-18
 */
@Service(version = "1.0.0")
public class SystemModuleServiceImpl extends ServiceImpl<SystemModuleMapper, SystemModule> implements SystemModuleService {
    @Resource
    RoleMapper roleMapper;

    @Resource
    SystemModuleMapper systemModuleMapper;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

    /**
     * @param user
     * @return java.util.List<SystemModule>
     * @author kochun
     * @description //TODO
     * @date 2019/8/20 10:55
     **/
    @Override
    public List<SystemModule> getSystemModuleListByUserId(SysUser user) {
         // 初始删除用户
        if (user.getDeleted()) {
            return new ArrayList<>();
        }
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        //queryWrapper.eq("")
        List<SysUserRole> roleList=sysUserRoleMapper.selectList(null);
        // 角色问题
        //if (role == null || role.getIsDelete()) {
        //    return new ArrayList<>();
        //}
        //if (role.getIsSuper()) {
            // 超级权限
            return systemModuleMapper.selectList(null);
        //} else {
          //  return systemModuleMapper.getSystemModuleListByUserId(user.getRoleId());
        //}
    }

    /***
     * 获取每个角色拥有的systemModule,用于角色权限判断
     * @author kochun
     * @date 2019/9/12 22:40
     * @return java.util.List<com.kochun.wxmp.core.bo.system.RoleSystemModuleVO>
     **/
    @Override
    public List<RoleSystemModuleVO> listRoleSystemModuleVO() {

        return  systemModuleMapper.listRoleSystemModuleVO();
    }
}
