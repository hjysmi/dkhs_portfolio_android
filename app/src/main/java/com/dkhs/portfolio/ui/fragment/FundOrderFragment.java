package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.FundOrderEngineImpl;
import com.dkhs.portfolio.ui.FriendsOrFollowersActivity;
import com.dkhs.portfolio.ui.adapter.FundOrderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FriendsFragment
 * @date 2015/6/02.13:27
 * @Description
 */
public class FundOrderFragment extends LoadMoreListFragment {

    private List<FundPriceBean> dataList = new ArrayList<>();
    private FundOrderEngineImpl fundOrderEngine = null;

    private FundOrderAdapter adapter;

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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        sort = bundle.getString("sort");
        loadData();

    }

    @Override
    public void loadData() {

//        mSwipeLayout.setRefreshing(true);
        adapter.setSortAndType(type, sort);
        setHttpHandler(getLoadEngine().loadDate(type, sort));
    }

    public void refresh(String type, String sort) {
        this.sort = sort;
        this.type = type;
        loadData();

    }


    @Override
    BaseAdapter getListAdapter() {

        if (null == adapter) {
            adapter = new FundOrderAdapter(getActivity(), dataList);
        }
        return adapter;
    }


    @Override
    public void loadFinish(MoreDataBean object) {
        super.loadFinish(object);

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
                fundOrderEngine.loadData();
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

    private void startLoadData() {
        if (getActivity() instanceof FriendsOrFollowersActivity) {
            ((FriendsOrFollowersActivity) getActivity()).rotateRefreshButton();
        }
    }

    private void endLoadData() {
        if (getActivity() instanceof FriendsOrFollowersActivity) {
            ((FriendsOrFollowersActivity) getActivity()).stopRefreshAnimation();
        }
    }

    @Override
    public void loadFail() {
        endLoadData();
        mSwipeLayout.setRefreshing(false);
    }

}