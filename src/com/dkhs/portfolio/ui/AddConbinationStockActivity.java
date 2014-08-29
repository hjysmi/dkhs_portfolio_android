/**
 * @Title AddConbinationStockActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version V1.0
 */
package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.ui.adapter.SelectFundAdapter;
import com.dkhs.portfolio.ui.adapter.AdatperSelectConbinStock.ISelectChangeListener;
import com.dkhs.portfolio.ui.fragment.FragmentSelectConbinStock;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.TabPageIndicator;

/**
 * @ClassName AddConbinationStockActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午12:11:20
 * @version 1.0
 */
public class AddConbinationStockActivity extends ModelAcitivity implements OnClickListener {
    public static final String KEY_SELECT_STOCK = "key_select_stock";
    private GridView mSelctStockView;
    private SelectFundAdapter mSelectStockAdapter;
    private Button btnAdd;
    ArrayList<FragmentSelectConbinStock> fragmentList = new ArrayList<FragmentSelectConbinStock>();// ViewPager中显示的数据

    private boolean isSelectByStock;

    // public static List<Integer> mSelectList = new ArrayList<Integer>();
    public static List<ConStockBean> mSelectIdList = new ArrayList<ConStockBean>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_conbina_stock);

        // if (null != getIntent()) {
        // isSelectByStock = getIntent().getBooleanExtra(KEY_SELECT_STOCK, false);
        // }
        // if (isSelectByStock) {
        // } else {
        // setTitle(R.string.select_fund);
        // }

        setTitle(R.string.select_stock);
        // initData();
        initView();

    }

    private void initData() {

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initView() {
        initTabPage();
        mSelctStockView = (GridView) findViewById(R.id.rl_add_stocklist);
        mSelectStockAdapter = new SelectFundAdapter(this, mSelectIdList);
        mSelctStockView.setAdapter(mSelectStockAdapter);
        mSelctStockView.setNumColumns(3);
        btnAdd = getRightButton();

        btnAdd.setText(getString(R.string.add_postional_format, mSelectIdList.size()));
        btnAdd.setOnClickListener(this);

        findViewById(R.id.btn_order).setVisibility(View.GONE);
    }

    private void initTabPage() {

        String[] titleArray = getResources().getStringArray(R.array.select_stock);

        FragmentSelectConbinStock mPagerFragment = FragmentSelectConbinStock.getInstance();
        FragmentSelectConbinStock mPagerFragment2 = FragmentSelectConbinStock.getInstance();
        FragmentSelectConbinStock mPagerFragment3 = FragmentSelectConbinStock.getInstance();
        FragmentSelectConbinStock mPagerFragment4 = FragmentSelectConbinStock.getInstance();
        // mPagerFragment.setCheckListener(this);
        // mPagerFragment2.setCheckListener(this);
        // mPagerFragment3.setCheckListener(this);
        fragmentList.add(mPagerFragment);
        fragmentList.add(mPagerFragment2);
        fragmentList.add(mPagerFragment3);
        fragmentList.add(mPagerFragment4);

        ScrollViewPager pager = (ScrollViewPager) findViewById(R.id.pager);
        // pager.setCanScroll(false);
        pager.setAdapter(new MyPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, titleArray));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                System.out.println("onPageSelected :" + arg0);
                ((FragmentSelectConbinStock) fragmentList.get(arg0)).refreshSelect();

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                System.out.println("onPageSelected :" + arg0);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                System.out.println("onPageScrollStateChanged :" + arg0);

            }
        });
        // pager.setOffscreenPageLimit(3);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }

    // 适配器
    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<FragmentSelectConbinStock> fragmentList;
        private String[] titleList;

        public MyPagerFragmentAdapter(FragmentManager fm, ArrayList<FragmentSelectConbinStock> fragmentList2,
                String[] titleList) {
            super(fm);
            this.fragmentList = fragmentList2;
            this.titleList = titleList;
        }

        // ViewPage中显示的内容
        @Override
        public Fragment getItem(int arg0) {

            System.out.println(" MyPagerFragmentAdapter getItem:" + arg0);
            return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
        }

        // Title中显示的内容
        @Override
        public CharSequence getPageTitle(int position) {
            // TODO Auto-generated method stub
            return (titleList.length > position) ? titleList[position] : "";
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mSelectIdList.clear();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (RIGHTBUTTON_ID == id) {
            Toast.makeText(this, "添加选择参照基金", Toast.LENGTH_SHORT).show();
        }
    }

    public void notifySelectDataChange(boolean isUpdataFragment) {
        btnAdd.setText(getString(R.string.add_postional_format, mSelectIdList.size()));
        // if (isUpdataFragment) {
        for (FragmentSelectConbinStock fragment : fragmentList) {
            fragment.refreshSelect();
        }
        // }
        mSelectStockAdapter.notifyDataSetChanged();
    }

}
