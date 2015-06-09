package com.dkhs.portfolio.utils;

import com.dkhs.portfolio.bean.UserEntity;

public class UserEntityDesUtil {


    /**
     * @param entity 加密的用户实体
     * @return
     */
    public static UserEntity decode(UserEntity entity) {

//        if (null == entity || TextUtils.isEmpty(entity.getAccess_token())) {
//            return entity;
//        }
////        byte[] privateKey = hexStringToByteArray(ConstantValue.DES_PASSWORD);
////        assert privateKey.length == 16; // you must ensure!
//        Encryption encryption = new Encryption(Encryption.getDefaultCipher(), PortfolioApplication.getInstance());
//
//
//        String authcode = encryption.decrypt(entity.getAccess_token());
//        entity.setAccess_token(authcode);
        return entity;
    }

    /**
     * @param entity 加解密的用户实体
     * @return
     */
    public static UserEntity encrypt(UserEntity entity) {
//        if (null == entity || TextUtils.isEmpty(entity.getAccess_token())) {
//            return entity;
//        }
//
////        byte[] privateKey = hexStringToByteArray(ConstantValue.DES_PASSWORD);
////        assert privateKey.length == 16; // you must ensure!
//        Encryption encryption = new Encryption(Encryption.getDefaultCipher(), PortfolioApplication.getInstance());
//
//        String authcode = encryption.encrypt(entity.getAccess_token());
//        entity.setAccess_token(authcode);
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
