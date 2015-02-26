/**
 * @Title MainInfoFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-26 下午3:31:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.BasePagerFragmentAdapter;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @ClassName MainInfoFragment
 * @Description 主界面资讯tab页
 * @author zjz
 * @date 2015-2-26 下午3:31:46
 * @version 1.0
 */
public class MainInfoFragment extends BaseTitleFragment {

    @ViewInject(R.id.hs_title)
    private HScrollTitleView hsTitle;
    @ViewInject(R.id.pager)
    private ScrollViewPager pager;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_main_info;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setTitle(R.string.title_info);
    }

    private void initView(View view) {
        hsTitle.setTitleList(getResources().getStringArray(R.array.main_info_title));
        hsTitle.setSelectPositionListener(titleSelectListener);

        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据

        fragmentList.add(new TestFragment());
        fragmentList.add(new TestFragment());
        fragmentList.add(new TestFragment());

        pager.setAdapter(new BasePagerFragmentAdapter(getChildFragmentManager(), fragmentList));
        pager.setOnPageChangeListener(pageChangeListener);
        // pager.setCurrentItem(titleIndex);

    }

    ISelectPostionListener titleSelectListener = new ISelectPostionListener() {

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

}
