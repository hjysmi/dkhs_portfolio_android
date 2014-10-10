package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @ClassName UserEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zcm
 * @date 2014-10-08 下午2:25:01
 * @version 1.0
 */
public class UserEngineImpl {

	 /**
	  * 登录
	 * @param username
	 * @param password
	 * @param logintype
	 * @param listener
	 */
	public void login(String username, String password, int logintype, ParseHttpListener<UserEntity> listener){
		RequestParams params = new RequestParams();
		if (logintype == ConstantValue.IS_CAPTCHA) {
			params.addBodyParameter("mobile", username);
			params.addBodyParameter("captcha", password);
		} else if (logintype == ConstantValue.IS_MOBILE) {
			params.addBodyParameter("mobile", username);
			params.addBodyParameter("password", password);
		} else if (logintype == ConstantValue.IS_EMAIL) {
			params.addBodyParameter("email", username);
			params.addBodyParameter("password", password);
		}
		DKHSClient.request(HttpMethod.POST, DKHSUrl.User.login, params, listener);
	 }
	
	/**
	 * 设置密码
	 * @param password
	 * @param captcha
	 * @param listener
	 */
	public void setPassword(String password, String captcha, ParseHttpListener<Object> listener){
		RequestParams params = new RequestParams();
		params.addBodyParameter("password", password);
		params.addBodyParameter("captcha", captcha);
		DKHSClient.request(HttpMethod.POST, DKHSUrl.User.setpassword, params, listener);
	}
	
	/**
	 * 修改密码
	 * @param oldpassword
	 * @param newpassword
	 * @param listener
	 */
	public void changePassword(String oldpassword, String newpassword, ParseHttpListener<Object> listener){
		RequestParams params = new RequestParams();
		params.addBodyParameter("old_password", oldpassword);
		params.addBodyParameter("new_password", newpassword);
		DKHSClient.request(HttpMethod.POST, DKHSUrl.User.changepassword, params, listener);
	}
	
	/**
	 * 是否设置过密码
	 * @param mobile
	 * @param listener
	 */
	public void isSetPassword(String mobile, ParseHttpListener<Object> listener){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		DKHSClient.request(HttpMethod.GET, DKHSUrl.User.is_setpassword, params, listener);
	}
	
	/**
	 * 获取验证码
	 * @param mobile
	 * @param listener
	 */
	public void getVericode(String mobile, ParseHttpListener<Object> listener){
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		DKHSClient.request(HttpMethod.GET, DKHSUrl.User.get_vericode, params, listener);
	}
	
	public void register(String mobile, String captha, ParseHttpListener<UserEntity> listener){
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobile", mobile);
		params.addBodyParameter("captcha", captha);
		DKHSClient.request(HttpMethod.POST, DKHSUrl.User.register, params, listener);
	}
	
}
