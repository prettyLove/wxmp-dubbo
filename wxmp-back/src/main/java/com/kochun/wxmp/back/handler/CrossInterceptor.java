package com.kochun.wxmp.back.handler;/**
 * @ClassNameCrossInterceptor
 * @Description //TODO
 * @Author kochun
 * @Date 2020-01-13 21:10
 * @Version V1.0
 **/

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: kochun
 * @Date: 2020-01-13 21:10
 * @version: 1.0
 */
@Slf4j
@Configuration
public class CrossInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

//        private static List<String> allowHosts;
//
//        //配置允许跨域的域名
//        public void setAllow(String allow){
//            if(allow != null){
//                allowHosts = Arrays.asList(allow.split(","));
//            }
//        }

        String origin  = request.getHeader(HttpHeaders.ORIGIN);
        if (origin != null) {
            //if(allowHosts.contains(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, HEAD");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Access-Token");
                response.setHeader("Access-Control-Max-Age", "3600");
//            } else {
//                log.warn("跨域失败：" + origin);
//            }
        }
        return true;
    }
}
