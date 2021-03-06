package com.zwx.bike.common.utils;

import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.zwx.bike.common.constants.Constants;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.user.entity.UserElement;

/**
 * Create By Zhang on 2018/3/27
 */
public class BaiduPushUtil {
    public static void pushMsgToSingleDevice(UserElement ue, String message) throws BikeException {

        PushKeyPair pair = new PushKeyPair(Constants.BAIDU_YUN_PUSH_API_KEY, Constants.BAIDU_YUN_PUSH_SECRET_KEY);
        BaiduPushClient pushClient = new BaiduPushClient(pair, Constants.CHANNEL_REST_URL);
        try {
            //  设置请求参数，创建请求实例
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
                    addChannelId(ue.getPushChannelId()).
                    addMsgExpires(new Integer(3600)).   //设置消息的有效时间,单位秒,默认3600*5.
                    addMessageType(1).              //设置消息类型,0表示透传消息,1表示通知,默认为0.
                    addMessage(message);
            //设置设备类型，deviceType => 1 for web, 2 for pc,
            // 3for android, 4 for ios, 5 for wp.
            if ("android".equals(ue.getPlatform())) {
                request.addDeviceType(3);
            } else if ("ios".equals(ue.getPlatform())) {
                request.addDeviceType(4);
            }
            //  执行Http请求
            PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
        } catch (PushClientException e) {
            e.printStackTrace();
            throw new BikeException(e.getMessage());
        } catch (PushServerException e) {
            e.printStackTrace();
            throw new BikeException(e.getErrorMsg());
        }

    }

}
