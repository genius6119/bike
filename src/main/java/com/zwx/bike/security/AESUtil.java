package com.zwx.bike.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * Create By Zhang on 2018/2/12
 */
public class AESUtil {
    public static final String KEY_ALGORITHM = "AES";
    public static final String KEY_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";


    /**
     * AES对称加密 都是百度复制回来的
     * @param data
     * @param key key需要16位
     * @return
     */
    public static String encrypt(String data,String key){
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"),KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.ENCRYPT_MODE , spec,new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] bs = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64Util.encode(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * AES对称解密 key需要16位
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_MODE);
            cipher.init(Cipher.DECRYPT_MODE , spec , new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] originBytes = Base64Util.decode(data);
            byte[] result = cipher.doFinal(originBytes);
            return new String(result,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
//
//    public static void main(String[] args) throws Exception {
//        /**
//         *@Author Zhang
//         *@Date 2018/2/12 18:46
//         *@Description AES加密数据
//         */
//        String key="123456789abcdefg";
//        String dataToEn="{'mobile':'17805056119','code':'6666','platform':'android','channelId':'123456'}";
//        String enResult=encrypt(dataToEn,key);
//        System.out.println(enResult);
//
//        /**
//         *@Author Zhang
//         *@Date 2018/2/12 18:47
//         *@Description RSA 加密AES的密钥
//         */
//        byte[] enKey=RSAUtil.encryptByPublicKey(key.getBytes("UTF-8"),"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHVIgyK7ip5iHhCVWF0I5ieESPvJCk96pIDvTKC+9LzybQqYvanDo96XxpI8B1aDHxhKF8rtRnRxJFBhl0 k58682XdOVTRtY8lYJrHQOIrWHqIahFoKdbfH3yH7cOw3Ny6XaRgL3atHS7U8cGEtzf9wSG8gpi3w5IleOKwcRTjvwIDAQAB");
////        System.out.println(new String(enKey,"UTF-8"));
//        String baseKey=Base64Util.encode(enKey);
//        System.out.println(baseKey);
//        /**
//         *@Author Zhang
//         *@Date 2018/2/12 18:54
//         *@Description 服务端RSA解密AES的key得到lastKey,再用这个lastKey解密
//         */
////        byte[] de=Base64Util.decode(baseKey);
////        byte[] deKeyResult=RSAUtil.decryptByPrivateKey(de);
////        String lastKey=new String(deKeyResult,"UTF-8");
////        System.out.println(lastKey);
////        String deResult=decrypt(enResult,lastKey);
////        System.out.println(deResult);
//
//
//    }

}
