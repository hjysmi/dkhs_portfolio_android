package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.adapter.OptionMarketAdapter;
import com.dkhs.portfolio.utils.UIUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * 个股新闻，公告，研报
 * Created by xuetong on 2016/1/13.
 */
public class StockNewsActivity extends ModelAcitivity {

    public static final String STOCK_NAME = "stock_name";
    public static final String SYMBOL = "symbol";
    public static final String CONTENT_TYPE = "content_type";
    public static final String CONTENT = "content";
    public final static String VOO = "bigvo";

    public static Intent newIntent(Context context, String symboName, String symbol, String content_type,String content) {
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


    private ListView mListView;

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
        context = this;
        initView();
        initDate();
    }


    private void initDate() {
        if (null != getIntent()) {
            vo = Parcels.unwrap(getIntent().getParcelableExtra(VOO));
            mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, OpitionNewsEngineImple.STOCK_All_NEWS, vo);
            ((OpitionNewsEngineImple) mLoadDataEngine).loadDatas();
        }

    }

    private void initView() {

        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        tv = (TextView) findViewById(android.R.id.empty);
        pb = (RelativeLayout) findViewById(android.R.id.progress);
        if (!(null != mDataList && mDataList.size() > 0)) {
            pb.setVisibility(View.VISIBLE);
        }
        mDataList = new ArrayList<>();

        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setEmptyView(tv);
        mListView.addFooterView(mFootView);
        mOptionMarketAdapter = new OptionMarketAdapter(mContext, mDataList);
        // if(null != mContext && mContext instanceof StockQuotesActivity){
        //  mOptionlistAdapter = new OptionForOnelistAdapter(context, mDataList);
        mListView.setAdapter(mOptionMarketAdapter);
        // }else{
        // mListView.setAdapter(mOptionMarketAdapter);
        // }

        mListView.removeFooterView(mFootView);


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

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
        mListView.setOnItemClickListener(itemBackClick);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

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

        mSwipeLayout.setEnabled(false);


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

    public void loadMore() {
        if (null != mLoadDataEngine && !isLoadingMore) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                // Toast.makeText(mContext, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            mListView.addFooterView(mFootView);

            isLoadingMore = true;
            // mLoadDataEngine.setLoadingDialog(getActivity());
            mLoadDataEngine.loadMore();
        }
    }


    LoadNewsDataEngine.ILoadDataBackListener mSelectStockBackListener = new LoadNewsDataEngine.ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
                pb.setVisibility(View.GONE);
                if (null != dataList && dataList.size() > 0) {
                    if (!isLoadingMore) {
                        mDataList.clear();
                    }
                    mDataList.addAll(dataList);
                    if (first || vo.getContentType().equals("10")) {
                        // initView(view);
                        first = false;
                    }
                    loadFinishUpdateView();
                } else {
                    tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
                   /* if (null != vo && null != vo.getPageTitle()) {
                        tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
                    }*/
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void loadingFail() {
            if (null != pb) {
                pb.setVisibility(View.GONE);
            }

            if (null == mDataList || mDataList.isEmpty()) {
                if (null != vo && null != vo.getPageTitle()) {
                    tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
                }
            }

        }

    };

    private void loadFinishUpdateView() {
        Log.e(TAG, "loadFinishUpdateView");
        mOptionMarketAdapter.notifyDataSetChanged();
        isLoadingMore = false;
        if (mListView != null) {
            mListView.removeFooterView(mFootView);
        }

    }


}
