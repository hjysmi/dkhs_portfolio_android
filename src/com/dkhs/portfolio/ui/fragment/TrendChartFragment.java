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

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetBean;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetValue;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePointEntity;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.ui.widget.TrendLinePointEntity;
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
    public static final String TREND_TYPE_TODAY = "trend_today";
    public static final String TREND_TYPE_SEVENDAY = "trend_seven_day";
    public static final String TREND_TYPE_MONTH = "trend_month";
    public static final String TREND_TYPE_HISTORY = "trend_history";

    private String trendType = TREND_TYPE_TODAY;
    // private boolean isTodayNetValue;
    private TextView tvTimeLeft;
    private TextView tvTimeRight;
    private TextView tvNetValue;
    private TextView tvUpValue;
    private TextView tvIncreaseValue;
    private TextView tvStartText;
    private TextView tvEndText;
    private TextView tvIncreaseText;

    private View viewDashLineTip;

    private TrendChart mMaChart;

    private NetValueEngine mNetValueDataEngine;
    private CombinationBean mCombinationBean;

    private Handler updateHandler;
    private Calendar mCreateCalender;

    private List<TrendLinePointEntity> lineDataList = new ArrayList<TrendLinePointEntity>();
    // private List<TrendLinePointEntity> todayLineDataList = new ArrayList<TrendLinePointEntity>();
    // private List<TrendLinePointEntity> weekLineDataList = new ArrayList<TrendLinePointEntity>();
    // private List<TrendLinePointEntity> monthLineDataList = new ArrayList<TrendLinePointEntity>();
    // private List<TrendLinePointEntity> alLineDataList = new ArrayList<TrendLinePointEntity>();

    private TodayNetValue mTodayNetvalue;
    private HistoryNetValue mWeekNetvalue;
    private HistoryNetValue mMonthNetvalue;
    private HistoryNetValue mAllNetvalue;

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

        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        mCreateCalender = TimeUtils.toCalendar(mCombinationBean.getCreateTime());

    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(ARGUMENT_TREND_TYPE);

    }

    public void setSelectType(String type) {
        this.trendType = type;
        if (null != mMaChart) {

            updateView();
        }
    }

    // tab切换时Ui更新为选中的tabUI
    private void updateView() {
        if (!TextUtils.isEmpty(trendType)) {

            if (isTodayShow()) {

                initTodayTrendTitle();
                if (null == mTodayNetvalue) {
                    dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
                } else {
                    setTodayViewLoad();
                    // computeTodayDataThread.start();
                }

            } else if (trendType.equalsIgnoreCase(TREND_TYPE_HISTORY)) {
                if (null == mAllNetvalue) {
                    mNetValueDataEngine.requeryHistory(historyNetValueListener);
                    historyNetValueListener.setLoadingDialog(getActivity());
                } else {

                    setHistoryViewload(mAllNetvalue);
                }
            } else if (trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {
                if (null == mAllNetvalue) {
                    mNetValueDataEngine.requeryOneMonth(historyNetValueListener);
                    historyNetValueListener.setLoadingDialog(getActivity());
                } else {

                    setHistoryViewload(mMonthNetvalue);
                }
            } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY)) {
                if (null == mAllNetvalue) {
                    mNetValueDataEngine.requerySevenDay(historyNetValueListener);
                    historyNetValueListener.setLoadingDialog(getActivity());
                } else {

                    setHistoryViewload(mWeekNetvalue);
                }
            }

        }

    }

    public void setUpdateHandler(Handler updateHandler) {
        this.updateHandler = updateHandler;
    }

    private void handleExtras(Bundle extras) {

        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
        mNetValueDataEngine = new NetValueEngine(mCombinationBean.getId());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trend_chart, null);
        mMaChart = (TrendChart) view.findViewById(R.id.machart);

        initMaChart(mMaChart);
        initView(view);
        // setupBottomTextViewData();
        updateView();
        return view;
    }

    private void initView(View view) {
        viewDashLineTip = view.findViewById(R.id.tv_dashline_tip);
        tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);
        tvNetValue = (TextView) view.findViewById(R.id.tv_now_netvalue);
        tvUpValue = (TextView) view.findViewById(R.id.tv_updown_value);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_increase_value);
        tvStartText = (TextView) view.findViewById(R.id.tv_netvalue_text);
        tvEndText = (TextView) view.findViewById(R.id.tv_updown_text);
        tvIncreaseText = (TextView) view.findViewById(R.id.tv_increase_text);

    }

    private void setupBottomTextViewData() {

        if (isTodayShow()) {

            tvStartText.setVisibility(View.INVISIBLE);
        } else {
            if (trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {

                tvIncreaseText.setText(R.string.month_income_rate);
            } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY)) {
                tvIncreaseText.setText(R.string.week_income_rate);
            } else {

                tvIncreaseText.setText(R.string.all_income_rate);
            }
            tvStartText.setText(R.string.start_netvalue);
            tvEndText.setText(R.string.end_netvalue);
            tvStartText.setVisibility(View.VISIBLE);
        }

    }

    private boolean isTodayShow() {
        if (TextUtils.isEmpty(trendType)) {
            return false;
        }
        return trendType.equalsIgnoreCase(TREND_TYPE_TODAY);
    }

    private void initMaChart(final TrendChart machart) {

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);

        machart.setDisplayBorder(false);

        machart.setLatitudeColor(Color.LTGRAY);

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
        // machart.setMaxValue(120);
        // machart.setMinValue(0);

        machart.setDisplayAxisXTitle(true);
        machart.setDisplayAxisYTitle(true);
        machart.setDisplayLatitude(true);
        machart.setDisplayLongitude(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            machart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    private void setLineData(List<TrendLinePointEntity> lineDataList) {
        if (isAdded()) {
            List<LineEntity> lines = new ArrayList<LineEntity>();
            LineEntity MA5 = new LineEntity();
            MA5.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
            MA5.setLineData(lineDataList);
            lines.add(MA5);
            mMaChart.setLineData(lines);
        }
    }

    private void initTodayTrendTitle() {

        List<String> xtitle = new ArrayList<String>();
        xtitle.add("9:30");
        xtitle.add("10:30");
        xtitle.add("11:30");
        xtitle.add("14:00");
        xtitle.add("15:00");

        mMaChart.setAxisXTitles(xtitle);
        mMaChart.setMaxPointNum(242);

        List<String> rightYtitle = new ArrayList<String>();
        rightYtitle.add(StringFromatUtils.get2PointPercent(-1f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(-0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(1f));
        mMaChart.setAxisRightYTitles(rightYtitle);
    }

    private void setTipVisible(boolean isShow) {

        if (isShow) {
            viewDashLineTip.setVisibility(View.VISIBLE);
        } else {
            viewDashLineTip.setVisibility(View.GONE);
        }

    }

    ParseHttpListener todayListener = new ParseHttpListener<TodayNetValue>() {

        @Override
        protected TodayNetValue parseDateTask(String jsonData) {
            TodayNetValue todayNetvalue = DataParse.parseObjectJson(TodayNetValue.class, jsonData);
            getMaxOffetValue(todayNetvalue);
            return todayNetvalue;
        }

        @Override
        protected void afterParseData(TodayNetValue todayNetvalue) {

            if (todayNetvalue != null) {

                mTodayNetvalue = todayNetvalue;
                setTodayViewLoad();

            }

        }
    };

    // float maxValue = 0;

    private void setTodayViewLoad() {

        List<TodayNetBean> dayNetValueList = mTodayNetvalue.getChartlist();
        if (dayNetValueList != null && dayNetValueList.size() > 0) {
            setYTitle(mTodayNetvalue.getBegin(), mTodayNetvalue.getMaxOffetValue());
            if (mMaChart.getDashLinePointSize() > 2) {

                setTipVisible(true);

            } else {
                setTipVisible(false);

            }
            setLineData(lineDataList);

            String lasttime = dayNetValueList.get(dayNetValueList.size() - 1).getTimestamp();

            Calendar calender = TimeUtils.toCalendar(lasttime);
            tvTimeLeft.setText(calender.get(Calendar.YEAR) + "-" + (calender.get(Calendar.MONTH) + 1) + "-"
                    + calender.get(Calendar.DAY_OF_MONTH));
            String timeStr = TimeUtils.getTimeString(lasttime);
            tvTimeRight.setText(timeStr);
        }

        tvNetValue.setText(StringFromatUtils.get4Point(mTodayNetvalue.getEnd()));
        if (isTodayShow()) {
            tvNetValue.setVisibility(View.INVISIBLE);
        }

        System.out.println("get current netvalue:" + mTodayNetvalue.getEnd());
        if (null != updateHandler) {
            System.out.println("send get current netvalue:" + mTodayNetvalue.getEnd());
            Message msg = updateHandler.obtainMessage();
            msg.obj = mTodayNetvalue.getEnd();
            updateHandler.sendMessage(msg);
        }
        float addupValue = dayNetValueList.get(dayNetValueList.size() - 1).getChange();
        tvUpValue.setTextColor(ColorTemplate.getUpOrDrownCSL(addupValue));
        tvUpValue.setText(StringFromatUtils.get4Point(addupValue));
        float increase = dayNetValueList.get(dayNetValueList.size() - 1).getPercentage();
        tvIncreaseValue.setTextColor(ColorTemplate.getUpOrDrownCSL(increase));
        tvIncreaseValue.setText(StringFromatUtils.getPercentValue(increase));

    }

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(TodayNetValue todayNetvalue) {
        lineDataList.clear();
        int dashLineSize = 0;
        int i = 0;
        float baseNum = todayNetvalue.getBegin();
        float maxNum = baseNum, minNum = baseNum;
        for (TodayNetBean bean : todayNetvalue.getChartlist()) {
            i++;
            if (bean.getNetvalue() > maxNum) {
                maxNum = bean.getNetvalue();

            } else if (bean.getNetvalue() < minNum) {
                minNum = bean.getNetvalue();
            }

            TrendLinePointEntity pointEntity = new TrendLinePointEntity();
            // HitstroyNetBean todayBean = dayNetValueList.get(i);
            pointEntity.setTime("时间:" + TimeUtils.getTimeString(bean.getTimestamp()));
            pointEntity.setValue(bean.getNetvalue());
            pointEntity.setIncreaseRange((bean.getNetvalue() - baseNum) / baseNum * 100);

            if (dashLineSize == 0 && TimeUtils.toCalendar(bean.getTimestamp()) != null) {
                if (TimeUtils.toCalendar(bean.getTimestamp()).after(mCreateCalender)) {
                    dashLineSize = i;
                }
            }
            lineDataList.add(pointEntity);

        }
        if (dashLineSize == 0) {
            dashLineSize = todayNetvalue.getChartlist().size();
        }

        mMaChart.setDashLinePointSize(dashLineSize);
        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        offetValue = maxNum > minNum ? maxNum : minNum;
        todayNetvalue.setMaxOffetValue(offetValue);
        return offetValue;
    }

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(HistoryNetValue historyNetValue) {

        lineDataList.clear();
        int dashLineSize = 0;
        float baseNum = historyNetValue.getBegin();
        float maxNum = baseNum, minNum = baseNum;
        List<HistoryNetBean> historyNetList = historyNetValue.getChartlist();
        int dataLenght = historyNetList.size();
        for (int i = 0; i < dataLenght; i++) {

            TrendLinePointEntity pointEntity = new TrendLinePointEntity();
            HistoryNetBean todayBean = historyNetList.get(i);
            float value = todayBean.getNetvalue();
            // pointEntity.setDesc(todayBean.getDate());
            pointEntity.setValue(value);
            pointEntity.setTime("日期:" + todayBean.getDate());
            pointEntity.setIncreaseRange((value - baseNum) / baseNum * 100);

            if (dashLineSize == 0 && TimeUtils.simpleDateToCalendar(todayBean.getDate()) != null) {
                if (TimeUtils.simpleDateToCalendar(todayBean.getDate()).after(mCreateCalender)) {
                    dashLineSize = i;
                }
            }

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
        if (dashLineSize == 0) {
            dashLineSize = dataLenght;
        }

        mMaChart.setDashLinePointSize(dashLineSize);
        historyNetValue.setMaxOffetValue(offetValue);
        return offetValue;

    }

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
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

        List<String> rightYtitle = new ArrayList<String>();

        rightYtitle.add(StringFromatUtils.get2PointPercent(-(offetYvalue / baseNum) * 100));
        rightYtitle.add(StringFromatUtils.get2PointPercent(-(halfOffetValue / baseNum) * 100));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        rightYtitle.add(StringFromatUtils.get2PointPercent((halfOffetValue / baseNum) * 100));
        rightYtitle.add(StringFromatUtils.get2PointPercent((offetYvalue / baseNum) * 100));

        mMaChart.setDrawRightYTitle(true);
        mMaChart.setAxisRightYTitles(rightYtitle);
        mMaChart.setDrawTrendChart(true);

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
            getMaxOffetValue(histroyValue);
            return histroyValue;
        }

        @Override
        protected void afterParseData(HistoryNetValue object) {
            if (object != null && isAdded()) {
                if (!TextUtils.isEmpty(trendType)) {
                    if (trendType.equalsIgnoreCase(TREND_TYPE_HISTORY)) {
                        mAllNetvalue = object;
                    } else if (trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {
                        mMonthNetvalue = object;
                    } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY)) {
                        mWeekNetvalue = object;
                    }
                    setHistoryViewload(object);
                }
            }
        }

    };

    private void setHistoryViewload(HistoryNetValue historyNetvalue) {
        try {

            List<HistoryNetBean> dayNetValueList = historyNetvalue.getChartlist();
            if (dayNetValueList != null) {

                int sizeLength = dayNetValueList.size();
                setYTitle(historyNetvalue.getBegin(), historyNetvalue.getMaxOffetValue());
                if (mMaChart.getDashLinePointSize() > 2) {

                    setTipVisible(true);

                } else {
                    setTipVisible(false);

                }
                // setHistoryPointTitle();
                setLineData(lineDataList);
                String strLeft = getString(R.string.time_start, dayNetValueList.get(0).getDate());
                String strRight = getString(R.string.time_end, dayNetValueList.get(sizeLength - 1).getDate());
                tvTimeLeft.setText(strLeft);

                tvTimeRight.setText(strRight);

                setXTitle(dayNetValueList);

            }
            tvNetValue.setTextColor(ColorTemplate.getTextColor(R.color.gray_textcolor));
            tvNetValue.setText(StringFromatUtils.get4Point(historyNetvalue.getBegin()));
            float addupValue = (historyNetvalue.getEnd() - historyNetvalue.getBegin()) / historyNetvalue.getBegin()
                    * 100;
            tvUpValue.setText(StringFromatUtils.get4Point(historyNetvalue.getEnd()));
            // fl
            tvIncreaseValue.setText(StringFromatUtils.get2PointPercent(addupValue));
            tvUpValue.setTextColor(ColorTemplate.getTextColor(R.color.gray_textcolor));
            tvIncreaseValue.setTextColor(ColorTemplate.getUpOrDrownCSL(addupValue));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

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

    @Override
    public void onPause() {

        super.onPause();

        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
    }

    public void onStart() {
        super.onStart();
        // if (trendType.equals(TREND_TYPE_TODAY)) {
        // dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
        // }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            dataHandler.sendEmptyMessage(1722);
            if (null != mNetValueDataEngine) {
                mNetValueDataEngine.requeryToday(todayListener);
                todayListener.setLoadingDialog(getActivity()).beforeRequest();
            }
            dataHandler.postDelayed(this, 60 * 1000);// 隔60s再执行一次
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isAdded()) {
            super.setUserVisibleHint(isVisibleToUser);
            if (trendType.equals(TREND_TYPE_TODAY)) {
                System.out.println("setUserVisibleHint:" + isVisibleToUser);

                // if (isVisibleToUser) {
                // // 相当于Fragment的onResume{
                // dataHandler.postDelayed(runnable, 60);// 打开定时器，60ms后执行runnable操作
                // } else {
                // System.out.println("dataHandler.removeCallbacks");
                // dataHandler.removeCallbacks(runnable);// 关闭定时器处理
                // // dataHandler.removeCallbacks(runnable);// 关闭定时器处理
                // // 相当于Fragment的onPause
                // }
            }
        }
    }

}
