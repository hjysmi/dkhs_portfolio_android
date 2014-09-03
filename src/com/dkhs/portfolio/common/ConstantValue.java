package com.dkhs.portfolio.common;


public interface ConstantValue {
	
	/**
	 * 无网络错误码
	 */
	int HTTP_ERROR_NET=505;
	/**
	 * 请求成功码
	 */
	int HTTP_OK=200;
	/**
	 * 请求出错码
	 */
	int HTTP_ERROR=404;
	/**
	 * des加密用密钥
	 */
	String DES_PASSWORD = "9b2648fcdfbad80f";

	/**
	 * 服务器地址
	 */
	String LOTTERY_URI = "http://10.0.2.2:8080/ZCWService/Entrance";
	/**
	 * 登陆请求地址
	 */
	public static final String LOGIN_URL="http://t2.dev.dkhs.com/api/v1/accounts/login.json";
	/**
	 * 获取token地址
	 */
	public static final String GET_ACCESSTOKEN_URL="http://t1.dev.dkhs.com/api/v1/oauth2/access_token/";
	/**
	 * userline话题
	 */
	public static final String USERLINE_URL="http://t1.dev.dkhs.com/api/v1/statusfeed/.json";
	/**
	 * 发表,评论,回复,话题
	 */
	public static final String POST_TOPIC_URL="http://t1.dev.dkhs.com/api/v1/statuses/";
	/**
	 * 注册地址
	 */
	public static final String REGISTER_URL="http://t2.dev.dkhs.com/api/v1/accounts/signup.json";

	public static final String CLIENT_ID="5e5aa376d092d983b119";
	public static final String CLIENT_SECERET="b6dd92078fcd20005f800cd6927fa84b5cde98a5";
	
	
	
	
}
