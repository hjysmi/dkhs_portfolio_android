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
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.HomeMoreBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.itemhandler.BannerHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.HomeMoreHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.mingle.autolist.AutoList;


public class HomePageFragment extends AutoListLoadMoreListFragment implements BannerHandler.RefreshEnable {
    private AutoList<Object> mDataList = new AutoList<>().applyAction(Object.class);
    private LoadMoreDataEngine mEngine = null;
    private BaseAdapter mAdapter;

    public HomePageFragment() {
    }


    @Override
    BaseAdapter getListAdapter() {

        if (mAdapter == null) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList).buildMultiItemView(BannerTopicsBean.class, new BannerHandler(mActivity, HomePageFragment.this))
            .buildMultiItemView(HomeMoreBean.class,new HomeMoreHandler(mActivity));
        }
        return mAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDataList.setup(this);
        mDataList.setAdapter(getListAdapter());
        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }


    @Override
    LoadMoreDataEngine getLoadEngine() {
        return null;
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
        if (mEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public String getEmptyText() {
        return "";
    }

    @Override
    public void enable() {
        mSwipeLayout.setEnabled(true);
    }

    @Override
    public void disEnable() {
        mSwipeLayout.setEnabled(false);
    }
}
