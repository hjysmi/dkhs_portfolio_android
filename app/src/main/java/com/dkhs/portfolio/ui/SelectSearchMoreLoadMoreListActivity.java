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
import com.dkhs.portfolio.base.widget.ImageButton;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.bean.itemhandler.TopicsHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreCombinationHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreFundManagerHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreStockFundHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreTitleHandler;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreType;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreUserHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.SelectGeneralSearchMoreEngineImpl;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.ClearableEditText;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PromptManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcm on 2015/11/20.
 */
public class SelectSearchMoreLoadMoreListActivity extends ModelAcitivity implements LoadMoreDataEngine.ILoadDataBackListener, PullToRefreshListView.OnLoadMoreListener,View.OnClickListener {

    private static String SEARCH_STRING = "search_string";
    private static String SEARCH_TYPE = "search_type";
    private String searchString;
    private SearchMoreType  searchType;
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
        initLoadMoreList();
        BusProvider.getInstance().register(this);
        postDelayedeData();
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

    public void initView(){
        etSearch.setText(searchString);
        if(searchType == SearchMoreType.MORE_REWARD ||searchType == SearchMoreType.MORE_TOPIC){
            tvCancel.setVisibility(View.VISIBLE);
            tvCancel.setText(R.string.search);
            tvCancel.setOnClickListener(this);
        }else{
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
                mAdapter.buildMultiItemView(QuotesBean.class, new SearchMoreUserHandler());
                break;
            case MORE_USER:
                mAdapter.buildMultiItemView(QuotesBean.class, new SearchMoreFundManagerHandler());
                //基金经理
                break;
            case MORE_COMBINATION:
                //组合
                mAdapter.buildMultiItemView(QuotesBean.class, new SearchMoreCombinationHandler(mContext));
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

    private SelectGeneralSearchMoreEngineImpl getLoadEngine(){
        if(mLoadMoreDataEngine == null)
            mLoadMoreDataEngine = new SelectGeneralSearchMoreEngineImpl(this,searchType);
        mLoadMoreDataEngine.setSearchString(searchString);
        return mLoadMoreDataEngine;
    }





    public void setListViewInit(ListView listview) {

    }

    public void postDelayedeData() {
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
//        if (object.getCurrentPage() == 1 && object.getResults().size() == 0) {
//            setEmptyText(getEmptyText());
//        }
        if (getLoadEngine().getCurrentpage() == 1 || getLoadEngine().getCurrentpage() == 0) {
            mDataList.clear();
        } else if (mDataList.size() > 0){
            mDataList.remove(0);
        }
        mDataList.addAll(object.getResults());
        if(mDataList.size() > 0){
            addTitle();
        }else{
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
        mDataList.add(0,String.format(getResources().getString(titleId), getLoadEngine().getTotalcount()));
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
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search_back:
                finish();
                break;
            case R.id.tv_search:
                search(true);
                break;
        }
    }

    private  class MyTextWatcher implements TextWatcher{

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
}

