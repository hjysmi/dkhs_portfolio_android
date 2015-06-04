package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入GuideActivity；否，则进入MainActivity (3)2s后执行(2)操作
 * 
 */
public class SplashActivity extends FragmentActivity {
    boolean isFirstIn = false;

    private static final int GO_ACCOUNT_MAIN = 1000;
    private static final int GO_GUIDE = 1001;
    private static final int GO_NOACCOUNT_MAIN = 1002;
    // 延迟2000豪秒
    private static final long SPLASH_DELAY_MILLIS = 2000;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    protected static final String TAG = "SplashActivity";
    private Context context;
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_NOACCOUNT_MAIN:
                    // goNoAccountMain();
                    goAccountMain();
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
        context = this;
        // hideHead();
        init();
        if (!AppConfig.isDebug) {
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 2);
        }
    }

    private void init() {
        UserEntity user;

        // user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);
        user = UserEngineImpl.getUserEntity();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getAccess_token())
                    && (PortfolioPreferenceManager.getIntValue(PortfolioPreferenceManager.KEY_APP_URL) == 2 || AppConfig.isDebug)) {
                user = UserEntityDesUtil.decode(user, "ENCODE", ConstantValue.DES_PASSWORD);
                GlobalParams.ACCESS_TOCKEN = user.getAccess_token();
                GlobalParams.USERNAME = user.getUsername();
                GlobalParams.MOBILE = user.getMobile();
                // 直接登陆
                mHandler.sendEmptyMessageDelayed(GO_ACCOUNT_MAIN, SPLASH_DELAY_MILLIS);
            } else {
                // 使用Handler的postDelayed方法，2秒后执行跳转到MainActivity
                mHandler.sendEmptyMessageDelayed(GO_NOACCOUNT_MAIN, SPLASH_DELAY_MILLIS);
            }
        } else {
            // 读取SharedPreferences中需要的数据
            // 使用SharedPreferences来记录程序的使用次数
            SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);

            // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
            isFirstIn = preferences.getBoolean("isFirstIn", true);

            // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
            if (!isFirstIn) {
                // 使用Handler的postDelayed方法，2秒后执行跳转到MainActivity
                mHandler.sendEmptyMessageDelayed(GO_NOACCOUNT_MAIN, SPLASH_DELAY_MILLIS);
            } else {
                mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
            }
        }

    }

    private void goNoAccountMain() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
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
        // Intent intent = new Intent(SplashActivity.this, TestActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_splash);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(context);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(context);
    }
}
