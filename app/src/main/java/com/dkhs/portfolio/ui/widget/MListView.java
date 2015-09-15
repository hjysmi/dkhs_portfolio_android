package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ListView;

import com.lidroid.xutils.util.LogUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MListView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/9/7.
 */
public class MListView extends ListView  implements NestedScrollingChild {

    private int mLastMotionY;

    public boolean isBlock=false;

    public MListView(Context context) {
        super(context);
    }

    public MListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId = -1;

    private int mStartPointerY=-1;
      private final int[] mScrollOffset = new int[2];


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        MotionEvent vtev = MotionEvent.obtain(ev);
        final int actionMasked = MotionEventCompat.getActionMasked(ev);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {

                // Remember where the motion event started
                mLastMotionY = (int) ev.getRawY();
                mStartPointerY = (int) ev.getRawY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                final ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }

                ViewParentCompat.onStartNestedScroll(this.getParent().getParent(), this, (View) this.getParent().getParent(),ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e("", "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }
                LogUtils.e("     getRawY  " +(int) ev.getRawY());
                if(getAdapter() != null &&  this.getChildAt(0)!= null ){
                    LogUtils.e(""+this.getChildAt(0).getTop());
                    if(this.getChildAt(0).getTop() ==0 ) {

                        ViewParentCompat.onNestedScroll(this.getParent().getParent(), this, 0, mLastMotionY - (int) ev.getRawY(), 0, 0);



                        LogUtils.e("tag " + isBlock);
                        if(isBlock){
                            mLastMotionY = (int) ev.getRawY();
                            return true;
                        }

                    }
                }
                mLastMotionY = (int) ev.getRawY();


        }

         super.onTouchEvent(ev);
        return true;
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return super.dispatchNestedFling(velocityX, velocityY, consumed);
    }
}
