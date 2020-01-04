package com.kochun.wxmp.service.back.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kochun.wxmp.core.dto.SystemModuleDTO;
import com.kochun.wxmp.core.vo.system.MenuMetaVo;
import com.kochun.wxmp.core.vo.system.MenuVo;
import com.kochun.wxmp.core.entity.system.SysUser;
import com.kochun.wxmp.core.entity.system.SysUserRole;
import com.kochun.wxmp.core.entity.system.SystemModule;
import com.kochun.wxmp.core.service.SystemModuleService;
import com.kochun.wxmp.mapper.system.RoleMapper;
import com.kochun.wxmp.mapper.system.SysUserMapper;
import com.kochun.wxmp.mapper.system.SysUserRoleMapper;
import com.kochun.wxmp.mapper.system.SystemModuleMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
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

    @Resource
    SysUserMapper sysUserMapper;

    /**
     * @param userId
     * @return java.util.List<SystemModule>
     * @author kochun
     * @description //TODO
     * @date 2019/8/20 10:55
     **/
    @Override
    public List<SystemModule> listSystemModuleByUserId(Long userId) {
        SysUser user= sysUserMapper.selectById(userId);
        // 初始删除用户
        if (user.getDeleted()) {
            return new ArrayList<>();
        }
        List<SysUserRole> roleList = sysUserRoleMapper.selectList(null);
        //角色问题
        if (roleList == null || roleList.size() == 0) {
            return new ArrayList<>();
        }
        if (user.getSupered()) {
            // 超级权限
            return systemModuleMapper.selectList(null);
        } else {
            return systemModuleMapper.listSystemModuleListByUserId(userId);
        }
    }

    /**
     * @param list 数据库里面获取到的全量菜单列表
     * @return
     * @方法名: parseMenuTree<br>
     * @描述: 组装菜单<br>
     */
    @Override
    public List<SystemModuleDTO> parseMenuTree(List<SystemModule> list) {
        List<SystemModuleDTO> result = new ArrayList<SystemModuleDTO>();
        List<SystemModuleDTO> targetList = new ArrayList<>();
        //创建菜单，排除
        for (SystemModule s : list) {
            if (s.getType() == 1) {
                SystemModuleDTO systemModuleDTO = new SystemModuleDTO();
                BeanUtils.copyProperties(s, systemModuleDTO);
                targetList.add(systemModuleDTO);
            }
        }
        //List<SystemModule> filterList=list.stream().filter(e -> e.getType()==1||e.getType()==0).collect(Collectors.toList());
        // 1、获取第一级节点
        for (SystemModuleDTO menu : targetList) {
            if(-1 == menu.getPid().intValue()) {
                result.add(menu);
            }
        }
      // 2、递归获取子节点
        for (SystemModuleDTO parent : result) {
             buildChilTree(parent,targetList);
        }
        return result;
    }

    /***
     * 递归，建立子树形结构
     * @author kochun
     * @date 2019/12/27 10:34
     * @param pNode, list
     * @return com.kochun.wxmp.core.dto.SystemModuleDTO
     **/
    private SystemModuleDTO buildChilTree(SystemModuleDTO pNode,List<SystemModuleDTO> list) {
        List<SystemModuleDTO> chilMenus = new ArrayList<>();
        for (SystemModuleDTO menuNode : list) {
            if (menuNode.getPid().equals(pNode.getId())) {
                chilMenus.add(buildChilTree(menuNode,list));
            }
        }
        pNode.setChildren(chilMenus);
        return pNode;
    }

    /***
     * //构建前端VUE树形菜单结构
     * @author kochun
     * @date 2019/12/27 10:39
     * @param systemModuleDTOS
     * @return java.util.List<com.kochun.wxmp.core.vo.system.MenuVo>
     **/
    @Override
    public List<MenuVo> buildMenus(List<SystemModuleDTO> systemModuleDTOS) {
        List<MenuVo> list = new LinkedList<>();
        systemModuleDTOS.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        List<SystemModuleDTO> menuDTOList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(StringUtils.isNotEmpty(menuDTO.getComponentName()) ? menuDTO.getComponentName() : menuDTO.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == 0 ? "/" + menuDTO.getPath() : menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if (!menuDTO.getIFrame()) {
                            if (menuDTO.getPid() == 0) {
                                menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                            } else if (!StringUtils.isEmpty(menuDTO.getComponent())) {
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getName(), menuDTO.getIcon(), !menuDTO.getCache()));
                        if (menuDTOList != null && menuDTOList.size() != 0) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDTOList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menuDTO.getPid().intValue() == 0) {
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (!menuDTO.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    /**
     * @param userName
     * @return java.util.List<SystemModule>
     * @author kochun
     * @description //TODO
     * @date 2019/8/20 10:55
     **/
    @Override
    public List<SystemModule> listSystemModulePermissionByUserName(String userName) {
        List<SystemModule> result=new ArrayList<>();
        QueryWrapper<SysUser> queryWrapper=new QueryWrapper();
        queryWrapper.eq("name",userName);
        List<SysUser> list=sysUserMapper.selectList(queryWrapper);
        if (list!=null&&list.size()>0){
            SysUser sysUser=list.get(0);
            if (sysUser.getSupered()){
                QueryWrapper<SystemModule> wrapper=new QueryWrapper();
                //wrapper.eq("type",2);
                result = systemModuleMapper.selectList(wrapper);

            }else {
                result=systemModuleMapper.listSystemModulePermissionByUserId(list.get(0).getId());
            }
        }
        return result;
    }


}
