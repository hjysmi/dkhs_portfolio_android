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
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetBean;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetValue;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
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
public class TrendChartFragment extends Fragment {
    public static final String ARGUMENT_TREND_TYPE = "trend_type";

    // private static final String SAVED_LIST_POSITION = "list_position";

    public static final String TREND_TYPE_TODAY = "trend_today";
    public static final String TREND_TYPE_SEVENDAY = "trend_seven_day";
    public static final String TREND_TYPE_MONTH = "trend_month";
    public static final String TREND_TYPE_HISTORY = "trend_history";

    private String trendType;
    private boolean isTodayNetValue;
    private TextView tvTimeLeft;
    private TextView tvNoData;
    private TextView tvTimeRight;
    private TextView tvNetValue;
    private TextView tvUpValue;
    private TextView tvIncreaseValue;
    private TextView tvStartText;
    private TextView tvEndText;
    private TextView tvIncreaseText;

    private TrendChart mMaChart;

    private NetValueEngine mNetValueDataEngine;
    private CombinationBean mCombinationBean;

    // public static final String TREND_TYPE_TODAY="trend_today";
    public static TrendChartFragment newInstance(String trendType) {
        TrendChartFragment fragment = new TrendChartFragment();

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
        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
        mNetValueDataEngine = new NetValueEngine(mCombinationBean.getId());
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trend_chart, null);
        mMaChart = (TrendChart) view.findViewById(R.id.machart);
        initMaChart(mMaChart);
        initView(view);
        setupViewData();
        return view;
    }

    private void initView(View view) {
        tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);
        tvNetValue = (TextView) view.findViewById(R.id.tv_now_netvalue);
        tvUpValue = (TextView) view.findViewById(R.id.tv_updown_value);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_increase_value);
        tvStartText = (TextView) view.findViewById(R.id.tv_netvalue_text);
        tvEndText = (TextView) view.findViewById(R.id.tv_updown_text);
        tvIncreaseText = (TextView) view.findViewById(R.id.tv_increase_text);
        tvNoData = (TextView) view.findViewById(R.id.tv_nodate);
    }

    private void setupViewData() {
        // String strRight = "";
        // String strLeft = "";
        // if (isTodayNetValue) {
        // strLeft = "2014-09-04";
        // strRight = "14:28";
        // } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY) || trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {
        // strLeft = getString(R.string.time_start, "2014-07-06");
        // strRight = getString(R.string.time_end, "2014-07-13");
        // } else if (trendType.equalsIgnoreCase(TREND_TYPE_HISTORY)) {
        // strLeft = getString(R.string.time_start, "2014-05-05");
        // strRight = getString(R.string.to_now);
        // }
        //
        // tvTimeLeft.setText(strLeft);
        // tvTimeRight.setText(strRight);

        if (!isTodayNetValue) {
            tvStartText.setText(R.string.start_netvalue);
            tvEndText.setText(R.string.end_netvalue);
            tvIncreaseText.setText(R.string.netvalue_up);
        }

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
        if (isAdded()) {
            List<LineEntity> lines = new ArrayList<LineEntity>();
            LineEntity MA5 = new LineEntity();
            // MA5.setTitle("MA5");
            // MA5.setLineColor(ColorTemplate.getRaddomColor())
            MA5.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
            MA5.setLineData(lineDataList);
            lines.add(MA5);
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

    private List<Float> initMA(int length) {
        List<Float> MA5Values = new ArrayList<Float>();
        for (int i = 0; i < length; i++) {
            // MA5Values.add((float) new Random().nextInt(99));
            MA5Values.add(new Random().nextFloat() * 100);
        }
        return MA5Values;

    }

    ParseHttpListener todayListener = new ParseHttpListener<TodayNetValue>() {

        @Override
        protected TodayNetValue parseDateTask(String jsonData) {
            TodayNetValue todayNetvalue = DataParse.parseObjectJson(TodayNetValue.class, jsonData);

            return todayNetvalue;
        }

        @Override
        protected void afterParseData(TodayNetValue todayNetvalue) {

            if (todayNetvalue != null) {

                List<TodayNetBean> dayNetValueList = todayNetvalue.getChartlist();
                if (dayNetValueList != null && dayNetValueList.size() > 0) {
                    setYTitle(todayNetvalue.getBegin(), getMaxOffetValue(todayNetvalue));
                    setTodayPointTitle();
                    setLineData(lineDataList);

                    String lasttime = dayNetValueList.get(dayNetValueList.size() - 1).getTimestamp();
                    // int zIndex = lasttime.indexOf("T");
                    Calendar calender = TimeUtils.toCalendar(lasttime);
                    // String dateStr = lasttime.substring(0, zIndex);
                    tvTimeLeft.setText(calender.get(Calendar.YEAR) + "-" + (calender.get(Calendar.MONTH) + 1) + "-"
                            + calender.get(Calendar.DAY_OF_MONTH));
                    String timeStr = calender.get(Calendar.HOUR) + ":" + (calender.get(Calendar.MINUTE));
                    tvTimeRight.setText(timeStr);
                }

                tvNetValue.setText(StringFromatUtils.get4Point(todayNetvalue.getEnd()));
                float addupValue = todayNetvalue.getEnd() - todayNetvalue.getBegin();
                tvUpValue.setText(StringFromatUtils.get4Point(addupValue));
                float increase = addupValue / todayNetvalue.getBegin() * 100;
                tvIncreaseValue.setText(StringFromatUtils.getPercentValue(increase));

            }

        }
    };

    // private List<LinePointEntity> convertTodayBeanToLineEntity(List<TodayNetBean> dayNetValueList) {
    // List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
    // int dataLenght = dayNetValueList.size();
    // for (int i = dataLenght - 1; i >= 0; i--) {
    // LinePointEntity pointEntity = new LinePointEntity();
    // TodayNetBean todayBean = dayNetValueList.get(i);
    // pointEntity.setDesc(TimeUtils.getTimeString(todayBean.getTimestamp()));
    // pointEntity.setValue(todayBean.getNetvalue());
    // lineDataList.add(pointEntity);
    // }
    //
    // return lineDataList;
    // }

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(TodayNetValue todayNetvalue) {
        lineDataList.clear();
        float baseNum = todayNetvalue.getBegin();
        float maxNum = baseNum, minNum = baseNum;
        for (TodayNetBean bean : todayNetvalue.getChartlist()) {
            if (bean.getNetvalue() > maxNum) {
                maxNum = bean.getNetvalue();

            } else if (bean.getNetvalue() < minNum) {
                minNum = bean.getNetvalue();
            }

            LinePointEntity pointEntity = new LinePointEntity();
            // HitstroyNetBean todayBean = dayNetValueList.get(i);
            pointEntity.setDesc(TimeUtils.getTimeString(bean.getTimestamp()));
            pointEntity.setValue(bean.getNetvalue());
            lineDataList.add(pointEntity);

        }

        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        offetValue = maxNum > minNum ? maxNum : minNum;

        return offetValue;
    }

    List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(HistoryNetValue historyNetValue) {
        lineDataList.clear();
        float baseNum = historyNetValue.getBegin();
        float maxNum = baseNum, minNum = baseNum;
        List<HistoryNetBean> historyNetList = historyNetValue.getChartlist();
        int dataLenght = historyNetList.size();
        for (int i = 0; i < dataLenght; i++) {

            LinePointEntity pointEntity = new LinePointEntity();
            HistoryNetBean todayBean = historyNetList.get(i);
            float value = todayBean.getNetvalue();
            pointEntity.setDesc(todayBean.getDate());
            pointEntity.setValue(value);
            lineDataList.add(pointEntity);

            if (value > maxNum) {
                maxNum = value;

            } else if (value < minNum) {
                minNum = value;
            }
        }
        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        offetValue = maxNum > minNum ? maxNum : minNum;

        return offetValue;
        // return lineDataList;
        //
        // for (HitstroyNetBean bean : historyNetValue.getChartlist()) {
        // LinePointEntity dayBean = new LinePointEntity();
        // if (bean.getNetvalue() > maxNum) {
        // maxNum = bean.getNetvalue();
        //
        // } else if (bean.getNetvalue() < minNum) {
        // minNum = bean.getNetvalue();
        // }
        //
        // dayBean.setNetvalue(bean.getNetvalue());
        // dayBean.setTimestamp(bean.getDate());
        // lineDataList.add(dayBean);
        // }

    }

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
        // int baseNum = 1;
        // 增加20的空白区域
        offetYvalue = offetYvalue / 0.8f;
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
                if (dayNetValueList != null && dayNetValueList.size() < 7) {
                    tvNoData.setVisibility(View.VISIBLE);
                } else {
                    int sizeLength = dayNetValueList.size();
                    setYTitle(object.getBegin(), getMaxOffetValue(object));
                    setHistoryPointTitle();
                    setLineData(lineDataList);
                    String strLeft = getString(R.string.time_start, dayNetValueList.get(0).getDate());
                    String strRight = getString(R.string.time_end, dayNetValueList.get(sizeLength - 1).getDate());
                    tvTimeLeft.setText(strLeft);

                    tvTimeRight.setText(strRight);

                    setXTitle(dayNetValueList);

                }

                tvNetValue.setText(StringFromatUtils.get4Point(object.getBegin()));
                float addupValue = object.getEnd() - object.getBegin();
                tvUpValue.setText(StringFromatUtils.get4Point(object.getEnd()));
                // fl
                tvIncreaseValue.setText(StringFromatUtils.get4Point(addupValue));

            }
        }

    };

    private void setXTitle(List<HistoryNetBean> dayNetValueList) {
        List<String> xtitle = new ArrayList<String>();
        String endDate = dayNetValueList.get(0).getDate();
        if (TextUtils.isEmpty(endDate)) {
            xtitle.add("");
        } else {
            xtitle.add(endDate);

        }
        xtitle.add(dayNetValueList.get(dayNetValueList.size() - 1).getDate());
        mMaChart.setMaxPointNum(dayNetValueList.size());
        mMaChart.setAxisXTitles(xtitle);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        System.out.println("--------------------onPause");
        System.out.println("dataHandler.removeCallbacks");
        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
    }

    public void onStart() {
        super.onStart();
        if (trendType.equals(TREND_TYPE_TODAY)) {
            dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
        } else if (trendType.equals(TREND_TYPE_SEVENDAY)) {

            mNetValueDataEngine.requerySevenDay(historyNetValueListener);
        } else if (trendType.equals(TREND_TYPE_MONTH)) {

            mNetValueDataEngine.requeryOneMonth(historyNetValueListener);
        } else if (trendType.equals(TREND_TYPE_HISTORY)) {

            mNetValueDataEngine.requeryHistory(historyNetValueListener);
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            dataHandler.sendEmptyMessage(1722);
            if (null != mNetValueDataEngine) {
                mNetValueDataEngine.requeryToday(todayListener);
            }
            dataHandler.postDelayed(this, 60 * 1000);// 隔60s再执行一次
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (trendType.equals(TREND_TYPE_TODAY)) {
            System.out.println("setUserVisibleHint:" + isVisibleToUser);

            if (isVisibleToUser) {
                // 相当于Fragment的onResume{
                dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
            } else {
                System.out.println("dataHandler.removeCallbacks");
                dataHandler.removeCallbacks(runnable);// 关闭定时器处理
                // dataHandler.removeCallbacks(runnable);// 关闭定时器处理
                // 相当于Fragment的onPause
            }
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param hidden
     * @return
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        System.out.println("onHiddenChanged:" + hidden);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();

        System.out.println("onDetach:");
    }

}
