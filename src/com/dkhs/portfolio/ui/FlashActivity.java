package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.dkhs.portfolio.R;

/**
 * 欢迎界面
 * 
 * @author zhangcm
 * 
 */
public class FlashActivity extends Activity implements AnimationListener,
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);

		View view = findViewById(R.id.welcome_layout);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);

		// AlphaAnimation anim = new AlphaAnimation(0.1f, 1.0f);
		// anim.setDuration(2000);
		// view.startAnimation(anim);
		//
		// anim.setAnimationListener(this);

	}

	@Override
	public void onAnimationEnd(Animation am) {
		// 动画执行完毕开启主界面
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		// intent.setClass(this, TestActivity.class);
		startActivity(intent);
		finish();

	}

	@Override
	public void onAnimationRepeat(Animation am) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.login:
			intent = new Intent(this, LoginActivity.class);
			break;

		default:
			break;
		}
		if (null != intent) {
			startActivity(intent);
			finish();
		}

	}

}
