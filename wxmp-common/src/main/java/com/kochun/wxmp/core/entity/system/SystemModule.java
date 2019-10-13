package com.kochun.wxmp.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SystemModule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统模块ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模块编码
     */
    private String code;

    /**
     * 模块名称
     */
    private String name;

    /**
     * 上级模块ID
     */
    private String pid;

    /**
     * 模块URL
     */
    private String url;

    /**
     * shiro权限集
     */
    private String perms;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 图标路径
     */
    private String iconUrl;

    /**
     * 是否显示：
     */
    private Boolean isShow;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

    private Integer type;

    /**
     * 菜单的级别
     */
    private Integer level;


}
