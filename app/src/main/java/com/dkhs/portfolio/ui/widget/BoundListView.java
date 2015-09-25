package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

import com.dkhs.portfolio.app.PortfolioApplication;

/**
 * Created by zjz on 2015/9/23.
 */
public class BoundListView extends ListView {
    public BoundListView(Context context) {
        super(context);
        initView();
    }

    public BoundListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BoundListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //    public BoundListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
    private int mMaxOverDistance = 50;

    private void initView() {
        DisplayMetrics metrics = PortfolioApplication.getInstance().getResources().getDisplayMetrics();
        float density = metrics.density;
        mMaxOverDistance = (int) (density * mMaxOverDistance);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxOverDistance, isTouchEvent);
    }
}


