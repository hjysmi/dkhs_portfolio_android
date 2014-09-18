/**
 * @Title SearchStockEngineImpl.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-17 下午3:15:01
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LoadSelectDataEngine.ILoadDataBackListener;
import com.dkhs.portfolio.net.DKHSClient;
import com.dkhs.portfolio.net.DKHSUrl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.PortfolioPreferenceManager;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
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
        DKHSClient.requestByGet(DKHSUrl.StockSymbol.profile, null, new ParseHttpListener<Boolean>() {

            @Override
            protected Boolean parseDateTask(String jsonData) {
                List<SearchStockBean> dataList = new ArrayList<SearchStockBean>();
                Type listType = new TypeToken<List<SearchStockBean>>() {
                }.getType();
                dataList = DataParse.parseJsonList(jsonData, listType);
                System.out.println("dataList size:" + dataList.size());

                System.out.println(" dataList.get(1).getId():" + dataList.get(1).getId());
                DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
                // dbUtils.configAllowTransaction(true);
                try {
                    dbUtils.saveAll(dataList);
                    LogUtils.d("Insert " + dataList.size() + " item to stock database success!");
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void afterParseData(Boolean object) {
                if (object) {

                    PortfolioPreferenceManager.setLoadSearchStock();
                }

            }
        });
    }

    // public boolean saveStockList() {
    // List<SearchStockBean> dataList = new ArrayList<SearchStockBean>();
    // for (int i = 0; i < 200; i++) {
    // SearchStockBean bean = new SearchStockBean();
    // bean.setStockId(101000001 + i);
    // if (i < 10) {
    //
    // bean.setStockCode((777000 + i) + "");
    // } else {
    // bean.setStockCode((666000 + i) + "");
    // }
    // bean.setStockName("数据股" + i);
    // dataList.add(bean);
    // }
    // DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
    // // dbUtils.configAllowTransaction(true);
    // try {
    // dbUtils.saveAll(dataList);
    // return true;
    // } catch (DbException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return false;
    // }

    public void searchStock(String key) {
        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
        // dbUtils.findById(SearchStockBean.class, key);
        List<SelectStockBean> selectStockList = new ArrayList<SelectStockBean>();
        try {
            List<SearchStockBean> searchStockList = dbUtils.findAll(Selector.from(SearchStockBean.class)
                    .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
                    .or("chi_spell", "LIKE", "%" + key + "%"));
            if (null != searchStockList) {
                for(SearchStockBean searchBean :searchStockList){
                    selectStockList.add(SelectStockBean.copy(searchBean));
                }
                LogUtils.d(" searchStockList size:" + selectStockList.size());
            } else {

                LogUtils.d(" searchStockList is null");
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
