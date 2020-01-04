package com.kochun.wxmp.core.dto;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author kochun
 * @date 2018-12-25
 */
@Data
public class SystemModuleDTO  implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 是否外链
     */
    private Boolean iFrame;

    /**
     * 编码
     */
    private String code;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 组件
     */
    private String component;

    /**
     * 上级菜单ID
     */
    private Long pid;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 图标
     */
    private String icon;

    /**
     * 链接地址
     */
    private String path;

    /**
     * vue前端是否缓存
     */
    private Boolean cache;

    /**
     * 菜单是否显示
     */
    private Boolean hidden;

    /**
     * VUE组件名
     */
    private String componentName;


    private Integer type;


    private List<SystemModuleDTO> children;

}
