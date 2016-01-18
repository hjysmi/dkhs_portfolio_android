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
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshPageListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshPageListView.OnLoadMoreListener;

/**
 * @author zjz
 * @version 1.0
 * @ClassName LoadMoreListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-22 上午9:50:28
 */
public abstract class LoadPageMoreListFragment extends BaseFragment implements ILoadDataBackListener,
        OnLoadMoreListener {

    PullToRefreshPageListView mListView;
    private View mProgressView;
    // private ListAdapter mAdapter;
    // private BaseSelectActivity mActivity;

    // private List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();

    // private boolean isLoadingMore;
    // private View mFootView;
    private TextView tvEmptyText;

    // LoadMoreDataEngine mLoadDataEngine;

    // public LoadMoreListFragment(ListAdapter mAdapter, LoadMoreDataEngine engine) {
    // this.mAdapter = mAdapter;
    // this.mLoadDataEngine = engine;
    // }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initLoadMoreList(view);
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_page_selectstock;
    }

    // public void setListAdatper(ListAdapter mAdapter){
    // mListView.setAdapter(mAdapter);
    // }
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

        // mFootView = View.inflate(getActivity(), R.layout.layout_loading_more_footer, null);
        mListView = (PullToRefreshPageListView) view.findViewById(android.R.id.list);
        tvEmptyText = (TextView) view.findViewById(android.R.id.empty);
        mProgressView = view.findViewById(R.id.my_progressbar);
        // mListView.setEmptyView(tvEmptyText);
        // mListView.addFooterView(mFootView);
        mListView.setAdapter(getListAdapter());
        mListView.setOnItemClickListener(getItemClickListener());
        setListViewInit(mListView);
        // mListView.removeFooterView(mFootView);
        // mListView.setOnScrollListener(new OnScrollListener() {
        //
        // @Override
        // public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //
        // switch (scrollState) {
        // case OnScrollListener.SCROLL_STATE_IDLE:
        //
        // {
        // // 判断是否滚动到底部
        // if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
        // loadMore();
        //
        // }
        // }
        //
        // }
        //
        // }
        //
        // @Override
        // public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //
        // }
        // });

        // getLoadEngine().loadData();
        // loadData();
        // loadData();

    }

    /*
     * @Override
     * public void onActivityCreated(Bundle savedInstanceState) {
     * // TODO Auto-generated method stub
     * super.onActivityCreated(savedInstanceState);
     * loadData();
     * 
     * }
     */

    private void loadMore() {
        if (null != getLoadEngine()) {
            if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
                // Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            // mListView.addFooterView(mFootView);
            // isLoadingMore = true;
            getLoadEngine().loadMore();
        }

    }

    abstract ListAdapter getListAdapter();

    abstract LoadMoreDataEngine getLoadEngine();

    abstract OnItemClickListener getItemClickListener();

    public void setListViewInit(PullToRefreshPageListView listview) {

    }

    public void loadData() {

    }

    // ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {
    //
    // @Override
    // public void loadFinish(List<SelectStockBean> dataList) {
    // if (null != dataList) {
    // mDataList.addAll(dataList);
    // mAdapterConbinStock.notifyDataSetChanged();
    // loadFinishUpdateView();
    // }
    //
    // }
    //
    // };

    // public void loadFinishUpdateView() {
    // isLoadingMore = false;
    // if (mListView != null) {
    // mListView.removeFooterView(mFootView);
    // }
    // }

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
                    mListView.setOnLoadListener(LoadPageMoreListFragment.this);
            }
            // loadFinishUpdateView();
        }
        // if (null != dataList && isAdded()) {
        // if (isRefresh) {
        // // mDataList.clear();
        //
        // isRefresh = false;
        // }
        // // mDataList.addAll(dataList);
        // mAdapterConbinStock.notifyDataSetChanged();
        // loadFinishUpdateView();
        // }

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
            getLoadEngine().loadMore();
        }
    }

    public void refreshDataSize(int size) {

    }

    public void showProgress(){
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void dissProgress(){
        if(mProgressView.getVisibility() == View.VISIBLE){
            mProgressView.setVisibility(View.GONE);
        }
    }
}
