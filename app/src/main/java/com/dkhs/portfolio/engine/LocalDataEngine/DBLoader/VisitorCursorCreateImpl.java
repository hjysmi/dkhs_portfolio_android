package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.database.Cursor;
import android.os.Bundle;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by zjz on 2015/9/10.
 */
public class VisitorCursorCreateImpl implements ICursorCreate {

    public static final String TYPE = "key_type";
    public static final int VALUE_A = 101;
    public static final int VALUE_B = 102;
    public static final int VALUE_C = 103;

    private Cursor getOptionalStockCursor() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            return db.findAllCursor(SelectStockBean.class);
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
            return db.findAllCursor(SelectStockBean.class);
        } catch (DbException e) {

        }
        return null;
    }

    private Cursor getCursorC() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            return db.findAllCursor(SelectStockBean.class);
        } catch (DbException e) {

        }
        return null;
    }

    @Override
    public Cursor createCusor(Bundle args) {
        if (null != args) {
            int typeIndex = args.getInt(TYPE, -1);
            LogUtils.d("createCusor typeIndex:" + typeIndex);
            if (typeIndex == VALUE_A) {
                return getOptionalStockCursor();
            }
        }
        return null;
    }
}
