package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AllMarketBean;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.MarketCenterStockEngineImple;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.IHttpListener;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.SelectGeneralActivity;
import com.dkhs.portfolio.ui.adapter.MarkStockAdatper;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.TopEvent;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkGridViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkIndexViewPool;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkPlateGridViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkStockViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.MarkTitleViewBean;
import com.dkhs.portfolio.ui.widget.ViewBean.ViewBean;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.squareup.otto.Subscribe;
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
public class MarketStockFragment extends VisiableLoadFragment implements View.OnClickListener {


    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_market_stock;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        generateCacheData();
        loadingAllData();

    }

    @Override
    public void requestData() {

    }


    private static final long mPollRequestTime = 1000 * 5;
    public SwipeRefreshLayout mSwipeLayout;
    private final int defViewCount = 6;


    private RecyclerView mRecyclerView;
    private MarkIndexViewPool mViewPool;
    private MarkStockAdatper mAdapter;
    private List<ViewBean> mViewBeanList;

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mViewPool = new MarkIndexViewPool();
        BusProvider.getInstance().register(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewShow() {

        updateHandler.postDelayed(updateRunnable, mPollRequestTime);

        if (isLoading) {
            startAnimaRefresh();
        } else {
            endAnimaRefresh();
        }

        StatService.onPageStart(getActivity(), this.getClass().getSimpleName());
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onViewHide() {
        updateHandler.removeCallbacks(updateRunnable);
        StatService.onPageEnd(getActivity(), this.getClass().getSimpleName());
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
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
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));


        mRecyclerView.setHasFixedSize(true);


        mViewBeanList = new ArrayList<ViewBean>(defViewCount);
//        mViewBeanList.add(new MarkTitleViewBean(R.string.market_title_index));
        mViewBeanList.add(new MarkTitleViewBean(R.string.market_title_hot));
        mViewBeanList.add(new MarkTitleViewBean(R.string.market_title_up));
        mViewBeanList.add(new MarkTitleViewBean(R.string.market_title_down));
        mViewBeanList.add(new MarkTitleViewBean(R.string.market_title_turnover));
        mViewBeanList.add(new MarkTitleViewBean(R.string.market_title_ampli));
        mAdapter = new MarkStockAdatper(mViewBeanList, mViewPool);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadingAllData();

            }
        });


//        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
//                .sizeProvider(mAdapter)
//                .color(Color.TRANSPARENT)
//                .build());
//        mRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(getActivity())
//                .sizeProvider(mAdapter)
//                .color(Color.TRANSPARENT)
//                .build());


    }


    private boolean isLoading;
    private boolean requestSuccess = false;

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
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_STOCK_ALL_MARKET_JSON,jsonData);
            return parseData(jsonData);
        }

        @Override
        protected void afterParseData(List<ViewBean> viewBeanList) {
            isLoading = false;
            if(!requestSuccess)
                requestSuccess = true;
            if (isAdded()) {
                endAnimaRefresh();

                mSwipeLayout.setRefreshing(false);
                if (null != viewBeanList) {

                    mViewBeanList.clear();
                    mViewBeanList.addAll(viewBeanList);
                    mAdapter.notifyDataSetChanged();
                }
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

    private List<ViewBean> parseData(String jsonData){
        mAllMarketBean = DataParse.parseObjectJson(AllMarketBean.class, jsonData);
        if (null == mAllMarketBean) {
            return null;
        }
        mAllMarketBean = DataParse.parseObjectJson(AllMarketBean.class, jsonData);
        if (null == mAllMarketBean) {
            return null;
        }
        List<ViewBean> dataList = null;

        dataList = new ArrayList<ViewBean>();
//            dataList.add(new MarkTitleViewBean(R.string.market_title_index));

        if (null != mAllMarketBean.getMidx_data()) {

            for (StockQuotesBean selectStockBean : mAllMarketBean.getMidx_data().getResults()) {

                dataList.add(new MarkGridViewBean(selectStockBean));
            }

        }

        dataList.add(new MarkTitleViewBean(R.string.market_title_hot));


        if (null != mAllMarketBean.getSect_data()) {

            for (SectorBean sectorBean : mAllMarketBean.getSect_data().getResults()) {

                dataList.add(new MarkPlateGridViewBean(sectorBean));
            }

        }

        dataList.add(new MarkTitleViewBean(R.string.market_title_up));
        for (StockQuotesBean selectStockBean : mAllMarketBean.getRise_data().getResults()) {
            dataList.add(new MarkStockViewBean(selectStockBean));
        }
        dataList.add(new MarkTitleViewBean(R.string.market_title_down));
        for (StockQuotesBean selectStockBean : mAllMarketBean.getDrop_data().getResults()) {
            dataList.add(new MarkStockViewBean(selectStockBean));
        }
        dataList.add(new MarkTitleViewBean(R.string.market_title_turnover));
        for (StockQuotesBean selectStockBean : mAllMarketBean.getTurnover_data().getResults()) {
            dataList.add(new MarkStockViewBean(selectStockBean, MarkStockViewBean.SUB_TYPE_TURNOVER));
        }
        dataList.add(new MarkTitleViewBean(R.string.market_title_ampli));
        for (StockQuotesBean selectStockBean : mAllMarketBean.getAmplitude_data().getResults()) {
            dataList.add(new MarkStockViewBean(selectStockBean, MarkStockViewBean.SUB_TYPE_AMPLITUDE));
        }
        return dataList;
    }


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
//                intent = new Intent(getActivity(), SelectGeneralActivity.class);
                intent = new Intent(getActivity(), SelectGeneralActivity.class);
                break;

            default:
                break;
        }

        if (null != intent) {
            UIUtils.startAnimationActivity(getActivity(), intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacks(updateRunnable);
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    WeakHandler updateHandler = new WeakHandler();
    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
//            if ( UIUtils.roundAble(engineList.get(0).getStatu())) {
            loadingAllData();
//            }
            if (NetUtil.checkNetWork() && requestSuccess && null != mAllMarketBean && UIUtils.roundAble(mAllMarketBean.getDrop_data().getTrade_status())) {
                updateHandler.postDelayed(updateRunnable, mPollRequestTime);
            }
        }
    };

    private void generateCacheData(){
        String allMarketJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_STOCK_ALL_MARKET_JSON);
        if(!TextUtils.isEmpty(allMarketJson)){
            try{
                mViewBeanList.clear();
                mViewBeanList.addAll(parseData(allMarketJson));
                mAdapter.notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void loadingAllData() {

        if (isLoading) {
            return;
        }
        isLoading = true;

        if (isAdded()) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startAnimaRefresh();
                }
            });
        }
        MarketCenterStockEngineImple.loadAllMarkets(plateListener);

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

    @Subscribe
    public void forward2Top(TopEvent event){
        if(event != null && isVisible()&& getUserVisibleHint()){
            if(mRecyclerView != null){
                mRecyclerView.smoothScrollToPosition(0);
            }
        }
    }



}
