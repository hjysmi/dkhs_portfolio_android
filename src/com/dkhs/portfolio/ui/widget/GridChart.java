package com.dkhs.portfolio.ui.widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.AttributeSet;
import android.view.View;

import com.dkhs.portfolio.R;

/**
 * 坐�?轴使用的View
 * 
 * @author limc
 * 
 */
public class GridChart extends View {

    // ////////////默认值////////////////
    /** 默认背景色 */
    public static final int DEFAULT_BACKGROUD_COLOR = Color.BLACK;

    /** 默认X坐标轴颜色 */
    public static final int DEFAULT_AXIS_X_COLOR = Color.LTGRAY;

    /** 默认Y坐标轴颜色 */
    public static final int DEFAULT_AXIS_Y_COLOR = Color.RED;

    /** 默认经线颜色 */
    public static final int DEFAULT_LONGITUDE_COLOR = Color.RED;

    /** 默认纬线颜色 */
    public static final int DEFAULT_LAITUDE_COLOR = Color.RED;

    /** 默认轴线左边距 */
    public static final float DEFAULT_AXIS_MARGIN_LEFT = 42f;

    /** 默认轴线底边据 */
    public static final float DEFAULT_AXIS_MARGIN_BOTTOM = 0f;

    /** 默认轴线上边距 */
    public static final float DEFAULT_AXIS_MARGIN_TOP = 5f;

    /** 默认轴线右边距 */
    public static final float DEFAULT_AXIS_MARGIN_RIGHT = 5f;

    /** 默认经线是否显示刻度 */
    public static final boolean DEFAULT_DISPLAY_LONGTITUDE = Boolean.TRUE;

    /** 默认经线是否使用虚线 */
    public static final boolean DEFAULT_DASH_LONGTITUDE = Boolean.TRUE;

    /** 默认纬线是否显示刻度 */
    public static final boolean DEFAULT_DISPLAY_LATITUDE = Boolean.TRUE;

    /** 默认纬线是否使用虚线 */
    public static final boolean DEFAULT_DASH_LATITUDE = Boolean.TRUE;

    /** 默认是否显示X轴刻度 */
    public static final boolean DEFAULT_DISPLAY_AXIS_X_TITLE = Boolean.TRUE;

    /** 默认是否显示Y轴刻度 */
    public static final boolean DEFAULT_DISPLAY_AXIS_Y_TITLE = Boolean.TRUE;

    /** 默认是否显示边框 */
    public static final boolean DEFAULT_DISPLAY_BORDER = Boolean.TRUE;

    /** 默认边框颜色 */
    public static final int DEFAULT_BORDER_COLOR = Color.RED;

    /** 默认经线刻度字体颜色 **/
    private int DEFAULT_LONGTITUDE_FONT_COLOR = Color.WHITE;
    /** 默认经线刻度字体颜色 **/
    private int DEFAULT_LONGTITUDE_FONT_COLOR_UP = Color.RED;
    /** 默认经线刻度字体颜色 **/
    private int DEFAULT_LONGTITUDE_FONT_COLOR_MID = Color.BLACK;
    /** 默认经线刻度字体颜色 **/
    private int DEFAULT_LONGTITUDE_FONT_COLOR_DOWN = Color.GREEN;

    /** 默认经线刻度字体大小 **/
    private int DEFAULT_LONGTITUDE_FONT_SIZE = 12;

    /** 默认经线刻度字体颜色 **/
    private int DEFAULT_LATITUDE_FONT_COLOR = Color.RED;;

    /** 默认经线刻度字体字体 **/
    private int DEFAULT_LATITUDE_FONT_SIZE = 12;

    /** 默认Y轴刻度最大显示长度 */
    private int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 5;

    /** 默认虚线效果 */
    public static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(new float[] { 3, 3, 3, 3 }, 1);

    /** 在控件被点击时默认显示X字线 */
    public static final boolean DEFAULT_DISPLAY_CROSS_X_ON_TOUCH = true;

    /** 在控件被点击时显示Y轴线 */
    public static final boolean DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH = true;

    /**
     * // /////////////属性////////////////
     * 
     * /** 背景色
     */
    private int backgroudColor = DEFAULT_BACKGROUD_COLOR;

    /** 坐标轴X颜色 */
    private int axisXColor = DEFAULT_AXIS_X_COLOR;

    /** 坐标轴Y颜色 */
    private int axisYColor = DEFAULT_AXIS_Y_COLOR;

    /** 经线颜色 */
    private int longitudeColor = DEFAULT_LONGITUDE_COLOR;

    /** 纬线颜色 */
    private int latitudeColor = DEFAULT_LAITUDE_COLOR;

    /** 轴线左边距 */
    protected float axisMarginLeft = DEFAULT_AXIS_MARGIN_LEFT;

    /** 轴线底边距 */
    protected float axisMarginBottom = DEFAULT_AXIS_MARGIN_BOTTOM;

    /** 轴线上边距 */
    protected float axisMarginTop = DEFAULT_AXIS_MARGIN_TOP;

    /** 轴线右边距 */
    protected float axisMarginRight = DEFAULT_AXIS_MARGIN_RIGHT;

    /** x轴是否显示 */
    private boolean displayAxisXTitle = DEFAULT_DISPLAY_AXIS_X_TITLE;

    /** y轴是否显示 */
    private boolean displayAxisYTitle = DEFAULT_DISPLAY_AXIS_Y_TITLE;

    /** 经线颜色是否对称 */
    private boolean displayAxisYTitleColor = Boolean.FALSE;

    /** 经线是否显示 */
    private boolean displayLongitude = DEFAULT_DISPLAY_LONGTITUDE;

    /** 经线是否使用虚线 */
    private boolean dashLongitude = DEFAULT_DASH_LONGTITUDE;

    /** 纬线是否显示 */
    private boolean displayLatitude = DEFAULT_DISPLAY_LATITUDE;

    /** 纬线是否使用虚线 */
    private boolean dashLatitude = DEFAULT_DASH_LATITUDE;

    /** 虚线效果 */
    private PathEffect dashEffect = DEFAULT_DASH_EFFECT;

    /** 显示边框 */
    private boolean displayBorder = DEFAULT_DISPLAY_BORDER;

    /** 边框色 */
    private int borderColor = DEFAULT_BORDER_COLOR;

    /** 经线刻度字体颜色 **/
    private int longtitudeFontColor = DEFAULT_LONGTITUDE_FONT_COLOR;

    /** 经线x轴刻度字体大小 **/
    private int longtitudeFontSize = DEFAULT_LONGTITUDE_FONT_SIZE;

    /** 经线刻度字体颜色 **/
    private int latitudeFontColor = DEFAULT_LATITUDE_FONT_COLOR;

    /** 经线刻度字体颜色 **/
    private int latitudeFontSize = DEFAULT_LATITUDE_FONT_SIZE;

    /** 横轴刻度�?�? */
    private List<String> axisXTitles;

    /** 纵轴刻度�?�? */
    private List<String> axisYTitles;

    /** 纵轴刻度�?��字符数 */
    private int axisYMaxTitleLength = DEFAULT_AXIS_Y_MAX_TITLE_LENGTH;

    /** 在控件被点击时显示x轴线 */
    private boolean displayCrossXOnTouch = DEFAULT_DISPLAY_CROSS_X_ON_TOUCH;

    /** 在控件被点击时显示y横线线 */
    private boolean displayCrossYOnTouch = DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH;

    // /** 选中位置X坐标 */
    // private float clickPostX = 0f;
    //
    // /** 选中位置Y坐标 */
    // private float clickPostY = 0f;

    /** 通知对象列表 */
    // private List<ITouchEventResponse> notifyList;

    // /** 当前被选中的坐标点 */
    // private PointF touchPoint;
    //
    // private boolean isTouch;

    private boolean isDrawXBorke;

    /** final bitmap that contains all information and is drawn to the screen */
    protected Bitmap mDrawBitmap;

    /** the canvas that is used for drawing on the bitmap */
    protected Canvas mDrawCanvas;

    // ////////////�??方�?//////////////
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
        latitudeFontSize = getResources().getDimensionPixelSize(R.dimen.title_text_font);
        longtitudeFontSize = getResources().getDimensionPixelSize(R.dimen.title_text_font);
    }

    public int xTitleTextHeight = 0;

    // //////////////方�?//////////////
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        drawXtitleText(canvas);
        drawYtitleText(canvas);

        // 设置背景色
        // super.setBackgroundColor(backgroudColor);
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
        if (displayLongitude) {
            drawAxisGridX(canvas);
        }
        if (displayLatitude || displayAxisYTitle) {
            drawAxisGridY(canvas);
        }

        // System.out.println("isTouch Up:" + isTouch);
        // 绘制触摸界面
        // if (displayCrossXOnTouch || displayCrossYOnTouch) {
        // if (isTouch) {
        // drawWithFingerClick(canvas);
        // }

        if (mDrawBitmap == null || mDrawCanvas == null) {

            // use RGB_565 for best performance
            mDrawBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
            mDrawCanvas = new Canvas(mDrawBitmap);
        }
        mDrawCanvas.drawColor(backgroudColor);
        // }
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param canvas
     * @return void
     */

    private void drawYtitleText(Canvas canvas) {
        if (null != axisYTitles) {
            int counts = axisYTitles.size();
            float length = super.getWidth() - axisMarginLeft;
            // 线条Paint
            Paint mPaintLine = new Paint();
            mPaintLine.setColor(latitudeColor);
            if (dashLatitude) {
                mPaintLine.setPathEffect(dashEffect);
            }
            // �?��Paint
            Paint mPaintFont = new Paint();
            mPaintFont.setColor(latitudeFontColor);
            mPaintFont.setTextSize(latitudeFontSize);

            mPaintFont.setAntiAlias(true);

            // 绘制线条坐Y轴
            if (counts > 1) {
                float postOffset = (super.getHeight() - axisMarginBottom - axisMarginTop * 2 - xTitleTextHeight)
                        / (counts);
                float offset = super.getHeight() - axisMarginBottom - axisMarginTop - xTitleTextHeight;

                float offsetX = super.getHeight() - axisMarginBottom - xTitleTextHeight / 2;

                for (int i = 0; i < counts; i++) {
                    // 绘制线条
                    // if (displayLatitude) {
                    // canvas.drawLine(axisMarginLeft, offset - i * postOffset, axisMarginLeft + length, offset - i
                    // * postOffset, mPaintLine);
                    // }
                    // 绘制刻度
                    if (displayAxisYTitle) {

                        if (displayAxisYTitleColor) {
                            mPaintFont.setColor(getYTitlePaintFont(i, counts));
                        }

                        // if (i < counts && i > 0) {
                        canvas.drawText(axisYTitles.get(i), axisMarginLeft, offset - i * postOffset + latitudeFontSize
                                / 2f, mPaintFont);
                        // } else if (0 == i) {
                        // canvas.drawText(axisYTitles.get(i), axisMarginLeft, super.getHeight() - this.axisMarginBottom
                        // - 5f,
                        // mPaintFont);
                        // }
                    }
                }
            }
        }

    }

    /**
     * 重新控件大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
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
        if (null != axisXTitles) {

            int counts = axisXTitles.size();
            Paint mXTitlePaint = new Paint();
            mXTitlePaint.setTextSize(latitudeFontSize);
            mXTitlePaint.setAntiAlias(true);
            mXTitlePaint.setColor(longitudeColor);
            // if (dashLongitude) {
            FontMetrics fm = mXTitlePaint.getFontMetrics();
            xTitleTextHeight = (int) (Math.ceil(fm.descent - fm.ascent) + 2);

            float postOffset = (super.getWidth() - axisMarginLeft - axisMarginRight) / (counts - 1);
            float offset = axisMarginLeft;
            float offsetX = super.getHeight() - axisMarginBottom;

            for (int i = 0; i < counts; i++) {
                // // 绘制线条
                // if (displayLongitude) {
                // canvas.drawLine(offset + i * postOffset, 0f, offset + i * postOffset, offsetX, mXTitlePaint);
                // }
                // 绘制标题刻度
                if (displayAxisXTitle) {
                    // if (i < counts && i > 0) {

                    canvas.drawText(axisXTitles.get(i), offset + i * postOffset - (axisXTitles.get(i).length())
                            * longtitudeFontSize / 2f, offsetX, mXTitlePaint);

                    // } else if (0 == i) {
                    // canvas.drawText(axisXTitles.get(i), this.axisMarginLeft + 2f, super.getHeight()
                    // - axisMarginBottom + longtitudeFontSize, mXTitlePaint);
                    // }
                }
            }
        }
    }

    /**
     * 获取X轴刻度�??,�?�??�最大1
     * 
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
            textColor = DEFAULT_LONGTITUDE_FONT_COLOR_DOWN;
        } else if (index == midIndex) {
            textColor = DEFAULT_LONGTITUDE_FONT_COLOR_MID;
        } else {
            textColor = DEFAULT_LONGTITUDE_FONT_COLOR_UP;

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

        Paint mPaint = new Paint();
        mPaint.setColor(borderColor);

        // 绘制边
        canvas.drawLine(1f, 1f, 1f + width, 1f, mPaint);
        canvas.drawLine(1f + width, 1f, 1f + width, 1f + height, mPaint);
        canvas.drawLine(1f + width, 1f + height, 1f, 1f + height, mPaint);
        canvas.drawLine(1f, 1f + height, 1f, 1f, mPaint);
    }

    /**
     * 绘制X轴
     * 
     * @param canvas
     */
    protected void drawXAxis(Canvas canvas) {

        float length = super.getWidth();
        float postY = super.getHeight() - axisMarginBottom - 1;

        Paint mPaint = new Paint();
        mPaint.setColor(axisXColor);

        canvas.drawLine(0f, postY, length, postY, mPaint);

    }

    /**
     * 绘制Y轴
     * 
     * @param canvas
     */
    protected void drawYAxis(Canvas canvas) {

        float length = super.getHeight() - axisMarginBottom;
        float postX = axisMarginLeft + 1;

        Paint mPaint = new Paint();
        mPaint.setColor(axisXColor);

        canvas.drawLine(postX, 0f, postX, length, mPaint);
    }

    private int xLineCounts = 5;

    /**
     * 绘制竖线
     * 
     * @param canvas
     */
    protected void drawAxisGridX(Canvas canvas) {

        // if (null != axisXTitles) {

        int counts = xLineCounts;
        float length = super.getHeight() - axisMarginBottom;
        Paint mXTitlePaint = new Paint();
        mXTitlePaint.setColor(longitudeColor);

        mXTitlePaint.setPathEffect(dashEffect);
        if (counts > 1) {
            float postOffset = (super.getWidth() - axisMarginLeft - axisMarginRight) / (counts - 1);
            float offset = axisMarginLeft;
            float offsetX = super.getHeight() - axisMarginBottom - xTitleTextHeight;
            for (int i = 0; i <= counts; i++) {
                // 绘制线条
                if (displayLongitude) {
                    canvas.drawLine(offset + i * postOffset, axisMarginTop + xTitleTextHeight / 2, offset + i
                            * postOffset, offsetX, mXTitlePaint);
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
        if (null != axisYTitles) {
            int counts = axisYTitles.size();
            float length = super.getWidth() - axisMarginLeft;
            float height = super.getHeight() - axisMarginTop - xTitleTextHeight / 2;
            // 线条Paint
            Paint mPaintLine = new Paint();
            mPaintLine.setColor(latitudeColor);
            if (dashLatitude) {
                mPaintLine.setPathEffect(dashEffect);
            }
            // �?��Paint
            Paint mPaintFont = new Paint();
            mPaintFont.setColor(latitudeFontColor);
            mPaintFont.setTextSize(latitudeFontSize);

            mPaintFont.setAntiAlias(true);

            // 绘制线条坐�?轴
            if (counts > 1) {
                float postOffset = (super.getHeight() - axisMarginBottom - axisMarginTop * 2 - xTitleTextHeight)
                        / (counts);

                // float offsetX = super.getHeight() - axisMarginBottom - xTitleTextHeight / 2;

                for (int i = 0; i <= counts; i++) {
                    // 绘制线条
                    if (displayLatitude) {
                        canvas.drawLine(axisMarginLeft, height - i * postOffset, length+axisMarginRight, height - i * postOffset,
                                mPaintLine);
                    }
                    // 绘制刻度
                    // if (displayAxisYTitle) {
                    //
                    // if (displayAxisYTitleColor) {
                    // mPaintFont.setColor(getYTitlePaintFont(i, counts));
                    // }
                    //
                    // if (i < counts && i > 0) {
                    // canvas.drawText(axisYTitles.get(i), 0f, offset - i * postOffset + latitudeFontSize / 2f,
                    // mPaintFont);
                    // } else if (0 == i) {
                    // canvas.drawText(axisYTitles.get(i), 0f, super.getHeight() - this.axisMarginBottom - 5f,
                    // mPaintFont);
                    // }
                    // }
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

    // 获得来自其他图表�??知
    // public void notifyEvent(GridChart chart) {
    // PointF point = chart.getTouchPoint();
    // // 如果没有�?中点
    // if (null != point) {
    // // 获取点击坐�?
    // clickPostX = point.x;
    // clickPostY = point.y;
    // }
    // // 设置当前控件�?��摸点
    // touchPoint = new PointF(clickPostX, clickPostY);
    // super.invalidate();
    // }

    // public void addNotify(ITouchEventResponse notify) {
    // if (null == notifyList) {
    // notifyList = new ArrayList<ITouchEventResponse>();
    // }
    // notifyList.add(notify);
    // }
    //
    // public void removeNotify(int i) {
    // if (null != notifyList && notifyList.size() > i) {
    // notifyList.remove(i);
    // }
    // }
    //
    // public void removeAllNotify() {
    // if (null != notifyList) {
    // notifyList.clear();
    // }
    // }

    // public void notifyEventAll(GridChart chart) {
    // if (null != notifyList) {
    // for (int i = 0; i < notifyList.size(); i++) {
    // ITouchEventResponse ichart = notifyList.get(i);
    // ichart.notifyEvent(chart);
    // }
    // }
    // }

    /**
     * Saves the current state of the chart to the gallery as a JPEG image. The
     * filename and compression can be set. 0 == maximum compression, 100 = low
     * compression (high quality). NOTE: Needs permission WRITE_EXTERNAL_STORAGE
     * 
     * @param fileName e.g. "my_image"
     * @param quality e.g. 50, min = 0, max = 100
     * @return returns true if saving was successfull, false if not
     */
    public boolean saveToGallery(String fileName, int quality) {

        // restrain quality
        if (quality < 0 || quality > 100)
            quality = 50;

        long currentTime = System.currentTimeMillis();

        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsolutePath() + "/DCIM");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false;
            }
        }

        String filePath = file.getAbsolutePath() + "/" + fileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

            mDrawBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out); // control
            // the jpeg
            // quality

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(8);

        values.put(Images.Media.TITLE, fileName);
        values.put(Images.Media.DISPLAY_NAME, fileName);
        values.put(Images.Media.DATE_ADDED, currentTime);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        values.put(Images.Media.DESCRIPTION, "MPAndroidChart-Library Save");
        values.put(Images.Media.ORIENTATION, 0);
        values.put(Images.Media.DATA, filePath);
        values.put(Images.Media.SIZE, size);

        return getContext().getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values) == null ? false
                : true;
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

    public void setDisplayAxisYTitleColor(boolean displayAxisXTitleColor) {
        this.displayAxisYTitleColor = displayAxisXTitle;
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

    // public PointF getTouchPoint() {
    // return touchPoint;
    // }
    //
    // public void setTouchPoint(PointF touchPoint) {
    // this.touchPoint = touchPoint;
    // }
    //
    // public boolean isTouch() {
    // return isTouch;
    // }
    //
    // public void setTouch(boolean isTouch) {
    // this.isTouch = isTouch;
    // }

    public boolean isDrawXBorke() {
        return isDrawXBorke;
    }

    public void setDrawXBorke(boolean isDrawXBorke) {
        this.isDrawXBorke = isDrawXBorke;
    }

}
