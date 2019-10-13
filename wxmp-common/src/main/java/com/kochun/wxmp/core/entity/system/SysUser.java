package com.kochun.wxmp.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_REGISTER = 0;
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_DISABLE = -1;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String password;

    private String fullName;

    /**
     * 性别
     */
    private Boolean sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 出生日期
     */
    private LocalDateTime birthday;

    /**
     * 用户状态，默认1
     */
    private Integer status;

    @TableField(value = "is_deleted")
    private Boolean deleted;

    @TableField(value = "is_super")
    private Boolean supered;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    /**
     * 激活验证码
     */
    private String validateCode;

    /**
     * 禁用时间
     */
    private LocalDateTime banTime;

    /***
     * 盐  用于加密 @TableField(exist = false)代表不属于表字段
     * @author kochun
     * @date 2019/8/29 11:29
     **/
    @TableField(exist = false)
    private String  salt;


}
