package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by zjz on 2015/6/13.
 */
public abstract class VisiableLoadFragment extends BaseFragment {

    private static final String TAG = VisiableLoadFragment.class.getSimpleName();
    protected Activity mActivity;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isViewShown && getUserVisibleHint()) {
            isViewShown = true;
            requestData();
        }

        getParentFragment();
    }


    public abstract void requestData();


    private boolean isViewShown;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {


        if (isVisibleToUser && !isViewShown && isVisible()) {

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
        if (hidden) {
            //do when hidden

            if (getView() != null) {
                onViewHide();
            }

        } else {
            if (getView() != null) {
                requestData();
                onViewShow();

            }
            //do when show
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (iStrictVisible()) {
            onViewShow();
        }
    }

    private boolean iStrictVisible() {
        if (getParentFragment() != null) {

            return getUserVisibleHint() && !isHidden() && isVisible() && getParentFragment().isVisible();
        } else {
            return getUserVisibleHint() && !isHidden() && isVisible();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onViewHide();
    }


    public void onViewShow() {
    }

    public void onViewHide() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
