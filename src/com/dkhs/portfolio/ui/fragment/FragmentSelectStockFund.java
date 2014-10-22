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
import android.widget.ListView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.FundDataEngine.OrderType;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.MainIndexEngineImple;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.engine.QuetosStockEngineImple;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.AddStockItemAdapter;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund.ISelectChangeListener;
import com.dkhs.portfolio.ui.adapter.OptionalPriceAdapter;
import com.dkhs.portfolio.ui.adapter.SelectCompareFundAdatper;
import com.dkhs.portfolio.ui.adapter.SelectStockAdatper;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName FragmentSelectStock
 * @Description 个股选择
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version 1.0
 */
public class FragmentSelectStockFund extends Fragment implements ISelectChangeListener, OnClickListener {
    private static final String TAG = FragmentSelectStockFund.class.getSimpleName();

    private static final String ARGUMENT_LOAD_FUND = "isloadfund";
    private static final String ARGUMENT_ITEM_CLICK_BACK = "argument_item_click_back";
    private static final String ARGUMENT_LOAD_TYPE = "load_type";

    private ListView mListView;
    private BaseAdatperSelectStockFund mAdapterConbinStock;
    // private BaseSelectActivity mActivity;

    private List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();

    private boolean isLoadingMore;
    private View mFootView;
    private boolean isFund;

    private boolean isItemClickBack;

    private int mViewType;
    private boolean fromPosition =false;
    LoadSelectDataEngine mLoadDataEngine;

    /**
     * view视图类型
     */
    public enum ViewType {

        // // 搜索类型
        // SEARCH_STOCK(-1),
        // // 搜索类型
        // SEARCH_FUND(-2),
        // 股票，自选股
        STOCK_OPTIONAL(1),
        // 股票，涨幅
        STOCK_INCREASE(2),
        // 股票，跌幅
        STOCK_DRAWDOWN(3),
        // 股票，跌幅
        STOCK_HANDOVER(4),
        // 股票，自选股
        STOCK_OPTIONAL_PRICE(8),

        // 基金，主要指数
        FUND_MAININDEX(5),
        // 基金，指数
        FUND_INDEX(6),
        // 基金，基金股票
        FUND_STOCK(7);

        private int typeId;

        ViewType(int type) {
            this.typeId = type;
        }

        public int getTypeId() {
            return typeId;
        }
    }

    public static FragmentSelectStockFund getStockFragment(ViewType type) {
        FragmentSelectStockFund fragment = new FragmentSelectStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putInt(ARGUMENT_LOAD_TYPE, type.getTypeId());
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSelectStockFund getFundFragment(ViewType type) {
        FragmentSelectStockFund fragment = new FragmentSelectStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, true);
        args.putInt(ARGUMENT_LOAD_TYPE, type.getTypeId());
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSelectStockFund getItemClickBackFragment(ViewType type) {
        FragmentSelectStockFund fragment = new FragmentSelectStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putBoolean(ARGUMENT_ITEM_CLICK_BACK, true);
        args.putInt(ARGUMENT_LOAD_TYPE, type.getTypeId());
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
            mViewType = bundle.getInt(ARGUMENT_LOAD_TYPE);
            fromPosition = bundle.getBoolean("fromPosition");
        }
        if (isItemClickBack) {
            mAdapterConbinStock = new AddStockItemAdapter(getActivity(), mDataList);
        } else if (isFund) {
            mAdapterConbinStock = new SelectCompareFundAdatper(getActivity(), mDataList);
        } else if (mViewType == ViewType.STOCK_OPTIONAL_PRICE.typeId) {
            mAdapterConbinStock = new OptionalPriceAdapter(getActivity(), mDataList);
        } else {
            mAdapterConbinStock = new SelectStockAdatper(getActivity(), mDataList);
            mAdapterConbinStock.setFromShow(!fromPosition);
        }

        mAdapterConbinStock.setCheckChangeListener(this);
        // if (mViewType != ViewType.SEARCH_STOCK.getTypeId() || mViewType != ViewType.SEARCH_FUND.getTypeId()) {
        initData();
        // }

    }

    private void initData() {
        if (isFund) {
            loadDataByFund();
        } else {
            loadDataByStock();
        }

    }

    private void loadDataByFund() {
        System.out.println("loadDataByFund view type:" + mViewType);
        if (mViewType == ViewType.FUND_MAININDEX.typeId) {
            mLoadDataEngine = new MainIndexEngineImple(mSelectStockBackListener);

        } else if (mViewType == ViewType.FUND_INDEX.typeId) {
            mLoadDataEngine = new FundDataEngine(mSelectStockBackListener, FundDataEngine.TYPE_INDEX);
        } else if (mViewType == ViewType.FUND_STOCK.typeId) {
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
            if (null != dataList) {
                mDataList.addAll(dataList);
                mAdapterConbinStock.notifyDataSetChanged();
                loadFinishUpdateView();
            }

        }

    };

    public void setOrderType(OrderType orderType) {
        if (mLoadDataEngine instanceof FundDataEngine) {
            ((FundDataEngine) mLoadDataEngine).setOrderType(orderType);
            mDataList.clear();
            mLoadDataEngine.loadData();
        }
        if (mLoadDataEngine instanceof MainIndexEngineImple) {
            ((MainIndexEngineImple) mLoadDataEngine).setOrderType(orderType);
            mDataList.clear();
            mLoadDataEngine.loadData();
        }
    }

    private void loadDataByStock() {

        if (mViewType == ViewType.STOCK_OPTIONAL.typeId || mViewType == ViewType.STOCK_OPTIONAL_PRICE.typeId) {
            mLoadDataEngine = new OptionalStockEngineImpl(mSelectStockBackListener);
        } else if (mViewType == ViewType.STOCK_HANDOVER.typeId) {
            mLoadDataEngine = new QuetosStockEngineImple(mSelectStockBackListener,
                    QuetosStockEngineImple.ORDER_TURNOVER);
        } else if (mViewType == ViewType.STOCK_DRAWDOWN.typeId) {
            mLoadDataEngine = new QuetosStockEngineImple(mSelectStockBackListener, QuetosStockEngineImple.ORDER_DOWN);

        } else {
            mLoadDataEngine = new QuetosStockEngineImple(mSelectStockBackListener,
                    QuetosStockEngineImple.ORDER_INCREASE);
        }
        mLoadDataEngine.loadData();

        // for (int i = 0; i < 20; i++) {
        // SelectStockBean csBean = new SelectStockBean();
        // csBean.name = "个股名" + i;
        // csBean.id = i + 100;
        // csBean.currentValue = 9.15f + i;
        // mDataList.add(csBean);
        // }
    }

    public void refresh() {
        if (mLoadDataEngine != null) {
            mDataList.clear();
            mLoadDataEngine.loadData();
        }
    }

    public void setCheckListener(ISelectChangeListener listener) {
        if (null != mAdapterConbinStock)
            mAdapterConbinStock.setCheckChangeListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View view = View.inflate(getActivity(), R.layout.fragment_selectstock, true);
        // initView(view);

        LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        inflater.inflate(R.layout.fragment_selectstock, wrapper, true);
        initView(wrapper);
        return wrapper;
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

    private void initView(View view) {
        mFootView = View.inflate(getActivity(), R.layout.layout_loading_more_footer, null);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        mListView.addFooterView(mFootView);
        mListView.setAdapter(mAdapterConbinStock);

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

            }
        });

        if (mViewType == ViewType.STOCK_OPTIONAL_PRICE.typeId) {
            mListView.setOnItemClickListener(priceStockItemClick);
        } else if (isItemClickBack) {
            mListView.setOnItemClickListener(itemBackClick);
            System.out.println("     mListView.setOnItemClickListener(itemBackClick);");
        }

    }

    OnItemClickListener priceStockItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SelectStockBean itemStock = mDataList.get(position);
            itemStock.isFollowed = true;
            // Toast.makeText(getActivity(), "选择股票：" + itemStock.name, Toast.LENGTH_SHORT).show();
            getActivity().startActivity(StockQuotesActivity.newIntent(getActivity(), itemStock));
        }
    };
    OnItemClickListener itemBackClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SelectStockBean itemStock = mDataList.get(position);
            setSelectBack(itemStock);
            System.out.println("OnItemClickListener itemBackClick ");
        }
    };

    private void loadMore() {
        if (null != mLoadDataEngine) {
            if (mLoadDataEngine.getCurrentpage() >= mLoadDataEngine.getTotalpage()) {
                Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
                return;
            }
            mListView.addFooterView(mFootView);
            // Thread thread = new Thread(null, loadMoreListItems);
            // thread.start();

            isLoadingMore = true;
            mLoadDataEngine.loadMore();
        }
    }

    // Runnable to load the items
    // private List<SelectStockBean> loadList;
    // private Runnable loadMoreListItems = new Runnable() {
    // @Override
    // public void run() {
    //
    // loadList = new ArrayList<SelectStockBean>();
    // try {
    // Thread.sleep(1000);
    // } catch (InterruptedException e) {
    // }
    // for (int i = 0; i < 20; i++) {
    // SelectStockBean csBean = new SelectStockBean();
    // csBean.name = "加载项" + i;
    // csBean.id = i + 100;
    // csBean.currentValue = 9.15f + i;
    // loadList.add(csBean);
    // }
    // getActivity().runOnUiThread(returnRes);
    // }
    // };
    //
    // private Runnable returnRes = new Runnable() {
    // @Override
    // public void run() {
    // if (loadList != null && loadList.size() > 0) {
    // mDataList.addAll(loadList);
    // }
    // loadFinishUpdateView();
    // }
    // };

    private void loadFinishUpdateView() {
        mAdapterConbinStock.notifyDataSetChanged();
        isLoadingMore = false;
        if (mListView != null) {
            mListView.removeFooterView(mFootView);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        BaseSelectActivity mActivity = (BaseSelectActivity) getActivity();
        mActivity.notifySelectDataChange(false);
    }

    @Override
    public void onClick(View v) {

    }

    public static final String ARGUMENT = "ARGUMENT";

    private void setSelectBack(SelectStockBean type) {
        Intent intent = new Intent();
        intent.putExtra(ARGUMENT, type);
        getActivity().setResult(-1, intent);

        getActivity().finish();
    }

}
