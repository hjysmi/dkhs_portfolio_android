package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

/**
 * Created by zjz on 2015/6/13.
 */
public abstract class VisiableLoadFragment extends BaseFragment {

    private static final String TAG = VisiableLoadFragment.class.getSimpleName();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewShown && getUserVisibleHint()) {
            isViewShown = true;
            requestData();
        }
    }


    public abstract void requestData();


    private boolean isViewShown;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        Log.e(TAG, this + "=============> setUserVisibleHint isVisibleToUser:" + isVisibleToUser + " isViewShown:" + isViewShown);
        if (isVisibleToUser && isViewShown) {
            onViewShow();
        } else {
            onViewHide();
        }

        if (isVisibleToUser && !isViewShown) {

            if (getView() != null) {
                isViewShown = true;
                requestData();
                onViewShow();
            } else {
                isViewShown = false;
            }
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, this + "=============> onHiddenChanged" + hidden);
        if (hidden) {
            //do when hidden
        } else {
            //do when show
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e(TAG, this + "=============> onResume");
        if (getUserVisibleHint()) {
            onViewShow();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        onViewHide();
//        Log.e(TAG, this + "=============> onPause");
    }


    public void onViewShow() {
//        Log.e(TAG, this + "=============> onViewShow");

    }

    public void onViewHide() {

//        Log.e(TAG, this + "=============> onViewHide");

    }
}
