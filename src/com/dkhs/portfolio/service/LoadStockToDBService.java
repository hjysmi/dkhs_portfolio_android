/**
 * @Title LoadStockToDBService.java
 * @Package com.dkhs.portfolio.service
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午1:43:22
 * @version V1.0
 */
package com.dkhs.portfolio.service;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.engine.SearchStockEngineImpl;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName LoadStockToDBService
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-17 下午1:43:22
 * @version 1.0
 */
public class LoadStockToDBService extends IntentService {

    private static final String SERVICE_NAME = "LoadStockToDBService";
    private static final String EXTRA_LOAD_URL = "extra_load_url";

    public static void requestDownload(Context context, String url) {
        Intent intent = new Intent(context, LoadStockToDBService.class);
        intent.putExtra(EXTRA_LOAD_URL, url);

        context.startService(intent);
    }

    public LoadStockToDBService() {
        super(SERVICE_NAME);
    }

    private void download(String url) {
        if (TextUtils.isEmpty(url)) {
            LogUtils.d("Load server url is null");
            return;
        }
        LogUtils.d("Load server url is " + url);
       if( new SearchStockEngineImpl().saveStockList()){
           PortfolioPreferenceManager.setLoadSearchStock();
       }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        download(intent.getExtras().getString(EXTRA_LOAD_URL, ""));
    }

}
