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

import android.app.Activity;
import android.app.DatePickerDialog;
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

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.engine.NetValueEngine.TodayNetBean;
import com.dkhs.portfolio.ui.SelectFundActivity;
import com.dkhs.portfolio.ui.adapter.CompareIndexAdapter;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePointEntity;
import com.dkhs.portfolio.ui.widget.MAChart;
import com.dkhs.portfolio.utils.ColorTemplate;

/**
 * @ClassName FragmentCompare
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午9:32:29
 * @version 1.0
 */
public class FragmentCompare extends Fragment implements OnClickListener {

    private final int REQUESTCODE_SELECT_FUND = 900;

    private GridView mGridView;
    private CompareIndexAdapter mAdapter;

    private Button btnStartTime;
    private Button btnEndTime;
    private Button btnSelectFund;
    private TextView tvTimeDuration;

    private int mYear;
    private int mMonth;
    private int mDay;

    private boolean isPickStartDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mAdapter = new CompareIndexAdapter(getActivity());
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compare, null);
        initView(view);
        MAChart maChartView = (MAChart) view.findViewById(R.id.machart);
        initMaChart(maChartView);
        return view;
    }

    private void initView(View view) {

        btnStartTime = (Button) view.findViewById(R.id.tv_compare_ftime);
        btnEndTime = (Button) view.findViewById(R.id.tv_compare_ttime);
        btnSelectFund = (Button) view.findViewById(R.id.btn_select_fund);
        tvTimeDuration = (TextView) view.findViewById(R.id.tv_addup_date);

        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);
        btnSelectFund.setOnClickListener(this);

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

    }

    private void initMaChart(MAChart machart) {

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

        machart.setLineData(lines);
        machart.setDisplayBorder(false);

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

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    private String strStartTime = "";
    private String strEndTime = "";

    private void updateDisplay() {
        String strMonth = String.format("%02d", (mMonth + 1));
        String strDay = String.format("%02d", mDay);
        StringBuilder sbTime = new StringBuilder().append(mYear).append("-").append(strMonth).append("-")
                .append(strDay);
        if (isPickStartDate) {

            btnStartTime.setText(sbTime);
            strStartTime = sbTime.toString();

        } else {
            btnEndTime.setText(sbTime);
            strEndTime = sbTime.toString();
        }
        updateDurationText();
    }

    private void updateDurationText() {
        String durTime = strStartTime + " ---- " + strEndTime;
        tvTimeDuration.setText(durTime);

    }

    private void updateSelectData(List<ConStockBean> listStock) {
        StringBuilder sb = new StringBuilder();
        for (ConStockBean csBean : listStock) {
            sb.append(csBean.getName());
            sb.append(" ");
        }
        btnSelectFund.setText(sb);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            Bundle b = data.getExtras(); // data为B中回传的Intent
            switch (requestCode) {
                case REQUESTCODE_SELECT_FUND:
                    ArrayList<ConStockBean> listStock = (ArrayList<ConStockBean>) data
                            .getSerializableExtra("list_select");
                    if (null != listStock) {
                        updateSelectData(listStock);
                    } else {

                    }
                    break;
            }
        }
    }

}
