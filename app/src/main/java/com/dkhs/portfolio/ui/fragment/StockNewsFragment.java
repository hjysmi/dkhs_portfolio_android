package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.itemhandler.StockNewsHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.ui.StockNewsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 个股新闻，公告，研报
 * Created by xuetong on 2016/1/13.
 */
public class StockNewsFragment extends LoadMoreListFragment {
    private String symbolname;
    private String content_type;
    private BaseAdapter mAdapter;
    private List<OptionNewsBean> mDataList;
    public static Fragment newIntent(String symbol, String content_type) {
        StockNewsFragment stockNewsFragment = new StockNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StockNewsActivity.CONTENT_TYPE, content_type);
        bundle.putString(StockNewsActivity.STOCK_NAME, symbol);
        stockNewsFragment.setArguments(bundle);
        return stockNewsFragment;
    }

    public StockNewsFragment() {
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataList = new ArrayList<>();
        mListView.setDivider(null);
        mListView.setAdapter(getListAdapter());
        postDelayedeData();
       /* if(getActivity() instanceof MainActivity){
            final Bundle bundle=((MainActivity)getActivity()).mBundle;
            if(bundle !=null) {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    //    handleIntent(bundle);

                    }
                },1200);
            }
        }*/
    }
  /*  public void handleIntent(Bundle bundle){
        if (bundle.containsKey("order_index")) {
            int sortType = bundle.getInt("order_index", 0);
            if(mSpinnerHandler != null){
                mSpinnerHandler.setSelection(sortType);
            }
        }
    }*/

    @Override
    public int setContentLayoutId() {
        return R.layout.empty_listview_reward;
    }
    @Override
    ListAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new DKBaseAdapter(mActivity, mDataList).buildSingleItemView(new StockNewsHandler(getActivity()));
        }
        return mAdapter;
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {


        return null;
    }

    @Override
    SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {

        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        };
    }

    @Override
    AdapterView.OnItemClickListener getItemClickListener() {

        return null;
    }

    @Override
    public void loadFail() {

    }

    @Override
    public void requestData() {

    }
}
