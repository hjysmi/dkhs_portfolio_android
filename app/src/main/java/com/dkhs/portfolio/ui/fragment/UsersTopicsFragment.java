package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.UserTopicsCommentEngineImpl;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.UserTopicsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName UsersTopicsFragment
 * @Description 我的悬赏，我的话题
 * @date 2015/7/27.
 */
public class UsersTopicsFragment extends LoadMoreListFragment {

    private List<Object> mDataList = new ArrayList<>();
    private UserTopicsCommentEngineImpl mTopicsEngine = null;
    private BaseAdapter mAdapter;
    private int mContentType;


    public static UsersTopicsFragment newIntent(String userId, String userName,int contentType) {
        UsersTopicsFragment usersTopicsFragment = new UsersTopicsFragment();

        Bundle bundle = new Bundle();
        bundle.putString(UserTopicsActivity.USER_NAME, userId);
        bundle.putString(UserTopicsActivity.USER_ID, userName);
        bundle.putInt(UserTopicsActivity.CONTENT_TYPE, contentType);
        usersTopicsFragment.setArguments(bundle);
        return usersTopicsFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setDivider(null);
        mContentType = getArguments().getInt(UserTopicsActivity.CONTENT_TYPE);
        loadData();
    }

    @Override
    ListAdapter getListAdapter() {

        if (mAdapter == null) {
//            mAdapter = new AutoAdapter(mActivity, mDataList) {
//
//                @Override
//                protected int getViewType(int position) {
//                    return new TopicsHandler(mActivity).getLayoutResId();
//                }
//            }.buildItemView(new TopicsHandler(mActivity));
            mAdapter = new DKBaseAdapter(mActivity, mDataList).buildSingleItemView(new TopicsHandler(mActivity));
        }
        return mAdapter;
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (mTopicsEngine == null) {
            mTopicsEngine = new UserTopicsCommentEngineImpl(this, getArguments().getString(UserTopicsActivity.USER_ID), UserTopicsCommentEngineImpl.StatusType.Topics,mContentType);
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
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void loadData() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
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
        if(mContentType == TopicsDetailActivity.TYPE_TOPIC){
            return getResources().getString(R.string.no_bbs_topic);
        }else{
            return getResources().getString(R.string.no_reward);
        }
    }
}
