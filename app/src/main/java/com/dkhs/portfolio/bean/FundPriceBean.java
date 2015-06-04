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
    private String tradedate;
    private float percent_tyear;
    private float net_value;
    private int symbol_stype;
    private float percent_year;
    private String chi_spell;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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


    public float getValue(String key) {
        float value=0 ;
        if (key.equals("percent_day")) {
            value = percent_day ;
        } else if (key.equals("percent_day")) {
            value = percent_day ;
        } else if (key.equals("percent_month")) {
            value = percent_month ;
        } else if (key.equals("percent_year")) {
            value = percent_year ;
        } else if (key.equals("percent_tyear")) {
            value = percent_tyear;
        } else if (key.equals("net_cumulative")) {
            value = net_cumulative;
        } else if (key.equals("year_yld")) {
            value = year_yld ;
        }
        return value;
    }

}
