package com.dkhs.portfolio.bean;

import com.mingle.autolist.AutoData;

/**
 * Created by zhangcm on 2015/9/21.14:06
 */
public class FundTradeBean extends AutoData {

    private String id;
    private String shares;
    private String amount;
    private String apply_date;
    private int status;
    private Fund fund;
    @Override
    public String getIdentifies() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApply_date() {
        return apply_date;
    }

    public void setApply_date(String apply_date) {
        this.apply_date = apply_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public class Fund{
        public String id;
        public String abbr_name;
    }
}
