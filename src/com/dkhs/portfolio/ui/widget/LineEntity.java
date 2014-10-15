package com.dkhs.portfolio.ui.widget;

import java.util.ArrayList;
import java.util.List;

public class LineEntity {

    /** 线表示数据 */
    private List<LinePointEntity> lineData;

    /** 线条名称 */
    private String title;
    /** 线条值 */
    private String value;

    /** 线表示颜色 */
    private int lineColor;

    /** 是否显示线 */
    private boolean display = true;

    public List<LinePointEntity> getLineData() {
        return lineData;
    }

    public void setLineData(List<LinePointEntity> lineData) {
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

    public LineEntity(List<LinePointEntity> lineData, String title, int lineColor) {
        super();
        this.lineData = lineData;
        this.title = title;
        this.lineColor = lineColor;
    }

    public void put(LinePointEntity value) {
        if (null == lineData) {
            lineData = new ArrayList<LinePointEntity>();
        }
        lineData.add(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
