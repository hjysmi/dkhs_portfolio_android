/**
 * @Title FundsOrderActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午1:56:21
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.FundsOrderEngineImpl;
import com.dkhs.portfolio.ui.fragment.FundsOrderFragment;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;

/**
 * @ClassName FundsOrderActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午1:56:21
 * @version 1.0
 */
public class FundsOrderActivity extends ModelAcitivity {

    private HScrollTitleView hsTitle;
    private ScrollViewPager pager;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_funds_order);
        setTitle(R.string.fund_order);
        initViews();
        // replaceDataList();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initViews() {

        hsTitle = (HScrollTitleView) findViewById(R.id.hs_title);
        hsTitle.setTitleList(getResources().getStringArray(R.array.combination_order));
        hsTitle.setSelectPositionListener(titleSelectPostion);

        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        fragmentList.add(FundsOrderFragment.getFragment(FundsOrderEngineImpl.ORDER_WEEK));
        fragmentList.add(FundsOrderFragment.getFragment(FundsOrderEngineImpl.ORDER_MONTH));
        fragmentList.add(FundsOrderFragment.getFragment(FundsOrderEngineImpl.ORDER_SEASON));

        pager = (ScrollViewPager) findViewById(R.id.pager);
        pager.setAdapter(new OrderFragmentAdapter(getSupportFragmentManager(), fragmentList));
        pager.setOnPageChangeListener(pageChangeListener);
        pager.setOffscreenPageLimit(3);

    }

    ISelectPostionListener titleSelectPostion = new ISelectPostionListener() {

        @Override
        public void onSelectPosition(int position) {
            if (null != pager) {
                pager.setCurrentItem(position);
            }
        }
    };

    OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            hsTitle.setSelectIndex(arg0);

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

    private class OrderFragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragmentList;

        public OrderFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList2) {
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

    }
}
