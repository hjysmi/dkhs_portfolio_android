package com.dkhs.portfolio.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;

public class PersonSignSettingActivity extends ModelAcitivity implements OnClickListener{
	private Button btnCancle;
	private Button btnSave;
	private EditText signText;
	private TextView signVlaue;
	public final static String DESCRIPTION = "Description";
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_sign_setting);
		setTitle(R.string.sign_text_title);
		
		initView();
		initListener();
	}
	private void initView(){
		signText = (EditText) findViewById(R.id.sign_edit_text);
		signVlaue = (TextView) findViewById(R.id.sign_text_text);
		btnCancle = getBtnBack();
		btnSave = getRightButton();
		
		Bundle b = getIntent().getExtras();
		if(null != b){
			signText.setText(b.getString(DESCRIPTION));
			signText.setSelection(signText.length());
			int k = 40 - signText.getText().toString().length();
			signVlaue.setText(k+"");
		}
		btnCancle.setText("取消");
		btnCancle.setBackgroundResource(R.drawable.white_black_selector);
		btnCancle.setCompoundDrawables(null, null, null, null);
		btnSave.setText("保存");
		btnSave.setBackgroundResource(R.drawable.white_black_selector);
	}
	private void initListener(){
		signText.addTextChangedListener(new EditListener());
		btnSave.setOnClickListener(this);
	}
	
	class EditListener implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			int k = 40 - signText.getText().toString().length();
			signVlaue.setText(k + "");
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_right:
			if(TextUtils.isEmpty(signText.getText().toString())){
				PromptManager.showToast(R.string.sign_text_notice);
				return;
			}
			setSign();
			break;

		default:
			break;
		}
	}
	public void setSign() {
        UserEngineImpl engine = new UserEngineImpl();
        engine.setSettingMessage(signText.getText().toString(), listener);
    }
	private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        };

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                UserEntity ue = DataParse.parseObjectJson(UserEntity.class, json);
                return ue;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(UserEntity entity) {

            // PromptManager.closeProgressDialog();
            if (null != entity) {
            	PromptManager.showToast(R.string.password_setting_name_success);
                finish();
            }
        }
    };
}
