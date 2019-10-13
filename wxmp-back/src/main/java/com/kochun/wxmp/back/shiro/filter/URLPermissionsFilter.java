package com.kochun.wxmp.back.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.kochun.wxmp.core.service.common.RedisService;
import com.kochun.wxmp.core.vo.internal.response.ResponseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
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
 * 通过字符串验证权限
 * @author chenyd
 *
 */

public class URLPermissionsFilter extends PermissionsAuthorizationFilter {

    @Resource
    private RedisService redisService;

    /**
     * mappedValue 访问该url时需要的权限
     * subject.isPermitted 判断访问的用户是否拥有mappedValue权限
     * 重写拦截器，只要符合配置的一个权限，即可通过
     */
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        System.out.println("进入Url Filter"+ URLPermissionsFilter.class.toString());
        System.out.println("mappedValue  "+ mappedValue);
        String[] perms = (String[]) mappedValue;
        System.out.println(JSONObject.toJSONString(perms));
        //Subject subject = getSubject(request, response);
        String urlPath=WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
        Subject subject = SecurityUtils.getSubject();
        // DefaultFilterChainManager
        // PathMatchingFilterChainResolver
        //String[] perms = (String[]) mappedValue;
        subject.isPermitted(urlPath);
        // subject.checkPermission(urlPath);
        //String[] perms= {urlPath};
        System.out.println(urlPath);
        boolean isPermitted = false;
        if (perms != null && perms.length > 0) {
            for (String str : perms) {
                if (subject.isPermitted(str)) {
                    isPermitted = true;
                }
            }
        }
        return isPermitted;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)   {
        // 返回无权限
        ResponseResult responseResult = new ResponseResult();
        responseResult.setMessage("无权限");
//        ResponseEntity<ResponseResult> responseEntity = new ResponseEntity<>(responseResult, HttpStatus.UNAUTHORIZED);
        PrintWriter out = null;
        HttpServletResponse res = (HttpServletResponse) response;
        // 203
        res.setStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            out = response.getWriter();
            out.println(JSONObject.toJSONString(responseResult));
        } catch (Exception e) {
        } finally {
            if (null != out) {
                out.flush();
                out.close();
            }
        }
        return false;
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //Subject subject = getSubject(request, response);

        //String urlPath=WebUtils.getPathWithinApplication(WebUtils.toHttp(request));

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.onPreHandle(request, response,mappedValue);
    }
}
