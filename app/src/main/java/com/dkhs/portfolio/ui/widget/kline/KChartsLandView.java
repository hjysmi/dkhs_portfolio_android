package com.dkhs.portfolio.ui.widget.kline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.ui.widget.KChartsLandCallBack;
import com.dkhs.portfolio.ui.widget.chart.StickChart;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class KChartsLandView extends GridChart implements GridChart.OnTabClickListener {

    /**
     * 触摸模式
     */
    private static int TOUCH_MODE;
    private final static int NONE = 0;
    private final static int DOWN = 1;
    private final static int MOVE = 2;
    private final static int ZOOM = 3;

    /**
     * 默认Y轴字体颜色 *
     */
    private static final int DEFAULT_AXIS_Y_TITLE_COLOR = Color.GRAY;

    /**
     * 默认X轴字体颜色 *
     */
    private static final int DEFAULT_AXIS_X_TITLE_COLOR = Color.GRAY;

    /**
     * 显示的最小Candle数
     */
    private final static int MIN_CANDLE_NUM = 50;

    /**
     * 默认显示的Candle数
     */
    private final static int DEFAULT_CANDLE_NUM = 50;
    /**
     * 显示最多的candle数
     */
    private final static int MAX_CANDLE_NUM = 300;
    /**
     * 最小可识别的移动距离
     */
    private final static int MIN_MOVE_DISTANCE = 15;
    protected static final String TAG = "KChartsLandView";

    /**
     * Candle宽度
     */
    private float mCandleWidth;

    /**
     * 触摸点
     */
    private float mStartX = 6;
    private float mStartY;

    /**
     * OHLC数据
     */
    private List<OHLCEntity> mOHLCData;

    /**
     * 显示的OHLC数据起始位置
     */
    private int mDataStartIndext = 0;

    /**
     * 显示的OHLC数据个数
     */
    private int mShowDataNum = DEFAULT_CANDLE_NUM;

    /**
     * 是否显示蜡烛详情
     */
    private boolean showDetails;

    /**
     * 当前数据的最大最小值
     */
    private double mMaxPrice;
    private double mMinPrice;

    /**
     * MA数据
     */
    private List<MALineEntity> MALineData;
    String textforFlush = "加载数据";
    private String mTabTitle;
    private StickChart mVolumnChartView;
    // 下部表的数据
    MACDEntity mMACDData;
    KDJEntity mKDJData;
    RSIEntity mRSIData;
    private MotionEvent e;
    private boolean ismove = true;
    private int zoomNum = 0;
    private long currentTime;
    // private DisplayDataChangeListener mDisplayChangeListener; // 显示数据变化监听
    private boolean go = true;
    private boolean firsttime = true;
    private String symbolType;
    private String symbol;
    private double dragValue = 0;
    private double hisDrag = 0;
    private Context context;
    private boolean isLoadMore = true;
    private boolean loadAble = true;

    public KChartsLandView(Context context) {
        super(context);
        init();
    }

    public KChartsLandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KChartsLandView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        super.setOnTabClickListener(this);

        mShowDataNum = DEFAULT_CANDLE_NUM;
        mDataStartIndext = 0;
        showDetails = false;
        mMaxPrice = -1;
        mMinPrice = -1;
        // mTabTitle = null;

        mOHLCData = new ArrayList<OHLCEntity>();
        mMACDData = new MACDEntity(null);
        mKDJData = new KDJEntity(null);
        mRSIData = new RSIEntity(null);
        /*
         * if(mOHLCData.size() >= 30){
         * mStartX = getWidth() - 6;
         * }else{
         * mStartX =(int) (getWidth() - 6 - ( 30 - mOHLCData.size()) * (mCandleWidth *3));
         * }
         */
        iniPaint();
    }

    private Paint textPaint;
    private Paint defPaint;
    private Paint redPaint;
    private Paint greenPaint;
    private Paint grayPaint;
    private Paint whitePaint;
    private Paint yellowPaint;
    private Paint magentaPaint;
    private Paint merchPaint;

    // private Paint
    private void iniPaint() {
        textPaint = new Paint();
        defPaint = new Paint();
        merchPaint = new Paint();
        textPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);

        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        greenPaint = new Paint();
        greenPaint.setColor(getResources().getColor(R.color.dark_green));
        grayPaint = new Paint();
        grayPaint.setColor(getResources().getColor(R.color.def_gray));

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        magentaPaint = new Paint();
        magentaPaint.setColor(Color.MAGENTA);
    }

    /*
     * @Override
     * public boolean dispatchTouchEvent(MotionEvent event) {
     * super.dispatchTouchEvent(event);
     * return true;
     * }
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTitles(canvas);
        if (mOHLCData == null || mOHLCData.size() <= 0) {
            return;
        }
        try {
            drawUpperRegion(canvas);
            drawLowerRegion(canvas);
            drawTitles(canvas);
            drawCandleDetails(canvas);
            if (firsttime) {
                setOnTouchOnce();
                firsttime = false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // public DisplayDataChangeListener getDisplayChangeListener() {
    // return mDisplayChangeListener;
    // }
    //
    // public void setDisplayChangeListener(DisplayDataChangeListener mDisplayChangeListener) {
    // this.mDisplayChangeListener = mDisplayChangeListener;
    // }

    private void drawCandleDetails(Canvas canvas) {
        boolean isB = false;
        if (symbol.contains("SH9")) {
            isB = true;
        }
        if (showDetails && mStartX > 3) {
            // Paint textPaintFor = new Paint();
            defPaint.reset();
            defPaint.setStyle(Paint.Style.FILL);// 充满
            defPaint.setColor(Color.WHITE);
            defPaint.setAntiAlias(true);
            FontMetrics fm = defPaint.getFontMetrics();
            // Paint p = new Paint();
            merchPaint.reset();
            Rect rects = new Rect();
            merchPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            merchPaint.getTextBounds("正", 0, "正".length(), rects);
            int textTextHeight = rects.height();
            int textMargin = getResources().getDimensionPixelSize(R.dimen.float_text_margin);
            int addNum = MIN_CANDLE_NUM - mOHLCData.size();
            float width = getWidth() - PADDING_LEFT;
            float left = 3.0f + PADDING_LEFT + 10 + textMargin;
            float top = 5.0f + DEFAULT_AXIS_TITLE_SIZE + 10 + textMargin;
            float right = 3.0f + 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT + 10 + textMargin;
            float bottom = 5.0f + 9 * textTextHeight + textMargin * 10;
            if ((bottom + top) >= getHeight()) {
                textMargin = (int) (textMargin - ((bottom) - getHeight()) / 9);
                left = 3.0f + PADDING_LEFT + 10 + textMargin;
                top = 5.0f + DEFAULT_AXIS_TITLE_SIZE + 10 + textMargin;
                right = 3.0f + 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT + 10 + textMargin;
                bottom = 5.0f + 9 * textTextHeight + textMargin * 10;
            }
            /*
             * if (mOHLCData.size() < MIN_CANDLE_NUM) {
             * if (mStartX - addNum * (mCandleWidth + 3) < (width / 2.0f + PADDING_LEFT)) {
             * right = width - 12.0f + PADDING_LEFT;
             * left = width - 12.0f - 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT;
             * }
             * } else {
             */
            if (mStartX < width / 2.0f) {
                right = width - 12.0f + PADDING_LEFT;
                left = width - 12.0f - 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT;
            }
            // }

            int selectIndext = (int) ((width - 2.0f - mStartX) / (mCandleWidth + 3) + mDataStartIndext);

            if (mOHLCData.size() < MIN_CANDLE_NUM) {
                selectIndext = (int) ((width - 2.0f - mStartX - addNum * (mCandleWidth + 3)) / (mCandleWidth + 3) + mDataStartIndext);
            }
            if (selectIndext < 0) {
                selectIndext = 0;
            }
            double rate = (getUperChartHeight() - 2) / (mMaxPrice - mMinPrice);
            float cl = (float) ((mMaxPrice - mOHLCData.get(selectIndext).getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
            float startX = (float) (width - 3 - (mCandleWidth + 3) * (selectIndext - mDataStartIndext) - (mCandleWidth - 1) / 2);
            if (mOHLCData.size() < MIN_CANDLE_NUM) {
                startX = (float) (width - 3 - (mCandleWidth + 3) * (selectIndext - mDataStartIndext)
                        - (mCandleWidth - 1) / 2 - addNum * (mCandleWidth + 3));
            }
            // Paint textPaint = new Paint();
            defPaint.reset();
            defPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            defPaint.setColor(Color.RED);
            // textPaint.setFakeBoldText(true);
            defPaint.setAntiAlias(true);
            if (null != mOHLCData.get(selectIndext).getInfo() && mOHLCData.get(selectIndext).getInfo().length() > 0) {
                merchPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
                merchPaint.getTextBounds(mOHLCData.get(selectIndext).getInfo(), 0, mOHLCData.get(selectIndext)
                        .getInfo().length(), rects);
                left = 3.0f + PADDING_LEFT + 10 + textMargin;
                top = 5.0f + DEFAULT_AXIS_TITLE_SIZE + 10 + textMargin * 2 + textTextHeight;
                right = 3.0f + 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT + 10 + textMargin;
                float leftInfo = 3.0f + PADDING_LEFT + 10;
                bottom = 5.0f + 10 * textTextHeight + textMargin * 11;
                if ((bottom + top) >= getHeight()) {
                    // textMargin = (int) (textMargin - ((bottom) - getHeight()) / 9);
                    left = 3.0f + PADDING_LEFT + 10 + textMargin;
                    top = 5.0f + DEFAULT_AXIS_TITLE_SIZE + 10 + textMargin * 2 + textTextHeight;
                    right = 3.0f + 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT + 10 + textMargin;
                    bottom = 5.0f + 10 * textTextHeight + textMargin * 11;
                }
                if (mOHLCData.size() < MIN_CANDLE_NUM) {
                    if (mStartX - addNum * (mCandleWidth + 3) < (width / 2.0f + PADDING_LEFT)) {
                        right = width - 12.0f + PADDING_LEFT;
                        left = width - 12.0f - 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT;
                        leftInfo = width - 12.0f - rects.width();
                    }
                } else {
                    if (mStartX < width / 2.0f) {
                        right = width - 12.0f + PADDING_LEFT;
                        left = width - 12.0f - 9 * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT;
                        leftInfo = width - 12.0f - rects.width() + PADDING_LEFT;
                    }
                }
                canvas.drawText(mOHLCData.get(selectIndext).getInfo(), leftInfo + 1, top - textMargin - textTextHeight,
                        defPaint);
            }
            // 绘制点击线条及详情区域
            // Paint paint = new Paint();
            merchPaint.reset();
            merchPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.blue_line));
            merchPaint.setAntiAlias(true);
            // paint.setAlpha(150);
            e.setLocation(startX, startX);
            mVolumnChartView.setIndex(mDataStartIndext);
            mVolumnChartView.onSet(e, ismove);
            canvas.drawLine(startX + PADDING_LEFT, 2.0f + DEFAULT_AXIS_TITLE_SIZE, startX + PADDING_LEFT,
                    UPER_CHART_BOTTOM, merchPaint);
            canvas.drawLine(PADDING_LEFT, cl, this.getWidth(), cl, merchPaint);// 十字光标横线

            merchPaint.setColor(Color.WHITE);
            merchPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            // Paint selectPaint = new Paint();
            merchPaint.reset();
            merchPaint.setAntiAlias(true);// 设置画笔的锯齿效果
            merchPaint.setStyle(Paint.Style.FILL);// 充满
            merchPaint.setColor(PortfolioApplication.getInstance().getResources().getColor(R.color.white_lucenty));
            RectF oval3 = new RectF(left - textMargin, top - textMargin, right, bottom);// 设置个新的长方形
            canvas.drawRoundRect(oval3, 20, 15, merchPaint);// 第二个参数是x半径，第三个参数是y半径

            merchPaint.setStyle(Paint.Style.STROKE);// 描边
            merchPaint.setStrokeWidth(2);
            merchPaint.setColor(Color.LTGRAY);
            canvas.drawRoundRect(oval3, 20, 15, merchPaint);

            Paint borderPaint = new Paint();
            borderPaint.setColor(Color.LTGRAY);

            // 绘制详情文字
            defPaint.setColor(Color.DKGRAY);
            canvas.drawText("日期: " + mOHLCData.get(selectIndext).getDate(), left + 1, top + textMargin, defPaint);

            canvas.drawText("开盘:", left + 1, top + textTextHeight + textMargin * 2, defPaint);
            double open = mOHLCData.get(selectIndext).getOpen();
            try {
                double ysdclose = mOHLCData.get(selectIndext + 1).getClose();
                if (open >= ysdclose) {
                    // textPaint.setColor(Color.DKGRAY);
                } else {
                    // textPaint.setColor(Color.DKGRAY);
                }
                if (!isB) {
                    canvas.drawText(new DecimalFormat("0.00").format(open), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f,
                            top + textTextHeight + textMargin * 2, defPaint);
                } else {
                    canvas.drawText(new DecimalFormat("0.000").format(open), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f,
                            top + textTextHeight + textMargin * 2, defPaint);
                }
            } catch (Exception e) {
                canvas.drawText(new DecimalFormat("0.00").format(open), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top
                        + textTextHeight + textMargin * 2, defPaint);
            }

            // textPaint.setColor(Color.DKGRAY);
            canvas.drawText("最高:", left + 1, top + textTextHeight * 2 + textMargin * 3, defPaint);
            double high = mOHLCData.get(selectIndext).getHigh();
            if (open < high) {
                // textPaint.setColor(Color.DKGRAY);
            } else {
                // textPaint.setColor(Color.DKGRAY);
            }
            if (!isB) {
                canvas.drawText(new DecimalFormat("0.00").format(high), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top
                        + textTextHeight * 2 + textMargin * 3, defPaint);
            } else {
                canvas.drawText(new DecimalFormat("0.000").format(high), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top
                        + textTextHeight * 2 + textMargin * 3, defPaint);
            }
            // textPaint.setColor(Color.DKGRAY);
            canvas.drawText("最低:", left + 1, top + textTextHeight * 3 + textMargin * 4, defPaint);
            double low = mOHLCData.get(selectIndext).getLow();
            try {
                double yesterday = (mOHLCData.get(selectIndext + 1).getLow() + mOHLCData.get(selectIndext + 1)
                        .getHigh()) / 2.0f;
                if (yesterday <= low) {
                    // textPaint.setColor(Color.DKGRAY);
                } else {
                    // textPaint.setColor(Color.DKGRAY);
                }
            } catch (Exception e) {

            }
            if (!isB) {
                canvas.drawText(new DecimalFormat("0.00").format(low), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top
                        + textTextHeight * 3 + textMargin * 4, defPaint);
            } else {
                canvas.drawText(new DecimalFormat("0.000").format(low), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top
                        + textTextHeight * 3 + textMargin * 4, defPaint);
            }
            // textPaint.setColor(Color.DKGRAY);
            canvas.drawText("收盘:", left + 1, top + textTextHeight * 4 + textMargin * 5, defPaint);
            double close = mOHLCData.get(selectIndext).getClose();
            try {
                double yesdopen = (mOHLCData.get(selectIndext + 1).getLow() + mOHLCData.get(selectIndext + 1).getHigh()) / 2.0f;
                if (yesdopen <= close) {
                    // textPaint.setColor(Color.DKGRAY);
                } else {
                    // textPaint.setColor(Color.DKGRAY);
                }
            } catch (Exception e) {

            }
            if (!isB) {
                canvas.drawText(new DecimalFormat("0.00").format(close), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top
                        + textTextHeight * 4 + textMargin * 5, defPaint);
            } else {
                canvas.drawText(new DecimalFormat("0.000").format(close), left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f,
                        top + textTextHeight * 4 + textMargin * 5, defPaint);
            }

            // textPaint.setColor(Color.DKGRAY);
            canvas.drawText("涨跌:", left + 1, top + textTextHeight * 5 + textMargin * 6, defPaint);
            try {
                if (!isB) {
                    canvas.drawText(new DecimalFormat("0.00").format(mOHLCData.get(selectIndext).getChange()), left + 1
                            + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 5 + textMargin * 6, defPaint);
                } else {
                    canvas.drawText(new DecimalFormat("0.000").format(mOHLCData.get(selectIndext).getChange()), left
                            + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 5 + textMargin * 6, defPaint);
                }
            } catch (Exception e) {
                /*
                 * canvas.drawText("--", left + 1 + DEFAULT_AXIS_TITLE_SIZE * 3.5f, top
                 * + DEFAULT_AXIS_TITLE_SIZE * 6.0f, textPaint);
                 */
            }

            // textPaint.setColor(Color.DKGRAY);
            canvas.drawText("涨幅:", left + 1, top + textTextHeight * 6 + textMargin * 7, defPaint);
            try {
                canvas.drawText(new DecimalFormat("0.00").format(mOHLCData.get(selectIndext).getPercentage()) + "%",
                        left + 1 + DEFAULT_AXIS_TITLE_SIZE * 2.5f, top + textTextHeight * 6 + textMargin * 7, defPaint);
            } catch (Exception e) {
                canvas.drawText("--", left + 1 + DEFAULT_AXIS_TITLE_SIZE * 3.5f, top + DEFAULT_AXIS_TITLE_SIZE * 6.0f,
                        defPaint);
            }

            // textPaint.setColor(Color.DKGRAY);
            canvas.drawText("成交量:", left + 1, top + textTextHeight * 7 + textMargin * 8, defPaint);
            canvas.drawText(UIUtils.getValue(mOHLCData.get(selectIndext).getVolume()), left + 1
                    + DEFAULT_AXIS_TITLE_SIZE * 3.5f, top + textTextHeight * 7 + textMargin * 8, defPaint);
        }

    }

    private void drawTitles(Canvas canvas) {
        // Paint textPaint = new Paint();
        textPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
        textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        textPaint.setAntiAlias(true);

        if (isDisplayAxisYTitle()) {
            // Y轴Titles
            int len = getUpperLatitudeNum() + 1;
            if (len > 0) {
                for (int i = 0; i < len; i++) {

                    String t = getYTitle(mMinPrice + (mMaxPrice - mMinPrice) / len * i);
                    defPaint.reset();
                    Rect rect = new Rect();
                    defPaint.setAntiAlias(true);
                    defPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
                    defPaint.getTextBounds(t, 0, t.length(), rect);
                    if (i != 0 && i != len) {

                        canvas.drawText(t, PADDING_LEFT - rect.width() - 3, UPER_CHART_BOTTOM - getLatitudeSpacing()
                                * i + rect.height() / 2, textPaint);
                    } else {

                        canvas.drawText(t, PADDING_LEFT - rect.width() - 3, UPER_CHART_BOTTOM - getLatitudeSpacing()
                                * i, textPaint);
                    }
                }
            }
            String t = getYTitle(mMaxPrice);

            defPaint.reset();
            defPaint.setAntiAlias(true);
            Rect rect = new Rect();
            defPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            defPaint.getTextBounds(t, 0, t.length(), rect);
            canvas.drawText(t, PADDING_LEFT - rect.width() - 3, DEFAULT_AXIS_TITLE_SIZE * 2 + 2, textPaint);
        }

        if (isDisplayAxisXTitle()) {
            // X轴Titles
            textPaint.setColor(DEFAULT_AXIS_X_TITLE_COLOR);
            canvas.drawText(mOHLCData.get(mDataStartIndext).getDate(), getWidth() - 4 - 4.5f * DEFAULT_AXIS_TITLE_SIZE,
                    UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE, textPaint);
            try {
                canvas.drawText(String.valueOf(mOHLCData.get(mDataStartIndext + mShowDataNum / 2).getDate()),
                        getWidth() / 2 - 2.25f * DEFAULT_AXIS_TITLE_SIZE + PADDING_LEFT, UPER_CHART_BOTTOM
                                + DEFAULT_AXIS_TITLE_SIZE, textPaint);
                canvas.drawText(String.valueOf(mOHLCData.get(mDataStartIndext + mShowDataNum - 1).getDate()),
                        2 + PADDING_LEFT, UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE, textPaint);
            } catch (Exception e) {

            }
        }

    }


    private String getYTitle(double value) {
        String t;
        if (symbolType.equals("5")) {
            t = new DecimalFormat("0").format(value);
        } else {
            t = new DecimalFormat("0.00").format(value);
        }
        t = UIUtils.nongNet(t);
        if (t.length() > 6) {
            t = t.substring(0, 6);
            if (t.substring(5, 6).equals(".")) {
                t = t.substring(0, 5);
            }
        }
        return t;
    }


    private void drawUpperRegion(Canvas canvas) {
        // 绘制蜡烛图
        try {
            // Paint redPaint = new Paint();
            // redPaint.setColor(Color.RED);
            // Paint greenPaint = new Paint();
            // greenPaint.setColor(getResources().getColor(R.color.dark_green));
            // Paint grayPaint = new Paint();
            // grayPaint.setColor(getResources().getColor(R.color.def_gray));
            int width = getWidth() - PADDING_LEFT;
            float tempFloatNum = mShowDataNum;
            mCandleWidth = (width - CANDLE_PADDING) / tempFloatNum - CANDLE_PADDING;
            double rate = (getUperChartHeight() - 2) / (mMaxPrice - mMinPrice);
            if (mOHLCData.size() >= MIN_CANDLE_NUM) {
                for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
                    OHLCEntity entity = mOHLCData.get(mDataStartIndext + i);
                    float open = (float) ((mMaxPrice - entity.getOpen()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
                    float close = (float) ((mMaxPrice - entity.getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
                    float high = (float) ((mMaxPrice - entity.getHigh()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
                    float low = (float) ((mMaxPrice - entity.getLow()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);

                    float left = (float) (width - CANDLE_PADDING - mCandleWidth * (i + 1) - i * CANDLE_PADDING
                            + PADDING_LEFT + dragValue);
                    float right = (float) (width - CANDLE_PADDING - mCandleWidth * i - i * CANDLE_PADDING
                            + PADDING_LEFT + dragValue);
                    float startMidLineX = (float) (right - mCandleWidth / 2.0);

                    if (open < close) {
                        canvas.drawRect(left, open, right, close, greenPaint);

                        canvas.drawLine(startMidLineX, high, startMidLineX, low, greenPaint);
                    } else if (open == close) {
                        double hisClose;
                        if (mOHLCData.size() > 1 && (mDataStartIndext + i + 1 < mOHLCData.size())) {
                            hisClose = mOHLCData.get(mDataStartIndext + i + 1).getClose();
                        } else {
                            hisClose = -1;
                        }
                        if (entity.getOpen() > hisClose) {
                            canvas.drawLine(left, open, right, open, redPaint);
                            canvas.drawLine(startMidLineX, high, startMidLineX, low, redPaint);
                        } else if (entity.getOpen() < hisClose) {
                            canvas.drawLine(left, open, right, open, greenPaint);
                            canvas.drawLine(startMidLineX, high, startMidLineX, low, greenPaint);
                        } else {
                            canvas.drawLine(left, open, right, open, grayPaint);
                            canvas.drawLine(startMidLineX, high, startMidLineX, low, grayPaint);
                        }
                    } else {
                        canvas.drawRect(left, close, right, open, redPaint);
                        canvas.drawLine(startMidLineX, high, startMidLineX, low, redPaint);
                    }
                    if (null != entity.getInfo() && entity.getInfo().length() > 0) {
                        // Paint p = new Paint();
                        defPaint.reset();
                        defPaint.setAntiAlias(true);
                        defPaint.setStyle(Paint.Style.FILL);
                        defPaint.setColor(getResources().getColor(R.color.ma10_color));
                        float circleRadius = 0;
                        if (mCandleWidth < 3f) {
                            circleRadius = 3f;
                        } else {
                            circleRadius = (float) (mCandleWidth / 2);
                        }
                        canvas.drawCircle(startMidLineX, (float) (UPER_CHART_BOTTOM - mCandleWidth), circleRadius,
                                defPaint);
                    }
                    if (dragValue > 0) {
                        Paint paint = new Paint();
                        paint.reset();
                        paint.setColor(getResources().getColor(R.color.white));
                        paint.setAntiAlias(true);
                        paint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.setting_text_phone));
                        Rect rect = new Rect();
                        paint.getTextBounds(textforFlush, 0, textforFlush.length(), rect);
                        float we = 0;
                        if (rect.width() <= dragValue) {
                            we = (float) (PADDING_LEFT + dragValue - rect.width());
                            // canvas.drawText(textforFlush, we, getHeight() / 2, paint);
                        } else {
                            int k = textforFlush.length() - (int) (textforFlush.length() * dragValue / rect.width());
                            // canvas.drawText(textforFlush, k, textforFlush.length(), PADDING_LEFT, getHeight() / 2,
                            // paint);
                        }

                    }
                }
                // 绘制上部曲线图及上部分MA值
                // float MATitleWidth = width / 10.0f * 10.0f / MALineData.size();
                String text = "";
                float wid = 0;
                for (int j = 0; j < MALineData.size(); j++) {
                    MALineEntity lineEntity = MALineData.get(j);

                    float startX = (float) (PADDING_LEFT);
                    float startY = 0;
                    // Paint paint = new Paint();
                    defPaint.reset();
                    defPaint.setColor(lineEntity.getLineColor());
                    defPaint.setAntiAlias(true);
                    defPaint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
                    int selectIndext;
                    if (showDetails) {
                        selectIndext = (int) ((width - CANDLE_PADDING - mStartX) / (mCandleWidth + CANDLE_PADDING) + mDataStartIndext);
                    } else {
                        selectIndext = mDataStartIndext;
                    }
                    mVolumnChartView.setCurrentIndex(selectIndext);
                    mVolumnChartView.setmShowDate(mShowDataNum);
                    if (selectIndext > lineEntity.getLineData().size() - 1 || selectIndext < 0) {
                        text = lineEntity.getTitle() + ":--";
                    } else
                        text = lineEntity.getTitle() + ":"
                                + new DecimalFormat("0.00").format(lineEntity.getLineData().get(selectIndext));
                    if (lineEntity.getLineData().get(selectIndext) == 0) {
                        text = lineEntity.getTitle() + ":--";
                    }
                    // Paint p = new Paint();
                    merchPaint.reset();
                    Rect rect = new Rect();
                    merchPaint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
                    merchPaint.getTextBounds(text, 0, text.length(), rect);
                    if (j == 0) {
                        wid = CANDLE_PADDING;
                    }/*
                      * else{
                      * wid = 2 + rect.width()*2/3 + wid + 5;
                      * }
                      */
                    canvas.drawText(text, wid + PADDING_LEFT, DEFAULT_AXIS_TITLE_SIZE, defPaint);
                    wid = wid + 32 + rect.width();
                    defPaint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.line_kline));
                    for (int i = 0; i < mShowDataNum && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
                        /*
                         * if(lineEntity.getLineData().get(mDataStartIndext + i) == 0){
                         * break;
                         * }
                         */
                        if (i != 0) {
                            if (!(startY == (float) (mMaxPrice * rate) || (null != lineEntity.getLineData().get(
                                    mDataStartIndext + i) && lineEntity.getLineData().get(mDataStartIndext + i) == 0))) {
                                canvas.drawLine((float) (startX + dragValue), startY + DEFAULT_AXIS_TITLE_SIZE + 4,
                                        (float) (width - CANDLE_PADDING - (CANDLE_PADDING + mCandleWidth) * i
                                                - mCandleWidth * 0.5f + PADDING_LEFT + dragValue),
                                        (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i))
                                                * rate + DEFAULT_AXIS_TITLE_SIZE + 4), defPaint);
                            }

                        }
                        startX = (float) (width - CANDLE_PADDING - (CANDLE_PADDING + mCandleWidth) * i - mCandleWidth
                                * 0.5f + PADDING_LEFT);
                        startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate);
                    }
                }
                if (null != e && !showDetails) {
                    e.setLocation(getWidth() - 3 - PADDING_LEFT, 0);
                    mVolumnChartView.setIndex(mDataStartIndext);
                    mVolumnChartView.onSet(e, ismove);
                }
            } else {
                int addNum = MIN_CANDLE_NUM - mOHLCData.size();
                for (int i = 0; i < mShowDataNum && mDataStartIndext + i < mOHLCData.size(); i++) {
                    OHLCEntity entity = mOHLCData.get(mDataStartIndext + i);
                    float open = (float) ((mMaxPrice - entity.getOpen()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
                    float close = (float) ((mMaxPrice - entity.getClose()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
                    float high = (float) ((mMaxPrice - entity.getHigh()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);
                    float low = (float) ((mMaxPrice - entity.getLow()) * rate + DEFAULT_AXIS_TITLE_SIZE + 4);

                    float left = (float) (width - 2 - mCandleWidth * (i + 1 + addNum) - (i + addNum) * 3 + PADDING_LEFT);
                    float right = (float) (width - 3 - mCandleWidth * (i + addNum) - (i + addNum) * 3 + PADDING_LEFT);
                    float startX = (float) (width - 3 - mCandleWidth * (i + addNum) - (mCandleWidth - 1) / 2
                            - (i + addNum) * 3 + PADDING_LEFT);
                    if (open < close) {
                        canvas.drawRect(left, open, right, close, greenPaint);

                        canvas.drawLine(startX, high, startX, low, greenPaint);
                    } else if (open == close) {
                        double hisClose;
                        if (mOHLCData.size() > 1 && (mDataStartIndext + i + 1 < mOHLCData.size())) {
                            hisClose = mOHLCData.get(mDataStartIndext + i + 1).getClose();
                        } else {
                            hisClose = 1;
                        }
                        if (entity.getOpen() > hisClose) {
                            canvas.drawLine(left, open, right, open, redPaint);
                            canvas.drawLine(startX, high, startX, low, redPaint);
                        } else if (entity.getOpen() < hisClose) {
                            canvas.drawLine(left, open, right, open, greenPaint);
                            canvas.drawLine(startX, high, startX, low, greenPaint);
                        } else {
                            canvas.drawLine(left, open, right, open, grayPaint);
                            canvas.drawLine(startX, high, startX, low, grayPaint);
                        }
                    } else {
                        canvas.drawRect(left, close, right, open, redPaint);
                        canvas.drawLine(startX, high, startX, low, redPaint);
                    }
                    if (null != entity.getInfo() && entity.getInfo().length() > 0) {
                        // Paint p = new Paint();
                        merchPaint.reset();
                        merchPaint.setAntiAlias(true);
                        merchPaint.setStyle(Paint.Style.FILL);
                        merchPaint.setColor(getResources().getColor(R.color.ma10_color));
                        float wid = 0;
                        if (mCandleWidth < 3f) {
                            wid = 3f;
                        } else {
                            wid = (float) (mCandleWidth / 2);
                        }
                        canvas.drawCircle(startX, (float) (UPER_CHART_BOTTOM - mCandleWidth), wid, merchPaint);
                    }
                }

                String text = "";
                float wid = 0;
                for (int j = 0; j < MALineData.size(); j++) {
                    MALineEntity lineEntity = MALineData.get(j);

                    float startX = 0;
                    float startY = 0;
                    // Paint paint = new Paint();
                    defPaint.reset();
                    defPaint.setColor(lineEntity.getLineColor());
                    defPaint.setAntiAlias(true);
                    defPaint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
                    int selectIndext = (int) ((width - CANDLE_PADDING - mStartX - mCandleWidth * addNum - CANDLE_PADDING
                            * addNum)
                            / (mCandleWidth + CANDLE_PADDING) + mDataStartIndext);
                    if (selectIndext < 0) {
                        selectIndext = 0;
                    }
                    mVolumnChartView.setCurrentIndex(selectIndext);
                    if (selectIndext > lineEntity.getLineData().size() - 1 || selectIndext < 0) {
                        text = lineEntity.getTitle() + ":--";
                    } else {
                        text = lineEntity.getTitle() + ":"
                                + new DecimalFormat("0.00").format(lineEntity.getLineData().get(selectIndext));
                    }
                    if (lineEntity.getLineData().get(selectIndext) == 0) {
                        text = lineEntity.getTitle() + ":--";
                    }
                    // Paint p = new Paint();
                    merchPaint.reset();
                    Rect rect = new Rect();
                    merchPaint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
                    merchPaint.getTextBounds(text, 0, text.length(), rect);
                    if (j == 0) {
                        wid = CANDLE_PADDING;
                    }
                    canvas.drawText(text, wid + PADDING_LEFT, DEFAULT_AXIS_TITLE_SIZE, defPaint);
                    wid = wid + 32 + rect.width();
                    defPaint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.line_kline));
                    for (int i = 0; i < mShowDataNum && mDataStartIndext + i < lineEntity.getLineData().size(); i++) {
                        /*
                         * if(lineEntity.getLineData().get(mDataStartIndext + i) == 0){
                         * break;
                         * }
                         */
                        if (i != 0) {
                            if (!(startY == (float) (mMaxPrice * rate) || (null != lineEntity.getLineData().get(
                                    mDataStartIndext + i) && lineEntity.getLineData().get(mDataStartIndext + i) == 0))) {
                                canvas.drawLine(
                                        startX + PADDING_LEFT,
                                        startY + DEFAULT_AXIS_TITLE_SIZE + 4,
                                        (float) (width - 2 - (3 + mCandleWidth) * (i + addNum) - mCandleWidth * 0.5f + PADDING_LEFT),
                                        (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i))
                                                * rate + DEFAULT_AXIS_TITLE_SIZE + 4), defPaint);
                            }

                        }
                        startX = (float) (width - 2 - (3 + mCandleWidth) * (i + addNum) - mCandleWidth * 0.5f);
                        startY = (float) ((mMaxPrice - lineEntity.getLineData().get(mDataStartIndext + i)) * rate);
                    }
                }
                if (null != e && !showDetails) {
                    e.setLocation(getWidth() - 3 - PADDING_LEFT, 0);
                    mVolumnChartView.setIndex(mDataStartIndext);
                    mVolumnChartView.onSet(e, ismove);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void drawLowerRegion(Canvas canvas) {
        if (!isShowLowerChartTabs()) {
            return;
        }
        float lowertop = LOWER_CHART_TOP + 1;
        float lowerHight = getHeight() - lowertop - 4;
        float viewWidth = getWidth() - PADDING_LEFT;

        // 下部表的数据
        // MACDData mMACDData;
        // KDJData mKDJData;
        // RSIData mRSIData;
        // Paint whitePaint = new Paint();
        // whitePaint.setColor(Color.WHITE);
        // Paint yellowPaint = new Paint();
        // yellowPaint.setColor(Color.YELLOW);
        // Paint magentaPaint = new Paint();
        // magentaPaint.setColor(Color.MAGENTA);

        // Paint textPaint = new Paint();
        defPaint.reset();
        defPaint.setColor(DEFAULT_AXIS_Y_TITLE_COLOR);
        defPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
        if (mTabTitle.trim().equalsIgnoreCase("MACD")) {
            List<Double> MACD = mMACDData.getMACD();
            List<Double> DEA = mMACDData.getDEA();
            List<Double> DIF = mMACDData.getDIF();

            double low = DEA.get(mDataStartIndext);
            double high = low;
            double rate = 0.0;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < MACD.size(); i++) {
                low = low < MACD.get(i) ? low : MACD.get(i);
                low = low < DEA.get(i) ? low : DEA.get(i);
                low = low < DIF.get(i) ? low : DIF.get(i);

                high = high > MACD.get(i) ? high : MACD.get(i);
                high = high > DEA.get(i) ? high : DEA.get(i);
                high = high > DIF.get(i) ? high : DIF.get(i);
            }
            rate = lowerHight / (high - low);

            Paint paint = new Paint();
            float zero = (float) (high * rate) + lowertop;
            if (zero < lowertop) {
                zero = lowertop;
            }
            // 绘制双线
            float dea = 0.0f;
            float dif = 0.0f;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < MACD.size(); i++) {
                // 绘制矩形
                if (MACD.get(i) >= 0.0) {
                    paint.setColor(Color.RED);
                    float top = (float) ((high - MACD.get(i)) * rate) + lowertop;
                    if (zero - top < 0.55f) {
                        canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext), zero,
                                viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
                    } else {
                        canvas.drawRect(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext), top,
                                viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
                    }

                } else {
                    paint.setColor(getResources().getColor(R.color.dark_green));
                    float bottom = (float) ((high - MACD.get(i)) * rate) + lowertop;

                    if (bottom - zero < 0.55f) {
                        canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext), zero,
                                viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext), zero, paint);
                    } else {
                        canvas.drawRect(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext), zero,
                                viewWidth - 2 - (float) mCandleWidth * (i - mDataStartIndext), bottom, paint);
                    }
                }

                if (i != mDataStartIndext) {
                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext)
                            + (float) mCandleWidth / 2, (float) ((high - DEA.get(i)) * rate) + lowertop, viewWidth - 2
                            - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, dea, whitePaint);

                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext)
                                    + (float) mCandleWidth / 2, (float) ((high - DIF.get(i)) * rate) + lowertop, viewWidth - 2
                                    - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, dif,
                            yellowPaint);
                }
                dea = (float) ((high - DEA.get(i)) * rate) + lowertop;
                dif = (float) ((high - DIF.get(i)) * rate) + lowertop;
            }

            canvas.drawText(new DecimalFormat("0.00").format(high), 2, lowertop + DEFAULT_AXIS_TITLE_SIZE - 2, defPaint);
            canvas.drawText(new DecimalFormat("0.00").format((high + low) / 2), 2, lowertop + lowerHight / 2
                    + DEFAULT_AXIS_TITLE_SIZE, defPaint);
            canvas.drawText(new DecimalFormat("0.00").format(low), 2, lowertop + lowerHight, defPaint);

        } else if (mTabTitle.trim().equalsIgnoreCase("KDJ")) {
            List<Double> Ks = mKDJData.getK();
            List<Double> Ds = mKDJData.getD();
            List<Double> Js = mKDJData.getJ();

            double low = Ks.get(mDataStartIndext);
            double high = low;
            double rate = 0.0;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < Ks.size(); i++) {
                low = low < Ks.get(i) ? low : Ks.get(i);
                low = low < Ds.get(i) ? low : Ds.get(i);
                low = low < Js.get(i) ? low : Js.get(i);

                high = high > Ks.get(i) ? high : Ks.get(i);
                high = high > Ds.get(i) ? high : Ds.get(i);
                high = high > Js.get(i) ? high : Js.get(i);
            }
            rate = lowerHight / (high - low);

            // 绘制白、黄、紫线
            float k = 0.0f;
            float d = 0.0f;
            float j = 0.0f;
            for (int i = mDataStartIndext; i < mDataStartIndext + mShowDataNum && i < Ks.size(); i++) {

                if (i != mDataStartIndext) {
                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext)
                            + (float) mCandleWidth / 2, (float) ((high - Ks.get(i)) * rate) + lowertop, viewWidth - 2
                            - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, k, whitePaint);

                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext)
                            + (float) mCandleWidth / 2, (float) ((high - Ds.get(i)) * rate) + lowertop, viewWidth - 2
                            - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, d, yellowPaint);

                    canvas.drawLine(viewWidth - 1 - (float) mCandleWidth * (i + 1 - mDataStartIndext)
                            + (float) mCandleWidth / 2, (float) ((high - Js.get(i)) * rate) + lowertop, viewWidth - 2
                            - (float) mCandleWidth * (i - mDataStartIndext) + (float) mCandleWidth / 2, j, magentaPaint);
                }
                k = (float) ((high - Ks.get(i)) * rate) + lowertop;
                d = (float) ((high - Ds.get(i)) * rate) + lowertop;
                j = (float) ((high - Js.get(i)) * rate) + lowertop;
            }

            canvas.drawText(new DecimalFormat("0.00").format(high), 2, lowertop + DEFAULT_AXIS_TITLE_SIZE - 2, defPaint);
            canvas.drawText(new DecimalFormat("0.00").format((high + low) / 2), 2, lowertop + lowerHight / 2
                    + DEFAULT_AXIS_TITLE_SIZE, defPaint);
            canvas.drawText(new DecimalFormat("0.00").format(low), 2, lowertop + lowerHight, defPaint);

        } else if (mTabTitle.trim().equalsIgnoreCase("RSI")) {

        }

    }

    float timeX = 0;
    float timeY = 0;
    int currentDate = mDataStartIndext;
    private boolean twoFingle = false;
    int currentShow = mShowDataNum;
    float longs;

    private long firstClick;
    private long lastClick;
    // 计算点击的次数
    private int count;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        e = event;

        switch (event.getAction()) {
            // 设置触摸模式
            case MotionEvent.ACTION_POINTER_2_DOWN:
                twoFingle = true;
                longs = Math.abs(event.getX(0) - event.getX(event.getPointerCount() - 1));
                currentShow = mShowDataNum;
                currentDate = mDataStartIndext;
                mVolumnChartView.setTouch(true);
                break;
            case MotionEvent.ACTION_DOWN:
                // if (null != mTouchListener) {
                // mTouchListener.chartTounching();
                // }
                getParent().requestDisallowInterceptTouchEvent(true);
                mVolumnChartView.setTouch(true);
                if (dragValue == 0) {
                    hisDrag = 0;
                }
                currentDate = mDataStartIndext;
                ismove = true;
                go = true;
                currentTime = System.currentTimeMillis();
                // TOUCH_MODE = DOWN;
                showDetails = false;
                timeX = event.getX();
                timeY = event.getY();
                if (hisDrag == 0) {
                    textforFlush = "加载数据";
                }
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(300);
                            if (go && !twoFingle) {
                                // if (null != mTouchListener) {
                                // mTouchListener.chartTounching();
                                // }
                                mStartX = (int) (event.getX() - 2 * mCandleWidth - 6 - PADDING_LEFT);
                                if (mOHLCData.size() < MIN_CANDLE_NUM) {
                                    mStartX = (int) (event.getX() - mCandleWidth - 3 - PADDING_LEFT);
                                }
                                mStartY = event.getY();
                                showDetails = true;
                                setCurrentData();
                                postInvalidate();
                            }
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
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
                            if (null != mKCallBack) {
                                mKCallBack.onDoubleClick(this);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mVolumnChartView.setTouch(false);
                // if (null != mTouchListener) {
                // mTouchListener.loseTouching();
                // }
                getParent().requestDisallowInterceptTouchEvent(false);
                mVolumnChartView.setMaxStickDataNum(mShowDataNum);
                if (!twoFingle) {
                    showDetails = false;
                    go = false;
                    mStartX = getWidth() - 6 - PADDING_LEFT;
                    if (mOHLCData.size() < MIN_CANDLE_NUM) {
                        mStartX = (int) (getWidth() - 6 - (mCandleWidth + 3) * (MIN_CANDLE_NUM - mOHLCData.size()) - PADDING_LEFT);
                    }
                    /*
                     * e.setLocation(getWidth() - 6, 0);
                     * mVolumnChartView.onSet(e,ismove,mDataStartIndext);
                     */
                    postInvalidate();
                    // Paint paint = new Paint();
                    Paint paint = new Paint();
                    paint.setColor(getResources().getColor(R.color.def_gray));
                    paint.setAntiAlias(true);
                    paint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.setting_text_phone));
                    textforFlush = "加载数据";
                    final Rect rect = new Rect();
                    paint.getTextBounds(textforFlush, 0, textforFlush.length(), rect);

                    /* 拉拽计算，平滑回弹效果 */
                    Thread tk = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            int tmp = (int) (dragValue / 4);
                            boolean show = true;
                            if (dragValue < rect.width()) {
                                show = false;
                            } else {
                                textforFlush = "加载中...";
                            }
                            int k = 0;
                            while (dragValue >= tmp && dragValue > 0 && k < 4) {
                                dragValue -= tmp;

                                setDragValue(dragValue);
                                if (k == 3 && show) {
                                    // dragValue = rect.width();
                                    hisDrag = dragValue;
                                    Log.e("LoadMore", "-----------isLoadMore:" + isLoadMore + "---------");
                                    if (isLoadMore) {
                                        if (null != mKCallBack) {
                                            /* 当需要显示加载更多的的时候，设置拖拽间距 */
                                            setDragValue(rect.width());
                                            mKCallBack.loadMore();
                                            mKCallBack.onLoadMoreDataStart();
                                        }
                                        isLoadMore = false;
                                    }
                                }
                                if (!show) {
                                    hisDrag = 0;
                                }
                                setCurrentData();
                                postInvalidate();
                                mVolumnChartView.setDragValue(dragValue);
                                if (dragValue < tmp && !show) {
                                    // dragValue = 0;
                                    setDragValue(0);
                                }
                                k++;
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    if (dragValue != 0) {
                        tk.start();
                    }
                }
                twoFingle = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!twoFingle) {
                    float horizontalSpacing = event.getX() - timeX;
                    float hor = event.getY() - timeY;
                    if (Math.abs(horizontalSpacing) > MIN_MOVE_DISTANCE || Math.abs(hor) > MIN_MOVE_DISTANCE && go) {
                        go = false;
                        // if (null != mTouchListener) {
                        // mTouchListener.loseTouching();
                        // }
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    /**
                     * 进行拖动K线蜡烛
                     */
                    if (!go && !showDetails) {
                        ismove = false;
                        if (event.getX() - PADDING_LEFT >= 0) {
                            mStartX = event.getX() - PADDING_LEFT;
                            mStartY = event.getY();
                        }
                        if (!(hisDrag > 0 && dragValue > 0)) {
                            horizontalSpacing = (float) (horizontalSpacing + hisDrag);
                            mDataStartIndext = (int) (currentDate + (horizontalSpacing / (mCandleWidth + 3)));
                        }
                        if (loadAble
                                && mOHLCData.size() > MIN_CANDLE_NUM
                                && currentDate + mShowDataNum + (horizontalSpacing / (mCandleWidth + 3)) > mOHLCData
                                .size()) {
                            dragValue = (hisDrag + (currentDate + mShowDataNum
                                    + (horizontalSpacing / (mCandleWidth + 3)) - mOHLCData.size())
                                    * (mCandleWidth + 3)) / 2;
                            mVolumnChartView.setDragValue(dragValue);
                        } else {
                            if (mOHLCData.size() > MIN_CANDLE_NUM && hisDrag > 0 && dragValue > 0) {
                                // dragValue = (hisDrag + horizontalSpacing) / 2;

                                setDragValue((hisDrag + horizontalSpacing) / 2);
                                Log.e("xyxyxyx", " ---- MotionEvent.ACTION_MOVE  setDragValue:" + dragValue);
                            } else {
                                // dragValue = 0;
                                setDragValue(0);
                            }
                            mVolumnChartView.setDragValue(dragValue);
                        }
                        if (mDataStartIndext < 0) {

                            mDataStartIndext = 0;
                        }
                        /*
                         * if (horizontalSpacing < 0) {
                         * mDataStartIndext--;
                         * if (mDataStartIndext < 0) {
                         * mDataStartIndext = 0;
                         * }
                         * } else if (horizontalSpacing > 0) {
                         * mDataStartIndext++;
                         * }
                         */
                        setCurrentData();
                        postInvalidate();
                        mVolumnChartView.onSet(e, false);
                        mVolumnChartView.setIndex(mDataStartIndext);
                    }
                    if (showDetails) {
                        // if (null != mTouchListener) {
                        // mTouchListener.chartTounching();
                        // }
                        getParent().requestDisallowInterceptTouchEvent(true);
                        if (event.getX() - PADDING_LEFT >= 0) {
                            mStartX = event.getX() - PADDING_LEFT;
                            mStartY = event.getY();
                        }
                        setCurrentData();
                        postInvalidate();
                    }
                } else if (event.getPointerCount() > 1 && !showDetails) {
                    // 缩放K线显示条数
                    ismove = false;
                    mShowDataNum = (int) (currentShow - (Math.abs(event.getX(0)
                            - event.getX(event.getPointerCount() - 1)) - longs)
                            / (mCandleWidth + CANDLE_PADDING));
                    // 设置最少显示蜡烛数
                    if (mShowDataNum < MIN_CANDLE_NUM) {
                        mShowDataNum = MIN_CANDLE_NUM;
                    }
                    // 限制最大显示蜡烛数
                    if (mShowDataNum > MAX_CANDLE_NUM) {
                        mShowDataNum = MAX_CANDLE_NUM;
                    }
                    // mDataStartIndext = (int) (currentShow + ((event.getX(0) - event.getX(event.getPointerCount()-1))
                    // - longs) / (mCandleWidth + 3))/2;
                    /*
                     * if (horizontalSpacing < 0) {
                     * mDataStartIndext--;
                     * if (mDataStartIndext < 0) {
                     * mDataStartIndext = 0;
                     * }
                     * } else if (horizontalSpacing > 0) {
                     * mDataStartIndext++;
                     * }
                     */
                    setCurrentData();
                    postInvalidate();
                    mVolumnChartView.onSet(e, false);
                    mVolumnChartView.setIndex(mDataStartIndext);
                    mVolumnChartView.setMaxStickDataNum(mShowDataNum);
                }
                break;
        }

        return true;
    }

    // private ITouchListener mTouchListener;

    // public void setITouchListener(ITouchListener touchListener) {
    // this.mTouchListener = touchListener;
    // }

    private void setOnTouchOnce() {
        showDetails = false;
        go = false;
        mStartX = getWidth() - 6 - PADDING_LEFT;
        if (mOHLCData.size() < MIN_CANDLE_NUM) {
            mStartX = (int) (getWidth() - 6 - (mCandleWidth + 3) * (MIN_CANDLE_NUM - mOHLCData.size()) - PADDING_LEFT);
        }
        /*
         * e.setLocation(getWidth() - 6, 0);
         * mVolumnChartView.onSet(e,ismove,mDataStartIndext);
         */
        postInvalidate();
    }

    private void setCurrentData() {
        try {
            if (mShowDataNum > mOHLCData.size()) {
                mShowDataNum = mOHLCData.size();
            }
            if (MIN_CANDLE_NUM > mOHLCData.size()) {
                mShowDataNum = MIN_CANDLE_NUM;
            }
            mVolumnChartView.setmShowDate(mShowDataNum);
            if (mShowDataNum > mOHLCData.size()) {
                mDataStartIndext = 0;
            } else if (mShowDataNum + mDataStartIndext > mOHLCData.size()) {
                mDataStartIndext = mOHLCData.size() - mShowDataNum;
            }
            mMinPrice = mOHLCData.get(mDataStartIndext).getLow();
            mMaxPrice = mOHLCData.get(mDataStartIndext).getHigh();

            for (int i = mDataStartIndext + 1; i < mOHLCData.size() && i < mShowDataNum + mDataStartIndext; i++) {
                OHLCEntity entity = mOHLCData.get(i);
                mMinPrice = mMinPrice < entity.getLow() ? mMinPrice : entity.getLow();
                mMaxPrice = mMaxPrice > entity.getHigh() ? mMaxPrice : entity.getHigh();
            }

            for (MALineEntity lineEntity : MALineData) {
                for (int i = mDataStartIndext; i < lineEntity.getLineData().size()
                        && i < mShowDataNum + mDataStartIndext; i++) {
                    mMinPrice = mMinPrice < lineEntity.getLineData().get(i) ? mMinPrice : lineEntity.getLineData().get(
                            i);
                    mMaxPrice = mMaxPrice > lineEntity.getLineData().get(i) ? mMaxPrice : lineEntity.getLineData().get(
                            i);
                }
            }
            double value = mMaxPrice - mMinPrice;
            mMinPrice = mMinPrice - (value * 0.1);
            mMaxPrice = mMaxPrice + (value * 0.1);
            if (mKCallBack != null) {
                mKCallBack.onDisplayDataChange(getDisplayOHLCEntitys());
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取显示的数据
     *
     * @return
     */
    public ArrayList<OHLCEntity> getDisplayOHLCEntitys() {
        if (mOHLCData == null || mOHLCData.size() == 0) {
            return null;
        }

        ArrayList<OHLCEntity> result = new ArrayList<OHLCEntity>();
        for (int i = 0; i < mOHLCData.size(); i++) {
            OHLCEntity entity = mOHLCData.get(i);
            result.add(entity);
        }

        return result;
    }

    private void zoomIn() {
        mShowDataNum++;
        if (mShowDataNum > mOHLCData.size()) {
            mShowDataNum = MIN_CANDLE_NUM > mOHLCData.size() ? MIN_CANDLE_NUM : mOHLCData.size();
        }

    }

    private void zoomOut() {
        mShowDataNum--;
        if (mShowDataNum < MIN_CANDLE_NUM) {
            mShowDataNum = MIN_CANDLE_NUM;
        }

    }

    /**
     * 缩小
     */
    private void zoomIn(int size) {
        if (zoomNum < 5) {
            mShowDataNum += size;
            if (mShowDataNum > mOHLCData.size() && mOHLCData.size() <= MAX_CANDLE_NUM) {
                mShowDataNum = MIN_CANDLE_NUM > mOHLCData.size() ? MIN_CANDLE_NUM : mOHLCData.size();
            } else if (mShowDataNum > mOHLCData.size() && mOHLCData.size() > MAX_CANDLE_NUM) {
                mShowDataNum = MAX_CANDLE_NUM;
            }
            /*
             * if(zoomNum == 0){
             * mShowDataNum = 40;
             * }
             * if(zoomNum == 9){
             * if(mOHLCData.size() > MAX_CANDLE_NUM){
             * mShowDataNum = MAX_CANDLE_NUM;
             * }else{
             * mShowDataNum = mOHLCData.size();
             * }
             * 
             * }
             */
            zoomNum++;
        }
    }

    /**
     * 放大
     */
    private void zoomOut(int size) {
        if (zoomNum > 0) {

            mShowDataNum -= size;
            if (mShowDataNum < MIN_CANDLE_NUM) {
                mShowDataNum = MIN_CANDLE_NUM;

            }
            /*
             * if(zoomNum == 1){
             * mShowDataNum = MIN_CANDLE_NUM;
             * }
             * if(zoomNum == 10){
             * mShowDataNum = 80;
             * }
             */
            /*
             * if(zoomNum == 1){
             * mShowDataNum = MIN_CANDLE_NUM;
             * }
             * zoomNum--;
             */
            zoomNum--;
        }
    }

    private void setTouchMode(MotionEvent event) {
        float daltX = Math.abs(event.getX() - mStartX);
        float daltY = Math.abs(event.getY() - mStartY);
        if (FloatMath.sqrt(daltX * daltX + daltY * daltY) > MIN_MOVE_DISTANCE) {
            if (daltX < daltY) {
                TOUCH_MODE = ZOOM;
            } else {
                TOUCH_MODE = MOVE;
            }
            mStartX = event.getX();
            mStartY = event.getY();
        }
    }

    /**
     * 初始化MA值，从数组的最后一个数据开始初始化
     *
     * @param entityList
     * @param days
     * @return
     */
    private void initMA(List<OHLCEntity> entityList) {
        if (entityList == null || entityList.size() <= 0) {
            return;
        }

        MALineEntity MA5 = new MALineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(getResources().getColor(R.color.text_content_color));
        // MA5.setLineData(initMA(mOHLCData, 5));

        MALineEntity MA10 = new MALineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(getResources().getColor(R.color.ma10_color));
        // MA10.setLineData(initMA(mOHLCData, 10));

        MALineEntity MA20 = new MALineEntity();
        MA20.setTitle("MA20");
        MA20.setLineColor(getResources().getColor(R.color.ma20_color));
        // MA20.setLineData(initMA(mOHLCData, 20));

        /*
         * List<Float> MAValues = new ArrayList<Float>();
         * 
         * float sum = 0;
         * float avg = 0;
         * for (int i = entityList.size() - 1; i >= 0; i--) {
         * sum = 0;
         * avg = 0;
         * if (i - days >= -1) {
         * for (int k = 0; k < days; k++) {
         * sum = (float) (sum + entityList.get(i - k).getClose());
         * }
         * avg = sum / days;
         * } else {
         * break;
         * }
         * MAValues.add(avg);
         * }
         * 
         * List<Float> result = new ArrayList<Float>();
         * for (int j = MAValues.size() - 1; j >= 0; j--) {
         * result.add(MAValues.get(j));
         * }
         */
        List<Float> result5 = new ArrayList<Float>();
        List<Float> result10 = new ArrayList<Float>();
        List<Float> result20 = new ArrayList<Float>();
        for (int i = 0; i < entityList.size(); i++) {
            result5.add((float) entityList.get(i).getMa5());
            result10.add((float) entityList.get(i).getMa10());
            result20.add((float) entityList.get(i).getMa20());
        }
        MA5.setLineData(result5);
        MA10.setLineData(result10);
        MA20.setLineData(result20);
        MALineData = new ArrayList<MALineEntity>();
        MALineData.add(MA5);
        MALineData.add(MA10);
        MALineData.add(MA20);
    }

    public List<OHLCEntity> getOHLCData() {
        return mOHLCData;
    }

    public void flushFirshData(OHLCEntity mOHLCEntity) {
        if (null != mOHLCData && mOHLCData.size() > 0) {
            mOHLCData.add(0, mOHLCEntity);
            mOHLCData.remove(1);
            initMALineData();
            mMACDData = new MACDEntity(mOHLCData);
            mKDJData = new KDJEntity(mOHLCData);
            mRSIData = new RSIEntity(mOHLCData);

            setCurrentData();
            postInvalidate();
        }
    }

    public void setOHLCData(List<OHLCEntity> OHLCData, int page) {

        if (null != mKCallBack) {
            mKCallBack.onLoadMoreDataEnd();
            // dragValue = 0;
            /* 在加载更多完成之后，显示拖拽间距为0 */
            setDragValue(0);
            mVolumnChartView.setDragValue(0);
        }
        if (OHLCData == null || OHLCData.size() <= 0) {
            if (page > 1) {
                loadAble = false;
                // Paint paint = new Paint();
                merchPaint.reset();
                merchPaint.setColor(getResources().getColor(R.color.def_gray));
                merchPaint.setAntiAlias(true);
                merchPaint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.setting_text_phone));
                textforFlush = "加载中...";
                final Rect rect = new Rect();
                merchPaint.getTextBounds(textforFlush, 0, textforFlush.length(), rect);
                dragValue = hisDrag;
                Thread tk = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        int tmp = (int) (dragValue / 4);
                        while (dragValue >= tmp && dragValue > 0) {
                            dragValue -= tmp;
                            setCurrentData();
                            postInvalidate();
                            mVolumnChartView.setDragValue(dragValue);

                            if (dragValue < tmp) {
                                dragValue = 0;
                                hisDrag = 0;
                            }
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                });
                tk.start();
            } else {
                mMaxPrice = -1;
                mMinPrice = -1;
                return;
            }
        }
        if (page > 1 && !(mOHLCData.get(0).getDate().equals(OHLCData.get(0).getDate())) && !isLoadMore) {
            this.mOHLCData.addAll(OHLCData);
            isLoadMore = true;

            mDataStartIndext = (int) (mDataStartIndext + (hisDrag / (mCandleWidth + 3)));
            hisDrag = 0;
            dragValue = 0;
            mVolumnChartView.setDragValue(dragValue);
            setCurrentData();
            postInvalidate();
        } else {
            if (null != mOHLCData && mOHLCData.size() < 1) {
                this.mOHLCData = OHLCData;
            }
        }

        if (page > 1 && hasMoreData(page)) {
            isLoadMore = true;
        }
        initMALineData();
        mMACDData = new MACDEntity(mOHLCData);
        mKDJData = new KDJEntity(mOHLCData);
        mRSIData = new RSIEntity(mOHLCData);

        setCurrentData();
        postInvalidate();
    }

    private boolean hasMoreData(int page) {
        if (null != mOHLCData) {
            int fullDataSize = (299/* second page size */ * (page - 1) + 300/* first page size */);
            if (mOHLCData.size() < fullDataSize) {
                return false;
            }

        }
        return true;
    }

    private void initMALineData() {
        // MALineEntity MA5 = new MALineEntity();
        // MA5.setTitle("MA5");
        // MA5.setLineColor(getResources().getColor(R.color.ma5_color));
        // MA5.setLineData(initMA(mOHLCData, 5));
        //
        // MALineEntity MA10 = new MALineEntity();
        // MA10.setTitle("MA10");
        // MA10.setLineColor(getResources().getColor(R.color.ma10_color));
        // MA10.setLineData(initMA(mOHLCData, 10));
        //
        // MALineEntity MA20 = new MALineEntity();
        // MA20.setTitle("MA20");
        // MA20.setLineColor(getResources().getColor(R.color.ma20_color));
        // MA20.setLineData(initMA(mOHLCData, 20));
        //
        // MALineData = new ArrayList<MALineEntity>();
        // MALineData.add(MA5);
        // MALineData.add(MA10);
        // MALineData.add(MA20);
        initMA(mOHLCData);

    }

    public void onTabClick(int indext) {
        String[] titles = getLowerChartTabTitles();
        mTabTitle = titles[indext];
        postInvalidate();
    }

    public void setLowerChartTabTitles(String[] LowerChartTabTitles) {
        super.setLowerChartTabTitles(LowerChartTabTitles);
        if (LowerChartTabTitles != null && LowerChartTabTitles.length > 0) {
            mTabTitle = LowerChartTabTitles[0];
        }
    }

    /**
     * 放大
     */
    public void makeLager() {
        zoomOut(MIN_CANDLE_NUM);
        setCurrentData();
        postInvalidate();
    }

    /**
     * 判断是否是最大
     *
     * @return
     */
    public boolean isLargest() {
        if (mOHLCData == null || mOHLCData.size() == 0) {
            return false;
        }
        if (mShowDataNum == mOHLCData.size()) {
            return true;
        }
        return mShowDataNum == MAX_CANDLE_NUM;
    }

    /**
     * 是否是最小
     *
     * @return
     */
    public boolean isSmallest() {
        return mShowDataNum == MIN_CANDLE_NUM;
    }

    public boolean iscanSmoll() {
        return mOHLCData == null || mOHLCData.size() < MIN_CANDLE_NUM;
    }

    /**
     * 缩小
     */
    public void makeSmaller() {
        zoomIn(MIN_CANDLE_NUM);
        setCurrentData();
        postInvalidate();
    }

    public int getShowDataNum() {
        return mShowDataNum;
    }

    public void setShowDataNum(int ShowDataNum) {
        this.mShowDataNum = ShowDataNum;
    }

    /**
     * 显示数据变化
     *
     * @author linbing
     */
    public interface DisplayDataChangeListener {

        /**
         * 显示的数据变化
         *
         * @param entitys
         */
        void onDisplayDataChange(List<OHLCEntity> entitys);

        void onLoadMoreDataStart();

        void onLoadMoreDataEnd();
    }

    public void setStick(StickChart mVolumnChartView) {
        this.mVolumnChartView = mVolumnChartView;
    }

    public String getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    // private OnDoubleClickListener mDoubleClicklistener;
    //
    // public OnDoubleClickListener getDoubleClicklistener() {
    // return mDoubleClicklistener;
    // }
    //
    // public void setDoubleClicklistener(OnDoubleClickListener mDoubleClicklistener) {
    // this.mDoubleClicklistener = mDoubleClicklistener;
    // }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setLoadMore(boolean loadMore) {
        this.isLoadMore = loadMore;
    }

    public boolean isLoadAble() {
        return loadAble;
    }

    public void setLoadAble(boolean loadAble) {
        this.loadAble = loadAble;
    }

    public void reSetdate() {
        mShowDataNum = 50;
        mOHLCData.clear();
        mDataStartIndext = 0;
    }

    private KChartsLandCallBack mKCallBack;

    public KChartsLandCallBack getKChartsLandCallBack() {
        return mKCallBack;
    }

    public void setKChartsLandCallBack(KChartsLandCallBack mKCallBack) {
        this.mKCallBack = mKCallBack;
    }

    public double getDragValue() {
        return dragValue;
    }

    public void setDragValue(double dragValue) {
        this.dragValue = dragValue;
    }

}
