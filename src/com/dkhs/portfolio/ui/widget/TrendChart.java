package com.dkhs.portfolio.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.StringFromatUtils;

public class TrendChart extends TrendGridChart {
    /** 显示数据线 */
    private List<LineEntity> lineData;

    /** 最大点数 */
    private int maxPointNum;

    /** 最低价格 */
    private float minValue;

    /** 最高价格 */
    private float maxValue;

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
    /** 选中位置X坐标 */
    private float clickPostX = 0f;

    /** 选中位置Y坐标 */
    private float clickPostY = 0f;
    
    
    private boolean isDrawDashLine;

    public TrendChart(Context context) {
        super(context);
        init();
    }

    public TrendChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TrendChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(2);
    }

    public void setSmallLine() {
        mLinePaint.setStrokeWidth(1);
    }

    /** 当前被选中的坐标点 */
    private PointF touchPoint;

    private boolean isTouch;

    /**
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                break;

            default:
                break;
        }

        if (event.getY() > 0 && event.getY() < super.getBottom() - getAxisMarginBottom()
                && event.getX() > super.getLeft() + getAxisMarginLeft() && event.getX() < super.getRight()) {

            /*
             * 判定用户是否触摸到�?���?如果是单点触摸则�?��绘制十字线 如果是2点触控则�?��K线放大
             */
            if (event.getPointerCount() == 1) {
                // 获取点击坐标
                clickPostX = event.getX();
                clickPostY = event.getY();

                PointF point = new PointF(clickPostX, clickPostY);
                touchPoint = point;
                // super.invalidate();
                super.invalidate();

                // 通知�?��其他�?联Chart
                // notifyEventAll(this);

            } else if (event.getPointerCount() == 2) {
            }
        }

        // return super.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // mCanvas = canvas;

        startPointX = mStartLineXpoint + 2;
        endY = mGridLineHeight - axisMarginBottom;
        // 绘制平线
        drawLines(canvas);
        if (isTouch) {
            getTouchPointData(canvas);

        }
        if (isTouch) {
            drawWithFingerClick(canvas);
        }

    }

    /**
     * 单点击事件
     */
    protected void drawWithFingerClick(Canvas canvas) {

        Paint mPaint = new Paint();
        mPaint.setColor(Color.CYAN);

        // 水平线长度
        float lineHLength = getWidth() - 2f;
        // 垂直线高度
        float lineVLength = mGridLineHeight - axisMarginBottom;

        // 绘制横纵线
        if (isDisplayAxisXTitle()) {

            if (clickPostX > 0 && clickPostY > 0) {
                // 绘制X轴�?���?
                // if (displayCrossXOnTouch) {
                // // TODO �?���?��小控制�?�?��
                // PointF BoxVS = new PointF(clickPostX - longtitudeFontSize * 5f / 2f, lineVLength + 2f);
                // PointF BoxVE = new PointF(clickPostX + longtitudeFontSize * 5f / 2f, lineVLength + axisMarginBottom
                // - 1f);
                //
                // // 绘制�?���?
                // drawAlphaTextBox(BoxVS, BoxVE, getAxisXGraduate(clickPostX), longtitudeFontSize, canvas);
                // }
            }
        }

        if (isDisplayAxisYTitle()) {
            lineHLength = lineHLength - getAxisMarginLeft();

            if (clickPostX > 0 && clickPostY > 0) {
                // // 绘制Y轴�?���?
                // if (displayCrossYOnTouch) {
                // PointF BoxHS = new PointF(1f, clickPostY - latitudeFontSize / 2f);
                // PointF BoxHE = new PointF(axisMarginLeft, clickPostY + latitudeFontSize / 2f);
                //
                // // 绘制�?���?
                // drawAlphaTextBox(BoxHS, BoxHE, getAxisYGraduate(clickPostY), latitudeFontSize, canvas);
                // }
            }
        }

        if (clickPostX > 0 && clickPostY > 0 && clickPostX < (mStartLineXpoint + mGridLineLenght)) {
            if (!isTouch) {
                mPaint.setColor(Color.TRANSPARENT);
            }
            // 显示纵线
            // if (displayCrossXOnTouch) {
            //
            // canvas.drawLine(clickPostX, axisMarginTop + xTitleTextHeight / 2, clickPostX, lineVLength, mPaint);
            canvas.drawLine(clickPostX, mStartLineYpoint, clickPostX, lineVLength, mPaint);

            // }

            // 显示横线
            // if (displayCrossYOnTouch) {

            // canvas.drawLine(axisMarginLeft, clickPostY, axisMarginLeft + lineHLength, clickPostY, mPaint);
            // }
            // Paint clearPaint = new Paint();
            // clearPaint.setColor(Color.TRANSPARENT);
            // clearPaint
            // .setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            // canvas.drawLine(clickPostX, 1f, clickPostX, lineVLength,
            // clearPaint);
            // canvas.drawLine(axisMarginLeft, clickPostY, axisMarginLeft
            // + lineHLength, clickPostY, clearPaint);
        }

        invalidate();
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
        // lineLength = (super.getWidth() - startPointX - super.getAxisMarginRight());
        // 点线距离
        pointLineLength = ((mGridLineLenght - 2) / (this.getMaxPointNum() - 1)) - 1;

        // 起始位置
        float startX;

        // float lineHeight = super.getHeight() - axisMarginTop - super.getAxisMarginBottom() - xTitleTextHeight;
        float lineHeight = mGridLineHeight - axisMarginBottom - 2 - mStartLineYpoint;

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

                List<LinePointEntity> lineData = line.getLineData();
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
                        if (j >= getMaxPointNum()) {
                            break;
                        }
                        float value = lineData.get(j).getValue();
                        // 获取终点Y坐�?
                        // j=1,vlaueY=29.866665
                        // minvalue = 220,maxvalue=280
                        // valueY为Y坐标的值
                        float hightPrecent = 0;
                        if (this.getMaxValue() == this.getMinValue()) {
                            hightPrecent = 0.5f;
                        } else {

                            hightPrecent = (1f - (value - this.getMinValue())
                                    / (this.getMaxValue() - this.getMinValue()));
                        }
                        float valueY = (float) (hightPrecent * (lineHeight));
                        valueY += mStartLineYpoint;
                        // valueY += mStartLineYpoint;
                        // if (dashLongitude) {
                        if (isDrawDashLine) {
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

    private int dashLineLenght;

    public void setDashLineLenght(int lenght) {
        this.dashLineLenght = lenght;
    }

    private boolean isFill;
    private int fillLineIndex = 0;

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
        if (getTouchPoint() != null && null != lineData && lineData.size() > 0) {
            int pointIndex = (int) ((getTouchPoint().x - super.getAxisMarginLeft() - pointLineLength / 2f) / (pointLineLength + 1)) + 1;
            LineEntity lineEntity = lineData.get(0);
            int maxPointSize = lineEntity.getLineData().size();
            if (pointIndex < maxPointSize) {
                // pointIndex = maxPointSize - 1;
                LinePointEntity data = lineEntity.getLineData().get(pointIndex);
                drawDataView(canvas, pointIndex, data);
            }
            // drawDataView(canvas);

        }

    }

    public void refreshClear() {
        // super.onDraw(mCanvas);
        // 绘制平�?��
        // mCanvas.
        // drawLines(mCanvas, false);
        setTouch(false);
        invalidate();
    }

    private List<String> pointTitleList;

    public void setPointTitleList(List<String> pTitleList) {
        this.pointTitleList = pTitleList;
    }

    // clearRect(x,y,width,height) ‒ clears the given area and makes it fully opaque
    private void drawDataView(Canvas canvas, int pointIndex, LinePointEntity date) {

        float midPointx = (super.getWidth() / 2.0f) + super.getAxisMarginLeft();
        float startX;
        int viewLength = 180;
        int viewHeight = 120;
        int margin = 20;
        float marginTop = margin + axisMarginTop;
        // = margin;
        // 当触摸点在左边
        if (getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + margin;

        } else {
            startX = super.getWidth() - viewLength - margin;

        }

        // 创建画笔 画背景图
        Paint selectPaint = new Paint();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(Color.WHITE);

        RectF oval3 = new RectF(startX, marginTop, startX + viewLength, marginTop + viewHeight);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/
        int textMargin = 10;

        FontMetrics fm = selectPaint.getFontMetrics();
        int textTextHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);

        float preYpoint = textTextHeight + textMargin + marginTop;
        selectPaint.reset();
        selectPaint.setColor(Color.BLACK);
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(getResources().getInteger(R.integer.select_touch_text));
        String firtLineText;
        if (null != pointTitleList && pointTitleList.size() > 0) {
            firtLineText = pointTitleList.get(0) + ":" + date.getDesc();
        } else {
            firtLineText = "日期：" + date.getDesc();
        }
        canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);

        // FontMetrics fontMetrics = selectPaint.getFontMetrics();
        // float preTextBottom = fontMetrics.bottom;
        int lineLength = lineData.size();
        for (int i = 0; i < lineLength; i++) {
            if (pointIndex < lineData.get(i).getLineData().size()) {

                selectPaint.setColor(lineData.get(i).getLineColor());
                preYpoint += textMargin + textTextHeight;
                String text = "";
                // if (i == 0) {
                if (TextUtils.isEmpty(lineData.get(i).getTitle())) {
                    // text = StringFromatUtils.get4Point(lineData.get(i).getLineData().get(pointIndex).getNetvalue());

                    if (null != pointTitleList && pointTitleList.size() > 1) {
                        text = pointTitleList.get(1) + ":"
                                + StringFromatUtils.get4Point(lineData.get(i).getLineData().get(pointIndex).getValue());
                    } else {
                        text = StringFromatUtils.get4Point(lineData.get(i).getLineData().get(pointIndex).getValue());
                    }
                } else {

                    // text = lineData.get(i).getTitle()
                    // + StringFromatUtils.get4Point(lineData.get(i).getLineData().get(pointIndex).getNetvalue());
                }
                // } else if (i == 1) {
                // text = "沪深300:1.43%";
                // } else {
                // text = "创业板:1.40%";
                // }
                if (i == 0)
                    canvas.drawText(text, startX + textMargin, preYpoint, selectPaint);
            }
        }

        /***************/

    }

    public List<LineEntity> getLineData() {
        return lineData;
    }

    public void setLineData(List<LineEntity> lineData) {
        this.lineData = lineData;

        invalidate();
    }

    public int getMaxPointNum() {
        return maxPointNum;
    }

    public void setMaxPointNum(int maxPointNum) {
        this.maxPointNum = maxPointNum;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
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

    public PointF getTouchPoint() {
        return touchPoint;
    }

    public void setTouchPoint(PointF touchPoint) {
        this.touchPoint = touchPoint;
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    public boolean isDrawDashLine() {
        return isDrawDashLine;
    }

    public void setDrawDashLine(boolean isDrawDashLine) {
        this.isDrawDashLine = isDrawDashLine;
    }
}
