/**
 * @Title FundsOrderActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午1:56:21
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.engine.FundsOrderEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.ui.adapter.BasePagerFragmentAdapter;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FundsOrderActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-29 下午1:56:21
 */
public class MarketCombinationFragment extends BaseFragment {

    private HScrollTitleView hsTitle;
    private ScrollViewPager pager;
    public static List<CombinationBean> mVisitorData = new ArrayList<CombinationBean>();
    private int titleIndex = 0;
    @Override
    public int setContentLayoutId() {
        return R.layout.activity_funds_order;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }



    /**
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void initViews(View  view) {

        hsTitle = (HScrollTitleView) view.findViewById(R.id.hs_title);
        hsTitle.setTitleList(getResources().getStringArray(R.array.combination_order));
        hsTitle.setSelectPositionListener(titleSelectPostion);

        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        fragmentList.add(CombinationRankFragment.getFragment(FundsOrderEngineImpl.ORDER_DAY));
        fragmentList.add(CombinationRankFragment.getFragment(FundsOrderEngineImpl.ORDER_WEEK));
        fragmentList.add(CombinationRankFragment.getFragment(FundsOrderEngineImpl.ORDER_MONTH));
        fragmentList.add(CombinationRankFragment.getFragment(FundsOrderEngineImpl.ORDER_ALL));

        pager = (ScrollViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(new BasePagerFragmentAdapter(getChildFragmentManager(), fragmentList));
        pager.setOnPageChangeListener(pageChangeListener);
        pager.setOffscreenPageLimit(1);
        pager.setCurrentItem(titleIndex);

    }

    private boolean isFromTitle;
    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                pager.setCurrentItem(position);
                // isFromTitle = true;
            }
        }
    };



    OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            System.out.println("onPageSelected:" + arg0 + "isFromTitle:" + isFromTitle);
            // if (!isFromTitle) {
            hsTitle.setSelectIndex(arg0);
            // }
            // isFromTitle = false;

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPause(getActivity());
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onResume(getActivity());
        if (!PortfolioApplication.hasUserLogin()) {
            loadVisitorCombinationList();
        }
    }

    private void loadVisitorCombinationList() {
        mVisitorData = new VisitorDataEngine().getCombinationBySort();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (null != mVisitorData) {
            mVisitorData = null;
        }
    }

}
