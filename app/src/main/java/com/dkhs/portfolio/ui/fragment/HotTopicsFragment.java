package com.dkhs.portfolio.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.BannerTopicsBean;
import com.dkhs.portfolio.bean.TopicsBean;
import com.dkhs.portfolio.ui.adapter.HotTopicsAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.dkhs.adpter.adapter.AutoLoadMoreRVAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotTopicsFragment extends VisiableLoadFragment {


    @ViewInject(R.id.rv)
    RecyclerView mRv;
    private List mList;



    public HotTopicsFragment() {
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_hot_topics;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        test();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void requestData() {

    }

    private void test() {

        mList=new ArrayList();
        mList.add(new BannerTopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mList.add(new TopicsBean());
        mRv.setLayoutManager( new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRv.setAdapter( AutoLoadMoreRVAdapter.warp(new HotTopicsAdapter(mActivity,mList)));

    }

}
