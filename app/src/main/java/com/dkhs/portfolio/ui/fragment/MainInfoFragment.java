/**
 * @Title MainInfoFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-26 下午3:31:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MainInfoFragment
 * @Description 主界面资讯tab页
 * @date 2015-2-26 下午3:31:46
 */
public class MainInfoFragment extends BaseTitleFragment {

    // @ViewInject(R.id.hs_title)
    // private HScrollTitleView hsTitle;
    // @ViewInject(R.id.pager)
    // private ScrollViewPager pager;
    //

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_yanbao;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setTitle(R.string.title_info);
    }

    private void initView(View view) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.yanbao_layout);
        String userId = null;
        UserEntity user = UserEngineImpl.getUserEntity();
        if (user != null) {
            userId = user.getId() + "";
        }

        // System.out.println("userId:" + userId);
        String[] name = getResources().getStringArray(R.array.main_info_title);
        NewsforModel infoEngine;
        List<Fragment> fragmentList = new ArrayList<Fragment>();

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("304");
        Fragment optionalInfoFragment = ReportListForAllFragment.getFragment(infoEngine,
                OpitionNewsEngineImple.NEWS_GROUP);
        fragmentList.add(optionalInfoFragment);

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("301");
        Fragment hongguanFragment = ReportListForAllFragment.getFragment(infoEngine,
                OpitionNewsEngineImple.NEWS_GROUP_TWO);
        fragmentList.add(hongguanFragment);

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("302");
        Fragment celueFragment = ReportListForAllFragment
                .getFragment(infoEngine, OpitionNewsEngineImple.NEWS_GROUP_TWO);
        fragmentList.add(celueFragment);

        new FragmentSelectAdapter(getActivity(), name, fragmentList, layout, getChildFragmentManager());

        // hsTitle.setTitleList(getResources().getStringArray(R.array.main_info_title));
        // hsTitle.setSelectPositionListener(titleSelectListener);
        //
        // ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();// ViewPager中显示的数据
        //
        // fragmentList.add(new TestFragment());
        // fragmentList.add(new TestFragment());
        // fragmentList.add(new TestFragment());
        //
        // pager.setAdapter(new BasePagerFragmentAdapter(getChildFragmentManager(), fragmentList));
        // pager.setOnPageChangeListener(pageChangeListener);
        // pager.setCurrentItem(titleIndex);

    }

    // ISelectPostionListener titleSelectListener = new ISelectPostionListener() {
    //
    // @Override
    // public void onSelectPosition(int position) {
    // if (null != pager) {
    // pager.setCurrentItem(position);
    // // isFromTitle = true;
    // }
    // }
    // };
    //
    // OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
    //
    // @Override
    // public void onPageSelected(int arg0) {
    // // if (!isFromTitle) {
    // hsTitle.setSelectIndex(arg0);
    // // }
    // // isFromTitle = false;
    //
    // }
    //
    // @Override
    // public void onPageScrolled(int arg0, float arg1, int arg2) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void onPageScrollStateChanged(int arg0) {
    // // TODO Auto-generated method stub
    //
    // }
    // };

}
