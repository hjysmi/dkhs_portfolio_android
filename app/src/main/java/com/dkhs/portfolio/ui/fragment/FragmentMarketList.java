/**
 * @Title FragmentSelectStock.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundDataEngine.OrderType;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.MainIndexEngineImple;
import com.dkhs.portfolio.engine.MarketCenterStockEngineImple;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.engine.QuetosStockEngineImple;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.MarketListActivity.ILoadingFinishListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund.ISelectChangeListener;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.ui.widget.PullToRefreshPageListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshPageListView.OnLoadMoreListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshPageListView.OnRefreshListener;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FragmentSelectStock
 * @Description 个股选择
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version 1.0
 */
public class FragmentMarketList extends BaseFragment implements ISelectChangeListener, OnClickListener,
        OnLoadMoreListener {
    private static final String TAG = FragmentMarketList.class.getSimpleName();

    protected static final String ARGUMENT_LOAD_FUND = "isloadfund";
    protected static final String ARGUMENT_ITEM_CLICK_BACK = "argument_item_click_back";
    protected static final String ARGUMENT_LOAD_TYPE = "load_type";
    protected static final String ARGUMENT_SECTOR_ID = "sector_id";

    protected PullToRefreshPageListView mListView;
    protected BaseAdatperSelectStockFund mAdapterConbinStock;
    protected boolean isLoadingMore;
    protected boolean isRefresh;
    protected List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();
    protected boolean isFund;
    protected boolean isItemClickBack;
    protected StockViewType mViewType;
    protected boolean fromPosition = false;
    LoadMoreDataEngine mLoadDataEngine;
    protected TextView tvEmptyText;
    public int timeMill;
    protected boolean flush = false;
    protected String mSecotrId;
    protected boolean isLoading;
    private HttpHandler mHttpHandler;
    private View mProgressView;

    public static FragmentMarketList getStockFragment(StockViewType type) {
        FragmentMarketList fragment = new FragmentMarketList();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putSerializable(ARGUMENT_LOAD_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentMarketList getStockFragmentByPlate(StockViewType type, String plateId) {
        FragmentMarketList fragment = new FragmentMarketList();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putSerializable(ARGUMENT_LOAD_TYPE, type);
        args.putString(ARGUMENT_SECTOR_ID, plateId);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentMarketList getFundFragment(StockViewType type) {
        FragmentMarketList fragment = new FragmentMarketList();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, true);
        args.putSerializable(ARGUMENT_LOAD_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentMarketList getItemClickBackFragment(StockViewType type) {
        FragmentMarketList fragment = new FragmentMarketList();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putBoolean(ARGUMENT_ITEM_CLICK_BACK, true);
        args.putSerializable(ARGUMENT_LOAD_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (null != bundle) {
            isFund = bundle.getBoolean(ARGUMENT_LOAD_FUND);
            isItemClickBack = bundle.getBoolean(ARGUMENT_ITEM_CLICK_BACK);
            mViewType = (StockViewType) bundle.getSerializable(ARGUMENT_LOAD_TYPE);
            fromPosition = bundle.getBoolean("fromPosition");
            mSecotrId = bundle.getString(ARGUMENT_SECTOR_ID);

        }

        setListViewAdapter();
//        initData();

    }

    private void setListViewAdapter() {
        if (mViewType == StockViewType.MARKET_STOCK_DOWNRATIO || mViewType == StockViewType.MARKET_STOCK_UPRATIO
                || mViewType == StockViewType.MARKET_INLAND_INDEX
                || mViewType == StockViewType.MARKET_INLAND_INDEX_CURRENT
                || mViewType == StockViewType.MARKET_INLAND_INDEX_ACE || mViewType == StockViewType.MARKET_PLATE_LIST
                || mViewType == StockViewType.MARKET_PLATE_LIST_ACE) {
            mAdapterConbinStock = new MarketCenterItemAdapter(getActivity(), mDataList);
        } else if (mViewType == StockViewType.MARKET_STOCK_AMPLIT || mViewType == StockViewType.MARKET_STOCK_AMPLIT_ACE
                || mViewType == StockViewType.MARKET_STOCK_TURNOVER
                || mViewType == StockViewType.MARKET_STOCK_TURNOVER_ACE) {
            mAdapterConbinStock = new MarketCenterItemAdapter(getActivity(), mDataList, true);
        }
    }

    private boolean isLoadStockType() {
        return mViewType == StockViewType.MARKET_STOCK_DOWNRATIO || mViewType == StockViewType.MARKET_STOCK_UPRATIO
                || mViewType == StockViewType.MARKET_STOCK_AMPLIT || mViewType == StockViewType.MARKET_STOCK_AMPLIT_ACE
                || mViewType == StockViewType.MARKET_STOCK_TURNOVER
                || mViewType == StockViewType.MARKET_STOCK_TURNOVER_ACE || mViewType == StockViewType.MARKET_PLATE_LIST
                || mViewType == StockViewType.MARKET_PLATE_LIST_ACE;
    }

    private void initData() {
        showProgress();
        loadDataByStock();

    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener<SelectStockBean>() {

        @Override
        public void loadFinish(MoreDataBean<SelectStockBean> object) {
            dissProgress();
            object.getResults();
            mListView.onLoadMoreComplete();
            mListView.onRefreshComplete();
            // mLoadDataEngine.getTotalpage()
            mListView.setCurrentPage(mLoadDataEngine.getCurrentpage());
            mListView.setTotalPage(mLoadDataEngine.getTotalpage());
            if (null != loadingFinishListener) {
                loadingFinishListener.loadingFinish();
            }
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                mListView.setCanLoadMore(false);
                mListView.setAutoLoadMore(false);
            } else {
                mListView.setCanLoadMore(true);
                mListView.setAutoLoadMore(true);
                if (mLoadDataEngine.getCurrentpage() == 1)
                    mListView.setOnLoadListener(FragmentMarketList.this);
            }
            if (flush) {
                // Toast.makeText(getActivity(), "没有更多的数据了",
                // Toast.LENGTH_SHORT).show();
                flush = false;
                // loadFinishUpdateView();
                return;
            }

            // loadFinishUpdateView();

            if (null != object && null != object.getResults() && object.getResults().size() > 0 && isAdded()) {
                mDataList.clear();
                mDataList.addAll(object.getResults());
                mAdapterConbinStock.notifyDataSetChanged();
                // mDataList = dataList;

            }
            if (isRefresh) {
                // mDataList.clear();
                isRefresh = false;
            }
            if (isLoadingMore) {

                mListView.setSelection(1);
            }
            if (null == mDataList || mDataList.size() == 0) {
                initNotice();
            }
            isLoadingMore = false;

        }

        @Override
        public void loadFail() {
            isLoadingMore = false;
            dissProgress();
            if (null == mDataList || mDataList.size() == 0) {
                initNotice();
            }
            if (null != loadingFinishListener) {
                loadingFinishListener.loadingFinish();
            }
        }
    };

    // ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {
    //
    // @Override
    // public void loadFinish(List<SelectStockBean> dataList) {
    // mListView.onLoadMoreComplete();
    // mListView.onRefreshComplete();
    // // mLoadDataEngine.getTotalpage()
    // mListView.setCurrentPage(mLoadDataEngine.getCurrentpage());
    // mListView.setTotalPage(mLoadDataEngine.getTotalpage());
    // if (null != loadingFinishListener) {
    // loadingFinishListener.loadingFinish();
    // }
    // if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
    // mListView.setCanLoadMore(false);
    // mListView.setAutoLoadMore(false);
    // } else {
    // mListView.setCanLoadMore(true);
    // mListView.setAutoLoadMore(true);
    // if (mLoadDataEngine.getCurrentpage() == 1)
    // mListView.setOnLoadListener(FragmentMarketList.this);
    // }
    // if (flush) {
    // // Toast.makeText(getActivity(), "没有更多的数据了",
    // // Toast.LENGTH_SHORT).show();
    // flush = false;
    // // loadFinishUpdateView();
    // return;
    // }
    //
    // // loadFinishUpdateView();
    // if (null != dataList && dataList.size() > 0 && isAdded()) {
    // mDataList.clear();
    // mDataList.addAll(dataList);
    // mAdapterConbinStock.notifyDataSetChanged();
    // // mDataList = dataList;
    //
    // }
    // if (isRefresh) {
    // // mDataList.clear();
    // isRefresh = false;
    // }
    // if (isLoadingMore) {
    //
    // mListView.setSelection(1);
    // }
    // if (null == mDataList || mDataList.size() == 0) {
    // initNotice();
    // }
    // isLoadingMore = false;
    //
    // }
    //
    // @Override
    // public void loadFail(ErrorBundle error) {
    // LogUtils.e("loading fail,error code:" + error.getErrorCode());
    // isLoadingMore = false;
    // if (null == mDataList || mDataList.size() == 0) {
    // initNotice();
    // }
    // if (null != loadingFinishListener) {
    // loadingFinishListener.loadingFinish();
    // }
    // }
    //
    // };

    public void setOrderType(OrderType orderType) {
        isRefresh = true;

        if (mLoadDataEngine instanceof MainIndexEngineImple) {
            ((MainIndexEngineImple) mLoadDataEngine).setOrderType(orderType);
            // mDataList.clear();

            mLoadDataEngine.setLoadingDialog(getActivity());
            cancelHttpHandler();
            mHttpHandler = mLoadDataEngine.loadData();
        }
    }

    public void setOptionalOrderType(String type) {
        isRefresh = true;
        if (mLoadDataEngine instanceof OptionalStockEngineImpl) {
            ((OptionalStockEngineImpl) mLoadDataEngine).setLoadType(type);
            // mDataList.clear();
            mLoadDataEngine.setLoadingDialog(getActivity());
            cancelHttpHandler();
            mHttpHandler = mLoadDataEngine.loadData();

        }
    }

    private void cancelHttpHandler() {
        if (mHttpHandler != null) {
            mHttpHandler.cancel();
        }
    }

    private void loadDataByStock() {
        if (mViewType == StockViewType.STOCK_OPTIONAL) {
            mLoadDataEngine = new OptionalStockEngineImpl(mSelectStockBackListener, false);
        } else if (mViewType == StockViewType.STOCK_OPTIONAL_PRICE) {
            mLoadDataEngine = new OptionalStockEngineImpl(mSelectStockBackListener, true);
        } else if (isLoadStockType() || mViewType == StockViewType.STOCK_HANDOVER
                || mViewType == StockViewType.STOCK_DRAWDOWN || mViewType == StockViewType.STOCK_INCREASE) {
            mLoadDataEngine = new OpitionCenterStockEngineImple(mSelectStockBackListener, mViewType, mSecotrId);
        } else if (mViewType == StockViewType.MARKET_INLAND_INDEX) {
            mLoadDataEngine = new MarketCenterStockEngineImple(mSelectStockBackListener,
                    MarketCenterStockEngineImple.DESC);
        } else if (mViewType == StockViewType.MARKET_INLAND_INDEX_CURRENT) {
            mLoadDataEngine = new MarketCenterStockEngineImple(mSelectStockBackListener,
                    MarketCenterStockEngineImple.CURRENT);
        } else if (mViewType == StockViewType.MARKET_INLAND_INDEX_ACE) {
            mLoadDataEngine = new MarketCenterStockEngineImple(mSelectStockBackListener,
                    MarketCenterStockEngineImple.ACE);
        } else {
            mLoadDataEngine = new QuetosStockEngineImple(mSelectStockBackListener,
                    QuetosStockEngineImple.ORDER_INCREASE);
        }
        if (null != loadingFinishListener) {
            loadingFinishListener.startLoadingData();
        }

        cancelHttpHandler();
        mHttpHandler = mLoadDataEngine.loadData();

    }

    public void refresh() {
        isRefresh = true;
        if (mLoadDataEngine != null && UIUtils.roundAble(mLoadDataEngine.getStatu()) && !isLoadingMore) {
            // mDataList.clear();
            isLoading = true;
            if (null != loadingFinishListener) {
                loadingFinishListener.startLoadingData();
            }
            cancelHttpHandler();
            mHttpHandler = mLoadDataEngine.loadData();
        }
    }

    public void refreshNoCaseTime() {
        isRefresh = true;
        if (mLoadDataEngine != null && !isLoadingMore) {
            // mDataList.clear();
            isLoading = true;
            if (null != loadingFinishListener) {
                loadingFinishListener.startLoadingData();
            }
            // cancelHttpHandler();
            // mHttpHandler = mLoadDataEngine.refreshDatabySize(mDataList.size());
            cancelHttpHandler();
            mHttpHandler = mLoadDataEngine.loadData();
        }
    }

    public void refreshForMarker() {

        if (mLoadDataEngine != null && !isLoadingMore) {
            // mDataList.clear();
            if (UIUtils.roundAble(mLoadDataEngine.getStatu())) {
                timeMill = 0;
                isRefresh = true;
                // if ((mViewType == StockViewType.MARKET_INLAND_INDEX_CURRENT
                // || mViewType == StockViewType.MARKET_INLAND_INDEX || mViewType ==
                // StockViewType.MARKET_INLAND_INDEX_ACE)
                // && null != mDataList) {
                // ((MarketCenterStockEngineImple) mLoadDataEngine).loadDataFromCurrent(mDataList.size());
                // }
                // if ((mViewType == StockViewType.MARKET_STOCK_UPRATIO || mViewType ==
                // StockViewType.MARKET_STOCK_DOWNRATIO)
                // && null != mDataList) {
                // ((OpitionCenterStockEngineImple) mLoadDataEngine).loadDataFromCurrent(mDataList.size());
                // }
                if (null != loadingFinishListener) {
                    loadingFinishListener.startLoadingData();
                }
                cancelHttpHandler();
                if (mLoadDataEngine instanceof OpitionCenterStockEngineImple) {
                    mHttpHandler = ((OpitionCenterStockEngineImple) mLoadDataEngine).loadByPage(mLoadDataEngine
                            .getCurrentpage());
                } else {
                    mHttpHandler = mLoadDataEngine.refreshDatabySize(mDataList.size());
                }
                // mLoadDataEngine.loadData();
            }
        } else {
            timeMill++;
            if (timeMill > 5) {
                timeMill = 0;
                isLoadingMore = false;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        wrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        inflater.inflate(R.layout.fragment_page_selectstock, wrapper, true);
        tvEmptyText = (TextView) wrapper.findViewById(android.R.id.empty);

        initView(wrapper);
        return wrapper;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    protected void initNotice() {
        if (null == tvEmptyText) {
            return;
        }
        switch (mViewType) {
            case STOCK_OPTIONAL:

            case STOCK_OPTIONAL_PRICE: {
                tvEmptyText.setText(R.string.nodate_tip_optional);
            }
                break;
            case FUND_INDEX:
            case FUND_MAININDEX:
            case FUND_STOCK: {
                tvEmptyText.setText(R.string.nodate_tip_funds);
            }
                break;

            case MARKET_INLAND_INDEX:
            case MARKET_INLAND_INDEX_CURRENT:
            case MARKET_INLAND_INDEX_ACE: {
                tvEmptyText.setText(R.string.nodate_tip_inland_index);
            }
                break;

            default: {
                if (null != tvEmptyText) {
                    tvEmptyText.setText(R.string.nodate_tip);
                }
            }
                break;
        }
    }

    public void refreshSelect() {

        if (null != mAdapterConbinStock) {
            mAdapterConbinStock.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG,"===========onStart(=============");
    }

    public void initView(View view) {
        mProgressView = view.findViewById(R.id.my_progressbar);
        mListView = (PullToRefreshPageListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapterConbinStock);
        mListView.setEmptyView(tvEmptyText);

        if (mViewType == StockViewType.STOCK_OPTIONAL_PRICE) {
            mListView.setOnItemClickListener(priceStockItemClick);
        } else if (isItemClickBack) {
            mListView.setOnItemClickListener(itemBackClick);
        }

        mListView.setCanRefresh(true);
        mListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (mLoadDataEngine.getCurrentpage() <= 1) {
                    mLoadDataEngine.setCurrentpage(0);
                } else {
                    mLoadDataEngine.setCurrentpage(mLoadDataEngine.getCurrentpage() - 2);
                }
                cancelHttpHandler();
                mHttpHandler = mLoadDataEngine.loadMore();
                // mLoadDataEngine.refreshDatabySize(dataSize)
                if (null != loadingFinishListener) {
                    loadingFinishListener.startLoadingData();
                }
            }
        });
        // mListView.setLoadPageView(true);

    }

    OnItemClickListener priceStockItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SelectStockBean itemStock = mDataList.get(position);
            itemStock.isFollowed = true;
            // Toast.makeText(getActivity(), "选择股票：" + itemStock.name,
            // Toast.LENGTH_SHORT).show();
            // UIUtils.startAnimationActivity(getActivity(), (StockQuotesActivity.newIntent(getActivity(), itemStock)));
            startActivity(StockQuotesActivity.newIntent(getActivity(), itemStock));
        }
    };
    OnItemClickListener itemBackClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            SelectStockBean itemStock = mDataList.get(position);
            startActivity(StockQuotesActivity.newIntent(getActivity(), itemStock));
            // UIUtils.startAnimationActivity(getActivity(), (StockQuotesActivity.newIntent(getActivity(), itemStock)));
            //
            getActivity().finish();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        BaseSelectActivity mActivity = (BaseSelectActivity) getActivity();
        mActivity.notifySelectDataChange(false);
    }

    @Override
    public void onClick(View v) {

    }


    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_select);



    @Override
    public void onLoadMore() {
        if (null != mLoadDataEngine) {

            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                flush = true;

            }

            // if (UIUtils.roundAble(mLoadDataEngine.getStatu())) {
            // mLoadDataEngine.setCurrentpage((mDataList.size() + 49) / 50);
            // }
            isLoadingMore = true;
            cancelHttpHandler();
            mHttpHandler = mLoadDataEngine.loadMore();
            if (null != loadingFinishListener) {
                loadingFinishListener.startLoadingData();
            }

        }
    }

    protected ILoadingFinishListener loadingFinishListener;

    public void setLoadingFinishListener(ILoadingFinishListener finishListener) {
        this.loadingFinishListener = finishListener;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public int setContentLayoutId() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void showProgress(){
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void dissProgress(){
        if(mProgressView.getVisibility() == View.VISIBLE){
            mProgressView.setVisibility(View.GONE);
        }
    }

}
