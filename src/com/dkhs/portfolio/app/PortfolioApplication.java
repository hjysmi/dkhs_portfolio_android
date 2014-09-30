/**
 * @Title PortfolioApplication.java
 * @Package com.dkhs.portfolio.app
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-2 上午10:26:13
 * @version V1.0
 */
package com.dkhs.portfolio.app;

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
    }

}
