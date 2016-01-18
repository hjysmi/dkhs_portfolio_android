package com.dkhs.portfolio.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangcm on 2015/10/12.19:59
 */
public class MyFundInfo implements Serializable{
    private List<FundShare> shares_list;
    private List<TradeRecord> trade_record_list;
    private FundQuoteBean fund;
    private String worth_value;
    private String income_total;
    private String income_latest;
//    "buy_unconfirm": 1, #买入待确认笔数
//    "amount_unconfirm": 100.01, #买入待确认金额
//    "sell_unconfirm": 1, #卖出待确认笔数
//    "shares_unconfirm": 100.01 #卖出待确认份额

    private int buy_unconfirm;
    private float amount_unconfirm;
    private int sell_unconfirm;
    private float shares_unconfirm;

    public int getBuy_unconfirm() {
        return buy_unconfirm;
    }

    public void setBuy_unconfirm(int buy_unconfirm) {
        this.buy_unconfirm = buy_unconfirm;
    }

    public float getAmount_unconfirm() {
        return amount_unconfirm;
    }

    public void setAmount_unconfirm(float amount_unconfirm) {
        this.amount_unconfirm = amount_unconfirm;
    }

    public int getSell_unconfirm() {
        return sell_unconfirm;
    }

    public void setSell_unconfirm(int sell_unconfirm) {
        this.sell_unconfirm = sell_unconfirm;
    }

    public float getShares_unconfirm() {
        return shares_unconfirm;
    }

    public void setShares_unconfirm(float shares_unconfirm) {
        this.shares_unconfirm = shares_unconfirm;
    }

    public String getWorth_value() {
        return worth_value;
    }

    public void setWorth_value(String worth_value) {
        this.worth_value = worth_value;
    }

    public String getIncome_total() {
        return income_total;
    }

    public void setIncome_total(String income_total) {
        this.income_total = income_total;
    }

    public String getIncome_latest() {
        return income_latest;
    }

    public void setIncome_latest(String income_latest) {
        this.income_latest = income_latest;
    }

    public FundQuoteBean getFund() {
        return fund;
    }

    public void setFund(FundQuoteBean fund) {
        this.fund = fund;
    }

    public List<FundShare> getShares_list() {
        return shares_list;
    }

    public void setShares_list(List<FundShare> shares_list) {
        this.shares_list = shares_list;
    }

    public List<TradeRecord> getTrade_record_list() {
        return trade_record_list;
    }

    public void setTrade_record_list(List<TradeRecord> trade_record_list) {
        this.trade_record_list = trade_record_list;
    }
}
