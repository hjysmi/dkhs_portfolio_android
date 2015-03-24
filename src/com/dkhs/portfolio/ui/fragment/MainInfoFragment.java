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
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.adapter.BasePagerFragmentAdapter;
import com.dkhs.portfolio.ui.adapter.FragmentSelectAdapter;
import com.dkhs.portfolio.ui.widget.HScrollTitleView;
import com.dkhs.portfolio.ui.widget.ScrollViewPager;
import com.dkhs.portfolio.ui.widget.HScrollTitleView.ISelectPostionListener;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @ClassName MainInfoFragment
 * @Description 主界面资讯tab页
 * @author zjz
 * @date 2015-2-26 下午3:31:46
 * @version 1.0
 */
public class MainInfoFragment extends BaseTitleFragment {

    // @ViewInject(R.id.hs_title)
    // private HScrollTitleView hsTitle;
    // @ViewInject(R.id.pager)
    // private ScrollViewPager pager;
    //
    private LinearLayout layout;

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
        layout = (LinearLayout) view.findViewById(R.id.yanbao_layout);
        UserEntity user;
        String userId = null;
        try {
            user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);
            if (user != null) {
                if (!TextUtils.isEmpty(user.getAccess_token())) {
                    user = UserEntityDesUtil.decode(user, "ENCODE", ConstantValue.DES_PASSWORD);
                }
                userId = user.getId() + "";
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] name = getResources().getStringArray(R.array.main_info_title);
        NewsforModel infoEngine;
        List<Fragment> fragmentList = new ArrayList<Fragment>();

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("304");
        Fragment optionalInfoFragment = ReportListForAllFragment.getFragment(infoEngine, OpitionNewsEngineImple.NEWS_GROUP);
        fragmentList.add(optionalInfoFragment);

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("301");
        Fragment hongguanFragment = ReportListForAllFragment.getFragment(infoEngine, OpitionNewsEngineImple.NEWS_GROUP_TWO);
        fragmentList.add(hongguanFragment);

        infoEngine = new NewsforModel();
        infoEngine.setUserid(userId);
        infoEngine.setContentSubType("302");
        Fragment celueFragment = ReportListForAllFragment.getFragment(infoEngine, OpitionNewsEngineImple.NEWS_GROUP_TWO);
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
