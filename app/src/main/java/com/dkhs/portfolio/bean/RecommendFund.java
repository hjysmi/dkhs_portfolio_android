package com.dkhs.portfolio.bean;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class RecommendFund {
    private String abbr_name;
    private String percent_six_month;
    private String recommend_title;
    private String recommend_desc;

    public String getAbbr_name() {
        return abbr_name;
    }

    public void setAbbr_name(String abbr_name) {
        this.abbr_name = abbr_name;
    }

    public String getPercent_six_month() {
        return percent_six_month;
    }

    public void setPercent_six_month(String percent_six_month) {
        this.percent_six_month = percent_six_month;
    }

    public String getRecommend_title() {
        return recommend_title;
    }

    public void setRecommend_title(String recommend_title) {
        this.recommend_title = recommend_title;
    }

    public String getRecommend_desc() {
        return recommend_desc;
    }

    public void setRecommend_desc(String recommend_desc) {
        this.recommend_desc = recommend_desc;
    }
}
