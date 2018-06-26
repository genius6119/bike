package com.zwx.bike.user.entity;

import lombok.Data;

/**
 * Create By Zhang on 2018/3/4
 */
@Data
public class LoginInfo {
    /**
     * @Author Zhang
     * @Date 2018/3/4 13:26
     * @Description 登录信息密文
     */
    private String data;
    /**
    RSA加密的ASE密钥
     */
    private String key;
}
