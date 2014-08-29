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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.ConStockBean;
import com.dkhs.portfolio.ui.AddConbinationStockActivity;
import com.dkhs.portfolio.ui.adapter.AdatperSelectConbinStock;
import com.dkhs.portfolio.ui.adapter.AdatperSelectConbinStock.ISelectChangeListener;

/**
 * @ClassName FragmentSelectStock
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version 1.0
 */
public class FragmentSelectConbinStock extends Fragment implements ISelectChangeListener {
    private static final String TAG = FragmentSelectConbinStock.class.getSimpleName();
    private ListView mListView;
    private AdatperSelectConbinStock mAdapterConbinStock;
    private AddConbinationStockActivity mActivity;

    private String mOrderType;
    private List<ConStockBean> mDataList = new ArrayList<ConStockBean>();

    // private

    public static FragmentSelectConbinStock getInstance() {
        FragmentSelectConbinStock fragment = new FragmentSelectConbinStock();
        Bundle args = new Bundle();
        // args.putString("order_type", value);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log.i(TAG, "===============onCreate================");
        mAdapterConbinStock = new AdatperSelectConbinStock(getActivity(), mDataList);
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
        // Log.i(TAG, "===============onCreateView " + this + "================");
        View view = View.inflate(getActivity(), R.layout.fragment_selectstock, null);
        initView(view);
        return view;
    }

    public void refreshSelect() {

        if (null != mAdapterConbinStock) {
            // Log.i(TAG, "===============refreshSelect " + this + "================");
            mAdapterConbinStock.notifyDataSetChanged();
        }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i(TAG, "===============onStart================");

    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.lv_select_stock);
        mListView.setAdapter(mAdapterConbinStock);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param buttonView
     * @param isChecked
     * @return
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        AddConbinationStockActivity mActivity = (AddConbinationStockActivity) getActivity();
        mActivity.notifySelectDataChange(false);
    }
}
