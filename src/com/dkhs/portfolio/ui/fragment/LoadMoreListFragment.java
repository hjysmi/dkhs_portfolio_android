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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.lidroid.xutils.http.HttpHandler;

/**
 * @ClassName LoadMoreListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 上午9:50:28
 * @version 1.0
 */
public abstract class LoadMoreListFragment extends Fragment implements ILoadDataBackListener, OnLoadMoreListener {

    PullToRefreshListView mListView;

    private TextView tvEmptyText;

    private HttpHandler mHttpHandler;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_listview, null);
        initLoadMoreList(view);
        // setListAdatper();
        return view;
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

    private void initLoadMoreList(View view) {

        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        tvEmptyText = (TextView) view.findViewById(android.R.id.empty);

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

    abstract ListAdapter getListAdapter();

    abstract LoadMoreDataEngine getLoadEngine();

    abstract OnItemClickListener getItemClickListener();

    public void setListViewInit(ListView listview) {

    }

    public void loadData() {

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param object
     * @return
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
            // loadFinishUpdateView();
        }

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
            mHttpHandler.cancel();
        }
        this.mHttpHandler = mHttpHandler;
    }
}
