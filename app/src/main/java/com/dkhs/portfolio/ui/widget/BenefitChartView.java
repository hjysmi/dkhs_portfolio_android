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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
    //    Calendar cStart, cEnd;
    DateTime dateStart, dateEnd;
    @ViewInject(R.id.machart)
    private TrendChart maChartView;


    @ViewInject(R.id.rl_compare_title)
    private View titleView;
    @ViewInject(R.id.loadView)
    private View loadView;
    @ViewInject(R.id.line)
    private View lineView;
    @ViewInject(R.id.contentView)
    private View contentView;
    @ViewInject(R.id.rl_empty)
    private View emptyView;
    private Context ctx;

    private String fundId;
    private int symbol_stype;
    private String abbrName;

    private List<LineEntity> lineEntityList = new ArrayList<LineEntity>();


    private View benifitView;

    private FundQuoteBean mFundQuoteBean;
    private LinkedList<ManagersEntity> inertManagerList = new LinkedList<>();

    public BenefitChartView(Context ctx) {
        this.ctx = ctx;
        mCompareEngine = new CompareEngine();
        benifitView = initView();
    }

    public View getBenifitView() {
        return benifitView;
    }

    public View initView() {
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_compare_index_view, new LinearLayout(ctx), false);
        ViewUtils.inject(this, view); // 注入view和事件
        return view;
    }

    private FundManagerInfoBean.AchivementsEntity achivementsEntity;

    public void draw(FundManagerInfoBean.AchivementsEntity achivementsEntity) {
        maChartView.setIsBenefitDash(true);
        maChartView.setIsDrawBenefit(true);
        loadView.setVisibility(View.VISIBLE);
        this.achivementsEntity = achivementsEntity;
        contentView.setVisibility(View.GONE);
        if (null != achivementsEntity.getEnd_date()) {
//            cEnd = TimeUtils.getCalendar(achivementsEntity.getEnd_date());
            dateEnd = new DateTime(achivementsEntity.getEnd_date());
        } else {
//            cEnd = Calendar.getInstance();
            dateEnd = new DateTime();
        }
        dateStart = new DateTime(achivementsEntity.getStart_date());
//        cStart = TimeUtils.getCalendar(achivementsEntity.getStart_date());
        symbol_stype = achivementsEntity.getFund().getSymbol_stype();
        fundId = achivementsEntity.getFund().getId() + "";
        abbrName = achivementsEntity.getFund().getAbbr_name();
        lineView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        onRequest();
    }


    private boolean mError;


    public void setError() {
        mError = true;
        emptyView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        loadView.setVisibility(View.GONE);


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
        this.mFundQuoteBean = fundQuoteBean;
        if (StockUitls.isDelistStock(mFundQuoteBean.getList_status() + "")) {
            setError();
        } else {
            emptyView.setVisibility(View.GONE);
            draw(fundQuoteBean);
        }
    }


    public void draw(FundQuoteBean fundQuoteBean) {
        loadView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);

//        cEnd = Calendar.getInstance();
        dateEnd = new DateTime();
        // FIXME: 2015/6/10 多个基金经理的时候 开始时间怎么算
//        cStart = TimeUtils.getCalendar(fundQuoteBean.getManagers().get(0).getStart_date());
        dateStart = new DateTime(fundQuoteBean.getManagers().get(0).getStart_date());
        this.symbol_stype = fundQuoteBean.getSymbol_stype();
        fundId = fundQuoteBean.getId() + "";
        abbrName = fundQuoteBean.getAbbrName();
        inertManagerList.clear();
        inertManagerList.addAll(fundQuoteBean.getManagers());
        onRequest();


    }

    private void onRequest() {
        initMaChart(maChartView);
        if (StockUitls.isStockType(symbol_stype)) {
            tvCombinationName.setText(abbrName);
            requestCompare();
        } else {
            titleView.setVisibility(View.GONE);
            if (StockUitls.isSepFund(symbol_stype)) {
                requestSepFund();
            } else {
                requestElseType();
            }
        }
//        if (StockUitls.isSepFund(symbol_stype)) {
//            titleView.setVisibility(View.GONE);
//            requestSepFund();
//        } else {
//            tvCombinationName.setText(abbrName);
//            requestCompare();
//        }
    }

    private void requestElseType() {
        lineEntityList.clear();
        maxOffsetValue = 0f;
        if (trendType == FundTrendType.Default) {
            mCompareEngine.compare(elseTypeListener, fundId, dateStart.toString(TimeUtils.FORMAT_TEMPLATE_DAY),
                    dateEnd.toString(TimeUtils.FORMAT_TEMPLATE_DAY));
        } else if (trendType == FundTrendType.OfficeDay) {
            mCompareEngine.compare(elseTypeListener, fundId, dateStart.toString(TimeUtils.FORMAT_TEMPLATE_DAY),
                    dateEnd.toString(TimeUtils.FORMAT_TEMPLATE_DAY));
        } else {
            mCompareEngine.compareByPeriod(elseTypeListener, fundId, trendType.getValue());

        }
    }


    private void requestSepFund() {
        lineEntityList.clear();
        maxOffsetValue = 0f;
        if (trendType == FundTrendType.Default) {
            mCompareEngine.compare(sepFundHttpListener, fundId, dateStart.toString(TimeUtils.FORMAT_TEMPLATE_DAY),
                    dateEnd.toString(TimeUtils.FORMAT_TEMPLATE_DAY));
        } else if (trendType == FundTrendType.OfficeDay) {
            mCompareEngine.compare(sepFundHttpListener, fundId, dateStart.toString(TimeUtils.FORMAT_TEMPLATE_DAY),
                    dateEnd.toString(TimeUtils.FORMAT_TEMPLATE_DAY));
        } else {
            mCompareEngine.compareByPeriod(sepFundHttpListener, fundId, trendType.getValue());

        }
    }

    private void requestCompare() {

        lineEntityList.clear();
        maxOffsetValue = 0f;
        if (trendType == FundTrendType.Default) {
            mCompareEngine.compare(compareListener, (fundId + "," + mCompareIds), dateStart.toString(TimeUtils.FORMAT_TEMPLATE_DAY),
                    dateEnd.toString(TimeUtils.FORMAT_TEMPLATE_DAY));
        } else if (trendType == FundTrendType.OfficeDay) {
            mCompareEngine.compare(compareListener, (fundId + "," + mCompareIds), dateStart.toString(TimeUtils.FORMAT_TEMPLATE_DAY),
                    dateEnd.toString(TimeUtils.FORMAT_TEMPLATE_DAY));
        } else {
            mCompareEngine.compareByPeriod(compareListener, (fundId + "," + mCompareIds), trendType.getValue());

        }
    }

    private void initMaChart(TrendChart machart) {
        machart.setMaxValue(120);
        machart.setMinValue(0);
        maChartView.setYlineCounts(2);
        maChartView.setFromCompare(true);
        maChartView.setSmallLine();
        maChartView.setCanTouable(canTouchable);
    }

    List<LineEntity> compareLinesList = new ArrayList<LineEntity>();

    private void setCompareLineList() {
        maChartView.refreshClear();
        lineEntityList.addAll(compareLinesList);

        float base = (maxOffsetValue + minOffsetValue) / 2;
        float off = Math.max(maxOffsetValue - base, base - minOffsetValue);
        setYTitle(base, off, baseNetValue);
        maChartView.setLineData(lineEntityList);
        maChartView.setIsFundTrendCompare(true);
        maChartView.setDisplayAxisYTitleColor(false);
        maChartView.setDisplayYRightTitleByZero(true);

        onFinishUpdateUI();
    }

    private void onFinishUpdateUI() {
        if (!mError) {
            if (StockUitls.isStockType(symbol_stype)) {
                titleView.setVisibility(View.VISIBLE);
            } else {
                titleView.setVisibility(View.GONE);
            }
            contentView.setVisibility(View.VISIBLE);
            loadView.setVisibility(View.GONE);
        }
    }

    private void setSepFundLineList() {
        maChartView.refreshClear();
        lineEntityList.addAll(compareLinesList);
        float base1 = (maxOffsetValue + minOffsetValue) / 2;
        float off1 = Math.max(maxOffsetValue - base1, base1 - minOffsetValue);
        setYTitle(base1, off1);
        maChartView.setLineData(lineEntityList);
        maChartView.setDisplayAxisYTitleColor(true);
        maChartView.setIsBenefitDash(true);
        maChartView.setIsDrawBenefit(true);
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


    public void drawFundIndex(List<FundManagerInfoBean.FundIndexEntity> lists) {
        initMaChart(maChartView);

        List<LineEntity> linesList = new ArrayList<LineEntity>();
        LineEntity lineEntity = new LineEntity();
        lineEntity.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
        List<SepFundPointEntity> lineDataList = new ArrayList<SepFundPointEntity>();
        List<String> xtitle = new ArrayList<String>();
        String startDay = lists.get(0).getTradedate();
        if (TextUtils.isEmpty(startDay)) {
            xtitle.add("");
        } else {
            xtitle.add(startDay);

        }
        xtitle.add(lists.get(lists.size() - 1).getTradedate());
        maChartView.setMaxPointNum(lists.size());
        maChartView.setAxisXTitles(xtitle);
        float value = lists.get(0).getPercentage();
        String firstDay = lists.get(0).getTradedate();
        maxOffsetValue = value;
        minOffsetValue = value;
        for (FundManagerInfoBean.FundIndexEntity entity : lists) {
            SepFundPointEntity pointEntity = new SepFundPointEntity();
            value = entity.getPercentage();
            pointEntity.setDesc(entity.getTradedate());
            pointEntity.setNetvalue(entity.getDay_index());
            pointEntity.setInfo(getManagerByData(firstDay, entity.getTradedate()));
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
        if (null != lineEntityList) {
            lineEntityList.removeAll(compareLinesList);

        }
        compareLinesList.clear();
        compareLinesList.addAll(linesList);
        lineView.setVisibility(View.GONE);
        setSepFundLineList();

    }

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
                String firstDay = sepFundChartBeans.get(0).getDate();
                maxOffsetValue = value;
                minOffsetValue = value;
                for (SepFundChartBean cPoint : sepFundChartBeans) {


                    SepFundPointEntity pointEntity = new SepFundPointEntity();
                    value = cPoint.getYear_yld();
                    pointEntity.setDesc(cPoint.getDate());
                    pointEntity.setNetvalue(cPoint.getTenthou_unit_incm());
                    pointEntity.setInfo(getManagerByData(firstDay, cPoint.getDate()));
                    pointEntity.setValue(value);
                    lineDataList.add(pointEntity);

                    SepFundPointEntity sepFloat = new SepFundPointEntity();


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


            } else {
                setError();
            }

        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            setError();
            super.onFailure(errCode, errMsg);
        }
    };


    /**
     * 在当前曲线的时间范围内，如果有现任基金经理任职，则要加一个小圆点，用于标注基金经理的任职信息。
     * 如果有多个任职经理，就显示多个圆圈
     * 如果基金经理任职日期，有在当前曲线范围内，但是没有对应到tradedate上（节假日到任），那么这个圆圈就画在TA到任后的第一个交易日上。
     */
    private String getManagerByData(String firstDay, String day) {
        StringBuilder sbMangerText = new StringBuilder();
        if (null != inertManagerList && !inertManagerList.isEmpty()) {
            Iterator<ManagersEntity> it = inertManagerList.iterator();
            while (it.hasNext()) {
                ManagersEntity managerEntity = it.next();
//                Calendar firstCal = TimeUtils.getCalendar(firstDay);
//                Calendar managerCal = TimeUtils.getCalendar(managerEntity.getStart_date());
                DateTime firstCal = new DateTime(firstDay);
                DateTime managerCal = new DateTime(managerEntity.getStart_date());
//                if (!managerCal.before(firstCal)) {
                if (!(TimeUtils.compareDateTime(managerCal, firstCal) < 0)) {
//                    Calendar currentCal = TimeUtils.getCalendar(day);
                    DateTime currentCal = new DateTime(day);
//                    if (managerCal.equals(currentCal) || currentCal.after(managerCal)) {
                    if (TimeUtils.compareDateTime(managerCal, currentCal) == 0 || TimeUtils.compareDateTime(currentCal, managerCal) > 0) {
                        sbMangerText.append(managerEntity.getName()).append("  ");
                        it.remove();
                        Log.d("InsertManagerData", " manager:" + managerEntity.getName() + " is  insert to" + day);
                    }
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
                String firstDay = null;
                LineEntity lineEntity;

                if (beanList != null && beanList.size() > 1) {


                    for (CompareFundsBean bean : beanList) {
                        lineEntity = new LineEntity();
                        isCurrentFund = false;
                        if (!TextUtils.isEmpty(bean.getFundsId()) && bean.getFundsId().equals(mCompareIds)) {
                            lineEntity.setTitle("沪深300");
                            lineEntity.setLineColor(ColorTemplate.getDefaultColors(0));
                        } else if (!TextUtils.isEmpty(bean.getFundsId()) && bean.getFundsId().equals(fundId)) {
                            lineEntity = new DefFundLineEntity();
                            Log.e("TTTTT", "  new DefFundLineEntity");
                            if (mFundQuoteBean != null) {
                                lineEntity.setTitle(mFundQuoteBean.getSymbol());
                            } else if (achivementsEntity != null) {
                                if (!TextUtils.isEmpty(achivementsEntity.getFund().getSymbol()))
                                    lineEntity.setTitle(achivementsEntity.getFund().getSymbol());
                            }
                            lineEntity.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
                            isCurrentFund = true;
                            baseNetValue = bean.getChartlist().get(0).getNetvalue();
                            firstDay = bean.getChartlist().get(0).getDate();
                        } else {
                            lineEntity.setLineColor(ColorTemplate.getDefaultColors(i));
                        }


                        List<DefFundPointEntity> lineDataList = new ArrayList<DefFundPointEntity>();
                        setXTitleByComparePoint(bean.getChartlist());
                        float net_cumulative = 0;

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
//                            net_cumulative = cPoint.getNetvalue();
                                net_cumulative = cPoint.getNet_cumulative();
                                pointEntity.setNet_cumulative(net_cumulative);
                                pointEntity.setNetvalue(cPoint.getNetvalue());
                                pointEntity.setInfo(getManagerByData(firstDay, cPoint.getDate()));
                                if (net_cumulative > maxOffsetNetValue) {
                                    maxOffsetNetValue = net_cumulative;
                                } else if (net_cumulative < minOffsetNetValue) {
                                    minOffsetNetValue = net_cumulative;
                                }
                            }

                        }
                        lineEntity.setLineData(lineDataList);
                        linesList.add(lineEntity);
                        i++;
                    }
                }

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
                setCompareLineList();
            } else {
                setError();
            }

        }


        // 这个方法不会走
        @Override
        public void onHttpFailure(int errCode, Throwable err) {
            super.onHttpFailure(errCode, err);
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            setError();
            super.onFailure(errCode, errMsg);
        }

    };
    ParseHttpListener elseTypeListener = new ParseHttpListener<List<LineEntity>>() {


        @Override
        protected List<LineEntity> parseDateTask(String jsonData) {
            List<LineEntity> linesList = new ArrayList<LineEntity>();
            try {
//                List<ComparePoint> beanList = DataParse.parseArrayJson(ComparePoint.class, new JSONArray(
//                        jsonData));
                JSONArray jsonArray = new JSONArray(jsonData);
                JSONObject json = jsonArray.getJSONObject(0);
                String chartlist = json.getString("chartlist");
                List<ComparePoint> lists = DataParse.parseArrayJson(ComparePoint.class, chartlist);

                LineEntity lineEntity;
                lineEntity = new DefFundLineEntity();
                if (mFundQuoteBean != null) {
                    lineEntity.setTitle(mFundQuoteBean.getSymbol());
                } else if (achivementsEntity != null) {
                    if (!TextUtils.isEmpty(achivementsEntity.getFund().getSymbol()))
                        lineEntity.setTitle(achivementsEntity.getFund().getSymbol());
                }
                lineEntity.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
                baseNetValue = lists.get(0).getNetvalue();
                List<DefFundPointEntity> lineDataList = new ArrayList<DefFundPointEntity>();
                setXTitleByComparePoint(lists);
                float net_cumulative = 0;
                String firstDay = lists.get(0).getDate();
                for (ComparePoint cPoint : lists) {
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
//                            net_cumulative = cPoint.getNetvalue();
                    net_cumulative = cPoint.getNet_cumulative();
                    pointEntity.setNet_cumulative(net_cumulative);
                    pointEntity.setNetvalue(cPoint.getNetvalue());
                    pointEntity.setInfo(getManagerByData(firstDay, cPoint.getDate()));
                    if (net_cumulative > maxOffsetNetValue) {
                        maxOffsetNetValue = net_cumulative;
                    } else if (net_cumulative < minOffsetNetValue) {
                        minOffsetNetValue = net_cumulative;
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
            } else {
                setError();
            }

        }


        // 这个方法不会走
        @Override
        public void onHttpFailure(int errCode, Throwable err) {
            super.onHttpFailure(errCode, err);
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            setError();
            super.onFailure(errCode, errMsg);
        }

    };

    private boolean canTouchable = true;

    public void setCanTouable(boolean canTouchable) {
        this.canTouchable = canTouchable;
    }

}
