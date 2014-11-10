package com.dkhs.portfolio.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.SIMCardInfo;

public class FindPasswrodActivity extends ModelAcitivity implements
		OnClickListener {
	private EditText phoneNumber;
	private EditText vericode;
	private Button code_receive;
	private Button next_step;
	private TextView textView;
	public static final String KEY_BACK_TITLE="key_back_title";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getvericode_layout);
		if(getIntent() != null){
			getIntent().getIntExtra(KEY_BACK_TITLE, R.string.find_passwrod);
		}
		setBackTitle(R.string.find_passwrod);
		initViews();
		setListener();
	}

	public void setListener() {
		// TODO Auto-generated method stub
		vericode.setOnClickListener(this);
		next_step.setOnClickListener(this);
	}

	private void initViews() {
		textView = (TextView) findViewById(R.id.textView);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		vericode = (EditText) findViewById(R.id.vericode);
		code_receive = (Button) findViewById(R.id.code_reveive);
		next_step = (Button) findViewById(R.id.next_step);
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.next_step:
			nextStep();
			break;
		case R.id.code_reveive:

			break;
		default:
			break;
		}
	}

	/**
	 * 下一步
	 */
	private void nextStep() {
		String telephone = phoneNumber.getText().toString();
		String code = vericode.getText().toString();
		// 校验手机号与验证码
		if (TextUtils.isEmpty(telephone) || TextUtils.isEmpty(code)) {
			// PromptManager.showToast(this, "手机号或者验证码不能为空");
			return;
		}
		if (SIMCardInfo.isMobileNO(telephone)) {
			// Intent intent = new Intent(this, RegisterActivity.class);
			// intent.putExtra("telephone", telephone);
			// intent.putExtra("code", code);
			// startActivityForResult(intent, LoginActivity.REQUEST_REGIST);
		} else {
			// PromptManager.showToast(this, "请输入正确的手机号");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LoginActivity.REQUEST_REGIST
				&& resultCode == LoginActivity.RESPONSE_REGIST) {
			setResult(LoginActivity.RESPONSE_REGIST);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
