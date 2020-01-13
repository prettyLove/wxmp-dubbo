package com.kochun.wxmp.back.config;

import com.kochun.wxmp.back.handler.CrossInterceptor;
import com.kochun.wxmp.back.handler.LocaleInterceptor;
import com.kochun.wxmp.back.handler.OptionsInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @Author kochun
 * @Description //TODO
 * @Date 2019/8/18 22:42
 **/
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


    @Resource
    private LocaleInterceptor localeInterceptor;

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //1允许任何域名使用
        corsConfiguration.addAllowedOrigin("*");
        // 2允许任何头
        corsConfiguration.addAllowedHeader("*");
        // 3允许任何方法（post、get等）
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 4
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(new CrossInterceptor()).addPathPatterns("/**");
        log.debug("跨域拦截器注册成功！");


        registry.addInterceptor(new OptionsInterceptor()).addPathPatterns("/**");
        log.debug("Options请求拦截器注册成功！");
    }


}