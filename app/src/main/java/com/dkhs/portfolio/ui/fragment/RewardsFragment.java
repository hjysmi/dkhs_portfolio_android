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

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.itemhandler.RewardsHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LocalDataEngine.RewardEngineImpl;
import com.mingle.autolist.AutoData;
import com.mingle.autolist.AutoList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsFragment extends LoadMoreListFragment  {

    private AutoList<TopicsBean> mDataList = new AutoList<>().applyAction(TopicsBean.class);
    private RewardEngineImpl mRewardEngine = null;
    private BaseAdapter mAdapter;

    public RewardsFragment() {
    }


    @Override
    public int setContentLayoutId() {
        return R.layout.empty_listview_reward;
    }
    @Override
    BaseAdapter getListAdapter() {

        if (mAdapter == null) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList).
                 buildSingleItemView(new RewardsHandler(mActivity));
        }
        return mAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDataList.setup(this);
        mDataList.setAdapter(getListAdapter());
        mDataList.setActionHandler(new AutoList.ActionHandler<AutoData>() {
            @Override
            public boolean beforeHandleAction(AutoData a) {

                if (a.action == AutoData.Action.Update) {

                    int index = mDataList.indexOf(a);
                    if (index != -1) {
                        TopicsBean topicsBean = mDataList.get(index);
                        topicsBean.favorites_count = ((TopicsBean) a).favorites_count;
                        topicsBean.attitudes_count = ((TopicsBean) a).attitudes_count;
                        topicsBean.comments_count = ((TopicsBean) a).comments_count;
                        topicsBean.like = ((TopicsBean) a).like;
                    }
                    return true;
                }
                return true;
            }

            @Override
            public void afterHandleAction(AutoData a) {

            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }




    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (mRewardEngine == null) {
            mRewardEngine = new RewardEngineImpl(this);
        }
        return mRewardEngine;
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
        if (mRewardEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public String getEmptyText() {
        return "暂无悬赏";
    }

}
