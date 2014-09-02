/**
 * @Title FragmentNetValueTrend.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LineChart;
import com.dkhs.portfolio.ui.widget.MAChart;
import com.dkhs.portfolio.utils.ColorTemplate;

/**
 * @ClassName FragmentNetValueTrend
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version 1.0
 */
public class FragmentNetValueTrend extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_netvalue_trend, null);
        MAChart maChartView = (MAChart) view.findViewById(R.id.machart);
        initMaChart(maChartView);
        return view;
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
