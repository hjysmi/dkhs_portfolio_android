/**
 * @Title FragmentNews.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午9:33:40
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ClassName FragmentNews
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午9:33:40
 * @version 1.0
 */
public class FragmentNews extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combination_news, null);
        initTabPage(view);
        return view;
    }

    private void initTabPage(View view) {

        String[] titleArray = getResources().getStringArray(R.array.detail_news_titles);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        fragmentList.add(new FragmentDiscussFlow());
        fragmentList.add(new TestFragment());
        fragmentList.add(new TestFragment());

        ScrollViewPager pager = (ScrollViewPager) view.findViewById(R.id.pager);
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
}
