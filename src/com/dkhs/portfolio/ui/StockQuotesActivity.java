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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FiveRangeItem;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentNewsList;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentreportNewsList;
import com.dkhs.portfolio.ui.fragment.KChartsFragment;
import com.dkhs.portfolio.ui.fragment.NewsFragment;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartFragment;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName StockQuotesActivity
 * @Description 个股行情
 * @author zjz
 * @date 2014-9-26 上午10:22:32
 * @version 1.0
 */
public class StockQuotesActivity extends ModelAcitivity implements OnClickListener, ITouchListener {

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
    private InterceptScrollView mScrollview; // 滚动条，用于滚动到头部

    private QuotesEngineImpl mQuotesEngine;
    private StockQuotesBean mStockQuotesBean;
    private long mStockId;
    private String mStockCode;
    private Context context;

    private HScrollTitleView hsTitle;
    // privaet view
    private ScrollViewPager pager;

    private StockQuotesChartFragment mStockQuotesChartFragment;
    private LinearLayout stockLayout;

    public static Intent newIntent(Context context, SelectStockBean bean) {
        Intent intent = new Intent(context, StockQuotesActivity.class);

        intent.putExtra(EXTRA_STOCK, bean);

        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        setIntent(intent);// must store the new intent unless getIntent() will return the old one

        processExtraData();

    }

    private void processExtraData() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        if (null != mStockBean) {

            mStockId = mStockBean.id;
            mStockCode = mStockBean.code;
            updateStockInfo();
        }
        // setAddOptionalButton();
        initTabPage();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_stockquotes);
        context = this;
        mQuotesEngine = new QuotesEngineImpl();
        // handle intent extras
        initView();
        processExtraData();
        initList();
    }

    private void handleExtras(Bundle extras) {
        mStockBean = (SelectStockBean) extras.getSerializable(EXTRA_STOCK);
    }

    private void initList() {
        String[] name = new String[4];
        name[0] = "新闻";
        name[1] = "公告";
        name[2] = "研报";
        name[3] = "F10";
        NewsforImpleEngine vo;
        List<Fragment> frag = new ArrayList<Fragment>();
        Fragment f1 = new FragmentNewsList();
        Bundle b1 = new Bundle();
        b1.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWSFOREACH);
        vo = new NewsforImpleEngine();
        vo.setSymbol(mStockBean.code);
        vo.setContentType("10");
        b1.putSerializable(FragmentNewsList.VO, vo);
        f1.setArguments(b1);
        frag.add(f1);
        Fragment f2 = new FragmentNewsList();
        Bundle b2 = new Bundle();
        b2.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWSFOREACH);
        vo = new NewsforImpleEngine();
        vo.setSymbol(mStockBean.code);
        vo.setContentType("20");
        b2.putSerializable(FragmentNewsList.VO, vo);
        f2.setArguments(b2);
        frag.add(f2);
        Fragment f4 = new FragmentreportNewsList();
        Bundle b4 = new Bundle();
        b4.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWS_OPITION_FOREACH);
        vo = new NewsforImpleEngine();
        vo.setSymbol(mStockBean.code);
        b4.putSerializable(FragmentNewsList.VO, vo);
        f4.setArguments(b4);
        frag.add(f4);
        Fragment f3 = new NewsFragment();
        frag.add(f3);
        new FragmentSelectAdapter(context, name, frag, stockLayout, getSupportFragmentManager());
    }

    private void initView() {

        tvCurrent = (TextView) findViewById(R.id.tv_current_price);
        tvHigh = (TextView) findViewById(R.id.tv_highest_value);
        tvLow = (TextView) findViewById(R.id.tv_lowest_value);
        tvOpen = (TextView) findViewById(R.id.tv_today_open_value);
        tvChange = (TextView) findViewById(R.id.tv_up_price);
        tvPercentage = (TextView) findViewById(R.id.tv_percentage);
        btnAddOptional = (Button) findViewById(R.id.btn_add_optional);
        btnAddOptional.setVisibility(View.GONE);
        stockLayout = (LinearLayout) findViewById(R.id.stock_layout);
        btnAddOptional.setOnClickListener(this);
        hsTitle = (HScrollTitleView) findViewById(R.id.hs_title);
        String[] titleArray = getResources().getStringArray(R.array.quotes_title);
        hsTitle.setTitleList(titleArray, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        hsTitle.setSelectPositionListener(titleSelectPostion);
        Button addButton = getRightButton();
        addButton.setBackgroundResource(R.drawable.ic_search_title);
        addButton.setOnClickListener(mSearchClick);

        Button btnRefresh = getSecondRightButton();
        btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
        stockLayout.setOnTouchListener(new OnLayoutlistener());
        initTabPage();
        // setupViewData();

        // scrollview + listview 会滚动到底部，需要滚动到头部
        scrollToTop();
        // setAddOptionalButton();
    }

    private void setAddOptionalButton() {
        if (mStockQuotesBean == null) {
            btnAddOptional.setVisibility(View.GONE);
            return;
        }
        btnAddOptional.setVisibility(View.VISIBLE);
        if (mStockQuotesBean.isFollowed() && null != btnAddOptional) {
            btnAddOptional.setText(R.string.delete_fllow);
            btnAddOptional.setBackgroundResource(R.drawable.bg_unfollowed);
            btnAddOptional.setTextColor(ColorTemplate.getTextColor(R.color.unfollowd));

        } else if (null != btnAddOptional) {
            btnAddOptional.setBackgroundResource(R.drawable.btn_blue_selector);
            btnAddOptional.setText(R.string.add_fllow);
            btnAddOptional.setTextColor(ColorTemplate.getTextColor(R.color.white));
        }
    }

    private void scrollToTop() {
        mScrollview = (InterceptScrollView) findViewById(R.id.sc_content);
        mScrollview.smoothScrollTo(0, 0);
    }

    private void setupViewData() {
        if (null != mQuotesEngine && mStockBean != null) {
            mQuotesEngine.quotes(mStockBean.code, listener);
        }
    }

    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                pager.setCurrentItem(position);
            }
        }
    };

    ParseHttpListener listener = new ParseHttpListener<StockQuotesBean>() {

        @Override
        protected StockQuotesBean parseDateTask(String jsonData) {
            StockQuotesBean stockQuotesBean = null;
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                JSONObject jsonOb = jsonArray.getJSONObject(0);

                stockQuotesBean = DataParse.parseObjectJson(StockQuotesBean.class, jsonOb);
                List<FiveRangeItem> buyList = new ArrayList<FiveRangeItem>();
                List<FiveRangeItem> sellList = new ArrayList<FiveRangeItem>();
                int i = 0;
                for (String buyPrice : stockQuotesBean.getBuyPrice().getBuyPrice()) {
                    FiveRangeItem buyItem = new FiveRangeItem();

                    buyItem.price = buyPrice;
                    if (i < stockQuotesBean.getBuyPrice().getBuyVol().size()) {
                        buyItem.vol = stockQuotesBean.getBuyPrice().getBuyVol().get(i);
                    } else {
                        buyItem.vol = "0";
                    }
                    buyItem.tag = "" + (++i);
                    buyList.add(buyItem);
                }
                // // i = 0;
                for (int j = 4; j >= 0; j--) {
                    FiveRangeItem sellItem = new FiveRangeItem();
                    if (j < stockQuotesBean.getSellPrice().getSellVol().size()) {
                        sellItem.price = stockQuotesBean.getSellPrice().getSellPrice().get(j);
                        sellItem.vol = stockQuotesBean.getSellPrice().getSellVol().get(j);
                    } else {
                        sellItem.vol = "0";
                    }
                    sellItem.tag = "" + (j + 1);
                    sellList.add(sellItem);
                }

                stockQuotesBean.setBuyList(buyList);
                stockQuotesBean.setSellList(sellList);
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
                setAddOptionalButton();
            }

        }
    };

    private void initTabPage() {

        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        mStockQuotesChartFragment = StockQuotesChartFragment.newInstance(StockQuotesChartFragment.TREND_TYPE_TODAY,
                mStockCode);
        mStockQuotesChartFragment.setITouchListener(this);
        fragmentList.add(mStockQuotesChartFragment);
        KChartsFragment fragment = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_DAY, mStockCode);
        fragment.setITouchListener(this);
        fragmentList.add(fragment);
        KChartsFragment fragment2 = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_WEEK, mStockCode);
        fragment2.setITouchListener(this);
        fragmentList.add(fragment2);
        KChartsFragment fragment3 = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_MONTH, mStockCode);
        fragment3.setITouchListener(this);
        fragmentList.add(fragment3);
        // fragmentList.add(new TestFragment());
        pager = (ScrollViewPager) this.findViewById(R.id.pager);
        pager.setCanScroll(false);
        pager.setAdapter(new MyPagerFragmentAdapter(getSupportFragmentManager(), fragmentList));

        // TabPageIndicator indicator = (TabPageIndicator) this.findViewById(R.id.indicator);
        // indicator.setViewPager(pager);

    }

    protected void updateStockView() {
        if (null != mStockQuotesBean) {

            tvCurrent.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));
            tvChange.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));
            tvPercentage.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));

            tvCurrent.setText(StringFromatUtils.get2Point(mStockQuotesBean.getCurrent()));
            tvChange.setText(StringFromatUtils.get2PointPlus(mStockQuotesBean.getChange()));

            // tvHigh.setTextColor(getTextColor(mStockQuotesBean.getHigh() - mStockQuotesBean.getLastClose()));
            // tvLow.setTextColor(getTextColor(mStockQuotesBean.getLow() - mStockQuotesBean.getLastClose()));
            tvOpen.setTextColor(getTextColor(mStockQuotesBean.getOpen() - mStockQuotesBean.getLastClose()));

            tvHigh.setText(StringFromatUtils.get2Point(mStockQuotesBean.getHigh()));
            tvLow.setText(StringFromatUtils.get2Point(mStockQuotesBean.getLow()));
            tvOpen.setText(StringFromatUtils.get2Point(mStockQuotesBean.getOpen()));
            tvPercentage.setText(StringFromatUtils.get2PointPercentPlus(mStockQuotesBean.getPercentage()));
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

        // private String[] titleList;

        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2) {
            super(fm);
            this.fragmentList = fragmentList2;
            // this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // return (titleList.length > position) ? titleList[position] : "";
            return "";
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
            finish();
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
                        updateStockInfo();
                        setAddOptionalButton();
                    }
                    break;
            }
        }
    }

    private void updateStockInfo() {
        setTitle(mStockBean.name);
        setTitleTipString(mStockBean.code);
    }

    // private boolean hasFollow = true;
    IHttpListener baseListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            mStockQuotesBean.setFollowed(!mStockQuotesBean.isFollowed());
            setAddOptionalButton();
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

                if (mStockQuotesBean.isFollowed()) {
                    mQuotesEngine.delfollow(mStockBean.id, baseListener);
                } else {
                    mQuotesEngine.symbolfollow(mStockBean.id, baseListener);
                }

                break;

            default:
                break;
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();
        listener.stopRequest(true);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void chartTounching() {
        if (mScrollview != null) {
            mScrollview.setIsfocus(true);
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loseTouching() {
        if (mScrollview != null) {
            mScrollview.setIsfocus(false);
        }

    }

    class OnLayoutlistener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    chartTounching();
                    break;
                case MotionEvent.ACTION_UP:
                    loseTouching();
                    break;
                default:
                    break;
            }
            return false;
        }

    }
}
