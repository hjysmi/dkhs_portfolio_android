/**
 * @Title CombinationStock.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-26 上午9:37:22
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Arrays;

/**
 * @author zjz
 * @version 1.0
 * @ClassName CombinationStock
 * @Description 我的组合，包含持股占比的bean
 * @date 2014-8-26 上午9:37:22
 */
@Parcel
public class ConStockBean extends StockBean {
    @SerializedName("percent")
    float percent;
    int dutyValue = -1;
    int dutyColor;
    float currentValue;
    float increaseValue;
    // // 涨幅(不需要再*100，5.71代表5.71%), float
    // private float increase_percent;
    // 是否停牌 1是 0否
    @SerializedName("is_stop")
    int isStop;
    @SerializedName("increase_percent")
    float increasePercent;
    // 2,='暂停交易' 3='终止上市'
    public String list_status;


    public ConStockBean(int id, float percent, int dutyColor, String name, String stockcode) {
        super();
        super.stockCode = stockcode;
        super.stockId = id;
        // setPercent(percent);
        this.dutyColor = dutyColor;
        this.stockName = name;

    }

    public ConStockBean() {
    }

    // public int getDutyValue() {
    // if (dutyValue == -1) {
    // dutyValue = (int) (percent * 100);
    // }
    // return dutyValue;
    // }

    // public void setDutyValue(int dutyValue) {
    // this.dutyValue = dutyValue;
    // }

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
        if (obj instanceof ConStockBean) {

            ConStockBean param = (ConStockBean) obj;
            return this.stockCode.equals(param.stockCode);
        }
        return false;
    }

    /**
     * @return
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{stockId, // auto-boxed
                stockCode, // auto-boxed
                stockName,});
    }

    @Override
    public ConStockBean clone() {
        ConStockBean stock = new ConStockBean();
        stock.stockCode = this.stockCode;
        stock.stockId = this.stockId;
        stock.stockSymbol = this.stockSymbol;
        stock.stockName = this.stockName;
        stock.dutyColor = this.dutyColor;
        stock.dutyValue = this.dutyValue;
        stock.percent = this.percent;
        stock.isStop = this.isStop;
        stock.increasePercent = this.increasePercent;
        stock.list_status = this.list_status;

        return stock;
    }

    // public float getPercent() {
    // return percent;
    // }

    // public void setPercent(float percent) {
    // this.dutyValue = (int) (percent * 100);
    // this.percent = percent;
    //
    // }

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
        return isStop == 1;
    }

    public void setIsStop(int isStop) {
        this.isStop = isStop;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getList_status() {
        return list_status;
    }

    public void setList_status(String list_status) {
        this.list_status = list_status;
    }

}
