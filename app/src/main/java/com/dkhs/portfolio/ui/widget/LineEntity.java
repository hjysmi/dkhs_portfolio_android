package com.dkhs.portfolio.ui.widget;

import java.util.ArrayList;
import java.util.List;

public class LineEntity<T extends LinePointEntity> {

    /** 线表示数据 */
    private List<T> lineData;

    /** 线条名称 */
    private String title;
    /** 线条值 */
    private String value;

    /** 线表示颜色 */
    private int lineColor;

    /** 是否显示线 */
    private boolean display = true;
    
    private float maxOffsetValue;

    private int maxVolNum;
    private int minVolNum;

    public List<T> getLineData() {
        return lineData;
    }

    public void setLineData(List<T> lineData) {
        this.lineData = lineData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public LineEntity() {
        super();
    }

    public LineEntity(List<T> lineData, String title, int lineColor) {
        super();
        this.lineData = lineData;
        this.title = title;
        this.lineColor = lineColor;
    }

    public void put(T value) {
        if (null == lineData) {
            lineData = new ArrayList<T>();
        }
        lineData.add(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMaxVolNum() {
        return maxVolNum;
    }

    public void setMaxVolNum(int maxVolNum) {
        this.maxVolNum = maxVolNum;
    }

    public int getMinVolNum() {
        return minVolNum;
    }

    public void setMinVolNum(int minVolNum) {
        this.minVolNum = minVolNum;
    }

    public float getMaxOffsetValue() {
        return maxOffsetValue;
    }

    public void setMaxOffsetValue(float maxOffsetValue) {
        this.maxOffsetValue = maxOffsetValue;
    }

}
