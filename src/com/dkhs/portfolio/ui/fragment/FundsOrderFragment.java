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
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FundsOrderEngineImpl;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.FundsOrderAdapter;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
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

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param arg0
     * @return
     */
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
    ListAdapter getListAdapter() {
        if (mAdapter == null) {
            mAdapter = new FundsOrderAdapter(getActivity(), mDataList);
        }
        return mAdapter;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param object
     * @return
     */
    @Override
    public void loadFinish(MoreDataBean object) {

        super.loadFinish(object);
        if (null != object.getResults()) {

            // mDataList = object.getResults();
            mDataList.addAll(object.getResults());
            // System.out.println("datalist size :" + mDataList.size());
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    LoadMoreDataEngine getLoadEngine() {
        if (null == orderEngine) {
            orderEngine = new FundsOrderEngineImpl(this, mOrderType);
        }
        return orderEngine;
    }

}
