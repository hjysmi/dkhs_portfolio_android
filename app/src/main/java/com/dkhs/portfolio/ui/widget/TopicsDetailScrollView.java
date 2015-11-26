package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

import com.dkhs.portfolio.R;
import com.lidroid.xutils.util.LogUtils;
import com.melnykov.fab.ObservableScrollView;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MScrollView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/9/7.
 */
public class TopicsDetailScrollView extends NestedScrollView {


    private TopicsDetailListView mTopicsDetailListView;

    private View mTopSticky;
    private int mTopStickyHeight;
    private ObservableScrollView.OnScrollChangedListener mOnScrollChangedListener;
    private int mActivePointerId;

    public TopicsDetailScrollView(Context context) {
        super(context);
    }

    public TopicsDetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopicsDetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollChangedListener(ObservableScrollView.OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;

    }

    @Override
    protected void onFinishInflate() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.scrollview_contain_listview, this, false);
        mTopicsDetailListView = (TopicsDetailListView) view.findViewById(R.id.lv);
        mTopSticky = view.findViewById(R.id.top_sticky);
        mTopicsDetailListView.mTopicsDetailScrollView = this;
        addView(view);
        super.onFinishInflate();
    }


    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (isFirst && hasWindowFocus && mTopicsDetailListView != null) {
            isFirst = false;
            mTopStickyHeight = mTopSticky.getMeasuredHeight();
            ViewGroup.LayoutParams layoutParams = mTopicsDetailListView.getLayoutParams();
            layoutParams.height = getMeasuredHeight() - mTopStickyHeight;
            mTopicsDetailListView.requestLayout();
        }

    }


    private int LastY = 0;

    private VelocityTracker mVelocityTracker;

    private boolean isFirstResponse = true;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isFirstResponse = true;
                LastY = (int) ev.getRawY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                if (mTopicsDetailListView.getAdapter() == null || mTopicsDetailListView.getAdapter().getCount() == 0) {
                    isFirstResponse = true;
                    return true;
                }
                if (mTopicsDetailListView.getTop() - getScrollY() < ev.getY()) {
                    isFirstResponse = false;
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (mTopicsDetailListView.getFirstVisiblePosition() == 0 && mTopicsDetailListView.getChildAt(0).getTop() == 0 ) {
                    if (mTopicsDetailListView.getTop() - mTopStickyHeight > getScrollY() && ev.getRawY() - LastY < -12  ) {
                        LastY = (int) ev.getRawY();
                        return true;
                    }
                    if (mTopicsDetailListView.getChildCount() > 0  && ev.getRawY() - LastY > 12) {

                        LastY = (int) ev.getRawY();
                        return true;
                    }
                }
                LastY = (int) ev.getRawY();

        }
        return super.onInterceptTouchEvent(ev);
    }

    private VelocityTracker initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        return mVelocityTracker;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (isFirstResponse) {
            return super.onTouchEvent(ev);
        } else {
            initVelocityTrackerIfNotExists().addMovement(ev);
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    LastY = (int) ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.smoothScrollBy(0, (int) (LastY - ev.getRawY()));
                    LastY = (int) ev.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    initVelocityTrackerIfNotExists().computeCurrentVelocity(1000);
//                    .computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) VelocityTrackerCompat.getYVelocity(initVelocityTrackerIfNotExists(), mActivePointerId);
                    fling((int) -initialVelocity);
                    break;

            }
        }
        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View view = (View) getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY()));

        if (diff == 0 && mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollBottom();
        }

        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(null, l, t, oldl, oldt);
        }
    }


    public boolean listViewIsBelow() {
        if (mTopicsDetailListView.getTop() - mTopStickyHeight > getScrollY()) {
            return true;
        }
        return false;
    }
}
