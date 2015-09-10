package com.dkhs.portfolio.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.ViewParentCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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
      private final int[] mScrollOffset = new int[2];


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        MotionEvent vtev = MotionEvent.obtain(ev);
        final int actionMasked = MotionEventCompat.getActionMasked(ev);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {

                // Remember where the motion event started
                mLastMotionY = (int) ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);

                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e("", "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }

                if(getAdapter() != null &&  this.getChildAt(0)!= null){
                    LogUtils.e(""+this.getChildAt(0).getTop());
                    if(this.getChildAt(0).getTop()==0) {
                        LogUtils.e("       " +(mLastMotionY-(int) ev.getY())  );

                        ViewParentCompat.onNestedScroll(this.getParent().getParent(),this,0, mLastMotionY-(int) ev.getY(), 0, 0);
                    }


                }

                mLastMotionY = (int) ev.getY();

                LogUtils.e("mLastMotionY   "+mLastMotionY);


        }

        return super.onTouchEvent(ev);
    }
}
