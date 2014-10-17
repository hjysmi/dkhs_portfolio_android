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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName FragmentNetValueTrend
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-1 下午1:52:54
 * @version 1.0
 */
public class FragmentNetValueTrend extends Fragment implements OnClickListener, FragmentLifecycle {

    // private EditText etCombinName;
    private TextView tvCombinName;
    private TextView tvCombinDesc;
    private TextView tvCombinCreateTime;
    private TextView tvIncreaseValue;
    private TextView tvIncreaseRatio;
    private View viewNetvalueHead;
    private ImageView ivUpDownIcon;
    // private Button btnEditName;

    private CombinationBean mCombinationBean;

    MyPagerFragmentAdapter mPagerAdapter;

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
        // etCombinName = (EditText) view.findViewById(R.id.et_combination_name);
        ivUpDownIcon = (ImageView) view.findViewById(R.id.tv_combination_image_uporlow);
        tvCombinDesc = (TextView) view.findViewById(R.id.tv_combination_desc);
        tvCombinCreateTime = (TextView) view.findViewById(R.id.tv_combination_time);
        tvCombinName = (TextView) view.findViewById(R.id.tv_combination_name);
        tvIncreaseRatio = (TextView) view.findViewById(R.id.tv_income_netvalue);
        tvIncreaseValue = (TextView) view.findViewById(R.id.tv_history_netvalue);
        viewNetvalueHead = view.findViewById(R.id.tv_combination_layout);
        // btnEditName = (Button) view.findViewById(R.id.btn_edit_combinname);
        // btnEditName.setOnClickListener(this);
        initTabPage(view);
        setupViewData();
        return view;
    }

    private void setupViewData() {
        if (null != mCombinationBean) {
            // System.out.println("mCombinationBean name:" + mCombinationBean.getName());
            tvCombinName.setText(mCombinationBean.getName());
            tvCombinDesc.setText(getString(R.string.descrition_format, mCombinationBean.getDescription()));
            tvCombinCreateTime.setText(getString(R.string.create_time_format,
                    TimeUtils.getSimpleFormatTime(mCombinationBean.getCreateTime())));
            // tvIncreaseRatio.setText(StringFromatUtils.getPercentValue(mCombinationBean.getAddUpValue()));
            // tvIncreaseValue.setText(StringFromatUtils.get4Point(1 + mCombinationBean.getAddUpValue()));
            // Message msg = updateHandler.obtainMessage();
            // msg.obj = 1 + mCombinationBean.getAddUpValue();
            // updateHandler.sendMessage(msg);
        }
    }

    Handler updateHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            System.out.println("hand update message");
            float netValue = (Float) msg.obj;
            if (netValue > 1) {
                ivUpDownIcon.setImageResource(R.drawable.ic_combination_up);
                viewNetvalueHead.setBackgroundResource(R.color.red);
            } else if (netValue < 1) {
                ivUpDownIcon.setImageResource(R.drawable.ic_combination_down);
                viewNetvalueHead.setBackgroundResource(R.color.green);

            } else {
                ivUpDownIcon.setImageDrawable(null);
                viewNetvalueHead.setBackgroundResource(R.color.red);
            }
            tvIncreaseValue.setText(StringFromatUtils.get4Point(netValue));
            tvIncreaseRatio.setText(StringFromatUtils.get2PointPercent((netValue - 1) * 100));
        };
    };

    private void initTabPage(View view) {

        String[] titleArray = getResources().getStringArray(R.array.trend_title);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        FragmentSwitchChart todayFragment = FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_TODAY);
        todayFragment.setUpdateHandler(updateHandler);
        fragmentList.add(todayFragment);
        fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_SEVENDAY));
        fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_MONTH));
        fragmentList.add(FragmentSwitchChart.newInstance(TrendChartFragment.TREND_TYPE_HISTORY));

        ScrollViewPager mViewPager = (ScrollViewPager) view.findViewById(R.id.pager);
        mViewPager.setCanScroll(false);
        mPagerAdapter = new MyPagerFragmentAdapter(getChildFragmentManager(), fragmentList, titleArray);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(pageChangeListener);

        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);

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

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        int currentPosition = 0;

        @Override
        public void onPageSelected(int newPosition) {

            Fragment fragmentToShow = (Fragment) mPagerAdapter.getItem(newPosition);
            fragmentToShow.setUserVisibleHint(true);

            Fragment fragmentToHide = (Fragment) mPagerAdapter.getItem(currentPosition);
            fragmentToHide.setUserVisibleHint(false);
            currentPosition = newPosition;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // switch (id) {
        // case R.id.btn_edit_combinname: {

        // if (tvCombinName.getVisibility() == View.VISIBLE) {
        // btnEditName.setBackgroundResource(R.drawable.action_alias_ok);
        // // etCombinName.setHint(tvCombinName.getText());
        // etCombinName.setText(tvCombinName.getText());
        // tvCombinName.setVisibility(View.GONE);
        // etCombinName.setVisibility(View.VISIBLE);
        // } else {
        // String nameText = etCombinName.getText().toString();
        // if (TextUtils.isEmpty(nameText)) {
        // // tvCombinName.setText(etCombinName.getText());
        // // Toast.makeText(getActivity(), R.string.tips_combination_name_null, Toast.LENGTH_LONG).show();
        // } else {
        // tvCombinName.setText(etCombinName.getText());
        // new MyCombinationEngineImpl().updateCombination(mCombinationBean.getId() + "", nameText, null);
        // }
        // tvCombinName.setVisibility(View.VISIBLE);
        // etCombinName.setVisibility(View.GONE);
        // btnEditName.setBackgroundResource(R.drawable.action_alias);
        // }
        // }
        //
        // break;
        //
        // default:
        // break;
        // }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onPauseFragment() {
        System.out.println("Fragment net value trend onPauseFragment（）");

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
