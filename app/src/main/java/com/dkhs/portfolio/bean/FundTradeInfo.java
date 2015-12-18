package com.dkhs.portfolio.bean;

/**
 * Created by zhangcm on 2015/10/14.10:08
 */
public class FundTradeInfo {
    private String id;
    private String shares;
    private String amount;
    private String apply_date;
    private int status;
    private String allot_no;
    private double discount_rate;
    private Fund fund;

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

    public String getAllot_no() {
        return allot_no;
    }

    public void setAllot_no(String allot_no) {
        this.allot_no = allot_no;
    }

    public double getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(double discount_rate) {
        this.discount_rate = discount_rate;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }
}
