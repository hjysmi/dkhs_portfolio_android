package com.dkhs.portfolio;

/**
 * Created by zhangcm on 2015/12/8.
 */
public class RecommendFundSpecialBean {
    //专题ID
    private String id;
    private String recommend_title;
    private String recommend_desc;
    private String recommend_image_sm;
    private String abbr_name;
    private String percent_six_month;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRecommend_image_sm() {
        return recommend_image_sm;
    }

    public void setRecommend_image_sm(String recommend_image_sm) {
        this.recommend_image_sm = recommend_image_sm;
    }

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
}
