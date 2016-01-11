package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.AdEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入GuideActivity；否，则进入MainActivity (3)2s后执行(2)操作
 */
public class SplashActivity extends FragmentActivity {
    boolean isFirstIn = false;
    private static final String ST_SPLASH_KEY = "splashAds";

    private static final int GO_ACCOUNT_MAIN = 1000;
    private static final int GO_GUIDE = 1001;
    private static final int GO_NOACCOUNT_MAIN = 1002;
    private static final int SHOW_AD = 1003;
    // 延迟2000豪秒
    private long splashDelayMills = 2000;
    private static final long SHOW_AD_MILLIS = 800;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    protected static final String TAG = "SplashActivity";
    private Context context;

    private ImageView adIm;
    private ImageView splashIM;
    private TextView forwardBtn;
    private int forwardSingal;

    /**
     * Handler:跳转到不同界面
     */
    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {


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
                case SHOW_AD:
                    showAD();
                    break;
            }
            return false;
        }
    });



    private void showAD() {
        if (adsEntity != null) {
            AlphaAnimation alphaAnimation=new AlphaAnimation(0.2f,1);
            alphaAnimation.setInterpolator(new DecelerateInterpolator());
            alphaAnimation.setDuration(800);
            adIm.startAnimation(alphaAnimation);
            BitmapUtils.displayNoEffect(adIm, adsEntity.getImage());
            adIm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(adsEntity.getRedirect_url())){
                        return;
                    }
                    mHandler.removeMessages(GO_GUIDE);
                    mHandler.removeMessages(GO_NOACCOUNT_MAIN);
                    mHandler.removeMessages(GO_ACCOUNT_MAIN);
                    Intent intent=  new Intent(SplashActivity.this, MainActivity.class);
//                    intent.putExtra("handlerUrl", "http://www.jianshu.com/users/8a2e2f6c64d7/latest_articles");
                    intent.putExtra("handlerUrl", adsEntity.getRedirect_url());
                    context.startActivity(intent);
                    SplashActivity.this.finish();

                }
            });
        }

    }

    private AdBean.AdsEntity adsEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        context = this;
        MobclickAgent.setDebugMode(true);
        MobclickAgent.updateOnlineConfig( this );
        // hideHead();
        init();
        if (!AppConfig.isDebug) {
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 2);
        }
        adIm = (ImageView) findViewById(R.id.adIM);
        splashIM = (ImageView) findViewById(R.id.splashIM);
        forwardBtn = (TextView) findViewById(R.id.btn_forward);
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeMessages(forwardSingal);
                mHandler.sendEmptyMessage(forwardSingal);
            }
        });
    }


    public void getSplashAds() {
        AdEngineImpl.getSplashAds(new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return AdBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                if (null != object) {
                    AdBean adBean = (AdBean) object;
                    if (adBean.getAds().size() > 0) {
                        AdBean.AdsEntity adsEntity = adBean.getAds().get(0);
                        PortfolioPreferenceManager.saveValue(ST_SPLASH_KEY, DataParse.objectToJson(adsEntity));
                        ImageLoaderUtils.loadImage(adBean.getAds().get(0).getImage());
                    }

                }
            }
        });
    }


    private void init() {
        UserEntity user;

        String splashAdStr = PortfolioPreferenceManager.getStringValue(ST_SPLASH_KEY);
        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (!TextUtils.isEmpty(splashAdStr)) {
            adsEntity = DataParse.parseObjectJson(AdBean.AdsEntity.class, splashAdStr);
        }
        if (adsEntity != null && !isFirstIn) {
            mHandler.sendEmptyMessageDelayed(SHOW_AD, SHOW_AD_MILLIS);
            forwardSingal = SHOW_AD;
            splashDelayMills = SHOW_AD_MILLIS + adsEntity.getDisplay_time() * 1000;
        }



        user = UserEngineImpl.getUserEntity();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getAccess_token())
                    && (PortfolioPreferenceManager.getIntValue(PortfolioPreferenceManager.KEY_APP_URL) == 2 || AppConfig.isDebug)) {
                GlobalParams.ACCESS_TOCKEN = user.getAccess_token();
                GlobalParams.USERNAME = user.getUsername();
                GlobalParams.MOBILE = user.getMobile();
                // 直接登陆


                mHandler.sendEmptyMessageDelayed(GO_ACCOUNT_MAIN, splashDelayMills);
                forwardSingal = GO_ACCOUNT_MAIN;
            } else {
                // 使用Handler的postDelayed方法，2秒后执行跳转到MainActivity
                mHandler.sendEmptyMessageDelayed(GO_NOACCOUNT_MAIN, splashDelayMills);
                forwardSingal = GO_NOACCOUNT_MAIN;
            }
        } else {


            // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
            if (!isFirstIn) {
                // 使用Handler的postDelayed方法，2秒后执行跳转到MainActivity
                mHandler.sendEmptyMessageDelayed(GO_NOACCOUNT_MAIN, splashDelayMills);
                forwardSingal = GO_NOACCOUNT_MAIN;
            } else {
                mHandler.sendEmptyMessageDelayed(GO_GUIDE, splashDelayMills);
                forwardSingal = GO_GUIDE;
            }
        }
        getSplashAds();


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


}
