package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dkhs.portfolio.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by zhangcm on 2015/9/24.15:57
 */
public class ResetTradePasswordActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_reset_trade_password);
        ViewUtils.inject(this);
        setTitle(R.string.reset_trade_password);
    }

    @OnClick({R.id.rl_remember_original_trade_password,R.id.rl_forget_original_trade_password})
    private void onClick(View view){
        if(view.getId() == R.id.rl_remember_original_trade_password){
            startActivity(TradePasswordSettingActivity.resetPwdIntent(mContext));
        }else{
            startActivity(new Intent(mContext, ForgetTradePasswordActivity.class));
        }

    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_reset_trade_password;
    }

}
