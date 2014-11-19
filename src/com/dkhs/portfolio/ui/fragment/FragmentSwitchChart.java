/**
 * @Title FragmentSwitchChart.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-11 上午9:52:31
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.DKHSSwitch;
import com.dkhs.portfolio.utils.PromptManager;

/**
 * @ClassName FragmentSwitchChart
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-11 上午9:52:31
 * @version 1.0
 */
public class FragmentSwitchChart extends Fragment {

    private String trendType;
    private DKHSSwitch swChart;
    private View chartView;
    // private View viewDashLineTip;

    private TrendChartFragment mFragmentChart;
    private FragmentReportForm mFragmentReport;

    // private Handler mHandler = new Handler() {
    // public void handleMessage(android.os.Message msg) {
    // boolean isShowTip = (Boolean) msg.obj;
    // if (isShowTip) {
    // viewDashLineTip.setVisibility(View.VISIBLE);
    // } else {
    // viewDashLineTip.setVisibility(View.GONE);
    // }
    // };
    // };

    private boolean isFromOrder;

    public static FragmentSwitchChart newInstance(String trendType, boolean isFromOrder) {
        FragmentSwitchChart fragment = new FragmentSwitchChart();

        Bundle arguments = new Bundle();
        arguments.putString(TrendChartFragment.ARGUMENT_TREND_TYPE, trendType);
        arguments.putBoolean("key_isfrom_oder", isFromOrder);
        fragment.setArguments(arguments);

        return fragment;
    }

    public FragmentSwitchChart() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTheme(R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }
    }

    private void handleArguments(Bundle arguments) {
        trendType = arguments.getString(TrendChartFragment.ARGUMENT_TREND_TYPE);
        isFromOrder = arguments.getBoolean("key_isfrom_oder");
        // mFragmentChart = TrendChartFragment.newInstance(trendType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trend_and_report, null);
        initView(view);
        return view;
    }

    // One controller for all.
    View.OnClickListener switchClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            DKHSSwitch switchButton = (DKHSSwitch) v;
            if (switchButton.isChecked()) {
                replaceChartView();
            } else {
                replaceReportView();

            }
        }
    };

    private final Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            replaceChartView();
        };
    };
    private Runnable runPager;

    private void initView(View view) {
        swChart = (DKHSSwitch) view.findViewById(R.id.switch_chart);
        // chartView = view.findViewById(id)
        // if (trendType.equalsIgnoreCase(TrendChartFragment.TREND_TYPE_TODAY)) {
        // swChart.setVisibility(View.GONE);
        // }
        //
        // runPager = new Runnable() {
        //
        // @Override
        // public void run() {
        // replaceChartView();
        // // replaceReportView();
        // }
        // };
        handler.sendEmptyMessageDelayed(77, 300);

        swChart.setOnClickListener(switchClickListener);

        // swChart.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // // System.out.println("isChecked :" + isChecked + " text:" + buttonView.getText());
        // if (isChecked) {
        // // 选中时 do some thing
        // // statusText.setText("开");
        // replaceReportView();
        // } else {
        // replaceChartView();
        // // 非选中时 do some thing
        // // statusText.setText("关");
        // }
        //
        // }
        // });
    }

    public void setSelectType(String type) {
        this.trendType = type;
        mFragmentChart.setSelectType(type);
        if (!TextUtils.isEmpty(trendType) && !isFromOrder) {

            if (trendType.equalsIgnoreCase(TrendChartFragment.TREND_TYPE_TODAY)) {
                swChart.setVisibility(View.GONE);
                if(mFragmentReport!=null){
                    replaceChartView();
                }
            } else {
                swChart.setVisibility(View.VISIBLE);
                if(mFragmentReport!=null){
                    mFragmentReport.setTrendType(type);
                }
            }

        }
        

        // if (null != mMaChart) {
        // PromptManager.showProgressDialog(getActivity(), "");
        // drawCharHandler.sendEmptyMessageDelayed(777, 1000);
        // // updateView();
        // }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        handler.removeCallbacks(runPager);
    }

    private void replaceChartView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (mFragmentChart == null) {

            if (mFragmentChart == null) {
                mFragmentChart = TrendChartFragment.newInstance(trendType);
            }

            mFragmentChart.setUpdateHandler(updHandler);
            // mFragmentChart.setTipshowHandler(mHandler);

            // replaceContentView(mFragment, R.id.btn_trend + "");
            // ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.replace(R.id.rl_chart, mFragmentChart);
        } else {
            ft.show(mFragmentChart);
        }
        if (null != mFragmentReport) {
            ft.hide(mFragmentReport);
        }
        ft.commit();
    }

    private void replaceReportView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        if (mFragmentReport == null) {
            mFragmentReport = FragmentReportForm.newInstance(trendType);
            ft.replace(R.id.rl_report, mFragmentReport);
        } else {
            mFragmentReport.setTrendType(trendType);
            ft.show(mFragmentReport);
        }
        if (null != mFragmentChart) {
            ft.hide(mFragmentChart);
        }
        ft.commit();

        // replaceContentView(mFragment, R.id.btn_trend + "");
        // ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("setUserVisibleHint:" + isVisibleToUser);
        // if (isVisibleToUser) {
        // 相当于Fragment的onResume
        if (null != mFragmentChart) {

            mFragmentChart.setUserVisibleHint(isVisibleToUser);

            // } else {
            // mFragmentChart.setUserVisibleHint(isVisibleToUser);
            // dataHandler.removeCallbacks(runnable);// 关闭定时器处理
            // 相当于Fragment的onPause
            // }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Handler updHandler;

    public void setUpdateHandler(Handler updateHandler) {

        this.updHandler = updateHandler;

    }

}
