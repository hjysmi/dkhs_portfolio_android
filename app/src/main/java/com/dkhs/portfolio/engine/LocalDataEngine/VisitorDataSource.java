package com.dkhs.portfolio.engine.LocalDataEngine;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.IResultCallback;
import com.dkhs.portfolio.engine.LocalDataEngine.DBLoader.LoaderHelper;

/**
 * Created by zjz on 2015/9/10.
 */
public class VisitorDataSource {

    public static void getOptionalStockList(FragmentActivity activity, IResultCallback resultCallback) {
        Bundle args = new Bundle();
        args.putInt(VisitorCursorCreateImpl.TYPE, VisitorCursorCreateImpl.VALUE_A);
        LoaderHelper.initCursorLoader(activity, args, resultCallback);
    }
    public static void getOptionalCombinationList(FragmentActivity activity, IResultCallback resultCallback) {
        Bundle args = new Bundle();
        args.putInt(VisitorCursorCreateImpl.TYPE, VisitorCursorCreateImpl.VALUE_B);
        LoaderHelper.initCursorLoader(activity, args, resultCallback);
    }

}
