package com.dkhs.portfolio.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;

/**
 * 密码设置
 *
 * @author weiting
 */
public class ModifyPswActivity extends ModelAcitivity implements OnClickListener {
    private EditText passwordSettingOld;
    private EditText passwordSettingNew;
    private CheckBox passwordSettingCheck;
    // private Button btnCancle;
    private TextView btnSave;
    private static final int CHANGE_PASSWORD_MODE = 2002;
    private Context context;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_settingpassword_onsetting);
        context = this;
        initView();
        setListener();
    }

    private void initView() {
        setTitle(getResources().getString(R.string.password_setting_title));
        passwordSettingOld = (EditText) findViewById(R.id.password_setting_old);
        passwordSettingNew = (EditText) findViewById(R.id.password_setting_new);
        passwordSettingCheck = (CheckBox) findViewById(R.id.password_setting_checkbox);
        // btnCancle = getBtnBack();
        btnSave = getRightButton();

        // btnCancle.setText("取消");
        // btnCancle.setBackgroundDrawable(null);
        // btnCancle.setCompoundDrawables(null, null, null, null);
        btnSave.setText("保存");
        btnSave.setBackgroundDrawable(null);
    }

    private void setListener() {
        passwordSettingCheck.setOnCheckedChangeListener(checkBox_Listener);
        btnSave.setOnClickListener(this);
    }

    CheckBox.OnCheckedChangeListener checkBox_Listener = new CheckBox.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (passwordSettingCheck.isChecked()) {
                passwordSettingOld.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                Editable etable = passwordSettingOld.getText();
                Selection.setSelection(etable, etable.length());
                passwordSettingNew.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                Editable etable2 = passwordSettingNew.getText();
                Selection.setSelection(etable2, etable2.length());
            } else {
                passwordSettingOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                Editable etable = passwordSettingOld.getText();
                Selection.setSelection(etable, etable.length());
                passwordSettingNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                Editable etable2 = passwordSettingNew.getText();
                Selection.setSelection(etable2, etable2.length());

            }

        }

    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_right:
                String oldPassword = passwordSettingOld.getText().toString();
                String newPassword = passwordSettingNew.getText().toString();
                if (TextUtils.isEmpty(oldPassword)) {
                    PromptManager.showToast(R.string.password_setting_old);
                    return;
                }
                if (TextUtils.isEmpty(newPassword)) {
                    PromptManager.showToast(R.string.password_setting_new);
                    return;
                }
                if (oldPassword.length() > 5 && newPassword.length() > 5) {
                    UserEngineImpl mUserEngineImpl = new UserEngineImpl();
                    listener.setLoadingDialog(context);
                    mUserEngineImpl.changePassword(oldPassword, newPassword, listener);
                } else {
                    PromptManager.showToast(R.string.password_setting_more);
                }
                break;

            default:
                break;
        }
    }

    private ParseHttpListener<Object> listener = new ParseHttpListener<Object>() {

        public void onHttpFailure(int errCode, String errMsg) {
            PromptManager.closeProgressDialog();
            super.onHttpFailure(errCode, errMsg);
        }

        ;

        public void onFailure(int errCode, String errMsg) {
            PromptManager.closeProgressDialog();
            super.onFailure(errCode, errMsg);
        }

        ;

        @Override
        protected Object parseDateTask(String jsonData) {
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            PromptManager.closeProgressDialog();
            PromptManager.showEditSuccessToast();
            finish();
        }
    };
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_change_password);

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_modifypsw;
    }
}
