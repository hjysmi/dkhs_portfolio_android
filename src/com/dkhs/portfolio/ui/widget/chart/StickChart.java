package com.dkhs.portfolio.ui.widget.chart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.widget.kline.MALineEntity;
import com.dkhs.portfolio.ui.widget.kline.OHLCEntity;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

public class StickChart extends GridChart {

    // //////////默认值///////////////

    /** 显示纬线数 */
    public static final int DEFAULT_LATITUDE_NUM = 4;

    /** 显示纬线数 */
    public static final int DEFAULT_LONGTITUDE_NUM = 4;

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
    private ArrayList<OHLCEntity> StickData;

    /** 图表中�?��蜡烛线 */
    private int maxStickDataNum;

    /** K线显示�?��价格 */
    protected float maxValue;

    /** K线显示�?��价格 */
    protected float minValue;
    /** K线显示MACD负值 */
    protected float loseValue;
    /** MA数据 */
    private List<MALineEntity> MALineData;
    private int currentIndex;
    // ///////////////�??函数///////////////
    private int mShowDate;
    private double dragValue = 0;
    private boolean isTouch = false;
    public int index;

    public StickChart(Context context) {
        super(context);
    }

    public int getmShowDate() {
        return mShowDate;
    }

    public void setmShowDate(int mShowDate) {
        this.mShowDate = mShowDate;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
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
        // currentIndex = index;
        if (index == 0 && !isTouch) {
            // index = currentIndex;
        }
        switch (checkType) {
            case CHECK_COLUME:
                setMaxValue();
                try {
                    initAxisY();
                    initAxisX();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                super.onDraw(canvas);
                drawSticks(canvas);
                break;
            case CHECK_MACD:
                setMACDMaxValue();
                try {
                    initMACDY();
                    initAxisX();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                super.onDraw(canvas);
                drawMADC(canvas);
                drawDiff(canvas);
                break;
            default:
                break;
        }
        // 绘制十字坐�?,防止被盖住
        if (isDisplayCrossXOnTouch() || isDisplayCrossYOnTouch()) {
            drawWithFingerClick(canvas);
        }
    }

    public void setMaxValue() {
        if (null != StickData) {
            try {
                maxValue = 0;
                for (int i = mShowDate + index - 1; i < StickData.size() && i >= index; i--) {
                    if (i >= 0 && i < StickData.size() && null != StickData.get(i)
                            && getMaxData(StickData.get(i)) > maxValue) {
                        maxValue = getMaxData(StickData.get(i));
                    }
                }
                if (mShowDate >= StickData.size()) {
                    for (int i = StickData.size() - 1; i < StickData.size() && i >= index; i--) {
                        if (i >= 0 && i < StickData.size() && null != StickData.get(i)
                                && getMaxData(StickData.get(i)) > maxValue) {
                            maxValue = getMaxData(StickData.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public float getMaxData(OHLCEntity entity) {
        double tmp = entity.getVolume();
        if (entity.getVol5() > tmp) {
            tmp = entity.getVol5();
        }
        if (entity.getVol10() > tmp) {
            tmp = entity.getVol10();
        }
        if (entity.getVol20() > tmp) {
            tmp = entity.getVol20();
        }
        return (float) tmp;
    }

    public void setMACDMaxValue() {
        try {
            if (null != StickData) {
                maxValue = 0;
                int k = mShowDate + index - 1;
                if (mShowDate > StickData.size()) {
                    k = StickData.size() - 1;
                }
                StickData.trimToSize();
                for (int i = k; i < StickData.size() && i >= index && i >= 0; i--) {
                    if (i >= 0 && Math.abs(StickData.get(i).getMacd()) > maxValue) {
                        maxValue = (float) Math.abs(StickData.get(i).getMacd());
                    }
                    if (i >= 0 && Math.abs(StickData.get(i).getDiff()) > maxValue) {
                        maxValue = (float) Math.abs(StickData.get(i).getDiff());
                    }
                    if (i >= 0 && Math.abs(StickData.get(i).getDea()) > maxValue) {
                        maxValue = (float) Math.abs(StickData.get(i).getDea());
                    }
                }
                minValue = -maxValue;
                // loseValue = -maxValue;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            if (null != StickData && StickData.size() > 0) {

                if (longtitudeNum == 4) {

                    // 竖屏缩放，日期无更改
                    float average = mShowDate / (longtitudeNum + 1);

                    for (int i = longtitudeNum + 1; i >= 0; i--) {
                        int indexs = (int) Math.floor(i * average);
                        if (indexs > maxStickDataNum - 1) {
                            indexs = maxStickDataNum - 1;

                        }
                        if (indexs >= StickData.size()) {
                            TitleX.add("");
                        } else {

                            int k = indexs - 1 + index;
                            if (longtitudeNum + 1 == i) {
                                k += 1;
                            }
                            if (k < 0) {
                                k = 0;
                            }
                            if (k >= StickData.size()) {
                                k = StickData.size() - 1;
                            }
                            TitleX.add(String.valueOf(StickData.get(k).getDate()));

                        }
                    }

                } else {

                    int firstDayIndex = mShowDate - 1;
                    firstDayIndex = Math.min(firstDayIndex, StickData.size() - 1);
                    if (firstDayIndex >= 0 && firstDayIndex < StickData.size()) {

                        TitleX.add(String.valueOf(StickData.get(firstDayIndex).getDate()));
                    }
                    if (index >= 0 && index < StickData.size() && firstDayIndex >= (mShowDate - 1)) {

                        TitleX.add(String.valueOf(StickData.get(index).getDate()));
                    }
                }

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
        float average = ((maxValue - minValue) / latitudeNum);
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
        String value = maxValue + "";
        if (value.length() < super.getAxisYMaxTitleLength()) {
            while (value.length() < super.getAxisYMaxTitleLength()) {
                value = new String(" ") + value;
            }
        }
        TitleY.add(value);

        super.setAxisYTitles(TitleY);
    }

    protected void initMACDY() {
        List<String> TitleY = new ArrayList<String>();
        String value = maxValue + "";
        TitleY.add(value);
        value = "0";
        TitleY.add(value);
        value = minValue + "";
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
            if (maxStickDataNum < 50) {
                maxStickDataNum = 50;
            }
            // 蜡烛棒宽度
            float stickWidth = 0;
            if (mShowDate > 0) {
                stickWidth = ((super.getWidth() - PADDING_LEFT - 3 - super.getAxisMarginRight()) / mShowDate) - 3;
            }

            // 蜡烛棒起始绘制位置

            Paint mPaintStick = new Paint();

            if (null != StickData) {
                float stickX = /* super.getAxisMarginLeft() + */+4 + PADDING_LEFT;
                /*
                 * if(maxStickDataNum > StickData.size()){
                 * stickX = (maxStickDataNum - StickData.size()) * (stickWidth + 3);
                 * }
                 */
                // 判断显示为方柱或显示为线条
                int num = mShowDate + index - 1;
                if (StickData.size() < maxStickDataNum) {
                    mShowDate = maxStickDataNum;
                    stickWidth = ((super.getWidth() - PADDING_LEFT - 3 - super.getAxisMarginRight()) / mShowDate) - 3;
                    num = StickData.size() - 1;
                }
                for (int i = num; i < StickData.size() && i >= index && i >= 0; i--) {
                    if (i >= 0) {
                        OHLCEntity ohlc = StickData.get(i);
                        if (null == ohlc) {
                            break;
                        }
                        if (ohlc.isup()) {
                            mPaintStick.setColor(stickFillColorUp);
                        } else {
                            mPaintStick.setColor(stickFillColorDown);
                        }
                        float highY = (float) ((1f - (ohlc.getVolume() - minValue) / (maxValue - minValue))
                                * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                .getAxisMarginTop());
                        float lowY = (float) ((1f) * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                .getAxisMarginTop());

                        // 绘制数据?��?据宽度判断绘制直线或方柱
                        if (stickWidth >= 2f) {
                            canvas.drawRect((float) (stickX + dragValue), highY + mTitleHeight, (float) (stickX
                                    + stickWidth + dragValue), lowY + mTitleHeight, mPaintStick);
                        } else {
                            canvas.drawLine((float) (stickX + dragValue), highY + mTitleHeight,
                                    (float) (stickX + dragValue), lowY + mTitleHeight, mPaintStick);
                        }

                        // X位移
                        stickX = stickX + 3 + stickWidth;
                    }
                }
                drawMA(canvas);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 绘制MACD线
     * 
     * @param canvas
     */
    protected void drawMADC(Canvas canvas) {
        // 初始化颜色 linbing
        try {
            stickFillColorUp = Color.RED;
            stickFillColorDown = getResources().getColor(R.color.dark_green);
            if (maxStickDataNum < 50) {
                maxStickDataNum = 50;
            }
            // 蜡烛棒宽度
            float stickWidth = 0;
            if (mShowDate > 0) {
                stickWidth = ((super.getWidth() - PADDING_LEFT - 3 - super.getAxisMarginRight()) / mShowDate) - 3;
            }
            Paint mPaintStick = new Paint();

            if (null != StickData) {
                float stickX = 3 + PADDING_LEFT;
                // 判断显示为方柱或显示为线条
                int num = mShowDate + index - 1;
                float highY = 0;
                float lowY = 0;
                float stickY = 0;
                float diff = 0;
                float stickDea = 0;
                float dea;
                if (StickData.size() < maxStickDataNum) {
                    mShowDate = maxStickDataNum;
                    stickWidth = ((super.getWidth() - PADDING_LEFT - 3 - super.getAxisMarginRight()) / mShowDate) - 3;
                    num = StickData.size() - 1;
                }
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
                paint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.line_kline));
                for (int i = num; i < StickData.size() && i >= index && i >= 0; i--) {
                    if (i >= 0) {
                        OHLCEntity ohlc = StickData.get(i);

                        if (ohlc.getMacd() >= 0) {
                            mPaintStick.setColor(stickFillColorUp);
                        } else {
                            mPaintStick.setColor(stickFillColorDown);
                        }
                        if (ohlc.getMacd() < 0) {
                            highY = (float) ((0.5f) * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                    .getAxisMarginTop());
                            lowY = (float) ((0.5f + (Math.abs(ohlc.getMacd())) / (maxValue * 2))
                                    * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                    .getAxisMarginTop());
                        } else {
                            highY = (float) ((1f - (ohlc.getMacd() - minValue) / (maxValue * 2))
                                    * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                    .getAxisMarginTop());

                            lowY = (float) ((0.5f) * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                    .getAxisMarginTop());
                        }
                        // 绘制蜡烛
                        if (stickWidth >= 2f) {
                            if (lowY - highY < 2) {
                                if (ohlc.getMacd() < 0) {
                                    lowY = lowY + 2;
                                } else {
                                    highY = highY - 2;
                                }
                            }
                            canvas.drawRect((float) (stickX + dragValue), highY + mTitleHeight, (float) (stickX
                                    + stickWidth + dragValue), lowY + mTitleHeight, mPaintStick);
                        } else {
                            canvas.drawLine((float) (stickX + dragValue), highY + mTitleHeight,
                                    (float) (stickX + dragValue), lowY + mTitleHeight, mPaintStick);
                        }
                        if (ohlc.getDea() < 0) {
                            dea = (float) (0.5f - (ohlc.getDea()) / (maxValue - minValue))
                                    * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight)
                                    - super.getAxisMarginTop() + mTitleHeight;
                        } else {
                            dea = (float) (1f - (ohlc.getDea() - minValue) / (maxValue - minValue))
                                    * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight)
                                    - super.getAxisMarginTop() + mTitleHeight;
                        }
                        if (ohlc.getDiff() < 0) {
                            diff = (float) (0.5f - (ohlc.getDiff()) / (maxValue - minValue))
                                    * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight)
                                    - super.getAxisMarginTop() + mTitleHeight;
                        } else {
                            diff = (float) (1f - (ohlc.getDiff() - minValue) / (maxValue - minValue))
                                    * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight)
                                    - super.getAxisMarginTop() + mTitleHeight;
                        }
                        if (i != num) {
                            paint.setColor(getResources().getColor(R.color.ma5_color));
                            canvas.drawLine((float) (dragValue + stickX - 3 - stickWidth / 2), stickY, (float) (stickX
                                    + stickWidth / 2 + dragValue), diff, paint);
                            paint.setColor(getResources().getColor(R.color.ma10_color));
                            canvas.drawLine((float) (dragValue + stickX - 3 - stickWidth / 2), stickDea,
                                    (float) (stickX + stickWidth / 2 + dragValue), dea, paint);
                        }
                        stickDea = dea;
                        stickY = diff;

                        // X位移
                        stickX = stickX + 3 + stickWidth;
                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void drawDiff(Canvas canvas) {
        int wid = 0;
        if (null == StickData || StickData.size() == 0) {
            return;
        }
        if (ismove) {
            float stickWidth = ((super.getWidth() - PADDING_LEFT - 3 - super.getAxisMarginRight()) / mShowDate) - 3;
            int selectIndext = StickData.size()
                    - (int) ((getWidth() - 2.0f - clickPostX - PADDING_LEFT) / (stickWidth + 3) + index) - 1;
            if (StickData.size() < mShowDate) {
                selectIndext = mShowDate - selectIndext;
                if (selectIndext < 0) {
                    selectIndext = 0;
                }
            }
            if ((StickData.size() - selectIndext - 1) >= 0 && (StickData.size() - selectIndext - 1) < StickData.size()) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(getResources().getColor(R.color.ma5_color));
                paint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
                Paint p = new Paint();
                p.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
                Rect rect = new Rect();
                // 此值为固定写死的静态文本
                String titile = "MACD(12.26.9)";
                p.getTextBounds(titile, 0, titile.length(), rect);
                wid = rect.width() + 32;
                canvas.drawText(titile, PADDING_LEFT, getResources().getDimensionPixelSize(R.dimen.title_text_font),
                        paint);
                String k = "DIFF:"
                        + StringFromatUtils.get4Point((float) StickData.get(StickData.size() - selectIndext - 1)
                                .getDiff());
                p.getTextBounds(k, 0, k.length(), rect);
                canvas.drawText(k, PADDING_LEFT + wid, getResources().getDimensionPixelSize(R.dimen.title_text_font),
                        paint);
                wid = rect.width() + 32 + wid;
                String dea = "DEA:"
                        + StringFromatUtils.get4Point((float) StickData.get(StickData.size() - selectIndext - 1)
                                .getDea());
                p.getTextBounds(dea, 0, dea.length(), rect);
                paint.setColor(getResources().getColor(R.color.ma10_color));
                canvas.drawText(dea, PADDING_LEFT + wid, getResources().getDimensionPixelSize(R.dimen.title_text_font),
                        paint);
                wid = wid + rect.width() + 32;
                String macd = "MACD:"
                        + StringFromatUtils.get4Point((float) StickData.get(StickData.size() - selectIndext - 1)
                                .getMacd());
                p.getTextBounds(macd, 0, macd.length(), rect);
                paint.setColor(getResources().getColor(R.color.ma20_color));
                canvas.drawText(macd, PADDING_LEFT + wid,
                        getResources().getDimensionPixelSize(R.dimen.title_text_font), paint);
            }
        } else {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(getResources().getColor(R.color.ma5_color));
            paint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
            Paint p = new Paint();
            p.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
            Rect rect = new Rect();
            int num = index;
            if (num >= StickData.size()) {
                num = StickData.size() - 1;
            }
            String titile = "MACD(12.26.9)";
            p.getTextBounds(titile, 0, titile.length(), rect);
            wid = rect.width() + 32;
            canvas.drawText(titile, PADDING_LEFT, getResources().getDimensionPixelSize(R.dimen.title_text_font), paint);
            String k = "DIFF:" + StringFromatUtils.get4Point((float) StickData.get(num).getDiff());
            p.getTextBounds(k, 0, k.length(), rect);
            canvas.drawText(k, PADDING_LEFT + wid, getResources().getDimensionPixelSize(R.dimen.title_text_font), paint);
            wid = rect.width() + 32 + wid;
            String dea = "DEA:" + StringFromatUtils.get4Point((float) StickData.get(num).getDea());
            p.getTextBounds(dea, 0, dea.length(), rect);
            paint.setColor(getResources().getColor(R.color.ma10_color));
            canvas.drawText(dea, PADDING_LEFT + wid, getResources().getDimensionPixelSize(R.dimen.title_text_font),
                    paint);
            wid = wid + rect.width() + 32;
            String macd = "MACD:" + StringFromatUtils.get4Point((float) StickData.get(num).getMacd());
            p.getTextBounds(macd, 0, macd.length(), rect);
            paint.setColor(getResources().getColor(R.color.ma20_color));
            canvas.drawText(macd, PADDING_LEFT + wid, getResources().getDimensionPixelSize(R.dimen.title_text_font),
                    paint);
        }
    }

    public void drawMA(Canvas canvas) {
        try {
            if (null == StickData || StickData.size() == 0) {
                return;
            }
            String text = "";
            float wid = 0;
            float stickWidth = ((super.getWidth() - PADDING_LEFT - super.getAxisMarginRight() - 3) / mShowDate) - 3;
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            paint.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
            int k;
            if (ismove) {
                k = currentIndex;
            } else {
                k = index;
            }
            text = "MA5:" + UIUtils.getValue(StickData.get(k).getVol5());
            Paint p = new Paint();
            p.setTextSize(getResources().getDimensionPixelOffset(R.dimen.title_text_font));
            Rect rect = new Rect();
            p.getTextBounds(text, 0, text.length(), rect);
            wid = 2;
            paint.setColor(getResources().getColor(R.color.ma5_color));
            canvas.drawText(text, wid + PADDING_LEFT, getResources().getDimensionPixelOffset(R.dimen.title_text_font),
                    paint);
            wid = wid + 32 + rect.width();
            text = "MA10:" + UIUtils.getValue(StickData.get(k).getVol10());
            p.getTextBounds(text, 0, text.length(), rect);
            paint.setColor(getResources().getColor(R.color.ma10_color));
            canvas.drawText(text, wid + PADDING_LEFT, getResources().getDimensionPixelOffset(R.dimen.title_text_font),
                    paint);
            wid = wid + 32 + rect.width();
            text = "MA20:" + UIUtils.getValue(StickData.get(k).getVol20());
            paint.setColor(getResources().getColor(R.color.ma20_color));
            canvas.drawText(text, wid + PADDING_LEFT, getResources().getDimensionPixelOffset(R.dimen.title_text_font),
                    paint);
            float startX = -stickWidth / 2 + 2 + PADDING_LEFT;
            float startY5 = 0;
            float startY10 = 0;
            float startY20 = 0;
            int nums = mShowDate + index - 1;
            if (StickData.size() < mShowDate) {
                nums = StickData.size() - 1 + index;
            }
            boolean draw5 = false;
            boolean draw10 = false;
            boolean draw20 = false;

            for (int j = nums; j < StickData.size() && j >= 0 && j >= index; j--) {
                // MALineEntity lineEntity = MALineData.get(j);

                // int addWid = 0;
                // startX = addWid + PADDING_LEFT;
                /*
                 * if(j == 0){
                 * addWid = (int) (4 * (stickWidth + 3));
                 * }else if( j == 1){
                 * addWid = (int) (9 * (stickWidth + 3));
                 * }else{
                 * addWid = (int) (19 * (stickWidth + 3));
                 * }
                 */
                // startX = startX + addWid + stickWidth / 2;
                /*
                 * int s = lineEntity.getLineData().size()- index;
                 * if(lineEntity.getLineData().size() < mShowDate){
                 * s =0;
                 * startX = addWid + PADDING_LEFT;
                 * }else{
                 * s = lineEntity.getLineData().size() - mShowDate - index;
                 * }
                 */
                paint.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.line_kline));
                if (j != nums) {
                    if (StickData.get(j).getVol5() > 0 && draw5) {
                        paint.setColor(getResources().getColor(R.color.ma5_color));
                        canvas.drawLine(
                                (float) (startX + dragValue),
                                startY5,
                                (float) (startX + 3 + stickWidth + dragValue),
                                (float) ((1f - (StickData.get(j).getVol5() - minValue) / (maxValue - minValue))
                                        * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                            .getAxisMarginTop()) + mTitleHeight, paint);
                    }
                    if (StickData.get(j).getVol10() > 0 && draw10) {
                        paint.setColor(getResources().getColor(R.color.ma10_color));
                        canvas.drawLine(
                                (float) (startX + dragValue),
                                startY10,
                                (float) (startX + 3 + stickWidth + dragValue),
                                (float) ((1f - (StickData.get(j).getVol10() - minValue) / (maxValue - minValue))
                                        * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                            .getAxisMarginTop()) + mTitleHeight, paint);
                    }
                    if (StickData.get(j).getVol20() > 0 && draw20) {
                        paint.setColor(getResources().getColor(R.color.ma20_color));
                        canvas.drawLine(
                                (float) (startX + dragValue),
                                startY20,
                                (float) (startX + 3 + stickWidth + dragValue),
                                (float) ((1f - (StickData.get(j).getVol20() - minValue) / (maxValue - minValue))
                                        * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                            .getAxisMarginTop()) + mTitleHeight, paint);
                    }
                }
                startX = startX + 3 + stickWidth;
                if (j >= 0) {
                    if (StickData.get(j).getVol5() > 0) {
                        startY5 = (float) ((1f - (StickData.get(j).getVol5() - minValue) / (maxValue - minValue))
                                * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                    .getAxisMarginTop()) + mTitleHeight;
                        draw5 = true;
                    }
                    if (StickData.get(j).getVol10() > 0) {
                        startY10 = (float) ((1f - (StickData.get(j).getVol10() - minValue) / (maxValue - minValue))
                                * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                    .getAxisMarginTop()) + mTitleHeight;
                        draw10 = true;
                    }
                    if (StickData.get(j).getVol20() > 0) {
                        startY20 = (float) ((1f - (StickData.get(j).getVol20() - minValue) / (maxValue - minValue))
                                * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                                    .getAxisMarginTop()) + mTitleHeight;
                        draw20 = true;
                    }
                }
                /*
                 * for (int i = s; i < lineEntity.getLineData().size() && i < s+mShowDate; i++) {
                 * if (i != s && i >0) {
                 * canvas.drawLine(
                 * (float)(startX + dragValue),
                 * startY,
                 * (float)(startX + 3 + stickWidth + dragValue),
                 * (float) ((1f - (lineEntity.getLineData().get(i) - minValue) / (maxValue - minValue))
                 * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                 * .getAxisMarginTop()) + mTitleHeight, paint);
                 * }
                 * startX = startX + 3 + stickWidth;
                 * if(i>=0)
                 * startY = (float) ((1f - (lineEntity.getLineData().get(i) - minValue) / (maxValue - minValue))
                 * (super.getHeight() - super.getAxisMarginBottom() - mTitleHeight) - super
                 * .getAxisMarginTop()) + mTitleHeight;
                 * }
                 */
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Push数据绘制K线图
    /*
     * public void pushData(OHLCEntity entity) {
     * if (null != entity) {
     * // 追�?��据到数据列表
     * addData(entity);
     * // 强制重�?
     * super.postInvalidate();
     * }
     * }
     */
    public void flushFirstData(OHLCEntity mOHLCEntity) {
        if (null != StickData && StickData.size() > 0) {
            StickData.add(0, mOHLCEntity);
            StickData.remove(1);
            postInvalidate();
        }
    }

    // Push数据绘制K线图
    public void addData(ArrayList<OHLCEntity> list, int page) {
        OHLCEntity entity;
        this.maxValue = 0;
        // if(page == 1){
        StickData = new ArrayList<OHLCEntity>();
        StickData = list;
        // }else{
        /*
         * for(int i = 0; i < list.size(); i++){
         * entity = list.get(i);
         * if (null != entity) {
         * // 追�?��据
         * if (i == (list.size() - mShowDate)) {
         * this.maxValue = ((int) entity.getVolume()) / 100 * 100;
         * }
         * 
         * this.StickData.add(entity);
         * if( i > (list.size() - mShowDate)){
         * if (this.maxValue < entity.getVolume()) {
         * this.maxValue = (float) entity.getVolume();
         * }
         * }
         * if (StickData.size() > maxStickDataNum) {
         * maxStickDataNum = maxStickDataNum + 1;
         * } else {
         * maxStickDataNum = this.StickData.size();
         * }
         * }
         * }
         */
        // StickData.addAll(list);
        // }
    }

    // private void initMALineData() {
    // MALineEntity MA5 = new MALineEntity();
    // MA5.setTitle("MA5");
    // MA5.setLineColor(getResources().getColor(R.color.ma5_color));
    // MA5.setLineData(initMA(StickData, 5));
    //
    // MALineEntity MA10 = new MALineEntity();
    // MA10.setTitle("MA10");
    // MA10.setLineColor(getResources().getColor(R.color.ma10_color));
    // MA10.setLineData(initMA(StickData, 10));
    //
    // MALineEntity MA20 = new MALineEntity();
    // MA20.setTitle("MA20");
    // MA20.setLineColor(getResources().getColor(R.color.ma20_color));
    // MA20.setLineData(initMA(StickData, 20));
    //
    // MALineData = new ArrayList<MALineEntity>();
    // MALineData.add(MA5);
    // MALineData.add(MA10);
    // MALineData.add(MA20);
    //
    // }

    // /**
    // * 初始化MA值，从数组的最后一个数据开始初始化
    // *
    // * @param entityList
    // * @param days
    // * @return
    // */
    // private List<Float> initMA(List<OHLCEntity> entityList, int days) {
    // List<Float> result = null;
    // try {
    // if (days < 2 || entityList == null || entityList.size() <= 0) {
    // return null;
    // }
    // List<Float> MAValues = new ArrayList<Float>();
    //
    // float sum = 0;
    // float avg = 0;
    // for (int i = entityList.size() - 1; i >= 0; i--) {
    // sum = 0;
    // avg = 0;
    // if (i - days >= -1) {
    // for(int k = 0; k < days && i-k < entityList.size() -1 && i-k >= 0; k++){
    // if(null == entityList.get(i-k)){
    // break;
    // }
    // sum = (float) (sum + entityList.get(i-k).getHigh());
    // }
    // avg = sum / days;
    // } else{
    // break;
    // }
    // MAValues.add(avg);
    // }
    //
    // result = new ArrayList<Float>();
    // for (int j = MAValues.size() - 1; j >= 0; j--) {
    // result.add(MAValues.get(j));
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return result;
    // }

    // ////////////属�?GetterSetter/////////////////

    public List<OHLCEntity> getStickData() {
        return StickData;
    }

    public void setStickData(ArrayList<OHLCEntity> stickData, int page) {
        // �?��已有数据
        if (null != StickData) {
            // if(!(page > 1)){
            StickData.clear();
            // }
        }
        // for (OHLCEntity e : stickData) {
        addData(stickData, page);
        // }
        // initMALineData();
    }

    public void setStickData(ArrayList<OHLCEntity> stickData) {
        // �?��已有数据
        if (null != StickData) {
            StickData.clear();
        }
        // for (OHLCEntity e : stickData) {
        addData(stickData, 1);
        // }
        // initMALineData();
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

    public double getDragValue() {
        return dragValue;
    }

    public void setDragValue(double dragValue) {
        this.dragValue = dragValue;
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
