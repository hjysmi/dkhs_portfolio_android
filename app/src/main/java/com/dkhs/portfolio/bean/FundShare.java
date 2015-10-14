package com.dkhs.portfolio.bean;

import java.io.Serializable;

/**
 * Created by zhangcm on 2015/10/12.19:56
 */
public class FundShare implements Serializable{
    private String id;
    private String shares_current;
    private String shares_enable;
    private Bank bank;

    public String getShares_current() {
        return shares_current;
    }

    public void setShares_current(String shares_current) {
        this.shares_current = shares_current;
    }

    public String getShares_enable() {
        return shares_enable;
    }

    public void setShares_enable(String shares_enable) {
        this.shares_enable = shares_enable;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
