package com.kochun.wxmp.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kochun.wxmp.core.entity.system.SystemRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author kochun
 * @since 2019-08-18
 */
public interface SystemRoleMapper extends BaseMapper<SystemRole> {


    @Select("SELECT * FROM system_role r,system_user_role sur WHERE sur.user_id=#{agr0} and sur.role_id=r.id")
    List<SystemRole> getRoleIdsByUserId(long userId);


    @Select("SELECT * FROM system_role r,system_user_role sur,system_user su WHERE su.name=#{agr0} and su.id=sur.user_id and sur.role_id=r.id")
    List<SystemRole> getRoleIdsByUserName(String userName);

}
