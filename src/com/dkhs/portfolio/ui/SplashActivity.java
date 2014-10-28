package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * 
 * class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入GuideActivity；否，则进入MainActivity (3)2s后执行(2)操作
 * 
 */
public class SplashActivity extends ModelAcitivity {
	boolean isFirstIn = true;

	private static final int GO_ACCOUNT_MAIN = 1000;
	private static final int GO_GUIDE = 1001;
	private static final int GO_NOACCOUNT_MAIN = 1002;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 2000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	protected static final String TAG = "SplashActivity";

	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_NOACCOUNT_MAIN:
				goNoAccountMain();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			case GO_ACCOUNT_MAIN:
				goAccountMain();
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		hideHead();
		init();
	}

	private void init() {
		UserEntity user;
		try {
			user = DbUtils.create(PortfolioApplication.getInstance())
					.findFirst(UserEntity.class);
			if (user != null) {
				if (!TextUtils.isEmpty(user.getAccess_token())) {
					user = UserEntityDesUtil.decode(user, "ENCODE",
							ConstantValue.DES_PASSWORD);
					GlobalParams.ACCESS_TOCKEN = user.getAccess_token();
					GlobalParams.MOBILE = user.getMobile();
					// 直接登陆
					mHandler.sendEmptyMessageDelayed(GO_ACCOUNT_MAIN,
							SPLASH_DELAY_MILLIS);
				} else {
					// 使用Handler的postDelayed方法，2秒后执行跳转到MainActivity
					mHandler.sendEmptyMessageDelayed(GO_NOACCOUNT_MAIN,
							SPLASH_DELAY_MILLIS);
				}
			} else {
				// 读取SharedPreferences中需要的数据
				// 使用SharedPreferences来记录程序的使用次数
				SharedPreferences preferences = getSharedPreferences(
						SHAREDPREFERENCES_NAME, MODE_PRIVATE);

				// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
//				isFirstIn = preferences.getBoolean("isFirstIn", true);

				// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
				if (!isFirstIn) {
					// 使用Handler的postDelayed方法，2秒后执行跳转到MainActivity
					mHandler.sendEmptyMessageDelayed(GO_NOACCOUNT_MAIN,
							SPLASH_DELAY_MILLIS);
				} else {
					mHandler.sendEmptyMessageDelayed(GO_GUIDE,
							SPLASH_DELAY_MILLIS);
				}
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void goNoAccountMain() {
		Intent intent = new Intent(SplashActivity.this,
				NoAccountMainActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void goAccountMain() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

}
