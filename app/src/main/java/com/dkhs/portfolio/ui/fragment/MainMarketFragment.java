/**
 * @Title MainMarketFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-6 上午9:46:52
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.InfoActivity;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.adapter.BasePagerFragmentAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.dkhs.portfolio.ui.widget.TabWidget;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MainMarketFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-6 上午9:46:52
 */
public class MainMarketFragment extends VisiableLoadFragment implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.rl_header_title)
    RelativeLayout mRlheadertitle;
    @ViewInject(R.id.vp)
    ViewPager vp;
    @ViewInject(R.id.btn_refresh)
    TextView mBtnrefresh;
    @ViewInject(R.id.btn_search)
    TextView mBtnsearch;
    @ViewInject(R.id.left_btn)
    TextView mLeftBtn;
    BasePagerFragmentAdapter mAdapter;
    private TabWidget tabWidget;
    private ArrayList<Fragment> fragmentList;


    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_main_market;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentList = new ArrayList<>();
        mRlheadertitle.setClickable(true);
        fragmentList.add(new MarketFundsHomeFragment());
        fragmentList.add(new MarketStockFragment());
        mAdapter = new BasePagerFragmentAdapter(getChildFragmentManager(), fragmentList);
        vp.setAdapter(mAdapter);
        vp.setOnPageChangeListener(this);
        tabWidget = new TabWidget(view);
        tabWidget.getmBtntitletabright().setVisibility(View.GONE);
        mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh),
                null, null, null);
        mBtnsearch.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
                null, null, null);
        tabWidget.setOnSelectListener(new TabWidget.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                vp.setCurrentItem(position);
            }
        });
        tabWidget.setSelection(0);

        vp.setCurrentItem(0);
        mBtnsearch.setOnClickListener((View.OnClickListener) fragmentList.get(0));
        mBtnrefresh.setOnClickListener((View.OnClickListener) fragmentList.get(0));
        vp.setOffscreenPageLimit(2);
        mLeftBtn.setVisibility(View.GONE);
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(InfoActivity.newIntent(mActivity,true));
            }
        });
        BusProvider.getInstance().register(this);


        if (mActivity instanceof MainActivity) {
            Bundle bundle = ((MainActivity) mActivity).mBundle;
            if (bundle != null)
                handIntent(bundle);
        }


    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void newIntent(NewIntent newIntent) {
        handIntent(newIntent.bundle);
    }


    private void handIntent(Bundle bundle) {

        if (bundle.containsKey("fund_index")) {
            int index = bundle.getInt("fund_index", 0);
            vp.setCurrentItem(index);
        }
    }

    @Override
    public void requestData() {

    }


    @Override
    public void onViewHide() {

        if (null != mAdapter && null != vp) {
            Fragment fragment = mAdapter.getItem(vp.getCurrentItem());
            if (fragment instanceof VisiableLoadFragment) {
                ((VisiableLoadFragment) fragment).onViewHide();
            }
        }
    }

    @Override
    public void onViewShow() {

        if (null != mAdapter && null != vp) {

            Fragment fragment = mAdapter.getItem(vp.getCurrentItem());
            if (fragment instanceof VisiableLoadFragment) {
                ((VisiableLoadFragment) fragment).onViewShow();
            } else {
                fragment.onResume();
            }
        }
    }


    private static final String TAG = MainMarketFragment.class.getSimpleName();

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        Fragment f = fragmentList.get(i);
        tabWidget.setSelection(i);
        switch (i) {
            case 0:
                mLeftBtn.setVisibility(View.GONE);
                mBtnsearch.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
                        null, null, null);
                mBtnsearch.setVisibility(View.VISIBLE);
                mBtnrefresh.setVisibility(View.VISIBLE);
                mBtnsearch.setOnClickListener((View.OnClickListener) f);
                mBtnrefresh.setOnClickListener((View.OnClickListener) f);
                break;
            case 1:
                mLeftBtn.setVisibility(View.VISIBLE);
                mBtnsearch.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
                        null, null, null);
                mBtnrefresh.setVisibility(View.VISIBLE);
                mBtnsearch.setVisibility(View.VISIBLE);
                mBtnrefresh.setOnClickListener((View.OnClickListener) f);
                mBtnsearch.setOnClickListener((View.OnClickListener) f);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
    @Subscribe
    public void rotateRefreshButton(RotateRefreshEvent rotateRefreshEvent) {
        if (isAdded() && !isHidden()) {
            mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing),
                    null, null, null);
            Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_around_center_point);
            mBtnrefresh.startAnimation(animation);
        }
    }
    @Subscribe
    public void stopRefreshAnimation(StopRefreshEvent stopRefreshEvent) {
        if (isAdded() && !isHidden()) {
            mBtnrefresh.clearAnimation();
            mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh),
                    null, null, null);
        }
    }
}
