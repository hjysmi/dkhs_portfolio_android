package com.dkhs.portfolio.engine;

import android.os.Handler;

import com.dkhs.portfolio.bean.User;


/**
 * 用户的业务操作
 * @author zhangcm
 *
 */
public abstract class UserEngine extends BaseEngine{

	/**
	 * @param user 登陆对象,不能为空,username和
	 * @param handler 用于回调,也不能为空,handleMsg里面处理返回值信息
	 */
	public abstract void login(User user, final Handler handler);
	/**
	 * @param username用户昵称
	 * @param mobile 手机
	 * @param gender 性别:0保密,1男,2女
	 * @param password 密码
	 * @param handler 回调
	 */
	public abstract void register(String username,String mobile,String gender,String password,final Handler handler);
}
