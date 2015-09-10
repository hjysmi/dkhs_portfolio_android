package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.content.Context;
import android.database.Cursor;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * Created by zjz on 2015/9/10.
 */
public class VisitorDataLoader extends SimpleCursorLoader {

    public VisitorDataLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadCursorBackgroud() {
        return getOptionalStockCursor();
    }


    private Cursor getOptionalStockCursor() {
        DbUtils db = AppConfig.getDBUtils();
        try {
            return db.findAllCursor(SelectStockBean.class);
        } catch (DbException e) {

        }
        return null;
    }
}
