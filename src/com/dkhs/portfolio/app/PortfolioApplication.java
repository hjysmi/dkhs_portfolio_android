/**
 * @Title PortfolioApplication.java
 * @Package com.dkhs.portfolio.app
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-2 上午10:26:13
 * @version V1.0
 */
package com.dkhs.portfolio.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

import com.dkhs.portfolio.service.LoadStockToDBService;
import com.dkhs.portfolio.utils.DataBaseUtil;
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
            copyDataBaseToPhone();
        }
        LoadStockToDBService.requestDownload(this);

        // 注册crashHandler
        CrashHandler crashHandler = CrashHandler.getInstance(getApplicationContext());

        DisplayMetrics metric = getResources().getDisplayMetrics();

        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）

        System.out.println("Devices width:" + width);
        System.out.println("Devices height:" + height);
        System.out.println("Devices density:" + density);
        System.out.println("Devices densityDpi:" + densityDpi);

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
        System.out.println("lists size :" + lists.size());
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
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                };
            }.start();
            // } catch (IOException e) {
            // throw new Error("Error copying database");
            // }
        }
    }

}
