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

import com.dkhs.adpter.adapter.AutoAdapter;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.CommentHandler;
import com.dkhs.portfolio.engine.CommentMeEngineImpl;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.DeleteCommentEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentMeFragment extends LoadMoreListFragment {


    public CommentMeFragment() {
    }

    private List<CommentBean> mDataList = new ArrayList<>();
    private CommentMeEngineImpl mTopicsEngine = null;
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
            mAdapter = new AutoAdapter(mActivity, mDataList) {
                @Override
                protected void initHandlers(HashMap<Integer, ItemHandler> itemHandlerHashMap) {
                    addHandler(0, new CommentHandler(true).setReplyComment(true));
                }

                @Override
                protected int getViewType(int position) {
                    return 0;
                }
            };
        }
        return mAdapter;
    }


    @Subscribe
    public void deleteSuccessUpdate(DeleteCommentEvent event) {

        for (CommentBean commentBean : mDataList) {
            if ((commentBean.getId()+"").equals(event.commentId)) {
                mDataList.remove(commentBean);
            }
        }
        mAdapter.notifyDataSetChanged();
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
    CommentMeEngineImpl getLoadEngine() {

        if (null == mTopicsEngine) {
            mTopicsEngine = new CommentMeEngineImpl(this);
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
        return "暂无人评论你";
    }


}
