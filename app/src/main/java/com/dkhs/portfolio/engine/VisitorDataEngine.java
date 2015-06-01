/**
 * @Title VisitorDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-4 上午10:46:37
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

import java.util.Collections;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName VisitorDataEngine
 * @Description 游客模式的本地数据引擎
 * @date 2015-3-4 上午10:46:37
 */
public class VisitorDataEngine {
    // public VisitorDataEngine()

    /**
     * 添加自选股到本地
     */
    public void saveOptionalStock(final SelectStockBean stockbean) {
        new Thread() {
            public void run() {
                DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
                try {
                    db.saveOrUpdate(stockbean);
                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    /**
     * 添加自选股到本地
     */
    public void saveCombination(final CombinationBean combean) {
        new Thread() {
            public void run() {
                DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
                try {
                    db.saveOrUpdate(combean);
                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            ;
        }.start();

    }

    /**
     * 修改股票列表数据
     */
    public void replaceOptionStock(List<SelectStockBean> stockList) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.replaceAll(stockList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改组合列表数据
     */
    public void replaceCombination(List<CombinationBean> comList) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.replaceAll(comList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询股票列表数据
     */
    public List<SelectStockBean> getOptionalStockList() {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        List<SelectStockBean> list = Collections.EMPTY_LIST;
        try {
            list = db.findAll(SelectStockBean.class);

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 通过类型查找
        return list;
    }

    /**
     * 查询排序的股票列表数据
     */
    public List<SelectStockBean> getOptionalStockListBySort() {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        List<SelectStockBean> list = Collections.EMPTY_LIST;
        try {
            // list = db.findAll(SelectStockBean.class);
            list = db.findAll(Selector.from(SelectStockBean.class).where("symbol_type", "in", new String[]{"1","5"}).orderBy("sortId", false));

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 通过类型查找

        return list;
    }

    /**
     * 查询排序的基金列表数据
     */
    public List<SelectStockBean> getOptionalFundsSort() {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        List<SelectStockBean> list = Collections.EMPTY_LIST;
        try {
            // list = db.findAll(SelectStockBean.class);
            list = db.findAll(Selector.from(SelectStockBean.class).where("symbol_type", "in", new String[]{"3",}).orderBy("sortId", false));

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 通过类型查找

        return list;
    }

    /**
     * 删除组合数据
     */
    public void delCombinationBean(CombinationBean comBean) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.delete(comBean);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 删除股票数据
     */
    public void delOptionalStock(SelectStockBean stockbean) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            System.out.println("delOptionalStock :" + stockbean.getName());
            db.delete(stockbean);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void delAllOptionalStock() {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.deleteAll(SelectStockBean.class);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void delAllCombinationBean() {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.deleteAll(CombinationBean.class);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public CombinationBean queryCombination(String comId) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        CombinationBean comBean = null;
        try {
            // db.findDbModelAll(selector)
            comBean = db.findById(CombinationBean.class, comId);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return comBean;
    }

    /**
     * 查询排序的组合列表数据
     */
    public List<CombinationBean> getCombinationBySort() {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        List<CombinationBean> list = Collections.EMPTY_LIST;
        try {
            // list = db.findAll(SelectStockBean.class);
            list = db.findAll(Selector.from(CombinationBean.class).orderBy("sortId"));

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 通过类型查找
        return list;
    }

    public String getStockSymbols() {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        StringBuilder sbSymbols = new StringBuilder();
        try {
            // db.findDbModelAll(selector)
            List<DbModel> dbModels = db.findDbModelAll(Selector.from(SelectStockBean.class).select("code"));
            for (DbModel codeColum : dbModels) {
                String code = codeColum.getString("code");
                sbSymbols.append(code);
                sbSymbols.append(",");
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sbSymbols.toString();
    }

    public boolean uploadUserFollowStock(IHttpListener listener) {
        List<SelectStockBean> dataList = getOptionalStockListBySort();
        StringBuilder sbIds = new StringBuilder();
        if (null != dataList && !dataList.isEmpty()) {
            for (SelectStockBean stock : dataList) {
                sbIds.append(stock.id);
                sbIds.append(",");
            }
            new QuotesEngineImpl().symbolFollows(sbIds.substring(0, sbIds.length() - 1), listener);
            return true;
        } else {
            return false;
        }

    }

    public boolean uploadUserFollowCombination(IHttpListener listener) {
        List<CombinationBean> dataList = getCombinationBySort();
        StringBuilder sbIds = new StringBuilder();
        if (null != dataList && !dataList.isEmpty()) {
            for (CombinationBean stock : dataList) {
                sbIds.append(stock.getId());
                sbIds.append(",");
            }
            new FollowComEngineImpl().followCombinations(sbIds.substring(0, sbIds.length() - 1), listener);
            return true;
        } else {
            return false;
        }
    }

}
