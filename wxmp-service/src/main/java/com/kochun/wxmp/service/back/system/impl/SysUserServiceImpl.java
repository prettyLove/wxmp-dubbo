package com.kochun.wxmp.service.back.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kochun.wxmp.core.entity.system.SysUser;
import com.kochun.wxmp.core.service.SysUserService;
import com.kochun.wxmp.mapper.system.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kochun
 * @since 2019-08-09
 */
@Service(version = "1.0.0")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    @Resource
    SysUserMapper sysUserMapper;

    @Value("${encryptSalt}")
    private String encryptSalt ;

    /**
     * @param userName
     * @return SysUser
     * @author kochun
     * @description 通过账号获得用户信息
     * @date 2019/8/20 10:17
     **/
    @Override
    public SysUser getUserByUserName(String userName) {
        QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",userName);
        return sysUserMapper.selectOne(queryWrapper);
    }


    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    @Override
    public SysUser getJwtTokenInfo(String username) {
        /**
         * @todo 从数据库或者缓存中取出jwt token生成时用的salt
         * salt = redisTemplate.opsForValue().get("token:"+username);
         */
        SysUser user = getUserByUserName(username);
        return user;
    }


    /**
     * @param email
     * @return SysUser
     * @author kochun
     * @description 根据邮箱查询用户
     * @date 2019/8/25 22:20
     **/
    @Override
    public SysUser findUserByEmail(String email) {
        if (StringUtils.isNotEmpty(email)){
            QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("email",email);
            return sysUserMapper.selectOne(queryWrapper);
        }
        return null;
    }

    /**
     * @param phone
     * @return SysUser
     * @author kochun
     * @description 根据手机号查询用户
     * @date 2019/8/25 22:20
     **/
    @Override
    public SysUser findUserByPhone(String phone) {
        if (StringUtils.isNotEmpty(phone)){
            QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("phone",phone);
            return sysUserMapper.selectOne(queryWrapper);
        }
        return null;
    }

    /**
     *  用户注册
     * @author kochun
     * @date 2019/8/25 22:41
     * @param user
     * @return int
     **/
    @Override
    public int register(SysUser user) {
        user.setStatus(0);
        user.setGmtCreate(LocalDateTime.now());
        user.setDeleted(false);
        return sysUserMapper.insert(user);
    }

    /**
     * //用户激活
     *
     * @param validateCode
     * @return
     * @author kochun
     * @date 2019/8/28 22:22
     **/
    @Override
    public int activated(String validateCode) {
        QueryWrapper<SysUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("validate_code",validateCode);
        SysUser sysUser=sysUserMapper.selectOne(queryWrapper);
        if (sysUser!=null){
            //激动 状态为1
            sysUser.setStatus(1);
           return sysUserMapper.updateById(sysUser);
        }
        return 0;
    }

    /**
     * 清除token信息
     * @param username 登录用户名
     */
    @Override
    public void deleteLoginInfo(String username) {
        /**
         * @todo 删除数据库或者缓存中保存的salt
         * redisTemplate.delete("token:"+username);
         */

    }
}
