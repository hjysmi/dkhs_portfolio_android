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
import com.dkhs.portfolio.bean.SepFundChartBean;
import com.dkhs.portfolio.engine.CompareEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
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
 * @ClassName BenefitChartView
 * @Description TODO(收益曲线视图)
 * @author zwm
 * @date 2015-6-10 下午4:18:02
 * @version 1.0
 */
public class BenefitChartView {

    private FundManagerInfoBean.AchivementsEntity mAchivementsEntity;
    // 默认沪深300的id
    private String mCompareIds = "106000232";
    @ViewInject(R.id.tv_combination_name)
    private TextView tvCombinationName;
    private CompareEngine mCompareEngine;
//    private CompareEngine netValueEngine;
    Calendar cStart, cEnd;
    @ViewInject(R.id.machart)
    private TrendChart maChartView;

    @ViewInject(R.id.tv_more_funds)
    private View moreFundView;
    @ViewInject(R.id.loadView)
    private View loadView;
    @ViewInject(R.id.contentView)
    private View contentView;
    private Context ctx;

    private List<LineEntity> lineEntityList = new ArrayList<LineEntity>();

    public BenefitChartView(Context ctx, FundManagerInfoBean.AchivementsEntity achivementsEntity) {
        this.mAchivementsEntity = achivementsEntity;
        this.ctx = ctx;
        if(null != achivementsEntity.getEnd_date()){
            cEnd = TimeUtils.simpleDateToCalendar(achivementsEntity.getEnd_date());
        }else{
            cEnd=Calendar.getInstance();
        }
        cStart =TimeUtils.simpleDateToCalendar(achivementsEntity.getStart_date());
        mCompareEngine = new CompareEngine();
    }
    public View initView() {
        View view=  LayoutInflater.from(ctx).inflate(R.layout.layout_compare_index_view,null);
        ViewUtils.inject(this, view); // 注入view和事件
        moreFundView.setVisibility(View.GONE);
        onViewCreated(view);
        return  view;
    }

    public void onViewCreated(View view) {
        initMaChart(maChartView);


        if (StockUitls.isSepFund(mAchivementsEntity.getFund().getSymbol_stype())){

            tvCombinationName.setVisibility(View.GONE);
            requestSepFund();
        }else {
            tvCombinationName.setText(mAchivementsEntity.getFund().getAbbr_name());
            requestCompare();
        }
    }

    private void requestSepFund() {
        lineEntityList.clear();
        maxOffsetValue = 0f;

        mCompareEngine.compare( sepFundHttpListener, mAchivementsEntity.getFund().getId()+"", TimeUtils.getTimeString(cStart),
                TimeUtils.getTimeString(cEnd));
    }


    private void initMaChart(TrendChart machart) {
        machart.setMaxValue(120);
        machart.setMinValue(0);
        maChartView.setYlineCounts(2);
        maChartView.setFromCompare(true);
    }

    List<LineEntity> compareLinesList = new ArrayList<LineEntity>();
    private void setCompareLineList() {
        lineEntityList.addAll(compareLinesList);
        float base=(maxOffsetNetValue+minOffsetNetValue)/2;
        float off=Math.max(maxOffsetNetValue-base,base-minOffsetNetValue)*1.2f;
        float base1=maxOffsetValue+minOffsetValue/2;
        float off1=Math.max(maxOffsetValue-base1,base1-minOffsetValue)*1.2f;
        setYTitle(base,off, base1,off1 );
        maChartView.setLineData(lineEntityList);
        contentView.setVisibility(View.VISIBLE);
        loadView.setVisibility(View.GONE);
    }

    private void setSepFundLineList() {
        lineEntityList.addAll(compareLinesList);
        float base1=maxOffsetValue+minOffsetValue/2;
        float off1=Math.max(maxOffsetValue-base1,base1-minOffsetValue)*1.2f;
        setYTitle(base1, off1);
        maChartView.setLineData(lineEntityList);
        contentView.setVisibility(View.VISIBLE);
        loadView.setVisibility(View.GONE);
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
    private void setYTitle(float baseNum,  float maxOffsetNetValue, float base,float offetYvalue) {
        // int baseNum = 1;

        List<String> ytitle = new ArrayList<String>();


        float value1 = maxOffsetNetValue / 0.8f;
        float value2 = maxOffsetNetValue / 2.0f;
        ytitle.add(StringFromatUtils.get2Point(baseNum - value1));
        ytitle.add(StringFromatUtils.get2Point(baseNum - value2));
        ytitle.add(StringFromatUtils.get2Point(baseNum));
        ytitle.add(StringFromatUtils.get2Point(baseNum + value2));
        ytitle.add(StringFromatUtils.get2Point(baseNum + value1));
        maChartView.setAxisYTitles(ytitle);
        maChartView.setMaxValue(baseNum + maxOffsetNetValue);
        maChartView.setMinValue(baseNum - maxOffsetNetValue);


        if(offetYvalue >0) {
            offetYvalue = offetYvalue / 0.8f;
            float halfOffetValue = offetYvalue / 2.0f;
            List<String> yrtitle = new ArrayList<String>();

            yrtitle.add(StringFromatUtils.get2PointPercent(base - offetYvalue));
            yrtitle.add(StringFromatUtils.get2PointPercent(base - halfOffetValue));
            yrtitle.add(StringFromatUtils.get2PointPercent(base));
            yrtitle.add(StringFromatUtils.get2PointPercent(base + halfOffetValue));
            yrtitle.add(StringFromatUtils.get2PointPercent(base + offetYvalue));
            maChartView.setDrawRightYTitle(true);
            maChartView.setAxisRightYTitles(yrtitle);
            maChartView.setDrawTrendChart(true);
        }


    }

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
        setYTitle(baseNum, offetYvalue,-1,-1);
    }


    float maxOffsetValue;
    float minOffsetValue;

    float maxOffsetNetValue;
    float minOffsetNetValue;

    private void requestCompare() {


        mCompareIds=mCompareIds+","+ mAchivementsEntity.getFund().getId();
        lineEntityList.clear();
        maxOffsetValue = 0f;
        mCompareEngine.compare(compareListener, mCompareIds, TimeUtils.getTimeString(cStart),
                TimeUtils.getTimeString(cEnd));
    }

    ParseHttpListener sepFundHttpListener = new ParseHttpListener<List<LineEntity>>() {

        @Override
        protected List<LineEntity> parseDateTask(String jsonData) {
            System.out.println("compareListener parseDateTask");
            List<LineEntity> linesList = new ArrayList<LineEntity>();
            try {


                JSONArray jsonArray=new JSONArray(jsonData);

                JSONObject json= jsonArray.getJSONObject(0);
                String chartlist=  json.getString("chartlist");


            List<SepFundChartBean> sepFundChartBeans=DataParse.parseArrayJson(SepFundChartBean.class, chartlist);


                int i = 0;
                float baseNum = 0;

                float tempMaxOffetValue = 0;

                    LineEntity lineEntity = new LineEntity();
                    lineEntity.setTitle(json.getString("symbol"));
                    lineEntity.setLineColor(ColorTemplate.getDefaultColors(i));
                    List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
                    setXTitleBySepFundChartBean(sepFundChartBeans);
                    for (SepFundChartBean cPoint : sepFundChartBeans) {
                        LinePointEntity pointEntity = new LinePointEntity();
                        // float value = cPoint.getPercentage();
                        float value = cPoint.getYear_yld();

                        pointEntity.setDesc(cPoint.getDate());
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
                // setLineListsData(object);
                // setYTitle(dayNetValueList.get(0).getPercentageBegin(),
                // getMaxOffetValue(object));
                if (null != lineEntityList) {
                    lineEntityList.removeAll(compareLinesList);
                }
                compareLinesList.clear();
                compareLinesList.addAll(object);
                setSepFundLineList();
            }

        }
    };



    ParseHttpListener compareListener = new ParseHttpListener<List<LineEntity>>() {

        @Override
        protected List<LineEntity> parseDateTask(String jsonData) {
            System.out.println("compareListener parseDateTask");
            List<LineEntity> linesList = new ArrayList<LineEntity>();
            try {
                List<CompareFundsBean> beanList = DataParse.parseArrayJson(CompareFundsBean.class, new JSONArray(
                        jsonData));
                // List<ComparePoint> chartlist = bean.getChartlist();
                // 解析数据，把线条数赋值
                int i = 0;
                for (CompareFundsBean bean : beanList) {
                    LineEntity lineEntity = new LineEntity();
                    lineEntity.setTitle(bean.getSymbol());
                    lineEntity.setLineColor(ColorTemplate.getDefaultColors(i));
                    List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
                    setXTitleByComparePoint(bean.getChartlist());
                    for (ComparePoint cPoint : bean.getChartlist()) {
                        LinePointEntity pointEntity = new LinePointEntity();
                        float value = cPoint.getPercentage();
                        float netV=cPoint.getNetvalue();
                        pointEntity.setDesc(cPoint.getDate());
                        pointEntity.setValue(netV);
                        lineDataList.add(pointEntity);
                        if (value > maxOffsetValue) {
                            maxOffsetValue = value;
                        } else if (value < minOffsetValue) {
                            minOffsetValue = value;
                        }
                        if (netV > maxOffsetNetValue) {
                            maxOffsetNetValue = netV;
                        } else if (netV < minOffsetNetValue) {
                            minOffsetNetValue = netV;
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
