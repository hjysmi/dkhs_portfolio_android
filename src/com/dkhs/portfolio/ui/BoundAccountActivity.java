package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
/**
 * 绑定第三方账号
 * @author weiting
 *
 */
public class BoundAccountActivity extends ModelAcitivity implements OnClickListener{
	private TextView boundTextEmail;
	private TextView boundTextQq;
	private TextView boundTextWeibo;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_bound_account);
		setTitle("绑定账号");
		initView();
		initListener();
	}
	private void initView(){
		boundTextEmail = (TextView) findViewById(R.id.bound_text_email);
		boundTextQq = (TextView) findViewById(R.id.bound_text_qq);
		boundTextWeibo = (TextView) findViewById(R.id.bound_text_weibo);
	}
	private void initListener(){
		boundTextEmail.setOnClickListener(this);
		boundTextQq.setOnClickListener(this);
		boundTextWeibo.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bound_text_email:
			
			break;
	case R.id.bound_text_qq:
				
				break;
	case R.id.bound_text_weibo:
		
		break;
		default:
			break;
		}
	}
	
}
