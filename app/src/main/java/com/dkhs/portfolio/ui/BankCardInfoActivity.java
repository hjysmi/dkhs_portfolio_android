package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by zhangcm on 2015/9/16.15:02
 */
public class BankCardInfoActivity extends ModelAcitivity {


    private boolean isResetPasswordType;
    private static final String LAYOUT_TYPE = "layout_type";

    @ViewInject(R.id.et_bank_card)
    private EditText etBankCard;

    @ViewInject(R.id.ll_bank_card)
    private View ll_bank_card;

    @ViewInject(R.id.ll_choose_bank_type)
    private View ll_choose_bank_type;

    public static Intent forgetTradePasswordIntent(Context context){
        Intent intent = new Intent(context, BankCardInfoActivity.class);
        intent.putExtra(LAYOUT_TYPE, true);
        return intent;
    }

    public static Intent bankCardInfoIntent(Context context){
        Intent intent = new Intent(context, BankCardInfoActivity.class);
        intent.putExtra(LAYOUT_TYPE, false);
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
    }

    private void handleExtras(Bundle extras) {
        isResetPasswordType = extras.getBoolean(LAYOUT_TYPE);
    }

    private void initViews(){
        if(isResetPasswordType){
            ll_bank_card.setVisibility(View.VISIBLE);
            ll_choose_bank_type.setVisibility(View.GONE);
        }else{
            ll_bank_card.setVisibility(View.GONE);
            ll_choose_bank_type.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.ll_choose_bank_type})
    private void onclick(View v){
        //TODO 点击下一步
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

}
