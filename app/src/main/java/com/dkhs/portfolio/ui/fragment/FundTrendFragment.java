/**
 * @Title CompareFundFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-5-5 下午4:18:02
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.BenefitChartView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

/**
 * @author zjz
 * @version 1.0
 * @ClassName CompareFundFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-5-5 下午4:18:02
 */
public class FundTrendFragment extends BaseFragment {

    public static final String EXTRA_TRENDTYPE = "extra_trendType";
    @ViewInject(R.id.rootView)
    private ViewGroup rootView;


    private BenefitChartView benefitChartView;

    private BenefitChartView.FundTrendType mTrendType;

    public static FundTrendFragment newInstance(BenefitChartView.FundTrendType trendType) {
        FundTrendFragment fragment = new FundTrendFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(EXTRA_TRENDTYPE, trendType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_fund_trend;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
        if (getArguments() != null) {
            mTrendType = (BenefitChartView.FundTrendType) getArguments().getSerializable(EXTRA_TRENDTYPE);
        }
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_TRENDTYPE, mTrendType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        benefitChartView = new BenefitChartView(getActivity());
        rootView.addView(benefitChartView.getBenifitView());


    }


    @Subscribe
    public void updateUI(FundQuoteBean fundQuoteBean) {

        if (fundQuoteBean != null && fundQuoteBean.getManagers() != null && fundQuoteBean.getManagers().size() > 0) {
            benefitChartView.draw(fundQuoteBean, mTrendType);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    private boolean isViewShown;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && !isViewShown) {

            if (getView() != null) {
                isViewShown = true;

            } else {
                isViewShown = false;
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


}
