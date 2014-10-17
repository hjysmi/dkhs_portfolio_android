/**
 * @Title TimesharingplanChart.java
 * @Package com.dkhs.portfolio.ui.fragment
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-17 下午3:59:21
 * @version V1.0
 */
package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.dkhs.portfolio.ui.fragment.TrendChartFragment;

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

    @Override
    protected void drawDataView(Canvas canvas, int pointIndex) {
        LineEntity lineentity = lineData.get(0);
    }

}
