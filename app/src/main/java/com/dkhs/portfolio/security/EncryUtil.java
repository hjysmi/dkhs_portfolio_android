package com.dkhs.portfolio.security;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * Created by zjz on 2015/7/21.
 */
public class EncryUtil {

    /**
     * 对易宝支付返回的结果进行验签
     *
     * @param data               易宝支付返回的业务数据密文
     * @param encrypt_key        易宝支付返回的对ybAesKey加密后的密文
     * @param yibaoPublickKey    易宝支付提供的公钥
     * @param merchantPrivateKey 商户自己的私钥
     * @return 验签是否通过
     * @throws Exception
     */
    public static boolean checkDecryptAndSign(String data, String encrypt_key,
                                              String yibaoPublickKey, String merchantPrivateKey) throws Exception {

        /** 1.使用YBprivatekey解开aesEncrypt。 */
        String AESKey = "";
        try {
            AESKey = RSA.decrypt(encrypt_key, merchantPrivateKey);
        } catch (Exception e) {
            /** AES密钥解密失败 */
            e.printStackTrace();
            return false;
        }

        /** 2.用aeskey解开data。取得data明文 */
        String realData = AES.decryptFromBase64(data, AESKey);

//        TreeMap<String, String> map = JSON.parseObject(realData,
//                new TypeReference<TreeMap<String, String>>() {
//                });

        Gson gson = new GsonBuilder().create();
        Type typeOfHashMap = new TypeToken<TreeMap<String, String>>() {
        }.getType();
        TreeMap<String, String> map = gson.fromJson(realData, typeOfHashMap);


        /** 3.取得data明文sign。 */
        String sign = CheckUtils.trimToEmpty(map.get("sign"));

        /** 4.对map中的值进行验证 */
        StringBuffer signData = new StringBuffer();
        Iterator<Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();

            /** 把sign参数隔过去 */
            if (CheckUtils.equals((String) entry.getKey(), "sign")) {
                continue;
            }
            signData.append(entry.getValue() == null ? "" : entry.getValue());
        }

        /** 5. result为true时表明验签通过 */
        boolean result = RSA.checkSign(signData.toString(), sign,
                yibaoPublickKey);

        return result;
    }

    /**
     * 生成RSA签名
     */
    public static String handleRSA(TreeMap<String, Object> map,
                                   String privateKey) {
        StringBuffer sbuffer = new StringBuffer();
        for (Entry<String, Object> entry : map.entrySet()) {
            sbuffer.append(entry.getValue());
        }
        String signTemp = sbuffer.toString();
        System.out.println("签名内容：" + signTemp);

        String sign = "";
        if (!TextUtils.isEmpty(privateKey)) {
            sign = RSA.sign(signTemp, privateKey);
        }
        return sign;
    }

    /**
     * 生成hmac
     */
    public static String handleHmac(TreeMap<String, String> map, String hmacKey) {
        StringBuffer sbuffer = new StringBuffer();
        for (Entry<String, String> entry : map.entrySet()) {
            sbuffer.append(entry.getValue());
        }
        String hmacTemp = sbuffer.toString();

        String hmac = "";
        if (!TextUtils.isEmpty(hmacKey)) {
            hmac = Digest.hmacSHASign(hmacTemp, hmacKey, Digest.ENCODE);
        }
        return hmac;
    }
}
