package com.dkhs.portfolio.ui.widget.kline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;


/**
 * 坐标轴使用的View
 *
 * @author nanjingbiao
 */
public class GridChart extends View {
    public static int CANDLE_PADDING = 3;
    // ////////////默认值////////////////
    /**
     * 默认背景色
     */
    public static final int DEFAULT_BACKGROUD = android.R.color.white;

    /**
     * 默认XY轴字体大小 *
     */
	public static int DEFAULT_AXIS_TITLE_SIZE = PortfolioApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.title_text_font);
//    public static int DEFAULT_AXIS_TITLE_SIZE = 0;

    /**
     * 默认XY坐标轴颜色
     */
    private static final int DEFAULT_AXIS_COLOR = Color.RED;

    /**
     * 默认经纬线颜色
     */
    private static final int DEFAULT_LONGI_LAITUDE_COLOR = Color.MAGENTA;

    /**
     * 默认上表纬线数
     */
    public static final int DEFAULT_UPER_LATITUDE_NUM = 1;

    /**
     * 默认下表纬线数
     */
    private static final int DEFAULT_LOWER_LATITUDE_NUM = 1;

    /**
     * 默认经线数
     */
    public static final int DEFAULT_LOGITUDE_NUM = 4;
    public static int PADDING_LEFT = PortfolioApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.padding_left);
    /**
     * 默认边框的颜色
     */
    public static final int DEFAULT_BORDER_COLOR = Color.RED;

    /**
     * 默认虚线效果
     */
    private static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(new float[]{3, 3, 3,
            3}, 1);

    /**
     * 下表的顶部
     */
    public static float LOWER_CHART_TOP;

    /**
     * 上表的底部
     */
    public static float UPER_CHART_BOTTOM;

    // /////////////属性////////////////
    /**
     * 背景色
     */
    private int mBackGround;

    /**
     * 坐标轴XY颜色
     */
    private int mAxisColor;

    /**
     * 经纬线颜色
     */
    private int mLongiLatitudeColor;

    /**
     * 虚线效果
     */
    private PathEffect mDashEffect;

    /**
     * 边线色
     */
    private int mBorderColor;

    /**
     * 上表高度
     */
    private float mUperChartHeight;

    /**
     * 是否显示下表Tabs
     */
    private boolean showLowerChartTabs = false;

    /**
     * 是否显示顶部Titles
     */
    private boolean showTopTitles;

    /**
     * 顶部Titles高度
     */
    private float topTitleHeight;

    /**
     * 下表TabTitles
     */
    private String[] mLowerChartTabTitles;

    /**
     * 下表Tab宽度
     */
    private float mTabWidth;

    /**
     * 下表Tab高度
     */
    private float mTabHight;

    /**
     * 下表TabIndex
     */
    private int mTabIndex;

    /**
     * 下表高度
     */
    private float mLowerChartHeight;

    private float longitudeSpacing;
    private float latitudeSpacing;

    /**
     * 上表纬线数   *
     */
    private int upperLatitudeNum;

    /**
     * 下表纬线数 *
     */
    private int lowerLatitudeNum;

    /**
     * 经线是否显示
     */
    private boolean displayLongitude = true;

    /**
     * 纬线是否显示
     */
    private boolean displayLatitude = true;

    /**
     * 经线是否显示
     */
    private boolean displayAxisXTitle = true;

    /**
     * 经线是否显示
     */
    private boolean displayAxisYTitle = true;

    private OnTabClickListener mOnTabClickListener;

    public GridChart(Context context) {
        super(context);
        init();
    }

    public GridChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GridChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        String lent = "2222.2正";
        Paint ps = new Paint();
        Rect rects = new Rect();
        ps.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
        ps.getTextBounds(lent, 0, lent.length(), rects);
        PADDING_LEFT = rects.width();
        mBackGround = DEFAULT_BACKGROUD;
        mAxisColor = DEFAULT_AXIS_COLOR;
        mLongiLatitudeColor = DEFAULT_LONGI_LAITUDE_COLOR;
        mDashEffect = DEFAULT_DASH_EFFECT;
        mBorderColor = DEFAULT_BORDER_COLOR;
//		showLowerChartTabs = true;
        showTopTitles = true;
        topTitleHeight = 0;
        mTabIndex = 0;
        mOnTabClickListener = null;

        mTabWidth = 0;
        mTabHight = 0;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundResource(mBackGround);
        int viewHeight = getHeight();
        int viewWidth = getWidth();
        /*if(9 * viewWidth < 16 * viewHeight){
			viewHeight = viewWidth * 9 /16;
		}else{
			viewWidth = viewHeight * 16 /9;
		}*/
        if (showLowerChartTabs) {
            mLowerChartHeight = viewHeight - 2 - LOWER_CHART_TOP;
        } else {
            mLowerChartHeight = 0;
        }

        if (showLowerChartTabs) {
            mTabHight = viewHeight / 16.0f;
        }

        //快速容错处理
        //DEFAULT_AXIS_TITLE_SIZE = 12;
        //DEFAULT_AXIS_TITLE_SIZE = DisplayUtil.sp2px(getContext(), DEFAULT_AXIS_TITLE_SIZE);
        if (showTopTitles) {
            topTitleHeight = DEFAULT_AXIS_TITLE_SIZE + 2;
        } else {
            topTitleHeight = 0;
        }

        //-------------------------------------------------------------------------
        //linbing 修改不显示底部时，隐藏底部
        upperLatitudeNum = DEFAULT_UPER_LATITUDE_NUM;
        lowerLatitudeNum = DEFAULT_LOWER_LATITUDE_NUM;
        //不显示底部区域时，将底部space 个数变成0
        if (!showLowerChartTabs) {
            upperLatitudeNum = upperLatitudeNum + lowerLatitudeNum + 1;
            lowerLatitudeNum = -1;
        }
        //-------------------------------------------------------------------------

        longitudeSpacing = (viewWidth - 2 - PADDING_LEFT) / (DEFAULT_LOGITUDE_NUM + 1);
//        latitudeSpacing = (viewHeight - 4 - DEFAULT_AXIS_TITLE_SIZE - topTitleHeight - mTabHight)
//                / (upperLatitudeNum + lowerLatitudeNum + 2);
        latitudeSpacing = (viewHeight - 4  - topTitleHeight - mTabHight)
                / (upperLatitudeNum + lowerLatitudeNum + 2);
        mUperChartHeight = latitudeSpacing * (upperLatitudeNum + 1);
        LOWER_CHART_TOP = viewHeight - 1 - latitudeSpacing * (lowerLatitudeNum + 1);
        UPER_CHART_BOTTOM = 1 + topTitleHeight + latitudeSpacing * (upperLatitudeNum + 1);


        // 绘制边框
        drawBorders(canvas, viewHeight, viewWidth);

        // 绘制经线
        drawLongitudes(canvas, viewHeight, longitudeSpacing);

        // 绘制纬线
        drawLatitudes(canvas, viewHeight, viewWidth, latitudeSpacing);

        // 绘制X线及LowerChartTitles
        drawRegions(canvas, viewHeight, viewWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        float x = event.getRawX();
        float y = event.getRawY();

        if (y <= LOWER_CHART_TOP + rect.top + 2
                && y >= UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE + rect.top) {
            if (mTabWidth <= 0) {
                return true;
            }
            int indext = (int) (x / mTabWidth);

            if (mTabIndex != indext) {
                mTabIndex = indext;
                mOnTabClickListener.onTabClick(mTabIndex);
            }
            return true;
        }

        return false;
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        mOnTabClickListener = onTabClickListener;
    }

    public interface OnTabClickListener {
        void onTabClick(int indext);
    }

    /**
     * 绘制边框
     *
     * @param canvas
     */
    private void drawBorders(Canvas canvas, int viewHeight, int viewWidth) {
        Paint paint = new Paint();
        paint.setColor(mBorderColor);
        paint.setStrokeWidth(1);

        int th = viewHeight - 1;

        if (!isDisplayAxisXTitle()) {
//            th -= DEFAULT_AXIS_TITLE_SIZE;
            th -= 0;
        }
        //th = (int) UPER_CHART_BOTTOM;
//		canvas.drawLine(1, 1, viewWidth - 1, 1, paint);
        canvas.drawLine(PADDING_LEFT, 1 + topTitleHeight, PADDING_LEFT, th, paint);
        canvas.drawLine(viewWidth - 1, th, viewWidth - 1, 1 + topTitleHeight, paint);
        if (isDisplayAxisXTitle()) {
            canvas.drawLine(viewWidth - 1, th, 1, th, paint);
        }
    }

    /**
     * 绘制经线
     *
     * @param canvas
     * @param viewHeight
     * @param viewWidth
     */
    private void drawLongitudes(Canvas canvas, int viewHeight, float longitudeSpacing) {
        if (!displayLongitude) {
            return;
        }
        Paint paint = new Paint();
        paint.setColor(mLongiLatitudeColor);
        paint.setPathEffect(mDashEffect);
        for (int i = 1; i <= DEFAULT_LOGITUDE_NUM; i++) {
            canvas.drawLine(PADDING_LEFT + longitudeSpacing * i, topTitleHeight + 2, 1 + longitudeSpacing * i + PADDING_LEFT,
                    UPER_CHART_BOTTOM, paint);
			/*canvas.drawLine(PADDING_LEFT + longitudeSpacing * i, LOWER_CHART_TOP, 1 + longitudeSpacing * i + PADDING_LEFT,
					viewHeight - 1, paint);*/
        }

    }

    /**
     * 绘制纬线
     *
     * @param canvas
     * @param viewHeight
     * @param viewWidth
     */
    private void drawLatitudes(Canvas canvas, int viewHeight, int viewWidth, float latitudeSpacing) {
        if (!displayLatitude) {
            return;
        }
        Paint paint = new Paint();
        paint.setColor(mLongiLatitudeColor);
        paint.setPathEffect(mDashEffect);
        if (upperLatitudeNum > 0) {
            for (int i = 1; i <= upperLatitudeNum; i++) {
                canvas.drawLine(PADDING_LEFT, topTitleHeight + 1 + latitudeSpacing * i, viewWidth - 1,
                        topTitleHeight + 1 + latitudeSpacing * i, paint);
            }
        }

        if (lowerLatitudeNum > 0) {
            for (int i = 1; i <= lowerLatitudeNum; i++) {
                canvas.drawLine(PADDING_LEFT, viewHeight - 1 - latitudeSpacing, viewWidth - 1, viewHeight - 1
                        - latitudeSpacing, paint);
            }
        }


    }

    private void drawRegions(Canvas canvas, int viewHeight, int viewWidth) {
        Paint paint = new Paint();
        paint.setColor(mAxisColor);
        paint.setAlpha(150);
        if (showTopTitles) {
            canvas.drawLine(PADDING_LEFT, 1 + DEFAULT_AXIS_TITLE_SIZE + 2, viewWidth - 1,
                    1 + DEFAULT_AXIS_TITLE_SIZE + 2, paint);
        }
        canvas.drawLine(PADDING_LEFT, UPER_CHART_BOTTOM, viewWidth - 1, UPER_CHART_BOTTOM, paint);
        if (showLowerChartTabs) {
            canvas.drawLine(PADDING_LEFT, LOWER_CHART_TOP, viewWidth - 1, LOWER_CHART_TOP, paint);
            canvas.drawLine(PADDING_LEFT, UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE + 2, viewWidth - 1,
                    UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE + 2, paint);
            if (mLowerChartTabTitles == null || mLowerChartTabTitles.length <= 0) {
                return;
            }
            mTabWidth = (viewWidth - 2) / 10.0f * 10.0f / mLowerChartTabTitles.length;
            if (mTabWidth < DEFAULT_AXIS_TITLE_SIZE * 2.5f + 2) {
                mTabWidth = DEFAULT_AXIS_TITLE_SIZE * 2.5f + 2;
            }

            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(DEFAULT_AXIS_TITLE_SIZE);
            for (int i = 0; i < mLowerChartTabTitles.length && mTabWidth * (i + 1) <= viewWidth - 2; i++) {
                if (i == mTabIndex) {
                    Paint bgPaint = new Paint();
                    bgPaint.setColor(Color.MAGENTA);
                    canvas.drawRect(mTabWidth * i + 1, LOWER_CHART_TOP, mTabWidth * (i + 1) + 1,
                            UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE + 2, bgPaint);
                }
                canvas.drawLine(mTabWidth * i + 1, LOWER_CHART_TOP, mTabWidth * i + 1,
                        UPER_CHART_BOTTOM + DEFAULT_AXIS_TITLE_SIZE + 2, paint);
                canvas.drawText(mLowerChartTabTitles[i], mTabWidth * i + mTabWidth / 2.0f
                                - mLowerChartTabTitles[i].length() / 3.0f * DEFAULT_AXIS_TITLE_SIZE,
                        LOWER_CHART_TOP - mTabHight / 2.0f + DEFAULT_AXIS_TITLE_SIZE / 2.0f,
                        textPaint);
            }
        }
    }

    public int getBackGround() {
        return mBackGround;
    }

    public void setBackGround(int BackGround) {
        this.mBackGround = BackGround;
    }

    public int getAxisColor() {
        return mAxisColor;
    }

    public void setAxisColor(int AxisColor) {
        this.mAxisColor = AxisColor;
    }

    public int getLongiLatitudeColor() {
        return mLongiLatitudeColor;
    }

    public void setLongiLatitudeColor(int LongiLatitudeColor) {
        this.mLongiLatitudeColor = LongiLatitudeColor;
    }

    public PathEffect getDashEffect() {
        return mDashEffect;
    }

    public void setDashEffect(PathEffect DashEffect) {
        this.mDashEffect = DashEffect;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int BorderColor) {
        this.mBorderColor = BorderColor;
    }

    public float getUperChartHeight() {
        return mUperChartHeight;
    }

    public void setUperChartHeight(float UperChartHeight) {
        this.mUperChartHeight = UperChartHeight;
    }

    public boolean isShowLowerChartTabs() {
        return showLowerChartTabs;
    }

    public void setShowLowerChartTabs(boolean showLowerChartTabs) {
        this.showLowerChartTabs = showLowerChartTabs;
    }

    public float getLowerChartHeight() {
        return mLowerChartHeight;
    }

    public void setLowerChartHeight(float LowerChartHeight) {
        this.mLowerChartHeight = LowerChartHeight;
    }

    public String[] getLowerChartTabTitles() {
        return mLowerChartTabTitles;
    }

    public void setLowerChartTabTitles(String[] LowerChartTabTitles) {
        this.mLowerChartTabTitles = LowerChartTabTitles;
    }

    public float getLongitudeSpacing() {
        return longitudeSpacing;
    }

    public void setLongitudeSpacing(float longitudeSpacing) {
        this.longitudeSpacing = longitudeSpacing;
    }

    public float getLatitudeSpacing() {
        return latitudeSpacing;
    }

    public void setLatitudeSpacing(float latitudeSpacing) {
        this.latitudeSpacing = latitudeSpacing;
    }

    public void setShowTopTitles(boolean showTopTitles) {
        this.showTopTitles = showTopTitles;
    }

    public float getTopTitleHeight() {
        return topTitleHeight;
    }

    public int getUpperLatitudeNum() {
        return upperLatitudeNum;
    }

    public void setUpperLatitudeNum(int upperLatitudeNum) {
        this.upperLatitudeNum = upperLatitudeNum;
    }

    public int getLowerLatitudeNum() {
        return lowerLatitudeNum;
    }

    public void setLowerLatitudeNum(int lowerLatitudeNum) {
        this.lowerLatitudeNum = lowerLatitudeNum;
    }

    public boolean isDisplayLongitude() {
        return displayLongitude;
    }

    public void setDisplayLongitude(boolean displayLongitude) {
        this.displayLongitude = displayLongitude;
    }

    public boolean isDisplayLatitude() {
        return displayLatitude;
    }

    public void setDisplayLatitude(boolean displayLatitude) {
        this.displayLatitude = displayLatitude;
    }

    public boolean isDisplayAxisXTitle() {
        return displayAxisXTitle;
    }

    public void setDisplayAxisXTitle(boolean displayAxisXTitle) {
        this.displayAxisXTitle = displayAxisXTitle;
    }

    public boolean isDisplayAxisYTitle() {
        return displayAxisYTitle;
    }

    public void setDisplayAxisYTitle(boolean displayAxisYTitle) {
        this.displayAxisYTitle = displayAxisYTitle;
    }


}
