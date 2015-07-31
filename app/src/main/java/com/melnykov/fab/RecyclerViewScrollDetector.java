package com.melnykov.fab;

import android.support.v7.widget.RecyclerView;

/**
 * @author zwm
 * @version 2.0
 * @ClassName RecyclerViewScrollViewDetector
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/31.
 */
public abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {


    private int mScrollThreshold;


    abstract void onScrollUp();


    abstract void onScrollDown();


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
        if (isSignificantDelta) {
            if (dy > 0) {
                onScrollUp();
            } else {
                onScrollDown();
            }
        }
    }


    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
