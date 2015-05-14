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
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
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
import com.dkhs.portfolio.receiver.SMSBroadcastReceiver;
import com.dkhs.portfolio.ui.widget.TextViewClickableSpan;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.SIMCardInfo;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.MobclickAgent;

public class BoundEmailActivity extends ModelAcitivity implements OnClickListener {
    // private Button btn_get_code;
    private Button rlfbutton;
    // private EditText code;
    private EditText etPhoneNum;
    private CheckBox cbAgree;

    public static final int REGIST_TYPE = 1001;
    public static final int FORGET_PSW_TYPE = 1002;
    private boolean isLoginByCaptcha = false;

    private static final int GET_CODE_UNABLE = 0;
    private static final int GET_CODE_ABLE = 1;
    private static final int GET_PHONE_NUMBER = 2;
    protected static final String TAG = "RLFActivity";
    public Timer mTimer = new Timer();// 定时器
    private boolean mobileAble = false;
    private boolean codeAble = false;
    private TextView rltAgreement;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_layout);
        initViews();
        setListener();
        initData();
        initLink();

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

                dlg.dismiss();
                startActivity(LoginActivity.getLoginActivity(BoundEmailActivity.this, etPhoneNum.getText().toString()));
                finish();


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
                SIMCardInfo info = new SIMCardInfo(BoundEmailActivity.this);
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
        current_type = getIntent().getIntExtra("activity_type", REGIST_TYPE);
        rlfbutton = (Button) findViewById(R.id.rlbutton);
        etPhoneNum = (EditText) findViewById(R.id.et_mobile);
        rltAgreement = (TextView) findViewById(R.id.rlt_agreement);
        rlfbutton.setEnabled(false);
        // code = (EditText) findViewById(R.id.et_verifycode);
        // tvMessage = (TextView) findViewById(R.id.tv_agree_info);
        cbAgree = (CheckBox) findViewById(R.id.cb_agree);
        setRegistAble();

        if (current_type == REGIST_TYPE) {
            setTitle("绑定邮箱");
            rlfbutton.setText("下一步");
        } else if (current_type == FORGET_PSW_TYPE) {
            setTitle(R.string.forget_password);
            rlfbutton.setText(R.string.confirm);
            cbAgree.setVisibility(View.GONE);

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
        /*SpannableStringBuilder sp = new SpannableStringBuilder();
        sp.append(str + str2);

        sp.setSpan(new TextViewClickableSpan(getResources().getColor(R.color.blue), this, null), str.length(),
                str.length() + str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/
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
                if (current_type == REGIST_TYPE) {
                    PromptManager.showProgressDialog(this, "正在发送邮件...", false);
                    engine.boundEmail(telephone, checkListener);
                } else if (current_type == FORGET_PSW_TYPE) {
                }

                break;
            case R.id.rlt_agreement:
            	Intent intent = new Intent(this,AgreementTextActivity.class);
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
        };

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
            if (object) {
                /*startActivity(VerificationActivity.newIntent(BoundEmailActivity.this, etPhoneNum.getText().toString(), null,
                        false));*/
            	PromptManager.showToast(R.string.bound_email_succes);
            	Intent intent=new Intent();
    			setResult(RESULT_OK, intent);
    			finish();
            }
        }
    };

    private boolean isValidPhoneNum() {
        if (TextUtils.isEmpty(telephone)) {
            // PromptManager.showToast("手机号码不能为空");
            etPhoneNum.setError(Html.fromHtml("<font color='red'>邮箱不能为空</font>"));
            etPhoneNum.requestFocus();
            return false;
        }
        if (!SIMCardInfo.isEmail(telephone)) {
            etPhoneNum.setError(Html.fromHtml("<font color='red'>请输入正确的邮箱</font>"));
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
                    Intent intent = new Intent(BoundEmailActivity.this, NewMainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(BoundEmailActivity.this, RegisterSuccessActivity.class);
                    intent.putExtra("username", entity.getUsername());
                    startActivity(intent);
                }
            } else {

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

    @Override
    public void onBackPressed() {
        // PromptManager.closeProgressDialog();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_dound_email);
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}
}