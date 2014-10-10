package com.dkhs.portfolio.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

public class LoginActivity extends ModelAcitivity implements OnClickListener {

	public static final int REQUEST_REGIST = 0;
	public static final int RESPONSE_REGIST = 1;
	private TextView username;
	private TextView password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		hideHead();
		setBackTitle(R.string.login_title);
		initViews();
		setListener();
		LogUtils.customTagPrefix = "LoginActivity";
	}

	public void setListener() {
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.tv_forget).setOnClickListener(this);

	}

	public void initViews() {
		username = (TextView) findViewById(R.id.username);
		password = (TextView) findViewById(R.id.password);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			login();
			break;
		case R.id.tv_forget:
			Intent intent = new Intent(LoginActivity.this, RLFActivity.class);
			intent.putExtra("activity_type", RLFActivity.LOGIN_TYPE);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void login() {
		userName = username.getText().toString();
		String passWord = password.getText().toString();
		if (NetUtil.checkNetWork(this)) {
			checkAndLogin(userName, passWord);
		} else {
			PromptManager.showNoNetWork(this);
		}

	}

	/**
	 * 校验用户
	 * 
	 * @param userName
	 * @param passWord
	 */
	private void checkAndLogin(String userName, String passWord) {
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
			Toast.makeText(this, "用户或者密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		PromptManager.showProgressDialog(this, R.string.logining);
		UserEngineImpl engine = new UserEngineImpl();
		if (checkEmail(userName)) {
			engine.login(userName, passWord, ConstantValue.IS_EMAIL, listener);
		} else if (isMobileNO(userName)) {
			engine.login(userName, passWord, ConstantValue.IS_MOBILE, listener);
		} else {
			Toast.makeText(this, "请输入手机号或者邮箱", Toast.LENGTH_SHORT).show();
		}
	}

	private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

		public void onHttpFailure(int errCode, String errMsg) {
			PromptManager.closeProgressDialog();
			super.onHttpFailure(errCode, errMsg);
		};

		public void onFailure(int errCode, String errMsg) {
			PromptManager.closeProgressDialog();
			super.onFailure(errCode, errMsg);
		};

		@Override
		protected UserEntity parseDateTask(String jsonData) {
			try {
				JSONObject json = new JSONObject(jsonData);
				UserEntity entity = DataParse.parseObjectJson(UserEntity.class,
						json.getJSONObject("user"));
				String token = (String) json.getJSONObject("token").get(
						"access_token");
				entity.setAccess_token(token);
				GlobalParams.ACCESS_TOCKEN = entity.getAccess_token();
				if(isMobileNO(userName)){
					GlobalParams.MOBILE = userName;
					entity.setMobile(userName);
				}
				saveUser(entity);
				return entity;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void afterParseData(UserEntity entity) {
			PromptManager.closeProgressDialog();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};
	private String userName;

	private void saveUser(final UserEntity user) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UserEntity entity = UserEntityDesUtil.decode(user, "DECODE",
						ConstantValue.DES_PASSWORD);
				DbUtils dbutil = DbUtils.create(PortfolioApplication
						.getInstance());
				UserEntity dbentity;
				try {
					dbentity = dbutil.findFirst(UserEntity.class);
					if (dbentity != null) {
						dbutil.delete(dbentity);
					}
					dbutil.save(entity);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 验证邮箱地址是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		boolean flag = false;
		try {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_REGIST && resultCode == RESPONSE_REGIST) {
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 检查软键盘是否弹出状态,是,就隐藏
	 */
	protected void checkSoftKeyboard() {
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
			// 隐藏软键盘
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		}
	}
}
