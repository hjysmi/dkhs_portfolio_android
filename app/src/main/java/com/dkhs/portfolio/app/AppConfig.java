package com.dkhs.portfolio.app;

import android.content.Context;
import android.util.Log;

import com.dkhs.portfolio.BuildConfig;
import com.dkhs.portfolio.bean.DraftBean;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.engine.LocalDataEngine.CityEngine;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.ChannelUtil;
import com.dkhs.portfolio.utils.DataBaseUtil;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

//import com.squareup.leakcanary.LeakCanary;

/**
 * Created by zjz on 2015/5/27.
 * App配置相关类
 * 包含了：
 * １.数据库是否更新
 * ２.设置友萌统计的平台名称
 * 3.图片下载工具类的初始化
 * 4.启动定时更新数据库的服务类
 * 5.消息中心模块的初始化
 */
public final class AppConfig {

    public static final boolean isDebug = BuildConfig.isSandbox;

    public static final int VERSION_CURRENT = 4;//当前数据库版本号

    //是否强制替换本地数据库
    private static final boolean hasReplaceRawDB = false;

    private Context mContext;

    public static void config(Context ctx) {

        AppConfig appConfig = new AppConfig();
        appConfig.init(ctx);
    }

    public void init(Context context) {
        this.mContext = context;
        //设置友盟统计的不同平台配置
        AnalyticsConfig.setChannel(ChannelUtil.getChannel(context));

        MobclickAgent.openActivityDurationTrack(false);
        copyCityDbToPhone();

        // 注册crashHandler，程序异常的日志管理工具

        if (isDebug) {
//            LeakCanary.install((Application) context);
//            CrashHandler.getInstance(context);
//            StatService.setDebugOn(true);
//            ANRWatchDog anrWatchDog = new ANRWatchDog();
//            anrWatchDog.start();

//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll()    // detect everything potentially suspect
//                    .penaltyLog()   // penalty is to write to log
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build());

        }
        //图片下载工具类的初始化
        ImageLoaderUtils.initImageLoader(context);
        //消息中心模块的初始化
        MessageManager.getInstance().connect();
    }



    private void copyCityDbToPhone() {

        final DataBaseUtil util = new DataBaseUtil(mContext);
        // 判断数据库是否存在
        boolean dbExist = util.checkCityDataBase();

        if (dbExist) {
            Log.i("tag", "The database is exist.");
        } else {// 不存在就把raw里的数据库写入手机

            new Thread() {
                public void run() {

                    try {
                        util.copyCityDataBase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }.start();

        }
    }

    public static DbUtils getDBUtils() {
        DbUtils.DaoConfig dbConfig = new DbUtils.DaoConfig(PortfolioApplication.getInstance());
        dbConfig.setDbVersion(VERSION_CURRENT);
        dbConfig.setDbUpgradeListener(new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
                Log.e("DBConfig", "oldVersion=" + oldVersion + " newVersion=" + newVersion);
                switch (newVersion) {
                    case 2:
                    case 3:
                        try {
                            db.dropTable(DraftBean.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    case 4:
                        try {
                            db.dropTable(SearchStockBean.class);
                            Log.d("wys","del searchStock table");
                        } catch (Exception e) {

                        }
                }

            }
        });
        return DbUtils.create(dbConfig);
    }

    public static DbUtils getCityDBUtils() {
        DbUtils.DaoConfig dbConfig = new DbUtils.DaoConfig(PortfolioApplication.getInstance());
        dbConfig.setDbName(CityEngine.CITYDBNAME);
        return DbUtils.create(dbConfig);
    }

}
