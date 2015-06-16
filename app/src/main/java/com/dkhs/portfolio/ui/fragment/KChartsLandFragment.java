package com.dkhs.portfolio.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.AbstractKChartView;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.KChartsLandCallBack;
import com.dkhs.portfolio.ui.widget.LandStockViewCallBack;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.ui.widget.chart.StickChart;
import com.dkhs.portfolio.ui.widget.kline.KChartsLandView;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
import com.dkhs.portfolio.ui.widget.kline.PageOHLCEntity;
import com.dkhs.portfolio.utils.StockUitls;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class KChartsLandFragment extends AbstractKChartView implements OnClickListener, KChartsLandCallBack, FragmentLifecycle {


    private static final String UNCHEK = "0";
    private static final String BEFORECHEK = "1";
    private static final String AFTERCHEK = "2";

    private KChartsLandView mMyChartsView;
    private StickChart mVolumnChartView; // 成交量饼图

    private ImageButton mLargerButton;
    private ImageButton mSmallerButton;

    public static final boolean testInterface = false; // 测试，使用本地数据
    private boolean first = true;
    private Timer mMarketTimer;
    List<OHLCEntity> ohlcs;
    private boolean having = true;
    // private String symbolType;
    private static final String TAG = KChartsLandFragment.class.getSimpleName();
    private RelativeLayout pb;
    private TextView tvUnCheck;
    private TextView tvBeforeCheck;
    private TextView tvAfterCheck;
    private TextView tvTurnover;
    private TextView tvMacd;
    private int page = 1;
    private View pbLoadMore;

    public static KChartsLandFragment getKChartFragment(Integer type, String stockcode, String symbolType) {
        KChartsLandFragment fg = new KChartsLandFragment();
        fg.setArguments(fg.getBundle(type, stockcode, symbolType));
        return fg;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_kcharts_land;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * @param view
     * @param savedInstanceState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        pbLoadMore = view.findViewById(R.id.pb_loadmore);
        mMyChartsView = (KChartsLandView) view.findViewById(R.id.my_charts_view);
        mVolumnChartView = (StickChart) view.findViewById(R.id.chart_volumn);
        tvUnCheck = (TextView) view.findViewById(R.id.klin_uncheck);
        tvBeforeCheck = (TextView) view.findViewById(R.id.klin_before_check);
        tvAfterCheck = (TextView) view.findViewById(R.id.klin_after_check);
        tvTurnover = (TextView) view.findViewById(R.id.kline_turnover);
        tvMacd = (TextView) view.findViewById(R.id.klin_macd);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        if (!TextUtils.isEmpty(getSymbolType()) && StockUitls.isIndexStock(getSymbolType())) {
            view.findViewById(R.id.land_kline_layout).setVisibility(View.GONE);
        }
        // if (TextUtils.isEmpty(getSymbolType())) {
        // getSymbolType() = getArguments().getString(SYMBOLETYPE);
        // if (!TextUtils.isEmpty(getSymbolType()) && UIUtils.isSymbleIndex(getSymbolType())) {
        // view.findViewById(R.id.land_kline_layout).setVisibility(View.GONE);
        // }
        // }
        if (!(null != ohlcs && ohlcs.size() > 0)) {
            pb.setVisibility(View.VISIBLE);
        }
        ohlcs = new ArrayList<OHLCEntity>();
        initChartView();
        initVloumnChartView();
        mMyChartsView.setStick(mVolumnChartView);
        mLargerButton = (ImageButton) view.findViewById(R.id.btn_large);
        // mLargerButton.setVisibility(View.INVISIBLE);
        tvUnCheck.setSelected(true);
        tvTurnover.setSelected(true);
        tvUnCheck.setOnClickListener(this);
        tvBeforeCheck.setOnClickListener(this);
        tvAfterCheck.setOnClickListener(this);
        tvTurnover.setOnClickListener(this);
        tvMacd.setOnClickListener(this);
        mLargerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                large(v);
            }
        });

        mSmallerButton = (ImageButton) view.findViewById(R.id.btn_small);
        mSmallerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                small(v);
            }
        });
        if (having) {
            mSmallerButton.setClickable(false);
            mSmallerButton.setSelected(true);
            mLargerButton.setClickable(false);
            mLargerButton.setSelected(true);
        }
        view.setClickable(true);
    }

    private void initChartView() {
        mMyChartsView.setAxisColor(Color.LTGRAY);
        mMyChartsView.setLongiLatitudeColor(Color.LTGRAY);
        mMyChartsView.setBorderColor(Color.LTGRAY);
        mMyChartsView.setDisplayLongitude(true);
        mMyChartsView.setDisplayAxisXTitle(false);

        mMyChartsView.setSymbolType(getSymbolType());
        mMyChartsView.setSymbol(getStockCode());
        mMyChartsView.setContext(getActivity());
        mMyChartsView.setKChartsLandCallBack(this);

    }

    /*
     * class OnChartListener implements OnTouchListener{
     * boolean g = true;
     * 
     * @Override
     * public boolean onTouch(View v, MotionEvent event) {
     * // TODO Auto-generated method stub
     * if(event.getAction() == MotionEvent.ACTION_DOWN){
     * g = true;
     * }
     * if(event.getAction() == MotionEvent.ACTION_MOVE){
     * if(g){
     * event.setLocation(0, 0);
     * mVolumnChartView.onSet(event);
     * }
     * g = false;
     * }
     * if(event.getAction() == MotionEvent.ACTION_UP && g){
     * mVolumnChartView.onSet(event);
     * }
     * return false;
     * }
     * 
     * }
     */
    private void initVloumnChartView() {
        mVolumnChartView.setAxisXColor(Color.LTGRAY);
        mVolumnChartView.setAxisYColor(Color.LTGRAY);
        mVolumnChartView.setLatitudeColor(Color.GRAY);
        mVolumnChartView.setLongitudeColor(Color.GRAY);
        mVolumnChartView.setBorderColor(Color.LTGRAY);
        mVolumnChartView.setLongtitudeFontColor(Color.GRAY);
        mVolumnChartView.setLatitudeFontColor(Color.GRAY);
        // mVolumnChartView.setStickFillColor(getResources().getColor(R.drawable.yellow));
        mVolumnChartView.setAxisMarginTop(0);
        mVolumnChartView.setAxisMarginRight(1);

        // 最大显示足数
        // mVolumnChartView.setMaxStickDataNum(52);
        // 最大纬线数
        mVolumnChartView.setLatitudeNum(1);
        // 最大经线数
        // mVolumnChartView.setLongtitudeNum(3);
        // 最大价格
        mVolumnChartView.setMaxValue(0);
        // 最小价格
        // mVolumnChartView.setMinValue(100);

        mVolumnChartView.setDisplayAxisXTitle(true);
        mVolumnChartView.setDisplayAxisYTitle(true);
        mVolumnChartView.setDisplayLatitude(false);
        mVolumnChartView.setDisplayLongitude(true);
        mVolumnChartView.setBackgroudColor(Color.WHITE);
    }

    /**
     * 刷新k线图控件
     *
     * @param ohlc
     */
    private void refreshChartsView(List<OHLCEntity> ohlc) {
        try {
            mMyChartsView.setOHLCData(ohlc, page);
            mMyChartsView.setShowLowerChartTabs(false);
            mMyChartsView.setLowerChartTabTitles(new String[]{"MACD", "KDJ"});
            mMyChartsView.postInvalidate();

            // 刷新成交量
            refreshVolumnCharts();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void refreshVolumnCharts() {
        try {
            // ArrayList<OHLCEntity> volumns = getVolumnFromOHLC(mMyChartsView.getDisplayOHLCEntitys());
            if (mMyChartsView.getDisplayOHLCEntitys() != null && mMyChartsView.getDisplayOHLCEntitys().size() > 0) {
                mVolumnChartView.setStickData(mMyChartsView.getDisplayOHLCEntitys(), page);
                mVolumnChartView.postInvalidate();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取k线数据
     *
     * @return
     */
    private List<OHLCEntity> getOHLCDatas() {

        // 获取K线类型，日，周，月
        try {

            mMyChartsView.setSymbolType(getSymbolType());
            mMyChartsView.setSymbol(getStockCode());
            // }
            String mtype = getKLineType();
            getQuotesDataEngine().queryKLine(mtype, getStockCode(), "0", mKlineHttpListener,
                    mLandCallBack.getCheckValue(), page);
            if (first) {
                // PromptManager.showProgressDialog(getActivity(), "", true);
                first = false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void loadMordKline() {
        page++;
        getOHLCDatas();
    }

    private IHttpListener mKlineHttpListener = new ParseHttpListener<List<OHLCEntity>>() {

        @Override
        protected List<OHLCEntity> parseDateTask(String jsonData) {
            return getOHLCDatasFromJson(jsonData);

        }

        public void onFailure(int errCode, String errMsg) {

            pb.setVisibility(View.GONE);
        }

        ;

        @Override
        protected void afterParseData(List<OHLCEntity> object) {
            Log.e("LoadMore", "-----------afterParseData---------");
            updateChartData(object);

            if (null != getKChartDataListener()) {
                setViewTypeData(ohlcs);
            }
        }
    };

    private void updateChartData(List<OHLCEntity> lineData) {
        if (null != lineData) {
            ohlcs.addAll(lineData);
        }
        updateChartView();
    }

    private void updateChartView() {
        refreshChartsView(ohlcs);
        pb.setVisibility(View.GONE);
        if (ohlcs.size() > 50 && having) {

            mSmallerButton.setClickable(true);
            mSmallerButton.setSelected(false);
            having = false;
        }
    }

    /**
     * 从json中解析k线数据
     *
     * @param jsonObject
     * @return
     */
    private List<OHLCEntity> getOHLCDatasFromJson(String jsonObject) {
        List<OHLCEntity> entitys = new ArrayList<OHLCEntity>();

        try {
            if (!jsonObject.contains("current_page")) {
                entitys = DataParse.parseArrayJson(OHLCEntity.class, jsonObject);
            } else {
                PageOHLCEntity mPageOHLCEntity = DataParse.parseObjectJson(PageOHLCEntity.class, jsonObject);
                page = mPageOHLCEntity.getPage();
                entitys = mPageOHLCEntity.getResults();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entitys;
    }

    /**
     * 测试数据
     *
     * @return
     */
    private List<OHLCEntity> getTestDatas() {
        return new ArrayList<OHLCEntity>();

    }

    public void large(View view) {
        mMyChartsView.makeLager();
        changeButtonState();
    }

    public void small(View view) {
        mMyChartsView.makeSmaller();
        changeButtonState();
    }

    /**
     * 改变增大，缩小按钮状态
     */
    private void changeButtonState() {
        if (mMyChartsView.isSmallest()) {
            // mLargerButton.setVisibility(View.INVISIBLE);
            mLargerButton.setClickable(false);
            mLargerButton.setSelected(true);
        } else {
            // mLargerButton.setVisibility(View.VISIBLE);
            mLargerButton.setClickable(true);
            mLargerButton.setSelected(false);
        }
        if (mMyChartsView.isLargest()) {
            // mSmallerButton.setVisibility(View.INVISIBLE);
            mSmallerButton.setClickable(false);
            mSmallerButton.setSelected(true);
        } else {
            // mSmallerButton.setVisibility(View.VISIBLE);
            mSmallerButton.setClickable(true);
            mSmallerButton.setSelected(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        Log.v(TAG, "-----------setUserVisibleHint:" + isVisibleToUser);
        setViewVisible(isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }


    public void setViewVisible(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            onVisible();
        } else {
            onUnVisible();
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        BusProvider.getInstance().register(this);
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }

    }

    @Override
    public void onVisible() {
        if (null != getKChartDataListener()) {
            List<OHLCEntity> lineDatas = getViewTypeData();
            if (null == lineDatas || lineDatas.isEmpty()) {
                getOHLCDatas();

            } else {
                if (null != ohlcs) {
                    ohlcs = lineDatas;
                    updateChartView();
                }
                // updateChartData(lineDatas);
            }
        } else {
            getOHLCDatas();
        }

        if (mMarketTimer == null) {
            mMarketTimer = new Timer(true);
            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
        }
        if (null != tvUnCheck && null != mLandCallBack) {

            updateCheckView(mLandCallBack.getCheckValue());
            updateVolumView(mLandCallBack.getStickType());

        }
    }

    @Override
    public void onUnVisible() {
        // 不可见时不执行操作
        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }
    }

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
            if (null != mLandCallBack) {

                // StockQuotesBean m = mLandCallBack.getStockQuotesBean();
                // if (null != m && UIUtils.roundAble(m)) {
                // mMarketTimer.cancel();
                // }
                // String mtype = getKLineType();
                // getQuotesDataEngine().queryKLine(mtype, getStockCode(), "1", mKlineFlushListener,
                // mLandCallBack.getCheckValue());
            }
        }
    }

    private IHttpListener mKlineFlushListener = new ParseHttpListener<List<OHLCEntity>>() {

        @Override
        protected List<OHLCEntity> parseDateTask(String jsonData) {
            return getOHLCDatasFromJson(jsonData);
        }

        @Override
        protected void afterParseData(List<OHLCEntity> object) {
            if (null == ohlcs || ohlcs.size() == 0) {
                String mtype = getKLineType();
                getQuotesDataEngine().queryKLine(mtype, getStockCode(), "0", mKlineHttpListener,
                        mLandCallBack.getCheckValue(), page);
            } else {
                if (null != object && object.size() > 0) {

                    mMyChartsView.flushFirshData(object.get(0));
                    mVolumnChartView.flushFirstData(object.get(0));
                }
            }

            if (null != getKChartDataListener()) {
                setViewTypeData(ohlcs);
            }

        }

        public void onFailure(int errCode, String errMsg) {
            pb.setVisibility(View.GONE);
        }

        ;
    };

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_Kline);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        BusProvider.getInstance().unregister(this);
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.klin_uncheck:
                if (!tvUnCheck.isSelected()) {
                    page = 1;
                    mMyChartsView.reSetdate();
                    updateCheckView(UNCHEK);
                    ohlcs.clear();
                    ArrayList<OHLCEntity> volumns = new ArrayList<OHLCEntity>();
                    mVolumnChartView.setStickData(volumns, page);
                    mVolumnChartView.postInvalidate();
                    refreshChartsView(ohlcs);
                    pb.setVisibility(View.VISIBLE);
                    mLandCallBack.setCheckValue(UNCHEK);
                    String mtype = getKLineType();
                    getQuotesDataEngine().queryKLine(mtype, getStockCode(), "0", mKlineHttpListener,
                            mLandCallBack.getCheckValue(), page);
                    // mLandCallBack.setChange(true);
                }
                break;
            case R.id.klin_before_check:
                if (!tvBeforeCheck.isSelected()) {
                    page = 1;
                    mMyChartsView.reSetdate();
                    updateCheckView(BEFORECHEK);
                    ohlcs.clear();
                    refreshChartsView(ohlcs);
                    ArrayList<OHLCEntity> volumns = new ArrayList<OHLCEntity>();
                    mVolumnChartView.setStickData(volumns, page);
                    mVolumnChartView.postInvalidate();
                    pb.setVisibility(View.VISIBLE);
                    mLandCallBack.setCheckValue(BEFORECHEK);
                    String mtype = getKLineType();
                    getQuotesDataEngine().queryKLine(mtype, getStockCode(), "0", mKlineHttpListener,
                            mLandCallBack.getCheckValue(), page);
                    // mLandCallBack.setChange(true);
                }
                break;
            case R.id.klin_after_check:
                if (!tvAfterCheck.isSelected()) {
                    page = 1;
                    mMyChartsView.reSetdate();
                    updateCheckView(AFTERCHEK);
                    ohlcs.clear();
                    refreshChartsView(ohlcs);
                    ArrayList<OHLCEntity> volumns = new ArrayList<OHLCEntity>();
                    mVolumnChartView.setStickData(volumns, page);
                    mVolumnChartView.postInvalidate();
                    pb.setVisibility(View.VISIBLE);
                    mLandCallBack.setCheckValue(AFTERCHEK);
                    String mtype = getKLineType();
                    getQuotesDataEngine().queryKLine(mtype, getStockCode(), "0", mKlineHttpListener,
                            mLandCallBack.getCheckValue(), page);
                    // mLandCallBack.setChange(true);
                }
                break;
            case R.id.kline_turnover:
                if (!tvTurnover.isSelected()) {
                    updateVolumView(StickChart.CHECK_COLUME);
                    refreshVolumnCharts();
                }
                break;
            case R.id.klin_macd:
                if (!tvMacd.isSelected()) {
                    updateVolumView(StickChart.CHECK_MACD);
                    refreshVolumnCharts();
                }
                break;
            default:
                break;
        }
    }

    private void updateCheckView(String checkvalue) {
        if (checkvalue.equals(UNCHEK)) {
            tvUnCheck.setSelected(true);
            tvBeforeCheck.setSelected(false);
            tvAfterCheck.setSelected(false);
        } else if (checkvalue.equals(BEFORECHEK)) {
            tvUnCheck.setSelected(false);
            tvBeforeCheck.setSelected(true);
            tvAfterCheck.setSelected(false);
        } else {
            tvUnCheck.setSelected(false);
            tvBeforeCheck.setSelected(false);
            tvAfterCheck.setSelected(true);
        }
    }

    private void updateVolumView(int stickType) {
        if (stickType == (StickChart.CHECK_COLUME)) {
            tvTurnover.setSelected(true);
            tvMacd.setSelected(false);
            mVolumnChartView.setCheckType(StickChart.CHECK_COLUME);
            mLandCallBack.setStickType(StickChart.CHECK_COLUME);
            mVolumnChartView.setLatitudeNum(1);
        } else if (stickType == (StickChart.CHECK_MACD)) {
            tvTurnover.setSelected(false);
            tvMacd.setSelected(true);
            mVolumnChartView.setCheckType(StickChart.CHECK_MACD);
            mLandCallBack.setStickType(StickChart.CHECK_MACD);
            mVolumnChartView.setLatitudeNum(3);
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private LandStockViewCallBack mLandCallBack;

    public LandStockViewCallBack getLandCallBack() {
        return mLandCallBack;
    }

    public void setLandCallBack(LandStockViewCallBack landCallBack) {
        this.mLandCallBack = landCallBack;
    }

    @Override
    public void onDisplayDataChange(List<OHLCEntity> entitys) {
        refreshVolumnCharts();
    }

    @Override
    public void onLoadMoreDataStart() {
        Log.e("LoadMore", "-----------onLoadMoreDataStart-----------");

        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    pbLoadMore.setVisibility(View.VISIBLE);

                }
            });
        }
    }

    @Override
    public void onLoadMoreDataEnd() {

        Log.e("LoadMore", "-----------onLoadMoreDataEnd-----------");

        if (isAdded()) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbLoadMore.setVisibility(View.GONE);

                }
            });
        }

    }

    private StockViewCallBack stockViewCallback;

    public void setStockViewCallback(StockViewCallBack stockViewCallback) {
        this.stockViewCallback = stockViewCallback;
    }

    @Override
    public void onDoubleClick(View view) {
        if (null != stockViewCallback) {
            stockViewCallback.landViewFadeOut();
        }

    }

    @Override
    public void loadMore() {

        Log.e("LoadMore", "-----------loadMore-----------");
        loadMordKline();

    }

}
