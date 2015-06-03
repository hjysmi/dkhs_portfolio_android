package com.dkhs.portfolio.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.umeng.analytics.MobclickAgent;

public class SettingNameActivity extends ModelAcitivity implements OnClickListener {
    private CheckBox cbShowPassword;
    private CheckBox cbShowRePassword;
    private EditText etPassword;
    private EditText etRePassword;
    private EditText etUserName;
    public static final String EXTRA_PHONENUM = "extra_phone";
    public static final String EXTRA_CODE = "extra_code";
    public static final String EXTRA_ISRESETPSW = "extra_isresetpsw";
    public static final String EXTRA_ISSETPSW = "extra_issetpsw";
    private String phoneNum;
    private String code;
    private Button rlfbutton;

    private UserEngineImpl engine;
    private boolean isResetPsw;
    private boolean isSetPsw;

    public static Intent newIntent(Context context, String phoneNum, String code, boolean resetPsw) {
        Intent intent = new Intent(context, SettingNameActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_ISRESETPSW, resetPsw);
        intent.putExtra(EXTRA_CODE, code);
        return intent;
    }

    public static Intent newSetPSWIntent(Context context, String phoneNum, String code) {
        Intent intent = new Intent(context, SettingNameActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_ISSETPSW, true);
        intent.putExtra(EXTRA_CODE, code);
        return intent;
    }

    private void handleExtras(Bundle extras) {

        phoneNum = extras.getString(EXTRA_PHONENUM);
        code = extras.getString(EXTRA_CODE);
        isResetPsw = extras.getBoolean(EXTRA_ISRESETPSW);
        isSetPsw = extras.getBoolean(EXTRA_ISSETPSW);

    }

    String strBefore;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        engine = new UserEngineImpl();
        rlfbutton = (Button) findViewById(R.id.rlbutton);
        rlfbutton.setOnClickListener(this);
        rlfbutton.setEnabled(false);
        cbShowPassword = (CheckBox) findViewById(R.id.cb_show_psw);
        cbShowRePassword = (CheckBox) findViewById(R.id.cb_show_repsw);

        etPassword = (EditText) findViewById(R.id.et_password);
        etRePassword = (EditText) findViewById(R.id.et_repassword);
        etUserName = (EditText) findViewById(R.id.et_username);

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
        }

        );
        cbShowRePassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 显示密码
                    etRePassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else { // 隐藏密码
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }

            }
        }

        );

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
                if (isSetPsw) {
                    if (s.length() > 0) {
                        rlfbutton.setEnabled(true);
                    } else {
                        rlfbutton.setEnabled(false);

                    }
                } else {

                    if (s.length() > 0 && etUserName.getText().length() > 0) {
                        rlfbutton.setEnabled(true);
                    } else {
                        rlfbutton.setEnabled(false);

                    }
                }
                // 一定要加上此判断，否则会进入死循环
                if (s.toString().equals(strBefore)) {
                    return;
                }
                int editStart = etPassword.getSelectionStart();
                // 注意：这里一定要用偏移量，而不是写死用1，因为要考虑到复制粘贴的情况下，不一定是一个个输入的
                int offset = s.toString().length() - strBefore.length();
                if (StringFromatUtils.isCN(s.toString())) {// 判断是否为中文符号，是中文符号则剪掉偏移量，再赋值
                    PromptManager.showToast("不可以输入中文符号");
                    s.delete(s.toString().length() - offset, s.toString().length());
                    int tempSelection = editStart - offset;
                    etPassword.setText(s);
                    etPassword.setSelection(tempSelection);

                } else {
                    strBefore = s.toString();// 不是中文符号则进行赋值
                    etPassword.setText(s);
                    etPassword.setSelection(editStart);
                }

                // if (s.length() > 5) {
                //
                // isPswAble = true;
                // } else {
                // isPswAble = false;
                // }
                // setRegistAble();
            }
        });
        etRePassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if (s.length() > 5 && etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                //
                // isRePswAble = true;
                // } else {
                // isRePswAble = false;
                // }
                // setRegistAble();
            }
        });
        etUserName.addTextChangedListener(new TextWatcher() {

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

                if (s.length() > 0 && etPassword.getText().length() > 0) {
                    rlfbutton.setEnabled(true);
                } else {
                    rlfbutton.setEnabled(false);

                }

                // 一定要加上此判断，否则会进入死循环
                if (s.toString().equals(strBefore)) {
                    return;
                }

                // int editStart = etPassword.getSelectionStart();
                // // 注意：这里一定要用偏移量，而不是写死用1，因为要考虑到复制粘贴的情况下，不一定是一个个输入的
                // int offset = s.toString().length() - strBefore.length();
                //
                // // int length = StringFromatUtils.getStringRealLength(s.toString());
                // String inputText = s.toString();
                // inputText = inputText.replaceAll(StringFromatUtils.regexUsername, "");
                // System.out.println("input text:" + inputText);
                // if (!TextUtils.isEmpty(inputText)) {
                //
                // etUserName.setText(inputText);
                // etUserName.setSelection(inputText.length());
                // }
                // if (length >= 6 && length <= 20) {
                //
                // isUserNameAble = true;
                // } else {
                // isUserNameAble = false;
                // }
                // setRegistAble();
            }
        });

        if (isResetPsw) {
            setTitle("修改密码");
            etUserName.setVisibility(View.GONE);
            findViewById(R.id.rl_repassword).setVisibility(View.VISIBLE);

        } else if (isSetPsw) {
            setTitle("设置密码");
            findViewById(R.id.ll_name).setVisibility(View.GONE);
            TextView tvPsw = (TextView) findViewById(R.id.tv_psw);
            tvPsw.setText("密码");

        } else {
            setTitle("设置昵称和密码");
            rlfbutton.setText("完成");

        }

    }

    // private boolean isUserNameAble;
    // private boolean isPswAble;
    // private boolean isRePswAble;
    //
    // private void setRegistAble() {
    // if (isResetPsw) {
    // if (isRePswAble && isPswAble) {
    // rlfbutton.setEnabled(true);
    // } else {
    // rlfbutton.setEnabled(false);
    // }
    // } else {
    //
    // if (isUserNameAble && isPswAble) {
    // rlfbutton.setEnabled(true);
    // } else {
    // rlfbutton.setEnabled(false);
    // }
    // }
    // }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlbutton) {
            if (isSetPsw) {
                if (!checkPassword()) {
                    return;
                }
                setPassword();
                // engine.register(phoneNum, etPassword.getText().toString(), code,
                // PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_USERNAME),
                // registerListener.setLoadingDialog(this, "正在绑定手机号", false));

            } else {

                if (!checkUserName() || !checkPassword()) {
                    return;
                }
                if (isResetPsw) {
                    if (!etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                        etPassword.requestFocus();
                        etPassword.setError(Html.fromHtml("<font color='red'>两次密码输入不一致</font>"));
                        return;
                    } else {

                    }
                } else {
                    engine.register(phoneNum, etPassword.getText().toString(), code, etUserName.getText().toString(),
                            registerListener.setLoadingDialog(this, "正在注册", false));
                }

            }
        }
    }

    private void setPassword() {
        engine.setPassword(phoneNum, etPassword.getText().toString(), code, new ParseHttpListener<Object>() {

            @Override
            protected Object parseDateTask(String jsonData) {
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_USER_ACCOUNT, phoneNum);
                return null;
            }

            @Override
            protected void afterParseData(Object object) {

                setResult(RESULT_OK, new Intent());
                finish();

            }
        }.setLoadingDialog(this, "正在绑定手机号", false));

    }

    private boolean checkUserName() {
        boolean isValid = true;
        // etUserName.
        String text = etUserName.getText().toString();
        text = text.replaceAll(" ", "");
        etUserName.setText(text);
        if (TextUtils.isEmpty(text)) {
            isValid = false;
            etUserName.setError(Html.fromHtml("<font color='red'>昵称不能为空</font>"));
            etUserName.requestFocus();
        } else if (StringFromatUtils.getStringRealLength(text) < 4) {
            isValid = false;
            etUserName.setError(Html.fromHtml("<font color='red'>昵称不能小于4个字符</font>"));
            etUserName.requestFocus();
        }
        return isValid;
    }

    private boolean checkPassword() {
        boolean isValid = true;
        String psw = etPassword.getText().toString();
        if (TextUtils.isEmpty(psw)) {
            isValid = false;
            etPassword.setError(Html.fromHtml("<font color='red'>密码不能为空</font>"));
            etPassword.requestFocus();
        } else if (StringFromatUtils.getStringRealLength(psw) < 6) {
            isValid = false;
            etPassword.setError(Html.fromHtml("<font color='red'>密码不能小于6个字符</font>"));
            etPassword.requestFocus();
        }
        return isValid;

    }

    private ParseHttpListener setPasswordListener = new ParseHttpListener() {

        @Override
        public void onSuccess(String result) {
            startActivity(LoginActivity.getLoginActivity(SettingNameActivity.this, phoneNum));
            finish();

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

        };
    };
    private ParseHttpListener<UserEntity> registerListener = new ParseHttpListener<UserEntity>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        };

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                UserEntity entity = DataParse.parseObjectJson(UserEntity.class, json.getJSONObject("user"));
                String token = (String) json.getJSONObject("token").get("access_token");
                entity.setAccess_token(token);
                entity.setMobile(phoneNum);
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

            // PromptManager.closeProgressDialog();
            if (null != entity) {

                uploadUserFollowStock();
            }
        }
    };
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_setting_password);

    ParseHttpListener uploadStockListner = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            new VisitorDataEngine().delAllOptionalStock();
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
            uploadUserFollowCombination();

        }
    }.setLoadingDialog(this, "正在注册", false);

    private VisitorDataEngine visitorEngine;

    public void uploadUserFollowStock() {
        if (null == visitorEngine) {
            visitorEngine = new VisitorDataEngine();
        }
        if (!visitorEngine.uploadUserFollowStock(uploadStockListner)) {
            uploadUserFollowCombination();
        }

    }

    ParseHttpListener uploadCombinationListener = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            new VisitorDataEngine().delAllCombinationBean();
            return null;
        }

        @Override
        protected void afterParseData(Object object) {

            Log.i("uploadUserFollowCombination", "uploadUserFollowCombination success");
            goMainPage();

        }
    }.setLoadingDialog(this, "正在注册", false);

    public void uploadUserFollowCombination() {
        if (!visitorEngine.uploadUserFollowCombination(uploadCombinationListener)) {
            goMainPage();
        }
    }

    private void goMainPage() {
        if (isSetPsw) {
            finish();
        } else {
            PortfolioApplication.getInstance().exitApp();
            Intent intent = new Intent(SettingNameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }
}
