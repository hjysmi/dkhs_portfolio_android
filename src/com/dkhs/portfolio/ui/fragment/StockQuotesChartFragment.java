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
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.FSDataBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetBean;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetValue;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.FiveRangeAdapter;
import com.dkhs.portfolio.ui.adapter.FiveRangeAdapter.FiveRangeItem;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePointEntity;
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

    // private static final String SAVED_LIST_POSITION = "list_position";

    public static final String TREND_TYPE_TODAY = "trend_today";
    public static final String TREND_TYPE_SEVENDAY = "trend_seven_day";
    public static final String TREND_TYPE_MONTH = "trend_month";
    public static final String TREND_TYPE_HISTORY = "trend_history";

    private String trendType;
    private boolean isTodayNetValue;

    private TrendChart mMaChart;

    private QuotesEngineImpl mQuotesDataEngine;
    private CombinationBean mCombinationBean;

    private FiveRangeAdapter mBuyAdapter, mSellAdapter;
    private ListView mListviewBuy, mListviewSell;

    private long mStockId;
    private String mStockCode;

    // public static final String TREND_TYPE_TODAY="trend_today";
    public static StockQuotesChartFragment newInstance(String trendType) {
        StockQuotesChartFragment fragment = new StockQuotesChartFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TREND_TYPE, trendType);
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
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(ARGUMENT_TREND_TYPE);
        if (trendType.equals(TREND_TYPE_TODAY)) {
            isTodayNetValue = true;
        }
    }

    private void handleSavedInstanceState(Bundle savedInstanceState) {
    }

    private void handleExtras(Bundle extras) {
        // TODO private void handleExtras(Bundle extras) {
        // mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
        // if (null != mCombinationBean) {
        SelectStockBean mSelectBean = (SelectStockBean) extras.getSerializable(StockQuotesActivity.EXTRA_STOCK);
        if (null != mSelectBean) {
            mStockId = mSelectBean.id;
            mStockCode = mSelectBean.code;
        }
        mQuotesDataEngine = new QuotesEngineImpl();
        // }
    }

    @Override
    public void onAttach(Activity activity) {
        // o
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_quotes_chart, null);
        mMaChart = (TrendChart) view.findViewById(R.id.machart);
        initMaChart(mMaChart);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListviewBuy = (ListView) view.findViewById(R.id.list_five_range_buy);
        mListviewSell = (ListView) view.findViewById(R.id.list_five_range_sall);

        mBuyAdapter = new FiveRangeAdapter(getActivity(), true);
        mSellAdapter = new FiveRangeAdapter(getActivity(), false);
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

    private void setLineData(List<LinePointEntity> lineDataList) {

        List<LineEntity> lines = new ArrayList<LineEntity>();
        LineEntity MA5 = new LineEntity();
        // MA5.setTitle("MA5");
        // MA5.setLineColor(ColorTemplate.getRaddomColor())
        MA5.setLineColor(getActivity().getResources().getColor(ColorTemplate.MY_COMBINATION_LINE));
        MA5.setLineData(lineDataList);

        LineEntity averageLine = new LineEntity();
        averageLine.setLineColor(getResources().getColor(R.color.orange));
        averageLine.setLineData(averagelineData);

        lines.add(MA5);
        lines.add(averageLine);
        mMaChart.setLineData(lines);
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
        this.mStockBean = bean;
        List<FiveRangeItem> buyList = new ArrayList<FiveRangeAdapter.FiveRangeItem>();
        List<FiveRangeItem> sellList = new ArrayList<FiveRangeAdapter.FiveRangeItem>();
        int i = 0;
        for (String buyPrice : bean.getBuyPrice().getBuyPrice()) {
            FiveRangeItem buyItem = new FiveRangeAdapter(getActivity(), isTodayNetValue).new FiveRangeItem();
            buyItem.tag = "买" + (++i);

            buyItem.price = buyPrice;
            if (i < bean.getBuyPrice().getBuyVol().size()) {
                buyItem.vol = bean.getBuyPrice().getBuyVol().get(i);
            } else {
                buyItem.vol = "0";
            }
            buyList.add(buyItem);
        }
        i = 0;
        for (String sellPrice : bean.getSellPrice().getSellPrice()) {
            FiveRangeItem sellItem = new FiveRangeAdapter(getActivity(), isTodayNetValue).new FiveRangeItem();
            sellItem.tag = "卖" + (++i);
            sellItem.price = sellPrice;
            if (i < bean.getSellPrice().getSellVol().size()) {
                sellItem.vol = bean.getSellPrice().getSellVol().get(i);
            } else {
                sellItem.vol = "0";
            }
            sellList.add(sellItem);
        }

        mBuyAdapter.setList(buyList);
        mSellAdapter.setList(sellList);

    }

    ParseHttpListener todayListener = new ParseHttpListener<FSDataBean>() {

        @Override
        protected FSDataBean parseDateTask(String jsonData) {
            FSDataBean fsDataBean = DataParse.parseObjectJson(FSDataBean.class, jsonData);

            return fsDataBean;
        }

        @Override
        protected void afterParseData(FSDataBean fsDataBean) {

            if (fsDataBean != null) {

                List<List<Float>> mainList = fsDataBean.getMainstr();

                // List<TodayNetBean> dayNetValueList = todayNetvalue.getChartlist();
                if (mainList != null && mainList.size() > 0) {
                    setYTitle(mainList.get(0).get(1), getMaxOffetValue(mainList));
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
    private float getMaxOffetValue(List<List<Float>> mainList) {
        lineDataList.clear();
        averagelineData.clear();
        int priceIndex = 1;
        float baseNum = mainList.get(0).get(priceIndex);
        float maxNum = baseNum, minNum = baseNum;
        for (List<Float> bean : mainList) {
            float iPrice = bean.get(priceIndex);
            if (iPrice > maxNum) {
                maxNum = iPrice;

            } else if (iPrice < minNum) {
                minNum = iPrice;
            }

            LinePointEntity pointEntity = new LinePointEntity();
            LinePointEntity point2Entity = new LinePointEntity();

            pointEntity.setDesc(TimeUtils.getTimeByMSecond(bean.get(0)));
            pointEntity.setValue(iPrice);
            point2Entity.setValue(bean.get(3));
            lineDataList.add(pointEntity);
            averagelineData.add(point2Entity);
        }

        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        offetValue = maxNum > minNum ? maxNum : minNum;

        return offetValue;
    }

    List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
    List<LinePointEntity> averagelineData = new ArrayList<LinePointEntity>();

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(HistoryNetValue historyNetValue) {
        lineDataList.clear();
        float baseNum = historyNetValue.getBegin();
        float maxNum = baseNum, minNum = baseNum;
        List<HistoryNetBean> historyNetList = historyNetValue.getChartlist();
        int dataLenght = historyNetList.size();
        for (int i = dataLenght - 1; i >= 0; i--) {

            LinePointEntity pointEntity = new LinePointEntity();
            HistoryNetBean todayBean = historyNetList.get(i);
            pointEntity.setDesc(todayBean.getDate());
            pointEntity.setValue(todayBean.getNetvalue());
            lineDataList.add(pointEntity);

            if (todayBean.getNetvalue() > maxNum) {
                maxNum = todayBean.getNetvalue();

            } else if (todayBean.getNetvalue() < minNum) {
                minNum = todayBean.getNetvalue();
            }
        }
        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        offetValue = maxNum > minNum ? maxNum : minNum;

        return offetValue;

    }

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
        // int baseNum = 1;
        List<String> ytitle = new ArrayList<String>();
        float halfOffetValue = offetYvalue / 2.0f;

        ytitle.add(StringFromatUtils.get4Point(baseNum - offetYvalue));
        ytitle.add(StringFromatUtils.get4Point(baseNum - halfOffetValue));
        ytitle.add(StringFromatUtils.get4Point(baseNum));
        ytitle.add(StringFromatUtils.get4Point(baseNum + halfOffetValue));
        ytitle.add(StringFromatUtils.get4Point(baseNum + offetYvalue));
        mMaChart.setAxisYTitles(ytitle);
        mMaChart.setMaxValue(baseNum + offetYvalue);
        mMaChart.setMinValue(baseNum - offetYvalue);

    }

    private void setTodayPointTitle() {
        List<String> titles = new ArrayList<String>();
        titles.add("时间");
        titles.add("当前净值");
        mMaChart.setPointTitleList(titles);
    }

    private void setHistoryPointTitle() {
        List<String> titles = new ArrayList<String>();
        titles.add("日期");
        titles.add("当前净值");
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

    ParseHttpListener historyNetValueListener = new ParseHttpListener<HistoryNetValue>() {

        @Override
        protected HistoryNetValue parseDateTask(String jsonData) {
            HistoryNetValue histroyValue = DataParse.parseObjectJson(HistoryNetValue.class, jsonData);
            return histroyValue;
        }

        @Override
        protected void afterParseData(HistoryNetValue object) {
            if (object != null) {

                List<HistoryNetBean> dayNetValueList = object.getChartlist();
                if (dayNetValueList != null && dayNetValueList.size() > 1) {
                    int sizeLength = dayNetValueList.size();
                    setYTitle(object.getBegin(), getMaxOffetValue(object));
                    setHistoryPointTitle();
                    setLineData(lineDataList);

                    setXTitle(dayNetValueList);

                }

            }
        }

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
            if (null != mQuotesDataEngine) {
                mQuotesDataEngine.queryTimeShare(mStockCode, todayListener);
            }
            dataHandler.postDelayed(this, 60 * 1000);// 隔60s再执行一次
        }
    };

}
