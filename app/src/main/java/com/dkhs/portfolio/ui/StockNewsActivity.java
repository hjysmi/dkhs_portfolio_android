package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.StockNewsFragment;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * 个股新闻，公告，研报
 * Created by xuetong on 2016/1/13.
 */
public class StockNewsActivity extends ModelAcitivity {

    public static final String STOCK_NAME = "stock_name";
    public static final String SYMBOL = "symbol";
    public static final String CONTENT_TYPE = "content_type";

    public static Intent newIntent(Context context, String symboName, String symbol, String content_type) {
        Intent intent = new Intent(context, StockNewsActivity.class);
        intent.putExtra(STOCK_NAME, symboName);
        intent.putExtra(SYMBOL, symbol);
        intent.putExtra(CONTENT_TYPE, content_type);
        return intent;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_stocknews);
        setTitle(getIntent().getStringExtra(STOCK_NAME));
        setTitle(String.format(UIUtils.getResString(this, R.string.stocknews), getIntent().getStringExtra(STOCK_NAME)));
        replaceBottomFirstTabFragment(StockNewsFragment.newIntent(getIntent().getStringExtra(STOCK_NAME), getIntent().getStringExtra(STOCK_NAME)));

    }

    private void replaceBottomFirstTabFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ll_main, fragment).commitAllowingStateLoss();
    }


}
