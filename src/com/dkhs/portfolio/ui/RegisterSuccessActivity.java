package com.dkhs.portfolio.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.umeng.analytics.MobclickAgent;

public class RegisterSuccessActivity extends ModelAcitivity implements OnClickListener {

	private static final int AUTO_LOGINING=0;
	private static final int AUTO_LOGIN=1;
	protected static final String TAG = "RegisterSuccessActivity";
	public Timer mTimer = new Timer();// 定时器 
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AUTO_LOGIN:
				mTimer.cancel();
				if(isAutoLoginAble)
					goMain();
				break;
			case AUTO_LOGINING:
				btn_sc_login.setText((5-count)+"秒后自动登录");
				count++;
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist_success);
		initViews();
		setListener();
		initData();
	}
	

	public void setListener() {
		btn_sc_login.setOnClickListener(this);
	}

	public void initViews() {
		TextView tv_username = (TextView) findViewById(R.id.tv_username);
		tv_username.setText(getIntent().getStringExtra("username"));
		btn_sc_login = (Button) findViewById(R.id.btn_sc_login);
		setTitle(R.string.register_success);
		mTimer = new Timer();
		timerTask();
	}

	@Override
	public void onClick(View v) {
		isAutoLoginAble  = false;
		goMain();
	}

	private void goMain() {
		Intent intent = new Intent(this, NewMainActivity.class);
		startActivity(intent);
		finish();
	}

	private int count = 0;
	private Button btn_sc_login;
	private boolean isAutoLoginAble =true;
	
	private void timerTask() {
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(isAutoLoginAble){
					if(count < 5){
						handler.sendEmptyMessage(AUTO_LOGINING);
					}else{
						handler.sendEmptyMessage(AUTO_LOGIN);
					}
				}else{
					mTimer.cancel();
				}
			}
		}, 0, 1000);
	}
	
	
	@Override
	public void onBackPressed() {
//		PromptManager.closeProgressDialog();
		isAutoLoginAble  = false;
		goMain();
		super.onBackPressed();
	}
	
	@Override
	public void finish() {
		super.finish();
	}

	public void initData() {
		// TODO Auto-generated method stub
		
	}
	private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_sign_success);
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
