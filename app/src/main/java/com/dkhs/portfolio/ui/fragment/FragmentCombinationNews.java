/**
 * @Title FragmentNews.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-3 上午9:33:40
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FragmentNews
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-3 上午9:33:40
 */
public class FragmentCombinationNews extends BaseFragment {
    public static final String DATA = "mCombinationBean";
    private CombinationBean mCombinationBean;

    public static FragmentCombinationNews newInstance() {
        FragmentCombinationNews fragment = new FragmentCombinationNews();

        return fragment;
    }

    public FragmentCombinationNews() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
    }

    private void handleExtras(Bundle extras) {
        mCombinationBean = Parcels.unwrap(extras.getParcelable(CombinationDetailActivity.EXTRA_COMBINATION));

    }

    /**
     * @param view
     * @param savedInstanceState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initTabPage(view);
    }

    private void initTabPage(View view) {
        LinearLayout comLayout = (LinearLayout) view.findViewById(R.id.com_layout);
        String[] titleArray = getResources().getStringArray(R.array.detail_news_titles);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        NewsforModel vo2 = new NewsforModel();
        vo2.setPortfolioId(mCombinationBean.getId() + "");
        vo2.setContentType("20");

        Fragment f2 = ReportListForAllFragment.getFragment(vo2, OpitionNewsEngineImple.NEWS_GROUP_FOREACH);
        fragmentList.add(f2);

        NewsforModel vo3 = new NewsforModel();
        vo3.setPortfolioId(mCombinationBean.getId() + "");
        vo3.setContentType("30");

        vo3.setPageTitle("研报正文");

        Fragment f3 = ReportListForAllFragment.getFragment(vo3, OpitionNewsEngineImple.NEWS_GROUP_FOREACH);
        fragmentList.add(f3);
        new FragmentSelectAdapter(getActivity(), titleArray, fragmentList, comLayout, getFragmentManager());

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
    public int setContentLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_combination_news;
    }
    @Override
    public int getPageStatisticsStringId() {
        return R.string.statistics_combination_news;
    }
}
