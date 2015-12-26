package com.dkhs.portfolio.bean;

import java.io.Serializable;

/**
 * Created by zhangcm on 2015/10/12.16:04
 */
public class Fund implements Serializable{
    private String id;
    private String name;
    private String abbr_name;
    private double percent_day;
    private String tradedate;
    private String net_value;
    private double fare_ratio_buy;
    private double discount_rate_buy;
    private double fare_ratio_sell;
    private double discount_rate_sell;
    private String symbol;

//    "allow_trade": False, #是否代销
//    "allow_buy": true, #是否可以买入
//    "allow_sell": true, #是否可以卖出
//    "trade_status": -1, #-1-'未知', 0-正常开放,1-认购时期,2-停止赎回,3-停止申购,4-封闭期,5-停止交易,6-基金终止,7-权益登记,8-红利发放,9-发行失败,10-非本机构代销,11-发行成功,12-转认购期
//    "amount_min_buy": "10.00", #最低买入金额
//    "amount_max_buy": "10000.00", #最高买入金额
//    "shares_min_sell": "10.00", #最低卖出份额
//    "shares_max_sell": "1000.00", #最高卖出份额
//    "shares_min": 1.0, #最低持有份额
//    "fare_ratio_buy": "1.00", #买入交易费率
//    "discount_rate_buy": "0.80", #买入折扣比率
//    "fare_ratio_sell": "1.00", #卖出交易费率
//    "discount_rate_sell": "0.90", #卖出折扣比率
//    "investment_risk": 0 , 0-'未知',1-'低',2-'中低',3-'中',4-'中高',5-'高'
    private boolean allow_buy;
    private boolean allow_sell;

    private String amount_min_buy;
    private String amount_max_buy;
    private String shares_min_sell;
    private String shares_max_sell;

    public boolean isAllow_buy() {
        return allow_buy;
    }

    public void setAllow_buy(boolean allow_buy) {
        this.allow_buy = allow_buy;
    }

    public boolean isAllow_sell() {
        return allow_sell;
    }

    public void setAllow_sell(boolean allow_sell) {
        this.allow_sell = allow_sell;
    }

    public String getAmount_min_buy() {
        return amount_min_buy;
    }

    public void setAmount_min_buy(String amount_min_buy) {
        this.amount_min_buy = amount_min_buy;
    }

    public String getAmount_max_buy() {
        return amount_max_buy;
    }

    public void setAmount_max_buy(String amount_max_buy) {
        this.amount_max_buy = amount_max_buy;
    }

    public String getShares_min_sell() {
        return shares_min_sell;
    }

    public void setShares_min_sell(String shares_min_sell) {
        this.shares_min_sell = shares_min_sell;
    }

    public String getShares_max_sell() {
        return shares_max_sell;
    }

    public void setShares_max_sell(String shares_max_sell) {
        this.shares_max_sell = shares_max_sell;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNet_value() {
        return net_value;
    }

    public void setNet_value(String net_value) {
        this.net_value = net_value;
    }


    public String getTradedate() {
        return tradedate;
    }

    public void setTradedate(String tradedate) {
        this.tradedate = tradedate;
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

    public String getAbbr_name() {
        return abbr_name;
    }

    public void setAbbr_name(String abbr_name) {
        this.abbr_name = abbr_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercent_day() {
        return percent_day;
    }

    public void setPercent_day(double percent_day) {
        this.percent_day = percent_day;
    }
}
