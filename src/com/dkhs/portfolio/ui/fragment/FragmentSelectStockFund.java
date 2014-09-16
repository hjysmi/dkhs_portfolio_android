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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.SelectCompareFundAdatper;
import com.dkhs.portfolio.ui.adapter.SelectFundAdapter;
import com.dkhs.portfolio.ui.adapter.SelectStockAdatper;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund.ISelectChangeListener;
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

    private List<ConStockBean> mDataList = new ArrayList<ConStockBean>();

    private boolean isLoadingMore;
    private View mFootView;
    private boolean isFund;
    private boolean isSearch;

    private int mViewType;

    /**
     * view视图类型
     */
    public enum ViewType {

        // 搜索类型
        SEARCH(-1),
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

    // public static FragmentSelectStockFund getInstance(boolean isFund, boolean isSearch) {
    // FragmentSelectStockFund fragment = new FragmentSelectStockFund();
    // Bundle args = new Bundle();
    // args.putBoolean(ARGUMENT_LOAD_FUND, isFund);
    // args.putBoolean("issearch", isSearch);
    // fragment.setArguments(args);
    // return fragment;
    // }

    public void searchByKey(String key) {
        testSearchKey(key);

    }

    private void testSearchKey(String key) {
        mDataList.clear();
        for (int i = 0; i < 20; i++) {
            ConStockBean csBean = new ConStockBean();
            if (isFund) {

                csBean.setName("基金" + key + i);
            } else {
                csBean.setName("股票" + key + i);

            }
            csBean.setStockId(i+101000001);
            csBean.setStockCode("" + (600000 + i));
            csBean.setCurrentValue(20.00f + i);
            mDataList.add(csBean);
        }
        mAdapterConbinStock.notifyDataSetChanged();
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
            // mAdapterConbinStock = new BaseAdatperSelectStockFund(getActivity(), mDataList);
        }
        mAdapterConbinStock.setCheckChangeListener(this);
        if (mViewType != ViewType.SEARCH.getTypeId()) {
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
            ConStockBean csBean = new ConStockBean();
            csBean.setName("基金名称" + i);
            csBean.setStockId(i + 100);

            csBean.setCurrentValue(9.15f + i);
            mDataList.add(csBean);
        }
    }

    private void loadDataByStock() {
        for (int i = 0; i < 20; i++) {
            ConStockBean csBean = new ConStockBean();
            csBean.setName("个股名" + i);
            csBean.setStockId(i+101000001);
            csBean.setCurrentValue(9.15f + i);
            mDataList.add(csBean);
        }
    }

    public void setCheckListener(ISelectChangeListener listener) {
        if (null != mAdapterConbinStock)
            mAdapterConbinStock.setCheckChangeListener(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_selectstock, null);
        initView(view);
        return view;
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
        mListView = (ListView) view.findViewById(R.id.lv_select_stock);
        mListView.addFooterView(mFootView);
        mListView.setAdapter(mAdapterConbinStock);

        mListView.removeFooterView(mFootView);
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(isLoadingMore)) {
                    System.out.println("Loading more");
                    mListView.addFooterView(mFootView);
                    Thread thread = new Thread(null, loadMoreListItems);
                    thread.start();
                }

            }
        });

    }

    // Runnable to load the items
    private List<ConStockBean> loadList;
    private Runnable loadMoreListItems = new Runnable() {
        @Override
        public void run() {
            isLoadingMore = true;
            loadList = new ArrayList<ConStockBean>();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            for (int i = 0; i < 20; i++) {
                ConStockBean csBean = new ConStockBean();
                csBean.setName("加载项" + i);
                csBean.setStockId(i + 120);
                csBean.setCurrentValue(30.15f + i);
                loadList.add(csBean);
            }
            getActivity().runOnUiThread(returnRes);
        }
    };

    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            if (loadList != null && loadList.size() > 0) {
                mDataList.addAll(loadList);
            }
            mAdapterConbinStock.notifyDataSetChanged();
            isLoadingMore = false;
            mListView.removeFooterView(mFootView);
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
}
