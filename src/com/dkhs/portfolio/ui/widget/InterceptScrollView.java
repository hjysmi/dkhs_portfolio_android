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
import android.graphics.Rect;
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
public class InterceptScrollView extends ScrollView{
	private boolean scrollable = true;
	private ScrollViewListener scrollViewListener = null;
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
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {  
        this.scrollViewListener = scrollViewListener;  
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
    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
     
     return 0;
    }
    @Override  
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {  
        super.onScrollChanged(x, y, oldx, oldy);  
        if (scrollViewListener != null) {  
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);  
        }  
    } 
    private boolean isIntercept = false;
       
   /* @Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
    	if(isIntercept){
    		return super.onTouchEvent(ev);
    	}else{
    		return false;
    	}
	}*/

	@Override
    public int computeVerticalScrollRange() {
        // TODO Auto-generated method stub
        return super.computeVerticalScrollRange();
    }

    /**
     * 
     * @param isfocus true的时候表示拦截当前事件，不继续往下分发，交给自身的onTouchEvent进行处理。
     * false则不拦截，继续往下传，让子控件来处理。
     */
    public synchronized void setIsfocus(boolean isfocus) {
        this.isIntercept = isfocus;
    }

	public boolean isScrollable() {
		return scrollable;
	}

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}
	public interface ScrollViewListener {  
		  
	    void onScrollChanged(InterceptScrollView scrollView, int x, int y, int oldx, int oldy);  
	  
	}
}

