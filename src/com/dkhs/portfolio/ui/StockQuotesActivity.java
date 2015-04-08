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
import java.util.Collections;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.fragment.F10Fragment;
import com.dkhs.portfolio.ui.fragment.FragmentForOptionOnr;
import com.dkhs.portfolio.ui.fragment.FragmentForStockSHC;
import com.dkhs.portfolio.ui.fragment.FragmentNewsList;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.ui.fragment.KChartsFragment;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartFragment;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.InterceptScrollView.ScrollViewListener;
import com.dkhs.portfolio.ui.widget.KChartDataListener;
import com.dkhs.portfolio.ui.widget.LandStockViewCallBack;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName StockQuotesActivity
 * @Description 个股行情
 * @author zjz
 * @date 2014-9-26 上午10:22:32
 * @version 1.0
 */
public class StockQuotesActivity extends ModelAcitivity implements OnClickListener, Serializable, StockViewCallBack,
        LandStockViewCallBack, KChartDataListener {

    private static final long serialVersionUID = 15121212311111156L;
    private SelectStockBean mStockBean;
    public static final String EXTRA_STOCK = "extra_stock";
    protected static final int MSG_WHAT_BEFORE_REQUEST = 99;
    protected static final int MSG_WHAT_AFTER_REQUEST = 97;
    private final int REQUESTCODE_SELECT_STOCK = 901;
    private final int REQUEST_CHECK = 888;
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
    private LinearLayout bottomLayout;
    private FragmentSelectAdapter mFragmentSelectAdapter;
    private StockQuotesActivity layouts;
    private View viewHeader;
    private String symbolType;
    private List<Fragment> frag;
    private Button klinVirtulCheck;
    private static String checkValue = "0";
    private static final long mPollRequestTime = 1000 * 15;
    private static final String TAG = "StockQuotesActivity";

    private StockLandView landStockview;

    // private View landScapeview;

    public static Intent newIntent(Context context, SelectStockBean bean) {
        Intent intent = new Intent(context, StockQuotesActivity.class);
        intent.putExtra(EXTRA_STOCK, bean);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, " --- onNewIntent--");
        setIntent(intent);// must store the new intent unless getIntent() will return the old one
        processExtraData();
        requestData();
        initList();
    }

    private void processExtraData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mStockBean = (SelectStockBean) extras.getSerializable(EXTRA_STOCK);
            if (null != mStockBean) {
                mStockId = mStockBean.id;
                mStockCode = mStockBean.code;
                symbolType = mStockBean.symbol_type;
                setTitleDate();
            }
        }
    }

    VisitorDataEngine mVisitorDataEngine;
    private List<SelectStockBean> localList;
    Handler viewHandler = new Handler();

    // private TextView tvAdd;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Log.e(TAG, " --- onCreate--");
        setContentView(R.layout.activity_stockquotes);
        context = this;
        layouts = this;

        mVisitorDataEngine = new VisitorDataEngine();

        // 已优化的地方 ：减少数据库操作、使用异步进行查询自选股列表
        if (!PortfolioApplication.hasUserLogin()) {
            getLocalOptionList();
        }

        mQuotesEngine = new QuotesEngineImpl();
        // handle intent extras
        processExtraData();
        initView();

    }

    private void getLocalOptionList() {
        new Thread() {
            public void run() {
                localList = mVisitorDataEngine.getOptionalStockList();
            };
        }.start();
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

        bottomLayout = (LinearLayout) findViewById(R.id.stock_layout);
        android.view.ViewGroup.LayoutParams l = bottomLayout.getLayoutParams();
        l.height = getResources().getDimensionPixelOffset(R.dimen.layout_height) * 2;// dm.heightPixels * 3 / 2 -

        klinVirtulCheck = (Button) findViewById(R.id.klin_virtul_check);
        klinVirtulCheck.setOnClickListener(this);
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

        viewHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                initTabPage();
            }
        }, 400);
        viewHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scrollToTop();

                // 需要优化的地方
                initList();

                setFuquanView();
                initLandStockView();

            }
        }, 800);

    }

    private void full(boolean paramBoolean) {
        if (paramBoolean) {
            WindowManager.LayoutParams localLayoutParams2 = getWindow().getAttributes();
            localLayoutParams2.flags = (0x400 | localLayoutParams2.flags);
            getWindow().setAttributes(localLayoutParams2);
            getWindow().addFlags(512);
            return;
        }
        WindowManager.LayoutParams localLayoutParams1 = getWindow().getAttributes();
        localLayoutParams1.flags = (0xFFFFFBFF & localLayoutParams1.flags);
        getWindow().setAttributes(localLayoutParams1);
        getWindow().clearFlags(512);
    }

    public void setLayoutHeight(int position) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        android.view.ViewGroup.LayoutParams l = bottomLayout.getLayoutParams();
        /*
         * if (0 == position) { l.height = LayoutParams.MATCH_PARENT; }
         */
        if (position < 3) {
            position = 3;
        }
        l.height = position * getResources().getDimensionPixelSize(R.dimen.layout_height) + 70;
    }

    public void setLayoutHeights(int height) {
        android.view.ViewGroup.LayoutParams l = bottomLayout.getLayoutParams();
        l.height = height + 70;
        scrollToTop();
    }

    private void initList() {
        bottomLayout.removeAllViews();
        if (null != mStockCode
                && (mStockCode.equals("SH000001") || mStockCode.equals("SZ399001") || mStockCode.equals("SZ399006"))) {
            String[] name = new String[3];
            name[0] = "涨幅榜";
            name[1] = "跌幅榜";
            name[2] = "换手率榜";
            String exchange;
            String listSector = null;
            if (mStockCode.equals("SH000001")) {
                exchange = OpitionCenterStockEngineImple.VALUE_EXCHANGE_SAHNG;
                listSector = null;
            } else if (mStockCode.equals("SZ399001")) {
                exchange = OpitionCenterStockEngineImple.VALUE_SYMBOL_TYPE;
                listSector = null;
            } else {
                exchange = OpitionCenterStockEngineImple.VALUE_EXCHANGE;
                listSector = OpitionCenterStockEngineImple.VALUE_SYMBOL_SELECT;
            }
            List<Fragment> fraglist = new ArrayList<Fragment>();
            Fragment f1 = FragmentForStockSHC.newIntent(exchange, StockViewType.MARKET_STOCK_UPRATIO,
                    OpitionCenterStockEngineImple.VALUE_SYMBOL_STYPE, listSector, true);
            fraglist.add(f1);
            Fragment f2 = FragmentForStockSHC.newIntent(exchange, StockViewType.MARKET_STOCK_DOWNRATIO,
                    OpitionCenterStockEngineImple.VALUE_SYMBOL_STYPE, listSector, true);
            fraglist.add(f2);
            Fragment f3 = FragmentForStockSHC
                    .newIntent(exchange, StockViewType.STOCK_HANDOVER, null, listSector, false);
            fraglist.add(f3);
            FragmentSelectAdapter mFragmentSelectAdapter = new FragmentSelectAdapter(context, name, fraglist,
                    bottomLayout, getSupportFragmentManager());
            mFragmentSelectAdapter.setScrollAble(false);
            mFragmentSelectAdapter.setOutLaoyout(layouts);
        } else if (!(null != mStockBean.symbol_type && UIUtils.isSymbleIndex(mStockBean.symbol_type))) {
            String[] name = new String[3];
            if ((null != mStockBean.symbol_type && UIUtils.isSymbleIndex(mStockBean.symbol_type))) {
                name = new String[2];
            }
            // name[0] = "新闻";
            name[0] = "公告";
            name[1] = "研报";
            if (!(null != mStockBean.symbol_type && UIUtils.isSymbleIndex(mStockBean.symbol_type))) {
                name[2] = "F10";
            }
            NewsforModel vo;
            frag = new ArrayList<Fragment>();
            Fragment f2 = new FragmentNewsList();
            Bundle b2 = new Bundle();
            b2.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWSFOREACH);
            vo = new NewsforModel();
            vo.setSymbol(mStockBean.code);
            vo.setContentType("20");
            vo.setPageTitle("公告正文");
            // vo.setLayout(stockLayout);
            b2.putSerializable(FragmentNewsList.VO, vo);
            // b2.putSerializable(FragmentNewsList.LAYOUT, layouts);
            f2.setArguments(b2);
            frag.add(f2);
            Fragment f4 = FragmentForOptionOnr.newIntent(context, mStockBean.code, mStockBean.name, "");
            frag.add(f4);
            if (!(null != mStockBean.symbol_type && mStockBean.symbol_type.equals("5"))) {
                Fragment f3 = new F10Fragment();
                Bundle b3 = new Bundle();
                b3.putSerializable(StockQuotesActivity.EXTRA_STOCK, mStockBean);
                frag.add(f3);
            }
            FragmentSelectAdapter mFragmentSelectAdapter = new FragmentSelectAdapter(context, name, frag, bottomLayout,
                    getSupportFragmentManager());
            mFragmentSelectAdapter.setScrollAble(false);
            mFragmentSelectAdapter.setOutLaoyout(layouts);
            // views.setOnTouchListener(new OnView());
        }
    }

    private boolean isIndexType() {
        return null != mStockBean && mStockBean.symbol_type != null
                && mStockBean.symbol_type.equalsIgnoreCase(StockUitls.SYMBOLTYPE_INDEX);
    }

    private void setAddOptionalButton() {
        if (mStockQuotesBean == null) {
            btnAddOptional.setVisibility(View.GONE);
            return;
        }
        btnAddOptional.setVisibility(View.VISIBLE);

        if (!PortfolioApplication.hasUserLogin()) {
            // SelectStockBean selectBean = SelectStockBean.
            SelectStockBean selectBean = new SelectStockBean();
            selectBean.id = mStockQuotesBean.getId();
            selectBean.code = mStockQuotesBean.getCode();
            if (localList != null && localList.contains(selectBean)) {
                mStockQuotesBean.setFollowed(true);
            }
        }

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
        Log.e(TAG, "scrollToTop");
        mScrollview = (InterceptScrollView) findViewById(R.id.sc_content);
        // mScrollview.smoothScrollTo(0, 0);
        mScrollview.setScrollViewListener(mScrollViewListener);
    }

    private boolean isFirstLoadQuotes = true;

    private void requestData() {

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
             * if (mScrollview.getScrollY() >=
             * getResources().getDimensionPixelOffset
             * (R.dimen.layout_height_all)) { chartTounching(); }
             */
            if (mScrollview.getScrollY() + mScrollview.getHeight() >= mScrollview.computeVerticalScrollRange()
                    && null != frag) {
                ((FragmentNewsList) frag.get(0)).loadMore();
                ((FragmentForOptionOnr) frag.get(1)).loadMore();
            }
        }
    };
    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                pager.setCurrentItem(position);
                if (position == 0) {
                    klinVirtulCheck.setVisibility(View.GONE);
                } else {
                    if (null != mStockBean && !UIUtils.isSymbleIndex(mStockBean.symbol_type)) {
                        klinVirtulCheck.setVisibility(View.VISIBLE);
                    }
                }

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
                    quoteHandler.removeCallbacks(updateRunnable);
                } else {
                    quoteHandler.postDelayed(updateRunnable, mPollRequestTime);
                }

                Collections.reverse(stockQuotesBean.getSellPrice().getSellVol());

                Collections.reverse(stockQuotesBean.getSellPrice().getSellPrice());
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
                landStockview.updateLandStockView(object);
                mStockQuotesChartFragment.setStockQuotesBean(mStockQuotesBean);
                setAddOptionalButton();
            }
        }
    };

    private void initTabPage() {

        Log.e(TAG, "====initTabPage view");
        fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        mStockQuotesChartFragment = StockQuotesChartFragment.newInstance(StockQuotesChartFragment.TREND_TYPE_TODAY,
                mStockCode);
        // mStockQuotesChartFragment.setITouchListener(this);
        mStockQuotesChartFragment.setStockViewCallBack(this);
        fragmentList.add(mStockQuotesChartFragment);
        KChartsFragment fragment = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_DAY, mStockCode,
                symbolType);
        // fragment.setITouchListener(this);
        fragment.setStockViewCallBack(this);
        fragment.setLandCallBack(this);
        fragment.setKChartDataListener(this);
        fragmentList.add(fragment);
        KChartsFragment fragment2 = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_WEEK, mStockCode,
                symbolType);
        // fragment2.setITouchListener(this);
        fragment2.setLandCallBack(this);
        fragment2.setStockViewCallBack(this);
        fragment2.setKChartDataListener(this);
        fragmentList.add(fragment2);
        KChartsFragment fragment3 = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_MONTH, mStockCode,
                symbolType);
        // fragment3.setITouchListener(this);
        fragment3.setLandCallBack(this);
        fragment3.setStockViewCallBack(this);
        fragment3.setKChartDataListener(this);
        fragmentList.add(fragment3);
        // fragmentList.add(new TestFragment());
        pager = (ScrollViewPager) this.findViewById(R.id.pager);
        pager.removeAllViews();
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
        if (null == mStockQuotesBean) {
            return;
        }
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
        tvHuanShouLv.setText(StringFromatUtils.get2PointPercent(mStockQuotesBean.getTurnover_rate()));
        tvChengjiaoLiang.setText(StringFromatUtils.convertToWanHand(mStockQuotesBean.getVolume()));
        tvChengjiaoE.setText(StringFromatUtils.convertToWan(mStockQuotesBean.getAmount()));
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
        if (value == 0 || mStockQuotesBean.getIs_stop() == 1) {
            return (ColorStateList) getResources().getColorStateList(R.color.theme_color);
        }
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
            // Intent intent = new Intent(StockQuotesActivity.this,
            // KChartLandScapeActivity.class);
            startActivityForResult(intent, REQUESTCODE_SELECT_STOCK);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("StockquoteActivity onActivityResult");
        if (resultCode == RESULT_OK) {
            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_SELECT_STOCK:
                    SelectStockBean selectBean = (SelectStockBean) data
                            .getSerializableExtra(FragmentSelectStockFund.ARGUMENT);
                    if (null != selectBean) {
                        mStockBean = selectBean;
                        setTitleDate();
                        setAddOptionalButton();
                    }
                    break;
                case REQUEST_CHECK:
                    checkValue = data.getStringExtra(ChangeCheckType.CHECK_TYPE);
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_KLIN_COMPLEX, checkValue);
                    setFuquanView();
                    break;
            }
        }
    }

    private void setFuquanView() {
        if (checkValue.equals("0")) {
            klinVirtulCheck.setText("不复权  ▼");
            // PortfolioApplication.getInstance().setCheckValue("0");
            // setc
        } else if (checkValue.equals("1")) {
            klinVirtulCheck.setText("前复权  ▼");
            // PortfolioApplication.getInstance().setCheckValue("1");
        } else {
            klinVirtulCheck.setText("后复权  ▼");
            // PortfolioApplication.getInstance().setCheckValue("2");
        }
        if (fragmentList.get(pager.getCurrentItem()) instanceof KChartsFragment) {
            ((KChartsFragment) fragmentList.get(pager.getCurrentItem())).regetDate(checkValue);
        }
    }

    private void setTitleDate() {
        setTitle(mStockBean.name + "(" + mStockBean.code + ")");
    }

    // private boolean hasFollow = true;
    IHttpListener baseListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            if (mStockQuotesBean.isFollowed()) {
                PromptManager.showDelFollowToast();
            } else {

                PromptManager.showFollowToast();
            }
            mStockQuotesBean.setFollowed(!mStockQuotesBean.isFollowed());
            setAddOptionalButton();
        }
    };
    Handler quoteHandler = new Handler();

    // Handler quoteHandler = new Handler();
    public void onStart() {
        super.onStart();
        // quoteHandler.postDelayed(updateRunnable, 6);// 打开定时器，60ms后执行runnable操作
    };

    public void onStop() {
        super.onStop();
        quoteHandler.removeCallbacks(updateRunnable);// 关闭定时器处理
    }

    Runnable updateRunnable = new Runnable() {

        @Override
        public void run() {
            // dataHandler.sendEmptyMessage(1722);
            quoteHandler.removeCallbacks(updateRunnable);
            Log.e(TAG, " ----  updateRunnable");

            requestData();
            quoteHandler.postDelayed(this, mPollRequestTime);// 隔60s再执行一次
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_optional:
                if (!PortfolioApplication.hasUserLogin()) {// 如果当前是游客模式，添加自选股到本地数据库
                    SelectStockBean selectBean = SelectStockBean.copy(mStockQuotesBean);
                    if (null != selectBean) {
                        if (selectBean.isFollowed) {
                            // selectBean.isFollowed = false;
                            mStockQuotesBean.setFollowed(false);
                            mVisitorDataEngine.delOptionalStock(selectBean);
                            PromptManager.showDelFollowToast();
                        } else {
                            selectBean.isFollowed = true;
                            mStockQuotesBean.setFollowed(true);
                            selectBean.sortId = 0;
                            mVisitorDataEngine.saveOptionalStock(selectBean);
                            PromptManager.showFollowToast();
                        }
                    }
                    localList = mVisitorDataEngine.getOptionalStockList();
                    setAddOptionalButton();

                } else {
                    if (mStockQuotesBean.isFollowed()) {
                        mQuotesEngine.delfollow(mStockBean.id, baseListener);
                    } else {
                        mQuotesEngine.symbolfollow(mStockBean.id, baseListener);
                    }
                }
                break;
            case R.id.btn_right_second: {
                // rotateRefreshButton();
                // quoteHandler.removeCallbacks(updateRunnable);
                requestData();

            }
                break;
            case R.id.klin_virtul_check:
                Intent intent = new Intent(this, ChangeCheckType.class);
                Bundle b = new Bundle();
                b.putString(ChangeCheckType.CHECK_TYPE, checkValue);
                intent.putExtras(b);
                startActivityForResult(intent, REQUEST_CHECK);
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

    // /**
    // * @Title
    // * @Description TODO: (用一句话描述这个方法的功能)
    // * @return
    // */
    // @Override
    // public void chartTounching() {
    // if (mScrollview != null) {
    // mScrollview.setIsfocus(true);
    // }
    // }

    // /**
    // * @Title
    // * @Description TODO: (用一句话描述这个方法的功能)
    // * @return
    // */
    // @Override
    // public void loseTouching() {
    // if (mScrollview != null) {
    // mScrollview.setIsfocus(false);
    // }
    // }

    public StockQuotesBean getmStockQuotesBean() {
        return mStockQuotesBean;
    }

    public void setmStockQuotesBean(StockQuotesBean mStockQuotesBean) {
        this.mStockQuotesBean = mStockQuotesBean;
    }

    public SelectStockBean getmStockBean() {
        return mStockBean;
    }

    public void setmStockBean(SelectStockBean mStockBean) {
        this.mStockBean = mStockBean;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPause(this);
    }

    private boolean isChange = false;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onResume(this);
        // if (isChange) {
        // // PortfolioApplication.getInstance().setChange(false);
        // isChange = false;
        // // checkValue = PortfolioApplication.getInstance().getCheckValue();
        // PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_KLIN_COMPLEX, checkValue);
        // setFuquanView();
        // }
        // if (PortfolioApplication.getInstance().getkLinePosition() != -1) {
        // hsTitle.setSelectIndex(PortfolioApplication.getInstance().getkLinePosition());
        // PortfolioApplication.getInstance().setkLinePosition(-1);
        // }
        viewHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                requestData();

            }
        }, 850);

    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
        setFuquanView();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void landViewFadeOut() {
        isFull = false;
        rotaVericteStockView();

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param paramInt
     * @return
     */
    @Override
    public void setViewType(int paramInt) {
        // TODO Auto-generated method stub

    }

    boolean isFull;

    @Override
    public void stockMarkShow() {
        isFull = !isFull;

        if (isFull) {

            rotaLandStockView();

        } else {

            rotaVericteStockView();

        }

    }

    private void initLandStockView() {
        landStockview = new StockLandView(this);
        landStockview.setStockViewCallback(this);
        landStockview.setKChartDataListener(this);
        DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
        LayoutParams localLayoutParams = new LayoutParams(localDisplayMetrics.heightPixels,
                localDisplayMetrics.widthPixels);

        this.landStockview.setLayoutParams(localLayoutParams);
        this.landStockview.setLandStockCallBack(this);
        this.landStockview.setStockBean(mStockBean);
        // this.landScapeview.setCallBack(this);
        this.landStockview.setVisibility(View.INVISIBLE);
        addContentView(this.landStockview, localLayoutParams);

        ViewHelper.setPivotY(landStockview, localDisplayMetrics.widthPixels);
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this.landStockview, "y",
                -localDisplayMetrics.widthPixels);
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this.landStockview, "rotation",
                new float[] { 90.0F });
        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator2 });
        localAnimatorSet.start();

    }

    private void rotaVericteStockView() {
        bottomLayout.setVisibility(View.VISIBLE);
        viewHeader.setVisibility(View.VISIBLE);
        landStockview.setVisibility(View.INVISIBLE);
        showHead();
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(this.bottomLayout, "alpha", new float[] { 1.0F })
                .setDuration(100L);
        ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(this.viewHeader, "alpha", new float[] { 1.0F })
                .setDuration(100L);

        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnimator) {
                StockQuotesActivity.this.setSwipeBackEnable(true);
                full(isFull);
            }
        });
        localAnimatorSet.playTogether(new Animator[] { bottomAnimator, headerAnimator });
        localAnimatorSet.start();
    }

    private void rotaLandStockView() {
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(this.bottomLayout, "alpha", new float[] { 0.0F })
                .setDuration(100L);
        ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(this.viewHeader, "alpha", new float[] { 0.0F })
                .setDuration(100L);

        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnimator) {
                full(isFull);
                bottomLayout.setVisibility(View.GONE);
                viewHeader.setVisibility(View.GONE);
                hideHead();
                landStockview.setVisibility(View.VISIBLE);
            }

            public void onAnimationStart(Animator paramAnimator) {
                super.onAnimationStart(paramAnimator);
                StockQuotesActivity.this.setSwipeBackEnable(false);
            }
        });
        localAnimatorSet.playTogether(new Animator[] { bottomAnimator, headerAnimator });
        localAnimatorSet.start();

    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == 4) {
            // if ((this.landStockview.isAnimator()) || (this.stockView.isAnimator()))
            // return false;
            if (null != this.landStockview && this.landStockview.isShown()) {
                landViewFadeOut();
                return false;
            }
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    private int stickType = 0;

    @Override
    public int getStickType() {
        return stickType;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public StockQuotesBean getStockQuotesBean() {
        // TODO Auto-generated method stub
        return mStockQuotesBean;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param stickValue
     * @return
     */
    @Override
    public void setStickType(int stickValue) {
        this.stickType = stickValue;

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public int getTabPosition() {
        if (null != pager) {

            return pager.getCurrentItem();
        }
        return 0;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @return
     */
    @Override
    public void setTabPosition(int position) {

        hsTitle.setSelectIndex(position);

    }

    private List<OHLCEntity> mDayKChart = Collections.EMPTY_LIST;
    private List<OHLCEntity> mWeekKChart = Collections.EMPTY_LIST;
    private List<OHLCEntity> mMonthKChart = Collections.EMPTY_LIST;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public List<OHLCEntity> getDayLineDatas() {
        // TODO Auto-generated method stub
        return this.mDayKChart;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param kLineDatas
     * @return
     */
    @Override
    public void setDayKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mDayKChart = kLineDatas;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public List<OHLCEntity> getMonthLineDatas() {
        return this.mMonthKChart;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param kLineDatas
     * @return
     */
    @Override
    public void setMonthKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mMonthKChart = kLineDatas;

    }

    @Override
    public List<OHLCEntity> getWeekLineDatas() {
        return this.mWeekKChart;
    }

    @Override
    public void setWeekKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mWeekKChart = kLineDatas;
    }

}
