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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CompareFundsBean;
import com.dkhs.portfolio.bean.CompareFundsBean.ComparePoint;
import com.dkhs.portfolio.bean.FundQuoteBean;
import com.dkhs.portfolio.bean.HistoryNetValue;
import com.dkhs.portfolio.bean.HistoryNetValue.HistoryNetBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.CompareEngine;
import com.dkhs.portfolio.engine.NetValueEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.BenefitChartView;
import com.dkhs.portfolio.ui.widget.LineEntity;
import com.dkhs.portfolio.ui.widget.LinePointEntity;
import com.dkhs.portfolio.ui.widget.TrendChart;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;
import org.parceler.transfuse.annotations.OnCreate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName CompareFundFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-5-5 下午4:18:02
 */
public class FundTrendFragment extends BaseFragment {


    @ViewInject(R.id.rootView)
    private ViewGroup rootView;


    private BenefitChartView benefitChartView;


    public static FundTrendFragment newInstance() {
        FundTrendFragment fragment = new FundTrendFragment();

        Bundle arguments = new Bundle();
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

        if (fundQuoteBean != null && fundQuoteBean.getManagers() != null && fundQuoteBean.getManagers().size()>0) {
            benefitChartView.draw(fundQuoteBean);
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
