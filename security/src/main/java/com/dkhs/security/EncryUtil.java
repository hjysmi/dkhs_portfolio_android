package com.dkhs.security;

import android.text.TextUtils;

import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * Created by zjz on 2015/7/21.
 */
public class EncryUtil {

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
