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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;

/**
 * @ClassName LoadMoreListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 上午9:50:28
 * @version 1.0
 */
public abstract class LoadMoreListFragment extends Fragment implements ILoadDataBackListener {

    ListView mListView;
    // private ListAdapter mAdapter;
    // private BaseSelectActivity mActivity;

    // private List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();

    private boolean isLoadingMore;
    private View mFootView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_listview, null);
        initLoadMoreList(view);
        // setListAdatper();
        return view;
    }

    // public void setListAdatper(ListAdapter mAdapter){
    // mListView.setAdapter(mAdapter);
    // }

    private void initLoadMoreList(View view) {
        mFootView = View.inflate(getActivity(), R.layout.layout_loading_more_footer, null);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        mListView.addFooterView(mFootView);
        mListView.setAdapter(getListAdapter());
        mListView.removeFooterView(mFootView);
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE:

                    {
                        // 判断是否滚动到底部
                        if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
                            loadMore();

                        }
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        getLoadEngine().loadData();

    }

    private void loadMore() {
        if (null != getLoadEngine()) {
            System.out.println("getLoadEngine().getCurrentpage() :" + getLoadEngine().getCurrentpage());
            System.out.println("getLoadEngine().getTotalpage() :" + getLoadEngine().getTotalpage());
            if (getLoadEngine().getCurrentpage() >= getLoadEngine().getTotalpage()) {
                Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            mListView.addFooterView(mFootView);
            isLoadingMore = true;
            getLoadEngine().loadMore();
        }

    }

    abstract ListAdapter getListAdapter();

    abstract LoadMoreDataEngine getLoadEngine();

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

    public void loadFinishUpdateView() {
        isLoadingMore = false;
        if (mListView != null) {
            mListView.removeFooterView(mFootView);
        }
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

            loadFinishUpdateView();
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
}
