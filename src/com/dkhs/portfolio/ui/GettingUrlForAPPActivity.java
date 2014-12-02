package com.dkhs.portfolio.ui;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;

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
		findViewById(R.id.setting_app_staging).setOnClickListener(this);
		findViewById(R.id.setting_app_main).setOnClickListener(this);
		findViewById(R.id.setting_app_right).setOnClickListener(this);
		findViewById(R.id.setting_app_test).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.setting_app_staging:
			PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 0);
			break;
		case R.id.setting_app_main:
			PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 1);
			break;
		case R.id.setting_app_right:
			PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 2);
			break;
		case R.id.setting_app_test:
			PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_APP_URL, 3);
			break;
		default:
			break;
		}
		finish();
	}

}