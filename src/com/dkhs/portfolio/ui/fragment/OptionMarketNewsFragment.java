package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dkhs.portfolio.ui.OptionListAcitivity;
import com.dkhs.portfolio.ui.adapter.OptionMarketAdapter;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 公告
 * 
 * @author weiting
 * 
 */
public class OptionMarketNewsFragment extends Fragment implements OnLoadMoreListener {
    private PullToRefreshListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    private OptionMarketAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    private TextView iv;
    private View view;
    public final static String NEWS_TYPE = "newsNum";
    public final static String VO = "bigvo";
    public final static String LAYOUT = "layout";
    private NewsforImpleEngine vo;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	view = inflater.inflate(R.layout.activity_option_market_news, null);
    	context = getActivity();
    	mDataList = new ArrayList<OptionNewsBean>();
        iv = (TextView) view.findViewById(android.R.id.empty);
        // iv.setText("暂无公告");
        Bundle bundle = getArguments();
        if (bundle != null) {
        	vo = (NewsforImpleEngine) bundle.getSerializable(VO);
        }
        initView();
        initDate();
		return view;
	}
	private void initDate() {
        try {
                NewsforImpleEngine vos = new NewsforImpleEngine();
                vos.setPortfolioId(vo.getPortfolioId());
                vos.setContentType(vo.getContentType());
                mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, OpitionNewsEngineImple.NEWS_GROUP_FOREACH,
                        vos);
//                mLoadDataEngine.setLoadingDialog(context);
                mLoadDataEngine.loadData();
                mLoadDataEngine.setFromYanbao(false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void initView() {
        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);

        mListView.setEmptyView(iv);
//        mListView.addFooterView(mFootView);
        mOptionMarketAdapter = new OptionMarketAdapter(context, mDataList);
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
                /*
                 * if(null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0){
                 * Intent intent = NewsActivity.newIntent(context, mDataList.get(position).getId(),
                 * "公告正文",mDataList.get(
                 * position).getSymbols().get(0).getAbbrName(),mDataList.get(position).getSymbols().get(0).getId());
                 * startActivity(intent);
                 * }else{
                 * Intent intent = NewsActivity.newIntent(context, mDataList.get(position).getId(), "公告正文",null,null);
                 * startActivity(intent);
                 * }
                 */
                Intent intent = OptionListAcitivity.newIntent(context, mDataList.get(position).getSymbols().get(0)
                        .getSymbol()
                        + "", "20", mDataList.get(position).getSymbols().get(0).getAbbrName());
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
    					mListView.setOnLoadListener(OptionMarketNewsFragment.this);
            	}
                if (null != dataList && dataList.size() > 0) {
                    mDataList.addAll(dataList);
                    /*if (first) {
                        initView();
                        first = false;
                    }*/
                    mOptionMarketAdapter.notifyDataSetChanged();
//                    loadFinishUpdateView();

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
            mListView.removeFooterView(mFootView);
        }
    }
    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_news_list);
    @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageEnd(mPageName);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageStart(mPageName);
	}
	@Override
	public void onLoadMore() {
		 if (null != mLoadDataEngine) {
	            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
	                // Toast.makeText(context, "没有更多的数据了", Toast.LENGTH_SHORT).show();
	                return;
	            }
	            mLoadDataEngine.loadMore();
	        }
	}
}
