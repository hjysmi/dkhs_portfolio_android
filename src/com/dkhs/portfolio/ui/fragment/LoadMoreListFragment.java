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
import android.widget.ListView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;

/**
 * @ClassName LoadMoreListActivity
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-22 上午9:50:28
 * @version 1.0
 */
public class LoadMoreListFragment extends Fragment {

    ListView mListView;
    // private ListAdapter mAdapterConbinStock;
    // private BaseSelectActivity mActivity;

    // private List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();

    private boolean isLoadingMore;
    private View mFootView;

    LoadMoreDataEngine mLoadDataEngine;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_listview, null);
        initLoadMoreList(view);
        return view;
    }

    private void initLoadMoreList(View view) {
        mFootView = View.inflate(getActivity(), R.layout.layout_loading_more_footer, null);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        mListView.addFooterView(mFootView);
        // mListView.setAdapter(mAdapterConbinStock);

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

    }

    private void loadMore() {
        if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            mListView.addFooterView(mFootView);
            isLoadingMore = true;
            mLoadDataEngine.loadMore();
        }

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

    public void loadFinishUpdateView() {
        isLoadingMore = false;
        if (mListView != null) {
            mListView.removeFooterView(mFootView);
        }
    }
}
