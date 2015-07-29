package com.dkhs.portfolio.ui.fragment;

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
import com.dkhs.portfolio.ui.FloatingActionMenu;
import com.dkhs.portfolio.ui.adapter.TopicsDetailAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopicDetailFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/27.
 */
public class TopicDetailFragment extends  LoadMoreListFragment {
    private TopicsBean mTopicsBean;
    private List<Object> mDataList = new ArrayList<>();
    private HotTopicEngineImpl mTopicsEngine= null;
    private BaseAdapter mAdapter;




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handExtraIntent();
        mDataList.add(mTopicsBean);
//        loadData();
    }

    private void handExtraIntent() {

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mTopicsBean= Parcels.unwrap(extras.getParcelable("topicsBean"));
        }

    }


    @Override
    ListAdapter getListAdapter() {

        if(mAdapter == null){
            mAdapter=  new TopicsDetailAdapter(mActivity,mDataList);
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
            mDataList.add(mTopicsEngine);
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

}
