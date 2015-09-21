package com.dkhs.portfolio.engine.LocalDataEngine.DBLoader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by zjz on 2015/9/10.
 * ListLoader.java   //https://github.com/casidiablo/groundy/blob/d60a51760582b8a03ea9a06ea7ca938d3785ea15/library/src/main/java/com/telly/groundy/loader/ListLoader.java
 */
public abstract class SimpleCursorLoader extends AsyncTaskLoader<Cursor> {
    private Cursor mCursor;
    private final ForceLoadContentObserver mObserver = new ForceLoadContentObserver();

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = loadCursorBackgroud(); // get your cursor from wherever you like

        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
            cursor.registerContentObserver(mObserver);
        }

        return cursor;
    }

    public SimpleCursorLoader(Context context) {
        super(context);
    }

    /* Runs on a worker thread */
    public abstract Cursor loadCursorBackgroud();

    /* Runs on the UI thread */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p/>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }
}
