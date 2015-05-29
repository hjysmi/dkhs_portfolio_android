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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.adapter.PagerFragmentAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentForOptionOnr;
import com.dkhs.portfolio.ui.fragment.FragmentForStockSHC;
import com.dkhs.portfolio.ui.fragment.FragmentNewsList;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund;
import com.dkhs.portfolio.ui.fragment.KChartsFragment;
import com.dkhs.portfolio.ui.fragment.StockQuotesChartFragment;
import com.dkhs.portfolio.ui.fragment.TabF10Fragment;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.IScrollExchangeListener;
import com.dkhs.portfolio.ui.widget.IStockQuoteScrollListener;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.InterceptScrollView.ScrollViewListener;
import com.dkhs.portfolio.ui.widget.KChartDataListener;
import com.dkhs.portfolio.ui.widget.LandStockViewCallBack;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
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
        LandStockViewCallBack, KChartDataListener, IStockQuoteScrollListener {

    private static final long serialVersionUID = 15121212311111156L;
    private long mLastClickTime = 0;

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
    private long mStockId;
    private String mStockCode;
    private Context context;
    private HScrollTitleView hsTitle;
    private HScrollTitleView hsTitleBottom;
    private HScrollTitleView hsTitleSticker;
    private ScrollViewPager pager;
    private ArrayList<Fragment> fragmentList;
    private StockQuotesChartFragment mStockQuotesChartFragment;
    private View bottomLayout;
    private StockQuotesActivity layouts;
    private View viewHeader;
    private String symbolType;
    //    private List<Fragment> bottmoTabFragmentList;
    private Button klinVirtulCheck;
    private static String checkValue = "0";
    private static final long mPollRequestTime = 1000 * 15;
    private static final String TAG = "StockQuotesActivity";
    private final int MENU_FOLLOWE_OR_UNFOLLOWE = 0;
    private final int MENU_REMIND = 1;
    private StockLandView landStockview;
    private FloatingActionMenu mActionMenu;


    public static Intent newIntent(Context context, SelectStockBean bean) {
        Intent intent = new Intent(context, StockQuotesActivity.class);
        intent.putExtra(EXTRA_STOCK, Parcels.wrap(bean));
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, " --- onNewIntent--");
        setIntent(intent);// must store the new intent unless getIntent() will return the old one
        processExtraData();
        requestData();
        initBottomTabFragment();
    }

    private void processExtraData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mStockBean = Parcels.unwrap(extras.getParcelable(EXTRA_STOCK));
            if (null != mStockBean) {
                mStockId = mStockBean.id;
                mStockCode = mStockBean.code;
                symbolType = mStockBean.symbol_type;
                setTitleDate();
            }
        }
    }

    private VisitorDataEngine mVisitorDataEngine;
    private List<SelectStockBean> localList;
    Handler viewHandler = new Handler();


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
            }

            ;
        }.start();
    }

    private void initView() {
        ViewStub viewstub;
        if (isIndexType()) {
            viewstub = (ViewStub) findViewById(R.id.layout_index_header);
        } else {
            viewstub = (ViewStub) findViewById(R.id.layout_stock_header);
        }
        mScrollview = (InterceptScrollView) findViewById(R.id.sc_content);
        if (viewstub != null) {
            viewHeader = viewstub.inflate();
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

        bottomLayout = findViewById(R.id.stock_layout);

        klinVirtulCheck = (Button) findViewById(R.id.klin_virtul_check);
        klinVirtulCheck.setOnClickListener(this);
        hsTitle = (HScrollTitleView) findViewById(R.id.hs_title);
        hsTitleBottom = (HScrollTitleView) findViewById(R.id.hs_title_bottom);
        hsTitleSticker = (HScrollTitleView) findViewById(R.id.hs_title_sticker);


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
                initLandStockView();

            }
        }, 800);


    }


    private int mTitleBarBottom;
    private int mMaxListHeight;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            mTitleBarBottom = getTitleView().getBottom();
            View contentView = findViewById(android.R.id.content);
            int contentHeight = contentView.getHeight();
            int hsTitleHeight = hsTitleBottom.getHeight();
            mMaxListHeight = contentHeight - hsTitleHeight;
            Log.e(TAG, "---------------contentHeight：" + contentHeight);
            Log.e(TAG, " --------------hsTitleHeight：" + hsTitleHeight);
            Log.e(TAG, " --------------mMaxListHeight：" + mMaxListHeight);
        }
    }

    private void handFollowOrUnfollowAction() {
        if (!PortfolioApplication.hasUserLogin()) {// 如果当前是游客模式，添加自选股到本地数据库
            final SelectStockBean selectBean = SelectStockBean.copy(mStockQuotesBean);
            if (null != selectBean) {
                if (selectBean.isFollowed) {
                    // selectBean.isFollowed = false;

                    PromptManager.getAlertDialog(this).setTitle(R.string.tips).setMessage(R.string.dialog_messag_delfollow).setButton1(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mStockQuotesBean.setFollowed(false);
                            mVisitorDataEngine.delOptionalStock(selectBean);
                            PromptManager.showDelFollowToast();
                        }
                    }).setButton3(getString(R.string.cancel), null).show();

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

                PromptManager.getAlertDialog(this).setTitle(R.string.tips).setMessage(R.string.dialog_messag_delfollow).setButton1(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mQuotesEngine.delfollow(mStockBean.id, baseListener);
                    }
                }).setButton3(getString(R.string.cancel), null).show();


            } else {
                mQuotesEngine.symbolfollow(mStockBean.id, baseListener);
            }
        }
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
        }
    }

    private void initBottomTabFragment() {
        tabBottomFragment = new ArrayList<Fragment>();
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
            mGrowFragmentUp.setStockQuoteScrollListener(this);
            mGrowFragmentDown.setStockQuoteScrollListener(this);
            mGrowFragmentHand.setStockQuoteScrollListener(this);
            tabBottomFragment.add(mGrowFragmentUp);
            tabBottomFragment.add(mGrowFragmentDown);
            tabBottomFragment.add(mGrowFragmentHand);
            replaceBottomTabFragment(tabBottomFragment.get(0));

            hsTitleBottom.setSelectPositionListener(new IndexTabSelectListener(exchange, listSector));
            hsTitleSticker.setSelectPositionListener(new IndexTabSelectListener(exchange, listSector));


        } else if (!(null != mStockBean.symbol_type && StockUitls.isIndexStock(mStockBean.symbol_type))) {

            FragmentNewsList fList = FragmentNewsList.newIntent(mStockBean.code);
            fList.setStockQuoteScrollListener(this);
            tabBottomFragment.add(fList);
            tabBottomFragment.add(FragmentForOptionOnr.newIntent(context, mStockBean.code, mStockBean.name, ""));
            tabBottomFragment.add(TabF10Fragment.newIntent(mStockBean.code, TabF10Fragment.TabType.INTRODUCTION));
            tabBottomFragment.add(TabF10Fragment.newIntent(mStockBean.code, TabF10Fragment.TabType.FINANCE));
            tabBottomFragment.add(TabF10Fragment.newIntent(mStockBean.code, TabF10Fragment.TabType.STOCK_HODLER));
            replaceBottomTabFragment(tabBottomFragment.get(0));

            hsTitleBottom.setSelectPositionListener(mStockBottomTabListener);
            hsTitleSticker.setSelectPositionListener(mStockBottomTabListener);

        }
    }

    private List<Fragment> tabBottomFragment;


    private ISelectPostionListener mStockBottomTabListener = new ISelectPostionListener() {
        @Override
        public void onSelectPosition(int position) {
            updateStickHeaderPosition(position);
            replaceBottomTabFragment(tabBottomFragment.get(position));

        }
    };

    @Override
    public int getMaxListHeight() {
        return mMaxListHeight - 70;
    }

    @Override
    public void interruptSrcollView() {
        mScrollview.setIsfocus(false);

    }

    @Override
    public void scrollviewObatin() {
        mScrollview.setIsfocus(true);
    }


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

    ;

    private void replaceBottomTabFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.stock_layout, fragment).commit();
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
                mQuotesEngine.quotes(mStockBean.code, quoteListener);
                isFirstLoadQuotes = false;
            } else {
                mQuotesEngine.quotesNotTip(mStockBean.code, quoteListener);
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
        }

        ;
    };
    ScrollViewListener mScrollViewListener = new ScrollViewListener() {

        @Override
        public void onScrollChanged(InterceptScrollView scrollView, int x, int y, int oldx, int oldy) {


            int offsetY = hsTitleBottom.getTop() - y;
            if (offsetY <= 0) {
                showStickHeader();
            } else {
                hideStickHeader();
            }

//            Log.e(TAG, "onScrollChanged botttomTitleTop:" + offsetY);
            /*
             * if (mScrollview.getScrollY() >=
             * getResources().getDimensionPixelOffset
             * (R.dimen.layout_height_all)) { chartTounching(); }
//             */
//            if (mScrollview.getScrollY() + mScrollview.getHeight() >= mScrollview.computeVerticalScrollRange()
//                    && null != bottmoTabFragmentList) {
//                Fragment fragment = bottmoTabFragmentList.get(mBottomFragmentAdapter.getCurrentItem());
//                if (fragment instanceof FragmentNewsList) {
//                    ((FragmentNewsList) fragment).loadMore();
//                }
//                if (fragment instanceof FragmentForOptionOnr) {
//                    ((FragmentForOptionOnr) fragment).loadMore();
//                }
//
////                ((FragmentNewsList) bottmoTabFragmentList.get(1)).loadMore();
////                ((FragmentForOptionOnr) bottmoTabFragmentList.get(2)).loadMore();
//            }
        }
    };


    private void showStickHeader() {
        if (hsTitleSticker.getVisibility() != View.VISIBLE) {
            hsTitleSticker.setVisibility(View.VISIBLE);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.stock_layout);
            if (null != fragment && fragment instanceof IScrollExchangeListener) {
                ((IScrollExchangeListener) fragment).scrollSelf();
                ;
            }
        }
//        stock_layout
    }

    private void hideStickHeader() {
        if (hsTitleSticker.getVisibility() != View.GONE) {
            hsTitleSticker.setVisibility(View.GONE);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.stock_layout);
            if (null != fragment && fragment instanceof IScrollExchangeListener) {
                ((IScrollExchangeListener) fragment).scrollParent();
            }
        }
    }

    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                pager.setCurrentItem(position);
                if (position == 0) {
                    klinVirtulCheck.setVisibility(View.GONE);
                } else {
                    if (null != mStockBean && !StockUitls.isIndexStock(mStockBean.symbol_type)) {
                        klinVirtulCheck.setVisibility(View.VISIBLE);
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

            stopRefreshAnimation();
            if (null != object) {
                mStockQuotesBean = object;
                updateStockView();
                if (null != landStockview) {
                    landStockview.updateLandStockView(mStockQuotesBean);
                }
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
        pager.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager(), fragmentList));
        // TabPageIndicator indicator = (TabPageIndicator) this.findViewById(R.id.indicator);
        // indicator.setViewPager(pager);


    }


    private void initBottomList() {

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
        setTitle(mStockQuotesBean.getName() + "(" + mStockQuotesBean.getSymbol() + ")");
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
            return (ColorStateList) getResources().getColorStateList(R.color.theme_color);
        }
        if (value < 0) {
            return (ColorStateList) getResources().getColorStateList(R.color.green);
        }
        return (ColorStateList) getResources().getColorStateList(R.color.red);
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
                            .getParcelableExtra(FragmentSelectStockFund.ARGUMENT));
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

    }

    private void setTitleDate() {
        setTitle(mStockBean.name + "(" + mStockBean.code + ")");
    }

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
        quoteListener.stopRequest(true);
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

                requestData();
            }

        }, 150);

    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
        setFuquanView();
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void landViewFadeOut() {
        isFull = false;
        rotaVericteStockView();

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
                new float[]{90.0F});
        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.playTogether(new Animator[]{localObjectAnimator1, localObjectAnimator2});
        localAnimatorSet.start();
        if (null != mStockQuotesBean) {
            landStockview.updateLandStockView(mStockQuotesBean);
        }

    }

    private void rotaVericteStockView() {
        bottomLayout.setVisibility(View.VISIBLE);
        viewHeader.setVisibility(View.VISIBLE);
        mActionMenu.setVisibility(View.VISIBLE);
        landStockview.setVisibility(View.INVISIBLE);
        fragmentList.get(pager.getCurrentItem()).setUserVisibleHint(true);
        showHead();
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(this.bottomLayout, "alpha", new float[]{1.0F})
                .setDuration(100L);
        ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(this.viewHeader, "alpha", new float[]{1.0F})
                .setDuration(100L);

        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnimator) {
                StockQuotesActivity.this.setSwipeBackEnable(true);
                full(isFull);
            }
        });
        localAnimatorSet.playTogether(new Animator[]{bottomAnimator, headerAnimator});
        localAnimatorSet.start();
    }

    private void rotaLandStockView() {
        ObjectAnimator bottomAnimator = ObjectAnimator.ofFloat(this.bottomLayout, "alpha", new float[]{0.0F})
                .setDuration(100L);
        ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(this.viewHeader, "alpha", new float[]{0.0F})
                .setDuration(100L);

        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator paramAnimator) {
                full(isFull);
                bottomLayout.setVisibility(View.GONE);
                viewHeader.setVisibility(View.GONE);
                mActionMenu.setVisibility(View.GONE);
                hideHead();
                landStockview.setVisibility(View.VISIBLE);
                if (null != pager && null != fragmentList) {
                    int position = pager.getCurrentItem();
                    if (fragmentList.size() > position) {
                        fragmentList.get(position).setUserVisibleHint(false);
                    }
                }
            }

            public void onAnimationStart(Animator paramAnimator) {
                super.onAnimationStart(paramAnimator);
                StockQuotesActivity.this.setSwipeBackEnable(false);
            }
        });
        localAnimatorSet.playTogether(new Animator[]{bottomAnimator, headerAnimator});
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
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public StockQuotesBean getStockQuotesBean() {
        // TODO Auto-generated method stub
        return mStockQuotesBean;
    }

    /**
     * @param stickValue
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void setStickType(int stickValue) {
        this.stickType = stickValue;

    }

    @Override
    public int getTabPosition() {
        if (null != pager) {

            return pager.getCurrentItem();
        }
        return 0;
    }


    @Override
    public void setTabPosition(int position) {

        hsTitle.setSelectIndex(position);

    }

    private List<OHLCEntity> mDayKChart = Collections.EMPTY_LIST;
    private List<OHLCEntity> mWeekKChart = Collections.EMPTY_LIST;
    private List<OHLCEntity> mMonthKChart = Collections.EMPTY_LIST;

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
        this.mDayKChart = kLineDatas;
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
