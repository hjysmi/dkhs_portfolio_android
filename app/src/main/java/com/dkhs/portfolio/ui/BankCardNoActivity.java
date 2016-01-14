package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.dkhs.portfolio.bean.Bank;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.bean.IdentityInfoBean;
import com.dkhs.portfolio.engine.TradeEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.utils.ActivityCode;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by zhangcm on 2015/9/14.15:02
 */
public class BankCardNoActivity extends ModelAcitivity implements View.OnClickListener{

    @ViewInject(R.id.btn_next)
    private Button btnNext;

    @ViewInject(R.id.et_bank_card)
    private EditText etBankcard;

    @ViewInject(R.id.tv_verified_tip)
    private TextView tvVerifiedTip;
    private IdentityInfoBean identityInfoBean;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_bank_card_no);
        ViewUtils.inject(this);
        setTitle(R.string.input_bank_card);
        etBankcard.addTextChangedListener(new TextWatcher() {
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
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (isChanged) {
                    location = etBankcard.getSelectionEnd();
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

                    etBankcard.setText(str);
                    Editable etable = etBankcard.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                    return;
                }
                if (!TextUtils.isEmpty(s) && s.length() >= 5) {
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setEnabled(false);
                }
            }
        });
        new TradeEngineImpl().getIdentityVerification(new ParseHttpListener<IdentityInfoBean>() {
            @Override
            public void onFailure(ErrorBundle errorBundle) {
            }
            @Override
            protected IdentityInfoBean parseDateTask(String jsonData) {
                return DataParse.parseObjectJson(IdentityInfoBean.class, jsonData);
            }

            @Override
            protected void afterParseData(IdentityInfoBean object) {
                identityInfoBean = object;
                if(object != null && object.status == 1){
                    //说明认证成功
                    tvVerifiedTip.setText(String.format(UIUtils.getResString(mContext,R.string.blank_verified_tip),identityInfoBean.real_name));
                }
            }
        });
    }
    private String bankCardNo;
    @OnClick(R.id.btn_next)
    public void onClick(View v){
        //TODO 点击下一步
        bankCardNo = etBankcard.getText().toString().trim().replace(" ","");
        ParseHttpListener<Bank> listener = new ParseHttpListener<Bank>() {
            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
            }

            @Override
            protected Bank parseDateTask(String jsonData) {
                Bank bank = null;
                try {
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    bank = DataParse.parseObjectJson(Bank.class, jsonData);
                } catch (Exception e) {
                }
                return bank;
            }

            @Override
            protected void afterParseData(Bank bank) {
                startActivityForResult(BankCardInfoActivity.bankCardInfoIntent(mContext,bankCardNo, bank,identityInfoBean), ActivityCode.BANK_CARD_INFO_REQUEST.ordinal());
            }
        };
        new TradeEngineImpl().checkBank(bankCardNo, listener.setLoadingDialog(mContext));
    }

    private ParseHttpListener<List<BindThreePlat>> bindsListener = new ParseHttpListener<List<BindThreePlat>>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }


        @Override
        protected List<BindThreePlat> parseDateTask(String jsonData) {

            return DataParse.parseArrayJson(BindThreePlat.class, jsonData);
        }

        @Override
        protected void afterParseData(List<BindThreePlat> entity) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ActivityCode.BANK_CARD_INFO_REQUEST.ordinal() && resultCode == ActivityCode.BANK_CARD_INFO_RESULT.ordinal()){
            setResult(ActivityCode.BANK_CARD_NO_RESULT.ordinal());
            manualFinish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
