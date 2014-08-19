package com.dkhs.portfolio.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.User;
import com.dkhs.portfolio.engine.UserEngine;
import com.dkhs.portfolio.utils.NetUtil;
import com.lidroid.xutils.util.LogUtils;

public class LoginActivity extends ModelAcitivity implements OnClickListener {

	public static final int REQUEST_REGIST = 0;
	public static final int RESPONSE_REGIST = 1;
	private TextView username;
	private TextView password;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// PromptManager.closeProgressDialog();
			// switch (msg.what) {
			// case ConstantValue.HTTP_OK:
			// AccessTokenEntity entity = (AccessTokenEntity) msg.obj;
			// DbUtils db = DbUtils.create(LoginActivity.this);
			// try {
			// AccessTokenEntity findFirst =
			// db.findFirst(AccessTokenEntity.class);
			// if (findFirst != null) {
			// db.delete(findFirst);
			// }
			// db.save(entity);
			// GlobalParams.ACCESS_TOCKEN = entity.getAccess_token();
			// } catch (DbException e) {
			// e.printStackTrace();
			// }
			// LogUtils.i(entity.toString());
			// checkSoftKeyboard();
			// setResult(MainActivity.RESPONSE_LOGIN);
			// finish();
			// break;
			// case ConstantValue.HTTP_ERROR:
			// PromptManager.showToast(LoginActivity.this, (String) msg.obj);
			// break;
			// case ConstantValue.HTTP_ERROR_NET:
			// PromptManager.showErrorDialog(LoginActivity.this, "网络异常");
			// break;
			// default:
			// break;
			// }
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setBackTitle(R.string.login_title);
		initViews();
		setListener();
		LogUtils.customTagPrefix = "LoginActivity";
	}

	public void setListener() {
		// findViewById(R.id.qq_login).setOnClickListener(this);
		// findViewById(R.id.weibo_login).setOnClickListener(this);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
		findViewById(R.id.tv_forget).setOnClickListener(this);
		// findViewById(R.id.fast_register).setOnClickListener(this);

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
		case R.id.register:
			regist();
			break;
		case R.id.tv_forget:
			findPasswrod();
			break;
		default:
			break;
		}
	}

	private void findPasswrod(){
		Intent intent = new Intent(this, FindPasswrodActivity.class);
		startActivity(intent);
	}
	
	private void regist() {
		// Intent intent = new Intent(this, GetVericodeActivity.class);
		// startActivityForResult(intent, REQUEST_REGIST);
	}

	private void login() {
		String userName = username.getText().toString();
		String passWord = password.getText().toString();
		check(userName, passWord);

	}

	/**
	 * 校验用户
	 * 
	 * @param userName
	 * @param passWord
	 */
	private void check(String userName, String passWord) {
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
			Toast.makeText(this, "用户或者密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		// if (NetUtil.checkNetWork(this)) {
		// PromptManager.showProgressDialog(this);
		// UserEngine userEngine = BeanFactory.getImpl(UserEngine.class);
		// userEngine.login(new User(userName, passWord), handler);
		// } else {
		// PromptManager.showNoNetWork(this);
		// }
		/*
		 * if (checkEmail(userName) || isMobileNO(userName)) { // 账号校验正确，开始登陆 if
		 * (NetUtil.checkNetWork(this)) {
		 * PromptManager.showProgressDialog(this); UserEngine userEngine =
		 * BeanFactory.getImpl(UserEngine.class); userEngine.login(new
		 * User(userName, passWord), handler); } else {
		 * PromptManager.showNoNetWork(this); } } else { Toast.makeText(this,
		 * "请输入手机号或者邮箱", Toast.LENGTH_SHORT).show(); }
		 */
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
