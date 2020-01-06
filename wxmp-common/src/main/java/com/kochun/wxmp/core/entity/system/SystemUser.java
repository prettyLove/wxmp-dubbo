package com.kochun.wxmp.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
public class SystemUser implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private LocalDate birthday;

    /**
     * 用户状态，默认1
     */
    private Integer status;

    private Boolean isDeleted;

    private Boolean isSuper;

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


}
