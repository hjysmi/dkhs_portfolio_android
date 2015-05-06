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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.NewCombinationDetailActivity;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;
import com.dkhs.portfolio.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName FragmentNews
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午9:33:40
 * @version 1.0
 */
public class FragmentCombinationNews extends BaseFragment implements FragmentLifecycle {
    public static final String DATA = "mCombinationBean";
    private CombinationBean mCombinationBean;
    private LinearLayout comLayout;

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
        mCombinationBean = (CombinationBean) extras.getSerializable(NewCombinationDetailActivity.EXTRA_COMBINATION);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param view
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initTabPage(view);
    }

    private void initTabPage(View view) {
        comLayout = (LinearLayout) view.findViewById(R.id.com_layout);
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
    public void onPauseFragment() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onResumeFragment() {
        // TODO Auto-generated method stub

    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_combination_text);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public int setContentLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_combination_news;
    }
}
