package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by zjz on 2015/6/13.
 */
public abstract class VisiableLoadFragment extends BaseFragment {


//    public VisiableLoadFragment getChildFragment() {
//        return childFragment;
//    }
//
//    public void setChildFragment(VisiableLoadFragment childFragment) {
//        this.childFragment = childFragment;
//    }
//
//    @Override
//    public VisiableLoadFragment getParentFragment() {
//        return parentFragment;
//    }
//
//    public void setParentFragment(VisiableLoadFragment parentFragment) {
//        this.parentFragment = parentFragment;
//    }

//    private VisiableLoadFragment childFragment;
//    private VisiableLoadFragment parentFragment;


    protected Activity mActivity;
    private static final String TAG = VisiableLoadFragment.class.getSimpleName();

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

//
//        if (isVisibleToUser && isViewShown && isVisible()) {
//            onViewShow();
//        } else {
//            onViewHide();
//        }

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
        } else {
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
//        Log.e(TAG, this + "=============> onPause");
    }


    public void onViewShow() {
//        Log.e(TAG, this + "=============> onViewShow");

    }

    public void onViewHide() {

//        Log.e(TAG, this + "=============> onViewHide");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity =getActivity();
    }
}
