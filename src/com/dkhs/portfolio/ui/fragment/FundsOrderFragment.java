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
import com.dkhs.portfolio.bean.ChampionBean;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundsOrderEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.OrderFundDetailActivity;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.FundsOrderAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.cache.MD5FileNameGenerator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    public static final String ORDER_TYPE_SEASON = "chng_pct_three_month";
    private String mOrderType;
    private FundsOrderAdapter mAdapter;
    private List<ChampionBean> mDataList = new ArrayList<ChampionBean>();
    private FundsOrderEngineImpl orderEngine;

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
        getLoadEngine().loadData();
    }

    @Override
    public void loadFinish(MoreDataBean object) {

        super.loadFinish(object);
        if (null != object.getResults() && object.getResults().size() > 0) {

            // mDataList = object.getResults();
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
        } else {
            if (mOrderType.contains(ORDER_TYPE_DAY)) {
                setEmptyText("日排行暂无数据");
            } else if (mOrderType.contains(ORDER_TYPE_WEEK)) {

                setEmptyText("周排行暂无数据");
            } else if (mOrderType.contains(ORDER_TYPE_MONTH)) {

                setEmptyText("月排行暂无数据");

            } else if (mOrderType.contains(ORDER_TYPE_SEASON)) {

                setEmptyText("季排行暂无数据");
            }
        }

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

                getActivity().startActivity(
                        OrderFundDetailActivity.getIntent(getActivity(),
                                CombinationBean.parse(mDataList.get(position)), true));
            }
        };
    }

}
