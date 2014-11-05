/**
 * @Title FragmentCompare.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午9:32:29
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.CompareFundsBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.CompareFundsBean.ComparePoint;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.CompareEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.SearchStockEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.SelectFundActivity;
import com.dkhs.portfolio.ui.SelectStockActivity;
import com.dkhs.portfolio.ui.adapter.CompareIndexAdapter;
import com.dkhs.portfolio.ui.adapter.CompareIndexAdapter.CompareFundItem;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePointEntity;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName FragmentCompare
 * @author zjz
 * @date 2014-9-3 上午9:32:29
 * @version 1.0
 */
public class FragmentCompare extends Fragment implements OnClickListener, FragmentLifecycle {

    private final int REQUESTCODE_SELECT_FUND = 900;

    private GridView mGridView;
    private CompareIndexAdapter mGridAdapter;
    private List<CompareFundItem> mCompareItemList;
    private List<SelectStockBean> selectStockList;
    private Button btnStartTime;
    private Button btnEndTime;
    private Button btnCompare;
    private Button btnSelectFund;
    private TextView tvTimeDuration;
    // private TextView tvNoData;
    private TextView tvIncreaseValue;
    private View increaseView;

    // private int mYear;
    // private int mMonth;
    // private int mDay;

    private boolean isPickStartDate;

    private CombinationBean mCombinationBean;

    // private String strStartTime = "";
    // private String strEndTime = "";
    // private String mDayFormat = "%d-%02d-%02d";

    // 默认上证指数，沪深300的id
    private String mCompareIds = "106000082,106000232";

    private CompareEngine mCompareEngine;
    // private Calendar mCurrentCalendar;

    private Calendar mCreateCalender;
    Calendar cStart, cEnd;
    // private Date mSelectStartDate;

    private TrendChart maChartView;
    private List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
    private List<LineEntity> lineEntityList = new ArrayList<LineEntity>();

    // private boolean isBeforeCreateDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCompareItemList = new ArrayList<CompareIndexAdapter.CompareFundItem>();
        selectStockList = new ArrayList<SelectStockBean>();
        mGridAdapter = new CompareIndexAdapter(getActivity(), mCompareItemList);

        cStart = Calendar.getInstance();
        cStart.add(Calendar.MONTH, -1);
        cEnd = Calendar.getInstance();
        // mYear = cStart.get(Calendar.YEAR);
        // mMonth = cStart.get(Calendar.MONTH);
        // mDay = cStart.get(Calendar.DAY_OF_MONTH);
        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        mCompareEngine = new CompareEngine();
        setGridItemData();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // recLifeCycle_with_savedInstanceState(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    private void setGridItemData() {

        CompareFundItem defalutItem1 = mGridAdapter.new CompareFundItem();
        defalutItem1.name = "沪深300";
        CompareFundItem defalutItem2 = mGridAdapter.new CompareFundItem();
        defalutItem2.name = "上证指数";
        SelectStockBean sBean1 = new SelectStockBean();
        sBean1.code = "106000082";
        sBean1.id = 106000082;

        sBean1.name = "上证指数";
        SelectStockBean sBean2 = new SelectStockBean();
        sBean2.code = "106000232";
        sBean2.id = 106000232;
        sBean2.name = "沪深300";

        selectStockList.add(sBean1);
        selectStockList.add(sBean2);

        mCompareItemList.add(defalutItem1);
        mCompareItemList.add(defalutItem2);
        mGridAdapter.notifyDataSetChanged();
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
        mCreateCalender = TimeUtils.toCalendar(mCombinationBean.getCreateTime());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compare, null);
        initView(view);
        maChartView = (TrendChart) view.findViewById(R.id.machart);
        initMaChart(maChartView);

        requestCompare();
        return view;
    }

    private void initView(View view) {

        btnStartTime = (Button) view.findViewById(R.id.tv_compare_ftime);
        btnEndTime = (Button) view.findViewById(R.id.tv_compare_ttime);
        btnSelectFund = (Button) view.findViewById(R.id.btn_select_fund);
        btnCompare = (Button) view.findViewById(R.id.btn_compare_fund);
        tvTimeDuration = (TextView) view.findViewById(R.id.tv_addup_date);
        // tvNoData = (TextView) view.findViewById(R.id.tv_nodate);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_addup_value);
        increaseView = view.findViewById(R.id.rl_addup_history);

        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);
        btnSelectFund.setOnClickListener(this);
        btnCompare.setOnClickListener(this);

        mGridView = (GridView) view.findViewById(R.id.gv_comparison);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                // // mImageFetcher.setPauseWork(true);
                // } else {
                // mImageFetcher.setPauseWork(false);
                // }
            }

            @Override
            public void
                    onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int columnWidth = (mGridView.getWidth() - (getResources()
                        .getDimensionPixelSize(R.dimen.compare_horspacing)) * 4) / 5;

                mGridAdapter.setItemHeight((int) (columnWidth));
            }
        });
        mGridView.setOnItemClickListener(compareItemClick);

        if (null != mCombinationBean) {
            // setStartTime(TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));
            // setStartTime(TimeUtils.getTimeString(cStart));
            // setEndTime(TimeUtils.getTimeString(cEnd));
            // Calendar cStart = Calendar.getInstance();
            // cStart.set(mYear, mMonth - 1, mDay);
            isBeforeMonthCreateDate(cStart);

            updateDayDisplay();
        }

    }

    OnItemClickListener compareItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // String name = mCompareItemList.get(position).name;
            // Toast.makeText(getActivity(), "选择" + name, Toast.LENGTH_SHORT).show();
            if (lineEntityList.size() >= position + 1) {
                boolean isHide = !(mCompareItemList.get(position).iSelect);
                mCompareItemList.get(position).iSelect = isHide;
                lineEntityList.get(position + 1).setDisplay(!isHide);
                maChartView.invalidate();
                mGridAdapter.notifyDataSetChanged();
            }
        }
    };

    // private void setStartTime(String startDay) {
    // strStartTime = startDay;
    // }
    //
    // private void setEndTime(String endDay) {
    // strEndTime = endDay;
    // }
    //
    // private String getStartTime() {
    // return strStartTime;
    // }
    //
    // private String getEndTime() {
    // return strEndTime;
    // }

    private void initMaChart(TrendChart machart) {

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);

        machart.setDisplayBorder(false);
        machart.setLatitudeColor(Color.LTGRAY);

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);
        machart.setBorderColor(Color.TRANSPARENT);
        machart.setBackgroudColor(Color.WHITE);
        machart.setAxisMarginTop(5);
        machart.setAxisMarginLeft(5);
        machart.setAxisMarginRight(5);

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
        maChartView.setFromCompare(true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            machart.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_compare_ftime: {
                isPickStartDate = true;
                showPickerDate();
            }

                break;
            case R.id.tv_compare_ttime: {
                isPickStartDate = false;
                showPickerDate();

            }
                break;
            case R.id.btn_compare_fund: {
                if (isBetween7day()) {
                    Toast.makeText(getActivity(), "查询时间范围太小，请不要小于7天", Toast.LENGTH_SHORT).show();
                } else {
                    btnCompare.setEnabled(false);
                    requestCompare();
                }
            }

                break;
            case R.id.btn_select_fund: {
                Intent intent = new Intent(getActivity(), SelectFundActivity.class);

                intent.putExtra(BaseSelectActivity.ARGUMENT_SELECT_LIST, (Serializable) selectStockList);

                // Intent intent = new Intent(getActivity(), SelectStockActivity.class);
                startActivityForResult(intent, REQUESTCODE_SELECT_FUND);
            }
                break;
            default:
                break;
        }

    }

    private boolean isBetween7day() {
        long between_days = (cEnd.getTimeInMillis() - cStart.getTimeInMillis()) / (1000 * 3600 * 24);
        return between_days < 7 ? true : false;
    }

    private void requestCompare() {
        lineEntityList.clear();
        maxOffsetValue = 0f;
        reShowGirdItem();
        new NetValueEngine(mCombinationBean.getId()).requeryDay(TimeUtils.getTimeString(cStart),
                TimeUtils.getTimeString(cEnd), historyNetValueListener);
        mCompareEngine.compare(compareListener, mCompareIds, TimeUtils.getTimeString(cStart),
                TimeUtils.getTimeString(cEnd));

    }
    private void reShowGirdItem(){
        for(CompareFundItem item:mCompareItemList){
            item.iSelect =false;
        }
        mGridAdapter.notifyDataSetChanged();
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
                btnCompare.setEnabled(true);
                List<HistoryNetBean> dayNetValueList = object.getChartlist();
                if (dayNetValueList != null && dayNetValueList.size() > 0) {

                    // int sizeLength = dayNetValueList.size();
                    getMaxOffetValue(object);
                    setYTitle(dayNetValueList.get(0).getPercentageBegin(), maxOffsetValue);
                    // setYTitle(dayNetValueList.get(0).getPercentageBegin(), );
                    // setHistoryPointTitle();
                    setXTitle(dayNetValueList);

                    LineEntity mCombinationLine = new LineEntity();
                    mCombinationLine.setTitle("我的组合");
                    mCombinationLine.setLineColor(ColorTemplate.MY_COMBINATION_LINE);
                    mCombinationLine.setLineData(lineDataList);
                    lineEntityList.remove(combinationLineEntity);
                    combinationLineEntity = mCombinationLine;

                    setLineData();

                }

                float increaseValue = (object.getEnd() - object.getBegin()) / object.getBegin();
                tvIncreaseValue.setText(StringFromatUtils.get2PointPercent(increaseValue * 100));
                if (increaseValue > 0) {
                    increaseView.setBackgroundColor(ColorTemplate.DEF_RED);
                } else {
                    increaseView.setBackgroundColor(ColorTemplate.DEF_GREEN);
                }
            }
        }

    };

    LineEntity combinationLineEntity = new LineEntity<LinePointEntity>();

    private void setLineData() {
        lineEntityList.add(0, combinationLineEntity);
        // lineEntityList.add(lineEntity);
        // maChartView.setDrawDashLine(isBeforeCreateDate);
        maChartView.setLineData(lineEntityList);
    }

    List<LineEntity> compareLinesList = new ArrayList<LineEntity>();

    private void setCompareLineList() {
        System.out.println("setLineListsData :" + compareLinesList.size());
        lineEntityList.addAll(compareLinesList);
        // maChartView.setDrawDashLine(isBeforeCreateDate);
        setYTitle(0, maxOffsetValue);
        maChartView.setLineData(lineEntityList);
    }

    // private void setHistoryPointTitle() {
    // List<String> titles = new ArrayList<String>();
    // titles.add("日期");
    // titles.add("当前净值");
    // maChartView.setPointTitleList(titles);
    // }

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
            float pointValue = todayBean.getPercentageBegin();
            pointEntity.setDesc(todayBean.getDate());
            if (dashLineSize == 0 && TimeUtils.simpleStringToCalend(todayBean.getDate()) != null) {
                if (TimeUtils.simpleStringToCalend(todayBean.getDate()).after(mCreateCalender)) {
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
                float baseNum = 0;
                float maxNum = baseNum, minNum = baseNum;
                float tempMaxOffetValue = 0;
                for (CompareFundsBean bean : beanList) {
                    LineEntity lineEntity = new LineEntity();
                    lineEntity.setTitle(bean.getSymbol());
                    lineEntity.setLineColor(ColorTemplate.getDefaultColor(i));

                    List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
                    for (ComparePoint cPoint : bean.getChartlist()) {
                        LinePointEntity pointEntity = new LinePointEntity();
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

                    mCompareItemList.get(i).value = StringFromatUtils.get2PointPercent(value * 100);
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
                // setYTitle(dayNetValueList.get(0).getPercentageBegin(), getMaxOffetValue(object));
                if (null != lineEntityList) {
                    lineEntityList.removeAll(compareLinesList);
                }
                compareLinesList.clear();
                compareLinesList.addAll(object);
                setCompareLineList();
                mGridAdapter.notifyDataSetChanged();
            }

        }
    };

    private void showPickerDate() {

        DatePickerDialog dpg = null;
        if (isPickStartDate) {
            dpg = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar), mDateSetListener, cStart.get(Calendar.YEAR),
                    cStart.get(Calendar.MONTH), cStart.get(Calendar.DAY_OF_MONTH));
            dpg.setTitle(R.string.dialog_start_time_title);
        } else {
            dpg = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar), mDateSetListener, cEnd.get(Calendar.YEAR),
                    cEnd.get(Calendar.MONTH), cEnd.get(Calendar.DAY_OF_MONTH));
            dpg.setTitle(R.string.dialog_end_time_title);
        }
        dpg.show();
    }

    private void showBeforeCreateDayDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar));

        builder.setTitle(R.string.dialog_before_createday_title)
                .setItems(R.array.query_compare_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            // queryFromCreateDay();
                            // } else {
                            queryBeforeCreateMonth();
                        }
                        updateDayDisplay();
                        // setSelectBack(which);
                    }
                }).setNegativeButton(R.string.cancel, null).setCancelable(false).show();

    }

    // private void queryFromCreateDay() {
    // setStartTime(TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));
    // }

    private void queryBeforeCreateMonth() {
        cStart = TimeUtils.toCalendar(mCombinationBean.getCreateTime());
        cStart.add(Calendar.MONTH, -1);
        cEnd = TimeUtils.toCalendar(mCombinationBean.getCreateTime());
        // setStartTime(String.format(mDayFormat, cStart.get(Calendar.YEAR), (cStart.get(Calendar.MONTH) + 1),
        // cStart.get(Calendar.DAY_OF_MONTH)));
        // setEndTime(TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));

    }

    int noOfTimesCalled = 0;

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // if (noOfTimesCalled % 2 == 0) {
            // mYear = year;
            // mMonth = monthOfYear;
            // mDay = dayOfMonth;

            // String sbTime = String.format(mDayFormat, year, (monthOfYear + 1), dayOfMonth);
            if (isPickStartDate) {
                // cStart = Calendar.getInstance();

                cStart.set(year, monthOfYear, dayOfMonth);
                // if (isBeforeCreateDate(cStart)) {
                if (isBeforeMonthCreateDate(cStart)) {
                    showBeforeCreateDayDialog();
                }

            } else {
                cEnd.set(year, monthOfYear, dayOfMonth);
            }
            updateDayDisplay();
            // }
            // noOfTimesCalled++;
        }
    };

    // private boolean isBeforeCreateDate(Calendar cStart, Calendar cCreate) {
    // isBeforeCreateDate = cStart.before(cCreate);
    // return isBeforeCreateDate;
    //
    // }

    private boolean isBeforeMonthCreateDate(Calendar cStart) {
        Calendar beforeMonthCaleder = TimeUtils.toCalendar(mCombinationBean.getCreateTime());
        beforeMonthCaleder.add(Calendar.MONTH, -1);
        boolean isBeforeCreateDate = cStart.before(beforeMonthCaleder);
        return isBeforeCreateDate;

    }

    private void updateDayDisplay() {

        btnStartTime.setText(TimeUtils.getTimeString(cStart));
        btnEndTime.setText(TimeUtils.getTimeString(cEnd));
        String durTime = btnStartTime.getText() + " 一一 " + btnEndTime.getText();
        tvTimeDuration.setText(durTime);

    }

    private void updateSelectData(List<SelectStockBean> listStock) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbCompareIds = new StringBuilder();
        mCompareItemList.clear();
        for (SelectStockBean csBean : listStock) {
            CompareFundItem item = mGridAdapter.new CompareFundItem();
            item.name = csBean.name;
            // item.value = csBean.id + "";
            mCompareItemList.add(item);

            sb.append(csBean.name);
            sb.append(" ");
            sbCompareIds.append(csBean.id);
            sbCompareIds.append(",");

        }
        int lenght = sbCompareIds.length();
        mCompareIds = sbCompareIds.substring(0, lenght - 1);

        btnSelectFund.setText(sb);
        mGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_SELECT_FUND:
                    ArrayList<SelectStockBean> listStock = (ArrayList<SelectStockBean>) data
                            .getSerializableExtra("list_select");
                    if (null != listStock) {
                        selectStockList = listStock;
                        updateSelectData(listStock);
                    } else {

                    }
                    break;
            }
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onPauseFragment() {

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResumeFragment() {

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        compareListener.stopRequest(true);
        historyNetValueListener.stopRequest(true);
    }

}
