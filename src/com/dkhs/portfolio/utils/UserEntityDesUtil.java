package com.dkhs.portfolio.utils;

import com.dkhs.portfolio.bean.UserEntity;

public class UserEntityDesUtil {


	/**
	 * @param entity 加解密的用户实体
	 * @param operation "DECODE"和"ENCODE"
	 * @param key 16位数的秘钥
	 * @return
	 */
	public static UserEntity decode(UserEntity entity , String operation, String key){
		DES des = new DES();
//		entity.setApp_id(des.authcode(entity.getApp_id(), operation, key));
		String authcode = des.authcode(entity.getAccess_token(), operation, key);
		entity.setAccess_token(authcode);
		return entity;
	}
}
