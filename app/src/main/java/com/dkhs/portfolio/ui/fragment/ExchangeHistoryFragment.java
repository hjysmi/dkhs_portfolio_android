package com.dkhs.portfolio.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ExchangeHistoryBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LocalDataEngine.ExchangeHistoryEngineImpl;
import com.dkhs.portfolio.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @我界面
 */
public class ExchangeHistoryFragment extends LoadMoreListFragment {


    public ExchangeHistoryFragment() {

    }

    private List<ExchangeHistoryBean> mDataList = new ArrayList<>();
    private ExchangeHistoryEngineImpl mExchangeEngine = null;
    private BaseAdapter mAdapter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postDelayedeData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }





    @Override
    public void requestData() {

    }

    @Override
    public void loadData() {
        mSwipeLayout.setRefreshing(true);
        setHttpHandler(getLoadEngine().loadData());


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
                    if(position == 0){
                        return 0;
                    }
                    return 1;
                }

            }.buildCustonTypeItemView(1,new ExchangeHistoryHandler(getActivity())).buildCustonTypeItemView(0,new ExchangeHistoryTitleHandler(getActivity()));
        }
        return mAdapter;
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (mExchangeEngine.getCurrentpage() == 1) {
            mDataList.clear();
            ExchangeHistoryBean bean2 = new ExchangeHistoryBean();
            bean2.created_at = "06-12";
            bean2.status = 2;
            bean2.package_amount = 500;
            mDataList.add(0, bean2);
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    ExchangeHistoryEngineImpl getLoadEngine() {

        if (null == mExchangeEngine) {
            mExchangeEngine = new ExchangeHistoryEngineImpl(this);
        }
        return mExchangeEngine;
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
        return "暂无兑换纪录";
    }


    static class ExchangeHistoryHandler extends SimpleItemHandler<ExchangeHistoryBean> {
        private Context mContext;

        public ExchangeHistoryHandler(Context context) {
            mContext = context;
        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_exchange_list;
        }

        @Override
        public void onBindView(ViewHolder vh, ExchangeHistoryBean data, int position) {
            vh.setTextView(R.id.exchange_time_tv, TimeUtils.getSimpleDay(data.created_at));
            vh.setTextView(R.id.exchange_amount_tv,"+"+data.package_amount+"");
            vh.setTextView(R.id.exchange_stauts_tv,data.status_explain+"");
        }
    }

    static class ExchangeHistoryTitleHandler extends SimpleItemHandler<ExchangeHistoryBean> {
        private Context mContext;

        public ExchangeHistoryTitleHandler(Context context) {
            mContext = context;
        }

        @Override
        public int getLayoutResId() {
            return R.layout.item_exchange_list_title;
        }
    }



}
