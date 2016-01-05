package com.dkhs.portfolio.bean;

/**
 * Created by zjz on 2015/6/1.
 */
public class FundPriceBean extends StockPriceBean {

    /**
     * percent_month : 27.5057
     * net_cumulative : 2.389
     * percent_day : -2.8633
     * percent_six_month : 103.3606
     * percent_season : 69.1088
     * percent_week : -3.0316
     * tenthou_unit_incm : 0.0
     * year_yld : 0.0
     * tradedate : 2015-05-27
     * percent_tyear : 75.7457
     * net_value : 2.239
     * percent_year : 146.5859
     */
    private float percent_month;
    private float net_cumulative;
    private float percent_day;
    private float percent_six_month;
    private float percent_season;
    private float percent_week;
    private float tenthou_unit_incm;
    private float year_yld;
    private String tradedate = "";
    private float percent_tyear;
    private float net_value;
    private int symbol_stype;
    private float percent_year;
    private float latest_cp_rate;
    private String chi_spell = "";
    private String recommend_title;
    private String recommend_desc;
    private int investment_risk;
    private double discount_rate_buy;
    private double fare_ratio_buy;
    private float amount_min_buy;
    private boolean allow_trade;
    private int trade_status;

    public int getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(int trade_status) {
        this.trade_status = trade_status;
    }

    public boolean isAllow_trade() {
        return allow_trade;
    }

    public void setAllow_trade(boolean allow_trade) {
        this.allow_trade = allow_trade;
    }

    public float getAmount_min_buy() {
        return amount_min_buy;
    }

    public void setAmount_min_buy(float amount_min_buy) {
        this.amount_min_buy = amount_min_buy;
    }

    public double getDiscount_rate_buy() {
        return discount_rate_buy;
    }

    public void setDiscount_rate_buy(double discount_rate_buy) {
        this.discount_rate_buy = discount_rate_buy;
    }

    public double getFare_ratio_buy() {
        return fare_ratio_buy;
    }

    public void setFare_ratio_buy(double fare_ratio_buy) {
        this.fare_ratio_buy = fare_ratio_buy;
    }

    public int getInvestment_risk() {
        return investment_risk;
    }

    public void setInvestment_risk(int investment_risk) {
        this.investment_risk = investment_risk;
    }

    public int getSymbol_stype() {
        return symbol_stype;
    }

    public void setSymbol_stype(int symbol_stype) {
        this.symbol_stype = symbol_stype;
    }

    public void setPercent_month(float percent_month) {
        this.percent_month = percent_month;
    }

    public void setNet_cumulative(float net_cumulative) {
        this.net_cumulative = net_cumulative;
    }

    public void setPercent_day(float percent_day) {
        this.percent_day = percent_day;
    }

    public void setPercent_six_month(float percent_six_month) {
        this.percent_six_month = percent_six_month;
    }

    public void setPercent_season(float percent_season) {
        this.percent_season = percent_season;
    }

    public void setPercent_week(float percent_week) {
        this.percent_week = percent_week;
    }

    public void setTenthou_unit_incm(float tenthou_unit_incm) {
        this.tenthou_unit_incm = tenthou_unit_incm;
    }

    public void setYear_yld(float year_yld) {
        this.year_yld = year_yld;
    }

    public void setTradedate(String tradedate) {
        this.tradedate = tradedate;
    }

    public void setPercent_tyear(float percent_tyear) {
        this.percent_tyear = percent_tyear;
    }

    public void setNet_value(float net_value) {
        this.net_value = net_value;
    }

    public void setPercent_year(float percent_year) {
        this.percent_year = percent_year;
    }

    public float getPercent_month() {
        return percent_month;
    }

    public float getNet_cumulative() {
        return net_cumulative;
    }

    public float getPercent_day() {
        return percent_day;
    }

    public float getPercent_six_month() {
        return percent_six_month;
    }

    public float getPercent_season() {
        return percent_season;
    }

    public float getPercent_week() {
        return percent_week;
    }

    public float getTenthou_unit_incm() {
        return tenthou_unit_incm;
    }

    public float getYear_yld() {
        return year_yld;
    }

    public String getTradedate() {
        return tradedate;
    }

    public float getPercent_tyear() {
        return percent_tyear;
    }

    public float getNet_value() {
        return net_value;
    }

    public float getPercent_year() {
        return percent_year;
    }
//    private String tradedate;


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

    public float getValue(String key) {
        float value = 0;
        switch (key) {
            case "-percent_day":
                value = percent_day;
                break;
            case "-percent_month":
                value = percent_month;
                break;
            case "-percent_year":
                value = percent_year;
                break;
            case "-percent_tyear":
                value = percent_tyear;
                break;
            case "-percent_season":
                value = percent_season;
                break;
            case "-percent_six_month":
                value = percent_six_month;
                break;
            case "-year_yld":
                value = year_yld;
                break;
            case "-net_cumulative":
                value = net_cumulative;
                break;
            case "-latest_cp_rate":
                value = latest_cp_rate;
                break;
        }
        return value;
    }

}
