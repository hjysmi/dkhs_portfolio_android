package com.dkhs.portfolio.engine.LocalDataEngine;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.IResultCallback;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.VisitorCursorCreateImpl;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.VisitorLoaderCallbacks;

/**
 * Created by zjz on 2015/9/10.
 */
public class VisitorDataSource {

    public static void getOptionalStockList(FragmentActivity activity, IResultCallback resultCallback) {
//        activity.getSupportLoaderManager()
//                .initLoader(0, args, new StockLoaderCallbacks<SelectStockBean>(activity, resultCallback));
        Bundle args = new Bundle();
        args.putInt(VisitorCursorCreateImpl.TYPE, VisitorCursorCreateImpl.VALUE_A);
        activity.getSupportLoaderManager().initLoader(0, args, new VisitorLoaderCallbacks(activity, resultCallback));
    }


//    static class StockLoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<Cursor> {
//        private Context context;
//        private ResultCallback resultCallback;
//
//        public StockLoaderCallbacks(Context context, ResultCallback resultCallback) {
//            this.context = context;
//            this.resultCallback = resultCallback;
//        }
//
//
//        @Override
//        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//            //args.getBoolean(EXTRA_SHOW_GIF, false);
//            return new DKDataLoader(context);
//        }
//
//        @Override
//        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//            if (data == null) return;
//
//            List<T> result = new ArrayList<T>();
//            while (data.moveToNext()) {
//                T entity = (T) CursorUtils.getEntity(AppConfig.getDBUtils(), data, SelectStockBean.class, CursorUtils.FindCacheSequence.getSeq());
//                result.add(entity);
//            }
//
//            if (resultCallback != null) {
//                resultCallback.onResultCallback(result);
//            }
//
//        }
//
//        @Override
//        public void onLoaderReset(Loader<Cursor> loader) {
//
//        }
//    }
//
//    public interface ResultCallback<T> {
//        void onResultCallback(List<T> stockList);
//    }
}
