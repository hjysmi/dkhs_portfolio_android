/**
 * @Title CompareFundFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-5-5 下午4:18:02
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CompareFundsBean;
import com.dkhs.portfolio.bean.CompareFundsBean.ComparePoint;
import com.dkhs.portfolio.bean.FundManagerInfoBean;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.ManagersEntity;
import com.dkhs.portfolio.bean.SepFundChartBean;
import com.dkhs.portfolio.engine.CompareEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.widget.LinePoint.DefFundPointEntity;
import com.dkhs.portfolio.ui.widget.LinePoint.SepFundPointEntity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName BenefitChartView
 * @Description TODO(收益曲线视图)
 * @date 2015-6-10 下午4:18:02
 */
public class BenefitChartView {

    // 默认沪深300的id
    private String mCompareIds = "106000232";
    @ViewInject(R.id.tv_combination_name)
    private TextView tvCombinationName;
    private CompareEngine mCompareEngine;
    Calendar cStart, cEnd;
    @ViewInject(R.id.machart)
    private TrendChart maChartView;

    @ViewInject(R.id.tv_more_funds)
    private View moreFundView;
    @ViewInject(R.id.rl_compare_title)
    private View titleView;
    @ViewInject(R.id.loadView)
    private View loadView;
    @ViewInject(R.id.contentView)
    private View contentView;
    private Context ctx;

    private String fundId;
    private int symbol_stype;
    private String abbrName;

    private List<LineEntity> lineEntityList = new ArrayList<LineEntity>();

    private View benifitView;

    private FundQuoteBean mFundQuoteBean;

    public BenefitChartView(Context ctx) {
        this.ctx = ctx;
        mCompareEngine = new CompareEngine();
        benifitView = initView();
    }

    public View getBenifitView() {
        return benifitView;
    }

    public View initView() {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_compare_index_view, null);
        ViewUtils.inject(this, view); // 注入view和事件
        moreFundView.setVisibility(View.GONE);
        return view;
    }

    public void draw(FundManagerInfoBean.AchivementsEntity achivementsEntity) {
        loadView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        if (null != achivementsEntity.getEnd_date()) {
            cEnd = TimeUtils.simpleDateToCalendar(achivementsEntity.getEnd_date());
        } else {
            cEnd = Calendar.getInstance();
        }
        cStart = TimeUtils.simpleDateToCalendar(achivementsEntity.getStart_date());
        symbol_stype = achivementsEntity.getFund().getSymbol_stype();
        fundId = achivementsEntity.getFund().getId() + "";
        abbrName = achivementsEntity.getFund().getAbbr_name();
        onRequest();
    }


    // key :period value:如：m:月; 3m:三个月; 6m:半年; "y":最近一年; "ty":今年以来;

    private FundTrendType trendType = FundTrendType.Default;

    public enum FundTrendType {

        Default("default"),

        Month("m"),

        Season("3m"),

        HalfYear("6m"),

        OneYear("y"),

        OfficeDay("office"),

        ToYear("ty");

        private String value;

        // 枚举对象构造函数
        private FundTrendType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public void draw(FundQuoteBean fundQuoteBean, FundTrendType type) {
        this.trendType = type;
        tvCombinationName.setVisibility(View.GONE);
        titleView.setVisibility(View.GONE);
        this.mFundQuoteBean = fundQuoteBean;
        draw(fundQuoteBean);
    }


    public void draw(FundQuoteBean fundQuoteBean) {
        loadView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);

        cEnd = Calendar.getInstance();

        // FIXME: 2015/6/10 多个基金经理的时候 开始时间怎么算
        cStart = TimeUtils.simpleDateToCalendar(fundQuoteBean.getManagers().get(0).getStart_date());

        this.symbol_stype = fundQuoteBean.getSymbol_stype();
        fundId = fundQuoteBean.getId() + "";
        abbrName = fundQuoteBean.getName();
        onRequest();


    }

    private void onRequest() {
        initMaChart(maChartView);
        if (StockUitls.isSepFund(symbol_stype)) {
            tvCombinationName.setVisibility(View.GONE);
            requestSepFund();
        } else {
            tvCombinationName.setText(abbrName);
            requestCompare();
        }
    }


    private void requestSepFund() {
        lineEntityList.clear();
        maxOffsetValue = 0f;
        if (trendType == FundTrendType.Default) {
            mCompareEngine.compare(sepFundHttpListener, fundId, TimeUtils.getTimeString(cStart),
                    TimeUtils.getTimeString(cEnd));
        } else if (trendType == FundTrendType.OfficeDay) {
            mCompareEngine.compare(sepFundHttpListener, fundId, TimeUtils.getTimeString(cStart),
                    TimeUtils.getTimeString(cEnd));
        } else {
            mCompareEngine.compareByPeriod(sepFundHttpListener, fundId, trendType.getValue());

        }
    }

    private void requestCompare() {

        lineEntityList.clear();
        maxOffsetValue = 0f;
        if (trendType == FundTrendType.Default) {
            mCompareEngine.compare(compareListener, (fundId + "," + mCompareIds), TimeUtils.getTimeString(cStart),
                    TimeUtils.getTimeString(cEnd));
        } else if (trendType == FundTrendType.OfficeDay) {
            mCompareEngine.compare(compareListener, (fundId + "," + mCompareIds), TimeUtils.getTimeString(cStart),
                    TimeUtils.getTimeString(cEnd));
        } else {
            mCompareEngine.compareByPeriod(compareListener, (fundId + "," + mCompareIds), trendType.getValue());

        }
    }

    private void initMaChart(TrendChart machart) {
        machart.setMaxValue(120);
        machart.setMinValue(0);
        maChartView.setYlineCounts(2);
        maChartView.setFromCompare(true);
    }

    List<LineEntity> compareLinesList = new ArrayList<LineEntity>();

    private void setCompareLineList() {
        maChartView.refreshClear();
        lineEntityList.addAll(compareLinesList);

        float base = (maxOffsetValue + minOffsetValue) / 2;
        float off = Math.max(maxOffsetValue - base, base - minOffsetValue);
//        float baseNetValue = (maxOffsetNetValue + minOffsetNetValue) / 2;
//        float offNetValue = Math.max(maxOffsetNetValue - baseNetValue, baseNetValue - minOffsetNetValue);
        setYTitle(base, off, baseNetValue);
        maChartView.setLineData(lineEntityList);
        maChartView.setIsFundTrendCompare(true);

//        if (mFundQuoteBean.getSymbol_stype() == 300)//股票型基金
//        {
        maChartView.setDisplayAxisYTitleColor(false);
        maChartView.setDisplayYRightTitleByZero(true);
//        }

        onFinishUpdateUI();
    }

    private void onFinishUpdateUI() {
        contentView.setVisibility(View.VISIBLE);
        loadView.setVisibility(View.GONE);
    }

    private void setSepFundLineList() {
        maChartView.refreshClear();
        lineEntityList.addAll(compareLinesList);
        float base1 = (maxOffsetValue + minOffsetValue) / 2;
        float off1 = Math.max(maxOffsetValue - base1, base1 - minOffsetValue);
        setYTitle(base1, off1);
        maChartView.setLineData(lineEntityList);
        maChartView.setDisplayAxisYTitleColor(false);
        onFinishUpdateUI();
    }


    private void setXTitleByComparePoint(List<ComparePoint> dayNetValueList) {
        List<String> xtitle = new ArrayList<String>();
        String startDay = dayNetValueList.get(0).getDate();
        if (TextUtils.isEmpty(startDay)) {
            xtitle.add("");
        } else {
            xtitle.add(startDay);

        }
        xtitle.add(dayNetValueList.get(dayNetValueList.size() - 1).getDate());
        maChartView.setMaxPointNum(dayNetValueList.size());
        maChartView.setAxisXTitles(xtitle);

    }

    private void setXTitleBySepFundChartBean(List<SepFundChartBean> sepFundChartBeans) {
        List<String> xtitle = new ArrayList<String>();
        String startDay = sepFundChartBeans.get(0).getDate();
        if (TextUtils.isEmpty(startDay)) {
            xtitle.add("");
        } else {
            xtitle.add(startDay);

        }
        xtitle.add(sepFundChartBeans.get(sepFundChartBeans.size() - 1).getDate());
        maChartView.setMaxPointNum(sepFundChartBeans.size());
        maChartView.setAxisXTitles(xtitle);
    }


    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float basePercentvalue, float maxOffPercentValue, float baseNetValue) {
        // int baseNum = 1;

        List<String> yValueTitles = new ArrayList<String>();
        List<String> yPercenttitle = new ArrayList<String>();

        maxOffPercentValue = maxOffPercentValue / 0.8f;
        float halfOffetValue = maxOffPercentValue / 2.0f;
        yPercenttitle.add(StringFromatUtils.get2PointPercent(basePercentvalue - maxOffPercentValue));
        yPercenttitle.add(StringFromatUtils.get2PointPercent(basePercentvalue - halfOffetValue));
        yPercenttitle.add(StringFromatUtils.get2PointPercent(basePercentvalue));
        yPercenttitle.add(StringFromatUtils.get2PointPercent(basePercentvalue + halfOffetValue));
        yPercenttitle.add(StringFromatUtils.get2PointPercent(basePercentvalue + maxOffPercentValue));


        float netValuePercent = 0;

        for (int i = 0; i < 5; i++) {
            netValuePercent = (1 + (basePercentvalue + (-2 + i) * halfOffetValue) / 100);

            yValueTitles.add(StringFromatUtils.get4Point(baseNetValue * netValuePercent));
        }


        maChartView.setAxisYTitles(yValueTitles);
        maChartView.setDrawRightYTitle(true);
        maChartView.setAxisRightYTitles(yPercenttitle);


        maChartView.setMaxValue(basePercentvalue + maxOffPercentValue);
        maChartView.setMinValue(basePercentvalue - maxOffPercentValue);


    }

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
//        setYTitle(baseNum, offetYvalue, -1, -1);


//        List<String> yValueTitles = new ArrayList<String>();


        offetYvalue = offetYvalue / 0.8f;
        float halfOffetValue = offetYvalue / 2.0f;


        List<String> yPercenttitle = new ArrayList<String>();

        yPercenttitle.add(StringFromatUtils.get2PointPercent(baseNum - offetYvalue));
        yPercenttitle.add(StringFromatUtils.get2PointPercent(baseNum - halfOffetValue));
        yPercenttitle.add(StringFromatUtils.get2PointPercent(baseNum));
        yPercenttitle.add(StringFromatUtils.get2PointPercent(baseNum + halfOffetValue));
        yPercenttitle.add(StringFromatUtils.get2PointPercent(baseNum + offetYvalue));


        maChartView.setAxisYTitles(yPercenttitle);
        maChartView.setDrawRightYTitle(false);


        maChartView.setMaxValue(baseNum + offetYvalue);
        maChartView.setMinValue(baseNum - offetYvalue);
    }


    float maxOffsetValue;
    float minOffsetValue;
    float maxOffsetNetValue;
    float minOffsetNetValue;
    float baseNetValue;


    ParseHttpListener sepFundHttpListener = new ParseHttpListener<List<LineEntity>>() {

        @Override
        protected List<LineEntity> parseDateTask(String jsonData) {
            List<LineEntity> linesList = new ArrayList<LineEntity>();
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                JSONObject json = jsonArray.getJSONObject(0);
                String chartlist = json.getString("chartlist");
                List<SepFundChartBean> sepFundChartBeans = DataParse.parseArrayJson(SepFundChartBean.class, chartlist);
                LineEntity lineEntity = new LineEntity();
                lineEntity.setTitle(json.getString("symbol"));
                lineEntity.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
                List<SepFundPointEntity> lineDataList = new ArrayList<SepFundPointEntity>();
                setXTitleBySepFundChartBean(sepFundChartBeans);
                float value = sepFundChartBeans.get(0).getYear_yld();
                maxOffsetValue = value;
                minOffsetValue = value;
                for (SepFundChartBean cPoint : sepFundChartBeans) {


                    SepFundPointEntity pointEntity = new SepFundPointEntity();
                    value = cPoint.getYear_yld();
                    pointEntity.setDesc(cPoint.getDate());
                    pointEntity.setNetvalue(cPoint.getTenthou_unit_incm());
                    pointEntity.setInfo(getManagerByData(cPoint.getDate()));
                    pointEntity.setValue(value);
                    lineDataList.add(pointEntity);
                    if (value > maxOffsetValue) {
                        maxOffsetValue = value;
                    } else if (value < minOffsetValue) {
                        minOffsetValue = value;
                    }


                }
                lineEntity.setLineData(lineDataList);
                linesList.add(lineEntity);

            } catch (JSONException e) {

                e.printStackTrace();
            }

            return linesList;
        }

        @Override
        protected void afterParseData(List<LineEntity> object) {
            if (null != object && object.size() > 0) {
                if (null != lineEntityList) {
                    lineEntityList.removeAll(compareLinesList);
                }
                compareLinesList.clear();
                compareLinesList.addAll(object);
                setSepFundLineList();
            }

        }
    };


    /**
     * 在当前曲线的时间范围内，如果有现任基金经理任职，则要加一个小圆点，用于标注基金经理的任职信息。
     * 如果有多个任职经理，就显示多个圆圈
     * 如果基金经理任职日期，有在当前曲线范围内，但是没有对应到tradedate上（节假日到任），那么这个圆圈就画在TA到任后的第一个交易日上。
     */
    private String getManagerByData(String day) {
        StringBuilder sbMangerText = new StringBuilder();
//        List<ManagersEntity> managersEntityList = null;
        if (null != mFundQuoteBean && null != mFundQuoteBean.getManagers() && !mFundQuoteBean.getManagers().isEmpty()) {
            for (ManagersEntity managerEntity : mFundQuoteBean.getManagers()) {
                if (managerEntity.getStart_date().equals(day)) {
                    sbMangerText.append(managerEntity.getName() + "  ");
                }
            }


        }
        return sbMangerText.toString();

    }


    ParseHttpListener compareListener = new ParseHttpListener<List<LineEntity>>() {

        @Override
        protected List<LineEntity> parseDateTask(String jsonData) {
            List<LineEntity> linesList = new ArrayList<LineEntity>();
            try {
                List<CompareFundsBean> beanList = DataParse.parseArrayJson(CompareFundsBean.class, new JSONArray(
                        jsonData));
                // List<ComparePoint> chartlist = bean.getChartlist();
                // 解析数据，把线条数赋值
                int i = 0;
//                maxOffsetValue = beanList.get(0).getChartlist().get(0).getPercentage();
//                minOffsetValue = maxOffsetValue;
                boolean isCurrentFund;
                for (CompareFundsBean bean : beanList) {
                    isCurrentFund = false;
                    LineEntity lineEntity = new LineEntity();
                    if (!TextUtils.isEmpty(bean.getFundsId()) && bean.getFundsId().equals(mCompareIds)) {
                        lineEntity.setTitle("沪深300");
                        lineEntity.setLineColor(ColorTemplate.getDefaultColors(0));
                    } else if (!TextUtils.isEmpty(bean.getFundsId()) && bean.getFundsId().equals(fundId)) {
                        lineEntity = new DefFundLineEntity();
                        lineEntity.setTitle(mFundQuoteBean.getCode());
                        lineEntity.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
                        isCurrentFund = true;
                        baseNetValue = bean.getChartlist().get(0).getNetvalue();
                    } else {
                        lineEntity.setLineColor(ColorTemplate.getDefaultColors(i));
                    }


                    List<DefFundPointEntity> lineDataList = new ArrayList<DefFundPointEntity>();
                    setXTitleByComparePoint(bean.getChartlist());
                    float netValue = 0;
                    for (ComparePoint cPoint : bean.getChartlist()) {
                        DefFundPointEntity pointEntity = new DefFundPointEntity();
                        float value = cPoint.getPercentage();

                        pointEntity.setDesc(cPoint.getDate());
                        pointEntity.setValue(value);
                        lineDataList.add(pointEntity);
                        if (value > maxOffsetValue) {
                            maxOffsetValue = value;
                        } else if (value < minOffsetValue) {
                            minOffsetValue = value;
                        }
                        if (isCurrentFund) {
                            netValue = cPoint.getNetvalue();
                            pointEntity.setNetvalue(cPoint.getNetvalue());
                            pointEntity.setInfo(getManagerByData(cPoint.getDate()));
                            if (netValue > maxOffsetNetValue) {
                                maxOffsetNetValue = netValue;
                            } else if (netValue < minOffsetNetValue) {
                                minOffsetNetValue = netValue;
                            }
                        }

                    }
                    lineEntity.setLineData(lineDataList);
                    linesList.add(lineEntity);
                    i++;
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

            return linesList;
        }

        @Override
        protected void afterParseData(List<LineEntity> object) {
            if (null != object && object.size() > 0) {
                // setLineListsData(object);
                // setYTitle(dayNetValueList.get(0).getPercentageBegin(),
                // getMaxOffetValue(object));
                if (null != lineEntityList) {
                    lineEntityList.removeAll(compareLinesList);
                }
                compareLinesList.clear();
                compareLinesList.addAll(object);
                setCompareLineList();
            }

        }
    };


}
