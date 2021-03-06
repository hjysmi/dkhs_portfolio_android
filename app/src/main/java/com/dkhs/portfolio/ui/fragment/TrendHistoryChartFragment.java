/**
 * @Title TrendChartFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
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

import org.joda.time.DateTime;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TrendChartFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-3 上午10:32:39
 */
public class TrendHistoryChartFragment extends VisiableLoadFragment {
    public static final String ARGUMENT_TREND_TYPE = "trend_type";
    public static final String TREND_TYPE_HISTORY = "trend_history";

    private String trendType = TREND_TYPE_HISTORY;
    // private boolean isTodayNetValue;
    private TextView tvTimeLeft;
    private TextView tvTimeRight;
    private TextView tvNetValue;
    private TextView tvUpValue;
    private TextView tvIncreaseValue;

    private View viewDashLineTip;

    private TrendChart mMaChart;

    private NetValueEngine mNetValueDataEngine;
    private CombinationBean mCombinationBean;

    private WeakHandler updateHandler;
    private DateTime mCreateDate;

    private DrawLineDataEntity historyNetvalue;
    private RelativeLayout pb;

    public static TrendHistoryChartFragment newInstance(String trendType) {
        TrendHistoryChartFragment fragment = new TrendHistoryChartFragment();

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

        mCreateDate = new DateTime(mCombinationBean.getCreateTime());

    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(ARGUMENT_TREND_TYPE);

    }

    private void handleExtras(Bundle extras) {

        mCombinationBean = Parcels.unwrap(extras.getParcelable(CombinationDetailActivity.EXTRA_COMBINATION));
        mNetValueDataEngine = new NetValueEngine(mCombinationBean.getId());

    }


    @Override
    public void requestData() {
        mNetValueDataEngine.requeryHistory(historyListener);
    }

//    private View rootView;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(, null);
//            mMaChart = (TrendChart) rootView.findViewById(R.id.machart);
//            pb = (RelativeLayout) rootView.findViewById(android.R.id.progress);
//            pb.setVisibility(View.VISIBLE);
//            initMaChart(mMaChart);
//            // setupBottomTextViewData();
//            initView(rootView);
//            // PromptManager.showProgressDialog(getActivity(), "");
//            mNetValueDataEngine.requeryHistory(historyListener);
//
//        }
//        // 缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//        return rootView;
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMaChart = (TrendChart) view.findViewById(R.id.machart);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        pb.setVisibility(View.VISIBLE);
        initMaChart(mMaChart);
        // setupBottomTextViewData();
        initView(view);
    }

    private void initView(View view) {
        viewDashLineTip = view.findViewById(R.id.tv_dashline_tip);
        tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);
        tvNetValue = (TextView) view.findViewById(R.id.tv_now_netvalue);
        tvUpValue = (TextView) view.findViewById(R.id.tv_updown_value);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_increase_value);
    }

    private void initMaChart(final TrendChart machart) {
        machart.setBoldLine();
        setInitYTitle();

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
        mMaChart.setDisplayAxisYTitleColor(false);
        mMaChart.setDisplayYRightTitleByZero(true);
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
    }

    private void setTipVisible(boolean isShow) {

        if (isShow) {
            viewDashLineTip.setVisibility(View.VISIBLE);
        } else {
            viewDashLineTip.setVisibility(View.GONE);
        }

    }

    ParseHttpListener historyListener = new ParseHttpListener<DrawLineDataEntity>() {

        @Override
        protected DrawLineDataEntity parseDateTask(String jsonData) {
            HistoryNetValue histroyValue = DataParse.parseObjectJson(HistoryNetValue.class, jsonData);
            if (!UIUtils.roundAble(histroyValue.getTrade_status())) {
                stopRequry();
            }
            DrawLineDataEntity lineData = null;
            if (null != histroyValue && histroyValue.getChartlist() != null) {
                lineData = new DrawLineDataEntity();
                getMaxOffetValue(lineData, histroyValue);
            }
            return lineData;
        }

        @Override
        protected void afterParseData(DrawLineDataEntity todayNetvalue) {
            pb.setVisibility(View.GONE);
            if (todayNetvalue != null) {
                historyNetvalue = todayNetvalue;
                setHistoryViewload();
            }

        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            if (null != pb)
                pb.setVisibility(View.GONE);
        }

    };

    public class DrawLineDataEntity {
        List<TrendLinePointEntity> dataList = new ArrayList<TrendLinePointEntity>();
        int dashLineSize;
        float begin;
        float end;
        String startDay = "";
        String endDay = "";
        float maxOffetvalue;
        float addupvalue;
        float netvalue;

    }

    private void setHistoryViewload() {
        try {
            // = historyNetvalue.da();
            if (historyNetvalue.dataList != null) {
                mMaChart.setDashLinePointSize(historyNetvalue.dashLineSize);
                if (mMaChart.getDashLinePointSize() > 2) {
                    setTipVisible(true);
                    setYTitle(historyNetvalue.begin, historyNetvalue.maxOffetvalue);
                } else {
                    setTipVisible(false);
                    setYTitle(historyNetvalue.begin, historyNetvalue.maxOffetvalue);
                }
                setLineData(historyNetvalue.dataList);
                if (strLeft == null) {
                    strLeft = getString(R.string.time_start);
                }
                if (strRight == null) {
                    strRight = getString(R.string.time_end);
                }
                strRight = getString(R.string.time_end, historyNetvalue.endDay);
                tvTimeLeft.setText(String.format(strLeft, historyNetvalue.startDay));
                tvTimeRight.setText(String.format(strRight, historyNetvalue.endDay));

                setXTitle(historyNetvalue);
            }
            tvNetValue.setVisibility(View.VISIBLE);
            tvNetValue.setTextColor(ColorTemplate.getTextColor(R.color.gray_textcolor));
            tvNetValue.setText(StringFromatUtils.get4Point(historyNetvalue.begin));
            float addupValue = (historyNetvalue.end - historyNetvalue.begin) / historyNetvalue.begin * 100;
            tvUpValue.setText(StringFromatUtils.get4Point(historyNetvalue.end));
            // fl
            tvIncreaseValue.setText(StringFromatUtils.get2PointPercent(addupValue));
            tvUpValue.setTextColor(ColorTemplate.getTextColor(R.color.gray_textcolor));
            tvIncreaseValue.setTextColor(ColorTemplate.getUpOrDrownCSL(addupValue));
            // PromptManager.closeProgressDialog();
        } catch (Exception e) {
            // TODO: handle exception
        }
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

    @SuppressLint("HandlerLeak")
    WeakHandler dataHandler = new WeakHandler();

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private float getMaxOffetValue(DrawLineDataEntity lineData, HistoryNetValue historyNetValue) {
        List<HistoryNetBean> historyNetList = historyNetValue.getChartlist();

        lineData.dataList.clear();
        lineData.end = historyNetValue.getEnd();
        if (null != historyNetList && historyNetList.size() > 0) {
            lineData.startDay = historyNetList.get(0).getDate();
            lineData.endDay = historyNetList.get(historyNetList.size() - 1).getDate();
        }
        int dashLineSize = 0;
        float baseNum = historyNetValue.getBegin();
        float maxNum = baseNum, minNum = baseNum;
        int dataLenght = 0;
        if (null != historyNetList) {
            DateTime beanDate;
            dataLenght = historyNetList.size();
            for (int i = 0; i < dataLenght; i++) {
                TrendLinePointEntity pointEntity = new TrendLinePointEntity();
                HistoryNetBean todayBean = historyNetList.get(i);
                float value = todayBean.getNetvalue();
                pointEntity.setValue(value);
                pointEntity.setTime("日期: " + todayBean.getDate());
                pointEntity.setIncreaseRange(todayBean.getPercentageBegin());
                beanDate = new DateTime(todayBean.getDate());
                if (dashLineSize == 0 && beanDate != null) {
                    if (TimeUtils.compareDateTime(beanDate, mCreateDate) > 0) {
                        dashLineSize = i;
                    }
                }
                lineData.dataList.add(pointEntity);
                if (value > maxNum) {
                    maxNum = value;
                } else if (value < minNum) {
                    minNum = value;
                }
            }
        }
        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;
        offetValue = maxNum > minNum ? maxNum : minNum;
        if (dashLineSize == 0) {
            dashLineSize = dataLenght;
        }
        // if (dashLineSize > 1) {
        // lineData.begin = 1;
        // } else {
        lineData.begin = historyNetValue.getBegin();
        // lineData.begin = historyNetValue.getLast_netvalue();
        // }
        lineData.dashLineSize = dashLineSize;
        lineData.maxOffetvalue = offetValue;
        return offetValue;

    }

    private void setXTitle(DrawLineDataEntity historyNetvalue) {
        List<String> xtitle = new ArrayList<String>();
        String endDate = historyNetvalue.startDay;
        if (TextUtils.isEmpty(endDate)) {
            xtitle.add("");
        } else {
            xtitle.add(endDate);

        }
        xtitle.add(historyNetvalue.endDay);
        mMaChart.setMaxPointNum(historyNetvalue.dataList.size());
        mMaChart.setAxisXTitles(xtitle);

    }

    public void startRequry() {
        dataHandler.postDelayed(runnable, 60 * 1000);// 隔60s再执行一次
    }

    public void stopRequry() {
        dataHandler.removeCallbacks(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
//        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // dataHandler.sendEmptyMessage(1722);
            if (null != mNetValueDataEngine) {
                mNetValueDataEngine.requeryHistory(historyListener);
            }
            dataHandler.postDelayed(this, 60 * 1000);// 隔60s再执行一次
        }
    };
    private String strLeft;
    private String strRight;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isAdded()) {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_trend_history);

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
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
}
