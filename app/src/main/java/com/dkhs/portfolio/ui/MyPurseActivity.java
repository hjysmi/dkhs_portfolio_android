package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 主贴详情
 */
public class MyPurseActivity extends ModelAcitivity implements LoadMoreDataEngine.ILoadDataBackListener, PullToRefreshListView.OnLoadMoreListener {

    @ViewInject(R.id.swipeRefreshLayout)
    private SwipeRefreshLayout mSwipeLayout;
    @ViewInject(R.id.lv_trading_record)
    private PullToRefreshListView mRecordListView;
    @ViewInject(R.id.tv_balance)
    private TextView mBalanceTv;
    @ViewInject(R.id.btn_balance_in)
    private TextView mBalanceInTv;
    @ViewInject(R.id.btn_balance_out)
    private TextView mBalanceOutTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse);
        setTitle(R.string.info_title_purse);
        ViewUtils.inject(this);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MyPurseActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
    }

    private void loadData() {
        mSwipeLayout.setRefreshing(true);
        getLoadEngine().loadData();
    }

    private LoadMoreDataEngine getLoadEngine() {
        return null;
    }

    @Override
    public void loadFinish(MoreDataBean object) {
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(true);
    }

    @OnClick({R.id.btn_balance_out, R.id.btn_balance_in})
    public void changeBalance(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_balance_in:
                intent = new Intent(MyPurseActivity.this, RechargeActivity.class);
                break;
            case R.id.btn_balance_out:
                break;
        }
        if (null != intent) {
            startActivity(intent);
        }
    }


    @Override
    public void onLoadMore() {
        getLoadEngine().loadMore();
    }
}
