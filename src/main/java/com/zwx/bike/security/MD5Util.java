package com.zwx.bike.security;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Create By Zhang on 2018/3/13
 */
public class MD5Util {
    public static String getMD5(String source){

        return DigestUtils.md5Hex(source);
    }

}
