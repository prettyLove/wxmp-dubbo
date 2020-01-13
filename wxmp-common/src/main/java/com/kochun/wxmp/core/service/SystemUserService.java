package com.kochun.wxmp.core.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kochun.wxmp.core.entity.system.SystemUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kochun
 * @since 2019-08-09
 */
public interface SystemUserService extends IService<SystemUser> {


    /**
     * @author kochun
     * @description 通过账号获得用户信息
     * @date 2019/8/20 10:17
     * @param userName
     * @return SysUser
     **/
    SystemUser getUserByUserName(String userName);


    SystemUser getJwtTokenInfo(String username);




    /**
     * 根据邮箱查询用户
     * @author kochun
     * @date 2019/8/25 22:20
     * @param email
     * @return SysUser
     **/
    SystemUser findUserByEmail(String email);

    /**
     * 根据手机号查询用户
     * @author kochun
     * @date 2019/8/25 22:20
     * @param phone
     * @return SysUser
     **/
    SystemUser findUserByPhone(String phone);


    /**
     * 注册方法
     * @author kochun
     * @date 2019/8/25 22:34
     * @param user
     * @return int
     **/
    int register(SystemUser user);


    /**
     * //用户激活
     * @author kochun
     * @date 2019/8/28 22:22
     * @param validateCode
     * @return
     **/
    int activated(String validateCode);



    void deleteLoginInfo(String username);


    IPage<SystemUser> list(Integer pageNumber,Integer pageSize,SystemUser systemUser);
}
