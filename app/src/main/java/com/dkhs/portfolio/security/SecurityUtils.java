package com.dkhs.portfolio.security;

import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.bean.EncryptData;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.security.AES;
import com.dkhs.security.CheckUtils;
import com.dkhs.security.DES;
import com.dkhs.security.EncryUtil;
import com.dkhs.security.RSA;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by zjz on 2015/7/21.
 * 网络请求的加密/解密逻辑实现
 */
public class SecurityUtils {

    public static final String RSA_CLIENT_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK6MQFZwLy5O9vfDwdI5XEnmV1kAZVeprLUW5lqJpCHUAPmFtK6XAsYapX3MjV0v3fpIGAkm7V8DbAo7pKyf0NXH7Pkgoi17dnONdu1sTzCqfpC2L9xwPoxFE478O3Rx4FYIN8iHdgpJTY4QWe8/z5sPu7We7UBTOPvPjjIM9JXBAgMBAAECgYA/fShRyJCAyZwvVvcDHnYhMzWGXAyArGjznZbAS9x1qYWJPUcRUkBiCoMzlNm+kZCuXtbPTWd97eK4fiVR3Pe6kKcABBfduH5kaBnlvv5CQhyDtZQ6ovHI2eRQCcAgPb+XF73dEeVEyQMl4fzJKWCnYyvWNOrnTuL66fDBKIXC2QJBAOWHL/0E9NX5v0GZI2jWnkt8BqVXIcUWOhqsZrWNc2TASHDqiKCqCLxI8MOnXWIb4XpPw/cxO27BZrGtRjAQoSsCQQDCrcYZU8n2/wDneTUzxpFLEdrzvoB4otBgLBI9GT7TAbd4XNsKv3oViO7Gr+pl2uSmSlvK/WsLqXUPqdxSAHbDAkEA0JIM8G2tkjdVLWXcAGtuAofcfih0PmTZPd3Fo1q7LiVwbnMg3mZEc9wQNKqEOB+/v+Z12804BYfnKrOa4RhrawJBAKFxEjK2tWnLFqjebyw8owiqCfTQKiHggwT+BzH72YmKhueduVg0ab2qDLlf8PX+jFcKmwy/EyJcqOMQbdDi6ZkCQQCC5F/M6UJdjZtjCn7coBR1jyPhh8Q12VzP76t+wsyHNyAANUnsdRmh7lIjEyOEY+QNR6VyNf7NUom0Rka1br6f";

    public static final String RSA_SERVER_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWGLXfDb9Sptwip4USaAPIIQhJOP8pZ+pl2N3x5PwgWe+3d4jjWRvYP997YMHeh7BnReo959yhIkcqRWN5YqslhzkvulvgBWk3fV87TXKi9iszOqg7Dc0jBqKeJMxKcGZ0g1GZHMYtj68jRqrs/hfwuUe38UoDuCVFaL97qPWiBwIDAQAB";

    private static final String TAG = "SecurityUtils";

    public static String getRandomAesKey() {

        String a = "";
        Random r = new Random();
        Long b = Math.abs(r.nextLong());
        r = null;
        a = b.toString().substring(0, 16);
        return a;
    }


    /**
     * 对包含签名请求的数据参数进行排序后，进行加密后封装成EncryptData数据类型
     **/
    public static EncryptData requestByEncryp(TreeMap map) throws Exception {


        map.put("timestamp", TimeUtils.getUTCdatetimeAsString());

        String sign = EncryUtil.handleRSA(map, RSA_CLIENT_PRIVATE);

        map.put("signature", sign);
        Gson gson = new GsonBuilder().create();
        String info = gson.toJson(map);

        String merchantAesKey = getRandomAesKey();
        String data = AES.encryptToBase64(info, merchantAesKey);


        String encryptkey = RSA.encrypt(merchantAesKey, RSA_SERVER_PUBLIC);


        EncryptData encryptData = new EncryptData();
        encryptData.setData(data);
        encryptData.setEncryptkey(encryptkey);
        return encryptData;


    }


    /**
     * 解密从服务端返回的数据，并进行验签，返回过滤了签名的数据集
     **/
    public static String encryptResponeJsonData(String jsonData) {
        try {


            EncryptData encryptData = DataParse.parseObjectJson(EncryptData.class, jsonData);

            String aesKey = RSA.decrypt(encryptData.getEncryptkey(), RSA_CLIENT_PRIVATE);

            String rawData = AES.decryptFromBase64(encryptData.getData(), aesKey);


            Type typeMap = new TypeToken<TreeMap<String, String>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            TreeMap<String, String> dataMap = gson.fromJson(rawData, typeMap);


            String sign = CheckUtils.trimToEmpty(dataMap.get("signature"));

            StringBuffer signData = new StringBuffer();
            Iterator<Map.Entry<String, String>> iter = dataMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();

                if (CheckUtils.equals((String) entry.getKey(), "signature")) {
                    continue;
                }
                signData.append(entry.getValue() == null ? "" : entry.getValue());
            }

            boolean isCheckSign = RSA.checkSign(signData.toString(), sign,
                    RSA_SERVER_PUBLIC);


            if (isCheckSign) {
                return dataMap.get("result");

            } else {
                Log.e(TAG, "验签失败！！！！");
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * @param entity 加密的用户实体
     * @return
     */
    public static UserEntity encrypt(UserEntity entity) {

        if (null == entity || TextUtils.isEmpty(entity.getAccess_token())) {
            return entity;
        }

        String authcode = new DES().encrypt(entity.getAccess_token(), ConstantValue.DES_PASSWORD);
        entity.setAccess_token(authcode);
        return entity;
    }

    /**
     * @param entity 解密的用户实体
     * @return
     */
    public static UserEntity decrypt(UserEntity entity) {
        if (null == entity || TextUtils.isEmpty(entity.getAccess_token())) {
            return entity;
        }

        String authcode = new DES().decrypt(entity.getAccess_token(), ConstantValue.DES_PASSWORD);
        entity.setAccess_token(authcode);
        return entity;
    }


}