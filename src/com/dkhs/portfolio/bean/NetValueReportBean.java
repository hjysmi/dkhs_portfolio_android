/**
 * @Title NetValueReportBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-23 下午1:03:39
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName NetValueReportBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-23 下午1:03:39
 * @version 1.0
 */
public class NetValueReportBean {
    private int id;
    private String date;
    @SerializedName("net_value")
    private float netValue;
    private float percentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getNetValue() {
        return netValue;
    }

    public void setNetValue(float netValue) {
        this.netValue = netValue;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}
