/**
 * @Title FragmentSwitchChart.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-11 上午9:52:31
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.dkhs.portfolio.R;

/**
 * @ClassName FragmentSwitchChart
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-11 上午9:52:31
 * @version 1.0
 */
public class FragmentSwitchChart extends Fragment {

    private String trendType;
    private Switch swChart;
    private View chartView;

    private Fragment mFragmentChart;
    private Fragment mFragmentReport;

    public static FragmentSwitchChart newInstance(String trendType) {
        FragmentSwitchChart fragment = new FragmentSwitchChart();

        Bundle arguments = new Bundle();
        arguments.putString(TrendChartFragment.ARGUMENT_TREND_TYPE, trendType);
        fragment.setArguments(arguments);

        return fragment;
    }

    public FragmentSwitchChart() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(TrendChartFragment.ARGUMENT_TREND_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switch_chart, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        swChart = (Switch) view.findViewById(R.id.switch_chart);
        // chartView = view.findViewById(id)
        if (trendType.equalsIgnoreCase(TrendChartFragment.TREND_TYPE_TODAY)) {
            swChart.setVisibility(View.GONE);
        }
        replaceChartView();

        swChart.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // System.out.println("isChecked :" + isChecked + " text:" + buttonView.getText());
                if (isChecked) {
                    // 选中时 do some thing
                    // statusText.setText("开");
                    replaceReportView();
                } else {
                    replaceChartView();
                    // 非选中时 do some thing
                    // statusText.setText("关");
                }

            }
        });
    }

    private void replaceChartView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        if (mFragmentChart == null) {
            mFragmentChart = TrendChartFragment.newInstance(trendType);
        }
        // replaceContentView(mFragment, R.id.btn_trend + "");
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.chart_content, mFragmentChart);
        ft.commit();
    }

    private void replaceReportView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        if (mFragmentReport == null) {
            mFragmentReport = FragmentReportForm.newInstance(trendType);
        }
        // replaceContentView(mFragment, R.id.btn_trend + "");
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.chart_content, mFragmentReport);
        ft.commit();
    }

}
