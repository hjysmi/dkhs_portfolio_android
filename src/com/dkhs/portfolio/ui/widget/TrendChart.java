package com.dkhs.portfolio.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.ITouchListener;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

public class TrendChart extends TrendGridChart {
    /** 显示数据线 */
    List<LineEntity> lineData;

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
    private Paint fillPaint;
    private float startPointX;
    private float endY;
    private int lineStrokeWidth;
    private boolean isTouchAble;
    /** 选中位置X坐标 */
    private float clickPostX = 0f;

    /** 选中位置Y坐标 */
    private float clickPostY = 0f;

    private boolean isFromCompare;
    private int dashLinePointSize;

    private boolean isDrawTimesharingplanChart;
    private boolean isDrawTrendChart;

    // 计数器，防止多次点击导致最后一次形成longpress的时间变短
    private int mCounter;
    // 是否释放了
    private boolean isReleased;
    // 长按的runnable
    private Runnable mLongPressRunnable;
    // 移动的阈值
    private static final int TOUCH_SLOP = 20;
    private boolean moves = false;
    private Context context;

    private StockViewCallBack callBack;

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

    private int textMargin;

    // 创建画笔 画背景图
    private Paint selectPaint;
    private Paint textPaint;

    private void init() {
        // lineStrokeWidth = getResources().getDimensionPixelOffset(R.dimen.line_stroke_width);
        lineStrokeWidth = getResources().getDimensionPixelOffset(R.dimen.line_weight_stroke_width);

        textMargin = getResources().getDimensionPixelSize(R.dimen.float_text_margin);

        mLongPressRunnable = new Runnable() {

            @Override
            public void run() {

                System.out.println("thread");
                System.out.println("mCounter--->>>" + mCounter);
                System.out.println("isReleased--->>>" + isReleased);
                mCounter--;
                // 计数器大于0，说明当前执行的Runnable不是最后一次down产生的。
                if (mCounter > 0 || isReleased)
                    return;
                isTouch = true;
            }
        };
        initPaint();
    }

    private void initPaint() {

        this.mLinePaint = new Paint();
        this.mLinePaint.setAntiAlias(true);
        this.fillPaint = new Paint();

        this.fillPath = new Path();

        this.fingerPaint = new Paint();
        this.fingerPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.blue_line));
        this.fingerPaint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.line_ten_width));

        this.selectPaint = new Paint();

        this.textPaint = new Paint();
    }

    public void setSmallLine() {
        lineStrokeWidth = getResources().getDimensionPixelOffset(R.dimen.line_stroke_width);
    }

    public void setBoldLine() {
        lineStrokeWidth = getResources().getDimensionPixelOffset(R.dimen.line_weight_stroke_width);
    }

    /** 当前被选中的坐标点 */
    private PointF touchPoint;
    float timeX = 0;
    float timeY = 0;
    private boolean isTouch;

    private long firstClick;
    private long lastClick;
    // 计算点击的次数
    private int count;

    /**
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moves = true;
                timeX = event.getX();
                timeY = event.getY();
                if (null != mTouchListener) {
                    mTouchListener.chartTounching();
                }
                // mScrollview.setIsfocus(true);
                getParent().requestDisallowInterceptTouchEvent(true);

                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                            if (moves) {
                                mCounter++;
                                isTouch = true;
                                moves = false;
                                if (null != mTouchListener) {
                                    mTouchListener.chartTounching();
                                }
                                // mScrollview.setIsfocus(true);
                                getParent().requestDisallowInterceptTouchEvent(true);
                            }
                        } catch (Exception e) {

                        }
                    }
                });
                t.start();

                if (event.getPointerCount() == 1) {
                    // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
                    if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
                        count = 0;
                    }

                    count++;
                    if (count == 1) {
                        firstClick = System.currentTimeMillis();
                    } else if (count == 2) {
                        lastClick = System.currentTimeMillis();
                        // 两次点击小于300ms 也就是连续点击
                        if (lastClick - firstClick < 300) {// 判断是否是执行了双击事件
                            if (null != mDoubleClicklistener) {
                                mDoubleClicklistener.OnDoubleClick(this);
                            }
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float horizontalSpacing = event.getX() - timeX;
                float hor = event.getY() - timeY;
                if (Math.abs(horizontalSpacing) > 15 || Math.abs(hor) > 15 && !isTouch) {
                    moves = false;
                    if (null != mTouchListener) {
                        mTouchListener.loseTouching();
                    }
                    // mScrollview.setIsfocus(false);
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (moves && !isTouch) {
                    // Intent intent = KChartLandScapeActivity.newIntent(context, mStockBean, 0);
                    // context.startActivity(intent);
                    if (null != callBack) {
                        callBack.stockMarkShow();
                    }
                }
                isTouch = false;
                moves = false;
                // 释放了
                isReleased = true;
                if (null != mTouchListener) {
                    mTouchListener.loseTouching();
                }
                // mScrollview.setIsfocus(false);
                getParent().requestDisallowInterceptTouchEvent(false);
                removeCallbacks(mLongPressRunnable);

                break;

            default:
                break;
        }
        if (isTouch) {
            /*
             * if (event.getY() > 0 && event.getY() < super.getBottom() - getAxisMarginBottom()
             * && event.getX() > super.getLeft() + getAxisMarginLeft() && event.getX() < super.getRight()) {
             */

            /*
             * 判定用户是否触摸到�?���?如果是单点触摸则�?��绘制十字线 如果是2点触控则�?��K线放大
             */
            if (event.getPointerCount() == 1) {
                if (null != mTouchListener) {
                    mTouchListener.chartTounching();
                }
                // mScrollview.setIsfocus(true);
                getParent().requestDisallowInterceptTouchEvent(true);
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
            // }
        }

        // return super.onTouchEvent(event);
        return true;
    }

    private boolean isTrendChartMeasure = true;

    private float midPointx;
    private int viewLength;
    private int viewHeight;
    private int margin;
    private float startX;
    private float marginTop;

    @Override
    protected void onDraw(Canvas canvas) {
        // mCanvas = canvas;
        super.onDraw(canvas);
        if (this.isTrendChartMeasure) {
            this.isTrendChartMeasure = false;
            startPointX = mStartLineXpoint + 2;
            endY = mGridLineHeight - axisMarginBottom;
            midPointx = (super.getWidth() / 2.0f) + super.getAxisMarginLeft();

            if (isDrawTimesharingplanChart) {
                viewHeight = 0;
                viewLength = getResources().getDimensionPixelOffset(R.dimen.float_fs_view_width);
            } else if (isDrawTrendChart) {
                viewHeight = getResources().getDimensionPixelOffset(R.dimen.float_fs_view_hight) / 3 * 2;
                viewLength = getResources().getDimensionPixelOffset(R.dimen.float_trend_view_width);
            }

            viewLength = getResources().getDimensionPixelOffset(R.dimen.float_trend_view_width);
            viewHeight = getResources().getDimensionPixelOffset(R.dimen.float_view_hight);
            margin = getResources().getDimensionPixelSize(R.dimen.float_view_top_margin);
            marginTop = margin + axisMarginTop;
        }
        mLinePaint.setStrokeWidth(lineStrokeWidth);
        // 绘制平线
        drawLines(canvas);

        drawTimesSharingChart(canvas);

        drawFingerTouch(canvas);
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    protected void drawTimesSharingChart(Canvas canvas) {

    }

    protected void drawFingerTouch(Canvas canvas) {
        if (isTouch) {
            getTouchPointData(canvas);

        }
    }

    private Paint fingerPaint;

    /**
     * 单点击事件
     */
    protected void drawWithFingerClick(Canvas canvas, int pointIndex) {

        // 水平线长度
        float lineHLength = getWidth() - 2f;
        // 垂直线高度
        float lineVLength = super.getmGridLineHeight() - axisMarginBottom;

        if (isDisplayAxisYTitle()) {
            lineHLength = lineHLength - getAxisMarginLeft();

        }

        if (clickPostX > 0 && clickPostY > 0 && clickPostX < (mStartLineXpoint + mGridLineLenght)) {
            if (!isTouch) {
                fingerPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.blue_line));
            }
            // 显示纵线
            // if (displayCrossXOnTouch) {
            //
            // canvas.drawLine(clickPostX, axisMarginTop + xTitleTextHeight / 2, clickPostX, lineVLength, mPaint);
            canvas.drawLine(clickPostX, mStartLineYpoint, clickPostX, lineVLength, fingerPaint);

            float value = ((LinePointEntity) lineData.get(0).getLineData().get(pointIndex)).getValue();

            float hightPrecent = 0;
            if (this.getMaxValue() == this.getMinValue()) {
                hightPrecent = 0.5f;
            } else {

                hightPrecent = (1f - (value - this.getMinValue()) / (this.getMaxValue() - this.getMinValue()));
            }
            float valueY = (float) (hightPrecent * (lineHeight));
            valueY += mStartLineYpoint;
            canvas.drawLine(axisMarginLeft, valueY, mGridLineLenght + axisMarginLeft, valueY, fingerPaint);

        }

        invalidate();
    }

    private float lineHeight;
    private Path fillPath;

    protected void drawLines(Canvas canvas) {
        // lineLength = (super.getWidth() - startPointX - super.getAxisMarginRight());
        // 点线距离
        pointLineLength = ((mGridLineLenght - 2) / (this.getMaxPointNum() - 1)) - 1;

        // 起始位置
        float startX;

        // float lineHeight = super.getHeight() - axisMarginTop - super.getAxisMarginBottom() - xTitleTextHeight;
        lineHeight = mGridLineHeight - axisMarginBottom - 2 - mStartLineYpoint;

        // Paint fillPaint = new Paint();
        fillPaint.reset();

        if (null == lineData) {
            return;
        }
        // 逐条输入MA线
        int lineSize = lineData.size();
        for (int i = 0; i < lineSize; i++) {
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
                // = new Path();
                fillPath.reset();

                // }
                if (lineData != null && lineData.size() > 0) {
                    for (int j = 0; j < lineData.size(); j++) {
                        // j=1,value=272
                        if (j >= getMaxPointNum()) {
                            break;
                        }
                        float value = lineData.get(j).getValue();
                        if (value > this.getMaxValue()) {
                            value = this.getMaxValue();
                        } else if (value < this.getMinValue()) {
                            value = this.getMinValue();
                        }
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
                        if (isFromCompare) {
                            if (j < dashLinePointSize && i == 0) {
                                mLinePaint.setPathEffect(dashEffect);
                            } else {
                                mLinePaint.setPathEffect(null);
                            }
                        } else {

                            if (j < dashLinePointSize) {
                                mLinePaint.setPathEffect(dashEffect);
                            } else {
                                mLinePaint.setPathEffect(null);
                            }
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
                            fillPaint.setAlpha(50);
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

    private void getTouchPointData(Canvas canvas) {
        if (getTouchPoint() != null && null != lineData && lineData.size() > 0) {
            // float fPointIndex = ((getTouchPoint().x - super.getAxisMarginLeft() - pointLineLength / 2f) /
            // (pointLineLength + 1));
            float fPointIndex = ((getTouchPoint().x - super.getAxisMarginLeft()) / (pointLineLength + 1));
            int pointIndex = Math.round(fPointIndex);
            LineEntity lineEntity = lineData.get(0);
            int maxPointSize = lineEntity.getLineData().size();
            if (pointIndex < maxPointSize && pointIndex >= 0) {
                drawWithFingerClick(canvas, pointIndex);
                if (isDrawTimesharingplanChart) {
                    drawTimesharingInfo(canvas, pointIndex);
                } else if (isDrawTrendChart) {
                    drawTrendChartInfo(canvas, pointIndex);
                } else {

                    if (lineData.size() == 1) {

                        // pointIndex = maxPointSize - 1;

                        drawSingleDataView(canvas, pointIndex);

                    } else {
                        drawDataView(canvas, pointIndex);
                    }
                }

            }
            // drawDataView(canvas);

        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void drawTimesharingInfo(Canvas canvas, int pointIndex) {
        // 当触摸点在左边
        if (getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + margin;

        } else {
            startX = super.getWidth() - viewLength - margin;

        }

        Rect rects = new Rect();

        selectPaint.reset();
        selectPaint.setTextSize(getLatitudeFontSize());
        selectPaint.getTextBounds("正", 0, "正".length(), rects);
        int textTextHeight = rects.height();

        // if (lineData.size() > 3) {
        viewHeight = (textTextHeight + textMargin) * (6) + textMargin;
        // 设置悬浮框高度（国内指数高为5，其它全6）
        if ((LinePointEntity) lineData.get(0).getLineData().get(pointIndex) instanceof FSLinePointEntity) {
            FSLinePointEntity fsPoints = (FSLinePointEntity) ((LinePointEntity) lineData.get(0).getLineData()
                    .get(pointIndex));
            if (fsPoints.isIndexType()) {
                viewHeight = (textTextHeight + textMargin) * (5) + textMargin;
            }
        }

        // }
        selectPaint.reset();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));

        RectF oval3 = new RectF(startX, marginTop, startX + viewLength, marginTop + viewHeight + 5);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        selectPaint.reset();
        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/

        float preYpoint = textTextHeight + textMargin + marginTop;
        selectPaint.reset();
        selectPaint.setColor(Color.BLACK);
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(getLatitudeFontSize());
        String firtLineText;

        LineEntity lineentity = lineData.get(0);
        LinePointEntity data = (LinePointEntity) lineentity.getLineData().get(pointIndex);
        try {

            if (data instanceof FSLinePointEntity) {
                FSLinePointEntity fsPoint = (FSLinePointEntity) data;
                float textMarginLeft = startX + textMargin;
                canvas.drawText(fsPoint.getTime(), textMarginLeft, preYpoint, selectPaint);
                preYpoint += textMargin + textTextHeight;
                canvas.drawText(fsPoint.getPrice(), textMarginLeft, preYpoint, selectPaint);
                if (!fsPoint.isIndexType()) {

                    preYpoint += textMargin + textTextHeight;
                    canvas.drawText(fsPoint.getAvgPriceDesc(), textMarginLeft, preYpoint, selectPaint);
                }

                preYpoint += textMargin + textTextHeight;
                canvas.drawText(fsPoint.getIncreaseValueDesc(), textMarginLeft, preYpoint, selectPaint);

                preYpoint += textMargin + textTextHeight;
                canvas.drawText(fsPoint.getIncreaseRangeDesc(), textMarginLeft, preYpoint, selectPaint);

                preYpoint += textMargin + textTextHeight;
                canvas.drawText(fsPoint.getTurnoverDesc(), textMarginLeft, preYpoint, selectPaint);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return void
     */
    private void drawTrendChartInfo(Canvas canvas, int pointIndex) {
        // 当触摸点在左边
        if (getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + margin;

        } else {
            startX = super.getWidth() - viewLength - margin;

        }
        selectPaint.reset();
        Rect rects = new Rect();
        selectPaint.setTextSize(getLatitudeFontSize());
        selectPaint.getTextBounds("正", 0, "正".length(), rects);
        int textTextHeight = rects.height();
        viewHeight = (textTextHeight + textMargin) * 3 + textMargin;
        if (lineData.size() > 3) {
            viewHeight = (textTextHeight + textMargin) * (lineData.size() + 1) + textMargin;
        }

        selectPaint.reset();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));

        RectF oval3 = new RectF(startX, marginTop - 2, startX + viewLength, marginTop + viewHeight + 5);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径
        selectPaint.reset();
        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/

        float preYpoint = textTextHeight + textMargin + marginTop;
        selectPaint.reset();
        selectPaint.setColor(Color.BLACK);
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(getLatitudeFontSize());
        String firtLineText;

        LineEntity lineentity = lineData.get(0);
        LinePointEntity data = (LinePointEntity) lineentity.getLineData().get(pointIndex);
        if (data instanceof TrendLinePointEntity) {
            TrendLinePointEntity fsPoint = (TrendLinePointEntity) data;
            canvas.drawText(fsPoint.getTime(), startX + textMargin, preYpoint, selectPaint);
            preYpoint += textMargin + textTextHeight;
            selectPaint.setColor(lineentity.getLineColor());
            canvas.drawText(fsPoint.getDataDesc(), startX + textMargin, preYpoint, selectPaint);
            preYpoint += textMargin + textTextHeight;
            if (fsPoint.getIncreaseRange() > 0) {

                selectPaint.setColor(ColorTemplate.DEF_RED);
            } else if (fsPoint.getIncreaseRange() < 0) {

                selectPaint.setColor(ColorTemplate.DEF_GREEN);
            } else {
                selectPaint.setColor(ColorTemplate.DEF_GRAY);

            }
            canvas.drawText(fsPoint.getIncreaseRangeDesc(), startX + textMargin, preYpoint, selectPaint);

        }

    }

    public void refreshClear() {
        setTouch(false);
        invalidate();
    }

    private List<String> pointTitleList;

    public void setPointTitleList(List<String> pTitleList) {
        this.pointTitleList = pTitleList;
    }

    // clearRect(x,y,width,height) ‒ clears the given area and makes it fully opaque

    /**
     * 
     * 描述走势图触摸时信息
     */
    private void drawSingleDataView(Canvas canvas, int pointIndex) {

        // 当触摸点在左边
        if (getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + margin;

        } else {
            startX = super.getWidth() - viewLength - margin;

        }

        // 创建画笔 画背景图
        selectPaint.reset();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));

        RectF oval3 = new RectF(startX, marginTop - 2, startX + viewLength, marginTop + viewHeight + 5);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/

        selectPaint.reset();
        Rect rects = new Rect();
        selectPaint.setTextSize(getLatitudeFontSize());
        selectPaint.getTextBounds("正", 0, "正".length(), rects);
        int textTextHeight = rects.height();
        float preYpoint = textTextHeight + textMargin + marginTop;
        selectPaint.reset();
        selectPaint.setColor(Color.BLACK);
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(getLatitudeFontSize());
        String firtLineText;
        LineEntity lineentity = lineData.get(0);
        LinePointEntity data = (LinePointEntity) lineentity.getLineData().get(pointIndex);
        if (null != pointTitleList && pointTitleList.size() > 0) {
            firtLineText = pointTitleList.get(0) + ":" + data.getDesc();
        } else {
            firtLineText = "日期：" + data.getDesc();
        }
        canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);

        if (pointIndex < lineentity.getLineData().size()) {

            selectPaint.setColor(lineentity.getLineColor());
            preYpoint += textMargin + textTextHeight;
            String text = "";
            if (TextUtils.isEmpty(lineentity.getTitle())) {

                if (null != pointTitleList && pointTitleList.size() > 1) {
                    text = pointTitleList.get(1)
                            + "："
                            + StringFromatUtils.get4Point(((LinePointEntity) lineentity.getLineData().get(pointIndex))
                                    .getValue());
                } else {
                    text = StringFromatUtils.get4Point(((LinePointEntity) lineentity.getLineData().get(pointIndex))
                            .getValue());
                }

            }
            canvas.drawText(text, startX + textMargin, preYpoint, selectPaint);

        }

    }

    /**
     * 
     * 描述业绩比较时触摸时信息
     */
    protected void drawDataView(Canvas canvas, int pointIndex) {

        // = margin;
        // 当触摸点在左边
        if (getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + margin;

        } else {
            startX = super.getWidth() - viewLength - margin;

        }

        selectPaint.reset();
        Rect rects = new Rect();
        selectPaint.setTextSize(getLatitudeFontSize());
        selectPaint.getTextBounds("正", 0, "正".length(), rects);
        int textTextHeight = rects.height();
        int size = 0;
        for (int i = 0; i < lineData.size(); i++) {
            if (lineData.get(i).isDisplay()) {
                size++;
            }
        }
        if (lineData.size() > 2) {
            viewHeight = (textTextHeight + textMargin) * (size + 1) + textMargin;
        } else if (lineData.size() == 2) {
            viewHeight = (textTextHeight + textMargin) * (size + 1) + textMargin;
        }

        selectPaint.reset();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));

        RectF oval3 = new RectF(startX, marginTop - 2, startX + viewLength, marginTop + viewHeight + 5);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/

        float preYpoint = textTextHeight + textMargin + marginTop;
        selectPaint.reset();
        selectPaint.setColor(Color.BLACK);
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(getLatitudeFontSize());
        String firtLineText = "";
        // if (null != pointTitleList && pointTitleList.size() > 0) {
        // firtLineText = pointTitleList.get(0) + ":" + date.getDesc();
        if (lineData.size() > 0 && lineData.get(0).getLineData().size() > 0) {
            firtLineText = "日期：" + ((LinePointEntity) lineData.get(0).getLineData().get(pointIndex)).getDesc();
        }
        canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);

        // FontMetrics fontMetrics = selectPaint.getFontMetrics();
        // float preTextBottom = fontMetrics.bottom;
        int lineLength = lineData.size();
        for (int i = 0; i < lineLength; i++) {
            if (pointIndex < lineData.get(i).getLineData().size() && lineData.get(i).isDisplay()) {

                selectPaint.setColor(lineData.get(i).getLineColor());
                preYpoint += textMargin + textTextHeight;
                String text = "";
                // if (i == 0) {
                if (TextUtils.isEmpty(lineData.get(i).getTitle())) {
                    // text = StringFromatUtils.get4Point(lineData.get(i).getLineData().get(pointIndex).getNetvalue());

                    if (null != pointTitleList && pointTitleList.size() > 1) {
                        text = pointTitleList.get(1)
                                + "："
                                + StringFromatUtils.get4Point(((LinePointEntity) lineData.get(i).getLineData()
                                        .get(pointIndex)).getValue());
                    } else {
                        text = StringFromatUtils.get4Point(((LinePointEntity) lineData.get(i).getLineData()
                                .get(pointIndex)).getValue());
                    }
                } else {

                    text = lineData.get(i).getTitle()
                            + "："
                            + StringFromatUtils.get2PointPercent(((LinePointEntity) lineData.get(i).getLineData()
                                    .get(pointIndex)).getValue());
                }
                // } else if (i == 1) {
                // text = "沪深300:1.43%";
                // } else {
                // text = "创业板:1.40%";
                // }
                // if (i == 0)
                canvas.drawText(text, startX + textMargin, preYpoint, selectPaint);
            }
        }

        /***************/

    }

    private ITouchListener mTouchListener;

    public void setITouchListener(ITouchListener touchListener) {
        this.mTouchListener = touchListener;
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

    public int getDashLinePointSize() {
        return dashLinePointSize;
    }

    public void setDashLinePointSize(int dashLinePointSize) {
        this.dashLinePointSize = dashLinePointSize;
    }

    public boolean isDrawFirstLineInfo() {
        return isDrawTimesharingplanChart;
    }

    public void setDrawFirstLineInfo(boolean isDrawFirstLineInfo) {
        this.isDrawTimesharingplanChart = isDrawFirstLineInfo;
    }

    public int getLineStrokeWidth() {
        return lineStrokeWidth;
    }

    public void setLineStrokeWidth(int lineStrokeWidth) {
        this.lineStrokeWidth = lineStrokeWidth;
    }

    public boolean isFromCompare() {
        return isFromCompare;
    }

    public void setFromCompare(boolean isFromCompare) {
        this.isFromCompare = isFromCompare;
    }

    public boolean isDrawTrendChart() {
        return isDrawTrendChart;
    }

    public void setDrawTrendChart(boolean isDrawTrendChart) {
        this.isDrawTrendChart = isDrawTrendChart;
    }

    // public boolean isDrawDashLine() {
    // return isDrawDashLine;
    // }
    //
    // public void setDrawDashLine(boolean isDrawDashLine) {
    // this.isDrawDashLine = isDrawDashLine;
    // }
    public void setContext(Context context) {
        this.context = context;
    }

    //
    // public SelectStockBean getmStockBean() {
    // return mStockBean;
    // }
    //
    // public void setmStockBean(SelectStockBean mStockBean) {
    // this.mStockBean = mStockBean;
    // }

    private OnDoubleClickListener mDoubleClicklistener;

    public OnDoubleClickListener getDoubleClicklistener() {
        return mDoubleClicklistener;
    }

    public void setDoubleClicklistener(OnDoubleClickListener mDoubleClicklistener) {
        this.mDoubleClicklistener = mDoubleClicklistener;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     */
    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        removeCallbacks(mLongPressRunnable);
    }

    public StockViewCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(StockViewCallBack callBack) {
        this.callBack = callBack;
    }

}
