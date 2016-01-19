package com.dkhs.portfolio.bean;

import java.io.Serializable;

/**
 * Created by zhangcm on 2015/10/12.19:57
 */
public class TradeRecord implements Serializable {
    private String id;
    private int direction;
//    #状态 0-委托成功 1-确认成功 2-确认失败 3-支付成功 4-支付失败
    private int status;
    private String shares;
    private String amount;
    private String time;
    private Bank bank;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
