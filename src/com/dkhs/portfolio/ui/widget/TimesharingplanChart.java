/**
 * @Title TimesharingplanChart.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-17 下午3:59:21
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.dkhs.portfolio.ui.widget.chart.StickEntity;
import com.dkhs.portfolio.utils.ColorTemplate;

/**
 * @ClassName TimesharingplanChart
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-17 下午3:59:21
 * @version 1.0
 */
public class TimesharingplanChart extends TrendChart {

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param context
     */
    public TimesharingplanChart(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public TimesharingplanChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TimesharingplanChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param canvas
     * @return
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void drawTimesSharingChart(Canvas canvas) {
        drawVolBorder(canvas);
        drawVolChart(canvas);
    }

    private void drawVolBorder(Canvas canvas) {
        Paint mXTitlePaint = new Paint();
        mXTitlePaint.setColor(getLongitudeColor());
        // mXTitlePaint.setColor(Color.RED);
        canvas.drawLine(axisMarginLeft, getmGridLineHeight(), axisMarginLeft + getmGridLineLenght(),
                getmGridLineHeight(), mXTitlePaint);

        canvas.drawLine(axisMarginLeft, getmGridLineHeight(), axisMarginLeft, getmGridLineHeight() + getVolHight(),
                mXTitlePaint);

        canvas.drawLine(axisMarginLeft, getmGridLineHeight() + getVolHight(), axisMarginLeft + getmGridLineLenght(),
                getmGridLineHeight() + getVolHight(), mXTitlePaint);

        canvas.drawLine(axisMarginLeft + getmGridLineLenght(), getmGridLineHeight(), axisMarginLeft
                + getmGridLineLenght(), getmGridLineHeight() + getVolHight(), mXTitlePaint);

        mXTitlePaint.setPathEffect(getDashEffect());
        canvas.drawLine(axisMarginLeft, getmGridLineHeight() + getVolHight() / 2,
                axisMarginLeft + getmGridLineLenght(), getmGridLineHeight() + getVolHight() / 2, mXTitlePaint);

        float offsetX = getmGridLineLenght() / 4;
        for (int i = 1; i <= 3; i++) {

            canvas.drawLine(axisMarginLeft + offsetX * i, getmGridLineHeight(), axisMarginLeft + offsetX * i,
                    getmGridLineHeight() + getVolHight(), mXTitlePaint);

        }

    }

    private void drawVolChart(Canvas canvas) {
        // 蜡烛棒宽度
        float stickWidth = (getmGridLineLenght() / 240);
        // 蜡烛棒起始绘制位置
        float stickX = super.getAxisMarginLeft() + 1;
        Paint mPaintStick = new Paint();

        if (null != lineData && lineData.size() > 0) {

            LineEntity lineentity = lineData.get(0);
            if (null != lineentity) {

                List<FSLinePointEntity> stickData = lineentity.getLineData();
                int offsetVol = (lineentity.getMaxVolNum() - lineentity.getMinVolNum());

                if (null != stickData) {
                    float highY = getmGridLineHeight() + getVolHight();
                    // 判断显示为方柱或显示为线条
                    for (int i = 0; i < stickData.size(); i++) {
                        FSLinePointEntity ohlc = stickData.get(i);

                        if (ohlc.getIncreaseRange() >= 0) {
                            mPaintStick.setColor(ColorTemplate.DEF_RED);
                        } else {
                            mPaintStick.setColor(ColorTemplate.DEF_GREEN);
                        }

                        // float highY = (float) ((1f - (ohlc.getHigh() - minValue) / (maxValue - minValue))
                        // * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                        // .getAxisMarginTop());
                        // float lowY = (float) ((1f - (ohlc.getLow() - minValue) / (maxValue - minValue))
                        // * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                        // .getAxisMarginTop());

                        float dValue = ohlc.getTurnover() - lineentity.getMinVolNum();

                        float percentYPoint = 0.0f;
                        if (offsetVol != 0 && dValue != 0) {
                            percentYPoint = dValue / offsetVol;
                        }

                        float lowY = getmGridLineHeight() + getVolHight() * (1 - percentYPoint);
                        // float lowY = getmGridLineHeight() + 0;

                        // 绘制数据?��?据宽度判断绘制直线或方柱
                        if (stickWidth >= 2f) {
                            canvas.drawRect(stickX, highY, stickX + stickWidth, lowY, mPaintStick);
                        } else {
                            canvas.drawLine(stickX, highY, stickX, lowY, mPaintStick);
                        }

                        // X位移
                        stickX = stickX + stickWidth;
                    }
                }
            }
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public float getmGridLineHeight() {
        // TODO Auto-generated method stub
        return super.getmGridLineHeight() * 2 / 3;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public float getVolHight() {

        return super.getmGridLineHeight() * 1 / 3;
    }

}