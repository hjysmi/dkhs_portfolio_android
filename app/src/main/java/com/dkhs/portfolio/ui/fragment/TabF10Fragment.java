package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.F10DataBean;
import com.dkhs.portfolio.bean.StockQuotesStopTopBean;
import com.dkhs.portfolio.engine.F10DataEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.F10ViewParse;
import com.dkhs.portfolio.utils.UIUtils;
import com.dkhs.widget.CircularProgress;

import java.util.List;

/**
 * Created by zjz on 2015/5/13.
 */
public class TabF10Fragment extends BaseFragment {

    public static final String TAG = "TabF10Fragment";
    private boolean isViewShown;
    private LinearLayout mContentView;
    private LinearLayout ll_loading;
    public static final String EXTRA_TAB_TYPE = "extra_tab_type";
    public static final String EXTRA_SYMBOL = "extra_symbol";
    private Context mContext;
    private CircularProgress loadView;

    public enum TabType {
        INTRODUCTION,
        STOCK_HODLER,
        FINANCE;
    }

    private TabType mTabtype;
    private String mSymbol;

    public static TabF10Fragment newIntent(String symbol, TabType tabType) {
        TabF10Fragment fragment = new TabF10Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_TAB_TYPE, tabType);
        bundle.putString(EXTRA_SYMBOL, symbol);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int setContentLayoutId() {
        return R.layout.activity_option_market_newslist;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handArguments();
    }

    private void handArguments() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            mTabtype = (TabType) bundle.getSerializable(EXTRA_TAB_TYPE);
            mSymbol = bundle.getString(EXTRA_SYMBOL);
        }
    }

    private void loadDate() {
        F10DataEngineImpl mDataEngine = new F10DataEngineImpl();
        if (mTabtype == TabType.INTRODUCTION) {
            mDataEngine.getIntroduction(mSymbol, requestListener);
        } else if (mTabtype == TabType.STOCK_HODLER) {
            mDataEngine.getStockHoder(mSymbol, requestListener);
        } else if (mTabtype == TabType.FINANCE) {
            mDataEngine.getFinance(mSymbol, requestListener);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof StockQuotesActivity) {
//            ((StockQuotesActivity) getActivity()).setLayoutHeight(2);
        }
        initView(view);
        loadDate();
    }


    private void initView(View view) {
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
        loadView = (CircularProgress) view.findViewById(R.id.loadView);
        mContentView = (LinearLayout) view.findViewById(R.id.ll_content);
        float dimen = UIUtils.dip2px(getActivity(), (UIUtils.getDimen(getActivity(), R.dimen.title_tool_bar) ));
        int minHeight = UIUtils.getDisplayMetrics().heightPixels - (int) dimen;
        mContentView.setMinimumHeight(minHeight);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            // fragment可见时加载数据
////            if (isVisibleToUser) {
////                getadle = true;
////                ((StockQuotesActivity) getActivity()).setLayoutHeights(this);
////
////            }
//            if (getView() != null) {
//                isViewShown = true;
//
//            } else {
//                isViewShown = false;
//                if (null != this.mContentView) {
//
////                    ((StockQuotesActivity) getActivity()).setLayoutHeights(this.mContentView.getHeight());
//                }
//            }
        } else {
            // 不可见时不执行操作
//            getadle = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    ParseHttpListener requestListener = new ParseHttpListener<List<F10DataBean>>() {

        @Override
        protected List<F10DataBean> parseDateTask(String jsonData) {
            return DataParse.parseArrayJson(F10DataBean.class, jsonData);

        }

        @Override
        protected void afterParseData(List<F10DataBean> dataBeanList) {
            ll_loading.setVisibility(View.GONE);
            if (null != dataBeanList) {
                for (F10DataBean dataBean : dataBeanList) {
                    final View view = new F10ViewParse(mContext, dataBean).getContentView();

                    TabF10Fragment.this.mContentView.addView(view);

                }
            }
            BusProvider.getInstance().post(new StockQuotesStopTopBean());
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            super.onFailure(errCode, errMsg);
            ll_loading.setVisibility(View.GONE);
        }
    };


}
