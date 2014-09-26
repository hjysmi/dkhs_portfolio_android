/**
 * @Title StockQuotesActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-26 上午10:22:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartFragment;
import com.dkhs.portfolio.ui.fragment.TestFragment;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;

/**
 * @ClassName StockQuotesActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-26 上午10:22:32
 * @version 1.0
 */
public class StockQuotesActivity extends ModelAcitivity {
    private SelectStockBean mStockBean;

    public static final String EXTRA_STOCK = "extra_stock";
    private final int REQUESTCODE_SELECT_STOCK = 901;

    public static Intent newIntent(Context context, SelectStockBean bean) {
        Intent intent = new Intent(context, StockQuotesActivity.class);

        intent.putExtra(EXTRA_STOCK, bean);

        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_stockquotes);

        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        initView();
    }

    private void handleExtras(Bundle extras) {
        mStockBean = (SelectStockBean) extras.getSerializable(EXTRA_STOCK);

    }

    private void initView() {
        if (null != mStockBean) {
            setTitle(mStockBean.name);

        }
        Button addButton = getRightButton();
        addButton.setBackgroundResource(R.drawable.ic_search_title);
        addButton.setOnClickListener(mSearchClick);

        Button btnRefresh = getSecondRightButton();
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);

        initTabPage();
        setupViewData();
    }

    private void setupViewData() {
    }

    private void initTabPage() {

        String[] titleArray = getResources().getStringArray(R.array.quotes_title);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        fragmentList.add(StockQuotesChartFragment.newInstance(StockQuotesChartFragment.TREND_TYPE_TODAY));
        fragmentList.add(StockQuotesChartFragment.newInstance(StockQuotesChartFragment.TREND_TYPE_SEVENDAY));
        fragmentList.add(StockQuotesChartFragment.newInstance(StockQuotesChartFragment.TREND_TYPE_MONTH));
        fragmentList.add(StockQuotesChartFragment.newInstance(StockQuotesChartFragment.TREND_TYPE_HISTORY));
        // fragmentList.add(new TestFragment());
        // fragmentList.add(new TestFragment());
        // fragmentList.add(new TestFragment());
        // fragmentList.add(new TestFragment());
        ScrollViewPager pager = (ScrollViewPager) this.findViewById(R.id.pager);
        pager.setCanScroll(false);
        pager.setAdapter(new MyPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, titleArray));

        TabPageIndicator indicator = (TabPageIndicator) this.findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }

    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private String[] titleList;

        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2, String[] titleList) {
            super(fm);
            this.fragmentList = fragmentList2;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.length > position) ? titleList[position] : "";
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }

    OnClickListener mSearchClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StockQuotesActivity.this, SelectAddOptionalActivity.class);
            startActivityForResult(intent, REQUESTCODE_SELECT_STOCK);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_SELECT_STOCK:

                    SelectStockBean selectBean = (SelectStockBean) data
                            .getSerializableExtra(FragmentSelectStockFund.ARGUMENT);
                    if (null != selectBean) {
                        mStockBean = selectBean;
                        setTitle(selectBean.name);
                    }
                    break;
            }
        }
    }

}
