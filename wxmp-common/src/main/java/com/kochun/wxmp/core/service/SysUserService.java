package com.kochun.wxmp.core.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kochun.wxmp.core.entity.system.SysUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kochun
 * @since 2019-08-09
 */
public interface SysUserService extends IService<SysUser> {


    /**
     * @author kochun
     * @description 通过账号获得用户信息
     * @date 2019/8/20 10:17
     * @param userName
     * @return SysUser
     **/
    SysUser getUserByUserName(String userName);


    SysUser getJwtTokenInfo(String username);




    /**
     * 根据邮箱查询用户
     * @author kochun
     * @date 2019/8/25 22:20
     * @param email
     * @return SysUser
     **/
    SysUser findUserByEmail(String email);

    /**
     * 根据手机号查询用户
     * @author kochun
     * @date 2019/8/25 22:20
     * @param phone
     * @return SysUser
     **/
    SysUser findUserByPhone(String phone);


    /**
     * 注册方法
     * @author kochun
     * @date 2019/8/25 22:34
     * @param user
     * @return int
     **/
    int register(SysUser user);


    /**
     * //用户激活
     * @author kochun
     * @date 2019/8/28 22:22
     * @param validateCode
     * @return
     **/
    int activated(String validateCode);



    void deleteLoginInfo(String username);
}
