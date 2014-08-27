package com.dkhs.portfolio.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dkhs.portfolio.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;

public class MAChart extends GridChart {
    /** 显示数据线 */
    private List<LineEntity> lineData;

    /** 最大点数 */
    private int maxPointNum;

    /** 最低价格 */
    private int minValue;

    /** 最高价格 */
    private int maxValue;

    /** 经线是否使用虚线 */
    private boolean dashLongitude = Boolean.FALSE;
    /** 虚线效果 */
    private PathEffect dashEffect = DEFAULT_DASH_EFFECT;

    ArrayList<HashMap<String, Object>> listItem;

    private float pointLineLength;

    private float lineLength;

    private Paint mLinePaint;

    private float startPointX;
    private float endY;

    private boolean isTouchAble;

    public MAChart(Context context) {
        super(context);
        init();
    }

    public MAChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MAChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // mCanvas = canvas;

        startPointX = super.getAxisMarginLeft() + super.getAxisMarginRight();
        endY = super.getHeight();
        // 绘制平�?��
        drawLines(canvas);
        if (super.isTouch() && isTouchAble) {
            getTouchPointData(canvas);

        }

    }

    // boolean drawAimation;
    // Handler mHandler = new Handler() {
    // public void handleMessage(android.os.Message msg) {
    // System.out.println("DrawAmian handleMessage");
    // isDrawAnimation = true;
    // // invalidate();
    // };
    // };
    // boolean isDrawAnimation;

    // public void drawLineAnimation(boolean isAnimation) {
    //
    // mHandler.sendEmptyMessageDelayed(11, 2000);
    // }

    protected void drawLines(Canvas canvas) {
        lineLength = (super.getWidth() - super.getAxisMarginLeft() - super.getAxisMarginRight() - this.getMaxPointNum());
        // 点线距离
        pointLineLength = (lineLength / this.getMaxPointNum()) - 1;
        // 起始位置
        float startX;

        Paint fillPaint = new Paint();
        // fillPaint.setColor(ColorTemplate.getRaddomColor());

        if (null == lineData) {
            return;
        }
        // 逐条输入MA线
        for (int i = 0; i < lineData.size(); i++) {
            LineEntity line = (LineEntity) lineData.get(i);
            if (line.isDisplay()) {
                mLinePaint.setColor(line.getLineColor());

                List<Float> lineData = line.getLineData();
                // 输�?�?��线
                // startx = 27
                // startX = super.getAxisMarginLeft() + pointLineLength / 2f;
                startX = startPointX;
                // 定义起始点
                PointF ptFirst = null;
                // if(i==0){
                Path fillPath = new Path();

                // }
                if (lineData != null && lineData.size() > 0) {
                    for (int j = 0; j < lineData.size(); j++) {
                        // j=1,value=272
                        float value = lineData.get(j).floatValue();
                        // 获取终点Y坐�?
                        // j=1,vlaueY=29.866665
                        // minvalue = 220,maxvalue=280
                        // valueY为Y坐标的值

                        float valueY = (float) ((1f - (value - this.getMinValue())
                                / (this.getMaxValue() - this.getMinValue())) * (super.getHeight() - super
                                .getAxisMarginBottom()));

                        // if (dashLongitude) {
                        if (j < 30) {
                            mLinePaint.setPathEffect(dashEffect);
                        } else {
                            mLinePaint.setPathEffect(null);
                        }

                        // 绘制线条
                        if (j > 0) {
                            // 画线路图
                            canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, mLinePaint);
                        }
                        // 重置起始点
                        ptFirst = new PointF(startX, valueY);

                        if (fillLineIndex == i && j == 0) {

                            fillPaint.setColor(line.getLineColor());
                            fillPaint.setAlpha(85);
                            fillPaint.setAntiAlias(true);
                            fillPath.moveTo(startX, valueY);
                        } else {
                            fillPath.lineTo(startX, valueY);
                        }
                        // X位移
                        startX = startX + 1 + pointLineLength;
                    }

                    // System.out.println("isFill:" + isFill + " fillLineIndex=" + fillLineIndex + " currentIndex:" +
                    // i);
                    if (isFill && fillLineIndex == i) {
                        try {

                            fillPath.lineTo(ptFirst.x, endY);
                            fillPath.lineTo(startPointX, endY);
                            canvas.drawPath(fillPath, fillPaint);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private boolean isFill;
    private int fillLineIndex;

    // paint.setAlpha(85);
    //
    // Path filled = new Path();
    // filled.moveTo(entries.get(0).getXIndex(), entries.get(0).getVal());
    //
    // // create a new path
    // for (int x = 1; x < entries.size(); x++) {
    //
    // filled.lineTo(entries.get(x).getXIndex(), entries.get(x).getVal());
    // }
    //
    // // close up
    // filled.lineTo(entries.get(entries.size() - 1).getXIndex(), mYChartMin);
    // filled.lineTo(entries.get(0).getXIndex(), mYChartMin);
    // filled.close();
    // mDrawCanvas.drawPath(filled, paint);

    private void getTouchPointData(Canvas canvas) {
        if (super.getTouchPoint() != null) {
            int pointIndex = (int) ((super.getTouchPoint().x - super.getAxisMarginLeft() - pointLineLength / 2f) / (pointLineLength + 1));
            LineEntity lineEntity = lineData.get(1);
            int maxPointSize = lineEntity.getLineData().size();
            if (pointIndex >= maxPointSize) {
                pointIndex = maxPointSize - 1;
            }
            float avg = lineEntity.getLineData().get(pointIndex);
            // drawDataView(canvas);

            drawDataView(canvas, pointIndex);
        }

    }

    public void refreshClear() {
        // super.onDraw(mCanvas);
        // 绘制平�?��
        // mCanvas.
        // drawLines(mCanvas, false);
        super.setTouch(false);
        invalidate();
    }

    // clearRect(x,y,width,height) ‒ clears the given area and makes it fully opaque
    private void drawDataView(Canvas canvas, int pointIndex) {

        float midPointx = (super.getWidth() / 2.0f) + super.getAxisMarginLeft();
        float startX;
        int viewLength = 160;
        int viewHeight = 160;
        int margin = 20;
        float startY = margin;
        // 当触摸点在左边
        if (super.getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + margin;

        } else {
            startX = super.getWidth() - viewLength - margin;

        }

        // 创建画笔 画背景图
        Paint selectPaint = new Paint();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(Color.WHITE);

        RectF oval3 = new RectF(startX, startY, startX + viewLength, startY + viewHeight);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/
        int textMargin = 20;
        float preYpoint = startY + (1.5f * textMargin);
        selectPaint.reset();
        selectPaint.setColor(Color.BLACK);
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(getResources().getInteger(R.integer.select_touch_text));
        canvas.drawText("日期：" + pointIndex, startX + textMargin, preYpoint, selectPaint);

        FontMetrics fontMetrics = selectPaint.getFontMetrics();
        float preTextBottom = fontMetrics.bottom;
        int lineLength = lineData.size();
        for (int i = 0; i < lineLength; i++) {

            selectPaint.setColor(lineData.get(i).getLineColor());
            preYpoint += textMargin * 1.5f + preTextBottom;
            String text = "";
            if (i == 0) {
                text = "我的涨幅:2.32%";
            } else if (i == 1) {
                text = "沪深300:1.43%";
            } else {
                text = "创业板:1.40%";
            }
            canvas.drawText(text, startX + textMargin, preYpoint, selectPaint);
        }

        /***************/

    }

    public List<LineEntity> getLineData() {
        return lineData;
    }

    public void setLineData(List<LineEntity> lineData) {
        this.lineData = lineData;
    }

    public int getMaxPointNum() {
        return maxPointNum;
    }

    public void setMaxPointNum(int maxPointNum) {
        this.maxPointNum = maxPointNum;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isFill() {
        return isFill;
    }

    public void setFill(boolean isFill) {
        this.isFill = isFill;
    }

    public int getFillLineIndex() {
        return fillLineIndex;
    }

    public void setFillLineIndex(int fillLineIndex) {
        this.fillLineIndex = fillLineIndex;
    }
}
