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
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName AboutUsActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-11-7 下午2:32:21
 * @version 1.0
 */
public class AboutUsActivity extends ModelAcitivity {
	private TextView aboutVersion;
	private Context context;
    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param arg0
     * @return
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
    private void initView(){
    	aboutVersion = (TextView) findViewById(R.id.about_version);
    	
    	
    	try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			aboutVersion.setText("谁牛 " + version);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_abort_us);
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}
}
