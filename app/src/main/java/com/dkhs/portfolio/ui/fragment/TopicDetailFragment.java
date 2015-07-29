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

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.engine.BaseInfoEngine;
import com.dkhs.portfolio.engine.HotTopicEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.TopicsCommendEngineImpl;
import com.dkhs.portfolio.net.SimpleParseHttpListener;
import com.dkhs.portfolio.ui.FloatingActionMenu;
import com.dkhs.portfolio.ui.adapter.TopicsDetailAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handExtraIntent();
        mDataList.add(mTopicsBean);
        mSwipeLayout.setRefreshing(false);
        BaseInfoEngine.getTopicsDetail(mTopicsBean.id + "", new SimpleParseHttpListener() {
            @Override
            public Class getClassType() {
                return TopicsBean.class;
            }

            @Override
            protected void afterParseData(Object object) {
                mTopicsBean = (TopicsBean) object;


                if (mDataList.size() > 0 && mDataList.get(0) instanceof TopicsBean) {
                    mDataList.remove(0);
                    mDataList.add(0, mTopicsBean);
                }

                mAdapter.notifyDataSetChanged();
                mSwipeLayout.setRefreshing(true);

            }
        });
        loadData(TopicsCommendEngineImpl.SortType.latest);
    }


    @Subscribe
    public void refresh(TopicsDetailRefreshEvent topicsDetailRefreshEvent){
        loadData(topicsDetailRefreshEvent.sortType);
    }

    private void handExtraIntent() {

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mTopicsBean = Parcels.unwrap(extras.getParcelable("topicsBean"));
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
            mTopicsCommendEngine = new TopicsCommendEngineImpl(this);
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
        setHttpHandler(getLoadEngine().loadData());
        super.loadData();
    }
    public void loadData(TopicsCommendEngineImpl.SortType sortType) {
        mSwipeLayout.setRefreshing(true);
        getLoadEngine().loadData(sortType);
    }

    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (mTopicsCommendEngine.getCurrentpage() == 1) {
            mDataList.clear();
            mDataList.add(mTopicsBean);
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

}
