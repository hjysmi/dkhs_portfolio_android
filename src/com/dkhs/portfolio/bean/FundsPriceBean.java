/**
 * @Title FundsPriceBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-9 下午4:11:51
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName FundsPriceBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-9 下午4:11:51
 * @version 1.0
 */
public class FundsPriceBean {
    // "id": 102000135,
    // "code": "519013",
    // "abbr_name": "海富通风格优势股票",
    // "chi_spell": "HFTFGYSGP",
    // "net_value": 2,
    // "net_cumulative": 0.894,
    // "percent_day": 0,
    // "percent_month": 0,
    // "percent_season": 0

    private long id;
    private String code;
    @SerializedName("abbr_name")
    private String name;

    @SerializedName("percent_day")
    private float percentDay;
    @SerializedName("percent_month")
    private float percentMonth;
    @SerializedName("percent_season")
    private float percentSeason;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPercentDay() {
        return percentDay;
    }

    public void setPercentDay(float percentDay) {
        this.percentDay = percentDay;
    }

    public float getPercentMonth() {
        return percentMonth;
    }

    public void setPercentMonth(float percentMonth) {
        this.percentMonth = percentMonth;
    }

    public float getPercentSeason() {
        return percentSeason;
    }

    public void setPercentSeason(float percentSeason) {
        this.percentSeason = percentSeason;
    }

}
