/**
 * @Title ChampionCollectionBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-29 下午12:48:26
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @ClassName ChampionCollectionBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-29 下午12:48:26
 * @version 1.0
 */
public class ChampionCollectionBean {
    @SerializedName("week")
    private ChampionBean week;
    @SerializedName("day")
    private ChampionBean day;
    @SerializedName("month")
    private ChampionBean month;
    @SerializedName("three_month")
    private ChampionBean season;

    public ChampionBean getWeek() {
        return week;
    }

    public void setWeek(ChampionBean week) {
        this.week = week;
    }

    public ChampionBean getMonth() {
        return month;
    }

    public void setMonth(ChampionBean month) {
        this.month = month;
    }

    public ChampionBean getSeason() {
        return season;
    }

    public void setSeason(ChampionBean season) {
        this.season = season;
    }

    public ChampionBean getDay() {
        return day;
    }

    public void setDay(ChampionBean day) {
        this.day = day;
    }
}
