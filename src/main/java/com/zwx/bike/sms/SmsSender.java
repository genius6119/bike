package com.zwx.bike.sms;

/**
 * Create By Zhang on 2018/3/20
 */
public interface SmsSender {
    void sendSMS(String phone,String tplId,String params);
}
