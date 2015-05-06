/**
 * @Title CompareFundFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-5-5 下午4:18:02
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.CompareFundsBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.CompareFundsBean.ComparePoint;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.engine.CompareEngine;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CompareFundsActivity;
import com.dkhs.portfolio.ui.NewCombinationDetailActivity;
import com.dkhs.portfolio.ui.adapter.CompareIndexAdapter;
import com.dkhs.portfolio.ui.adapter.CompareIndexAdapter.CompareFundItem;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePointEntity;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @ClassName CompareFundFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-5-5 下午4:18:02
 * @version 1.0
 */
public class CompareIndexFragment extends BaseFragment implements OnClickListener {

    private CombinationBean mCombinationBean;
    // 默认沪深300的id
    private String mCompareIds = "106000232";

    @ViewInject(R.id.tv_combination_name)
    private TextView tvCombinationName;

    private CompareEngine mCompareEngine;
    private NetValueEngine netValueEngine;
    private Calendar mCreateCalender;
    Calendar cStart, cEnd;

    @ViewInject(R.id.machart)
    private TrendChart maChartView;

    private List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
    private List<LineEntity> lineEntityList = new ArrayList<LineEntity>();

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_compare_index;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        cStart = Calendar.getInstance();
        cStart.add(Calendar.MONTH, -1);
        cEnd = Calendar.getInstance();
        netValueEngine = new NetValueEngine(mCombinationBean.getId());
        mCompareEngine = new CompareEngine();
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(NewCombinationDetailActivity.EXTRA_COMBINATION);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initMaChart(maChartView);
        tvCombinationName.setText(mCombinationBean.getName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        requestCompare();
    }

    private void initMaChart(TrendChart machart) {

        machart.setMaxValue(120);
        machart.setMinValue(0);
        maChartView.setYlineCounts(2);
        maChartView.setFromCompare(true);

    }

    @OnClick({ R.id.tv_more_funds })
    public void onClick(View v) {
        if (v.getId() == R.id.tv_more_funds) {
            startActivity(CompareFundsActivity.newIntent(getActivity(), mCombinationBean));
        }
    }

    ParseHttpListener historyNetValueListener = new ParseHttpListener<HistoryNetValue>() {

        @Override
        protected HistoryNetValue parseDateTask(String jsonData) {
            HistoryNetValue histroyValue = DataParse.parseObjectJson(HistoryNetValue.class, jsonData);
            return histroyValue;
        }

        @Override
        protected void afterParseData(HistoryNetValue object) {
            if (object != null && isAdded()) {
                List<HistoryNetBean> dayNetValueList = object.getChartlist();
                if (dayNetValueList != null && dayNetValueList.size() > 0) {

                    getMaxOffetValue(object);
                    setYTitle(dayNetValueList.get(0).getPercentageBegin(), maxOffsetValue);
                    setXTitle(dayNetValueList);

                    LineEntity mCombinationLine = new LineEntity();
                    mCombinationLine.setTitle("当前组合");
                    mCombinationLine.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
                    mCombinationLine.setLineData(lineDataList);
                    lineEntityList.remove(combinationLineEntity);
                    combinationLineEntity = mCombinationLine;

                    setLineData();

                }

                float increaseValue = (object.getEnd() - object.getBegin()) / object.getBegin();
                // tvIncreaseValue.setText(StringFromatUtils.get2PointPercent((increaseValue) * 100));
                // if (increaseValue > 0) {
                // increaseView.setBackgroundColor(ColorTemplate.DEF_RED);
                // } else {
                // increaseView.setBackgroundColor(ColorTemplate.DEF_GREEN);
                // }
            }
        }

    };

    LineEntity combinationLineEntity = new LineEntity<LinePointEntity>();
    List<LineEntity> compareLinesList = new ArrayList<LineEntity>();

    private void setLineData() {
        lineEntityList.add(0, combinationLineEntity);
        maChartView.setLineData(lineEntityList);
    }

    private void setCompareLineList() {
        lineEntityList.addAll(compareLinesList);
        setYTitle(0, maxOffsetValue);
        maChartView.setLineData(lineEntityList);
    }

    private void setXTitle(List<HistoryNetBean> dayNetValueList) {
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

    /**
     * 设置纵坐标标题，并设置曲线的最大值和最小值
     */
    private void setYTitle(float baseNum, float offetYvalue) {
        // int baseNum = 1;
        offetYvalue = offetYvalue / 0.8f;
        List<String> ytitle = new ArrayList<String>();
        float halfOffetValue = offetYvalue / 2.0f;

        ytitle.add(StringFromatUtils.get2PointPercent(baseNum - offetYvalue));
        ytitle.add(StringFromatUtils.get2PointPercent(baseNum - halfOffetValue));
        ytitle.add(StringFromatUtils.get2PointPercent(baseNum));
        ytitle.add(StringFromatUtils.get2PointPercent(baseNum + halfOffetValue));
        ytitle.add(StringFromatUtils.get2PointPercent(baseNum + offetYvalue));
        maChartView.setAxisYTitles(ytitle);
        maChartView.setMaxValue(baseNum + offetYvalue);
        maChartView.setMinValue(baseNum - offetYvalue);

    }

    /**
     * 遍历所有净值，取出最大值和最小值，计算以1为基准的最大偏差值
     */
    private void getMaxOffetValue(HistoryNetValue historyNetValue) {
        // lineDataList.clear();
        int dashLineSize = 0;
        List<HistoryNetBean> historyNetList = historyNetValue.getChartlist();
        int dataLenght = historyNetList.size();
        float baseNum = historyNetList.get(0).getPercentageBegin();
        float maxNum = baseNum, minNum = baseNum;
        float tempMaxOffet = 0;
        for (int i = 0; i < dataLenght; i++) {
            LinePointEntity pointEntity = new LinePointEntity();
            HistoryNetBean todayBean = historyNetList.get(i);
            // float pointValue = todayBean.getPercentage();
            float pointValue = todayBean.getPercentageBegin();
            pointEntity.setDesc(todayBean.getDate());
            if (dashLineSize == 0 && TimeUtils.simpleDateToCalendar(todayBean.getDate()) != null) {
                if (TimeUtils.simpleDateToCalendar(todayBean.getDate()).after(mCreateCalender)) {
                    dashLineSize = i;
                }
            }

            pointEntity.setValue(pointValue);
            lineDataList.add(pointEntity);

            if (pointValue > maxNum) {
                maxNum = pointValue;
            } else if (pointValue < minNum) {
                minNum = pointValue;
            }
        }
        float offetValue;
        maxNum = maxNum - baseNum;
        minNum = baseNum - minNum;

        tempMaxOffet = maxNum > minNum ? maxNum : minNum;
        if (tempMaxOffet > maxOffsetValue) {
            maxOffsetValue = tempMaxOffet;
        }
        if (dashLineSize == 0) {
            dashLineSize = dataLenght;
        }
        // System.out.println("dashLineSize:" + dashLineSize);
        maChartView.setDashLinePointSize(dashLineSize);

        // return offetValue;

    }

    float maxOffsetValue;

    private void requestCompare() {
        lineEntityList.clear();
        maxOffsetValue = 0f;
        netValueEngine.requeryDay(TimeUtils.getTimeString(cStart), TimeUtils.getTimeString(cEnd),
                historyNetValueListener);
        mCompareEngine.compare(compareListener, mCompareIds, TimeUtils.getTimeString(cStart),
                TimeUtils.getTimeString(cEnd));
        compareListener.setLoadingDialog(getActivity());
    }

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
                float baseNum = 0;
                float maxNum = baseNum, minNum = baseNum;
                float tempMaxOffetValue = 0;
                for (CompareFundsBean bean : beanList) {
                    LineEntity lineEntity = new LineEntity();
                    lineEntity.setTitle(bean.getSymbol());
                    lineEntity.setLineColor(ColorTemplate.getDefaultColors(i));

                    List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
                    for (ComparePoint cPoint : bean.getChartlist()) {
                        LinePointEntity pointEntity = new LinePointEntity();
                        // float value = cPoint.getPercentage();
                        float value = cPoint.getPercentage();
                        pointEntity.setDesc(cPoint.getDate());
                        pointEntity.setValue(value);
                        lineDataList.add(pointEntity);
                        if (value > maxNum) {
                            maxNum = value;
                        } else if (value < minNum) {
                            minNum = value;
                        }

                    }

                    lineEntity.setLineData(lineDataList);
                    linesList.add(lineEntity);

                    float value = (bean.getEnd() - bean.getBegin()) / bean.getBegin();

                    // mCompareItemList.get(i).value = StringFromatUtils.get2PointPercent(value);
                    i++;
                }
                maxNum = maxNum - baseNum;
                minNum = baseNum - minNum;

                tempMaxOffetValue = maxNum > minNum ? maxNum : minNum;
                if (tempMaxOffetValue > maxOffsetValue) {
                    maxOffsetValue = tempMaxOffetValue;
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
