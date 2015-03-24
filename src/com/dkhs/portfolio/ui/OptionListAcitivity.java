package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.common.ConstantValue;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.adapter.OptionForOnelistAdapter;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.dkhs.portfolio.utils.UserEntityDesUtil;
import com.lidroid.xutils.DbUtils;
import com.umeng.analytics.MobclickAgent;

public class OptionListAcitivity extends ModelAcitivity {

    private PullToRefreshListView mListView;

    private boolean isLoadingMore;
    // private View mFootView;
    private Context context;
    private OptionForOnelistAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private TextView iv;
    private static final String SYMBOL = "symbol";
    private static final String TYPE = "type";
    private static final String NAME = "name";
    private String symbol;
    private String type;
    private String name;
    private RelativeLayout pb;

    public SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_option_market_news);
        context = this;
        mDataList = new ArrayList<OptionNewsBean>();

        iv = (TextView) findViewById(android.R.id.empty);
        pb = (RelativeLayout) findViewById(android.R.id.progress);
        pb.setVisibility(View.VISIBLE);
        // iv.setText("暂无公告");
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            symbol = extras.getString(SYMBOL);
            type = extras.getString(TYPE);
            name = extras.getString(NAME);
        }
        ((TextView) findViewById(R.id.tv_title)).setText(name + "公告");
        initDate();
    }

    public static Intent newIntent(Context context, String symbolName, String type, String name) {
        Intent intent = new Intent(context, OptionListAcitivity.class);
        Bundle b = new Bundle();
        b.putString(SYMBOL, symbolName);
        b.putString(TYPE, type);
        b.putString(NAME, name);
        intent.putExtras(b);
        return intent;
    }

    private void initDate() {
        UserEntity user;
        try {
            user = DbUtils.create(PortfolioApplication.getInstance()).findFirst(UserEntity.class);
            if (user != null) {
                if (!TextUtils.isEmpty(user.getAccess_token())) {
                    user = UserEntityDesUtil.decode(user, "ENCODE", ConstantValue.DES_PASSWORD);
                }
                String userId = user.getId() + "";
                NewsforImpleEngine vo = new NewsforImpleEngine();
                vo.setUserid(userId);
                vo.setSymbol(symbol);
                vo.setContentType(type);
                mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,
                        OpitionNewsEngineImple.NEWSFOREACH, vo);
                // mLoadDataEngine.setLoadingDialog(context);
                mLoadDataEngine.loadData();
                mLoadDataEngine.setFromYanbao(false);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void initView() {
        mListView = (PullToRefreshListView) findViewById(android.R.id.list);

        mListView.setEmptyView(iv);
        mOptionMarketAdapter = new OptionForOnelistAdapter(context, mDataList);
        mListView.setAdapter(mOptionMarketAdapter);
        mListView.setOnLoadListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {

                loadMore();
            }
        });
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
        //
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
        mListView.setOnItemClickListener(itemBackClick);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        });
    }

    OnItemClickListener itemBackClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            try {
                if (null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0) {
                    Intent intent = NewsActivity.newIntent(context, mDataList.get(position).getId(), "公告正文", mDataList
                            .get(position).getSymbols().get(0).getAbbrName(),
                            mDataList.get(position).getSymbols().get(0).getId());
                    startActivity(intent);
                } else {
                    Intent intent = NewsActivity
                            .newIntent(context, mDataList.get(position).getId(), "公告正文", null, null);
                    startActivity(intent);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private void loadMore() {
        if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                // Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }

            isLoadingMore = true;
            // mLoadDataEngine.setLoadingDialog(context);
            mLoadDataEngine.loadMore();
        }
    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
                pb.setVisibility(View.GONE);
                if (null != dataList && dataList.size() > 0) {
                    mDataList.addAll(dataList);
                    if (first) {
                        initView();
                        first = false;
                    }
                    mOptionMarketAdapter.notifyDataSetChanged();
                    loadFinishUpdateView();

                } else {
                    iv.setText("暂无公告");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    };

    private void loadFinishUpdateView() {
        mOptionMarketAdapter.notifyDataSetChanged();
        isLoadingMore = false;
        if (mListView != null) {
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_option_market_one);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
    }
}
