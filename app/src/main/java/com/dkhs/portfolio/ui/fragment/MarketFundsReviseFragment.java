/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.eventbus.IDataUpdateListener;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;

import java.util.ArrayList;

/**
 * @author zjz
 * @version 1.0
 * @ClassName TabFundFragment
 * @Description TODO(基金tab Fragment)
 * @date 2015-2-7 上午11:03:26
 */
public class MarketFundsReviseFragment extends VisiableLoadFragment implements IDataUpdateListener, OnClickListener {

    public static final Integer[] PERCENT_TITLE_IDS = new Integer[]{R.string.percent_day,
            R.string.percent_year,
            R.string.percent_month,
            R.string.percent_season,
            R.string.percent_six_month,
            R.string.percent_tyear,
            R.string.office_tenure};


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        MarketFundsFragment fg = null;
        ArrayList<Fragment> fragments = new ArrayList<>();
        for(int i = 0;i<PERCENT_TITLE_IDS.length;i++){
            fg = MarketFundsFragment.getFragment(0);
            fragments.add(fg);
        }
        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getActivity().getSupportFragmentManager(),fragments);

        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }


    @Override
    public void dataUpdate(boolean isEmptyData) {

    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_revise_market_funds;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void requestData() {

    }

    class GoogleMusicAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragmentList;

        public GoogleMusicAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            mFragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
//            return ((mFragmentList == null || mFragmentList.size() < 0) ? null : mFragmentList.get(position));
            int index = position % PERCENT_TITLE_IDS.length;
            return TestFragment.newInstance(getActivity().getString(PERCENT_TITLE_IDS[index]));
        }



        @Override
        public CharSequence getPageTitle(int position) {
            int index = position % PERCENT_TITLE_IDS.length;
            return getActivity().getString(PERCENT_TITLE_IDS[index]);
        }

        @Override
        public int getCount() {
            return PERCENT_TITLE_IDS.length;
        }
    }
}
