/**
 * @Title: ScrollViewPager.java
 * @Package: com.naerju.views
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: zhoujunzhou
 * @date: 2014-5-13 上午11:44:12
 * @version: V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @ClassName: ScrollViewPager
 * @Description: 设置是否可以滑动界面的ViewPager
 * @author: zhoujunzhou
 * @date: 2014-5-13 上午11:44:12
 * @version: 1.0
 */
public class ScrollViewPager extends ViewPager {

    private boolean isCanScroll = true;

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void ScrollViewPager(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

}
