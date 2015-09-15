package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.dkhs.portfolio.app.AppConfig;
import com.lidroid.xutils.db.sqlite.CursorUtils;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/9/10.
 */
public abstract class SimpleCursorLoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<Cursor> {

    protected Context context;
    private IResultCallback resultCallback;

    public Class<T> getEntityType() {
        return entityType;
    }

    public void setEntityType(Class<T> entityType) {
        this.entityType = entityType;
    }

    private Class<T> entityType;

    public SimpleCursorLoaderCallbacks(Context context, IResultCallback resultCallback) {
        this.context = context;
        this.resultCallback = resultCallback;
//        this.entityType = entityType;
    }


//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        LogUtils.d("SimpleCursorLoaderCallbacks onLoadFinished ");

        if (data == null) return;
        LogUtils.d(" onLoadFinished Cursor size：" + data.getCount());
        List<T> result = new ArrayList<T>();
        while (data.moveToNext()) {
            T entity = (T) CursorUtils.getEntity(AppConfig.getDBUtils(), data, entityType, CursorUtils.FindCacheSequence.getSeq());
            result.add(entity);
        }

        if (resultCallback != null) {
            resultCallback.onResultCallback(result);
        }

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
