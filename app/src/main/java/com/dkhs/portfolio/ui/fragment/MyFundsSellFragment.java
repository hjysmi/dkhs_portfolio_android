package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.dkhs.portfolio.bean.FundTradeBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.itemhandler.BannerHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.MyFundsEngineImpl;
import com.dkhs.portfolio.ui.SellFundInfoActivity;
import com.dkhs.portfolio.ui.adapter.MyFundTradeAdapter;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.mingle.autolist.AutoList;

/**
 * Created by zhangcm on 2015/9/21.11:19
 */
public class MyFundsSellFragment extends AutoListLoadMoreListFragment implements BannerHandler.RefreshEnable{

    private AutoList<FundTradeBean> mDataList = new AutoList<FundTradeBean>().applyAction(FundTradeBean.class);
    private MyFundsEngineImpl mFundsEngine= null;
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
        mDataList.setup(this);
        mDataList.setAdapter(getListAdapter());
        return super.onCreateView(inflater, container, savedInstanceState);
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
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void requestData() {

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
        if (mFundsEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO 打开卖出基金详情
                startActivity(SellFundInfoActivity.getFundInfoIntent(getActivity(), mDataList.get(position).getId(),false));

            }
        };
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {

        if (null == mFundsEngine) {
            mFundsEngine = new MyFundsEngineImpl(this, 1);
        }
        return mFundsEngine;
    }

    @Override
    BaseAdapter getListAdapter() {
        if (null == mAdapter) {
//            mAdapter = new LatestTopicsAdapter(mActivity, mDataList);
            mAdapter = new MyFundTradeAdapter(getActivity(), mDataList);
        }
        return mAdapter;
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
    public String getEmptyText() {
        return "暂无基金交易记录";
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

}
