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
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundOrderEngineImpl;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.ItemView.FundOrderItemHandler;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FriendsFragment
 * @date 2015/6/02.13:27
 * @Description
 */
public class FundOrderFragment extends LoadMoreListFragment implements MarketFundsFragment.OnRefreshI {

    private List<FundPriceBean> dataList = new ArrayList<>();
    private FundOrderEngineImpl fundOrderEngine = null;

    private DKBaseAdapter adapter;




    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    public static FundOrderFragment newInstant(String type, String sort) {

        FundOrderFragment fundsOrderFragment = new FundOrderFragment();

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
    private int allowTrade;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        sort = bundle.getString("sort");
//        mListView.setDivider(null);
        loadData();
    }

    public void refresh(){
        LogUtils.d("wys", "fund order refresh");
        loadData();
    }


    @Override
    public void requestData() {

    }

    public void setType(String type,int allowTrade){
        LogUtils.d("wys", "fund order setType");
        if(this.type != type || this.allowTrade != allowTrade){
            this.type = type;
            this.allowTrade = allowTrade;
            if(isVisible()){
                loadData();
            }
        }
    }

    @Override
    public void loadData() {

        mSwipeLayout.setRefreshing(true);
        startLoadData();
        ((FundOrderItemHandler)adapter.getItemHandler(DKBaseAdapter.DEF_VIEWTYPE)).setSortAndType(type, sort);
        setHttpHandler(getLoadEngine().loadDate(type, sort,allowTrade));
    }

    @Override
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

        if (null == adapter) {
            adapter = new DKBaseAdapter(getActivity(), dataList).buildSingleItemView(new FundOrderItemHandler(getActivity()));
        }
        return adapter;
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);
        endLoadData();
        mSwipeLayout.setRefreshing(false);
        if (fundOrderEngine.getCurrentpage() == 1) {
            dataList.clear();
        }
        dataList.addAll(object.getResults());
        adapter.notifyDataSetChanged();


    }

    @Override
    FundOrderEngineImpl getLoadEngine() {

        if (null == fundOrderEngine) {
            fundOrderEngine = new FundOrderEngineImpl(this);
        }
        return fundOrderEngine;
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

                startActivity(FundDetailActivity.newIntent(getActivity(), SelectStockBean.copy(dataList.get(position))));
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