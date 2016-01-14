/**
 * @Title TabFundsFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-2-7 上午11:03:26
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.baidu.mobstat.StatService;
import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialBannerBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialFinancingBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialFundManagerBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialLineBean;
import com.dkhs.portfolio.bean.RecommendFundSpecialMarketBean;
import com.dkhs.portfolio.bean.SafeSignBean;
import com.dkhs.portfolio.bean.SpaceBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.bean.itemhandler.SafeSignHandler;
import com.dkhs.portfolio.bean.itemhandler.fundspecial.FundSpecialBannerHandler;
import com.dkhs.portfolio.bean.itemhandler.fundspecial.FundSpecialFinancingHandler;
import com.dkhs.portfolio.bean.itemhandler.fundspecial.FundSpecialFundManagerBannerHandler;
import com.dkhs.portfolio.bean.itemhandler.fundspecial.FundSpecialHandler;
import com.dkhs.portfolio.bean.itemhandler.fundspecial.FundSpecialMainValueHandler;
import com.dkhs.portfolio.bean.itemhandler.fundspecial.FundSpecialMarketHandler;
import com.dkhs.portfolio.bean.itemhandler.fundspecial.FundSpecialTitleHandler;
import com.dkhs.portfolio.bean.itemhandler.fundspecial.FundSpecialTitleType;
import com.dkhs.portfolio.bean.itemhandler.homepage.SpaceHandler;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.FundHomeEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.net.StringDecodeUtil;
import com.dkhs.portfolio.ui.SelectGeneralActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.NewIntent;
import com.dkhs.portfolio.ui.eventbus.RotateRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.StopRefreshEvent;
import com.dkhs.portfolio.ui.eventbus.TopEvent;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MarketFundsFragment
 * @Description TODO(基金tab Fragment)
 * @date 2015-2-7 上午11:03:26
 * @modified zcm
 * @date 2015-12-7 上午9:40:54
 */
public class MarketFundsHomeFragment extends VisiableLoadFragment implements OnClickListener {

    public static final String TAG = "MarketFundsFragment";
    private int requestCount = 0;
    @ViewInject(R.id.swipe_container)
    public SwipeRefreshLayout mSwipeLayout;
    @ViewInject(android.R.id.list)
    private PullToRefreshListView mListView;

    private BaseAdapter mAdapter;
    private ArrayList mDataList = new ArrayList<>();

    private List<StockQuotesBean> mainValue = new ArrayList<>();
    private List<RecommendFundSpecialBannerBean> specialBanners = new ArrayList<>();
    private List<RecommendFundSpecialLineBean> specialLines = new ArrayList<>();
    private List<RecommendFundSpecialFinancingBean> specialFinancings = new ArrayList<>();
    private List<FundManagerBean> specialFundmanagers = new ArrayList<>();

    @Override
    public int setContentLayoutId() {
        return R.layout.empty_listview;
    }


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

    private void handIntent(Bundle bundle) {
        if (bundle.containsKey("fund_manager_ranking")) {
            boolean fundManagerRanking = bundle.getBoolean("fund_manager_ranking", true);
            if (fundManagerRanking) {
            } else {
            }
        }
    }

    @Subscribe
    public void forward2Top(TopEvent event) {
        if (event != null && isVisible() && getUserVisibleHint()) {
            if (mListView != null) {
                mListView.smoothScrollToPosition(0);
            }
        }
    }


    @Subscribe
    public void newIntent(NewIntent newIntent) {
        handIntent(newIntent.bundle);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initLoadMoreList(getView());
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getCache();
                queryData();
            }
        }, 500);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void requestData() {

    }

    public void queryData() {
        startAnimaRefresh();
        loadData();
    }

    /**
     * 获取缓存
     */
    private void getCache() {
        try {
            String mainValueJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_MAIN_VALUE_JSON);
            if (!TextUtils.isEmpty(mainValueJson)) {
                mainValueJson = StringDecodeUtil.decodeUnicode(mainValueJson);
                JSONObject jsonObject = new JSONObject(mainValueJson);
                JSONArray results = jsonObject.getJSONArray("results");
                mainValue.clear();
                mainValue.addAll(DataParse.parseArrayJson(StockQuotesBean.class, results));
                if (mainValue.size() > 0) {
                    mainValue.get(0).setTrade_status(jsonObject.getString("trade_status"));
                }
            }
            String specialBannersJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_BANNERS_JSON);
            if (!TextUtils.isEmpty(specialBannersJson)) {
                specialBannersJson = StringDecodeUtil.decodeUnicode(specialBannersJson);
                JSONObject jsonObject = new JSONObject(specialBannersJson);
                JSONArray results = jsonObject.getJSONArray("results");
                specialBanners.clear();
                specialBanners.addAll(DataParse.parseArrayJson(RecommendFundSpecialBannerBean.class, results));
            }
            String specialLinesJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_LINES_JSON);
            if (!TextUtils.isEmpty(specialLinesJson)) {
                specialLinesJson = StringDecodeUtil.decodeUnicode(specialLinesJson);
                JSONObject jsonObject = new JSONObject(specialLinesJson);
                JSONArray results = jsonObject.getJSONArray("results");
                specialLines.clear();
                specialLines.addAll(DataParse.parseArrayJson(RecommendFundSpecialLineBean.class, results));
            }
            String specialFinacingsJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_FINANCINGS_JSON);
            if (!TextUtils.isEmpty(specialFinacingsJson)) {
                specialFinacingsJson = StringDecodeUtil.decodeUnicode(specialFinacingsJson);
                JSONObject jsonObject = new JSONObject(specialFinacingsJson);
                JSONArray results = jsonObject.getJSONArray("results");
                specialFinancings.clear();
                specialFinancings.addAll(DataParse.parseArrayJson(RecommendFundSpecialFinancingBean.class, results));
            }
            String specialFundmanagersJson = PortfolioPreferenceManager.getStringValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_FUNDMANAGERS_JSON);
            if (!TextUtils.isEmpty(specialFundmanagersJson)) {
                specialFundmanagersJson = StringDecodeUtil.decodeUnicode(specialFundmanagersJson);
                JSONObject jsonObject = new JSONObject(specialFundmanagersJson);
                JSONArray results = jsonObject.getJSONArray("results");
                specialFundmanagers.clear();
                specialFundmanagers.addAll(DataParse.parseArrayJson(FundManagerBean.class, results));
            }
            generateData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        requestCount = 0;
        FundHomeEngineImpl engine = new FundHomeEngineImpl();
        engine.getMarketInfo(new ParseHttpListener<List<StockQuotesBean>>() {
            @Override
            protected List<StockQuotesBean> parseDateTask(String jsonData) {
                List<StockQuotesBean> lists = null;
                try {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_MAIN_VALUE_JSON, jsonData);
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray results = jsonObject.getJSONArray("results");
                    lists = DataParse.parseArrayJson(StockQuotesBean.class, results);
                    if (lists.size() > 0) {
                        lists.get(0).setTrade_status(jsonObject.getString("trade_status"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return lists;
            }

            @Override
            protected void afterParseData(List<StockQuotesBean> object) {
                mainValue.clear();
                mainValue.addAll(object);
                requestCount++;
                needRefresh();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                mSwipeLayout.setRefreshing(false);
                endAnimaRefresh();
                super.onFailure(errCode, errMsg);
            }
        });
        engine.getRecommendBanners(new ParseHttpListener<List<RecommendFundSpecialBannerBean>>() {
            @Override
            protected List<RecommendFundSpecialBannerBean> parseDateTask(String jsonData) {
                List<RecommendFundSpecialBannerBean> lists = null;
                try {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_BANNERS_JSON, jsonData);
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray results = jsonObject.getJSONArray("results");
                    lists = DataParse.parseArrayJson(RecommendFundSpecialBannerBean.class, results);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return lists;
            }

            @Override
            protected void afterParseData(List<RecommendFundSpecialBannerBean> object) {
                specialBanners.clear();
                specialBanners.addAll(object);
                requestCount++;
                needRefresh();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                mSwipeLayout.setRefreshing(false);
                endAnimaRefresh();
                super.onFailure(errCode, errMsg);
            }
        });
        engine.getRecommendSpecials(new ParseHttpListener<List<RecommendFundSpecialLineBean>>() {
            @Override
            protected List<RecommendFundSpecialLineBean> parseDateTask(String jsonData) {
                List<RecommendFundSpecialLineBean> lists = null;
                try {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_LINES_JSON, jsonData);
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray results = jsonObject.getJSONArray("results");
                    lists = DataParse.parseArrayJson(RecommendFundSpecialLineBean.class, results);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return lists;
            }

            @Override
            protected void afterParseData(List<RecommendFundSpecialLineBean> object) {
                specialLines.clear();
                specialLines.addAll(object);
                requestCount++;
                needRefresh();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                mSwipeLayout.setRefreshing(false);
                endAnimaRefresh();
                super.onFailure(errCode, errMsg);
            }
        });
        engine.getRecommendSpecialFinancings(new ParseHttpListener<List<RecommendFundSpecialFinancingBean>>() {
            @Override
            protected List<RecommendFundSpecialFinancingBean> parseDateTask(String jsonData) {
                List<RecommendFundSpecialFinancingBean> lists = null;
                try {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_FINANCINGS_JSON, jsonData);
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray results = jsonObject.getJSONArray("results");
                    lists = DataParse.parseArrayJson(RecommendFundSpecialFinancingBean.class, results);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return lists;
            }

            @Override
            protected void afterParseData(List<RecommendFundSpecialFinancingBean> object) {
                specialFinancings.clear();
                specialFinancings.addAll(object);
                requestCount++;
                needRefresh();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                mSwipeLayout.setRefreshing(false);
                endAnimaRefresh();
                super.onFailure(errCode, errMsg);
            }
        });
        engine.getRecommendFundManager(new ParseHttpListener<List<FundManagerBean>>() {


            @Override
            protected List<FundManagerBean> parseDateTask(String jsonData) {
                List<FundManagerBean> lists = null;
                try {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_SPECIAL_FUNDMANAGERS_JSON, jsonData);
                    jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray results = jsonObject.getJSONArray("results");
                    lists = DataParse.parseArrayJson(FundManagerBean.class, results);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return lists;
            }

            @Override
            protected void afterParseData(List<FundManagerBean> object) {
                specialFundmanagers.clear();
                specialFundmanagers.addAll(object);
                requestCount++;
                needRefresh();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                mSwipeLayout.setRefreshing(false);
                endAnimaRefresh();
                super.onFailure(errCode, errMsg);
            }
        });
    }

    private boolean isLoading;

    private void refresh() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        mSwipeLayout.setRefreshing(true);
        queryData();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startAnimaRefresh();
            }
        });
    }

    private boolean requestSuccess = false;

    private void needRefresh() {
        if (requestCount == 5) {
            if (!requestSuccess)
                requestSuccess = true;
            if (isAdded())
                endAnimaRefresh();
            isLoading = false;
            mSwipeLayout.setRefreshing(false);
            if (mainValue.size() > 0 && UIUtils.roundAble(Integer.parseInt(mainValue.get(0).getTrade_status()))) {
                updateHandler.removeCallbacks(updateRunnable);
                updateHandler.postDelayed(updateRunnable, mPollRequestTime);
            }
            generateData();
        }
    }

    private void generateData() {
        mDataList.clear();
        mDataList.add(mainValue);
        Message msg = Message.obtain();
        msg.obj = specialBanners;
        mDataList.add(msg);
        mDataList.add(FundSpecialTitleType.TITLE_FUND_MARKET);
        mDataList.add(new RecommendFundSpecialMarketBean());
        int position = 0;
        for (RecommendFundSpecialLineBean item : specialLines) {
            item.position = position;
            mDataList.add(item);
            position++;
        }
        if (specialFundmanagers.size() > 0) {
            mDataList.add(FundSpecialTitleType.TITLE_FUND_MANAGER);
            RecommendFundSpecialFundManagerBean managerBean = new RecommendFundSpecialFundManagerBean();
            managerBean.lists = specialFundmanagers;
            mDataList.add(managerBean);
        }
        mDataList.add(FundSpecialTitleType.TITLE_SPECIAL);
        for (RecommendFundSpecialFinancingBean item : specialFinancings) {
            mDataList.add(item);
        }
        if (mDataList != null && mDataList.size() != 0) {
            mDataList.add(new SpaceBean());
            mDataList.add(new SafeSignBean());
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateMainValue() {
        if (mainValue.size() > 0 && UIUtils.roundAble(Integer.parseInt(mainValue.get(0).getTrade_status()))) {
            updateHandler.removeCallbacks(updateRunnable);
            updateHandler.postDelayed(updateRunnable, mPollRequestTime);
        }
        mDataList.remove(0);
        mDataList.add(0, mainValue);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewShow() {
        if (requestSuccess && mainValue.size() > 0 && UIUtils.roundAble(Integer.parseInt(mainValue.get(0).getTrade_status()))) {
            updateHandler.postDelayed(updateRunnable, mPollRequestTime);
        }
        StatService.onPageStart(getActivity(), TAG);
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && isVisible()) {
            if (getView() != null) {
                onViewShow();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initLoadMoreList(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        queryData();
                    }
                });
            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        mListView.setCanRefresh(false);
        mListView.setAdapter(getListAdapter());
        mListView.setDivider(null);
    }

    BaseAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList).buildMultiItemView(ArrayList.class, new FundSpecialMainValueHandler(mActivity))
                    .buildMultiItemView(Message.class, new FundSpecialBannerHandler(mActivity))
                    .buildMultiItemView(FundSpecialTitleType.class, new FundSpecialTitleHandler(mActivity))
                    .buildMultiItemView(RecommendFundSpecialMarketBean.class, new FundSpecialMarketHandler(mActivity))
                    .buildMultiItemView(RecommendFundSpecialLineBean.class, new FundSpecialHandler(mActivity))
                    .buildMultiItemView(RecommendFundSpecialFundManagerBean.class, new FundSpecialFundManagerBannerHandler(mActivity))
                    .buildMultiItemView(RecommendFundSpecialFinancingBean.class, new FundSpecialFinancingHandler(mActivity))
                    .buildMultiItemView(SpaceBean.class, new SpaceHandler(mActivity))
                    .buildMultiItemView(SafeSignBean.class, new SafeSignHandler(mActivity));
        }
        return mAdapter;
    }

    @Override
    public void onViewHide() {
        updateHandler.removeCallbacks(updateRunnable);
        StatService.onPageEnd(getActivity(), TAG);
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacks(updateRunnable);
    }

    private static final long mPollRequestTime = 1000 * 5;

    WeakHandler updateHandler = new WeakHandler();
    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            FundHomeEngineImpl engine = new FundHomeEngineImpl();
            engine.getMarketInfo(new ParseHttpListener<List<StockQuotesBean>>() {
                @Override
                protected List<StockQuotesBean> parseDateTask(String jsonData) {
                    List<StockQuotesBean> lists = null;
                    try {
                        PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_FUNDS_HOME_MAIN_VALUE_JSON, jsonData);
                        jsonData = StringDecodeUtil.decodeUnicode(jsonData);
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray results = jsonObject.getJSONArray("results");
                        lists = DataParse.parseArrayJson(StockQuotesBean.class, results);
                        if (lists.size() > 0) {
                            lists.get(0).setTrade_status(jsonObject.getString("trade_status"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return lists;
                }

                @Override
                protected void afterParseData(List<StockQuotesBean> object) {
                    mainValue.clear();
                    mainValue.addAll(object);
                    updateMainValue();
                }
            });
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:
                refresh();
                break;
            case R.id.btn_search:
                Intent intent = new Intent(getActivity(), SelectGeneralActivity.class);
                UIUtils.startAnimationActivity(getActivity(), intent);
                break;
        }
    }

    public void startAnimaRefresh() {
        if (isAdded() && getUserVisibleHint()) {
            BusProvider.getInstance().post(new RotateRefreshEvent());
        }
    }

    public void endAnimaRefresh() {
        if (isAdded() && getUserVisibleHint()) {
            BusProvider.getInstance().post(new StopRefreshEvent());
        }
    }
}
