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
import java.util.List;
import java.util.Random;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.MAChart;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.lidroid.xutils.util.LogUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @ClassName TrendChartFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午10:32:39
 * @version 1.0
 */
public class TrendChartFragment extends Fragment {
    public static final String ARGUMENT_TREND_TYPE = "trend_type";

    // private static final String SAVED_LIST_POSITION = "list_position";

    public static final String TREND_TYPE_TODAY = "trend_today";
    public static final String TREND_TYPE_SEVENDAY = "trend_seven_day";
    public static final String TREND_TYPE_MONTH = "trend_month";
    public static final String TREND_TYPE_HISTORY = "trend_history";

    private String trendType;
    private TextView tvTimeLeft;
    private TextView tvTimeRight;

    // public static final String TREND_TYPE_TODAY="trend_today";
    public static TrendChartFragment newInstance(String trendType) {
        TrendChartFragment fragment = new TrendChartFragment();

        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TREND_TYPE, trendType);
        fragment.setArguments(arguments);

        return fragment;
    }

    public TrendChartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handle fragment arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // restore saved state
        if (savedInstanceState != null) {
            handleSavedInstanceState(savedInstanceState);
        }

        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(ARGUMENT_TREND_TYPE);
    }

    private void handleSavedInstanceState(Bundle savedInstanceState) {
    }

    private void handleExtras(Bundle extras) {
        // TODO
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param activity
     * @return
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trend_chart, null);
        MAChart maChartView = (MAChart) view.findViewById(R.id.machart);
        initMaChart(maChartView);
        initView(view);
        setupViewData();
        return view;
    }

    private void initView(View view) {
        tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);

    }

    private void setupViewData() {
        String strRight = "";
        String strLeft = "";
        if (trendType.equals(TREND_TYPE_TODAY)) {
            strLeft = "2014-09-04";
            strRight = "14:28";
        } else if (trendType.equalsIgnoreCase(TREND_TYPE_SEVENDAY) || trendType.equalsIgnoreCase(TREND_TYPE_MONTH)) {
            strLeft = getString(R.string.time_start, "2014-07-06");
            strRight = getString(R.string.time_end, "2014-07-13");
        } else if (trendType.equalsIgnoreCase(TREND_TYPE_HISTORY)) {
            strLeft = getString(R.string.time_start, "2014-05-05");
            strRight = getString(R.string.to_now);
        }

        tvTimeLeft.setText(strLeft);
        tvTimeRight.setText(strRight);

    }

    private void initMaChart(MAChart machart) {

        machart.setAxisXColor(Color.LTGRAY);
        machart.setAxisYColor(Color.LTGRAY);
        List<LineEntity> lines = new ArrayList<LineEntity>();

        LineEntity MA5 = new LineEntity();
        // MA5.setTitle("MA5");
        // MA5.setLineColor(ColorTemplate.getRaddomColor())
        MA5.setLineColor(getActivity().getResources().getColor(ColorTemplate.MY_COMBINATION_LINE));
        MA5.setLineData(initMA(new Random().nextInt(72)));
        lines.add(MA5);
        machart.setLineData(lines);
        machart.setDisplayBorder(false);
        // machart.setDrawXBorke(true);

        List<String> ytitle = new ArrayList<String>();
        ytitle.add("1.1051");
        ytitle.add("1.0532");
        ytitle.add("1.0001");
        ytitle.add("1.0001");
        ytitle.add("1.0522");
        ytitle.add("1.1031");

        List<String> xtitle = new ArrayList<String>();
        xtitle.add("9:30");
        // xtitle.add("10:30");
        // xtitle.add("11:30");
        // xtitle.add("14:00");
        xtitle.add("15:00");

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
        // machart.setFill(true);
        // machart.setFillLineIndex(2);
    }

    private List<Float> initMA(int length) {
        List<Float> MA5Values = new ArrayList<Float>();
        for (int i = 0; i < length; i++) {
            // MA5Values.add((float) new Random().nextInt(99));
            MA5Values.add(new Random().nextFloat() * 100);
        }
        return MA5Values;

    }
}
