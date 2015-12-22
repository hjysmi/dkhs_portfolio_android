package com.dkhs.portfolio.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangcm on 2015/10/12.19:59
 */
public class MyFundInfo implements Serializable{
    private List<FundShare> shares_list;
    private List<TradeRecord> trade_record_list;
    private Fund fund;
    private String worth_value;
    private String income_total;
    private String income_latest;

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

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
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
