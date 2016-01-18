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
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.itemhandler.BannerHandler;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.engine.HotTopicEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.ReConNetEvent;
import com.dkhs.portfolio.ui.eventbus.TopEvent;
import com.dkhs.portfolio.ui.eventbus.TopicSortTypeEvent;
import com.mingle.autolist.AutoData;
import com.mingle.autolist.AutoList;
import com.squareup.otto.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotTopicsFragment extends AutoListLoadMoreListFragment implements BannerHandler.RefreshEnable {

    private AutoList<TopicsBean> mDataList = new AutoList<>().applyAction(TopicsBean.class);
    private HotTopicEngineImpl mTopicsEngine = null;
    private BaseAdapter mAdapter;

    public HotTopicsFragment() {
    }


    @Override
    BaseAdapter getListAdapter() {

        if (mAdapter == null) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList).
                    buildMultiItemView(BannerTopicsBean.class, new BannerHandler(mActivity, HotTopicsFragment.this))
                    .buildMultiItemView(TopicsBean.class, new TopicsHandler(mActivity));
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
                switch (a.action) {
                    case Add:
                        if(mTopicsEngine.getSortType() == 1){//当选择最新排序时，话题列表刷新用户发表的新话题
                            mDataList.addT(1,a);
                        }
                        break;
                    case Delete:
                        mDataList.deleteT(a);
                        break;
                }
            }
        });
        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void forward2Top(TopEvent event){
        if(event != null && isVisible()&& getUserVisibleHint()){
            if(mListView != null){
                mListView.smoothScrollToPosition(0);
            }
        }
    }

//    @Subscribe
//    public void updateList(UpdateTopicsListEvent updateTopicsListEvent) {
//
//        TopicsBean topicsBean = updateTopicsListEvent.topicsBean;
//
//        for (Object object : mDataList) {
//
//            if (object instanceof TopicsBean) {
//                TopicsBean topicsBean1 = (TopicsBean) object;
//
//                if (topicsBean.id == topicsBean1.id) {
//                    topicsBean1.favorites_count = topicsBean.favorites_count;
//                    topicsBean1.attitudes_count = topicsBean.attitudes_count;
//                    topicsBean1.comments_count = topicsBean.comments_count;
//                    topicsBean1.like = topicsBean.like;
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                }
//            }
//        }
//
//    }

    @Subscribe
    public void updateList(TopicSortTypeEvent topicSortTypeEvent){
        mTopicsEngine.loadData(topicSortTypeEvent.sortType);
    }

    @Subscribe
    public void netChange(ReConNetEvent event) {
        if(mDataList == null || mDataList.size() == 0){
            loadData();
        }
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (mTopicsEngine == null) {
            mTopicsEngine = new HotTopicEngineImpl(this);
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
        if (mTopicsEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public String getEmptyText() {
        return "暂无话题";
    }

    @Override
    public void enable() {
        mSwipeLayout.setEnabled(true);
    }

    @Override
    public void disEnable() {
        mSwipeLayout.setEnabled(false);
    }

    @Override
    public void postDelayedeData() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((HotTopicEngineImpl) getLoadEngine()).loadCacheData();
                loadData();
            }
        }, 500);
    }

    @Override
    public int getPageStatisticsStringId(){
        return R.string.statistics_topics;
    }
}
