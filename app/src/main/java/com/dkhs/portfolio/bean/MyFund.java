package com.dkhs.portfolio.bean;

/**
 * Created by zhangcm on 2015/10/12.16:04
 */
public class MyFund {

    private Fund fund;
    private String worth_value;
    private String income_total;
    private String income_latest;
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

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
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
