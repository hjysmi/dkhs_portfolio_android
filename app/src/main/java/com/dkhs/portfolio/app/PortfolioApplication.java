/**
 * @Title PortfolioApplication.java
 * @Package com.dkhs.portfolio.app
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-2 上午10:26:13
 * @version V1.0
 */
package com.dkhs.portfolio.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.dkhs.portfolio.common.GlobalParams;

import java.util.ArrayList;
import java.util.List;

//import com.squareup.leakcanary.LeakCanary;

/**
 * @author zjz
 * @version 1.0
 * @ClassName PortfolioApplication
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-2 上午10:26:13
 */
public class PortfolioApplication extends MultiDexApplication {
    private static PortfolioApplication mInstance;


    private boolean isLogin;


    public static PortfolioApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        mInstance = this;
        AppConfig.config(this);
        //监听手势超时
        mHandler = new MyHandler(getMainLooper());
        super.onCreate();
    }
    /**
     * 无操作后锁屏时间
     */
    private final static int SHOW_GESTURE_DELAY = GlobalParams.SHOW_GESTURE_DELAY;

    private final static int NEED_SHOW_GESTURE = 2;

    private MyHandler mHandler;

    public void onUserInteraction() {
        mHandler.removeMessages(NEED_SHOW_GESTURE);
        mHandler.sendEmptyMessageDelayed(NEED_SHOW_GESTURE, SHOW_GESTURE_DELAY);
    }

    private class MyHandler extends Handler {

        public MyHandler(Looper mainLooper) {
            super(mainLooper);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEED_SHOW_GESTURE:
                    GlobalParams.needShowGesture = true;
                    break;
            }
        }
    }


    public List<Activity> getLists() {
        return lists;
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
    private List<Activity> assestsLists = new ArrayList<>();
    public void addAssestsActivity(Activity activity) {
        assestsLists.add(activity);
    }
    public void removeAssestsActivity(Activity activity) {
        assestsLists.remove(activity);
    }

    public void exitAssests() {
        if (assestsLists.size() > 0) {
            for (int i = 0; i < assestsLists.size(); i++) {
                Activity activity = assestsLists.get(i);
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


    public static boolean hasUserLogin() {
        return !TextUtils.isEmpty(GlobalParams.ACCESS_TOCKEN);
    }

    /**
     * app是否在前台
     *
     * @return
     */
    public boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName());

    }


    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

}
