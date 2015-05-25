package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by zjz on 2015/5/22.
 */
@Parcel
public class PositionAdjustBean extends StockBean {

    @SerializedName("from_percent")
    float fromPercent;
    @SerializedName("to_percent")
    float toPercent;
    @SerializedName("created_at")
    String modifyTime;

    public float getFromPercent() {
        return fromPercent;
    }

    public void setFromPercent(float fromPercent) {
        this.fromPercent = fromPercent;
    }

    public float getToPercent() {
        return toPercent;
    }

    public void setToPercent(float toPercent) {
        this.toPercent = toPercent;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}