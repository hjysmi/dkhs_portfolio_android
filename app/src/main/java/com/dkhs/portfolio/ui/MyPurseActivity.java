package com.dkhs.portfolio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AccountInfoBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NoDataBean;
import com.dkhs.portfolio.bean.WalletChangeBean;
import com.dkhs.portfolio.bean.itemhandler.WalletChangeHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.NoDataHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LocalDataEngine.WalletEngineImpl;
import com.dkhs.portfolio.engine.LocalDataEngine.WalletExchangeEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.PayResEvent;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mingle.autolist.AutoList;
import com.squareup.otto.Subscribe;

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

    private WalletExchangeEngineImpl mWalletEngineImpl;
    private AutoList<Object> mDataList = new AutoList<>().applyAction(WalletChangeBean.class);
    private DKBaseAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse);
        setTitle(R.string.info_title_purse);
        ViewUtils.inject(this);
        BusProvider.getInstance().register(this);
        initData();
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        loadData();
        getAccountInfo();

    }

    private void getAccountInfo(){
        WalletEngineImpl.getMineAccountInfo(new ParseHttpListener() {
            @Override
            protected Object parseDateTask(String jsonData) {
                Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                return gson.fromJson(jsonData, AccountInfoBean.class);
            }

            @Override
            protected void afterParseData(Object object) {
                AccountInfoBean bean = (AccountInfoBean) object;
                float available = bean.available;
                mBalanceTv.setText(String.valueOf(available));
            }
        });
    }

    private void initData(){
        mAdapter = new DKBaseAdapter(this,mDataList).buildMultiItemView(WalletChangeBean.class,new WalletChangeHandler(this))
        .buildMultiItemView(NoDataBean.class,new NoDataHandler());
        mRecordListView.setAdapter(mAdapter);
    }
    private void loadData() {
        mSwipeLayout.setRefreshing(true);
        getLoadEngine().loadData();
    }

    private LoadMoreDataEngine getLoadEngine() {
        if(mWalletEngineImpl == null){
            mWalletEngineImpl = new WalletExchangeEngineImpl(this);
        }
        return mWalletEngineImpl;
    }

    @Override
    public void loadFinish(MoreDataBean object) {
        mSwipeLayout.setRefreshing(false);
        if (mWalletEngineImpl.getCurrentpage() == 1) {
            mDataList.clear();
            if (object.getResults().size() == 0) {
                NoDataBean noDataBean = new NoDataBean();
                mDataList.add(noDataBean);
            }
        }
        mDataList.addAll(object.getResults());
        mAdapter.notifyDataSetChanged();
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

    @Subscribe
    public void updateData(PayResEvent event){
        if(event.errCode == 0){
            loadData();
            getAccountInfo();
        }
    }

    @Override
    public void onLoadMore() {
        getLoadEngine().loadMore();
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
}
