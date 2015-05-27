/**
 * @Title MainMarketFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-6 上午9:46:52
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MainMarketFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-2-6 上午9:46:52
 */
public class MainMarketFragment extends BaseFragment {


    @ViewInject(R.id.btn_titletab_right)
    Button mBtntitletabright;
    @ViewInject(R.id.btn_titletab_center)
    Button mBtntitletabcenter;
    @ViewInject(R.id.btn_titletab_left)
    Button mBtntitletableft;
    @ViewInject(R.id.rl_header_title)
    RelativeLayout mRlheadertitle;
    @ViewInject(R.id.view_datalist)
    FrameLayout mViewdatalist;
    @ViewInject(R.id.btn_refresh)
    Button mBtnrefresh;
    @ViewInject(R.id.btn_search)
    Button mBtnsearch;
    private Fragment previousF;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_main_marke;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh),
                null, null, null);

        mBtnsearch.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.btn_search_select),
                null, null, null);
        displayStockFragment();
    }


    @OnClick({R.id.btn_titletab_right, R.id.btn_titletab_left, R.id.btn_titletab_center})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_titletab_right: {
                mBtntitletabright.setEnabled(false);
                mBtntitletableft.setEnabled(true);
                mBtntitletabcenter.setEnabled(true);
                displayFundsFragment();

                // PromptManager.showCustomToast(R.drawable.ic_toast_gantan, R.string.message_timeout);
            }
            break;
            case R.id.btn_titletab_left: {
                mBtntitletabright.setEnabled(true);
                mBtntitletableft.setEnabled(false);
                mBtntitletabcenter.setEnabled(true);
                displayStockFragment();
            }
            break;
            case R.id.btn_titletab_center: {
                mBtntitletabright.setEnabled(true);
                mBtntitletableft.setEnabled(true);
                mBtntitletabcenter.setEnabled(false);
                displayFundFragment();
            }
            break;
            default:
                break;
        }
    }

    private void displayFundFragment() {
        FragmentTransaction b = getChildFragmentManager().beginTransaction();
        Fragment f = getChildFragmentManager().findFragmentByTag("tabFundFragment");
        if (previousF != null) {
            b.hide(previousF);
        }
        if (f != null) {
            b.show(f);
        } else {
            f = new TabFundFragment();
            b.add(R.id.view_datalist, f, "tabFundFragment");
        }
        mBtnsearch.setVisibility(View.GONE);
        mBtnrefresh.setOnClickListener((View.OnClickListener) f);
        previousF = f;
        b.commit();
    }

    private void displayStockFragment() {
        FragmentTransaction b = getChildFragmentManager().beginTransaction();

        Fragment f = getChildFragmentManager().findFragmentByTag("stockFragment");
        if (previousF != null) {
            b.hide(previousF);
        }
        if (f != null) {
            b.show(f);
        } else {
            f = new StockFragment();
            b.add(R.id.view_datalist, f, "stockFragment");
        }
        mBtnsearch.setVisibility(View.VISIBLE);
        mBtnsearch.setOnClickListener((View.OnClickListener) f);
        mBtnrefresh.setOnClickListener((View.OnClickListener) f);
        previousF = f;
        b.commit();
    }

    private void displayFundsFragment() {
        FragmentTransaction b = getChildFragmentManager().beginTransaction();
        Fragment f = getChildFragmentManager().findFragmentByTag("FundsOrderActivity");
        if (previousF != null) {
            b.hide(previousF);
        }
        if (f != null) {
            b.show(f);
        } else {
            f = new FundsOrderMainFragment();
            b.add(R.id.view_datalist, f, "FundsOrderActivity");
        }
        previousF = f;
        b.commit();
    }


    @Subscribe
    public void rotateRefreshButton(RotateRefreshEvent rotateRefreshEvent) {

        if (isAdded() && !isHidden()) {

            mBtnrefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing),
                    null, null, null);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_around_center_point);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        BusProvider.getInstance().unregister(this);
    }
}
