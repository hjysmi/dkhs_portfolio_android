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
import android.widget.ListAdapter;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.HotTopicEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.ui.adapter.HotTopicsAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdateTopicsListEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotTopicsFragment extends LoadMoreListFragment {

    private List<TopicsBean> mDataList = new ArrayList<>();
    private HotTopicEngineImpl mTopicsEngine = null;
    private BaseAdapter mAdapter;

    public HotTopicsFragment() {
    }


    @Override
    ListAdapter getListAdapter() {

        if (mAdapter == null) {
            mAdapter = new HotTopicsAdapter(mActivity, mDataList);
        }
        return mAdapter;
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
    public void updateList(UpdateTopicsListEvent updateTopicsListEvent) {

        TopicsBean topicsBean = updateTopicsListEvent.topicsBean;

        for (Object object : mDataList) {

            if (object instanceof TopicsBean) {
                TopicsBean topicsBean1 = (TopicsBean) object;

                if (topicsBean.id == topicsBean1.id) {
                    topicsBean1.favorites_count = topicsBean.favorites_count;
                    topicsBean1.attitudes_count = topicsBean.attitudes_count;
                    topicsBean1.comments_count = topicsBean.comments_count;
                    topicsBean1.like = topicsBean.like;
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }

    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (mTopicsEngine == null) {
            mTopicsEngine = new HotTopicEngineImpl(this);
        }
        return mTopicsEngine;
    }

    @Override
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
        return null;
    }

    @Override
    public void requestData() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setDivider(null);
        postDelayedeData();

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
    public String getEmptyText() {
        return "暂无话题";
    }
}
