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

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.OrderFundDetailActivity;
import com.dkhs.portfolio.ui.widget.InterceptScrollView;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.ui.widget.TrendLinePointEntity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName TrendChartFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version 1.0
 */
public class TrendMonthChartFragment extends BaseFragment {
    public static final String ARGUMENT_TREND_TYPE = "trend_type";
    public static final String TREND_TYPE_MONTH = "trend_month";

    private String trendType = TREND_TYPE_MONTH;
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

    private Calendar mCreateCalender;

    private DrawLineDataEntity monthNetvalue;
    private RelativeLayout pb;

    public static TrendMonthChartFragment newInstance(String trendType) {
        TrendMonthChartFragment fragment = new TrendMonthChartFragment();

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

    private void handleExtras(Bundle extras) {

        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
        mNetValueDataEngine = new NetValueEngine(mCombinationBean.getId());

    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_trend_chart, null);
            mMaChart = (TrendChart) rootView.findViewById(R.id.machart);
            pb = (RelativeLayout) rootView.findViewById(android.R.id.progress);
            pb.setVisibility(View.VISIBLE);
            if (getActivity().getClass().getName().equals("com.dkhs.portfolio.ui.OrderFundDetailActivity")) {
                InterceptScrollView mScrollview = ((OrderFundDetailActivity) getActivity()).getScroll();
                mMaChart.setScroll(mScrollview);
            }
            initMaChart(mMaChart);
            // setupBottomTextViewData();
            initView(rootView);
            // PromptManager.showProgressDialog(getActivity(), "");
            mNetValueDataEngine.requeryOneMonth(monthListener);

        }
        // 缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private void initView(View view) {
        viewDashLineTip = view.findViewById(R.id.tv_dashline_tip);
        tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);
        tvNetValue = (TextView) view.findViewById(R.id.tv_now_netvalue);
        tvUpValue = (TextView) view.findViewById(R.id.tv_updown_value);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_increase_value);
    }

    private boolean isTodayShow() {
        if (TextUtils.isEmpty(trendType)) {
            return false;
        }
        return trendType.equalsIgnoreCase(TREND_TYPE_MONTH);
    }

    private void initMaChart(final TrendChart machart) {
        machart.setBoldLine();

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);

        machart.setDisplayBorder(false);

        machart.setLatitudeColor(Color.LTGRAY);

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);
        machart.setBorderColor(Color.TRANSPARENT);
        machart.setBackgroudColor(Color.WHITE);
        machart.setAxisMarginTop(10);
        machart.setAxisMarginLeft(10);
        machart.setAxisMarginRight(10);

        machart.setLongtitudeFontSize(10);
        machart.setLongtitudeFontColor(Color.GRAY);
        machart.setDisplayAxisYTitleColor(true);
        machart.setLatitudeColor(Color.GRAY);
        machart.setLatitudeFontColor(Color.GRAY);
        machart.setLongitudeColor(Color.GRAY);

        machart.setDisplayAxisXTitle(true);
        machart.setDisplayAxisYTitle(true);
        machart.setDisplayLatitude(true);
        machart.setDisplayLongitude(true);

        setInitYTitle();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            machart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

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

    ParseHttpListener monthListener = new ParseHttpListener<DrawLineDataEntity>() {

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
                monthNetvalue = todayNetvalue;
                setMonthViewLoad();
            }
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

    private void setMonthViewLoad() {
        try {
            // = historyNetvalue.da();
            if (monthNetvalue.dataList != null) {
                mMaChart.setDashLinePointSize(monthNetvalue.dashLineSize);
                if (mMaChart.getDashLinePointSize() > 2) {
                    setTipVisible(true);
                    setYTitle(monthNetvalue.begin, monthNetvalue.maxOffetvalue);
                } else {
                    setTipVisible(false);
                    setYTitle(monthNetvalue.begin, monthNetvalue.maxOffetvalue);
                }
                setLineData(monthNetvalue.dataList);
                if (strLeft == null) {
                    strLeft = getString(R.string.time_start);
                }
                if (strRight == null) {
                    strRight = getString(R.string.time_end);
                }
                strRight = getString(R.string.time_end, monthNetvalue.endDay);
                tvTimeLeft.setText(String.format(strLeft, monthNetvalue.startDay));
                tvTimeRight.setText(String.format(strRight, monthNetvalue.endDay));

                setXTitle(monthNetvalue);
            }
            tvNetValue.setVisibility(View.VISIBLE);
            tvNetValue.setTextColor(ColorTemplate.getTextColor(R.color.gray_textcolor));
            tvNetValue.setText(StringFromatUtils.get4Point(monthNetvalue.begin));
            float addupValue = (monthNetvalue.end - monthNetvalue.begin) / monthNetvalue.begin * 100;
            tvUpValue.setText(StringFromatUtils.get4Point(monthNetvalue.end));
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
     * /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {

        System.out.println("setYTitle baseNum:" + baseNum);
        System.out.println("setYTitle offetYvalue:" + offetYvalue);

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

    Handler dataHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        };
    };

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

            dataLenght = historyNetList.size();
            for (int i = 0; i < dataLenght; i++) {
                TrendLinePointEntity pointEntity = new TrendLinePointEntity();
                HistoryNetBean todayBean = historyNetList.get(i);
                float value = todayBean.getNetvalue();
                pointEntity.setValue(value);
                pointEntity.setTime("日期: " + todayBean.getDate());
                pointEntity.setIncreaseRange(todayBean.getPercentageBegin());
                if (dashLineSize == 0 && TimeUtils.simpleDateToCalendar(todayBean.getDate()) != null) {
                    if (TimeUtils.simpleDateToCalendar(todayBean.getDate()).after(mCreateCalender)) {
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
        // lineData.begin = historyNetValue.getBegin();
        lineData.begin = historyNetValue.getBegin();
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // dataHandler.sendEmptyMessage(1722);
            if (null != mNetValueDataEngine)
                mNetValueDataEngine.requeryOneMonth(monthListener);
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

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_trend_month);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public int setContentLayoutId() {
        // TODO Auto-generated method stub
        return 0;
    }
}
