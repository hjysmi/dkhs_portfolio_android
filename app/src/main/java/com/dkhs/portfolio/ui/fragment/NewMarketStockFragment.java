package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AllMarketBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.MarketCenterStockEngineImple;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.adapter.MarkStockAdatper;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkGridViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkIndexViewPool;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkPlateGridViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkStockViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkTitleViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.ViewBean;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName StockFragment
 * @Description TODO(股票)
 * @date 2015/5/25.
 */
public class NewMarketStockFragment extends VisiableLoadFragment implements View.OnClickListener {


    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_market_stock;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }

    @Override
    public void requestData() {

    }


    private static final long mPollRequestTime = 1000 * 5;
    public SwipeRefreshLayout mSwipeLayout;


    private RecyclerView mRecyclerView;
    private MarkIndexViewPool mViewPool;
    private MarkStockAdatper mAdapter;
    private List<ViewBean> dataList;

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mViewPool = new MarkIndexViewPool();

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());

    }

    @Override
    public void onViewShow() {
        loadingAllData();
        updateHandler.postDelayed(updateRunnable, mPollRequestTime);


        if (isLoading) {
            startAnimaRefresh();
        } else {
            endAnimaRefresh();
        }
    }

    @Override
    public void onViewHide() {
        updateHandler.removeCallbacks(updateRunnable);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void initView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mRecyclerView.setHasFixedSize(true);


        dataList = new ArrayList<ViewBean>(5);
        dataList.add(new MarkTitleViewBean(R.string.market_title_index));
        dataList.add(new MarkTitleViewBean(R.string.market_title_hot));
        dataList.add(new MarkTitleViewBean(R.string.market_title_up));
        dataList.add(new MarkTitleViewBean(R.string.market_title_down));
        dataList.add(new MarkTitleViewBean(R.string.market_title_turnover));
        dataList.add(new MarkTitleViewBean(R.string.market_title_ampli));
        mAdapter = new MarkStockAdatper(dataList, mViewPool);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadingAllData();

            }
        });

    }


    private boolean isLoading;

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    private AllMarketBean mAllMarketBean;

    IHttpListener plateListener = new ParseHttpListener<List<ViewBean>>() {
        @Override
        protected List<ViewBean> parseDateTask(String jsonData) {

            mAllMarketBean = DataParse.parseObjectJson(AllMarketBean.class, jsonData);
            List<ViewBean> dataList = null;
            if (null != mAllMarketBean) {
                dataList = new ArrayList<ViewBean>();
                dataList.add(new MarkTitleViewBean(R.string.market_title_index));
                dataList.add(new MarkGridViewBean(mAllMarketBean.getMidx_data()));
                dataList.add(new MarkTitleViewBean(R.string.market_title_hot));
                dataList.add(new MarkPlateGridViewBean(mAllMarketBean.getSect_data()));
                dataList.add(new MarkTitleViewBean(R.string.market_title_up));
                for (StockQuotesBean selectStockBean : mAllMarketBean.getRise_data().getResults()) {
                    dataList.add(new MarkStockViewBean(selectStockBean, false));
                }
                dataList.add(new MarkTitleViewBean(R.string.market_title_down));
                for (StockQuotesBean selectStockBean : mAllMarketBean.getDrop_data().getResults()) {
                    dataList.add(new MarkStockViewBean(selectStockBean, false));
                }
                dataList.add(new MarkTitleViewBean(R.string.market_title_turnover));
                for (StockQuotesBean selectStockBean : mAllMarketBean.getTurnover_data().getResults()) {
                    dataList.add(new MarkStockViewBean(selectStockBean, true));
                }
                dataList.add(new MarkTitleViewBean(R.string.market_title_ampli));
                for (StockQuotesBean selectStockBean : mAllMarketBean.getAmplitude_data().getResults()) {
                    dataList.add(new MarkStockViewBean(selectStockBean, true));
                }

            }
            return dataList;
        }

        @Override
        protected void afterParseData(List<ViewBean> object) {
            endAnimaRefresh();
            isLoading = false;
            mSwipeLayout.setRefreshing(false);
            if (null != object) {

                dataList.clear();
                dataList.addAll(object);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            isLoading = false;
            if (isAdded()) {

                endAnimaRefresh();
                mSwipeLayout.setRefreshing(false);

            }
        }
    };


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_refresh:
                loadingAllData();

                break;
            case R.id.btn_right:
                intent = new Intent(getActivity(), SelectAddOptionalActivity.class);

                break;
            case R.id.btn_search:
                intent = new Intent(getActivity(), SelectAddOptionalActivity.class);
                break;


            default:
                break;
        }

        if (null != intent) {
            UIUtils.startAminationActivity(getActivity(), intent);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }


    WeakHandler updateHandler = new WeakHandler();
    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
//            if ( UIUtils.roundAble(engineList.get(0).getStatu())) {
            loadingAllData();
//            }
            if (null != mAllMarketBean && UIUtils.roundAble(mAllMarketBean.getDrop_data().getTrade_status())) {
                updateHandler.postDelayed(updateRunnable, mPollRequestTime);
            }
        }
    };

    private void loadingAllData() {

        if (isLoading) {
            return;
        }
        isLoading = true;

        MarketCenterStockEngineImple.loadAllMarkets(plateListener);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startAnimaRefresh();
            }
        });
    }


    public void startAnimaRefresh() {
        if (isAdded() && getUserVisibleHint()) {
            BusProvider.getInstance().post(new RotateRefreshEvent());
        }
    }

    public void endAnimaRefresh() {
        if (isAdded() && getUserVisibleHint()) {
            BusProvider.getInstance().post(new StopRefreshEvent());
        }
    }


}
