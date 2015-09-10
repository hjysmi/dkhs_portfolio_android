package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;

/**
 * Created by zjz on 2015/9/10.
 */
public abstract class DKCursorLoaderCallbacks<T> extends SimpleCursorLoaderCallbacks implements ICursorCreate {

    public DKCursorLoaderCallbacks(Context context, Class<T> entityType, IResultCallback resultCallback) {
        super(context, entityType, resultCallback);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new DKDataLoader(context, this.createCusor(args));
    }

}
