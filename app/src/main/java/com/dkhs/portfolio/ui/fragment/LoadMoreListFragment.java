/**
 * @Title LoadMoreListActivity.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-22 上午9:50:28
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.lidroid.xutils.http.HttpHandler;

/**
 * @author zjz
 * @version 1.0
 * @ClassName LoadMoreListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-22 上午9:50:28
 */
public abstract class LoadMoreListFragment extends VisiableLoadFragment implements ILoadDataBackListener, OnLoadMoreListener {

    PullToRefreshListView mListView;

    private TextView tvEmptyText;

    private HttpHandler mHttpHandler;
    View mProgressView;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.empty_listview;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initLoadMoreList(view);
        super.onViewCreated(view, savedInstanceState);
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

    private void initLoadMoreList(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(setOnRefreshListener());
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        tvEmptyText = (TextView) view.findViewById(android.R.id.empty);
        mProgressView = (RelativeLayout) view.findViewById(android.R.id.progress);

        mListView.setAdapter(getListAdapter());
        mListView.setOnItemClickListener(getItemClickListener());
        setListViewInit(mListView);

    }

    //
    // private void loadMore() {
    // if (null != getLoadEngine()) {
    // if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
    // // Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
    // return;
    // }
    // getLoadEngine().loadMore();
    // }
    //
    // }

    public void showProgress(){
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void dismissProgress(){
        if(mProgressView.getVisibility() == View.VISIBLE){
            mProgressView.setVisibility(View.GONE);
        }
    }

    abstract ListAdapter getListAdapter();

    abstract LoadMoreDataEngine getLoadEngine();

    abstract SwipeRefreshLayout.OnRefreshListener setOnRefreshListener();

    abstract OnItemClickListener getItemClickListener();

    public void setListViewInit(ListView listview) {

    }

    public void loadData() {

    }

    public void postDelayedeData() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {

                loadData();

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
        if (isAdded()) {
            mListView.onLoadMoreComplete();
            if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
                mListView.setCanLoadMore(false);
                mListView.setAutoLoadMore(false);
            } else {
                mListView.setCanLoadMore(true);
                mListView.setAutoLoadMore(true);
                if (getLoadEngine().getCurrentpage() == 1)
                    mListView.setOnLoadListener(LoadMoreListFragment.this);
            }

            if (object.getCurrentPage() == 1 && object.getResults().size() == 0) {
                setEmptyText(getEmptyText());
            } else {
                setListViewVisible();
            }
//             loadFinishUpdateView();

        }

    }

    public String getEmptyText() {
        return "暂无数据";
    }

    public void setListItemClick(OnItemClickListener listener) {
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
}
