/**
 * @Title FundsOrderFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundsOrderEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.ui.NewCombinationDetailActivity;
import com.dkhs.portfolio.ui.OrderFundDetailActivity;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.FundsOrderAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.StockViewType;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.cache.MD5FileNameGenerator;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/**
 * @ClassName FundsOrderFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version 1.0
 */
public class FundsOrderFragment extends LoadMoreListFragment {

    private static final String ARGUMENT_ORDER_TYPE = "order_type";
    public static final String ORDER_TYPE_DAY = "chng_pct_day";
    public static final String ORDER_TYPE_WEEK = "chng_pct_week";
    public static final String ORDER_TYPE_MONTH = "chng_pct_month";
    // public static final String ORDER_TYPE_SEASON = "chng_pct_three_month";
    public static final String ORDER_TYPE_ALL = "net_value";
    private String mOrderType;
    private FundsOrderAdapter mAdapter;
    private List<CombinationBean> mDataList = new ArrayList<CombinationBean>();
    private FundsOrderEngineImpl orderEngine;
    private boolean isvisible = false;

    public static FundsOrderFragment getFragment(String orderType) {
        FundsOrderFragment fragment = new FundsOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_ORDER_TYPE, orderType);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Bundle bundle = getArguments();

        if (null != bundle) {
            mOrderType = bundle.getString(ARGUMENT_ORDER_TYPE);
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    ListAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new FundsOrderAdapter(getActivity(), mDataList, mOrderType);
        }
        return mAdapter;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loadData() {
        if (isvisible) {
            setHttpHandler(getLoadEngine().loadData());
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param view
     * @param savedInstanceState
     * @return
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
        mListView.setDividerHeight(0);
    }

    @Override
    public void loadFinish(MoreDataBean object) {

        super.loadFinish(object);
        mSwipeLayout.setRefreshing(false);
        if (null != object && null != object.getResults() && object.getResults().size() > 0) {
            // add by zcm -----2014.12.15
            setListViewVisible();
            // add by zcm -----2014.12.15
            // mDataList = object.getResults();
            if (isRefresh) {
                mDataList.clear();
                isRefresh = false;
            }
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
            // PromptManager.closeProgressDialog();
        }
        if (null == mDataList || mDataList.size() == 0) {
            if (mOrderType.contains(ORDER_TYPE_DAY)) {
                // modify by zcm -----2014.12.15
                setEmptyText("还没有开盘,请耐心等待");
                // modify by zcm -----2014.12.15
            } else if (mOrderType.contains(ORDER_TYPE_WEEK)) {

                setEmptyText("周排行暂无数据");
            } else if (mOrderType.contains(ORDER_TYPE_MONTH)) {

                setEmptyText("月排行暂无数据");

            } else if (mOrderType.contains(ORDER_TYPE_ALL)) {

                setEmptyText("总排行暂无数据");
            }
        }

    }

    private boolean isRefresh;

    Handler dataHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (mDataList.isEmpty()) {
                loadData();
            } else {
                setHttpHandler(getLoadEngine().refreshDatabySize(mDataList.size()));
                isRefresh = true;
            }
            dataHandler.postDelayed(this, 60 * 1000);
        }
    };

    @Override
    public void onPause() {

        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(mPageName);

        if (isvisible) {
            // loadData();
            dataHandler.removeCallbacks(runnable);
            dataHandler.postDelayed(runnable, 60);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            Bundle bundle = getArguments();

            if (null != bundle) {
                mOrderType = bundle.getString(ARGUMENT_ORDER_TYPE);
            }
            isvisible = true;
            // loadData();
            dataHandler.postDelayed(runnable, 60);

        } else {
            isvisible = false;
            dataHandler.removeCallbacks(runnable);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (null == orderEngine) {
            orderEngine = new FundsOrderEngineImpl(this, mOrderType);
        }
        return orderEngine;
    }

    @Override
    OnItemClickListener getItemClickListener() {

        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(NewCombinationDetailActivity.newIntent(getActivity(), mDataList.get(position)));

                
                // getActivity().startActivity(
                //       OrderFundDetailActivity.getIntent(getActivity(), mDataList.get(position), true, mOrderType));
            }
        };
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_funds);

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    OnRefreshListener setOnRefreshListener() {
        // TODO Auto-generated method stub
        return new OnRefreshListener() {

            @Override
            public void onRefresh() {
                setHttpHandler(getLoadEngine().refreshDatabySize(mDataList.size()));
                isRefresh = true;
            }
        };
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void loadFail() {
        mSwipeLayout.setRefreshing(false);
        isRefresh = false;

    }

}
