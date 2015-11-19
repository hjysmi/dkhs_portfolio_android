package com.dkhs.portfolio.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.engine.LoadNewsDataEngine;
import com.dkhs.portfolio.engine.LoadNewsDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.NewsforModel;
import com.dkhs.portfolio.engine.OpitionNewsEngineImple;
import com.dkhs.portfolio.ui.NewsActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.TopicsDetailActivity;
import com.dkhs.portfolio.ui.adapter.OptionForOnelistAdapter;
import com.dkhs.portfolio.ui.widget.IScrollExchangeListener;
import com.dkhs.portfolio.ui.widget.IStockQuoteScrollListener;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 需要优化界面
 * 个股行情界面，个股界面时（公告 TAB）
 */
public class FragmentNewsList extends Fragment implements Serializable, IScrollExchangeListener {
    /**
     *
     */
    private static final long serialVersionUID = 6565512311564641L;

    private ListView mListView;

    private boolean isLoadingMore;
    private View mFootView;
    private Context context;
    // private OptionMarketAdapter mOptionMarketAdapter;
    private List<OptionNewsBean> mDataList;
    private LoadNewsDataEngine mLoadDataEngine;
    boolean first = true;
    public final static String NEWS_TYPE = "newsNum";
    public final static String VO = "bigvo";
    public final static String LAYOUT = "layout";

    private static final String TAG = "FragmentNewsList";
    private NewsforModel vo;
    private TextView tv;
    private boolean getadle = true;
    private OptionForOnelistAdapter mOptionlistAdapter;
    private RelativeLayout pb;
    public SwipeRefreshLayout mSwipeLayout;
    private View mContentView;

    public static FragmentNewsList newIntent(String stockCode) {
        FragmentNewsList noticeFragemnt = new FragmentNewsList();
        NewsforModel vo;
        Bundle b2 = new Bundle();
        b2.putInt(FragmentNewsList.NEWS_TYPE, OpitionNewsEngineImple.NEWSFOREACH);
        vo = new NewsforModel();
        vo.setSymbol(stockCode);
        vo.setContentType("20");
        vo.setPageTitle("公告正文");
        b2.putParcelable(FragmentNewsList.VO, Parcels.wrap(vo));
        noticeFragemnt.setArguments(b2);
        return noticeFragemnt;
    }

    // private LinearLayout layouts;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_option_market_news, null);
        context = getActivity();

        if (null != context && context instanceof StockQuotesActivity && getadle) {
//            ((StockQuotesActivity) getActivity()).setLayoutHeight(2);
        }
        initView(view);
        // if (null != vo && vo.getContentType().equals("20")) {

        // }
        if (!isViewShown) {
            initDate();
        }
        mContentView = view.findViewById(R.id.ll_content);
        return view;
    }

    private void initDate() {

        Bundle bundle = getArguments();

        if (null != bundle) {
            vo = Parcels.unwrap(bundle.getParcelable(VO));
            // layouts = vo.getLayout();
            int types = bundle.getInt(NEWS_TYPE);
            mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, types, vo);
            // mLoadDataEngine.setLoadingDialog(getActivity());
            mLoadDataEngine.loadData();
        }

    }

    private void initView(View view) {

        mFootView = View.inflate(context, R.layout.layout_loading_more_footer, null);
        tv = (TextView) view.findViewById(android.R.id.empty);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        if (!(null != mDataList && mDataList.size() > 0)) {
            pb.setVisibility(View.VISIBLE);
        }
        mDataList = new ArrayList<OptionNewsBean>();

        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(tv);
        mListView.addFooterView(mFootView);
        // mOptionMarketAdapter = new OptionMarketAdapter(mContext, mDataList);
        // if(null != mContext && mContext instanceof StockQuotesActivity){
        mOptionlistAdapter = new OptionForOnelistAdapter(context, mDataList);
        mListView.setAdapter(mOptionlistAdapter);

        // }else{
        // mListView.setAdapter(mOptionMarketAdapter);
        // }

        mListView.removeFooterView(mFootView);


        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE:

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
                if (firstVisibleItem == 0) {
                    scrollParent();
                }
            }
        });
        mListView.setOnItemClickListener(itemBackClick);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.theme_blue);
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

        mSwipeLayout.setEnabled(false);

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    OnItemClickListener itemBackClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            try {
                if (null != mDataList.get(position).getSymbols() && mDataList.get(position).getSymbols().size() > 0) {
                    Intent intent = NewsActivity.newIntent(context, mDataList.get(position).getId(), vo.getPageTitle(),
                            mDataList.get(position).getSymbols().get(0).getAbbrName(), mDataList.get(position)
                                    .getSymbols().get(0).getId());
//                    startActivity(intent);
                    TopicsDetailActivity.startActivity(getActivity(),mDataList.get(position).getId());
                } else {
                    Intent intent = NewsActivity.newIntent(context, mDataList.get(position).getId(), vo.getPageTitle(),
                            null, null);
                    TopicsDetailActivity.startActivity(getActivity(),mDataList.get(position).getId());
//                    startActivity(intent);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    public void loadMore() {
        if (null != mLoadDataEngine && !isLoadingMore && getadle) {
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

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<OptionNewsBean> dataList) {
            try {
                pb.setVisibility(View.GONE);
                if (null != dataList && dataList.size() > 0) {
                    if (!isLoadingMore) {
                        mDataList.clear();
                    }
                    mDataList.addAll(dataList);
                    /*
                     * if (null != mContext
                     * && mContext instanceof StockQuotesActivity && getadle) {
                     * ((StockQuotesActivity) getActivity()).setLayoutHeight(mDataList.size());
                     * }
                     */
                    if (first || vo.getContentType().equals("20")) {
                        // initView(view);
                        first = false;
                    }
                    // layouts.getLayoutParams().height = dataList.size() * 150;
                    // mOptionMarketAdapter.notifyDataSetChanged();
                    if (null != mOptionlistAdapter) {

                        loadFinishUpdateView();
                    }
                } else {
                    if (null != vo && null != vo.getPageTitle()) {
                        tv.setText("暂无" + vo.getPageTitle().substring(0, vo.getPageTitle().length() - 2));
                    }
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
        mOptionlistAdapter.notifyDataSetChanged();
        int height = 0;
        if (null != mOptionlistAdapter) {
            mOptionlistAdapter.notifyDataSetChanged();
            for (int i = 0, len = mOptionlistAdapter.getCount(); i < len; i++) {
                View listItem = mOptionlistAdapter.getView(i, null, mListView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                int list_child_item_height = listItem.getMeasuredHeight() + mListView.getDividerHeight();
                height += list_child_item_height; // 统计所有子项的总高度
                if (null != mStockQuoteScrollListener) {
                    if (height > mStockQuoteScrollListener.getMaxListHeight()) {
                        height = mStockQuoteScrollListener.getMaxListHeight();
                        break;
                    }
                }
            }
        }
        isLoadingMore = false;
        if (mListView != null) {
            mListView.removeFooterView(mFootView);
        }
        mContentView.getLayoutParams().height = height;
//        ((StockQuotesActivity) getActivity()).setLayoutHeights(mListView.getLayoutParams().height);
//        if (null != mContext && mContext instanceof StockQuotesActivity) {
//        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_news);
    IStockQuoteScrollListener mStockQuoteScrollListener;

    public void setStockQuoteScrollListener(IStockQuoteScrollListener scrollListener) {
        this.mStockQuoteScrollListener = scrollListener;
    }



    private boolean isViewShown;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            // fragment可见时加载数据
            /*
             * mDataList = new ArrayList<OptionNewsBean>();
             * mLoadDataEngine = new OpitionNewsEngineImple(mSelectStockBackListener, types, vo);
             * mLoadDataEngine.setLoadingDialog(getActivity());
             * mLoadDataEngine.loadData();
             * mLoadDataEngine.setFromYanbao(false);
             * if (null != mContext && mContext instanceof StockQuotesActivity) {
             * ((StockQuotesActivity) getActivity()).setLayoutHeight(2);
             * }
             */
//            if (isVisibleToUser) {
//                getadle = true;
//                if (null == mDataList || mDataList.size() < 2) {
//                    if (null != mContext && mContext instanceof StockQuotesActivity && getadle) {
//                        ((StockQuotesActivity) getActivity()).setLayoutHeight(0);
//                    }
//                } else if (null != mDataList) {
//                    if (null != mContext && mContext instanceof StockQuotesActivity && getadle) {
//                        int height = 0;
//                        for (int i = 0, len = mOptionlistAdapter.getCount(); i < len; i++) {
//                            View listItem = mOptionlistAdapter.getView(i, null, mListView);
//                            listItem.measure(0, 0); // 计算子项View 的宽高
//                            int list_child_item_height = listItem.getMeasuredHeight() + mListView.getDividerHeight();
//                            height += list_child_item_height; // 统计所有子项的总高度
//                        }
//                        ((StockQuotesActivity) getActivity()).setLayoutHeights(height);
//                    }
//                }
//            }

            if (getView() != null) {
                isViewShown = true;

                initDate();
            } else {
                isViewShown = false;
            }
        } else {
            // 不可见时不执行操作
            getadle = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void scrollSelf() {

        Log.e(TAG, " scrollSelf");

        if (null != mStockQuoteScrollListener) {
            mStockQuoteScrollListener.scrollviewObatin();

        }
    }

    @Override
    public void scrollParent() {


        Log.e(TAG, " scrollParent");
        if (null != mStockQuoteScrollListener) {
            mStockQuoteScrollListener.interruptSrcollView();
        }
    }

}
