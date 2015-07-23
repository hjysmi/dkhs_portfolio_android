package com.dkhs.portfolio.security;


import com.dkhs.portfolio.bean.EncryptData;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.SecurityParseHttpLister;
import com.dkhs.portfolio.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by zjz on 2015/7/21.
 */
public class SecurityUtils {

    // 客户端户（RSA）私钥
    public static final String RSA_CLIENT_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK6MQFZwLy5O9vfDwdI5XEnmV1kAZVeprLUW5lqJpCHUAPmFtK6XAsYapX3MjV0v3fpIGAkm7V8DbAo7pKyf0NXH7Pkgoi17dnONdu1sTzCqfpC2L9xwPoxFE478O3Rx4FYIN8iHdgpJTY4QWe8/z5sPu7We7UBTOPvPjjIM9JXBAgMBAAECgYA/fShRyJCAyZwvVvcDHnYhMzWGXAyArGjznZbAS9x1qYWJPUcRUkBiCoMzlNm+kZCuXtbPTWd97eK4fiVR3Pe6kKcABBfduH5kaBnlvv5CQhyDtZQ6ovHI2eRQCcAgPb+XF73dEeVEyQMl4fzJKWCnYyvWNOrnTuL66fDBKIXC2QJBAOWHL/0E9NX5v0GZI2jWnkt8BqVXIcUWOhqsZrWNc2TASHDqiKCqCLxI8MOnXWIb4XpPw/cxO27BZrGtRjAQoSsCQQDCrcYZU8n2/wDneTUzxpFLEdrzvoB4otBgLBI9GT7TAbd4XNsKv3oViO7Gr+pl2uSmSlvK/WsLqXUPqdxSAHbDAkEA0JIM8G2tkjdVLWXcAGtuAofcfih0PmTZPd3Fo1q7LiVwbnMg3mZEc9wQNKqEOB+/v+Z12804BYfnKrOa4RhrawJBAKFxEjK2tWnLFqjebyw8owiqCfTQKiHggwT+BzH72YmKhueduVg0ab2qDLlf8PX+jFcKmwy/EyJcqOMQbdDi6ZkCQQCC5F/M6UJdjZtjCn7coBR1jyPhh8Q12VzP76t+wsyHNyAANUnsdRmh7lIjEyOEY+QNR6VyNf7NUom0Rka1br6f";
    // 服务端（RSA）公钥
    public static final String RSA_SERVER_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWGLXfDb9Sptwip4USaAPIIQhJOP8pZ+pl2N3x5PwgWe+3d4jjWRvYP997YMHeh7BnReo959yhIkcqRWN5YqslhzkvulvgBWk3fV87TXKi9iszOqg7Dc0jBqKeJMxKcGZ0g1GZHMYtj68jRqrs/hfwuUe38UoDuCVFaL97qPWiBwIDAQAB";
    // 商户自己随机生成的AESkey
//    private String merchantAesKey = getRandom(16);
//    private String merchantAesKey = "6585339788518466";


    public static String getRandomAesKey() {

        String a = "";
        Random r = new Random();
        Long b = Math.abs(r.nextLong());
        r = null;
        a = b.toString().substring(0, 16);
        return a;
    }


    public void testCreditCardPay() throws Exception {

        int amount = 5;
        String idcard = "hello world!";
        String owner = "张三";


        TreeMap map = new TreeMap();
        map.put("amount", amount);
        map.put("idcard", idcard);
        map.put("owner", owner);

        String requestUrl = "/api/v1/encrypt/";
        String requestPostUrl = "/api/v1/encrypt/encrypt_post/";

        DKHSClient.requestGetByEncryp(map, requestUrl, httpListener);
    }


    ParseHttpListener httpListener = new SecurityParseHttpLister() {

        @Override
        protected Object parseDateTask(String jsonData) {
            System.out.println("encry HttpListener：" + jsonData);
            return null;
        }

        @Override
        protected void afterParseData(Object object) {

        }
    };


    public static EncryptData requestByEncryp(TreeMap map) throws Exception {


        map.put("timestamp", TimeUtils.getUTCdatetimeAsString());

        // 通过私钥生成RSA签名
        String sign = EncryUtil.handleRSA(map, RSA_CLIENT_PRIVATE);
        System.out.println("RSA签名：" + sign);

        //将签名参数传递给服务器
        map.put("signature", sign);
        // 生成data
        Gson gson = new GsonBuilder().create();
        String info = gson.toJson(map);
        System.out.println("业务数据明文：" + info);

        String merchantAesKey = getRandomAesKey();
        //通过随机生成的16位AESkey 对数据进行AES加密
        String data = AES.encryptToBase64(info, merchantAesKey);

        System.out.println("merchantAesKey：" + merchantAesKey);
        System.out.println("含有签名的业务数据密文data:" + data);

        // 使用RSA算法将商户自己随机生成的AESkey加密（使用公钥加密）
        String encryptkey = RSA.encrypt(merchantAesKey, RSA_SERVER_PUBLIC);
        System.out.println("公钥加密后的 merchantAesKey:" + encryptkey);


        //将data和encryptkey传给服务器
        System.out.println("完成后data：" + data);
        System.out.println("完成后encryptkey：" + encryptkey);

        EncryptData encryptData = new EncryptData();
        encryptData.setData(data);
        encryptData.setEncryptkey(encryptkey);
        return encryptData;


    }


    public static String encryptResponeJsonData(String jsonData) {
        try {


            EncryptData encryptData = DataParse.parseObjectJson(EncryptData.class, jsonData);

            System.out.println("解密前的aesKey：" + encryptData.getEncryptkey());
            String aesKey = com.dkhs.portfolio.security.RSA.decrypt(encryptData.getEncryptkey(), RSA_CLIENT_PRIVATE);
            System.out.println("解密后aesKey：" + aesKey);

            String rawData = com.dkhs.portfolio.security.AES.decryptFromBase64(encryptData.getData(), aesKey);
            System.out.println("解密后data：" + rawData);


            Type typeMap = new TypeToken<TreeMap<String, String>>() {
            }.getType();
            Gson gson = new GsonBuilder().create();
            TreeMap<String, String> dataMap = gson.fromJson(rawData, typeMap);


            /** 3.取得data明文sign。 */
            String sign = CheckUtils.trimToEmpty(dataMap.get("signature"));

            /** 4.对map中的值进行验证 */
            StringBuffer signData = new StringBuffer();
            Iterator<Map.Entry<String, String>> iter = dataMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();

                /** 把sign参数隔过去 */
                if (CheckUtils.equals((String) entry.getKey(), "signature")) {
                    continue;
                }
                signData.append(entry.getValue() == null ? "" : entry.getValue());
            }

            /** 5. result为true时表明验签通过 */
            boolean isCheckSign = RSA.checkSign(signData.toString(), sign,
                    RSA_SERVER_PUBLIC);


            if (isCheckSign) {
                System.out.println("========验签通过===========");
                System.out.println("respone result:" + dataMap.get("result"));
                return dataMap.get("result");

            } else {
                System.out.println("验签失败！！！！");
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
