/**
 * @Title CombinationStock.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 上午9:37:22
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName CombinationStock
 * @Description 我的组合，包含持股占比的bean
 * @author zjz
 * @date 2014-8-26 上午9:37:22
 * @version 1.0
 */
public class ConStockBean extends StockBean implements Serializable {
    private static final long serialVersionUID = 129554888L;
    @SerializedName("percent")
    protected float percent;
    protected int dutyValue = -1;
    protected int dutyColor;
    protected float currentValue;
    protected float increaseValue;
    // 是否停牌 1是 0否
    @SerializedName("is_stop")
    private int isStop;
    @SerializedName("increase_percent")
    protected float increasePercent;

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param id Id
     * @param dutyValue 占比
     * @param dutyColor 占比颜色值
     * @param name 股票名称
     * @param num 股票号码
     */
    public ConStockBean(int id, float percent, int dutyColor, String name, String stockcode) {
        super();
        super.stockCode = stockcode;
        super.stockId = id;
        setPercent(percent);
        this.dutyColor = dutyColor;
        this.stockName = name;

    }

    public ConStockBean() {
    }

    public int getDutyValue() {
        if (dutyValue == -1) {
            dutyValue = (int) (percent * 100);
        }
        return dutyValue;
    }

    public void setDutyValue(int dutyValue) {
        this.dutyValue = dutyValue;
    }

    public String getName() {
        return stockName;
    }

    public void setName(String name) {
        this.stockName = name;
    }

    public int getDutyColor() {
        return dutyColor;
    }

    public void setDutyColor(int dutyColor) {
        this.dutyColor = dutyColor;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public float getIncreaseValue() {
        return increaseValue;
    }

    public void setIncreaseValue(float increaseValue) {
        this.increaseValue = increaseValue;
    }

    public boolean equals(Object obj) {
        ConStockBean param = (ConStockBean) obj;
        if (this.stockId == param.stockId) {
            return true;
        } else {
            return false;
        }
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.dutyValue = (int) (percent * 100);
        this.percent = percent;

    }

    public float getIncreasePercent() {
        return increasePercent;
    }

    public void setIncreasePercent(float increasePercent) {
        this.increasePercent = increasePercent;
    }

    public int getIsStop() {
        return isStop;
    }

    public boolean isStop() {
        return isStop == 1 ? true : false;
    }

    public void setIsStop(int isStop) {
        this.isStop = isStop;
    }

}
