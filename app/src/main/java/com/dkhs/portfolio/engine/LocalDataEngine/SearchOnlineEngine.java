package com.dkhs.portfolio.engine.LocalDataEngine;

import android.text.TextUtils;

import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.SelectGeneralEngine;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.fragment.FragmentSearchStockFund;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Created by wuyongsen on 2015/12/4.
 */
public class SearchOnlineEngine {
    private final static int PAGE_SIZE = 20;
    /**
     * 所有的上证A股(symbol_stype=101)，不显示指数、基金、上证B股
     */
    private final static String SYMBOL_TYPE_STOCK = "1";
    private final static String SYMBOL_STYPE_STOCK = "101";
    /**
     * 股票、指数和基金
     */
    private final static String SYMBOL_TYPE_STOCKANDINDEX = "1,3,5";

    /**
     * 返回股票型和指数型基金
     */
    private final static String SYMBOL_TYPE_FUND = "3";
    private final static String SYMBOL_STYPE_FUND = "300,303";

    private int currentPage = 1;
    private String mSearchType;
    private ILoadDataCallBack mCallBack;

    public SearchOnlineEngine(String searchType, ILoadDataCallBack callBack) {
        mSearchType = searchType;
        mCallBack = callBack;
    }

    private void searchStockFund(String symbolType, String symbolSType, int page, String key) {
        SelectGeneralEngine.searchStockFund(symbolType, symbolSType, key, page, PAGE_SIZE, new ParseHttpListener<MoreDataBean<SelectStockBean>>() {
            @Override
            protected MoreDataBean<SelectStockBean> parseDateTask(String jsonData) {
                MoreDataBean<SelectStockBean> moreBean = null;
                if (!TextUtils.isEmpty(jsonData)) {

                    try {
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        moreBean = (MoreDataBean) gson.fromJson(jsonData, new TypeToken<MoreDataBean<SelectStockBean>>() {
                        }.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return moreBean;
            }

            @Override
            protected void afterParseData(MoreDataBean<SelectStockBean> object) {
                if(object == null){
                    return;
                }
                currentPage = object.getCurrentPage();
                if (mCallBack != null) {
                    mCallBack.loadFinish(object);
                }
            }
        });
    }

    public void loadMore(String key) {
        if (mSearchType.equalsIgnoreCase(FragmentSearchStockFund.SEARCH_TYPE_STOCK)) {
            searchStockFund(SYMBOL_TYPE_STOCK, SYMBOL_STYPE_STOCK, currentPage + 1, key);
        } else if (mSearchType.equalsIgnoreCase(FragmentSearchStockFund.SEARCH_TYPE_STOCKANDINDEX)) {
            searchStockFund(SYMBOL_TYPE_STOCKANDINDEX, "", currentPage + 1, key);
        } else if(mSearchType.equalsIgnoreCase(FragmentSearchStockFund.SEARCH_TYPE_FUNDS)){
            searchStockFund(SYMBOL_TYPE_FUND,SYMBOL_STYPE_FUND, currentPage + 1, key);
        }
    }

    public void searchByKey(String key) {
        if (mSearchType.equalsIgnoreCase(FragmentSearchStockFund.SEARCH_TYPE_STOCK)) {
            searchStockFund(SYMBOL_TYPE_STOCK, SYMBOL_STYPE_STOCK, 1, key);
        } else if (mSearchType.equalsIgnoreCase(FragmentSearchStockFund.SEARCH_TYPE_STOCKANDINDEX)) {
            searchStockFund(SYMBOL_TYPE_STOCKANDINDEX, "", 1, key);
        } else if(mSearchType.equalsIgnoreCase(FragmentSearchStockFund.SEARCH_TYPE_FUNDS)){
            searchStockFund(SYMBOL_TYPE_FUND,SYMBOL_STYPE_FUND, 1, key);
        }
    }

    public  interface ILoadDataCallBack {
        void loadFinish(MoreDataBean<SelectStockBean> data);
    }
}