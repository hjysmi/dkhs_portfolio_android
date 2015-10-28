package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LocalDataEngine.UserRewardsEngineImpl;
import com.dkhs.portfolio.ui.MyRewardActivity;
import com.dkhs.portfolio.ui.UserTopicsActivity;
import com.dkhs.portfolio.ui.eventbus.AddTopicsEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MyTopicsFrament
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/27.
 */
public class UsersRewardFragment extends LoadMoreListFragment {

    private List<Object> mDataList = new ArrayList<>();
    private UserRewardsEngineImpl mRewardsEngine = null;
    private BaseAdapter mAdapter;


    public static UsersRewardFragment newIntent(String userId, String userName) {
        UsersRewardFragment usersTopicsFragment = new UsersRewardFragment();

        Bundle bundle = new Bundle();
        bundle.putString(UserTopicsActivity.USER_NAME, userId);
        bundle.putString(UserTopicsActivity.USER_ID, userName);
        usersTopicsFragment.setArguments(bundle);
        return usersTopicsFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setDivider(null);
        loadData();
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
    public void updateRewardList(AddTopicsEvent event){
        TopicsBean data = event.topicsBean;
        if(data != null){
            mDataList.add(0,data);
            mAdapter.notifyDataSetChanged();
        }
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
        if (mRewardsEngine == null) {
            mRewardsEngine = new UserRewardsEngineImpl(this, getArguments().getString(MyRewardActivity.USER_ID), UserRewardsEngineImpl.StatusType.REWARD);
        }
        return mRewardsEngine;
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
        mSwipeLayout.setRefreshing(true);
        setHttpHandler(getLoadEngine().loadData());
        super.loadData();
    }

    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (mRewardsEngine.getCurrentpage() == 1) {
            mDataList.clear();

        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public String getEmptyText() {
        return getResources().getString(R.string.no_reward);
    }
}
