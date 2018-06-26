package com.zwx.bike.security;


import org.springframework.stereotype.Component;

import javax.crypto.Cipher;


import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Create By Zhang on 2018/2/12
 */
public class RSAUtil {

    /**
     * 私钥字符串
     */
    private static String PRIVATE_KEY ="";

    /**
     * 公钥字符串
     */
    private static String PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHVIgyK7ip5iHhCVWF0I5i eESPvJCk96pIDvTKC+9LzybQqYvanDo96XxpI8B1aDHxhKF8rtRnRxJFBhl0 k58682XdOVTRtY8lYJrHQOIrWHqIahFoKdbfH3yH7cOw3Ny6XaRgL3atHS7U 8cGEtzf9wSG8gpi3w5IleOKwcRTjvwIDAQAB";

    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 读取密钥字符串
     * @throws Exception
     */

    public static void convert() throws Exception {
        byte[] data = null;

        try {
            InputStream is = RSAUtil.class.getResourceAsStream("/enc_pri");
            int length = is.available();
            data = new byte[length];
            is.read(data);
        } catch (Exception e) {
        }

        String dataStr = new String(data);
        try {
            PRIVATE_KEY = dataStr;
        } catch (Exception e) {
        }

        if (PRIVATE_KEY == null) {
            throw new Exception("Fail to retrieve key");
        }
    }

    /**
     * 私钥解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data) throws Exception {
        convert();
        byte[] keyBytes = Base64Util.decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);


        return cipher.doFinal(data);
    }

    /**
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        byte[] keyBytes = Base64Util.decode(key);
        X509EncodedKeySpec pkcs8KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(pkcs8KeySpec);

//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }


    public static void main(String[] args) throws Exception {
//        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(KEY_ALGORITHM);
//        keyPairGenerator.initialize(1024);
//        KeyPair keyPair=keyPairGenerator.generateKeyPair();
//        PublicKey publicKey=keyPair.getPublic();
//        PrivateKey privateKey=keyPair.getPrivate();
//        System.out.println(Base64Util.encode(publicKey.getEncoded()));
//        System.out.println(Base64Util.encode(privateKey.getEncoded()));

        String data ="老张";
        byte[] enResult=encryptByPublicKey(data.getBytes("UTF-8"),PUBLIC_KEY);
        System.out.println(enResult.toString());
        byte[] result = decryptByPrivateKey(enResult);
        System.out.println(new String(result,"UTF-8"));



        }

}
