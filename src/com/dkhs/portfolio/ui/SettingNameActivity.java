package com.dkhs.portfolio.ui;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class SettingNameActivity extends ModelAcitivity implements OnClickListener {
    private CheckBox cbShowPassword;
    private CheckBox cbShowRePassword;
    private EditText etPassword;
    private EditText etRePassword;
    private EditText etUserName;
    public static final String EXTRA_PHONENUM = "extra_phone";
    public static final String EXTRA_CODE = "extra_code";
    public static final String EXTRA_ISRESETPSW = "extra_isresetpsw";
    private String phoneNum;
    private String code;
    private Button rlfbutton;

    private UserEngineImpl engine;
    private boolean isResetPsw;

    public static Intent newIntent(Context context, String phoneNum, String code, boolean resetPsw) {
        Intent intent = new Intent(context, SettingNameActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_ISRESETPSW, resetPsw);
        intent.putExtra(EXTRA_CODE, code);
        return intent;
    }

    private void handleExtras(Bundle extras) {

        phoneNum = extras.getString(EXTRA_PHONENUM);
        code = extras.getString(EXTRA_CODE);
        isResetPsw = extras.getBoolean(EXTRA_ISRESETPSW);

    }

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
        setRegistAble();
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

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5) {

                    isPswAble = true;
                } else {
                    isPswAble = false;
                }
                setRegistAble();
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
                if (s.length() > 5 && etPassword.getText().toString().equals(etRePassword.getText().toString())) {

                    isRePswAble = true;
                } else {
                    isRePswAble = false;
                }
                setRegistAble();
            }
        });
        etUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = StringFromatUtils.getStringRealLength(s.toString());
                if (length >= 6 && length <= 20) {

                    isUserNameAble = true;
                } else {
                    isUserNameAble = false;
                }
                setRegistAble();
            }
        });

        if (isResetPsw) {
            setTitle("修改密码");
            etUserName.setVisibility(View.GONE);
            findViewById(R.id.rl_repassword).setVisibility(View.VISIBLE);

        } else {
            setTitle("设置昵称");

        }

    }

    private boolean isUserNameAble;
    private boolean isPswAble;
    private boolean isRePswAble;

    private void setRegistAble() {
        if (isResetPsw) {
            if (isRePswAble && isPswAble) {
                rlfbutton.setEnabled(true);
            } else {
                rlfbutton.setEnabled(false);
            }
        } else {

            if (isUserNameAble && isPswAble) {
                rlfbutton.setEnabled(true);
            } else {
                rlfbutton.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlbutton) {
            if (isResetPsw) {
                if (!etPassword.getText().toString().equals(etRePassword.getText().toString())) {
                    etPassword.requestFocus();
                    etPassword.setError(Html.fromHtml("<font color='red'>两次密码输入不一致</font>"));
                    return;
                } else {
                    engine.setPassword(phoneNum, etRePassword.getText().toString(), code,
                            setPasswordListener.setLoadingDialog(this, "正在修改", false));
                }
            } else {

                engine.register(phoneNum, etPassword.getText().toString(), code, etUserName.getText().toString(),
                        registerListener.setLoadingDialog(this, "正在注册", false));
            }
        }
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
                GlobalParams.ACCESS_TOCKEN = entity.getAccess_token();
                GlobalParams.MOBILE = phoneNum;
                saveUser(entity);
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

                Intent intent = new Intent(SettingNameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private void saveUser(final UserEntity user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                UserEntity entity = UserEntityDesUtil.decode(user, "DECODE", ConstantValue.DES_PASSWORD);
                DbUtils dbutil = DbUtils.create(PortfolioApplication.getInstance());
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
}
