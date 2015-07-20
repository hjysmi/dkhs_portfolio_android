package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestTopicsFragment extends LoadMoreListFragment {


    @Override
    ListAdapter getListAdapter() {
        return null;
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        return null;
    }

    @Override
    SwipeRefreshLayout.OnRefreshListener setOnRefreshListener() {
        return null;
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
