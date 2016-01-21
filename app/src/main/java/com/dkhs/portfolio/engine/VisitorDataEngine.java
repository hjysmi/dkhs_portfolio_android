/**
 * @Title VisitorDataEngine.java
 * @Package com.dkhs.portfolio.engine
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-3-4 上午10:46:37
 * @version V1.0
 */
package com.dkhs.portfolio.engine;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SearchHistoryBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.net.IHttpListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
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


    public static void deleteHistory(SearchHistoryBean bean){
        DbUtils db = AppConfig.getDBUtils();
        try {
            db.deleteById(SearchHistoryBean.class,bean.getId());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public static List<SelectStockBean> getHistory(){
        List<SelectStockBean> listAll = new ArrayList<>();
        DbUtils db = AppConfig.getDBUtils();
        try {
            List<SearchHistoryBean> lists = db.findAll(Selector.from(SearchHistoryBean.class).orderBy("saveTime", true));
            if (null != lists) {
                for (SearchHistoryBean searchBean : lists) {
                    listAll.add(SelectStockBean.copy(searchBean));
                }
                LogUtils.d(" searchHistoryStock size:" + lists.size());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return listAll;
    }

    public static void saveHistory(final SearchHistoryBean stockbean) {
        new Thread() {
            public void run() {
                DbUtils db = AppConfig.getDBUtils();
                try {
                    List<SearchHistoryBean> listAll = db.findAll(Selector.from(SearchHistoryBean.class).orderBy("saveTime", true));
                    int i = 0;
                    if(listAll != null && listAll.size() > 0){
                        i = listAll.size() - 1;
                        while(i > 9){
                            db.delete(listAll.get(i));
                            i--;
                        }
                    }
                    SearchHistoryBean his = db.findById(SearchHistoryBean.class, stockbean.getId());
                    stockbean.setSaveTime(System.currentTimeMillis() / 1000);
                    if(his != null){
                        db.delete(listAll.get(i));
                    }
                    db.saveOrUpdate(stockbean);

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    public static void clearHistoryStock() {
        new Thread() {
            public void run() {
                DbUtils db = AppConfig.getDBUtils();
                try {
                    db.deleteAll(SearchHistoryBean.class);
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
    public void saveOptionalStock(final SelectStockBean stockbean) {
        new Thread() {
            public void run() {
                DbUtils db = AppConfig.getDBUtils();
                try {
                    db.saveOrUpdate(stockbean);
//                    db.saveOrUpdate(historyBean);
                } catch (DbException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }.start();
    }

    /**
     * 添加自选股到本地
     */
    public void saveCombination(final CombinationBean combean) {
        new Thread() {
            public void run() {
                DbUtils db = AppConfig.getDBUtils();
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
        DbUtils db = AppConfig.getDBUtils();
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
        DbUtils db = AppConfig.getDBUtils();
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
        DbUtils db = AppConfig.getDBUtils();
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
     * 查询自选基金列表数据
     */
    public List<SelectStockBean> getOptionalFundList() {
        DbUtils db = AppConfig.getDBUtils();
        List<SelectStockBean> list = Collections.EMPTY_LIST;
        try {
            list = db
                    .findAll(Selector
                            .from(SelectStockBean.class)
                            .where("symbol_type", "in", new String[]{"3"}));

        } catch (DbException e) {
            e.printStackTrace();
        }// 通过类型查找
        return list;
    }

    /**
     * 查询排序的股票列表数据
     */
    public List<SelectStockBean> getOptionalStockListBySort() {
        DbUtils db = AppConfig.getDBUtils();
        List<SelectStockBean> list = Collections.EMPTY_LIST;
        try {
            // list = db.findAll(SelectStockBean.class);
            list = db.findAll(Selector.from(SelectStockBean.class).where("symbol_type", "in", new String[]{"1", "5"}).orderBy("sortId", false));

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 通过类型查找

        return list;
    }

    public List<SelectStockBean> getOptionalStockAndFundBySort() {
        DbUtils db = AppConfig.getDBUtils();
        List<SelectStockBean> list = Collections.EMPTY_LIST;
        try {
            list = db.findAll(Selector.from(SelectStockBean.class).where("symbol_type", "in", new String[]{"1", "3,", "5"}));

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
        DbUtils db = AppConfig.getDBUtils();
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
        DbUtils db = AppConfig.getDBUtils();
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
        DbUtils db = AppConfig.getDBUtils();
        try {
            System.out.println("delOptionalStock :" + stockbean.getName());
            db.delete(stockbean);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void delAllOptionalStock() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            db.deleteAll(SelectStockBean.class);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void delAllCombinationBean() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            db.deleteAll(CombinationBean.class);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public CombinationBean queryCombination(String comId) {
        DbUtils db = AppConfig.getDBUtils();
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
        DbUtils db = AppConfig.getDBUtils();
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
        DbUtils db = AppConfig.getDBUtils();
        StringBuilder sbSymbols = new StringBuilder();
        try {
            // db.findDbModelAll(selector)
            List<DbModel> dbModels = db.findDbModelAll(Selector.from(SelectStockBean.class).select("symbol").where("symbol_type","in",new String[]{"1","5"}));
            if(dbModels != null && dbModels.size() > 0){
                for (DbModel codeColum : dbModels) {

                    String symbol = codeColum.getString("symbol");
                    sbSymbols.append(symbol);
                    sbSymbols.append(",");
                }
            }
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sbSymbols.toString();
    }

    public boolean uploadUserFollowStock(IHttpListener listener) {
        List<SelectStockBean> dataList = getOptionalStockList();
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
