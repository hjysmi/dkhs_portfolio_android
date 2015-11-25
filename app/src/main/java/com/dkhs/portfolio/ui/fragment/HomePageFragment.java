package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.HomeMoreBean;
import com.dkhs.portfolio.bean.RecommendFund;
import com.dkhs.portfolio.bean.RecommendFundBean;
import com.dkhs.portfolio.bean.RecommendFundManager;
import com.dkhs.portfolio.bean.itemhandler.BannerHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.HomeMoreHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.RecommendFundHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.RecommendFundManagerHandler;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.HomePageEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomePageFragment extends VisiableLoadFragment implements BannerHandler.RefreshEnable {

    private static final int MSG_FUND = 0;
    private static final int MSG_FUND_MANAGER = 1;

    private ArrayList mDataList = new ArrayList<>();
    private ArrayList<RecommendFund> recommendFunds = new ArrayList<>();
    private ArrayList<RecommendFundManager> recommendFundManagers = new ArrayList<>();
    private HomePageEngine mEngine = null;
    private BaseAdapter mAdapter;

    private ParseHttpListener<List<RecommendFund>> recommendFundListener = new ParseHttpListener<List<RecommendFund>>() {

        @Override
        protected List<RecommendFund> parseDateTask(String jsonData) {
            //缓存
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_JSON,jsonData);
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray results = jsonObject.getJSONArray("results");
                List<RecommendFund> list = DataParse.parseArrayJson(RecommendFund.class, results);
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(List<RecommendFund> object) {
            Message msg = Message.obtain();
            msg.what = MSG_FUND;
            msg.obj = object;
            mHandler.sendMessage(msg);
        }
    };


    private ParseHttpListener<List<RecommendFundManager>> fundManagerListener = new ParseHttpListener<List<RecommendFundManager>>() {


        @Override
        protected List<RecommendFundManager> parseDateTask(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                JSONArray results = jsonObject.getJSONArray("results");
                List<RecommendFundManager> list = DataParse.parseArrayJson(RecommendFundManager.class, results);
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void afterParseData(List<RecommendFundManager> object) {
            Message msg = Message.obtain();
            msg.what = MSG_FUND_MANAGER;
            msg.obj = object;
            mHandler.sendMessage(msg);
        }
    };



    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mSwipeLayout.setRefreshing(false);
            switch (msg.what){
                case MSG_FUND:
                    ArrayList<RecommendFund> list = (ArrayList<RecommendFund>) msg.obj;
                    RecommendFundBean bean = new RecommendFundBean(list);
                    mDataList.clear();
                    mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND));
                    mDataList.add(bean);
                    mAdapter.notifyDataSetChanged();
                    break;
                case MSG_FUND_MANAGER:
                    ArrayList<RecommendFundManager> managers = (ArrayList<RecommendFundManager>) msg.obj;
                    mDataList.clear();
                    mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND_MANAGER));
                    mDataList.addAll(managers);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });
    private SwipeRefreshLayout mSwipeLayout;
    private PullToRefreshListView mListView;

    public HomePageFragment() {

    }


    BaseAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList).buildMultiItemView(BannerTopicsBean.class, new BannerHandler(mActivity, HomePageFragment.this))
            .buildMultiItemView(HomeMoreBean.class,new HomeMoreHandler(mActivity))
            .buildMultiItemView(RecommendFundManager.class,new RecommendFundManagerHandler(mActivity))
            .buildMultiItemView(RecommendFundBean.class,new RecommendFundHandler(mActivity));
        }
        return mAdapter;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mEngine = new HomePageEngine(getActivity());
        BusProvider.getInstance().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_home_page;
    }


    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }



    @Override
    public void requestData() {
        getCache();
        getNetData();
    }

    /**
     * 获取缓存
     */
    private void getCache(){

    }

    /**
     * 获取网络数据
     */
    private void getNetData(){
//        mSwipeLayout.setRefreshing(true);
        HomePageEngine.getRecommendFund(recommendFundListener);
//        HomePageEngine.getRecommendFundManager(fundManagerListener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initLoadMoreList(view);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void enable() {

    }

    @Override
    public void disEnable() {

    }

    private void initLoadMoreList(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNetData();
            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(getListAdapter());
    }

}
