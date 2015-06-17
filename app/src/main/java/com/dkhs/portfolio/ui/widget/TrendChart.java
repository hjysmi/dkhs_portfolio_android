package com.dkhs.portfolio.ui.widget;

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
import android.util.Log;
import android.view.MotionEvent;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.widget.LinePoint.DefFundPointEntity;
import com.dkhs.portfolio.ui.widget.LinePoint.FundLinePointEntity;
import com.dkhs.portfolio.ui.widget.LinePoint.LinePointEntity;
import com.dkhs.portfolio.ui.widget.LinePoint.SepFundPointEntity;
import com.dkhs.portfolio.ui.widget.LinePoint.TrendLinePointEntity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrendChart extends TrendGridChart {
    /**
     * 显示数据线
     */
    List<LineEntity> lineData;

    /**
     * 最大点数
     */
    private int maxPointNum;

    /**
     * 最低价格
     */
    private float minValue;

    /**
     * 最高价格
     */
    private float maxValue;

    /**
     * 经线是否使用虚线
     */
    private boolean dashLongitude = Boolean.FALSE;
    /**
     * 虚线效果
     */
    private PathEffect dashEffect = DEFAULT_DASH_EFFECT;

    ArrayList<HashMap<String, Object>> listItem;

    private float pointLineLength;

    private float lineLength;

    private Paint mLinePaint;
    private Paint fillPaint;
    private Paint defPaint;
    private boolean isTouchAble;
    private float startPointX;
    private float endY;
    private int lineStrokeWidth;
    /**
     * 选中位置X坐标
     */
    private float clickPostX = 0f;

    /**
     * 选中位置Y坐标
     */
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

//        this.mLinePaint = new Paint();
        this.defPaint = new Paint();
        this.mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        this.mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStyle(Paint.Style.STROKE);
        this.fillPaint = new Paint();

        this.fillPath = new Path();

        this.fingerPaint = new Paint();
        this.fingerPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.blue_line));
        this.fingerPaint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.line_ten_width));

        this.selectPaint = new Paint();

        this.textPaint = new Paint();
        Rect rects = new Rect();
        selectPaint.setTextSize(getLatitudeFontSize());
        selectPaint.getTextBounds("正", 0, "正".length(), rects);
        floatTextHeight = rects.height();
    }

    public void setSmallLine() {
        lineStrokeWidth = getResources().getDimensionPixelOffset(R.dimen.line_stroke_width);
    }

    public void setBoldLine() {
        lineStrokeWidth = getResources().getDimensionPixelOffset(R.dimen.line_weight_stroke_width);
    }

    /**
     * 当前被选中的坐标点
     */
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
                    // if (null != mTouchListener) {
                    // mTouchListener.loseTouching();
                    // }
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
                // if (null != mTouchListener) {
                // mTouchListener.loseTouching();
                // }
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
                // if (null != mTouchListener) {
                // mTouchListener.chartTounching();
                // }
                // mScrollview.setIsfocus(true);
                getParent().requestDisallowInterceptTouchEvent(true);
                // 获取点击坐标
                clickPostX = event.getX();
                clickPostY = event.getY();

                touchPoint = new PointF(clickPostX, clickPostY);
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
    private int mFloatViewWidth;
    private int mFloatViewHeight;
    private int mFloatViewMargin;
    private float startX;
    private float mFloatViewMarginTop;

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
                mFloatViewHeight = 0;
                mFloatViewWidth = getResources().getDimensionPixelOffset(R.dimen.float_fs_view_width);
            } else if (isDrawTrendChart) {
                mFloatViewHeight = getResources().getDimensionPixelOffset(R.dimen.float_fs_view_hight) / 3 * 2;
                mFloatViewWidth = getResources().getDimensionPixelOffset(R.dimen.float_trend_view_width);
            } else {

                mFloatViewWidth = getResources().getDimensionPixelOffset(R.dimen.float_trend_view_width);
                mFloatViewHeight = getResources().getDimensionPixelOffset(R.dimen.float_view_hight);
            }
            mFloatViewMargin = getResources().getDimensionPixelSize(R.dimen.float_view_top_margin);
            mFloatViewMarginTop = mFloatViewMargin + axisMarginTop;
        }
        mLinePaint.setStrokeWidth(lineStrokeWidth);
        // 绘制平线
        drawLines(canvas);

        super.reDrawYtitleText(canvas);


        drawTimesSharingChart(canvas);

        drawFingerTouch(canvas);
    }

    /**
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
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
        float lineHLength = getWidth() - lineStrokeWidth;
        // 垂直线高度
        float lineVLength = super.getmGridLineHeight() - axisMarginBottom;

        if (isDisplayAxisYTitle()) {
            lineHLength = lineHLength - getAxisMarginLeft();

        }

        if (clickPostX > mStartLineXpoint && clickPostY > 0 && clickPostX < (mStartLineXpoint + mGridLineLenght)) {
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
        pointLineLength = ((mGridLineLenght - lineStrokeWidth) / (this.getMaxPointNum() - 1));

        // 起始位置
        float startX;

        lineHeight = mGridLineHeight - axisMarginBottom - 2 - mStartLineYpoint;
        //如果需要控制画线的上下区域不超出矩形的范围，还需要减去线宽
//        lineHeight = mGridLineHeight - axisMarginBottom - lineStrokeWidth - mStartLineYpoint;

        fillPaint.reset();
        Path linePath = new Path();
        if (null == lineData) {
            return;
        }
        // 逐条输入MA线
        int lineSize = lineData.size();
        for (int i = 0; i < lineSize; i++) {
            LineEntity line = (LineEntity) lineData.get(i);
            linePath.reset();
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

                        LinePointEntity pointEntity = lineData.get(j);

                        if (j >= getMaxPointNum()) {
                            break;
                        }
                        float value = pointEntity.getValue();
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

//                            canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, mLinePaint);
                            linePath.lineTo(startX, valueY);
                        } else {
                            linePath.moveTo(startX, valueY);
                        }

                        if (pointEntity instanceof FundLinePointEntity && !TextUtils.isEmpty(((FundLinePointEntity) pointEntity).getInfo())) {
                            // Paint p = new Paint();
                            defPaint.reset();
                            defPaint.setAntiAlias(true);
                            defPaint.setStyle(Paint.Style.FILL);
                            defPaint.setColor(getResources().getColor(R.color.ma10_color));
                            float circleRadius = 0;

                            circleRadius = 5f;

                            canvas.drawCircle(startX, (float) (lineHeight - circleRadius * 2), circleRadius,
                                    defPaint);
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
                        startX = startX + pointLineLength;
                    }

                    canvas.drawPath(linePath, mLinePaint);
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
            float fPointIndex = ((getTouchPoint().x - super.getAxisMarginLeft()) / (pointLineLength));
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
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void drawTimesharingInfo(Canvas canvas, int pointIndex) {
        // 当触摸点在左边
        if (getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + mFloatViewMargin;

        } else {
            startX = super.getWidth() - mFloatViewWidth - mFloatViewMargin;

        }

        Rect rects = new Rect();

        selectPaint.reset();
        selectPaint.setTextSize(getLatitudeFontSize());
        selectPaint.getTextBounds("正", 0, "正".length(), rects);
        int textTextHeight = rects.height();

        // if (lineData.size() > 3) {
        mFloatViewHeight = (textTextHeight + textMargin) * (6) + textMargin;
        // 设置悬浮框高度（国内指数高为5，其它全6）
        if ((LinePointEntity) lineData.get(0).getLineData().get(pointIndex) instanceof FSLinePointEntity) {
            FSLinePointEntity fsPoints = (FSLinePointEntity) ((LinePointEntity) lineData.get(0).getLineData()
                    .get(pointIndex));
            if (fsPoints.isIndexType()) {
                mFloatViewHeight = (textTextHeight + textMargin) * (5) + textMargin;
            }
        }

        // }
        selectPaint.reset();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));

        RectF oval3 = new RectF(startX, mFloatViewMarginTop, startX + mFloatViewWidth, mFloatViewMarginTop + mFloatViewHeight + 5);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        selectPaint.reset();
        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/

        float preYpoint = textTextHeight + textMargin + mFloatViewMarginTop;
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
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    private void drawTrendChartInfo(Canvas canvas, int pointIndex) {
        // 当触摸点在左边
        if (getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + mFloatViewMargin;

        } else {
            startX = super.getWidth() - mFloatViewWidth - mFloatViewMargin;

        }
        selectPaint.reset();
        Rect rects = new Rect();
        selectPaint.setTextSize(getLatitudeFontSize());
        selectPaint.getTextBounds("正", 0, "正".length(), rects);
        int textTextHeight = rects.height();
        mFloatViewHeight = (textTextHeight + textMargin) * 3 + textMargin;
        if (lineData.size() > 3) {
            mFloatViewHeight = (textTextHeight + textMargin) * (lineData.size() + 1) + textMargin;
        }

        selectPaint.reset();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));

        RectF oval3 = new RectF(startX, mFloatViewMarginTop - 2, startX + mFloatViewWidth, mFloatViewMarginTop + mFloatViewHeight + 5);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径
        selectPaint.reset();
        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/

        float preYpoint = textTextHeight + textMargin + mFloatViewMarginTop;
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

    private float borderEnd;

    private int floatTextHeight;

    private void drawFloatView(Canvas canvas) {
        // 当触摸点在左边
        if (getTouchPoint().x > midPointx) {
            startX = getAxisMarginLeft() + mFloatViewMargin;

        } else {
            startX = super.getWidth() - mFloatViewWidth - mFloatViewMargin;

        }

        borderEnd = startX + mFloatViewWidth - textMargin;

        // 创建画笔 画背景图
        selectPaint.reset();
        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        selectPaint.setStyle(Paint.Style.FILL);// 充满
        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));

        RectF oval3 = new RectF(startX, mFloatViewMarginTop - 2, startX + mFloatViewWidth, mFloatViewMarginTop + mFloatViewHeight + 5);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        selectPaint.setStyle(Paint.Style.STROKE);// 描边
        selectPaint.setStrokeWidth(2);
        selectPaint.setColor(Color.LTGRAY);
        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/
        selectPaint.reset();
        Rect rects = new Rect();
        selectPaint.setTextSize(getLatitudeFontSize());
        selectPaint.getTextBounds("正", 0, "正".length(), rects);
        floatTextHeight = rects.height();

    }


    /**
     * 描述走势图触摸时信息
     */
    private void drawSingleDataView(Canvas canvas, int pointIndex) {

        LineEntity lineentity = lineData.get(0);
        LinePointEntity data = (LinePointEntity) lineentity.getLineData().get(pointIndex);
        if (data instanceof SepFundPointEntity) {
            //显示基金经理
            mFloatViewHeight = (floatTextHeight + textMargin) * (3) + textMargin;
            if (!TextUtils.isEmpty(((SepFundPointEntity) data).getInfo())) {
                mFloatViewHeight = (floatTextHeight + textMargin) * (4) + textMargin;
            }
        }


        drawFloatView(canvas);

        /******* draw text ********/


        float preYpoint = floatTextHeight + textMargin + mFloatViewMarginTop;
        selectPaint.reset();
        selectPaint.setColor(Color.BLACK);
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(getLatitudeFontSize());


        String firtLineText;


        if (null != pointTitleList && pointTitleList.size() > 0) {
            firtLineText = pointTitleList.get(0) + ":";

            canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);
            float xTitleWidth = selectPaint.measureText(data.getDesc());
            canvas.drawText(data.getDesc(), borderEnd - xTitleWidth, preYpoint, selectPaint);
        } else {
            firtLineText = "日期：";
            canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);
            float xTitleWidth = selectPaint.measureText(data.getDesc());
            canvas.drawText(data.getDesc(), borderEnd - xTitleWidth, preYpoint, selectPaint);
        }


        if (pointIndex < lineentity.getLineData().size()) {


            if (data instanceof SepFundPointEntity) {
                selectPaint.setColor(Color.BLACK);
                preYpoint += textMargin + floatTextHeight;

                firtLineText = "万份收益：";
                canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);
                float xTitleWidth = selectPaint.measureText(((SepFundPointEntity) data).getNetvalue() + "");
                canvas.drawText(((SepFundPointEntity) data).getNetvalue() + "", borderEnd - xTitleWidth, preYpoint, selectPaint);

                preYpoint += textMargin + floatTextHeight;

                firtLineText = "七日年化：";
                canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);
                float percentValue = ((SepFundPointEntity) data).getValue();
                firtLineText = StringFromatUtils.get2PointPercent(percentValue);
//                selectPaint.setColor(ColorTemplate.getPercentColor(firtLineText));
                xTitleWidth = selectPaint.measureText(firtLineText);
                canvas.drawText(firtLineText, borderEnd - xTitleWidth, preYpoint, selectPaint);

                //显示基金经理
                if (!TextUtils.isEmpty(((SepFundPointEntity) data).getInfo())) {
                    String managerName = ((SepFundPointEntity) data).getInfo() + " 到任";
                    preYpoint += textMargin + floatTextHeight;
                    selectPaint.setColor(ColorTemplate.DEF_RED);
                    canvas.drawText(managerName, startX + textMargin, preYpoint, selectPaint);


                }


            } else {


                selectPaint.setColor(lineentity.getLineColor());
                preYpoint += textMargin + floatTextHeight;
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

    }


    public boolean isFundTrendCompare() {
        return isFundTrendCompare;
    }

    public void setIsFundTrendCompare(boolean isFundCompare) {
        this.isFundTrendCompare = isFundCompare;
    }

    private boolean isFundTrendCompare;


    /**
     * 描述业绩比较时触摸时信息
     */
    protected void drawDataView(Canvas canvas, int pointIndex) {

//        // 当触摸点在左边
//        if (getTouchPoint().x > midPointx) {
//            startX = getAxisMarginLeft() + mFloatViewMargin;
//
//        } else {
//            startX = super.getWidth() - mFloatViewWidth - mFloatViewMargin;
//
//        }
//
//        selectPaint.reset();
//        Rect rects = new Rect();
//        selectPaint.setTextSize(getLatitudeFontSize());
//        selectPaint.getTextBounds("正", 0, "正".length(), rects);
//        int textTextHeight = rects.height();
        int size = 0;
        for (int i = 0; i < lineData.size(); i++) {
            if (lineData.get(i).isDisplay()) {
                size++;
            }
        }
        if (lineData.size() > 2) {
            mFloatViewHeight = (floatTextHeight + textMargin) * (size + 1) + textMargin;
        } else if (lineData.size() == 2) {
            mFloatViewHeight = (floatTextHeight + textMargin) * (size + 1) + textMargin;
        }
        if (isFundTrendCompare) {

            mFloatViewHeight = (floatTextHeight + textMargin) * (size + 3) + textMargin;


            for (LineEntity lineEntity : lineData) {
                if (lineEntity instanceof DefFundLineEntity) {
                    LinePointEntity pointEntity = (LinePointEntity) lineEntity.getLineData().get(pointIndex);
                    String manager = ((DefFundPointEntity) pointEntity).getInfo() + "";
                    if (!TextUtils.isEmpty(manager)) {
                        mFloatViewHeight = (floatTextHeight + textMargin) * (size + 4) + textMargin;
                    }
                }


            }
        }
        drawFloatView(canvas);

//        selectPaint.reset();
//        selectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
//        selectPaint.setStyle(Paint.Style.FILL);// 充满
//        selectPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));
//
//        RectF oval3 = new RectF(startX, mFloatViewMarginTop - 2, startX + mFloatViewWidth, mFloatViewMarginTop + mFloatViewHeight + 5);// 设置个新的长方形
//        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径
//
//        selectPaint.setStyle(Paint.Style.STROKE);// 描边
//        selectPaint.setStrokeWidth(2);
//        selectPaint.setColor(Color.LTGRAY);
//        canvas.drawRoundRect(oval3, 20, 15, selectPaint);// 第二个参数是x半径，第三个参数是y半径

        /*********** End *************/

        /******* draw text ********/

        float preYpoint = floatTextHeight + textMargin + mFloatViewMarginTop;
        selectPaint.reset();
        selectPaint.setColor(Color.BLACK);
        selectPaint.setAntiAlias(true);
        selectPaint.setTextSize(getLatitudeFontSize());
        String firtLineText = "";
        // if (null != pointTitleList && pointTitleList.size() > 0) {
        // firtLineText = pointTitleList.get(0) + ":" + date.getDesc();
        float valueWidth;
        String valueText;

        LinePointEntity firstPoint = null;
        if (lineData.size() > 0 && lineData.get(0).getLineData().size() > 0) {
            firstPoint = (LinePointEntity) lineData.get(0).getLineData().get(0);
            firtLineText = "日期：";
            valueText = ((LinePointEntity) lineData.get(0).getLineData().get(pointIndex)).getDesc();
            valueWidth = selectPaint.measureText(valueText);
            canvas.drawText(valueText, borderEnd - valueWidth, preYpoint, selectPaint);

        }


        canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);


//        for (LineEntity lineEntity : lineData) {
//
//        }


        int lineLength = lineData.size();
        for (int i = 0; i < lineLength; i++) {
            LineEntity lineEntity = lineData.get(i);
            //显示零费率的净值文字
            if (lineEntity instanceof DefFundLineEntity) {

                LinePointEntity pointEntity = (LinePointEntity) lineEntity.getLineData().get(pointIndex);
                if (pointEntity instanceof DefFundPointEntity) {

                    Log.e("TTTTT", "  pointEntity instanceof DefFundPointEntity");

                    valueText = ((DefFundPointEntity) pointEntity).getNetvalue() + "";
                    preYpoint += textMargin + floatTextHeight;
                    firtLineText = "单位净值：";
                    canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);

                    valueWidth = selectPaint.measureText(valueText);
                    canvas.drawText(valueText, borderEnd - valueWidth, preYpoint, selectPaint);

                    valueText = ((DefFundPointEntity) pointEntity).getNet_cumulative() + "";
                    preYpoint += textMargin + floatTextHeight;
                    firtLineText = "累计净值：";
                    canvas.drawText(firtLineText, startX + textMargin, preYpoint, selectPaint);

                    valueWidth = selectPaint.measureText(valueText);
                    canvas.drawText(valueText, borderEnd - valueWidth, preYpoint, selectPaint);
                }


            }

            if (pointIndex < lineEntity.getLineData().size() && lineEntity.isDisplay()) {
                selectPaint.setColor(lineEntity.getLineColor());
                preYpoint += textMargin + floatTextHeight;
                String text = "";
                // if (i == 0) {
                if (TextUtils.isEmpty(lineEntity.getTitle())) {
                    // text = StringFromatUtils.get4Point(lineData.get(i).getLineData().get(pointIndex).getNetvalue());

                    if (null != pointTitleList && pointTitleList.size() > 1) {
                        text = pointTitleList.get(1) + "：";


                        canvas.drawText(text, startX + textMargin, preYpoint, selectPaint);

                        valueText = StringFromatUtils.get4Point(((LinePointEntity) lineEntity.getLineData()
                                .get(pointIndex)).getValue());
                        valueWidth = selectPaint.measureText(valueText);
                        canvas.drawText(valueText, borderEnd - valueWidth, preYpoint, selectPaint);


                    } else {
                        text = StringFromatUtils.get4Point(((LinePointEntity) lineEntity.getLineData()
                                .get(pointIndex)).getValue());

                        canvas.drawText(text, startX + textMargin, preYpoint, selectPaint);
                    }
                } else {

                    text = lineEntity.getTitle()
                            + "：";


                    valueText = StringFromatUtils.get2PointPercent(((LinePointEntity) lineEntity.getLineData()
                            .get(pointIndex)).getValue());
                    valueWidth = selectPaint.measureText(valueText);
                    canvas.drawText(text, startX + textMargin, preYpoint, selectPaint);
                    canvas.drawText(valueText, borderEnd - valueWidth, preYpoint, selectPaint);
                }


            }


        }


        //显示基金经理的文字
        for (LineEntity lineEntity : lineData) {

            if (lineEntity instanceof DefFundLineEntity) {

                LinePointEntity pointEntity = (LinePointEntity) lineEntity.getLineData().get(pointIndex);
                if (pointEntity instanceof DefFundPointEntity) {
                    if (!TextUtils.isEmpty(((DefFundPointEntity) pointEntity).getInfo())) {
                        String managerName = ((DefFundPointEntity) pointEntity).getInfo() + " 到任";
                        preYpoint += textMargin + floatTextHeight;
                        selectPaint.setColor(ColorTemplate.DEF_RED);
                        canvas.drawText(managerName, startX + textMargin, preYpoint, selectPaint);

                    }

                }
            }

        }
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
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
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
