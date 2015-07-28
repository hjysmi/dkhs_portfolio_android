package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.dkhs.adpter.adapter.AutoAdapter;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.engine.HotTopicEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.TopicsEngineImpl;
import com.dkhs.portfolio.ui.FloatingActionMenu;
import com.dkhs.portfolio.ui.adapter.TopicsDetailAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MyTopicsFrament
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/27.
 */
public class UsersTopicsFrament extends  LoadMoreListFragment {

    private List<Object> mDataList = new ArrayList<>();
    private TopicsEngineImpl mTopicsEngine= null;
    private BaseAdapter mAdapter;




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Override
    ListAdapter getListAdapter() {

        if(mAdapter == null){
            mAdapter=  new AutoAdapter(mActivity,mDataList) {
                @Override
                protected void initHandlers(HashMap<Integer, ItemHandler> itemHandlerHashMap) {
                    addHandler(0,new TopicsHandler(mActivity));
                }

                @Override
                protected int getViewType(int position) {
                    return 0;
                }
            };
        }
        return mAdapter;
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if(mTopicsEngine ==null){
            mTopicsEngine=  new TopicsEngineImpl(this);
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
            mDataList.add(mAdapter);
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }
}
