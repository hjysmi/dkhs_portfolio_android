/**
 * @Title LoadStockToDBService.java
 * @Package com.dkhs.portfolio.service
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午1:43:22
 * @version V1.0
 */
package com.dkhs.portfolio.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.dkhs.portfolio.engine.SearchStockEngineImpl;

/**
 * @ClassName LoadStockToDBService
 * @Description 本地股票数据更新服务
 * @author zjz
 * @date 2014-9-17 下午1:43:22
 * @version 1.0
 */
public class LoadStockToDBService extends IntentService {

    private static final String SERVICE_NAME = "LoadStockToDBService";
    private static final String EXTRA_LOAD_URL = "extra_load_url";

    public static void requestDownload(Context context) {
        Intent intent = new Intent(context, LoadStockToDBService.class);
        // intent.putExtra(EXTRA_LOAD_URL, url);

        context.startService(intent);
    }

    public LoadStockToDBService() {
        super(SERVICE_NAME);
    }

    private void downLoadStockPofile() {
        // if( new SearchStockEngineImpl().saveStockList()){
        // PortfolioPreferenceManager.setLoadSearchStock();
        // }
//        new Thread(){
//            public void run() {
//                
                SearchStockEngineImpl.loadStockList();
        // };
        // }.start();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // download(intent.getExtras().getString(EXTRA_LOAD_URL, ""));
        downLoadStockPofile();
    }

}
