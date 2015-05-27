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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.dkhs.portfolio.R;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
    public ImageButton mBtnrefresh;
    @ViewInject(R.id.btn_search)
    ImageButton mBtnsearch;
    private Fragment previousF;
    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_main_marke;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        getDataForNet();
    }
    /**
     * iniView initData
     */
    public void initData() {
        displayStockFragment();
    }

    /**
     * getData from net
     */
    public void getDataForNet() {
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
        if(previousF!=null){
            b.hide(previousF);
        }
        if (f != null) {
            b.show(f);
        } else {
            f = new TabFundFragment();
            b.add(R.id.view_datalist, f, "tabFundFragment");
        }
        previousF=f;
        b.commit();
    }

    private void displayStockFragment() {
        FragmentTransaction b = getChildFragmentManager().beginTransaction();

        Fragment f = getChildFragmentManager().findFragmentByTag("stockFragment");
        if(previousF!=null){
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
        previousF=f;
        b.commit();
    }

    private void displayFundsFragment() {
        FragmentTransaction b = getChildFragmentManager().beginTransaction();
        Fragment f = getChildFragmentManager().findFragmentByTag("FundsOrderActivity");
        if(previousF!=null){
            b.hide(previousF);
        }
        if (f != null) {
            b.show(f);
        } else {
            f = new FundsOrderMainFragment();
            b.add(R.id.view_datalist, f, "FundsOrderActivity");
        }
        previousF=f;
        b.commit();
    }



}
