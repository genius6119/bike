package com.zwx.bike.jms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zwx.bike.sms.SmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * Create By Zhang on 2018/3/20
 * activemq发送短信
 */
@Component("SmsProcesor")
public class SmsProcessor {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Autowired
    @Qualifier("verCodeService")
    private SmsSender smsSender;

    public void sendSmsToQueue(Destination destination,final String message){
        jmsTemplate.convertAndSend(destination,message);
    }

    @JmsListener(destination = "sms.queue")     /**监听队列*/
    public void doSendMessage(String text){
        JSONObject jsonObject=JSON.parseObject(text);
        smsSender.sendSMS(jsonObject.getString("mobile"),jsonObject.getString("tplId"),jsonObject.getString("vercode"));
    }

}
