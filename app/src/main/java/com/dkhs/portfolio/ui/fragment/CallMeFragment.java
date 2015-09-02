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
import com.dkhs.portfolio.bean.LikeBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CommentHandler;
import com.dkhs.portfolio.engine.CallMeEngineImpl;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.DeleteCommentEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @我界面
 */
public class CallMeFragment extends LoadMoreListFragment {


    public CallMeFragment() {

    }

    private List<LikeBean> mDataList = new ArrayList<>();
    private CallMeEngineImpl mTopicsEngine = null;
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
    public void deleteSuccessUpdate(DeleteCommentEvent event) {
        for (LikeBean likeBean : mDataList) {
            if (likeBean.status_type == 1 && (likeBean.getId() + "").equals(event.commentId)) {
                mDataList.remove(likeBean);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void requestData() {

    }

    @Override
    public void loadData() {
        mSwipeLayout.setRefreshing(true);
        setHttpHandler(getLoadEngine().loadDate());
    }


    @Override
    public void onLoadMore() {
        super.onLoadMore();
    }

    @Override
    BaseAdapter getListAdapter() {

        if (null == mAdapter) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList) {
                @Override
                protected int getViewType(int position) {

                    if (mDataList.get(position).status_type == 0) {
                        return 0;
                    } else if (mDataList.get(position).status_type == 1) {
                        return 1;
                    } else if (mDataList.get(position).status_type == 2) {
                        return 2;
                    }
                    return mDataList.get(position).status_type;
                }
            }.buildCustonTypeItemView(0, new TopicsHandler(mActivity))
                    .buildCustonTypeItemView(1, new CommentHandler(mActivity).setReplyComment(true))
                    .buildCustonTypeItemView(2, new TopicsHandler(mActivity));

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
    CallMeEngineImpl getLoadEngine() {

        if (null == mTopicsEngine) {
            mTopicsEngine = new CallMeEngineImpl(this);
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
        return "暂无人@提到你";
    }


}
