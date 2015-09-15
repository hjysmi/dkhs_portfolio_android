package com.dkhs.portfolio.engine.LocalDataEngine;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.DKCursorLoaderCallbacks;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.IResultCallback;

/**
 * Created by zjz on 2015/9/10.
 */
public class VisitorLoaderCallbacks extends DKCursorLoaderCallbacks {
    VisitorCursorCreateImpl visitorCursorCreate;

    public VisitorLoaderCallbacks(Context context, IResultCallback resultCallback) {
        super(context, resultCallback);
        visitorCursorCreate = new VisitorCursorCreateImpl();
    }


    @Override
    public Cursor createCusor(Bundle args) {
        return visitorCursorCreate.createCusor(args);
    }

    @Override
    public Class<?> getCursorClass(Bundle args) {
        return visitorCursorCreate.getCursorClass(args);
    }
}
