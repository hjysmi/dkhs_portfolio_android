/**
 * @Title FragmentSelectStock.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundDataEngine;
import com.dkhs.portfolio.engine.FundDataEngine.OrderType;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.MainIndexEngineImple;
import com.dkhs.portfolio.engine.MarketCenterStockEngineImple;
import com.dkhs.portfolio.engine.OpitionCenterStockEngineImple;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.engine.QuetosStockEngineImple;
import com.dkhs.portfolio.net.ErrorBundle;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.HistoryPositionDetailActivity;
import com.dkhs.portfolio.ui.MarketCenterActivity.ILoadingFinishListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.AddStockItemAdapter;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund.ISelectChangeListener;
import com.dkhs.portfolio.ui.adapter.MarketCenterItemAdapter;
import com.dkhs.portfolio.ui.adapter.OptionalPriceAdapter;
import com.dkhs.portfolio.ui.adapter.SelectCompareFundAdatper;
import com.dkhs.portfolio.ui.adapter.SelectStockAdatper;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnLoadMoreListener;
import com.dkhs.portfolio.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName FragmentSelectStock
 * @Description 个股选择
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version 1.0
 */
public class FragmentSelectStockFund extends BaseFragment implements ISelectChangeListener, OnClickListener,
        OnLoadMoreListener {
    private static final String TAG = FragmentSelectStockFund.class.getSimpleName();

    protected static final String ARGUMENT_LOAD_FUND = "isloadfund";
    protected static final String ARGUMENT_ITEM_CLICK_BACK = "argument_item_click_back";
    protected static final String ARGUMENT_LOAD_TYPE = "load_type";
    protected static final String ARGUMENT_SECTOR_ID = "sector_id";

    protected PullToRefreshListView mListView;
    protected BaseAdatperSelectStockFund mAdapterConbinStock;
    protected boolean isLoadingMore;
    protected boolean isRefresh;
    protected List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();
    protected boolean isFund;
    protected boolean isItemClickBack;
    protected StockViewType mViewType;
    protected boolean fromPosition = false;
    LoadSelectDataEngine mLoadDataEngine;
    protected TextView tvEmptyText;
    public int timeMill;
    protected boolean flush = false;
    protected String mSecotrId;
    protected boolean isLoading;
    private RelativeLayout pb;

    /**
     * view视图类型
     */
    public enum StockViewType {

        /** 股票，自选股 */
        STOCK_OPTIONAL(1),
        /** 股票，涨幅 */
        STOCK_INCREASE(2),
        /** 股票，跌幅 */
        STOCK_DRAWDOWN(3),
        /** 股票，换手率 */
        STOCK_HANDOVER(4),

        /** 基金，主要指数 */
        FUND_MAININDEX(5),
        /** 基金，指数 */
        FUND_INDEX(6),
        /** 基金，基金股票 */
        FUND_STOCK(7),
        /** 股票，自选股 */
        STOCK_OPTIONAL_PRICE(8),

        /** 行情中心，国内指数排行，高到低 */
        MARKET_INLAND_INDEX(9),
        /** 行情中心，国内指数排行榜查询，低到高 */
        MARKET_INLAND_INDEX_ACE(10),
        /** 行情中心, 国内指数不排序 */
        MARKET_INLAND_INDEX_CURRENT(11),

        /** 行情中心,个股排行，跌幅 */
        MARKET_STOCK_DOWNRATIO(12),
        /** 行情中心,个股排行，涨幅 */
        MARKET_STOCK_UPRATIO(13),
        /** 行情中心,个股换手排行榜查询 高到低 */
        MARKET_STOCK_TURNOVER(14),
        /** 行情中心,个股换手排行榜查询 低到高 */
        MARKET_STOCK_TURNOVER_ACE(15),
        /** 行情中心 ,个股振幅，高到低 */
        MARKET_STOCK_AMPLIT(16),
        /** 行情中心 ,个股振幅，低到高 */
        MARKET_STOCK_AMPLIT_ACE(17),
        /** 行情中心 ,板块列表，高到底 */
        MARKET_PLATE_LIST(18),
        /** 行情中心 ,个股振幅，低到高 */
        MARKET_PLATE_LIST_ACE(19);

        private int typeId;

        StockViewType(int type) {
            this.typeId = type;
        }

        public int getTypeId() {
            return typeId;
        }
    }

    public static FragmentSelectStockFund getStockFragment(StockViewType type) {
        FragmentSelectStockFund fragment = new FragmentSelectStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putSerializable(ARGUMENT_LOAD_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSelectStockFund getStockFragmentByPlate(StockViewType type, String plateId) {
        FragmentSelectStockFund fragment = new FragmentSelectStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putSerializable(ARGUMENT_LOAD_TYPE, type);
        args.putString(ARGUMENT_SECTOR_ID, plateId);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSelectStockFund getFundFragment(StockViewType type) {
        FragmentSelectStockFund fragment = new FragmentSelectStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, true);
        args.putSerializable(ARGUMENT_LOAD_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSelectStockFund getItemClickBackFragment(StockViewType type) {
        FragmentSelectStockFund fragment = new FragmentSelectStockFund();
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
        if (isItemClickBack) {
            if (mViewType == StockViewType.STOCK_HANDOVER) {

                mAdapterConbinStock = new AddStockItemAdapter(getActivity(), mDataList, true);
            } else {
                mAdapterConbinStock = new AddStockItemAdapter(getActivity(), mDataList);
            }
        } else if (isFund) {
            mAdapterConbinStock = new SelectCompareFundAdatper(getActivity(), mDataList);
        } else if (mViewType == StockViewType.STOCK_OPTIONAL_PRICE) {
            mAdapterConbinStock = new OptionalPriceAdapter(getActivity(), mDataList);
        } else if (mViewType == StockViewType.MARKET_STOCK_DOWNRATIO || mViewType == StockViewType.MARKET_STOCK_UPRATIO
                || mViewType == StockViewType.MARKET_INLAND_INDEX
                || mViewType == StockViewType.MARKET_INLAND_INDEX_CURRENT
                || mViewType == StockViewType.MARKET_INLAND_INDEX_ACE || mViewType == StockViewType.MARKET_PLATE_LIST
                || mViewType == StockViewType.MARKET_PLATE_LIST_ACE) {
            mAdapterConbinStock = new MarketCenterItemAdapter(getActivity(), mDataList);
        } else if (mViewType == StockViewType.MARKET_STOCK_AMPLIT || mViewType == StockViewType.MARKET_STOCK_AMPLIT_ACE
                || mViewType == StockViewType.MARKET_STOCK_TURNOVER
                || mViewType == StockViewType.MARKET_STOCK_TURNOVER_ACE) {
            mAdapterConbinStock = new MarketCenterItemAdapter(getActivity(), mDataList, true);
        } else if (mViewType == StockViewType.STOCK_HANDOVER) {
            mAdapterConbinStock = new SelectStockAdatper(getActivity(), mDataList, true);
            mAdapterConbinStock.setFromShow(!fromPosition);

        } else {
            mAdapterConbinStock = new SelectStockAdatper(getActivity(), mDataList);
            mAdapterConbinStock.setFromShow(!fromPosition);
        }

        mAdapterConbinStock.setCheckChangeListener(this);
        initData();

    }

    private boolean isLoadStockType() {
        if (mViewType == StockViewType.MARKET_STOCK_DOWNRATIO || mViewType == StockViewType.MARKET_STOCK_UPRATIO
                || mViewType == StockViewType.MARKET_STOCK_AMPLIT || mViewType == StockViewType.MARKET_STOCK_AMPLIT_ACE
                || mViewType == StockViewType.MARKET_STOCK_TURNOVER
                || mViewType == StockViewType.MARKET_STOCK_TURNOVER_ACE || mViewType == StockViewType.MARKET_PLATE_LIST
                || mViewType == StockViewType.MARKET_PLATE_LIST_ACE) {
            return true;
        }
        return false;
    }

    private void initData() {
        if (isFund) {
            loadDataByFund();
        } else {
            loadDataByStock();
        }

    }

    private void loadDataByFund() {
        if (mViewType == StockViewType.FUND_MAININDEX) {
            mLoadDataEngine = new MainIndexEngineImple(mSelectStockBackListener);

        } else if (mViewType == StockViewType.MARKET_INLAND_INDEX) {

        } else if (mViewType == StockViewType.FUND_INDEX) {
            mLoadDataEngine = new FundDataEngine(mSelectStockBackListener, FundDataEngine.TYPE_INDEX);
        } else if (mViewType == StockViewType.FUND_STOCK) {
            mLoadDataEngine = new FundDataEngine(mSelectStockBackListener, FundDataEngine.TYPE_STOCK);

        }
        if (null != mLoadDataEngine) {
            mLoadDataEngine.loadData();

        } else {
            LogUtils.d("LoadDataEngine is null");
        }

    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            mListView.onLoadMoreComplete();
            mSwipeLayout.setRefreshing(false);
            pb.setVisibility(View.GONE);
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
                    mListView.setOnLoadListener(FragmentSelectStockFund.this);
            }
            if (flush) {
                // Toast.makeText(getActivity(), "没有更多的数据了",
                // Toast.LENGTH_SHORT).show();
                flush = false;
                // loadFinishUpdateView();
                return;
            }
            if (isRefresh || mViewType == StockViewType.STOCK_OPTIONAL_PRICE) {
                mDataList.clear();
                isRefresh = false;

            }
            // loadFinishUpdateView();
            if (null != dataList && dataList.size() > 0 && isAdded()) {
                mDataList.addAll(dataList);
                mAdapterConbinStock.notifyDataSetChanged();

            }
            if (null == mDataList || mDataList.size() == 0) {
                initNotice();
            } else {
                hideNotice();
            }

        }

        @Override
        public void loadFail(ErrorBundle error) {
            LogUtils.e("loading fail,error code:" + error.getErrorCode());
            if (null == mDataList || mDataList.size() == 0) {
                initNotice();
            } else {
                hideNotice();
            }
            if (null != loadingFinishListener) {
                loadingFinishListener.loadingFinish();
            }
        }

    };

    public void setOrderType(OrderType orderType) {
        isRefresh = true;
        if (mLoadDataEngine instanceof FundDataEngine) {
            ((FundDataEngine) mLoadDataEngine).setOrderType(orderType);
            // mDataList.clear();

            mLoadDataEngine.setLoadingDialog(getActivity());
            mLoadDataEngine.loadData();
        }
        if (mLoadDataEngine instanceof MainIndexEngineImple) {
            ((MainIndexEngineImple) mLoadDataEngine).setOrderType(orderType);
            // mDataList.clear();

            mLoadDataEngine.setLoadingDialog(getActivity());
            mLoadDataEngine.loadData();
        }
    }

    public void setOptionalOrderType(String type) {
        isRefresh = true;
        if (mLoadDataEngine instanceof OptionalStockEngineImpl) {
            ((OptionalStockEngineImpl) mLoadDataEngine).setLoadType(type);
            // mDataList.clear();
            mLoadDataEngine.setLoadingDialog(getActivity());
            mLoadDataEngine.loadData();

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
        mLoadDataEngine.loadData();

    }

    public void refresh() {
        isRefresh = true;
        if (mLoadDataEngine != null && UIUtils.roundAble(mLoadDataEngine.getStatu()) && !isLoadingMore) {
            // mDataList.clear();
            isLoading = true;
            if (null != loadingFinishListener) {
                loadingFinishListener.startLoadingData();
            }
            mLoadDataEngine.loadData();
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
            mLoadDataEngine.cancelLoadingDialog();
            // mLoadDataEngine.refreshDatabySize(mDataList.size());
            mLoadDataEngine.loadData();
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
                mLoadDataEngine.refreshDatabySize(mDataList.size());
            }
        } else {
            timeMill++;
            if (timeMill > 5) {
                timeMill = 0;
                isLoadingMore = false;
            }
        }
    }

    public void setCheckListener(ISelectChangeListener listener) {
        if (null != mAdapterConbinStock)
            mAdapterConbinStock.setCheckChangeListener(listener);
    }

    public SwipeRefreshLayout mSwipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        inflater.inflate(R.layout.fragment_selectstock, wrapper, true);
        tvEmptyText = (TextView) wrapper.findViewById(android.R.id.empty);
        pb = (RelativeLayout) wrapper.findViewById(android.R.id.progress);
        if (!(null != mDataList && mDataList.size() > 0)) {
            pb.setVisibility(View.VISIBLE);
        }
        initView(wrapper);
        return wrapper;
    }

    private void hideNotice() {
        if (null == tvEmptyText) {
            return;
        }
        tvEmptyText.setVisibility(View.GONE);
    }

    protected void initNotice() {
        if (null == tvEmptyText) {
            return;
        }
        tvEmptyText.setVisibility(View.VISIBLE);
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
                tvEmptyText.setText(R.string.nodate_tip);
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
        LogUtils.d("===========FragmentSelectCombinStock onStart(=============");
    }

    public void initView(View view) {

        mListView = (PullToRefreshListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapterConbinStock);

        if (mViewType == StockViewType.STOCK_OPTIONAL_PRICE) {
            mListView.setOnItemClickListener(priceStockItemClick);
        } else if (isItemClickBack) {
            mListView.setOnItemClickListener(itemBackClick);
        }

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        // mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refreshNoCaseTime();

            }
        });

    }

    OnItemClickListener priceStockItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SelectStockBean itemStock = mDataList.get(position);
            itemStock.isFollowed = true;
            // Toast.makeText(getActivity(), "选择股票：" + itemStock.name,
            // Toast.LENGTH_SHORT).show();
            getActivity().startActivity(StockQuotesActivity.newIntent(getActivity(), itemStock));
        }
    };
    OnItemClickListener itemBackClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            SelectStockBean itemStock = mDataList.get(position);
            getActivity().startActivity(StockQuotesActivity.newIntent(getActivity(), itemStock));
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

    public static final String ARGUMENT = "ARGUMENT";

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_stock_select);

    @Override
    public void onPause() {
        super.onPause();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageEnd(mPageName);
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (!isVisibleToUser) {
            if (null != pb)
                pb.setVisibility(View.GONE);
        }
        if (null != mDataList && mDataList.size() > 0) {
            pb.setVisibility(View.GONE);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onLoadMore() {
        if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                flush = true;
                /*
                 * PromptManager.showProgressDialog(getActivity(), "", true);
                 * 
                 * PromptManager.closeProgressDialog(); return;
                 */
            }

            if (UIUtils.roundAble(mLoadDataEngine.getStatu()))
                mLoadDataEngine.setCurrentpage((mDataList.size() + 49) / 50);
            mLoadDataEngine.loadMore();
            if (null != loadingFinishListener) {
                loadingFinishListener.startLoadingData();
            }

        }
    }

    protected ILoadingFinishListener loadingFinishListener;

    public void setLoadingFinishListener(ILoadingFinishListener finishListener) {
        this.loadingFinishListener = finishListener;
    }

}
