package com.dkhs.portfolio.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsforImpleEngine;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.adapter.OptionlistAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentreportNewsList;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

public class ReportForOneListActivity extends ModelAcitivity implements OnLoadMoreListener {
    private PullToRefreshListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private OptionlistAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private TextView iv;
    private static final String SYMBOL = "symbol";
    private static final String NAME = "name";
    private static final String SUB = "sub";
    private String symbol;
    private String name;
    private String subType;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_option_market_news);
        context = this;
        mDataList = new ArrayList<OptionNewsBean>();

        iv = (TextView) findViewById(android.R.id.empty);
        // iv.setText("暂无公告");
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            symbol = extras.getString(SYMBOL);
            name = extras.getString(NAME);
            subType = extras.getString(SUB);
        }
        ((TextView) findViewById(R.id.tv_title)).setText("研报-" + name);
        initDate();
    }

    public static Intent newIntent(Context context, String symbolName, String name, String subType) {
        Intent intent = new Intent(context, ReportForOneListActivity.class);
        Bundle b = new Bundle();
        b.putString(SYMBOL, symbolName);
        b.putString(NAME, name);
        b.putString(SUB, subType);
        intent.putExtras(b);
        return intent;
    }

    private void initDate() {
        try {
            NewsforImpleEngine vo = new NewsforImpleEngine();
            vo.setSymbol(symbol);
            vo.setContentSubType(subType);
            if (null == subType) {
                mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,
                        OpitionNewsEngineImple.NEWS_OPITION_FOREACH, vo);
            } else {
                mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener,
                        OpitionNewsEngineImple.GROUP_FOR_ONE, vo);
            }
//            mLoadDataEngine.setLoadingDialog(context);
            mLoadDataEngine.loadData();
            mLoadDataEngine.setFromYanbao(false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void initView() {
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        mListView = (PullToRefreshListView) findViewById(android.R.id.list);

        mListView.setEmptyView(iv);
//        mListView.addFooterView(mFootView);
        mOptionMarketAdapter = new OptionlistAdapter(context, mDataList);
        mListView.setAdapter(mOptionMarketAdapter);

//        mListView.removeFooterView(mFootView);
//        mListView.setOnScrollListener(new OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
//
//                switch (scrollState) {
//                    case OnScrollListener.SCROLL_STATE_IDLE:
//
//                    {
//                        // 判断是否滚动到底部
//                        if (absListView.getLastVisiblePosition() == absListView.getCount() - 1 && !isLoadingMore) {
//                            loadMore();
//
//                        }
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
        mListView.setOnItemClickListener(itemBackClick);

    }

    OnItemClickListener itemBackClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            try {
                Intent intent;
                if (null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0) {
                    intent = YanbaoNewsActivity.newIntent(context, mDataList.get(position).getId(),
                            mDataList.get(position).getSymbols().get(0).getSymbol(), mDataList.get(position)
                                    .getSymbols().get(0).getAbbrName());
                } else {
                    intent = YanbaoNewsActivity.newIntent(context, mDataList.get(position).getId(), null, null);
                }
                startActivity(intent);
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
            mListView.addFooterView(mFootView);

            isLoadingMore = true;
            mLoadDataEngine.setLoadingDialog(context);
            mLoadDataEngine.loadMore();
        }
    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
            	mListView.onLoadMoreComplete();
            	if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine
    					.getTotalpage()) {
            		mListView.setCanLoadMore(false);
    				mListView.setAutoLoadMore(false);
            	}else{
            		mListView.setCanLoadMore(true);
    				mListView.setAutoLoadMore(true);
    				if(mLoadDataEngine.getCurrentpage() == 1)
    					mListView.setOnLoadListener(ReportForOneListActivity.this);
            	}
                if (null != dataList && dataList.size() > 0) {
                    mDataList.addAll(dataList);
                    if (first) {
                        initView();
                        first = false;
                    }
                    mOptionMarketAdapter.notifyDataSetChanged();
//                    loadFinishUpdateView();

                } else {
                    iv.setText("暂无研报");
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
            mListView.removeFooterView(mFootView);
        }
    }
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_yanbao_list);
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onLoadMore() {
		if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                // Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }

            isLoadingMore = true;
            mLoadDataEngine.loadMore();
        }
	}
}
