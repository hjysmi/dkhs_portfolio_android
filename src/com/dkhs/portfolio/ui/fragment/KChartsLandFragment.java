package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.KChartLandScapeActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.DoubleclickEvent;
import com.dkhs.portfolio.ui.widget.LandStockViewCallBack;
import com.dkhs.portfolio.ui.widget.OnDoubleClickListener;
import com.dkhs.portfolio.ui.widget.chart.StickChart;
import com.dkhs.portfolio.ui.widget.kline.KChartsLandView;
import com.dkhs.portfolio.ui.widget.kline.PageOHLCEntity;
import com.dkhs.portfolio.ui.widget.kline.KChartsLandView.DisplayDataChangeListener;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

public class KChartsLandFragment extends Fragment implements OnClickListener {
    public static final int TYPE_CHART_DAY = 1;
    public static final int TYPE_CHART_WEEK = 2;
    public static final int TYPE_CHART_MONTH = 3;
    private static final String UNCHEK = "0";
    private static final String BEFORECHEK = "1";
    private static final String AFTERCHEK = "2";

    private KChartsLandView mMyChartsView;
    private StickChart mVolumnChartView; // 成交量饼图
    private Integer type = TYPE_CHART_DAY; // 类型，日K线，周k先，月k线
    private String mStockCode; // 股票code
    private QuotesEngineImpl mQuotesDataEngine;

    private ImageButton mLargerButton;
    private ImageButton mSmallerButton;

    public static final boolean testInterface = false; // 测试，使用本地数据
    private boolean first = true;
    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 45;
    List<OHLCEntity> ohlcs;
    private boolean having = true;
    private String symbolType;
    private final static String TYPE = "type";
    private final static String CODE = "code";
    private final static String SYMBOLETYPE = "symboltype";
    private static final String TAG = "KChartsLandFragment";
    private RelativeLayout pb;
    private TextView tvUnCheck;
    private TextView tvBeforeCheck;
    private TextView tvAfterCheck;
    private TextView tvTurnover;
    private TextView tvMacd;
    private int page = 1;
    private boolean addmore = true;
    private View pbLoadMore;

    public static KChartsLandFragment getKChartFragment(Integer type, String stockcode, String symbolType) {
        KChartsLandFragment fg = new KChartsLandFragment();
        Bundle b = new Bundle();
        b.putInt(TYPE, type);
        b.putString(CODE, stockcode);
        b.putString(SYMBOLETYPE, symbolType);
        fg.setArguments(b);
        fg.setType(type);
        fg.setStockCode(stockcode);
        fg.setSymbolType(symbolType);
        return fg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mQuotesDataEngine == null) {
            mQuotesDataEngine = new QuotesEngineImpl();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kcharts_land, null);
        pbLoadMore = view.findViewById(R.id.pb_loadmore);
        mMyChartsView = (KChartsLandView) view.findViewById(R.id.my_charts_view);
        mVolumnChartView = (StickChart) view.findViewById(R.id.chart_volumn);
        tvUnCheck = (TextView) view.findViewById(R.id.klin_uncheck);
        tvBeforeCheck = (TextView) view.findViewById(R.id.klin_before_check);
        tvAfterCheck = (TextView) view.findViewById(R.id.klin_after_check);
        tvTurnover = (TextView) view.findViewById(R.id.kline_turnover);
        tvMacd = (TextView) view.findViewById(R.id.klin_macd);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        if (!TextUtils.isEmpty(symbolType) && UIUtils.isSymbleIndex(symbolType)) {
            view.findViewById(R.id.land_kline_layout).setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(symbolType)) {
            symbolType = getArguments().getString(SYMBOLETYPE);
            if (!TextUtils.isEmpty(symbolType) && UIUtils.isSymbleIndex(symbolType)) {
                view.findViewById(R.id.land_kline_layout).setVisibility(View.GONE);
            }
        }
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
        return view;
    }

    private DisplayDataChangeListener mDisplayDataChangeListener = new DisplayDataChangeListener() {

        @Override
        public void onDisplayDataChange(List<OHLCEntity> entitys) {
            refreshVolumnCharts();
        }

        @Override
        public void onLoadMoreDataStart() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    pbLoadMore.setVisibility(View.VISIBLE);

                }
            });
        }

        @Override
        public void onLoadMoreDataEnd() {
            // TODO Auto-generated method stub

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pbLoadMore.setVisibility(View.GONE);

                }
            });

        }

    };
    private ITouchListener mTouchListener;

    public void setITouchListener(ITouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    private void initChartView() {
        mMyChartsView.setAxisColor(Color.LTGRAY);
        mMyChartsView.setLongiLatitudeColor(Color.LTGRAY);
        mMyChartsView.setBorderColor(Color.LTGRAY);
        mMyChartsView.setDisplayLongitude(true);
        mMyChartsView.setDisplayAxisXTitle(false);
        mMyChartsView.setDisplayChangeListener(mDisplayDataChangeListener);
        mMyChartsView.setITouchListener(mTouchListener);
        mMyChartsView.setSymbolType(getSymbolType());
        mMyChartsView.setSymbol(mStockCode);
        mMyChartsView.setContext(getActivity());
        mMyChartsView.setDoubleClicklistener(new OnDoubleClickListener() {

            @Override
            public void OnDoubleClick(View view) {
                // TODO Auto-generated method stub
                BusProvider.getInstance().post(new DoubleclickEvent());
            }
        });
        // mMyChartsView.setOnTouchListener(new OnChartListener());
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
            mMyChartsView.setLowerChartTabTitles(new String[] { "MACD", "KDJ" });
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
            ArrayList<OHLCEntity> volumns = getVolumnFromOHLC(mMyChartsView.getDisplayOHLCEntitys());
            if (volumns != null && volumns.size() > 0) {
                mVolumnChartView.setStickData(volumns, page);
                mVolumnChartView.postInvalidate();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取成交量
     * 
     * @param ohlc
     * @return
     */
    private ArrayList<OHLCEntity> getVolumnFromOHLC(ArrayList<OHLCEntity> ohlc) {
        try {
            if (ohlc == null || ohlc.size() == 0) {
                return null;
            }

            /*
             * List<OHLCEntity> volumns = new ArrayList<OHLCEntity>();
             * OHLCEntity temp = null;
             * OHLCEntity entity = null;
             * double k = 0;
             * for (int i = ohlc.size() - 1; i >= 0; i--) {
             * entity = ohlc.get(i);
             * if(i%2 == 0){
             * k = i;
             * }else{
             * k = -i;
             * }
             * temp = new OHLCEntity(entity.getVolume(), 0,
             * entity.getDate(),entity.getMacd(),entity.getDiff(),entity.getDea());
             * temp.setUp(entity.isup());
             * volumns.add(temp);
             * }
             */

            return ohlc;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取k线数据
     * 
     * @return
     */
    private List<OHLCEntity> getOHLCDatas() {
        // 测试，使用写死数据
        if (testInterface) {
            return getTestDatas();
        }

        // 测试
        // mStockCode = "SZ002252";
        // 获取K线类型，日，周，月
        try {
            if (null == mStockCode) {
                Bundle b = getArguments();
                type = b.getInt(TYPE);
                mStockCode = b.getString(CODE);
                symbolType = b.getString(SYMBOLETYPE);
                mMyChartsView.setSymbolType(symbolType);
                mMyChartsView.setSymbol(mStockCode);
            }
            String mtype = getKLineType();
            mQuotesDataEngine.queryKLine(mtype, mStockCode, "0", mKlineHttpListener,
            // ((StockQuotesActivity) getActivity()).getCheckValue(), page);
                    "0", page);
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
        if (addmore) {
            page++;
            getOHLCDatas();
        }
    }

    /**
     * 获取K线类型，日，周，月
     * 
     * @return
     */
    private String getKLineType() {
        switch (type) {
            case TYPE_CHART_DAY:
                return "d";
            case TYPE_CHART_WEEK:
                return "w";
            case TYPE_CHART_MONTH:
                return "m";
            default:
                break;
        }
        return "d";
    }

    private IHttpListener mKlineHttpListener = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            try {
                pb.setVisibility(View.GONE);
                List<OHLCEntity> ohlc = getOHLCDatasFromJson(result);
                ohlcs = new ArrayList<OHLCEntity>();
                for (int i = ohlc.size() - 1; i >= 0; i--) {
                    ohlcs.add(ohlc.get(i));
                }
                refreshChartsView(ohlcs);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // PromptManager.closeProgressDialog();
        }

        public void onFailure(int errCode, String errMsg) {
            // Toast.makeText(getActivity(), "数据获取失败！", Toast.LENGTH_LONG).show();
            // PromptManager.closeProgressDialog();
            pb.setVisibility(View.GONE);
        };
    };

    /**
     * 从json中解析k线数据
     * 
     * @param jsonObject
     * @return
     */
    private List<OHLCEntity> getOHLCDatasFromJson(String jsonObject) {
        List<OHLCEntity> entitys = new ArrayList<OHLCEntity>();
        if (jsonObject == null || jsonObject.trim().length() == 0) {
            return entitys;
        }
        List<OHLCEntity> entity;
        try {
            if (!jsonObject.contains("current_page")) {
                entity = DataParse.parseArrayJson(OHLCEntity.class, jsonObject);
            } else {
                PageOHLCEntity mPageOHLCEntity = DataParse.parseObjectJson(PageOHLCEntity.class, jsonObject);
                page = mPageOHLCEntity.getPage();
                entity = mPageOHLCEntity.getResults();
            }
            // JSONArray ja = new JSONArray(jsonObject);
            // List<OHLCEntity> entity = DataParse.parseArrayJson(OHLCEntity.class, jsonObject);

            for (int i = entity.size() - 1; i >= 0; i--) {
                entitys.add(entity.get(i));
            }
            int len = entitys.size();

            if (len > 50 && having) {
                mSmallerButton.setClickable(true);
                mSmallerButton.setSelected(false);
                having = false;
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
        List<OHLCEntity> ohlc = new ArrayList<OHLCEntity>();

        return ohlc;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStockCode() {
        return mStockCode;
    }

    public void setStockCode(String mStockCode) {
        this.mStockCode = mStockCode;
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        Log.e(TAG, "setUserVisibleHint :" + this);
        if (isVisibleToUser) {
            // fragment可见时加载数据
            mQuotesDataEngine = new QuotesEngineImpl();
            List<OHLCEntity> ohlc = getOHLCDatas();
            if (mMarketTimer == null) {
                mMarketTimer = new Timer(true);
                mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
            }
            if (null != tvUnCheck && null != mLandCallBack) {
                if (mLandCallBack.getCheckValue().equals(UNCHEK)) {
                    tvUnCheck.setSelected(true);
                    tvBeforeCheck.setSelected(false);
                    tvAfterCheck.setSelected(false);
                } else if (mLandCallBack.getCheckValue().equals(BEFORECHEK)) {
                    tvUnCheck.setSelected(false);
                    tvBeforeCheck.setSelected(true);
                    tvAfterCheck.setSelected(false);
                } else {
                    tvUnCheck.setSelected(false);
                    tvBeforeCheck.setSelected(false);
                    tvAfterCheck.setSelected(true);
                }
                if (mLandCallBack.getStickType() == (StickChart.CHECK_COLUME)) {
                    tvTurnover.setSelected(true);
                    tvMacd.setSelected(false);
                    mVolumnChartView.setCheckType(StickChart.CHECK_COLUME);
                    mLandCallBack.setStickType(StickChart.CHECK_COLUME);
                    mVolumnChartView.setLatitudeNum(1);
                } else if (mLandCallBack.getStickType() == (StickChart.CHECK_MACD)) {
                    tvTurnover.setSelected(false);
                    tvMacd.setSelected(true);
                    mVolumnChartView.setCheckType(StickChart.CHECK_MACD);
                    ((KChartLandScapeActivity) getActivity()).setStickType(StickChart.CHECK_MACD);
                    mVolumnChartView.setLatitudeNum(3);
                }
            }
        } else {
            // 不可见时不执行操作
            if (mMarketTimer != null) {
                mMarketTimer.cancel();
                mMarketTimer = null;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
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

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
            if (null != mLandCallBack) {

                StockQuotesBean m = mLandCallBack.getStockQuotesBean();
                if (null != m && UIUtils.roundAble(m)) {
                    mMarketTimer.cancel();
                }
                String mtype = getKLineType();
                mQuotesDataEngine.queryKLine(mtype, mStockCode, "1", mKlineHttpListenerFlush,
                        mLandCallBack.getCheckValue());
            }
        }
    }

    private IHttpListener mKlineHttpListenerFlush = new BasicHttpListener() {

        @Override
        public void onSuccess(String result) {
            try {
                pb.setVisibility(View.GONE);
                List<OHLCEntity> ohlc = getOHLCDatasFromJson(result);
                if (null == ohlcs || ohlcs.size() == 0) {
                    String mtype = getKLineType();
                    mQuotesDataEngine.queryKLine(mtype, mStockCode, "0", mKlineHttpListener,
                            mLandCallBack.getCheckValue(), page);
                } else {
                    if (ohlc.size() > 0) {
                        /*
                         * ohlcs.add(0, ohlc.get(0));
                         * ohlcs.remove(1);
                         */
                        mMyChartsView.flushFirshData(ohlc.get(0));
                        mVolumnChartView.flushFirstData(ohlc.get(0));
                    }
                    // refreshChartsView(ohlcs);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // PromptManager.closeProgressDialog();
        }

        public void onFailure(int errCode, String errMsg) {
            // Toast.makeText(getActivity(), "数据获取失败！", Toast.LENGTH_LONG).show();
            // PromptManager.closeProgressDialog();
            pb.setVisibility(View.GONE);
        };
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
                    tvUnCheck.setSelected(true);
                    tvBeforeCheck.setSelected(false);
                    tvAfterCheck.setSelected(false);
                    ohlcs.clear();
                    ArrayList<OHLCEntity> volumns = new ArrayList<OHLCEntity>();
                    mVolumnChartView.setStickData(volumns, page);
                    mVolumnChartView.postInvalidate();
                    refreshChartsView(ohlcs);
                    pb.setVisibility(View.VISIBLE);
                    mLandCallBack.setCheckValue(UNCHEK);
                    String mtype = getKLineType();
                    mQuotesDataEngine.queryKLine(mtype, mStockCode, "0", mKlineHttpListener,
                            mLandCallBack.getCheckValue(), page);
                    // mLandCallBack.setChange(true);
                    mLandCallBack.setCheckValue(UNCHEK);
                }
                break;
            case R.id.klin_before_check:
                if (!tvBeforeCheck.isSelected()) {
                    page = 1;
                    mMyChartsView.reSetdate();
                    tvUnCheck.setSelected(false);
                    tvBeforeCheck.setSelected(true);
                    tvAfterCheck.setSelected(false);
                    ohlcs.clear();
                    refreshChartsView(ohlcs);
                    ArrayList<OHLCEntity> volumns = new ArrayList<OHLCEntity>();
                    mVolumnChartView.setStickData(volumns, page);
                    mVolumnChartView.postInvalidate();
                    pb.setVisibility(View.VISIBLE);
                    mLandCallBack.setCheckValue(BEFORECHEK);
                    String mtype = getKLineType();
                    mQuotesDataEngine.queryKLine(mtype, mStockCode, "0", mKlineHttpListener,
                            mLandCallBack.getCheckValue(), page);
                    // mLandCallBack.setChange(true);
                    mLandCallBack.setCheckValue(BEFORECHEK);
                }
                break;
            case R.id.klin_after_check:
                if (!tvAfterCheck.isSelected()) {
                    page = 1;
                    mMyChartsView.reSetdate();
                    tvUnCheck.setSelected(false);
                    tvBeforeCheck.setSelected(false);
                    tvAfterCheck.setSelected(true);
                    ohlcs.clear();
                    refreshChartsView(ohlcs);
                    ArrayList<OHLCEntity> volumns = new ArrayList<OHLCEntity>();
                    mVolumnChartView.setStickData(volumns, page);
                    mVolumnChartView.postInvalidate();
                    pb.setVisibility(View.VISIBLE);
                    mLandCallBack.setCheckValue(AFTERCHEK);
                    String mtype = getKLineType();
                    mQuotesDataEngine.queryKLine(mtype, mStockCode, "0", mKlineHttpListener,
                            mLandCallBack.getCheckValue(), page);
                    // mLandCallBack.setChange(true);
                    mLandCallBack.setCheckValue(AFTERCHEK);
                }
                break;
            case R.id.kline_turnover:
                if (!tvTurnover.isSelected()) {
                    tvTurnover.setSelected(true);
                    tvMacd.setSelected(false);
                    mVolumnChartView.setCheckType(StickChart.CHECK_COLUME);
                    ((KChartLandScapeActivity) getActivity()).setStickType(StickChart.CHECK_COLUME);
                    mVolumnChartView.setLatitudeNum(1);
                    refreshVolumnCharts();
                }
                break;
            case R.id.klin_macd:
                if (!tvMacd.isSelected()) {
                    tvTurnover.setSelected(false);
                    tvMacd.setSelected(true);
                    mVolumnChartView.setCheckType(StickChart.CHECK_MACD);
                    ((KChartLandScapeActivity) getActivity()).setStickType(StickChart.CHECK_MACD);
                    mVolumnChartView.setLatitudeNum(3);
                    refreshVolumnCharts();
                }
                break;
            default:
                break;
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

}
