/**
 * @Title MarqueeText.java
 * @Package com.dkhs.portfolio.ui.widget
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-20 下午2:17:33
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @ClassName MarqueeText
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-20 下午2:17:33
 * @version 1.0
 */
public class MarqueeText extends TextView implements Runnable {

    private int currentScrollX;// 当前滚动的位置
    private boolean isStop = false;
    private int textWidth;
    private boolean isMeasure = false;

    public MarqueeText(Context con) {
        super(con);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        if (!isMeasure) {// 文字宽度只需获取一次就可以了
            getTextWidth();
            isMeasure = true;
        }
    }

    /**
     * 获取文字宽度
     */
    private void getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }

    @Override
    public void run() {
        currentScrollX -= 6;// 滚动速度
        scrollTo(currentScrollX, 0);
        if (isStop) {
            return;
        }
        if (getScrollX() <= -(this.getWidth())) {
            scrollTo(textWidth, 0);
            currentScrollX = textWidth;
            // return;
        }
        postDelayed(this, 5);
    }

    // 开始滚动
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);
    }

    // 停止滚动
    public void stopScroll() {
        isStop = true;
    }

    // 从头开始滚动
    public void startFor0() {
        currentScrollX = 0;
        startScroll();
    }
}
