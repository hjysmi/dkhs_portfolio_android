/**
 * @Title SearchStockEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午3:15:01
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SearchFundsBean;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockProfileDataBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

/**
 * @ClassName SearchStockEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-17 下午3:15:01
 * @version 1.0
 */
public class SearchStockEngineImpl {

    /**
     * 从服务器下载股票信息到本地数据
     * 请服务器获取到3348条数据，包括解析数据，插入数据到数据库一共耗时42秒
     */
    public static void loadStockList() {
        StringBuilder loadUrl = new StringBuilder(DKHSUrl.StockSymbol.profile + "?symbol_type=1,3,5");
        // "last_datetime"
        String lastLoadTime = PortfolioPreferenceManager
                .getStringValue(PortfolioPreferenceManager.KEY_LAST_LOAD_DATETIME);
        if (!TextUtils.isEmpty(lastLoadTime)) {
            loadUrl.append("&last_datetime=");
            loadUrl.append(lastLoadTime);
        }

        DKHSClient.requestLong(HttpMethod.GET, loadUrl.toString(), null, new ParseHttpListener<String>() {

            @Override
            protected String parseDateTask(String jsonData) {

                try {

                    // Type listType = new TypeToken<List<SearchStockBean>>() {
                    // }.getType();
                    StockProfileDataBean dataBean = DataParse.parseObjectJson(StockProfileDataBean.class, jsonData);
                    // dataList = DataParse.parseJsonList(jsonData, listType);
                    if (null != dataBean) {

                        List<SearchStockBean> dataList = dataBean.getResults();
                        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                        // dbUtils.configAllowTransaction(true);
                        dbUtils.replaceAll(dataList);
                    }

                    // LogUtils.d("Insert " + dataList.size() + " item to stock database success!");
                    return dataBean.getLast_datetime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            protected void afterParseData(String object) {
                if (!TextUtils.isEmpty(object)) {
                    PortfolioPreferenceManager.saveValue(PortfolioPreferenceManager.KEY_LAST_LOAD_DATETIME, object);
                    // PortfolioPreferenceManager.setLoadSearchStock();
                }

            }
        });
        // DKHSClient.requestByGet(DKHSUrl.StockSymbol.profile + "?symbol_type=3,5", null,
        // new ParseHttpListener<Boolean>() {
        //
        // @Override
        // protected Boolean parseDateTask(String jsonData) {
        // List<SearchFundsBean> dataList = new ArrayList<SearchFundsBean>();
        // Type listType = new TypeToken<List<SearchFundsBean>>() {
        // }.getType();
        // dataList = DataParse.parseJsonList(jsonData, listType);
        //
        // DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
        // // dbUtils.configAllowTransaction(true);
        // try {
        // dbUtils.replaceAll(dataList);
        // LogUtils.d("Insert " + dataList.size() + " item to funds database success!");
        // return true;
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return false;
        // }
        //
        // @Override
        // protected void afterParseData(Boolean object) {
        // // if (object) {
        // //
        // // PortfolioPreferenceManager.setLoadSearchStock();
        // // }
        //
        // }
        // });
    }

    public void searchStock(String key) {

        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
        // dbUtils.findById(SearchStockBean.class, key);
        List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
        try {
            // List<SearchStockBean> searchStockList = dbUtils.findAll(Selector.from(SearchStockBean.class)
            // .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
            // .or("chi_spell", "LIKE", "%" + key + "%").and("symbol_type", "=", "1").and("is_stop", "!=", "1"));
            List<SearchStockBean> searchStockList = dbUtils.findAll(Selector
                    .from(SearchStockBean.class)
                    .where("symbol_type", "=", "1")
                    .and(WhereBuilder.b("is_stop", "!=", "1"))
                    .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                            .or("stock_code", "LIKE", "%" + key + "%").or("chi_spell", "LIKE", "%" + key + "%")));
            if (null != searchStockList) {
                for (SearchStockBean searchBean : searchStockList) {
                    selectStockList.add(SelectStockBean.copy(searchBean));
                }
                LogUtils.d(" searchStockDataList size:" + selectStockList.size());
            } else {

                LogUtils.d(" searchStockDataList is null");
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        iLoadListener.loadFinish(selectStockList);
    }

    public void searchFunds(String key) {
        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
        // dbUtils.findById(SearchStockBean.class, key);
        List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
        try {
            // List<SearchStockBean> searchStockList =
            // dbUtils.findAll(Selector.from(SearchStockBean.class).where(whereBuilder)
            // .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
            // .or("chi_spell", "LIKE", "%" + key + "%").and("symbol_type", "=", "3"));
            List<SearchStockBean> searchStockList = dbUtils.findAll(Selector
                    .from(SearchStockBean.class)
                    .where("symbol_type", "in", new String[] { "3", "5" })
                    .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                            .or("stock_code", "LIKE", "%" + key + "%").or("chi_spell", "LIKE", "%" + key + "%")));
            if (null != searchStockList) {
                for (SearchStockBean searchBean : searchStockList) {
                    selectStockList.add(SelectStockBean.copy(searchBean));
                }
                LogUtils.d(" searchfundDataList size:" + selectStockList.size());
            } else {

                LogUtils.d(" searchFundDataList is null");
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        iLoadListener.loadFinish(selectStockList);
    }

    public void searchStockAndIndex(String key) {
        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
        // dbUtils.findById(SearchStockBean.class, key);
        List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
        try {
            // List<SearchFundsBean> searchStockList = dbUtils.findAll(Selector.from(SearchFundsBean.class)
            // .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
            // .or("chi_spell", "LIKE", "%" + key + "%").and("symbol_type", "=", "1,5"));

            List<SearchStockBean> searchStockList = dbUtils.findAll(Selector
                    .from(SearchStockBean.class)
                    .where("symbol_type", "in", new String[] { "1", "5" })
                    .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                            .or("stock_code", "LIKE", "%" + key + "%").or("chi_spell", "LIKE", "%" + key + "%")));

            if (null != searchStockList) {
                for (SearchStockBean searchBean : searchStockList) {
                    selectStockList.add(SelectStockBean.copy(searchBean));
                }
                LogUtils.d(" searchfundDataList size:" + selectStockList.size());
            } else {

                LogUtils.d(" searchFundDataList is null");
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        iLoadListener.loadFinish(selectStockList);
    }

    public SearchStockEngineImpl(ILoadDataBackListener loadListener) {
        this.iLoadListener = loadListener;
    }

    private ILoadDataBackListener iLoadListener;

    public void setLoadDataBackListener(ILoadDataBackListener backListener) {
        this.iLoadListener = backListener;
    }

}
