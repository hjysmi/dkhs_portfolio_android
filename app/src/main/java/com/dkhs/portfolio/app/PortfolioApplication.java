/**
 * @Title PortfolioApplication.java
 * @Package com.dkhs.portfolio.app
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-2 上午10:26:13
 * @version V1.0
 */
package com.dkhs.portfolio.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.service.ReLoadDataService;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewMessageEvent;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.ChannelUtil;
import com.dkhs.portfolio.utils.DataBaseUtil;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.AnalyticsConfig;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @ClassName PortfolioApplication
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-2 上午10:26:13
 * @version 1.0
 */
public class PortfolioApplication extends Application {
    private static PortfolioApplication mInstance;

    private boolean isDebug = true;
    private boolean isLogin;

    // private String checkValue = "0";
    // private boolean change = false;
    // private int kLinePosition = -1;

    public static PortfolioApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        AnalyticsConfig.setChannel(ChannelUtil.getChannel(this));
        if(!isDebug){
            setRongYunMetaData();
        }
        super.onCreate();
        mInstance = this;
        if (!PortfolioPreferenceManager.hasLoadSearchStock()) {
            copyDataBaseToPhone();
        }

        // 注册crashHandler
        CrashHandler crashHandler = CrashHandler.getInstance(getApplicationContext());
        ImageLoaderUtils.initImageLoader(getApplicationContext());
        Intent demand = new Intent(this, ReLoadDataService.class);
        startService(demand);

        MessageManager.getInstance();

    }


    private void setRongYunMetaData() {
        ApplicationInfo appi;
        try {
            appi = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            appi.metaData.putString("RONG_CLOUD_APP_KEY", "tdrvipksrgsu5");
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private List<Activity> lists = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        lists.add(activity);
    }

    public void exitApp() {
        if (lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                Activity activity = lists.get(i);
                if (activity != null)
                    activity.finish();
            }
        }
    }

    public void clearActivities() {
        if (lists.size() > 1) {
            for (int i = 0; i < lists.size() - 1; i++) {
                Activity activity = lists.get(i);
                if (activity != null)
                    activity.finish();
            }
            Activity activity = lists.get(lists.size() - 1);
            lists.clear();
            lists.add(activity);
        }
    }

    private void copyDataBaseToPhone() {

        final DataBaseUtil util = new DataBaseUtil(this);
        // 判断数据库是否存在
        boolean dbExist = util.checkDataBase();

        if (dbExist) {
            Log.i("tag", "The database is exist.");
        } else {// 不存在就把raw里的数据库写入手机

            new Thread() {
                public void run() {

                    try {
                        util.copyDataBase();
                        PortfolioPreferenceManager.setLoadSearchStock();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
            }.start();

        }
    }

    public static boolean hasUserLogin() {
        if (!TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * app是否在前台
     * @return
     */
    private boolean isRunningForeground ()
    {
        ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName()))
        {
            return true ;
        }

        return false ;
    }


    public boolean isDebug() {
        return isDebug;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

}
