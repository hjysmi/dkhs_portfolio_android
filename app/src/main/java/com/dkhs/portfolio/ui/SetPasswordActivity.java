package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.SIMCardInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class SetPasswordActivity extends ModelAcitivity implements OnClickListener {

    // public static final int SET_PASSWORD_TYPE = 1001;
    // public static final int LOGIN_TYPE = 1002;
    // public static final int LOGOUT_TYPE = 1003;
    //
    // public Timer mTimer;// 定时器
    // private static final int GET_CODE_UNABLE = 0;
    // private static final int GET_CODE_ABLE = 1;
    // private static final int SET_PASSWORD_MODE = 2001;
    // private static final int CHANGE_PASSWORD_MODE = 2002;
    // private static final int GET_VERICODE_MODE = 2003;
    // private static int curtMode = 0;
    //
    // protected static final String TAG = "SetPasswordActivity";
    // private int count;
    // @SuppressLint("HandlerLeak")
    // private Handler handler = new Handler() {
    //
    // public void handleMessage(Message msg) {
    // switch (msg.what) {
    // case GET_CODE_ABLE:
    // btn_get_code.setText(R.string.get_code);
    // btn_get_code.setClickable(true);
    // count = 0;
    // btn_get_code.setBackgroundResource(R.drawable.button_normal_blue);
    // mTimer.cancel();
    // break;
    // case GET_CODE_UNABLE:
    // btn_get_code.setText((60 - count) + "秒");
    // break;
    // default:
    // break;
    // }
    // };
    // };
    private EditText etPassword;
    private Button rlfbutton;


    private UserEngineImpl engine;

    private String phoneNum;
    private String code;

    private String strBefore;

    public static Intent newIntent(Context context, String phoneNum, String code) {
        Intent intent = new Intent(context, SetPasswordActivity.class);
        intent.putExtra(SettingNameActivity.EXTRA_PHONENUM, phoneNum);
        intent.putExtra(SettingNameActivity.EXTRA_CODE, code);
        return intent;
    }

    private void handleExtras(Bundle extras) {

        phoneNum = extras.getString(SettingNameActivity.EXTRA_PHONENUM);
        code = extras.getString(SettingNameActivity.EXTRA_CODE);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpassword);
        engine = new UserEngineImpl();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        setTitle("设置新密码");
        initViews();

    }

    /**
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void initViews() {
        rlfbutton = (Button) findViewById(R.id.rlbutton);
        rlfbutton.setOnClickListener(this);
        rlfbutton.setText(R.string.confirm);
        rlfbutton.setEnabled(false);
        etPassword = (EditText) findViewById(R.id.et_password);
        CheckBox cbShowPassword = (CheckBox) findViewById(R.id.cb_show_psw);

        cbShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else { // 隐藏密码
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }
                int textEnd = etPassword.getText().length();
                etPassword.setSelection(textEnd);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 在文本变化之前先获取到文本值
                strBefore = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6) {
                    rlfbutton.setEnabled(true);
                } else {
                    rlfbutton.setEnabled(false);

                }
                // 一定要加上此判断，否则会进入死循环
//                if (s.toString().equals(strBefore)) {
//                    return;
//                }
//                int editStart = etPassword.getSelectionStart();
//                // 注意：这里一定要用偏移量，而不是写死用1，因为要考虑到复制粘贴的情况下，不一定是一个个输入的
//                int offset = s.toString().length() - strBefore.length();
//                if (StringFromatUtils.isCN(s.toString())) {// 判断是否为中文符号，是中文符号则剪掉偏移量，再赋值
//                    PromptManager.showToast("不可以输入中文符号");
//                    s.delete(s.toString().length() - offset, s.toString().length());
//                    int tempSelection = editStart - offset;
//                    etPassword.setText(s);
//                    etPassword.setSelection(tempSelection);
//
//                } else {
//                    strBefore = s.toString();// 不是中文符号则进行赋值
//                    etPassword.setText(s);
//                    etPassword.setSelection(editStart);
//                }

                // if (s.length() > 5) {
                //
                // isPswAble = true;
                // } else {
                // isPswAble = false;
                // }
                // setRegistAble();
            }
        });

    }

    @Override
    public void onClick(View v) {
        String psw = etPassword.getText().toString();
        if (v.getId() == R.id.rlbutton) {
            if (TextUtils.isEmpty(psw)) {
                etPassword.requestFocus();
                etPassword.setError(Html.fromHtml("<font color='red'>密码不能为空</font>"));
                return;
            }
            if (psw.length() < 6) {
                etPassword.requestFocus();
                etPassword.setError(Html.fromHtml("<font color='red'>密码不能小于6位</font>"));
                return;
            }
            engine.setPassword(phoneNum, etPassword.getText().toString(), code,
                    setPasswordListener.setLoadingDialog(this, "正在修改", false));
        }

    }

    private ParseHttpListener setPasswordListener = new ParseHttpListener() {

        @Override
        public void onSuccess(String result) {
            engine.login(phoneNum, etPassword.getText().toString(), ConstantValue.IS_MOBILE, listener.setLoadingDialog(SetPasswordActivity.this, false));

        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);

        }

        @Override
        protected Object parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            // TODO Auto-generated method stub

        }

    };
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_resetting_password);


    private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        // public void onHttpFailure(int errCode, String errMsg) {
        // PromptManager.closeProgressDialog();
        // super.onHttpFailure(errCode, errMsg);
        // };

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            PromptManager.closeProgressDialog();
            startActivity(LoginActivity.getLoginActivity(SetPasswordActivity.this, phoneNum));
            finish();
        }


        @Override
        protected UserEntity parseDateTask(String jsonData) {
            if (TextUtils.isEmpty(jsonData)) {
                return null;
            }
            try {
                JSONObject json = new JSONObject(jsonData);
                UserEntity entity = DataParse.parseObjectJson(UserEntity.class, json.getJSONObject("user"));
                String token = (String) json.getJSONObject("token").get("access_token");
                entity.setAccess_token(token);
                if (SIMCardInfo.isMobileNO(phoneNum)) {
                    GlobalParams.MOBILE = phoneNum;
                    entity.setMobile(phoneNum);
                }
                engine.saveLoginUserInfo(entity);
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT, phoneNum);
                return entity;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(UserEntity entity) {
            if (null != entity) {
                PromptManager.closeProgressDialog();
                PortfolioApplication.getInstance().exitApp();
                PortfolioApplication.getInstance().setLogin(true);
                goMainPage();
            }
        }
    };

    private void goMainPage() {

        // 设置小红点可以出现
        PortfolioApplication.getInstance().exitApp();
        Intent intent = new Intent(SetPasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_SetPassword;
    }
}
