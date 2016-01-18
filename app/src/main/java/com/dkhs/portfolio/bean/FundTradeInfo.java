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
//    #费率折扣
    private double fare_ratio;
    private FundQuoteBean fund;
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

    public double getFare_ratio() {
        return fare_ratio;
    }

    public void setFare_ratio(double fare_ratio) {
        this.fare_ratio = fare_ratio;
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
