package com.dkhs.portfolio.bean;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class RecommendFundManager {
    private int id;
    private String avatar_sm;
    private String name;
    private String recommend_title;
    private String index_rate_week;
    private String win_rate_week;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar_sm() {
        return avatar_sm;
    }

    public void setAvatar_sm(String avatar_sm) {
        this.avatar_sm = avatar_sm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecommend_title() {
        return recommend_title;
    }

    public void setRecommend_title(String recommend_title) {
        this.recommend_title = recommend_title;
    }

    public String getIndex_rate_week() {
        return index_rate_week;
    }

    public void setIndex_rate_week(String index_rate_week) {
        this.index_rate_week = index_rate_week;
    }

    public String getWin_rate_week() {
        return win_rate_week;
    }

    public void setWin_rate_week(String win_rate_week) {
        this.win_rate_week = win_rate_week;
    }
}
