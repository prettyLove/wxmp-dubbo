package com.kochun.wxmp.service.back.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kochun.wxmp.core.entity.system.SystemUser;
import com.kochun.wxmp.core.service.SystemUserService;
import com.kochun.wxmp.mapper.system.SystemUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kochun
 * @since 2019-08-09
 */
@Service(version = "1.0.0")
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService {


    @Resource
    SystemUserMapper sysUserMapper;

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
    public SystemUser getUserByUserName(String userName) {
        QueryWrapper<SystemUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",userName);
        return sysUserMapper.selectOne(queryWrapper);
    }


    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    @Override
    public SystemUser getJwtTokenInfo(String username) {
        /**
         * @todo 从数据库或者缓存中取出jwt token生成时用的salt
         * salt = redisTemplate.opsForValue().get("token:"+username);
         */
        SystemUser user = getUserByUserName(username);
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
    public SystemUser findUserByEmail(String email) {
        if (StringUtils.isNotEmpty(email)){
            QueryWrapper<SystemUser> queryWrapper=new QueryWrapper<>();
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
    public SystemUser findUserByPhone(String phone) {
        if (StringUtils.isNotEmpty(phone)){
            QueryWrapper<SystemUser> queryWrapper=new QueryWrapper<>();
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
    public int register(SystemUser user) {
        user.setStatus(0);
        user.setGmtCreate(LocalDateTime.now());
        user.setIsDeleted(false);
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
        QueryWrapper<SystemUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("validate_code",validateCode);
        SystemUser sysUser=sysUserMapper.selectOne(queryWrapper);
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

    @Override
    public IPage<SystemUser> list(Integer pageNumber, Integer pageSize, SystemUser systemUser) {
        QueryWrapper queryWrapper=new QueryWrapper();
        if (pageNumber!=null&&pageSize!=null){
            IPage<SystemUser> iPage=new Page<>(pageNumber,pageSize) ;
            iPage=sysUserMapper.selectPage(iPage,queryWrapper);
            return  iPage;
        }else {
            IPage<SystemUser> iPage=new Page<>(0,0);
            iPage.setRecords(sysUserMapper.selectList(queryWrapper));
            return  iPage;
        }
    }
}
