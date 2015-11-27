package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by xuetong on 2015/11/26.
 * 最近在开发中遇到HorizontalScrollView嵌套ScrollView导致滑动卡的情况，
 * 急横向水平滑动的View和垂直水平滑动的View，都在接收处理滑动时间，但是这种情况下触摸事件就会发生冲突。
 * 导致滑动非常卡，甚至出现程序停止响应。这种情况下我们需要重写view。
 * 下面是两个简单的例子，重写水平滑动View只接收水平方向上滑动的事件。我们使用手势GestureDetector来作区分。
 */
public class CustomHScrollView extends HorizontalScrollView {
    private GestureDetector mGestureDetector;
    private View.OnTouchListener mGestureListener;

    private static final String TAG = "CustomHScrollView";

    /**
     * @function CustomHScrollView constructor
     * @param context Interface to global information about an application environment.
     *
     */
    public CustomHScrollView(Context context) {
        super(context);
// TODO Auto-generated constructor stub
        mGestureDetector = new GestureDetector(new HScrollDetector());
        setFadingEdgeLength(0);
    }

    /**
     * @function CustomHScrollView constructor
     * @param context Interface to global information about an application environment.
     * @param attrs A collection of attributes, as found associated with a tag in an XML document.
     */
    public CustomHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
// TODO Auto-generated constructor stub
        mGestureDetector = new GestureDetector(new HScrollDetector());
        setFadingEdgeLength(0);
    }

    /**
     * @function CustomHScrollView constructor
     * @param context Interface to global information about an application environment.
     * @param attrs A collection of attributes, as found associated with a tag in an XML document.
     * @param defStyle style of view
     */
    public CustomHScrollView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
// TODO Auto-generated constructor stub
        mGestureDetector = new GestureDetector(new HScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the y direction
    class HScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(Math.abs(distanceX) > Math.abs(distanceY)) {
                return true;
            }

            return false;
        }
    }

}
