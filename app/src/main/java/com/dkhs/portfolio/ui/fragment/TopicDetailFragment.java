package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.LoadingBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NoDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.engine.BaseInfoEngine;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.adapter.TopicsDetailAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.LikesPeopleEvent;
import com.dkhs.portfolio.ui.eventbus.TopicsDetailRefreshEvent;
import com.mingle.autolist.AutoData;
import com.mingle.autolist.AutoList;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopicDetailFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/27.
 */
public class TopicDetailFragment extends AutoListLoadMoreListFragment  {

    private TopicsBean mTopicsBean;
    private AutoList<Object> mDataList = new AutoList<>();
    private TopicsCommendEngineImpl mTopicsCommendEngine = null;
    private TopicsDetailAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private boolean mScrollToComment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(TopicsBean topicsBean);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handExtraIntent();

        mDataList.add(mTopicsBean);
        mDataList.add(new LoadingBean());
        mDataList.setup(this);
        mDataList.setAdapter(getListAdapter());
        mDataList.setActionHandler(new AutoList.ActionHandler<AutoData>() {
            @Override
            public boolean beforeHandleAction(AutoData a) {

                if(mSortType == TopicsCommendEngineImpl.SortType.like){

                    return true;
                }
                if(a.action== AutoData.Action.Add){
                    if (mDataList.get(1) instanceof NoDataBean) {
                        mDataList.remove(1);
                    }
                    mDataList.addT(1, a);
                    mTopicsBean.comments_count += 1;
                    return true;
                }else  if(a.action== AutoData.Action.Delete){
                    mDataList.deleteT(a);
                    mTopicsBean.comments_count -= 1;
                    if (mDataList.size() == 1) {
                        NoDataBean noDataBean = new NoDataBean();
                        noDataBean.noData = "暂无评论";
                        mDataList.add(1, noDataBean);
                    }
                    return true;
                }

                return false;
            }

            @Override
            public void afterHandleAction(AutoData a) {

            }
        });
        mSwipeLayout.setRefreshing(false);

        mListView.setDivider(null);


        ((TopicsDetailActivity) getActivity()).mFloatingActionMenu.attachToListViewTop(mListView, null, null);
        loadData();
        mListView.smoothScrollToPosition(1);
        mListView.setOnLoadListener(this);
        mListView.setAutoLoadMore(false);
        if (mScrollToComment) {

            //// FIXME: 2015/7/31  滑动到帖子位置
//            mListView.smoothScrollToPosition(1);
//            mListView.scrollBy(0,-50);



        }

//        View v = new View(mActivity);
//        v.setBackgroundColor(Color.BLUE);
//        v.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.floating_action_menu_item_height)));
//        mListView.addFooterView(v);
    }

    TopicsCommendEngineImpl.SortType mSortType;

    @Subscribe
    public void refresh(TopicsDetailRefreshEvent topicsDetailRefreshEvent) {
        loadData(topicsDetailRefreshEvent.sortType);
        mSortType=topicsDetailRefreshEvent.sortType;
    }


    private void handExtraIntent() {

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mTopicsBean = Parcels.unwrap(extras.getParcelable("topicsBean"));
            mScrollToComment = getActivity().getIntent().getBooleanExtra("scrollToComment", false);
        }
    }

    @Override
    TopicsDetailAdapter getListAdapter() {

        if (mAdapter == null) {
            mAdapter = new TopicsDetailAdapter(mActivity, mDataList);
        }
        return mAdapter;
    }

    @Override
    TopicsCommendEngineImpl getLoadEngine() {
        if (mTopicsCommendEngine == null) {
            mTopicsCommendEngine = new TopicsCommendEngineImpl(this, mTopicsBean.id + "");

        }
        return mTopicsCommendEngine;
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
        BaseInfoEngine.getTopicsDetail(mTopicsBean.id + "", new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return TopicsBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                mSwipeLayout.setRefreshing(false);
                mTopicsBean = (TopicsBean) object;
                if (mListener != null)
                    mListener.onFragmentInteraction(mTopicsBean);
                if (mDataList.size() > 0 && mDataList.get(0) instanceof TopicsBean) {
                    mDataList.remove(0);
                    mDataList.add(0, mTopicsBean);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                super.onFailure(errCode, errMsg);
                mSwipeLayout.setRefreshing(false);
            }
        });
        mSwipeLayout.setRefreshing(true);
        setHttpHandler(getLoadEngine().loadData());
        super.loadData();
    }

    public void loadData(TopicsCommendEngineImpl.SortType sortType) {
        mSwipeLayout.setRefreshing(true);
        getLoadEngine().loadData(sortType);
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        if (isAdded()) {
            mListView.onLoadMoreComplete();
            if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
                mListView.setCanLoadMore(false);
                mListView.setAutoLoadMore(false);
            } else {
                mListView.setCanLoadMore(true);
                mListView.setAutoLoadMore(true);
                if (getLoadEngine().getCurrentpage() == 1)
                    mListView.setOnLoadListener(this);
            }
        }
        mSwipeLayout.setRefreshing(false);
        if (mTopicsCommendEngine.getCurrentpage() == 1) {
            mDataList.clear();
            mDataList.add(mTopicsBean);
            if (object.getResults().size() == 0) {
                NoDataBean noDataBean = new NoDataBean();
                if (mTopicsCommendEngine.isLikes()) {
                    noDataBean.noData = "暂无人点赞";
                } else {
                    noDataBean.noData = "暂无评论";
                }
                mDataList.add(noDataBean);
            }
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

    public void addLikePeople(UserEntity userEntity) {
        if(mSortType != TopicsCommendEngineImpl.SortType.like){
            return ;
        }
        if (mDataList.size() > 1) {
            if (mDataList.get(1) instanceof NoDataBean) {
                mDataList.remove(1);
            }

            boolean had = false;
            for (Object userEntity1 : mDataList) {
                if (userEntity1 instanceof UserEntity) {
                    if (((UserEntity) userEntity1).getId() == userEntity.getId()) {
                        had = true;
                        break;
                    }
                }
            }
            if (!had) {
                mDataList.add(1, userEntity);
            }
        }
//        mTopicsBean.attitudes_count =mDataList.size()-1;

    }

    public void removeLikePeople(UserEntity userEntity) {
        if(mSortType != TopicsCommendEngineImpl.SortType.like){

            return ;
        }


        for (Object o : mDataList) {
            if (o instanceof UserEntity) {
                if (((UserEntity) o).getId() == userEntity.getId()) {
                    mDataList.remove(o);
                    if (mDataList.size() == 1) {
                        NoDataBean noDataBean = new NoDataBean();
                        noDataBean.noData = "暂无人点赞";
                        mDataList.add(1, noDataBean);
                    }
                    break;
                }
            }
        }
//        mTopicsBean.attitudes_count =mDataList.size()-1;

    }


    public void like() {

        if (mTopicsBean != null) {
            mTopicsBean.attitudes_count += 1;
            addLikePeople(UserEngineImpl.getUserEntity());
            mAdapter.notifyDataSetChanged();

        }

    }

    public void unLike() {
        if (mTopicsBean != null) {
            mTopicsBean.attitudes_count -= 1;
            removeLikePeople(UserEngineImpl.getUserEntity());
            mAdapter.notifyDataSetChanged();
        }
    }


}
