package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.Bank;
import com.dkhs.portfolio.bean.IdentityAuthBean;
import com.dkhs.portfolio.bean.IdentityInfoBean;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.messagecenter.MessageHandler;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.SIMCardInfo;
import com.dkhs.portfolio.utils.UIUtils;
import com.hxcr.chinapay.activity.Initialize;
import com.hxcr.chinapay.util.CPGlobaInfo;
import com.hxcr.chinapay.util.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangcm on 2015/9/16.15:02
 */
public class BankCardInfoActivity extends ModelAcitivity implements View.OnClickListener {

    public static String BANK = "bank";
    private Bank bank;
    private IdentityInfoBean identityInfoBean;
    private String bank_card_id;
    private boolean isResetPasswordType;
    private MyBankCard mBankCard;
    public static String BANK_CARD = "bank_card";
    private static final String LAYOUT_TYPE = "layout_type";
    private static final String BANK_CARD_NO = "card_no";
    private static final String IDENTITY_INFO_BEAN = "identity_info_bean";

    @ViewInject(R.id.et_bank_card)
    private EditText et_bank_card;

    @ViewInject(R.id.et_real_name)
    private EditText et_real_name;

    @ViewInject(R.id.et_id_card_no)
    private EditText et_id_card_no;
    @ViewInject(R.id.tv_id_card_num)
    private TextView tv_id_card_num;

    @ViewInject(R.id.et_bank_card_mobile)
    private EditText et_bank_card_mobile;

    @ViewInject(R.id.ll_bank_card)
    private View ll_bank_card;

    @ViewInject(R.id.ll_choose_bank_type)
    private View ll_choose_bank_type;

    @ViewInject(R.id.tv_bank)
    private TextView tv_bank;

    @ViewInject(R.id.tv_limit_value)
    private TextView tv_limit_value;

    @ViewInject(R.id.btn_bind_bank_card)
    private Button btn_bind_bank_card;

    @ViewInject(R.id.btn_get_code)
    private TextView btn_get_code;

    @ViewInject(R.id.et_verifycode)
    private EditText et_verifycode;

    @ViewInject(R.id.cb_agree)
    private CheckBox cb_agree;

    public static Intent forgetTradePasswordIntent(Context context, MyBankCard bankCard) {
        Intent intent = new Intent(context, BankCardInfoActivity.class);
        intent.putExtra(LAYOUT_TYPE, true);
        intent.putExtra(BANK_CARD, bankCard);
        return intent;
    }

    public static Intent bankCardInfoIntent(Context context, String cardNo, IdentityInfoBean identityInfoBean) {
        Intent intent = new Intent(context, BankCardInfoActivity.class);
        intent.putExtra(LAYOUT_TYPE, false);
        intent.putExtra(BANK_CARD_NO, cardNo);
        if (identityInfoBean != null)
            intent.putExtra(IDENTITY_INFO_BEAN, Parcels.wrap(identityInfoBean));
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_bank_card_info);
        Bundle extras = getIntent().getExtras();
        ViewUtils.inject(this);
        if (extras != null) {
            handleExtras(extras);
        }
        setTitle(R.string.input_bank_card_info);
        initViews();
        if (!isResetPasswordType) {
            initData();
        }
    }

    private void initData() {
        ParseHttpListener<Bank> listener = new ParseHttpListener<Bank>() {
            @Override
            protected Bank parseDateTask(String jsonData) {
                try {
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    bank = DataParse.parseObjectJson(Bank.class, jsonData);
                } catch (Exception e) {
                }
                return bank;
            }

            @Override
            protected void afterParseData(Bank bank) {
                if (!TextUtils.isEmpty(bank.getName())) {
                    tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value), bank.getSingle_limit(), bank.getSingle_day_limit()));
                    tv_limit_value.setVisibility(View.VISIBLE);
                    tv_bank.setText(bank.getName());
                    tv_bank.setTextColor(UIUtils.getResColor(mContext, R.color.black));
                    btnStatus++;
                }
            }
        };
        new TradeEngineImpl().checkBank(bankCardNo, listener.setLoadingDialog(mContext));

    }

    private void handleExtras(Bundle extras) {
        isResetPasswordType = extras.getBoolean(LAYOUT_TYPE);
        bankCardNo = extras.getString(BANK_CARD_NO, "");
        mBankCard = (MyBankCard) extras.getSerializable(BANK_CARD);
        identityInfoBean = Parcels.unwrap(extras.getParcelable(IDENTITY_INFO_BEAN));
        if (mBankCard != null) {
            bank = mBankCard.getBank();
            btnStatus++;
        }
        if (identityInfoBean != null && identityInfoBean.status == 1) {
            needFillNameAndId = false;
            et_real_name.setText(identityInfoBean.real_name);
            et_real_name.setEnabled(false);
            et_real_name.setFocusable(false);
            et_id_card_no.setVisibility(View.GONE);
            tv_id_card_num.setVisibility(View.VISIBLE);
            tv_id_card_num.setText(identityInfoBean.id_card_no_masked);
        }
    }

    private void initViews() {
        if (isResetPasswordType) {
            ll_bank_card.setVisibility(View.VISIBLE);
            ll_choose_bank_type.setVisibility(View.GONE);
            et_bank_card.setHint(String.format(getResources().getString(R.string.blank_hint_card_no), mBankCard.getBank_card_no_tail()));
            tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value), bank.getSingle_limit(), bank.getSingle_day_limit()));
            tv_limit_value.setVisibility(View.VISIBLE);
            tv_bank.setText(bank.getName());
            tv_bank.setTextColor(UIUtils.getResColor(mContext, R.color.black));
        } else {
            ll_bank_card.setVisibility(View.GONE);
            ll_choose_bank_type.setVisibility(View.VISIBLE);
        }
        et_bank_card.addTextChangedListener(new TextWatcher() {
            private boolean isBeforeNull;
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
                isBeforeNull = TextUtils.isEmpty(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (isChanged) {
                    location = et_bank_card.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if (index % 5 == 4) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    et_bank_card.setText(str);
                    Editable etable = et_bank_card.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                    return;
                }
                if (!TextUtils.isEmpty(s)) {
                    if (isBeforeNull)
                        btnStatus++;
                    checkBtnStatus();
                } else {
                    btnStatus--;
                    btn_bind_bank_card.setEnabled(false);
                }
            }
        });
        et_real_name.addTextChangedListener(new TextWatcher() {
            private String beforeS;
            private boolean isBeforeAble;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeS = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && !checkName(s.toString())) {
                    et_real_name.setText(beforeS);
                    Editable etable = et_real_name.getText();
                    Selection.setSelection(etable, start);
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() >= 2) {
                    if (!isBeforeAble) {
                        btnStatus++;
                        isBeforeAble = true;
                    }
                    checkBtnStatus();
                } else {
                    if (isBeforeAble) {
                        isBeforeAble = false;
                        btnStatus--;
                    }
                    btn_bind_bank_card.setEnabled(false);
                }
            }

            /**
             * 判定输入汉字
             * @param c
             * @return
             */
            public boolean hasSpecialChar(char c) {
                Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
                return ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
            }

            /**
             * 检测String是否包含特殊字符
             * @param name
             * @return
             */
            public boolean checkName(String name) {
                boolean res = true;
                char[] cTemp = name.toCharArray();
                for (int i = 0; i < name.length(); i++) {
                    if (hasSpecialChar(cTemp[i])) {
                        res = false;
                        break;
                    }
                }
                return res;
            }
        });
        et_id_card_no.addTextChangedListener(new TextWatcher() {
            private String beforeS;
            private boolean isBeforeAble;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeS = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() <= 17) {
                    boolean isNum = false;
                    try {
                        Long.parseLong(s.toString());
                        isNum = true;
                    } catch (Exception e) {

                    } finally {
                        if (!isNum) {
                            et_id_card_no.setText(beforeS);
                            Editable etable = et_id_card_no.getText();
                            Selection.setSelection(etable, start);
                            return;
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() >= 14) {
                    if (!isBeforeAble) {
                        btnStatus++;
                        isBeforeAble = true;
                    }
                    checkBtnStatus();
                } else {
                    if (isBeforeAble) {
                        isBeforeAble = false;
                        btnStatus--;
                    }
                    btn_bind_bank_card.setEnabled(false);
                }
            }

            /**
             * 判定输入汉字
             * @param c
             * @return
             */
            public boolean hasSpecialChar(char c) {
                Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
                return ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
            }

            /**
             * 检测String是否包含特殊字符
             * @param name
             * @return
             */
            public boolean checkName(String name) {
                boolean res = true;
                char[] cTemp = name.toCharArray();
                for (int i = 0; i < name.length(); i++) {
                    if (hasSpecialChar(cTemp[i])) {
                        res = false;
                        break;
                    }
                }
                return res;
            }
        });
//        et_id_card_no.addTextChangedListener(new MyTextWatcher(false));
        et_bank_card_mobile.addTextChangedListener(new MyTextWatcher(true));
        et_verifycode.addTextChangedListener(new MyTextWatcher(false));
        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnStatus++;
                } else {
                    btnStatus--;
                }
                checkBtnStatus();
            }
        });
        btn_get_code.setEnabled(false);
        btn_get_code.setTextColor(ColorTemplate.getTextColor(R.color.text_content_color));
    }

    private int count = 0;
    public Timer mTimer = new Timer();// 定时器
    private static final int GET_CODE_UNABLE = 11;
    private static final int GET_CODE_ABLE = 12;
    private WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GET_CODE_ABLE:
                    btn_get_code.setText(R.string.get_code);
                    btn_get_code.setEnabled(true);
                    btn_get_code.setTextColor(ColorTemplate.getTextColor(R.color.theme_blue));

                    count = 0;
                    mTimer.cancel();
                    break;
                case GET_CODE_UNABLE:
                    btn_get_code.setText(String.format(getResources().getString(R.string.blank_get_code), 60 - count));
                    btn_get_code.setEnabled(false);
                    btn_get_code.setTextColor(getResources().getColor(R.color.text_content_color));

                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private void getVerifyCode() {
        if (NetUtil.checkNetWork()) {
            listener.setLoadingDialog(this);
            new UserEngineImpl().getVericode(et_bank_card_mobile.getText().toString(), listener);
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

    ParseHttpListener listener = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            return null;
        }

        @Override
        protected void afterParseData(Object object) {
        }

    };

    private TradeEngineImpl tradeEngine = new TradeEngineImpl();
    private String mobile;
    private String captcha;
    private String bankCardNo;
    private String realName;
    private String idCardNo;

    @OnClick({R.id.ll_choose_bank_type, R.id.btn_bind_bank_card, R.id.rlt_agreement, R.id.btn_get_code})
    public void onClick(View v) {
        //TODO 点击下一步
        if (v.getId() == R.id.ll_choose_bank_type) {
            //TODO 选择银行卡
            Intent intent = new Intent(this, ChooseBankActivity.class);
            startActivityForResult(intent, 0);
        } else if (v.getId() == R.id.rlt_agreement) {
            new MessageHandler(this).handleURL(DKHSClient.getAbsoluteUrl(DKHSUrl.Funds.bank_agreement));
        } else if (v.getId() == R.id.btn_get_code) {
            getVerifyCode();
        } else if (isResetPasswordType) {
            ParseHttpListener listener = new ParseHttpListener<Boolean>() {
                @Override
                protected Boolean parseDateTask(String jsonData) {
                    try {
                        JSONObject json = new JSONObject(jsonData);
                        return json.getBoolean("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void afterParseData(Boolean object) {
                    if (object) {
                        // TODO: 2015/12/26 验证成功
                        startActivity(TradePasswordSettingActivity.forgetPwdIntent(mContext, mBankCard.getId(), bankCardNo, realName, idCardNo, mobile, captcha));
                    } else {
                        PromptManager.showToast("验证失败");
                    }
                }
            };
            mobile = et_bank_card_mobile.getText().toString().trim();
            captcha = et_verifycode.getText().toString().trim();
            bankCardNo = et_bank_card.getText().toString().trim().replace(" ", "");
            realName = et_real_name.getText().toString().trim();
            idCardNo = et_id_card_no.getText().toString().trim();
            tradeEngine.resetTradePassword(mBankCard.getId(), bankCardNo, realName, idCardNo, mobile, captcha, null, listener.setLoadingDialog(mContext));
        } else {
            //绑卡
            ParseHttpListener listener = new ParseHttpListener<IdentityAuthBean>() {
                @Override
                protected IdentityAuthBean parseDateTask(String jsonData) {
                    try {
                        JSONObject json = new JSONObject(jsonData);
                        return DataParse.parseObjectJson(IdentityAuthBean.class, json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void afterParseData(IdentityAuthBean object) {
                    if (null != object) {
                        bank_card_id = object.bank_card_id;
                        Utils.setPackageName(getPackageName());
                        Intent intent = new Intent(BankCardInfoActivity.this, Initialize.class);
                        intent.putExtra(CPGlobaInfo.XML_TAG, writeXml(object));
                        startActivity(intent);
                    }
                }
            };
            mobile = et_bank_card_mobile.getText().toString().trim();
            captcha = et_verifycode.getText().toString().trim();
            realName = et_real_name.getText().toString().trim();
            idCardNo = et_id_card_no.getText().toString().trim();
            if (identityInfoBean != null && identityInfoBean.status == 1) {
                realName = null;
                idCardNo = null;
            }
            tradeEngine.verifyIdentityAuth(bank.getId(), bankCardNo, realName, idCardNo, mobile, captcha, listener.setLoadingDialog(this));
        }
    }

    private String writeXml(IdentityAuthBean bean) {
        XmlSerializer se = Xml.newSerializer();
        StringWriter w = new StringWriter();
        try {

            se.setOutput(w);
            se.startDocument("UTF-8", false);
            se.startTag("", "CpPay");
            se.attribute("", "application", "LunchPay.Req");

            if (!TextUtils.isEmpty(bean.env)) {
                se.startTag("", "env");
                se.text(bean.env.toUpperCase());
                se.endTag("", "env");
            }

            se.startTag("", "merchantId");
            se.text(bean.merchantId);
            se.endTag("", "merchantId");

            se.startTag("", "merchantOrderId");
            se.text(bean.merchantOrderId);
            se.endTag("", "merchantOrderId");

            se.startTag("", "merchantOrderTime");
            se.text(bean.merchantOrderTime);
            se.endTag("", "merchantOrderTime");

            se.startTag("", "orderKey");
            se.text(bean.orderKey);
            se.endTag("", "orderKey");

            se.startTag("", "sign");
            se.text(bean.sign);
            se.endTag("", "sign");


            se.endTag("", "CpPay");
            se.endDocument();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return w.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 1) {
            if (bank == null || TextUtils.isEmpty(bank.getName()))
                btnStatus++;
            bank = (Bank) data.getSerializableExtra(BANK);
            if (bank != null) {
                tv_limit_value.setText(String.format(getResources().getString(R.string.blank_limit_value), bank.getSingle_limit(), bank.getSingle_day_limit()));
                tv_limit_value.setVisibility(View.VISIBLE);
                tv_bank.setText(bank.getName());
                tv_bank.setTextColor(UIUtils.getResColor(mContext, R.color.black));
                tv_bank.setTag(bank.getId());
                checkBtnStatus();
            }
        } else if (requestCode == 1 && resultCode == 0) {
            setResult(2);
            manualFinish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int btnStatus = 1;
    private boolean needFillNameAndId = true;

    private void checkBtnStatus() {
        int checkCount = isResetPasswordType ? 7 : 6;
        if (!needFillNameAndId)
            checkCount = 4;
        btn_bind_bank_card.setEnabled(btnStatus == checkCount);
    }

    private class MyTextWatcher implements TextWatcher {
        private boolean isBeforeNull;
        private boolean isMobieWatch;

        public MyTextWatcher(boolean isMobieWatch) {
            this.isMobieWatch = isMobieWatch;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            isBeforeNull = TextUtils.isEmpty(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isMobieWatch) {
                if (count == 0 && !TextUtils.isEmpty(s) && SIMCardInfo.isMobileNO(s.toString())) {
                    btn_get_code.setEnabled(true);
                    btn_get_code.setTextColor(ColorTemplate.getTextColor(R.color.theme_blue));
                }
            }
            if (!TextUtils.isEmpty(s)) {
                if (isBeforeNull)
                    btnStatus++;
                checkBtnStatus();
            } else {
                btnStatus--;
                btn_bind_bank_card.setEnabled(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.getPayResult() != null && !Utils.getPayResult().equals("")) {

            //根据返回码做出相应处理
            if (Utils.getPayResult().indexOf("0000") > -1) {
                //认证成功，返回卡信息及用户信息
                final ParseHttpListener<Boolean> isTradePwdSetListener = new ParseHttpListener<Boolean>() {
                    @Override
                    protected Boolean parseDateTask(String jsonData) {
                        try {
                            JSONObject json = new JSONObject(jsonData);
                            if (json.has("status")) {
                                return json.getBoolean("status");
                            }

                        } catch (Exception e) {
                        }
                        return null;
                    }

                    @Override
                    protected void afterParseData(Boolean object) {
                        if (null != object) {
                            if (object) {
                                //TODO 设置过交易密码
                                setResult(2);
                                manualFinish();
                            } else {
                                //TODO 没设置过交易密码
                                startActivityForResult(TradePasswordSettingActivity.firstSetPwdIntent(mContext, bank_card_id, bankCardNo, realName, idCardNo, mobile, captcha), 1);
                            }
                        }
                    }
                };
                tradeEngine.isTradePasswordSet(isTradePwdSetListener);
            } else {//认证失败
            }

        }
        CPGlobaInfo.init(); //清空返回结果
    }

}
