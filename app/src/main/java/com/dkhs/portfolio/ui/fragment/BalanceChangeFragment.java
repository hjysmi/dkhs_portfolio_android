package com.dkhs.portfolio.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.WalletChangeBean;
import com.dkhs.portfolio.bean.itemhandler.WalletChangeHandler;
import com.dkhs.portfolio.engine.LocalDataEngine.WalletExchangeEngineImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @我的钱包　交易历史
 */
public class BalanceChangeFragment extends LoadMoreListFragment {


    private List<WalletChangeBean> mDataList = new ArrayList<>();
    private WalletExchangeEngineImpl mWalletEngine = null;
    private BaseAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public BalanceChangeFragment() {

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        postDelayedeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }





    @Override
    public void requestData() {

    }

    @Override
    public void loadData() {
        mSwipeLayout.setRefreshing(true);
        setHttpHandler(getLoadEngine().loadData());


    }


    @Override
    public void onLoadMore() {
        super.onLoadMore();
    }

    @Override
    BaseAdapter getListAdapter() {
        if (null == mAdapter) {
            mAdapter =  new DKBaseAdapter(mActivity,mDataList).buildSingleItemView(new WalletChangeHandler(mActivity));
        }
        return mAdapter;
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (mWalletEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    WalletExchangeEngineImpl getLoadEngine() {

        if (null == mWalletEngine) {
            mWalletEngine = new WalletExchangeEngineImpl(this);
        }
        return mWalletEngine;
    }

    //    @Override
    SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        };
    }


    @Override
    AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        };
    }


    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public String getEmptyText() {
        return "";
    }







}
