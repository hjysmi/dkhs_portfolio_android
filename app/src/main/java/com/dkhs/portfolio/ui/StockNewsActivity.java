package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.adapter.OptionMarketAdapter;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.utils.UIUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * 个股新闻，公告，研报
 * Created by xuetong on 2016/1/13.
 */
public class StockNewsActivity extends ModelAcitivity implements PullToRefreshListView.OnLoadMoreListener, View.OnClickListener {

    public static final String STOCK_NAME = "stock_name";
    public static final String SYMBOL = "symbol";
    public static final String CONTENT_TYPE = "content_type";
    public static final String CONTENT = "content";
    public final static String VOO = "bigvo";

    public static Intent newIntent(Context context, String symboName, String symbol, String content_type, String content) {
        Intent intent = new Intent(context, StockNewsActivity.class);
        intent.putExtra(STOCK_NAME, symboName);
        intent.putExtra(SYMBOL, symbol);
        intent.putExtra(CONTENT_TYPE, content_type);
        intent.putExtra(CONTENT, content);
        NewsforModel vo = new NewsforModel();
        vo.setSymboName(symboName);
        vo.setSymbol(symbol);
        vo.setContentType(content_type);
        vo.setPageTitle(content);
        intent.putExtra(VOO, Parcels.wrap(vo));
        return intent;
    }


    private PullToRefreshListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    private OptionMarketAdapter mOptionMarketAdapter;
    boolean first = true;
    public final static String NEWS_TYPE = "newsNum";
    public final static String VO = "bigvo";
    public final static String LAYOUT = "layout";

    private static final String TAG = "FragmentNewsList";
    private NewsforModel vo;
    private TextView tv;
    private RelativeLayout pb;
    public SwipeRefreshLayout mSwipeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_listview);
        setTitle(String.format(UIUtils.getResString(this, R.string.stocknews), getIntent().getStringExtra(STOCK_NAME), getIntent().getStringExtra(CONTENT)));
        vo = Parcels.unwrap(getIntent().getParcelableExtra(VOO));
        context = this;
        initView();
        initDate();
    }


    private void initDate() {
        mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, OpitionNewsEngineImple.STOCK_All_NEWS, vo);
        ((OpitionNewsEngineImple) mLoadDataEngine).loadDatas();
    }

    private TextView btnRefresh;

    private void initView() {
        btnRefresh = getSecondRightButton();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh_selector),
                null, null, null);
        btnRefresh.setOnClickListener(this);
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        tv = (TextView) findViewById(android.R.id.empty);
        pb = (RelativeLayout) findViewById(android.R.id.progress);
        if (!(null != mDataList && mDataList.size() > 0)) {
            pb.setVisibility(View.VISIBLE);
        }
        mDataList = new ArrayList<>();

        mListView = (PullToRefreshListView) findViewById(android.R.id.list);
        mListView.setEmptyView(tv);
        mListView.addFooterView(mFootView);
        mOptionMarketAdapter = new OptionMarketAdapter(mContext, mDataList);
        mListView.setAdapter(mOptionMarketAdapter);
        mListView.removeFooterView(mFootView);


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                    {
                        // 判断是否滚动到底部
                        if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
                            //   loadMore();
                            onLoadMore();
                        }

                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mListView.setOnItemClickListener(itemBackClick);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private boolean isRefresh;

    private void refreshData() {
        isRefresh = true;
        rotateRefreshButton();
        if (null != mLoadDataEngine) {
            ((OpitionNewsEngineImple) mLoadDataEngine).loadDatas();
        } else {
            tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
        }
    }

    public void stopRefreshAnimation() {
        btnRefresh.clearAnimation();
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refresh),
                null, null, null);
    }

    private void rotateRefreshButton() {
        btnRefresh.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.nav_refreshing),
                null, null, null);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_around_center_point);
        btnRefresh.startAnimation(animation);
    }

    AdapterView.OnItemClickListener itemBackClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            try {
                if (null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0) {
                    TopicsDetailActivity.startActivity(context, mDataList.get(position).getId());
                } else {
                    TopicsDetailActivity.startActivity(context, mDataList.get(position).getId());
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    LoadNewsDataEngine.ILoadDataBackListener mSelectStockBackListener = new LoadNewsDataEngine.ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
                pb.setVisibility(View.GONE);
                mSwipeLayout.setRefreshing(false);
                mListView.onLoadMoreComplete();
                if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                    mListView.setCanLoadMore(false);
                    mListView.setAutoLoadMore(false);
                } else {
                    mListView.setCanLoadMore(true);
                    mListView.setAutoLoadMore(true);
                    if (mLoadDataEngine.getCurrentpage() == 1)
                        mListView.setOnLoadListener(StockNewsActivity.this);
                }
                if (isRefresh) {
                    mDataList.clear();
                    isRefresh = false;
                }
                stopRefreshAnimation();
                if (null != dataList && dataList.size() > 0) {

                    mDataList.addAll(dataList);
                    mOptionMarketAdapter.notifyDataSetChanged();
                    hideEmptyText();
                } else {
                    setEmptyText();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void loadingFail() {
            pb.setVisibility(View.GONE);
            mSwipeLayout.setRefreshing(false);
            if (null == mDataList || mDataList.isEmpty()) {
                // iv.setText("暂无资讯");
                setEmptyText();
            } else {
                hideEmptyText();
            }
        }

    };

    private void hideEmptyText() {
        tv.setVisibility(View.GONE);
    }

    private void setEmptyText() {
        tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
    }

    @Override
    public void onClick(View v) {
        mSwipeLayout.setRefreshing(true);
        refreshData();
    }

    @Override
    public void onLoadMore() {
        if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                // Toast.makeText(mContext, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            isLoadingMore = true;
            mLoadDataEngine.loadMore();
        }
    }
}
