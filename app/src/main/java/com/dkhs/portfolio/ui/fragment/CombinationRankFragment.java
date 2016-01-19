/**
 * @Title FundsOrderFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.CombinationRankEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.ui.CombinationDetailActivity;
import com.dkhs.portfolio.ui.adapter.CombinationRankAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FundsOrderFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-29 下午4:03:33
 */
public class CombinationRankFragment extends LoadMoreListFragment {

    private static final String ARGUMENT_ORDER_TYPE = "order_type";
    public static final String ORDER_TYPE_DAY = "chng_pct_day";
    public static final String ORDER_TYPE_WEEK = "chng_pct_week";
    public static final String ORDER_TYPE_MONTH = "chng_pct_month";
    // public static final String ORDER_TYPE_SEASON = "chng_pct_three_month";
    public static final String ORDER_TYPE_ALL = "net_value";
    private String mOrderType;
    private CombinationRankAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private CombinationRankEngineImpl orderEngine;

    public static CombinationRankFragment getFragment(String orderType) {
        CombinationRankFragment fragment = new CombinationRankFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_ORDER_TYPE, orderType);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getArguments();

        if (null != bundle) {
            mOrderType = bundle.getString(ARGUMENT_ORDER_TYPE);
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    ListAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new CombinationRankAdapter(getActivity(), mDataList, mOrderType);
        }
        return mAdapter;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadData() {
        if (getUserVisibleHint()) {
            BusProvider.getInstance().post(new RotateRefreshEvent());
            setHttpHandler(getLoadEngine().loadData());
        }
    }

    /**
     * @param view
     * @param savedInstanceState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setDividerHeight(0);
    }

    @Override
    public void requestData() {

    }

    @Override
    public void loadFinish(MoreDataBean object) {

        super.loadFinish(object);
        if (getUserVisibleHint()) {
            BusProvider.getInstance().post(new StopRefreshEvent());
        }
        mSwipeLayout.setRefreshing(false);
        dismissProgress();
        if (null != object && null != object.getResults() && object.getResults().size() > 0) {
            // add by zcm -----2014.12.15
            setListViewVisible();
            // add by zcm -----2014.12.15
            // mDataList = object.getResults();
            if (isRefresh) {
                mDataList.clear();
                isRefresh = false;
            }
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
            // PromptManager.closeProgressDialog();
        }
        if (null == mDataList || mDataList.size() == 0) {
            if (mOrderType.contains(ORDER_TYPE_DAY)) {
                // modify by zcm -----2014.12.15
                setEmptyText("还没有开盘,请耐心等待");
                // modify by zcm -----2014.12.15
            } else if (mOrderType.contains(ORDER_TYPE_WEEK)) {

                setEmptyText("周排行暂无数据");
            } else if (mOrderType.contains(ORDER_TYPE_MONTH)) {

                setEmptyText("月排行暂无数据");

            } else if (mOrderType.contains(ORDER_TYPE_ALL)) {

                setEmptyText("总排行暂无数据");
            }
        }

    }

    private boolean isRefresh;

    Handler dataHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mDataList.isEmpty()) {
                loadData();
            } else {
                BusProvider.getInstance().post(new RotateRefreshEvent());
                setHttpHandler(getLoadEngine().refreshDatabySize(mDataList.size()));
                isRefresh = true;
            }
            dataHandler.postDelayed(this, 60 * 1000);
        }
    };

    @Override
    public void onPause() {

        super.onPause();
//
    }

    @Override
    public void onResume() {
        super.onResume();
//

    }


    @Override
    public void onViewHide() {
        super.onViewHide();
        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
    }

    @Override
    public void onViewShow() {
        super.onViewShow();
        dataHandler.removeCallbacks(runnable);
        showProgress();
        dataHandler.postDelayed(runnable, 60);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            Bundle bundle = getArguments();
            if (null != bundle) {
                mOrderType = bundle.getString(ARGUMENT_ORDER_TYPE);
            }
//            // loadData();
//            dataHandler.postDelayed(runnable, 60);

        } else {
//            dataHandler.removeCallbacks(runnable);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (null == orderEngine) {
            orderEngine = new CombinationRankEngineImpl(this, mOrderType);
        }
        return orderEngine;
    }

    @Override
    OnItemClickListener getItemClickListener() {

        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CombinationBean selectBean = mDataList.get(position);
                if (null != MarketCombinationFragment.mVisitorData && !PortfolioApplication.hasUserLogin()) {
                    selectBean.setFollowed(MarketCombinationFragment.mVisitorData.contains(selectBean));
                }
                startActivity(CombinationDetailActivity.newIntent(getActivity(), selectBean));

            }
        };
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_funds);

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    OnRefreshListener setOnRefreshListener() {
        return new OnRefreshListener() {

            @Override
            public void onRefresh() {
                BusProvider.getInstance().post(new RotateRefreshEvent());
                setHttpHandler(getLoadEngine().refreshDatabySize(mDataList.size()));
                isRefresh = true;
            }
        };
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);
        dismissProgress();
        isRefresh = false;

    }

}
