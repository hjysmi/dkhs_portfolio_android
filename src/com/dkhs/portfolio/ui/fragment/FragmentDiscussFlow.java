/**
 * @Title Fragment_discuss_flow.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-9 下午4:56:51
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.DiscussFlowAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * @ClassName Fragment_discuss_flow
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-9 下午4:56:51
 * @version 1.0
 */
public class FragmentDiscussFlow extends Fragment {

    private ListView mListView;
    private DiscussFlowAdapter mFlowAdapter;
    private View mFootView;
    private boolean isLoadingMore;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFlowAdapter = new DiscussFlowAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discuss_flow, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mFootView = View.inflate(getActivity(), R.layout.layout_loading_more_footer, null);
        mListView = (ListView) view.findViewById(R.id.lv_discuss_flow);
        mListView.addFooterView(mFootView);
        mListView.setAdapter(mFlowAdapter);

        mListView.removeFooterView(mFootView);
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(isLoadingMore)) {
                    System.out.println("Loading more");
                    mListView.addFooterView(mFootView);
                    // Thread thread = new Thread(null, loadMoreListItems);
                    // thread.start();
                }

            }
        });

    }

}
