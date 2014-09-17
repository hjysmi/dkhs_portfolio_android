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

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * @ClassName SearchStockEngineImpl
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-17 下午3:15:01
 * @version 1.0
 */
public class SearchStockEngineImpl {
    public boolean saveStockList() {
        List<SearchStockBean> dataList = new ArrayList<SearchStockBean>();
        for (int i = 0; i < 200; i++) {
            SearchStockBean bean = new SearchStockBean();
            bean.setStockId(101000001 + i);
            bean.setStockCode((666000 + i) + "");
            bean.setStockName("数据股" + i);
            dataList.add(bean);
        }
        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
        // dbUtils.configAllowTransaction(true);
        try {
            dbUtils.saveAll(dataList);
            return true;
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void searchStock(String key) {
        DbUtils dbUtils = DbUtils.create(PortfolioApplication.getInstance());
        // dbUtils.findById(SearchStockBean.class, key);
        try {
            List<SearchStockBean> searchStockList = dbUtils.findAll(Selector.from(SearchStockBean.class).where(
                    "stock_name", "LIKE", "%" + key + "%"));
            if (null != searchStockList) {
                System.out.println(" searchStockList size:" + searchStockList.size());
            } else {

                System.out.println(" searchStockList is null");
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
