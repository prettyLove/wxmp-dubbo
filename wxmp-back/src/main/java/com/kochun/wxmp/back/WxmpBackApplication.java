package com.kochun.wxmp.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/***
 * exclude= {DataSourceAutoConfiguration.class} 用于解决Mybatis 启动报错 Failed to auto-configure a DataSource
 *配置dubbo.xml方式配置dubbo，主要解决shiro初始化时RPC对象未初始化的问题，采用xml格式初始化配置可以解决
 * @author kochun
 * @date 2019/9/29 15:39
 **/
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@ImportResource({"classpath:dubbo.xml"})
public class WxmpBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxmpBackApplication.class, args);
    }

}
