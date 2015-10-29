package com.dkhs.portfolio.engine.LocalDataEngine;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.bean.SearchHistoryBean;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.ICursorCreate;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

/**
 * Created by zjz on 2015/9/10.
 */
public class VisitorCursorCreateImpl implements ICursorCreate {

    public static final String TYPE = "key_type";
    public static final String TYPE_SEARCH = "key_searchkey";
    public static final int VALUE_A = 101;
    public static final int VALUE_B = 102;
    public static final int VALUE_C = 103;
    public static final int VALUE_SEARCH_STOCK = 104;
    public static final int VALUE_SEARCH_FUNDS = 105;
    public static final int VALUE_SEARCH_STOCKINDEXFUNDS = 106;
    public static final int VALUE_SEARCH_HISTORYSTOCK = 107;

    private Cursor getOptionalStockCursor() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            return db.findSelectorAllCursor(Selector.from(SelectStockBean.class).limit(20));
        } catch (DbException e) {

        }
        return null;
    }

    private Cursor getCursorA() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            return db.findAllCursor(SelectStockBean.class);
        } catch (DbException e) {

        }
        return null;
    }

    private Cursor getCursorB() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            return db.findSelectorAllCursor(Selector.from(SelectStockBean.class).limit(20));
        } catch (DbException e) {

        }
        return null;
    }

    private Cursor getCursorC() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            return db.findSelectorAllCursor(Selector.from(SelectStockBean.class).limit(20));
        } catch (DbException e) {

        }
        return null;
    }

    private Cursor searchStock(String key) {
        DbUtils dbUtils = AppConfig.getDBUtils();
        try {
            // List<SearchStockBean> searchStockList = dbUtils.findAll(Selector.from(SearchStockBean.class)
            // .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
            // .or("chi_spell", "LIKE", "%" + key + "%").and("symbol_type", "=", "1").and("is_stop", "!=",
            // "1"));
            return dbUtils
                    .findSelectorAllCursor(Selector.from(SearchStockBean.class)
                            .where("symbol_type", "=", "1")
                                    // .and(WhereBuilder.b("is_stop", "!=", "1"))
                            .and(WhereBuilder.b("list_status", "!=", "2"))
                            .and(WhereBuilder.b("list_status", "!=", "3"))
                            .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                                    .or("stock_code", "LIKE", "%" + key + "%")
                                    .or("stock_symbol", "LIKE", "%" + key + "%")
                                    .or("chi_spell", "LIKE", "%" + key + "%"))
                            .limit(20));
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Cursor searchFunds(String key) {

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
        DbUtils dbUtils = AppConfig.getDBUtils();
        try {
            // List<SearchStockBean> searchStockList = dbUtils.findAll(Selector.from(SearchStockBean.class)
            // .where("stock_name", "LIKE", "%" + key + "%").or("stock_code", "LIKE", "%" + key + "%")
            // .or("chi_spell", "LIKE", "%" + key + "%").and("symbol_type", "=", "1").and("is_stop", "!=",
            // "1"));
            return dbUtils
                    .findSelectorAllCursor(Selector
                            .from(SearchStockBean.class)
                            .where("symbol_type", "in", new String[]{"3", "5"})
                            .and("symbol_stype", "in", new String[]{"300", "303"})
                            .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                                    .or("stock_code", "LIKE", "%" + key + "%")
                                    .or("stock_symbol", "LIKE", "%" + key + "%")
                                    .or("chi_spell", "LIKE", "%" + key + "%"))
                            .limit(20));
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    private Cursor searchStockIndexFunds(String key) {
        DbUtils dbUtils = AppConfig.getDBUtils();
        try {
            return dbUtils
                    .findSelectorAllCursor(Selector
                            .from(SearchStockBean.class)
                            .where("symbol_type", "in", new String[]{"1", "5", "3"})
                            .and(WhereBuilder.b("stock_name", "LIKE", "%" + key + "%")
                                    .or("stock_code", "LIKE", "%" + key + "%")
                                    .or("stock_symbol", "LIKE", "%" + key + "%")
                                    .or("chi_spell", "LIKE", "%" + key + "%"))
                            .limit(20));

        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private Cursor searchHistoryStock() {
        DbUtils dbUtils = AppConfig.getDBUtils();
        try {
            return dbUtils
                    .findSelectorAllCursor(Selector.from(SearchHistoryBean.class).orderBy("saveTime", true)
                                    .limit(20)
                    );
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Cursor createCusor(Bundle args) {

        if (null != args) {
            int typeIndex = args.getInt(TYPE, -1);
            String searchKey = args.getString(TYPE_SEARCH);
            if (typeIndex == VALUE_A) {
                return getOptionalStockCursor();
            } else if (typeIndex == VALUE_SEARCH_STOCK && !TextUtils.isEmpty(searchKey)) {

                return searchStock(searchKey);

            } else if (typeIndex == VALUE_SEARCH_FUNDS && !TextUtils.isEmpty(searchKey)) {
                return searchFunds(searchKey);

            } else if (typeIndex == VALUE_SEARCH_STOCKINDEXFUNDS && !TextUtils.isEmpty(searchKey)) {
                return searchStockIndexFunds(searchKey);
            } else if (typeIndex == VALUE_SEARCH_HISTORYSTOCK) {
                return searchHistoryStock();
            }

        }
        return null;
    }

    @Override
    public Class getCursorClass(Bundle args) {
        if (null != args) {
            int typeIndex = args.getInt(TYPE, -1);
            if (typeIndex == VALUE_A) {
                return SelectStockBean.class;
            } else if (typeIndex == VALUE_SEARCH_FUNDS || typeIndex == VALUE_SEARCH_STOCK || typeIndex == VALUE_SEARCH_STOCKINDEXFUNDS) {
                return SearchStockBean.class;
            } else if (typeIndex == VALUE_SEARCH_HISTORYSTOCK) {
                return SearchHistoryBean.class;
            }

        }
        return null;
    }
}
