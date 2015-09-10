package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.dkhs.portfolio.bean.SelectStockBean;

/**
 * Created by zjz on 2015/9/10.
 */
public class VisitorLoaderCallbacks extends DKCursorLoaderCallbacks {

    public VisitorLoaderCallbacks(Context context, IResultCallback resultCallback) {
        super(context, SelectStockBean.class, resultCallback);
    }


    @Override
    public Cursor createCusor(Bundle args) {
        return new VisitorCursorCreateImpl().createCusor(args);
    }

}
