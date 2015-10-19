package com.dkhs.portfolio.ui.wallets;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.WithDrawResBean;
import com.dkhs.portfolio.common.GlobalParams;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.engine.WithDrawEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.receiver.SMSBroadcastReceiver;
import com.dkhs.portfolio.ui.MyPurseActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.WithDrawEvent;
import com.dkhs.portfolio.ui.fragment.BaseFragment;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Timer;
import java.util.TimerTask;

public class WithDrawFragment extends BaseFragment implements View.OnClickListener{

    private static final int GET_CODE_UNABLE = 11;
    private static final int GET_CODE_ABLE = 12;
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public Timer mTimer = new Timer();// 定时器
    private int count ;
    private UserEngineImpl engine;
    private SMSBroadcastReceiver mSMSBroadcastReceiver;

    private float avail;
    @ViewInject(R.id.et_withdraw_amount)
    private EditText amountEt;
    @ViewInject(R.id.et_alipay_account)
    private EditText accountEt;
    @ViewInject(R.id.et_bound_name)
    private EditText boundNameEt;
    @ViewInject(R.id.et_verifycode)
    private EditText verifycodeEt;
    @ViewInject(R.id.btn_getCode)
    private TextView getCodeBtn;
    @ViewInject(R.id.tv_send_code)
    private TextView sendCodeTv;
    @ViewInject(R.id.rlbutton)
    private Button  rlBtn;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_with_draw;
    }
    public static WithDrawFragment newInstance(Bundle bundle){
        WithDrawFragment fragment = new WithDrawFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            avail = bundle.getFloat(MyPurseActivity.AVAIL_AMOUNT);
        }
        engine = new UserEngineImpl();
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();

        // 实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        // 注册广播
        getActivity().registerReceiver(mSMSBroadcastReceiver, intentFilter);

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
                verifycodeEt.setText(codeSb.substring(0, 4));

            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initData(){
        amountEt.setFilters(new InputFilter[]{lengthfilter});
        String availHint = String.format(getString(R.string.with_draw_available),avail);
        String sendCodeMsg = String.format(getString(R.string.msg_send_post), GlobalParams.MOBILE);
        amountEt.setHint(availHint);
        sendCodeTv.setText(sendCodeMsg);
    }

    private WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GET_CODE_ABLE:
                    getCodeBtn.setText(R.string.get_code);
                    getCodeBtn.setEnabled(true);
//                    btn_get_code.setBackgroundResource(R.drawable.btn_blue_selector);
                    getCodeBtn.setTextColor(ColorTemplate.getTextColor(R.color.theme_blue));
                    count = 0;
                    mTimer.cancel();
                    break;
                case GET_CODE_UNABLE:
                    getCodeBtn.setText("重新发送("+(60 - count)+")s");
//                    btn_get_code.setBackgroundResource(R.drawable.btn_unable_gray);
                    getCodeBtn.setTextColor(getResources().getColor(R.color.text_content_color));
                    getCodeBtn.setEnabled(false);
                    break;

                default:
                    break;
            }
            return true;
        }
    });

    @Override
    public void onDestroyView() {
        if(handler != null){
            handler.removeMessages(GET_CODE_UNABLE);
            handler.removeMessages(GET_CODE_ABLE);
        }
        super.onDestroyView();
    }

    private void getVerifyCode() {
        if (NetUtil.checkNetWork()) {
            engine.getVericode(GlobalParams.MOBILE, new ParseHttpListener<Object>() {

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

    @OnClick({R.id.btn_getCode,R.id.rlbutton})
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_getCode){
            getVerifyCode();
        }else if(v.getId() == R.id.rlbutton){
            String amount = amountEt.getText().toString();
            String account = accountEt.getText().toString();
            String name = boundNameEt.getText().toString();
            String verifycode = verifycodeEt.getText().toString();
            if(checkDataValid(amount,account,name,verifycode)){
                withdraw(amount,account,name,verifycode);
            }
        }

    }

    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("\\.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - 2;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    @Override
    public void onDestroy() {
        if(mSMSBroadcastReceiver != null)
            getActivity().unregisterReceiver(mSMSBroadcastReceiver);
        super.onDestroy();
    }

    private boolean checkDataValid(String amount,String account,String name,String verifycode){
        if(TextUtils.isEmpty(amount)){
            PromptManager.showToast("提取金额不能为空");
            return false;
        }
        if(TextUtils.isEmpty(account)){
            PromptManager.showToast("账号不能为空");
            return false;
        }
        if(TextUtils.isEmpty(name)){
            PromptManager.showToast("账户实名不能为空");
            return false;
        }
        if(TextUtils.isEmpty(verifycode)){
            PromptManager.showToast("验证码不能为空");
            return false;
        }
        return true;
    }

    private void withdraw(String amount,String account,String name,String verifycode){
        WithDrawEngineImpl.withDraw(amount, account, name, verifycode, new ParseHttpListener() {
            @Override
            protected Object parseDateTask(String jsonData) {
                WithDrawResBean obj = DataParse.parseObjectJson(WithDrawResBean.class, jsonData);
                LogUtils.d("wys","serial_no"+obj.serial_no);
                return obj;
            }

            @Override
            protected void afterParseData(Object object) {
                if(object != null){
                    //TODO 发送提取成功
                    BusProvider.getInstance().post(new WithDrawEvent(0));
                    getActivity().finish();
                }else{
                    PromptManager.showToast("提取失败");
                }
            }
        });
    }
}
