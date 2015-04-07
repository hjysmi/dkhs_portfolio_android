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
import com.dkhs.portfolio.ui.AbstractKChartView;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.fragment.FragmentMarkerCenter.RequestMarketTask;
import com.dkhs.portfolio.ui.widget.KChartDataListener;
import com.dkhs.portfolio.ui.widget.LandStockViewCallBack;
import com.dkhs.portfolio.ui.widget.StockViewCallBack;
import com.dkhs.portfolio.ui.widget.chart.StickChart;
import com.dkhs.portfolio.ui.widget.kline.KChartsView;
import com.dkhs.portfolio.ui.widget.kline.KChartsView.DisplayDataChangeListener;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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

public class KChartsFragment extends AbstractKChartView {

    private KChartsView mMyChartsView;
    private StickChart mVolumnChartView; // 成交量饼图

    // private QuotesEngineImpl mQuotesDataEngine;

    private ImageButton mLargerButton;
    private ImageButton mSmallerButton;

    public static final boolean testInterface = false; // 测试，使用本地数据
    // private Timer mMarketTimer;
    List<OHLCEntity> ohlcs = new ArrayList<OHLCEntity>();
    private boolean having = true;
    private RelativeLayout pb;
    private String checkValue = "0";

    public static KChartsFragment getKChartFragment(Integer type, String stockcode, String symbolType) {

        KChartsFragment fg = new KChartsFragment();
        fg.setArguments(fg.getBundle(type, stockcode, symbolType));
        return fg;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_kcharts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param view
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
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
        mMyChartsView.setType(getViewType());
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
    }

    private DisplayDataChangeListener mDisplayDataChangeListener = new DisplayDataChangeListener() {

        @Override
        public void onDisplayDataChange(List<OHLCEntity> entitys) {
            refreshVolumnCharts();
        }

    };

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
        mMyChartsView.setSymbol(getSymbolType());
        // mMyChartsView.setOnTouchListener(new OnChartListener());
    }

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
            ArrayList<OHLCEntity> volumns = mMyChartsView.getDisplayOHLCEntitys();
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
     * 获取k线数据
     * 
     * @return
     */
    private void queryDefData() {

        String mtype = getKLineType();
        getQuotesDataEngine().queryKLine(mtype, getStockCode(), mLandCallBack.getCheckValue(), mKlineHttpListener,
                getCheckValue());
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
            queryDefData();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private IHttpListener mKlineHttpListener = new ParseHttpListener<List<OHLCEntity>>() {

        @Override
        protected List<OHLCEntity> parseDateTask(String jsonData) {
            List<OHLCEntity> ohlc = getOHLCDatasFromJson(jsonData);

            return ohlc;
        }

        public void onFailure(int errCode, String errMsg) {

            pb.setVisibility(View.GONE);
        };

        @Override
        protected void afterParseData(List<OHLCEntity> object) {
            updateChartData(object);
            if (null != getKChartDataListener()) {
                setViewTypeData(ohlcs);
            }

        }
    };

    private void updateChartData(List<OHLCEntity> lineData) {
        pb.setVisibility(View.GONE);
        if (null != lineData) {
            ohlcs.addAll(lineData);
        }
        refreshChartsView(ohlcs);
        if (lineData.size() > 50 && having) {
            // mSmallerButton.setClickable(true);
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
            // JSONArray ja = new JSONArray(jsonObject);
            entitys = DataParse.parseArrayJson(OHLCEntity.class, jsonObject);

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

            mLargerButton.setSelected(true);
        } else {

            mLargerButton.setSelected(false);
        }
        if (mMyChartsView.isLargest()) {

            mSmallerButton.setSelected(true);
        } else {

            mSmallerButton.setSelected(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            // fragment可见时加载数据
            if (null != mLandCallBack) {
                checkValue = mLandCallBack.getCheckValue();
            }
            if (getQuotesDataEngine() == null) {
                setQuotesDataEngine(new QuotesEngineImpl());
            }
            if (null != getKChartDataListener()) {
                List<OHLCEntity> lineDatas = getViewTypeData();
                if (null == lineDatas || lineDatas.isEmpty()) {
                    queryDefData();

                } else {
                    updateChartData(lineDatas);
                }
            } else {
                queryDefData();
            }
            pollRequestData();

        } else {
            // 不可见时不执行操作
            updateHandler.removeCallbacks(requestMarketRunnable);
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

        // if (mMarketTimer != null) {
        // mMarketTimer.cancel();
        // mMarketTimer = null;
        // }
        updateHandler.removeCallbacks(requestMarketRunnable);

    }

    /**
     * 定时轮询获取K线数据
     */
    private void pollRequestData() {
        if (null != mLandCallBack) {
            StockQuotesBean m = mLandCallBack.getStockQuotesBean();
            if (m != null && !UIUtils.roundAble(m)) {
                updateHandler.postDelayed(requestMarketRunnable, mPollRequestTime);
            }
        }
    }

    Handler updateHandler = new Handler();
    Runnable requestMarketRunnable = new Runnable() {
        @Override
        public void run() {
            if (null != mLandCallBack) {

                StockQuotesBean m = mLandCallBack.getStockQuotesBean();
                String mtype = getKLineType();
                getQuotesDataEngine().queryKLine(mtype, getStockCode(), "1", mKlineFlushListener, getCheckValue());
                if (UIUtils.roundAble(m)) {
                    // mMarketTimer.cancel();
                    updateHandler.removeCallbacks(requestMarketRunnable);
                } else {
                    updateHandler.postDelayed(requestMarketRunnable, mPollRequestTime);
                }
            }

        }
    };

    // public class RequestMarketTask extends TimerTask {
    //
    // @Override
    // public void run() {
    // StockQuotesBean m = ((StockQuotesActivity) getActivity()).getmStockQuotesBean();
    // if (null != m && UIUtils.roundAble(m)) {
    // mMarketTimer.cancel();
    // }
    // String mtype = getKLineType();
    // getQuotesDataEngine().queryKLine(mtype, getStockCode(), "1", mKlineFlushListener, getCheckValue());
    // }
    // }

    private IHttpListener mKlineFlushListener = new ParseHttpListener<List<OHLCEntity>>() {

        @Override
        protected List<OHLCEntity> parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return getOHLCDatasFromJson(jsonData);
        }

        @Override
        protected void afterParseData(List<OHLCEntity> object) {
            if (null == ohlcs || ohlcs.size() == 0) {
                // String mtype = getKLineType();
                // getQuotesDataEngine().queryKLine(mtype, getStockCode(), mLandCallBack.getCheckValue(),
                // mKlineHttpListener, getCheckValue());
                queryDefData();
            } else {
                if (object != null && !object.isEmpty()) {
                    ohlcs.add(0, object.get(0));
                    ohlcs.remove(1);
                }
                refreshChartsView(ohlcs);
            }

            if (null != getKChartDataListener()) {
                setViewTypeData(ohlcs);
            }

        }

        public void onFailure(int errCode, String errMsg) {
            pb.setVisibility(View.GONE);
        };
    };

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
