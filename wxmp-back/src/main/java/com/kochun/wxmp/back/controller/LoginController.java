package com.kochun.wxmp.back.controller;

import com.kochun.wxmp.back.il8n.LocaleMessage;
import com.kochun.wxmp.back.shiro.AesCipherUtil;
import com.kochun.wxmp.back.shiro.JwtUtil;
import com.kochun.wxmp.common.Constant;
import com.kochun.wxmp.common.exception.CustomUnauthorizedException;
import com.kochun.wxmp.core.entity.system.SystemUser;
import com.kochun.wxmp.core.service.SystemUserService;
import com.kochun.wxmp.core.service.common.RedisService;
import com.kochun.wxmp.core.vo.internal.request.LoginVo;
import com.kochun.wxmp.core.vo.internal.response.JwtAuthenticationResponse;
import com.kochun.wxmp.core.vo.internal.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * //
 *
 * @author kochun
 * @date 2019/8/29 22:10
 **/
@Api(tags = "登陆", value = "用户登陆退出")
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Value("${system.config.refreshTokenExpireTime}")
    private int refreshTokenExpireTime;

    @Resource
    private SystemUserService systemUserService;

    @Resource
    LocaleMessage localeMessage;

    @Resource
    RedisService redisService;


    // api 参数说明
    @ApiOperation(value = "用户登陆", notes = "用户名密码必须")
    @PostMapping(value = "/login")
    public ResponseEntity<?> signIn(@RequestBody LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        ResponseResult responseResult;

        if (loginVo.getUsername() == null || loginVo.getPassword() == null) {
            System.out.println(localeMessage.getMessage("PARAMS_BAD"));
            responseResult = ResponseResult.failResponse(localeMessage.getMessage("welcome"));
            return new ResponseEntity<>(responseResult, HttpStatus.OK);
        }
        responseResult = ResponseResult.failResponse("error");

        SystemUser user = systemUserService.getUserByUserName(loginVo.getUsername());

        try {

            if (user != null) {
                if (user.getStatus() == SystemUser.STATUS_REGISTER) {
                    responseResult.setMessage("用户未激活");
                    return new ResponseEntity<>(responseResult, HttpStatus.OK);
                }
                if (user.getStatus() == SystemUser.STATUS_DISABLE) {
                    if (LocalDateTime.now().compareTo(user.getBanTime()) < 0) {
                        responseResult.setMessage("用户被禁用");
                        return new ResponseEntity<>(responseResult, HttpStatus.OK);
                    } else {

                        SystemUser liftUser = systemUserService.getById(user.getId());
                        liftUser.setStatus(SystemUser.STATUS_NORMAL);
                        systemUserService.updateById(liftUser);
                    }
                }
                // 密码进行AES解密
                String key = AesCipherUtil.deCrypto(user.getPassword());
                // 因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
                if (key.equals(loginVo.getPassword())) {
                    // 清除可能存在的Shiro权限信息缓存
                    if (redisService.hasKey(Constant.PREFIX_SHIRO_CACHE + loginVo.getUsername())) {
                        redisService.del(Constant.PREFIX_SHIRO_CACHE + loginVo.getUsername());
                    }
                    // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
                    String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                    redisService.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + loginVo.getUsername(), currentTimeMillis, refreshTokenExpireTime);

                    // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
                    String token = JwtUtil.sign(loginVo.getUsername(), currentTimeMillis);

                    //使用token作为key,保存用户信息,保存时间为token刷新时间
                    // SysUserDTO sysUserDTO=new SysUserDTO();
                    //BeanUtils.copyProperties(user,sysUserDTO);
                    redisService.set(token,user,refreshTokenExpireTime);

                    response.setHeader("Authorization", token);
                    response.setHeader("Access-Control-Expose-Headers", "Authorization");
                    responseResult = ResponseResult.successResponse("sign success");
                    JwtAuthenticationResponse authenticationResponse = new JwtAuthenticationResponse(token, refreshTokenExpireTime,user);
                    responseResult.setData(authenticationResponse);

                } else {
                    throw new CustomUnauthorizedException("帐号或密码错误(Account or Password Error.)");
                }
            }else{
                throw new AuthenticationException("帐号或密码错误(Account or Password Error.)");
            }
        } catch (LockedAccountException lae) {
            responseResult.setMessage("用户已经被锁定不能登录，请与管理员联系！");
        } catch (AuthenticationException e) {
            responseResult.setMessage("用户或密码不正确！");
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(responseResult, HttpStatus.OK);
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        System.out.println("用户退出=========");
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            String token = (String) subject.getPrincipals().getPrimaryPrincipal();
            //// TODO: 2019/8/29  是否将用户信息存到redis中，目前没有
            SystemUser user = (SystemUser) redisService.get(token);
            systemUserService.deleteLoginInfo(user.getName());
            redisService.del(token);
        }
        SecurityUtils.getSubject().logout();
        ResponseResult responseResult = ResponseResult.successResponse();
        return new ResponseEntity<>(responseResult, HttpStatus.OK);
    }

}
