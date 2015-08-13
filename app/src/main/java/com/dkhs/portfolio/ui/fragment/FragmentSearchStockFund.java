/**
 * @Title FragmentSelectStock.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-29 上午9:36:16
 * @version V1.0
 */
package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.common.WeakHandler;
import com.dkhs.portfolio.engine.LoadMoreDataEngine;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.SearchStockEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.AddSearchItemAdapter;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund;
import com.dkhs.portfolio.ui.adapter.BaseAdatperSelectStockFund.ISelectChangeListener;
import com.dkhs.portfolio.ui.adapter.SearchFundAdatper;
import com.dkhs.portfolio.ui.adapter.SearchStockAdatper;
import com.dkhs.portfolio.ui.widget.MAlertDialog;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StockUitls;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName FragmentSelectStock
 * @Description 个股选择
 * @date 2014-8-29 上午9:36:16
 */
public class FragmentSearchStockFund extends VisiableLoadFragment implements ISelectChangeListener, OnClickListener {
    private static final String TAG = FragmentSearchStockFund.class.getSimpleName();

    private static final String ARGUMENT_LOAD_FUND = "isloadfund";
    private static final String ARGUMENT_LOAD_STATUS = "isload_status";
    private static final String ARGUMENT_SEARCH_TYPE = "argument_search_type";
    private static final String SEARCH_TYPE_FUNDS = "search_type_funds";
    private static final String SEARCH_TYPE_STOCK = "search_type_stock";
    //    private static final String SEARCH_TYPE_STATUS_STOCK = "search_type_status_stock";
    private static final String SEARCH_TYPE_HISTORY = "search_type_history";
    private static final String SEARCH_TYPE_STOCKANDINDEX = "search_type_stockandindex";

    private static final String ARGUMENT_ITEM_CLICK_BACK = "argument_item_click_back";
    public static final String EXTRA_STOCK = "argument_select_stock";


    private boolean isItemClickBack;
    private String mSearchType;

    private BaseAdatperSelectStockFund mAdapterConbinStock;

    private List<SelectStockBean> mDataList = new ArrayList<SelectStockBean>();

    private View tvHistoryTip;
    private View tvClearHistory;
    private boolean isFund;
    private boolean isStatus;

    LoadMoreDataEngine mLoadDataEngine;
    SearchStockEngineImpl mSearchEngine;

    public static FragmentSearchStockFund getStockFragment() {
        FragmentSearchStockFund fragment = new FragmentSearchStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putString(ARGUMENT_SEARCH_TYPE, SEARCH_TYPE_STOCK);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSearchStockFund getFundFragment() {
        FragmentSearchStockFund fragment = new FragmentSearchStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, true);
        args.putString(ARGUMENT_SEARCH_TYPE, SEARCH_TYPE_FUNDS);
        // args.putInt(ARGUMENT_LOAD_TYPE, type.getTypeId());
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentSearchStockFund getItemClickBackFragment(boolean isStatus) {
        FragmentSearchStockFund fragment = new FragmentSearchStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putBoolean(ARGUMENT_ITEM_CLICK_BACK, true);
        args.putString(ARGUMENT_SEARCH_TYPE, SEARCH_TYPE_STOCKANDINDEX);
        args.putBoolean(ARGUMENT_LOAD_STATUS, isStatus);
        fragment.setArguments(args);
        return fragment;
    }


    public static FragmentSearchStockFund getHistoryFragment(boolean isItemClickBack, boolean isStatus) {
        FragmentSearchStockFund fragment = new FragmentSearchStockFund();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_LOAD_FUND, false);
        args.putString(ARGUMENT_SEARCH_TYPE, SEARCH_TYPE_HISTORY);
        args.putBoolean(ARGUMENT_LOAD_STATUS, isStatus);
        args.putBoolean(ARGUMENT_ITEM_CLICK_BACK, isItemClickBack);
        fragment.setArguments(args);
        return fragment;
    }

    public void searchByKey(String key) {
        if (!TextUtils.isEmpty(mSearchType)) {
            mDataList.clear();
            if (mSearchType.equalsIgnoreCase(SEARCH_TYPE_FUNDS)) {

                // new SearchStockEngineImpl(mSelectStockBackListener).searchStock(key);
                mSearchEngine.searchFunds(key);
            } else if (mSearchType.equalsIgnoreCase(SEARCH_TYPE_STOCK)) {

                mSearchEngine.searchStock(key);
            } else {
                mSearchEngine.searchStockIndexFunds(key);
            }
            mAdapterConbinStock.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (null != bundle) {
            isFund = bundle.getBoolean(ARGUMENT_LOAD_FUND);
            isItemClickBack = bundle.getBoolean(ARGUMENT_ITEM_CLICK_BACK);
            mSearchType = bundle.getString(ARGUMENT_SEARCH_TYPE);
            isStatus = bundle.getBoolean(ARGUMENT_LOAD_STATUS);

        }
        if (isFund) {
            mAdapterConbinStock = new SearchFundAdatper(getActivity(), mDataList, true);
        } else if (isItemClickBack) {
            mAdapterConbinStock = new AddSearchItemAdapter(getActivity(), mDataList, isStatus);
        } else {
            mAdapterConbinStock = new SearchStockAdatper(getActivity(), mDataList, true);
        }
        mAdapterConbinStock.setCheckChangeListener(this);

        mSearchEngine = new SearchStockEngineImpl(mSelectStockBackListener);

    }

    // private List<SelectStockBean> hasLoadSelect

    ILoadDataBackListener mSelectStockBackListener = new ILoadDataBackListener() {

        @Override
        public void loadFinish(MoreDataBean object) {
            if (null != object && null != object.getResults()) {
                Message msg = new Message();
//                msg.obj = mDataList.addAll(object.getResults());
                msg.what = 777;
                msg.obj = object;
//                msg.sendToTarget();
                updateHandler.sendMessage(msg);
            }
        }

        @Override
        public void loadFail() {
            // TODO Auto-generated method stub

        }

    };

    private WeakHandler updateHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            MoreDataBean bean = (MoreDataBean) msg.obj;
            if (null != bean && null != bean.getResults()) {
                mDataList.addAll(bean.getResults());
                mAdapterConbinStock.notifyDataSetChanged();


            }

            if (isSearchHistory()) {
                if (!mDataList.isEmpty()) {

                    showHistoryText(true);
                } else {
                    showHistoryText(false);
                }
            }
            return true;
        }
    });

    OnItemClickListener itemBackClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            SelectStockBean itemStock = mDataList.get(position);

            VisitorDataEngine.saveHistory(itemStock.parseHistoryBean());
            if (isStatus) {
//                PromptManager.showToast("选择添加话题股票：" + itemStock.getName());
                setSelectBack(itemStock);
//                getActivity().finish();
            } else if (StockUitls.isFundType(itemStock.symbol_type)) {
                startActivity(FundDetailActivity.newIntent(getActivity(), itemStock));
            } else {

                startActivity(StockQuotesActivity.newIntent(getActivity(), itemStock));
            }


        }
    };


    private void setSelectBack(SelectStockBean type) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_STOCK, Parcels.wrap(type));
        getActivity().setResult(Activity.RESULT_OK, intent);

        getActivity().finish();
    }

    public void setCheckListener(ISelectChangeListener listener) {
        if (null != mAdapterConbinStock)
            mAdapterConbinStock.setCheckChangeListener(listener);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_search;
    }

    public void refreshSelect() {

        if (null != mAdapterConbinStock) {
            mAdapterConbinStock.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void initView(View view) {
        tvHistoryTip = view.findViewById(R.id.tv_history_tip);

        ListView mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        mListView.setAdapter(mAdapterConbinStock);
        if (isItemClickBack) {
            mListView.setOnItemClickListener(itemBackClick);
        }

        if (isSearchHistory()) {
            View footView = View.inflate(getActivity(), R.layout.layout_history_bottom, null);
            footView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDelDialog();
                }
            });
            tvClearHistory = footView.findViewById(R.id.tv_clear);
            mListView.addFooterView(footView, null, false);
        }

    }


    private void showDelDialog() {

        MAlertDialog builder = PromptManager.getAlertDialog(getActivity());

        builder.setMessage(R.string.dialog_msg_del_history)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                clickDelHistory();

            }
        });


        builder.show();
    }

    private void clickDelHistory() {
        mDataList.clear();
        showHistoryText(false);
        mAdapterConbinStock.notifyDataSetChanged();
        VisitorDataEngine.clearHistoryStock();
    }

    private void showHistoryText(boolean iShow) {
        if (iShow) {
            tvClearHistory.setVisibility(View.VISIBLE);
            tvHistoryTip.setVisibility(View.VISIBLE);
        } else {
            tvClearHistory.setVisibility(View.GONE);
            tvHistoryTip.setVisibility(View.GONE);
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

    @Override
    public void requestData() {

    }


    @Override
    public void onViewShow() {
        super.onViewShow();
        if (isSearchHistory()) {
            mDataList.clear();
            mSearchEngine.searchHistoryStock();
        }

    }


    private boolean isSearchHistory() {
        return !TextUtils.isEmpty(mSearchType) && mSearchType.equals(SEARCH_TYPE_HISTORY);
    }

    @Override
    public void onViewHide() {
        super.onViewHide();

    }
}
