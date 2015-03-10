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
import com.dkhs.portfolio.bean.SelectStockBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

/**
 * @ClassName VisitorDataEngine
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-3-4 上午10:46:37
 * @version 1.0
 */
public class VisitorDataEngine {
    // public VisitorDataEngine()

    public void saveOptionalStock(SelectStockBean stockbean) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.save(stockbean);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void replaceOptionStock(List<SelectStockBean> stockList) {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        try {
            db.replaceAll(stockList);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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

    public List<SelectStockBean> getOptionalStockListBySort() {
        DbUtils db = DbUtils.create(PortfolioApplication.getInstance());
        List<SelectStockBean> list = Collections.EMPTY_LIST;
        try {
            // list = db.findAll(SelectStockBean.class);
            list = db.findAll(Selector.from(SelectStockBean.class).orderBy("sortId",true));

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 通过类型查找
        return list;
    }

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

    // public void

}
