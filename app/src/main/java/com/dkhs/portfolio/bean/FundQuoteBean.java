package com.dkhs.portfolio.bean;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by zjz on 2015/6/8.
 */
@Parcel
public class FundQuoteBean extends QuotesBean {

    /**
     * followed_at : 2015-06-01T06:50:47Z
     * managers : [{"id":302001636,"cp_rate":30.38,"name":"张少华","start_date":"2010-10-22"},{"id":302003987,"cp_rate":1.9789,"name":"袁英杰","start_date":"2015-01-30"}]
     * symbol_type : 3
     * percent_season : 1.4355
     * percent_six_month : 2.8388
     * percent_week : 0.1075
     * tenthou_unit_incm : 0.0
     * server_time : 2015-06-08T03:01:38Z
     * followed : true
     * amount : null
     * id : 102001701
     * open : null
     * name : 申万菱信深证成指分级证券投资基金
     * tradedate : 2015-06-05
     * is_alert : false
     * percent_year : 25.6553
     * mana_name : 申万菱信基金管理有限公司
     * chi_spell : SWSY
     * net_cumulative : 1.2778
     * symbol : SZ150022
     * percent_day : 0.0195
     * list_status : 1
     * alert_settings : null
     * code : 150022
     * net_value : 1.0246
     * estab_date : 2010-10-22T00:00:00Z
     * percent_month : 0.4805
     * symbol_stype : 308
     * year_yld : 0.0
     * volume : null
     * high : null
     * percent_tyear : 2.4415
     * low : null
     * abbr_name : 申万收益
     */
    List<ManagersEntity> managers;
    float percent_season;
    float percent_six_month;
    float percent_week;
    float tenthou_unit_incm;
    //净值日期
    String tradedate;
    float percent_year;
    float net_cumulative;
    float percent_day;
    float net_value;
    //成立日期
    String estab_date;
    float percent_month;
    float year_yld;
    float percent_tyear;

    String mana_name;

    public String getMana_name() {
        return mana_name;
    }

    public void setMana_name(String mana_name) {
        this.mana_name = mana_name;
    }

    public void setManagers(List<ManagersEntity> managers) {
        this.managers = managers;
    }


    public void setPercent_season(float percent_season) {
        this.percent_season = percent_season;
    }

    public void setPercent_six_month(float percent_six_month) {
        this.percent_six_month = percent_six_month;
    }

    public void setPercent_week(float percent_week) {
        this.percent_week = percent_week;
    }

    public void setTenthou_unit_incm(float tenthou_unit_incm) {
        this.tenthou_unit_incm = tenthou_unit_incm;
    }


    public void setTradedate(String tradedate) {
        this.tradedate = tradedate;
    }


    public void setPercent_year(float percent_year) {
        this.percent_year = percent_year;
    }


    public void setNet_cumulative(float net_cumulative) {
        this.net_cumulative = net_cumulative;
    }


    public void setPercent_day(float percent_day) {
        this.percent_day = percent_day;
    }


    public void setNet_value(float net_value) {
        this.net_value = net_value;
    }

    public void setEstab_date(String estab_date) {
        this.estab_date = estab_date;
    }

    public void setPercent_month(float percent_month) {
        this.percent_month = percent_month;
    }

    public void setYear_yld(float year_yld) {
        this.year_yld = year_yld;
    }


    public void setPercent_tyear(float percent_tyear) {
        this.percent_tyear = percent_tyear;
    }


    public List<ManagersEntity> getManagers() {
        return managers;
    }


    public float getPercent_season() {
        return percent_season;
    }

    public float getPercent_six_month() {
        return percent_six_month;
    }

    public float getPercent_week() {
        return percent_week;
    }

    public float getTenthou_unit_incm() {
        return tenthou_unit_incm;
    }


    public String getTradedate() {
        return tradedate;
    }


    public float getPercent_year() {
        return percent_year;
    }


    public float getNet_cumulative() {
        return net_cumulative;
    }


    public float getPercent_day() {
        return percent_day;
    }


    public float getNet_value() {
        return net_value;
    }

    public String getEstab_date() {
        return estab_date;
    }

    public float getPercent_month() {
        return percent_month;
    }


    public float getYear_yld() {
        return year_yld;
    }


    public float getPercent_tyear() {
        return percent_tyear;
    }


}
