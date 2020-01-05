package com.kochun.wxmp.back.controller;

import com.kochun.wxmp.back.il8n.LocaleMessage;
import com.kochun.wxmp.back.shiro.PasswordHelper;
import com.kochun.wxmp.common.utils.UUIDUtil;
import com.kochun.wxmp.core.entity.system.EmailTemplate;
import com.kochun.wxmp.core.entity.system.SysUser;
import com.kochun.wxmp.core.entity.system.SystemConfig;
import com.kochun.wxmp.core.service.EmailService;
import com.kochun.wxmp.core.service.EmailTemplateService;
import com.kochun.wxmp.core.service.SysUserService;
import com.kochun.wxmp.core.service.SystemConfigService;
import com.kochun.wxmp.core.service.common.RedisService;
import com.kochun.wxmp.core.vo.internal.response.ResponseResult;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kochun
 * @since 2019-08-18
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Reference(version = "1.0.0")
    private SysUserService sysUserService;

    @Reference(version = "1.0.0")
    private EmailTemplateService emailTemplateService;

    @Reference(version = "1.0.0")
    private SystemConfigService systemConfigService;

    @Reference(version = "1.0.0")
    private EmailService emailService;

    @Reference(version = "1.0.0")
    private RedisService redisService;


    @Resource
    private LocaleMessage localeMessage;
    /**
     * 用户注册
     * @param entity
     * @return
     */
    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Validated @RequestBody SysUser entity){
        ResponseResult responseResult;
        SysUser userByName = sysUserService.getUserByUserName(entity.getName());
        SysUser userByEmail = sysUserService.findUserByEmail(entity.getEmail());
        SysUser userByPhone = sysUserService.findUserByPhone(entity.getPhone());
        if (null == userByName) {
            if (null == userByEmail) {
                if (null == userByPhone) {
                    //密码MD5加密
                    entity.setPassword(PasswordHelper.encryptPassword(null, entity.getPassword()));
                    String validateCode = PasswordHelper.encryptPassword(null, UUIDUtil.getUUID());
                    int result = sysUserService.register(entity);
                    if (result > 0) {
                        EmailTemplate emailTemplate = emailTemplateService.getByOperation("USER_ACTIVE");
                        SystemConfig host = systemConfigService.getByKey("USER_ACTIVE_HOST");
                        String url = host.getVarValue() + "?validateCode=" + validateCode;
                        String href = emailTemplate.getContent().replace("_url_", url);
                        emailService.sendEmail(entity.getEmail(), href, emailTemplate.getSubject());
                        responseResult = ResponseResult.successResponse(localeMessage.getMessage("REGISTER_SUCCESS"));
                    } else {
                        responseResult = ResponseResult.failResponse(localeMessage.getMessage("FAILED"));
                    }
                } else {
                    responseResult = ResponseResult.failResponse(localeMessage.getMessage("PHONE_EXIST"));
                }
            } else {
                responseResult = ResponseResult.failResponse(localeMessage.getMessage("EMAIL_EXIST"));
            }
        } else {
            responseResult = ResponseResult.failResponse(localeMessage.getMessage("NAME_EXIST"));
        }
        return new ResponseEntity<>(responseResult, HttpStatus.OK);
    }


    /**
     * 用户激活
     * @param validateCode
     * @return
     */
    @PatchMapping(value = "/activated")
    public ResponseEntity<?> activated(String validateCode){
        ResponseResult responseResult;
        int result = sysUserService.activated(validateCode);
        if (result > 0) {
            responseResult = ResponseResult.successResponse(localeMessage.getMessage("SUCCESS"));
        } else {
            responseResult = ResponseResult.failResponse(localeMessage.getMessage("FAILED"));
        }
        return new ResponseEntity<>(responseResult, HttpStatus.OK);
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> activated(){
        ResponseResult responseResult;

        responseResult = ResponseResult.successResponse(localeMessage.getMessage("SUCCESS"));

        return new ResponseEntity<>(responseResult, HttpStatus.OK);
    }


    @GetMapping("/getUserByToken")
    public ResponseEntity<?> getUserByToken() {
        Subject subject = SecurityUtils.getSubject();
        ResponseResult responseResult = ResponseResult.successResponse();
        if (subject.getPrincipals() != null) {
            String token = (String) subject.getPrincipals().getPrimaryPrincipal();
            //// TODO: 2019/8/29  是否将用户信息存到redis中，目前没有
            SysUser user = (SysUser) redisService.get(token);
            if (user!=null){
                responseResult.setData(user);
            }else {
                responseResult = ResponseResult.failResponse();
                responseResult.setMessage("获取用户信息为null");
            }
        }else {
            responseResult = ResponseResult.failResponse();
            responseResult.setMessage("没有token信息");
        }
        return new ResponseEntity<>(responseResult, HttpStatus.OK);
    }



}
