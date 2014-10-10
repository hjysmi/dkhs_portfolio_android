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
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.engine.CompareEngine;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.BasicHttpListener;
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
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午9:32:29
 * @version 1.0
 */
public class FragmentCompare extends Fragment implements OnClickListener, FragmentLifecycle {

    private final int REQUESTCODE_SELECT_FUND = 900;

    private GridView mGridView;
    private CompareIndexAdapter mAdapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCompareItemList = new ArrayList<CompareIndexAdapter.CompareFundItem>();

        mAdapter = new CompareIndexAdapter(getActivity(), mCompareItemList);

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
        CompareFundItem defalutItem1 = mAdapter.new CompareFundItem();
        defalutItem1.name = "沪深300";
        CompareFundItem defalutItem2 = mAdapter.new CompareFundItem();
        defalutItem2.name = "上证指数";
        mCompareItemList.add(defalutItem1);
        mCompareItemList.add(defalutItem2);
        mAdapter.notifyDataSetChanged();
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);
        mCreateCalender = TimeUtils.toCalendar(mCombinationBean.getCreateTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compare, null);
        initView(view);
        TrendChart maChartView = (TrendChart) view.findViewById(R.id.machart);
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
        mGridView.setAdapter(mAdapter);
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

                mAdapter.setItemHeight((int) (columnWidth));
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
        List<LineEntity> lines = new ArrayList<LineEntity>();

        LineEntity MA1 = new LineEntity();
        MA1.setLineColor(getResources().getColor(ColorTemplate.DEFAULTCOLORS[0]));
        MA1.setLineData(initMA(72));
        MA1.setTitle("线条一");
        LineEntity MA2 = new LineEntity();
        MA2.setLineColor(getResources().getColor(ColorTemplate.DEFAULTCOLORS[1]));
        MA2.setLineData(initMA(72));
        MA2.setTitle("线条二");
        LineEntity MA3 = new LineEntity();
        MA3.setLineColor(getResources().getColor(ColorTemplate.DEFAULTCOLORS[2]));
        MA3.setLineData(initMA(72));
        MA3.setTitle("线条三");
        LineEntity MA4 = new LineEntity();
        MA4.setLineColor(getResources().getColor(ColorTemplate.DEFAULTCOLORS[3]));
        MA4.setLineData(initMA(72));
        MA4.setTitle("线条四");
        LineEntity MA5 = new LineEntity();
        MA5.setLineColor(getResources().getColor(ColorTemplate.DEFAULTCOLORS[4]));
        MA5.setLineData(initMA(72));
        MA5.setTitle("线条五");

        lines.add(MA1);
        lines.add(MA2);
        lines.add(MA3);
        lines.add(MA4);
        lines.add(MA5);

        List<String> linetitle = new ArrayList<String>();

        List<String> ytitle = new ArrayList<String>();
        ytitle.add("1.1051");
        ytitle.add("1.0532");
        ytitle.add("1.0001");
        ytitle.add("0.0000");
        ytitle.add("1.0522");
        ytitle.add("1.1031");

        List<String> xtitle = new ArrayList<String>();
        xtitle.add("08-08");
        // xtitle.add("10:30");
        // xtitle.add("11:30");
        // xtitle.add("14:00");
        xtitle.add("08-10");

        machart.setDisplayBorder(false);
        machart.setSmallLine();
        machart.setDashLineLenght(20);
        machart.setLatitudeColor(Color.LTGRAY);

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);
        machart.setBorderColor(Color.TRANSPARENT);
        machart.setBackgroudColor(Color.WHITE);
        machart.setAxisMarginTop(10);
        machart.setAxisMarginLeft(20);
        machart.setAxisMarginRight(10);
        machart.setAxisYTitles(ytitle);
        machart.setAxisXTitles(xtitle);
        machart.setLongtitudeFontSize(10);
        machart.setLongtitudeFontColor(Color.GRAY);
        machart.setDisplayAxisYTitleColor(true);
        machart.setLatitudeColor(Color.GRAY);
        machart.setLatitudeFontColor(Color.GRAY);
        machart.setLongitudeColor(Color.GRAY);
        machart.setMaxValue(120);
        machart.setMinValue(0);
        machart.setMaxPointNum(72);
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
                Toast.makeText(getActivity(), "业绩比较查询", Toast.LENGTH_SHORT).show();
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

                // List<HistoryNetBean> dayNetValueList = object.getChartlist();
                // if (dayNetValueList != null && dayNetValueList.size() < 7) {
                // tvNoData.setVisibility(View.VISIBLE);
                // } else {
                // int sizeLength = dayNetValueList.size();
                // setYTitle(object.getEnd(), getMaxOffetValue(object));
                // setHistoryPointTitle();
                // setLineData(lineDataList);
                // String strLeft = getString(R.string.time_start, dayNetValueList.get(sizeLength - 1).getDate());
                // String strRight = getString(R.string.time_end, dayNetValueList.get(0).getDate());
                // tvTimeLeft.setText(strLeft);
                //
                // tvTimeRight.setText(strRight);
                //
                // setXTitle(dayNetValueList);
                //
                // }
                //
                // tvNetValue.setText(StringFromatUtils.get4Point(object.getBegin()));
                // float addupValue = object.getEnd() - object.getBegin();
                // tvUpValue.setText(StringFromatUtils.get4Point(object.getEnd()));
                // // fl
                // tvIncreaseValue.setText(StringFromatUtils.get4Point(addupValue));

                float increaseValue = (object.getEnd() - object.getBegin()) / object.getBegin();
                tvIncreaseValue.setText(StringFromatUtils.get4PointPercent(increaseValue * 100));
                if (increaseValue > 0) {
                    increaseView.setBackgroundColor(ColorTemplate.DEF_RED);
                } else {
                    increaseView.setBackgroundColor(ColorTemplate.DEF_GREEN);
                }
            }
        }

    };

    ParseHttpListener compareListener = new ParseHttpListener<String>() {

        @Override
        protected String parseDateTask(String jsonData) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void afterParseData(String object) {
            // TODO Auto-generated method stub

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
                }).setNegativeButton(R.string.cancel, null).show();

    }

    private void queryFromCreateDay() {
        setStartTime(TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));
    }

    private void queryBeforeCreateMonth() {

        setStartTime(String.format(mDayFormat, mCreateCalender.get(Calendar.YEAR),
                (mCreateCalender.get(Calendar.MONTH)), mCreateCalender.get(Calendar.DAY_OF_MONTH)));
        setEndTime(TimeUtils.getSimpleDay(mCombinationBean.getCreateTime()));

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
    };

    private boolean isBeforeCreateDate(Calendar cStart, Calendar cCreate) {

        return cStart.before(cCreate);

    }

    private void updateDayDisplay() {

        btnStartTime.setText(getStartTime());
        btnEndTime.setText(getEndTime());
        String durTime = btnStartTime.getText() + " ---- " + btnEndTime.getText();
        tvTimeDuration.setText(durTime);

    }

    private void updateSelectData(List<SelectStockBean> listStock) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbCompareIds = new StringBuilder();
        for (SelectStockBean csBean : listStock) {
            sb.append(csBean.name);
            sb.append(" ");
            sbCompareIds.append(csBean.id);
            sbCompareIds.append(",");
        }
        mCompareIds = sbCompareIds.toString();
        btnSelectFund.setText(sb);
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
        // TODO Auto-generated method stub

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onResumeFragment() {
        // TODO Auto-generated method stub

    }

}
