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
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.SearchStockEngineImpl;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.AddSearchItemAdapter;
import com.dkhs.portfolio.ui.adapter.AddStockItemAdapter;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund.ISelectChangeListener;
import com.dkhs.portfolio.ui.adapter.SearchStockAdatper;
import com.dkhs.portfolio.ui.fragment.FragmentSelectStockFund.ViewType;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName FragmentSelectStock
 * @Description 个股选择
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version 1.0
 */
public class FragmentSearchStockFund extends Fragment implements ISelectChangeListener, OnClickListener {
    private static final String TAG = FragmentSearchStockFund.class.getSimpleName();

    private static final String ARGUMENT_LOAD_FUND = "isloadfund";
    private static final String ARGUMENT_ITEM_CLICK_BACK = "argument_item_click_back";

    private boolean isItemClickBack;

    private ListView mListView;
    private BaseAdatperSelectStockFund mAdapterConbinStock;

    private List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();

    private View mFootView;
    private boolean isFund;

    LoadSelectDataEngine mLoadDataEngine;

    public static FragmentSearchStockFund getStockFragment() {
        FragmentSearchStockFund fragment = new FragmentSearchStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        // args.putInt(ARGUMENT_LOAD_TYPE, type.getTypeId());
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSearchStockFund getFundFragment() {
        FragmentSearchStockFund fragment = new FragmentSearchStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, true);
        // args.putInt(ARGUMENT_LOAD_TYPE, type.getTypeId());
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSearchStockFund getItemClickBackFragment() {
        FragmentSearchStockFund fragment = new FragmentSearchStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putBoolean(ARGUMENT_ITEM_CLICK_BACK, true);
        fragment.setArguments(args);
        return fragment;
    }

    public void searchByKey(String key) {
        mDataList.clear();

        if (isFund) {

            // new SearchStockEngineImpl(mSelectStockBackListener).searchStock(key);
        } else {

            new SearchStockEngineImpl(mSelectStockBackListener).searchStock(key);
        }
        mAdapterConbinStock.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (null != bundle) {
            isFund = bundle.getBoolean(ARGUMENT_LOAD_FUND);
            isItemClickBack = bundle.getBoolean(ARGUMENT_ITEM_CLICK_BACK);

        }
        if (isFund) {
            mAdapterConbinStock = new SearchStockAdatper(getActivity(), mDataList);
        } else if (isItemClickBack) {
            mAdapterConbinStock = new AddSearchItemAdapter(getActivity(), mDataList);
        } else {
            mAdapterConbinStock = new SearchStockAdatper(getActivity(), mDataList);
        }
        mAdapterConbinStock.setCheckChangeListener(this);

    }

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(List<SelectStockBean> dataList) {
            if (null != dataList) {
                mDataList.addAll(dataList);
                mAdapterConbinStock.notifyDataSetChanged();
            }

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
    public static final String ARGUMENT = "ARGUMENT";

    private void setSelectBack(SelectStockBean type) {
        Intent intent = new Intent();
        intent.putExtra(ARGUMENT, type);
        getActivity().setResult(-1, intent);

        getActivity().finish();
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
        // mFootView = View.inflate(getActivity(), R.layout.layout_loading_more_footer, null);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        // mListView.addFooterView(mFootView);
        mListView.setAdapter(mAdapterConbinStock);
        if (isItemClickBack) {
            mListView.setOnItemClickListener(itemBackClick);
            // System.out.println("     mListView.setOnItemClickListener(itemBackClick);");
        }
        // mListView.removeFooterView(mFootView);

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
