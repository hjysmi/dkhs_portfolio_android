package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.fragment.FragmentMarkerCenter.RequestMarketTask;
import com.dkhs.portfolio.ui.widget.LandStockViewCallBack;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.ui.widget.chart.StickChart;
import com.dkhs.portfolio.ui.widget.kline.KChartsView;
import com.dkhs.portfolio.ui.widget.kline.KChartsView.DisplayDataChangeListener;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.kline.KChartsView;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

public class KChartsFragment extends Fragment {
    public static final int TYPE_CHART_DAY = 1;
    public static final int TYPE_CHART_WEEK = 2;
    public static final int TYPE_CHART_MONTH = 3;

    public static final String ARGUMENT_VIEW_TYPE = "view_type";
    public static final String ARGUMENT_STOCK_CODE = "stock_code";
    public static final String ARGUMENT_SYMBOL_TYPE = "symbol_type";

    private KChartsView mMyChartsView;
    private StickChart mVolumnChartView; // 成交量饼图
    private Integer mViewType = TYPE_CHART_DAY; // 类型，日K线，周k先，月k线
    private String mStockCode; // 股票code
    private QuotesEngineImpl mQuotesDataEngine;

    private ImageButton mLargerButton;
    private ImageButton mSmallerButton;

    public static final boolean testInterface = false; // 测试，使用本地数据
    private boolean first = true;
    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 45;
    List<OHLCEntity> ohlcs = new ArrayList<OHLCEntity>();
    private boolean having = true;
    private String symbolType;
    private RelativeLayout pb;
    private String checkValue = "0";

    public static KChartsFragment getKChartFragment(Integer type, String stockcode, String symbolType) {

        KChartsFragment fg = new KChartsFragment();
        // fg.setType(type);
        // fg.setStockCode(stockcode);
        // fg.setSymbolType(symbolType);
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_VIEW_TYPE, type);
        arguments.putString(ARGUMENT_STOCK_CODE, stockcode);
        arguments.putString(ARGUMENT_SYMBOL_TYPE, symbolType);
        fg.setArguments(arguments);

        return fg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
        if (mQuotesDataEngine == null) {
            mQuotesDataEngine = new QuotesEngineImpl();
        }
    }

    private void handleArguments(Bundle arguments) {
        mViewType = arguments.getInt(ARGUMENT_VIEW_TYPE);
        mStockCode = arguments.getString(ARGUMENT_STOCK_CODE);
        symbolType = arguments.getString(ARGUMENT_SYMBOL_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kcharts, null);
        mMyChartsView = (KChartsView) view.findViewById(R.id.my_charts_view);
        mVolumnChartView = (StickChart) view.findViewById(R.id.chart_volumn);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        if (!(null != ohlcs && ohlcs.size() > 0)) {
            pb.setVisibility(View.VISIBLE);
        }
        ohlcs = new ArrayList<OHLCEntity>();
        initChartView();
        initVloumnChartView();
        mMyChartsView.setStick(mVolumnChartView);
        mMyChartsView.setContext(getActivity());
        mMyChartsView.setmStockBean(((StockQuotesActivity) getActivity()).getmStockBean());
        mMyChartsView.setType(mViewType);
        mLargerButton = (ImageButton) view.findViewById(R.id.btn_large);
        // mLargerButton.setVisibility(View.INVISIBLE);

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
            // mSmallerButton.setClickable(false);
            mSmallerButton.setSelected(true);
            // mLargerButton.setClickable(false);
            mLargerButton.setSelected(true);
        }
        return view;
    }

    private DisplayDataChangeListener mDisplayDataChangeListener = new DisplayDataChangeListener() {

        @Override
        public void onDisplayDataChange(List<OHLCEntity> entitys) {
            refreshVolumnCharts();
        }

    };

    // private ITouchListener mTouchListener;
    //
    // public void setITouchListener(ITouchListener touchListener) {
    // this.mTouchListener = touchListener;
    // }

    private void initChartView() {
        if (null != mStockCallback) {
            mMyChartsView.setCallBack(mStockCallback);
        }

        mMyChartsView.setAxisColor(Color.LTGRAY);
        mMyChartsView.setLongiLatitudeColor(Color.LTGRAY);
        mMyChartsView.setBorderColor(Color.LTGRAY);
        mMyChartsView.setDisplayLongitude(false);
        mMyChartsView.setDisplayAxisXTitle(false);
        mMyChartsView.setDisplayChangeListener(mDisplayDataChangeListener);
        // mMyChartsView.setITouchListener(mTouchListener);
        mMyChartsView.setSymbolType(getSymbolType());
        mMyChartsView.setSymbol(mStockCode);
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
        mVolumnChartView.setDisplayLongitude(false);
        mVolumnChartView.setBackgroudColor(Color.WHITE);
    }

    /**
     * 刷新k线图控件
     * 
     * @param ohlc
     */
    private void refreshChartsView(List<OHLCEntity> ohlc) {
        try {
            mMyChartsView.setOHLCData(ohlc);
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
                mVolumnChartView.setStickData(volumns);
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
             * for (int i = ohlc.size() - 1; i >= 0; i--) {
             * entity = ohlc.get(i);
             * temp = new OHLCEntity(entity.getVolume(), 0, entity.getDate(), entity.getMacd(), entity.getDiff(),
             * entity.getDea());
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
        // if (testInterface) {
        // return getTestDatas();
        // }

        // 测试
        // mStockCode = "SZ002252";
        // 获取K线类型，日，周，月
        try {
            String mtype = getKLineType();
            // if()
            mQuotesDataEngine.queryKLine(mtype, mStockCode, "0", mKlineHttpListener, getCheckValue());
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

    public void regetDate(String checkValue) {
        try {
            setCheckValue(checkValue);
            ohlcs.clear();
            refreshChartsView(ohlcs);
            ArrayList<OHLCEntity> volumns = new ArrayList<OHLCEntity>();
            mVolumnChartView.setStickData(volumns);
            mVolumnChartView.postInvalidate();
            pb.setVisibility(View.VISIBLE);
            getOHLCDatas();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取K线类型，日，周，月
     * 
     * @return
     */
    private String getKLineType() {
        switch (mViewType) {
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

    private IHttpListener mKlineHttpListener = new ParseHttpListener<List<OHLCEntity>>() {

        @Override
        protected List<OHLCEntity> parseDateTask(String jsonData) {
            List<OHLCEntity> ohlc = getOHLCDatasFromJson(jsonData);
            // ohlcs = new ArrayList<OHLCEntity>();
            // Collections.reverse(ohlc);
            return ohlc;
        }

        public void onFailure(int errCode, String errMsg) {
            // Toast.makeText(getActivity(), "数据获取失败！", Toast.LENGTH_LONG).show();
            // PromptManager.closeProgressDialog();
            pb.setVisibility(View.GONE);
        };

        @Override
        protected void afterParseData(List<OHLCEntity> object) {
            pb.setVisibility(View.GONE);
            if (null != object) {
                ohlcs.addAll(object);
            }
            refreshChartsView(ohlcs);
            if (object.size() > 50 && having) {
                // mSmallerButton.setClickable(true);
                mSmallerButton.setSelected(false);
                having = false;
            }

        }
    };

    /**
     * 从json中解析k线数据
     * 
     * @param jsonObject
     * @return
     */
    private List<OHLCEntity> getOHLCDatasFromJson(String jsonObject) {
        List<OHLCEntity> entitys = new ArrayList<OHLCEntity>();
        // if (jsonObject == null || jsonObject.trim().length() == 0) {
        // return entitys;
        // }

        try {
            // JSONArray ja = new JSONArray(jsonObject);
            entitys = DataParse.parseArrayJson(OHLCEntity.class, jsonObject);
            // for (int i = entity.size() - 1; i >= 0; i--) {
            // entitys.add(entity.get(i));
            // }
            // int len = ja.length();

            /*
             * if (len > 0) {
             * JSONObject jo = null;
             * OHLCEntity ohlc = null;
             * for (int i = len - 1; i >= 0; i--) {
             * jo = ja.getJSONObject(i);
             * if (jo != null) {
             * ohlc = new OHLCEntity();
             * if (jo.has("open"))
             * ohlc.setOpen(jo.getDouble("open"));
             * if (jo.has("high"))
             * ohlc.setHigh(jo.getDouble("high"));
             * if (jo.has("low"))
             * ohlc.setLow(jo.getDouble("low"));
             * if (jo.has("close"))
             * ohlc.setClose(jo.getDouble("close"));
             * if (jo.has("tradedate"))
             * ohlc.setDate(jo.getString("tradedate"));
             * if (jo.has("volume"))
             * ohlc.setVolume(jo.getDouble("volume"));
             * if (jo.has("change"))
             * ohlc.setChange(jo.getDouble("change"));
             * if (jo.has("percentage"))
             * ohlc.setPercentage(jo.getDouble("percentage"));
             * if (jo.has("macd"))
             * ohlc.setMacd(jo.getDouble("macd"));
             * if (jo.has("diff"))
             * ohlc.setDiff(jo.getDouble("diff"));
             * if (jo.has("dea"))
             * ohlc.setDea(jo.getDouble("dea"));
             * entitys.add(ohlc);
             * }
             * }
             * }
             */

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
            // mLargerButton.setClickable(false);
            mLargerButton.setSelected(true);
        } else {
            // mLargerButton.setVisibility(View.VISIBLE);
            // mLargerButton.setClickable(true);
            mLargerButton.setSelected(false);
        }
        if (mMyChartsView.isLargest()) {
            // mSmallerButton.setVisibility(View.INVISIBLE);
            // mSmallerButton.setClickable(false);
            mSmallerButton.setSelected(true);
        } else {
            // mSmallerButton.setVisibility(View.VISIBLE);
            // mSmallerButton.setClickable(true);
            mSmallerButton.setSelected(false);
        }
    }

    public Integer getType() {
        return mViewType;
    }

    public void setType(Integer type) {
        this.mViewType = type;
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
        if (isVisibleToUser) {
            // fragment可见时加载数据
            if (null != mLandCallBack) {
                checkValue = mLandCallBack.getCheckValue();
            }
            mQuotesDataEngine = new QuotesEngineImpl();
            getOHLCDatas();
            if (mMarketTimer == null) {
                mMarketTimer = new Timer(true);
                mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
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
            StockQuotesBean m = ((StockQuotesActivity) getActivity()).getmStockQuotesBean();
            if (null != m && UIUtils.roundAble(m)) {
                mMarketTimer.cancel();
            }
            String mtype = getKLineType();
            mQuotesDataEngine.queryKLine(mtype, mStockCode, "1", mKlineFlushListener, getCheckValue());
        }
    }

    private IHttpListener mKlineFlushListener = new ParseHttpListener<List<OHLCEntity>>() {

        @Override
        protected List<OHLCEntity> parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return getOHLCDatasFromJson(jsonData);
        }

        @Override
        protected void afterParseData(List<OHLCEntity> object) {
            if (null == ohlcs || ohlcs.size() == 0) {
                String mtype = getKLineType();
                mQuotesDataEngine.queryKLine(mtype, mStockCode, mLandCallBack.getCheckValue(), mKlineHttpListener,
                        getCheckValue());
            } else {
                if (object != null && !object.isEmpty()) {
                    ohlcs.add(0, object.get(0));
                    ohlcs.remove(1);
                }
                refreshChartsView(ohlcs);
            }

        }

        public void onFailure(int errCode, String errMsg) {
            pb.setVisibility(View.GONE);
        };
    };

    // private IHttpListener mKlineHttpListenerFlush = new BasicHttpListener() {
    //
    // @Override
    // public void onSuccess(String result) {
    // try {
    // List<OHLCEntity> ohlc = getOHLCDatasFromJson(result);
    // if (null == ohlcs || ohlcs.size() == 0) {
    // String mtype = getKLineType();
    // mQuotesDataEngine.queryKLine(mtype, mStockCode, mLandCallBack.getCheckValue(), mKlineHttpListener,
    // getCheckValue());
    // } else {
    // if (ohlc.size() > 0) {
    // ohlcs.add(0, ohlc.get(0));
    // ohlcs.remove(1);
    // }
    // refreshChartsView(ohlcs);
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // // PromptManager.closeProgressDialog();
    // }
    //
    // public void onFailure(int errCode, String errMsg) {
    // // Toast.makeText(getActivity(), "数据获取失败！", Toast.LENGTH_LONG).show();
    // // PromptManager.closeProgressDialog();
    // pb.setVisibility(View.GONE);
    // };
    // };
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_Kline);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    private LandStockViewCallBack mLandCallBack;

    public LandStockViewCallBack getLandCallBack() {
        return mLandCallBack;
    }

    public void setLandCallBack(LandStockViewCallBack mLandCallBack) {
        this.mLandCallBack = mLandCallBack;
    }

    private StockViewCallBack mStockCallback;

    public void setStockViewCallBack(StockViewCallBack callBack) {
        // this.mMaChart.setCallBack(callBack);
        this.mStockCallback = callBack;
        if (null != mMyChartsView) {
            mMyChartsView.setCallBack(mStockCallback);
        }
    }

}
