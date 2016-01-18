package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.receiver.SMSBroadcastReceiver;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class VerificationActivity extends ModelAcitivity implements OnClickListener {

    public static final String EXTRA_PHONENUM = "extra_phone";
    public static final String EXTRA_CODE = "extra_code";
    public static final String EXTRA_ISRESETPSW = "extra_isresetpsw";
    public static final String EXTRA_SETPSW = "extra_set_psw";
    public static final String EXTRA_NAME = "extra_name";

    public static final int REQUEST_BOUND_THREE_PLATFORM = 2;
    private String phoneNum;
    private String mVerifyCode;
    private Button rlfbutton;
    private EditText etVerifucode;
    private TextView btn_get_code;
    private Context context;
    private UserEngineImpl engine;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;
    private boolean isSetPsw;
    private boolean isRegisterThreePlatform;
    private boolean isForgetPsw;
    private String name;

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public static Intent newIntent(Context context, String phoneNum, String code, boolean resetPsw) {
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_ISRESETPSW, resetPsw);
        intent.putExtra(EXTRA_SETPSW, resetPsw);
        intent.putExtra(EXTRA_CODE, code);
        return intent;
    }

    public static Intent newSettPswIntent(Context context, String phoneNum, String code) {
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_SETPSW, true);
        intent.putExtra(EXTRA_CODE, code);
        return intent;
    }

    public static Intent newThreePlatformIntent(Context context, String phoneNum, String code, String name) {
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_SETPSW, true);
        intent.putExtra(EXTRA_CODE, code);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(RLFActivity.EXTRA_REGISTER_THREE_PLATFORM, true);
        return intent;
    }

    public static Intent newForgetPswIntent(Context context, String phoneNum) {
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_SETPSW, true);
        intent.putExtra(RLFActivity.EXTRA_FORGET_PSW, true);
        return intent;
    }

    private void handleExtras(Bundle extras) {
        phoneNum = extras.getString(EXTRA_PHONENUM);
        isSetPsw = extras.getBoolean(EXTRA_SETPSW);
        isRegisterThreePlatform = extras.getBoolean(RLFActivity.EXTRA_REGISTER_THREE_PLATFORM);
        isForgetPsw = extras.getBoolean(RLFActivity.EXTRA_FORGET_PSW);
        name = extras.getString(EXTRA_NAME);
    }

    String strBefore;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        setTitle("填写验证码");
        context = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        engine = new UserEngineImpl();
        rlfbutton = (Button) findViewById(R.id.rlbutton);
        rlfbutton.setOnClickListener(this);
        rlfbutton.setEnabled(false);
        TextView tvPhoneNum = (TextView) findViewById(R.id.tv_phonenum);
        tvPhoneNum.setText(phoneNum);
        etVerifucode = (EditText) findViewById(R.id.et_verifycode);
        btn_get_code = (TextView) findViewById(R.id.btn_getCode);
        btn_get_code.setOnClickListener(this);
        btn_get_code.setEnabled(true);
        // 生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        // 实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        // 注册广播
        this.registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mSMSBroadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {

                if (!TextUtils.isEmpty(message) && message.contains("多快好省")) {

                    StringBuilder codeSb = new StringBuilder();
                    int codeLength = 0;
                    for (String sss : message.replaceAll("[^0-9]", ",").split(",")) {
                        if (codeLength >= 6) {
                            break;
                        }
                        if (sss.length() > 0) {
                            codeSb.append(sss);
                            codeLength++;
                        }
                    }

                    System.out.println("ReceiveCode:" + codeSb);
                    // System.out.println("code:"+codeSb.substring(0, 6));
                    etVerifucode.setText(codeSb.substring(0, 4));
                }

            }
        });
        etVerifucode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    rlfbutton.setEnabled(true);
                } else {
                    rlfbutton.setEnabled(false);
                }

            }
        });

        getVerifyCode();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rlbutton) {
            verifyCode = etVerifucode.getText().toString();
            if (TextUtils.isEmpty(verifyCode)) {
                // PromptManager.showToast("手机号码不能为空");
                etVerifucode.setError(Html.fromHtml("<font color='red'>验证码不能为空</font>"));
                etVerifucode.requestFocus();
                return;
            }
            new UserEngineImpl().checkVericode(phoneNum, verifyCode, new ParseHttpListener<Boolean>() {

                @Override
                protected Boolean parseDateTask(String jsonData) {
                    try {
                        JSONObject json = new JSONObject(jsonData);
                        if (json.has("status")) {
                            return json.getBoolean("status");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return false;
                }

                @Override
                protected void afterParseData(Boolean object) {
                    if (object) {
                        if (!isSetPsw) {
                            bindMobile();
                        } else if (isForgetPsw) {
                            startActivity(SetPasswordActivity.newIntent(VerificationActivity.this, phoneNum, verifyCode));
                            finish();
                        } else if (isRegisterThreePlatform) {
                            startActivity(
                                    SettingNameActivity.newThreePlatformIntent(VerificationActivity.this, phoneNum, verifyCode, false, name));
                            finish();
                        } else {
                            startActivity(SettingNameActivity.newIntent(VerificationActivity.this, phoneNum,
                                    verifyCode, false));
                            finish();
                        }

                    } else {
                        PromptManager.showToast("验证码有误");
                    }
                }
            }.setLoadingDialog(this));
        }
        if (v.getId() == R.id.btn_getCode) {
            getVerifyCode();
        }
    }

    private void bindMobile() {
        new UserEngineImpl().bindMobile(phoneNum, verifyCode, new BasicHttpListener() {

            @Override
            public void onSuccess(String result) {

                startActivityForResult(
                        SettingNameActivity.newSetPSWIntent(VerificationActivity.this, phoneNum, verifyCode),
                        RLFActivity.REQUESTCODE_SET_PASSWROD);
            }
        });
    }

    public Timer mTimer = new Timer();// 定时器
    private static final int GET_CODE_UNABLE = 11;
    private static final int GET_CODE_ABLE = 12;
    private static final int GET_PHONE_NUMBER = 13;
    ParseHttpListener listener = new ParseHttpListener<Object>() {

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

    private void getVerifyCode() {
        if (NetUtil.checkNetWork()) {
            listener.setLoadingDialog(context);
            engine.getVericode(phoneNum, listener);
            if (mTimer != null) {
                mTimer = null;
            }
            mTimer = new Timer();
            timerTask();

        } else {
            PromptManager.showNoNetWork();
        }
    }

    private int count = 0;

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

    private WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GET_CODE_ABLE:
                    btn_get_code.setText(R.string.get_code);
                    btn_get_code.setEnabled(true);
//                    btn_get_code.setBackgroundResource(R.drawable.btn_blue_selector);
                    btn_get_code.setTextColor(ColorTemplate.getTextColor(R.color.theme_blue));

                    count = 0;
                    mTimer.cancel();
                    break;
                case GET_CODE_UNABLE:
                    btn_get_code.setText("重新发送(" + (60 - count) + "s)");
                    btn_get_code.setEnabled(false);
//                    btn_get_code.setBackgroundResource(R.drawable.btn_unable_gray);
                    btn_get_code.setTextColor(getResources().getColor(R.color.text_content_color));

                    break;
                case GET_PHONE_NUMBER:
                    // if (!TextUtils.isEmpty(phoneNumber)) {
                    // if (phoneNumber.startsWith("+86")) {
                    // phoneNumber = phoneNumber.replace("+86", "");
                    // }
                    // etPhoneNum.setText(phoneNumber);
                    // }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private String verifyCode;

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_verification);


    @Override
    public int getPageStatisticsStringId() {
        if (isForgetPsw) {
            return R.string.statistics_verification_psw;
        } else if (isSetPsw && !isRegisterThreePlatform) {
            return R.string.statistics_verification;
        }
        return super.getPageStatisticsStringId();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case RLFActivity.REQUESTCODE_SET_PASSWROD: {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }

                break;
                case REQUEST_BOUND_THREE_PLATFORM:
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (btn_get_code.isEnabled()) {
            super.onBackPressed();
        } else {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        MAlertDialog builder = PromptManager.getAlertDialog(this);
        builder.setMessage(R.string.get_code_hint).setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton(R.string.wait, null);
        builder.show();
    }

    @Override
    public void finish() {
        super.finish();
        UIUtils.outAnimationActivity(this);
    }
}
