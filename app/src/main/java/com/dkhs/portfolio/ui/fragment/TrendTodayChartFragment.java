/**
 * @Title TrendChartFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.TodayNetBean;
import com.dkhs.portfolio.bean.TodayNetValue;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePoint.TrendLinePointEntity;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.http.HttpHandler;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TrendChartFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-3 上午10:32:39
 */
public class TrendTodayChartFragment extends VisiableLoadFragment {
    public static final String ARGUMENT_TREND_TYPE = "trend_type";
    public static final String TREND_TYPE_TODAY = "trend_today";
    public static final int MSG_UPDATE_VIEW = 500;

    private String trendType = TREND_TYPE_TODAY;
    // private boolean isTodayNetValue;

    private View viewDashLineTip;

    private TrendChart mMaChart;

    private NetValueEngine mNetValueDataEngine;
    private CombinationBean mCombinationBean;

    private Calendar mCreateCalender;
    private DrawLineDataEntity mTodayLineData;

    private RelativeLayout pb;

    public static TrendTodayChartFragment newInstance(String trendType) {
        TrendTodayChartFragment fragment = new TrendTodayChartFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TREND_TYPE, trendType);
        fragment.setArguments(arguments);

        return fragment;
    }

    private WeakHandler updateHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_VIEW) {
                pb.setVisibility(View.GONE);
                setTodayViewLoad();
            }
            return false;
        }
    });

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

//    public void setUpdateHandler(WeakHandler updateHandler) {
//        this.updateHandler = updateHandler;
//    }

    private void handleExtras(Bundle extras) {

        mCombinationBean = Parcels.unwrap(extras.getParcelable(CombinationDetailActivity.EXTRA_COMBINATION));
        mNetValueDataEngine = new NetValueEngine(mCombinationBean.getId());

    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMaChart = (TrendChart) view.findViewById(R.id.machart);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        pb.setVisibility(View.VISIBLE);
        initMaChart(mMaChart);
        // setupBottomTextViewData();
        initView(view);
        initTodayTrendTitle();
    }

    private void initView(View view) {
        viewDashLineTip = view.findViewById(R.id.tv_dashline_tip);
    }

    public void startRequry() {
        if (null != mNetValueDataEngine) {
            cancelRequest();
            mHttpHandler = mNetValueDataEngine.requeryToday(todayListener);
        }

    }

    public void stopRequry() {
        dataHandler.removeCallbacks(runnable);
    }

    private void initMaChart(final TrendChart machart) {
        machart.setBoldLine();
        setInitYTitle();
    }

    private List<LineEntity> lines;
    private LineEntity MA5;

    private void setLineData(List<TrendLinePointEntity> lineDataList) {
        if (isAdded()) {
            if (lines == null) {
                lines = new ArrayList<LineEntity>();
            } else {
                lines.clear();
            }
            if (MA5 == null) {
                MA5 = new LineEntity();
            }
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
        mMaChart.setDisplayAxisYTitleColor(false);
        mMaChart.setDisplayYRightTitleByZero(true);
    }

    private void setTipVisible(boolean isShow) {

        if (isShow) {
            viewDashLineTip.setVisibility(View.VISIBLE);
        } else {
            viewDashLineTip.setVisibility(View.GONE);
        }

    }


    ParseHttpListener todayListener = new ParseHttpListener<DrawLineDataEntity>() {

        @Override
        protected DrawLineDataEntity parseDateTask(String jsonData) {

            Log.e(TAG, "===============> ParseHttpListener parseDateTask");

            TodayNetValue todayNetvalue = DataParse.parseObjectJson(TodayNetValue.class, jsonData);
            if (!UIUtils.roundAble(todayNetvalue.getTrade_status())) {
                stopRequry();
            }
            DrawLineDataEntity todayLine = null;
            // todayNetvalue.setChartlist(new ArrayList<NetValueEngine.TodayNetBean>());
            if (null != todayNetvalue && todayNetvalue.getChartlist() != null) {
                todayLine = new DrawLineDataEntity();
                getMaxOffetValue(todayLine, todayNetvalue);
            }

            return todayLine;
        }

        @Override
        protected void afterParseData(DrawLineDataEntity todayNetvalue) {
            updateHandler.sendEmptyMessage(MSG_UPDATE_VIEW);

            if (todayNetvalue != null) {

                mTodayLineData = todayNetvalue;
                // PromptManager.closeProgressDialog();
            }

        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            pb.setVisibility(View.GONE);
        }

    };


    public class DrawLineDataEntity {
        List<TrendLinePointEntity> dataList = new ArrayList<TrendLinePointEntity>();
        int dashLineSize;
        float begin;
        float end;
        String startDay;
        String endDay;
        float maxOffetvalue;
        float addupvalue;
        float netvalue;

    }

    private void setTodayViewLoad() {
        if (mTodayLineData.dataList != null) {
            setYTitle(mTodayLineData.begin, mTodayLineData.maxOffetvalue);
            mMaChart.setDashLinePointSize(mTodayLineData.dashLineSize);
            if (mMaChart.getDashLinePointSize() > 2) {
                setTipVisible(true);
            } else {
                setTipVisible(false);
            }
            setLineData(mTodayLineData.dataList);

            // String lasttime = mTodayLineData.endDay;
            // Calendar calender = TimeUtils.toCalendar(lasttime);
            // tvTimeLeft.setText(calender.get(Calendar.YEAR) + "-" + (calender.get(Calendar.MONTH) + 1) + "-"
            // + calender.get(Calendar.DAY_OF_MONTH));
            // String timeStr = TimeUtils.getTimeString(lasttime);
            // tvTimeRight.setText(timeStr);
        }
        if (null != updateHandler) {
//            Message msg = updateHandler.obtainMessage();
            Message msg = new Message();
            msg.obj = mTodayLineData.end;
            updateHandler.sendMessage(msg);
        }
    }

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(DrawLineDataEntity lineData, TodayNetValue todayNetvalue) {
        List<TodayNetBean> dayNetValueList = todayNetvalue.getChartlist();
        // List<TodayNetBean> dayNetValueList = new ArrayList<NetValueEngine.TodayNetBean>();

        lineData.dataList.clear();
        lineData.end = todayNetvalue.getEnd();
        if (dayNetValueList.size() > 0) {

            lineData.endDay = dayNetValueList.get(dayNetValueList.size() - 1).getTimestamp();
            lineData.addupvalue = dayNetValueList.get(dayNetValueList.size() - 1).getChange();
            lineData.netvalue = dayNetValueList.get(dayNetValueList.size() - 1).getPercentage();
        }
        int dashLineSize = 0;
        int i = 0;
        float baseNum = todayNetvalue.getLast_netvalue();
        float maxNum = baseNum, minNum = baseNum;
        for (TodayNetBean bean : todayNetvalue.getChartlist()) {
            i++;
            if (bean.getNetvalue() > maxNum) {
                maxNum = bean.getNetvalue();

            } else if (bean.getNetvalue() < minNum) {
                minNum = bean.getNetvalue();
            }
            TrendLinePointEntity pointEntity = new TrendLinePointEntity();
            pointEntity.setTime("时间: " + TimeUtils.getTimeString(bean.getTimestamp()));
            pointEntity.setValue(bean.getNetvalue());
            pointEntity.setIncreaseRange(((bean.getNetvalue() - baseNum) / baseNum) * 100);

            if (dashLineSize == 0 && TimeUtils.toCalendar(bean.getTimestamp()) != null) {
                if (TimeUtils.toCalendar(bean.getTimestamp()).after(mCreateCalender)) {
                    dashLineSize = i;
                }
            }
            lineData.dataList.add(pointEntity);
        }
        if (dashLineSize == 0) {
            dashLineSize = todayNetvalue.getChartlist().size();
        }
        // if (dashLineSize > 1) {
        // lineData.begin = 1;
        // } else {
        // lineData.begin = todayNetvalue.getBegin();
        lineData.begin = todayNetvalue.getLast_netvalue();
        // }
        lineData.dashLineSize = dashLineSize;
        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;
        offetValue = maxNum > minNum ? maxNum : minNum;
        lineData.maxOffetvalue = offetValue;
        return offetValue;
    }

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
        // 增加20的空白区域
        if (offetYvalue != 0) {
            offetYvalue = offetYvalue / 0.8f;
        } else {
            offetYvalue = baseNum * 0.01f;
        }
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

    private void setInitYTitle() {
        List<String> ytitle = new ArrayList<String>();
        ytitle.add(StringFromatUtils.get4Point(0.99f));
        ytitle.add(StringFromatUtils.get4Point(0.995f));
        ytitle.add(StringFromatUtils.get4Point(1.0f));
        ytitle.add(StringFromatUtils.get4Point(1.005f));
        ytitle.add(StringFromatUtils.get4Point(1.01f));
        mMaChart.setAxisYTitles(ytitle);

        List<String> rightYtitle = new ArrayList<String>();

        rightYtitle.add(StringFromatUtils.get2PointPercent(-1f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(-0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(0.5f));
        rightYtitle.add(StringFromatUtils.get2PointPercent(1.0f));

        mMaChart.setDrawRightYTitle(true);
        mMaChart.setAxisRightYTitles(rightYtitle);
    }

    WeakHandler dataHandler = new WeakHandler() {
    };
    private HttpHandler mHttpHandler;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // dataHandler.sendEmptyMessage(1722);
            startRequry();
            dataHandler.postDelayed(this, 60 * 1000);// 隔60s再执行一次
        }
    };

    @Override
    public void requestData() {
        startRequry();
    }

    private void cancelRequest() {
        if (null != mHttpHandler) {
            mHttpHandler.cancel();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isAdded()) {
            super.setUserVisibleHint(isVisibleToUser);
            if (trendType.equals(TREND_TYPE_TODAY)) {
            }
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_trend_today);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        stopRequry();
    }

    private static final String TAG = TrendTodayChartFragment.class.getSimpleName();

    @Override
    public void onViewHide() {
        super.onViewHide();
        stopRequry();
        cancelRequest();
        Log.e(TAG, "===============> onViewHide");
    }


    @Override
    public void onViewShow() {
        super.onViewShow();
        dataHandler.postDelayed(runnable, 60 * 1000);// 隔60s再执行一次
        Log.e(TAG, "===============> startRequry");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public int setContentLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_trend_chart;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRequry();
    }
}
