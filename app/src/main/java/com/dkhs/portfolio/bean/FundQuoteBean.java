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


    float last_net_value;
    float change;

    String mana_name;
    String end_shares;

    //  Double amount_min;
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

    public float getLast_net_value() {
        return last_net_value;
    }

    public void setLast_net_value(float last_net_value) {
        this.last_net_value = last_net_value;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public String getEnd_shares() {
        return end_shares;
    }

    public void setEnd_shares(String end_shares) {
        this.end_shares = end_shares;
    }

    int investment_risk;
    String shares_min;
    String amount_min_buy;
    String amount_max_buy;
    boolean allow_buy;
    boolean allow_sell;
    String shares_min_sell;
    String shares_max_sell;
    boolean allow_trade;

    int trade_status;
    double fare_ratio_buy;
    double discount_rate_buy;
    double fare_ratio_sell;
    double discount_rate_sell;

    public String getShares_max_sell() {
        return shares_max_sell;
    }

    public void setShares_max_sell(String shares_max_sell) {
        this.shares_max_sell = shares_max_sell;
    }

    public double getFare_ratio_sell() {
        return fare_ratio_sell;
    }

    public void setFare_ratio_sell(double fare_ratio_sell) {
        this.fare_ratio_sell = fare_ratio_sell;
    }

    public double getDiscount_rate_sell() {
        return discount_rate_sell;
    }

    public void setDiscount_rate_sell(double discount_rate_sell) {
        this.discount_rate_sell = discount_rate_sell;
    }

    public boolean isAllow_sell() {
        return allow_sell;
    }

    public void setAllow_sell(boolean allow_sell) {
        this.allow_sell = allow_sell;
    }

    public int getInvestment_risk() {
        return investment_risk;
    }

    public void setInvestment_risk(int investment_risk) {
        this.investment_risk = investment_risk;
    }

    public String getShares_min() {
        return shares_min;
    }

    public void setShares_min(String shares_min) {
        this.shares_min = shares_min;
    }

    public double getFare_ratio_buy() {
        return fare_ratio_buy;
    }

    public void setFare_ratio_buy(double fare_ratio_buy) {
        this.fare_ratio_buy = fare_ratio_buy;
    }

    public double getDiscount_rate_buy() {
        return discount_rate_buy;
    }

    public void setDiscount_rate_buy(double discount_rate_buy) {
        this.discount_rate_buy = discount_rate_buy;
    }

    public String getAmount_min_buy() {
        return amount_min_buy;
    }

    public void setAmount_min_buy(String amount_min_buy) {
        this.amount_min_buy = amount_min_buy;
    }

    public boolean isAllow_trade() {
        return allow_trade;
    }

    public void setAllow_trade(boolean allow_trade) {
        this.allow_trade = allow_trade;
    }

    public int getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(int trade_status) {
        this.trade_status = trade_status;
    }

    public String getAmount_max_buy() {
        return amount_max_buy;
    }

    public void setAmount_max_buy(String amount_max_buy) {
        this.amount_max_buy = amount_max_buy;
    }

    public boolean isAllow_buy() {
        return allow_buy;
    }

    public void setAllow_buy(boolean allow_buy) {
        this.allow_buy = allow_buy;
    }

    public String getShares_min_sell() {
        return shares_min_sell;
    }

    public void setShares_min_sell(String shares_min_sell) {
        this.shares_min_sell = shares_min_sell;
    }
}
