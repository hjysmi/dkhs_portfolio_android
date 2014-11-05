/**   
 * @Title FixedSpeedScroller.java 
 * @Package com.dkhs.portfolio.ui.widget 
 * @Description TODO(用一句话描述该文件做什么) 
 * @author zjz  
 * @date 2014-11-5 下午2:38:29 
 * @version V1.0   
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/** 
 * @ClassName FixedSpeedScroller 
 * @Description TODO(这里用一句话描述这个类的作用) 
 * @author zjz 
 * @date 2014-11-5 下午2:38:29 
 * @version 1.0 
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 1000;

    public FixedSpeedScroller(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
        // TODO Auto-generated constructor stub
    }

    // public FixedSpeedScroller(Context context, Interpolator interpolator,
    // boolean flywheel) {
    // super(context, interpolator, flywheel);
    // // TODO Auto-generated constructor stub
    // }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    /**
     * @Description 设置滑动间隔
     * @author Created by qinxianyuzou on 2014-10-29.
     * @param duration
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }
}
