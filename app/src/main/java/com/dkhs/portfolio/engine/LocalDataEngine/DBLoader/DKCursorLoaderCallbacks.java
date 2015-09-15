package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.lidroid.xutils.util.LogUtils;

/**
 * Created by zjz on 2015/9/10.
 */
public abstract class DKCursorLoaderCallbacks<T> extends SimpleCursorLoaderCallbacks implements ICursorCreate {

    public DKCursorLoaderCallbacks(Context context, IResultCallback resultCallback) {
        super(context, resultCallback);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        LogUtils.d("DKCursorLoaderCallbacks onCreateLoader");
        setEntityType(this.getCursorClass(args));
        return new DKDataLoader(context, this.createCusor(args));
    }

}
