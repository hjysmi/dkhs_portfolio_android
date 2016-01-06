package com.dkhs.portfolio.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.ui.widget.IScrollExchangeListener;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 需要优化界面
 * 个股行情界面，指数界面时（涨幅榜/跌幅榜/换手率榜）
 */
public class FragmentForStockSHC extends BaseFragment implements IScrollExchangeListener {
    private static final String TAG = "FragmentForStockSHC";
    /**
     *
     */
    private ListView mListView;

    private Context context;
    private List<SelectStockBean> mDataList;
    private LoadMoreDataEngine mLoadDataEngine;
    boolean first = true;
    private View mContentView;
    public final static String NEWS_TYPE = "newsNum";
    public final static String VO = "bigvo";
    public final static String LAYOUT = "layout";
    private MarketCenterItemAdapter mOptionlistAdapter;
    private Timer mMarketTimer;
    private static final long mPollRequestTime = 1000 * 5;
    private final static String EXCHANGE = "exchange";
    private final static String SORT = "sort";
    private final static String STYPE = "symbol_stype";
    private final static String SECTOR = "list_sector";
    private final static String BOOLEAN = "boolean";
    private String exchange;
    private StockViewType sort;
    private String symbol_stype;
    private String list_sector;
    private boolean setColor;
    private RelativeLayout pb;

    public static FragmentForStockSHC newIntent(String exchange, StockViewType sort, String symbol_stype, String list_sector,
                                                boolean setColor) {
        FragmentForStockSHC mFragmentForStockSHC = new FragmentForStockSHC();
        Bundle b = new Bundle();
        b.putString(EXCHANGE, exchange);
        b.putSerializable(SORT, sort);
        b.putString(STYPE, symbol_stype);
        b.putString(SECTOR, list_sector);
        b.putBoolean(BOOLEAN, setColor);
        mFragmentForStockSHC.setArguments(b);
        return mFragmentForStockSHC;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            initDate();
        }
        context = getActivity();

    }

    /**
     * @param view
     * @param savedInstanceState
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        initView(view);
//        if (null != mContext && mContext instanceof StockQuotesActivity && getadle) {
//            ((StockQuotesActivity) getActivity()).setLayoutHeight(2);
//        }
    }


    private void initDate() {
        Bundle bundle = getArguments();

        if (null != bundle) {
            sort = (StockViewType) bundle.getSerializable(SORT);
            exchange = bundle.getString(EXCHANGE);
            symbol_stype = bundle.getString(STYPE);
            list_sector = bundle.getString(SECTOR);
            setColor = bundle.getBoolean(BOOLEAN);
        }

    }

    private void initView(View view) {
//        view.findViewById(R.id.swipe_container).setEnabled(false);
        mContentView = view.findViewById(R.id.ll_content);
        TextView tv = (TextView) view.findViewById(android.R.id.empty);
        pb = (RelativeLayout) view.findViewById(android.R.id.progress);
        pb.setVisibility(View.VISIBLE);
        mDataList = new ArrayList<SelectStockBean>();

        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(tv);
        if (setColor) {
            mOptionlistAdapter = new MarketCenterItemAdapter(context, mDataList);
        } else {
            mOptionlistAdapter = new MarketCenterItemAdapter(context, mDataList, true);
        }
        mListView.setAdapter(mOptionlistAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//                Log.e(TAG, " onScroll firstVisibleItem:" + firstVisibleItem);
                if (firstVisibleItem == 0) {
                    scrollParent();
                }
            }

        });
        mLoadDataEngine = new OpitionCenterStockEngineImple(new StockLoadDataListener(), sort, 10, list_sector,
                symbol_stype, exchange);
        mLoadDataEngine.loadData();

    }

    OnItemClickListener itemBackClick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            UIUtils.startAnimationActivity(getActivity(),
                    StockQuotesActivity.newIntent(getActivity(), mDataList.get(position)));
        }
    };

    @Override
    public void scrollSelf() {

        Log.e(TAG, " scrollSelf");

//        mListView.getParent().requestDisallowInterceptTouchEvent(false);
//        if (null != mStockQuoteScrollListener) {
//            mStockQuoteScrollListener.scrollviewObatin();
//
//        }
    }

    @Override
    public void scrollParent() {


        Log.e(TAG, " scrollParent");
//        if (null != mStockQuoteScrollListener) {
//            mStockQuoteScrollListener.interruptSrcollView();
//        }
//        mListView.getParent().requestDisallowInterceptTouchEvent(true);
    }

    class StockLoadDataListener implements com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener {

        /**
         * @param object
         * @return
         * @Title
         * @Description TODO: (用一句话描述这个方法的功能)
         */
        @Override
        public void loadFinish(MoreDataBean object) {
            pb.setVisibility(View.GONE);
            if (null != object && null != object.getResults()) {
                mDataList.clear();
                mDataList.addAll(object.getResults());
                mOptionlistAdapter.notifyDataSetChanged();
                loadFinishUpdateView();
            }

        }

        /**
         * @return
         * @Title
         * @Description TODO: (用一句话描述这个方法的功能)
         */
        @Override
        public void loadFail() {
            pb.setVisibility(View.GONE);

        }


    }

    private void loadFinishUpdateView() {
        // mOptionMarketAdapter.notifyDataSetChanged();
        try {

            int height = 0;
            if (null != mOptionlistAdapter) {
                mOptionlistAdapter.notifyDataSetChanged();
                Log.e(TAG, " loadFinishUpdateView size:" + mOptionlistAdapter.getCount());
                Log.e(TAG, " last size name:" + mDataList.get(mDataList.size() - 1).getName());


                View listItem = mOptionlistAdapter.getView(0, null, mListView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                int list_child_item_height = listItem.getMeasuredHeight() + mListView.getDividerHeight();
                Log.e(TAG, " list_child_item_height :" + list_child_item_height);

//                for (int i = 0, len = mOptionlistAdapter.getCount(); i < len; i++) {
//
//                    height += list_child_item_height; // 统计所有子项的总高度
//                    if (null != mStockQuoteScrollListener) {
//                        if (height > mStockQuoteScrollListener.getMaxListHeight()) {
//                            height = mStockQuoteScrollListener.getMaxListHeight();
//                            Log.e(TAG, " break size:" + mDataList.get(i).getName());
//                            break;
//                        }
//                    }
//                }

                height = list_child_item_height * mOptionlistAdapter.getCount();
              /*  if (null != mStockQuoteScrollListener) {
                    if (height <= mStockQuoteScrollListener.getMaxListHeight()) {
                        mStockQuoteScrollListener = null;
                    }
                }*/


                mContentView.getLayoutParams().height = height;
//                mListView.getLayoutParams().height = height;
//                }
            }
//            if (null != mContext && mContext instanceof StockQuotesActivity && getadle) {
//                ((StockQuotesActivity) getActivity()).setLayoutHeights(height);
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_news);

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
//        if (mMarketTimer == null && isTimerStart) {
//            mMarketTimer = new Timer(true);
//            mMarketTimer.schedule(new RequestMarketTask(), mPollRequestTime, mPollRequestTime);
//        }
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (mMarketTimer != null) {
            mMarketTimer.cancel();
            mMarketTimer = null;
        }
    }

    public class RequestMarketTask extends TimerTask {

        @Override
        public void run() {
            if (UIUtils.roundAble(mLoadDataEngine.getStatu())) {
//                mLoadDataEngine.loadData();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            if (isVisibleToUser) {
//                if (null == mDataList || mDataList.size() < 2) {
//                    if (null != mContext && mContext instanceof StockQuotesActivity && getadle) {
//                        ((StockQuotesActivity) getActivity()).setLayoutHeight(0);
//                    }
//                } else if (null != mDataList) {
//                    int height = 0;
//                    for (int i = 0, len = mOptionlistAdapter.getCount(); i < len; i++) {
//                        View listItem = mOptionlistAdapter.getView(i, null, mListView);
//                        listItem.measure(0, 0); // 计算子项View 的宽高
//                        int list_child_item_height = listItem.getMeasuredHeight() + mListView.getDividerHeight();
//                        height += list_child_item_height; // 统计所有子项的总高度
//                    }
//                    if (null != mContext && mContext instanceof StockQuotesActivity && getadle) {
//                        ((StockQuotesActivity) getActivity()).setLayoutHeights(height);
//                    }
//                }
            }
        } else {
            // 不可见时不执行操作
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public int setContentLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_stock_order_byindex;
    }


  /*  IStockQuoteScrollListener mStockQuoteScrollListener;

    public void setStockQuoteScrollListener(IStockQuoteScrollListener scrollListener) {
        this.mStockQuoteScrollListener = scrollListener;
    }
*/

}
