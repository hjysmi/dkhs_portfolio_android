/**
 * @Title SearchStockEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午3:15:01
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SearchHistoryBean;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockProfileDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.IResultCallback;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.LoaderHelper;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.VisitorCursorCreateImpl;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName SearchStockEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-17 下午3:15:01
 */
public class SearchStockEngineImpl {

    /**
     * 从服务器下载股票信息到本地数据
     * 请服务器获取到3348条数据，包括解析数据，插入数据到数据库一共耗时42秒
     */
    public static void loadStockList() {
        // 股票类型 1, '股票' 2, '债券' 3, '基金' 5, '指数' 默认 1
        String loadStockUrl = DKHSUrl.StockSymbol.profile + "?symbol_type=1&exchange=1,2";
        String loadFundUrl = DKHSUrl.StockSymbol.profile + "?symbol_type=3&exchange=1,2,12";
        String loadIndexUrl = DKHSUrl.StockSymbol.profile + "?symbol_type=5&exchange=1,2";

//         DKHSClient.requestLong(HttpMethod.GET, loadStockUrl, null, stockProfiListener);
//         DKHSClient.requestLong(HttpMethod.GET, loadFundUrl, null, stockProfiListener);
//         DKHSClient.requestLong(HttpMethod.GET, loadIndexUrl, null, stockProfiListener);

        String lastLoadTime = PortfolioPreferenceManager
                .getStringValue(PortfolioPreferenceManager.KEY_LAST_LOAD_DATETIME);
        StringBuilder sbLastDate = new StringBuilder("&last_datetime=");
        if (TextUtils.isEmpty(lastLoadTime)) {

            sbLastDate.append("2015-09-10T06:37:15Z");
        } else {

            sbLastDate.append(lastLoadTime);
        }
        DKHSClient.requestLong(HttpMethod.GET, loadStockUrl + sbLastDate.toString(), null, stockProfiListener);
        DKHSClient.requestLong(HttpMethod.GET, loadFundUrl + sbLastDate.toString(), null, stockProfiListener);
        DKHSClient.requestLong(HttpMethod.GET, loadIndexUrl + sbLastDate.toString(), null, stockProfiListener);

    }

    static ParseHttpListener stockProfiListener = new ParseHttpListener<String>() {

        @Override
        protected String parseDateTask(String jsonData) {

            try {

                StockProfileDataBean dataBean = DataParse.parseObjectJson(StockProfileDataBean.class, jsonData);
                // dataList = DataParse.parseJsonList(jsonData, listType);
                if (null != dataBean) {

                    List<SearchStockBean> dataList = dataBean.getResults();
                    DbUtils dbUtils = AppConfig.getDBUtils();
                    // dbUtils.configAllowTransaction(true);
                    try {
                        dbUtils.saveOrUpdateAll(dataList);
                        LogUtils.d("Insert " + dataList.size() + " item to stock database success!");
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return dataBean.getLast_datetime();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void afterParseData(String object) {
            if (!TextUtils.isEmpty(object)) {
                PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_LAST_LOAD_DATETIME, object);

            }

        }
    };

    public void searchStockByLoader(String key, FragmentActivity activity) {
        Bundle args = new Bundle();
        args.putInt(VisitorCursorCreateImpl.TYPE, VisitorCursorCreateImpl.VALUE_SEARCH_STOCK);
        args.putString(VisitorCursorCreateImpl.TYPE_SEARCH, key);
        LoaderHelper.initCursorLoader(activity, args, new SearchStockCallBack());

    }

    private void setSearchBack(List<SelectStockBean> selectStockList) {
        if (null != iLoadListener) {

            MoreDataBean moreDataBean = new MoreDataBean<SelectStockBean>();
            moreDataBean.setCurrentPage(1);
            moreDataBean.setResults(selectStockList);
            moreDataBean.setTotalCount(selectStockList.size());
            moreDataBean.setTotalPage(1);
            iLoadListener.loadFinish(moreDataBean);
        }
    }


    public void searchFundsByLoader(String key, FragmentActivity activity) {
        Bundle args = new Bundle();
        args.putInt(VisitorCursorCreateImpl.TYPE, VisitorCursorCreateImpl.VALUE_SEARCH_FUNDS);
        args.putString(VisitorCursorCreateImpl.TYPE_SEARCH, key);
        LoaderHelper.initCursorLoader(activity, args, new SearchStockCallBack());

    }

    private class SearchStockCallBack implements IResultCallback<SearchStockBean> {
        @Override
        public void onResultCallback(List<SearchStockBean> searchStockList) {
            List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
            if (null != searchStockList) {
                for (SearchStockBean searchBean : searchStockList) {
                    selectStockList.add(SelectStockBean.copy(searchBean));
                }
                LogUtils.d(" searchStockDataList size:" + selectStockList.size());
            } else {

                LogUtils.d(" searchStockDataList is null");
            }

            setSearchBack(selectStockList);


        }
    }

    public void searchStockIndexFunds(String key, FragmentActivity activity) {
        LogUtils.e(" searchStockIndexFunds:" + key);
        Bundle args = new Bundle();
        args.putInt(VisitorCursorCreateImpl.TYPE, VisitorCursorCreateImpl.VALUE_SEARCH_STOCKINDEXFUNDS);
        args.putString(VisitorCursorCreateImpl.TYPE_SEARCH, key);
        LoaderHelper.initCursorLoader(activity, args, new SearchStockCallBack());

    }


    public void searchHistoryStock(FragmentActivity activity) {
        Bundle args = new Bundle();
        args.putInt(VisitorCursorCreateImpl.TYPE, VisitorCursorCreateImpl.VALUE_SEARCH_HISTORYSTOCK);

        LoaderHelper.initCursorLoader(activity, args, new IResultCallback<SearchHistoryBean>() {
            @Override
            public void onResultCallback(List<SearchHistoryBean> searchStockList) {
                List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
                if (null != searchStockList) {
                    for (SearchHistoryBean searchBean : searchStockList) {
                        selectStockList.add(SelectStockBean.copy(searchBean));
                    }
                    LogUtils.d(" searchHistoryStock size:" + selectStockList.size());
                } else {

                    LogUtils.d(" searchHistoryStock is null");
                }

                setSearchBack(selectStockList);
            }
        });

    }


    public SearchStockEngineImpl(ILoadDataBackListener loadListener) {
        this.iLoadListener = loadListener;
    }

    private ILoadDataBackListener iLoadListener;

    public void setLoadDataBackListener(ILoadDataBackListener backListener) {
        this.iLoadListener = backListener;
    }

}
