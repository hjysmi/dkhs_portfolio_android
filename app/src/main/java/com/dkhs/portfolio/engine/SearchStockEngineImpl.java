/**
 * @Title SearchStockEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午3:15:01
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import android.text.TextUtils;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.MoreDataBean;
import com.dkhs.portfolio.bean.SearchHistoryBean;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockProfileDataBean;
import com.dkhs.portfolio.engine.LoadMoreDataEngine.ILoadDataBackListener;
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

            sbLastDate.append("2015-06-08T09:57:56Z");
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

                // Type listType = new TypeToken<List<SearchStockBean>>() {
                // }.getType();
                StockProfileDataBean dataBean = DataParse.parseObjectJson(StockProfileDataBean.class, jsonData);
                // dataList = DataParse.parseJsonList(jsonData, listType);
                if (null != dataBean) {

                    List<SearchStockBean> dataList = dataBean.getResults();
                    DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
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

    public void searchStock(final String key) {

        new Thread() {
            public void run() {

                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                // dbUtils.findById(SearchStockBean.class, key);
                List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
                try {
                    // List<SearchStockBean> searchStockList = dbUtils.findAll(Selector.from(SearchStockBean.class)
                    // .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
                    // .or("chi_spell", "LIKE", "%" + key + "%").and("symbol_type", "=", "1").and("is_stop", "!=",
                    // "1"));
                    List<SearchStockBean> searchStockList = dbUtils
                            .findAll(Selector.from(SearchStockBean.class)
                                    .where("symbol_type", "=", "1")
                                            // .and(WhereBuilder.b("is_stop", "!=", "1"))
                                    .and(WhereBuilder.b("list_status", "!=", "2"))
                                    .and(WhereBuilder.b("list_status", "!=", "3"))
                                    .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                                            .or("stock_code", "LIKE", "%" + key + "%")
                                            .or("stock_symbol", "LIKE", "%" + key + "%")
                                            .or("chi_spell", "LIKE", "%" + key + "%")));
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
                setSearchBack(selectStockList);

            }

            ;
        }.start();

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

    public void searchFunds(final String key) {
        new Thread() {
            public void run() {
                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                // dbUtils.findById(SearchStockBean.class, key);
                List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
                try {
                    // List<SearchStockBean> searchStockList =
                    // dbUtils.findAll(Selector.from(SearchStockBean.class).where(whereBuilder)
                    // .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
                    // .or("chi_spell", "LIKE", "%" + key + "%").and("symbol_type", "=", "3"));


                    /**
                     * (300, '股票'),
                     (301, '混合型基金'),
                     (302, '债卷型基金'),
                     (303, '指数型基金'),
                     (304, '保本型基金'),
                     (305, 'qdii'),
                     (306, 'etf'),
                     (307, 'lof'),
                     (308, 'other'),
                     */
                    List<SearchStockBean> searchStockList = dbUtils
                            .findAll(Selector
                                    .from(SearchStockBean.class)
                                    .where("symbol_type", "in", new String[]{"3", "5"})
                                    .and("symbol_stype", "in", new String[]{"300", "303"})
                                    .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                                            .or("stock_code", "LIKE", "%" + key + "%")
                                            .or("stock_symbol", "LIKE", "%" + key + "%")
                                            .or("chi_spell", "LIKE", "%" + key + "%")));
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

                setSearchBack(selectStockList);
            }

            ;
        }.start();

    }

//    public void searchStockAndIndex(final String key) {
//        new Thread() {
//            public void run() {
//                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
//                // dbUtils.findById(SearchStockBean.class, key);
//                List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
//                try {
//                    // List<SearchFundsBean> searchStockList = dbUtils.findAll(Selector.from(SearchFundsBean.class)
//                    // .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
//                    // .or("chi_spell", "LIKE", "%" + key + "%").and("symbol_type", "=", "1,5"));
//
//                    List<SearchStockBean> searchStockList = dbUtils
//                            .findAll(Selector
//                                    .from(SearchStockBean.class)
//                                    .where("symbol_type", "in", new String[] { "1", "5" })
//                                    .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
//                                            .or("stock_code", "LIKE", "%" + key + "%")
//                                            .or("chi_spell", "LIKE", "%" + key + "%")));
//
//                    if (null != searchStockList) {
//                        for (SearchStockBean searchBean : searchStockList) {
//                            selectStockList.add(SelectStockBean.copy(searchBean));
//                        }
//                        LogUtils.d(" searchfundDataList size:" + selectStockList.size());
//                    } else {
//
//                        LogUtils.d(" searchFundDataList is null");
//                    }
//                } catch (DbException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                MoreDataBean moreDataBean = new MoreDataBean<SearchStockBean>();
//                moreDataBean.setCurrentPage(1);
//                moreDataBean.setResults(selectStockList);
//                moreDataBean.setTotalCount(selectStockList.size());
//                moreDataBean.setTotalPage(1);
//                iLoadListener.loadFinish(moreDataBean);
//            };
//        }.start();
//    }

    public void searchStockIndexFunds(final String key) {
        new Thread() {
            public void run() {
                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                // dbUtils.findById(SearchStockBean.class, key);
                List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
                try {

                    List<SearchStockBean> searchStockList = dbUtils
                            .findAll(Selector
                                    .from(SearchStockBean.class)
                                    .where("symbol_type", "in", new String[]{"1", "5", "3"})
                                    .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                                            .or("stock_code", "LIKE", "%" + key + "%")
                                            .or("stock_symbol", "LIKE", "%" + key + "%")
                                            .or("chi_spell", "LIKE", "%" + key + "%")));

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
                setSearchBack(selectStockList);
            }

            ;
        }.start();
    }

    public void searchHistoryStock() {
        new Thread() {
            public void run() {
                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                // dbUtils.findById(SearchStockBean.class, key);
                List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
                try {

                    List<SearchHistoryBean> searchStockList = dbUtils
                            .findAll(Selector.from(SearchHistoryBean.class).orderBy("saveTime", true)
                                            .limit(20)
                            );

                    if (null != searchStockList) {
                        for (SearchHistoryBean searchBean : searchStockList) {
                            selectStockList.add(SelectStockBean.copy(searchBean));
                        }
                        LogUtils.d(" searchHistoryStock size:" + selectStockList.size());
                    } else {

                        LogUtils.d(" searchHistoryStock is null");
                    }
                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                setSearchBack(selectStockList);
            }

            ;
        }.start();
    }

    public SearchStockEngineImpl(ILoadDataBackListener loadListener) {
        this.iLoadListener = loadListener;
    }

    private ILoadDataBackListener iLoadListener;

    public void setLoadDataBackListener(ILoadDataBackListener backListener) {
        this.iLoadListener = backListener;
    }

}
