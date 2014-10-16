package com.dkhs.portfolio.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

public class SettingActivity extends ModelAcitivity implements OnClickListener{
public static boolean isSetPassword = true;
private LinearLayout settingLayoutGroup;
private Context context;
	
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
		context = this;
		initViews();
		setListener();
		initData();
		loadCombinationData();
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
		settingLayoutGroup = (LinearLayout) findViewById(R.id.setting_layout_group);
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
	public void createGroupShow(List<CombinationBean> lsit){
		int i = 0;
		for (CombinationBean combinationBean : lsit) {
			LayoutInflater l = LayoutInflater.from(context);
			View view = l.inflate(R.layout.setting_group_item, null);
			Switch s = (Switch) view.findViewById(R.id.switch1);
			s.setText(combinationBean.getName());
			if(i++%2 == 0)
				s.setChecked(true);
			settingLayoutGroup.addView(view);
		}
	}
	private void loadCombinationData() {
        new MyCombinationEngineImpl().getCombinationList(new ParseHttpListener<List<CombinationBean>>() {

            @Override
            protected List<CombinationBean> parseDateTask(String jsonData) {
                Type listType = new TypeToken<List<CombinationBean>>() {
                }.getType();
                List<CombinationBean> combinationList = DataParse.parseJsonList(jsonData, listType);

                return combinationList;
            }

            @Override
            protected void afterParseData(List<CombinationBean> dataList) {
                createGroupShow(dataList);
            }

        }.setLoadingDialog(SettingActivity.this, R.string.loading));
    }
}
