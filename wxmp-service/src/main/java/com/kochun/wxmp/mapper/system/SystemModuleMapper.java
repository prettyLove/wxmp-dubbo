package com.kochun.wxmp.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kochun.wxmp.core.vo.system.RoleSystemModuleVO;
import com.kochun.wxmp.core.entity.system.SystemModule;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kochun
 * @since 2019-08-18
 */
public interface SystemModuleMapper extends BaseMapper<SystemModule> {

    /**
     * 根据用户ID 查询菜单信息
     *
     * @param userId
     * @return
     */
    @Select("SELECT * FROM system_module where id IN(SELECT module_id from role_permission WHERE role_id IN(SELECT role_id FROM sys_user_role WHERE  user_id=#{agr0}))")
    List<SystemModule> listSystemModuleListByUserId(long userId);



    /**
     * 根据用户ID 查询所有权限permission，菜单除外
     *
     * @param userId
     * @return
     */
    @Select("SELECT * FROM system_module where id IN(SELECT module_id from role_permission WHERE role_id IN(SELECT role_id FROM sys_user_role WHERE  user_id=#{agr0}))")
    List<SystemModule> listSystemModulePermissionByUserId(long userId);


}
