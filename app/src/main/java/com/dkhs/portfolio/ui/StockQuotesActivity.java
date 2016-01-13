/**
 * @Title StockQuotesActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-26 上午10:22:32
 * @version V1.0
 */

package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.base.widget.RelativeLayout;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.bean.StockQuotesStopTopBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.IResultCallback;
import com.dkhs.portfolio.engine.LocalDataEngine.VisitorDataSource;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.PagerFragmentAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.fragment.FragmentForOptionOnr;
import com.dkhs.portfolio.ui.fragment.FragmentForStockSHC;
import com.dkhs.portfolio.ui.fragment.FragmentNewsList;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.FragmentStockNewsList;
import com.dkhs.portfolio.ui.fragment.KChartsFragment;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartFragment;
import com.dkhs.portfolio.ui.fragment.TabF10Fragment;
import com.dkhs.portfolio.ui.widget.ChangeFollowView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.InterceptScrollView.ScrollViewListener;
import com.dkhs.portfolio.ui.widget.KChartDataListener;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName StockQuotesActivity
 * @Description 个股行情
 * @date 2014-9-26 上午10:22:32
 */
public class StockQuotesActivity extends ModelAcitivity implements OnClickListener, Serializable, StockViewCallBack,
        KChartDataListener {

    private static final long serialVersionUID = 15121212311111156L;
    private long mLastClickTime = 0;

    private SelectStockBean mStockBean;
    public static final String EXTRA_STOCK = "extra_stock";
    protected static final int MSG_WHAT_BEFORE_REQUEST = 99;
    protected static final int MSG_WHAT_AFTER_REQUEST = 97;
    private final int REQUESTCODE_SELECT_STOCK = 901;
    private final int REQUEST_CHECK = 888;
    private final int REQUEST_LAND = 100;
    private TextView tvCurrent;
    private TextView tvHigh;
    private TextView tvLow;
    private TextView tvOpen;
    private TextView tvChange;
    private TextView tvPercentage;
    private TextView btnRefresh;
    private TextView tvChengjiaoLiang;
    private TextView tvChengjiaoE;
    private TextView tvHuanShouLv;
    private TextView tvStockStatus;
    private View viewPriceChange;
    private TextView tvLiuzhi;
    private TextView tvZongzhi;
    private TextView tvShiying;
    private TextView tvAmount;
    private TextView tvYesterDay;
    private InterceptScrollView mScrollview; // 滚动条，用于滚动到头部
    private QuotesEngineImpl mQuotesEngine;
    private StockQuotesBean mStockQuotesBean;
    private String mStockCode;
    private Context context;
    private HScrollTitleView hsTitle;
    private HScrollTitleView hsTitleBottom;
    private HScrollTitleView hs_title_first;
    private HScrollTitleView hsTitleSticker;
    private ScrollViewPager pager;
    private ArrayList<Fragment> fragmentList;
    private StockQuotesChartFragment mStockQuotesChartFragment;
    private View bottomLayout;
    private RelativeLayout stock_layout_first;
    private View viewHeader;
    private String symbolType;
    //    private List<Fragment> bottmoTabFragmentList;
    private TextView tvKlinVirtulCheck;
    private static String checkValue = "0";
    private static final long mPollRequestTime = 1000 * 15;
    private static final String TAG = "StockQuotesActivity";
    private final int MENU_FOLLOWE_OR_UNFOLLOWE = 0;
    private final int MENU_REMIND = 1;
    private FloatingActionMenu mActionMenu;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public static Intent newIntent(Context context, SelectStockBean bean) {
        Intent intent = new Intent(context, StockQuotesActivity.class);
        intent.putExtra(EXTRA_STOCK, Parcels.wrap(bean));
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// must store the new intent unless newIntent() will return the old one
        processExtraData();
        requestData();
        initBottomTabFragment();
    }

    private void processExtraData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mStockBean = Parcels.unwrap(extras.getParcelable(EXTRA_STOCK));
            if (null != mStockBean) {
                mStockCode = mStockBean.symbol;
                symbolType = mStockBean.symbol_type;
                setTitleDate();
            }
        }
    }

    private VisitorDataEngine mVisitorDataEngine;
    private List<SelectStockBean> localList = new ArrayList<>();
    Handler viewHandler = new Handler();


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        BusProvider.getInstance().register(this);
        hadFragment();

        setContentView(R.layout.activity_stockquotes);
        context = this;

        mVisitorDataEngine = new VisitorDataEngine();

        // 已优化的地方 ：减少数据库操作、使用异步进行查询自选股列表
        if (!PortfolioApplication.hasUserLogin()) {
            getLocalOptionList();
        }

        mQuotesEngine = new QuotesEngineImpl();
        // handle intent extras
        processExtraData();
        initView();

        changeFollowView = new ChangeFollowView(this);
        changeFollowView.setmChangeListener(changeFollowListener);
    }

    private void getLocalOptionList() {
//        new Thread() {
//            public void run() {
//                localList = mVisitorDataEngine.getOptionalStockList();
//            }
//
//            ;
//        }.start();
        VisitorDataSource.getOptionalStockList(this, new IResultCallback<SelectStockBean>() {
            @Override
            public void onResultCallback(List<SelectStockBean> resultList) {
                localList.clear();
                localList.addAll(resultList);
            }
        });
    }

    LinearLayout ll_top;

    private void initView() {
        ll_top = (LinearLayout) findViewById(R.id.ll_top);
        ViewStub viewstub;
        if (isIndexType()) {
            viewstub = (ViewStub) findViewById(R.id.layout_index_header);
        } else {
            viewstub = (ViewStub) findViewById(R.id.layout_stock_header);
        }
        mScrollview = (InterceptScrollView) findViewById(R.id.sc_content);
        if (viewstub != null) {
            viewHeader = viewstub.inflate();
            viewHeader.setBackgroundColor(getResources().getColor(R.color.white));
            // views = findViewById(R.id.layout_view);
            tvCurrent = (TextView) viewHeader.findViewById(R.id.tv_current_price);
            tvHigh = (TextView) viewHeader.findViewById(R.id.tv_highest_value);
            tvLow = (TextView) viewHeader.findViewById(R.id.tv_lowest_value);
            tvOpen = (TextView) viewHeader.findViewById(R.id.tv_today_open_value);
            tvYesterDay = (TextView) viewHeader.findViewById(R.id.tv_yesterday_value);
            tvChange = (TextView) viewHeader.findViewById(R.id.tv_up_price);
            tvChengjiaoLiang = (TextView) viewHeader.findViewById(R.id.tv_liang_value);
            tvChengjiaoE = (TextView) viewHeader.findViewById(R.id.tv_e_value);
            tvHuanShouLv = (TextView) viewHeader.findViewById(R.id.tv_huan_value);
            tvStockStatus = (TextView) viewHeader.findViewById(R.id.tv_stock_status);
            viewPriceChange = (View) viewHeader.findViewById(R.id.layout_change_price);
            tvLiuzhi = (TextView) viewHeader.findViewById(R.id.tv_liuzhi_value);
            tvZongzhi = (TextView) viewHeader.findViewById(R.id.tv_zongzhi_value);
            tvShiying = (TextView) viewHeader.findViewById(R.id.tv_shiying_value);
            tvAmount = (TextView) viewHeader.findViewById(R.id.tv_amount_value);
            tvPercentage = (TextView) viewHeader.findViewById(R.id.tv_percentage);

            mActionMenu = (FloatingActionMenu) findViewById(R.id.floating_action_view);
            mActionMenu.setVisibility(View.GONE);
            mActionMenu.attachToScrollView(mScrollview);
            mActionMenu.setOnMenuItemSelectedListener(new FloatingActionMenu.OnMenuItemSelectedListener() {
                @Override
                public boolean onMenuItemSelected(int paramInt) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return false;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    switch (paramInt) {
                        case MENU_FOLLOWE_OR_UNFOLLOWE:
                            handFollowOrUnfollowAction();
                            break;
                        case MENU_REMIND:
                            if (!UIUtils.iStartLoginActivity(StockQuotesActivity.this)) {

                                startActivity(StockRemindActivity.newStockIntent(StockQuotesActivity.this,
                                        SelectStockBean.copy(mStockQuotesBean)));

                            }
                            break;
                    }
                    return false;
                }
            });


        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.theme_blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
        bottomLayout = findViewById(R.id.stock_layout);
        stock_layout_first = (RelativeLayout) findViewById(R.id.stock_layout_first);
        float dimen = UIUtils.dip2px(this, (UIUtils.getDimen(this, R.dimen.title_tool_bar)));
        int minHeight = UIUtils.getDisplayMetrics().heightPixels - (int) dimen;
        bottomLayout.setMinimumHeight(minHeight);
        tvKlinVirtulCheck = (TextView) findViewById(R.id.klin_virtul_check);
        tvKlinVirtulCheck.setOnClickListener(this);
        hsTitle = (HScrollTitleView) findViewById(R.id.hs_title);
        hsTitleBottom = (HScrollTitleView) findViewById(R.id.hs_title_bottom);
        hs_title_first = (HScrollTitleView) findViewById(R.id.hs_title_first);
        hsTitleSticker = (HScrollTitleView) findViewById(R.id.hs_title_sticker);


        String[] titleArray = getResources().getStringArray(R.array.quotes_title);
        hsTitle.setTitleList(titleArray, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        hsTitle.setSelectPositionListener(titleSelectPostion);
//        TextView addButton = getRightButton();
//        // addButton.setBackgroundResource(R.drawable.ic_search_title);
//        addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
//                null, null, null);
//        addButton.setOnClickListener(mSearchClick);
        btnRefresh = getSecondRightButton();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh_selector),
                null, null, null);
        // btnRefresh.setBackgroundResource(R.drawable.nav_refresh_selector);
        btnRefresh.setOnClickListener(this);
        // stockLayout.setOnTouchListener(new OnLayoutlistener());
        initBottomTabTitle();
        viewHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                initTabPage();
            }
        }, 100);
        viewHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scrollToTop();

                // 需要优化的地方
                initBottomTabFragment();

                setFuquanView();

            }
        }, 800);


    }


    //  private int mMaxListHeight;
    int top;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            top = ll_top.getMeasuredHeight();
        }
    }

    private ChangeFollowView changeFollowView;

    private void handFollowOrUnfollowAction() {
        final SelectStockBean selectBean = SelectStockBean.copy(mStockQuotesBean);
        if (null != changeFollowView && null != selectBean) {
            changeFollowView.changeFollowNoDialog(selectBean);
        }
    }

    private ChangeFollowView.IChangeSuccessListener changeFollowListener = new ChangeFollowView.IChangeSuccessListener() {
        @Override
        public void onChange(SelectStockBean stockBean) {
            mQuotesEngine.quotes(mStockBean.symbol, quoteListener);

            mStockQuotesBean.setFollowed(stockBean.isFollowed());
            if (!PortfolioApplication.hasUserLogin()) {
//                localList = mVisitorDataEngine.getOptionalStockList();
                getLocalOptionList();
            }
            setAddOptionalButton();
            BusProvider.getInstance().post(stockBean);
        }
    };


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


    private void initBottomTabTitle() {
        if (null != mStockCode
                && (mStockCode.equals("SH000001") || mStockCode.equals("SZ399001") || mStockCode.equals("SZ399006"))) {
            String[] stockListTiles = getResources().getStringArray(R.array.select_optional_stock);
            hsTitleBottom.setTitleList(stockListTiles, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
            hsTitleSticker.setTitleList(stockListTiles, getResources().getDimensionPixelSize(R.dimen.title_2text_length));


        } else if (!(null != mStockBean.symbol_type && StockUitls.isIndexStock(mStockBean.symbol_type))) {

            String[] stockListTiles = getResources().getStringArray(R.array.stock_quote_info_title);
            hsTitleBottom.setTitleList(stockListTiles, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
            hsTitleSticker.setTitleList(stockListTiles, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
            String[] stockListTilesFirst = getResources().getStringArray(R.array.stock_quote_info_title_first);
            hs_title_first.setTitleList(stockListTilesFirst, getResources().getDimensionPixelSize(R.dimen.title_2text_length));
        }
    }

    private void initBottomTabFragment() {
        tabBottomFragment = new ArrayList<>();
        tabBottomFirstFragment = new ArrayList<>();
        if (null != mStockCode
                && (mStockCode.equals("SH000001") || mStockCode.equals("SZ399001") || mStockCode.equals("SZ399006"))) {

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

            FragmentForStockSHC mGrowFragmentUp = FragmentForStockSHC.newIntent(exchange, FragmentSelectStockFund.StockViewType.MARKET_STOCK_UPRATIO,
                    OpitionCenterStockEngineImple.VALUE_SYMBOL_STYPE, listSector, true);
            FragmentForStockSHC mGrowFragmentDown = FragmentForStockSHC.newIntent(exchange, FragmentSelectStockFund.StockViewType.MARKET_STOCK_DOWNRATIO,
                    OpitionCenterStockEngineImple.VALUE_SYMBOL_STYPE, listSector, true);
            FragmentForStockSHC mGrowFragmentHand = FragmentForStockSHC
                    .newIntent(exchange, FragmentSelectStockFund.StockViewType.STOCK_HANDOVER, null, listSector, false);
            tabBottomFragment.add(mGrowFragmentUp);
            tabBottomFragment.add(mGrowFragmentDown);
            tabBottomFragment.add(mGrowFragmentHand);
            replaceBottomTabFragment(tabBottomFragment.get(0));

            hsTitleBottom.setSelectPositionListener(new IndexTabSelectListener(exchange, listSector));
            hsTitleSticker.setSelectPositionListener(new IndexTabSelectListener(exchange, listSector));


        } else if (!(null != mStockBean.symbol_type && StockUitls.isIndexStock(mStockBean.symbol_type))) {

            tabBottomFirstFragment.add(FragmentStockNewsList.newIntent(mStockBean.symbol));
            tabBottomFirstFragment.add(FragmentNewsList.newIntent(mStockBean.symbol));
            tabBottomFirstFragment.add(FragmentForOptionOnr.newIntent(context, mStockBean.symbol, mStockBean.name, ""));
            replaceBottomFirstTabFragment(tabBottomFirstFragment.get(0));
            hs_title_first.setSelectPositionListener(mStockBottomTabFirstListener);
            //
            /*FragmentNewsList fList = FragmentNewsList.newIntent(mStockBean.symbol);
            // fList.setStockQuoteScrollListener(this);
            tabBottomFragment.add(fList);
            tabBottomFragment.add(FragmentForOptionOnr.newIntent(context, mStockBean.symbol, mStockBean.name, ""));*/
            tabBottomFragment.add(TabF10Fragment.newIntent(mStockBean.symbol, TabF10Fragment.TabType.INTRODUCTION));
            tabBottomFragment.add(TabF10Fragment.newIntent(mStockBean.symbol, TabF10Fragment.TabType.FINANCE));
            tabBottomFragment.add(TabF10Fragment.newIntent(mStockBean.symbol, TabF10Fragment.TabType.STOCK_HODLER));
            replaceBottomTabFragment(tabBottomFragment.get(0));

            hsTitleBottom.setSelectPositionListener(mStockBottomTabListener);
            hsTitleSticker.setSelectPositionListener(mStockBottomTabListener);

        }
    }

    private List<Fragment> tabBottomFragment;
    private List<Fragment> tabBottomFirstFragment;


    private ISelectPostionListener mStockBottomTabListener = new ISelectPostionListener() {
        @Override
        public void onSelectPosition(int position) {

            updateStickHeaderPosition(position);
            replaceBottomTabFragment(tabBottomFragment.get(position));

        }
    };

    private ISelectPostionListener mStockBottomTabFirstListener = new ISelectPostionListener() {
        @Override
        public void onSelectPosition(int position) {
            updatetabBottomFirstPosition(position);
            replaceBottomFirstTabFragment(tabBottomFirstFragment.get(position));
        }
    };


    private void updateStickHeaderPosition(int position) {
        if (hsTitleBottom.getCurrentPosition() != position) {
            hsTitleBottom.setSelectPositionListener(null);
            hsTitleBottom.setSelectIndex(position);
            hsTitleBottom.setSelectPositionListener(mStockBottomTabListener);
        }
        if (hsTitleSticker.getCurrentPosition() != position) {
            hsTitleSticker.setSelectPositionListener(null);
            hsTitleSticker.setSelectIndex(position);
            hsTitleSticker.setSelectPositionListener(mStockBottomTabListener);
        }
    }

    private void updatetabBottomFirstPosition(int position) {
        if (hs_title_first.getCurrentPosition() != position) {
            hs_title_first.setSelectPositionListener(null);
            hs_title_first.setSelectIndex(position);
            hs_title_first.setSelectPositionListener(mStockBottomTabFirstListener);
        }
    }

    public class IndexTabSelectListener implements ISelectPostionListener {

        private String mExchange;
        private String mListSector = null;

        public IndexTabSelectListener(String exchange, String listSector) {
            this.mExchange = exchange;
            this.mListSector = listSector;
        }

        @Override
        public void onSelectPosition(int position) {
            updateStickHeaderPosition(position);
            replaceBottomTabFragment(tabBottomFragment.get(position));
        }
    }

    private void replaceBottomTabFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.stock_layout, fragment).commitAllowingStateLoss();
    }
    private void replaceBottomFirstTabFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.stock_layout_first, fragment).commitAllowingStateLoss();
    }


    private boolean isIndexType() {
        return null != mStockBean && mStockBean.symbol_type != null
                && mStockBean.symbol_type.equalsIgnoreCase(StockUitls.SYMBOLTYPE_INDEX);
    }


    private void setAddOptionalButton() {
        if (mStockQuotesBean == null) {
            mActionMenu.setVisibility(View.GONE);
            return;
        }
        mActionMenu.setVisibility(View.VISIBLE);

        if (!PortfolioApplication.hasUserLogin()) {
            // SelectStockBean selectBean = SelectStockBean.
            SelectStockBean selectBean = new SelectStockBean();
            selectBean.id = mStockQuotesBean.getId();
            selectBean.code = mStockQuotesBean.getCode();
            if (localList != null && localList.contains(selectBean)) {
                mStockQuotesBean.setFollowed(true);
            }
        }

        initFloatingActionMenu(mStockQuotesBean);

    }

    private void scrollToTop() {

        mScrollview.setScrollViewListener(mScrollViewListener);
    }

    private boolean isFirstLoadQuotes = true;

    private void requestData() {
        if (null != mQuotesEngine && mStockBean != null) {
            // requestUiHandler.sendEmptyMessage(MSG_WHAT_BEFORE_REQUEST);
            rotateRefreshButton();
            if (isFirstLoadQuotes) {
                mQuotesEngine.quotes(mStockBean.symbol, quoteListener);
                isFirstLoadQuotes = false;
            } else {
                mQuotesEngine.quotesNotTip(mStockBean.symbol, quoteListener);
            }
            // listener.setLoadingDialog(context);
        }
    }

    WeakHandler requestUiHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
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
            return false;
        }
    });


    ScrollViewListener mScrollViewListener = new ScrollViewListener() {

        @Override
        public void onScrollChanged(InterceptScrollView scrollView, int x, int y, int oldx, int oldy) {
            if (y >= (ll_top.getMeasuredHeight())) {
                hsTitleSticker.setVisibility(View.VISIBLE);
            } else {
                hsTitleSticker.setVisibility(View.GONE);
            }
        }

        @Override
        public void onScrollBottom() {
          //  BusProvider.getInstance().post(new StockNewListLoadListBean());
        }
    };


    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                pager.setCurrentItem(position);
                if (position == 0) {
                    tvKlinVirtulCheck.setVisibility(View.GONE);
                } else {
                    if (null != mStockBean && !StockUitls.isIndexStock(mStockBean.symbol_type)) {
                        tvKlinVirtulCheck.setVisibility(View.VISIBLE);
                    }
                }

            }
        }
    };
    ParseHttpListener quoteListener = new ParseHttpListener<StockQuotesBean>() {

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

            mSwipeRefreshLayout.setRefreshing(false);
            stopRefreshAnimation();
            if (null != object) {
                mStockQuotesBean = object;
                updateStockView();
                mStockQuotesChartFragment.setStockQuotesBean(mStockQuotesBean);
                setAddOptionalButton();
            }
        }
    };

    private void initTabPage() {

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
        fragment.setKChartDataListener(this);
        fragmentList.add(fragment);
        KChartsFragment fragment2 = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_WEEK, mStockCode,
                symbolType);
        // fragment2.setITouchListener(this);
        fragment2.setStockViewCallBack(this);
        fragment2.setKChartDataListener(this);
        fragmentList.add(fragment2);
        KChartsFragment fragment3 = KChartsFragment.getKChartFragment(KChartsFragment.TYPE_CHART_MONTH, mStockCode,
                symbolType);
        // fragment3.setITouchListener(this);
        fragment3.setStockViewCallBack(this);
        fragment3.setKChartDataListener(this);
        fragmentList.add(fragment3);
        // fragmentList.add(new TestFragment());
        pager = (ScrollViewPager) this.findViewById(R.id.pager);
        pager.removeAllViews();
        pager.setCanScroll(false);
        pager.setOffscreenPageLimit(4);
        pager.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager(), fragmentList));
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
        setTitle(mStockQuotesBean.getAbbrName() + "(" + mStockQuotesBean.getSymbol() + ")");
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
            tvYesterDay.setText(StringFromatUtils.get3Point(mStockQuotesBean.getLastClose()));
        } else {
            tvChange.setText(StringFromatUtils.get2PointPlus(mStockQuotesBean.getChange()));
            tvCurrent.setText(StringFromatUtils.get2Point(mStockQuotesBean.getCurrent()));
            tvHigh.setText(StringFromatUtils.get2Point(mStockQuotesBean.getHigh()));
            tvLow.setText(StringFromatUtils.get2Point(mStockQuotesBean.getLow()));
            tvOpen.setText(StringFromatUtils.get2Point(mStockQuotesBean.getOpen()));
            tvYesterDay.setText(StringFromatUtils.get2Point(mStockQuotesBean.getLastClose()));
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

        tvAmount.setText(StringFromatUtils.convertToWan(mStockQuotesBean.getAmount()));
        setTitleTipString(mStockQuotesBean.getTradetile() + " "
                + TimeUtils.getMDTimeString(mStockQuotesBean.getMoment()));
        if (StockUitls.isDelistStock(mStockBean.list_status)) {// 退市股票
            tvStockStatus.setVisibility(View.VISIBLE);
            viewPriceChange.setVisibility(View.GONE);
            tvStockStatus.setText(R.string.exit_stock);

        } else if (mStockBean.isStop) {// 停牌股票
            tvStockStatus.setVisibility(View.VISIBLE);
            viewPriceChange.setVisibility(View.GONE);
            tvStockStatus.setText(R.string.stop_stock);

        } else {
            tvStockStatus.setVisibility(View.GONE);
            viewPriceChange.setVisibility(View.VISIBLE);
        }
    }

    private ColorStateList getTextColor(float value) {
        if (value == 0 || mStockQuotesBean.getIs_stop() == 1) {
            return (ColorStateList) getResources().getColorStateList(R.color.tab_normal_black);
        }
        if (value < 0) {
            return (ColorStateList) getResources().getColorStateList(R.color.tag_green);
        }
        return (ColorStateList) getResources().getColorStateList(R.color.tag_red);
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
        if (resultCode == RESULT_OK) {
            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_SELECT_STOCK:
                    SelectStockBean selectBean = Parcels.unwrap(data
                            .getParcelableExtra(FragmentSelectStockFund.ARGUMENT_SELECT));
                    if (null != selectBean) {
                        mStockBean = selectBean;
                        setTitleDate();
                        setAddOptionalButton();
                    }
                    break;
                case REQUEST_CHECK:
                    checkValue = data.getStringExtra(ChangeCheckType.CHECK_TYPE);

                    setFuquanView();
                    if (fragmentList.get(pager.getCurrentItem()) instanceof KChartsFragment) {
                        ((KChartsFragment) fragmentList.get(pager.getCurrentItem())).regetDate(checkValue);
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == 0 && requestCode == REQUEST_LAND) {
            mDayKChart = (ArrayList<OHLCEntity>) data.getSerializableExtra("day_data");
            mWeekKChart = (ArrayList<OHLCEntity>) data.getSerializableExtra("week_data");
            mMonthKChart = (ArrayList<OHLCEntity>) data.getSerializableExtra("month_data");
            index = data.getIntExtra("tab_positon", 0);
            hsTitle.setSelectIndex(index);
        }
    }

    private void setFuquanView() {
        switch (checkValue) {
            case "0":
                tvKlinVirtulCheck.setText("不复权  ▼");
                // PortfolioApplication.getInstance().setCheckValue("0");
                // setc
                break;
            case "1":
                tvKlinVirtulCheck.setText("前复权  ▼");
                // PortfolioApplication.getInstance().setCheckValue("1");
                break;
            default:
                tvKlinVirtulCheck.setText("后复权  ▼");
                // PortfolioApplication.getInstance().setCheckValue("2");
                break;
        }

    }

    private void setTitleDate() {
        setTitle(mStockBean.name + "(" + mStockBean.symbol + ")");
    }

    Handler quoteHandler = new Handler();

    // Handler quoteHandler = new Handler();
    public void onStart() {
        super.onStart();
        // quoteHandler.postDelayed(updateRunnable, 6);// 打开定时器，60ms后执行runnable操作
    }

    ;

    public void onStop() {
        super.onStop();
        quoteHandler.removeCallbacks(updateRunnable);// 关闭定时器处理
    }

    Runnable updateRunnable = new Runnable() {

        @Override
        public void run() {
            // dataHandler.sendEmptyMessage(1722);
            quoteHandler.removeCallbacks(updateRunnable);
            requestData();
            quoteHandler.postDelayed(this, mPollRequestTime);// 隔60s再执行一次
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

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


    private void initFloatingActionMenu(StockQuotesBean item) {


        if (null != mActionMenu) {
            mActionMenu.removeAllItems();
            if (item.isFollowed()) {
                mActionMenu.addItem(MENU_FOLLOWE_OR_UNFOLLOWE, R.string.delete_fllow,
                        R.drawable.btn_del_item_normal);
                if (isIndexType()) {
                    mActionMenu.addItem(MENU_REMIND, R.string.float_menu_index_remind, R.drawable.ic_fm_remind);
                } else {
                    mActionMenu.addItem(MENU_REMIND, R.string.float_menu_stock_remind, R.drawable.ic_fm_remind);
                }
            } else {

                mActionMenu.addItem(MENU_FOLLOWE_OR_UNFOLLOWE, R.string.add_fllow, R.drawable.ic_add);

            }
        }

    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        quoteListener.stopRequest(true);
    }

    @Subscribe
    public void scrollToTop(StockQuotesStopTopBean bean) {
        if (hsTitleSticker.getVisibility() == View.VISIBLE) {
            mScrollview.smoothScrollBy(0, -(mScrollview.getScrollY() - top - hsTitleSticker.getMeasuredHeight()));
        }
    }

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
    }

    private boolean isChange = false;

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        viewHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                requestData();
            }

        }, 150);

    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        StockQuotesActivity.checkValue = checkValue;
        setFuquanView();
    }

    /**
     * @param paramInt
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void setViewType(int paramInt) {
        // TODO Auto-generated method stub

    }


    @Override
    public void stockMarkShow() {
        //开启横屏UI
        startActivityForResult(StockLandActivity.getIntent(this, mStockBean, mStockQuotesBean, pager.getCurrentItem(), mDayKChart, mWeekKChart, mMonthKChart), REQUEST_LAND);
    }

    //    private List<OHLCEntity> mDayKChart = Collections.EMPTY_LIST;
//    private List<OHLCEntity> mWeekKChart = Collections.EMPTY_LIST;
//    private List<OHLCEntity> mMonthKChart = Collections.EMPTY_LIST;
    private ArrayList<OHLCEntity> mDayKChart = new ArrayList<>();
    private ArrayList<OHLCEntity> mWeekKChart = new ArrayList<>();
    private ArrayList<OHLCEntity> mMonthKChart = new ArrayList<>();

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public List<OHLCEntity> getDayLineDatas() {
        // TODO Auto-generated method stub
        return this.mDayKChart;
    }

    /**
     * @param kLineDatas
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void setDayKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mDayKChart = (ArrayList<OHLCEntity>) kLineDatas;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public List<OHLCEntity> getMonthLineDatas() {
        return this.mMonthKChart;
    }

    /**
     * @param kLineDatas
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void setMonthKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mMonthKChart = (ArrayList<OHLCEntity>) kLineDatas;

    }


    @Override
    public List<OHLCEntity> getWeekLineDatas() {
        return this.mWeekKChart;
    }

    @Override
    public void setWeekKlineDatas(List<OHLCEntity> kLineDatas) {
        this.mWeekKChart = (ArrayList<OHLCEntity>) kLineDatas;
    }


    @Override
    public void landViewFadeOut() {
    }

    private int index;
}
