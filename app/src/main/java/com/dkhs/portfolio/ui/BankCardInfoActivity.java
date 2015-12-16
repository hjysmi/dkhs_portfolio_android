package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.TextView;
import com.dkhs.portfolio.bean.Bank;
import com.dkhs.portfolio.bean.IdentityAuthBean;
import com.dkhs.portfolio.bean.MyBankCard;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.hxcr.chinapay.activity.Initialize;
import com.hxcr.chinapay.util.CPGlobaInfo;
import com.hxcr.chinapay.util.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

/**
 * Created by zhangcm on 2015/9/16.15:02
 */
public class BankCardInfoActivity extends ModelAcitivity implements View.OnClickListener{

    public static String BANK = "bank";
    private Bank bank;
    private boolean isResetPasswordType;
    private MyBankCard mBankCard;
    private String bankCrardNo;
    public static String BANK_CARD = "bank_card";
    private static final String LAYOUT_TYPE = "layout_type";
    private static final String BANK_CARD_NO = "card_no";

    @ViewInject(R.id.et_bank_card)
    private EditText etBankCard;

    @ViewInject(R.id.et_real_name)
    private EditText et_real_name;

    @ViewInject(R.id.et_id_card_no)
    private EditText et_id_card_no;

    @ViewInject(R.id.et_bank_card_mobile)
    private EditText et_bank_card_mobile;

    @ViewInject(R.id.ll_bank_card)
    private View ll_bank_card;

    @ViewInject(R.id.ll_choose_bank_type)
    private View ll_choose_bank_type;

    @ViewInject(R.id.tv_bank)
    private TextView tvBank;

    @ViewInject(R.id.btn_bind_bank_card)
    private Button btn_bind_bank_card;

    public static Intent forgetTradePasswordIntent(Context context,MyBankCard bankCard) {
        Intent intent = new Intent(context, BankCardInfoActivity.class);
        intent.putExtra(LAYOUT_TYPE, true);
        intent.putExtra(BANK_CARD, bankCard);
        return intent;
    }

    public static Intent bankCardInfoIntent(Context context, String cardNo) {
        Intent intent = new Intent(context, BankCardInfoActivity.class);
        intent.putExtra(LAYOUT_TYPE, false);
        intent.putExtra(BANK_CARD_NO, cardNo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_bank_card_info);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        ViewUtils.inject(this);
        setTitle(R.string.input_bank_card_info);
        initViews();
        if(isResetPasswordType){

        }else{
            initData();
        }
    }

    private void initData() {
        ParseHttpListener<Bank> listener = new ParseHttpListener<Bank>() {
            @Override
            protected Bank parseDateTask(String jsonData) {
                try{
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    bank = DataParse.parseObjectJson(Bank.class, jsonData);
                }catch (Exception e){
                }
                return bank;
            }

            @Override
            protected void afterParseData(Bank bank) {
                if(!TextUtils.isEmpty(bank.getName())){
                    tvBank.setText(bank.getName());
                    btnStatus++;
                }
            }
        };
        new TradeEngineImpl().checkBank(bankCrardNo,listener.setLoadingDialog(mContext));

    }

    private void handleExtras(Bundle extras) {
        isResetPasswordType = extras.getBoolean(LAYOUT_TYPE);
        bankCrardNo = extras.getString(BANK_CARD_NO, "");
        mBankCard = (MyBankCard) extras.getSerializable(BANK_CARD);
    }

    private void initViews() {
        if (isResetPasswordType) {
            ll_bank_card.setVisibility(View.VISIBLE);
            ll_choose_bank_type.setVisibility(View.GONE);
            etBankCard.setHint(String.format(getResources().getString(R.string.blank_hint_card_no),mBankCard.getBank_card_no_tail()));
        } else {
            ll_bank_card.setVisibility(View.GONE);
            ll_choose_bank_type.setVisibility(View.VISIBLE);
        }
        etBankCard.addTextChangedListener(new MyTextWatcher());
        et_real_name.addTextChangedListener(new MyTextWatcher());
        et_id_card_no.addTextChangedListener(new MyTextWatcher());
        et_bank_card_mobile.addTextChangedListener(new MyTextWatcher());
    }
    private TradeEngineImpl tradeEngine  = new TradeEngineImpl();;
    private String mobile;
    @OnClick({R.id.ll_choose_bank_type, R.id.btn_bind_bank_card})
    public void onClick(View v) {
        //TODO 点击下一步
        if (v.getId() == R.id.ll_choose_bank_type) {
            //TODO 选择银行卡
            Intent intent = new Intent(this, ChooseBankActivity.class);
            startActivityForResult(intent, 0);
        } else if (isResetPasswordType) {

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
                        Utils.setPackageName(getPackageName());
                        Intent intent = new Intent(BankCardInfoActivity.this, Initialize.class);
                        intent.putExtra(CPGlobaInfo.XML_TAG, writeXml(object));
                        startActivity(intent);
                    }
                }
            };
            mobile = et_bank_card_mobile.getText().toString();
            tradeEngine.verifyIdentityAuth(bank.getId(), bankCrardNo,et_real_name.getText().toString(),et_id_card_no.getText().toString(),mobile,listener.setLoadingDialog(this));
//            tradeEngine.verifyIdentityAuth("6", "6217714900199342", "徐志成", "350322197412300517", "18106963378", listener.setLoadingDialog(this));
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

            se.startTag("", "env");
            se.text(bean.env.toUpperCase());
            se.endTag("", "env");

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
            if (bank == null) {
                bank = (Bank) data.getSerializableExtra(BANK);
                if (bank != null) {
                    btnStatus++;
                }
            }
            if (bank != null) {
                tvBank.setText(bank.getName());
                tvBank.setTag(bank.getId());
                checkBtnStatus();
            }
        }else if(requestCode == 1 && resultCode == 0){
            setResult(0);
            manualFinish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int btnStatus = 0;

    private void checkBtnStatus() {
        if (btnStatus == 4) {
            btn_bind_bank_card.setEnabled(true);
        } else {
            btn_bind_bank_card.setEnabled(false);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private boolean isBeforeNull;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            isBeforeNull = TextUtils.isEmpty(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
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
                        try{
                            JSONObject json = new JSONObject(jsonData);
                            if(json.has("status")){
                                return json.getBoolean("status");
                            }

                        }catch (Exception e){
                        }
                        return null;
                    }

                    @Override
                    protected void afterParseData(Boolean object) {
                        if(null != object){
                            if(object){
                                //TODO 设置过交易密码
                                PromptManager.showToast("已经设置过交易密码");
                                setResult(0);
                                manualFinish();
                            }else{
                                //TODO 没设置过交易密码
                                PromptManager.showToast("没有设置过交易密码");
                                startActivityForResult(TradePasswordSettingActivity.firstSetPwdIntent(mContext), 0);
                            }
                        }
                    }
                };
                PromptManager.showToast("绑卡成功，判断是否设置过交易密码");
                tradeEngine.isTradePasswordSet(isTradePwdSetListener);
            } else {//认证失败
            }

        }
        CPGlobaInfo.init(); //清空返回结果
    }

}
