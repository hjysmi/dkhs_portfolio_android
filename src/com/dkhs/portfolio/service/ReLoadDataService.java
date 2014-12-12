/**
 * @Title ReLoadDataService.java
 * @Package com.dkhs.portfolio.service
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-12 下午1:08:58
 * @version V1.0
 */
package com.dkhs.portfolio.service;

import com.dkhs.portfolio.engine.SearchStockEngineImpl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @ClassName ReLoadDataService
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-12-12 下午1:08:58
 * @version 1.0
 */
public class ReLoadDataService extends Service {

    private static final String TAG = "ReLoadDataService";
    // 30分钟
    // private int repeatTime = 30 * 60 * 1000;
    private int repeatTime = 60 * 1000;
    private boolean first = true;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReLoadDataService.class);
        PendingIntent pendingServiceIntent = PendingIntent.getService(this, 1000, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + repeatTime, repeatTime,
                pendingServiceIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // if (first == true) {
        onHandleIntent(intent);
        // }
        // first = false;
        return Service.START_STICKY;
    }

    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "ReLoadDataService onHandleIntent");
        SearchStockEngineImpl.loadStockList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
