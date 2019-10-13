package com.kochun.wxmp.common.utils;

import java.util.UUID;

public class UUIDUtil {


    /**
     * 获取处理过的UUID
     *
     * @return
     */
    public static String getUUID() {
        String uuidStr = UUID.randomUUID().toString();
        String uuid = uuidStr.replaceAll("-", "").toUpperCase();
        return uuid;
    }

    public static String getUUIDWithHyphen() {
        return UUID.randomUUID().toString();
    }
}
