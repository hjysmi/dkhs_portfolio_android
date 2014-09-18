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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.QuetosStockEngineImple;
import com.dkhs.portfolio.engine.SearchStockEngineImpl;
import com.dkhs.portfolio.engine.OptionalStockEngineImpl;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund.ISelectChangeListener;
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
    private static final String ARGUMENT_LOAD_TYPE = "load_type";

    private ListView mListView;
    private BaseAdatperSelectStockFund mAdapterConbinStock;
    // private BaseSelectActivity mActivity;

    private List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();

    private boolean isLoadingMore;
    private View mFootView;
    private boolean isFund;
    private boolean isSearch;

    private int mViewType;

    LoadSelectDataEngine mLoadDataEngine;

    /**
     * view视图类型
     */
    public enum ViewType {

        // 搜索类型
        SEARCH_STOCK(-1),
        // 搜索类型
        SEARCH_FUND(-2),
        // 股票，自选股
        STOCK_OPTIONAL(1),
        // 股票，涨幅
        STOCK_INCREASE(2),
        // 股票，跌幅
        STOCK_DRAWDOWN(3),
        // 股票，跌幅
        STOCK_HANDOVER(4),

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

    public enum OrderType {
        // 排序模式，日排行
        DAY(1),
        // 排序模式，月排行
        MONTH(2),
        // 排序模式，季度排行
        QUARTER(3);

        private int typeId;

        OrderType(int type) {
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

    public void searchByKey(String key) {
        mDataList.clear();
        // testSearchKey(key);
        if (mViewType == ViewType.SEARCH_STOCK.typeId) {

            new SearchStockEngineImpl(mSelectStockBackListener).searchStock(key);
        }
        mAdapterConbinStock.notifyDataSetChanged();
    }

    private void testSearchKey(String key) {

        // for (int i = 0; i < 20; i++) {
        // ConStockBean csBean = new ConStockBean();
        // if (isFund) {
        //
        // csBean.setName("基金" + key + i);
        // } else {
        // csBean.setName("股票" + key + i);
        //
        // }
        // csBean.setStockId(i+101000001);
        // csBean.setStockCode("" + (600000 + i));
        // csBean.setCurrentValue(20.00f + i);
        // mDataList.add(csBean);
        // }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (null != bundle) {
            isFund = bundle.getBoolean(ARGUMENT_LOAD_FUND);
            mViewType = bundle.getInt(ARGUMENT_LOAD_TYPE);
        }
        if (isFund) {
            mAdapterConbinStock = new SelectCompareFundAdatper(getActivity(), mDataList);
        } else {
            mAdapterConbinStock = new SelectStockAdatper(getActivity(), mDataList);
        }
        mAdapterConbinStock.setCheckChangeListener(this);
        if (mViewType != ViewType.SEARCH_STOCK.getTypeId() || mViewType != ViewType.SEARCH_FUND.getTypeId()) {
            initData();
        }

    }

    private void initData() {
        if (isFund) {
            loadDataByFund();
        } else {
            loadDataByStock();
        }

    }

    private void loadDataByFund() {
        for (int i = 0; i < 20; i++) {
            SelectStockBean csBean = new SelectStockBean();
            csBean.name = "基金名称" + i;
            csBean.id = i + 100;
            csBean.currentValue = 9.15f + i;
            mDataList.add(csBean);
        }
    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList) {
                System.out.println("Update view:" + dataList.size());
                mDataList.addAll(dataList);
                mAdapterConbinStock.notifyDataSetChanged();
                loadFinishUpdateView();
            }

        }

    };

    private void loadDataByStock() {

        if (mViewType == ViewType.STOCK_OPTIONAL.typeId) {
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
                        if (mViewType != ViewType.SEARCH_STOCK.typeId || mViewType != ViewType.SEARCH_FUND.getTypeId()) {
                            {
                                // 判断是否滚动到底部
                                if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                                    System.out.println("Loading more");
                                    mListView.addFooterView(mFootView);
                                    // Thread thread = new Thread(null, loadMoreListItems);
                                    // thread.start();
                                    loadMore();
                                }
                            }
                        }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void loadMore() {

        if (null != mLoadDataEngine) {
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
}
