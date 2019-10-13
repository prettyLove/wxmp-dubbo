package com.kochun.wxmp.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Base64工具
 * @author Wang926454
 * @date 2018/8/21 15:14
 */
public class Base64ConvertUtil {

    /**
     * 加密JDK1.8
     * @param str
     * @return java.lang.String
     * @author Wang926454
     * @date 2018/8/21 15:28
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.encodeBase64(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    /**
     * 解密JDK1.8
     * @param str
     * @return java.lang.String
     * @author Wang926454
     * @date 2018/8/21 15:28
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.decodeBase64(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }

}
