package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.TopicsEngineImpl;
import com.dkhs.portfolio.ui.adapter.HotTopicsAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.dkhs.adpter.adapter.AutoLoadMoreRVAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotTopicsFragment extends LoadMoreListFragment {




    private List<TopicsBean> mDataList = new ArrayList<>();
    private TopicsEngineImpl mTopicsEngine= null;


    private BaseAdapter mAdapter;



    public HotTopicsFragment() {
    }
    @Override
    ListAdapter getListAdapter() {

        if(mAdapter !=null){
            mAdapter=  new HotTopicsAdapter(mActivity,mDataList);
        }
        return mAdapter;
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if(mTopicsEngine !=null){
            mTopicsEngine=  new TopicsEngineImpl(this);
        }
        return mTopicsEngine;
    }

    @Override
    SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {
        loadData();
        return null;
    }

    @Override
    AdapterView.OnItemClickListener getItemClickListener() {
        return null;
    }

    @Override
    public void requestData() {


    }

    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void loadData() {
        mSwipeLayout.setRefreshing(true);
        setHttpHandler(getLoadEngine().loadData());
        super.loadData();
    }
}
