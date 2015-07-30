package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.LatestTopicsEngineImpl;
import com.dkhs.portfolio.ui.adapter.LatestTopicsAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdateTopicsListEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestTopicsFragment extends LoadMoreListFragment {


    public LatestTopicsFragment(){

    }

    private List<TopicsBean> mDataList = new ArrayList<>();
    private LatestTopicsEngineImpl mTopicsEngine= null;


    private BaseAdapter mAdapter;



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setDivider(null);
        postDelayedeData();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void updateList(UpdateTopicsListEvent updateTopicsListEvent){

        TopicsBean topicsBean=updateTopicsListEvent.topicsBean;

        for(TopicsBean topicsBean1: mDataList){
            if( topicsBean.id ==topicsBean1.id){
                topicsBean1.favorites_count=topicsBean.favorites_count;
                topicsBean1.like=topicsBean.like;
                mAdapter.notifyDataSetChanged();
                break;
            }
        }

    }



    @Override
    public void requestData() {

    }

    @Override
    public void loadData() {

        mSwipeLayout.setRefreshing(true);

        setHttpHandler(getLoadEngine().loadDate( ));
    }


    @Override
    public void onLoadMore() {
        super.onLoadMore();
    }

    @Override
    BaseAdapter getListAdapter() {

        if (null == mAdapter) {
            mAdapter = new LatestTopicsAdapter(mActivity, mDataList);
        }
        return mAdapter;
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (mTopicsEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    LatestTopicsEngineImpl getLoadEngine() {

        if (null == mTopicsEngine) {
            mTopicsEngine = new LatestTopicsEngineImpl(this);
        }
        return mTopicsEngine;
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
        return "暂无话题";
    }


}
