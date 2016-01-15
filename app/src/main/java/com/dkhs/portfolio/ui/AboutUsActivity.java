/**
 * @Title AboutUsActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-7 下午2:32:21
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;

/**
 * @author zjz
 * @version 1.0
 * @ClassName AboutUsActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-11-7 下午2:32:21
 */
public class AboutUsActivity extends ModelAcitivity {

    private Context context;

    /**
     * @param arg0
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setTitle("关于我们");
        setContentView(R.layout.activity_about_us);
        context = this;
        initView();
    }

    private void initView() {
        TextView aboutVersion;
        aboutVersion = (TextView) findViewById(R.id.about_version);


        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            aboutVersion.setText("谁牛金融 " + version);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_abort_us);

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_AboutUs;
    }
}
