package com.kochun.wxmp.back.shiro.filter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.kochun.wxmp.back.shiro.JwtUtil;
import com.kochun.wxmp.back.shiro.jwt.JwtToken;
import com.kochun.wxmp.common.Constant;
import com.kochun.wxmp.common.exception.CustomException;
import com.kochun.wxmp.common.utils.JsonConvertUtil;
import com.kochun.wxmp.core.entity.system.SystemUser;
import com.kochun.wxmp.core.service.SystemUserService;
import com.kochun.wxmp.core.service.common.RedisService;
import com.kochun.wxmp.core.vo.internal.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT过滤
 * @author dolyw.com
 * @date 2018/8/30 15:47
 */
public class JwtAuthFilter extends BasicHttpAuthenticationFilter {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Resource
    RedisService redisService;

    @Value("${system.config.refreshTokenExpireTime}")
    private int refreshTokenExpireTime;

    @Resource
    private SystemUserService systemUserService;

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 查看当前Header中是否携带Authorization属性(Token)，有的话就进行登录认证授权
        if (this.isLoginAttempt(request, response)) {
            try {
                System.out.println("JwtFilter 进入========");
                String urlPath=WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
                System.out.println("当前请求路径：  "+urlPath);
                // 进行Shiro的登录UserRealm
                this.executeLogin(request, response);
            } catch (Exception e) {
                // 认证出现异常，传递错误信息msg
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable instanceof TokenExpiredException) {
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "Token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                // Token认证失败直接返回Response信息
                this.response403(response, msg);

                return false;
            }
        } else {
            // 没有携带Token
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            // 获取当前请求类型
            String httpMethod = httpServletRequest.getMethod();
            // 获取当前请求URI
            String requestURI = httpServletRequest.getRequestURI();
            logger.info("当前请求 {} Authorization属性(Token)为空 请求类型 {}", requestURI, httpMethod);
            // mustLoginFlag = true 开启任何请求必须登录才可访问
            Boolean mustLoginFlag = false;
            if (mustLoginFlag) {
                this.response403(response, "请先登录");
                return false;
            }
        }
        return true;
    }

    /**
     * 这个方法当token失效后悔进入，
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法，只是将executeLogin方法调用去除了
     * 如果没有去除将会循环调用doGetAuthenticationInfo方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        System.out.println("token过期时进入。。。。。。。。。。。。");

        //response401(response,"token已过期");
        // this.sendChallenge(request, response);
        return false;
    }

    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        return StringUtils.isNotEmpty(token);
    }

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        JwtToken token = new JwtToken(this.getAuthzHeader(request));
        // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获
        this.getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE,PATCH");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        // 获取当前Token的帐号信息
        String account = JwtUtil.getClaim(token, Constant.ACCOUNT);
        // 判断Redis中RefreshToken是否存在
        if (redisService.hasKey(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisService.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                logger.info("刷新当前请求token===================");
                // 获取当前最新时间戳
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                // 读取配置文件，获取refreshTokenExpireTime属性
               // String refreshTokenExpireTime = PropertiesUtil.getProperty("refreshTokenExpireTime");
                // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
                redisService.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account, currentTimeMillis, Integer.parseInt(JwtUtil.refreshTokenExpireTime));
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                token = JwtUtil.sign(account, currentTimeMillis);
                // 将新刷新的AccessToken再次进行Shiro的登录
                JwtToken jwtToken = new JwtToken(token);
                // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
                this.getSubject(request, response).login(jwtToken);
                // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.setHeader("Authorization", token);
                httpServletResponse.setHeader("refreshToken","true");
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");

                SystemUser user=systemUserService.getUserByUserName(account);
                redisService.set(token,user,refreshTokenExpireTime);

                logger.info("刷新当前请求token===================  :"+token);
                return true;
            }
        }
        return false;
    }

    /**
     * 无需转发，直接返回Response信息
     */
    private void response401(ServletResponse response, String msg) {

        System.out.println("response401。。。。。。。。。。。。");
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);

        httpServletResponse.setHeader("Access-control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","*");

        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            String data = JsonConvertUtil.objectToJson(ResponseResult.failResponse(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):" + msg, null));
            out.append(data);
        } catch (IOException e) {
            logger.error("直接返回Response信息出现IOException异常:" + e.getMessage());
            throw new CustomException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }


    /**
     * 403	Forbidden	服务器理解请求客户端的请求，但是拒绝执行此请求
     * 无需转发，直接返回Response信息
     */
    private void response403(ServletResponse response, String msg) {

        System.out.println("response403。。。。。。。。。。。。");
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);

        httpServletResponse.setHeader("Access-control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Headers","*");

        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            String data = JsonConvertUtil.objectToJson(ResponseResult.failResponse(HttpStatus.FORBIDDEN.value(), "无权访问" + msg, null));
            out.append(data);
        } catch (IOException e) {
            logger.error("直接返回Response信息出现IOException异常:" + e.getMessage());
            throw new CustomException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }
}
