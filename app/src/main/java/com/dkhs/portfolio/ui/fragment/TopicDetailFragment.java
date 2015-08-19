package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.dkhs.portfolio.bean.CommentBean;
import com.dkhs.portfolio.bean.LoadingBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NoDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.BaseInfoEngine;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.adapter.TopicsDetailAdapter;
import com.dkhs.portfolio.ui.eventbus.AddCommentEvent;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.DeleteCommentEvent;
import com.dkhs.portfolio.ui.eventbus.TopicsDetailRefreshEvent;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopicDetailFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/27.
 */
public class TopicDetailFragment extends LoadMoreListFragment {
    private TopicsBean mTopicsBean;
    private List<Object> mDataList = new ArrayList<>();
    private TopicsCommendEngineImpl mTopicsCommendEngine = null;
    private BaseAdapter mAdapter;
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
        mSwipeLayout.setRefreshing(false);

        mListView.setDivider(null);
//        View v = new View(mActivity);
//        v.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.floating_action_menu_item_height)));
//        mListView.addFooterView(v);

        ((TopicsDetailActivity) getActivity()).mFloatingActionMenu.attachToListViewTop(mListView, null, null);
        loadData();

        if (mScrollToComment) {

            //// FIXME: 2015/7/31  滑动到帖子位置
//            mListView.smoothScrollToPosition(1);
//            mListView.scrollBy(0,-50);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Subscribe
    public void refresh(TopicsDetailRefreshEvent topicsDetailRefreshEvent) {
        loadData(topicsDetailRefreshEvent.sortType);
    }

    @Subscribe
    public void delete(DeleteCommentEvent deleteCommentEvent) {
        for (Object o : mDataList) {
            if (o instanceof CommentBean) {
                if ((((CommentBean) o).getId() + "").equals(deleteCommentEvent.commentId)) {
                    mDataList.remove(o);
                    mTopicsBean.comments_count -= 1;
                    if (mDataList.size() == 1) {
                        NoDataBean noDataBean = new NoDataBean();
                        noDataBean.noData = "暂无评论";
                        mDataList.add(1, noDataBean);
                    }
                    mAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Subscribe
    public void add(AddCommentEvent addCommentEvent) {

        if (mDataList.size() > 1) {


            if (mDataList.get(1) instanceof NoDataBean) {
                mDataList.remove(1);
            }
            mTopicsBean.comments_count += 1;
            mDataList.add(1, addCommentEvent.commentBean);
            mAdapter.notifyDataSetChanged();
        }

    }


    private void handExtraIntent() {

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mTopicsBean = Parcels.unwrap(extras.getParcelable("topicsBean"));
            mScrollToComment = getActivity().getIntent().getBooleanExtra("scrollToComment", false);
        }
    }

    @Override
    ListAdapter getListAdapter() {

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
            //        BaseInfoEngine.getTopicsDetail(  "1756323", new SimpleParseHttpListener() {
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
//                mListView.setAutoLoadMore(false);
            } else {
                mListView.setCanLoadMore(true);
//                mListView.setAutoLoadMore(true);
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
                noDataBean.noData = "暂无评论";
                mDataList.add(noDataBean);
            }
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }


    public void like() {

        if (mTopicsBean != null) {
            mTopicsBean.attitudes_count += 1;
            mAdapter.notifyDataSetChanged();

        }

    }

    public void unLike() {
        if (mTopicsBean != null) {
            mTopicsBean.attitudes_count -= 1;
            mAdapter.notifyDataSetChanged();
        }
    }


}
