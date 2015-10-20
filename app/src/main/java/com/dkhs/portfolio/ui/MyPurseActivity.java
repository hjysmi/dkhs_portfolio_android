package com.dkhs.portfolio.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dkhs.adpter.adapter.DKBaseAdapter;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.AccountInfoBean;
import com.dkhs.portfolio.bean.BindThreePlat;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NoDataBean;
import com.dkhs.portfolio.bean.WalletChangeBean;
import com.dkhs.portfolio.bean.itemhandler.WalletChangeHandler;
import com.dkhs.portfolio.bean.itemhandler.combinationdetail.NoDataHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.WalletEngineImpl;
import com.dkhs.portfolio.engine.LocalDataEngine.WalletExchangeEngineImpl;
import com.dkhs.portfolio.engine.UserEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.PayResEvent;
import com.dkhs.portfolio.ui.eventbus.WithDrawEvent;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.mingle.autolist.AutoList;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * 主贴详情
 */
public class MyPurseActivity extends ModelAcitivity implements LoadMoreDataEngine.ILoadDataBackListener, PullToRefreshListView.OnLoadMoreListener {

    private static final int WITH_DRAW_AVAIL = 0;
    private static final int WITH_DRAW_UNAVAIL = 1;
    public static final String AVAIL_AMOUNT = "avail_amount";
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
    private boolean withDrawAvailable = false;
    private float available = 0;

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
                available = bean.available;
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
                UserEngineImpl.queryThreePlatBind(bindsListener);
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

    @Subscribe
    public void updateData(WithDrawEvent event){
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

    private ParseHttpListener<List<BindThreePlat>> bindsListener = new ParseHttpListener<List<BindThreePlat>>() {

        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
        }

        @Override
        protected List<BindThreePlat> parseDateTask(String jsonData) {
            LogUtils.e(jsonData);
            return DataParse.parseArrayJson(BindThreePlat.class, jsonData);
        }

        @Override
        protected void afterParseData(List<BindThreePlat> entity) {
            if (null != entity && entity.size() > 0) {
                for(BindThreePlat bindThreePlat :entity){
                    if(bindThreePlat.getProvider().equals("mobile")&&bindThreePlat.isStatus()){//取provider为mobile中的status这个值判断当前用户是否绑定过手机号
                        Log.d("wys","bound"+bindThreePlat.getUsername());
                        withDrawAvailable = true;
                    }
                }
                Message msg = Message.obtain();
                /*if(withDrawAvailable){
                    msg.what = WITH_DRAW_AVAIL;
                }else{
                    msg.what = WITH_DRAW_UNAVAIL;
                }
                msg.setTarget();*/
                if(withDrawAvailable){
                    Intent intent =  new Intent(MyPurseActivity.this,WithDrawActivity.class);
                    intent.putExtra(AVAIL_AMOUNT,available);
                    startActivity(intent);
                }else{
                    showBoundMobileDialog();
                }
            }

        }
    };

    private void showBoundMobileDialog(){
            MAlertDialog builder = PromptManager.getAlertDialog(this);
            builder.setMessage(R.string.msg_bound_mobile).setPositiveButton(R.string.btn_bound_mobile, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(RLFActivity.bindPhoneIntent(MyPurseActivity.this));
                }
            }).setNegativeButton(R.string.cancel, null);
        builder.show();
    }
}
