package com.dkhs.portfolio.ui.widget.chart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.kline.MALineEntity;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

public class StickChart extends GridChart {

    // //////////默认值///////////////

    /** 显示纬线数 */
    public static final int DEFAULT_LATITUDE_NUM = 4;

    /** 显示纬线数 */
    public static final int DEFAULT_LONGTITUDE_NUM = 3;

    /** 柱条边�?��色 */
    public static final int DEFAULT_STICK_BORDER_COLOR = Color.RED;

    /** 柱条填�?��色 */
    public static final int DEFAULT_STICK_FILL_COLOR = Color.RED;

    // //////////属�?列表/////////////////

    /** 柱条边�?��色 */
    private int stickBorderColor = DEFAULT_STICK_BORDER_COLOR;

    /** 柱条填�?��色 */
    private int stickFillColorUp = DEFAULT_STICK_FILL_COLOR;

    private int stickFillColorDown = DEFAULT_STICK_FILL_COLOR;

    /** 显示纬线数 */
    private int latitudeNum = DEFAULT_LATITUDE_NUM;

    /** 显示经线数 */
    private int longtitudeNum = DEFAULT_LONGTITUDE_NUM;

    /** K线数据 */
    private List<StickEntity> StickData;

    /** 图表中�?��蜡烛线 */
    private int maxStickDataNum;

    /** K线显示�?��价格 */
    protected float maxValue;

    /** K线显示�?��价格 */
    protected float minValue;
    /** MA数据 */
    private List<MALineEntity> MALineData;

    // ///////////////�??函数///////////////

    public StickChart(Context context) {
        super(context);
    }

    public StickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public StickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // /////////////函数方�?////////////////

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initAxisY();
        initAxisX();
        super.onDraw(canvas);

        drawSticks(canvas);

        // 绘制十字坐�?,防止被盖住
        if (isDisplayCrossXOnTouch() || isDisplayCrossYOnTouch()) {
            drawWithFingerClick(canvas);
        }
    }

    /**
     * 获取X轴刻度位置,�?�??�最大1
     * 
     * @param value
     * @return
     */
    @Override
    public String getAxisXGraduate(Object value) {
        float graduate = Float.valueOf(super.getAxisXGraduate(value));
        int index = (int) Math.floor(graduate * maxStickDataNum);

        if (index >= maxStickDataNum) {
            index = maxStickDataNum - 1;
        } else if (index < 0) {
            index = 0;
        }

        return String.valueOf(StickData.get(index).getDate());
    }

    /**
     * 获取Y轴刻度位置,�?�??�最大1
     * 
     * @param value
     * @return
     */
    @Override
    public String getAxisYGraduate(Object value) {
        float graduate = Float.valueOf(super.getAxisYGraduate(value));
        return String.valueOf((int) Math.floor(graduate * (maxValue - minValue) + minValue));
    }

    /**
     * 获得来自其他图�?��通知
     */
    @Override
    public void notifyEvent(GridChart chart) {

        // CandleStickChart candlechart = (CandleStickChart)chart;
        //
        // this.maxStickDataNum = candlechart.getMaxCandleSticksNum();

        // 不显示Y轴信息
        super.setDisplayCrossYOnTouch(false);
        // �?���??通知
        super.notifyEvent(chart);
        // 对外�??通知
        super.notifyEventAll(this);
    }

    /**
     * 初始化X轴
     */
    protected void initAxisX() {
        List<String> TitleX = new ArrayList<String>();
        try {
            if (null != StickData) {
                float average = maxStickDataNum / longtitudeNum;
                // �?��刻度
                for (int i = 0; i < longtitudeNum; i++) {
                    int index = (int) Math.floor(i * average);
                    if (index > maxStickDataNum - 1) {
                        index = maxStickDataNum - 1;
                    }
                    // 追�??�?
                    TitleX.add(String.valueOf(StickData.get(index).getDate()));
                }
                TitleX.add(String.valueOf(StickData.get(maxStickDataNum - 1).getDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setAxisXTitles(TitleX);
    }

    public int getSelectedIndex() {
        if (null == super.getTouchPoint()) {
            return 0;
        }
        float graduate = Float.valueOf(super.getAxisXGraduate(super.getTouchPoint().x));
        int index = (int) Math.floor(graduate * maxStickDataNum);

        if (index >= maxStickDataNum) {
            index = maxStickDataNum - 1;
        } else if (index < 0) {
            index = 0;
        }

        return index;
    }

    /**
     * 多点触控事件
     */
    protected void drawWithFingerMove() {
    }

    /**
     * 初始化Y轴
     */
    protected void initAxisY() {
        List<String> TitleY = new ArrayList<String>();
        float average = (int) ((maxValue - minValue) / latitudeNum) / 100 * 100;
        ;
        // �?��刻度
        for (int i = 0; i < latitudeNum; i++) {
            String value = String.valueOf((int) Math.floor(minValue + i * average));
            if (value.length() < super.getAxisYMaxTitleLength()) {
                while (value.length() < super.getAxisYMaxTitleLength()) {
                    value = new String(" ") + value;
                }
            }
            TitleY.add(value);
        }
        // �?���?��值
        String value = String.valueOf((int) Math.floor(((int) maxValue) / 100 * 100));
        if (value.length() < super.getAxisYMaxTitleLength()) {
            while (value.length() < super.getAxisYMaxTitleLength()) {
                value = new String(" ") + value;
            }
        }
        TitleY.add(value);

        super.setAxisYTitles(TitleY);
    }

    @Override
    protected void drawMaxYValue(Paint paint, Canvas canvas) {
        super.drawMaxYValue(paint, canvas);
        // String maxValueStr = String.valueOf(maxValue);
        // canvas.drawText(maxValueStr, 0f,
        // paint.measureText(maxValueStr.substring(0,1)), paint);
    }

    /**
     * 绘制柱状线
     * 
     * @param canvas
     */
    protected void drawSticks(Canvas canvas) {
        // 初始化颜色 linbing
        try {
            stickFillColorUp = Color.RED;
            stickFillColorDown = getResources().getColor(R.color.dark_green);

            // 蜡烛棒宽度
            float stickWidth = ((super.getWidth() /*- super.getAxisMarginLeft()*/- super.getAxisMarginRight()) / maxStickDataNum) - 3;
            // 蜡烛棒起始绘制位置
            float stickX = /* super.getAxisMarginLeft() + */3;

            Paint mPaintStick = new Paint();
            drawMA(canvas);
            if (null != StickData) {

                // 判断显示为方柱或显示为线条
                for (int i = 0; i < StickData.size(); i++) {
                    StickEntity ohlc = StickData.get(i);

                    if (ohlc.isUp()) {
                        mPaintStick.setColor(stickFillColorUp);
                    } else {
                        mPaintStick.setColor(stickFillColorDown);
                    }
                    float highY = (float) ((1f - (ohlc.getHigh() - minValue) / (maxValue - minValue))
                            * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                            .getAxisMarginTop());
                    float lowY = (float) ((1f - (ohlc.getLow() - minValue) / (maxValue - minValue))
                            * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                            .getAxisMarginTop());

                    // 绘制数据?��?据宽度判断绘制直线或方柱
                    if (stickWidth >= 2f) {
                        canvas.drawRect(stickX, highY + mTitleHeight, stickX + stickWidth, lowY + mTitleHeight,
                                mPaintStick);
                    } else {
                        canvas.drawLine(stickX, highY + mTitleHeight, stickX, lowY + mTitleHeight, mPaintStick);
                    }

                    // X位移
                    stickX = stickX + 3 + stickWidth;
                }
            }
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void drawMA(Canvas canvas) {
        try {
            String text = "";
            float wid = titalWid * 2 + 10;
            float stickWidth = ((super.getWidth() /*- super.getAxisMarginLeft()*/- super.getAxisMarginRight()) / maxStickDataNum) - 3;
            for (int j = 0; j < MALineData.size(); j++) {
                MALineEntity lineEntity = MALineData.get(j);

                float startX = -stickWidth / 2 - 3;
                float startY = 0;
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(lineEntity.getLineColor());
                paint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));

                float total = Float.parseFloat(new DecimalFormat("#.##").format(lineEntity.getLineData().get(index))) / 100;
                if (total < 10000) {
                    text = new DecimalFormat("#.##").format(total);
                } else if (total > 10000 && total < 10000000) {
                    total = total / 10000;
                    text = new DecimalFormat("#.##").format(total) + "万";
                } else {
                    total = total / 10000000;
                    text = new DecimalFormat("#.##").format(total) + "千万";
                }
                text = lineEntity.getTitle() + ":" + text;
                Paint p = new Paint();
                p.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
                p.setColor(lineEntity.getLineColor());
                Rect rect = new Rect();
                p.getTextBounds(text, 0, text.length(), rect);
                if (j == 0) {
                    wid = wid + 2;
                } else {
                    wid = rect.width() / 2 + wid - 30;
                }
                canvas.drawText(text, wid, getResources().getDimensionPixelOffset(R.dimen.title_text_font), paint);
                wid = wid + 2 + rect.width();
                for (int i = 0; i < lineEntity.getLineData().size(); i++) {
                    if (i != 0) {
                        canvas.drawLine(
                                startX,
                                startY,
                                startX + 3 + stickWidth,
                                (float) ((1f - (lineEntity.getLineData().get(i) - minValue) / (maxValue - minValue))
                                        * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                            .getAxisMarginTop()) + mTitleHeight, paint);
                    }
                    startX = startX + 3 + stickWidth;
                    startY = (float) ((1f - (lineEntity.getLineData().get(i) - minValue) / (maxValue - minValue))
                            * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                .getAxisMarginTop()) + mTitleHeight;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Push数据绘制K线图
    public void pushData(StickEntity entity) {
        if (null != entity) {
            // 追�?��据到数据列表
            addData(entity);
            // 强制重�?
            super.postInvalidate();
        }
    }

    // Push数据绘制K线图
    public void addData(StickEntity entity) {
        if (null != entity) {
            // 追�?��据
            if (null == StickData || 0 == StickData.size()) {
                StickData = new ArrayList<StickEntity>();
                this.maxValue = ((int) entity.getHigh()) / 100 * 100;
            }

            this.StickData.add(entity);

            if (this.maxValue < entity.getHigh()) {
                this.maxValue = 100 + ((int) entity.getHigh()) / 100 * 100;
            }

            if (StickData.size() > maxStickDataNum) {
                maxStickDataNum = maxStickDataNum + 1;
            } else {
                maxStickDataNum = this.StickData.size();
            }
        }
    }

    private void initMALineData() {
        MALineEntity MA5 = new MALineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(Color.GRAY);
        MA5.setLineData(initMA(StickData, 5));

        MALineEntity MA10 = new MALineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(Color.YELLOW);
        MA10.setLineData(initMA(StickData, 10));

        MALineEntity MA20 = new MALineEntity();
        MA20.setTitle("MA20");
        MA20.setLineColor(Color.rgb(139, 0, 225));
        MA20.setLineData(initMA(StickData, 20));

        MALineData = new ArrayList<MALineEntity>();
        MALineData.add(MA5);
        MALineData.add(MA10);
        MALineData.add(MA20);

    }

    /**
     * 初始化MA值，从数组的最后一个数据开始初始化
     * 
     * @param entityList
     * @param days
     * @return
     */
    private List<Float> initMA(List<StickEntity> entityList, int days) {
        if (days < 2 || entityList == null || entityList.size() <= 0) {
            return null;
        }
        List<Float> MAValues = new ArrayList<Float>();

        float sum = 0;
        float avg = 0;
        for (int i = entityList.size() - 1; i >= 0; i--) {
            float close = (float) entityList.get(i).getHigh();
            if (i > entityList.size() - days) {
                sum = sum + close;
                avg = sum / (entityList.size() - i);
            } else {
                sum = close + avg * (days - 1);
                avg = sum / days;
            }
            MAValues.add(avg);
        }

        List<Float> result = new ArrayList<Float>();
        for (int j = MAValues.size() - 1; j >= 0; j--) {
            result.add(MAValues.get(j));
        }
        return result;
    }

    // ////////////属�?GetterSetter/////////////////

    public List<StickEntity> getStickData() {
        return StickData;
    }

    public void setStickData(List<StickEntity> stickData) {
        // �?��已有数据
        if (null != StickData) {
            StickData.clear();
        }
        for (StickEntity e : stickData) {
            addData(e);
        }
        initMALineData();
    }

    public int getLatitudeNum() {
        return latitudeNum;
    }

    public void setLatitudeNum(int latitudeNum) {
        this.latitudeNum = latitudeNum;
    }

    public int getMaxStickDataNum() {
        return maxStickDataNum;
    }

    public void setMaxStickDataNum(int maxStickDataNum) {
        this.maxStickDataNum = maxStickDataNum;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public int getStickBorderColor() {
        return stickBorderColor;
    }

    public void setStickBorderColor(int stickBorderColor) {
        this.stickBorderColor = stickBorderColor;
    }

    public int getLongtitudeNum() {
        return longtitudeNum;
    }

    public void setLongtitudeNum(int longtitudeNum) {
        this.longtitudeNum = longtitudeNum;
    }
}
