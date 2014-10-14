/**
 * @Title PortfolioApplication.java
 * @Package com.dkhs.portfolio.app
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-2 上午10:26:13
 * @version V1.0
 */
package com.dkhs.portfolio.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.dkhs.portfolio.service.LoadStockToDBService;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;

/**
 * @ClassName PortfolioApplication
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-2 上午10:26:13
 * @version 1.0
 */
public class PortfolioApplication extends Application {
    private static PortfolioApplication mInstance;

    public static PortfolioApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mInstance = this;
        if (!PortfolioPreferenceManager.hasLoadSearchStock()) {
            LoadStockToDBService.requestDownload(this);
        }

        // 注册crashHandler
        CrashHandler crashHandler = CrashHandler.getInstance(getApplicationContext());

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

}