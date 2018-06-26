package com.zwx.bike.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * Create By Zhang on 2018/3/19
 */
public class RandomNumberCode {
    public static String verCode(){
        Random random=new Random();
        /**取随机生成的数的第2位到第6位*/
        return StringUtils.substring(String.valueOf(random.nextInt()),2,6);
    }

    /**随机生成订单号*/
    public static String randomNo(){
        Random random=new Random();
        return String.valueOf(Math.abs(random.nextInt()*-10));
    }

}
