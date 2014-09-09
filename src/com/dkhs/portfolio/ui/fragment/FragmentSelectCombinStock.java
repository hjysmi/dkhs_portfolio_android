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
import com.dkhs.portfolio.ui.AddCombinationStockActivity;
import com.dkhs.portfolio.ui.adapter.AdatperSelectCombinStock;
import com.dkhs.portfolio.ui.adapter.AdatperSelectCombinStock.ISelectChangeListener;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName FragmentSelectStock
 * @Description 个股选择
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version 1.0
 */
public class FragmentSelectCombinStock extends Fragment implements ISelectChangeListener, OnClickListener {
    private static final String TAG = FragmentSelectCombinStock.class.getSimpleName();
    private ListView mListView;
    private AdatperSelectCombinStock mAdapterConbinStock;
    private AddCombinationStockActivity mActivity;

    private String mOrderType;
    private List<ConStockBean> mDataList = new ArrayList<ConStockBean>();

    private boolean isLoadingMore;
    private View mFootView;

    public static FragmentSelectCombinStock getInstance() {
        FragmentSelectCombinStock fragment = new FragmentSelectCombinStock();
        Bundle args = new Bundle();
        // args.putString("order_type", value);

        fragment.setArguments(args);
        return fragment;
    }

    public void searchByKey(String key) {
        testSearchKey(key);
    }

    private void testSearchKey(String key) {
        mDataList.clear();
        for (int i = 0; i < 20; i++) {
            ConStockBean csBean = new ConStockBean();
            csBean.setName(key + i);
            csBean.setId(i);
            csBean.setCurrentValue(20.00f + i);
            mDataList.add(csBean);
        }
        mAdapterConbinStock.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log.i(TAG, "===============onCreate================");
        mAdapterConbinStock = new AdatperSelectCombinStock(getActivity(), mDataList);
        mAdapterConbinStock.setCheckChangeListener(this);
        Bundle bundle = getArguments();
        if (null != bundle) {
            mOrderType = bundle.getString("order_type", "");
        }
        initData();

    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            ConStockBean csBean = new ConStockBean();
            csBean.setName("个股名" + i);
            csBean.setId(i + 100);
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
                csBean.setId(i + 120);
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
        AddCombinationStockActivity mActivity = (AddCombinationStockActivity) getActivity();
        mActivity.notifySelectDataChange(false);
    }

    @Override
    public void onClick(View v) {

    }
}
