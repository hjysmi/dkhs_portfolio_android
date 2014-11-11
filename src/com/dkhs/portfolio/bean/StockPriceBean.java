/**
 * @Title StockPriceBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午1:56:29
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName StockPriceBean
 * @Description 用于解析从服务器返回的股票行情相关的bean
 * @author zjz
 * @date 2014-9-18 下午1:56:29
 * @version 1.0
 */
public class StockPriceBean {

    private long id;
    @SerializedName("abbr_name")
    private String abbrname;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("current")
    private float current;
    private float change;

    // 涨跌幅
    @SerializedName("percentage")
    private float percentage;
    @SerializedName("followed")
    private boolean isFollowed;

    @SerializedName("chng_pct_day")
    private float dayPercentage;
    @SerializedName("chng_pct_month")
    private float monthPercentage;
    @SerializedName("chng_pct_three_month")
    private float seasonPercentage;

    // (0, '其他'),(1, '股票'),(2, '债券'),(3, '基金'),(4, '权证'),(5, '指数'),(6, '集合理财'),(9, '期货'),(10, '期权')
    private String symbol_type;

    // 是否停牌 1是 0否
    @SerializedName("is_stop")
    private int isStop;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAbbrname() {
        return abbrname;
    }

    public void setAbbrname(String abbrname) {
        this.abbrname = abbrname;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public float getDayPercentage() {
        return dayPercentage;
    }

    public void setDayPercentage(float dayPercentage) {
        this.dayPercentage = dayPercentage;
    }

    public float getMonthPercentage() {
        return monthPercentage;
    }

    public void setMonthPercentage(float monthPercentage) {
        this.monthPercentage = monthPercentage;
    }

    public float getSeasonPercentage() {
        return seasonPercentage;
    }

    public void setSeasonPercentage(float seasonPercentage) {
        this.seasonPercentage = seasonPercentage;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
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

    public String getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(String symbol_type) {
        this.symbol_type = symbol_type;
    }

}
