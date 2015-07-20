package com.dkhs.portfolio.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

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
        getParentFragment();
    }

    public abstract void requestData();

    @Override
    public void onViewShow() {
        super.onViewShow();
        requestData();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
