package com.dkhs.portfolio.utils;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;

public class UserEntityDesUtil {


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
