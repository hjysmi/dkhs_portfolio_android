package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.fragment.MyFundsBuyFragment;
import com.dkhs.portfolio.ui.fragment.MyFundsSellFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by zhangcm on 2015/9/21.10:47
 */
public class TradeRecordActivity extends AssestsBaseActivity{
    @ViewInject(R.id.ll_trade_record)
    private LinearLayout llTradeRecord;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trade_record);
        ViewUtils.inject(this);
        setTitle(R.string.trade_record);
        initViews();
    }

    private FragmentSelectAdapter mAdpter;

    private void initViews(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyFundsBuyFragment());
        fragments.add(new MyFundsSellFragment());

        mAdpter = new FragmentSelectAdapter(this, getResources().getStringArray(R.array.my_funds_title), fragments, llTradeRecord, getSupportFragmentManager());
    }

}
