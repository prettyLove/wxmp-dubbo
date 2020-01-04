package com.kochun.wxmp.core.vo.system;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: kochun
 * @Date: 2019-09-12 22:18
 * @version: 1.0
 */
@Data
public class RoleSystemModuleVO implements Serializable {

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 模块URL
     */
    private String moduleUrl;

}
