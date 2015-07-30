package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.HotTopicEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.ui.adapter.HotTopicsAdapter;
import com.dkhs.portfolio.ui.widget.kline.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotTopicsFragment extends LoadMoreListFragment {

    private List<TopicsBean> mDataList = new ArrayList<>();
    private HotTopicEngineImpl mTopicsEngine= null;
    private BaseAdapter mAdapter;

    public HotTopicsFragment() {
    }



    @Override
    ListAdapter getListAdapter() {

        if(mAdapter == null){
            mAdapter=  new HotTopicsAdapter(mActivity,mDataList);
        }
        return mAdapter;
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if(mTopicsEngine ==null){
            mTopicsEngine=  new HotTopicEngineImpl(this);
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
        loadData();
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

}
