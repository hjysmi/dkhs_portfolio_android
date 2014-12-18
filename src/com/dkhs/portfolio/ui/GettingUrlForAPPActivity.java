package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class GettingUrlForAPPActivity extends Activity implements
		OnClickListener {
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting_url);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 1);
		findViewById(R.id.setting_app_staging).setOnClickListener(this);
		findViewById(R.id.setting_app_main).setOnClickListener(this);
		findViewById(R.id.setting_app_right).setOnClickListener(this);
		findViewById(R.id.setting_app_test).setOnClickListener(this);
		findViewById(R.id.setting_app_cancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//开发环境
		case R.id.setting_app_staging:
			PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 0);
			break;
			//测试环境
		case R.id.setting_app_main:
			PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 1);
			break;
			//服务器调试
		case R.id.setting_app_right:
			PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 3);
			break;
			//正式环境
		case R.id.setting_app_test:
			PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 2);
			break;
		case R.id.setting_app_cancel:
			finish();
			break;
		default:
			break;
		}
		finish();
	}
	private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_url_choose);
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}
}