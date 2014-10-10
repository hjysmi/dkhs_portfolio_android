package com.dkhs.portfolio.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class SettingActivity extends ModelAcitivity implements OnClickListener{
public static boolean isSetPassword = true;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			default:
				break;
			}
		};
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		initViews();
		setListener();
		initData();
	}
	
	public void initData() {
		UserEngineImpl engine = new UserEngineImpl();
		if(!TextUtils.isEmpty(GlobalParams.MOBILE)){
			engine.isSetPassword(GlobalParams.MOBILE, new ParseHttpListener<Object>() {
				
				
				@Override
				protected Object parseDateTask(String jsonData) {
					// TODO Auto-generated method stub
					return jsonData;
				}
				
				@Override
				protected void afterParseData(Object object) {
					try {
						JSONObject json = new JSONObject((String) object);
						if(json.has("status")){
							isSetPassword = json.getBoolean("status");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	}

	public void setListener() {
		findViewById(R.id.btn_exit).setOnClickListener(this);
		findViewById(R.id.btn_setpassword).setOnClickListener(this);
	}

	public void initViews() {
		// TODO Auto-generated method stub
		setTitle(R.string.setting);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_exit:
			if(isSetPassword){
				DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
				try {
					GlobalParams.ACCESS_TOCKEN = null;
					GlobalParams.MOBILE = null;
					dbUtils.deleteAll(UserEntity.class);
					PortfolioApplication.getInstance().exitApp();
					intent = new Intent(this, NoAccountMainActivity.class);
					startActivity(intent);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					PortfolioApplication.getInstance().exitApp();
					intent = new Intent(this, NoAccountMainActivity.class);
					startActivity(intent);
				}
			}else{
				intent = new Intent(this, SetPasswordActivity.class);
				intent.putExtra("type", SetPasswordActivity.LOGOUT_TYPE);
				intent.putExtra("is_setpassword", isSetPassword);
				startActivity(intent);
			}
			break;
		case R.id.btn_setpassword:
			if(isSetPassword){
				intent = new Intent(this, SetPasswordActivity.class);
				intent.putExtra("type", SetPasswordActivity.SET_PASSWORD_TYPE);
				intent.putExtra("is_setpassword", isSetPassword);
			}else{
				intent = new Intent(this, SetPasswordActivity.class);
				intent.putExtra("type", SetPasswordActivity.SET_PASSWORD_TYPE);
				intent.putExtra("needClear", false);
				intent.putExtra("is_setpassword", isSetPassword);
			}
			startActivity(intent);
			
			break;

		default:
			break;
		}
	}
}
