package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.Action1;
import com.dkhs.portfolio.engine.AdEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.security.SecurityUtils;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.SIMCardInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

public class RLFActivity extends ModelAcitivity implements OnClickListener {
    // private Button btn_get_code;
    private Button rlfbutton;
    // private EditText code;
    private EditText etPhoneNum;
    private CheckBox cbAgree;
    private TextView mAdTV;

    public static final int REGIST_TYPE = 1001;
    public static final int FORGET_PSW_TYPE = 1002;
    public static final int SETTING_PASSWORD_TYPE = 1003;
    public static final int REGIST_THREE_PLAT = 1004;
    private boolean isLoginByCaptcha = false;

    private static final int GET_CODE_UNABLE = 0;
    private static final int GET_CODE_ABLE = 1;
    private static final int GET_PHONE_NUMBER = 2;
    protected static final String TAG = "RLFActivity";
    public Timer mTimer = new Timer();// 定时器
    private boolean mobileAble = false;
    private boolean codeAble = false;
    private TextView rltAgreement;
    private TextView tvHint;

    private boolean isSettingPsw;
    private boolean isRegisterThreePlatform;
    private String name;
    public static final String EXTRA_SETTING_PASSWORD = "extra_setting_password";
    public static final String EXTRA_ACTIVITY_TYPE = "activity_type";
    public static final String EXTRA_REGISTER_THREE_PLATFORM = "register_three_platform";
    public static final String EXTRA_FORGET_PSW = "extra_forget_psw";
    public static final String EXTRA_NAME = "extra_name";

    public static final int REQUESTCODE_SET_PASSWROD = 999;
    public static final int REQUEST_BOUND_THREE_PLATFORM = 2;


    public static Intent bindPhoneIntent(Context context) {
        Intent intent = new Intent(context, RLFActivity.class);
        intent.putExtra(EXTRA_SETTING_PASSWORD, true);
        intent.putExtra(EXTRA_ACTIVITY_TYPE, SETTING_PASSWORD_TYPE);
        return intent;
    }

    public static Intent registerThreePlatform(Context context, String name) {
        Intent intent = new Intent(context, RLFActivity.class);
        intent.putExtra(EXTRA_SETTING_PASSWORD, true);
        intent.putExtra(EXTRA_REGISTER_THREE_PLATFORM, true);
        intent.putExtra(EXTRA_ACTIVITY_TYPE, REGIST_THREE_PLAT);
        intent.putExtra(EXTRA_NAME, name);
        return intent;
    }

    public static Intent registerIntent(Context context) {
        Intent intent = new Intent(context, RLFActivity.class);
        intent.putExtra(EXTRA_ACTIVITY_TYPE, REGIST_TYPE);
        return intent;
    }

    public static Intent forgetPswIntent(Context context) {
        Intent intent = new Intent(context, RLFActivity.class);
        intent.putExtra(EXTRA_SETTING_PASSWORD, true);
        intent.putExtra(EXTRA_ACTIVITY_TYPE, FORGET_PSW_TYPE);
        return intent;
    }
//    public static Intent bindPhoneIntent(Context context) {
//        Intent intent = new Intent(context, RLFActivity.class);
//        intent.putExtra(EXTRA_SETTING_PASSWORD, true);
//        intent.putExtra(EXTRA_ACTIVITY_TYPE, SETTING_PASSWORD_TYPE);
//        return intent;
//    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlf_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initViews();
        setListener();
        initData();
        initLink();
        if (!isSettingPsw) {
            getSignUpInfo();
        }
    }

    private void getSignUpInfo() {
        AdEngineImpl.getSignUp(new Action1<AdBean>() {
            @Override
            public void call(AdBean adBean) {
                if (adBean != null) {
                    updateSignUp(adBean);
                }
            }
        });
    }


    private void updateSignUp(AdBean o) {
        mAdTV = (TextView) findViewById(R.id.ad);
        if (o.getAds().size() > 0) {
            AdBean.AdsEntity adsEntity = o.getAds().get(0);

            if (!TextUtils.isEmpty(adsEntity.getTitle())) {
                mAdTV.setVisibility(View.VISIBLE);
                mAdTV.setText(adsEntity.getTitle());
            }
        }

    }

    private void handleExtras(Bundle extras) {
        isSettingPsw = extras.getBoolean(EXTRA_SETTING_PASSWORD);
        current_type = getIntent().getIntExtra(EXTRA_ACTIVITY_TYPE, REGIST_TYPE);
        isRegisterThreePlatform = extras.getBoolean(EXTRA_REGISTER_THREE_PLATFORM);
        name = extras.getString(EXTRA_NAME);
    }

    private void showCaptchaLoginDailog() {
        PromptManager.closeProgressDialog();
        isLoginByCaptcha = true;
        final MAlertDialog dpg = PromptManager.getAlertDialog(this);
        dpg.setCancelable(false);
        dpg.setMessage(R.string.register_already);
        dpg.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dpg.dismiss();
                // startActivity(LoginActivity.getLoginActivity(RLFActivity.this, etPhoneNum.getText().toString()));
                finish();
            }
        });
        dpg.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isLoginByCaptcha = false;
                dpg.dismiss();
            }
        });
        dpg.show();

    }

    private void showHasBindnDailog() {
        PromptManager.closeProgressDialog();
        final MAlertDialog dpg = PromptManager.getAlertDialog(this);
        dpg.setCancelable(false);
        dpg.setMessage(R.string.has_bind_message);
        dpg.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dpg.dismiss();
                // startActivity(LoginActivity.getLoginActivity(RLFActivity.this, etPhoneNum.getText().toString()));
//                finish();
            }
        });
//        dpg.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dpg.dismiss();
//            }
//        });
        dpg.show();

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
                // handler.sendEmptyMessage(GET_PHONE_NUMBER);
            }
        }.start();
    }

    public void setListener() {
        // btn_get_code.setOnClickListener(this);
        rlfbutton.setOnClickListener(this);
        etPhoneNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mobileAble = SIMCardInfo.isMobileNO(s.toString());

                setRegistAble();
            }
        });

    }

    private void setRegistAble() {
        if (cbAgree.isChecked() && etPhoneNum.getText().length() > 0) {
            rlfbutton.setEnabled(true);
        } else {
            rlfbutton.setEnabled(false);
        }
    }

    public void initViews() {

        rlfbutton = (Button) findViewById(R.id.rlbutton);
        etPhoneNum = (EditText) findViewById(R.id.et_mobile);
        rltAgreement = (TextView) findViewById(R.id.rlt_agreement);
        tvHint = (TextView) findViewById(R.id.tv_hint);
        rlfbutton.setEnabled(false);
        // code = (EditText) findViewById(R.id.et_verifycode);
        // tvMessage = (TextView) findViewById(R.id.tv_agree_info);
        cbAgree = (CheckBox) findViewById(R.id.cb_agree);
        setRegistAble();

        if (current_type == REGIST_TYPE) {
            setTitle("填写手机号");
            rlfbutton.setText(R.string.register);
            tvHint.setVisibility(View.INVISIBLE);
        } else if (current_type == FORGET_PSW_TYPE) {
            setTitle(R.string.write_phone);
            rlfbutton.setText("下一步");
//            rlfbutton.setText(R.string.confirm);
            cbAgree.setVisibility(View.GONE);
            rltAgreement.setVisibility(View.INVISIBLE);
            tvHint.setVisibility(View.INVISIBLE);


        } else if (current_type == SETTING_PASSWORD_TYPE) {
            setTitle("绑定手机号");
            rlfbutton.setText("下一步");
            tvHint.setVisibility(View.VISIBLE);
        } else if (current_type == REGIST_THREE_PLAT) {
            setTitle(R.string.verify_phone);
            rlfbutton.setText("下一步");
            tvHint.setVisibility(View.VISIBLE);
            cbAgree.setVisibility(View.GONE);
            rltAgreement.setVisibility(View.INVISIBLE);
        }


        cbAgree.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setRegistAble();

            }
        });
    }

    /**
     * 提示用户默认是同意服务条款和隐私政策
     */
    // private TextView tvMessage;

    private static final String str = "我已阅读并同意";
    private static final String str2 = "《多快好省服务协议》";

    private void initLink() {
        // 跳转隐私政策
        // Intent intent1 = new Intent(this, ActivityTermsPrivate.class);
        // intent1.putExtra(ActivityTermsPrivate.TYPE, ActivityTermsPrivate.TYPE_POLICY);
        // // 跳转服务条款
        // Intent intent = new Intent(this, .class);
        // intent2.putExtra(ActivityTermsPrivate.TYPE, ActivityTermsPrivate.TYPE_TERMS);

        /*
         * SpannableStringBuilder sp = new SpannableStringBuilder();
         * sp.append(str2);
         * 
         * sp.setSpan(new TextViewClickableSpan(getResources().getColor(R.drawable.agreement_color_selector), this,
         * null), 0,
         * str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
         */
        cbAgree.setText(str);
        // 设置TextView可点击
        cbAgree.setMovementMethod(LinkMovementMethod.getInstance());
        rltAgreement.setText(str2);
        rltAgreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        telephone = etPhoneNum.getText().toString();
        // verify_code = code.getText().toString();
        switch (v.getId()) {

            case R.id.rlbutton:
                if (!isValidPhoneNum()) {
                    return;
                }
                // if (TextUtils.isEmpty(verify_code)) {
                // code.requestFocus();
                // return;
                // }

                // if (!cbAgree.isChecked() && current_type == REGIST_TYPE) {
                // PromptManager.showToast("请同意服务协议");
                // return;
                // }

                if (current_type == REGIST_TYPE || current_type == SETTING_PASSWORD_TYPE || current_type == REGIST_THREE_PLAT) {
                    // // engine.register(telephone, verify_code, listener);
                    //
                    // Intent intent = new Intent(RLFActivity.this, SettingNameActivity.class);
                    // startActivity(intent);
                    PromptManager.showProgressDialog(this, "正在验证...", false);
                    engine.checkMobile(telephone, checkListener);
                } else if (current_type == FORGET_PSW_TYPE) {
                    // startActivity(SettingNameActivity.newIntent(RLFActivity.this,
                    // etPhoneNum.getText().toString(),
                    // code.getText().toString(), true));
                    // engine.login(telephone, verify_code, ConstantValue.IS_CAPTCHA, listener);
                    // PromptManager.showProgressDialog(this, "正在登录...", false);
                    startActivity(VerificationActivity.newForgetPswIntent(RLFActivity.this, etPhoneNum
                            .getText().toString()));
                    finish();
                }

                break;
            case R.id.rlt_agreement:
                Intent intent = new Intent(this, AgreementTextActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    ParseHttpListener checkListener = new ParseHttpListener<Boolean>() {
        @Override
        public void requestCallBack() {
            super.requestCallBack();
            PromptManager.closeProgressDialog();
        }


        @Override
        protected Boolean parseDateTask(String jsonData) {
            try {
                JSONObject resultObject = new JSONObject(jsonData);
                return resultObject.optBoolean("status");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void afterParseData(Boolean object) {
            if (!object) {
                if (isRegisterThreePlatform) {
                    startActivityForResult((VerificationActivity.newThreePlatformIntent(RLFActivity.this, etPhoneNum
                            .getText().toString(), null, name)), REQUEST_BOUND_THREE_PLATFORM);
                } else if (isSettingPsw) {
                    startActivityForResult(VerificationActivity.newIntent(RLFActivity.this, etPhoneNum.getText().toString(),
                            null, false), REQUESTCODE_SET_PASSWROD);
                } else {
                    startActivityForResult((VerificationActivity.newSettPswIntent(RLFActivity.this, etPhoneNum
                            .getText().toString(), null)), REQUESTCODE_SET_PASSWROD);

                }
            } else {
                // Intent i = new Intent(RLFActivity.this, LoginActivity.class);
                // startActivity(i);
                // finish();
                if (isSettingPsw) {//手机号已经绑定，不可再绑定
                    showHasBindnDailog();
                } else {


                    showCaptchaLoginDailog();
                }
            }
        }
    };

    private boolean isValidPhoneNum() {
        if (TextUtils.isEmpty(telephone)) {
            // PromptManager.showToast("手机号码不能为空");
            etPhoneNum.setError(Html.fromHtml("<font color='red'>手机号码不能为空</font>"));
            etPhoneNum.requestFocus();
            return false;
        }
        if (!SIMCardInfo.isMobileNO(telephone)) {
            etPhoneNum.setError(Html.fromHtml("<font color='red'>请输入正确的手机号码</font>"));
            etPhoneNum.requestFocus();
            // PromptManager.showToast("请输入正确的手机号码");
            return false;
        }
        return true;
    }

    private ParseHttpListener<UserEntity> listener = new ParseHttpListener<UserEntity>() {

        public void onFailure(int errCode, String errMsg) {
            isLoginByCaptcha = false;
            PromptManager.closeProgressDialog();
            super.onFailure(errCode, errMsg);
        }

        ;

        public void onHttpSuccess(String result) {
            if (result.startsWith("{")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.has("errors")) {
                        JSONObject errors = jsonObject.getJSONObject("errors");
                        if (errors.has("mobile_registered")) {
//                            showCaptchaLoginDailog();
                            showHasBindnDailog();
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
        }

        ;

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

                    if (isSettingPsw) {
                        Intent intent = new Intent(RLFActivity.this, RegisterSuccessActivity.class);
                        intent.putExtra("username", entity.getUsername());
                        startActivity(intent);
                    }
                }
            } else {
                // Intent intent = new Intent(RLFActivity.this, SetPasswordActivity.class);
                // intent.putExtra("verify_code", verify_code);
                // intent.putExtra("type", SetPasswordActivity.LOGIN_TYPE);
                // startActivity(intent);
            }
            finish();
        }
    };

    private void saveUser(final UserEntity user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserEntity entity = SecurityUtils.encrypt(user);
                DbUtils dbutil = DbUtils.create(PortfolioApplication.getInstance());
                UserEntity dbentity;
                try {
                    dbentity = dbutil.findFirst(UserEntity.class);
                    if (dbentity != null) {
                        dbutil.delete(dbentity);
                    }
                    dbutil.save(entity);
                } catch (DbException e) {
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

    @Override
    public void onBackPressed() {
        // PromptManager.closeProgressDialog();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_sign_account);

    @Override
    public int getPageStatisticsStringId() {
        if (current_type == REGIST_TYPE) {
            return R.string.statistics_rlf;
        } else if (current_type == FORGET_PSW_TYPE) {
            return R.string.statistics_rlf_psw;
        } else {
            return 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUESTCODE_SET_PASSWROD: {
                    setResult(RESULT_OK);
                    finish();
                }

                break;
                case REQUEST_BOUND_THREE_PLATFORM:

                    break;
            }
        }


    }

}
