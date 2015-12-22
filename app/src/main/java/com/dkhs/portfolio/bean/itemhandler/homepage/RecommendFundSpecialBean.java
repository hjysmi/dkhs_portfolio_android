package com.dkhs.portfolio.bean.itemhandler.homepage;

import java.util.List;

/**
 * Created by zhangcm on 2015/12/8.
 */
public class RecommendFundSpecialBean {
    //专题ID
    private String id;
    private String recommend_title;
    private String recommend_desc;
    private String recommend_image_sm;
    private List<Symbol> symbols;


    public class Symbol{
        public String abbr_name;
        public float percent_six_month;
    }
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

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
    }
}
