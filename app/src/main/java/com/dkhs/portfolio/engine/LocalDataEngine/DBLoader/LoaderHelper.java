package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;

import com.dkhs.portfolio.engine.LocalDataEngine.VisitorCursorCreateImpl;
import com.dkhs.portfolio.engine.LocalDataEngine.VisitorLoaderCallbacks;

/**
 * Created by zjz on 2015/9/10.
 */
public class LoaderHelper {

    public static void initCursorLoader(FragmentActivity activity, Bundle bundle, LoaderManager.LoaderCallbacks<Cursor> callbackscallback) {
        activity.getSupportLoaderManager().initLoader(0, bundle, callbackscallback);
    }

    public static void initCursorLoader(FragmentActivity activity, Bundle bundle, IResultCallback resultCallback) {
        int id = bundle.getInt(VisitorCursorCreateImpl.TYPE, 0);
        activity.getSupportLoaderManager().restartLoader(id, bundle, new VisitorLoaderCallbacks(activity, resultCallback));
    }


}
