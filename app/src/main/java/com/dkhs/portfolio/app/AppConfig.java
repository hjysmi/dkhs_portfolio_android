package com.dkhs.portfolio.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.dkhs.portfolio.service.ReLoadDataService;
import com.dkhs.portfolio.ui.messagecenter.MessageManager;
import com.dkhs.portfolio.utils.ChannelUtil;
import com.dkhs.portfolio.utils.DataBaseUtil;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.umeng.analytics.AnalyticsConfig;

import java.io.IOException;

/**
 * Created by zjz on 2015/5/27.
 */
public final class AppConfig {

    public static final boolean isDebug = false;

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

        //融云API，根据不同的版本配置不同的APP key,默认为debug key
        //正式版本需要修改为release key
//        if (!isDebug) {
//            setRongYunMetaData();
//        }


        //是否替换本地raw里面的数据库
        if (hasReplaceRawDB || !PortfolioPreferenceManager.hasLoadSearchStock()) {
            copyDataBaseToPhone();
        }

        // 注册crashHandler，程序异常的日志管理工具
        CrashHandler crashHandler = CrashHandler.getInstance(context);

        //图片下载工具类的初始化
        ImageLoaderUtils.initImageLoader(context);


        //启动定时更新数据库的服务类
        Intent demand = new Intent(context, ReLoadDataService.class);
        context.startService(demand);

        //消息中心模块的初始化
        MessageManager.getInstance();


//        if (!BuildConfig.DEBUG) {
//            new ANRWatchDog().start();
//        }
    }


    private void setRongYunMetaData() {
        ApplicationInfo appi;
        try {
            appi = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            appi.metaData.putString("RONG_CLOUD_APP_KEY", "tdrvipksrgsu5");
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
    }


    private void copyDataBaseToPhone() {

        final DataBaseUtil util = new DataBaseUtil(mContext);
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
                }

                ;
            }.start();

        }
    }
}
