package com.dkhs.portfolio.ui.widget;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by zjz on 2015/6/29.
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    private long mLastClickTime = 0;

    @Override
    public void onClick(View v) {
        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        onSingleClick(v);
    }

    /**
     * click响应函数
     *
     * @param v The view that was clicked.
     */
    public abstract void onSingleClick(View v);
}
