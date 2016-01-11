package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.FundManagerRankingsEngineImpl;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.ui.ItemView.FMRankingItemHandler;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FundManagerRankingsFragment
 * @date 2015/6/02.13:27
 * @Description 基金经理排行
 */
public class FundManagerRankingsFragment extends LoadMoreListFragment implements MarketFundsFragment.OnRefreshI {

    private List<FundManagerBean> mDataList = new ArrayList<>();
    private FundManagerRankingsEngineImpl mFundManagerRankingsEngine = null;


    private DKBaseAdapter mAdapter;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    public static FundManagerRankingsFragment newInstant(String type, String sort) {

        FundManagerRankingsFragment fundsOrderFragment = new FundManagerRankingsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("sort", sort);
        fundsOrderFragment.setArguments(bundle);
        return fundsOrderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String type;
    private String sort;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        sort = bundle.getString("sort");
        loadData();
    }


    @Override
    public void requestData() {

    }

    public void refresh(){
        loadData();
    }

    @Override
    public void loadData() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
        startLoadData();

        if (!sort.equals("work_seniority") && !sort.equals("-work_seniority")) {
            ((FMRankingItemHandler) mAdapter.getItemHandler(DKBaseAdapter.DEF_VIEWTYPE)).setSortKey(sort);
        }

        setHttpHandler(getLoadEngine().loadDate(type, sort));
    }

    public void refresh(String type, String sort) {
        this.sort = sort;
        this.type = type;
        loadData();
    }

    @Override
    public void onLoadMore() {
        startLoadData();
        super.onLoadMore();
    }

    @Override
    BaseAdapter getListAdapter() {

        if (null == mAdapter) {
//            mAdapter = new Dk(mActivity, mDataList);
            mAdapter = new DKBaseAdapter(mActivity, mDataList).buildSingleItemView(new FMRankingItemHandler());
        }
        return mAdapter;
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        endLoadData();
        mSwipeLayout.setRefreshing(false);
        if (mFundManagerRankingsEngine.getCurrentpage() == 1) {
            mDataList.clear();
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();


    }

    @Override
    FundManagerRankingsEngineImpl getLoadEngine() {

        if (null == mFundManagerRankingsEngine) {
            mFundManagerRankingsEngine = new FundManagerRankingsEngineImpl(this);
        }
        return mFundManagerRankingsEngine;
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

                FundManagerBean fundManagerBean = mDataList.get(position);
                startActivity(FundManagerActivity.newIntent(mActivity, fundManagerBean.id + ""));
            }
        };
    }


    @Override
    public void loadFail() {
        endLoadData();
        mSwipeLayout.setRefreshing(false);
    }

    public void startLoadData() {
        if (isAdded() && getUserVisibleHint()) {
            BusProvider.getInstance().post(new RotateRefreshEvent());
        }
    }

    public void endLoadData() {
        if (isAdded() && getUserVisibleHint()) {
            BusProvider.getInstance().post(new StopRefreshEvent());
        }
    }

}