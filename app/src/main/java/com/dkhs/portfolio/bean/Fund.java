package com.dkhs.portfolio.bean;

import java.io.Serializable;

/**
 * Created by zhangcm on 2015/10/12.16:04
 */
public class Fund implements Serializable{
    private String id;
    private String name;
    private String abbr_name;
    private double percent_latest;
    private String amount_min;
    private String shares_min;
    private String tradedate;
    private String net_value;
    private double fare_ratio_buy;
    private double discount_rate_buy;
    private double fare_ratio_sell;
    private double discount_rate_sell;
    private String symbol;
//    基金状态判断：
//    allow_trade是否代销
//    trade_status 基金状态
//    判断可以申购：allow_trade==True and (trade_status ==0 or trade_status==2)
//    判断可以赎回：allow_trade==True and (trade_status ==0 or trade_status==3)
    private boolean allow_trade;
    private int trade_status;

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

    public String getAmount_min() {
        return amount_min;
    }

    public void setAmount_min(String amount_min) {
        this.amount_min = amount_min;
    }

    public String getShares_min() {
        return shares_min;
    }

    public void setShares_min(String shares_min) {
        this.shares_min = shares_min;
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

    public double getPercent_latest() {
        return percent_latest;
    }

    public void setPercent_latest(double percent_latest) {
        this.percent_latest = percent_latest;
    }
}
