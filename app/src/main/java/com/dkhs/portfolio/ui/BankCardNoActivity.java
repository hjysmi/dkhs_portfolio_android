package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.Button;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
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

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_bank_card_no);
        ViewUtils.inject(this);
        setTitle(R.string.input_bank_card);
        etBankcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s) && s.length() >= 5){
                    btnNext.setEnabled(true);
                }else{
                    btnNext.setEnabled(false);
                }
            }
        });
    }
    @OnClick(R.id.btn_next)
    public void onClick(View v){
        //TODO 点击下一步
        startActivityForResult(BankCardInfoActivity.bankCardInfoIntent(mContext, etBankcard.getText().toString()),1);
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
        if(requestCode == 1 && resultCode ==0){
            setResult(1);
            manualFinish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
