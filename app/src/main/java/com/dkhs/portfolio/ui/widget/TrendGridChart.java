package com.dkhs.portfolio.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ColorTemplate;

import java.util.List;

/**
 * 坐�?轴使用的View
 *
 * @author limc
 */
public class TrendGridChart extends View {

    // ////////////默认值////////////////
    /**
     * 默认背景色
     */
    public static final int DEFAULT_BACKGROUD_COLOR = Color.WHITE;

    /**
     * 默认X坐标轴颜色
     */
    public static final int DEFAULT_AXIS_X_COLOR = Color.LTGRAY;

    /**
     * 默认Y坐标轴颜色
     */
    public static final int DEFAULT_AXIS_Y_COLOR = Color.LTGRAY;

    /**
     * 默认经线颜色
     */
    public static final int DEFAULT_LONGITUDE_COLOR = Color.GRAY;

    /**
     * 默认纬线颜色
     */
    public static final int DEFAULT_LAITUDE_COLOR = Color.GRAY;

    /**
     * 默认轴线左边距
     */
    public static final float DEFAULT_AXIS_MARGIN_LEFT = 10f;

    /**
     * 默认轴线底边据
     */
    public static final float DEFAULT_AXIS_MARGIN_BOTTOM = 0f;

    /**
     * 默认轴线上边距
     */
    public static final float DEFAULT_AXIS_MARGIN_TOP = 5f;

    /**
     * 默认轴线右边距
     */
    public static final float DEFAULT_AXIS_MARGIN_RIGHT = 10f;

    /**
     * 默认经线是否显示刻度
     */
    public static final boolean DEFAULT_DISPLAY_LONGTITUDE = Boolean.TRUE;

    /**
     * 默认经线是否使用虚线
     */
    public static final boolean DEFAULT_DASH_LONGTITUDE = Boolean.TRUE;

    /**
     * 默认纬线是否显示刻度
     */
    public static final boolean DEFAULT_DISPLAY_LATITUDE = Boolean.TRUE;

    /**
     * 默认纬线是否使用虚线
     */
    public static final boolean DEFAULT_DASH_LATITUDE = Boolean.TRUE;

    /**
     * 默认是否显示X轴刻度
     */
    public static final boolean DEFAULT_DISPLAY_AXIS_X_TITLE = Boolean.TRUE;

    /**
     * 默认是否显示Y轴刻度
     */
    public static final boolean DEFAULT_DISPLAY_AXIS_Y_TITLE = Boolean.TRUE;

    /**
     * 默认是否显示边框
     */
    public static final boolean DEFAULT_DISPLAY_BORDER = Boolean.FALSE;

    /**
     * 默认边框颜色
     */
    public static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;

    /**
     * 默认经线刻度字体颜色 *
     */
    private int DEFAULT_LONGTITUDE_FONT_COLOR = Color.GRAY;
    /**
     * 默认经线刻度字体颜色 *
     */
    private int DEFAULT_LONGTITUDE_FONT_COLOR_UP = Color.RED;
    /**
     * 默认经线刻度字体颜色 *
     */
    private int DEFAULT_LONGTITUDE_FONT_COLOR_MID = Color.BLACK;
    /**
     * 默认经线刻度字体颜色 *
     */
    private int DEFAULT_LONGTITUDE_FONT_COLOR_DOWN = Color.GREEN;

    /**
     * 默认经线刻度字体大小 *
     */
    private int DEFAULT_LONGTITUDE_FONT_SIZE = 10;

    /**
     * 默认经线刻度字体颜色 *
     */
    private int DEFAULT_LATITUDE_FONT_COLOR = Color.BLACK;

    /**
     * 默认经线刻度字体字体 *
     */
    private int DEFAULT_LATITUDE_FONT_SIZE = 15;

    /**
     * 默认Y轴刻度最大显示长度
     */
    private int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 5;

    /**
     * 默认虚线效果
     */
    public static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(new float[]{3, 3, 3, 3}, 1);

    /**
     * 在控件被点击时默认显示X字线
     */
    public static final boolean DEFAULT_DISPLAY_CROSS_X_ON_TOUCH = true;

    /**
     * 在控件被点击时显示Y轴线
     */
    public static final boolean DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH = true;

    /**
     * // /////////////属性////////////////
     * <p/>
     * /** 背景色
     */
    private int backgroudColor = DEFAULT_BACKGROUD_COLOR;

    /**
     * 坐标轴X颜色
     */
    private int axisXColor = DEFAULT_AXIS_X_COLOR;

    /**
     * 坐标轴Y颜色
     */
    private int axisYColor = DEFAULT_AXIS_Y_COLOR;

    /**
     * 经线颜色
     */
    private int longitudeColor = DEFAULT_LONGITUDE_COLOR;

    /**
     * 纬线颜色
     */
    private int latitudeColor = DEFAULT_LAITUDE_COLOR;

    /**
     * 轴线左边距
     */
    protected float axisMarginLeft = DEFAULT_AXIS_MARGIN_LEFT;

    /**
     * 轴线底边距
     */
    protected float axisMarginBottom = DEFAULT_AXIS_MARGIN_BOTTOM;

    /**
     * 轴线上边距
     */
    protected float axisMarginTop = DEFAULT_AXIS_MARGIN_TOP;

    /**
     * 轴线右边距
     */
    protected float axisMarginRight = DEFAULT_AXIS_MARGIN_RIGHT;

    /**
     * x轴是否显示
     */
    private boolean displayAxisXTitle = DEFAULT_DISPLAY_AXIS_X_TITLE;

    /**
     * y轴是否显示
     */
    private boolean displayAxisYTitle = DEFAULT_DISPLAY_AXIS_Y_TITLE;

    /**
     * 经线颜色是否对称
     */
    private boolean displayAxisYTitleColor = Boolean.TRUE;


    /**
     * 右部标题颜色是否以0为基准
     */
    private boolean displayYRightTitleByZero = Boolean.FALSE;

    /**
     * 经线是否显示
     */
    private boolean displayLongitude = DEFAULT_DISPLAY_LONGTITUDE;

    /**
     * 经线是否使用虚线
     */
    private boolean dashLongitude = DEFAULT_DASH_LONGTITUDE;

    /**
     * 纬线是否显示
     */
    private boolean displayLatitude = DEFAULT_DISPLAY_LATITUDE;

    /**
     * 纬线是否使用虚线
     */
    private boolean dashLatitude = DEFAULT_DASH_LATITUDE;

    /**
     * 虚线效果
     */
    private PathEffect dashEffect = DEFAULT_DASH_EFFECT;

    /**
     * 显示边框
     */
    private boolean displayBorder = DEFAULT_DISPLAY_BORDER;

    /**
     * 边框色
     */
    private int borderColor = DEFAULT_BORDER_COLOR;

    /**
     * 经线刻度字体颜色 *
     */
    private int longtitudeFontColor = DEFAULT_LONGTITUDE_FONT_COLOR;

    /**
     * 经线x轴刻度字体大小 *
     */
    private int longtitudeFontSize = DEFAULT_LONGTITUDE_FONT_SIZE;

    /**
     * 经线刻度字体颜色 *
     */
    private int latitudeFontColor = DEFAULT_LATITUDE_FONT_COLOR;

    /**
     * 经线刻度字体颜色 *
     */
    private int latitudeFontSize = DEFAULT_LATITUDE_FONT_SIZE;

    /**
     * 横轴刻度�?�?
     */
    private List<String> axisXTitles;

    /**
     * 纵轴刻度�?�?
     */
    private List<String> axisYTitles;
    private List<String> axisRightYTitles;

    /**
     * 纵轴刻度�?��字符数
     */
    private int axisYMaxTitleLength = DEFAULT_AXIS_Y_MAX_TITLE_LENGTH;

    /**
     * 在控件被点击时显示x轴线
     */
    private boolean displayCrossXOnTouch = DEFAULT_DISPLAY_CROSS_X_ON_TOUCH;

    /**
     * 在控件被点击时显示y横线线
     */
    private boolean displayCrossYOnTouch = DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH;

    // /** 选中位置X坐标 */
    // private float clickPostX = 0f;
    //
    // /** 选中位置Y坐标 */
    // private float clickPostY = 0f;

    /**
     * 通知对象列表
     */
    // private List<ITouchEventResponse> notifyList;

    // /** 当前被选中的坐标点 */
    // private PointF touchPoint;
    //
    // private boolean isTouch;

    private boolean isDrawXBorke;
    private boolean isDrawRightYTitle;

    /** final bitmap that contains all information and is drawn to the screen */
    // protected Bitmap mDrawBitmap;

    /**
     * the canvas that is used for drawing on the bitmap
     */
    // protected Canvas mDrawCanvas;

    private final int xLineCounts = 5;
    private int yLineCounts = 5;

    private Paint mTextPaint;
    public int xTitleTextHeight = 0;

    public float mGridLineHeight = 0;
    public float mGridLineLenght;
    public float mStartLineYpoint = 0;
    public float mStartLineXpoint;
    public float volHight;
    private boolean isMeasure = true;

    public TrendGridChart(Context context) {
        super(context);
        init(context, null);
    }

    public TrendGridChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);

    }

    public TrendGridChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private int mHightWeight;
    private int mWidthWeight;

    private void init(Context context, AttributeSet attrs) {

        // this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        // @Override
        // public void onGlobalLayout() {
        // getLayoutParams().height = getWidth() * 3 / 4;
        //
        // }
        // });
        if (null != attrs) {

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrendChart);
            mHightWeight = a.getInt(R.styleable.TrendChart_gridHeightWeight, 0);
            mWidthWeight = a.getInt(R.styleable.TrendChart_gridWidthWeight, 0);

            a.recycle();
        }

        latitudeFontSize = getResources().getDimensionPixelSize(R.dimen.title_text_font);
        longtitudeFontSize = getResources().getDimensionPixelSize(R.dimen.title_text_font);

        mTextPaint = new Paint();

        mTextPaint.setTextSize(latitudeFontSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(ColorTemplate.THEME_COLOR);
        // mXTitlePaint.setTextAlign(Paint.Align.CENTER);
        // if (dashLongitude) {
        FontMetrics fm = mTextPaint.getFontMetrics();
        xTitleTextHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);

    }


    /**
     * 重新控件大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        int measuredHeight = measureWidth(heightMeasureSpec);
        if (mHightWeight > 0 && mWidthWeight > 0) {
            measuredHeight = measuredWidth / mWidthWeight * mHightWeight;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public void resetLayoutWeight(int widthWeight, int heightWeight) {
        this.mHightWeight = heightWeight;
        this.mWidthWeight = widthWeight;
        requestLayout();
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        if (this.isMeasure) {
            this.isMeasure = false;
            mGridLineHeight = getmGridLineHeight();
            mStartLineYpoint = axisMarginTop + xTitleTextHeight / 2;
            mStartLineXpoint = axisMarginLeft;
            mGridLineLenght = getWidth() - mStartLineXpoint - axisMarginRight;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }

        drawXtitleText(canvas);

        // 设置背景色
         super.setBackgroundColor(backgroudColor);
        // 绘制XY轴
        if (isDrawXBorke) {
            drawXAxis(canvas);
        }
        // drawYAxis(canvas);

        // 绘制边界
        if (this.displayBorder) {
            drawBorder(canvas);
        }

        // 绘制经线纬线
        // if (displayLongitude) {
        drawAxisGridX(canvas);
        // }
        if (displayLatitude || displayAxisYTitle) {
            // System.out.println("displayAxisYTitle:"+);
            drawAxisGridY(canvas);
        }

        reDrawYtitleText(canvas);

    }


    public void reDrawYtitleText(Canvas canvas) {
        if (isDrawRightYTitle) {
            drawRightYtitleText(canvas);
        }

        drawYtitleText(canvas);
    }

    /**
     * @param canvas
     * @return void
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    public void drawYtitleText(Canvas canvas) {
        if (null != axisYTitles) {
            int counts = axisYTitles.size();
            float length = super.getWidth() - axisMarginLeft;
            // 线条Paint
            // Paint mPaintLine = new Paint();
            // mXTitlePaint.reset();
            // mXTitlePaint.setColor(latitudeColor);
            // if (dashLatitude) {
            // mXTitlePaint.setPathEffect(dashEffect);
            // }
            // �?��Paint
            mTextPaint.reset();
            // Paint mPaintFont = new Paint();
            mTextPaint.setColor(latitudeFontColor);
            mTextPaint.setTextSize(latitudeFontSize);

            mTextPaint.setAntiAlias(true);

            // 绘制线条坐Y轴
            if (counts > 1) {
                float postOffset = (mGridLineHeight - axisMarginTop - xTitleTextHeight / 2) / (counts - 1);
                float offset = super.getHeight() - axisMarginBottom - axisMarginTop - xTitleTextHeight - getVolHight();

                // float offsetX = super.getHeight() - axisMarginBottom - xTitleTextHeight / 2;

                for (int i = 0; i < counts; i++) {
                    // 绘制线条
                    // if (displayLatitude) {
                    // canvas.drawLine(axisMarginLeft, offset - i * postOffset, axisMarginLeft + length, offset - i
                    // * postOffset, mPaintLine);
                    // }
                    // 绘制刻度
                    if (displayAxisYTitle) {

                        if (displayAxisYTitleColor) {
                            if(isDrawBenefit){
                                mTextPaint.setColor(getYTitlePaintFontForBenefit(i));
                            }else{
                                mTextPaint.setColor(getYTitlePaintFont(i, counts));
                            }
                        }

                        if (i == 0) {
                            canvas.drawText(axisYTitles.get(i), axisMarginLeft, offset - i * postOffset
                                    + latitudeFontSize / 2f, mTextPaint);
                        } else if (i == counts - 1) {
//                            canvas.drawText(axisYTitles.get(i), axisMarginLeft, offset - i * postOffset
//                                    + latitudeFontSize / 2f + xTitleTextHeight, mTextPaint);
                            canvas.drawText(axisYTitles.get(i), axisMarginLeft, offset - i * postOffset
                                    + latitudeFontSize + latitudeFontSize / 2, mTextPaint);
                        } else {
//                            canvas.drawText(axisYTitles.get(i), axisMarginLeft, offset - i * postOffset
//                                    + latitudeFontSize / 2f + xTitleTextHeight / 2, mTextPaint);
                            canvas.drawText(axisYTitles.get(i), axisMarginLeft, offset - i * postOffset
                                    + latitudeFontSize, mTextPaint);
                        }

                        // canvas.drawText(axisYTitles.get(i), axisMarginLeft, super.getHeight() - this.axisMarginBottom
                        // - 5f,
                        // mPaintFont);
                        // }
                    }
                }
            }
        }

    }

    public void drawRightYtitleText(Canvas canvas) {
        if (null != axisRightYTitles) {
            int counts = axisRightYTitles.size();

            // 线条Paint
            // Paint mPaintLine = new Paint();
            // mPaintLine.setColor(latitudeColor);
            // if (dashLatitude) {
            // mPaintLine.setPathEffect(dashEffect);
            // }
            // �?��Paint
            // Paint mPaintFont = new Paint();
            mTextPaint.reset();
            mTextPaint.setColor(latitudeFontColor);
            mTextPaint.setTextSize(latitudeFontSize);

            mTextPaint.setAntiAlias(true);

            // 绘制线条坐Y轴
            if (counts > 1) {
                float postOffset = (mGridLineHeight - axisMarginTop - xTitleTextHeight / 2) / (counts - 1);
                float offset = super.getHeight() - axisMarginBottom - axisMarginTop - xTitleTextHeight - getVolHight();

                for (int i = 0; i < counts; i++) {
                    float xTitleWidth = mTextPaint.measureText(axisRightYTitles.get(i));
                    float offetText = xTitleWidth;
                    float startX = (super.getWidth() - axisMarginLeft - offetText);
                    if (displayAxisYTitleColor) {
                        mTextPaint.setColor(getLongtitudeFontColor());
                    }
                    if (displayYRightTitleByZero) {
                        mTextPaint.setColor(ColorTemplate.getPercentColor(axisRightYTitles.get(i)));
                    }

                    if (i == 0) {
                        canvas.drawText(axisRightYTitles.get(i), startX, offset - i * postOffset + latitudeFontSize
                                / 2f, mTextPaint);
                    } else if (i == counts - 1) {
                        canvas.drawText(axisRightYTitles.get(i), startX, offset - i * postOffset + latitudeFontSize
                                / 2f + latitudeFontSize, mTextPaint);
                    } else {
                        canvas.drawText(axisRightYTitles.get(i), startX, offset - i * postOffset + latitudeFontSize
                                , mTextPaint);
                    }

                    // canvas.drawText(axisRightYTitles.get(i), startX, offset - i * postOffset + latitudeFontSize / 2f,
                    // mPaintFont);

                }
            }
        }

    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    /**
     * 失去焦点事件
     */
    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

    }

    private void drawXtitleText(Canvas canvas) {
        if (null != axisXTitles && axisXTitles.size() > 0) {
            mTextPaint.reset();
            mTextPaint.setTextSize(latitudeFontSize);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(ColorTemplate.THEME_COLOR);
            int counts = axisXTitles.size();

            // 1. 粗略计算文字宽度

            float offset = axisMarginLeft;
            float offsetX = super.getHeight() - axisMarginBottom;

            float offetText = 0;

            float xTitleWidth = mTextPaint.measureText(axisXTitles.get(counts - 1));
            offetText = xTitleWidth / 2;

            float postOffset = (super.getWidth() - axisMarginLeft - axisMarginRight) / (counts - 1);

            for (int i = 0; i < counts; i++) {

                if (displayAxisXTitle) {

                    if (i == 0 && axisXTitles.size() > 0) {

                        canvas.drawText(axisXTitles.get(i), offset + i * postOffset, offsetX, mTextPaint);

                    } else if (i < axisXTitles.size() - 1) {

                        canvas.drawText(axisXTitles.get(i), offset + i * postOffset - offetText, offsetX, mTextPaint);
                    } else if (i == axisXTitles.size() - 1) {
                        canvas.drawText(axisXTitles.get(i), super.getWidth() - xTitleWidth - axisMarginRight, offsetX,
                                mTextPaint);
                    }

                }
            }

        }
    }

    /**
     * @param value
     * @return
     */
    public String getAxisXGraduate(Object value) {

        float length = super.getWidth() - axisMarginLeft - 2 * axisMarginRight;
        float valueLength = ((Float) value).floatValue() - axisMarginLeft - axisMarginRight;

        return String.valueOf(valueLength / length);
    }

    /**
     * 获取Y轴刻度�??,�?�??�最大1
     *
     * @param value
     * @return
     */
    public String getAxisYGraduate(Object value) {

        float length = super.getHeight() - axisMarginBottom - 2 * axisMarginTop;
        float valueLength = length - (((Float) value).floatValue() - axisMarginTop);

        return String.valueOf(valueLength / length);
    }

    private int getYTitlePaintFont(int index, int counts) {
        double midIndex = Math.ceil(counts / 2.0) - 1;
        // System.out.println("Index :" + index + " mid:" + midIndex);
        int textColor;
        if (index < midIndex) {
            textColor = ColorTemplate.DEF_GREEN;
        } else if (index == midIndex) {
            textColor = DEFAULT_LONGTITUDE_FONT_COLOR_MID;
        } else {
            textColor = ColorTemplate.DEF_RED;

        }
        return textColor;
    }
    private boolean isBenefitDash = false;

    public void setIsBenefitDash(boolean isBenefitDash) {
        this.isBenefitDash = isBenefitDash;
    }

    private boolean isDrawBenefit = false;

    public void setIsDrawBenefit(boolean isDrawBenefit) {
        this.isDrawBenefit = isDrawBenefit;
    }

    private int getYTitlePaintFontForBenefit(int index) {
        int textColor;
        if(axisYTitles.get(index).startsWith("-")){
            textColor = ColorTemplate.DEF_GREEN;
        }else{
            textColor = ColorTemplate.DEF_RED;
        }
        return textColor;
    }

    /**
     * 绘制边�?
     *
     * @param canvas
     */
    protected void drawBorder(Canvas canvas) {
        float width = super.getWidth() - 2;
        float height = super.getHeight() - 2;

        mTextPaint.reset();
        mTextPaint.setColor(borderColor);

        // 绘制边
        canvas.drawLine(1f, 1f, 1f + width, 1f, mTextPaint);
        canvas.drawLine(1f + width, 1f, 1f + width, 1f + height, mTextPaint);
        canvas.drawLine(1f + width, 1f + height, 1f, 1f + height, mTextPaint);
        canvas.drawLine(1f, 1f + height, 1f, 1f, mTextPaint);
    }

    /**
     * 绘制X轴
     *
     * @param canvas
     */
    protected void drawXAxis(Canvas canvas) {

        float length = super.getWidth();
        float postY = super.getHeight() - axisMarginBottom - 1;

        // Paint mPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setColor(axisXColor);

        canvas.drawLine(0f, postY, length, postY, mTextPaint);

    }

    /**
     * 绘制Y轴
     *
     * @param canvas
     */
    protected void drawYAxis(Canvas canvas) {

        float length = super.getHeight() - axisMarginBottom;
        float postX = axisMarginLeft + 1;

        mTextPaint.reset();
        mTextPaint.setColor(axisXColor);

        canvas.drawLine(postX, 0f, postX, length, mTextPaint);
    }

    /**
     * 绘制竖线
     *
     * @param canvas
     */
    protected void drawAxisGridX(Canvas canvas) {

        int counts = yLineCounts;

        mTextPaint.reset();
        mTextPaint.setColor(longitudeColor);
        // mTextPaint.setAntiAlias(true);

        if (counts > 1) {
            float postOffset = mGridLineLenght / (counts - 1);
            float offset = axisMarginLeft;
            for (int i = 0; i < counts; i++) {
                if (i == 0 || i == counts - 1) {

                    mTextPaint.setPathEffect(null);
                } else {

                    mTextPaint.setPathEffect(dashEffect);
                }
                // 绘制线条
                if (displayLongitude) {
                    canvas.drawLine(offset + i * postOffset, mStartLineYpoint, offset + i * postOffset,
                            mGridLineHeight, mTextPaint);
                }

            }
        }
        // }
    }

    /**
     * 绘制横线
     *
     * @param canvas
     */
    protected void drawAxisGridY(Canvas canvas) {
        // if (null != axisYTitles) {
        int counts = xLineCounts;
        float length = mGridLineLenght;
        float height = mGridLineHeight;
        // 线条Paint
        // Paint mPaintLine = new Paint();
        mTextPaint.reset();
        // mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(latitudeColor);
        if (dashLatitude) {
            mTextPaint.setPathEffect(dashEffect);
        }

        // 绘制线条坐�?轴
        if (counts > 1) {

            float postOffset = (mGridLineHeight - axisMarginTop - xTitleTextHeight / 2) / (counts - 1);
            // float postOffset = (super.getHeight() - axisMarginBottom - axisMarginTop * 2 - xTitleTextHeight)
            // / (counts - 1);

            // float offsetX = super.getHeight() - axisMarginBottom - xTitleTextHeight / 2;

            for (int i = 0; i < counts; i++) {
                mTextPaint.setPathEffect(dashEffect);

                    if (i == 0 || i == counts - 1) {
                        mTextPaint.setPathEffect(null);
                    }
                if(!isBenefitDash && i == 2){
                    mTextPaint.setPathEffect(null);
                }
                // 绘制线条
                if (displayLatitude) {
                    canvas.drawLine(mStartLineXpoint, height - i * postOffset, length + mStartLineXpoint, height - i
                            * postOffset, mTextPaint);
                }

            }
        }

    }

    private void drawDashLine() {

    }

    protected void zoomIn() {

    }

    protected void zoomOut() {

    }

    public int getBackgroudColor() {
        return backgroudColor;
    }

    public void setBackgroudColor(int backgroudColor) {
        this.backgroudColor = backgroudColor;
    }

    public int getAxisXColor() {
        return axisXColor;
    }

    public void setAxisXColor(int axisXColor) {
        this.axisXColor = axisXColor;
    }

    public int getAxisYColor() {
        return axisYColor;
    }

    public void setAxisYColor(int axisYColor) {
        this.axisYColor = axisYColor;
    }

    public int getLongitudeColor() {
        return longitudeColor;
    }

    public void setLongitudeColor(int longitudeColor) {
        this.longitudeColor = longitudeColor;
    }

    public int getLatitudeColor() {
        return latitudeColor;
    }

    public void setLatitudeColor(int latitudeColor) {
        this.latitudeColor = latitudeColor;
    }

    public float getAxisMarginLeft() {
        return axisMarginLeft;
    }

    public void setAxisMarginLeft(float axisMarginLeft) {
        this.axisMarginLeft = axisMarginLeft;

        // 如果左边距为0?��?不显示Y坐�?轴
        if (0f == axisMarginLeft) {
            this.displayAxisYTitle = Boolean.FALSE;
        }
    }

    public float getAxisMarginBottom() {
        return axisMarginBottom;
    }

    public void setAxisMarginBottom(float axisMarginBottom) {
        this.axisMarginBottom = axisMarginBottom;

        // 如果下边距为0?��?不显示X坐�?轴
        if (0f == axisMarginBottom) {
            this.displayAxisXTitle = Boolean.FALSE;
        }
    }

    public float getAxisMarginTop() {
        return axisMarginTop;
    }

    public void setAxisMarginTop(float axisMarginTop) {
        this.axisMarginTop = axisMarginTop;
    }

    public float getAxisMarginRight() {
        return axisMarginRight;
    }

    public void setAxisMarginRight(float axisMarginRight) {
        this.axisMarginRight = axisMarginRight;
    }

    public List<String> getAxisXTitles() {
        return axisXTitles;
    }

    public void setAxisXTitles(List<String> axisXTitles) {
        this.axisXTitles = axisXTitles;
    }

    public List<String> getAxisYTitles() {
        return axisYTitles;
    }

    public void setAxisYTitles(List<String> axisYTitles) {
        this.axisYTitles = axisYTitles;
    }

    public boolean isDisplayLongitude() {
        return displayLongitude;
    }

    public void setDisplayLongitude(boolean displayLongitude) {
        this.displayLongitude = displayLongitude;
    }

    public boolean isDashLongitude() {
        return dashLongitude;
    }

    public void setDashLongitude(boolean dashLongitude) {
        this.dashLongitude = dashLongitude;
    }

    public boolean isDisplayLatitude() {
        return displayLatitude;
    }

    public void setDisplayLatitude(boolean displayLatitude) {
        this.displayLatitude = displayLatitude;
    }

    public boolean isDashLatitude() {
        return dashLatitude;
    }

    public void setDashLatitude(boolean dashLatitude) {
        this.dashLatitude = dashLatitude;
    }

    public PathEffect getDashEffect() {
        return dashEffect;
    }

    public void setDashEffect(PathEffect dashEffect) {
        this.dashEffect = dashEffect;
    }

    public boolean isDisplayAxisXTitle() {
        return displayAxisXTitle;
    }

    public void setDisplayAxisXTitle(boolean displayAxisXTitle) {
        this.displayAxisXTitle = displayAxisXTitle;

        // 如果不显示X轴刻度,则底边边距为0
        if (false == displayAxisXTitle) {
            this.axisMarginBottom = 0;
        }
    }

    public boolean isDisplayAxisYTitle() {
        return displayAxisYTitle;
    }

    public void setDisplayAxisYTitleColor(boolean isDisplayYtitleColor) {
        this.displayAxisYTitleColor = isDisplayYtitleColor;
    }

    public boolean isMidAxisYTitle() {
        return displayAxisYTitle;
    }

    public void setDisplayAxisYTitle(boolean displayAxisYTitle) {
        this.displayAxisYTitle = displayAxisYTitle;

        // 如果不显示Y轴刻度,则左边边距为0
        if (false == displayAxisYTitle) {
            this.axisMarginLeft = 0;
        }
    }

    public boolean isDisplayBorder() {
        return displayBorder;
    }

    public void setDisplayBorder(boolean displayBorder) {
        this.displayBorder = displayBorder;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public int getLongtitudeFontColor() {
        return longtitudeFontColor;
    }

    public void setLongtitudeFontColor(int longtitudeFontColor) {
        this.longtitudeFontColor = longtitudeFontColor;
    }

    public int getLongtitudeFontSize() {
        return longtitudeFontSize;
    }

    public void setLongtitudeFontSize(int longtitudeFontSize) {
        this.longtitudeFontSize = longtitudeFontSize;
    }

    public int getLatitudeFontColor() {
        return latitudeFontColor;
    }

    public void setLatitudeFontColor(int latitudeFontColor) {
        this.latitudeFontColor = latitudeFontColor;
    }

    public int getLatitudeFontSize() {
        return latitudeFontSize;
    }

    public void setLatitudeFontSize(int latitudeFontSize) {
        this.latitudeFontSize = latitudeFontSize;
    }

    public int getAxisYMaxTitleLength() {
        return axisYMaxTitleLength;
    }

    public void setAxisYMaxTitleLength(int axisYMaxTitleLength) {
        this.axisYMaxTitleLength = axisYMaxTitleLength;
    }

    public boolean isDisplayCrossXOnTouch() {
        return displayCrossXOnTouch;
    }

    public void setDisplayCrossXOnTouch(boolean displayCrossXOnTouch) {
        this.displayCrossXOnTouch = displayCrossXOnTouch;
    }

    public boolean isDisplayCrossYOnTouch() {
        return displayCrossYOnTouch;
    }

    public void setDisplayCrossYOnTouch(boolean displayCrossYOnTouch) {
        this.displayCrossYOnTouch = displayCrossYOnTouch;
    }

    public boolean isDrawXBorke() {
        return isDrawXBorke;
    }

    public void setDrawXBorke(boolean isDrawXBorke) {
        this.isDrawXBorke = isDrawXBorke;
    }

    public boolean isDrawRightYTitle() {
        return isDrawRightYTitle;
    }

    public void setDrawRightYTitle(boolean isDrawRightYTitle) {
        this.isDrawRightYTitle = isDrawRightYTitle;
    }

    public List<String> getAxisRightYTitles() {
        return axisRightYTitles;
    }

    public void setAxisRightYTitles(List<String> axisRightYTitles) {
        this.axisRightYTitles = axisRightYTitles;
    }

    public float getmGridLineHeight() {
        return getHeight() - xTitleTextHeight / 2 - axisMarginTop - axisMarginBottom;
    }

    public void setmGridLineHeight(float mGridLineHeight) {
        this.mGridLineHeight = mGridLineHeight;
    }

    public float getmGridLineLenght() {
        return mGridLineLenght;
    }

    public void setmGridLineLenght(float mGridLineLenght) {
        this.mGridLineLenght = mGridLineLenght;
    }

    public float getVolHight() {
        return volHight;
    }

    public void setVolHight(float volHight) {
        this.volHight = volHight;
    }

    public int getxLineCounts() {
        return xLineCounts;
    }

    protected Paint getmTextPaint() {
        return mTextPaint;
    }

    public int getyLineCounts() {
        return yLineCounts;
    }

    public void setYlineCounts(int counts) {
        this.yLineCounts = counts;
    }

    public boolean isDisplayYRightTitleByZero() {
        return displayYRightTitleByZero;
    }

    public void setDisplayYRightTitleByZero(boolean displayYRightTitleByZero) {
        this.displayYRightTitleByZero = displayYRightTitleByZero;
    }
}
