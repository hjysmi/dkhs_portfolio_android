package com.dkhs.portfolio.bean;

/**
 * Created by zhangcm on 2015/10/12.16:04
 */
public class MyFund {

    private FundQuoteBean fund;
    private String worth_value;
    private String income_total;
    private String income_latest;
//    allow_trade是否代销
//    trade_status 基金状态
//    判断可以申购：allow_trade==True and (trade_status ==0 or trade_status==2)
//    判断可以赎回：allow_trade==True and (trade_status ==0 or trade_status==3)
    private boolean allow_trade;
    private int trade_status;
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

    public void setBuy_unconfirm(int buy_unconfirm) {
        this.buy_unconfirm = buy_unconfirm;
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

    public FundQuoteBean getFund() {
        return fund;
    }

    public void setFund(FundQuoteBean fund) {
        this.fund = fund;
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
}
