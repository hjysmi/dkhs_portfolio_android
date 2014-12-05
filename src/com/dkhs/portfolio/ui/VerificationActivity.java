package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
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
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.service.SMSBroadcastReceiver;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;

public class VerificationActivity extends ModelAcitivity implements OnClickListener {

    public static final String EXTRA_PHONENUM = "extra_phone";
    public static final String EXTRA_CODE = "extra_code";
    public static final String EXTRA_ISRESETPSW = "extra_isresetpsw";
    private String phoneNum;
    private String mVerifyCode;
    private Button rlfbutton;
    private TextView tvPhoneNum;
    private EditText etVerifucode;
    private Button btn_get_code;
    private Context context;
    private UserEngineImpl engine;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public static Intent newIntent(Context context, String phoneNum, String code, boolean resetPsw) {
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra(EXTRA_PHONENUM, phoneNum);
        intent.putExtra(EXTRA_ISRESETPSW, resetPsw);
        intent.putExtra(EXTRA_CODE, code);
        return intent;
    }

    private void handleExtras(Bundle extras) {
        phoneNum = extras.getString(EXTRA_PHONENUM);
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
        tvPhoneNum = (TextView) findViewById(R.id.tv_phonenum);
        tvPhoneNum.setText(phoneNum);
        etVerifucode = (EditText) findViewById(R.id.et_verifycode);
        btn_get_code = (Button) findViewById(R.id.btn_getCode);
        btn_get_code.setOnClickListener(this);
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
                etVerifucode.setText(codeSb.substring(0, 6));

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
            String verifyCode = etVerifucode.getText().toString();
            if (TextUtils.isEmpty(verifyCode)) {
                // PromptManager.showToast("手机号码不能为空");
                etVerifucode.setError(Html.fromHtml("<font color='red'>验证码不能为空</font>"));
                etVerifucode.requestFocus();
                return;
            }
            startActivity(SettingNameActivity.newIntent(VerificationActivity.this, phoneNum, verifyCode, false));
        }
        if (v.getId() == R.id.btn_getCode) {
            getVerifyCode();
        }
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
            engine.getVericode(phoneNum, listener);
            listener.setLoadingDialog(context);
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_CODE_ABLE:
                    btn_get_code.setText(R.string.get_code);
                    btn_get_code.setEnabled(true);
                    btn_get_code.setBackgroundResource(R.drawable.btn_blue_selector);
                    btn_get_code.setTextColor(ColorTemplate.getTextColor(R.color.btn_blue_textselector));

                    count = 0;
                    mTimer.cancel();
                    break;
                case GET_CODE_UNABLE:
                    btn_get_code.setText((60 - count) + "秒后重新获取验证码");
                    btn_get_code.setEnabled(false);
                    btn_get_code.setBackgroundResource(R.drawable.btn_unable_gray);
                    btn_get_code.setTextColor(getResources().getColor(R.color.white));

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
        };
    };

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

}
