package com.dkhs.portfolio.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by zjz on 2015/5/14.
 */
public class PagerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    // private String[] titleList;
    public PagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList2) {
        super(fm);
        this.fragmentList = fragmentList2;
        // this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // return (titleList.length > position) ? titleList[position] : "";
        return "";
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}
