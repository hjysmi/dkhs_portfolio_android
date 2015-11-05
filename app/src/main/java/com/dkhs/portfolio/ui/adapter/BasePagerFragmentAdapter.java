/**
 * @Title BasePagerFragmentAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-26 下午3:45:30
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName BasePagerFragmentAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-26 下午3:45:30
 */
public class BasePagerFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    /**
     * Tab标题
     */
    private static final String[] TITLE = new String[]{"基金", "股票", "组合"};

    public BasePagerFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2) {
        super(fm);
        this.fragmentList = fragmentList2;

    }

    @Override
    public Fragment getItem(int arg0) {

        return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position % TITLE.length];
    }
}
