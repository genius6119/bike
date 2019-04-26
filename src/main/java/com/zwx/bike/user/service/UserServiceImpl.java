package com.zwx.bike.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zwx.bike.cache.CommonCacheUtil;
import com.zwx.bike.common.constants.Constants;
import com.zwx.bike.common.exception.BikeException;
import com.zwx.bike.common.utils.QiniuFileUploadUtil;
import com.zwx.bike.common.utils.RandomNumberCode;
import com.zwx.bike.jms.SmsProcessor;
import com.zwx.bike.security.AESUtil;
import com.zwx.bike.security.Base64Util;
import com.zwx.bike.security.MD5Util;
import com.zwx.bike.security.RSAUtil;

import com.zwx.bike.user.dao.UserMapper;
import com.zwx.bike.user.entity.User;
import com.zwx.bike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.Destination;
import java.lang.invoke.ConstantCallSite;
import java.util.HashMap;
import java.util.Map;

/**
 * Create By Zhang on 2018/2/9
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String SMS_QUEUE="sms.queue";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonCacheUtil cacheUtil;

    private static final String VERIFCODE_PREFIX="verify.code.";

    @Autowired
    private SmsProcessor smsProcessor;

    @Override
    public String login(String data, String key) throws BikeException {
        String token=null;
        String decryptData=null;
        try {
            byte[] aesKey=RSAUtil.decryptByPrivateKey(Base64Util.decode(key));
            decryptData= AESUtil.decrypt(data,new String(aesKey,"UTF-8"));
            if (decryptData==null){
                throw new Exception();
            }

            JSONObject jsonObject=JSON.parseObject(decryptData);
            String mobile=jsonObject.getString("mobile");
            String code=jsonObject.getString("code");
            String platform=jsonObject.getString("platform");
            String channelId = jsonObject.getString("channelId");//推送频道编码 单个设备唯一
            if(StringUtils.isBlank(mobile)||StringUtils.isBlank(code)||StringUtils.isBlank(channelId)){
                throw new Exception();
            }
            /**
             *去redis取验证码 比较手机号码和验证码是否匹配
             */
            String verCode=cacheUtil.getCacheValue(mobile);
            User user;
            if (code.equals(verCode)){
                /**手机匹配*/
                user =userMapper.selectByMobile(mobile);
                if (user==null){
                    user=new User();
                    user.setMobile(mobile);
                    user.setNickname(mobile);
                    userMapper.insertSelective(user);

                }
            }else {
                throw new BikeException("手机号与验证码不匹配");
            }
            /**生成token*/
            try {
                token =generateToken(user);
            } catch (Exception e) {
                throw new BikeException("生成token失败");
            }
            UserElement ue=new UserElement();
            ue.setMobile(mobile);
            ue.setUserId(user.getId());
            ue.setToken(token);
            ue.setPlatform(platform);
            ue.setPushChannelId(channelId);
            cacheUtil.putTokenWhenLogin(ue);
            /**
             *判断用户是否在数据库存在 如果存在，存redis 如果不存在，帮他注册
             */
        }catch (Exception e){
            log.error("fail to decrypt data",e);
            throw new BikeException("数据解析错误");
        }

        return token;
    }

    @Override
    public void modifyNickName(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void sendVercode(String mobile, String ip) throws BikeException {
        String verCode= RandomNumberCode.verCode();
        int result=cacheUtil.cacheForVerificationCode(VERIFCODE_PREFIX+mobile,verCode,"reg",60,ip);
        /**校验*/
        if (result == 1) {
            log.info("当前验证码未过期，请稍后重试");
            throw new BikeException("当前验证码未过期，请稍后重试");
        } else if (result == 2) {
            log.info("超过当日验证码次数上线");
            throw new BikeException("超过当日验证码次数上限");
        } else if (result == 3) {
            log.info("超过当日验证码次数上限 {}", ip);
            throw new BikeException(ip + "超过当日验证码次数上限");
        }
        log.info("Sending verify code {} for phone {}",verCode,mobile);
        /**校验通过了，可以发短信验证码,
         * 发消息到队列
         */

        Destination destination=new ActiveMQQueue(SMS_QUEUE);
        Map<String,String> smsParam=new HashMap<>();
        smsParam.put("mobile",mobile);
        smsParam.put("tplId", Constants.MDSMS_VERCODE_TPLID);
        smsParam.put("vercode",verCode);
        String message=JSON.toJSONString(smsParam);
        smsProcessor.sendSmsToQueue(destination,message);
    }


    @Override
    public String uploadHeadImg(MultipartFile file, Long id) throws BikeException {
        try {
            //获取user 得到原来的头像地址
            User user = userMapper.selectByPrimaryKey(id);
            // 调用七牛
            String imgUrlName = QiniuFileUploadUtil.uploadHeadImg(file);
            user.setHeadImg(imgUrlName);
            //更新用户头像URL
            userMapper.updateByPrimaryKeySelective(user);
            return Constants.QINIU_HEAD_IMG_BUCKET_URL+"/"+Constants.QINIU_HEAD_IMG_BUCKET_NAME+"/"+imgUrlName;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new BikeException("头像上传失败");
        }
    }


    /**
     * 生成token方法 md5加密
     * */
    private String generateToken(User user) {
        String source=user.getId()+":"+user.getMobile()+":"+System.currentTimeMillis();
        return MD5Util.getMD5(source);
    }
}
