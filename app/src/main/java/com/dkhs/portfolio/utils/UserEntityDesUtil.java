package com.dkhs.portfolio.utils;

import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.github.gfx.util.encrypt.Encryption;

public class UserEntityDesUtil {


    /**
     * @param entity 加密的用户实体
     * @return
     */
    public static UserEntity encrypt(UserEntity entity) {

        if (null == entity || TextUtils.isEmpty(entity.getAccess_token())) {
            return entity;
        }
//        byte[] privateKey = hexStringToByteArray(ConstantValue.DES_PASSWORD);
//        assert privateKey.length == 16; // you must ensure!
        Encryption encryption = new Encryption(Encryption.getDefaultCipher(), PortfolioApplication.getInstance());

        Log.e("UserEntityDesUtil", "before encrypt:" + entity.getAccess_token());
        String authcode = encryption.encrypt(entity.getAccess_token());
        Log.e("UserEntityDesUtil", "after encrypt:" + authcode);
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

//        byte[] privateKey = hexStringToByteArray(ConstantValue.DES_PASSWORD);
//        assert privateKey.length == 16; // you must ensure!
        Encryption encryption = new Encryption(Encryption.getDefaultCipher(), PortfolioApplication.getInstance());

        Log.e("UserEntityDesUtil", "before decrypt:" + entity.getAccess_token());
        String authcode = encryption.decrypt(entity.getAccess_token());
        Log.e("UserEntityDesUtil", "after decrypt:" + authcode);
        entity.setAccess_token(authcode);
        return entity;
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length() / 2;
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
