package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by zjz on 2015/9/10.
 */
public class DKDataLoader extends SimpleCursorLoader {

    public DKDataLoader(Context context, Cursor cursorCreate) {
        super(context);
        this.cursorCreate = cursorCreate;
    }

    @Override
    public Cursor loadCursorBackgroud() {
        if (null != cursorCreate)
            return cursorCreate;
        return null;
    }


    private Cursor cursorCreate;


}
