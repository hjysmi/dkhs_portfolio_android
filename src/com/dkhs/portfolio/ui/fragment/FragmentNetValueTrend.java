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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.MyCombinationEngineImpl;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LineChart;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName FragmentNetValueTrend
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version 1.0
 */
public class FragmentNetValueTrend extends Fragment implements OnClickListener {

    private EditText etCombinName;
    private TextView tvCombinName;
    private TextView tvCombinDesc;
    private TextView tvCombinCreateTime;
    private TextView tvIncreaseValue;
    private TextView tvIncreaseRatio;
    private Button btnEditName;

    private CombinationBean mCombinationBean;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle arguments = getArguments();
        if (arguments != null) {
            handleArguments(arguments);
        }

        // handle intent extras
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }

    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = (CombinationBean) extras.getSerializable(CombinationDetailActivity.EXTRA_COMBINATION);

    }

    private void handleArguments(Bundle arguments) {
        // TODO Auto-generated method stub

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_netvalue_trend, null);
        etCombinName = (EditText) view.findViewById(R.id.et_combination_name);

        tvCombinDesc = (TextView) view.findViewById(R.id.tv_combination_desc);
        tvCombinCreateTime = (TextView) view.findViewById(R.id.tv_combination_time);
        tvCombinName = (TextView) view.findViewById(R.id.tv_combination_name);
        tvIncreaseRatio = (TextView) view.findViewById(R.id.tv_income_netvalue);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_history_netvalue);
        btnEditName = (Button) view.findViewById(R.id.btn_edit_combinname);
        btnEditName.setOnClickListener(this);
        initTabPage(view);
        setupViewData();
        return view;
    }

    private void setupViewData() {
        if (null != mCombinationBean) {
            tvCombinName.setText(mCombinationBean.getName());
            tvCombinDesc.setText(mCombinationBean.getDescription());
            tvCombinCreateTime.setText(TimeUtils.getSimpleFormatTime(mCombinationBean.getCreateTime()));
            tvIncreaseRatio.setText(StringFromatUtils.getPercentValue(mCombinationBean.getAddUpValue()));
            tvIncreaseValue.setText(StringFromatUtils.get4Point(mCombinationBean.getAddUpValue() / 100f));
        }
    }

    private void initTabPage(View view) {

        String[] titleArray = getResources().getStringArray(R.array.trend_title);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_TODAY));
        fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_SEVENDAY));
        fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_MONTH));
        fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_HISTORY));

        ScrollViewPager pager = (ScrollViewPager) view.findViewById(R.id.pager);
        pager.setCanScroll(false);
        pager.setAdapter(new MyPagerFragmentAdapter(getChildFragmentManager(), fragmentList, titleArray));

        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }

    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private String[] titleList;

        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2, String[] titleList) {
            super(fm);
            this.fragmentList = fragmentList2;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int arg0) {

            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (titleList.length > position) ? titleList[position] : "";
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_edit_combinname: {

                if (tvCombinName.getVisibility() == View.VISIBLE) {
                    btnEditName.setBackgroundResource(R.drawable.action_alias_ok);
//                    etCombinName.setHint(tvCombinName.getText());
                    etCombinName.setText(tvCombinName.getText());
                    tvCombinName.setVisibility(View.GONE);
                    etCombinName.setVisibility(View.VISIBLE);
                } else {
                    String nameText = etCombinName.getText().toString();
                    if (TextUtils.isEmpty(nameText)) {
                        // tvCombinName.setText(etCombinName.getText());
                        // Toast.makeText(getActivity(), R.string.tips_combination_name_null, Toast.LENGTH_LONG).show();
                    } else {
                        tvCombinName.setText(etCombinName.getText());
                        new MyCombinationEngineImpl().updateCombination(mCombinationBean.getId() + "", nameText, null);
                    }
                    tvCombinName.setVisibility(View.VISIBLE);
                    etCombinName.setVisibility(View.GONE);
                    btnEditName.setBackgroundResource(R.drawable.action_alias);
                }
            }

                break;

            default:
                break;
        }

    }
}
