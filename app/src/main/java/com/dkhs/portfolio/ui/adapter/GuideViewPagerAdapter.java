package com.dkhs.portfolio.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.LoginActivity;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.RLFActivity;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.List;

/**
 * 
 * class desc: 引导页面适配器
 * 
 */
public class GuideViewPagerAdapter extends PagerAdapter {

    // 界面列表
    private List<View> views;
    private Activity activity;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    public GuideViewPagerAdapter(List<View> views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    // 销毁arg1位置的界面
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    // 获得当前界面数
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    // 初始化arg1位置的界面
    @Override
    public Object instantiateItem(View view, int arg1) {
        ((ViewPager) view).addView(views.get(arg1), 0);
        if (arg1 == views.size() - 1) {
            view.findViewById(R.id.guide_layout).getLayoutParams().height = (int) (UIUtils.getDisplayMetrics().heightPixels * 0.23);
            view.findViewById(R.id.btn_start_login).setOnClickListener(startClickListener);
            view.findViewById(R.id.btn_start_register).setOnClickListener(registerClickListener);
            view.findViewById(R.id.iv_start).setOnClickListener(startClickListener);
            // Button startLogin = (Button) arg0
            // .findViewById(R.id.btn_start_login);
            // startLogin.setOnClickListener(new OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // // 设置已经引导
            // setGuided();
            // goHome();
            //
            // }
            //
            // });
        }
        return views.get(arg1);
    }

    OnClickListener startClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            setGuided();
            goLogin();

        }
    };
    OnClickListener registerClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            setGuided();
            // goRegister();
            goMainPage();

        }
    };

    private void goLogin() {
        // 跳转
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void goMainPage() {
        // 跳转
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void goRegister() {
        // 跳转
        Intent intent = new Intent(activity, RLFActivity.class);
        intent.putExtra("activity_type", RLFActivity.REGIST_TYPE);

        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferences preferences = activity.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        // 存入数据
        editor.putBoolean("isFirstIn", false);
        // 提交修改
        editor.apply();
    }

    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

}
