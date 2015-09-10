package com.dkhs.portfolio.engine.LocalDataEngine;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.VisitorDataLoader;
import com.lidroid.xutils.db.sqlite.CursorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjz on 2015/9/10.
 */
public class VisitorDataSource {

    public static void getOptionalStockList(FragmentActivity activity, Bundle args, ResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new StockLoaderCallbacks<SelectStockBean>(activity, resultCallback));
    }

    static class StockLoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context context;
        private ResultCallback resultCallback;

        public StockLoaderCallbacks(Context context, ResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
        }


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            //args.getBoolean(EXTRA_SHOW_GIF, false);
            return new VisitorDataLoader(context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) return;

            List<T> result = new ArrayList<T>();
            while (data.moveToNext()) {
                T entity = (T) CursorUtils.getEntity(AppConfig.getDBUtils(), data, SelectStockBean.class, CursorUtils.FindCacheSequence.getSeq());
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

    public interface ResultCallback<T> {
        void onResultCallback(List<T> stockList);
    }
}
