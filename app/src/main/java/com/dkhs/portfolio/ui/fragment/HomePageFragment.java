package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.RelativeLayout;
import com.dkhs.portfolio.bean.AdBean;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.FundPriceBean;
import com.dkhs.portfolio.bean.HomeMoreBean;
import com.dkhs.portfolio.bean.RecommendFundBean;
import com.dkhs.portfolio.bean.itemhandler.homepage.HomeMoreHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.HomePageBannerHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.RecomendPortfolioHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.RecommendFundHandler;
import com.dkhs.portfolio.bean.itemhandler.homepage.RecommendFundManagerHandler;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.HomePageEngine;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.SelectGeneralActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomePageFragment extends VisiableLoadFragment implements HomePageBannerHandler.RefreshEnable,AbsListView.OnScrollListener {

    private static final int REQUEST_SUCCESS = 15;
    private static final int REQUESS_FAIL = -1;


    private ArrayList mDataList = new ArrayList<>();
    //网络数据
    private ArrayList<FundPriceBean> recommendFunds = new ArrayList<>();
    private ArrayList<FundManagerBean> recommendFundManagers = new ArrayList<>();
    private ArrayList<CombinationBean> recommendPortfolios = new ArrayList<>();
    private BannerTopicsBean bean;

    private BaseAdapter mAdapter;

    private ParseHttpListener<List<FundPriceBean>> recommendFundListener = new ParseHttpListener<List<FundPriceBean>>() {

        @Override
        public void onFailure(int errCode, String errMsg) {
            mHandler.sendEmptyMessage(REQUESS_FAIL);
            super.onFailure(errCode, errMsg);
        }

        @Override
        public void onSuccess(String jsonObject) {
            //缓存
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_JSON,jsonObject);
            super.onSuccess(jsonObject);
        }

        @Override
        protected List<FundPriceBean> parseDateTask(String jsonData) {
            return parseFund(jsonData);
        }

        @Override
        protected void afterParseData(List<FundPriceBean> object) {
            recommendFunds = (ArrayList<FundPriceBean>) object;
            HomePageFragment.this.mWhat = mWhat | 1;
            mHandler.sendEmptyMessage(mWhat);
        }
    };

    @Nullable
    private List<FundPriceBean> parseFund(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");
            List<FundPriceBean> list = DataParse.parseArrayJson(FundPriceBean.class, results);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private ParseHttpListener<List<FundManagerBean>> fundManagerListener = new ParseHttpListener<List<FundManagerBean>>() {

        @Override
        public void onFailure(int errCode, String errMsg) {
            mHandler.sendEmptyMessage(REQUESS_FAIL);
            super.onFailure(errCode, errMsg);
        }

        @Override
        public void onSuccess(String jsonObject) {
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_MANAGER_JSON,jsonObject);
            super.onSuccess(jsonObject);
        }

        @Override
        protected List<FundManagerBean> parseDateTask(String jsonData) {
            return parseFundManager(jsonData);
        }

        @Override
        protected void afterParseData(List<FundManagerBean> object) {
            recommendFundManagers = (ArrayList<FundManagerBean>) object;
            HomePageFragment.this.mWhat = mWhat | 2;
            mHandler.sendEmptyMessage(mWhat);
        }
    };

    @Nullable
    private List<FundManagerBean> parseFundManager(String jsonData) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");
            List<FundManagerBean> list = DataParse.parseArrayJson(FundManagerBean.class, results);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ParseHttpListener<List<CombinationBean>> portfolioListener = new ParseHttpListener<List<CombinationBean>>() {

        @Override
        public void onFailure(int errCode, String errMsg) {
            mHandler.sendEmptyMessage(REQUESS_FAIL);
            super.onFailure(errCode, errMsg);
        }

        @Override
        public void onSuccess(String jsonObject) {
            PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_RECOMMEND_PORTFOLIO_JSON,jsonObject);
            super.onSuccess(jsonObject);
        }

        @Override
        protected List<CombinationBean> parseDateTask(String jsonData) {
            return parsePortfolio(jsonData);
        }

        @Override
        protected void afterParseData(List<CombinationBean> object) {
            recommendPortfolios = (ArrayList<CombinationBean>) object;
            HomePageFragment.this.mWhat = mWhat | 4;
            mHandler.sendEmptyMessage(mWhat);
        }
    };

    @Nullable
    private List<CombinationBean> parsePortfolio(String jsonData) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray results = jsonObject.getJSONArray("results");
            List<CombinationBean> list = DataParse.parseArrayJson(CombinationBean.class, results);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ParseHttpListener<BannerTopicsBean> bannerListener = new ParseHttpListener<BannerTopicsBean>() {

        @Override
        public void onFailure(int errCode, String errMsg) {
            mHandler.sendEmptyMessage(REQUESS_FAIL);
            super.onFailure(errCode, errMsg);
        }

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
            HomePageFragment.this.mWhat = mWhat | 8;
            mHandler.sendEmptyMessage(mWhat);
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
            Log.d("wys", "msg" + msg.what);
            switch (msg.what){
                case REQUESS_FAIL:
                    mSwipeLayout.setRefreshing(false);
                    mWhat = 0;
                    break;
                case REQUEST_SUCCESS:
                    LogUtils.d("wys","swipe close refresh");
                    mSwipeLayout.setRefreshing(false);
                    mWhat = 0;
                default:
                    generateData();
                    break;
            }
            return false;
        }
    });
    private SwipeRefreshLayout mSwipeLayout;
    private PullToRefreshListView mListView;
    private EditText mSearchEt;
    private RelativeLayout mSearchLl;
    private View divider;

    public HomePageFragment() {

    }


    BaseAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList).buildMultiItemView(BannerTopicsBean.class, new HomePageBannerHandler(mActivity,HomePageFragment.this))
            .buildMultiItemView(HomeMoreBean.class,new HomeMoreHandler(mActivity))
            .buildMultiItemView(FundManagerBean.class,new RecommendFundManagerHandler(mActivity))
            .buildMultiItemView(RecommendFundBean.class,new RecommendFundHandler(mActivity))
            .buildMultiItemView(CombinationBean.class,new RecomendPortfolioHandler(mActivity));
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
//        mSwipeLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getActivity(), 24));
//        mSwipeLayout.setRefreshing(true);
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
        mWhat = 0;
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
        mSwipeLayout.setEnabled(true);
    }

    @Override
    public void disEnable() {
        mSwipeLayout.setEnabled(false);
    }


    private void initLoadMoreList(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSearchEt = (EditText) view.findViewById(R.id.et_search_key);
        mSearchEt.setFocusable(false);
        mSearchEt.setFocusableInTouchMode(false);
        mSearchEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectGeneralActivity.class);
                UIUtils.startAnimationActivity(getActivity(), intent);
            }
        });
        mSearchLl = (RelativeLayout) view.findViewById(R.id.ll_search);
        divider = view.findViewById(R.id.divider);
        mSearchLl.setBackgroundColor(Color.WHITE);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtils.d("wys", "onRefresh");
                getNetData();
            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        mListView.setCanRefresh(false);
        mListView.setAdapter(getListAdapter());
        mListView.setDivider(null);
        mListView.setOnScrollListener(this);
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
        mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_REWARD));
        //推荐基金经理
        if(recommendFundManagers != null && recommendFundManagers.size() > 0){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND_MANAGER));
            mDataList.addAll(recommendFundManagers);
        }else if(!TextUtils.isEmpty(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_MANAGER_JSON))){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND_MANAGER));
            String fundManagerJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_MANAGER_JSON);
            List<FundManagerBean> fundManagers = parseFundManager(fundManagerJson);
            mDataList.addAll(fundManagers);
        }
        //推荐基金
        if(recommendFunds != null && recommendFunds.size() > 0){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND,true));
            RecommendFundBean bean = new RecommendFundBean(recommendFunds);
            mDataList.add(bean);
        }else if(!TextUtils.isEmpty(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_JSON))){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_FUND));
            ArrayList<FundPriceBean> fundBeans = (ArrayList<FundPriceBean>) parseFund(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_FUND_JSON));
            RecommendFundBean bean = new RecommendFundBean(fundBeans);
            mDataList.add(bean);
        }

        //推荐组合
        if(recommendPortfolios != null && recommendPortfolios.size() > 0){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_PORTFOLIO));
            mDataList.addAll(recommendPortfolios);
        }else if(!TextUtils.isEmpty(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_PORTFOLIO_JSON))){
            mDataList.add(new HomeMoreBean(HomeMoreBean.TYPE_PORTFOLIO));
            ArrayList<CombinationBean> portfolios = (ArrayList<CombinationBean>) parsePortfolio(PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_RECOMMEND_PORTFOLIO_JSON));
            mDataList.addAll(portfolios);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onViewShow() {
        super.onViewShow();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
// 判断当前最上面显示的是不是头布局，因为Xlistview有刷新控件，所以头布局的位置是1，即第二个
        ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);
        if (firstVisibleItem == 0) {
            // 获取头布局
            View v = mListView.getChildAt(0);
            if (v != null) {
                // 获取头布局现在的最上部的位置的相反数
                int top = -v.getTop();
                // 获取头布局的高度
                int headerHeight = v.getHeight();
                // 满足这个条件的时候，是头布局在XListview的最上面第一个控件的时候，只有这个时候，我们才调整透明度
                if (top <= headerHeight && top >= 0) {
                    // 获取当前位置占头布局高度的百分比
                    float f = (float) top / (float) headerHeight;
                    divider.getBackground().setAlpha((int) (f * 230));
                    mSearchLl.getBackground().setAlpha((int) (f * 180));
                }
            }
        } else if (firstVisibleItem > 0) {
            divider.getBackground().setAlpha(230);
            mSearchLl.getBackground().setAlpha(180);
        }
    }
}
