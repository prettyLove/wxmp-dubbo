package com.kochun.wxmp.back.handler;/**
 * @ClassNameOptionsInterceptor
 * @Description //TODO
 * @Author kochun
 * @Date 2020-01-13 21:13
 * @Version V1.0
 **/

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: kochun
 * @Date: 2020-01-13 21:13
 * @version: 1.0
 */

public class OptionsInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if("OPTIONS".equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        return true;
    }
}