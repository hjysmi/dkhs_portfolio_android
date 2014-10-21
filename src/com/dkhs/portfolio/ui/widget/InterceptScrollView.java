/**
 * @Title InterceptScrollView.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-21 上午9:20:36
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @ClassName InterceptScrollView
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-21 上午9:20:36
 * @version 1.0
 */
public class InterceptScrollView extends ScrollView {

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param context
     */
    public InterceptScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public InterceptScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param ev
     * @return
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // return false;
        if (isIntercept) {
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private boolean isIntercept = false;

    /**
     * 
     * @param isfocus true的时候表示拦截当前事件，不继续往下分发，交给自身的onTouchEvent进行处理。
     * false则不拦截，继续往下传，让子控件来处理。
     */
    public synchronized void setIsfocus(boolean isfocus) {
        this.isIntercept = isfocus;
    }

}
