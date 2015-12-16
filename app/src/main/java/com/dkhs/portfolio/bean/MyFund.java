package com.dkhs.portfolio.bean;

/**
 * Created by zhangcm on 2015/10/12.16:04
 */
public class MyFund {

    private Fund fund;
    private String worth_value;
    private String income_total;
    private String income_latest;

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
