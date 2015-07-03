package com.dkhs.portfolio.ui.fragment;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.MarketCenterStockEngineImple;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.engine.PlateLoadMoreEngineImpl;
import com.dkhs.portfolio.ui.MarketListActivity;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.MarketCenterGridAdapter;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter;
import com.dkhs.portfolio.ui.adapter.MarketPlateGridAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.dkhs.portfolio.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        return R.layout.bg_activity_marketcenter;
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

    public final static String INLAND_INDEX = "inland_index";

    private GridView gvMainIndex;
    private List<SelectStockBean> mIndexDataList = new ArrayList<SelectStockBean>();
    private MarketCenterGridAdapter mIndexAdapter;

    private GridView gvPlate;
    private List<SectorBean> mSecotrList = new ArrayList<SectorBean>();
    private MarketPlateGridAdapter mPlateAdapter;

    // 涨幅榜
    private ListView lvIncease;
    private List<SelectStockBean> mIncreaseDataList = new ArrayList<SelectStockBean>();
    private MarketCenterItemAdapter mIncreaseAdapter;

    // 跌幅榜
    private ListView lvDown;
    private MarketCenterItemAdapter mDownAdapter;
    private List<SelectStockBean> mDownDataList = new ArrayList<SelectStockBean>();

    private ListView lvHandover;
    private MarketCenterItemAdapter mTurnOverAdapter;
    private List<SelectStockBean> mTurnOverDataList = new ArrayList<SelectStockBean>();

    // 振幅榜
    private ListView lvAmplit;
    private MarketCenterItemAdapter mAmplitAdapter;
    private List<SelectStockBean> mAmpliDataList = new ArrayList<SelectStockBean>();

    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 10;

    public SwipeRefreshLayout mSwipeLayout;

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);

        initData();

    }

    @Override
    public void onResume() {
        super.onResume();


        // MobclickAgent.onResume(getActivity());

    }

    @Override
    public void onViewShow() {
        if (null != engineList && engineList.size() > 0 && UIUtils.roundAble(engineList.get(0).getStatu())) {
            if (mMarketTimer == null) {
                mMarketTimer = new Timer(true);
                mMarketTimer.schedule(new RequestMarketTask(), 30, mPollRequestTime);
            }
        } else {
            loadingAllData();
        }


        if (isLoading) {
            startAnimaRefresh();
        } else {
            endAnimaRefresh();
        }
    }

    @Override
    public void onViewHide() {
        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }
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


        gvMainIndex = (GridView) view.findViewById(R.id.gv_mainindex);
        gvPlate = (GridView) view.findViewById(R.id.gv_plate);
        View btnMoreIndex = view.findViewById(R.id.btn_more_index);
        View btnMorePlate = view.findViewById(R.id.btn_more_plate);
        View btnMoreIncease = view.findViewById(R.id.btn_more_incease);
        View btnMoreDown = view.findViewById(R.id.btn_more_down);
        View btnMoreHand = view.findViewById(R.id.btn_more_handover);
        View btnMoreAmplit = view.findViewById(R.id.btn_more_amplitude);

        lvIncease = (ListView) view.findViewById(R.id.lv_market_incease);
        lvDown = (ListView) view.findViewById(R.id.lv_market_down);
        lvHandover = (ListView) view.findViewById(R.id.lv_market_handover);
        lvAmplit = (ListView) view.findViewById(R.id.lv_market_amplitude);

        btnMoreIndex.setOnClickListener(this);
        btnMorePlate.setOnClickListener(this);
        btnMoreIncease.setOnClickListener(this);
        btnMoreDown.setOnClickListener(this);
        btnMoreHand.setOnClickListener(this);
        btnMoreAmplit.setOnClickListener(this);

        mIndexAdapter = new MarketCenterGridAdapter(getActivity(), mIndexDataList, false);
        mPlateAdapter = new MarketPlateGridAdapter(getActivity(), mSecotrList);
        gvPlate.setAdapter(mPlateAdapter);
        gvPlate.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SectorBean bean = mSecotrList.get(position);
                startActivity(MarketListActivity.newIntent(getActivity(), MarketListActivity.LoadViewType.PlateList, bean.getId(),
                        bean.getAbbr_name()));

                // UIUtils.startAnimationActivity(
                // getActivity(),
                // MarketListActivity.newIntent(getActivity(), LoadViewType.PlateList, bean.getId(),
                // bean.getAbbr_name()));

            }
        });

        gvMainIndex.setAdapter(mIndexAdapter);
        gvMainIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // PromptManager.showToast("position:" + position);
                startQuoteActivity(mIndexDataList.get(position));

            }
        });

        // gvPlate.setAdapter(new MarketCenterGridAdapter(this, 6, true));
        mIncreaseAdapter = new MarketCenterItemAdapter(getActivity(), mIncreaseDataList);
        mDownAdapter = new MarketCenterItemAdapter(getActivity(), mDownDataList);
        mTurnOverAdapter = new MarketCenterItemAdapter(getActivity(), mTurnOverDataList, true);
        mAmplitAdapter = new MarketCenterItemAdapter(getActivity(), mAmpliDataList, true);
        lvIncease.setAdapter(mIncreaseAdapter);
        lvDown.setAdapter(mDownAdapter);
        lvHandover.setAdapter(mTurnOverAdapter);
        lvAmplit.setAdapter(mAmplitAdapter);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        // mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadingAllData();

            }
        });
        setDefTransittion();

    }


    private void setDefTransittion() {
        LayoutTransition mTransitioner = new LayoutTransition();
        AnimationHelper.setupCustomAnimations(mTransitioner, this);
        gvMainIndex.setLayoutTransition(mTransitioner);
        gvPlate.setLayoutTransition(mTransitioner);
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

    private List<LoadMoreDataEngine> engineList;
    private PlateLoadMoreEngineImpl plateEngine;

    private void initData() {
        engineList = new ArrayList<LoadMoreDataEngine>();
        engineList.add(new OpitionCenterStockEngineImple(new StockLoadDataListener(
                OpitionCenterStockEngineImple.ORDER_INCREASE), FragmentSelectStockFund.StockViewType.MARKET_STOCK_UPRATIO, 10));
        engineList.add(new OpitionCenterStockEngineImple(new StockLoadDataListener(
                OpitionCenterStockEngineImple.ORDER_DOWN), FragmentSelectStockFund.StockViewType.MARKET_STOCK_DOWNRATIO, 10));
        engineList.add(new OpitionCenterStockEngineImple(new StockLoadDataListener(
                OpitionCenterStockEngineImple.ORDER_TURNOVER), FragmentSelectStockFund.StockViewType.MARKET_STOCK_TURNOVER, 10));
        engineList.add(new OpitionCenterStockEngineImple(new StockLoadDataListener(
                OpitionCenterStockEngineImple.ORDER_AMPLITU), FragmentSelectStockFund.StockViewType.MARKET_STOCK_AMPLIT, 10));
        engineList.add(new MarketCenterStockEngineImple(new StockLoadDataListener(INLAND_INDEX),
                MarketCenterStockEngineImple.CURRENT, 3));

        plateEngine = new PlateLoadMoreEngineImpl(plateListener);
        // for (LoadMoreDataEngine mLoadDataEngine : engineList) {
        // mLoadDataEngine.loadData();
        // }
        // plateEngine.loadData();
    }

    com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener plateListener = new com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener<SectorBean>() {

        @Override
        public void loadFinish(MoreDataBean<SectorBean> object) {
            endAnimaRefresh();
            mSwipeLayout.setRefreshing(false);
            if (null != object) {
                mSecotrList.clear();
                List<SectorBean> sectorList = object.getResults();
                mSecotrList.addAll(sectorList);
                mPlateAdapter.notifyDataSetChanged();
            } else {
                // System.out.println("MoreDataBean is null");
            }

        }

        @Override
        public void loadFail() {
            if (isAdded()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endAnimaRefresh();
                        mSwipeLayout.setRefreshing(false);
                    }
                });
            }


        }

    };

    private boolean hasSetLVIncreaseHigh;
    private boolean hasSetLVDownHigh;
    private boolean hasSetLVHandoverHigh;
    private boolean hasSetLVAmpltHigh;

    class StockLoadDataListener implements LoadMoreDataEngine.ILoadDataBackListener {

        private String type;

        public StockLoadDataListener(String loadType) {
            this.type = loadType;
        }

        /**
         * @param object
         * @return
         * @Title
         * @Description TODO: (用一句话描述这个方法的功能)
         */
        @Override
        public void loadFinish(MoreDataBean object) {

            isLoading = false;
            List<SelectStockBean> dataList = Collections.EMPTY_LIST;
            if (null != object) {
                dataList = object.getResults();
            }
            if (null != dataList && !TextUtils.isEmpty(type)) {
                if (type.equals(OpitionCenterStockEngineImple.ORDER_INCREASE)) {
                    mIncreaseDataList.clear();
                    mIncreaseDataList.addAll(dataList);
                    mIncreaseAdapter.notifyDataSetChanged();
                    if (!hasSetLVIncreaseHigh) {
                        hasSetLVIncreaseHigh = true;
                        UIUtils.setListViewHeightBasedOnChildren(lvIncease);
                    }
                } else {
                    if (type.equals(OpitionCenterStockEngineImple.ORDER_DOWN)) {
                        mDownDataList.clear();
                        mDownDataList.addAll(dataList);
                        mDownAdapter.notifyDataSetChanged();
                        if (!hasSetLVDownHigh) {
                            hasSetLVDownHigh = true;
                            UIUtils.setListViewHeightBasedOnChildren(lvDown);
                        }
                    } else if (type.equals(OpitionCenterStockEngineImple.ORDER_TURNOVER)) {
                        mTurnOverDataList.clear();
                        mTurnOverDataList.addAll(dataList);
                        mTurnOverAdapter.notifyDataSetChanged();
                        if (!hasSetLVHandoverHigh) {
                            hasSetLVHandoverHigh = true;
                            UIUtils.setListViewHeightBasedOnChildren(lvHandover);
                        }
                    } else if (type.equals(OpitionCenterStockEngineImple.ORDER_AMPLITU)) {
                        mAmpliDataList.clear();
                        mAmpliDataList.addAll(dataList);
                        mAmplitAdapter.notifyDataSetChanged();
                        if (!hasSetLVAmpltHigh) {
                            hasSetLVAmpltHigh = true;
                            UIUtils.setListViewHeightBasedOnChildren(lvAmplit);
                        }
                    } else if (type.equals(INLAND_INDEX)) {
                        mIndexDataList.clear();
                        mIndexDataList.addAll(dataList);
                        mIndexAdapter.notifyDataSetChanged();
                    }
                }

            }

        }

        /**
         * @return
         * @Title
         * @Description TODO: (用一句话描述这个方法的功能)
         */
        @Override
        public void loadFail() {
            if (isAdded()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endAnimaRefresh();
                    }
                });
            }
            isLoading = false;

        }

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
                intent = new Intent(getActivity(), SelectAddOptionalActivity.class);
                break;
            case R.id.btn_more_index: {
                intent = MarketListActivity.newIntent(getActivity(), MarketListActivity.LoadViewType.IndexUp);
            }
            break;
            case R.id.btn_more_plate: {
                intent = MarketListActivity.newIntent(getActivity(), MarketListActivity.LoadViewType.PlateHot);
            }
            break;
            case R.id.btn_more_incease: {
                intent = MarketListActivity.newIntent(getActivity(), MarketListActivity.LoadViewType.StockIncease);
            }
            break;
            case R.id.btn_more_down: {
                intent = MarketListActivity.newIntent(getActivity(), MarketListActivity.LoadViewType.StockDown);
            }
            break;
            case R.id.btn_more_handover: {
                intent = MarketListActivity.newIntent(getActivity(), MarketListActivity.LoadViewType.StockTurnOver);
            }
            break;
            case R.id.btn_more_amplitude: {
                intent = MarketListActivity.newIntent(getActivity(), MarketListActivity.LoadViewType.StockAmplit);
            }
            break;

            // case R.id.btn_right_second: {
            //
            //
            // // rotateRefreshButton();
            // // quoteHandler.removeCallbacks(runnable);
            // // setupViewData();
            // // quoteHandler.postDelayed(runnable, 6 * 1000);
            // }
            // break;
            default:
                break;
        }

        if (null != intent) {
            UIUtils.startAnimationActivity(getActivity(), intent);
        }
    }

    private void startQuoteActivity(SelectStockBean itemStock) {
        UIUtils.startAnimationActivity(getActivity(), StockQuotesActivity.newIntent(getActivity(), itemStock));
        // startActivity(StockQuotesActivity.newIntent(getActivity(), itemStock));

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
            // if (null != engineList && engineList.size() > 0 && UIUtils.roundAble(engineList.get(0).getStatu())) {
            loadingAllData();
            // }
        }
    }

    private void loadingAllData() {

        if (isLoading && null != engineList) {
            return;
        }
        isLoading = true;
        for (LoadMoreDataEngine mLoadDataEngine : engineList) {
            mLoadDataEngine.loadData();
        }

        if (null != plateEngine) {
            plateEngine.loadData();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startAnimaRefresh();
            }
        });
    }


    public interface ILoadingFinishListener {
        void loadingFinish();

        void startLoadingData();
    }


    public void startAnimaRefresh() {
        if (isAdded() && getUserVisibleHint()) {
            BusProvider.getInstance().post(new RotateRefreshEvent());
        }
    }

    public void endAnimaRefresh() {
//        if (isAdded() && !getUserVisibleHinGt()) {
        BusProvider.getInstance().post(new StopRefreshEvent());
//        }
    }


}
