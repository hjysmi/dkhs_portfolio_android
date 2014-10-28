package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;

public class NoAccountMainActivity extends ModelAcitivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		hideHead();
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
		PortfolioApplication.getInstance().clearActivities();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
			
			break;
		case R.id.register:
			Intent intent = new Intent(this, RLFActivity.class);
			intent.putExtra("activity_type", RLFActivity.REGIST_TYPE);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	



}
