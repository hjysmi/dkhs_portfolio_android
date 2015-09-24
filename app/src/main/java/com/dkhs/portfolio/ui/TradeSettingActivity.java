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
public class TradeSettingActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trade_setting);
        ViewUtils.inject(this);
        setTitle(R.string.setting);
    }

    @OnClick(R.id.rl_reset_trade_password)
    private void onClick(View view){
        startActivity(new Intent(mContext, ResetTradePasswordActivity.class));
    }

}
