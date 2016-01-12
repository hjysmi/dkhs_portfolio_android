package com.dkhs.portfolio.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.lidroid.xutils.http.HttpHandler;

/**
 * Created by zhangcm on 2015/9/21.15:50
 */
public abstract  class LoadMoreListActivity extends AssestsBaseActivity implements LoadMoreDataEngine.ILoadDataBackListener, PullToRefreshListView.OnLoadMoreListener {

    PullToRefreshListView mListView;

    private TextView tvEmptyText;

    private HttpHandler mHttpHandler;
    View mProgressView;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.empty_listview);
        initLoadMoreList();
        BusProvider.getInstance().register(this);
        postDelayedeData();
    }

    // add by zcm -----2014.12.15
    public void setListViewVisible() {
        if (mListView.getVisibility() == View.VISIBLE && tvEmptyText.getVisibility() == View.GONE)
            return;
        mListView.setVisibility(View.VISIBLE);
        tvEmptyText.setVisibility(View.GONE);
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

    public SwipeRefreshLayout mSwipeLayout;

    private void initLoadMoreList(){
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(setOnRefreshListener());
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mListView = (PullToRefreshListView) findViewById(android.R.id.list);
        tvEmptyText = (TextView) findViewById(android.R.id.empty);
        mProgressView = (RelativeLayout) findViewById(android.R.id.progress);

        mListView.setAdapter(getListAdapter());
        mListView.setOnItemClickListener(getItemClickListener());
        mListView.setDivider(null);
        setListViewInit(mListView);

    }


    abstract ListAdapter getListAdapter();

    abstract LoadMoreDataEngine getLoadEngine();

    abstract SwipeRefreshLayout.OnRefreshListener setOnRefreshListener();

    abstract AdapterView.OnItemClickListener getItemClickListener();

    abstract void loadData();

    public void setListViewInit(ListView listview) {

    }

    public void postDelayedeData(){
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadData();

            }
        },500);
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

            if (object.getCurrentPage()==1&& object.getResults().size()==0){
                setEmptyText(getEmptyText());
            }
    }

    public String getEmptyText(){
        return "暂无数据";
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
}
