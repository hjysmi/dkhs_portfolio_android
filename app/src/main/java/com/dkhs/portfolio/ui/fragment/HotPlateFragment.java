/**
 * @Title FundsOrderFragment.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午4:03:33
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.PlateLoadMoreEngineImpl;
import com.dkhs.portfolio.ui.MarketListActivity;
import com.dkhs.portfolio.ui.MarketListActivity.ILoadingFinishListener;
import com.dkhs.portfolio.ui.MarketListActivity.LoadViewType;
import com.dkhs.portfolio.ui.adapter.HotPlateAdapter;
import com.dkhs.portfolio.ui.widget.PullToRefreshPageListView;
import com.dkhs.portfolio.ui.widget.PullToRefreshPageListView.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FundsOrderFragment
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-10-29 下午4:03:33
 */
public class HotPlateFragment extends LoadPageMoreListFragment {

    private static final String ARGUMENT_ORDER_TYPE = "order_type";
    // public static final String ORDER_TYPE_DAY = "chng_pct_day";
    // public static final String ORDER_TYPE_WEEK = "chng_pct_week";
    // public static final String ORDER_TYPE_MONTH = "chng_pct_month";
    // // public static final String ORDER_TYPE_SEASON = "chng_pct_three_month";
    public static final String ORDER_TYPE_UP = "-percentage";
    public static final String ORDER_TYPE_DOWN = "percentage";
    private String mOrderType;
    // private FundsOrderAdapter mAdapter;
    private HotPlateAdapter mAdapter;
    private List<SectorBean> mDataList = new ArrayList<SectorBean>();
    private PlateLoadMoreEngineImpl orderEngine;

    public static HotPlateFragment getFragment(String orderType) {
        HotPlateFragment fragment = new HotPlateFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private PullToRefreshPageListView mListView;

    /**
     * @param listview
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void setListViewInit(PullToRefreshPageListView listview) {
        // TODO Auto-generated method stub
        super.setListViewInit(listview);
        mListView = listview;
        mListView.setCanRefresh(true);
        mListView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (orderEngine.getCurrentpage() <= 1) {
                    orderEngine.refreshDatabySize(1);
                } else {
                    // orderEngine.setCurrentpage(orderEngine.getCurrentpage() - 2);
                    orderEngine.refreshDatabySize(orderEngine.getCurrentpage() - 1);
                }
                // orderEngine.loadMore();
                if (null != finishListener) {
                    finishListener.startLoadingData();
                }
            }
        });
    }

    @Override
    ListAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new HotPlateAdapter(getActivity(), mDataList);
        }
        return mAdapter;
    }

    @Override
    public void loadData() {
        if (null != finishListener) {
            finishListener.startLoadingData();
        }
        getLoadEngine().loadData();
    }

    private boolean isRefresh;

    public void refreshData() {
        isRefresh = true;
        if (null != finishListener) {
            finishListener.startLoadingData();
        }
        getLoadEngine().refreshDatabySize(getLoadEngine().getCurrentpage());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        showProgress();
        loadData();
    }

    private ILoadingFinishListener finishListener;

    public void setLoadingFinishListener(ILoadingFinishListener finishlistener) {
        this.finishListener = finishlistener;
    }

    @Override
    public void loadFinish(MoreDataBean object) {
        dissProgress();
        mListView.onLoadMoreComplete();
        mListView.onRefreshComplete();
        // mLoadDataEngine.getTotalpage()
        mListView.setCurrentPage(orderEngine.getCurrentpage());
        mListView.setTotalPage(orderEngine.getTotalpage());

        super.loadFinish(object);
        if (null != finishListener) {
            finishListener.loadingFinish();
        }
        if (null != object.getResults() && object.getResults().size() > 0) {
            // add by zcm -----2014.12.15
            setListViewVisible();
            if (isRefresh) {
                // mDataList.clear();
            }
            mDataList.clear();
            // add by zcm -----2014.12.15
            // mDataList = object.getResults();
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
            // PromptManager.closeProgressDialog();
        }
        if (null == mDataList || mDataList.size() == 0) {
            setEmptyText("热门行业暂无数据");
        }

    }

    WeakHandler dataHandler = new WeakHandler() {

    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // loadData();
            // dataHandler.postDelayed(this, 60 * 1000);
        }
    };

    @Override
    public void onPause() {

        super.onPause();
        dataHandler.removeCallbacks(runnable);// 关闭定时器处理
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            // dataHandler.postDelayed(runnable, 60 * 1000);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (null == orderEngine) {
            // orderEngine = new PlateEngineImpl(this);
            orderEngine = new PlateLoadMoreEngineImpl(this, mOrderType);
        }
        return orderEngine;
    }

    @Override
    OnItemClickListener getItemClickListener() {

        return new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SectorBean bean = mDataList.get(position);
                startActivity(MarketListActivity.newIntent(getActivity(), LoadViewType.PlateList, bean.getId(),
                        bean.getAbbr_name()));

            }
        };
    }

    private final String mPageName = PortfolioApplication.getInstance().getString(R.string.count_funds);

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public void loadFail() {
        dissProgress();
        mListView.onLoadMoreComplete();
        mListView.onRefreshComplete();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */

}
