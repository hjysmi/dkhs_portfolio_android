/**
 * 
 * @Title StockQuotesActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-26 上午10:22:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
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
import com.dkhs.portfolio.ui.fragment.FragmentForOptionOnr;
import com.dkhs.portfolio.ui.fragment.FragmentNewsList;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentreportNewsList;
import com.dkhs.portfolio.ui.fragment.KChartsFragment;
import com.dkhs.portfolio.ui.fragment.NewsFragment;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartFragment;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.InterceptScrollView.ScrollViewListener;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName StockQuotesActivity
 * @Description 个股行情
 * @author zjz
 * @date 2014-9-26 上午10:22:32
 * @version 1.0
 */
public class StockQuotesActivity extends ModelAcitivity implements OnClickListener, ITouchListener, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 15121212311111156L;

    private SelectStockBean mStockBean;

    public static final String EXTRA_STOCK = "extra_stock";

    protected static final int MSG_WHAT_BEFORE_REQUEST = 99;

    protected static final int MSG_WHAT_AFTER_REQUEST = 97;
    private final int REQUESTCODE_SELECT_STOCK = 901;

    private TextView tvCurrent;
    private TextView tvHigh;
    private TextView tvLow;
    private TextView tvOpen;
    private TextView tvChange;
    private TextView tvPercentage;
    private Button btnRefresh;

    private TextView tvChengjiaoLiang;
    private TextView tvChengjiaoE;
    private TextView tvHuanShouLv;
    private TextView tvLiuzhi;
    private TextView tvZongzhi;
    private TextView tvShiying;
    private TextView tvShiJing;

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
    private ArrayList<Fragment> fragmentList;
    private StockQuotesChartFragment mStockQuotesChartFragment;
    private LinearLayout stockLayout;
    private FragmentSelectAdapter mFragmentSelectAdapter;
    private StockQuotesActivity layouts;
    private View viewHeader;
    private String symbolType;

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

    }

    public void setLayoutHeight(int position) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        android.view.ViewGroup.LayoutParams l = stockLayout.getLayoutParams();
        /*
         * if (0 == position) {
         * l.height = LayoutParams.MATCH_PARENT;
         * }
         */
        if (position < 3) {
            position = 3;
        }
        l.height = position * getResources().getDimensionPixelOffset(R.dimen.layout_height);
    }

    public void setLayoutHeights(int height) {
        android.view.ViewGroup.LayoutParams l = stockLayout.getLayoutParams();
        l.height = height;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_stockquotes);
        context = this;
        layouts = this;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        mQuotesEngine = new QuotesEngineImpl();

        // handle intent extras
        processExtraData();
        initView();
        setupViewDatas();
        android.view.ViewGroup.LayoutParams l = stockLayout.getLayoutParams();
        l.height = getResources().getDimensionPixelOffset(R.dimen.layout_height) * 2;// dm.heightPixels * 3 / 2 -
        // getResources().getDimensionPixelOffset(R.dimen.layout_height);
        initList();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void setupViewDatas() {
        if (null != mStockBean) {

            mStockId = mStockBean.id;
            mStockCode = mStockBean.code;
            symbolType = mStockBean.symbol_type;
            updateStockInfo();
        }
        // setAddOptionalButton();
        initTabPage();

    }

    private void handleExtras(Bundle extras) {
        mStockBean = (SelectStockBean) extras.getSerializable(EXTRA_STOCK);
    }

    private void initList() {
        String[] name = new String[3];
        if ((null != mStockBean.symbol_type && mStockBean.symbol_type.equals("5"))) {
            name = new String[2];
        }
        // name[0] = "新闻";
        name[0] = "公告";
        name[1] = "研报";
        if (!(null != mStockBean.symbol_type && mStockBean.symbol_type.equals("5"))) {
            name[2] = "F10";
        }
        NewsforImpleEngine vo;
        List<Fragment> frag = new ArrayList<Fragment>();
        Fragment f1 = new FragmentNewsList();
        Bundle b1 = new Bundle();
        b1.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWSFOREACH);
        vo = new NewsforImpleEngine();
        vo.setSymbol(mStockBean.code);
        vo.setContentType("10");
        vo.setPageTitle("新闻正文");
        // vo.setLayout(stockLayout);
        b1.putSerializable(FragmentNewsList.VO, vo);
        // b1.putSerializable(FragmentNewsList.LAYOUT, layouts);
        f1.setArguments(b1);
        // frag.add(f1);
        Fragment f2 = new FragmentNewsList();
        Bundle b2 = new Bundle();
        b2.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWSFOREACH);
        vo = new NewsforImpleEngine();
        vo.setSymbol(mStockBean.code);
        vo.setContentType("20");
        vo.setPageTitle("公告正文");
        // vo.setLayout(stockLayout);
        b2.putSerializable(FragmentNewsList.VO, vo);
        // b2.putSerializable(FragmentNewsList.LAYOUT, layouts);
        f2.setArguments(b2);
        frag.add(f2);
        Fragment f4 = FragmentForOptionOnr.newIntent(context, mStockBean.code, mStockBean.name, "");
        Bundle b4 = new Bundle();
        b4.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWS_OPITION_FOREACH);
        vo = new NewsforImpleEngine();
        vo.setSymbol(mStockBean.code);
        vo.setPageTitle("研报正文");
        // vo.setLayout(stockLayout);
        b4.putSerializable(FragmentNewsList.VO, vo);
        // b4.putSerializable(FragmentNewsList.LAYOUT, layouts);
        // f4.setArguments(b4);
        frag.add(f4);
        if (!(null != mStockBean.symbol_type && mStockBean.symbol_type.equals("5"))) {
            Fragment f3 = new NewsFragment();
            Bundle b3 = new Bundle();
            b3.putSerializable(StockQuotesActivity.EXTRA_STOCK, mStockBean);
            frag.add(f3);
        }
        FragmentSelectAdapter mFragmentSelectAdapter = new FragmentSelectAdapter(context, name, frag, stockLayout,
                getSupportFragmentManager());
        mFragmentSelectAdapter.setScrollAble(false);
        mFragmentSelectAdapter.setOutLaoyout(layouts);
        // views.setOnTouchListener(new OnView());
    }

    private boolean isIndexType() {
        return null != mStockBean && mStockBean.symbol_type != null
                && mStockBean.symbol_type.equalsIgnoreCase(StockUitls.SYMBOLTYPE_INDEX);
    }

    private void initView() {
        ViewStub viewstub;
        if (isIndexType()) {
            viewstub = (ViewStub) findViewById(R.id.layout_index_header);

        } else {
            viewstub = (ViewStub) findViewById(R.id.layout_stock_header);

        }
        if (viewstub != null) {
            viewHeader = viewstub.inflate();

            // views = findViewById(R.id.layout_view);
            tvCurrent = (TextView) viewHeader.findViewById(R.id.tv_current_price);
            tvHigh = (TextView) viewHeader.findViewById(R.id.tv_highest_value);
            tvLow = (TextView) viewHeader.findViewById(R.id.tv_lowest_value);
            tvOpen = (TextView) viewHeader.findViewById(R.id.tv_today_open_value);
            tvChange = (TextView) viewHeader.findViewById(R.id.tv_up_price);

            tvChengjiaoLiang = (TextView) viewHeader.findViewById(R.id.tv_liang_value);
            tvChengjiaoE = (TextView) viewHeader.findViewById(R.id.tv_e_value);
            tvHuanShouLv = (TextView) viewHeader.findViewById(R.id.tv_huan_value);
            tvLiuzhi = (TextView) viewHeader.findViewById(R.id.tv_liuzhi_value);
            tvZongzhi = (TextView) viewHeader.findViewById(R.id.tv_zongzhi_value);
            tvShiying = (TextView) viewHeader.findViewById(R.id.tv_shiying_value);
            tvShiJing = (TextView) viewHeader.findViewById(R.id.tv_shijing_value);

            tvPercentage = (TextView) viewHeader.findViewById(R.id.tv_percentage);
            btnAddOptional = (Button) viewHeader.findViewById(R.id.btn_add_optional);
            btnAddOptional.setVisibility(View.GONE);
            btnAddOptional.setOnClickListener(this);

        }
        findViewById(R.id.stock_land).setOnClickListener(this);
        stockLayout = (LinearLayout) findViewById(R.id.stock_layout);
        hsTitle = (HScrollTitleView) findViewById(R.id.hs_title);
        String[] titleArray = getResources().getStringArray(R.array.quotes_title);
        hsTitle.setTitleList(titleArray, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        hsTitle.setSelectPositionListener(titleSelectPostion);
        Button addButton = getRightButton();
        // addButton.setBackgroundResource(R.drawable.ic_search_title);
        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
                null, null, null);
        addButton.setOnClickListener(mSearchClick);

        btnRefresh = getSecondRightButton();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh_selector),
                null, null, null);
        // btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
        btnRefresh.setOnClickListener(this);

        // stockLayout.setOnTouchListener(new OnLayoutlistener());
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
            btnAddOptional.setBackgroundResource(R.drawable.btn_addoptional_selector);
            btnAddOptional.setText(R.string.add_fllow);
            btnAddOptional.setTextColor(ColorTemplate.getTextColor(R.color.white));
        }
    }

    private void scrollToTop() {
        mScrollview = (InterceptScrollView) findViewById(R.id.sc_content);
        // mScrollview.smoothScrollTo(0, 0);
        mScrollview.setScrollViewListener(mScrollViewListener);

    }

    private boolean isFirstLoadQuotes = true;

    private void setupViewData() {
        if (null != mQuotesEngine && mStockBean != null) {
            // requestUiHandler.sendEmptyMessage(MSG_WHAT_BEFORE_REQUEST);
            rotateRefreshButton();
            if (isFirstLoadQuotes) {

                mQuotesEngine.quotes(mStockBean.code, listener);
                isFirstLoadQuotes = false;
            } else {

                mQuotesEngine.quotesNotTip(mStockBean.code, listener);
            }
            // listener.setLoadingDialog(context);
        }
    }

    Handler requestUiHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_WHAT_BEFORE_REQUEST: {
                    rotateRefreshButton();
                }

                    break;
                case MSG_WHAT_AFTER_REQUEST: {
                    stopRefreshAnimation();
                }

                    break;

                default:
                    break;
            }
        };
    };

    ScrollViewListener mScrollViewListener = new ScrollViewListener() {

        @Override
        public void onScrollChanged(InterceptScrollView scrollView, int x, int y, int oldx, int oldy) {
            // TODO Auto-generated method stub
            /*
             * if (mScrollview.getScrollY() >= getResources().getDimensionPixelOffset(R.dimen.layout_height_all)) {
             * chartTounching();
             * }
             */
            Log.e("mScrollViewListener", mScrollview.getScrollY() + "---" + mScrollview.getHeight());
        }

    };
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
                if (null != stockQuotesBean && UIUtils.roundAble(stockQuotesBean)) {
                    quoteHandler.removeCallbacks(runnable);
                }
                List<FiveRangeItem> buyList = new ArrayList<FiveRangeItem>();
                List<FiveRangeItem> sellList = new ArrayList<FiveRangeItem>();
                int i = 0;
                for (; i < 5; i++) {
                    // String buyPrice : stockQuotesBean.getBuyPrice().getBuyPrice()
                    FiveRangeItem buyItem = new FiveRangeItem();
                    if (i < stockQuotesBean.getBuyPrice().getBuyVol().size()) {
                        String buyPrice = stockQuotesBean.getBuyPrice().getBuyPrice().get(i);
                        if (isFloatText(buyPrice)) {

                            buyItem.price = Float.parseFloat(buyPrice);
                        } else {
                            buyItem.price = 0;
                        }
                        String volText = stockQuotesBean.getBuyPrice().getBuyVol().get(i);
                        if (isFloatText(volText)) {
                            buyItem.vol = Integer.parseInt(volText);
                        } else {

                            buyItem.vol = 0;
                        }
                    } else {
                        buyItem.vol = 0;

                    }
                    buyItem.tag = "" + (i + 1);
                    buyList.add(buyItem);
                }

                for (int j = 4; j >= 0; j--) {
                    FiveRangeItem sellItem = new FiveRangeItem();
                    if (j < stockQuotesBean.getSellPrice().getSellVol().size()) {
                        String sellPrice = stockQuotesBean.getSellPrice().getSellPrice().get(j);
                        if (isFloatText(sellPrice)) {

                            sellItem.price = Float.parseFloat(sellPrice);
                        } else {
                            sellItem.price = 0;
                        }

                        // sellItem.price = Float.parseFloat(stockQuotesBean.getSellPrice().getSellPrice().get(j));
                        String sellVol = stockQuotesBean.getSellPrice().getSellVol().get(j);
                        if (isFloatText(sellVol)) {
                            sellItem.vol = Integer.parseInt(sellVol);
                        } else {

                            sellItem.vol = 0;
                        }
                    } else {
                        sellItem.vol = 0;
                    }
                    sellItem.tag = "" + (j + 1);
                    sellList.add(sellItem);
                }

                stockQuotesBean.setBuyList(buyList);
                stockQuotesBean.setSellList(sellList);
            } catch (Exception e) {

                e.printStackTrace();
            }
            return stockQuotesBean;
        }

        @Override
        protected void afterParseData(StockQuotesBean object) {
            // requestUiHandler.sendEmptyMessage(MSG_WHAT_AFTER_REQUEST);
            stopRefreshAnimation();
            if (null != object) {
                mStockQuotesBean = object;
                updateStockView();
                mStockQuotesChartFragment.setStockQuotesBean(mStockQuotesBean);
                setAddOptionalButton();
            }

        }
    };

    private boolean isFloatText(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            return false;
        }
    }

    private void initTabPage() {

        fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        mStockQuotesChartFragment = StockQuotesChartFragment.newInstance(StockQuotesChartFragment.TREND_TYPE_TODAY,
                mStockCode);
        mStockQuotesChartFragment.setITouchListener(this);
        fragmentList.add(mStockQuotesChartFragment);
        KChartsFragment fragment = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_DAY, mStockCode,
                symbolType);
        fragment.setITouchListener(this);
        fragmentList.add(fragment);
        KChartsFragment fragment2 = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_WEEK, mStockCode,
                symbolType);
        fragment2.setITouchListener(this);
        fragmentList.add(fragment2);
        KChartsFragment fragment3 = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_MONTH, mStockCode,
                symbolType);
        fragment3.setITouchListener(this);
        fragmentList.add(fragment3);
        // fragmentList.add(new TestFragment());
        pager = (ScrollViewPager) this.findViewById(R.id.pager);
        pager.setCanScroll(false);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new MyPagerFragmentAdapter(getSupportFragmentManager(), fragmentList));

        // TabPageIndicator indicator = (TabPageIndicator) this.findViewById(R.id.indicator);
        // indicator.setViewPager(pager);

    }

    private void updateCurrentText() {
        new Handler().postDelayed(new Runnable() {

            public void run() {
                tvCurrent.setBackgroundDrawable(null);

            }

        }, 500);
    }

    private float mPrePrice = 0;

    protected void updateStockView() {
        if (null != mStockQuotesBean) {
            // if (mStockBean != null && !mStockBean.isStop) {
            tvCurrent.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));
            tvChange.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));
            tvPercentage.setTextColor(getTextColor(mStockQuotesBean.getPercentage()));
            tvOpen.setTextColor(getTextColor(mStockQuotesBean.getOpen() - mStockQuotesBean.getLastClose()));

            // String curentText = tvCurrent.getText().toString();
            if (mPrePrice > 0) {

                if (mStockQuotesBean.getCurrent() > mPrePrice) {
                    tvCurrent.setBackgroundResource(R.color.red_bg);
                } else if (mStockQuotesBean.getCurrent() < mPrePrice) {
                    tvCurrent.setBackgroundResource(R.color.green_bg);
                }

            }
            mPrePrice = mStockQuotesBean.getCurrent();
            updateCurrentText();

            if (StockUitls.isShangZhengB(mStockQuotesBean.getSymbol())) {
                tvChange.setText(StringFromatUtils.get3PointPlus(mStockQuotesBean.getChange()));
                tvCurrent.setText(StringFromatUtils.get3Point(mStockQuotesBean.getCurrent()));
                tvHigh.setText(StringFromatUtils.get3Point(mStockQuotesBean.getHigh()));
                tvLow.setText(StringFromatUtils.get3Point(mStockQuotesBean.getLow()));
                tvOpen.setText(StringFromatUtils.get3Point(mStockQuotesBean.getOpen()));

            } else {

                tvChange.setText(StringFromatUtils.get2PointPlus(mStockQuotesBean.getChange()));
                tvCurrent.setText(StringFromatUtils.get2Point(mStockQuotesBean.getCurrent()));
                tvHigh.setText(StringFromatUtils.get2Point(mStockQuotesBean.getHigh()));
                tvLow.setText(StringFromatUtils.get2Point(mStockQuotesBean.getLow()));
                tvOpen.setText(StringFromatUtils.get2Point(mStockQuotesBean.getOpen()));
            }
            tvPercentage.setText(StringFromatUtils.get2PointPercentPlus(mStockQuotesBean.getPercentage()));
            tvHuanShouLv.setText(StringFromatUtils.get2PointPercent(mStockQuotesBean.getTurnover_rate() * 100));
            tvChengjiaoLiang.setText(StringFromatUtils.convertToWanHand(mStockQuotesBean.getVolume()));
            tvChengjiaoE.setText(StringFromatUtils.convertToWan(mStockQuotesBean.getAmount()));
        }

        if (isIndexType()) {

            tvLiuzhi.setText(" —");
            tvZongzhi.setText(" —");
        } else {
            tvLiuzhi.setText(StringFromatUtils.convertToWan(mStockQuotesBean.getMarket_capital()));
            tvZongzhi.setText(StringFromatUtils.convertToWan((long) mStockQuotesBean.getTotal_capital()));

        }
        tvShiying.setText(StringFromatUtils.get2Point(mStockQuotesBean.getPe_ttm()));
        tvShiJing.setText(StringFromatUtils.get2Point(mStockQuotesBean.getPb()));

        setTitleTipString(mStockQuotesBean.getTradetile() + " "
                + TimeUtils.getMDTimeString(mStockQuotesBean.getMoment()));

        // }
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
            // Intent intent = new Intent(StockQuotesActivity.this, KChartLandScapeActivity.class);
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
        setTitle(mStockBean.name + "(" + mStockBean.code + ")");
        // setTitleTipString(mStockBean.code);
    }

    // private boolean hasFollow = true;
    IHttpListener baseListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            mStockQuotesBean.setFollowed(!mStockQuotesBean.isFollowed());
            setAddOptionalButton();
        }
    };

    Handler quoteHandler = new Handler();

    // Handler quoteHandler = new Handler();

    public void onStart() {
        super.onStart();

        quoteHandler.postDelayed(runnable, 6);// 打开定时器，60ms后执行runnable操作

    };

    public void onStop() {
        super.onStop();
        quoteHandler.removeCallbacks(runnable);// 关闭定时器处理

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // dataHandler.sendEmptyMessage(1722);

            setupViewData();
            quoteHandler.postDelayed(this, 5 * 1000);// 隔60s再执行一次
        }
    };

    // Runnable quoterunnable = new Runnable() {
    // @Override
    // public void run() {
    // // dataHandler.sendEmptyMessage(1722);
    //
    // setupViewData();
    // quoteHandler.postDelayed(this, 30 * 1000);// 隔60s再执行一次
    // }
    // };

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
            case R.id.btn_right_second: {
                // rotateRefreshButton();
                quoteHandler.removeCallbacks(runnable);
                setupViewData();
                quoteHandler.postDelayed(runnable, 6 * 1000);
            }
                break;
            case R.id.stock_land:
            	Intent intent = KChartLandScapeActivity.newIntent(context, mStockBean);
            	startActivity(intent);
            	break;
            default:
                break;
        }

    }

    private void rotateRefreshButton() {
        // RotateAnimation ani = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
        // Animation.RELATIVE_TO_SELF,
        // 0.5f);
        // ani.setDuration(500);
        // ani.setRepeatCount(-1);
        // // LinearInterpolator inter = new LinearInterpolator();
        // // ani.setInterpolator(inter);
        // // Matrix matrix = new Matrix();
        // // matrix.preRotate(360, 100, 200);

        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing), null,
                null, null);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        btnRefresh.startAnimation(animation);
        // btnRefresh.startAnimation(ani);
    }

    private void stopRefreshAnimation() {
        btnRefresh.clearAnimation();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh), null,
                null, null);
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
            return true;
        }

    }

    class OnView implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            loseTouching();
            return true;
        }

    }

    public StockQuotesBean getmStockQuotesBean() {
        return mStockQuotesBean;
    }

    public void setmStockQuotesBean(StockQuotesBean mStockQuotesBean) {
        this.mStockQuotesBean = mStockQuotesBean;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onResume(this);
    }
}
