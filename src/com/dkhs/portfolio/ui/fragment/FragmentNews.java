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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;

/**
 * @ClassName FragmentNews
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-3 上午9:33:40
 * @version 1.0
 */
public class FragmentNews extends BaseFragment implements FragmentLifecycle {
	public static final String DATA = "mCombinationBean";
	private CombinationBean mCombinationBean;
	private LinearLayout comLayout;
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
        Bundle bunds = getArguments();
        if(null != bunds){
        	mCombinationBean = (CombinationBean) bunds.get(DATA);
        }
        System.out.println("================FragmentNews onCreate()======================");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_combination_news, null);
        initTabPage(view);
        System.out.println("================FragmentNews onCreateView()======================");
        return view;
    }

    private void initTabPage(View view) {
    	comLayout = (LinearLayout) view.findViewById(R.id.com_layout);
        String[] titleArray = getResources().getStringArray(R.array.detail_news_titles);
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        /*fragmentList.add(new FragmentDiscussFlow());
        fragmentList.add(new TestFragment());*/
        Fragment f1 = new FragmentNewsList();
        Bundle b1 = new Bundle();
        b1.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWS_GROUP_FOREACH);
        NewsforImpleEngine vo = new NewsforImpleEngine();
        vo.setPortfolioId(mCombinationBean.getId()+"");
        vo.setContentType("10");
        vo.setPageTitle("新闻正文");
        b1.putSerializable(FragmentNewsList.VO, vo);
        f1.setArguments(b1);
        //fragmentList.add(f1);
        Fragment f2 = new FragmentNewsList();
        Bundle b2 = new Bundle();
        b2.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWS_GROUP_FOREACH);
        NewsforImpleEngine vo2 = new NewsforImpleEngine();
        vo2.setPortfolioId(mCombinationBean.getId()+"");
        vo2.setContentType("20");
        vo2.setPageTitle("公告正文");
        b2.putSerializable(FragmentNewsList.VO, vo2);
        f2.setArguments(b2);
        fragmentList.add(f2);
        Fragment f3 = new FragmentNewsList();
        Bundle b3 = new Bundle();
        b3.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWS_GROUP_FOREACH);
        NewsforImpleEngine vo3 = new NewsforImpleEngine();
        vo3.setPortfolioId(mCombinationBean.getId()+"");
        vo3.setContentType("30");
        vo3.setPageTitle("研报正文");
        b3.putSerializable(FragmentNewsList.VO, vo3);
        f3.setArguments(b3);
        /*Fragment f3 = new FragmentreportOneList();
        vo = new NewsforImpleEngine();
        vo.setPortfolioId(mCombinationBean.getId());
        vo.setContentType("30");
        Bundle b3 = new Bundle();
        b3.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWS_GROUP_FOREACH);
        b3.putSerializable(FragmentNewsList.VO, vo);
        f3.setArguments(b3);*/
        fragmentList.add(f3);
        new FragmentSelectAdapter(getActivity(),titleArray,fragmentList,comLayout,getFragmentManager());
        /*ScrollViewPager pager = (ScrollViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new MyPagerFragmentAdapter(getChildFragmentManager(), fragmentList, titleArray));

        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(pager);*/

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

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onPauseFragment() {
        // TODO Auto-generated method stub

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
