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
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.SelectFundAdapter;
import com.dkhs.portfolio.ui.fragment.MainFragment;
import com.dkhs.portfolio.ui.fragment.MenuLeftFragment;
import com.dkhs.portfolio.ui.fragment.MenuRightFragment;
import com.dkhs.portfolio.ui.fragment.TestFragment;
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

    private boolean isSelectByStock;

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
        initView();

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void initView() {
        initTabPage();
        mSelctStockView = (GridView) findViewById(R.id.rl_add_stocklist);
        mSelectStockAdapter = new SelectFundAdapter(this);
        mSelctStockView.setAdapter(mSelectStockAdapter);
        mSelctStockView.setNumColumns(3);
        btnAdd = getRightButton();

        btnAdd.setText(getString(R.string.add_postional_format, 5));
        btnAdd.setOnClickListener(this);

        findViewById(R.id.btn_order).setVisibility(View.GONE);
    }

    private void initTabPage() {
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        String[] titleArray = getResources().getStringArray(R.array.select_stock);

        // Fragment mViewPagerFragment = new MenuRightFragment();
        // Fragment mViewPagerFragment2 = new MainFragment();
        // Fragment mViewPagerFragment3 = new MenuLeftFragment();
        fragmentList.add(TestFragment.getInstance());
        fragmentList.add(TestFragment.getInstance());
        fragmentList.add(TestFragment.getInstance());

        ScrollViewPager pager = (ScrollViewPager) findViewById(R.id.pager);
        // pager.setCanScroll(false);
        pager.setAdapter(new MyPagerFragmentAdapter(getSupportFragmentManager(), fragmentList, titleArray));

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }

    // 适配器
    private class MyPagerFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;
        private String[] titleList;

        public MyPagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titleList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        // ViewPage中显示的内容
        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (RIGHTBUTTON_ID == id) {
            Toast.makeText(this, "添加选择参照基金", Toast.LENGTH_SHORT).show();
        }
    }
}
