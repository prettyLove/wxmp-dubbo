package com.kochun.wxmp.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author kochun
 * @since 2020-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SystemModule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    /**
     * 权限标识

     */
    private String permission;

    private Integer type;


}
