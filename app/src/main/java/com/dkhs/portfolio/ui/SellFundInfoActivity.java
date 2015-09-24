package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by zhangcm on 2015/9/24.15:56
 */
public class SellFundInfoActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_sell_fund_info);
        ViewUtils.inject(this);
        setTitle(R.string.sell_fund_info);
    }

}
