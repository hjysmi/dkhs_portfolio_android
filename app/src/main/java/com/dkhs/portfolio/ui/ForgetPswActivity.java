/**
 * @Title ForgetPswActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-11-3 下午5:03:25
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

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

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.receiver.SMSBroadcastReceiver;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.SIMCardInfo;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author zjz
 * @version 1.0
 * @ClassName ForgetPswActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-11-3 下午5:03:25
 */
public class ForgetPswActivity extends ModelAcitivity implements OnClickListener {
    private Button btn_get_code;
    private Button rlfbutton;
    private EditText etVerifucode;
    private EditText etPhoneNum;

    public Timer mTimer = new Timer();// 定时器
    private static final int GET_CODE_UNABLE = 11;
    private static final int GET_CODE_ABLE = 12;

    private UserEngineImpl engine;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setTitle(R.string.forget_password);
        setContentView(R.layout.activity_forget_psw);
        engine = new UserEngineImpl();
        rlfbutton = (Button) findViewById(R.id.rlbutton);
        rlfbutton.setOnClickListener(this);
        rlfbutton.setEnabled(false);

        etVerifucode = (EditText) findViewById(R.id.et_verifycode);
        etPhoneNum = (EditText) findViewById(R.id.et_mobile);
        btn_get_code = (Button) findViewById(R.id.btn_getCode);
        btn_get_code.setOnClickListener(this);
        btn_get_code.setEnabled(false);
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
                etVerifucode.setText(codeSb.substring(0, 4));

            }
        });

        etPhoneNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btn_get_code.setEnabled(true);
                    if (etVerifucode.getText().length() > 0) {
                        rlfbutton.setEnabled(true);
                    } else {
                        rlfbutton.setEnabled(false);
                    }
                } else {
                    rlfbutton.setEnabled(false);
                    btn_get_code.setEnabled(false);
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
                if (s.length() > 0 && etPhoneNum.getText().length() > 0) {
                    rlfbutton.setEnabled(true);
                } else {
                    rlfbutton.setEnabled(false);
                }

            }
        });
    }

    private void checkPhoneRegister() {
        engine.isSetPassword(etPhoneNum.getText().toString(), new ParseHttpListener<Boolean>() {

            @Override
            protected Boolean parseDateTask(String jsonData) {
                boolean isSetPassword = false;
                try {
                    JSONObject json = new JSONObject(jsonData);
                    if (json.has("status")) {
                        isSetPassword = json.optBoolean("status");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return isSetPassword;
            }

            @Override
            protected void afterParseData(Boolean object) {
                if (object) {
                    getVerifyCode();
                }
            }
        });
    }

    private void getVerifyCode() {
        if (NetUtil.checkNetWork()) {
            engine.getVericode(etPhoneNum.getText().toString(), new ParseHttpListener<Object>() {

                @Override
                protected Object parseDateTask(String jsonData) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                protected void afterParseData(Object object) {
                    // TODO Auto-generated method stub

                }

            });

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
                    btn_get_code.setBackgroundResource(R.drawable.btn_blue_selector);
                    btn_get_code.setTextColor(ColorTemplate.getTextColor(R.color.btn_blue_textselector));
                    count = 0;
                    mTimer.cancel();
                    break;
                case GET_CODE_UNABLE:
                    btn_get_code.setText((60 - count) + "秒后重新获取验证码");
                    btn_get_code.setBackgroundResource(R.drawable.btn_unable_gray);
                    btn_get_code.setTextColor(getResources().getColor(R.color.white));
                    btn_get_code.setEnabled(false);
                    break;

                default:
                    break;
            }
            return true;
        }
    });

    private String telephone;
    private String verifyCode;

    @Override
    public void onClick(View v) {
        verifyCode = etVerifucode.getText().toString();
        telephone = etPhoneNum.getText().toString();
        if (v.getId() == R.id.rlbutton) {
            if (TextUtils.isEmpty(verifyCode)) {
                // PromptManager.showToast("手机号码不能为空");
                etVerifucode.setError(Html.fromHtml("<font color='red'>验证码不能为空</font>"));
                etVerifucode.requestFocus();
                return;
            }
            new UserEngineImpl().checkVericode(telephone, verifyCode, new ParseHttpListener<Boolean>() {

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
                        startActivity(SetPasswordActivity.newIntent(ForgetPswActivity.this, telephone, verifyCode));
                    } else {
                        PromptManager.showToast("验证码有误");
                    }
                }
            }.setLoadingDialog(this));
        }
        if (v.getId() == R.id.btn_getCode) {
            if (!isValidPhoneNum()) {
                return;
            }
            // getVerifyCode();
            checkPhoneRegister();
        }
    }

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

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 注销短信监听广播
        this.unregisterReceiver(mSMSBroadcastReceiver);
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_forget_psd);

}
