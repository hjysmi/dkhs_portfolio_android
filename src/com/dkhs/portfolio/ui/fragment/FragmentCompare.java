/**
 * @Title FragmentCompare.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午9:32:29
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
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.SelectFundActivity;
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

    private Button btnStartTime;
    private Button btnEndTime;
    private Button btnCompare;
    private Button btnSelectFund;
    private TextView tvTimeDuration;
    private TextView tvNoData;
    private TextView tvIncreaseValue;
    private View increaseView;

    private int mYear;
    private int mMonth;
    private int mDay;

    private boolean isPickStartDate;

    private CombinationBean mCombinationBean;

    private String strStartTime = "";
    private String strEndTime = "";
    private String mDayFormat = "%d-%02d-%02d";

    // 默认上证指数，沪深300的id
    private String mCompareIds = "106000082,106000232";

    private CompareEngine mCompareEngine;
    private Calendar mCurrentCalendar;

    private Calendar mCreateCalender;

    // private Date mSelectStartDate;

    private TrendChart maChartView;
    private List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
    private List<LineEntity> lineEntityList = new ArrayList<LineEntity>();

    private boolean isBeforeCreateDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCompareItemList = new ArrayList<CompareIndexAdapter.CompareFundItem>();

        mGridAdapter = new CompareIndexAdapter(getActivity(), mCompareItemList);

        mCurrentCalendar = Calendar.getInstance();
        mYear = mCurrentCalendar.get(Calendar.YEAR);
        mMonth = mCurrentCalendar.get(Calendar.MONTH);
        mDay = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);
        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

        mCompareEngine = new CompareEngine();
        setGridItemData();
    }

    private void setGridItemData() {
        CompareFundItem defalutItem1 = mGridAdapter.new CompareFundItem();
        defalutItem1.name = "沪深300";
        CompareFundItem defalutItem2 = mGridAdapter.new CompareFundItem();
        defalutItem2.name = "上证指数";
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
        tvNoData = (TextView) view.findViewById(R.id.tv_nodate);
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

        if (null != mCombinationBean) {
            setStartTime(TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));
            setEndTime(String.format(mDayFormat, mYear, (mMonth + 1), mDay));
            updateDayDisplay();
        }

    }

    private void setStartTime(String startDay) {
        strStartTime = startDay;
    }

    private void setEndTime(String endDay) {
        strEndTime = endDay;
    }

    private String getStartTime() {
        return strStartTime;
    }

    private String getEndTime() {
        return strEndTime;
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

        // machart.setLineData(lines);
        tvNoData.setVisibility(View.VISIBLE);
    }

    private List<LinePointEntity> initMA(int length) {
        List<LinePointEntity> MA5Values = new ArrayList<LinePointEntity>();
        NetValueEngine outer = new NetValueEngine(0);
        for (int i = 0; i < length; i++) {
            // MA5Values.add((float) new Random().nextInt(99));
            LinePointEntity bean = new LinePointEntity();
            bean.setDesc("2014-09-23");
            bean.setValue(new Random().nextFloat() * 100);
            MA5Values.add(bean);
        }
        return MA5Values;

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
                requestCompare();
            }

                break;
            case R.id.btn_select_fund: {
                Intent intent = new Intent(getActivity(), SelectFundActivity.class);
                // Intent intent = new Intent(getActivity(), SelectStockActivity.class);
                startActivityForResult(intent, REQUESTCODE_SELECT_FUND);
            }
                break;
            default:
                break;
        }

    }

    private void requestCompare() {
        lineEntityList.clear();
        new NetValueEngine(mCombinationBean.getId()).requeryDay(getStartTime(), getEndTime(), historyNetValueListener);
        mCompareEngine.compare(compareListener, mCompareIds, getStartTime(), getEndTime());

    }

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
                    tvNoData.setVisibility(View.GONE);
                    // int sizeLength = dayNetValueList.size();
                    setYTitle(dayNetValueList.get(0).getPercentageBegin(), getMaxOffetValue(object));
                    setHistoryPointTitle();
                    setXTitle(dayNetValueList);

                    LineEntity mCombinationLine = new LineEntity();
                    mCombinationLine.setTitle("我的组合");
                    mCombinationLine.setLineColor(getActivity().getResources().getColor(
                            ColorTemplate.MY_COMBINATION_LINE));
                    mCombinationLine.setLineData(lineDataList);
                    setLineData(mCombinationLine);

                }

                float increaseValue = (object.getBegin() - object.getEnd()) / object.getEnd();
                tvIncreaseValue.setText(StringFromatUtils.get4PointPercent(increaseValue * 100));
                if (increaseValue > 0) {
                    increaseView.setBackgroundColor(ColorTemplate.DEF_RED);
                } else {
                    increaseView.setBackgroundColor(ColorTemplate.DEF_GREEN);
                }
            }
        }

    };

    private void setLineData(LineEntity lineEntity) {
        lineEntityList.add(lineEntity);
        maChartView.setDrawDashLine(isBeforeCreateDate);
        maChartView.setLineData(lineEntityList);
    }

    private void setLineListsData(List<LineEntity> linesList) {
        System.out.println("setLineListsData :" + linesList.size());
        lineEntityList.addAll(linesList);
        maChartView.setDrawDashLine(isBeforeCreateDate);
        maChartView.setLineData(lineEntityList);
    }

    private void setHistoryPointTitle() {
        List<String> titles = new ArrayList<String>();
        titles.add("日期");
        titles.add("当前净值");
        maChartView.setPointTitleList(titles);
    }

    private void setXTitle(List<HistoryNetBean> dayNetValueList) {
        List<String> xtitle = new ArrayList<String>();
        String endDate = dayNetValueList.get(dayNetValueList.size() - 1).getDate();
        if (TextUtils.isEmpty(endDate)) {
            xtitle.add("");
        } else {
            xtitle.add(endDate);

        }
        xtitle.add(dayNetValueList.get(0).getDate());
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
    private float getMaxOffetValue(HistoryNetValue historyNetValue) {
        // lineDataList.clear();

        List<HistoryNetBean> historyNetList = historyNetValue.getChartlist();
        int dataLenght = historyNetList.size();
        float baseNum = historyNetList.get(0).getPercentageBegin();
        float maxNum = baseNum, minNum = baseNum;
        for (int i = 0; i <= dataLenght - 1; i++) {
            LinePointEntity pointEntity = new LinePointEntity();
            HistoryNetBean todayBean = historyNetList.get(i);
            float pointValue = todayBean.getPercentageBegin();
            System.out.println("");
            pointEntity.setDesc(todayBean.getDate());
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

        offetValue = maxNum > minNum ? maxNum : minNum;

        return offetValue;

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
                for (CompareFundsBean bean : beanList) {
                    LineEntity lineEntity = new LineEntity();
                    lineEntity.setTitle(bean.getSymbol());
                    lineEntity.setLineColor(ColorTemplate.getDefaultColor(i));

                    List<LinePointEntity> lineDataList = new ArrayList<LinePointEntity>();
                    for (ComparePoint cPoint : bean.getChartlist()) {
                        LinePointEntity pointEntity = new LinePointEntity();
                        pointEntity.setDesc(cPoint.getDate());
                        pointEntity.setValue(cPoint.getPercentage());
                        lineDataList.add(pointEntity);
                    }

                    lineEntity.setLineData(lineDataList);
                    linesList.add(lineEntity);

                    float value = (bean.getEnd() - bean.getBegin()) / bean.getBegin();

                    mCompareItemList.get(i).value = StringFromatUtils.get2PointPercent(value);
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
                setLineListsData(object);

                mGridAdapter.notifyDataSetChanged();
            }

        }
    };

    private void showPickerDate() {
        DatePickerDialog dpg = new DatePickerDialog(new ContextThemeWrapper(getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), mDateSetListener, mYear, mMonth, mDay);
        if (isPickStartDate) {

            dpg.setTitle(R.string.dialog_start_time_title);
        } else {

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
                            queryFromCreateDay();
                        } else {
                            queryBeforeCreateMonth();
                        }
                        updateDayDisplay();
                        // setSelectBack(which);
                    }
                }).setNegativeButton(R.string.cancel, null).setCancelable(false).show();

    }

    private void queryFromCreateDay() {
        setStartTime(TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));
    }

    private void queryBeforeCreateMonth() {

        setStartTime(String.format(mDayFormat, mCreateCalender.get(Calendar.YEAR),
                (mCreateCalender.get(Calendar.MONTH)), mCreateCalender.get(Calendar.DAY_OF_MONTH)));
        setEndTime(TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));

    }

    int noOfTimesCalled = 0;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if (noOfTimesCalled % 2 == 0) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                String sbTime = String.format(mDayFormat, mYear, (mMonth + 1), mDay);
                if (isPickStartDate) {
                    Calendar cStart = Calendar.getInstance();
                    cStart.set(mYear, mMonth, mDay); // NB: 2 means March, not February!

                    if (isBeforeCreateDate(cStart, mCreateCalender)) {
                        showBeforeCreateDayDialog();
                    } else {
                        setStartTime(sbTime.toString());
                    }

                } else {
                    setEndTime(sbTime.toString());
                }
                updateDayDisplay();
            }
            noOfTimesCalled++;
        }
    };

    private boolean isBeforeCreateDate(Calendar cStart, Calendar cCreate) {
        isBeforeCreateDate = cStart.before(cCreate);
        return isBeforeCreateDate;

    }

    private void updateDayDisplay() {

        btnStartTime.setText(getStartTime());
        btnEndTime.setText(getEndTime());
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

}
