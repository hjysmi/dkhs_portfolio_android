package com.dkhs.portfolio.ui;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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
import com.dkhs.portfolio.utils.SIMCardInfo;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class RLFActivity extends ModelAcitivity implements OnClickListener {
    private Button btn_get_code;
    private Button rlfbutton;
    private EditText code;
    private EditText mobile;
    public static final int REGIST_TYPE = 1001;
    public static final int LOGIN_TYPE = 1002;
    private boolean isLoginByCaptcha = false;

    private static final int GET_CODE_UNABLE = 0;
    private static final int GET_CODE_ABLE = 1;
    private static final int GET_PHONE_NUMBER = 2;
    protected static final String TAG = "RLFActivity";
    public Timer mTimer = new Timer();// 定时器
    private boolean mobileAble = false;
    private boolean codeAble = false;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_CODE_ABLE:
                    btn_get_code.setText(R.string.get_code);
                    btn_get_code.setClickable(true);
                    count = 0;
                    btn_get_code.setBackgroundResource(R.drawable.button_normal_blue);
                    mTimer.cancel();
                    break;
                case GET_CODE_UNABLE:
                    btn_get_code.setText((60 - count) + "秒");
                    break;
                case GET_PHONE_NUMBER:
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        if (phoneNumber.startsWith("+86")) {
                            phoneNumber = phoneNumber.replace("+86", "");
                        }
                        mobile.setText(phoneNumber);
                    }
                    break;
                default:
                    break;
            }
        };
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rlf_layout);
        initViews();
        setListener();
        initData();

    }

    private void showCaptchaLoginDailog() {
        PromptManager.closeProgressDialog();
        isLoginByCaptcha = true;
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        dlg.setCancelable(false);
        Window window = dlg.getWindow();
        window.setContentView(R.layout.captcha_login_dialog_layout);
        Button login = (Button) window.findViewById(R.id.login);
        login.setClickable(true);
        login.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (NetUtil.checkNetWork(getApplicationContext())) {
                    PromptManager.showToast(R.string.logining);
                    engine.login(telephone, verify_code, ConstantValue.IS_CAPTCHA, listener);
                    dlg.dismiss();
                } else {
                    PromptManager.showNoNetWork(getApplicationContext());
                }

            }
        });
        window.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isLoginByCaptcha = false;
                dlg.dismiss();
            }
        });
    }

    private String phoneNumber;

    public void initData() {
        engine = new UserEngineImpl();
        new Thread() {

            @Override
            public void run() {
                SIMCardInfo info = new SIMCardInfo(RLFActivity.this);
                phoneNumber = info.getNativePhoneNumber();
                Log.i(TAG, "运营商是: " + info.getProvidersName());
                handler.sendEmptyMessage(GET_PHONE_NUMBER);
            }
        }.start();
    }

    public void setListener() {
        btn_get_code.setOnClickListener(this);
        rlfbutton.setOnClickListener(this);
        mobile.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mobileAble = false;
                    setRegistAble(false);
                } else {
                    mobileAble = true;
                    if (codeAble) {
                        setRegistAble(true);
                    }
                }
            }
        });
        code.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 6) {
                    setRegistAble(false);
                    codeAble = false;
                } else {
                    codeAble = true;
                    if (mobileAble) {
                        setRegistAble(true);
                    }
                }
            }
        });
    }

    private void setRegistAble(boolean flag) {
        if (flag) {
            rlfbutton.setBackgroundResource(R.drawable.button_normal_blue);
            rlfbutton.setClickable(true);
        } else {
            rlfbutton.setBackgroundResource(R.drawable.button_unable);
            rlfbutton.setClickable(false);
        }
    }

    public void initViews() {
        current_type = getIntent().getIntExtra("activity_type", REGIST_TYPE);
        rlfbutton = (Button) findViewById(R.id.rlbutton);
        if (current_type == REGIST_TYPE) {
            setTitle(R.string.register);
            rlfbutton.setText(R.string.register);
        } else if (current_type == LOGIN_TYPE) {
            setTitle(R.string.forget_password);
            rlfbutton.setText(R.string.login);
        }
        btn_get_code = (Button) findViewById(R.id.button_getcode);
        mobile = (EditText) findViewById(R.id.et_mobile);
        code = (EditText) findViewById(R.id.et_verifycode);
    }

    @Override
    public void onClick(View v) {
        telephone = mobile.getText().toString();
        verify_code = code.getText().toString();
        switch (v.getId()) {
            case R.id.button_getcode:
                if (TextUtils.isEmpty(telephone)) {
                    PromptManager.showToast("手机号码不能为空");
                    return;
                }
                if (!isMobileNO(telephone)) {
                    PromptManager.showToast("请输入正确的手机号码");
                    return;
                }
                if (NetUtil.checkNetWork(this)) {
                    PromptManager.showProgressDialog(RLFActivity.this, R.string.getting_code, false);
                    engine.getVericode(telephone, new ParseHttpListener<Object>() {

                        @Override
                        public void onHttpFailure(int errCode, String errMsg) {
                            super.onHttpFailure(errCode, errMsg);
                            btn_get_code.setClickable(true);
                            btn_get_code.setText(R.string.get_code);
                            count = 0;
                            btn_get_code.setBackgroundResource(R.drawable.button_normal_blue);
                            mTimer.cancel();
                            PromptManager.closeProgressDialog();
                        }

                        @Override
                        protected Object parseDateTask(String jsonData) {
                            return null;
                        }

                        @Override
                        protected void afterParseData(Object object) {
                            PromptManager.closeProgressDialog();
                            PromptManager.showToast(R.string.get_code_success);

                        }
                    });
                    btn_get_code.setClickable(false);
                    btn_get_code.setBackgroundResource(R.drawable.button_unable);
                    btn_get_code.setText("60秒");
                    if (mTimer != null) {
                        mTimer = null;
                    }
                    mTimer = new Timer();
                    timerTask();
                } else {
                    PromptManager.showNoNetWork(this);
                }
                break;
            case R.id.rlbutton:
                if (!isMobileNO(telephone)) {
                    PromptManager.showToast("请输入正确的手机号码");
                    return;
                }
                if (TextUtils.isEmpty(verify_code)) {
                    PromptManager.showToast("请输入验证码");
                    return;
                }
                if (NetUtil.checkNetWork(this)) {
                    if (current_type == REGIST_TYPE) {
                        engine.register(telephone, verify_code, listener);
                        PromptManager.showProgressDialog(this, "正在注册...", false);
                    } else if (current_type == LOGIN_TYPE) {
                        engine.login(telephone, verify_code, ConstantValue.IS_CAPTCHA, listener);
                        PromptManager.showProgressDialog(this, "正在登录...", false);
                    }
                } else {
                    PromptManager.showNoNetWork(this);
                }
                break;

            default:
                break;
        }
    }

    private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        public void onFailure(int errCode, String errMsg) {
            isLoginByCaptcha = false;
            PromptManager.closeProgressDialog();
            super.onFailure(errCode, errMsg);
        };

        public void onHttpSuccess(String result) {
            if (result.startsWith("{")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.has("errors")) {
                        JSONObject errors = jsonObject.getJSONObject("errors");
                        if (errors.has("mobile_registered")) {
                            showCaptchaLoginDailog();
                        } else {
                            onFailure(777, result);
                        }
                        return;
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            onSuccess(result);
        };

        @Override
        protected UserEntity parseDateTask(String jsonData) {
            try {
                JSONObject json = new JSONObject(jsonData);
                UserEntity entity = DataParse.parseObjectJson(UserEntity.class, json.getJSONObject("user"));
                String token = (String) json.getJSONObject("token").get("access_token");
                entity.setAccess_token(token);
                entity.setMobile(telephone);
                GlobalParams.ACCESS_TOCKEN = entity.getAccess_token();
                GlobalParams.MOBILE = telephone;
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
            if (current_type == REGIST_TYPE) {
                if (isLoginByCaptcha) {
                    Intent intent = new Intent(RLFActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RLFActivity.this, RegisterSuccessActivity.class);
                    intent.putExtra("username", entity.getUsername());
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(RLFActivity.this, SetPasswordActivity.class);
                intent.putExtra("verify_code", verify_code);
                intent.putExtra("type", SetPasswordActivity.LOGIN_TYPE);
                startActivity(intent);
            }
            finish();
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

    private int count = 0;
    /**
     * 验证码
     */
    private String verify_code;
    private String telephone;
    private int current_type;
    private View rl_inviter;
    private UserEngineImpl engine;

    private void timerTask() {
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (count < 60) {
                    handler.sendEmptyMessage(GET_CODE_UNABLE);
                    count++;
                } else {
                    handler.sendEmptyMessage(GET_CODE_ABLE);
                }
            }
        }, 0, 1000);
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
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public void onBackPressed() {
        // PromptManager.closeProgressDialog();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
