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
import com.dkhs.portfolio.bean.itemhandler.SpinnerHandler;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LocalDataEngine.RewardEngineImpl;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.eventbus.AddTopicsEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.TopicStateEvent;
import com.dkhs.portfolio.ui.eventbus.TopicSortTypeEvent;
import com.mingle.autolist.AutoData;
import com.mingle.autolist.AutoList;
import com.squareup.otto.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsFragment extends LoadMoreListFragment  {

    /**
     * 最近发布
     */
    public static final int SORT_LATEST = 0;
    /**
     * 赏金最高
     */
    public static final int SORT_HIGHEST = 1;
    /**
     * 悬赏中
     */
    public static final int SORT_REWARDING = 2;
    private AutoList<TopicsBean> mDataList = new AutoList<>().applyAction(TopicsBean.class);
    private RewardEngineImpl mRewardEngine = null;
    private BaseAdapter mAdapter;
    private int mSortType = SORT_LATEST;

    public RewardsFragment() {
    }


    @Override
    public int setContentLayoutId() {
        return R.layout.empty_listview_reward;
    }
    @Override
    BaseAdapter getListAdapter() {

        if (mAdapter == null) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList){
                @Override
                protected int getViewType(int position) {
                    if(position == 0){
                        return 0;
                    }else{
                        return 1;
                    }
                }
            }.
                    buildCustonTypeItemView(0,new SpinnerHandler(mActivity,mSortType))
            .buildCustonTypeItemView(1,new TopicsHandler(mActivity));
        }
        return mAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mDataList.setup(this);
        mDataList.setAdapter(getListAdapter());
        BusProvider.getInstance().register(this);
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
        setHttpHandler(((RewardEngineImpl)getLoadEngine()).loadData(mSortType));
        super.loadData();
    }

    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (mRewardEngine.getCurrentpage() == 1) {
            mDataList.clear();
            mDataList.add(new TopicsBean());
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public String getEmptyText() {
        return "暂无悬赏";
    }

    @Subscribe
    public void updateRewardList(AddTopicsEvent event){
        TopicsBean data = event.topicsBean;
        if(data != null&&data.content_type == TopicsDetailActivity.TYPE_REWARD){
            //第一个数据为SpinnerHandler处理，所以应该置于索引1
            mDataList.add(1,data);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     *用于刷新悬赏状态变化
     */
    @Subscribe
    public void updateRewardState(TopicStateEvent event){
        int topicId = event.id;
        int state = event.state;
        for(TopicsBean bean :mDataList){
            if(bean.id == topicId){
                bean.reward_state = state;
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void updateList(TopicSortTypeEvent topicSortTypeEvent){
        mSortType = topicSortTypeEvent.sortType;
        loadData();
    }
    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }
}
