/**
 * @Title TrendChartFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.FSDataBean;
import com.dkhs.portfolio.bean.FSDataBean.TimeStock;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.FiveRangeAdapter;
import com.dkhs.portfolio.ui.adapter.FiveRangeAdapter.FiveRangeItem;
import com.dkhs.portfolio.ui.widget.FSLinePointEntity;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePointEntity;
import com.dkhs.portfolio.ui.widget.TimesharingplanChart;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName TrendChartFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version 1.0
 */
public class StockQuotesChartFragment extends Fragment {
    public static final String ARGUMENT_TREND_TYPE = "trend_type";
    public static final String ARGUMENT_STOCK_CODE = "stock_code";

    // private static final String SAVED_LIST_POSITION = "list_position";

    public static final String TREND_TYPE_TODAY = "trend_today";
    public static final String TREND_TYPE_SEVENDAY = "trend_seven_day";
    public static final String TREND_TYPE_MONTH = "trend_month";
    public static final String TREND_TYPE_HISTORY = "trend_history";

    private String trendType;
    private boolean isTodayNetValue;

    private TimesharingplanChart mMaChart;

    private QuotesEngineImpl mQuotesDataEngine;
    private CombinationBean mCombinationBean;

    private FiveRangeAdapter mBuyAdapter, mSellAdapter;
    private ListView mListviewBuy, mListviewSell;

//    private long mStockId;
    private String mStockCode;
    LineEntity fenshiPiceLine;

    // public static final String TREND_TYPE_TODAY="trend_today";
    public static StockQuotesChartFragment newInstance(String trendType,String stockCode) {
        StockQuotesChartFragment fragment = new StockQuotesChartFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TREND_TYPE, trendType);
        arguments.putString(ARGUMENT_STOCK_CODE, stockCode);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handle fragment arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // restore saved state
        if (savedInstanceState != null) {
            handleSavedInstanceState(savedInstanceState);
        }

        // handle intent extras
        // Bundle extras = getActivity().getIntent().getExtras();
        // if (extras != null) {
        // handleExtras(extras);
        // }

        fenshiPiceLine = new LineEntity();
        // MA5.setTitle("MA5");
        // MA5.setLineColor(ColorTemplate.getRaddomColor())
        fenshiPiceLine.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
        mBuyAdapter = new FiveRangeAdapter(getActivity(), true);
        mSellAdapter = new FiveRangeAdapter(getActivity(), false);
        // fenshiPiceLine.setLineData(lineDataList);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        mQuotesDataEngine = new QuotesEngineImpl();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(ARGUMENT_TREND_TYPE);
        mStockCode = arguments.getString(ARGUMENT_STOCK_CODE);
        if (trendType.equals(TREND_TYPE_TODAY)) {
            isTodayNetValue = true;
        }
    }

    private void handleSavedInstanceState(Bundle savedInstanceState) {
    }

//    private void handleExtras(Bundle extras) {
//        // TODO private void handleExtras(Bundle extras) {
//        // mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
//        // if (null != mCombinationBean) {
//        SelectStockBean mSelectBean = (SelectStockBean) extras.getSerializable(StockQuotesActivity.EXTRA_STOCK);
//        if (null != mSelectBean) {
//            mStockId = mSelectBean.id;
//            mStockCode = mSelectBean.code;
//        }
//        
//        // }
//    }

    @Override
    public void onAttach(Activity activity) {
        // o
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_quotes_chart, null);
        mMaChart = (TimesharingplanChart) view.findViewById(R.id.timesharingchart);
        initMaChart(mMaChart);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListviewBuy = (ListView) view.findViewById(R.id.list_five_range_buy);
        mListviewSell = (ListView) view.findViewById(R.id.list_five_range_sall);

        mListviewBuy.setAdapter(mBuyAdapter);
        mListviewSell.setAdapter(mSellAdapter);
        // tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        // tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);
        // tvNetValue = (TextView) view.findViewById(R.id.tv_now_netvalue);
        // tvUpValue = (TextView) view.findViewById(R.id.tv_updown_value);
        // tvIncreaseValue = (TextView) view.findViewById(R.id.tv_increase_value);
        // tvStartText = (TextView) view.findViewById(R.id.tv_netvalue_text);
        // tvEndText = (TextView) view.findViewById(R.id.tv_updown_text);
        // tvIncreaseText = (TextView) view.findViewById(R.id.tv_increase_text);
    }

    private void initMaChart(TrendChart machart) {

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);

        machart.setDisplayBorder(false);
        // machart.setDrawXBorke(true);

        machart.setLatitudeColor(Color.LTGRAY);

        // machart.setMaxValue(120);
        // machart.setMinValue(0);
        // machart.setMaxPointNum(72);
        // machart.setDisplayAxisYTitle(false);
        // machart.setDisplayLatitude(true);
        // machart.setFill(true);

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);
        machart.setBorderColor(Color.TRANSPARENT);
        machart.setBackgroudColor(Color.WHITE);
        machart.setAxisMarginTop(10);
        machart.setAxisMarginLeft(20);
        machart.setAxisMarginRight(10);

        machart.setLongtitudeFontSize(10);
        machart.setLongtitudeFontColor(Color.GRAY);
        machart.setDisplayAxisYTitleColor(true);
        machart.setLatitudeColor(Color.GRAY);
        machart.setLatitudeFontColor(Color.GRAY);
        machart.setLongitudeColor(Color.GRAY);
        machart.setMaxValue(120);
        machart.setMinValue(0);

        machart.setDisplayAxisXTitle(true);
        machart.setDisplayAxisYTitle(true);
        machart.setDisplayLatitude(true);
        machart.setDisplayLongitude(true);
        machart.setFill(true);
        machart.setDrawFirstLineInfo(true);
        
        
        machart.setITouchListener(mTouchListener);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            machart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if (isTodayNetValue) {
            initTodayTrendTitle();
        } else {
            // initTrendTitle();
        }
        // machart.setFill(true);
        // machart.setFillLineIndex(2);

    }
    
    private ITouchListener mTouchListener;

    public void setITouchListener(ITouchListener touchListener) {
        this.mTouchListener = touchListener;
    }

    private void setLineData(List<FSLinePointEntity> lineDataList) {
        if (isAdded()) {
            List<LineEntity> lines = new ArrayList<LineEntity>();

            fenshiPiceLine.setLineData(lineDataList);
            LineEntity averageLine = new LineEntity();
            averageLine.setLineColor(PortfolioApplication.getInstance().getResources().getColor(R.color.orange));
            averageLine.setLineData(averagelineData);

            lines.add(0, fenshiPiceLine);
            lines.add(averageLine);
            mMaChart.setLineData(lines);
        }
    }

    // private void set

    private void initTodayTrendTitle() {

        List<String> xtitle = new ArrayList<String>();
        xtitle.add("9:30");
        xtitle.add("10:30");
        xtitle.add("11:30");
        xtitle.add("14:00");
        xtitle.add("15:00");

        mMaChart.setAxisXTitles(xtitle);
        mMaChart.setMaxPointNum(240);

        List<String> rightYtitle = new ArrayList<String>();
        rightYtitle.add(StringFromatUtils.get2PointPercent(-1f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(-0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(1f));
        mMaChart.setAxisRightYTitles(rightYtitle);
    }

    // private void initTrendTitle() {
    // // List<String> ytitle = new ArrayList<String>();
    // // ytitle.add("1.1051");
    // // ytitle.add("1.0532");
    // // ytitle.add("1.0000");
    // // ytitle.add("1.0001");
    // // ytitle.add("1.0522");
    //
    // List<String> xtitle = new ArrayList<String>();
    // xtitle.add("2014-09-15");
    // xtitle.add("2014-09-22");
    // mMaChart.setMaxPointNum(7);
    // mMaChart.setAxisYTitles(ytitle);
    // mMaChart.setAxisXTitles(xtitle);
    // }

    private List<LinePointEntity> initMA(int length) {
        List<LinePointEntity> MA5Values = new ArrayList<LinePointEntity>();
        NetValueEngine outer = new NetValueEngine(0);
        for (int i = 0; i < length; i++) {
            // MA5Values.add((float) new Random().nextInt(99));
            LinePointEntity bean = new LinePointEntity();
            bean.setDesc("2014-09-23");
            bean.setValue(1);
            MA5Values.add(bean);
        }
        return MA5Values;

    }

    StockQuotesBean mStockBean;

    public void setStockQuotesBean(StockQuotesBean bean) {
        if (isAdded()) {

            this.mStockBean = bean;
            List<FiveRangeItem> buyList = new ArrayList<FiveRangeAdapter.FiveRangeItem>();
            List<FiveRangeItem> sellList = new ArrayList<FiveRangeAdapter.FiveRangeItem>();
            int i = 0;
            for (String buyPrice : bean.getBuyPrice().getBuyPrice()) {
                FiveRangeItem buyItem = new FiveRangeAdapter(getActivity(), isTodayNetValue).new FiveRangeItem();

                buyItem.price = buyPrice;
                if (i < bean.getBuyPrice().getBuyVol().size()) {
                    buyItem.vol = bean.getBuyPrice().getBuyVol().get(i);
                } else {
                    buyItem.vol = "0";
                }
                buyItem.tag = "" + (++i);
                buyList.add(buyItem);
            }
            // i = 0;
            for (String sellPrice : bean.getSellPrice().getSellPrice()) {
                FiveRangeItem sellItem = new FiveRangeAdapter(getActivity(), isTodayNetValue).new FiveRangeItem();
                sellItem.price = sellPrice;
                if (i < bean.getSellPrice().getSellVol().size()) {
                    sellItem.vol = bean.getSellPrice().getSellVol().get(i);
                } else {
                    sellItem.vol = "0";
                }
                sellItem.tag = "" + (i--);
                sellList.add(sellItem);
            }

            mBuyAdapter.setList(buyList);
            mSellAdapter.setList(sellList);

            mBuyAdapter.setCompareValue(bean.getLastClose());
            mSellAdapter.setCompareValue(bean.getLastClose());
        }

    }

    FSDataBean mFsDataBean = new FSDataBean();
    ParseHttpListener todayListener = new ParseHttpListener<FSDataBean>() {

        @Override
        protected FSDataBean parseDateTask(String jsonData) {
            FSDataBean fsDataBean = null;
            try {
                fsDataBean = DataParse.parseObjectJson(FSDataBean.class, jsonData);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fsDataBean;
        }

        @Override
        protected void afterParseData(FSDataBean fsDataBean) {

            if (fsDataBean != null) {
                mFsDataBean.setCurtime(fsDataBean.getCurtime());
                if (null == mFsDataBean.getMainstr()) {
                    mFsDataBean.setMainstr(fsDataBean.getMainstr());
                } else {

                    mFsDataBean.getMainstr().addAll(fsDataBean.getMainstr());
                }
                List<TimeStock> mainList = mFsDataBean.getMainstr();

                // List<TodayNetBean> dayNetValueList = todayNetvalue.getChartlist();
                if (mainList != null && mainList.size() > 0) {
                    setYTitle(mainList.get(0).getCurrent(), getMaxOffetValue(mainList));
                    setTodayPointTitle();
                    setLineData(lineDataList);
                    //
                    // String lasttime = dayNetValueList.get(dayNetValueList.size() - 1).getTimestamp();
                    // // int zIndex = lasttime.indexOf("T");
                    // Calendar calender = TimeUtils.toCalendar(lasttime);
                    // // String dateStr = lasttime.substring(0, zIndex);
                }

            }

        }
    };

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(List<TimeStock> mainList) {
        lineDataList.clear();
        averagelineData.clear();

        float baseNum = mainList.get(0).getCurrent();
        float maxNum = baseNum, minNum = baseNum;

        int minVolCount = mainList.get(0).getVolume();
        int maxVolCount = minVolCount;

        for (TimeStock bean : mainList) {
            float iPrice = bean.getCurrent();
            int volCount = bean.getVolume();

            if (iPrice > maxNum) {
                maxNum = iPrice;

            } else if (iPrice < minNum) {
                minNum = iPrice;
            }

            if (volCount < minVolCount) {
                minVolCount = volCount;
            } else if (volCount > maxVolCount) {
                maxVolCount = volCount;
            }

            FSLinePointEntity pointEntity = new FSLinePointEntity();
            LinePointEntity averagePoint = new LinePointEntity();

            // pointEntity.setDesc(TimeUtils.getTimeString(bean.getTime()));
            pointEntity.setValue(iPrice);
            pointEntity.setTime(TimeUtils.getTimeString(bean.getTime()));
            pointEntity.setPrice(StringFromatUtils.get2Point(iPrice));
            pointEntity.setIncreaseValue(iPrice - baseNum);
            pointEntity.setIncreaseRange(bean.getPercentage());
            pointEntity.setTurnover(bean.getVolume());
            pointEntity.setAvgPrice(bean.getAvgline());

            averagePoint.setValue(bean.getAvgline());
            lineDataList.add(pointEntity);
            averagelineData.add(averagePoint);
        }

        fenshiPiceLine.setMaxVolNum(maxVolCount);
        fenshiPiceLine.setMinVolNum(minVolCount);

        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        offetValue = maxNum > minNum ? maxNum : minNum;

        return offetValue;
    }

    List<FSLinePointEntity> lineDataList = new ArrayList<FSLinePointEntity>();
    List<LinePointEntity> averagelineData = new ArrayList<LinePointEntity>();

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
        // int baseNum = 1;
        offetYvalue = offetYvalue / 0.8f;
        List<String> ytitle = new ArrayList<String>();
        List<String> rightYtitle = new ArrayList<String>();
        float halfOffetValue = offetYvalue / 2.0f;

        ytitle.add(StringFromatUtils.get2Point(baseNum - offetYvalue));
        ytitle.add(StringFromatUtils.get2Point(baseNum - halfOffetValue));
        ytitle.add(StringFromatUtils.get2Point(baseNum));
        ytitle.add(StringFromatUtils.get2Point(baseNum + halfOffetValue));
        ytitle.add(StringFromatUtils.get2Point(baseNum + offetYvalue));
        mMaChart.setAxisYTitles(ytitle);
        mMaChart.setMaxValue(baseNum + offetYvalue);
        mMaChart.setMinValue(baseNum - offetYvalue);

        rightYtitle.add(StringFromatUtils.get2PointPercent(-(baseNum + offetYvalue) / baseNum));
        rightYtitle.add(StringFromatUtils.get2PointPercent(-(baseNum + halfOffetValue) / baseNum));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        rightYtitle.add(StringFromatUtils.get2PointPercent((baseNum + halfOffetValue) / baseNum));
        rightYtitle.add(StringFromatUtils.get2PointPercent((baseNum + offetYvalue) / baseNum));

        mMaChart.setDrawRightYTitle(true);
        mMaChart.setAxisRightYTitles(rightYtitle);

    }

    private void setTodayPointTitle() {
        List<String> titles = new ArrayList<String>();
        titles.add("时间");
        titles.add("价格");
        mMaChart.setPointTitleList(titles);
    }

    Handler dataHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (trendType.equals(TREND_TYPE_TODAY)) {

                // setLineData(initMA(new Random().nextInt(240)));

            } else {

            }
        };
    };

    private void setXTitle(List<HistoryNetBean> dayNetValueList) {
        List<String> xtitle = new ArrayList<String>();
        xtitle.add(dayNetValueList.get(dayNetValueList.size() - 1).getDate());
        xtitle.add(dayNetValueList.get(0).getDate());
        mMaChart.setMaxPointNum(dayNetValueList.size());
        mMaChart.setAxisXTitles(xtitle);

    }

    public void onStart() {
        super.onStart();
        if (null != mStockBean) {
            setStockQuotesBean(mStockBean);
        }
        if (trendType.equals(TREND_TYPE_TODAY)) {
            dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
        }

    };

    public void onStop() {
        super.onStop();
        dataHandler.removeCallbacks(runnable);// 关闭定时器处理

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            dataHandler.sendEmptyMessage(1722);
            if (mQuotesDataEngine == null) {
                return;
            }
            if (null != mQuotesDataEngine && TextUtils.isEmpty(mFsDataBean.getCurtime())) {
                mQuotesDataEngine.queryTimeShare(mStockCode, todayListener);

            } else {
                mQuotesDataEngine.queryMoreTimeShare(mStockCode, mFsDataBean.getCurtime(), todayListener);

            }
            dataHandler.postDelayed(this, 60 * 1000);// 隔60s再执行一次
        }
    };

}
