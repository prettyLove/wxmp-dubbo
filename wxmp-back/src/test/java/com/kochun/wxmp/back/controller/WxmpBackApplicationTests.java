package com.kochun.wxmp.back.controller;

import com.kochun.wxmp.back.shiro.AesCipherUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxmpBackApplicationTests {

    @Resource
    AesCipherUtil aesCipherUtil;
    @Test
    public void contextLoads() {

        String password=aesCipherUtil.enCrypto("admin");
        System.out.println(password);
    }

}
