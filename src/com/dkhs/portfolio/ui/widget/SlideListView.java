/**
 * @Title ListViewEx.java
 * @Package com.dkhs.portfolio.ui
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:58:06
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import com.dkhs.portfolio.ui.adapter.MyCombinationAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * @ClassName SlideListView
 * @Description 自定义可以滑动删除的listview
 * @author zjz
 * @date 2015-03-11 下午3:58:06
 * @version 1.0
 */
public class SlideListView extends ListView {

    private static final String TAG = "SlideListView";

    private SlideView mFocusedItemView;
    private MyCombinationAdapter mAdapter;

    public SlideListView(Context context) {
        super(context);
    }

    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void shrinkListItem(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((SlideView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    private int mLastPostion = INVALID_POSITION;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param ev
     * @return
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        MyCombinationAdapter mAdapter = (MyCombinationAdapter) getAdapter();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int position = pointToPosition(x, y);
                if (position != mLastPostion && mAdapter.getmLastSlideViewWithStatusOn() != null) {
                    mAdapter.getmLastSlideViewWithStatusOn().shrink();
                    mAdapter.setmLastSlideViewWithStatusOn(null);
                    mLastPostion = INVALID_POSITION;
                    mFocusedItemView = null;
                    return true;
                }
            }
                break;
            case MotionEvent.ACTION_UP:
                // mFocusedItemView = null;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        MyCombinationAdapter mAdapter = (MyCombinationAdapter) getAdapter();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int position = pointToPosition(x, y);
                Log.e(TAG, "onTouchEvent postion=" + position);
                if (position != INVALID_POSITION) {
                    MessageItem data = (MessageItem) getItemAtPosition(position);
                    mFocusedItemView = data.slideView;
                    mLastPostion = position;

                }

            }
            case MotionEvent.ACTION_UP:
                break;
        }
        // switch (event.getAction()) {
        // case MotionEvent.ACTION_DOWN: {
        // int x = (int) event.getX();
        // int y = (int) event.getY();
        // int position = pointToPosition(x, y);
        // Log.e(TAG, "postion=" + position);
        // if (position != INVALID_POSITION) {
        // MessageItem data = (MessageItem) getItemAtPosition(position);
        // mFocusedItemView = data.slideView;
        // Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
        // } else if (null != mFocusedItemView) {
        // mFocusedItemView.shrink();
        // mFocusedItemView = null;
        // }
        // }
        // case MotionEvent.ACTION_UP:
        // // mFocusedItemView = null;
        // break;
        // default:
        // break;
        // }

        if (mFocusedItemView != null) {
            mFocusedItemView.onRequireTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    public class MessageItem<T> {
        public T data;
        public SlideView slideView;
    }

}
