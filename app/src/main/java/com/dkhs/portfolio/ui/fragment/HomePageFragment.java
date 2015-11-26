package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.HomeMoreBean;
import com.dkhs.portfolio.bean.RecommendFund;
import com.dkhs.portfolio.bean.RecommendFundBean;
import com.dkhs.portfolio.bean.RecommendFundManager;
import com.dkhs.portfolio.bean.RecommendPortfolio;
import com.dkhs.portfolio.bean.itemhandler.BannerHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.HomeMoreHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.RecomendPortfolioHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.RecommendFundHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.RecommendFundManagerHandler;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.HomePageEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomePageFragment extends VisiableLoadFragment implements BannerHandler.RefreshEnable {

    private static final int MSG_FUND = 0;
    private static final int MSG_FUND_MANAGER = 1;


    private ArrayList mDataList = new ArrayList<>();
    //网络数据
    private ArrayList<RecommendFund> recommendFunds = new ArrayList<>();
    private ArrayList<RecommendFundManager> recommendFundManagers = new ArrayList<>();
    private ArrayList<RecommendPortfolio> recommendPortfolios = new ArrayList<>();
    private BannerTopicsBean bean;

    private BaseAdapter mAdapter;

    private ParseHttpListener<List<RecommendFund>> recommendFundListener = new ParseHttpListener<List<RecommendFund>>() {

        @Override
        public void onSuccess(String jsonObject) {
            LogUtils.d("wys","success");
            //缓存
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_JSON,jsonObject);
            super.onSuccess(jsonObject);
        }

        @Override
        protected List<RecommendFund> parseDateTask(String jsonData) {
            return parseFund(jsonData);
        }

        @Override
        protected void afterParseData(List<RecommendFund> object) {
            recommendFunds = (ArrayList<RecommendFund>) object;
            mHandler.sendEmptyMessage(mWhat | 1);
        }
    };

    @Nullable
    private List<RecommendFund> parseFund(String jsonData) {
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


    private ParseHttpListener<List<RecommendFundManager>> fundManagerListener = new ParseHttpListener<List<RecommendFundManager>>() {


        @Override
        public void onSuccess(String jsonObject) {
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_MANAGER_JSON,jsonObject);
            super.onSuccess(jsonObject);
        }

        @Override
        protected List<RecommendFundManager> parseDateTask(String jsonData) {
            return parseFundManager(jsonData);
        }

        @Override
        protected void afterParseData(List<RecommendFundManager> object) {
            recommendFundManagers = (ArrayList<RecommendFundManager>) object;
            mHandler.sendEmptyMessage(mWhat | 2);
        }
    };

    @Nullable
    private List<RecommendFundManager> parseFundManager(String jsonData) {
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

    private ParseHttpListener<List<RecommendPortfolio>> portfolioListener = new ParseHttpListener<List<RecommendPortfolio>>() {


        @Override
        public void onSuccess(String jsonObject) {
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_RECOMMEND_PORTFOLIO_JSON,jsonObject);
            super.onSuccess(jsonObject);
        }

        @Override
        protected List<RecommendPortfolio> parseDateTask(String jsonData) {
            return parsePortfolio(jsonData);
        }

        @Override
        protected void afterParseData(List<RecommendPortfolio> object) {
            recommendPortfolios = (ArrayList<RecommendPortfolio>) object;
            mHandler.sendEmptyMessage(mWhat | 4);
        }
    };

    @Nullable
    private List<RecommendPortfolio> parsePortfolio(String jsonData) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");
            List<RecommendPortfolio> list = DataParse.parseArrayJson(RecommendPortfolio.class, results);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ParseHttpListener<BannerTopicsBean> bannerListener = new ParseHttpListener<BannerTopicsBean>() {


        @Override
        public void onSuccess(String jsonObject) {
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_HOME_BANNER_JSON,jsonObject);
            super.onSuccess(jsonObject);
        }

        @Override
        protected BannerTopicsBean parseDateTask(String jsonData) {
            return parseBanner(jsonData);
        }

        @Override
        protected void afterParseData(BannerTopicsBean object) {
            bean = object;
            mHandler.sendEmptyMessage(mWhat | 8);
        }
    };

    @Nullable
    private BannerTopicsBean parseBanner(String jsonData) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            AdBean ad = DataParse.parseObjectJson(AdBean.class, jsonObject);
            BannerTopicsBean bean = new BannerTopicsBean();
            bean.hotTopicsBeans = null;
            bean.adBean = ad;
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private int mWhat = 0;
    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 15:
                    LogUtils.d("wys","swipe close refresh");
                    mSwipeLayout.setRefreshing(false);
                default:
                    generateData();
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
            .buildMultiItemView(RecommendFundBean.class,new RecommendFundHandler(mActivity))
            .buildMultiItemView(RecommendPortfolio.class,new RecomendPortfolioHandler(mActivity));
        }
        return mAdapter;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        generateData();
    }

    /**
     * 获取网络数据
     */
    private void getNetData(){
        mSwipeLayout.setRefreshing(true);
        HomePageEngine.getRecommendFund(recommendFundListener);
        HomePageEngine.getRecommendFundManager(fundManagerListener);
        HomePageEngine.getRecommendPortfolio(portfolioListener);
        HomePageEngine.getBanner(bannerListener);
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

    private void generateData(){
        mDataList.clear();
        //banner广告栏
        if(bean != null){
            mDataList.add(bean);
        }else if(!TextUtils.isEmpty(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_HOME_BANNER_JSON))){
            String bannerJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_HOME_BANNER_JSON);
            BannerTopicsBean banner = parseBanner(bannerJson);
            mDataList.add(banner);
        }
        //推荐基金经理
        if(recommendFundManagers != null && recommendFundManagers.size() > 0){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND_MANAGER));
            mDataList.addAll(recommendFundManagers);
        }else if(!TextUtils.isEmpty(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_MANAGER_JSON))){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND_MANAGER));
            String fundManagerJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_MANAGER_JSON);
            List<RecommendFundManager> fundManagers = parseFundManager(fundManagerJson);
            mDataList.addAll(fundManagers);
        }
        //推荐基金
        if(recommendFunds != null && recommendFunds.size() > 0){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND));
            RecommendFundBean bean = new RecommendFundBean(recommendFunds);
            mDataList.add(bean);
        }else if(!TextUtils.isEmpty(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_JSON))){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND));
            ArrayList<RecommendFund> fundBeans = (ArrayList<RecommendFund>) parseFund(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_JSON));
            RecommendFundBean bean = new RecommendFundBean(fundBeans);
            mDataList.add(bean);
        }

        //推荐组合
        if(recommendPortfolios != null && recommendPortfolios.size() > 0){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_PORTFOLIO));
            mDataList.addAll(recommendPortfolios);
        }else if(!TextUtils.isEmpty(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_PORTFOLIO_JSON))){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_PORTFOLIO));
            ArrayList<RecommendPortfolio> portfolios = (ArrayList<RecommendPortfolio>) parsePortfolio(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_PORTFOLIO_JSON));
            mDataList.addAll(portfolios);
        }
        mAdapter.notifyDataSetChanged();
    }

}
