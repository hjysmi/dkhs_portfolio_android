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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.KChartsFragment;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartFragment;
import com.dkhs.portfolio.ui.fragment.TestFragment;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.google.gson.JsonObject;

/**
 * @ClassName StockQuotesActivity
 * @Description 个股行情
 * @author zjz
 * @date 2014-9-26 上午10:22:32
 * @version 1.0
 */
public class StockQuotesActivity extends ModelAcitivity implements OnClickListener {

    private SelectStockBean mStockBean;

    public static final String EXTRA_STOCK = "extra_stock";
    private final int REQUESTCODE_SELECT_STOCK = 901;

    private TextView tvCurrent;
    private TextView tvHigh;
    private TextView tvLow;
    private TextView tvOpen;
    private TextView tvChange;
    private TextView tvPercentage;
    private Button btnAddOptional;
    private ScrollView mScrollview; //滚动条，用于滚动到头部

    private QuotesEngineImpl mQuotesEngine;
    private StockQuotesBean mStockQuotesBean;
    private long mStockId;

    private StockQuotesChartFragment mStockQuotesChartFragment;

    public static Intent newIntent(Context context, SelectStockBean bean) {
        Intent intent = new Intent(context, StockQuotesActivity.class);

        intent.putExtra(EXTRA_STOCK, bean);

        return intent;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_stockquotes);
        mQuotesEngine = new QuotesEngineImpl();
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
            mStockId = mStockBean.id;

        }

        tvCurrent = (TextView) findViewById(R.id.tv_current_price);
        tvHigh = (TextView) findViewById(R.id.tv_highest_value);
        tvLow = (TextView) findViewById(R.id.tv_lowest_value);
        tvOpen = (TextView) findViewById(R.id.tv_today_open_value);
        tvChange = (TextView) findViewById(R.id.tv_up_price);
        tvPercentage = (TextView) findViewById(R.id.tv_percentage);
        btnAddOptional = (Button) findViewById(R.id.btn_add_optional);
        btnAddOptional.setOnClickListener(this);

        Button addButton = getRightButton();
        addButton.setBackgroundResource(R.drawable.ic_search_title);
        addButton.setOnClickListener(mSearchClick);

        Button btnRefresh = getSecondRightButton();
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);

        initTabPage();
        // setupViewData();
        
        //scrollview + listview 会滚动到底部，需要滚动到头部
        scrollToTop();
    }
    
    private void scrollToTop() {
    	mScrollview = (ScrollView) findViewById(R.id.sc_content);
        mScrollview.smoothScrollTo(0, 0);		
	}

    private void setupViewData() {
        if (null != mQuotesEngine) {
            mQuotesEngine.quotes(mStockBean.code, listener);
        }
    }

    ParseHttpListener listener = new ParseHttpListener<StockQuotesBean>() {

        @Override
        protected StockQuotesBean parseDateTask(String jsonData) {
            StockQuotesBean stockQuotesBean = null;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                JSONObject jsonOb = jsonArray.getJSONObject(0);

                stockQuotesBean = DataParse.parseObjectJson(StockQuotesBean.class, jsonOb);

            } catch (JSONException e) {

                e.printStackTrace();
            }
            return stockQuotesBean;
        }

        @Override
        protected void afterParseData(StockQuotesBean object) {
            if (null != object) {
                mStockQuotesBean = object;
                updateStockView();
                mStockQuotesChartFragment.setStockQuotesBean(mStockQuotesBean);
            }

        }
    };

    private void initTabPage() {

        String[] titleArray = getResources().getStringArray(R.array.quotes_title);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        mStockQuotesChartFragment = StockQuotesChartFragment.newInstance(StockQuotesChartFragment.TREND_TYPE_TODAY);
        fragmentList.add(mStockQuotesChartFragment);

        fragmentList.add(KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_DAY,mStockId));
        fragmentList.add(KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_WEEK,mStockId));
        fragmentList.add(KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_MONTH,mStockId));
        // fragmentList.add(new TestFragment());
        ScrollViewPager pager = (ScrollViewPager) this.findViewById(R.id.pager);
        pager.setCanScroll(false);
        pager.setAdapter(new MyPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, titleArray));

        TabPageIndicator indicator = (TabPageIndicator) this.findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }

    protected void updateStockView() {
        if (null != mStockQuotesBean) {

            tvCurrent.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));
            tvChange.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));
            tvPercentage.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));

            tvCurrent.setText(StringFromatUtils.get2Point(mStockQuotesBean.getCurrent()));
            tvChange.setText(StringFromatUtils.get2Point(mStockQuotesBean.getChange()));

            tvHigh.setTextColor(getTextColor(mStockQuotesBean.getHigh() - mStockQuotesBean.getLastClose()));
            tvLow.setTextColor(getTextColor(mStockQuotesBean.getLow() - mStockQuotesBean.getLastClose()));
            tvOpen.setTextColor(getTextColor(mStockQuotesBean.getOpen() - mStockQuotesBean.getLastClose()));

            tvHigh.setText(StringFromatUtils.get2Point(mStockQuotesBean.getHigh()));
            tvLow.setText(StringFromatUtils.get2Point(mStockQuotesBean.getLow()));
            tvOpen.setText(StringFromatUtils.get2Point(mStockQuotesBean.getOpen()));
            tvPercentage.setText(StringFromatUtils.get2PointPercent(mStockQuotesBean.getPercentage()));
        }
    }

    private ColorStateList getTextColor(float value) {
        if (value < 0) {

            return (ColorStateList) getResources().getColorStateList(R.color.green);

        }
        return (ColorStateList) getResources().getColorStateList(R.color.red);
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

    private boolean hasFollow = true;
    IHttpListener baseListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            hasFollow = !hasFollow;
            if (hasFollow) {

                btnAddOptional.setText(R.string.delete_fllow);
            } else {

                btnAddOptional.setText(R.string.add_fllow);
            }
        }
    };

    Handler dataHandler = new Handler();

    public void onStart() {
        super.onStart();

        dataHandler.postDelayed(runnable, 6);// 打开定时器，60ms后执行runnable操作

    };

    public void onStop() {
        super.onStop();
        dataHandler.removeCallbacks(runnable);// 关闭定时器处理

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // dataHandler.sendEmptyMessage(1722);
            
            setupViewData();
            dataHandler.postDelayed(this, 60 * 1000);// 隔60s再执行一次
        }
    };

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param v
     * @return
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_optional:

                if (hasFollow) {
                    mQuotesEngine.delfollow(mStockBean.id, baseListener);
                } else {
                    mQuotesEngine.symbolfollow(mStockBean.id, baseListener);
                }

                break;

            default:
                break;
        }

    }

}
