package com.dkhs.portfolio.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;

public class UserNameChangeActivity extends ModelAcitivity implements OnClickListener{
	private Button btnCancle;
	private Button btnSave;
	private EditText changeEditName;
	private UserEngineImpl mUserEngineImpl;
	private Context context;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_change_username);
		context = this;
		initView();
		setListener();
		mUserEngineImpl = new UserEngineImpl();
	}
	private void initView(){
		setTitle(getResources().getString(R.string.change_usesr_name_title));
		changeEditName = (EditText) findViewById(R.id.change_edit_name);
		btnCancle = getBtnBack();
		btnSave = getRightButton();
		
		btnCancle.setText("取消");
		btnCancle.setBackgroundResource(R.drawable.white_black_selector);
		btnCancle.setCompoundDrawables(null, null, null, null);
		btnSave.setText("保存");
		btnSave.setBackgroundResource(R.drawable.white_black_selector);
		changeEditName.setText(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME));
		changeEditName.setSelection(changeEditName.length());
	}
	private void setListener(){
		btnSave.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_right:
			/*String userName = changeEditName.getText().toString();
			if(TextUtils.isEmpty(userName)){
				PromptManager.showToast(R.string.password_setting_name_null);
				return;
			}*/
			String userName = changeEditName.getText().toString();
			if(checkUserName()){
				mUserEngineImpl.setUserName(userName, listener);
				listener.setLoadingDialog(context).beforeRequest();
			}
			break;

		default:
			break;
		}
	}
	private boolean checkUserName() {
        boolean isValid = true;
        // etUserName.
        String text = changeEditName.getText().toString();
        if (TextUtils.isEmpty(text)) {
            isValid = false;
            changeEditName.setError(Html.fromHtml("<font color='red'>用户名不能为空</font>"));
            changeEditName.requestFocus();
        } else if (StringFromatUtils.getStringRealLength(text) < 4) {
            isValid = false;
            changeEditName.setError(Html.fromHtml("<font color='red'>用户名不能小于4个字符</font>"));
            changeEditName.requestFocus();
        }
        return isValid;
    }
	private ParseHttpListener<String> listener = new ParseHttpListener<String>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        };

        @Override
        protected String parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                return json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(String entity) {

            // PromptManager.closeProgressDialog();
            if (null != entity) {
            	PromptManager.showToast(R.string.password_setting_name_success);
            	PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USERNAME,changeEditName.getText().toString());
            	Intent intent=new Intent();
    			setResult(RESULT_OK, intent);
    			finish();
            }
        }
    };
}
