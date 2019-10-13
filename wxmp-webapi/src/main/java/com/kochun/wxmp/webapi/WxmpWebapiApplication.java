package com.kochun.wxmp.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class WxmpWebapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxmpWebapiApplication.class, args);
    }

}
