/**
 * @Title VisitorDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-4 上午10:46:37
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import java.util.Collections;
import java.util.List;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

/**
 * @ClassName VisitorDataEngine
 * @Description 游客模式的本地数据引擎
 * @author zjz
 * @date 2015-3-4 上午10:46:37
 * @version 1.0
 */
public class VisitorDataEngine {
    // public VisitorDataEngine()

    /**
     * 添加自选股到本地
     */
    public void saveOptionalStock(SelectStockBean stockbean) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.save(stockbean);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 添加自选股到本地
     */
    public void saveCombination(CombinationBean combean) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.save(combean);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 修改股票列表数据
     */
    public void replaceOptionStock(List<SelectStockBean> stockList) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.replaceAll(stockList);
        } catch (DbException e) {
            // TODO Auto-generated catch block
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
            list = db.findAll(Selector.from(SelectStockBean.class).orderBy("sortId", false));

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

}