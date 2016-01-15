package com.dkhs.portfolio.ui;

import android.os.Bundle;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.ExchangeHistoryFragment;

/**
 * 主贴详情
 */
public class ExchangeHistoryActivity extends ModelAcitivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_history);
        setTitle(R.string.exchange_history);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new ExchangeHistoryFragment()).commitAllowingStateLoss();
    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_exchangehistory;
    }
}
