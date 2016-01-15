package com.dkhs.portfolio.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.base.widget.ImageButton;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreCombinationHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreFundManagerHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreStockFundHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreTitleHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreType;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreUserHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.IResultCallback;
import com.dkhs.portfolio.engine.LocalDataEngine.VisitorDataSource;
import com.dkhs.portfolio.engine.SelectGeneralSearchMoreEngineImpl;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.UpdateCombinationEvent;
import com.dkhs.portfolio.ui.widget.ClearableEditText;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcm on 2015/11/20.
 */
public class SelectSearchMoreLoadMoreListActivity extends ModelAcitivity implements LoadMoreDataEngine.ILoadDataBackListener, PullToRefreshListView.OnLoadMoreListener, View.OnClickListener {

    private static String SEARCH_STRING = "search_string";
    private static String SEARCH_TYPE = "search_type";
    private String searchString;
    private SearchMoreType searchType;
    @ViewInject(android.R.id.list)

    PullToRefreshListView mListView;
    @ViewInject(android.R.id.empty)
    private TextView tvEmptyText;
    @ViewInject(R.id.tv_search)
    private TextView tvCancel;
    @ViewInject(R.id.et_search_key)
    private ClearableEditText etSearch;
    @ViewInject(R.id.btn_search_back)
    private ImageButton btnBack;

    private HttpHandler mHttpHandler;
    @ViewInject(android.R.id.progress)
    View mProgressView;

    private List<Object> mDataList = new ArrayList<>();
    private DKBaseAdapter mAdapter;


    public static Intent getIntent(Context context, String searchString, SearchMoreType searchType) {

        Intent intent = new Intent(context, SelectSearchMoreLoadMoreListActivity.class);
        intent.putExtra(SEARCH_STRING, searchString);
        intent.putExtra(SEARCH_TYPE, searchType.ordinal());
        return intent;

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.select_search_more_empty_listview);
        ViewUtils.inject(this);
        hideHead();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            handleExtras(extras);
        }
        initView();
        initSearchHint();
        initLoadMoreList();
        BusProvider.getInstance().register(this);
        postDelayedeData();
    }

    private void initSearchHint() {
        switch (searchType) {
            case MORE_STOCK:
                etSearch.setHint(R.string.search_stock_hint);
                break;
            case MORE_FUND:
                etSearch.setHint(R.string.search_fund_hint_other);
                break;
            case MORE_FUND_MANAGER:
                etSearch.setHint(R.string.search_fund_manger_hint);
                break;
            case MORE_USER:
                etSearch.setHint(R.string.search_user_hint);
                //用户
                break;
            case MORE_COMBINATION:
                //组合
                etSearch.setHint(R.string.search_com_hint);
                break;
            case MORE_REWARD:
                etSearch.setHint(R.string.search_reward_hint);
                break;
            //悬赏
            case MORE_TOPIC:
                etSearch.setHint(R.string.search_topic_hint);
                break;
        }
    }

    private void handleExtras(Bundle extras) {
        searchString = extras.getString(SEARCH_STRING);
        searchType = SearchMoreType.valueOf(extras.getInt(SEARCH_TYPE));
    }

    // add by zcm -----2014.12.15
    public void setListViewVisible() {
        if (mListView.getVisibility() == View.VISIBLE && tvEmptyText.getVisibility() == View.GONE)
            return;
        mListView.setVisibility(View.VISIBLE);
        tvEmptyText.setVisibility(View.GONE);
    }

    public void initView() {
        etSearch.setText(searchString);
        if (searchType == SearchMoreType.MORE_REWARD || searchType == SearchMoreType.MORE_TOPIC) {
            tvCancel.setVisibility(View.VISIBLE);
            tvCancel.setText(R.string.search);
            tvCancel.setOnClickListener(this);
        } else {
            tvCancel.setVisibility(View.GONE);
            etSearch.addTextChangedListener(new MyTextWatcher());
        }
        btnBack.setOnClickListener(this);

    }

    private void search(boolean show) {
        searchString = etSearch.getText().toString();
        if (show && TextUtils.isEmpty(searchString.trim())) {
            PromptManager.showToast("请输入搜索内容");
            return;
        }
        getLoadEngine().setSearchString(searchString);
        getLoadEngine().loadData();
    }

    // add by zcm -----2014.12.15

    public void setEmptyText(String text) {
        mListView.setVisibility(View.GONE);
        tvEmptyText.setText(text);
        tvEmptyText.setVisibility(View.VISIBLE);
    }

    public void setEmptyText(int stringId) {
        mListView.setVisibility(View.GONE);
        tvEmptyText.setText(getResources().getString(stringId));
        tvEmptyText.setVisibility(View.VISIBLE);
    }


    private void initLoadMoreList() {
//        mListView = (PullToRefreshListView) findViewById(android.R.id.list);
        mListView.setCanRefresh(false);
//        tvEmptyText = (TextView) findViewById(android.R.id.empty);
//        mProgressView = findViewById(android.R.id.progress);

        mListView.setAdapter(getListAdapter());
        mListView.setDivider(null);
        setListViewInit(mListView);

    }


    private ListAdapter getListAdapter() {
        mAdapter = new DKBaseAdapter(mContext, mDataList).buildMultiItemView(String.class, new SearchMoreTitleHandler());
        switch (searchType) {
            case MORE_STOCK:
            case MORE_FUND:
                //股票
                //基金
                mAdapter.buildMultiItemView(QuotesBean.class, new SearchMoreStockFundHandler(mContext));
                break;
            case MORE_FUND_MANAGER:
                //基金经理
                //   mAdapter.buildMultiItemView(QuotesBean.class, new SearchMoreUserHandler());
                mAdapter.buildMultiItemView(FundManagerBean.class, new SearchMoreFundManagerHandler());
                break;
            case MORE_USER:
                mAdapter.buildMultiItemView(UserEntity.class, new SearchMoreUserHandler());
                //用户
                break;
            case MORE_COMBINATION:
                //组合
                mAdapter.buildMultiItemView(CombinationBean.class, new SearchMoreCombinationHandler(mContext));
                //  mAdapter.buildMultiItemView(QuotesBean.class, new SearchMoreCombinationHandler(mContext));
                break;
            case MORE_REWARD:
                //悬赏
            case MORE_TOPIC:
                //话题
                mAdapter.buildMultiItemView(TopicsBean.class, new TopicsHandler(mContext));
                break;
        }
        return mAdapter;
    }

    private SelectGeneralSearchMoreEngineImpl mLoadMoreDataEngine;

    private SelectGeneralSearchMoreEngineImpl getLoadEngine() {
        if (mLoadMoreDataEngine == null)
            mLoadMoreDataEngine = new SelectGeneralSearchMoreEngineImpl(this, searchType, SelectSearchMoreLoadMoreListActivity.this);
        mLoadMoreDataEngine.setSearchString(searchString);
        return mLoadMoreDataEngine;
    }


    public void setListViewInit(ListView listview) {

    }

    public void postDelayedeData() {
        if (!PortfolioApplication.hasUserLogin())
            loadVisitorData();
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setHttpHandler(getLoadEngine().loadData());
            }
        }, 500);
    }

    /**
     * @param object
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFinish(MoreDataBean object) {
        mListView.onLoadMoreComplete();
        if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
            mListView.setCanLoadMore(false);
            mListView.setAutoLoadMore(false);
        } else {
            mListView.setCanLoadMore(true);
            mListView.setAutoLoadMore(true);
            if (getLoadEngine().getCurrentpage() == 1)
                mListView.setOnLoadListener(this);
        }
        if (object.getCurrentPage() == 1 && object.getResults().size() == 0) {
            setEmptyText(getEmptyText());
        }
        if (getLoadEngine().getCurrentpage() == 1 || getLoadEngine().getCurrentpage() == 0) {
            mListView.smoothScrollToPosition(0);
            mDataList.clear();
        } else if (mDataList.size() > 0) {
            mDataList.remove(0);
        }
        mDataList.addAll(object.getResults());
        if (mDataList.size() > 0) {
            if(!PortfolioApplication.hasUserLogin()){
                switch (searchType) {
                    case MORE_STOCK:
                    case MORE_FUND:
                    case MORE_COMBINATION:
                        for (Object o : mDataList) {
                            if(o instanceof QuotesBean){
                                int index = mFollowStockList.indexOf(o);
                                if(index != -1){
                                    QuotesBean quotesBean = (QuotesBean) o;
                                    quotesBean.setFollowed(mFollowStockList.get(index).isFollowed);
                                }
                            }else if(o instanceof CombinationBean){
                                int index = mFollowCombinationList.indexOf(o);
                                if(index != -1){
                                    CombinationBean combinationBean = (CombinationBean) o;
                                    combinationBean.setFollowed(mFollowCombinationList.get(index).isFollowed());
                                }
                            }
                        }
                        break;
                }
            }
            addTitle();
            mListView.setVisibility(View.VISIBLE);
            tvEmptyText.setVisibility(View.GONE);
        } else {
            setEmptyText(getEmptyText());
        }
        mAdapter.notifyDataSetChanged();
    }

    private void addTitle() {
        int titleId = 0;
        switch (searchType) {
            case MORE_STOCK:
                titleId = R.string.blank_select_search_more_stock;
                break;
            case MORE_FUND:
                titleId = R.string.blank_select_search_more_fund;
                break;
            case MORE_FUND_MANAGER:
                titleId = R.string.blank_select_search_more_fund_manager;
                break;
            case MORE_USER:
                titleId = R.string.blank_select_search_more_user;
                break;
            case MORE_COMBINATION:
                titleId = R.string.blank_select_search_more_combination;
                break;
            case MORE_REWARD:
                titleId = R.string.blank_select_search_more_reward;
                break;
            case MORE_TOPIC:
                titleId = R.string.blank_select_search_more_topic;
                break;
        }
        mDataList.add(0, String.format(getResources().getString(titleId), getLoadEngine().getTotalcount()));
    }

    @Override
    public void loadFail() {

    }

    public String getEmptyText() {
        return "抱歉，未找到相关结果";
    }

    public void setListItemClick(AdapterView.OnItemClickListener listener) {
        if (null != listener && null != mListView) {
            mListView.setOnItemClickListener(listener);
        }
    }

    @Override
    public void onLoadMore() {
        if (null != getLoadEngine()) {
            if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
                return;
            }
            setHttpHandler(getLoadEngine().loadMore());
        }
    }

    public void refreshDataSize(int size) {

    }

    public HttpHandler getHttpHandler() {
        return mHttpHandler;
    }

    public void setHttpHandler(HttpHandler mHttpHandler) {
        if (null != this.mHttpHandler) {
            this.mHttpHandler.cancel();
        }
        this.mHttpHandler = mHttpHandler;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_back:
                finish();
                break;
            case R.id.tv_search:
                search(true);
                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            search(false);
        }
    }

    private List<SelectStockBean> mFollowStockList = new ArrayList<>();
    private List<CombinationBean> mFollowCombinationList = new ArrayList<>();

    private void loadVisitorData() {
        mFollowCombinationList.clear();
        mFollowStockList.clear();
        VisitorDataSource.getOptionalStockList(this, new IResultCallback<SelectStockBean>() {
            @Override
            public void onResultCallback(List<SelectStockBean> resultList) {
                mFollowStockList.addAll(resultList);
            }
        });
        VisitorDataSource.getOptionalStockList(this, new IResultCallback<CombinationBean>() {
            @Override
            public void onResultCallback(List<CombinationBean> resultList) {
                mFollowCombinationList.addAll(resultList);
            }
        });
//        mFollowCombinationList.addAll(new VisitorDataEngine().getCombinationBySort());
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);

    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void receiveStockData(SelectStockBean itemStock) {
        int index = mDataList.indexOf(itemStock);
        if (index != -1) {
            Object o = mDataList.get(index);
            if (o instanceof QuotesBean) {
                QuotesBean mQuotesBean = (QuotesBean) o;
                ((QuotesBean) o).setFollowed(itemStock.isFollowed);
                mAdapter.notifyDataSetChanged();
            }
        }
        if (!PortfolioApplication.hasUserLogin()) {
            index = mFollowStockList.indexOf(itemStock);
            if (index != -1) {
                mFollowStockList.remove(index);
                if (itemStock.isFollowed()) {
                    mFollowStockList.add(itemStock);
                }
            }
        }
    }
    @Subscribe
    public void receiveCombinationData(UpdateCombinationEvent updateCombination) {
        CombinationBean combinationBean = updateCombination.mCombinationBean;
        int index = mDataList.indexOf(combinationBean);
        if (index != -1) {
            Object o = mDataList.get(index);
            if (o instanceof CombinationBean) {
                CombinationBean mCombinationBean = (CombinationBean) o;
                mCombinationBean.setFollowed(combinationBean.isFollowed());
                mAdapter.notifyDataSetChanged();
            }
        }
        if (!PortfolioApplication.hasUserLogin()) {
            index = mFollowCombinationList.indexOf(combinationBean);
            if (index != -1) {
                mFollowCombinationList.remove(index);
                if (combinationBean.isFollowed()) {
                    mFollowCombinationList.add(combinationBean);
                }
            }
        }
    }

    @Override
    public int getPageStatisticsStringId() {
        switch (searchType) {
            case MORE_STOCK:
                //股票
                return R.string.statistics_SelectSearchMoreLoadMoreList_stock;
            case MORE_FUND:
                return R.string.statistics_SelectSearchMoreLoadMoreList_fund;
                //基金
            case MORE_FUND_MANAGER:
                //基金经理
                return R.string.statistics_SelectSearchMoreLoadMoreList_FUND_MANAGER;
            case MORE_USER:
                return R.string.statistics_SelectSearchMoreLoadMoreList_MORE_USER;
                //用户
            case MORE_COMBINATION:
                //组合
            return R.string.statistics_SelectSearchMoreLoadMoreList_MORE_COMBINATION;
            case MORE_REWARD:
                return R.string.statistics_SelectSearchMoreLoadMoreList_MORE_REWARD;
                //悬赏
            case MORE_TOPIC:
                //话题
                return R.string.statistics_SelectSearchMoreLoadMoreList_MORE_TOPIC;
        }
        return super.getPageStatisticsStringId();
    }
}

