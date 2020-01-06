package com.kochun.wxmp.service.back.system.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kochun.wxmp.core.entity.system.SystemUserRole;
import com.kochun.wxmp.mapper.system.SystemUserRoleMapper;
import org.apache.dubbo.config.annotation.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kochun
 * @since 2019-08-22
 */
@Service(version = "1.0.0")
public class SystemUserRoleServiceImpl extends ServiceImpl<SystemUserRoleMapper, SystemUserRole> implements IService<SystemUserRole> {

}
