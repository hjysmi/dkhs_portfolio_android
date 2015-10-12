package com.dkhs.portfolio.bean;

/**
 * Created by wuyongsen on 2015/10/12.
 */
public class AccountInfoBean {
    private String available;
    private String frozen_trade;
    private String frozen_withdraw;

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getFrozen_trade() {
        return frozen_trade;
    }

    public void setFrozen_trade(String frozen_trade) {
        this.frozen_trade = frozen_trade;
    }

    public String getFrozen_withdraw() {
        return frozen_withdraw;
    }

    public void setFrozen_withdraw(String frozen_withdraw) {
        this.frozen_withdraw = frozen_withdraw;
    }
}
