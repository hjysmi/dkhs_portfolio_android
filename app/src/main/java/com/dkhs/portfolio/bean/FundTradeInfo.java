package com.dkhs.portfolio.bean;

/**
 * Created by zhangcm on 2015/10/14.10:08
 */
public class FundTradeInfo {
//    #交易记录编号
    private String id;
//    #份额
    private String shares;
//    #金额
    private String amount;
//    #委托时间
    private String apply_date;
//    #状态 0：委托成功 1:交易成功 2:交易失败
    private int status;
//    #申请编号
    private String allot_no;
//    #申购费率
    private double discount_rate;
    private FundQuoteBean fund;

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

    public FundQuoteBean getFund() {
        return fund;
    }

    public void setFund(FundQuoteBean fund) {
        this.fund = fund;
    }
}
