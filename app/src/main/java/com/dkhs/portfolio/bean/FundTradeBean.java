package com.dkhs.portfolio.bean;

import com.mingle.autolist.AutoData;

/**
 * Created by zhangcm on 2015/9/21.14:06
 */
public class FundTradeBean extends AutoData {

    private int id;
    private String trade_time;
    private String trade_status;
    private String fund_name;
    private double trade_value;

    @Override
    public String getIdentifies() {
        return String.valueOf(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public double getTrade_value() {
        return trade_value;
    }

    public void setTrade_value(double trade_value) {
        this.trade_value = trade_value;
    }
}
