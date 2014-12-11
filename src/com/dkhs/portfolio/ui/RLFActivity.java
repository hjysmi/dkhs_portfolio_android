package com.dkhs.portfolio.ui;

import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.TextViewClickableSpan;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.SIMCardInfo;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.MobclickAgent;

public class RLFActivity extends ModelAcitivity implements OnClickListener {
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

    // @SuppressLint("HandlerLeak")
    // private Handler handler = new Handler() {
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
    // case GET_PHONE_NUMBER:
    // if (!TextUtils.isEmpty(phoneNumber)) {
    // if (phoneNumber.startsWith("+86")) {
    // phoneNumber = phoneNumber.replace("+86", "");
    // }
    // etPhoneNum.setText(phoneNumber);
    // }
    // break;
    // default:
    // break;
    // }
    // };
    // };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rlf_layout);
        initViews();
        setListener();
        initData();
        initLink();

    }

    private void showCaptchaLoginDailog() {
        PromptManager.closeProgressDialog();
        isLoginByCaptcha = true;
        final AlertDialog dpg = new AlertDialog.Builder(new ContextThemeWrapper(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar)).create();
        dpg.setCancelable(false);
        dpg.setTitle(R.string.login_by_captcha);
        dpg.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dpg.dismiss();
				startActivity(LoginActivity.getLoginActivity(RLFActivity.this, etPhoneNum.getText().toString()));
				finish();
			}
		});
        dpg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
        	
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		isLoginByCaptcha = false;
        		dpg.dismiss();
        	}
        });
        dpg.show();
//        final AlertDialog dlg = new AlertDialog.Builder(this).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.captcha_login_dialog_layout);
//        Button login = (Button) window.findViewById(R.id.login);
//        login.setClickable(true);
//        login.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//
//                dlg.dismiss();
//                startActivity(LoginActivity.getLoginActivity(RLFActivity.this, etPhoneNum.getText().toString()));
//                finish();
//                // if (NetUtil.checkNetWork(getApplicationContext())) {
//                // // PromptManager.showToast(R.string.logining);
//                // // engine.login(telephone, verify_code, ConstantValue.IS_CAPTCHA, listener);
//                // } else {
//                // PromptManager.showNoNetWork(getApplicationContext());
//                // }
//
//            }
//        });
//        window.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                isLoginByCaptcha = false;
//                dlg.dismiss();
//            }
//        });
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
        // code.addTextChangedListener(new TextWatcher() {
        //
        // @Override
        // public void onTextChanged(CharSequence s, int start, int before, int count) {
        //
        // }
        //
        // @Override
        // public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //
        // }
        //
        // @Override
        // public void afterTextChanged(Editable s) {
        // if (s.length() == 6 && mobileAble) {
        //
        // codeAble = true;
        // } else {
        // codeAble = false;
        // }
        // setRegistAble();
        // }
        // });
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
            setTitle("注册账号");
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
        // 跳转隐私政策
        // Intent intent1 = new Intent(this, ActivityTermsPrivate.class);
        // intent1.putExtra(ActivityTermsPrivate.TYPE, ActivityTermsPrivate.TYPE_POLICY);
        // // 跳转服务条款
        // Intent intent = new Intent(this, .class);
        // intent2.putExtra(ActivityTermsPrivate.TYPE, ActivityTermsPrivate.TYPE_TERMS);

        SpannableStringBuilder sp = new SpannableStringBuilder();
        sp.append(str2);

        sp.setSpan(new TextViewClickableSpan(getResources().getColor(R.color.blue), this, null), 0,
                str2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cbAgree.setText(str);
        // 设置TextView可点击
        cbAgree.setMovementMethod(LinkMovementMethod.getInstance());
        rltAgreement.setText(sp);
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

                if (current_type == REGIST_TYPE) {
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
                }

                break;
            case R.id.rlt_agreement:
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
            if (!object) {
                startActivity(VerificationActivity.newIntent(RLFActivity.this, etPhoneNum.getText().toString(), null,
                        false));
            } else {
                // Intent i = new Intent(RLFActivity.this, LoginActivity.class);
                // startActivity(i);
                // finish();
                showCaptchaLoginDailog();
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

    // private void timerTask() {
    // mTimer.schedule(new TimerTask() {
    //
    // @Override
    // public void run() {
    // if (count < 60) {
    // handler.sendEmptyMessage(GET_CODE_UNABLE);
    // count++;
    // } else {
    // handler.sendEmptyMessage(GET_CODE_ABLE);
    // }
    // }
    // }, 0, 1000);
    // }

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
