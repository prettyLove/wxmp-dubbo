package com.kochun.wxmp.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kochun.wxmp.core.bo.system.RoleSystemModuleVO;
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
     * @param roleId
     * @return
     */
    @Select("SELECT sm.id, sm.code,sm.name,sm.pid,sm.url ,sm.icon_url FROM system_module sm " +
            "            WHERE sm.id in ( SELECT p.module_id FROM role_permission p WHERE p.role_id=#{agr0})" +
            "            AND sm.is_deleted=0 AND sm.is_show=1 order by sm.sort asc")
    List<SystemModule> getSystemModuleListByUserId(long roleId);


    /***
     * 获得所有角色对应的权限模块
     * @author kochun
     * @date 2019/9/13 17:46
     * @param
     * @return java.util.List<com.kochun.wxmp.core.bo.system.RoleSystemModuleVO>
     **/
    @Select("SELECT r.`code` as role_code,r.`name` as role_name,sm.url as module_url ,sm.`code` as module_code, sm.`name` as module_name from role r,role_permission rp,system_module sm WHERE r.id=rp.role_id AND rp.module_id=sm.id ")
    List<RoleSystemModuleVO> listRoleSystemModuleVO();

}
